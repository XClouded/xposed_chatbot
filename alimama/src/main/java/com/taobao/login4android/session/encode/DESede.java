package com.taobao.login4android.session.encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DESede {
    private static final String Algorithm = "DESede";
    public static final String ISO88591 = "ISO-8859-1";

    public static byte[] decryptMode(byte[] bArr, byte[] bArr2) {
        try {
            byte[] bArr3 = new byte[24];
            if (bArr.length >= 24) {
                System.arraycopy(bArr, 0, bArr3, 0, 24);
            } else {
                System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
            }
            SecretKey generateSecret = SecretKeyFactory.getInstance(Algorithm).generateSecret(new DESedeKeySpec(bArr3));
            Cipher instance = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            instance.init(2, generateSecret);
            return instance.doFinal(bArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
