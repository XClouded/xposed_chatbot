package com.taobao.android.sso.v2.launch.util;

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
            java.lang.String r6 = com.taobao.android.sso.v2.launch.util.Base64.encode(r6)     // Catch:{ Exception -> 0x004d, Throwable -> 0x004b }
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.sso.v2.launch.util.Rsa.encrypt(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065 A[SYNTHETIC, Splitter:B:25:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0075 A[SYNTHETIC, Splitter:B:34:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007e A[SYNTHETIC, Splitter:B:40:0x007e] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0060=Splitter:B:22:0x0060, B:31:0x0070=Splitter:B:31:0x0070} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String decrypt(java.lang.String r5, java.lang.String r6) {
        /*
            r0 = 0
            java.security.spec.PKCS8EncodedKeySpec r1 = new java.security.spec.PKCS8EncodedKeySpec     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            byte[] r6 = com.taobao.android.sso.v2.launch.util.Base64.decode(r6)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            r1.<init>(r6)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            java.lang.String r6 = "RSA"
            java.security.KeyFactory r6 = java.security.KeyFactory.getInstance(r6)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            java.security.PrivateKey r6 = r6.generatePrivate(r1)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            java.lang.String r1 = "RSA/ECB/PKCS1Padding"
            javax.crypto.Cipher r1 = javax.crypto.Cipher.getInstance(r1)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            r2 = 2
            r1.init(r2, r6)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            byte[] r5 = com.taobao.android.sso.v2.launch.util.Base64.decode(r5)     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            int r6 = r1.getBlockSize()     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            r2.<init>()     // Catch:{ Exception -> 0x006e, Throwable -> 0x005e, all -> 0x005c }
            r3 = 0
        L_0x002c:
            int r4 = r5.length     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            if (r3 >= r4) goto L_0x0040
            int r4 = r5.length     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            int r4 = r4 - r3
            if (r4 >= r6) goto L_0x0036
            int r4 = r5.length     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            int r4 = r4 - r3
            goto L_0x0037
        L_0x0036:
            r4 = r6
        L_0x0037:
            byte[] r4 = r1.doFinal(r5, r3, r4)     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            r2.write(r4)     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            int r3 = r3 + r6
            goto L_0x002c
        L_0x0040:
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            byte[] r6 = r2.toByteArray()     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            java.lang.String r1 = "UTF-8"
            java.nio.charset.Charset r1 = java.nio.charset.Charset.forName(r1)     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            r5.<init>(r6, r1)     // Catch:{ Exception -> 0x005a, Throwable -> 0x0058 }
            r2.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0079
        L_0x0053:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x0079
        L_0x0058:
            r5 = move-exception
            goto L_0x0060
        L_0x005a:
            r5 = move-exception
            goto L_0x0070
        L_0x005c:
            r5 = move-exception
            goto L_0x007c
        L_0x005e:
            r5 = move-exception
            r2 = r0
        L_0x0060:
            r5.printStackTrace()     // Catch:{ all -> 0x007a }
            if (r2 == 0) goto L_0x0078
            r2.close()     // Catch:{ IOException -> 0x0069 }
            goto L_0x0078
        L_0x0069:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0078
        L_0x006e:
            r5 = move-exception
            r2 = r0
        L_0x0070:
            r5.printStackTrace()     // Catch:{ all -> 0x007a }
            if (r2 == 0) goto L_0x0078
            r2.close()     // Catch:{ IOException -> 0x0069 }
        L_0x0078:
            r5 = r0
        L_0x0079:
            return r5
        L_0x007a:
            r5 = move-exception
            r0 = r2
        L_0x007c:
            if (r0 == 0) goto L_0x0086
            r0.close()     // Catch:{ IOException -> 0x0082 }
            goto L_0x0086
        L_0x0082:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0086:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.sso.v2.launch.util.Rsa.decrypt(java.lang.String, java.lang.String):java.lang.String");
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

    public static boolean doCheck(String str, String str2, String str3) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str3)));
            Signature instance = Signature.getInstance("SHA1WithRSA");
            instance.initVerify(generatePublic);
            instance.update(str.getBytes("utf-8"));
            return instance.verify(Base64.decode(str2));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
