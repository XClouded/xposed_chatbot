package com.huawei.hianalytics.log.e;

import android.text.TextUtils;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.coloros.mcssdk.c.a;
import com.huawei.hianalytics.util.c;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class b {
    public static String a(String str) {
        String str2;
        String str3;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            instance.update(str.getBytes("UTF-8"));
            return c.a(instance.digest());
        } catch (NoSuchAlgorithmException unused) {
            str2 = "LogCrypter";
            str3 = "getSHA256StrJava, getInstance - No such algorithm,transformation!";
            com.huawei.hianalytics.g.b.d(str2, str3);
            return "";
        } catch (UnsupportedEncodingException unused2) {
            str2 = "LogCrypter";
            str3 = "getSHA256StrJava, getBytes - Unsupported coding format!";
            com.huawei.hianalytics.g.b.d(str2, str3);
            return "";
        }
    }

    public static String a(Key key) {
        return c.a(key.getEncoded());
    }

    public static Cipher a(int i, Key key, byte[] bArr) {
        String str;
        String str2;
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(i, key, ivParameterSpec);
            return instance;
        } catch (NoSuchPaddingException unused) {
            str2 = "LogCrypter";
            str = "AES:getCipher() No such filling parameters !";
            com.huawei.hianalytics.g.b.c(str2, str);
            return null;
        } catch (NoSuchAlgorithmException unused2) {
            str2 = "LogCrypter";
            str = "AES:getCipher() getInstance - No such algorithm,transformation!";
            com.huawei.hianalytics.g.b.c(str2, str);
            return null;
        } catch (InvalidAlgorithmParameterException unused3) {
            str2 = "LogCrypter";
            str = "AES:getCipher() Invalid algorithm parameters";
            com.huawei.hianalytics.g.b.c(str2, str);
            return null;
        } catch (InvalidKeyException unused4) {
            str2 = "LogCrypter";
            str = "AES:getCipher() init - Invalid key!";
            com.huawei.hianalytics.g.b.c(str2, str);
            return null;
        }
    }

    public static byte[] a() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static Key b() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return new SecretKeySpec(bArr, "AES");
    }
}
