package com.taobao.monitor.impl.logger;

import android.util.Log;

public class Logger {
    private static final String TAG = "APMLogger";
    private static boolean isDebug = false;

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
        String format2String = format2String(objArr);
        Log.e(TAG, str + ":" + format2String);
    }

    public static void w(String str, Object... objArr) {
        if (isDebug) {
            String format2String = format2String(objArr);
            Log.w(TAG, str + ":" + format2String);
        }
    }

    public static void throwException(Throwable th) {
        if (isDebug) {
            throw new RuntimeException(th);
        }
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
