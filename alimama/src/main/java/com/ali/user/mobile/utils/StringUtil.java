package com.ali.user.mobile.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.android.dinamic.DinamicConstant;

public class StringUtil {
    protected static final String CALLBACK = "https://www.alipay.com/webviewbridge";

    public static boolean checkWebviewBridge(String str) {
        Uri parse = Uri.parse(str);
        StringBuilder sb = new StringBuilder();
        sb.append(parse.getAuthority());
        sb.append(parse.getPath());
        return CALLBACK.contains(sb.toString());
    }

    public static boolean contains(String str, String str2) {
        return (str == null || str2 == null || str.indexOf(str2) < 0) ? false : true;
    }

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String replace(String str, String str2, String str3) {
        return replace(str, str2, str3, -1);
    }

    public static String replace(String str, String str2, String str3, int i) {
        if (str == null || str2 == null || str3 == null || str2.length() == 0 || i == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length());
        int i2 = 0;
        do {
            int indexOf = str.indexOf(str2, i2);
            if (indexOf == -1) {
                break;
            }
            stringBuffer.append(str.substring(i2, indexOf));
            stringBuffer.append(str3);
            i2 = str2.length() + indexOf;
            i--;
        } while (i != 0);
        stringBuffer.append(str.substring(i2));
        return stringBuffer.toString();
    }

    public static String hideAccount(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.contains(DinamicConstant.DINAMIC_PREFIX_AT)) {
            int indexOf = str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT);
            String substring = str.substring(0, indexOf);
            String substring2 = str.substring(indexOf, str.length());
            if (substring.length() >= 3) {
                return substring.substring(0, 3) + "***" + substring2;
            }
            return substring + "***" + substring2;
        } else if (str.matches("1\\d{10}")) {
            String substring3 = str.substring(0, 3);
            String substring4 = str.substring(7, str.length());
            return substring3 + "****" + substring4;
        } else if (str.length() <= 1) {
            return str;
        } else {
            String substring5 = str.substring(0, 1);
            String substring6 = str.substring(str.length() - 1, str.length());
            return substring5 + "***" + substring6;
        }
    }
}
