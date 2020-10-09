package com.alipay.sdk.encrypt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class d {
    private static final String a = "RSA";

    private static PublicKey b(String str, String str2) throws NoSuchAlgorithmException, Exception {
        return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(a.a(str2)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x004d A[SYNTHETIC, Splitter:B:24:0x004d] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005b A[SYNTHETIC, Splitter:B:32:0x005b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] a(java.lang.String r5, java.lang.String r6) {
        /*
            r0 = 0
            java.lang.String r1 = "RSA"
            java.security.PublicKey r6 = b(r1, r6)     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            java.lang.String r1 = "RSA/ECB/PKCS1Padding"
            javax.crypto.Cipher r1 = javax.crypto.Cipher.getInstance(r1)     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            r2 = 1
            r1.init(r2, r6)     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            java.lang.String r6 = "UTF-8"
            byte[] r5 = r5.getBytes(r6)     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            int r6 = r1.getBlockSize()     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            r2.<init>()     // Catch:{ Exception -> 0x0046, all -> 0x0044 }
            r3 = 0
        L_0x0021:
            int r4 = r5.length     // Catch:{ Exception -> 0x0042 }
            if (r3 >= r4) goto L_0x0035
            int r4 = r5.length     // Catch:{ Exception -> 0x0042 }
            int r4 = r4 - r3
            if (r4 >= r6) goto L_0x002b
            int r4 = r5.length     // Catch:{ Exception -> 0x0042 }
            int r4 = r4 - r3
            goto L_0x002c
        L_0x002b:
            r4 = r6
        L_0x002c:
            byte[] r4 = r1.doFinal(r5, r3, r4)     // Catch:{ Exception -> 0x0042 }
            r2.write(r4)     // Catch:{ Exception -> 0x0042 }
            int r3 = r3 + r6
            goto L_0x0021
        L_0x0035:
            byte[] r5 = r2.toByteArray()     // Catch:{ Exception -> 0x0042 }
            r2.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x0056
        L_0x003d:
            r6 = move-exception
            com.alipay.sdk.util.c.a(r6)
            goto L_0x0056
        L_0x0042:
            r5 = move-exception
            goto L_0x0048
        L_0x0044:
            r5 = move-exception
            goto L_0x0059
        L_0x0046:
            r5 = move-exception
            r2 = r0
        L_0x0048:
            com.alipay.sdk.util.c.a(r5)     // Catch:{ all -> 0x0057 }
            if (r2 == 0) goto L_0x0055
            r2.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0055
        L_0x0051:
            r5 = move-exception
            com.alipay.sdk.util.c.a(r5)
        L_0x0055:
            r5 = r0
        L_0x0056:
            return r5
        L_0x0057:
            r5 = move-exception
            r0 = r2
        L_0x0059:
            if (r0 == 0) goto L_0x0063
            r0.close()     // Catch:{ IOException -> 0x005f }
            goto L_0x0063
        L_0x005f:
            r6 = move-exception
            com.alipay.sdk.util.c.a(r6)
        L_0x0063:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.encrypt.d.a(java.lang.String, java.lang.String):byte[]");
    }
}
