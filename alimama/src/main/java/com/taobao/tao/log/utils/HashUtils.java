package com.taobao.tao.log.utils;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.security.MessageDigest;

public class HashUtils {
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final int MD5_BIT16 = 1;
    public static final int MD5_BIT32 = 0;

    public static String hash(String str, String str2) {
        if (str != null) {
            try {
                MessageDigest instance = MessageDigest.getInstance(str2);
                instance.update(str.getBytes());
                return convertToHex(instance.digest());
            } catch (Exception unused) {
            }
        }
        return "";
    }

    public static String convertToHex(byte[] bArr) {
        int length = bArr.length;
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            sb.append(HEX[(bArr[i] >> 4) & 15]);
            sb.append(HEX[bArr[i] & 15]);
        }
        return sb.toString();
    }

    public static String md5(String str, int i) {
        if (i == 0) {
            return hash(str, MessageDigestAlgorithms.MD5);
        }
        if (i != 1) {
            return "";
        }
        String hash = hash(str, MessageDigestAlgorithms.MD5);
        if (hash.length() == 32) {
            return hash.substring(8, 24);
        }
        return "";
    }
}
