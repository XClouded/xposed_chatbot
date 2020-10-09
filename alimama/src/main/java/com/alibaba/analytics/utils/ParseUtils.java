package com.alibaba.analytics.utils;

public class ParseUtils {
    public static int parseInteger(String str) {
        if (!StringUtils.isNotBlank(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public static Double parseDouble(String str) {
        if (!StringUtils.isNotBlank(str)) {
            return null;
        }
        try {
            return Double.valueOf(Double.parseDouble(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
