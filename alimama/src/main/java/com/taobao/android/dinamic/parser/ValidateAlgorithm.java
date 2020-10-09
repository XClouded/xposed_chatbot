package com.taobao.android.dinamic.parser;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ValidateAlgorithm {
    public static final int MD5 = 1;
    public static final int MD5_AES = 2;
    public static final int MD5_RSA = 3;
    public static final int NONE = 0;

    public static byte[] generateByNone(byte[] bArr) {
        return bArr;
    }

    public static byte[] getCheckSum(byte[] bArr, int i) {
        if (i != 1) {
            return generateByNone(bArr);
        }
        try {
            return generateByMD5(bArr);
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public static byte[] generateByMD5(byte[] bArr) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        instance.update(bArr);
        return instance.digest();
    }
}
