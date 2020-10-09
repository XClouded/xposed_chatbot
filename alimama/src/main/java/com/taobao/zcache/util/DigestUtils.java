package com.taobao.zcache.util;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import kotlin.UByte;

public class DigestUtils {
    public static String md5ToHex(String str) {
        if (str == null) {
            return null;
        }
        try {
            return digest(str.getBytes("utf-8"), MessageDigestAlgorithms.MD5);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String digest(byte[] bArr, String str) {
        return bytesToHexString(digest2byte(bArr, str));
    }

    private static byte[] digest2byte(byte[] bArr, String str) {
        try {
            return MessageDigest.getInstance(str).digest(bArr);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    private static String bytesToHexString(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }
}
