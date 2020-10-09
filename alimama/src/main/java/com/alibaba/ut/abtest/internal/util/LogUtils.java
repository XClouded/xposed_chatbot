package com.alibaba.ut.abtest.internal.util;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.multiprocess.MultiProcessService;
import com.taobao.tlog.adapter.AdapterForTLog;

public final class LogUtils {
    private static final String LOG_MODULE = "UTABTest";
    private static Boolean isDebuggable = null;
    private static boolean tlogEnabled = true;

    private LogUtils() {
    }

    public static void logV(String str, String str2) {
        log("V", str, str2, (Throwable) null);
    }

    public static void logVAndReport(String str, String str2) {
        log("V", str, str2, (Throwable) null);
        reportLog("debug", "base", str, str2, (Throwable) null);
    }

    public static void logD(String str, String str2) {
        log("D", str, str2, (Throwable) null);
    }

    public static void logDAndReport(String str, String str2) {
        log("D", str, str2, (Throwable) null);
        reportLog("debug", "base", str, str2, (Throwable) null);
    }

    public static void logI(String str, String str2) {
        log("I", str, str2, (Throwable) null);
    }

    public static void logIAndReport(String str, String str2) {
        log("I", str, str2, (Throwable) null);
        reportLog("debug", "base", str, str2, (Throwable) null);
    }

    public static void logW(String str, String str2) {
        log("W", str, str2, (Throwable) null);
    }

    public static void logWAndReport(String str, String str2) {
        log("W", str, str2, (Throwable) null);
        reportLog("warn", "base", str, str2, (Throwable) null);
    }

    public static void logW(String str, String str2, Throwable th) {
        log("W", str, str2, th);
    }

    public static void logWAndReport(String str, String str2, Throwable th) {
        log("W", str, str2, th);
        reportLog("warn", "base", str, str2, th);
    }

    public static void logE(String str, String str2) {
        log("E", str, str2, (Throwable) null);
    }

    public static void logEAndReport(String str, String str2) {
        log("E", str, str2, (Throwable) null);
        reportLog("error", "base", str, str2, (Throwable) null);
    }

    public static void logE(String str, String str2, Throwable th) {
        log("E", str, str2, th);
    }

    public static void logEAndReport(String str, String str2, Throwable th) {
        log("E", str, str2, th);
        reportLog("error", "base", str, str2, th);
    }

    public static void logResultAndReport(String str, String str2) {
        log("W", str, str2, (Throwable) null);
        reportLog("debug", "result", str, str2, (Throwable) null);
    }

    public static void logConfigAndReport(String str, String str2) {
        log("W", str, str2, (Throwable) null);
        reportLog("debug", BindingXConstants.KEY_CONFIG, str, str2, (Throwable) null);
    }

    private static void log(String str, String str2, String str3, Throwable th) {
        if (TextUtils.equals(str, "V")) {
            if (isDebugMode()) {
                Log.v(getRealTag(str2), str3);
            } else if (AdapterForTLog.isValid() && tlogEnabled) {
                AdapterForTLog.logv(getRealTag(str2), str3);
            }
        } else if (TextUtils.equals(str, "D")) {
            if (isDebugMode()) {
                Log.d(getRealTag(str2), str3);
            } else if (AdapterForTLog.isValid() && tlogEnabled) {
                AdapterForTLog.logd(getRealTag(str2), str3);
            }
        } else if (TextUtils.equals(str, "I")) {
            if (isDebugMode()) {
                Log.i(getRealTag(str2), str3);
            } else if (AdapterForTLog.isValid() && tlogEnabled) {
                AdapterForTLog.logi(getRealTag(str2), str3);
            }
        } else if (TextUtils.equals(str, "W")) {
            if (isDebugMode()) {
                Log.w(getRealTag(str2), str3, th);
            } else if (AdapterForTLog.isValid() && tlogEnabled) {
                AdapterForTLog.logw(getRealTag(str2), str3, th);
            }
        } else if (!TextUtils.equals(str, "E")) {
        } else {
            if (isDebugMode()) {
                Log.e(getRealTag(str2), str3, th);
            } else if (AdapterForTLog.isValid() && tlogEnabled) {
                AdapterForTLog.loge(getRealTag(str2), str3, th);
            }
        }
    }

    private static void reportLog(String str, String str2, String str3, String str4, Throwable th) {
        if (th != null) {
            MultiProcessService multiProcessService = ABContext.getInstance().getMultiProcessService();
            multiProcessService.reportLog(str, str2, str3, str4 + "\n" + Log.getStackTraceString(th));
            return;
        }
        ABContext.getInstance().getMultiProcessService().reportLog(str, str2, str3, str4);
    }

    private static String getRealTag(String str) {
        return "UTABTest." + str;
    }

    private static boolean isDebugMode() {
        return ABContext.getInstance().isDebugMode() || isAPKDebuggable();
    }

    private static synchronized boolean isAPKDebuggable() {
        synchronized (LogUtils.class) {
            if (isDebuggable != null) {
                boolean booleanValue = isDebuggable.booleanValue();
                return booleanValue;
            }
            try {
                isDebuggable = Boolean.valueOf((ABContext.getInstance().getContext().getApplicationInfo().flags & 2) != 0);
                boolean booleanValue2 = isDebuggable.booleanValue();
                return booleanValue2;
            } catch (Exception unused) {
                return false;
            }
        }
    }

    public static boolean isLogDebugEnable() {
        if (isDebugMode()) {
            return true;
        }
        String logLevel = AdapterForTLog.getLogLevel(LOG_MODULE);
        if (TextUtils.equals("L", logLevel) || TextUtils.equals("V", logLevel)) {
            return false;
        }
        return true;
    }

    public static void setTlogEnabled(boolean z) {
        tlogEnabled = z;
    }
}
