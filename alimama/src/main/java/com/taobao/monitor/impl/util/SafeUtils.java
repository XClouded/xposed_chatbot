package com.taobao.monitor.impl.util;

public class SafeUtils {
    private SafeUtils() {
    }

    public static String getSafeString(Object obj, String str) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj != null ? obj.toString() : str;
    }
}
