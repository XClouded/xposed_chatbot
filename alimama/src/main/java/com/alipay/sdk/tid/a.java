package com.alipay.sdk.tid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alipay.sdk.util.c;
import java.lang.ref.WeakReference;

final class a extends SQLiteOpenHelper {
    private static final String a = "msp.db";
    private static final int b = 1;
    private WeakReference<Context> c;

    a(Context context) {
        super(context, a, (SQLiteDatabase.CursorFactory) null, 1);
        this.c = new WeakReference<>(context);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists tb_tid (name text primary key, tid text, key_tid text, dt datetime);");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("drop table if exists tb_tid");
    }

    /* access modifiers changed from: package-private */
    public void a() {
        SQLiteDatabase sQLiteDatabase;
        Exception e;
        try {
            sQLiteDatabase = getWritableDatabase();
            try {
                sQLiteDatabase.execSQL("drop table if exists tb_tid");
                if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
                    return;
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    c.a(e);
                    return;
                } catch (Throwable th) {
                    th = th;
                    if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                        sQLiteDatabase.close();
                    }
                    throw th;
                }
            }
        } catch (Exception e3) {
            Exception exc = e3;
            sQLiteDatabase = null;
            e = exc;
            c.a(e);
            if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
                return;
            }
            sQLiteDatabase.close();
        } catch (Throwable th2) {
            Throwable th3 = th2;
            sQLiteDatabase = null;
            th = th3;
            sQLiteDatabase.close();
            throw th;
        }
        sQLiteDatabase.close();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v3, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (r2.isOpen() != false) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        r2.close();
        r1 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x005a, code lost:
        if (r2.isOpen() != false) goto L_0x002d;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(java.lang.String r5, java.lang.String r6) {
        /*
            r4 = this;
            java.lang.String r0 = "select tid from tb_tid where name=?"
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r4.getReadableDatabase()     // Catch:{ Exception -> 0x004d, all -> 0x003a }
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            java.lang.String r5 = r4.c(r5, r6)     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            r6 = 0
            r3[r6] = r5     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            android.database.Cursor r5 = r2.rawQuery(r0, r3)     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            boolean r0 = r5.moveToFirst()     // Catch:{ Exception -> 0x0034, all -> 0x0031 }
            if (r0 == 0) goto L_0x0020
            java.lang.String r6 = r5.getString(r6)     // Catch:{ Exception -> 0x0034, all -> 0x0031 }
            r1 = r6
        L_0x0020:
            if (r5 == 0) goto L_0x0025
            r5.close()
        L_0x0025:
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x005d
        L_0x002d:
            r2.close()
            goto L_0x005d
        L_0x0031:
            r6 = move-exception
            r1 = r5
            goto L_0x003c
        L_0x0034:
            goto L_0x004f
        L_0x0036:
            r6 = move-exception
            goto L_0x003c
        L_0x0038:
            r5 = r1
            goto L_0x004f
        L_0x003a:
            r6 = move-exception
            r2 = r1
        L_0x003c:
            if (r1 == 0) goto L_0x0041
            r1.close()
        L_0x0041:
            if (r2 == 0) goto L_0x004c
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x004c
            r2.close()
        L_0x004c:
            throw r6
        L_0x004d:
            r5 = r1
            r2 = r5
        L_0x004f:
            if (r5 == 0) goto L_0x0054
            r5.close()
        L_0x0054:
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x005d
            goto L_0x002d
        L_0x005d:
            boolean r5 = android.text.TextUtils.isEmpty(r1)
            if (r5 != 0) goto L_0x0073
            java.lang.ref.WeakReference<android.content.Context> r5 = r4.c
            java.lang.Object r5 = r5.get()
            android.content.Context r5 = (android.content.Context) r5
            java.lang.String r5 = com.alipay.sdk.util.a.c(r5)
            java.lang.String r1 = com.alipay.sdk.encrypt.b.b(r1, r5)
        L_0x0073:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.a.a(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v2, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (r2.isOpen() != false) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        r2.close();
        r1 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x005a, code lost:
        if (r2.isOpen() != false) goto L_0x002d;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0056  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String b(java.lang.String r5, java.lang.String r6) {
        /*
            r4 = this;
            java.lang.String r0 = "select key_tid from tb_tid where name=?"
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r4.getReadableDatabase()     // Catch:{ Exception -> 0x004d, all -> 0x003a }
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            java.lang.String r5 = r4.c(r5, r6)     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            r6 = 0
            r3[r6] = r5     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            android.database.Cursor r5 = r2.rawQuery(r0, r3)     // Catch:{ Exception -> 0x0038, all -> 0x0036 }
            boolean r0 = r5.moveToFirst()     // Catch:{ Exception -> 0x0034, all -> 0x0031 }
            if (r0 == 0) goto L_0x0020
            java.lang.String r6 = r5.getString(r6)     // Catch:{ Exception -> 0x0034, all -> 0x0031 }
            r1 = r6
        L_0x0020:
            if (r5 == 0) goto L_0x0025
            r5.close()
        L_0x0025:
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x005d
        L_0x002d:
            r2.close()
            goto L_0x005d
        L_0x0031:
            r6 = move-exception
            r1 = r5
            goto L_0x003c
        L_0x0034:
            goto L_0x004f
        L_0x0036:
            r6 = move-exception
            goto L_0x003c
        L_0x0038:
            r5 = r1
            goto L_0x004f
        L_0x003a:
            r6 = move-exception
            r2 = r1
        L_0x003c:
            if (r1 == 0) goto L_0x0041
            r1.close()
        L_0x0041:
            if (r2 == 0) goto L_0x004c
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x004c
            r2.close()
        L_0x004c:
            throw r6
        L_0x004d:
            r5 = r1
            r2 = r5
        L_0x004f:
            if (r5 == 0) goto L_0x0054
            r5.close()
        L_0x0054:
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.isOpen()
            if (r5 == 0) goto L_0x005d
            goto L_0x002d
        L_0x005d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.a.b(java.lang.String, java.lang.String):java.lang.String");
    }

    private String c(String str, String str2) {
        return str + str2;
    }
}
