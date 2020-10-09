package com.alibaba.taffy.core.util;

import android.util.Log;
import com.taobao.weex.BuildConfig;

public class TLog {
    public static final boolean Compileable = true;
    private static final int DEBUG = 3;
    private static final int ERROR = 6;
    private static final int INFO = 4;
    private static int LogLevel = 2;
    private static final String PREFIX = "TAF-";
    private static final int VERBOSE = 2;
    private static final int WARN = 5;

    public static void setLogEnabled(boolean z) {
        if (3 > LogLevel) {
            return;
        }
        if (z) {
            setLogLevel(3);
        } else {
            setLogLevel(4);
        }
    }

    public static void setLogLevel(int i) {
        LogLevel = i;
    }

    public static void d(String str, String str2) {
        if (3 >= LogLevel) {
            String str3 = PREFIX + str;
            if (str2 == null) {
                str2 = BuildConfig.buildJavascriptFrameworkVersion;
            }
            Log.d(str3, str2);
        }
    }

    public static void d(String str, Object obj) {
        if (3 >= LogLevel) {
            String str2 = BuildConfig.buildJavascriptFrameworkVersion;
            if (obj != null) {
                str2 = obj.toString();
            }
            Log.d(PREFIX + str, str2);
        }
    }

    public static void e(String str, String str2) {
        e(str, str2, (Throwable) null);
    }

    public static void e(String str, String str2, Throwable th) {
        if (6 >= LogLevel) {
            Log.e(PREFIX + str, str2, th);
        }
    }

    public static void i(String str, String str2) {
        if (4 >= LogLevel) {
            Log.i(PREFIX + str, str2);
        }
    }

    public static void v(String str, String str2) {
        if (2 >= LogLevel) {
            String str3 = PREFIX + str;
            if (str2 == null) {
                str2 = BuildConfig.buildJavascriptFrameworkVersion;
            }
            Log.v(str3, str2);
        }
    }

    public static void v(String str, Object obj) {
        if (2 >= LogLevel) {
            String str2 = BuildConfig.buildJavascriptFrameworkVersion;
            if (obj != null) {
                str2 = obj.toString();
            }
            Log.v(PREFIX + str, str2);
        }
    }

    public static void w(String str, String str2) {
        w(str, str2, (Throwable) null);
    }

    public static void w(String str, String str2, Throwable th) {
        if (5 >= LogLevel) {
            Log.w(PREFIX + str, str2, th);
        }
    }
}
