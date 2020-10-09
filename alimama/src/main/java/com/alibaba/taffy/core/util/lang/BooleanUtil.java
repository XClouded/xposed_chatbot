package com.alibaba.taffy.core.util.lang;

public class BooleanUtil {
    public static boolean toBoolean(int i) {
        return i != 0;
    }

    public static boolean toBoolean(Object obj) {
        return toBoolean(obj, false);
    }

    public static boolean toBoolean(Object obj, boolean z) {
        if (obj == null) {
            return z;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue() != 0;
        }
        return "true".equalsIgnoreCase(obj.toString());
    }
}
