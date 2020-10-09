package com.taobao.alivfsadapter.utils;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public class AVFSLogUtils {
    public static int d(String str, Throwable th, Object... objArr) {
        return 0;
    }

    public static int d(String str, Object... objArr) {
        return 0;
    }

    public static String getStackTraceString(Throwable th) {
        return "";
    }

    public static int println(int i, String str, Object... objArr) {
        return 0;
    }

    public static int v(String str, Throwable th, Object... objArr) {
        return 0;
    }

    public static int v(String str, Object... objArr) {
        return 0;
    }

    public static int w(String str, Object... objArr) {
        return Log.w(str, concat(objArr));
    }

    public static boolean isLoggable(String str, int i) {
        return Log.isLoggable(str, i);
    }

    public static int e(String str, Throwable th, Object... objArr) {
        return Log.e(str, concat(objArr), th);
    }

    public static int wtf(String str, Throwable th) {
        return Log.wtf(str, th);
    }

    public static int w(String str, Throwable th, Object... objArr) {
        return Log.w(str, concat(objArr), th);
    }

    public static int w(String str, Throwable th) {
        return Log.w(str, th);
    }

    public static int e(String str, Object... objArr) {
        return Log.e(str, concat(objArr));
    }

    public static int wtf(String str, Throwable th, Object... objArr) {
        return Log.wtf(str, concat(objArr), th);
    }

    public static int wtf(String str, Object... objArr) {
        return Log.wtf(str, concat(objArr));
    }

    public static int i(String str, Object... objArr) {
        return Log.i(str, concat(objArr));
    }

    public static int i(String str, Throwable th, Object... objArr) {
        return Log.i(str, concat(objArr), th);
    }

    private static String concat(Object[] objArr) {
        String str = "";
        for (int i = 0; i < objArr.length; i++) {
            str = str + objArr[i] + Operators.SPACE_STR;
        }
        return str;
    }
}
