package com.ta.utdid2.android.utils;

import com.coloros.mcssdk.c.a;
import com.ta.audid.utils.RC4;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String IV = "IUQSvE6r1TfFPdPEjfklLw==";

    public static String encrypt(String str) {
        byte[] bArr;
        try {
            bArr = encrypt(getRawKey(), str.getBytes());
        } catch (Exception unused) {
            bArr = null;
        }
        if (bArr != null) {
            return toHex(bArr);
        }
        return null;
    }

    public static String decrypt(String str) {
        try {
            return new String(decrypt(getRawKey(), toByte(str)));
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] getRawKey() throws Exception {
        return RC4.rc4(new byte[]{Framer.ENTER_FRAME_PREFIX, 83, -50, -89, -84, -114, 80, 99, 10, 63, 22, -65, -11, 30, 101, -118});
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance(a.a);
        instance.init(1, secretKeySpec, new IvParameterSpec(getIvBytes()));
        return instance.doFinal(bArr2);
    }

    private static byte[] decrypt(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance(a.a);
        instance.init(2, secretKeySpec, new IvParameterSpec(getIvBytes()));
        return instance.doFinal(bArr2);
    }

    private static byte[] toByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = Integer.valueOf(str.substring(i2, i2 + 2), 16).byteValue();
        }
        return bArr;
    }

    private static String toHex(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte appendHex : bArr) {
            appendHex(stringBuffer, appendHex);
        }
        return stringBuffer.toString();
    }

    private static void appendHex(StringBuffer stringBuffer, byte b) {
        stringBuffer.append(a.f.charAt((b >> 4) & 15));
        stringBuffer.append(a.f.charAt(b & 15));
    }

    private static byte[] getIvBytes() {
        try {
            byte[] decode = Base64.decode(IV.getBytes("UTF-8"), 2);
            if (decode != null) {
                return RC4.rc4(decode);
            }
        } catch (Exception unused) {
        }
        return new byte[16];
    }
}
