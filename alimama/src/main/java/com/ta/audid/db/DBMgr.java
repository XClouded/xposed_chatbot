package com.ta.audid.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.ta.audid.db.annotation.Column;
import com.ta.audid.db.annotation.Ingore;
import com.ta.audid.db.annotation.TableName;
import com.ta.audid.utils.UtdidLogger;
import com.taobao.weex.el.parse.Operators;
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

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0134, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0136, code lost:
        r12 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0134 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:29:0x0093] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:68:0x0126=Splitter:B:68:0x0126, B:86:0x0157=Splitter:B:86:0x0157} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<? extends com.ta.audid.db.Entity> find(java.lang.Class<? extends com.ta.audid.db.Entity> r12, java.lang.String r13, java.lang.String r14, int r15) {
        /*
            r11 = this;
            monitor-enter(r11)
            java.util.List r0 = java.util.Collections.EMPTY_LIST     // Catch:{ all -> 0x0162 }
            if (r12 != 0) goto L_0x0007
            monitor-exit(r11)
            return r0
        L_0x0007:
            java.lang.String r1 = r11.getTablename(r12)     // Catch:{ all -> 0x0162 }
            android.database.sqlite.SQLiteDatabase r2 = r11.checkTableAvailable(r12, r1)     // Catch:{ all -> 0x0162 }
            r3 = 0
            if (r2 != 0) goto L_0x001b
            java.lang.String r12 = "db is null"
            java.lang.Object[] r13 = new java.lang.Object[r3]     // Catch:{ all -> 0x0162 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ all -> 0x0162 }
            monitor-exit(r11)
            return r0
        L_0x001b:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0162 }
            r4.<init>()     // Catch:{ all -> 0x0162 }
            java.lang.String r5 = "SELECT * FROM "
            r4.append(r5)     // Catch:{ all -> 0x0162 }
            r4.append(r1)     // Catch:{ all -> 0x0162 }
            boolean r1 = android.text.TextUtils.isEmpty(r13)     // Catch:{ all -> 0x0162 }
            if (r1 == 0) goto L_0x0031
            java.lang.String r13 = ""
            goto L_0x0042
        L_0x0031:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0162 }
            r1.<init>()     // Catch:{ all -> 0x0162 }
            java.lang.String r5 = " WHERE "
            r1.append(r5)     // Catch:{ all -> 0x0162 }
            r1.append(r13)     // Catch:{ all -> 0x0162 }
            java.lang.String r13 = r1.toString()     // Catch:{ all -> 0x0162 }
        L_0x0042:
            r4.append(r13)     // Catch:{ all -> 0x0162 }
            boolean r13 = android.text.TextUtils.isEmpty(r14)     // Catch:{ all -> 0x0162 }
            if (r13 == 0) goto L_0x004e
            java.lang.String r13 = ""
            goto L_0x005f
        L_0x004e:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0162 }
            r13.<init>()     // Catch:{ all -> 0x0162 }
            java.lang.String r1 = " ORDER BY "
            r13.append(r1)     // Catch:{ all -> 0x0162 }
            r13.append(r14)     // Catch:{ all -> 0x0162 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0162 }
        L_0x005f:
            r4.append(r13)     // Catch:{ all -> 0x0162 }
            if (r15 > 0) goto L_0x0067
            java.lang.String r13 = ""
            goto L_0x0078
        L_0x0067:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0162 }
            r13.<init>()     // Catch:{ all -> 0x0162 }
            java.lang.String r14 = " LIMIT "
            r13.append(r14)     // Catch:{ all -> 0x0162 }
            r13.append(r15)     // Catch:{ all -> 0x0162 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0162 }
        L_0x0078:
            r4.append(r13)     // Catch:{ all -> 0x0162 }
            java.lang.String r13 = r4.toString()     // Catch:{ all -> 0x0162 }
            java.lang.String r14 = "DBMgr"
            r15 = 2
            java.lang.Object[] r1 = new java.lang.Object[r15]     // Catch:{ all -> 0x0162 }
            java.lang.String r4 = "sql"
            r1[r3] = r4     // Catch:{ all -> 0x0162 }
            r4 = 1
            r1[r4] = r13     // Catch:{ all -> 0x0162 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r14, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0162 }
            r14 = 0
            android.database.Cursor r13 = r2.rawQuery(r13, r14)     // Catch:{ Throwable -> 0x013c }
            java.util.ArrayList r14 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0136, all -> 0x0134 }
            r14.<init>()     // Catch:{ Throwable -> 0x0136, all -> 0x0134 }
            java.util.List r0 = r11.getAllFields(r12)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
        L_0x009c:
            if (r13 == 0) goto L_0x0126
            boolean r1 = r13.moveToNext()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            if (r1 == 0) goto L_0x0126
            java.lang.Object r1 = r12.newInstance()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            com.ta.audid.db.Entity r1 = (com.ta.audid.db.Entity) r1     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            r5 = 0
        L_0x00ab:
            int r6 = r0.size()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            if (r5 >= r6) goto L_0x0121
            java.lang.Object r6 = r0.get(r5)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.reflect.Field r6 = (java.lang.reflect.Field) r6     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.Class r7 = r6.getType()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.String r8 = r11.getColumnName(r6)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            int r9 = r13.getColumnIndex(r8)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            r10 = -1
            if (r9 == r10) goto L_0x0104
            java.lang.Class<java.lang.Long> r8 = java.lang.Long.class
            if (r7 == r8) goto L_0x00f8
            java.lang.Class r8 = java.lang.Long.TYPE     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            if (r7 != r8) goto L_0x00cf
            goto L_0x00f8
        L_0x00cf:
            java.lang.Class<java.lang.Integer> r8 = java.lang.Integer.class
            if (r7 == r8) goto L_0x00ef
            java.lang.Class r8 = java.lang.Integer.TYPE     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            if (r7 != r8) goto L_0x00d8
            goto L_0x00ef
        L_0x00d8:
            java.lang.Class r8 = java.lang.Double.TYPE     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            if (r7 == r8) goto L_0x00e6
            java.lang.Class<java.lang.Double> r8 = java.lang.Double.class
            if (r7 != r8) goto L_0x00e1
            goto L_0x00e6
        L_0x00e1:
            java.lang.String r7 = r13.getString(r9)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            goto L_0x0100
        L_0x00e6:
            double r7 = r13.getDouble(r9)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.Double r7 = java.lang.Double.valueOf(r7)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            goto L_0x0100
        L_0x00ef:
            int r7 = r13.getInt(r9)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            goto L_0x0100
        L_0x00f8:
            long r7 = r13.getLong(r9)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
        L_0x0100:
            r6.set(r1, r7)     // Catch:{ Exception -> 0x011e }
            goto L_0x011e
        L_0x0104:
            java.lang.String r6 = "DBMgr"
            java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            r9.<init>()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.String r10 = "can not get field: "
            r9.append(r10)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            r9.append(r8)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            java.lang.String r8 = r9.toString()     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            r7[r3] = r8     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            com.ta.audid.utils.UtdidLogger.w(r6, r7)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
        L_0x011e:
            int r5 = r5 + 1
            goto L_0x00ab
        L_0x0121:
            r14.add(r1)     // Catch:{ Throwable -> 0x0131, all -> 0x0134 }
            goto L_0x009c
        L_0x0126:
            com.ta.audid.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r12.closeCursor(r13)     // Catch:{ all -> 0x0162 }
            com.ta.audid.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r12.closeWritableDatabase(r2)     // Catch:{ all -> 0x0162 }
            goto L_0x0155
        L_0x0131:
            r12 = move-exception
            r0 = r14
            goto L_0x0137
        L_0x0134:
            r12 = move-exception
            goto L_0x0157
        L_0x0136:
            r12 = move-exception
        L_0x0137:
            r14 = r13
            goto L_0x013d
        L_0x0139:
            r12 = move-exception
            r13 = r14
            goto L_0x0157
        L_0x013c:
            r12 = move-exception
        L_0x013d:
            java.lang.String r13 = "DBMgr"
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ all -> 0x0139 }
            java.lang.String r1 = "[get]"
            r15[r3] = r1     // Catch:{ all -> 0x0139 }
            r15[r4] = r12     // Catch:{ all -> 0x0139 }
            com.ta.audid.utils.UtdidLogger.w(r13, r15)     // Catch:{ all -> 0x0139 }
            com.ta.audid.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r12.closeCursor(r14)     // Catch:{ all -> 0x0162 }
            com.ta.audid.db.SqliteHelper r12 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r12.closeWritableDatabase(r2)     // Catch:{ all -> 0x0162 }
            r14 = r0
        L_0x0155:
            monitor-exit(r11)
            return r14
        L_0x0157:
            com.ta.audid.db.SqliteHelper r14 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r14.closeCursor(r13)     // Catch:{ all -> 0x0162 }
            com.ta.audid.db.SqliteHelper r13 = r11.mHelper     // Catch:{ all -> 0x0162 }
            r13.closeWritableDatabase(r2)     // Catch:{ all -> 0x0162 }
            throw r12     // Catch:{ all -> 0x0162 }
        L_0x0162:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.find(java.lang.Class, java.lang.String, java.lang.String, int):java.util.List");
    }

    public void insert(Entity entity) {
        if (entity != null) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(entity);
            insert((List<? extends Entity>) arrayList);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:47|48|49|50|51|52|53|54|55|56) */
    /* JADX WARNING: Can't wrap try/catch for region: R(12:12|13|(8:16|(6:19|20|21|(2:23|73)(2:24|74)|29|17)|71|30|(2:32|(1:34)(1:35))(1:36)|37|38|14)|39|40|41|42|43|44|45|57|58) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:46|59|60|61|62|63|64|65) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:49|50|51|52|53|54|55|56) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:12|13|(8:16|(6:19|20|21|(2:23|73)(2:24|74)|29|17)|71|30|(2:32|(1:34)(1:35))(1:36)|37|38|14)|39|40|41|42|43|44) */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0163, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x012f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0132 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x014b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x014e */
    /* JADX WARNING: Missing exception handler attribute for start block: B:61:0x0156 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x0159 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:43:0x0132=Splitter:B:43:0x0132, B:63:0x0159=Splitter:B:63:0x0159, B:55:0x014e=Splitter:B:55:0x014e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void insert(java.util.List<? extends com.ta.audid.db.Entity> r20) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            monitor-enter(r19)
            if (r2 == 0) goto L_0x0162
            int r0 = r20.size()     // Catch:{ all -> 0x015f }
            if (r0 != 0) goto L_0x000f
            goto L_0x0162
        L_0x000f:
            r3 = 0
            java.lang.Object r0 = r2.get(r3)     // Catch:{ all -> 0x015f }
            com.ta.audid.db.Entity r0 = (com.ta.audid.db.Entity) r0     // Catch:{ all -> 0x015f }
            java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x015f }
            java.lang.String r4 = r1.getTablename(r0)     // Catch:{ all -> 0x015f }
            java.lang.Object r0 = r2.get(r3)     // Catch:{ all -> 0x015f }
            com.ta.audid.db.Entity r0 = (com.ta.audid.db.Entity) r0     // Catch:{ all -> 0x015f }
            java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x015f }
            android.database.sqlite.SQLiteDatabase r5 = r1.checkTableAvailable(r0, r4)     // Catch:{ all -> 0x015f }
            r6 = 1
            if (r5 != 0) goto L_0x003c
            java.lang.String r0 = "DBMgr"
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch:{ all -> 0x015f }
            java.lang.String r4 = "can not get available db"
            r2[r3] = r4     // Catch:{ all -> 0x015f }
            com.ta.audid.utils.UtdidLogger.w(r0, r2)     // Catch:{ all -> 0x015f }
            monitor-exit(r19)
            return
        L_0x003c:
            java.lang.Object r0 = r2.get(r3)     // Catch:{ Throwable -> 0x013a }
            com.ta.audid.db.Entity r0 = (com.ta.audid.db.Entity) r0     // Catch:{ Throwable -> 0x013a }
            java.lang.Class r0 = r0.getClass()     // Catch:{ Throwable -> 0x013a }
            java.util.List r7 = r1.getAllFields(r0)     // Catch:{ Throwable -> 0x013a }
            android.content.ContentValues r8 = new android.content.ContentValues     // Catch:{ Throwable -> 0x013a }
            r8.<init>()     // Catch:{ Throwable -> 0x013a }
            r5.beginTransaction()     // Catch:{ Throwable -> 0x013a }
            r9 = 0
        L_0x0053:
            int r0 = r20.size()     // Catch:{ Throwable -> 0x013a }
            if (r9 >= r0) goto L_0x012c
            java.lang.Object r0 = r2.get(r9)     // Catch:{ Throwable -> 0x013a }
            r10 = r0
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ Throwable -> 0x013a }
            r11 = 0
        L_0x0061:
            int r0 = r7.size()     // Catch:{ Throwable -> 0x013a }
            r12 = 2
            if (r11 >= r0) goto L_0x00a4
            java.lang.Object r0 = r7.get(r11)     // Catch:{ Throwable -> 0x013a }
            java.lang.reflect.Field r0 = (java.lang.reflect.Field) r0     // Catch:{ Throwable -> 0x013a }
            java.lang.String r13 = r1.getColumnName(r0)     // Catch:{ Throwable -> 0x013a }
            java.lang.Object r0 = r0.get(r10)     // Catch:{ Exception -> 0x0093 }
            if (r0 == 0) goto L_0x008d
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0093 }
            r14.<init>()     // Catch:{ Exception -> 0x0093 }
            r14.append(r0)     // Catch:{ Exception -> 0x0093 }
            java.lang.String r0 = ""
            r14.append(r0)     // Catch:{ Exception -> 0x0093 }
            java.lang.String r0 = r14.toString()     // Catch:{ Exception -> 0x0093 }
            r8.put(r13, r0)     // Catch:{ Exception -> 0x0093 }
            goto L_0x00a1
        L_0x008d:
            java.lang.String r0 = ""
            r8.put(r13, r0)     // Catch:{ Exception -> 0x0093 }
            goto L_0x00a1
        L_0x0093:
            r0 = move-exception
            java.lang.String r13 = "DBMgr"
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "get field failed"
            r12[r3] = r14     // Catch:{ Throwable -> 0x013a }
            r12[r6] = r0     // Catch:{ Throwable -> 0x013a }
            com.ta.audid.utils.UtdidLogger.w(r13, r12)     // Catch:{ Throwable -> 0x013a }
        L_0x00a1:
            int r11 = r11 + 1
            goto L_0x0061
        L_0x00a4:
            long r13 = r10._id     // Catch:{ Throwable -> 0x013a }
            r15 = -1
            int r0 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r0 != 0) goto L_0x00fa
            java.lang.String r0 = "_id"
            r8.remove(r0)     // Catch:{ Throwable -> 0x013a }
            r0 = 0
            long r13 = r5.insert(r4, r0, r8)     // Catch:{ Throwable -> 0x013a }
            r11 = 4
            r17 = 3
            r0 = 6
            int r18 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r18 == 0) goto L_0x00dd
            r10._id = r13     // Catch:{ Throwable -> 0x013a }
            java.lang.String r13 = "DBMgr"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "mDbName"
            r0[r3] = r14     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = r1.mDbName     // Catch:{ Throwable -> 0x013a }
            r0[r6] = r14     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "tablename"
            r0[r12] = r14     // Catch:{ Throwable -> 0x013a }
            r0[r17] = r4     // Catch:{ Throwable -> 0x013a }
            java.lang.String r12 = "insert:success"
            r0[r11] = r12     // Catch:{ Throwable -> 0x013a }
            r11 = 5
            r0[r11] = r10     // Catch:{ Throwable -> 0x013a }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r13, (java.lang.Object[]) r0)     // Catch:{ Throwable -> 0x013a }
            goto L_0x0125
        L_0x00dd:
            java.lang.String r13 = "DBMgr"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "mDbName"
            r0[r3] = r14     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = r1.mDbName     // Catch:{ Throwable -> 0x013a }
            r0[r6] = r14     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "tablename"
            r0[r12] = r14     // Catch:{ Throwable -> 0x013a }
            r0[r17] = r4     // Catch:{ Throwable -> 0x013a }
            java.lang.String r12 = "insert:error"
            r0[r11] = r12     // Catch:{ Throwable -> 0x013a }
            r11 = 5
            r0[r11] = r10     // Catch:{ Throwable -> 0x013a }
            com.ta.audid.utils.UtdidLogger.w(r13, r0)     // Catch:{ Throwable -> 0x013a }
            goto L_0x0125
        L_0x00fa:
            java.lang.String r0 = "_id=?"
            java.lang.String[] r11 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x013a }
            long r12 = r10._id     // Catch:{ Throwable -> 0x013a }
            java.lang.String r10 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x013a }
            r11[r3] = r10     // Catch:{ Throwable -> 0x013a }
            int r0 = r5.update(r4, r8, r0, r11)     // Catch:{ Throwable -> 0x013a }
            long r10 = (long) r0     // Catch:{ Throwable -> 0x013a }
            java.lang.String r0 = "DBMgr"
            java.lang.Object[] r12 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x013a }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013a }
            r13.<init>()     // Catch:{ Throwable -> 0x013a }
            java.lang.String r14 = "db update :"
            r13.append(r14)     // Catch:{ Throwable -> 0x013a }
            r13.append(r10)     // Catch:{ Throwable -> 0x013a }
            java.lang.String r10 = r13.toString()     // Catch:{ Throwable -> 0x013a }
            r12[r3] = r10     // Catch:{ Throwable -> 0x013a }
            com.ta.audid.utils.UtdidLogger.w(r0, r12)     // Catch:{ Throwable -> 0x013a }
        L_0x0125:
            r8.clear()     // Catch:{ Throwable -> 0x013a }
            int r9 = r9 + 1
            goto L_0x0053
        L_0x012c:
            r5.setTransactionSuccessful()     // Catch:{ Exception -> 0x012f }
        L_0x012f:
            r5.endTransaction()     // Catch:{ Exception -> 0x0132 }
        L_0x0132:
            com.ta.audid.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x015f }
        L_0x0134:
            r0.closeWritableDatabase(r5)     // Catch:{ all -> 0x015f }
            goto L_0x0151
        L_0x0138:
            r0 = move-exception
            goto L_0x0153
        L_0x013a:
            r0 = move-exception
            java.lang.String r2 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r6]     // Catch:{ all -> 0x0138 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0138 }
            r4[r3] = r0     // Catch:{ all -> 0x0138 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ all -> 0x0138 }
            r5.setTransactionSuccessful()     // Catch:{ Exception -> 0x014b }
        L_0x014b:
            r5.endTransaction()     // Catch:{ Exception -> 0x014e }
        L_0x014e:
            com.ta.audid.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x015f }
            goto L_0x0134
        L_0x0151:
            monitor-exit(r19)
            return
        L_0x0153:
            r5.setTransactionSuccessful()     // Catch:{ Exception -> 0x0156 }
        L_0x0156:
            r5.endTransaction()     // Catch:{ Exception -> 0x0159 }
        L_0x0159:
            com.ta.audid.db.SqliteHelper r2 = r1.mHelper     // Catch:{ all -> 0x015f }
            r2.closeWritableDatabase(r5)     // Catch:{ all -> 0x015f }
            throw r0     // Catch:{ all -> 0x015f }
        L_0x015f:
            r0 = move-exception
            monitor-exit(r19)
            throw r0
        L_0x0162:
            monitor-exit(r19)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.insert(java.util.List):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:13|14|15|(4:18|(2:20|57)(2:21|56)|22|16)|23|24|25|26|27|28|29|41|42|43) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:14|15|(4:18|(2:20|57)(2:21|56)|22|16)|23|24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x010d, code lost:
        return 0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x00d5 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x00d8 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00f1 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00f4 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:46:0x0100 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x0103 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:48:0x0103=Splitter:B:48:0x0103, B:39:0x00f4=Splitter:B:39:0x00f4, B:27:0x00d8=Splitter:B:27:0x00d8} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.util.List<? extends com.ta.audid.db.Entity> r18) {
        /*
            r17 = this;
            r1 = r17
            r2 = r18
            monitor-enter(r17)
            r3 = 0
            if (r2 == 0) goto L_0x010c
            int r0 = r18.size()     // Catch:{ all -> 0x0109 }
            if (r0 != 0) goto L_0x0010
            goto L_0x010c
        L_0x0010:
            java.lang.Object r0 = r2.get(r3)     // Catch:{ all -> 0x0109 }
            com.ta.audid.db.Entity r0 = (com.ta.audid.db.Entity) r0     // Catch:{ all -> 0x0109 }
            java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x0109 }
            java.lang.String r0 = r1.getTablename(r0)     // Catch:{ all -> 0x0109 }
            java.lang.Object r4 = r2.get(r3)     // Catch:{ all -> 0x0109 }
            com.ta.audid.db.Entity r4 = (com.ta.audid.db.Entity) r4     // Catch:{ all -> 0x0109 }
            java.lang.Class r4 = r4.getClass()     // Catch:{ all -> 0x0109 }
            android.database.sqlite.SQLiteDatabase r4 = r1.checkTableAvailable(r4, r0)     // Catch:{ all -> 0x0109 }
            r5 = 1
            if (r4 != 0) goto L_0x003c
            java.lang.String r0 = "DBMgr"
            java.lang.Object[] r2 = new java.lang.Object[r5]     // Catch:{ all -> 0x0109 }
            java.lang.String r4 = "db is null"
            r2[r3] = r4     // Catch:{ all -> 0x0109 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r2)     // Catch:{ all -> 0x0109 }
            monitor-exit(r17)
            return r3
        L_0x003c:
            r6 = 2
            r4.beginTransaction()     // Catch:{ Throwable -> 0x00e0 }
            r7 = 0
        L_0x0041:
            int r8 = r18.size()     // Catch:{ Throwable -> 0x00e0 }
            if (r7 >= r8) goto L_0x00d2
            java.lang.String r8 = "_id=?"
            java.lang.String[] r9 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x00e0 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e0 }
            r10.<init>()     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Object r11 = r2.get(r7)     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ Throwable -> 0x00e0 }
            long r11 = r11._id     // Catch:{ Throwable -> 0x00e0 }
            r10.append(r11)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r11 = ""
            r10.append(r11)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x00e0 }
            r9[r3] = r10     // Catch:{ Throwable -> 0x00e0 }
            int r8 = r4.delete(r0, r8, r9)     // Catch:{ Throwable -> 0x00e0 }
            long r8 = (long) r8     // Catch:{ Throwable -> 0x00e0 }
            r10 = 0
            r12 = 5
            r13 = 4
            r14 = 3
            r15 = 6
            int r16 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r16 > 0) goto L_0x009d
            java.lang.String r8 = "DBMgr"
            java.lang.Object[] r9 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = "db"
            r9[r3] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = r1.mDbName     // Catch:{ Throwable -> 0x00e0 }
            r9[r5] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = "tableName"
            r9[r6] = r10     // Catch:{ Throwable -> 0x00e0 }
            r9[r14] = r0     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = " delete failed _id"
            r9[r13] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Object r10 = r2.get(r7)     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ Throwable -> 0x00e0 }
            long r10 = r10._id     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Long r10 = java.lang.Long.valueOf(r10)     // Catch:{ Throwable -> 0x00e0 }
            r9[r12] = r10     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.utils.UtdidLogger.w(r8, r9)     // Catch:{ Throwable -> 0x00e0 }
            goto L_0x00ce
        L_0x009d:
            java.lang.String r8 = "DBMgr"
            java.lang.Object[] r9 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = "db "
            r9[r3] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = r1.mDbName     // Catch:{ Throwable -> 0x00e0 }
            r9[r5] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = "tableName"
            r9[r6] = r10     // Catch:{ Throwable -> 0x00e0 }
            r9[r14] = r0     // Catch:{ Throwable -> 0x00e0 }
            java.lang.String r10 = "delete success _id"
            r9[r13] = r10     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Object r10 = r2.get(r7)     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.db.Entity r10 = (com.ta.audid.db.Entity) r10     // Catch:{ Throwable -> 0x00e0 }
            long r10 = r10._id     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Long r10 = java.lang.Long.valueOf(r10)     // Catch:{ Throwable -> 0x00e0 }
            r9[r12] = r10     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r8, (java.lang.Object[]) r9)     // Catch:{ Throwable -> 0x00e0 }
            java.lang.Object r8 = r2.get(r7)     // Catch:{ Throwable -> 0x00e0 }
            com.ta.audid.db.Entity r8 = (com.ta.audid.db.Entity) r8     // Catch:{ Throwable -> 0x00e0 }
            r9 = -1
            r8._id = r9     // Catch:{ Throwable -> 0x00e0 }
        L_0x00ce:
            int r7 = r7 + 1
            goto L_0x0041
        L_0x00d2:
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00d5 }
        L_0x00d5:
            r4.endTransaction()     // Catch:{ Throwable -> 0x00d8 }
        L_0x00d8:
            com.ta.audid.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x0109 }
        L_0x00da:
            r0.closeWritableDatabase(r4)     // Catch:{ all -> 0x0109 }
            goto L_0x00f7
        L_0x00de:
            r0 = move-exception
            goto L_0x00fd
        L_0x00e0:
            r0 = move-exception
            java.lang.String r7 = "DBMgr"
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x00de }
            java.lang.String r8 = "db delete error:"
            r6[r3] = r8     // Catch:{ all -> 0x00de }
            r6[r5] = r0     // Catch:{ all -> 0x00de }
            com.ta.audid.utils.UtdidLogger.w(r7, r6)     // Catch:{ all -> 0x00de }
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x00f1 }
        L_0x00f1:
            r4.endTransaction()     // Catch:{ Throwable -> 0x00f4 }
        L_0x00f4:
            com.ta.audid.db.SqliteHelper r0 = r1.mHelper     // Catch:{ all -> 0x0109 }
            goto L_0x00da
        L_0x00f7:
            int r0 = r18.size()     // Catch:{ all -> 0x0109 }
            monitor-exit(r17)
            return r0
        L_0x00fd:
            r4.setTransactionSuccessful()     // Catch:{ Throwable -> 0x0100 }
        L_0x0100:
            r4.endTransaction()     // Catch:{ Throwable -> 0x0103 }
        L_0x0103:
            com.ta.audid.db.SqliteHelper r2 = r1.mHelper     // Catch:{ all -> 0x0109 }
            r2.closeWritableDatabase(r4)     // Catch:{ all -> 0x0109 }
            throw r0     // Catch:{ all -> 0x0109 }
        L_0x0109:
            r0 = move-exception
            monitor-exit(r17)
            throw r0
        L_0x010c:
            monitor-exit(r17)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.delete(java.util.List):int");
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

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00e1, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r2.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00e6, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r2.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0110, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        r2.endTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0122, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        com.ta.audid.utils.UtdidLogger.w(TAG, "endTransaction", r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0130, code lost:
        r14 = r13.mHelper;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0139, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:58:0x010c */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e1 A[ExcHandler: all (r14v5 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:12:0x0039] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void update(java.util.List<? extends com.ta.audid.db.Entity> r14) {
        /*
            r13 = this;
            monitor-enter(r13)
            if (r14 == 0) goto L_0x0138
            int r0 = r14.size()     // Catch:{ all -> 0x0135 }
            if (r0 != 0) goto L_0x000b
            goto L_0x0138
        L_0x000b:
            r0 = 0
            java.lang.Object r1 = r14.get(r0)     // Catch:{ all -> 0x0135 }
            com.ta.audid.db.Entity r1 = (com.ta.audid.db.Entity) r1     // Catch:{ all -> 0x0135 }
            java.lang.Class r1 = r1.getClass()     // Catch:{ all -> 0x0135 }
            java.lang.String r1 = r13.getTablename(r1)     // Catch:{ all -> 0x0135 }
            java.lang.Object r2 = r14.get(r0)     // Catch:{ all -> 0x0135 }
            com.ta.audid.db.Entity r2 = (com.ta.audid.db.Entity) r2     // Catch:{ all -> 0x0135 }
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x0135 }
            android.database.sqlite.SQLiteDatabase r2 = r13.checkTableAvailable(r2, r1)     // Catch:{ all -> 0x0135 }
            r3 = 1
            if (r2 != 0) goto L_0x0038
            java.lang.String r14 = "DBMgr"
            java.lang.Object[] r1 = new java.lang.Object[r3]     // Catch:{ all -> 0x0135 }
            java.lang.String r2 = "[update] db is null"
            r1[r0] = r2     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r14, r1)     // Catch:{ all -> 0x0135 }
            monitor-exit(r13)
            return
        L_0x0038:
            r4 = 2
            r2.beginTransaction()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.Object r5 = r14.get(r0)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            com.ta.audid.db.Entity r5 = (com.ta.audid.db.Entity) r5     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.util.List r5 = r13.getAllFields(r5)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r6 = 0
        L_0x004b:
            int r7 = r14.size()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            if (r6 >= r7) goto L_0x00b7
            android.content.ContentValues r7 = new android.content.ContentValues     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r7.<init>()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r8 = 0
        L_0x0057:
            int r9 = r5.size()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            if (r8 >= r9) goto L_0x0092
            java.lang.Object r9 = r5.get(r8)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.reflect.Field r9 = (java.lang.reflect.Field) r9     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r9.setAccessible(r3)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.String r10 = r13.getColumnName(r9)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            r11.<init>()     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.Object r12 = r14.get(r6)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.Object r9 = r9.get(r12)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            r11.append(r9)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.String r9 = ""
            r11.append(r9)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            java.lang.String r9 = r11.toString()     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            r7.put(r10, r9)     // Catch:{ Exception -> 0x0087, all -> 0x00e1 }
            goto L_0x008f
        L_0x0087:
            r9 = move-exception
            java.lang.String r10 = ""
            java.lang.Object[] r11 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            com.ta.audid.utils.UtdidLogger.e(r10, r9, r11)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
        L_0x008f:
            int r8 = r8 + 1
            goto L_0x0057
        L_0x0092:
            java.lang.String r8 = "_id=?"
            java.lang.String[] r9 = new java.lang.String[r3]     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r10.<init>()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.Object r11 = r14.get(r6)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            com.ta.audid.db.Entity r11 = (com.ta.audid.db.Entity) r11     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            long r11 = r11._id     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r10.append(r11)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.String r11 = ""
            r10.append(r11)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r9[r0] = r10     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            r2.update(r1, r7, r8, r9)     // Catch:{ Exception -> 0x010c, all -> 0x00e1 }
            int r6 = r6 + 1
            goto L_0x004b
        L_0x00b7:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00bb }
            goto L_0x00c9
        L_0x00bb:
            r14 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r6 = "setTransactionSuccessful"
            r5[r0] = r6     // Catch:{ all -> 0x0135 }
            r5[r3] = r14     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r1, r5)     // Catch:{ all -> 0x0135 }
        L_0x00c9:
            r2.endTransaction()     // Catch:{ Exception -> 0x00cd }
            goto L_0x00db
        L_0x00cd:
            r14 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = "endTransaction"
            r4[r0] = r5     // Catch:{ all -> 0x0135 }
            r4[r3] = r14     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r1, r4)     // Catch:{ all -> 0x0135 }
        L_0x00db:
            com.ta.audid.db.SqliteHelper r14 = r13.mHelper     // Catch:{ all -> 0x0135 }
        L_0x00dd:
            r14.closeWritableDatabase(r2)     // Catch:{ all -> 0x0135 }
            goto L_0x0133
        L_0x00e1:
            r14 = move-exception
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00e6 }
            goto L_0x00f4
        L_0x00e6:
            r1 = move-exception
            java.lang.String r5 = "DBMgr"
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r7 = "setTransactionSuccessful"
            r6[r0] = r7     // Catch:{ all -> 0x0135 }
            r6[r3] = r1     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r5, r6)     // Catch:{ all -> 0x0135 }
        L_0x00f4:
            r2.endTransaction()     // Catch:{ Exception -> 0x00f8 }
            goto L_0x0106
        L_0x00f8:
            r1 = move-exception
            java.lang.String r5 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r6 = "endTransaction"
            r4[r0] = r6     // Catch:{ all -> 0x0135 }
            r4[r3] = r1     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r5, r4)     // Catch:{ all -> 0x0135 }
        L_0x0106:
            com.ta.audid.db.SqliteHelper r0 = r13.mHelper     // Catch:{ all -> 0x0135 }
            r0.closeWritableDatabase(r2)     // Catch:{ all -> 0x0135 }
            throw r14     // Catch:{ all -> 0x0135 }
        L_0x010c:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x0110 }
            goto L_0x011e
        L_0x0110:
            r14 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r6 = "setTransactionSuccessful"
            r5[r0] = r6     // Catch:{ all -> 0x0135 }
            r5[r3] = r14     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r1, r5)     // Catch:{ all -> 0x0135 }
        L_0x011e:
            r2.endTransaction()     // Catch:{ Exception -> 0x0122 }
            goto L_0x0130
        L_0x0122:
            r14 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0135 }
            java.lang.String r5 = "endTransaction"
            r4[r0] = r5     // Catch:{ all -> 0x0135 }
            r4[r3] = r14     // Catch:{ all -> 0x0135 }
            com.ta.audid.utils.UtdidLogger.w(r1, r4)     // Catch:{ all -> 0x0135 }
        L_0x0130:
            com.ta.audid.db.SqliteHelper r14 = r13.mHelper     // Catch:{ all -> 0x0135 }
            goto L_0x00dd
        L_0x0133:
            monitor-exit(r13)
            return
        L_0x0135:
            r14 = move-exception
            monitor-exit(r13)
            throw r14
        L_0x0138:
            monitor-exit(r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.update(java.util.List):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00b3, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        com.ta.audid.utils.UtdidLogger.e("", r9, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c5, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00eb, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r2.setTransactionSuccessful();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f0, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        com.ta.audid.utils.UtdidLogger.w(TAG, "setTransactionSuccessful", r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0143, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:60:0x0116 */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00eb A[ExcHandler: all (r15v5 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:12:0x0039] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateLogPriority(java.util.List<? extends com.ta.audid.db.Entity> r15) {
        /*
            r14 = this;
            monitor-enter(r14)
            if (r15 == 0) goto L_0x0142
            int r0 = r15.size()     // Catch:{ all -> 0x013f }
            if (r0 != 0) goto L_0x000b
            goto L_0x0142
        L_0x000b:
            r0 = 0
            java.lang.Object r1 = r15.get(r0)     // Catch:{ all -> 0x013f }
            com.ta.audid.db.Entity r1 = (com.ta.audid.db.Entity) r1     // Catch:{ all -> 0x013f }
            java.lang.Class r1 = r1.getClass()     // Catch:{ all -> 0x013f }
            java.lang.String r1 = r14.getTablename(r1)     // Catch:{ all -> 0x013f }
            java.lang.Object r2 = r15.get(r0)     // Catch:{ all -> 0x013f }
            com.ta.audid.db.Entity r2 = (com.ta.audid.db.Entity) r2     // Catch:{ all -> 0x013f }
            java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x013f }
            android.database.sqlite.SQLiteDatabase r2 = r14.checkTableAvailable(r2, r1)     // Catch:{ all -> 0x013f }
            r3 = 1
            if (r2 != 0) goto L_0x0038
            java.lang.String r15 = "DBMgr"
            java.lang.Object[] r1 = new java.lang.Object[r3]     // Catch:{ all -> 0x013f }
            java.lang.String r2 = "[update] db is null"
            r1[r0] = r2     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r15, r1)     // Catch:{ all -> 0x013f }
            monitor-exit(r14)
            return
        L_0x0038:
            r4 = 2
            r2.beginTransaction()     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            java.lang.Object r5 = r15.get(r0)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            com.ta.audid.db.Entity r5 = (com.ta.audid.db.Entity) r5     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            java.util.List r5 = r14.getAllFields(r5)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            r6 = 0
        L_0x004b:
            int r7 = r15.size()     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            if (r6 >= r7) goto L_0x00c1
            android.content.ContentValues r7 = new android.content.ContentValues     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            r7.<init>()     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            r8 = 0
        L_0x0057:
            int r9 = r5.size()     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            if (r8 >= r9) goto L_0x00be
            java.lang.Object r9 = r5.get(r8)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            java.lang.reflect.Field r9 = (java.lang.reflect.Field) r9     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            java.lang.String r10 = r14.getColumnName(r9)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            if (r10 == 0) goto L_0x00bb
            java.lang.String r11 = "priority"
            boolean r11 = r10.equalsIgnoreCase(r11)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            if (r11 == 0) goto L_0x00bb
            r9.setAccessible(r3)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r11.<init>()     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.Object r12 = r15.get(r6)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.Object r9 = r9.get(r12)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r11.append(r9)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.String r9 = ""
            r11.append(r9)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.String r9 = r11.toString()     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r7.put(r10, r9)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.String r9 = "_id=?"
            java.lang.String[] r10 = new java.lang.String[r3]     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r11.<init>()     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.Object r12 = r15.get(r6)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            com.ta.audid.db.Entity r12 = (com.ta.audid.db.Entity) r12     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            long r12 = r12._id     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r11.append(r12)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.String r12 = ""
            r11.append(r12)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r10[r0] = r11     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            r2.update(r1, r7, r9, r10)     // Catch:{ Exception -> 0x00b3, all -> 0x00eb }
            goto L_0x00be
        L_0x00b3:
            r9 = move-exception
            java.lang.String r10 = ""
            java.lang.Object[] r11 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
            com.ta.audid.utils.UtdidLogger.e(r10, r9, r11)     // Catch:{ Exception -> 0x0116, all -> 0x00eb }
        L_0x00bb:
            int r8 = r8 + 1
            goto L_0x0057
        L_0x00be:
            int r6 = r6 + 1
            goto L_0x004b
        L_0x00c1:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00c5 }
            goto L_0x00d3
        L_0x00c5:
            r15 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r6 = "setTransactionSuccessful"
            r5[r0] = r6     // Catch:{ all -> 0x013f }
            r5[r3] = r15     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r1, r5)     // Catch:{ all -> 0x013f }
        L_0x00d3:
            r2.endTransaction()     // Catch:{ Exception -> 0x00d7 }
            goto L_0x00e5
        L_0x00d7:
            r15 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r5 = "endTransaction"
            r4[r0] = r5     // Catch:{ all -> 0x013f }
            r4[r3] = r15     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r1, r4)     // Catch:{ all -> 0x013f }
        L_0x00e5:
            com.ta.audid.db.SqliteHelper r15 = r14.mHelper     // Catch:{ all -> 0x013f }
        L_0x00e7:
            r15.closeWritableDatabase(r2)     // Catch:{ all -> 0x013f }
            goto L_0x013d
        L_0x00eb:
            r15 = move-exception
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x00f0 }
            goto L_0x00fe
        L_0x00f0:
            r1 = move-exception
            java.lang.String r5 = "DBMgr"
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r7 = "setTransactionSuccessful"
            r6[r0] = r7     // Catch:{ all -> 0x013f }
            r6[r3] = r1     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r5, r6)     // Catch:{ all -> 0x013f }
        L_0x00fe:
            r2.endTransaction()     // Catch:{ Exception -> 0x0102 }
            goto L_0x0110
        L_0x0102:
            r1 = move-exception
            java.lang.String r5 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r6 = "endTransaction"
            r4[r0] = r6     // Catch:{ all -> 0x013f }
            r4[r3] = r1     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r5, r4)     // Catch:{ all -> 0x013f }
        L_0x0110:
            com.ta.audid.db.SqliteHelper r0 = r14.mHelper     // Catch:{ all -> 0x013f }
            r0.closeWritableDatabase(r2)     // Catch:{ all -> 0x013f }
            throw r15     // Catch:{ all -> 0x013f }
        L_0x0116:
            r2.setTransactionSuccessful()     // Catch:{ Exception -> 0x011a }
            goto L_0x0128
        L_0x011a:
            r15 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r6 = "setTransactionSuccessful"
            r5[r0] = r6     // Catch:{ all -> 0x013f }
            r5[r3] = r15     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r1, r5)     // Catch:{ all -> 0x013f }
        L_0x0128:
            r2.endTransaction()     // Catch:{ Exception -> 0x012c }
            goto L_0x013a
        L_0x012c:
            r15 = move-exception
            java.lang.String r1 = "DBMgr"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x013f }
            java.lang.String r5 = "endTransaction"
            r4[r0] = r5     // Catch:{ all -> 0x013f }
            r4[r3] = r15     // Catch:{ all -> 0x013f }
            com.ta.audid.utils.UtdidLogger.w(r1, r4)     // Catch:{ all -> 0x013f }
        L_0x013a:
            com.ta.audid.db.SqliteHelper r15 = r14.mHelper     // Catch:{ all -> 0x013f }
            goto L_0x00e7
        L_0x013d:
            monitor-exit(r14)
            return
        L_0x013f:
            r15 = move-exception
            monitor-exit(r14)
            throw r15
        L_0x0142:
            monitor-exit(r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.updateLogPriority(java.util.List):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0036, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void execSQL(java.lang.Class<? extends com.ta.audid.db.Entity> r3, java.lang.String r4) {
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
            com.ta.audid.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0032 }
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
            com.ta.audid.utils.UtdidLogger.w(r0, r4, r1)     // Catch:{ all -> 0x001e }
            com.ta.audid.db.SqliteHelper r4 = r2.mHelper     // Catch:{ all -> 0x0032 }
            goto L_0x001a
        L_0x002c:
            com.ta.audid.db.SqliteHelper r0 = r2.mHelper     // Catch:{ all -> 0x0032 }
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
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.execSQL(java.lang.Class, java.lang.String):void");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0051, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int delete(java.lang.Class<? extends com.ta.audid.db.Entity> r6, java.lang.String r7, java.lang.String[] r8) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 0
            r1 = 5
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0052 }
            java.lang.String r2 = "whereArgs"
            r3 = 0
            r1[r3] = r2     // Catch:{ all -> 0x0052 }
            r2 = 1
            r1[r2] = r8     // Catch:{ all -> 0x0052 }
            r2 = 2
            java.lang.String r4 = ""
            r1[r2] = r4     // Catch:{ all -> 0x0052 }
            r2 = 3
            java.lang.String r4 = "whereArgs"
            r1[r2] = r4     // Catch:{ all -> 0x0052 }
            r2 = 4
            r1[r2] = r8     // Catch:{ all -> 0x0052 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ all -> 0x0052 }
            if (r6 == 0) goto L_0x004f
            java.lang.String r0 = r5.getTablename(r6)     // Catch:{ all -> 0x0052 }
            android.database.sqlite.SQLiteDatabase r0 = r5.checkTableAvailable(r6, r0)     // Catch:{ all -> 0x0052 }
            if (r0 != 0) goto L_0x002b
            monitor-exit(r5)
            return r3
        L_0x002b:
            java.lang.String r6 = r5.getTablename(r6)     // Catch:{ Throwable -> 0x003b }
            int r6 = r0.delete(r6, r7, r8)     // Catch:{ Throwable -> 0x003b }
            com.ta.audid.db.SqliteHelper r7 = r5.mHelper     // Catch:{ all -> 0x0052 }
            r7.closeWritableDatabase(r0)     // Catch:{ all -> 0x0052 }
            goto L_0x0050
        L_0x0039:
            r6 = move-exception
            goto L_0x0049
        L_0x003b:
            r6 = move-exception
            java.lang.String r7 = "DBMgr"
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ all -> 0x0039 }
            com.ta.audid.utils.UtdidLogger.w(r7, r6, r8)     // Catch:{ all -> 0x0039 }
            com.ta.audid.db.SqliteHelper r6 = r5.mHelper     // Catch:{ all -> 0x0052 }
            r6.closeWritableDatabase(r0)     // Catch:{ all -> 0x0052 }
            goto L_0x004f
        L_0x0049:
            com.ta.audid.db.SqliteHelper r7 = r5.mHelper     // Catch:{ all -> 0x0052 }
            r7.closeWritableDatabase(r0)     // Catch:{ all -> 0x0052 }
            throw r6     // Catch:{ all -> 0x0052 }
        L_0x004f:
            r6 = 0
        L_0x0050:
            monitor-exit(r5)
            return r6
        L_0x0052:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.DBMgr.delete(java.lang.Class, java.lang.String, java.lang.String[]):int");
    }

    public String getTablename(Class<?> cls) {
        String str;
        if (cls == null) {
            return null;
        }
        if (this.mClsTableNameMap.containsKey(cls)) {
            return this.mClsTableNameMap.get(cls);
        }
        TableName tableName = (TableName) cls.getAnnotation(TableName.class);
        if (tableName == null || TextUtils.isEmpty(tableName.value())) {
            str = cls.getName().replace(".", "_");
        } else {
            str = tableName.value();
        }
        this.mClsTableNameMap.put(cls, str);
        return str;
    }

    private SQLiteDatabase checkTableAvailable(Class<? extends Entity> cls, String str) {
        Cursor cursor;
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        Boolean bool = true;
        int i = 0;
        if (this.mCheckdbMap.get(str) == null || !this.mCheckdbMap.get(str).booleanValue()) {
            bool = false;
        }
        if (cls != null && !bool.booleanValue()) {
            List<Field> allFields = getAllFields(cls);
            ArrayList arrayList = new ArrayList();
            String str2 = " SELECT * FROM " + str + " LIMIT 0";
            if (allFields != null) {
                try {
                    cursor = writableDatabase.rawQuery(str2, (String[]) null);
                } catch (Exception unused) {
                    UtdidLogger.d(TAG, "has not create talbe:", str);
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
                UtdidLogger.w(TAG, "update db error...", e);
            }
            sb.delete(0, sb2.length());
            UtdidLogger.d(TAG, null, "excute sql:", sb2);
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
        UtdidLogger.d(TAG, "excute sql:", sb2);
        try {
            sQLiteDatabase.execSQL(sb2);
        } catch (Exception e) {
            UtdidLogger.w(TAG, "create db error", e);
        }
    }

    private String getSQLType(Class<?> cls) {
        return (cls == Long.TYPE || cls == Integer.TYPE || cls == Long.class) ? "INTEGER" : "TEXT";
    }

    public synchronized int count(Class<? extends Entity> cls) {
        SqliteHelper sqliteHelper;
        int i = 0;
        if (cls == null) {
            return 0;
        }
        String tablename = getTablename(cls);
        SQLiteDatabase checkTableAvailable = checkTableAvailable(cls, tablename);
        if (checkTableAvailable != null) {
            Cursor cursor = null;
            try {
                Cursor rawQuery = checkTableAvailable.rawQuery("SELECT count(*) FROM " + tablename, (String[]) null);
                if (rawQuery != null) {
                    try {
                        rawQuery.moveToFirst();
                        i = rawQuery.getInt(0);
                    } catch (Throwable th) {
                        th = th;
                        cursor = rawQuery;
                        this.mHelper.closeCursor(cursor);
                        this.mHelper.closeWritableDatabase(checkTableAvailable);
                        throw th;
                    }
                }
                this.mHelper.closeCursor(rawQuery);
                sqliteHelper = this.mHelper;
            } catch (Throwable th2) {
                th = th2;
                this.mHelper.closeCursor(cursor);
                this.mHelper.closeWritableDatabase(checkTableAvailable);
                throw th;
            }
            sqliteHelper.closeWritableDatabase(checkTableAvailable);
        } else {
            UtdidLogger.d(TAG, "db is null");
        }
    }

    public synchronized int count(Class<? extends Entity> cls, String str) {
        SqliteHelper sqliteHelper;
        String str2;
        int i = 0;
        if (cls == null) {
            return 0;
        }
        String tablename = getTablename(cls);
        SQLiteDatabase checkTableAvailable = checkTableAvailable(cls, tablename);
        if (checkTableAvailable != null) {
            Cursor cursor = null;
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT count(*) FROM ");
                sb.append(tablename);
                if (TextUtils.isEmpty(str)) {
                    str2 = "";
                } else {
                    str2 = " WHERE " + str;
                }
                sb.append(str2);
                Cursor rawQuery = checkTableAvailable.rawQuery(sb.toString(), (String[]) null);
                if (rawQuery != null) {
                    try {
                        rawQuery.moveToFirst();
                        i = rawQuery.getInt(0);
                    } catch (Throwable th) {
                        cursor = rawQuery;
                        th = th;
                        this.mHelper.closeCursor(cursor);
                        this.mHelper.closeWritableDatabase(checkTableAvailable);
                        throw th;
                    }
                }
                this.mHelper.closeCursor(rawQuery);
                sqliteHelper = this.mHelper;
            } catch (Throwable th2) {
                th = th2;
                UtdidLogger.d(TAG, th.toString());
                this.mHelper.closeCursor(cursor);
                sqliteHelper = this.mHelper;
                sqliteHelper.closeWritableDatabase(checkTableAvailable);
                return i;
            }
            sqliteHelper.closeWritableDatabase(checkTableAvailable);
        } else {
            UtdidLogger.d(TAG, "db is null");
        }
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
                UtdidLogger.e("delete db data", e, new Object[0]);
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
}
