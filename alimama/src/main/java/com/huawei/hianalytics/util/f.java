package com.huawei.hianalytics.util;

import alimama.com.unweventparse.constants.EventConstants;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import java.util.Map;
import java.util.regex.Pattern;

public class f {
    public static int a(int i, int i2, int i3) {
        if (i > i2) {
            b.c("HianalyticsSDK", "checkIntRange(): parameter overlarge.");
            return i2;
        } else if (i >= i3) {
            return i;
        } else {
            b.c("HianalyticsSDK", "checkIntRange(): parameter under size.");
            return i3;
        }
    }

    public static String a(String str, int i, int i2) {
        String str2;
        String str3;
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt <= i && parseInt >= i2) {
                return String.valueOf(str);
            }
            str2 = "HianalyticsSDK";
            str3 = "checkMcc(): MCC out of range!";
            b.c(str2, str3);
            return "";
        } catch (NumberFormatException unused) {
            str2 = "HianalyticsSDK";
            str3 = "checkMcc(): mcc is not a number!please set up correctly";
        }
    }

    public static String a(String str, String str2, String str3, String str4) {
        if (!TextUtils.isEmpty(str2)) {
            return a(str, str2, str3) ? str2 : str4;
        }
        b.c("HianalyticsSDK", "checkStrParameter() Parameter verification failure! Parameter:" + str);
        return str4;
    }

    public static boolean a(String str) {
        return !a(EventConstants.EVENTID, str, 256);
    }

    public static boolean a(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (Pattern.compile(str2).matcher(str).matches()) {
            return true;
        }
        b.c("HianalyticsSDK", "isURLMatch(): URL check failed.");
        return false;
    }

    public static boolean a(String str, String str2, int i) {
        String str3;
        StringBuilder sb;
        String str4;
        if (TextUtils.isEmpty(str2)) {
            str3 = "HianalyticsSDK";
            sb = new StringBuilder();
            str4 = "checkString() Parameter is empty : ";
        } else if (str2.length() <= i) {
            return true;
        } else {
            str3 = "HianalyticsSDK";
            sb = new StringBuilder();
            str4 = "checkString() Failure of parameter length check! Parameter:";
        }
        sb.append(str4);
        sb.append(str);
        b.c(str3, sb.toString());
        return false;
    }

    public static boolean a(String str, String str2, String str3) {
        String str4;
        StringBuilder sb;
        String str5;
        if (TextUtils.isEmpty(str2)) {
            str4 = "HianalyticsSDK";
            sb = new StringBuilder();
            str5 = "checkString() Parameter is null! Parameter:";
        } else if (Pattern.compile(str3).matcher(str2).matches()) {
            return true;
        } else {
            str4 = "HianalyticsSDK";
            sb = new StringBuilder();
            str5 = "checkString() Parameter verification failure! Parameter:";
        }
        sb.append(str5);
        sb.append(str);
        b.c(str4, sb.toString());
        return false;
    }

    public static boolean a(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            b.c("HianalyticsSDK", "onEvent() mapValue has not data.so,The data will be empty");
            return false;
        } else if (map.size() == 1 && (map.get("constants") != null || map.get("_constants") != null)) {
            b.c("HianalyticsSDK", "checkMap() the key can't be constants or _constants");
            return false;
        } else if (map.size() <= 2048 && map.toString().length() <= 204800) {
            return true;
        } else {
            b.c("HianalyticsSDK", "checkMap Map data is too big! size: %d , length: %d", Integer.valueOf(map.size()), Integer.valueOf(map.toString().length()));
            return false;
        }
    }
}
