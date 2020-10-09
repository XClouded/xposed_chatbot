package com.alibaba.motu.tbrest.utils;

import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence == null || (length = charSequence.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    public static String defaultString(String str, String str2) {
        return isBlank(str) ? str2 : str;
    }

    public static int hashCode(String str) {
        if (str.length() <= 0) {
            return 0;
        }
        int i = 0;
        for (char c : str.toCharArray()) {
            i = (i * 31) + c;
        }
        return i;
    }

    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return ((String) obj).toString();
        }
        if (obj instanceof Integer) {
            return "" + ((Integer) obj).intValue();
        } else if (obj instanceof Long) {
            return "" + ((Long) obj).longValue();
        } else if (obj instanceof Double) {
            return "" + ((Double) obj).doubleValue();
        } else if (obj instanceof Float) {
            return "" + ((Float) obj).floatValue();
        } else if (obj instanceof Short) {
            return "" + ((Short) obj).shortValue();
        } else if (obj instanceof Byte) {
            return "" + ((Byte) obj).byteValue();
        } else if (obj instanceof Boolean) {
            return ((Boolean) obj).toString();
        } else {
            if (obj instanceof Character) {
                return ((Character) obj).toString();
            }
            return obj.toString();
        }
    }

    public static String convertMapToString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (String next : map.keySet()) {
            String str = map.get(next);
            if (!(str == null || next == null)) {
                if (z) {
                    if (!"--invalid--".equals(str)) {
                        stringBuffer.append(next + SymbolExpUtil.SYMBOL_EQUAL + str);
                    } else {
                        stringBuffer.append(next);
                    }
                    z = false;
                } else if (!"--invalid--".equals(str)) {
                    stringBuffer.append(",");
                    stringBuffer.append(next + SymbolExpUtil.SYMBOL_EQUAL + str);
                } else {
                    stringBuffer.append(",");
                    stringBuffer.append(next);
                }
            }
        }
        return stringBuffer.toString();
    }
}
