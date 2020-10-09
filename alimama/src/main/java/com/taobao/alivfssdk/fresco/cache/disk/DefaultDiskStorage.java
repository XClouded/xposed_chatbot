package com.taobao.alivfssdk.fresco.cache.disk;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.taobao.alivfssdk.fresco.binaryresource.BinaryResource;
import com.taobao.alivfssdk.fresco.binaryresource.FileBinaryResource;
import com.taobao.alivfssdk.fresco.cache.common.CacheErrorLogger;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.PairCacheKey;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallback;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorage;
import com.taobao.alivfssdk.fresco.common.file.FileTree;
import com.taobao.alivfssdk.fresco.common.file.FileTreeVisitor;
import com.taobao.alivfssdk.fresco.common.file.FileUtils;
import com.taobao.alivfssdk.fresco.common.internal.CountingOutputStream;
import com.taobao.alivfssdk.fresco.common.internal.Preconditions;
import com.taobao.alivfssdk.fresco.common.internal.VisibleForTesting;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DefaultDiskStorage implements DiskStorage {
    private static final String CONTENT_FILE_EXTENSION = ".cnt";
    private static final String DEFAULT_DISK_STORAGE_VERSION_PREFIX = "v2";
    private static final int SHARDING_BUCKET_COUNT = 100;
    private static final String TAG = "DefaultDiskStorage";
    private static final String TEMP_FILE_EXTENSION = ".tmp";
    static final long TEMP_FILE_LIFETIME_MS = TimeUnit.MINUTES.toMillis(30);
    /* access modifiers changed from: private */
    public final CacheErrorLogger mCacheErrorLogger;
    private final boolean mIsExternal;
    /* access modifiers changed from: private */
    public final File mRootDirectory;
    /* access modifiers changed from: private */
    public final File mVersionDirectory;

    public void close() throws IOException {
    }

    public boolean isEnabled() {
        return true;
    }

    public void purgeUnexpectedResources() throws IOException {
    }

    public DefaultDiskStorage(File file, int i, CacheErrorLogger cacheErrorLogger) {
        Preconditions.checkNotNull(file);
        this.mRootDirectory = file;
        this.mIsExternal = isExternal(file, cacheErrorLogger);
        this.mVersionDirectory = new File(this.mRootDirectory, getVersionSubdirectoryName(i));
        this.mCacheErrorLogger = cacheErrorLogger;
        recreateDirectoryIfVersionChanges();
    }

    private static boolean isExternal(File file, CacheErrorLogger cacheErrorLogger) {
        String str;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            String file2 = externalStorageDirectory.toString();
            try {
                str = file.getCanonicalPath();
                try {
                    return str.contains(file2);
                } catch (IOException e) {
                    e = e;
                    CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.OTHER;
                    cacheErrorLogger.logError(cacheErrorCategory, TAG, "failed to read folder to check if external: " + str, e);
                    return false;
                }
            } catch (IOException e2) {
                e = e2;
                str = null;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory2 = CacheErrorLogger.CacheErrorCategory.OTHER;
                cacheErrorLogger.logError(cacheErrorCategory2, TAG, "failed to read folder to check if external: " + str, e);
                return false;
            }
        }
        return false;
    }

    @VisibleForTesting
    static String getVersionSubdirectoryName(int i) {
        return String.format((Locale) null, "%s.ols%d.%d", new Object[]{DEFAULT_DISK_STORAGE_VERSION_PREFIX, 100, Integer.valueOf(i)});
    }

    public boolean isExternal() {
        return this.mIsExternal;
    }

    public String getStorageName() {
        String absolutePath = this.mRootDirectory.getAbsolutePath();
        return "_" + absolutePath.substring(absolutePath.lastIndexOf(47) + 1, absolutePath.length()) + "_" + absolutePath.hashCode();
    }

    private void recreateDirectoryIfVersionChanges() {
        boolean z = true;
        if (this.mRootDirectory.exists() && this.mVersionDirectory.exists()) {
            z = false;
        }
        if (z) {
            try {
                FileUtils.mkdirs(this.mVersionDirectory);
            } catch (FileUtils.CreateDirectoryException unused) {
                if (this.mCacheErrorLogger != null) {
                    CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                    CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR;
                    cacheErrorLogger.logError(cacheErrorCategory, TAG, "version directory could not be created: " + this.mVersionDirectory, (Throwable) null);
                }
            }
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

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public File getContentFileFor(String str, CacheKey cacheKey) {
        return new File(getFilename(str, cacheKey));
    }

    private String getSubdirectoryPath(String str) {
        String valueOf = String.valueOf(Math.abs(str.hashCode() % 100));
        return this.mVersionDirectory + File.separator + valueOf;
    }

    private File getSubdirectory(String str) {
        return new File(getSubdirectoryPath(str));
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
            FileInfo access$000 = DefaultDiskStorage.this.getShardFileInfo(file);
            if (access$000 != null && access$000.type == FileType.CONTENT) {
                this.result.add(new EntryImpl(access$000.resourceId, file));
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
            FileInfo access$000 = DefaultDiskStorage.this.getShardFileInfo(file);
            boolean z = false;
            if (access$000 == null) {
                return false;
            }
            if (access$000.type == FileType.TEMP) {
                return isRecentFile(file);
            }
            if (access$000.type == FileType.CONTENT) {
                z = true;
            }
            Preconditions.checkState(z);
            return true;
        }

        private boolean isRecentFile(File file) {
            return file.lastModified() > System.currentTimeMillis() - DefaultDiskStorage.TEMP_FILE_LIFETIME_MS;
        }
    }

    private void mkdirs(File file, String str) throws IOException {
        try {
            FileUtils.mkdirs(file);
        } catch (FileUtils.CreateDirectoryException e) {
            this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR, TAG, str, e);
            throw e;
        }
    }

    public DiskStorage.Inserter insert(String str, CacheKey cacheKey, Object obj) throws IOException {
        FileInfo fileInfo = new FileInfo(FileType.TEMP, str, cacheKey);
        File subdirectory = getSubdirectory(fileInfo.resourceId);
        if (!subdirectory.exists()) {
            mkdirs(subdirectory, "insert");
        }
        try {
            return new InserterImpl(str, fileInfo.createTempFile(subdirectory));
        } catch (IOException e) {
            this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_TEMPFILE, TAG, "insert", e);
            throw e;
        }
    }

    public BinaryResource getResource(String str, CacheKey cacheKey, Object obj) {
        File contentFileFor = getContentFileFor(str, cacheKey);
        if (!contentFileFor.exists()) {
            return null;
        }
        contentFileFor.setLastModified(System.currentTimeMillis());
        return FileBinaryResource.createOrNull(contentFileFor);
    }

    private String getFilename(String str, CacheKey cacheKey) {
        FileInfo fileInfo = new FileInfo(FileType.CONTENT, str, cacheKey);
        return fileInfo.toPath(getSubdirectoryPath(fileInfo.resourceId));
    }

    public boolean contains(String str, CacheKey cacheKey, Object obj) {
        return query(str, cacheKey, false);
    }

    public boolean touch(String str, CacheKey cacheKey, Object obj) {
        return query(str, cacheKey, true);
    }

    private boolean query(String str, CacheKey cacheKey, boolean z) {
        File contentFileFor = getContentFileFor(str, cacheKey);
        boolean exists = contentFileFor.exists();
        if (z && exists) {
            contentFileFor.setLastModified(System.currentTimeMillis());
        }
        return exists;
    }

    public List<String> getCatalogs(String str) {
        File[] listFiles = getSubdirectory(str).listFiles();
        ArrayList arrayList = new ArrayList();
        if (listFiles != null) {
            for (File fromFile : listFiles) {
                FileInfo fromFile2 = FileInfo.fromFile(fromFile);
                if (fromFile2 != null && fromFile2.type == FileType.CONTENT && str.equals(fromFile2.resourceId) && !TextUtils.isEmpty(fromFile2.catalog)) {
                    try {
                        arrayList.add(new String(Base64.decode(fromFile2.catalog, 11), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return arrayList;
    }

    public long remove(DiskStorage.Entry entry) {
        return doRemove(((EntryImpl) entry).getResource().getFile());
    }

    public long remove(String str, CacheKey cacheKey) {
        return doRemove(getContentFileFor(str, cacheKey));
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

    @VisibleForTesting
    static class EntryImpl implements DiskStorage.Entry {
        private final String id;
        private final FileBinaryResource resource;
        private long size;
        private long timestamp;

        private EntryImpl(String str, File file) {
            Preconditions.checkNotNull(file);
            this.id = (String) Preconditions.checkNotNull(str);
            this.resource = FileBinaryResource.createOrNull(file);
            this.size = -1;
            this.timestamp = -1;
        }

        public String getId() {
            return this.id;
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

    private static class FileInfo {
        public final String catalog;
        public final String resourceId;
        public final FileType type;

        private FileInfo(FileType fileType, String str, CacheKey cacheKey) {
            this.type = fileType;
            this.resourceId = str;
            if (!(cacheKey instanceof PairCacheKey) || TextUtils.isEmpty(((PairCacheKey) cacheKey).mKey2)) {
                this.catalog = null;
                return;
            }
            try {
                this.catalog = Base64.encodeToString(((PairCacheKey) cacheKey).mKey2.getBytes("UTF-8"), 11);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

        private FileInfo(FileType fileType, String str, String str2) {
            this.type = fileType;
            this.resourceId = str;
            this.catalog = str2;
        }

        public String toString() {
            return this.type + Operators.BRACKET_START_STR + this.resourceId + Operators.BRACKET_END_STR;
        }

        public String toPath(String str) {
            String str2 = str + File.separator + this.resourceId;
            if (!TextUtils.isEmpty(this.catalog)) {
                str2 = str2 + Operators.AND_NOT + this.catalog;
            }
            return str2 + this.type.extension;
        }

        public File createTempFile(File file) throws IOException {
            String str = this.resourceId;
            if (!TextUtils.isEmpty(this.catalog)) {
                str = str + Operators.AND_NOT + this.catalog + ".";
            }
            return File.createTempFile(str, ".tmp", file);
        }

        @Nullable
        public static FileInfo fromFile(File file) {
            FileType fromExtension;
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(46);
            String str = null;
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
            int lastIndexOf3 = substring.lastIndexOf(33);
            if (lastIndexOf3 > 0) {
                str = substring.substring(lastIndexOf3 + 1);
                substring = substring.substring(0, lastIndexOf3);
            }
            return new FileInfo(fromExtension, substring, str);
        }
    }

    @VisibleForTesting
    class InserterImpl implements DiskStorage.Inserter {
        private final String mResourceId;
        @VisibleForTesting
        final File mTemporaryFile;

        public InserterImpl(String str, File file) {
            this.mResourceId = str;
            this.mTemporaryFile = file;
        }

        public void writeData(WriterCallback writerCallback, CacheKey cacheKey, Object obj) throws IOException {
            try {
                OutputStream fileOutputStream = new FileOutputStream(this.mTemporaryFile);
                try {
                    CountingOutputStream countingOutputStream = new CountingOutputStream(fileOutputStream);
                    OutputStream write = writerCallback.write(countingOutputStream);
                    try {
                        write.flush();
                        long count = countingOutputStream.getCount();
                        write.close();
                        if (this.mTemporaryFile.length() != count) {
                            throw new IncompleteFileException(count, this.mTemporaryFile.length());
                        }
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        fileOutputStream = write;
                        th = th2;
                        fileOutputStream.close();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream.close();
                    throw th;
                }
            } catch (FileNotFoundException e) {
                DefaultDiskStorage.this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.WRITE_UPDATE_FILE_NOT_FOUND, DefaultDiskStorage.TAG, "updateResource", e);
                throw e;
            }
        }

        public BinaryResource commit(CacheKey cacheKey, Object obj) throws IOException {
            CacheErrorLogger.CacheErrorCategory cacheErrorCategory;
            File contentFileFor = DefaultDiskStorage.this.getContentFileFor(this.mResourceId, cacheKey);
            try {
                FileUtils.rename(this.mTemporaryFile, contentFileFor);
                if (contentFileFor.exists()) {
                    contentFileFor.setLastModified(System.currentTimeMillis());
                }
                return FileBinaryResource.createOrNull(contentFileFor);
            } catch (FileUtils.RenameException e) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_OTHER;
                } else if (cause instanceof FileUtils.ParentDirNotFoundException) {
                    cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND;
                } else if (cause instanceof FileNotFoundException) {
                    cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND;
                } else {
                    cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_RENAME_FILE_OTHER;
                }
                DefaultDiskStorage.this.mCacheErrorLogger.logError(cacheErrorCategory, DefaultDiskStorage.TAG, "commit", e);
                throw e;
            }
        }

        public boolean cleanUp() {
            return !this.mTemporaryFile.exists() || this.mTemporaryFile.delete();
        }
    }
}
