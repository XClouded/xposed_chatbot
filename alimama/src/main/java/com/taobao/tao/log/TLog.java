package com.taobao.tao.log;

import android.text.TextUtils;
import android.util.Log;
import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.weex.el.parse.Operators;

public class TLog {
    private static void log(LogLevel logLevel, String str, String str2, String str3, String str4, String str5, String str6) {
        TLogNative.dispatch(logLevel.getIndex(), str, str2, str3, str4, str5, str6);
    }

    private static void log(LogLevel logLevel, String str, String str2, String str3) {
        log(logLevel, str, str2, "C", "", "", str3);
    }

    public static void logv(String str, String str2, String str3) {
        if (TLogInitializer.getInstance().isDebugable()) {
            if (TextUtils.isEmpty(str)) {
                Log.v(str2, str3 == null ? "" : str3);
            } else {
                Log.v(str + "." + str2, str3 == null ? "" : str3);
            }
        }
        log(LogLevel.V, str, str2, str3);
    }

    @Deprecated
    public static void logv(String str, String str2) {
        logv("", str, str2);
    }

    @Deprecated
    public static void logv(String str, String... strArr) {
        if (strArr != null) {
            if (strArr.length == 1) {
                logv("", str, strArr[0]);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String append : strArr) {
                sb.append(append);
                sb.append(Operators.SPACE_STR);
            }
            logv("", str, sb.toString());
        }
    }

    public static void logd(String str, String str2, String str3) {
        if (TLogInitializer.getInstance().isDebugable()) {
            if (TextUtils.isEmpty(str)) {
                Log.d(str2, str3 == null ? "" : str3);
            } else {
                Log.d(str + "." + str2, str3 == null ? "" : str3);
            }
        }
        log(LogLevel.D, str, str2, str3);
    }

    @Deprecated
    public static void logd(String str, String str2) {
        logd("", str, str2);
    }

    @Deprecated
    public static void logd(String str, String... strArr) {
        if (strArr != null) {
            if (strArr.length == 1) {
                logd("", str, strArr[0]);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String append : strArr) {
                sb.append(append);
                sb.append(Operators.SPACE_STR);
            }
            logd("", str, sb.toString());
        }
    }

    public static void logi(String str, String str2, String str3) {
        if (TLogInitializer.getInstance().isDebugable()) {
            if (TextUtils.isEmpty(str)) {
                Log.i(str2, str3 == null ? "" : str3);
            } else {
                Log.i(str + "." + str2, str3 == null ? "" : str3);
            }
        }
        log(LogLevel.I, str, str2, str3);
    }

    @Deprecated
    public static void logi(String str, String str2) {
        logi("", str, str2);
    }

    @Deprecated
    public static void logi(String str, String... strArr) {
        if (strArr != null) {
            if (strArr.length == 1) {
                logi("", str, strArr[0]);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String append : strArr) {
                sb.append(append);
                sb.append(Operators.SPACE_STR);
            }
            logi("", str, sb.toString());
        }
    }

    public static void logw(String str, String str2, String str3) {
        if (TLogInitializer.getInstance().isDebugable()) {
            if (TextUtils.isEmpty(str)) {
                Log.w(str2, str3 == null ? "" : str3);
            } else {
                Log.w(str + "." + str2, str3 == null ? "" : str3);
            }
        }
        log(LogLevel.W, str, str2, str3);
    }

    @Deprecated
    public static void logw(String str, String str2) {
        logw("", str, str2);
    }

    @Deprecated
    public static void logw(String str, String... strArr) {
        if (strArr != null) {
            if (strArr.length == 1) {
                logw("", str, strArr[0]);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String append : strArr) {
                sb.append(append);
                sb.append(Operators.SPACE_STR);
            }
            logw("", str, sb.toString());
        }
    }

    public static void loge(String str, String str2, String str3) {
        if (TLogInitializer.getInstance().isDebugable()) {
            if (TextUtils.isEmpty(str)) {
                Log.e(str2, str3 == null ? "" : str3);
            } else {
                Log.e(str + "." + str2, str3 == null ? "" : str3);
            }
        }
        log(LogLevel.E, str, str2, str3);
    }

    @Deprecated
    public static void loge(String str, String str2) {
        loge("", str, str2);
    }

    @Deprecated
    public static void loge(String str, String... strArr) {
        if (strArr != null) {
            if (strArr.length == 1) {
                loge("", str, strArr[0]);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String append : strArr) {
                sb.append(append);
                sb.append(Operators.SPACE_STR);
            }
            loge("", str, sb.toString());
        }
    }

    public static void logw(String str, String str2, Throwable th) {
        String str3;
        if (th == null) {
            str3 = str2 + "******* NULL == e *******";
        } else {
            str3 = getExceptionMsg(str2, th);
        }
        logw("", str, str3);
    }

    public static void loge(String str, String str2, Throwable th) {
        String str3;
        if (th == null) {
            str3 = str2 + "******* NULL == e *******";
        } else {
            str3 = getExceptionMsg(str2, th);
        }
        loge("", str, str3);
    }

    public static void traceLog(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            log(LogLevel.F, "", "Trace", ApiConstants.UTConstants.UT_SUCCESS_T, str, str2, "NULL == log");
        }
    }

    private static String getExceptionMsg(String str, Throwable th) {
        if (th == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String message = th.getMessage();
        String name = th.getClass().getName();
        sb.append("\t");
        sb.append(str + "\t");
        sb.append(name);
        sb.append("  ");
        sb.append(message);
        sb.append("\r\n");
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (message == null || message.length() == 0) {
            return null;
        }
        for (StackTraceElement append : stackTrace) {
            sb.append("\tat  ");
            sb.append(append);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
