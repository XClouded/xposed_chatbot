package com.alibaba.ut.utils;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    private static String TAG = "Analytics.ut4aplus";
    private static final String TAG_ENABLELOG = "enablelog";
    private static boolean isDebug = true;
    private static String log_prefix = "Analytics.ut4aplus.";
    private static HashMap<String, Integer> mTlogMap = new HashMap<>();

    public static boolean isTlogEnable(String str) {
        return false;
    }

    static {
        mTlogMap.put("V", 5);
        mTlogMap.put("D", 4);
        mTlogMap.put("I", 3);
        mTlogMap.put("W", 2);
        mTlogMap.put("E", 1);
        mTlogMap.put("L", 0);
    }

    public static void setLogPrefix(String str) {
        log_prefix = str;
    }

    public static void setDebug(boolean z) {
        String str = TAG;
        Log.i(str, "set environment =" + z);
        isDebug = z;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void d() {
        if (!isTlogEnable("D") && isDebug()) {
            Log.d(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void e() {
        if (isDebug()) {
            Log.e(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void i() {
        if (isDebug()) {
            Log.i(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void d(String str, Map<String, String> map) {
        if (isDebug()) {
            Log.d(buildLogTag(), buildLogMsg(str, map));
        }
    }

    public static void d(String str, Object... objArr) {
        if (isDebug()) {
            try {
                String buildLogMsg = buildLogMsg(str, objArr);
                if (!TextUtils.isEmpty(buildLogMsg)) {
                    int i = 2048;
                    if (buildLogMsg.length() > 2048) {
                        int i2 = 0;
                        int length = buildLogMsg.length();
                        while (true) {
                            Log.d(buildLogTag(), buildLogMsg.substring(i2, i));
                            int i3 = i + 2048;
                            if (i3 > length) {
                                i3 = length;
                            }
                            if (i != i3) {
                                int i4 = i;
                                i = i3;
                                i2 = i4;
                            } else {
                                return;
                            }
                        }
                    }
                }
                Log.d(buildLogTag(), buildLogMsg);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static void i(String str, Object... objArr) {
        if (isDebug()) {
            Log.i(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void w(String str, Object... objArr) {
        if (isDebug()) {
            Log.w(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void w(String str, Throwable th, Object... objArr) {
        if (isDebug()) {
            Log.w(buildLogTag(), buildLogMsg(str, objArr), th);
        }
    }

    public static void e(String str, Object... objArr) {
        if (isDebug()) {
            Log.e(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void e(String str, Throwable th, Object... objArr) {
        if (isDebug()) {
            Log.e(buildLogTag(), buildLogMsg(str, objArr), th);
        }
    }

    private static String formatKv(Object obj, Object obj2) {
        Object[] objArr = new Object[2];
        if (obj == null) {
            obj = "";
        }
        objArr[0] = obj;
        if (obj2 == null) {
            obj2 = "";
        }
        objArr[1] = obj2;
        return String.format("%s:%s", objArr);
    }

    private static String buildLogTag() {
        return buildLogTag(log_prefix);
    }

    private static String buildLogTag(String str) {
        if (TextUtils.isEmpty(str)) {
            str = TAG;
        }
        StackTraceElement stackTrace = getStackTrace();
        String str2 = "";
        if (stackTrace != null) {
            String className = stackTrace.getClassName();
            if (!TextUtils.isEmpty(className)) {
                str2 = className.substring(className.lastIndexOf(46) + 1);
            }
        }
        return str + str2 + "." + String.valueOf(Process.myPid()) + "." + (Thread.currentThread().getId() + "");
    }

    static String buildLogMsg(String str, Object... objArr) {
        if (str == null && objArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StackTraceElement stackTrace = getStackTrace();
        String str2 = "";
        if (stackTrace != null) {
            str2 = stackTrace.getMethodName();
        }
        int i = 0;
        sb.append(String.format("[%s]", new Object[]{str2}));
        if (str != null) {
            sb.append(Operators.SPACE_STR);
            sb.append(str);
        }
        if (objArr != null) {
            while (true) {
                int i2 = i + 1;
                if (i2 >= objArr.length) {
                    break;
                }
                sb.append(",");
                sb.append(formatKv(objArr[i], objArr[i2]));
                i = i2 + 1;
            }
            if (i == objArr.length - 1) {
                sb.append(",");
                sb.append(objArr[i]);
            }
        }
        return sb.toString();
    }

    static String buildLogMsg(String str, Map<String, String> map) {
        if (str == null || map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StackTraceElement stackTrace = getStackTrace();
        String str2 = "";
        if (stackTrace != null) {
            str2 = stackTrace.getMethodName();
        }
        sb.append(String.format("[%s]", new Object[]{str2}));
        if (str != null) {
            sb.append(Operators.SPACE_STR);
            sb.append(str);
        }
        for (Map.Entry next : map.entrySet()) {
            sb.append(",");
            sb.append(((String) next.getKey()) + " : " + ((String) next.getValue()));
        }
        return sb.toString();
    }

    private static StackTraceElement getStackTrace() {
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (!stackTraceElement.isNativeMethod() && !stackTraceElement.getClassName().equals(Thread.class.getName()) && !stackTraceElement.getClassName().equals(Logger.class.getName())) {
                return stackTraceElement;
            }
        }
        return null;
    }
}
