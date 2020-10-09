package com.alibaba.analytics.core.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.analytics.utils.Logger;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DBMgr {
    private static final String TAG = "DBMgr";
    private HashMap<String, Boolean> mCheckdbMap = new HashMap<>();
    private HashMap<Class<?>, List<Field>> mClsFieldsMap = new HashMap<>();
    private HashMap<Class<?>, String> mClsTableNameMap = new HashMap<>();
    private String mDbName;
    private HashMap<Field, String> mFieldNameMap = new HashMap<>();
    private SqliteHelper mHelper;

    public DBMgr(Context context, String str) {
        this.mHelper = new SqliteHelper(context, str);
        this.mDbName = str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0140, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0142, code lost:
        r12 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0140 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:29:0x0099] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:75:0x0132=Splitter:B:75:0x0132, B:93:0x0163=Splitter:B:93:0x0163} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<? extends com.alibaba.analytics.core.db.Entity> find(java.lang.Class<? extends com.alibaba.analytics.core.db.Entity> r12, java.lang.String r13, java.lang.String r14, int r15) {
        /*
            r11 = this;
            monitor-enter(r11)
            java.util.List r0 = java.util.Collections.EMPTY_LIST     // Catch:{ all -> 0x016e }
            if (r12 != 0) goto L_0x0007
            monitor-exit(r11)
            return r0
        L_0x0007:
            java.lang.String r1 = r11.getTablename(r12)     // Catch:{ all -> 0x016e }
            android.database.sqlite.SQLiteDatabase r2 = r11.checkTableAvailable(r12, r1)     // Catch:{ all -> 0x016e }
            r3 = 2
            r4 = 1
            r5 = 0
            if (r2 != 0) goto L_0x0023
            java.lang.String r12 = "DBMgr"
            java.lang.Object[] r13 = new java.lang.Object[r3]     // Catch:{ all -> 0x016e }
            java.lang.String r14 = "[find] db is null. tableName"
            r13[r5] = r14     // Catch:{ all -> 0x016e }
            r13[r4] = r1     // Catch:{ all -> 0x016e }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ all -> 0x016e }
            monitor-exit(r11)
            return r0
        L_0x0023:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r6.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r7 = "SELECT * FROM "
            r6.append(r7)     // Catch:{ all -> 0x016e }
            r6.append(r1)     // Catch:{ all -> 0x016e }
            boolean r1 = android.text.TextUtils.isEmpty(r13)     // Catch:{ all -> 0x016e }
            if (r1 == 0) goto L_0x0039
            java.lang.String r13 = ""
            goto L_0x004a
        L_0x0039:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r1.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r7 = " WHERE "
            r1.append(r7)     // Catch:{ all -> 0x016e }
            r1.append(r13)     // Catch:{ all -> 0x016e }
            java.lang.String r13 = r1.toString()     // Catch:{ all -> 0x016e }
        L_0x004a:
            r6.append(r13)     // Catch:{ all -> 0x016e }
            boolean r13 = android.text.TextUtils.isEmpty(r14)     // Catch:{ all -> 0x016e }
            if (r13 == 0) goto L_0x0056
            java.lang.String r13 = ""
            goto L_0x0067
        L_0x0056:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r13.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r1 = " ORDER BY "
            r13.append(r1)     // Catch:{ all -> 0x016e }
            r13.append(r14)     // Catch:{ all -> 0x016e }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x016e }
        L_0x0067:
            r6.append(r13)     // Catch:{ all -> 0x016e }
            if (r15 > 0) goto L_0x006f
            java.lang.String r13 = ""
            goto L_0x0080
        L_0x006f:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r13.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r14 = " LIMIT "
            r13.append(r14)     // Catch:{ all -> 0x016e }
            r13.append(r15)     // Catch:{ all -> 0x016e }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x016e }
        L_0x0080:
            r6.append(r13)     // Catch:{ all -> 0x016e }
            java.lang.String r13 = r6.toString()     // Catch:{ all -> 0x016e }
            java.lang.String r14 = "DBMgr"
            java.lang.Object[] r15 = new java.lang.Object[r3]     // Catch:{ all -> 0x016e }
            java.lang.String r1 = "sql"
            r15[r5] = r1     // Catch:{ all -> 0x016e }
            r15[r4] = r13     // Catch:{ all -> 0x016e }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r14, (java.lang.Object[]) r15)     // Catch:{ all -> 0x016e }
            r14 = 0
            android.database.Cursor r13 = r2.rawQuery(r13, r14)     // Catch:{ Throwable -> 0x0148 }
            java.util.ArrayList r14 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0142, all -> 0x0140 }
            r14.<init>()     // Catch:{ Throwable -> 0x0142, all -> 0x0140 }
            java.util.List r15 = r11.getAllFields(r12)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
        L_0x00a2:
            if (r13 == 0) goto L_0x0132
            boolean r0 = r13.moveToNext()     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            if (r0 == 0) goto L_0x0132
            java.lang.Object r0 = r12.newInstance()     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            com.alibaba.analytics.core.db.Entity r0 = (com.alibaba.analytics.core.db.Entity) r0     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            r1 = 0
        L_0x00b1:
            int r6 = r15.size()     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            if (r1 >= r6) goto L_0x012d
            java.lang.Object r6 = r15.get(r1)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.reflect.Field r6 = (java.lang.reflect.Field) r6     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.Class r7 = r6.getType()     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.String r8 = r11.getColumnName(r6)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            int r9 = r13.getColumnIndex(r8)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            r10 = -1
            if (r9 == r10) goto L_0x011d
            java.lang.Class<java.lang.Long> r8 = java.lang.Long.class
            if (r7 == r8) goto L_0x00fe
            java.lang.Class r8 = java.lang.Long.TYPE     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            if (r7 != r8) goto L_0x00d5
            goto L_0x00fe
        L_0x00d5:
            java.lang.Class<java.lang.Integer> r8 = java.lang.Integer.class
            if (r7 == r8) goto L_0x00f5
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            if (r7 != r8) goto L_0x00de
            goto L_0x00f5
        L_0x00de:
            java.lang.Class r8 = java.lang.Double.TYPE     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            if (r7 == r8) goto L_0x00ec
            java.lang.Class<java.lang.Double> r8 = java.lang.Double.class
            if (r7 != r8) goto L_0x00e7
            goto L_0x00ec
        L_0x00e7:
            java.lang.String r7 = r13.getString(r9)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            goto L_0x0106
        L_0x00ec:
            double r7 = r13.getDouble(r9)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.Double r7 = java.lang.Double.valueOf(r7)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            goto L_0x0106
        L_0x00f5:
            int r7 = r13.getInt(r9)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            goto L_0x0106
        L_0x00fe:
            long r7 = r13.getLong(r9)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
        L_0x0106:
            r6.set(r0, r7)     // Catch:{ Exception -> 0x010a }
            goto L_0x012a
        L_0x010a:
            r8 = move-exception
            boolean r8 = r8 instanceof java.lang.IllegalArgumentException     // Catch:{ Throwable -> 0x012a, all -> 0x0140 }
            if (r8 == 0) goto L_0x012a
            boolean r8 = r7 instanceof java.lang.String     // Catch:{ Throwable -> 0x012a, all -> 0x0140 }
            if (r8 == 0) goto L_0x012a
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x012a, all -> 0x0140 }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)     // Catch:{ Throwable -> 0x012a, all -> 0x0140 }
            r6.set(r0, r7)     // Catch:{ Throwable -> 0x012a, all -> 0x0140 }
            goto L_0x012a
        L_0x011d:
            java.lang.String r6 = "DBMgr"
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            java.lang.String r9 = "can not get field"
            r7[r5] = r9     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            r7[r4] = r8     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r6, (java.lang.Object[]) r7)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
        L_0x012a:
            int r1 = r1 + 1
            goto L_0x00b1
        L_0x012d:
            r14.add(r0)     // Catch:{ Throwable -> 0x013d, all -> 0x0140 }
            goto L_0x00a2
        L_0x0132:
            com.alibaba.analytics.core.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x016e }
            r12.closeCursor(r13)     // Catch:{ all -> 0x016e }
            com.alibaba.analytics.core.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x016e }
            r12.closeWritableDatabase(r2)     // Catch:{ all -> 0x016e }
            goto L_0x0161
        L_0x013d:
            r12 = move-exception
            r0 = r14
            goto L_0x0143
        L_0x0140:
            r12 = move-exception
            goto L_0x0163
        L_0x0142:
            r12 = move-exception
        L_0x0143:
            r14 = r13
            goto L_0x0149
        L_0x0145:
            r12 = move-exception
            r13 = r14
            goto L_0x0163
        L_0x0148:
            r12 = move-exception
        L_0x0149:
            java.lang.String r13 = "DBMgr"
            java.lang.Object[] r15 = new java.lang.Object[r3]     // Catch:{ all -> 0x0145 }
            java.lang.String r1 = "[get]"
            r15[r5] = r1     // Catch:{ all -> 0x0145 }
            r15[r4] = r12     // Catch:{ all -> 0x0145 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r13, (java.lang.Object[]) r15)     // Catch:{ all -> 0x0145 }
            com.alibaba.analytics.core.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x016e }
            r12.closeCursor(r14)     // Catch:{ all -> 0x016e }
            com.alibaba.analytics.core.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x016e }
            r12.closeWritableDatabase(r2)     // Catch:{ all -> 0x016e }
            r14 = r0
        L_0x0161:
            monitor-exit(r11)
            return r14
        L_0x0163:
            com.alibaba.analytics.core.db.SqliteHelper r14 = r11.mHelper     // Catch:{ all -> 0x016e }
            r14.closeCursor(r13)     // Catch:{ all -> 0x016e }
            com.alibaba.analytics.core.db.SqliteHelper r13 = r11.mHelper     // Catch:{ all -> 0x016e }
            r13.closeWritableDatabase(r2)     // Catch:{ all -> 0x016e }
            throw r12     // Catch:{ all -> 0x016e }
        L_0x016e:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.find(java.lang.Class, java.lang.String, java.lang.String, int):java.util.List");
    }

    public void insert(Entity entity) {
        if (entity != null) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(entity);
            insert((List<? extends Entity>) arrayList);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:56|57|58|59|60|61) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:48|49|50|51|52|53|54|55) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:15|16|(8:19|(6:22|23|24|(2:26|71)(2:27|72)|32|20)|69|33|(2:35|(1:37))(1:38)|39|40|17)|41|42|43|44|45|46) */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x010e, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00e9 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00ec */
    /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00f6 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00f9 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:56:0x00ff */
    /* JADX WARNING: Missing exception handler attribute for start block: B:58:0x0102 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:60:0x0105 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x0105=Splitter:B:60:0x0105, B:53:0x00f9=Splitter:B:53:0x00f9, B:45:0x00ec=Splitter:B:45:0x00ec} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void insert(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r15) {
        /*
            r14 = this;
            monitor-enter(r14)
            if (r15 == 0) goto L_0x010d
            int r0 = r15.size()     // Catch:{ all -> 0x010a }
            if (r0 != 0) goto L_0x000b
            goto L_0x010d
        L_0x000b:
            r0 = 0
            java.lang.Object r1 = r15.get(r0)     // Catch:{ all -> 0x010a }
            com.alibaba.analytics.core.db.Entity r1 = (com.alibaba.analytics.core.db.Entity) r1     // Catch:{ all -> 0x010a }
            java.lang.Class r1 = r1.getClass()     // Catch:{ all -> 0x010a }
            java.lang.String r1 = r14.getTablename(r1)     // Catch:{ all -> 0x010a }
            java.lang.Object r2 = r15.get(r0)     // Catch:{ all -> 0x010a }
            com.alibaba.analytics.core.db.Entity r2 = (com.alibaba.analytics.core.db.Entity) r2     // Catch:{ all -> 0x010a }
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x010a }
            android.database.sqlite.SQLiteDatabase r2 = r14.checkTableAvailable(r2, r1)     // Catch:{ all -> 0x010a }
            r3 = 2
            r4 = 1
            if (r2 != 0) goto L_0x003b
            java.lang.String r15 = "DBMgr"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x010a }
            java.lang.String r3 = "[insert]can not get available db. tableName"
            r2[r0] = r3     // Catch:{ all -> 0x010a }
            r2[r4] = r1     // Catch:{ all -> 0x010a }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r15, (java.lang.Object[]) r2)     // Catch:{ all -> 0x010a }
            monitor-exit(r14)
            return
        L_0x003b:
            if (r15 == 0) goto L_0x0052
            java.lang.String r5 = "DBMgr"
            java.lang.Object[] r6 = new java.lang.Object[r3]     // Catch:{ all -> 0x010a }
            java.lang.String r7 = "entities.size"
            r6[r0] = r7     // Catch:{ all -> 0x010a }
            int r7 = r15.size()     // Catch:{ all -> 0x010a }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x010a }
            r6[r4] = r7     // Catch:{ all -> 0x010a }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ all -> 0x010a }
        L_0x0052:
            java.lang.Object r5 = r15.get(r0)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            com.alibaba.analytics.core.db.Entity r5 = (com.alibaba.analytics.core.db.Entity) r5     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.util.List r5 = r14.getAllFields(r5)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            android.content.ContentValues r6 = new android.content.ContentValues     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r6.<init>()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r2.beginTransaction()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r7 = 0
        L_0x0069:
            int r8 = r15.size()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            if (r7 >= r8) goto L_0x00e6
            java.lang.Object r8 = r15.get(r7)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            com.alibaba.analytics.core.db.Entity r8 = (com.alibaba.analytics.core.db.Entity) r8     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r9 = 0
        L_0x0076:
            int r10 = r5.size()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            if (r9 >= r10) goto L_0x00b8
            java.lang.Object r10 = r5.get(r9)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.reflect.Field r10 = (java.lang.reflect.Field) r10     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.String r11 = r14.getColumnName(r10)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.Object r10 = r10.get(r8)     // Catch:{ Exception -> 0x00a7 }
            if (r10 == 0) goto L_0x00a1
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a7 }
            r12.<init>()     // Catch:{ Exception -> 0x00a7 }
            r12.append(r10)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r10 = ""
            r12.append(r10)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r10 = r12.toString()     // Catch:{ Exception -> 0x00a7 }
            r6.put(r11, r10)     // Catch:{ Exception -> 0x00a7 }
            goto L_0x00b5
        L_0x00a1:
            java.lang.String r10 = ""
            r6.put(r11, r10)     // Catch:{ Exception -> 0x00a7 }
            goto L_0x00b5
        L_0x00a7:
            r10 = move-exception
            java.lang.String r11 = "DBMgr"
            java.lang.Object[] r12 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.String r13 = "get field failed"
            r12[r0] = r13     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r12[r4] = r10     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
        L_0x00b5:
            int r9 = r9 + 1
            goto L_0x0076
        L_0x00b8:
            long r9 = r8._id     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r11 = -1
            int r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r13 != 0) goto L_0x00d1
            java.lang.String r9 = "_id"
            r6.remove(r9)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r9 = 0
            long r9 = r2.insert(r1, r9, r6)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            int r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r13 == 0) goto L_0x00e0
            r8._id = r9     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            goto L_0x00e0
        L_0x00d1:
            java.lang.String r9 = "_id=?"
            java.lang.String[] r10 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            long r11 = r8._id     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            java.lang.String r8 = java.lang.String.valueOf(r11)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r10[r0] = r8     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            r2.update(r1, r6, r9, r10)     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
        L_0x00e0:
            r6.clear()     // Catch:{ Throwable -> 0x00ff, all -> 0x00f2 }
            int r7 = r7 + 1
            goto L_0x0069
        L_0x00e6:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00e9 }
        L_0x00e9:
            r2.endTransaction()     // Catch:{ Exception -> 0x00ec }
        L_0x00ec:
            com.alibaba.analytics.core.db.SqliteHelper r15 = r14.mHelper     // Catch:{ all -> 0x010a }
        L_0x00ee:
            r15.closeWritableDatabase(r2)     // Catch:{ all -> 0x010a }
            goto L_0x0108
        L_0x00f2:
            r15 = move-exception
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00f6 }
        L_0x00f6:
            r2.endTransaction()     // Catch:{ Exception -> 0x00f9 }
        L_0x00f9:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r14.mHelper     // Catch:{ all -> 0x010a }
            r0.closeWritableDatabase(r2)     // Catch:{ all -> 0x010a }
            throw r15     // Catch:{ all -> 0x010a }
        L_0x00ff:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x0102 }
        L_0x0102:
            r2.endTransaction()     // Catch:{ Exception -> 0x0105 }
        L_0x0105:
            com.alibaba.analytics.core.db.SqliteHelper r15 = r14.mHelper     // Catch:{ all -> 0x010a }
            goto L_0x00ee
        L_0x0108:
            monitor-exit(r14)
            return
        L_0x010a:
            r15 = move-exception
            monitor-exit(r14)
            throw r15
        L_0x010d:
            monitor-exit(r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.insert(java.util.List):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(16:13|14|(6:17|(1:19)|20|(4:22|(1:24)(1:25)|26|68)(1:69)|27|15)|67|28|(2:30|(1:32)(1:33))|34|35|36|37|38|39|40|52|53|54) */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01cf, code lost:
        return 0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x0197 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:38:0x019a */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x01b3 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:50:0x01b6 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x01c2 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x01c5 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:59:0x01c5=Splitter:B:59:0x01c5, B:50:0x01b6=Splitter:B:50:0x01b6, B:38:0x019a=Splitter:B:38:0x019a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r17) {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            monitor-enter(r16)
            r3 = 0
            if (r2 == 0) goto L_0x01ce
            int r0 = r17.size()     // Catch:{ all -> 0x01cb }
            if (r0 != 0) goto L_0x0010
            goto L_0x01ce
        L_0x0010:
            java.lang.Object r0 = r2.get(r3)     // Catch:{ all -> 0x01cb }
            com.alibaba.analytics.core.db.Entity r0 = (com.alibaba.analytics.core.db.Entity) r0     // Catch:{ all -> 0x01cb }
            java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x01cb }
            java.lang.String r0 = r1.getTablename(r0)     // Catch:{ all -> 0x01cb }
            java.lang.Object r4 = r2.get(r3)     // Catch:{ all -> 0x01cb }
            com.alibaba.analytics.core.db.Entity r4 = (com.alibaba.analytics.core.db.Entity) r4     // Catch:{ all -> 0x01cb }
            java.lang.Class r4 = r4.getClass()     // Catch:{ all -> 0x01cb }
            android.database.sqlite.SQLiteDatabase r4 = r1.checkTableAvailable(r4, r0)     // Catch:{ all -> 0x01cb }
            r5 = 2
            r6 = 1
            if (r4 != 0) goto L_0x003f
            java.lang.String r2 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r5]     // Catch:{ all -> 0x01cb }
            java.lang.String r5 = "[delete] db is null. tableName"
            r4[r3] = r5     // Catch:{ all -> 0x01cb }
            r4[r6] = r0     // Catch:{ all -> 0x01cb }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ all -> 0x01cb }
            monitor-exit(r16)
            return r3
        L_0x003f:
            r4.beginTransaction()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ Throwable -> 0x01a2 }
            r7.<init>()     // Catch:{ Throwable -> 0x01a2 }
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ Throwable -> 0x01a2 }
            r8.<init>()     // Catch:{ Throwable -> 0x01a2 }
            r9 = 0
        L_0x004d:
            int r10 = r17.size()     // Catch:{ Throwable -> 0x01a2 }
            r11 = 8
            if (r9 >= r10) goto L_0x010c
            int r10 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            if (r10 <= 0) goto L_0x0060
            java.lang.String r10 = " OR "
            r7.append(r10)     // Catch:{ Throwable -> 0x01a2 }
        L_0x0060:
            java.lang.String r10 = "_id=?"
            r7.append(r10)     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Object r10 = r2.get(r9)     // Catch:{ Throwable -> 0x01a2 }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ Throwable -> 0x01a2 }
            long r12 = r10._id     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r10 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x01a2 }
            r8.add(r10)     // Catch:{ Throwable -> 0x01a2 }
            int r10 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            r12 = 20
            if (r10 != r12) goto L_0x0108
            int r10 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String[] r10 = new java.lang.String[r10]     // Catch:{ Throwable -> 0x01a2 }
            r8.toArray(r10)     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = r7.toString()     // Catch:{ Throwable -> 0x01a2 }
            int r10 = r4.delete(r0, r12, r10)     // Catch:{ Throwable -> 0x01a2 }
            long r12 = (long) r10     // Catch:{ Throwable -> 0x01a2 }
            int r10 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            long r14 = (long) r10     // Catch:{ Throwable -> 0x01a2 }
            int r10 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r10 != 0) goto L_0x00cb
            java.lang.String r10 = "DBMgr"
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "delete success. DbName"
            r11[r3] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = r1.mDbName     // Catch:{ Throwable -> 0x01a2 }
            r11[r6] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "tableName"
            r11[r5] = r14     // Catch:{ Throwable -> 0x01a2 }
            r14 = 3
            r11[r14] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "whereArgs size"
            r15 = 4
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            int r14 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ Throwable -> 0x01a2 }
            r15 = 5
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "ret size"
            r15 = 6
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Long r12 = java.lang.Long.valueOf(r12)     // Catch:{ Throwable -> 0x01a2 }
            r13 = 7
            r11[r13] = r12     // Catch:{ Throwable -> 0x01a2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ Throwable -> 0x01a2 }
            goto L_0x00fe
        L_0x00cb:
            java.lang.String r10 = "DBMgr"
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "delete fail. DbName"
            r11[r3] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = r1.mDbName     // Catch:{ Throwable -> 0x01a2 }
            r11[r6] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "tableName"
            r11[r5] = r14     // Catch:{ Throwable -> 0x01a2 }
            r14 = 3
            r11[r14] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "whereArgs size"
            r15 = 4
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            int r14 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ Throwable -> 0x01a2 }
            r15 = 5
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r14 = "ret size"
            r15 = 6
            r11[r15] = r14     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Long r12 = java.lang.Long.valueOf(r12)     // Catch:{ Throwable -> 0x01a2 }
            r13 = 7
            r11[r13] = r12     // Catch:{ Throwable -> 0x01a2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r10, (java.lang.Object[]) r11)     // Catch:{ Throwable -> 0x01a2 }
        L_0x00fe:
            int r10 = r7.length()     // Catch:{ Throwable -> 0x01a2 }
            r7.delete(r3, r10)     // Catch:{ Throwable -> 0x01a2 }
            r8.clear()     // Catch:{ Throwable -> 0x01a2 }
        L_0x0108:
            int r9 = r9 + 1
            goto L_0x004d
        L_0x010c:
            int r9 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            if (r9 <= 0) goto L_0x0194
            int r9 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String[] r9 = new java.lang.String[r9]     // Catch:{ Throwable -> 0x01a2 }
            r8.toArray(r9)     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x01a2 }
            int r7 = r4.delete(r0, r7, r9)     // Catch:{ Throwable -> 0x01a2 }
            long r9 = (long) r7     // Catch:{ Throwable -> 0x01a2 }
            int r7 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            long r12 = (long) r7     // Catch:{ Throwable -> 0x01a2 }
            int r7 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1))
            if (r7 != 0) goto L_0x0161
            java.lang.String r7 = "DBMgr"
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = "delete success. DbName"
            r11[r3] = r12     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = r1.mDbName     // Catch:{ Throwable -> 0x01a2 }
            r11[r6] = r12     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = "tableName"
            r11[r5] = r12     // Catch:{ Throwable -> 0x01a2 }
            r12 = 3
            r11[r12] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r0 = "whereArgs size"
            r12 = 4
            r11[r12] = r0     // Catch:{ Throwable -> 0x01a2 }
            int r0 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x01a2 }
            r8 = 5
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r0 = "ret size"
            r8 = 6
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Long r0 = java.lang.Long.valueOf(r9)     // Catch:{ Throwable -> 0x01a2 }
            r8 = 7
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r7, (java.lang.Object[]) r11)     // Catch:{ Throwable -> 0x01a2 }
            goto L_0x0194
        L_0x0161:
            java.lang.String r7 = "DBMgr"
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = "delete fail. DbName"
            r11[r3] = r12     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = r1.mDbName     // Catch:{ Throwable -> 0x01a2 }
            r11[r6] = r12     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r12 = "tableName"
            r11[r5] = r12     // Catch:{ Throwable -> 0x01a2 }
            r12 = 3
            r11[r12] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r0 = "whereArgs size"
            r12 = 4
            r11[r12] = r0     // Catch:{ Throwable -> 0x01a2 }
            int r0 = r8.size()     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x01a2 }
            r8 = 5
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.String r0 = "ret size"
            r8 = 6
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            java.lang.Long r0 = java.lang.Long.valueOf(r9)     // Catch:{ Throwable -> 0x01a2 }
            r8 = 7
            r11[r8] = r0     // Catch:{ Throwable -> 0x01a2 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r7, (java.lang.Object[]) r11)     // Catch:{ Throwable -> 0x01a2 }
        L_0x0194:
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x0197 }
        L_0x0197:
            r4.endTransaction()     // Catch:{ Throwable -> 0x019a }
        L_0x019a:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x01cb }
        L_0x019c:
            r0.closeWritableDatabase(r4)     // Catch:{ all -> 0x01cb }
            goto L_0x01b9
        L_0x01a0:
            r0 = move-exception
            goto L_0x01bf
        L_0x01a2:
            r0 = move-exception
            java.lang.String r7 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x01a0 }
            java.lang.String r8 = "db delete error"
            r5[r3] = r8     // Catch:{ all -> 0x01a0 }
            r5[r6] = r0     // Catch:{ all -> 0x01a0 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r7, (java.lang.Object[]) r5)     // Catch:{ all -> 0x01a0 }
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x01b3 }
        L_0x01b3:
            r4.endTransaction()     // Catch:{ Throwable -> 0x01b6 }
        L_0x01b6:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x01cb }
            goto L_0x019c
        L_0x01b9:
            int r0 = r17.size()     // Catch:{ all -> 0x01cb }
            monitor-exit(r16)
            return r0
        L_0x01bf:
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x01c2 }
        L_0x01c2:
            r4.endTransaction()     // Catch:{ Throwable -> 0x01c5 }
        L_0x01c5:
            com.alibaba.analytics.core.db.SqliteHelper r2 = r1.mHelper     // Catch:{ all -> 0x01cb }
            r2.closeWritableDatabase(r4)     // Catch:{ all -> 0x01cb }
            throw r0     // Catch:{ all -> 0x01cb }
        L_0x01cb:
            r0 = move-exception
            monitor-exit(r16)
            throw r0
        L_0x01ce:
            monitor-exit(r16)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.delete(java.util.List):int");
    }

    public int delete(Entity entity) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(entity);
        return delete((List<? extends Entity>) arrayList);
    }

    public void update(Entity entity) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(entity);
        update((List<? extends Entity>) arrayList);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:11|12|(6:15|(6:18|19|20|57|24|16)|55|25|26|13)|27|28|29|30|31|32|33|48|49) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:37|38|39|40|41) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:42|43|44|45|46|47) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:11|12|(6:15|(6:18|19|20|57|24|16)|55|25|26|13)|27|28|29|30|31|32) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0089, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r8.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c1, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r2.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r2.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r12.mHelper.closeWritableDatabase(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00cd, code lost:
        throw r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00dd, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00b8 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00bb */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00c5 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00c8 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00ce */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x00d1 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:46:0x00d4 */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c1 A[ExcHandler: all (r13v3 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:11:0x003b] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:46:0x00d4=Splitter:B:46:0x00d4, B:39:0x00c8=Splitter:B:39:0x00c8, B:31:0x00bb=Splitter:B:31:0x00bb} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void update(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r13) {
        /*
            r12 = this;
            monitor-enter(r12)
            if (r13 == 0) goto L_0x00dc
            int r0 = r13.size()     // Catch:{ all -> 0x00d9 }
            if (r0 != 0) goto L_0x000b
            goto L_0x00dc
        L_0x000b:
            r0 = 0
            java.lang.Object r1 = r13.get(r0)     // Catch:{ all -> 0x00d9 }
            com.alibaba.analytics.core.db.Entity r1 = (com.alibaba.analytics.core.db.Entity) r1     // Catch:{ all -> 0x00d9 }
            java.lang.Class r1 = r1.getClass()     // Catch:{ all -> 0x00d9 }
            java.lang.String r1 = r12.getTablename(r1)     // Catch:{ all -> 0x00d9 }
            java.lang.Object r2 = r13.get(r0)     // Catch:{ all -> 0x00d9 }
            com.alibaba.analytics.core.db.Entity r2 = (com.alibaba.analytics.core.db.Entity) r2     // Catch:{ all -> 0x00d9 }
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x00d9 }
            android.database.sqlite.SQLiteDatabase r2 = r12.checkTableAvailable(r2, r1)     // Catch:{ all -> 0x00d9 }
            r3 = 1
            if (r2 != 0) goto L_0x003b
            java.lang.String r13 = "DBMgr"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x00d9 }
            java.lang.String r4 = "[update] db is null. tableName"
            r2[r0] = r4     // Catch:{ all -> 0x00d9 }
            r2[r3] = r1     // Catch:{ all -> 0x00d9 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r13, (java.lang.Object[]) r2)     // Catch:{ all -> 0x00d9 }
            monitor-exit(r12)
            return
        L_0x003b:
            r2.beginTransaction()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.Object r4 = r13.get(r0)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            com.alibaba.analytics.core.db.Entity r4 = (com.alibaba.analytics.core.db.Entity) r4     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.Class r4 = r4.getClass()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.util.List r4 = r12.getAllFields(r4)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r5 = 0
        L_0x004d:
            int r6 = r13.size()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            if (r5 >= r6) goto L_0x00b5
            android.content.ContentValues r6 = new android.content.ContentValues     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r6.<init>()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r7 = 0
        L_0x0059:
            int r8 = r4.size()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            if (r7 >= r8) goto L_0x0090
            java.lang.Object r8 = r4.get(r7)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r8.setAccessible(r3)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.String r9 = r12.getColumnName(r8)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            r10.<init>()     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.Object r11 = r13.get(r5)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.Object r8 = r8.get(r11)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            r10.append(r8)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.String r8 = ""
            r10.append(r8)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            java.lang.String r8 = r10.toString()     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            r6.put(r9, r8)     // Catch:{ Exception -> 0x0089, all -> 0x00c1 }
            goto L_0x008d
        L_0x0089:
            r8 = move-exception
            r8.printStackTrace()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
        L_0x008d:
            int r7 = r7 + 1
            goto L_0x0059
        L_0x0090:
            java.lang.String r7 = "_id=?"
            java.lang.String[] r8 = new java.lang.String[r3]     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r9.<init>()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.Object r10 = r13.get(r5)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            com.alibaba.analytics.core.db.Entity r10 = (com.alibaba.analytics.core.db.Entity) r10     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            long r10 = r10._id     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r9.append(r10)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.String r10 = ""
            r9.append(r10)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r8[r0] = r9     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            r2.update(r1, r6, r7, r8)     // Catch:{ Exception -> 0x00ce, all -> 0x00c1 }
            int r5 = r5 + 1
            goto L_0x004d
        L_0x00b5:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00b8 }
        L_0x00b8:
            r2.endTransaction()     // Catch:{ Exception -> 0x00bb }
        L_0x00bb:
            com.alibaba.analytics.core.db.SqliteHelper r13 = r12.mHelper     // Catch:{ all -> 0x00d9 }
        L_0x00bd:
            r13.closeWritableDatabase(r2)     // Catch:{ all -> 0x00d9 }
            goto L_0x00d7
        L_0x00c1:
            r13 = move-exception
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00c5 }
        L_0x00c5:
            r2.endTransaction()     // Catch:{ Exception -> 0x00c8 }
        L_0x00c8:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r12.mHelper     // Catch:{ all -> 0x00d9 }
            r0.closeWritableDatabase(r2)     // Catch:{ all -> 0x00d9 }
            throw r13     // Catch:{ all -> 0x00d9 }
        L_0x00ce:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00d1 }
        L_0x00d1:
            r2.endTransaction()     // Catch:{ Exception -> 0x00d4 }
        L_0x00d4:
            com.alibaba.analytics.core.db.SqliteHelper r13 = r12.mHelper     // Catch:{ all -> 0x00d9 }
            goto L_0x00bd
        L_0x00d7:
            monitor-exit(r12)
            return
        L_0x00d9:
            r13 = move-exception
            monitor-exit(r12)
            throw r13
        L_0x00dc:
            monitor-exit(r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.update(java.util.List):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:11|12|(5:15|(4:18|(3:22|23|58)|27|16)|57|28|13)|29|30|31|32|33|34|35|50|51) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:39|40|41|42|43) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:44|45|46|47|48|49) */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b5, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r8.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00cb, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r2.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r2.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r13.mHelper.closeWritableDatabase(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d7, code lost:
        throw r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00e7, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00c2 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c5 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00cf */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00d2 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x00d8 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:46:0x00db */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x00de */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00cb A[ExcHandler: all (r14v3 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:11:0x003b] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:41:0x00d2=Splitter:B:41:0x00d2, B:33:0x00c5=Splitter:B:33:0x00c5, B:48:0x00de=Splitter:B:48:0x00de} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateLogPriority(java.util.List<? extends com.alibaba.analytics.core.db.Entity> r14) {
        /*
            r13 = this;
            monitor-enter(r13)
            if (r14 == 0) goto L_0x00e6
            int r0 = r14.size()     // Catch:{ all -> 0x00e3 }
            if (r0 != 0) goto L_0x000b
            goto L_0x00e6
        L_0x000b:
            r0 = 0
            java.lang.Object r1 = r14.get(r0)     // Catch:{ all -> 0x00e3 }
            com.alibaba.analytics.core.db.Entity r1 = (com.alibaba.analytics.core.db.Entity) r1     // Catch:{ all -> 0x00e3 }
            java.lang.Class r1 = r1.getClass()     // Catch:{ all -> 0x00e3 }
            java.lang.String r1 = r13.getTablename(r1)     // Catch:{ all -> 0x00e3 }
            java.lang.Object r2 = r14.get(r0)     // Catch:{ all -> 0x00e3 }
            com.alibaba.analytics.core.db.Entity r2 = (com.alibaba.analytics.core.db.Entity) r2     // Catch:{ all -> 0x00e3 }
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x00e3 }
            android.database.sqlite.SQLiteDatabase r2 = r13.checkTableAvailable(r2, r1)     // Catch:{ all -> 0x00e3 }
            r3 = 1
            if (r2 != 0) goto L_0x003b
            java.lang.String r14 = "DBMgr"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x00e3 }
            java.lang.String r4 = "[updateLogPriority] db is null. tableName"
            r2[r0] = r4     // Catch:{ all -> 0x00e3 }
            r2[r3] = r1     // Catch:{ all -> 0x00e3 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r14, (java.lang.Object[]) r2)     // Catch:{ all -> 0x00e3 }
            monitor-exit(r13)
            return
        L_0x003b:
            r2.beginTransaction()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            java.lang.Object r4 = r14.get(r0)     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            com.alibaba.analytics.core.db.Entity r4 = (com.alibaba.analytics.core.db.Entity) r4     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            java.lang.Class r4 = r4.getClass()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            java.util.List r4 = r13.getAllFields(r4)     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            r5 = 0
        L_0x004d:
            int r6 = r14.size()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            if (r5 >= r6) goto L_0x00bf
            android.content.ContentValues r6 = new android.content.ContentValues     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            r6.<init>()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            r7 = 0
        L_0x0059:
            int r8 = r4.size()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            if (r7 >= r8) goto L_0x00bc
            java.lang.Object r8 = r4.get(r7)     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            java.lang.String r9 = r13.getColumnName(r8)     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            if (r9 == 0) goto L_0x00b9
            java.lang.String r10 = "priority"
            boolean r10 = r9.equalsIgnoreCase(r10)     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
            if (r10 == 0) goto L_0x00b9
            r8.setAccessible(r3)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r10.<init>()     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.Object r11 = r14.get(r5)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.Object r8 = r8.get(r11)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r10.append(r8)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.String r8 = ""
            r10.append(r8)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.String r8 = r10.toString()     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r6.put(r9, r8)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.String r8 = "_id=?"
            java.lang.String[] r9 = new java.lang.String[r3]     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r10.<init>()     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.Object r11 = r14.get(r5)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            com.alibaba.analytics.core.db.Entity r11 = (com.alibaba.analytics.core.db.Entity) r11     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            long r11 = r11._id     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r10.append(r11)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.String r11 = ""
            r10.append(r11)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r9[r0] = r10     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            r2.update(r1, r6, r8, r9)     // Catch:{ Exception -> 0x00b5, all -> 0x00cb }
            goto L_0x00bc
        L_0x00b5:
            r8 = move-exception
            r8.printStackTrace()     // Catch:{ Exception -> 0x00d8, all -> 0x00cb }
        L_0x00b9:
            int r7 = r7 + 1
            goto L_0x0059
        L_0x00bc:
            int r5 = r5 + 1
            goto L_0x004d
        L_0x00bf:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00c2 }
        L_0x00c2:
            r2.endTransaction()     // Catch:{ Exception -> 0x00c5 }
        L_0x00c5:
            com.alibaba.analytics.core.db.SqliteHelper r14 = r13.mHelper     // Catch:{ all -> 0x00e3 }
        L_0x00c7:
            r14.closeWritableDatabase(r2)     // Catch:{ all -> 0x00e3 }
            goto L_0x00e1
        L_0x00cb:
            r14 = move-exception
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00cf }
        L_0x00cf:
            r2.endTransaction()     // Catch:{ Exception -> 0x00d2 }
        L_0x00d2:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r13.mHelper     // Catch:{ all -> 0x00e3 }
            r0.closeWritableDatabase(r2)     // Catch:{ all -> 0x00e3 }
            throw r14     // Catch:{ all -> 0x00e3 }
        L_0x00d8:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00db }
        L_0x00db:
            r2.endTransaction()     // Catch:{ Exception -> 0x00de }
        L_0x00de:
            com.alibaba.analytics.core.db.SqliteHelper r14 = r13.mHelper     // Catch:{ all -> 0x00e3 }
            goto L_0x00c7
        L_0x00e1:
            monitor-exit(r13)
            return
        L_0x00e3:
            r14 = move-exception
            monitor-exit(r13)
            throw r14
        L_0x00e6:
            monitor-exit(r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.updateLogPriority(java.util.List):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0036, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void execSQL(java.lang.Class<? extends com.alibaba.analytics.core.db.Entity> r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x0035
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x0032 }
            if (r0 != 0) goto L_0x0035
            java.lang.String r0 = r2.getTablename(r3)     // Catch:{ all -> 0x0032 }
            android.database.sqlite.SQLiteDatabase r3 = r2.checkTableAvailable(r3, r0)     // Catch:{ all -> 0x0032 }
            if (r3 != 0) goto L_0x0015
            monitor-exit(r2)
            return
        L_0x0015:
            r3.execSQL(r4)     // Catch:{ Throwable -> 0x0020 }
            com.alibaba.analytics.core.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0032 }
        L_0x001a:
            r4.closeWritableDatabase(r3)     // Catch:{ all -> 0x0032 }
            goto L_0x0035
        L_0x001e:
            r4 = move-exception
            goto L_0x002c
        L_0x0020:
            r4 = move-exception
            java.lang.String r0 = "DBMgr"
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x001e }
            com.alibaba.analytics.utils.Logger.w(r0, r4, r1)     // Catch:{ all -> 0x001e }
            com.alibaba.analytics.core.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0032 }
            goto L_0x001a
        L_0x002c:
            com.alibaba.analytics.core.db.SqliteHelper r0 = r2.mHelper     // Catch:{ all -> 0x0032 }
            r0.closeWritableDatabase(r3)     // Catch:{ all -> 0x0032 }
            throw r4     // Catch:{ all -> 0x0032 }
        L_0x0032:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0035:
            monitor-exit(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.execSQL(java.lang.Class, java.lang.String):void");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.lang.Class<? extends com.alibaba.analytics.core.db.Entity> r3, java.lang.String r4, java.lang.String[] r5) {
        /*
            r2 = this;
            monitor-enter(r2)
            r0 = 0
            if (r3 == 0) goto L_0x0037
            java.lang.String r1 = r2.getTablename(r3)     // Catch:{ all -> 0x0034 }
            android.database.sqlite.SQLiteDatabase r1 = r2.checkTableAvailable(r3, r1)     // Catch:{ all -> 0x0034 }
            if (r1 != 0) goto L_0x0010
            monitor-exit(r2)
            return r0
        L_0x0010:
            java.lang.String r3 = r2.getTablename(r3)     // Catch:{ Throwable -> 0x0020 }
            int r3 = r1.delete(r3, r4, r5)     // Catch:{ Throwable -> 0x0020 }
            com.alibaba.analytics.core.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0034 }
            r4.closeWritableDatabase(r1)     // Catch:{ all -> 0x0034 }
            goto L_0x0038
        L_0x001e:
            r3 = move-exception
            goto L_0x002e
        L_0x0020:
            r3 = move-exception
            java.lang.String r4 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r0]     // Catch:{ all -> 0x001e }
            com.alibaba.analytics.utils.Logger.w(r4, r3, r5)     // Catch:{ all -> 0x001e }
            com.alibaba.analytics.core.db.SqliteHelper r3 = r2.mHelper     // Catch:{ all -> 0x0034 }
            r3.closeWritableDatabase(r1)     // Catch:{ all -> 0x0034 }
            goto L_0x0037
        L_0x002e:
            com.alibaba.analytics.core.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0034 }
            r4.closeWritableDatabase(r1)     // Catch:{ all -> 0x0034 }
            throw r3     // Catch:{ all -> 0x0034 }
        L_0x0034:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0037:
            r3 = 0
        L_0x0038:
            monitor-exit(r2)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.delete(java.lang.Class, java.lang.String, java.lang.String[]):int");
    }

    public String getTablename(Class<?> cls) {
        String str;
        if (cls == null) {
            Logger.e(TAG, "cls is null");
            return null;
        } else if (this.mClsTableNameMap.containsKey(cls)) {
            return this.mClsTableNameMap.get(cls);
        } else {
            TableName tableName = (TableName) cls.getAnnotation(TableName.class);
            if (tableName == null || TextUtils.isEmpty(tableName.value())) {
                str = cls.getName().replace(".", "_");
            } else {
                str = tableName.value();
            }
            this.mClsTableNameMap.put(cls, str);
            return str;
        }
    }

    private SQLiteDatabase checkTableAvailable(Class<? extends Entity> cls, String str) {
        Cursor cursor;
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        Boolean bool = true;
        int i = 0;
        if (this.mCheckdbMap.get(str) == null || !this.mCheckdbMap.get(str).booleanValue()) {
            bool = false;
        }
        if (!(cls == null || bool.booleanValue() || writableDatabase == null)) {
            List<Field> allFields = getAllFields(cls);
            ArrayList arrayList = new ArrayList();
            String str2 = " SELECT * FROM " + str + " LIMIT 0";
            if (allFields != null) {
                try {
                    cursor = writableDatabase.rawQuery(str2, (String[]) null);
                } catch (Exception unused) {
                    Logger.d(TAG, "has not create table", str);
                    cursor = null;
                }
                int i2 = cursor == null ? 1 : 0;
                while (i < allFields.size()) {
                    Field field = allFields.get(i);
                    if (!"_id".equalsIgnoreCase(getColumnName(field)) && (i2 != 0 || (cursor != null && cursor.getColumnIndex(getColumnName(field)) == -1))) {
                        arrayList.add(field);
                    }
                    i++;
                }
                this.mHelper.closeCursor(cursor);
                i = i2;
            }
            if (i != 0) {
                createTable(writableDatabase, str, arrayList);
            } else if (arrayList.size() > 0) {
                updateTable(writableDatabase, str, arrayList);
            }
            this.mCheckdbMap.put(str, true);
        }
        return writableDatabase;
    }

    private void updateTable(SQLiteDatabase sQLiteDatabase, String str, ArrayList<Field> arrayList) {
        String str2 = "ALTER TABLE " + str + " ADD COLUMN ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            sb.append(str2);
            sb.append(getColumnName(arrayList.get(i)));
            sb.append(Operators.SPACE_STR);
            sb.append(getSQLType(arrayList.get(i).getType()));
            String sb2 = sb.toString();
            try {
                sQLiteDatabase.execSQL(sb2);
            } catch (Exception e) {
                Logger.w(TAG, "update db error...", e);
            }
            sb.delete(0, sb2.length());
            Logger.d(TAG, null, "excute sql:", sb2);
        }
    }

    private void createTable(SQLiteDatabase sQLiteDatabase, String str, ArrayList<Field> arrayList) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + str + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT ,");
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                Class<?> type = arrayList.get(i).getType();
                sb.append(Operators.SPACE_STR);
                sb.append(getColumnName(arrayList.get(i)));
                sb.append(Operators.SPACE_STR);
                sb.append(getSQLType(type));
                sb.append(Operators.SPACE_STR);
                sb.append(getDefaultValue(type));
            }
        }
        sb.append(" );");
        String sb2 = sb.toString();
        Logger.d(TAG, "excute sql:", sb2);
        try {
            sQLiteDatabase.execSQL(sb2);
        } catch (Exception e) {
            Logger.w(TAG, "create db error", e);
        }
    }

    private String getSQLType(Class<?> cls) {
        return (cls == Long.TYPE || cls == Integer.TYPE || cls == Long.class) ? "INTEGER" : "TEXT";
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x004e A[Catch:{ all -> 0x0045 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:20:0x003a=Splitter:B:20:0x003a, B:29:0x005b=Splitter:B:29:0x005b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int count(java.lang.Class<? extends com.alibaba.analytics.core.db.Entity> r8) {
        /*
            r7 = this;
            monitor-enter(r7)
            r0 = 0
            if (r8 != 0) goto L_0x0006
            monitor-exit(r7)
            return r0
        L_0x0006:
            java.lang.String r1 = r7.getTablename(r8)     // Catch:{ all -> 0x007e }
            android.database.sqlite.SQLiteDatabase r8 = r7.checkTableAvailable(r8, r1)     // Catch:{ all -> 0x007e }
            r2 = 1
            if (r8 == 0) goto L_0x006e
            r3 = 0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0047 }
            r4.<init>()     // Catch:{ Throwable -> 0x0047 }
            java.lang.String r5 = "SELECT count(*) FROM "
            r4.append(r5)     // Catch:{ Throwable -> 0x0047 }
            r4.append(r1)     // Catch:{ Throwable -> 0x0047 }
            java.lang.String r1 = r4.toString()     // Catch:{ Throwable -> 0x0047 }
            android.database.Cursor r1 = r8.rawQuery(r1, r3)     // Catch:{ Throwable -> 0x0047 }
            if (r1 == 0) goto L_0x003a
            r1.moveToFirst()     // Catch:{ Throwable -> 0x0035, all -> 0x0032 }
            int r3 = r1.getInt(r0)     // Catch:{ Throwable -> 0x0035, all -> 0x0032 }
            r0 = r3
            goto L_0x003a
        L_0x0032:
            r0 = move-exception
            r3 = r1
            goto L_0x0063
        L_0x0035:
            r3 = move-exception
            r6 = r3
            r3 = r1
            r1 = r6
            goto L_0x0048
        L_0x003a:
            com.alibaba.analytics.core.db.SqliteHelper r2 = r7.mHelper     // Catch:{ all -> 0x007e }
            r2.closeCursor(r1)     // Catch:{ all -> 0x007e }
            com.alibaba.analytics.core.db.SqliteHelper r1 = r7.mHelper     // Catch:{ all -> 0x007e }
        L_0x0041:
            r1.closeWritableDatabase(r8)     // Catch:{ all -> 0x007e }
            goto L_0x007c
        L_0x0045:
            r0 = move-exception
            goto L_0x0063
        L_0x0047:
            r1 = move-exception
        L_0x0048:
            boolean r4 = com.alibaba.analytics.utils.Logger.isDebug()     // Catch:{ all -> 0x0045 }
            if (r4 == 0) goto L_0x005b
            java.lang.String r4 = "DBMgr"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0045 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0045 }
            r2[r0] = r1     // Catch:{ all -> 0x0045 }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r4, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0045 }
        L_0x005b:
            com.alibaba.analytics.core.db.SqliteHelper r1 = r7.mHelper     // Catch:{ all -> 0x007e }
            r1.closeCursor(r3)     // Catch:{ all -> 0x007e }
            com.alibaba.analytics.core.db.SqliteHelper r1 = r7.mHelper     // Catch:{ all -> 0x007e }
            goto L_0x0041
        L_0x0063:
            com.alibaba.analytics.core.db.SqliteHelper r1 = r7.mHelper     // Catch:{ all -> 0x007e }
            r1.closeCursor(r3)     // Catch:{ all -> 0x007e }
            com.alibaba.analytics.core.db.SqliteHelper r1 = r7.mHelper     // Catch:{ all -> 0x007e }
            r1.closeWritableDatabase(r8)     // Catch:{ all -> 0x007e }
            throw r0     // Catch:{ all -> 0x007e }
        L_0x006e:
            java.lang.String r8 = "DBMgr"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x007e }
            java.lang.String r4 = "[count] db is null. tableName"
            r3[r0] = r4     // Catch:{ all -> 0x007e }
            r3[r2] = r1     // Catch:{ all -> 0x007e }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r8, (java.lang.Object[]) r3)     // Catch:{ all -> 0x007e }
        L_0x007c:
            monitor-exit(r7)
            return r0
        L_0x007e:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.DBMgr.count(java.lang.Class):int");
    }

    public synchronized void clear(Class<? extends Entity> cls) {
        if (cls != null) {
            clear(getTablename(cls));
        }
    }

    public synchronized void clear(String str) {
        if (str != null) {
            try {
                SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                if (writableDatabase != null) {
                    writableDatabase.delete(str, (String) null, (String[]) null);
                    this.mHelper.closeWritableDatabase(writableDatabase);
                }
            } catch (Exception e) {
                Logger.e("delete db data", e, new Object[0]);
            }
        } else {
            return;
        }
        return;
    }

    private List<Field> getAllFields(Class cls) {
        if (this.mClsFieldsMap.containsKey(cls)) {
            return this.mClsFieldsMap.get(cls);
        }
        List<Field> emptyList = Collections.emptyList();
        if (cls != null) {
            emptyList = new ArrayList<>();
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getAnnotation(Ingore.class) == null && !field.isSynthetic()) {
                    field.setAccessible(true);
                    emptyList.add(field);
                }
            }
            if (!(cls.getSuperclass() == null || cls.getSuperclass() == Object.class)) {
                emptyList.addAll(getAllFields(cls.getSuperclass()));
            }
            this.mClsFieldsMap.put(cls, emptyList);
        }
        return emptyList;
    }

    private String getColumnName(Field field) {
        String str;
        if (this.mFieldNameMap.containsKey(field)) {
            return this.mFieldNameMap.get(field);
        }
        Column column = (Column) field.getAnnotation(Column.class);
        if (column == null || TextUtils.isEmpty(column.value())) {
            str = field.getName();
        } else {
            str = column.value();
        }
        this.mFieldNameMap.put(field, str);
        return str;
    }

    private String getDefaultValue(Class cls) {
        return (cls == Long.TYPE || cls == Integer.TYPE || cls == Long.class) ? "default 0" : "default \"\"";
    }

    public double getDbFileSize() {
        File databasePath = Variables.getInstance().getContext().getDatabasePath(Constants.Database.DATABASE_NAME);
        if (databasePath == null) {
            return 0.0d;
        }
        double length = (double) databasePath.length();
        Double.isNaN(length);
        return (length / 1024.0d) / 1024.0d;
    }
}
