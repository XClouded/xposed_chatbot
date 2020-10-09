package com.taobao.alivfssdk.utils;

import android.util.Base64;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;

public class EncryptUtils {
    private static String CIPHER_ALGORITHM = "AES/CFB8/NoPadding";
    private static String KEY_ALGORITHM = "AES";
    private static String PASSWORD_HASH_ALGORITHM = "SHA-256";

    /* JADX WARNING: Removed duplicated region for block: B:18:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] createChecksum(java.io.File r4) throws java.lang.Exception {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0027 }
            r1.<init>(r4)     // Catch:{ all -> 0x0027 }
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch:{ all -> 0x0025 }
            java.lang.String r0 = "MD5"
            java.security.MessageDigest r0 = java.security.MessageDigest.getInstance(r0)     // Catch:{ all -> 0x0025 }
        L_0x0010:
            int r2 = r1.read(r4)     // Catch:{ all -> 0x0025 }
            if (r2 <= 0) goto L_0x001a
            r3 = 0
            r0.update(r4, r3, r2)     // Catch:{ all -> 0x0025 }
        L_0x001a:
            r3 = -1
            if (r2 != r3) goto L_0x0010
            byte[] r4 = r0.digest()     // Catch:{ all -> 0x0025 }
            r1.close()
            return r4
        L_0x0025:
            r4 = move-exception
            goto L_0x0029
        L_0x0027:
            r4 = move-exception
            r1 = r0
        L_0x0029:
            if (r1 == 0) goto L_0x002e
            r1.close()
        L_0x002e:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.utils.EncryptUtils.createChecksum(java.io.File):byte[]");
    }

    public static String md5(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            byte[] createChecksum = createChecksum(file);
            String str = "";
            for (int i = 0; i < createChecksum.length; i++) {
                str = str + Integer.toString((createChecksum[i] & UByte.MAX_VALUE) + 256, 16).substring(1);
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String str) {
        try {
            byte[] digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5).digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                byte b2 = b & UByte.MAX_VALUE;
                if (b2 < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b2));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e2);
        }
    }

    public static String md5(byte[] bArr) {
        try {
            byte[] digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5).digest(bArr);
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                byte b2 = b & UByte.MAX_VALUE;
                if (b2 < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b2));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
    }

    public static String getBase64(String str) {
        return new String(Base64.encode(str.getBytes(), 11));
    }

    public static String getFromBase64(String str) {
        return new String(Base64.decode(str.getBytes(), 11));
    }

    public static InputStream newCipherInputStream(String str, InputStream inputStream) throws Exception {
        return new CipherInputStream(inputStream, buildCipher(str, 2));
    }

    public static OutputStream newCipherOutputStream(String str, OutputStream outputStream) throws Exception {
        return new CipherOutputStream(outputStream, buildCipher(str, 1));
    }

    private static Cipher buildCipher(String str, int i) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
        Key buildKey = buildKey(str);
        byte[] bArr = new byte[instance.getBlockSize()];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = 0;
        }
        try {
            instance.init(i, buildKey, new IvParameterSpec(bArr));
            return instance;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Key buildKey(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest instance = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
        instance.update(str.getBytes("UTF-8"));
        return new SecretKeySpec(instance.digest(), KEY_ALGORITHM);
    }
}
