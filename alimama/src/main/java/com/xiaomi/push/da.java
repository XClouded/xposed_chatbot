package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;

public class da {
    public static int a(Context context, int i) {
        int a = gr.a(context);
        if (-1 == a) {
            return -1;
        }
        return (i * (a == 0 ? 13 : 11)) / 10;
    }

    public static int a(hg hgVar) {
        return eu.a(hgVar.a());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        if (com.xiaomi.push.fa.a(r2) != -1) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        if (com.xiaomi.push.fa.a(r2) != -1) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int a(com.xiaomi.push.ir r2, com.xiaomi.push.hg r3) {
        /*
            int[] r0 = com.xiaomi.push.db.a
            int r1 = r3.ordinal()
            r0 = r0[r1]
            r1 = -1
            switch(r0) {
                case 1: goto L_0x00b5;
                case 2: goto L_0x00b5;
                case 3: goto L_0x00b5;
                case 4: goto L_0x00b5;
                case 5: goto L_0x00b5;
                case 6: goto L_0x00b5;
                case 7: goto L_0x00b5;
                case 8: goto L_0x00b5;
                case 9: goto L_0x00b5;
                case 10: goto L_0x00b5;
                case 11: goto L_0x0055;
                case 12: goto L_0x000e;
                default: goto L_0x000c;
            }
        L_0x000c:
            goto L_0x00bd
        L_0x000e:
            int r3 = r3.a()
            int r3 = com.xiaomi.push.eu.a((int) r3)
            if (r2 == 0) goto L_0x0053
            boolean r0 = r2 instanceof com.xiaomi.push.ib     // Catch:{ Exception -> 0x004e }
            if (r0 == 0) goto L_0x0037
            com.xiaomi.push.ib r2 = (com.xiaomi.push.ib) r2     // Catch:{ Exception -> 0x004e }
            java.lang.String r2 = r2.a()     // Catch:{ Exception -> 0x004e }
            boolean r0 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x004e }
            if (r0 != 0) goto L_0x0033
            int r0 = com.xiaomi.push.fa.a(r2)     // Catch:{ Exception -> 0x004e }
            if (r0 == r1) goto L_0x0033
        L_0x002e:
            int r2 = com.xiaomi.push.fa.a(r2)     // Catch:{ Exception -> 0x004e }
            goto L_0x0034
        L_0x0033:
            r2 = r3
        L_0x0034:
            r1 = r2
            goto L_0x00bd
        L_0x0037:
            boolean r0 = r2 instanceof com.xiaomi.push.ia     // Catch:{ Exception -> 0x004e }
            if (r0 == 0) goto L_0x0053
            com.xiaomi.push.ia r2 = (com.xiaomi.push.ia) r2     // Catch:{ Exception -> 0x004e }
            java.lang.String r2 = r2.a()     // Catch:{ Exception -> 0x004e }
            boolean r0 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x004e }
            if (r0 != 0) goto L_0x0053
            int r0 = com.xiaomi.push.fa.a(r2)     // Catch:{ Exception -> 0x004e }
            if (r0 == r1) goto L_0x0053
            goto L_0x002e
        L_0x004e:
            java.lang.String r2 = "PERF_ERROR : parse Command type error"
            com.xiaomi.channel.commonutils.logger.b.d(r2)
        L_0x0053:
            r1 = r3
            goto L_0x00bd
        L_0x0055:
            int r3 = r3.a()
            int r3 = com.xiaomi.push.eu.a((int) r3)
            if (r2 == 0) goto L_0x0053
            boolean r0 = r2 instanceof com.xiaomi.push.hx     // Catch:{ Exception -> 0x00ae }
            if (r0 == 0) goto L_0x0080
            com.xiaomi.push.hx r2 = (com.xiaomi.push.hx) r2     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = r2.f570d     // Catch:{ Exception -> 0x00ae }
            boolean r0 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00ae }
            if (r0 != 0) goto L_0x0033
            com.xiaomi.push.hq r0 = com.xiaomi.push.eu.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00ae }
            int r0 = com.xiaomi.push.eu.a((java.lang.Enum) r0)     // Catch:{ Exception -> 0x00ae }
            if (r0 == r1) goto L_0x0033
            com.xiaomi.push.hq r2 = com.xiaomi.push.eu.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00ae }
            int r2 = com.xiaomi.push.eu.a((java.lang.Enum) r2)     // Catch:{ Exception -> 0x00ae }
            goto L_0x0034
        L_0x0080:
            boolean r0 = r2 instanceof com.xiaomi.push.Cif     // Catch:{ Exception -> 0x00ae }
            if (r0 == 0) goto L_0x0053
            com.xiaomi.push.if r2 = (com.xiaomi.push.Cif) r2     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = r2.f630d     // Catch:{ Exception -> 0x00ae }
            boolean r0 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00ae }
            if (r0 != 0) goto L_0x0053
            com.xiaomi.push.hq r0 = com.xiaomi.push.eu.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00ae }
            int r0 = com.xiaomi.push.eu.a((java.lang.Enum) r0)     // Catch:{ Exception -> 0x00ae }
            if (r0 == r1) goto L_0x00a1
            com.xiaomi.push.hq r0 = com.xiaomi.push.eu.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00ae }
            int r0 = com.xiaomi.push.eu.a((java.lang.Enum) r0)     // Catch:{ Exception -> 0x00ae }
            r3 = r0
        L_0x00a1:
            com.xiaomi.push.hq r0 = com.xiaomi.push.hq.UploadTinyData     // Catch:{ Exception -> 0x00ae }
            com.xiaomi.push.hq r2 = com.xiaomi.push.eu.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00ae }
            boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x00ae }
            if (r2 == 0) goto L_0x0053
            goto L_0x00bd
        L_0x00ae:
            r1 = r3
            java.lang.String r2 = "PERF_ERROR : parse Notification type error"
            com.xiaomi.channel.commonutils.logger.b.d(r2)
            goto L_0x00bd
        L_0x00b5:
            int r2 = r3.a()
            int r1 = com.xiaomi.push.eu.a((int) r2)
        L_0x00bd:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.da.a(com.xiaomi.push.ir, com.xiaomi.push.hg):int");
    }

    public static void a(String str, Context context, int i, int i2) {
        if (i > 0 && i2 > 0) {
            int a = a(context, i2);
            if (i != eu.a((Enum) hq.UploadTinyData)) {
                ev.a(context.getApplicationContext()).a(str, i, 1, (long) a);
            }
        }
    }

    public static void a(String str, Context context, ic icVar, int i) {
        hg a;
        if (context != null && icVar != null && (a = icVar.a()) != null) {
            int a2 = a(a);
            if (i <= 0) {
                byte[] a3 = iq.a(icVar);
                i = a3 != null ? a3.length : 0;
            }
            a(str, context, a2, i);
        }
    }

    public static void a(String str, Context context, ir irVar, hg hgVar, int i) {
        a(str, context, a(irVar, hgVar), i);
    }

    public static void a(String str, Context context, byte[] bArr) {
        if (context != null && bArr != null && bArr.length > 0) {
            ic icVar = new ic();
            try {
                iq.a(icVar, bArr);
                a(str, context, icVar, bArr.length);
            } catch (iw unused) {
                b.a("fail to convert bytes to container");
            }
        }
    }
}
