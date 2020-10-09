package com.taobao.alivfssdk.cache;

import android.os.Environment;
import com.taobao.alivfsadapter.AVFSAdapterManager;
import com.taobao.alivfsadapter.AVFSDBFactory;
import com.taobao.alivfsadapter.AVFSDataBase;
import com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem;
import com.taobao.alivfssdk.fresco.binaryresource.BinaryResource;
import com.taobao.alivfssdk.fresco.binaryresource.ByteArrayBinaryResource;
import com.taobao.alivfssdk.fresco.cache.common.CacheErrorLogger;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallback;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorage;
import com.taobao.alivfssdk.fresco.common.file.FileTree;
import com.taobao.alivfssdk.fresco.common.file.FileUtils;
import com.taobao.alivfssdk.fresco.common.internal.Preconditions;
import com.taobao.alivfssdk.fresco.common.internal.VisibleForTesting;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import com.taobao.weex.common.Constants;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SQLiteDefaultDiskStorage implements DiskStorage {
    private static final String AVFS_ENCRYPT_SQLITE_NAME = "alivfs_encrypt.sqlite";
    private static final String AVFS_SQLITE_NAME = "alivfs.sqlite";
    private static final String DEFAULT_DISK_STORAGE_VERSION_PREFIX = "v2";
    private static final String TAG = "SQLiteDefaultDiskStorage";
    private final CacheErrorLogger mCacheErrorLogger;
    private AVFSDataBase mDatabase;
    private final boolean mIsEncrypted;
    private final boolean mIsExternal;
    private final File mRootDirectory;
    private final File mVersionDirectory;

    public boolean isEnabled() {
        return true;
    }

    public void purgeUnexpectedResources() throws IOException {
    }

    public SQLiteDefaultDiskStorage(File file, int i, boolean z, CacheErrorLogger cacheErrorLogger) {
        Preconditions.checkNotNull(file);
        this.mRootDirectory = file;
        this.mIsEncrypted = z;
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
        return String.format((Locale) null, "%s.sqlite.%d", new Object[]{DEFAULT_DISK_STORAGE_VERSION_PREFIX, Integer.valueOf(i)});
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
            } catch (FileUtils.CreateDirectoryException unused) {
                CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.WRITE_CREATE_DIR;
                cacheErrorLogger.logError(cacheErrorCategory, TAG, "version directory could not be created: " + this.mVersionDirectory, (Throwable) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public AVFSSQLiteCacheItem getContentCacheItemFor(String str, CacheKey cacheKey) {
        try {
            return AVFSSQLiteCacheItem.get(getValuesDataBase(), str, cacheKey);
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e.getMessage(), e);
            return null;
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
        return new InserterImpl(str, cacheKey);
    }

    public BinaryResource getResource(String str, CacheKey cacheKey, Object obj) {
        try {
            AVFSSQLiteCacheItem aVFSSQLiteCacheItem = AVFSSQLiteCacheItem.get(getValuesDataBase(), str, cacheKey);
            if (aVFSSQLiteCacheItem == null || aVFSSQLiteCacheItem.value == null) {
                return null;
            }
            return new ByteArrayBinaryResource(aVFSSQLiteCacheItem.value);
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public boolean contains(String str, CacheKey cacheKey, Object obj) {
        return query(str, cacheKey, false);
    }

    public List<String> getCatalogs(String str) {
        try {
            return AVFSSQLiteCacheItem.extendsKeysForKey(getValuesDataBase(), str);
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public boolean touch(String str, CacheKey cacheKey, Object obj) {
        return query(str, cacheKey, true);
    }

    private boolean query(String str, CacheKey cacheKey, boolean z) {
        AVFSSQLiteCacheItem contentCacheItemFor = getContentCacheItemFor(str, cacheKey);
        boolean z2 = contentCacheItemFor != null;
        if (z && z2) {
            try {
                contentCacheItemFor.updateReadTime(getValuesDataBase(), System.currentTimeMillis());
            } catch (IOException e) {
                AVFSCacheLog.e(TAG, e.getMessage(), e);
            }
        }
        return z2;
    }

    public long remove(DiskStorage.Entry entry) {
        return doRemove(((EntryImpl) entry).getCacheItem());
    }

    public long remove(String str, CacheKey cacheKey) {
        return doRemove(new AVFSSQLiteCacheItem(str, cacheKey));
    }

    private long doRemove(AVFSSQLiteCacheItem aVFSSQLiteCacheItem) {
        try {
            if (aVFSSQLiteCacheItem.delete(getValuesDataBase())) {
                return aVFSSQLiteCacheItem.size;
            }
            return -1;
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e.getMessage(), e);
            return -1;
        }
    }

    public void clearAll() throws IOException {
        AVFSSQLiteCacheItem.deleteAll(getValuesDataBase());
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
        return new DiskStorage.DiskDumpInfoEntry(entryImpl.getCacheItem().toString(), typeOfBytes, (float) entryImpl.getSize(), str);
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
        AVFSSQLiteCacheItem[] items = AVFSSQLiteCacheItem.getItems(getValuesDataBase());
        AVFSSQLiteCacheItem[] items2 = AVFSSQLiteCacheItem.getItems(getValuesDataBase());
        ArrayList arrayList = new ArrayList();
        for (AVFSSQLiteCacheItem aVFSSQLiteCacheItem : items) {
            arrayList.add(new EntryImpl(aVFSSQLiteCacheItem.key, aVFSSQLiteCacheItem));
        }
        for (AVFSSQLiteCacheItem aVFSSQLiteCacheItem2 : items2) {
            arrayList.add(new EntryImpl(aVFSSQLiteCacheItem2.key, aVFSSQLiteCacheItem2));
        }
        return Collections.unmodifiableList(arrayList);
    }

    @VisibleForTesting
    static class EntryImpl implements DiskStorage.Entry {
        private final AVFSSQLiteCacheItem cacheItem;
        private final String id;
        private long size;
        private long timestamp;

        private EntryImpl(String str, AVFSSQLiteCacheItem aVFSSQLiteCacheItem) {
            Preconditions.checkNotNull(aVFSSQLiteCacheItem);
            this.id = (String) Preconditions.checkNotNull(str);
            this.cacheItem = aVFSSQLiteCacheItem;
            this.size = -1;
            this.timestamp = -1;
        }

        public String getId() {
            return this.id;
        }

        public AVFSSQLiteCacheItem getCacheItem() {
            return this.cacheItem;
        }

        public long getTimestamp() {
            if (this.timestamp < 0) {
                this.timestamp = this.cacheItem.time;
            }
            return this.timestamp;
        }

        public BinaryResource getResource() {
            return new ByteArrayBinaryResource(this.cacheItem.value);
        }

        public long getSize() {
            if (this.size < 0) {
                this.size = this.cacheItem.size;
            }
            return this.size;
        }
    }

    @VisibleForTesting
    class InserterImpl implements DiskStorage.Inserter {
        private final String mResourceId;
        @VisibleForTesting
        final AVFSSQLiteCacheItem mTemporaryCacheItem;

        public boolean cleanUp() {
            return true;
        }

        public InserterImpl(String str, CacheKey cacheKey) {
            this.mResourceId = str;
            this.mTemporaryCacheItem = new AVFSSQLiteCacheItem(str, cacheKey);
        }

        public void writeData(WriterCallback writerCallback, CacheKey cacheKey, Object obj) throws IOException {
            AVFSSQLiteCacheItem aVFSSQLiteCacheItem = this.mTemporaryCacheItem;
            aVFSSQLiteCacheItem.getClass();
            OutputStream byteArrayOutputStream = new AVFSSQLiteCacheItem.ByteArrayOutputStream();
            try {
                OutputStream write = writerCallback.write(byteArrayOutputStream);
                try {
                    write.flush();
                    write.close();
                } catch (Throwable th) {
                    Throwable th2 = th;
                    byteArrayOutputStream = write;
                    th = th2;
                    byteArrayOutputStream.close();
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                byteArrayOutputStream.close();
                throw th;
            }
        }

        public BinaryResource commit(CacheKey cacheKey, Object obj) throws IOException {
            this.mTemporaryCacheItem.time = System.currentTimeMillis();
            this.mTemporaryCacheItem.save(SQLiteDefaultDiskStorage.this.getValuesDataBase());
            return new ByteArrayBinaryResource(this.mTemporaryCacheItem.value);
        }
    }

    /* access modifiers changed from: protected */
    public AVFSDataBase getValuesDataBase() throws IOException {
        if (this.mDatabase == null) {
            if (!this.mVersionDirectory.exists()) {
                mkdirs(this.mVersionDirectory, "getDataBase");
            }
            if (this.mIsEncrypted) {
                String absolutePath = new File(this.mVersionDirectory, "alivfs_encrypt.sqlite").getAbsolutePath();
                try {
                    AVFSDBFactory dBFactory = AVFSAdapterManager.getInstance().getDBFactory();
                    this.mDatabase = dBFactory.createDataBase(absolutePath, getStorageName() + "_Encrypt", 1);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            } else {
                try {
                    this.mDatabase = AVFSAdapterManager.getInstance().getDBFactory().createDataBase(new File(this.mVersionDirectory, "alivfs.sqlite").getAbsolutePath(), 1);
                } catch (Exception e2) {
                    throw new IOException(e2);
                }
            }
            AVFSSQLiteCacheItem.createTable(this.mDatabase);
        }
        return this.mDatabase;
    }

    public void close() throws IOException {
        if (this.mDatabase != null) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }
}
