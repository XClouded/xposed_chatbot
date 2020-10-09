package com.ta.audid.utils;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

public class UtdidLogger {
    private static final String TAG_LOG_PREFIX = "Utdid.";
    private static boolean isDebug = false;
    private static boolean isSDebug = false;

    public static void setDebug(boolean z) {
        Log.i(TAG_LOG_PREFIX, "setDebug:" + z);
        isDebug = z;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void d() {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void e() {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void i() {
        if (isDebug) {
            Log.i(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void d(String str, Map<String, String> map) {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg(str, map));
        }
    }

    public static void d(String str, Object... objArr) {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void sd(String str, Object... objArr) {
        if (isSDebug) {
            Log.d(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void i(String str, Object... objArr) {
        if (isDebug) {
            Log.i(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void w(String str, Object... objArr) {
        if (isDebug) {
            Log.w(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void w(String str, Throwable th, Object... objArr) {
        if (isDebug) {
            Log.w(buildLogTag(), buildLogMsg(str, objArr), th);
        }
    }

    public static void e(String str, Object... objArr) {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void e(String str, Throwable th, Object... objArr) {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg(str, objArr), th);
        }
    }

    public static void se(String str, Object... objArr) {
        if (isSDebug) {
            Log.e(buildLogTag(), buildLogMsg(str, objArr));
        }
    }

    public static void se(String str, Throwable th, Object... objArr) {
        if (isSDebug) {
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
        StackTraceElement stackTrace = getStackTrace();
        String str = "";
        String str2 = "";
        if (stackTrace != null) {
            String className = stackTrace.getClassName();
            if (!TextUtils.isEmpty(className)) {
                str = className.substring(className.lastIndexOf(46) + 1);
            }
            str2 = stackTrace.getMethodName();
        }
        return TAG_LOG_PREFIX + str + "." + str2 + "." + String.valueOf(Process.myPid()) + "." + (Thread.currentThread().getId() + "");
    }

    private static String buildLogMsg(String str, Object... objArr) {
        if (str == null && objArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Object[] objArr2 = new Object[1];
        if (str == null) {
            str = "-";
        }
        int i = 0;
        objArr2[0] = str;
        sb.append(String.format("[%s] ", objArr2));
        if (objArr != null) {
            int length = objArr.length;
            while (true) {
                int i2 = i + 1;
                if (i2 >= objArr.length) {
                    break;
                }
                sb.append(formatKv(objArr[i], objArr[i2]));
                if (i2 < length - 1) {
                    sb.append(",");
                }
                i = i2 + 1;
            }
            if (i == objArr.length - 1) {
                sb.append(objArr[i]);
            }
        }
        return sb.toString();
    }

    private static String buildLogMsg(String str, Map<String, String> map) {
        if (str == null && map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Object[] objArr = new Object[1];
        if (str == null) {
            str = "-";
        }
        int i = 0;
        objArr[0] = str;
        sb.append(String.format("[%s] ", objArr));
        int size = map.size();
        for (Map.Entry next : map.entrySet()) {
            sb.append(((String) next.getKey()) + " : " + ((String) next.getValue()));
            i++;
            if (i < size) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static StackTraceElement getStackTrace() {
        try {
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                if (!stackTraceElement.isNativeMethod()) {
                    if (!stackTraceElement.getClassName().equals(Thread.class.getName())) {
                        if (!stackTraceElement.getClassName().equals(UtdidLogger.class.getName())) {
                            return stackTraceElement;
                        }
                    }
                }
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }
}
