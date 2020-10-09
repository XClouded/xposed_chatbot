package com.taobao.android.dinamic.tempate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import com.taobao.android.dinamic.tempate.db.Entry;
import java.io.File;

public class FileCache {
    private static final String FREE_SPACE_ORDER_BY = String.format("%s ASC", new Object[]{"last_access"});
    private static final String[] FREE_SPACE_PROJECTION = {"_id", "filename", "tag", "size"};
    private static final String ID_WHERE = "_id=?";
    private static final int LRU_CAPACITY = 4;
    private static final int MAX_DELETE_COUNT = 16;
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
    private long mTotalBytes;
    private OnDeleteFileListener onDeleteFileListener;

    public interface OnDeleteFileListener {
        void afterDeleteFile();

        void beforeDeleteFile(File file);
    }

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

    public static void deleteFiles(Context context, File file, String str) {
        try {
            context.getDatabasePath(str).delete();
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isFile()) {
                        file2.delete();
                    }
                }
            }
        } catch (Throwable th) {
            Log.w(TAG, "cannot reset database", th);
        }
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
    }

    public void setOnDeleteFileListener(OnDeleteFileListener onDeleteFileListener2) {
        this.onDeleteFileListener = onDeleteFileListener2;
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
                } else {
                    this.mTotalBytes += fileEntry.size;
                }
                FileEntry.SCHEMA.insertOrReplace(this.mDbHelper.getWritableDatabase(), fileEntry);
                if (this.mTotalBytes > this.mCapacity) {
                    freeSomeSpaceIfNeed(16);
                }
            }
            return;
        }
        file.delete();
        throw new IllegalArgumentException("file too large: " + fileEntry.size);
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
                    this.mTotalBytes -= queryDatabase.size;
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

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.android.dinamic.tempate.db.FileCache.FileEntry queryDatabase(java.lang.String r13) {
        /*
            r12 = this;
            long r0 = com.taobao.android.dinamic.tempate.db.Utils.crc64Long((java.lang.String) r13)
            r2 = 2
            java.lang.String[] r7 = new java.lang.String[r2]
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r1 = 0
            r7[r1] = r0
            r0 = 1
            r7[r0] = r13
            r13 = 0
            com.taobao.android.dinamic.tempate.db.FileCache$DatabaseHelper r0 = r12.mDbHelper     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            android.database.sqlite.SQLiteDatabase r3 = r0.getReadableDatabase()     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            java.lang.String r4 = TABLE_NAME     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
            com.taobao.android.dinamic.tempate.db.EntrySchema r0 = com.taobao.android.dinamic.tempate.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x0051, all -> 0x004c }
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
            com.taobao.android.dinamic.tempate.db.FileCache$FileEntry r1 = new com.taobao.android.dinamic.tempate.db.FileCache$FileEntry     // Catch:{ Throwable -> 0x004a }
            r1.<init>()     // Catch:{ Throwable -> 0x004a }
            com.taobao.android.dinamic.tempate.db.EntrySchema r2 = com.taobao.android.dinamic.tempate.db.FileCache.FileEntry.SCHEMA     // Catch:{ Throwable -> 0x004a }
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.db.FileCache.queryDatabase(java.lang.String):com.taobao.android.dinamic.tempate.db.FileCache$FileEntry");
    }

    private void updateLastAccess(long j) {
        if (Build.VERSION.SDK_INT == 29) {
            try {
                updateWithOnConflict(this.mDbHelper.getWritableDatabase(), TABLE_NAME, ID_WHERE, String.valueOf(j));
            } catch (Throwable th) {
                Log.e(TAG, "sdk int 29 update db exception", th);
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("last_access", Long.valueOf(System.currentTimeMillis()));
            try {
                this.mDbHelper.getWritableDatabase().update(TABLE_NAME, contentValues, ID_WHERE, new String[]{String.valueOf(j)});
            } catch (Throwable th2) {
                Log.e(TAG, "update db exception", th2);
            }
        }
    }

    private int updateWithOnConflict(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3) {
        SQLiteStatement compileStatement;
        sQLiteDatabase.acquireReference();
        try {
            StringBuilder sb = new StringBuilder(120);
            sb.append("UPDATE ");
            sb.append(str);
            sb.append(" SET ");
            sb.append("last_access=?");
            if (!TextUtils.isEmpty(str2)) {
                sb.append(" WHERE ");
                sb.append(str2);
            }
            compileStatement = sQLiteDatabase.compileStatement(sb.toString());
            compileStatement.bindLong(1, System.currentTimeMillis());
            compileStatement.bindString(2, str3);
            int executeUpdateDelete = compileStatement.executeUpdateDelete();
            compileStatement.close();
            sQLiteDatabase.releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            sQLiteDatabase.releaseReference();
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x007a A[SYNTHETIC, Splitter:B:35:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x008e A[SYNTHETIC, Splitter:B:43:0x008e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize() {
        /*
            r11 = this;
            monitor-enter(r11)
            boolean r0 = r11.mInitialized     // Catch:{ all -> 0x0092 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r11)
            return
        L_0x0007:
            r0 = 1
            r11.mInitialized = r0     // Catch:{ all -> 0x0092 }
            java.io.File r0 = r11.mRootDir     // Catch:{ all -> 0x0092 }
            boolean r0 = r0.isDirectory()     // Catch:{ all -> 0x0092 }
            if (r0 != 0) goto L_0x003d
            java.io.File r0 = r11.mRootDir     // Catch:{ all -> 0x0092 }
            r0.mkdirs()     // Catch:{ all -> 0x0092 }
            java.io.File r0 = r11.mRootDir     // Catch:{ all -> 0x0092 }
            boolean r0 = r0.isDirectory()     // Catch:{ all -> 0x0092 }
            if (r0 == 0) goto L_0x0020
            goto L_0x003d
        L_0x0020:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ all -> 0x0092 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0092 }
            r1.<init>()     // Catch:{ all -> 0x0092 }
            java.lang.String r2 = "cannot create: "
            r1.append(r2)     // Catch:{ all -> 0x0092 }
            java.io.File r2 = r11.mRootDir     // Catch:{ all -> 0x0092 }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ all -> 0x0092 }
            r1.append(r2)     // Catch:{ all -> 0x0092 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0092 }
            r0.<init>(r1)     // Catch:{ all -> 0x0092 }
            throw r0     // Catch:{ all -> 0x0092 }
        L_0x003d:
            r0 = 0
            com.taobao.android.dinamic.tempate.db.FileCache$DatabaseHelper r1 = r11.mDbHelper     // Catch:{ Throwable -> 0x0070 }
            android.database.sqlite.SQLiteDatabase r2 = r1.getReadableDatabase()     // Catch:{ Throwable -> 0x0070 }
            java.lang.String r3 = TABLE_NAME     // Catch:{ Throwable -> 0x0070 }
            java.lang.String[] r4 = PROJECTION_SIZE_SUM     // Catch:{ Throwable -> 0x0070 }
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            android.database.Cursor r1 = r2.query(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0070 }
            boolean r0 = r1.moveToNext()     // Catch:{ Throwable -> 0x0069, all -> 0x0064 }
            if (r0 == 0) goto L_0x005e
            r0 = 0
            long r2 = r1.getLong(r0)     // Catch:{ Throwable -> 0x0069, all -> 0x0064 }
            r11.mTotalBytes = r2     // Catch:{ Throwable -> 0x0069, all -> 0x0064 }
        L_0x005e:
            if (r1 == 0) goto L_0x007d
            r1.close()     // Catch:{ all -> 0x0092 }
            goto L_0x007d
        L_0x0064:
            r0 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L_0x008c
        L_0x0069:
            r0 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
            goto L_0x0071
        L_0x006e:
            r1 = move-exception
            goto L_0x008c
        L_0x0070:
            r1 = move-exception
        L_0x0071:
            java.lang.String r2 = "FileCache"
            java.lang.String r3 = "query total bytes exception"
            android.util.Log.e(r2, r3, r1)     // Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x007d
            r0.close()     // Catch:{ all -> 0x0092 }
        L_0x007d:
            long r0 = r11.mTotalBytes     // Catch:{ all -> 0x0092 }
            long r2 = r11.mCapacity     // Catch:{ all -> 0x0092 }
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 <= 0) goto L_0x008a
            r0 = 16
            r11.freeSomeSpaceIfNeed(r0)     // Catch:{ all -> 0x0092 }
        L_0x008a:
            monitor-exit(r11)
            return
        L_0x008c:
            if (r0 == 0) goto L_0x0091
            r0.close()     // Catch:{ all -> 0x0092 }
        L_0x0091:
            throw r1     // Catch:{ all -> 0x0092 }
        L_0x0092:
            r0 = move-exception
            monitor-exit(r11)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.db.FileCache.initialize():void");
    }

    private void freeSomeSpaceIfNeed(int i) {
        Cursor query = this.mDbHelper.getReadableDatabase().query(TABLE_NAME, FREE_SPACE_PROJECTION, (String) null, (String[]) null, (String) null, (String) null, FREE_SPACE_ORDER_BY);
        while (i > 0) {
            try {
                if (this.mTotalBytes <= this.mCapacity || !query.moveToNext()) {
                    break;
                }
                long j = query.getLong(0);
                String string = query.getString(1);
                String string2 = query.getString(2);
                long j2 = query.getLong(3);
                synchronized (this.mEntryMap) {
                    if (this.mEntryMap.get(string2) == null) {
                        i--;
                        if (deleteFileAndCallListener(new File(this.mRootDir, string))) {
                            this.mTotalBytes -= j2;
                            this.mDbHelper.getWritableDatabase().delete(TABLE_NAME, ID_WHERE, new String[]{String.valueOf(j)});
                        } else {
                            Log.w(TAG, "unable to delete file: " + string);
                        }
                    }
                }
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        }
        query.close();
    }

    private boolean deleteFileAndCallListener(File file) {
        if (file == null) {
            return true;
        }
        if (this.onDeleteFileListener != null) {
            try {
                this.onDeleteFileListener.beforeDeleteFile(file);
            } catch (Throwable th) {
                Log.e(TAG, "before delete file action exception", th);
            }
        }
        boolean delete = file.delete();
        if (this.onDeleteFileListener == null) {
            return delete;
        }
        try {
            this.onDeleteFileListener.afterDeleteFile();
            return delete;
        } catch (Throwable th2) {
            Log.e(TAG, "after delete file action exception", th2);
            return delete;
        }
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
