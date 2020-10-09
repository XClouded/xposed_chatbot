package com.taobao.accs.utl;

import android.util.Log;
import com.taobao.tlog.adapter.AdapterForTLog;
import com.taobao.weex.el.parse.Operators;

public class ALog {
    private static final String REFLECT_TLOG = "com.taobao.tlog.adapter.AdapterForTLog";
    public static volatile boolean isUseTlog = false;
    private static String preTag = "NAccs.";

    public enum Level {
        V,
        D,
        I,
        W,
        E,
        L
    }

    @Deprecated
    public static boolean isPrintLog() {
        return false;
    }

    @Deprecated
    public static void setEnableTLog(boolean z) {
    }

    @Deprecated
    public static void setPrintLog(boolean z) {
    }

    static {
        try {
            Class.forName("com.taobao.tlog.adapter.AdapterForTLog");
            isUseTlog = true;
        } catch (ClassNotFoundException unused) {
            isUseTlog = false;
        }
    }

    @Deprecated
    public static void initALog(String str, int i) {
        preTag = str;
    }

    @Deprecated
    public static void setUseTlog(boolean z) {
        isUseTlog = z;
    }

    public static boolean isPrintLog(Level level) {
        if (!isUseTlog) {
            return true;
        }
        Level level2 = Level.L;
        try {
            level2 = Level.valueOf(AdapterForTLog.getLogLevel());
        } catch (Exception unused) {
        }
        if (level.ordinal() >= level2.ordinal()) {
            return true;
        }
        return false;
    }

    public static void v(String str, String str2, Object... objArr) {
        if (!isPrintLog(Level.V)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logv(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.v(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (!isPrintLog(Level.D)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logd(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.d(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (!isPrintLog(Level.I)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logi(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.i(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (!isPrintLog(Level.W)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.w(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Throwable th, Object... objArr) {
        if (!isPrintLog(Level.W)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(str), buildLogMsg(str2, objArr), th);
        } else {
            Log.w(buildLogTag(str), buildLogMsg(str2, objArr), th);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (!isPrintLog(Level.E)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.loge(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.e(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void e(String str, String str2, Throwable th, Object... objArr) {
        if (!isPrintLog(Level.E)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.loge(buildLogTag(str), buildLogMsg(str2, objArr), th);
        } else {
            Log.e(buildLogTag(str), buildLogMsg(str2, objArr), th);
        }
    }

    private static String formatKv(Object obj, Object obj2) {
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            obj = "";
        }
        sb.append(obj);
        sb.append(":");
        if (obj2 == null) {
            obj2 = "";
        }
        sb.append(obj2);
        return sb.toString();
    }

    private static String buildLogTag(String str) {
        return preTag + str;
    }

    private static String buildLogMsg(String str, Object... objArr) {
        if (str == null && objArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            sb.append(Operators.SPACE_STR);
            sb.append(str);
        }
        if (objArr != null) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (i2 >= objArr.length) {
                    break;
                }
                sb.append(Operators.SPACE_STR);
                sb.append(formatKv(objArr[i], objArr[i2]));
                i = i2 + 1;
            }
            if (i == objArr.length - 1) {
                sb.append(Operators.SPACE_STR);
                sb.append(objArr[i]);
            }
        }
        return sb.toString();
    }
}
