package com.vivo.push.util;

import com.coloros.mcssdk.c.a;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: CryptographicTool */
public final class g {
    public static String a(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = (char) (bArr[i] ^ 16);
        }
        return new String(cArr);
    }

    public static byte[] a(String str, String str2, byte[] bArr) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("utf-8"), "AES");
        Cipher instance = Cipher.getInstance(a.a);
        instance.init(2, secretKeySpec, new IvParameterSpec(str.getBytes("utf-8")));
        return instance.doFinal(bArr);
    }
}
