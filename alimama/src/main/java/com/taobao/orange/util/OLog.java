package com.taobao.orange.util;

import android.util.Log;
import com.taobao.orange.OConstant;
import com.taobao.tlog.adapter.AdapterForTLog;
import com.taobao.weex.el.parse.Operators;

public class OLog {
    private static final String PRE_TAG = "NOrange.";
    public static volatile boolean isUseTlog = false;

    @Deprecated
    public static void setPrintLog(boolean z) {
    }

    @Deprecated
    public static void setUseTlog(boolean z) {
    }

    static {
        try {
            Class.forName(OConstant.REFLECT_TLOG);
            isUseTlog = true;
        } catch (ClassNotFoundException unused) {
            isUseTlog = false;
        }
    }

    public static class Level {
        public static final int D = 1;
        public static final int E = 4;
        public static final int I = 2;
        public static final int L = 5;
        public static final int V = 0;
        public static final int W = 3;

        static int valueOf(String str) {
            switch (str.charAt(0)) {
                case 'D':
                    return 1;
                case 'E':
                    return 4;
                case 'I':
                    return 2;
                case 'V':
                    return 0;
                case 'W':
                    return 3;
                default:
                    return 5;
            }
        }
    }

    public static boolean isPrintLog(int i) {
        if (isUseTlog && i < Level.valueOf(AdapterForTLog.getLogLevel())) {
            return false;
        }
        return true;
    }

    public static void v(String str, String str2, Object... objArr) {
        if (!isPrintLog(0)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logv(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.v(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (!isPrintLog(1)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logd(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.d(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (!isPrintLog(2)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logi(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.i(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (!isPrintLog(3)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.w(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Throwable th, Object... objArr) {
        if (!isPrintLog(3)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(str), buildLogMsg(str2, objArr), th);
        } else {
            Log.w(buildLogTag(str), buildLogMsg(str2, objArr), th);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (!isPrintLog(4)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.loge(buildLogTag(str), buildLogMsg(str2, objArr));
        } else {
            Log.e(buildLogTag(str), buildLogMsg(str2, objArr));
        }
    }

    public static void e(String str, String str2, Throwable th, Object... objArr) {
        if (!isPrintLog(4)) {
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
        return PRE_TAG + str;
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
