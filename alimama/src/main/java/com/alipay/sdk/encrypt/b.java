package com.alipay.sdk.encrypt;

import com.alipay.sdk.util.c;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class b {
    public static String a(String str, String str2) {
        return a(1, str, str2);
    }

    public static String b(String str, String str2) {
        return a(2, str, str2);
    }

    public static String a(int i, String str, String str2) {
        byte[] bArr;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "DES");
            Cipher instance = Cipher.getInstance("DES");
            instance.init(i, secretKeySpec);
            if (i == 2) {
                bArr = a.a(str);
            } else {
                bArr = str.getBytes("UTF-8");
            }
            byte[] doFinal = instance.doFinal(bArr);
            if (i == 2) {
                return new String(doFinal);
            }
            return a.a(doFinal);
        } catch (Exception e) {
            c.a(e);
            return null;
        }
    }
}
