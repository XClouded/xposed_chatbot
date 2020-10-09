package com.taobao.alivfssdk.utils;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.text.SimpleDateFormat;

public class AVFSCacheLog {
    private static SimpleDateFormat sDateFormat;

    public static int d(String str, Throwable th, Object... objArr) {
        return 0;
    }

    public static int d(String str, Object... objArr) {
        return 0;
    }

    public static String dateString(long j) {
        return "";
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

    public static String bytesIntoHumanReadable(long j) {
        if (j >= 0 && j < 1024) {
            return j + " B";
        } else if (j >= 1024 && j < 1048576) {
            return (j / 1024) + " KB";
        } else if (j >= 1048576 && j < 1073741824) {
            return (j / 1048576) + " MB";
        } else if (j >= 1073741824 && j < 1099511627776L) {
            return (j / 1073741824) + " GB";
        } else if (j >= 1099511627776L) {
            return (j / 1099511627776L) + " TB";
        } else {
            return j + " Bytes";
        }
    }
}
