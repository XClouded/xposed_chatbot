package com.taobao.weex.appfram.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.taobao.weex.utils.WXLogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WXSQLiteOpenHelper extends SQLiteOpenHelper {
    static final String COLUMN_KEY = "key";
    static final String COLUMN_PERSISTENT = "persistent";
    static final String COLUMN_TIMESTAMP = "timestamp";
    static final String COLUMN_VALUE = "value";
    private static final String DATABASE_NAME = "WXStorage";
    private static final int DATABASE_VERSION = 2;
    private static final int SLEEP_TIME_MS = 30;
    private static final String STATEMENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS default_wx_storage (key TEXT PRIMARY KEY,value TEXT NOT NULL,timestamp TEXT NOT NULL,persistent INTEGER DEFAULT 0)";
    static final String TABLE_STORAGE = "default_wx_storage";
    static final String TAG_STORAGE = "weex_storage";
    static SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private Context mContext;
    private SQLiteDatabase mDb;
    private long mMaximumDatabaseSize = 52428800;

    public WXSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 2);
        this.mContext = context;
    }

    @Nullable
    public SQLiteDatabase getDatabase() {
        ensureDatabase();
        return this.mDb;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(STATEMENT_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != i2) {
            if (i2 == 2) {
                boolean z = true;
                if (i == 1) {
                    WXLogUtils.d(TAG_STORAGE, "storage is updating from version " + i + " to version " + i2);
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        sQLiteDatabase.beginTransaction();
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + "ALTER TABLE default_wx_storage ADD COLUMN timestamp TEXT;");
                        sQLiteDatabase.execSQL("ALTER TABLE default_wx_storage ADD COLUMN timestamp TEXT;");
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + "ALTER TABLE default_wx_storage ADD COLUMN persistent INTEGER;");
                        sQLiteDatabase.execSQL("ALTER TABLE default_wx_storage ADD COLUMN persistent INTEGER;");
                        String str = "UPDATE default_wx_storage SET timestamp = '" + sDateFormatter.format(new Date()) + "' , " + COLUMN_PERSISTENT + " = 0";
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + str);
                        sQLiteDatabase.execSQL(str);
                        sQLiteDatabase.setTransactionSuccessful();
                        WXLogUtils.d(TAG_STORAGE, "storage updated success (" + (System.currentTimeMillis() - currentTimeMillis) + "ms)");
                    } catch (Exception e) {
                        WXLogUtils.d(TAG_STORAGE, "storage updated failed from version " + i + " to version " + i2 + "," + e.getMessage());
                        z = false;
                    } catch (Throwable th) {
                        sQLiteDatabase.endTransaction();
                        throw th;
                    }
                    sQLiteDatabase.endTransaction();
                    if (!z) {
                        WXLogUtils.d(TAG_STORAGE, "storage is rollback,all data will be removed");
                        deleteDB();
                        onCreate(sQLiteDatabase);
                        return;
                    }
                    return;
                }
            }
            deleteDB();
            onCreate(sQLiteDatabase);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(11:14|15|17|18|19|20|21|22|23|42|24) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x002b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void ensureDatabase() {
        /*
            r4 = this;
            monitor-enter(r4)
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ all -> 0x0068 }
            if (r0 == 0) goto L_0x000f
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ all -> 0x0068 }
            boolean r0 = r0.isOpen()     // Catch:{ all -> 0x0068 }
            if (r0 == 0) goto L_0x000f
            monitor-exit(r4)
            return
        L_0x000f:
            r0 = 0
        L_0x0010:
            r1 = 2
            if (r0 >= r1) goto L_0x0035
            if (r0 <= 0) goto L_0x001b
            r4.deleteDB()     // Catch:{ SQLiteException -> 0x0019 }
            goto L_0x001b
        L_0x0019:
            r1 = move-exception
            goto L_0x0022
        L_0x001b:
            android.database.sqlite.SQLiteDatabase r1 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0019 }
            r4.mDb = r1     // Catch:{ SQLiteException -> 0x0019 }
            goto L_0x0035
        L_0x0022:
            r1.printStackTrace()     // Catch:{ Throwable -> 0x0048 }
            r1 = 30
            java.lang.Thread.sleep(r1)     // Catch:{ InterruptedException -> 0x002b }
            goto L_0x0032
        L_0x002b:
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0048 }
            r1.interrupt()     // Catch:{ Throwable -> 0x0048 }
        L_0x0032:
            int r0 = r0 + 1
            goto L_0x0010
        L_0x0035:
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ Throwable -> 0x0048 }
            if (r0 != 0) goto L_0x003b
            monitor-exit(r4)
            return
        L_0x003b:
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ Throwable -> 0x0048 }
            r4.createTableIfNotExists(r0)     // Catch:{ Throwable -> 0x0048 }
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ Throwable -> 0x0048 }
            long r1 = r4.mMaximumDatabaseSize     // Catch:{ Throwable -> 0x0048 }
            r0.setMaximumSize(r1)     // Catch:{ Throwable -> 0x0048 }
            goto L_0x0066
        L_0x0048:
            r0 = move-exception
            r1 = 0
            r4.mDb = r1     // Catch:{ all -> 0x0068 }
            java.lang.String r1 = "weex_storage"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0068 }
            r2.<init>()     // Catch:{ all -> 0x0068 }
            java.lang.String r3 = "ensureDatabase failed, throwable = "
            r2.append(r3)     // Catch:{ all -> 0x0068 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0068 }
            r2.append(r0)     // Catch:{ all -> 0x0068 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0068 }
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r1, (java.lang.String) r0)     // Catch:{ all -> 0x0068 }
        L_0x0066:
            monitor-exit(r4)
            return
        L_0x0068:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.ensureDatabase():void");
    }

    public synchronized void setMaximumSize(long j) {
        this.mMaximumDatabaseSize = j;
        if (this.mDb != null) {
            this.mDb.setMaximumSize(this.mMaximumDatabaseSize);
        }
    }

    private boolean deleteDB() {
        closeDatabase();
        return this.mContext.deleteDatabase(DATABASE_NAME);
    }

    public synchronized void closeDatabase() {
        if (this.mDb != null && this.mDb.isOpen()) {
            this.mDb.close();
            this.mDb = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void createTableIfNotExists(@androidx.annotation.NonNull android.database.sqlite.SQLiteDatabase r3) {
        /*
            r2 = this;
            r0 = 0
            java.lang.String r1 = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = 'default_wx_storage'"
            android.database.Cursor r1 = r3.rawQuery(r1, r0)     // Catch:{ Exception -> 0x0028 }
            if (r1 == 0) goto L_0x001b
            int r0 = r1.getCount()     // Catch:{ Exception -> 0x0018, all -> 0x0015 }
            if (r0 <= 0) goto L_0x001b
            if (r1 == 0) goto L_0x0014
            r1.close()
        L_0x0014:
            return
        L_0x0015:
            r3 = move-exception
            r0 = r1
            goto L_0x0032
        L_0x0018:
            r3 = move-exception
            r0 = r1
            goto L_0x0029
        L_0x001b:
            java.lang.String r0 = "CREATE TABLE IF NOT EXISTS default_wx_storage (key TEXT PRIMARY KEY,value TEXT NOT NULL,timestamp TEXT NOT NULL,persistent INTEGER DEFAULT 0)"
            r3.execSQL(r0)     // Catch:{ Exception -> 0x0018, all -> 0x0015 }
            if (r1 == 0) goto L_0x0031
            r1.close()
            goto L_0x0031
        L_0x0026:
            r3 = move-exception
            goto L_0x0032
        L_0x0028:
            r3 = move-exception
        L_0x0029:
            r3.printStackTrace()     // Catch:{ all -> 0x0026 }
            if (r0 == 0) goto L_0x0031
            r0.close()
        L_0x0031:
            return
        L_0x0032:
            if (r0 == 0) goto L_0x0037
            r0.close()
        L_0x0037:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.createTableIfNotExists(android.database.sqlite.SQLiteDatabase):void");
    }
}
