package com.taobao.aipc.utils;

public class IPCUtils {
    public static int getUserIdFromAuthority(String str, int i) {
        int lastIndexOf;
        if (str == null || (lastIndexOf = str.lastIndexOf(64)) == -1) {
            return i;
        }
        try {
            return Integer.parseInt(str.substring(0, lastIndexOf));
        } catch (NumberFormatException unused) {
            return -10000;
        }
    }

    public static String getAuthorityWithoutUserId(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(str.lastIndexOf(64) + 1);
    }

    public static String getAuthorities(String str, String str2) {
        return "content://" + str + str2;
    }
}
