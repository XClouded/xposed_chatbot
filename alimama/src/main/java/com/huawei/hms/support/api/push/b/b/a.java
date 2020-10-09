package com.huawei.hms.support.api.push.b.b;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: AES128_CBC */
public class a {
    @SuppressLint({"TrulyRandom"})
    public static String a(String str, byte[] bArr) {
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length <= 0) {
            return "";
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher instance = Cipher.getInstance(com.coloros.mcssdk.c.a.a);
            byte[] bArr2 = new byte[16];
            new SecureRandom().nextBytes(bArr2);
            instance.init(1, secretKeySpec, new IvParameterSpec(bArr2));
            return a(com.huawei.hms.support.api.push.b.a.a.a.a(bArr2), com.huawei.hms.support.api.push.b.a.a.a.a(instance.doFinal(str.getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException | IllegalArgumentException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException unused) {
            com.huawei.hms.support.log.a.d("AES128_CBC", "aes cbc encrypter data error");
            return "";
        }
    }

    public static String b(String str, byte[] bArr) {
        if (TextUtils.isEmpty(str) || str.length() < 32 || bArr == null || bArr.length <= 0) {
            return "";
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher instance = Cipher.getInstance(com.coloros.mcssdk.c.a.a);
            String a = a(str);
            String b = b(str);
            if (!TextUtils.isEmpty(a)) {
                if (!TextUtils.isEmpty(b)) {
                    instance.init(2, secretKeySpec, new IvParameterSpec(com.huawei.hms.support.api.push.b.a.a.a.b(a)));
                    return new String(instance.doFinal(com.huawei.hms.support.api.push.b.a.a.a.b(b)), "UTF-8");
                }
            }
            com.huawei.hms.support.log.a.c("AES128_CBC", "iv or enData is null");
            return "";
        } catch (UnsupportedEncodingException | IllegalArgumentException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException unused) {
            com.huawei.hms.support.log.a.d("AES128_CBC", "aes cbc decrypter data error");
            return "";
        }
    }

    private static String a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (str2.length() >= 16 && str.length() >= 16) {
            stringBuffer.append(str2.substring(0, 6));
            stringBuffer.append(str.substring(0, 6));
            stringBuffer.append(str2.substring(6, 10));
            stringBuffer.append(str.substring(6, 16));
            stringBuffer.append(str2.substring(10, 16));
            stringBuffer.append(str.substring(16));
            stringBuffer.append(str2.substring(16));
        }
        return stringBuffer.toString();
    }

    private static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() >= 48) {
            stringBuffer.append(str.substring(6, 12));
            stringBuffer.append(str.substring(16, 26));
            stringBuffer.append(str.substring(32, 48));
        }
        return stringBuffer.toString();
    }

    private static String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() >= 48) {
            stringBuffer.append(str.substring(0, 6));
            stringBuffer.append(str.substring(12, 16));
            stringBuffer.append(str.substring(26, 32));
            stringBuffer.append(str.substring(48));
        }
        return stringBuffer.toString();
    }
}
