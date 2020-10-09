package com.huawei.hianalytics.f.g;

import com.huawei.hianalytics.g.b;
import com.taobao.android.tlog.protocol.utils.RSAUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class f {

    public static class a extends Exception {
        public a() {
            super("Fail to encrypt with RSA");
        }
    }

    public static String a(String str) {
        try {
            return b(str);
        } catch (a unused) {
            b.c("RsaCryPter", "rsaEncrypt(): Fail to encrypt with RSA!");
            return "";
        }
    }

    private static byte[] a(byte[] bArr) {
        try {
            PublicKey c = c(com.huawei.hianalytics.f.a.a.b() + "2d1e55658d041b98ce28d81f5c7fe8b85b528f6afea350f28da6e833df875e19a6c71c59050298b28323c8910980c12a8e731e0c47dc14da076e88e25a8b7e9a7c33b27baf12e1c9de861523af15f577789389b700578670b6e37ff5e" + e.d());
            if (c != null) {
                Cipher instance = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
                instance.init(1, c);
                return instance.doFinal(bArr);
            }
            throw new UnsupportedEncodingException();
        } catch (UnsupportedEncodingException unused) {
            b.c("RsaCryPter", "rsaEncrypt(): getBytes - Unsupported coding format!");
            throw new a();
        } catch (NoSuchAlgorithmException unused2) {
            b.c("RsaCryPter", "rsaEncrypt(): getInstance - No such algorithm,transformation");
            throw new a();
        } catch (InvalidKeyException unused3) {
            b.c("RsaCryPter", "rsaEncrypt(): init - Invalid key!");
            throw new a();
        } catch (NoSuchPaddingException unused4) {
            b.c("RsaCryPter", "rsaEncrypt():  No such filling parameters ");
            throw new a();
        } catch (BadPaddingException unused5) {
            b.c("RsaCryPter", "rsaEncrypt():False filling parameters!");
            throw new a();
        } catch (InvalidKeySpecException unused6) {
            b.c("RsaCryPter", "rsaEncrypt(): Invalid key specification");
            throw new a();
        } catch (IllegalBlockSizeException unused7) {
            b.c("RsaCryPter", "rsaEncrypt(): doFinal - The provided block is not filled with");
            throw new a();
        }
    }

    private static String b(String str) {
        try {
            return e.a(a(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException unused) {
            b.c("RsaCryPter", "rsaEncrypt(): Unsupported Encoding - utf-8!");
            throw new a();
        }
    }

    private static PublicKey c(String str) {
        return KeyFactory.getInstance(RSAUtils.KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(e.a(str)));
    }
}
