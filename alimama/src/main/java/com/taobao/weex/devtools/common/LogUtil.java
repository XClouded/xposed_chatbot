package com.taobao.weex.devtools.common;

import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.weex.devtools.inspector.console.CLog;
import com.taobao.weex.devtools.inspector.protocol.module.Console;
import java.util.HashMap;
import java.util.Locale;

public class LogUtil {
    private static final String TAG = "weex";
    private static final HashMap<String, Console.MessageLevel> sLevelMap = new HashMap<>(6);

    public static void e(String str, Object... objArr) {
        e(format(str, objArr));
    }

    public static void e(Throwable th, String str, Object... objArr) {
        e(th, format(str, objArr));
    }

    public static void e(String str) {
        if (isLoggable(6)) {
            LogRedirector.e("weex", str);
        }
    }

    public static void e(Throwable th, String str) {
        if (isLoggable(6)) {
            LogRedirector.e("weex", str, th);
        }
    }

    public static void w(String str, Object... objArr) {
        w(format(str, objArr));
    }

    public static void w(Throwable th, String str, Object... objArr) {
        w(th, format(str, objArr));
    }

    public static void w(String str) {
        if (isLoggable(5)) {
            LogRedirector.w("weex", str);
        }
    }

    public static void w(Throwable th, String str) {
        if (isLoggable(5)) {
            LogRedirector.w("weex", str, th);
        }
    }

    public static void i(String str, Object... objArr) {
        i(format(str, objArr));
    }

    public static void i(Throwable th, String str, Object... objArr) {
        i(th, format(str, objArr));
    }

    public static void i(String str) {
        if (isLoggable(4)) {
            LogRedirector.i("weex", str);
        }
    }

    public static void i(Throwable th, String str) {
        if (isLoggable(4)) {
            LogRedirector.i("weex", str, th);
        }
    }

    public static void d(String str, Object... objArr) {
        d(format(str, objArr));
    }

    public static void d(Throwable th, String str, Object... objArr) {
        d(th, format(str, objArr));
    }

    public static void d(String str) {
        if (isLoggable(3)) {
            LogRedirector.d("weex", str);
        }
    }

    public static void d(Throwable th, String str) {
        if (isLoggable(3)) {
            LogRedirector.d("weex", str, th);
        }
    }

    public static void v(String str, Object... objArr) {
        v(format(str, objArr));
    }

    public static void v(Throwable th, String str, Object... objArr) {
        v(th, format(str, objArr));
    }

    public static void v(String str) {
        if (isLoggable(2)) {
            LogRedirector.v("weex", str);
        }
    }

    public static void v(Throwable th, String str) {
        if (isLoggable(2)) {
            LogRedirector.v("weex", str, th);
        }
    }

    private static String format(String str, Object... objArr) {
        return String.format(Locale.US, str, objArr);
    }

    public static boolean isLoggable(int i) {
        switch (i) {
            case 5:
            case 6:
                return true;
            default:
                return LogRedirector.isLoggable("weex", i);
        }
    }

    static {
        sLevelMap.put("verbose", Console.MessageLevel.LOG);
        sLevelMap.put(ApiConstants.ApiField.INFO, Console.MessageLevel.LOG);
        sLevelMap.put("assert", Console.MessageLevel.LOG);
        sLevelMap.put("debug", Console.MessageLevel.DEBUG);
        sLevelMap.put("warning", Console.MessageLevel.WARNING);
        sLevelMap.put("error", Console.MessageLevel.ERROR);
    }

    public static void log(String str, String str2) {
        CLog.writeToConsole(sLevelMap.get(str), Console.MessageSource.JAVASCRIPT, str2);
    }
}
