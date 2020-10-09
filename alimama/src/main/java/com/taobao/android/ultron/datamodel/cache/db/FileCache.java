package com.taobao.android.ultron.datamodel.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import com.taobao.android.ultron.datamodel.cache.db.Entry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileCache {
    private static final String ID_WHERE = "_id=?";
    private static final int LRU_CAPACITY = 4;
    private static final String[] PROJECTION_SIZE_SUM = {String.format("sum(%s)", new Object[]{"size"})};
    private static final String QUERY_WHERE = "hash_code=? AND tag=?";
    private static final String TABLE_NAME = FileEntry.SCHEMA.getTableName();
    private static final String TAG = "FileCache";
    private long mCapacity;
    private DatabaseHelper mDbHelper;
    private final LruCache<String, CacheEntry> mEntryMap;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public File mRootDir;

    public static final class CacheEntry {
        public File cacheFile;
        /* access modifiers changed from: private */
        public long id;
        public String tag;

        private CacheEntry(long j, String str, File file) {
            this.id = j;
            this.tag = str;
            this.cacheFile = file;
        }
    }

    public CacheEntry lookup(String str) {
        if (!this.mInitialized) {
            try {
                initialize();
            } catch (Exception e) {
                Log.e(TAG, "file cache init exception:", e);
                return null;
            }
        }
        CacheEntry cacheEntry = this.mEntryMap.get(str);
        if (cacheEntry != null) {
            if (cacheEntry.cacheFile.isFile()) {
                synchronized (this) {
                    updateLastAccess(cacheEntry.id);
                }
                return cacheEntry;
            }
            this.mEntryMap.remove(str);
        }
        synchronized (this) {
            FileEntry queryDatabase = queryDatabase(str);
            if (queryDatabase == null) {
                return null;
            }
            CacheEntry cacheEntry2 = new CacheEntry(queryDatabase.id, str, new File(this.mRootDir, queryDatabase.filename));
            if (!cacheEntry2.cacheFile.isFile()) {
                try {
                    this.mDbHelper.getWritableDatabase().delete(TABLE_NAME, ID_WHERE, new String[]{String.valueOf(queryDatabase.id)});
                } catch (Throwable th) {
                    Log.w(TAG, "cannot delete entry: " + queryDatabase.filename, th);
                }
            } else {
                this.mEntryMap.put(str, cacheEntry2);
                return cacheEntry2;
            }
        }
        return null;
    }

    public FileCache(Context context, File file, String str, long j) {
        this(context, file, str, j, 4);
    }

    public FileCache(Context context, File file, String str, long j, int i) {
        this.mInitialized = false;
        this.mRootDir = file;
        this.mCapacity = j;
        this.mEntryMap = new LruCache<>(i);
        this.mDbHelper = new DatabaseHelper(context, str);
        if (Build.VERSION.SDK_INT >= 16) {
            this.mDbHelper.setWriteAheadLoggingEnabled(false);
        }
    }

    public void del(String str) {
        synchronized (this) {
            FileEntry queryDatabase = queryDatabase(str);
            if (queryDatabase != null) {
                CacheEntry cacheEntry = new CacheEntry(queryDatabase.id, str, new File(this.mRootDir, queryDatabase.filename));
                try {
                    this.mDbHelper.getWritableDatabase().delete(TABLE_NAME, ID_WHERE, new String[]{String.valueOf(queryDatabase.id)});
                } catch (Throwable th) {
                    Log.w(TAG, "cannot delete db entry: " + queryDatabase.filename, th);
                }
                try {
                    cacheEntry.cacheFile.delete();
                } catch (Throwable th2) {
                    Log.w(TAG, "cannot delete file: " + queryDatabase.filename, th2);
                }
            }
        }
        return;
    }

    public void store(String str, File file) {
        if (!this.mInitialized) {
            try {
                initialize();
            } catch (Exception e) {
                Log.e(TAG, "file cache init exception:", e);
                return;
            }
        }
        Utils.assertTrue(file.getParentFile().equals(this.mRootDir));
        FileEntry fileEntry = new FileEntry();
        fileEntry.hashCode = Utils.crc64Long(str);
        fileEntry.tag = str;
        fileEntry.filename = file.getName();
        fileEntry.size = file.length();
        fileEntry.lastAccess = System.currentTimeMillis();
        if (fileEntry.size < this.mCapacity) {
            synchronized (this) {
                FileEntry queryDatabase = queryDatabase(str);
                if (queryDatabase != null) {
                    fileEntry.filename = queryDatabase.filename;
                    fileEntry.size = queryDatabase.size;
                }
                FileEntry.SCHEMA.insertOrReplace(this.mDbHelper.getWritableDatabase(), fileEntry);
                Log.e("detail_FileCache", "insertOrReplace entry:" + fileEntry);
            }
            return;
        }
        file.delete();
        throw new IllegalArgumentException("file too large: " + fileEntry.size);
    }

    public List<CacheEntry> getAll() {
        if (!this.mInitialized) {
            try {
                initialize();
            } catch (Exception e) {
                Log.e(TAG, "file cache init exception:", e);
                return null;
            }
        }
        synchronized (this) {
            ArrayList arrayList = new ArrayList();
            List<FileEntry> allFiles = getAllFiles();
            if (allFiles == null) {
                return null;
            }
            for (FileEntry next : allFiles) {
                CacheEntry cacheEntry = new CacheEntry(next.id, next.tag, new File(this.mRootDir, next.filename));
                if (!cacheEntry.cacheFile.isFile()) {
                    try {
                        this.mDbHelper.getWritableDatabase().delete(TABLE_NAME, ID_WHERE, new String[]{String.valueOf(next.id)});
                    } catch (Throwable th) {
                        Log.w(TAG, "cannot delete entry: " + next.filename, th);
                    }
                } else {
                    arrayList.add(cacheEntry);
                }
            }
            return arrayList;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0049, code lost:
        if (r2 != null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004b, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004e, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0036, code lost:
        if (r2 != null) goto L_0x004b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0052  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry> getAllFiles() {
        /*
            r12 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            com.taobao.android.ultron.datamodel.cache.db.FileCache$DatabaseHelper r2 = r12.mDbHelper     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
            android.database.sqlite.SQLiteDatabase r3 = r2.getReadableDatabase()     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
            java.lang.String r4 = TABLE_NAME     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
            com.taobao.android.ultron.datamodel.cache.db.EntrySchema r2 = com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
            java.lang.String[] r5 = r2.getProjection()     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r2 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Throwable -> 0x003e, all -> 0x003b }
        L_0x001d:
            boolean r3 = r2.moveToNext()     // Catch:{ Throwable -> 0x0039 }
            if (r3 == 0) goto L_0x0036
            com.taobao.android.ultron.datamodel.cache.db.FileCache$FileEntry r3 = new com.taobao.android.ultron.datamodel.cache.db.FileCache$FileEntry     // Catch:{ Throwable -> 0x0039 }
            r3.<init>()     // Catch:{ Throwable -> 0x0039 }
            com.taobao.android.ultron.datamodel.cache.db.EntrySchema r4 = com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x0039 }
            r4.cursorToObject(r2, r3)     // Catch:{ Throwable -> 0x0039 }
            long r4 = r3.id     // Catch:{ Throwable -> 0x0039 }
            r12.updateLastAccess(r4)     // Catch:{ Throwable -> 0x0039 }
            r0.add(r3)     // Catch:{ Throwable -> 0x0039 }
            goto L_0x001d
        L_0x0036:
            if (r2 == 0) goto L_0x004e
            goto L_0x004b
        L_0x0039:
            r1 = move-exception
            goto L_0x0042
        L_0x003b:
            r0 = move-exception
            r2 = r1
            goto L_0x0050
        L_0x003e:
            r2 = move-exception
            r11 = r2
            r2 = r1
            r1 = r11
        L_0x0042:
            java.lang.String r3 = "FileCache"
            java.lang.String r4 = "query database exception"
            android.util.Log.e(r3, r4, r1)     // Catch:{ all -> 0x004f }
            if (r2 == 0) goto L_0x004e
        L_0x004b:
            r2.close()
        L_0x004e:
            return r0
        L_0x004f:
            r0 = move-exception
        L_0x0050:
            if (r2 == 0) goto L_0x0055
            r2.close()
        L_0x0055:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.db.FileCache.getAllFiles():java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry queryDatabase(java.lang.String r13) {
        /*
            r12 = this;
            long r0 = com.taobao.android.ultron.datamodel.cache.db.Utils.crc64Long((java.lang.String) r13)
            r2 = 2
            java.lang.String[] r7 = new java.lang.String[r2]
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r1 = 0
            r7[r1] = r0
            r0 = 1
            r7[r0] = r13
            r13 = 0
            com.taobao.android.ultron.datamodel.cache.db.FileCache$DatabaseHelper r0 = r12.mDbHelper     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            android.database.sqlite.SQLiteDatabase r3 = r0.getReadableDatabase()     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            java.lang.String r4 = TABLE_NAME     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            com.taobao.android.ultron.datamodel.cache.db.EntrySchema r0 = com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            java.lang.String[] r5 = r0.getProjection()     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            java.lang.String r6 = "hash_code=? AND tag=?"
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r0 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            boolean r1 = r0.moveToNext()     // Catch:{ Throwable -> 0x004a }
            if (r1 != 0) goto L_0x0035
            if (r0 == 0) goto L_0x0034
            r0.close()
        L_0x0034:
            return r13
        L_0x0035:
            com.taobao.android.ultron.datamodel.cache.db.FileCache$FileEntry r1 = new com.taobao.android.ultron.datamodel.cache.db.FileCache$FileEntry     // Catch:{ Throwable -> 0x004a }
            r1.<init>()     // Catch:{ Throwable -> 0x004a }
            com.taobao.android.ultron.datamodel.cache.db.EntrySchema r2 = com.taobao.android.ultron.datamodel.cache.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x004a }
            r2.cursorToObject(r0, r1)     // Catch:{ Throwable -> 0x004a }
            long r2 = r1.id     // Catch:{ Throwable -> 0x004a }
            r12.updateLastAccess(r2)     // Catch:{ Throwable -> 0x004a }
            if (r0 == 0) goto L_0x0049
            r0.close()
        L_0x0049:
            return r1
        L_0x004a:
            r1 = move-exception
            goto L_0x0053
        L_0x004c:
            r0 = move-exception
            r11 = r0
            r0 = r13
            r13 = r11
            goto L_0x0061
        L_0x0051:
            r1 = move-exception
            r0 = r13
        L_0x0053:
            java.lang.String r2 = "FileCache"
            java.lang.String r3 = "query database exception"
            android.util.Log.e(r2, r3, r1)     // Catch:{ all -> 0x0060 }
            if (r0 == 0) goto L_0x005f
            r0.close()
        L_0x005f:
            return r13
        L_0x0060:
            r13 = move-exception
        L_0x0061:
            if (r0 == 0) goto L_0x0066
            r0.close()
        L_0x0066:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.db.FileCache.queryDatabase(java.lang.String):com.taobao.android.ultron.datamodel.cache.db.FileCache$FileEntry");
    }

    private void updateLastAccess(long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_access", Long.valueOf(System.currentTimeMillis()));
        try {
            this.mDbHelper.getWritableDatabase().update(TABLE_NAME, contentValues, ID_WHERE, new String[]{String.valueOf(j)});
        } catch (Exception e) {
            Log.e(TAG, "update db exception", e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize() {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.mInitialized     // Catch:{ all -> 0x003f }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r3)
            return
        L_0x0007:
            r0 = 1
            r3.mInitialized = r0     // Catch:{ all -> 0x003f }
            java.io.File r0 = r3.mRootDir     // Catch:{ all -> 0x003f }
            boolean r0 = r0.isDirectory()     // Catch:{ all -> 0x003f }
            if (r0 != 0) goto L_0x003d
            java.io.File r0 = r3.mRootDir     // Catch:{ all -> 0x003f }
            r0.mkdirs()     // Catch:{ all -> 0x003f }
            java.io.File r0 = r3.mRootDir     // Catch:{ all -> 0x003f }
            boolean r0 = r0.isDirectory()     // Catch:{ all -> 0x003f }
            if (r0 == 0) goto L_0x0020
            goto L_0x003d
        L_0x0020:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ all -> 0x003f }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x003f }
            r1.<init>()     // Catch:{ all -> 0x003f }
            java.lang.String r2 = "cannot create: "
            r1.append(r2)     // Catch:{ all -> 0x003f }
            java.io.File r2 = r3.mRootDir     // Catch:{ all -> 0x003f }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ all -> 0x003f }
            r1.append(r2)     // Catch:{ all -> 0x003f }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x003f }
            r0.<init>(r1)     // Catch:{ all -> 0x003f }
            throw r0     // Catch:{ all -> 0x003f }
        L_0x003d:
            monitor-exit(r3)
            return
        L_0x003f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.db.FileCache.initialize():void");
    }

    @Entry.Table("file_cache")
    private static class FileEntry extends Entry {
        public static final EntrySchema SCHEMA = new EntrySchema(FileEntry.class);
        @Entry.Column("filename")
        public String filename;
        @Entry.Column(indexed = true, value = "hash_code")
        public long hashCode;
        @Entry.Column(indexed = true, value = "last_access")
        public long lastAccess;
        @Entry.Column("size")
        public long size;
        @Entry.Column("tag")
        public String tag;

        public interface Columns extends Entry.Columns {
            public static final String FILENAME = "filename";
            public static final String HASH_CODE = "hash_code";
            public static final String LAST_ACCESS = "last_access";
            public static final String SIZE = "size";
            public static final String TAG = "tag";
        }

        private FileEntry() {
        }

        public String toString() {
            return "FileEntry{hashCode=" + this.hashCode + ", tag='" + this.tag + '\'' + ", filename='" + this.filename + '\'' + ", size=" + this.size + ", lastAccess=" + this.lastAccess + '}';
        }
    }

    private final class DatabaseHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            FileEntry.SCHEMA.createTables(sQLiteDatabase);
            File[] listFiles = FileCache.this.mRootDir.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (!file.delete()) {
                        Log.w(FileCache.TAG, "fail to remove: " + file.getAbsolutePath());
                    }
                }
            }
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            FileEntry.SCHEMA.dropTables(sQLiteDatabase);
            onCreate(sQLiteDatabase);
        }
    }
}
