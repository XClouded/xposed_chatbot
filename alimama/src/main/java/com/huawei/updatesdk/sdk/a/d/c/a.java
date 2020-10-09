package com.huawei.updatesdk.sdk.a.d.c;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.huawei.updatesdk.sdk.a.d.b.a.b;

public class a {
    private static final Uri a = Uri.parse("content://telephony/carriers/preferapn");
    private static final Uri b = Uri.parse("content://telephony/carriers/preferapn/0");
    private static final Uri c = Uri.parse("content://telephony/carriers/preferapn/1");

    /* renamed from: com.huawei.updatesdk.sdk.a.d.c.a$a  reason: collision with other inner class name */
    public static class C0016a {
        private String a;
        private String b;
        private String c;

        public String a() {
            return this.a;
        }

        public void a(String str) {
            this.a = str;
        }

        public String b() {
            return this.b;
        }

        public void b(String str) {
            this.b = str;
        }

        public void c(String str) {
            this.c = str;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0053, code lost:
        if (r5 != null) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0069, code lost:
        if (r5 != null) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006b, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        if (r0.a() != null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        return r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.huawei.updatesdk.sdk.a.d.c.a.C0016a a(android.content.Context r5) throws java.lang.SecurityException {
        /*
            com.huawei.updatesdk.sdk.a.d.c.a$a r0 = new com.huawei.updatesdk.sdk.a.d.c.a$a
            r0.<init>()
            r1 = 0
            boolean r2 = com.huawei.updatesdk.sdk.a.d.b.a.b.b()     // Catch:{ SecurityException -> 0x0078, Exception -> 0x005f }
            if (r2 == 0) goto L_0x0011
            android.database.Cursor r5 = d(r5)     // Catch:{ SecurityException -> 0x0078, Exception -> 0x005f }
            goto L_0x0015
        L_0x0011:
            android.database.Cursor r5 = c(r5)     // Catch:{ SecurityException -> 0x0078, Exception -> 0x005f }
        L_0x0015:
            if (r5 != 0) goto L_0x001d
            if (r5 == 0) goto L_0x001c
            r5.close()
        L_0x001c:
            return r1
        L_0x001d:
            boolean r2 = r5.moveToNext()     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            if (r2 == 0) goto L_0x0053
            java.lang.String r2 = "_id"
            int r2 = r5.getColumnIndex(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = r5.getString(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            r0.a(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = "name"
            int r2 = r5.getColumnIndex(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = r5.getString(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            r0.c(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = "apn"
            int r2 = r5.getColumnIndex(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = r5.getString(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.util.Locale r3 = java.util.Locale.getDefault()     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            java.lang.String r2 = r2.toLowerCase(r3)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            r0.b(r2)     // Catch:{ SecurityException -> 0x0058, Exception -> 0x0056 }
            goto L_0x001d
        L_0x0053:
            if (r5 == 0) goto L_0x006e
            goto L_0x006b
        L_0x0056:
            r2 = move-exception
            goto L_0x0062
        L_0x0058:
            r0 = move-exception
            r1 = r5
            goto L_0x007a
        L_0x005b:
            r5 = move-exception
            r0 = r5
            r5 = r1
            goto L_0x0082
        L_0x005f:
            r5 = move-exception
            r2 = r5
            r5 = r1
        L_0x0062:
            java.lang.String r3 = "ApnUtil"
            java.lang.String r4 = "getDefaultAPN, Exception: "
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r3, r4, r2)     // Catch:{ all -> 0x0076 }
            if (r5 == 0) goto L_0x006e
        L_0x006b:
            r5.close()
        L_0x006e:
            java.lang.String r5 = r0.a()
            if (r5 != 0) goto L_0x0075
            r0 = r1
        L_0x0075:
            return r0
        L_0x0076:
            r0 = move-exception
            goto L_0x0082
        L_0x0078:
            r5 = move-exception
            r0 = r5
        L_0x007a:
            java.lang.String r5 = "ApnUtil"
            java.lang.String r2 = "getDefaultAPN, SecurityException: "
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r5, r2, r0)     // Catch:{ all -> 0x005b }
            throw r0     // Catch:{ all -> 0x005b }
        L_0x0082:
            if (r5 == 0) goto L_0x0087
            r5.close()
        L_0x0087:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.d.c.a.a(android.content.Context):com.huawei.updatesdk.sdk.a.d.c.a$a");
    }

    public static boolean b(Context context) {
        try {
            C0016a a2 = a(context);
            if (a2 != null) {
                return a2.b() != null && a2.b().contains("wap");
            }
            return true;
        } catch (SecurityException e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("ApnUtil", "isWap(), SecurityException: ", e);
            return false;
        }
    }

    private static Cursor c(Context context) {
        return context.getContentResolver().query(a, (String[]) null, (String) null, (String[]) null, (String) null);
    }

    private static Cursor d(Context context) {
        Cursor cursor;
        if (b.a().a() == 0) {
            cursor = f(context);
            if (cursor == null) {
                cursor = e(context);
            }
        } else {
            cursor = e(context);
            if (cursor == null) {
                cursor = f(context);
            }
        }
        return cursor == null ? c(context) : cursor;
    }

    private static Cursor e(Context context) {
        return context.getContentResolver().query(c, (String[]) null, (String) null, (String[]) null, (String) null);
    }

    private static Cursor f(Context context) {
        return context.getContentResolver().query(b, (String[]) null, (String) null, (String[]) null, (String) null);
    }
}
