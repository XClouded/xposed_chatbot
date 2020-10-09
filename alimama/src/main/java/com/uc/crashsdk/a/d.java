package com.uc.crashsdk.a;

import alimama.com.unwetaologger.base.UNWLogger;
import com.uc.crashsdk.a;
import com.uc.crashsdk.b;
import com.uc.crashsdk.e;
import com.uc.crashsdk.h;
import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: ProGuard */
public class d {
    static final /* synthetic */ boolean a = (!d.class.desiredAssertionStatus());
    private static boolean b = true;
    private static final Object c = new Object();
    private static boolean d = false;
    private static String e = "hsdk";
    private static String f = "alid ";
    private static String g = null;
    private static final Object h = new Object();
    private static String i = null;

    public static void a() {
        f.a(0, new e(500), 90000);
    }

    public static void a(int i2) {
        if (i2 == 500) {
            synchronized (c) {
                g = null;
                a(!b.A());
                if (g.b(g)) {
                    h.a(g);
                }
            }
        } else if (!a) {
            throw new AssertionError();
        }
    }

    public static String b() {
        try {
            return "inv" + f + "cras" + e;
        } catch (Throwable th) {
            g.b(th);
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0083 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(boolean r12) {
        /*
            boolean r0 = d
            r1 = 0
            if (r0 == 0) goto L_0x0006
            return r1
        L_0x0006:
            r0 = r12 ^ 1
            d = r0
            java.lang.String r0 = com.uc.crashsdk.a.b
            java.lang.String r2 = "2.0"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x001d
            r0 = 536870912(0x20000000, float:1.0842022E-19)
            boolean r0 = com.uc.crashsdk.b.c(r0)
            if (r0 == 0) goto L_0x001d
            return r1
        L_0x001d:
            boolean r0 = com.uc.crashsdk.b.x()
            if (r0 != 0) goto L_0x0024
            return r1
        L_0x0024:
            java.lang.String r0 = com.uc.crashsdk.b.m()
            java.lang.String r2 = com.uc.crashsdk.a.b.a((java.lang.String) r0)
            r3 = 0
            r4 = 0
            boolean r6 = com.uc.crashsdk.a.g.b((java.lang.String) r2)
            r7 = 3
            r8 = 2
            r9 = 1
            if (r6 == 0) goto L_0x0052
            java.lang.String r6 = " "
            r10 = 4
            java.lang.String[] r2 = r2.split(r6, r10)
            int r6 = r2.length
            if (r6 != r7) goto L_0x0052
            r3 = r2[r1]
            r4 = r2[r9]
            long r4 = com.uc.crashsdk.a.g.c(r4)
            r2 = r2[r8]
            long r10 = com.uc.crashsdk.a.g.c(r2)
            int r2 = (int) r10
            goto L_0x0053
        L_0x0052:
            r2 = 0
        L_0x0053:
            b = r9
            long r10 = java.lang.System.currentTimeMillis()
            long r10 = r10 - r4
            r4 = 259200000(0xf731400, double:1.280618154E-315)
            int r6 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r6 >= 0) goto L_0x0080
            java.lang.String r4 = "o"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x006b
        L_0x0069:
            r4 = 0
            goto L_0x0081
        L_0x006b:
            java.lang.String r4 = "2"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x0076
            b = r1
            goto L_0x0069
        L_0x0076:
            java.lang.String r4 = "1"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x0080
            b = r1
        L_0x0080:
            r4 = 1
        L_0x0081:
            if (r4 == 0) goto L_0x018b
            if (r12 == 0) goto L_0x0087
            goto L_0x018b
        L_0x0087:
            int r12 = android.os.Process.myPid()
            if (r2 != r12) goto L_0x008e
            return r1
        L_0x008e:
            java.lang.String r12 = "per"
            g = r12
            java.lang.String r12 = f()
            if (r12 == 0) goto L_0x017f
            java.lang.String r2 = "retcode="
            boolean r2 = r12.contains(r2)
            if (r2 == 0) goto L_0x017f
            java.lang.String r2 = "retcode=0"
            boolean r2 = r12.contains(r2)
            if (r2 == 0) goto L_0x00b1
            b = r9
            java.lang.String r2 = "o"
            java.lang.String r3 = "aus"
            g = r3
            goto L_0x00c8
        L_0x00b1:
            b = r1
            java.lang.String r2 = "1"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c2
            java.lang.String r2 = "2"
            java.lang.String r3 = "auf2"
            g = r3
            goto L_0x00c8
        L_0x00c2:
            java.lang.String r2 = "1"
            java.lang.String r3 = "auf1"
            g = r3
        L_0x00c8:
            java.util.Locale r3 = java.util.Locale.US
            java.lang.String r4 = "%s %d %d"
            java.lang.Object[] r5 = new java.lang.Object[r7]
            r5[r1] = r2
            long r6 = java.lang.System.currentTimeMillis()
            java.lang.Long r2 = java.lang.Long.valueOf(r6)
            r5[r9] = r2
            int r2 = android.os.Process.myPid()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r5[r8] = r2
            java.lang.String r2 = java.lang.String.format(r3, r4, r5)
            com.uc.crashsdk.a.b.a((java.lang.String) r0, (java.lang.String) r2)
            boolean r0 = com.uc.crashsdk.a.g.b((java.lang.String) r12)
            if (r0 == 0) goto L_0x017e
            java.lang.String r0 = "`"
            r2 = 30
            java.lang.String[] r12 = r12.split(r0, r2)
            int r0 = r12.length
            r2 = 0
        L_0x00fb:
            if (r2 >= r0) goto L_0x017e
            r3 = r12[r2]
            java.lang.String r4 = "="
            java.lang.String[] r3 = r3.split(r4, r8)
            int r4 = r3.length
            if (r4 != r8) goto L_0x017a
            r4 = r3[r1]
            java.lang.String r4 = r4.trim()
            r3 = r3[r9]
            java.lang.String r3 = r3.trim()
            boolean r5 = com.uc.crashsdk.a.g.b((java.lang.String) r3)
            if (r5 == 0) goto L_0x0124
            java.lang.String r5 = "http"
            boolean r5 = r3.startsWith(r5)
            if (r5 == 0) goto L_0x0124
            r5 = 1
            goto L_0x0125
        L_0x0124:
            r5 = 0
        L_0x0125:
            java.lang.String r6 = "logurl"
            boolean r6 = r6.equals(r4)
            if (r6 == 0) goto L_0x0133
            if (r5 == 0) goto L_0x017a
            com.uc.crashsdk.e.a((java.lang.String) r3, (boolean) r1)
            goto L_0x017a
        L_0x0133:
            java.lang.String r6 = "staturl"
            boolean r6 = r6.equals(r4)
            if (r6 == 0) goto L_0x0141
            if (r5 == 0) goto L_0x017a
            com.uc.crashsdk.a.h.b(r3)
            goto L_0x017a
        L_0x0141:
            java.lang.String r6 = "policyurl"
            boolean r6 = r6.equals(r4)
            if (r6 == 0) goto L_0x016f
            if (r5 == 0) goto L_0x017a
            java.lang.Object r4 = h
            monitor-enter(r4)
            i = r3     // Catch:{ all -> 0x016c }
            java.lang.String r3 = com.uc.crashsdk.b.i()     // Catch:{ all -> 0x016c }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x016c }
            r5.<init>()     // Catch:{ all -> 0x016c }
            java.lang.String r6 = i     // Catch:{ all -> 0x016c }
            r5.append(r6)     // Catch:{ all -> 0x016c }
            java.lang.String r6 = "\n"
            r5.append(r6)     // Catch:{ all -> 0x016c }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x016c }
            com.uc.crashsdk.a.b.a((java.lang.String) r3, (java.lang.String) r5)     // Catch:{ all -> 0x016c }
            monitor-exit(r4)     // Catch:{ all -> 0x016c }
            goto L_0x017a
        L_0x016c:
            r12 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x016c }
            throw r12
        L_0x016f:
            java.lang.String r5 = "logpolicy"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x017a
            com.uc.crashsdk.e.c((java.lang.String) r3)
        L_0x017a:
            int r2 = r2 + 1
            goto L_0x00fb
        L_0x017e:
            return r9
        L_0x017f:
            if (r12 != 0) goto L_0x0186
            java.lang.String r12 = "ner"
            g = r12
            goto L_0x018a
        L_0x0186:
            java.lang.String r12 = "ser"
            g = r12
        L_0x018a:
            return r1
        L_0x018b:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.d.a(boolean):boolean");
    }

    private static String e() {
        if (g.a(i)) {
            synchronized (h) {
                String str = "https://woodpecker.uc.cn";
                if (h.O()) {
                    str = "https://wpk-auth.ucweb.com";
                }
                i = g.a(b.i(), str + "/api/crashsdk/validate", true);
            }
        }
        return i;
    }

    static byte[] c() {
        return new byte[]{6, 0, 23, 8};
    }

    private static String f() {
        byte[] bArr;
        String e2;
        byte[] a2;
        byte[] bArr2;
        StringBuilder sb = new StringBuilder();
        a(sb, "platform", h.f());
        a(sb, "pkgname", a.a);
        a(sb, UNWLogger.LOG_VALUE_TYPE_PROCESS, e.g());
        a(sb, "version", a.a());
        a(sb, "cver", "2.3.1.0");
        a(sb, "inter", h.O() ? "true" : "false");
        a(sb, "os", "android");
        String sb2 = sb.toString();
        byte[] bArr3 = new byte[16];
        c.a(bArr3, 0, h.d());
        c.a(bArr3, 4, c.b());
        c.a(bArr3, 8, c());
        c.a(bArr3, 12, a.e());
        try {
            bArr = c.a(sb2.getBytes(), bArr3, true);
        } catch (Throwable th) {
            g.a(th);
            bArr = null;
        }
        if (bArr == null || (e2 = e()) == null || (a2 = c.a(e2, bArr)) == null) {
            return null;
        }
        try {
            bArr2 = c.a(a2, bArr3, false);
        } catch (Throwable th2) {
            g.a(th2);
            bArr2 = null;
        }
        if (bArr2 != null) {
            return new String(bArr2);
        }
        return null;
    }

    private static StringBuilder a(StringBuilder sb, String str, String str2) {
        if (sb.length() > 0) {
            sb.append("`");
        }
        sb.append(str);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(str2);
        return sb;
    }

    public static boolean d() {
        try {
            if (!e.x()) {
                if (!b.D()) {
                    a(true);
                    return b;
                }
            }
            return true;
        } catch (Throwable unused) {
        }
    }
}
