package com.huawei.hianalytics.f.g;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import com.coloros.mcssdk.c.a;
import com.huawei.hianalytics.g.b;
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

public abstract class c {
    public static Pair<byte[], String> a(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 32) {
            return new Pair<>(new byte[0], str);
        }
        String substring = str.substring(0, 32);
        return new Pair<>(e.a(substring), str.substring(32));
    }

    public static String a(String str, Context context) {
        String a = a.a(context).a();
        Pair<byte[], String> a2 = a(str);
        return b(a, (byte[]) a2.first, (String) a2.second);
    }

    public static String a(String str, byte[] bArr, String str2) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str) || bArr == null || bArr.length == 0) {
            return "";
        }
        try {
            return e.a(b(str, bArr, str2.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException unused) {
            b.c("AesCrypter", "aesEncrypt():getBytes - Unsupported coding format!");
            return "";
        }
    }

    public static String a(String str, byte[] bArr, byte[] bArr2) {
        return (bArr2 == null || TextUtils.isEmpty(str) || bArr == null || bArr.length == 0) ? "" : e.a(b(str, bArr, bArr2));
    }

    public static String a(byte[] bArr, String str) {
        if (bArr == null || bArr.length == 0) {
            return str;
        }
        if (str == null) {
            str = "";
        }
        return e.a(bArr).concat(str);
    }

    public static byte[] a() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static String b(String str, Context context) {
        String a = a.a(context).a();
        byte[] a2 = a();
        return a(a2, a(a, a2, str));
    }

    public static String b(String str, byte[] bArr, String str2) {
        String str3;
        String str4;
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length == 0 || TextUtils.isEmpty(str2)) {
            return "";
        }
        try {
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(2, new SecretKeySpec(e.a(str), "AES"), new IvParameterSpec(bArr));
            return new String(instance.doFinal(e.a(str2)), "UTF-8");
        } catch (NoSuchAlgorithmException unused) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): getInstance - No such algorithm,transformation";
            b.c(str4, str3);
            return "";
        } catch (InvalidKeyException unused2) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): init - Invalid key!";
            b.c(str4, str3);
            return "";
        } catch (InvalidAlgorithmParameterException unused3) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): init - Invalid algorithm parameters !";
            b.c(str4, str3);
            return "";
        } catch (NoSuchPaddingException unused4) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt():  No such filling parameters ";
            b.c(str4, str3);
            return "";
        } catch (BadPaddingException unused5) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): False filling parameters!";
            b.c(str4, str3);
            return "";
        } catch (UnsupportedEncodingException unused6) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): getBytes - Unsupported coding format!";
            b.c(str4, str3);
            return "";
        } catch (IllegalBlockSizeException unused7) {
            str4 = "AesCrypter";
            str3 = "aesDecrypt(): doFinal - The provided block is not filled with";
            b.c(str4, str3);
            return "";
        }
    }

    private static byte[] b(String str, byte[] bArr, byte[] bArr2) {
        String str2;
        String str3;
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length == 0) {
            return new byte[0];
        }
        try {
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(1, new SecretKeySpec(e.a(str), "AES"), new IvParameterSpec(bArr));
            return instance.doFinal(bArr2);
        } catch (NoSuchAlgorithmException unused) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): getInstance - No such algorithm,transformation";
            b.c(str2, str3);
            return new byte[0];
        } catch (InvalidKeyException unused2) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): init - Invalid key!";
            b.c(str2, str3);
            return new byte[0];
        } catch (InvalidAlgorithmParameterException unused3) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): init - Invalid algorithm parameters !";
            b.c(str2, str3);
            return new byte[0];
        } catch (NoSuchPaddingException unused4) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): No such filling parameters ";
            b.c(str2, str3);
            return new byte[0];
        } catch (BadPaddingException unused5) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): False filling parameters!";
            b.c(str2, str3);
            return new byte[0];
        } catch (IllegalBlockSizeException unused6) {
            str2 = "AesCrypter";
            str3 = "aesEncrypt(): doFinal - The provided block is not filled with";
            b.c(str2, str3);
            return new byte[0];
        }
    }
}
