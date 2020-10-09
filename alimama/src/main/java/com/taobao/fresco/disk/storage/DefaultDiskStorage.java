package com.taobao.fresco.disk.storage;

import com.taobao.fresco.disk.common.BinaryResource;
import com.taobao.fresco.disk.common.Clock;
import com.taobao.fresco.disk.common.CountingOutputStream;
import com.taobao.fresco.disk.common.SystemClock;
import com.taobao.fresco.disk.common.WriterCallback;
import com.taobao.fresco.disk.fs.FileBinaryResource;
import com.taobao.fresco.disk.fs.FileTree;
import com.taobao.fresco.disk.fs.FileTreeVisitor;
import com.taobao.fresco.disk.fs.FileUtils;
import com.taobao.fresco.disk.storage.DiskStorage;
import com.taobao.phenix.compat.SimpleDiskCache;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DefaultDiskStorage implements DiskStorage {
    private static final String CONTENT_FILE_EXTENSION = ".cnt";
    private static final String DEFAULT_DISK_STORAGE_VERSION_PREFIX = "v1";
    private static final int SHARDING_BUCKET_COUNT = 100;
    private static final String TEMP_FILE_EXTENSION = ".tmp";
    static final long TEMP_FILE_LIFETIME_MS = TimeUnit.MINUTES.toMillis(30);
    private static DefaultDiskStorage sInstance;
    private boolean isEnabled;
    /* access modifiers changed from: private */
    public Clock mClock;
    /* access modifiers changed from: private */
    public File mRootDirectory;
    /* access modifiers changed from: private */
    public File mVersionDirectory;

    public static synchronized DefaultDiskStorage instance(File file, int i) {
        DefaultDiskStorage defaultDiskStorage;
        synchronized (DefaultDiskStorage.class) {
            if (sInstance == null) {
                sInstance = new DefaultDiskStorage(file, i);
            }
            defaultDiskStorage = sInstance;
        }
        return defaultDiskStorage;
    }

    public DefaultDiskStorage(File file, int i) {
        if (file == null) {
            FLog.e(SimpleDiskCache.TAG, "DefaultDiskStorage init with root dir: null, version: %d", Integer.valueOf(i));
            return;
        }
        FLog.d(SimpleDiskCache.TAG, "DefaultDiskStorage init with root dir: %s, version: %d", file, Integer.valueOf(i));
        this.mRootDirectory = file;
        this.mVersionDirectory = new File(this.mRootDirectory, getVersionSubdirectoryName(i));
        recreateDirectoryIfVersionChanges();
        this.mClock = SystemClock.get();
        this.isEnabled = this.mVersionDirectory.exists();
    }

    static String getVersionSubdirectoryName(int i) {
        return String.format((Locale) null, "%s.ols%d.%d", new Object[]{DEFAULT_DISK_STORAGE_VERSION_PREFIX, 100, Integer.valueOf(i)});
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    private void recreateDirectoryIfVersionChanges() {
        boolean z = true;
        if (this.mRootDirectory.exists()) {
            if (!this.mVersionDirectory.exists()) {
                FileTree.deleteRecursively(this.mRootDirectory);
            } else {
                z = false;
            }
        }
        if (z) {
            try {
                FileUtils.mkdirs(this.mVersionDirectory);
            } catch (FileUtils.CreateDirectoryException e) {
                FLog.e(SimpleDiskCache.TAG, "CacheError: WRITE_CREATE_DIR, version directory could not be created " + this.mVersionDirectory + ":" + e.getMessage(), new Object[0]);
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public void updateResource(String str, BinaryResource binaryResource, WriterCallback writerCallback, Object obj) throws IOException {
        File file = ((FileBinaryResource) binaryResource).getFile();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                CountingOutputStream countingOutputStream = new CountingOutputStream(fileOutputStream);
                writerCallback.write(countingOutputStream);
                countingOutputStream.flush();
                long count = countingOutputStream.getCount();
                fileOutputStream.close();
                if (file.length() != count) {
                    throw new IncompleteFileException(count, file.length());
                }
            } catch (Throwable th) {
                fileOutputStream.close();
                throw th;
            }
        } catch (FileNotFoundException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: WRITE_UPDATE_FILE_NOT_FOUND, updateResource:" + e.getMessage(), new Object[0]);
            throw e;
        }
    }

    /* access modifiers changed from: package-private */
    public File getContentFileFor(String str) {
        FileInfo fileInfo = new FileInfo(FileType.CONTENT, str);
        return fileInfo.toFile(getSubdirectory(fileInfo.resourceId));
    }

    private File getSubdirectory(String str) {
        return new File(this.mVersionDirectory, String.valueOf(Math.abs(str.hashCode() % 100)));
    }

    public void purgeUnexpectedResources() {
        FileTree.walkFileTree(this.mRootDirectory, new PurgingVisitor());
    }

    private void mkdirs(File file, String str) throws IOException {
        try {
            FileUtils.mkdirs(file);
        } catch (FileUtils.CreateDirectoryException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: WRITE_CREATE_DIR, " + str + ":" + e.getMessage(), new Object[0]);
            throw e;
        }
    }

    public FileBinaryResource createTemporary(String str, Object obj) throws IOException {
        FileInfo fileInfo = new FileInfo(FileType.TEMP, str);
        File subdirectory = getSubdirectory(fileInfo.resourceId);
        if (!subdirectory.exists()) {
            mkdirs(subdirectory, "createTemporary");
        }
        try {
            return FileBinaryResource.createOrNull(fileInfo.createTempFile(subdirectory));
        } catch (IOException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: WRITE_CREATE_TEMPFILE, createTemporary:" + e.getMessage(), new Object[0]);
            throw e;
        }
    }

    public FileBinaryResource commit(String str, BinaryResource binaryResource, Object obj) throws IOException {
        String str2;
        File file = ((FileBinaryResource) binaryResource).getFile();
        File contentFileFor = getContentFileFor(str);
        try {
            FileUtils.rename(file, contentFileFor);
            if (contentFileFor.exists()) {
                contentFileFor.setLastModified(this.mClock.now());
            }
            return FileBinaryResource.createOrNull(contentFileFor);
        } catch (FileUtils.RenameException e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                if (cause instanceof FileUtils.ParentDirNotFoundException) {
                    str2 = "WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND";
                } else if (cause instanceof FileNotFoundException) {
                    str2 = "WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND";
                }
                FLog.e(SimpleDiskCache.TAG, "CacheError: " + str2 + ", commit:" + e.getMessage(), new Object[0]);
                throw e;
            }
            str2 = "WRITE_RENAME_FILE_OTHER";
            FLog.e(SimpleDiskCache.TAG, "CacheError: " + str2 + ", commit:" + e.getMessage(), new Object[0]);
            throw e;
        }
    }

    public FileBinaryResource getResource(String str, Object obj) {
        File contentFileFor = getContentFileFor(str);
        if (!contentFileFor.exists()) {
            return null;
        }
        contentFileFor.setLastModified(this.mClock.now());
        return FileBinaryResource.createOrNull(contentFileFor);
    }

    public boolean contains(String str, Object obj) {
        return query(str, false);
    }

    public boolean touch(String str, Object obj) {
        return query(str, true);
    }

    private boolean query(String str, boolean z) {
        File contentFileFor = getContentFileFor(str);
        boolean exists = contentFileFor.exists();
        if (z && exists) {
            contentFileFor.setLastModified(this.mClock.now());
        }
        return exists;
    }

    public long remove(DiskStorage.Entry entry) {
        return doRemove(((EntryImpl) entry).getResource().getFile());
    }

    public long remove(String str) {
        return doRemove(getContentFileFor(str));
    }

    private long doRemove(File file) {
        if (!file.exists()) {
            return 0;
        }
        long length = file.length();
        if (file.delete()) {
            return length;
        }
        return -1;
    }

    public void clearAll() {
        FileTree.deleteContents(this.mRootDirectory);
    }

    public DiskStorage.DiskDumpInfo getDumpInfo() throws IOException {
        List<DiskStorage.Entry> entries = getEntries();
        DiskStorage.DiskDumpInfo diskDumpInfo = new DiskStorage.DiskDumpInfo();
        for (DiskStorage.Entry dumpCacheEntry : entries) {
            DiskStorage.DiskDumpInfoEntry dumpCacheEntry2 = dumpCacheEntry(dumpCacheEntry);
            String str = dumpCacheEntry2.type;
            if (!diskDumpInfo.typeCounts.containsKey(str)) {
                diskDumpInfo.typeCounts.put(str, 0);
            }
            diskDumpInfo.typeCounts.put(str, Integer.valueOf(diskDumpInfo.typeCounts.get(str).intValue() + 1));
            diskDumpInfo.entries.add(dumpCacheEntry2);
        }
        return diskDumpInfo;
    }

    private DiskStorage.DiskDumpInfoEntry dumpCacheEntry(DiskStorage.Entry entry) throws IOException {
        EntryImpl entryImpl = (EntryImpl) entry;
        String str = "";
        byte[] read = entryImpl.getResource().read();
        String typeOfBytes = typeOfBytes(read);
        if (typeOfBytes.equals(Constants.Name.UNDEFINED) && read.length >= 4) {
            str = String.format((Locale) null, "0x%02X 0x%02X 0x%02X 0x%02X", new Object[]{Byte.valueOf(read[0]), Byte.valueOf(read[1]), Byte.valueOf(read[2]), Byte.valueOf(read[3])});
        }
        return new DiskStorage.DiskDumpInfoEntry(entryImpl.getResource().getFile().getPath(), typeOfBytes, (float) entryImpl.getSize(), str);
    }

    private String typeOfBytes(byte[] bArr) {
        if (bArr.length < 2) {
            return Constants.Name.UNDEFINED;
        }
        if (bArr[0] == -1 && bArr[1] == -40) {
            return "jpg";
        }
        if (bArr[0] == -119 && bArr[1] == 80) {
            return "png";
        }
        if (bArr[0] == 82 && bArr[1] == 73) {
            return "webp";
        }
        return (bArr[0] == 71 && bArr[1] == 73) ? "gif" : Constants.Name.UNDEFINED;
    }

    public List<DiskStorage.Entry> getEntries() throws IOException {
        EntriesCollector entriesCollector = new EntriesCollector();
        FileTree.walkFileTree(this.mVersionDirectory, entriesCollector);
        return entriesCollector.getEntries();
    }

    /* access modifiers changed from: private */
    public FileInfo getShardFileInfo(File file) {
        FileInfo fromFile = FileInfo.fromFile(file);
        if (fromFile == null) {
            return null;
        }
        if (getSubdirectory(fromFile.resourceId).equals(file.getParentFile())) {
            return fromFile;
        }
        return null;
    }

    private enum FileType {
        CONTENT(".cnt"),
        TEMP(".tmp");
        
        public final String extension;

        private FileType(String str) {
            this.extension = str;
        }

        public static FileType fromExtension(String str) {
            if (".cnt".equals(str)) {
                return CONTENT;
            }
            if (".tmp".equals(str)) {
                return TEMP;
            }
            return null;
        }
    }

    private static class IncompleteFileException extends IOException {
        public final long actual;
        public final long expected;

        public IncompleteFileException(long j, long j2) {
            super("File was not written completely. Expected: " + j + ", found: " + j2);
            this.expected = j;
            this.actual = j2;
        }
    }

    private static class FileInfo {
        public final String resourceId;
        public final FileType type;

        private FileInfo(FileType fileType, String str) {
            this.type = fileType;
            this.resourceId = str;
        }

        public static FileInfo fromFile(File file) {
            FileType fromExtension;
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(46);
            if (lastIndexOf <= 0 || (fromExtension = FileType.fromExtension(name.substring(lastIndexOf))) == null) {
                return null;
            }
            String substring = name.substring(0, lastIndexOf);
            if (fromExtension.equals(FileType.TEMP)) {
                int lastIndexOf2 = substring.lastIndexOf(46);
                if (lastIndexOf2 <= 0) {
                    return null;
                }
                substring = substring.substring(0, lastIndexOf2);
            }
            return new FileInfo(fromExtension, substring);
        }

        public String toString() {
            return this.type + Operators.BRACKET_START_STR + this.resourceId + Operators.BRACKET_END_STR;
        }

        public File toFile(File file) {
            return new File(file, this.resourceId + this.type.extension);
        }

        public File createTempFile(File file) throws IOException {
            return File.createTempFile(this.resourceId + ".", ".tmp", file);
        }
    }

    private class EntriesCollector implements FileTreeVisitor {
        private final List<DiskStorage.Entry> result;

        public void postVisitDirectory(File file) {
        }

        public void preVisitDirectory(File file) {
        }

        private EntriesCollector() {
            this.result = new ArrayList();
        }

        public void visitFile(File file) {
            FileInfo access$300 = DefaultDiskStorage.this.getShardFileInfo(file);
            if (access$300 != null && access$300.type == FileType.CONTENT) {
                this.result.add(new EntryImpl(file));
            }
        }

        public List<DiskStorage.Entry> getEntries() {
            return Collections.unmodifiableList(this.result);
        }
    }

    private class PurgingVisitor implements FileTreeVisitor {
        private boolean insideBaseDirectory;

        private PurgingVisitor() {
        }

        public void preVisitDirectory(File file) {
            if (!this.insideBaseDirectory && file.equals(DefaultDiskStorage.this.mVersionDirectory)) {
                this.insideBaseDirectory = true;
            }
        }

        public void visitFile(File file) {
            if (!this.insideBaseDirectory || !isExpectedFile(file)) {
                file.delete();
            }
        }

        public void postVisitDirectory(File file) {
            if (!DefaultDiskStorage.this.mRootDirectory.equals(file) && !this.insideBaseDirectory) {
                file.delete();
            }
            if (this.insideBaseDirectory && file.equals(DefaultDiskStorage.this.mVersionDirectory)) {
                this.insideBaseDirectory = false;
            }
        }

        private boolean isExpectedFile(File file) {
            FileInfo access$300 = DefaultDiskStorage.this.getShardFileInfo(file);
            boolean z = false;
            if (access$300 == null) {
                return false;
            }
            if (access$300.type == FileType.TEMP) {
                return isRecentFile(file);
            }
            if (access$300.type == FileType.CONTENT) {
                z = true;
            }
            Preconditions.checkState(z);
            return true;
        }

        private boolean isRecentFile(File file) {
            return file.lastModified() > DefaultDiskStorage.this.mClock.now() - DefaultDiskStorage.TEMP_FILE_LIFETIME_MS;
        }
    }

    class EntryImpl implements DiskStorage.Entry {
        private final FileBinaryResource resource;
        private long size;
        private long timestamp;

        private EntryImpl(File file) {
            Preconditions.checkNotNull(file);
            this.resource = FileBinaryResource.createOrNull(file);
            this.size = -1;
            this.timestamp = -1;
        }

        public long getTimestamp() {
            if (this.timestamp < 0) {
                this.timestamp = this.resource.getFile().lastModified();
            }
            return this.timestamp;
        }

        public FileBinaryResource getResource() {
            return this.resource;
        }

        public long getSize() {
            if (this.size < 0) {
                this.size = this.resource.size();
            }
            return this.size;
        }
    }
}
