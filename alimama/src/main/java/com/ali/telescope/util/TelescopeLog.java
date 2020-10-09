package com.ali.telescope.util;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public class TelescopeLog {
    public static final int LOG_LEVEL_CLOSE = 0;
    public static final int LOG_LEVEL_D = 4;
    public static final int LOG_LEVEL_E = 1;
    public static final int LOG_LEVEL_I = 3;
    public static final int LOG_LEVEL_V = 5;
    public static final int LOG_LEVEL_W = 2;
    private static final String SP = "|";
    public static final String TAG = "Telescope";
    public static int sLogLevel = 1;

    public static boolean isLogOpen() {
        return sLogLevel > 0;
    }

    public static <T> void v(String str, T... tArr) {
        if (sLogLevel >= 5) {
            Log.v(TAG, Operators.ARRAY_START_STR + str + "] " + buildMessage(str, tArr));
        }
    }

    public static <T> void i(String str, T... tArr) {
        if (sLogLevel >= 3) {
            Log.i(TAG, Operators.ARRAY_START_STR + str + "] " + buildMessage(str, tArr));
        }
    }

    public static <T> void d(String str, T... tArr) {
        if (sLogLevel >= 4) {
            Log.d(TAG, Operators.ARRAY_START_STR + str + "] " + buildMessage(str, tArr));
        }
    }

    public static <T> void w(String str, T... tArr) {
        if (sLogLevel >= 2) {
            Log.w(TAG, Operators.ARRAY_START_STR + str + "] " + buildMessage(str, tArr));
        }
    }

    public static <T> void e(String str, T... tArr) {
        if (sLogLevel >= 1) {
            Log.e(TAG, Operators.ARRAY_START_STR + str + "] " + buildMessage(str, tArr));
        }
    }

    public static void v(String str, String str2, Throwable th) {
        if (sLogLevel >= 5) {
            Log.v(TAG, Operators.ARRAY_START_STR + str + "] " + str2, th);
        }
    }

    public static void i(String str, String str2, Throwable th) {
        if (sLogLevel >= 3) {
            Log.i(TAG, Operators.ARRAY_START_STR + str + "] " + str2, th);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (sLogLevel >= 4) {
            Log.d(TAG, Operators.ARRAY_START_STR + str + "] " + str2, th);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if (sLogLevel >= 2) {
            Log.w(TAG, Operators.ARRAY_START_STR + str + "] " + str2, th);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (sLogLevel >= 1) {
            Log.e(TAG, Operators.ARRAY_START_STR + str + "] " + str2, th);
        }
    }

    private static <T> String buildMessage(String str, T... tArr) {
        if (tArr == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        if (tArr.length > 0) {
            sb.append(tArr[0]);
            for (int i = 1; i < tArr.length; i++) {
                sb.append(SP);
                sb.append(tArr[i]);
            }
        }
        return sb.toString();
    }
}
