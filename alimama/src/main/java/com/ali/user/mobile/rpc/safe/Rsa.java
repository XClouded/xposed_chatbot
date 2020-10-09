package com.ali.user.mobile.rpc.safe;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static PublicKey getPublicKeyFromX509(String str, String str2) throws NoSuchAlgorithmException, Exception {
        KeyFactory keyFactory;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(str2));
        try {
            keyFactory = KeyFactory.getInstance(str);
        } catch (Throwable unused) {
            keyFactory = KeyFactory.getInstance(str);
        }
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(str);
        }
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0059 A[SYNTHETIC, Splitter:B:26:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0069 A[SYNTHETIC, Splitter:B:35:0x0069] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0071 A[SYNTHETIC, Splitter:B:40:0x0071] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0054=Splitter:B:23:0x0054, B:32:0x0064=Splitter:B:32:0x0064} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String encrypt(java.lang.String r5, java.lang.String r6) {
        /*
            r0 = 0
            java.lang.String r1 = "RSA"
            java.security.PublicKey r6 = getPublicKeyFromX509(r1, r6)     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            java.lang.String r1 = "RSA/ECB/PKCS1Padding"
            javax.crypto.Cipher r1 = javax.crypto.Cipher.getInstance(r1)     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            r2 = 1
            r1.init(r2, r6)     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            java.lang.String r6 = "UTF-8"
            byte[] r5 = r5.getBytes(r6)     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            int r6 = r1.getBlockSize()     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            r2.<init>()     // Catch:{ Exception -> 0x0062, Throwable -> 0x0052, all -> 0x004f }
            r3 = 0
        L_0x0021:
            int r4 = r5.length     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            if (r3 >= r4) goto L_0x0035
            int r4 = r5.length     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            int r4 = r4 - r3
            if (r4 >= r6) goto L_0x002b
            int r4 = r5.length     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            int r4 = r4 - r3
            goto L_0x002c
        L_0x002b:
            r4 = r6
        L_0x002c:
            byte[] r4 = r1.doFinal(r5, r3, r4)     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            r2.write(r4)     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            int r3 = r3 + r6
            goto L_0x0021
        L_0x0035:
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            byte[] r6 = r2.toByteArray()     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            java.lang.String r6 = com.ali.user.mobile.rpc.safe.Base64.encode(r6)     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            r5.<init>(r6)     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
            r2.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x006d
        L_0x0046:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x006d
        L_0x004b:
            r5 = move-exception
            goto L_0x0054
        L_0x004d:
            r5 = move-exception
            goto L_0x0064
        L_0x004f:
            r5 = move-exception
            r2 = r0
            goto L_0x006f
        L_0x0052:
            r5 = move-exception
            r2 = r0
        L_0x0054:
            r5.printStackTrace()     // Catch:{ all -> 0x006e }
            if (r2 == 0) goto L_0x006c
            r2.close()     // Catch:{ IOException -> 0x005d }
            goto L_0x006c
        L_0x005d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x006c
        L_0x0062:
            r5 = move-exception
            r2 = r0
        L_0x0064:
            r5.printStackTrace()     // Catch:{ all -> 0x006e }
            if (r2 == 0) goto L_0x006c
            r2.close()     // Catch:{ IOException -> 0x005d }
        L_0x006c:
            r5 = r0
        L_0x006d:
            return r5
        L_0x006e:
            r5 = move-exception
        L_0x006f:
            if (r2 == 0) goto L_0x0079
            r2.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0079
        L_0x0075:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0079:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.rpc.safe.Rsa.encrypt(java.lang.String, java.lang.String):java.lang.String");
    }

    public static String sign(String str, String str2) {
        try {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2)));
            Signature instance = Signature.getInstance("SHA1WithRSA");
            instance.initSign(generatePrivate);
            instance.update(str.getBytes("utf-8"));
            return Base64.encode(instance.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
