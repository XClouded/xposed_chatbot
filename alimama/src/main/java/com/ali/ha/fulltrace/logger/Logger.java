package com.ali.ha.fulltrace.logger;

import android.util.Log;

public class Logger {
    private static final String TAG = "FTLogger";
    private static final boolean THROW_EXCEPTION = false;
    private static boolean isDebug = true;

    private Logger() {
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean z) {
        isDebug = z;
    }

    public static void i(String str, Object... objArr) {
        if (isDebug) {
            String format2String = format2String(objArr);
            Log.i(TAG, str + ":" + format2String);
        }
    }

    public static void d(String str, Object... objArr) {
        if (isDebug) {
            String format2String = format2String(objArr);
            Log.d(TAG, str + ":" + format2String);
        }
    }

    public static void e(String str, Object... objArr) {
        if (isDebug) {
            String format2String = format2String(objArr);
            Log.e(TAG, str + ":" + format2String);
        }
    }

    public static void w(String str, Object... objArr) {
        if (isDebug) {
            String format2String = format2String(objArr);
            Log.w(TAG, str + ":" + format2String);
        }
    }

    public static void throwException(Throwable th) {
        boolean z = isDebug;
    }

    private static String format2String(Object... objArr) {
        if (objArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objArr) {
            if (obj != null) {
                sb.append("->");
                sb.append(obj.toString());
            }
        }
        return sb.toString();
    }
}
