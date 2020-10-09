package com.coloros.mcssdk.c;

import android.text.TextUtils;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class a {
    public static final String a = "AES/CBC/PKCS5Padding";
    public static final String b = "AES";
    public static final String c = "SHA1PRNG";
    public static final String d = "Crypto";
    public static final String e = "AndroidOpenSSL";
    public static final String f = "0123456789ABCDEF";
    public static final String g = "com.coloros.crypto.seed.defalut";
    public static byte[] h;

    static {
        try {
            h = c(g.getBytes(Charset.defaultCharset()), e);
        } catch (Exception e2) {
            e2.printStackTrace();
            d.b("getRawKey--Exception;" + e2.getMessage());
        }
    }

    private static String a(String str) {
        return b(str.getBytes(Charset.defaultCharset()));
    }

    private static String a(String str, String str2) {
        byte[] bArr;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            bArr = a(a(str.getBytes(Charset.defaultCharset())), str2.getBytes(Charset.defaultCharset()));
        } catch (Exception e2) {
            e2.printStackTrace();
            try {
                bArr = a(c(str.getBytes(Charset.defaultCharset()), e), str2.getBytes(Charset.defaultCharset()));
            } catch (Exception e3) {
                e3.printStackTrace();
                bArr = null;
            }
        }
        return b(bArr);
    }

    private static String a(byte[] bArr, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return b(a(bArr, str.getBytes(Charset.defaultCharset())));
    }

    private static void a(StringBuffer stringBuffer, byte b2) {
        stringBuffer.append(f.charAt((b2 >> 4) & 15));
        stringBuffer.append(f.charAt(b2 & 15));
    }

    private static byte[] a(byte[] bArr) {
        SecureRandom secureRandom;
        KeyGenerator instance = KeyGenerator.getInstance("AES");
        try {
            secureRandom = SecureRandom.getInstance(c, d);
        } catch (Exception e2) {
            SecureRandom instance2 = SecureRandom.getInstance(c, e);
            d.a("getRawKey exception:" + e2.getMessage());
            secureRandom = instance2;
        }
        secureRandom.setSeed(bArr);
        instance.init(128, secureRandom);
        return instance.generateKey().getEncoded();
    }

    private static byte[] a(byte[] bArr, byte[] bArr2) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance(a);
        instance.init(1, secretKeySpec, new IvParameterSpec(new byte[instance.getBlockSize()]));
        return instance.doFinal(bArr2);
    }

    private static String b(String str) {
        return new String(c(str), Charset.defaultCharset());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        r3 = b(c(r3.getBytes(java.nio.charset.Charset.defaultCharset()), e), c(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0035, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0036, code lost:
        r3.printStackTrace();
        r3 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x001e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String b(java.lang.String r3, java.lang.String r4) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.nio.charset.Charset r0 = java.nio.charset.Charset.defaultCharset()     // Catch:{ Exception -> 0x001e }
            byte[] r0 = r3.getBytes(r0)     // Catch:{ Exception -> 0x001e }
            byte[] r0 = a((byte[]) r0)     // Catch:{ Exception -> 0x001e }
            byte[] r2 = c(r4)     // Catch:{ Exception -> 0x001e }
            byte[] r0 = b((byte[]) r0, (byte[]) r2)     // Catch:{ Exception -> 0x001e }
            r3 = r0
            goto L_0x003a
        L_0x001e:
            java.nio.charset.Charset r0 = java.nio.charset.Charset.defaultCharset()     // Catch:{ Exception -> 0x0035 }
            byte[] r3 = r3.getBytes(r0)     // Catch:{ Exception -> 0x0035 }
            java.lang.String r0 = "AndroidOpenSSL"
            byte[] r3 = c(r3, r0)     // Catch:{ Exception -> 0x0035 }
            byte[] r4 = c(r4)     // Catch:{ Exception -> 0x0035 }
            byte[] r3 = b((byte[]) r3, (byte[]) r4)     // Catch:{ Exception -> 0x0035 }
            goto L_0x003a
        L_0x0035:
            r3 = move-exception
            r3.printStackTrace()
            r3 = r1
        L_0x003a:
            java.lang.String r4 = new java.lang.String
            java.nio.charset.Charset r0 = java.nio.charset.Charset.defaultCharset()
            r4.<init>(r3, r0)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coloros.mcssdk.c.a.b(java.lang.String, java.lang.String):java.lang.String");
    }

    private static String b(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b2 : bArr) {
            stringBuffer.append(f.charAt((b2 >> 4) & 15));
            stringBuffer.append(f.charAt(b2 & 15));
        }
        return stringBuffer.toString();
    }

    private static String b(byte[] bArr, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new String(b(bArr, c(str)), Charset.defaultCharset());
    }

    private static byte[] b(byte[] bArr, byte[] bArr2) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance(a);
        instance.init(2, secretKeySpec, new IvParameterSpec(new byte[instance.getBlockSize()]));
        return instance.doFinal(bArr2);
    }

    private static byte[] c(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = Integer.valueOf(str.substring(i2, i2 + 2), 16).byteValue();
        }
        return bArr;
    }

    private static byte[] c(byte[] bArr, String str) {
        KeyGenerator instance = KeyGenerator.getInstance("AES");
        SecureRandom instance2 = SecureRandom.getInstance(c, str);
        instance2.setSeed(bArr);
        instance.init(128, instance2);
        return instance.generateKey().getEncoded();
    }
}
