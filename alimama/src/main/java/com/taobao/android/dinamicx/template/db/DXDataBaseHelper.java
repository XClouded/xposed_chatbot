package com.taobao.android.dinamicx.template.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.db.DXFileDataBaseEntry;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DXDataBaseHelper {
    private static final String INSERT_SQL = ("insert or replace into " + TABLE_NAME + Operators.BRACKET_START_STR + DXFileDataBaseEntry.Columns.BIZ_TYPE + "," + "name" + "," + "version" + "," + DXFileDataBaseEntry.Columns.MAIN_PATH + "," + DXFileDataBaseEntry.Columns.STYLE_FILES + "," + "url" + ") " + "values(?,?,?,?,?,?)");
    private static final String ORDER_BY = "version desc";
    private static final String[] QUERY_COLUMNS = {DXFileDataBaseEntry.Columns.BIZ_TYPE, "name", "version", DXFileDataBaseEntry.Columns.MAIN_PATH, DXFileDataBaseEntry.Columns.STYLE_FILES, "url"};
    private static final String QUERY_WHERE = "biz_type=? AND name=?";
    private static final String QUERY_WHERE_DEL = "biz_type=? AND name=? AND version=?";
    private static final String TABLE_NAME = DXFileDataBaseEntry.SCHEMA.getTableName();
    private static final String TAG = "DXDataBaseHelper";
    private DataBaseHelperManager mDbHelper;

    public DXDataBaseHelper(Context context, String str) {
        this.mDbHelper = new DataBaseHelperManager(context, str);
    }

    public void store(String str, List<DXTemplateItem> list) {
        if (!TextUtils.isEmpty(str) && list != null && !list.isEmpty()) {
            SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
            if (writableDatabase == null || !writableDatabase.isOpen()) {
                trackError(str, DXMonitorConstant.DX_MONITOR_DB_STORE, (DXTemplateItem) null, (int) DXError.DX_DB_STORE_ERROR, "SQLiteDatabase = null");
            } else {
                boolean z = true;
                try {
                    SQLiteStatement compileStatement = writableDatabase.compileStatement(INSERT_SQL);
                    writableDatabase.beginTransaction();
                    for (DXTemplateItem next : list) {
                        if (next.packageInfo != null) {
                            if (!TextUtils.isEmpty(next.packageInfo.mainFilePath)) {
                                if (insertOrReplaceItem(compileStatement, str, next)) {
                                }
                            }
                        }
                        z = false;
                    }
                    if (z) {
                        writableDatabase.setTransactionSuccessful();
                    }
                    writableDatabase.endTransaction();
                } catch (Throwable th) {
                    trackError(str, DXMonitorConstant.DX_MONITOR_DB_STORE, (DXTemplateItem) null, (int) DXError.DX_DB_STORE_ERROR, th);
                }
            }
            this.mDbHelper.closeDatabase();
        }
    }

    public void store(String str, DXTemplateItem dXTemplateItem) {
        if (!TextUtils.isEmpty(str) && dXTemplateItem != null && dXTemplateItem.packageInfo != null && !TextUtils.isEmpty(dXTemplateItem.packageInfo.mainFilePath)) {
            SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
            if (writableDatabase == null || !writableDatabase.isOpen()) {
                trackError(str, DXMonitorConstant.DX_MONITOR_DB_STORE, dXTemplateItem, (int) DXError.DX_DB_STORE_ERROR, "SQLiteDatabase = null");
            } else {
                try {
                    insertOrReplaceItem(writableDatabase.compileStatement(INSERT_SQL), str, dXTemplateItem);
                } catch (Throwable th) {
                    trackError(str, DXMonitorConstant.DX_MONITOR_DB_STORE, dXTemplateItem, (int) DXError.DX_DB_STORE_ERROR, th);
                }
            }
            this.mDbHelper.closeDatabase();
        }
    }

    private boolean insertOrReplaceItem(@NonNull SQLiteStatement sQLiteStatement, @NonNull String str, @NonNull DXTemplateItem dXTemplateItem) {
        bindString(sQLiteStatement, 1, str);
        bindString(sQLiteStatement, 2, dXTemplateItem.name);
        sQLiteStatement.bindLong(3, dXTemplateItem.version);
        bindString(sQLiteStatement, 4, dXTemplateItem.packageInfo.mainFilePath);
        bindString(sQLiteStatement, 5, changeMap2String(dXTemplateItem.packageInfo.subFilePathDict));
        bindString(sQLiteStatement, 6, dXTemplateItem.templateUrl);
        if (sQLiteStatement.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDbSize() {
        /*
            r6 = this;
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r0 = r6.mDbHelper
            android.database.sqlite.SQLiteDatabase r0 = r0.getReadableDatabase()
            r1 = 0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x003c }
            r2.<init>()     // Catch:{ Throwable -> 0x003c }
            java.lang.String r3 = "select * from "
            r2.append(r3)     // Catch:{ Throwable -> 0x003c }
            java.lang.String r3 = TABLE_NAME     // Catch:{ Throwable -> 0x003c }
            r2.append(r3)     // Catch:{ Throwable -> 0x003c }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x003c }
            android.database.Cursor r0 = r0.rawQuery(r2, r1)     // Catch:{ Throwable -> 0x003c }
            r0.moveToFirst()     // Catch:{ Throwable -> 0x0035, all -> 0x0030 }
            int r1 = r0.getCount()     // Catch:{ Throwable -> 0x0035, all -> 0x0030 }
            if (r0 == 0) goto L_0x002a
            r0.close()
        L_0x002a:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r0 = r6.mDbHelper
            r0.closeDatabase()
            goto L_0x0052
        L_0x0030:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0053
        L_0x0035:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x003d
        L_0x003a:
            r0 = move-exception
            goto L_0x0053
        L_0x003c:
            r0 = move-exception
        L_0x003d:
            java.lang.String r2 = "DinamicX_Db"
            java.lang.String r3 = "DB_Query"
            r4 = 60015(0xea6f, float:8.4099E-41)
            r6.trackError(r2, r3, r4, r0)     // Catch:{ all -> 0x003a }
            if (r1 == 0) goto L_0x004c
            r1.close()
        L_0x004c:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r0 = r6.mDbHelper
            r0.closeDatabase()
            r1 = 0
        L_0x0052:
            return r1
        L_0x0053:
            if (r1 == 0) goto L_0x0058
            r1.close()
        L_0x0058:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r1 = r6.mDbHelper
            r1.closeDatabase()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.db.DXDataBaseHelper.getDbSize():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0075, code lost:
        r5 = r5.split(",");
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem> query(java.lang.String r20, com.taobao.android.dinamicx.template.download.DXTemplateItem r21) {
        /*
            r19 = this;
            r7 = r19
            r8 = r21
            java.util.LinkedList r9 = new java.util.LinkedList
            r9.<init>()
            r0 = 2
            java.lang.String[] r14 = new java.lang.String[r0]
            r1 = 0
            r14[r1] = r20
            java.lang.String r2 = r8.name
            r3 = 1
            r14[r3] = r2
            r18 = 0
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r2 = r7.mDbHelper     // Catch:{ Throwable -> 0x00ba }
            android.database.sqlite.SQLiteDatabase r10 = r2.getReadableDatabase()     // Catch:{ Throwable -> 0x00ba }
            if (r10 != 0) goto L_0x0034
            java.lang.String r3 = "DB_Query"
            r5 = 60015(0xea6f, float:8.4099E-41)
            java.lang.String r6 = "SQLiteDatabase = null"
            r1 = r19
            r2 = r20
            r4 = r21
            r1.trackError((java.lang.String) r2, (java.lang.String) r3, (com.taobao.android.dinamicx.template.download.DXTemplateItem) r4, (int) r5, (java.lang.String) r6)     // Catch:{ Throwable -> 0x00ba }
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r0 = r7.mDbHelper
            r0.closeDatabase()
            return r9
        L_0x0034:
            java.lang.String r11 = TABLE_NAME     // Catch:{ Throwable -> 0x00ba }
            java.lang.String[] r12 = QUERY_COLUMNS     // Catch:{ Throwable -> 0x00ba }
            java.lang.String r13 = "biz_type=? AND name=?"
            r15 = 0
            r16 = 0
            java.lang.String r17 = "version desc"
            android.database.Cursor r2 = r10.query(r11, r12, r13, r14, r15, r16, r17)     // Catch:{ Throwable -> 0x00ba }
            if (r2 == 0) goto L_0x00b0
        L_0x0045:
            boolean r4 = r2.moveToNext()     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            if (r4 == 0) goto L_0x00b0
            com.taobao.android.dinamicx.template.download.DXTemplateItem r4 = new com.taobao.android.dinamicx.template.download.DXTemplateItem     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r4.<init>()     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo r5 = new com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r5.<init>()     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r4.packageInfo = r5     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            java.lang.String r5 = r8.name     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r4.name = r5     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            long r5 = r2.getLong(r0)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r4.version = r5     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo r5 = r4.packageInfo     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r6 = 3
            java.lang.String r6 = r2.getString(r6)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r5.mainFilePath = r6     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r5 = 4
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            if (r6 != 0) goto L_0x009e
            java.lang.String r6 = ","
            java.lang.String[] r5 = r5.split(r6)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            int r6 = r5.length     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            if (r6 <= r3) goto L_0x009e
            int r10 = r6 % 2
            if (r10 != 0) goto L_0x009e
            com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo r10 = r4.packageInfo     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            java.util.HashMap r11 = new java.util.HashMap     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r11.<init>()     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r10.subFilePathDict = r11     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r10 = 0
        L_0x008c:
            if (r10 >= r6) goto L_0x009e
            com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo r11 = r4.packageInfo     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            java.util.Map<java.lang.String, java.lang.String> r11 = r11.subFilePathDict     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r12 = r5[r10]     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            int r13 = r10 + 1
            r13 = r5[r13]     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r11.put(r12, r13)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            int r10 = r10 + 2
            goto L_0x008c
        L_0x009e:
            r5 = 5
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r4.templateUrl = r5     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            r9.addFirst(r4)     // Catch:{ Throwable -> 0x00ab, all -> 0x00a9 }
            goto L_0x0045
        L_0x00a9:
            r0 = move-exception
            goto L_0x00d5
        L_0x00ab:
            r0 = move-exception
            r6 = r0
            r18 = r2
            goto L_0x00bc
        L_0x00b0:
            if (r2 == 0) goto L_0x00cf
            r2.close()
            goto L_0x00cf
        L_0x00b6:
            r0 = move-exception
            r2 = r18
            goto L_0x00d5
        L_0x00ba:
            r0 = move-exception
            r6 = r0
        L_0x00bc:
            java.lang.String r3 = "DB_Query"
            r5 = 60015(0xea6f, float:8.4099E-41)
            r1 = r19
            r2 = r20
            r4 = r21
            r1.trackError((java.lang.String) r2, (java.lang.String) r3, (com.taobao.android.dinamicx.template.download.DXTemplateItem) r4, (int) r5, (java.lang.Throwable) r6)     // Catch:{ all -> 0x00b6 }
            if (r18 == 0) goto L_0x00cf
            r18.close()
        L_0x00cf:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r0 = r7.mDbHelper
            r0.closeDatabase()
            return r9
        L_0x00d5:
            if (r2 == 0) goto L_0x00da
            r2.close()
        L_0x00da:
            com.taobao.android.dinamicx.template.db.DXDataBaseHelper$DataBaseHelperManager r1 = r7.mDbHelper
            r1.closeDatabase()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.db.DXDataBaseHelper.query(java.lang.String, com.taobao.android.dinamicx.template.download.DXTemplateItem):java.util.LinkedList");
    }

    public void delete(String str, DXTemplateItem dXTemplateItem) {
        if (!TextUtils.isEmpty(str) && DXTemplateNamePathUtil.isValid(dXTemplateItem)) {
            try {
                String[] strArr = {str, dXTemplateItem.name, String.valueOf(dXTemplateItem.version)};
                SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
                if (writableDatabase != null) {
                    writableDatabase.delete(TABLE_NAME, QUERY_WHERE_DEL, strArr);
                    this.mDbHelper.closeDatabase();
                }
            } catch (Throwable th) {
                trackError(str, DXMonitorConstant.DX_MONITOR_DB_DELETE, dXTemplateItem, (int) DXError.DX_DB_DELETE_ERROR, th);
            }
        }
    }

    private String changeMap2String(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : map.entrySet()) {
            sb.append((String) next.getKey());
            sb.append(',');
            sb.append((String) next.getValue());
            sb.append(',');
        }
        if (sb.length() <= 0) {
            return null;
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void deleteAll() {
        SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (writableDatabase != null) {
            try {
                writableDatabase.execSQL("delete from " + TABLE_NAME);
            } catch (Throwable th) {
                trackError("DinamicX_db", DXMonitorConstant.DX_MONITOR_DB_DELETE_ALL, DXError.DX_DB_DELETE_ALL_ERROR, th);
            }
        }
        this.mDbHelper.closeDatabase();
    }

    public void closeDatabase() {
        this.mDbHelper.closeDatabaseByEnd();
    }

    public void dropTable() {
        SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (writableDatabase != null) {
            DXFileDataBaseEntry.SCHEMA.dropTables(writableDatabase);
        }
        this.mDbHelper.closeDatabase();
    }

    private final class DataBaseHelperManager {
        private SQLiteDatabase database = null;
        private DatabaseHelper helper;

        /* access modifiers changed from: package-private */
        public void closeDatabase() {
        }

        DataBaseHelperManager(Context context, String str) {
            this.helper = new DatabaseHelper(context, str);
            try {
                this.database = this.helper.getWritableDatabase();
            } catch (Throwable th) {
                DXDataBaseHelper.this.trackError("DinamicX_db", DXMonitorConstant.DX_MONITOR_DB_OPEN, DXError.DX_DB_OPEN_ERROR, th);
            }
        }

        /* access modifiers changed from: package-private */
        public SQLiteDatabase getWritableDatabase() {
            if (this.database == null) {
                try {
                    this.database = this.helper.getWritableDatabase();
                } catch (Throwable th) {
                    DXDataBaseHelper.this.trackError("DinamicX_db", DXMonitorConstant.DX_MONITOR_DB_OPEN, DXError.DX_DB_OPEN_ERROR, th);
                }
            }
            return this.database;
        }

        /* access modifiers changed from: package-private */
        public SQLiteDatabase getReadableDatabase() {
            if (this.database == null) {
                try {
                    this.database = this.helper.getReadableDatabase();
                } catch (Throwable th) {
                    DXDataBaseHelper.this.trackError("DinamicX_db", DXMonitorConstant.DX_MONITOR_DB_OPEN, DXError.DX_DB_OPEN_ERROR, th);
                }
            }
            return this.database;
        }

        /* access modifiers changed from: package-private */
        public void closeDatabaseByEnd() {
            try {
                if (this.database != null && this.database.isOpen()) {
                    this.database.close();
                }
            } catch (Throwable th) {
                this.database = null;
                throw th;
            }
            this.database = null;
        }
    }

    private void bindString(@NonNull SQLiteStatement sQLiteStatement, int i, String str) {
        if (str == null) {
            sQLiteStatement.bindNull(i);
        } else {
            sQLiteStatement.bindString(i, str);
        }
    }

    private final class DatabaseHelper extends SQLiteOpenHelper {
        static final int DATABASE_VERSION = 1;

        DatabaseHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            long nanoTime = System.nanoTime();
            DXFileDataBaseEntry.SCHEMA.createTables(sQLiteDatabase);
            DXDataBaseHelper.this.trackerPerform(DXMonitorConstant.DX_MONITOR_DB_CREATE, System.nanoTime() - nanoTime);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            DXFileDataBaseEntry.SCHEMA.dropTables(sQLiteDatabase);
            onCreate(sQLiteDatabase);
        }
    }

    /* access modifiers changed from: private */
    public void trackError(String str, String str2, int i, Throwable th) {
        trackError(str, str2, (DXTemplateItem) null, i, th);
    }

    private void trackError(String str, String str2, DXTemplateItem dXTemplateItem, int i, Throwable th) {
        trackError(str, str2, dXTemplateItem, i, DXExceptionUtil.getStackTrace(th));
    }

    private void trackError(String str, String str2, DXTemplateItem dXTemplateItem, int i, String str3) {
        DXError dXError = new DXError(str);
        dXError.dxTemplateItem = dXTemplateItem;
        DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_DB, str2, i);
        dXErrorInfo.reason = str3;
        dXError.dxErrorInfoList = new ArrayList();
        dXError.dxErrorInfoList.add(dXErrorInfo);
        DXAppMonitor.trackerError(dXError);
    }

    /* access modifiers changed from: private */
    public void trackerPerform(String str, long j) {
        DXAppMonitor.trackerPerform(2, "DinamicX_db", DXMonitorConstant.DX_MONITOR_DB, str, (DXTemplateItem) null, (Map<String, String>) null, (double) j, true);
    }
}
