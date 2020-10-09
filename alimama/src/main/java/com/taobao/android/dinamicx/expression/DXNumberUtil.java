package com.taobao.android.dinamicx.expression;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;

public class DXNumberUtil {
    public static boolean parseBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof String) {
            return parseStringBoolean((String) obj);
        }
        if (obj instanceof JSONArray) {
            if (((JSONArray) obj).size() > 0) {
                return true;
            }
        } else if (obj instanceof JSONObject) {
            if (((JSONObject) obj).size() > 0) {
                return true;
            }
        } else if (!(obj instanceof Number) || ((Number) obj).intValue() != 0) {
            return true;
        }
        return false;
    }

    private static boolean parseStringBoolean(String str) {
        return str != null && !str.equals("false") && !str.equalsIgnoreCase("0") && !str.isEmpty();
    }

    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public static boolean compareNumber(Number number, Number number2) {
        return number.doubleValue() == number2.doubleValue();
    }

    public static boolean hasDigit(String str) {
        if (!TextUtils.isEmpty(str) && str.indexOf(".") != -1) {
            return true;
        }
        return false;
    }

    public static boolean isFloatPointNum(Object obj) {
        return (obj instanceof Double) || (obj instanceof Float) || (obj instanceof BigDecimal);
    }

    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Throwable unused) {
            return 0.0d;
        }
    }

    public static long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Throwable unused) {
            return 0;
        }
    }
}
