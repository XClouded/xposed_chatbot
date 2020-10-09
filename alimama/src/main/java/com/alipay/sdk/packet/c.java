package com.alipay.sdk.packet;

import com.alipay.sdk.cons.a;
import com.alipay.sdk.encrypt.d;
import com.alipay.sdk.encrypt.e;
import com.alipay.sdk.util.n;
import java.util.Locale;

public final class c {
    private boolean a;
    private String b = n.a(24);

    public c(boolean z) {
        this.a = z;
    }

    public d a(b bVar, boolean z) {
        byte[] bArr;
        if (bVar == null) {
            return null;
        }
        byte[] bytes = bVar.a().getBytes();
        byte[] bytes2 = bVar.b().getBytes();
        if (z) {
            try {
                bytes2 = com.alipay.sdk.encrypt.c.a(bytes2);
            } catch (Exception unused) {
                z = false;
            }
        }
        if (this.a) {
            bArr = a(bytes, a(this.b, a.c), a(this.b, bytes2));
        } else {
            bArr = a(bytes, bytes2);
        }
        return new d(z, bArr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0067 A[SYNTHETIC, Splitter:B:29:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006d A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0079 A[SYNTHETIC, Splitter:B:41:0x0079] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alipay.sdk.packet.b a(com.alipay.sdk.packet.d r6) {
        /*
            r5 = this;
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            byte[] r2 = r6.b()     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            r1.<init>(r2)     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            r2 = 5
            byte[] r3 = new byte[r2]     // Catch:{ Exception -> 0x0059 }
            r1.read(r3)     // Catch:{ Exception -> 0x0059 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x0059 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0059 }
            int r3 = a((java.lang.String) r4)     // Catch:{ Exception -> 0x0059 }
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x0059 }
            r1.read(r3)     // Catch:{ Exception -> 0x0059 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x0059 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0059 }
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0057 }
            r1.read(r2)     // Catch:{ Exception -> 0x0057 }
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x0057 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x0057 }
            int r2 = a((java.lang.String) r3)     // Catch:{ Exception -> 0x0057 }
            if (r2 <= 0) goto L_0x0052
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0057 }
            r1.read(r2)     // Catch:{ Exception -> 0x0057 }
            boolean r3 = r5.a     // Catch:{ Exception -> 0x0057 }
            if (r3 == 0) goto L_0x0042
            java.lang.String r3 = r5.b     // Catch:{ Exception -> 0x0057 }
            byte[] r2 = b(r3, r2)     // Catch:{ Exception -> 0x0057 }
        L_0x0042:
            boolean r6 = r6.a()     // Catch:{ Exception -> 0x0057 }
            if (r6 == 0) goto L_0x004c
            byte[] r2 = com.alipay.sdk.encrypt.c.b(r2)     // Catch:{ Exception -> 0x0057 }
        L_0x004c:
            java.lang.String r6 = new java.lang.String     // Catch:{ Exception -> 0x0057 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x0057 }
            goto L_0x0053
        L_0x0052:
            r6 = r0
        L_0x0053:
            r1.close()     // Catch:{ Exception -> 0x006b }
            goto L_0x006b
        L_0x0057:
            r6 = move-exception
            goto L_0x0062
        L_0x0059:
            r6 = move-exception
            r4 = r0
            goto L_0x0062
        L_0x005c:
            r6 = move-exception
            r1 = r0
            goto L_0x0077
        L_0x005f:
            r6 = move-exception
            r1 = r0
            r4 = r1
        L_0x0062:
            com.alipay.sdk.util.c.a(r6)     // Catch:{ all -> 0x0076 }
            if (r1 == 0) goto L_0x006a
            r1.close()     // Catch:{ Exception -> 0x006a }
        L_0x006a:
            r6 = r0
        L_0x006b:
            if (r4 != 0) goto L_0x0070
            if (r6 != 0) goto L_0x0070
            return r0
        L_0x0070:
            com.alipay.sdk.packet.b r0 = new com.alipay.sdk.packet.b
            r0.<init>(r4, r6)
            return r0
        L_0x0076:
            r6 = move-exception
        L_0x0077:
            if (r1 == 0) goto L_0x007c
            r1.close()     // Catch:{ Exception -> 0x007c }
        L_0x007c:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.packet.c.a(com.alipay.sdk.packet.d):com.alipay.sdk.packet.b");
    }

    private static byte[] a(String str, String str2) {
        return d.a(str, str2);
    }

    private static byte[] a(String str, byte[] bArr) {
        return e.a(str, bArr);
    }

    private static byte[] b(String str, byte[] bArr) {
        return e.b(str, bArr);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:8|9|(1:11)|49|12|13|14|15|16|52) */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0034 */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c A[SYNTHETIC, Splitter:B:29:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0053 A[SYNTHETIC, Splitter:B:33:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x005b A[SYNTHETIC, Splitter:B:40:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0062 A[SYNTHETIC, Splitter:B:44:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] a(byte[]... r7) {
        /*
            r0 = 0
            if (r7 == 0) goto L_0x0066
            int r1 = r7.length
            if (r1 != 0) goto L_0x0008
            goto L_0x0066
        L_0x0008:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0044, all -> 0x0040 }
            r1.<init>()     // Catch:{ Exception -> 0x0044, all -> 0x0040 }
            java.io.DataOutputStream r2 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            r2.<init>(r1)     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            int r3 = r7.length     // Catch:{ Exception -> 0x0038 }
            r4 = 0
        L_0x0014:
            if (r4 >= r3) goto L_0x002a
            r5 = r7[r4]     // Catch:{ Exception -> 0x0038 }
            int r6 = r5.length     // Catch:{ Exception -> 0x0038 }
            java.lang.String r6 = a((int) r6)     // Catch:{ Exception -> 0x0038 }
            byte[] r6 = r6.getBytes()     // Catch:{ Exception -> 0x0038 }
            r2.write(r6)     // Catch:{ Exception -> 0x0038 }
            r2.write(r5)     // Catch:{ Exception -> 0x0038 }
            int r4 = r4 + 1
            goto L_0x0014
        L_0x002a:
            r2.flush()     // Catch:{ Exception -> 0x0038 }
            byte[] r7 = r1.toByteArray()     // Catch:{ Exception -> 0x0038 }
            r1.close()     // Catch:{ Exception -> 0x0034 }
        L_0x0034:
            r2.close()     // Catch:{ Exception -> 0x0057 }
            goto L_0x0057
        L_0x0038:
            r7 = move-exception
            goto L_0x0047
        L_0x003a:
            r7 = move-exception
            r2 = r0
            goto L_0x0059
        L_0x003d:
            r7 = move-exception
            r2 = r0
            goto L_0x0047
        L_0x0040:
            r7 = move-exception
            r1 = r0
            r2 = r1
            goto L_0x0059
        L_0x0044:
            r7 = move-exception
            r1 = r0
            r2 = r1
        L_0x0047:
            com.alipay.sdk.util.c.a(r7)     // Catch:{ all -> 0x0058 }
            if (r1 == 0) goto L_0x0051
            r1.close()     // Catch:{ Exception -> 0x0050 }
            goto L_0x0051
        L_0x0050:
        L_0x0051:
            if (r2 == 0) goto L_0x0056
            r2.close()     // Catch:{ Exception -> 0x0056 }
        L_0x0056:
            r7 = r0
        L_0x0057:
            return r7
        L_0x0058:
            r7 = move-exception
        L_0x0059:
            if (r1 == 0) goto L_0x0060
            r1.close()     // Catch:{ Exception -> 0x005f }
            goto L_0x0060
        L_0x005f:
        L_0x0060:
            if (r2 == 0) goto L_0x0065
            r2.close()     // Catch:{ Exception -> 0x0065 }
        L_0x0065:
            throw r7
        L_0x0066:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.packet.c.a(byte[][]):byte[]");
    }

    private static String a(int i) {
        return String.format(Locale.getDefault(), "%05d", new Object[]{Integer.valueOf(i)});
    }

    private static int a(String str) {
        return Integer.parseInt(str);
    }
}
