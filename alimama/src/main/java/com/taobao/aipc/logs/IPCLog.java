package com.taobao.aipc.logs;

import androidx.annotation.NonNull;
import com.taobao.tlog.adapter.AdapterForTLog;

public final class IPCLog {
    private static final String REFLECT_TLOG = "com.taobao.tlog.adapter.AdapterForTLog";
    private static final String TAG_PRE = "AIPC.";
    private static volatile boolean isUseTlog;
    private static ILogger sILogger = new LogcatLogger();
    private static boolean sIsDebug;

    static {
        try {
            Class.forName("com.taobao.tlog.adapter.AdapterForTLog");
            isUseTlog = true;
        } catch (ClassNotFoundException unused) {
            isUseTlog = false;
        }
    }

    private IPCLog() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    public static void setUseTlog(boolean z) {
        isUseTlog = z;
    }

    public static void setLogger(@NonNull ILogger iLogger) {
        sILogger = iLogger;
    }

    public static void debug(boolean z) {
        sIsDebug = z;
    }

    public static void v(String str, String str2) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(2, TAG_PRE + str, str2, (Throwable) null);
            return;
        }
        AdapterForTLog.logv(TAG_PRE + str, str2);
    }

    public static void d(String str, String str2) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(3, TAG_PRE + str, str2, (Throwable) null);
            return;
        }
        AdapterForTLog.logd(TAG_PRE + str, str2);
    }

    public static void i(String str, String str2) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(4, TAG_PRE + str, str2, (Throwable) null);
            return;
        }
        AdapterForTLog.logi(TAG_PRE + str, str2);
    }

    public static void w(String str, String str2) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(5, TAG_PRE + str, str2, (Throwable) null);
            return;
        }
        AdapterForTLog.logw(TAG_PRE + str, str2);
    }

    public static void e(String str, String str2) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(6, TAG_PRE + str, str2, (Throwable) null);
            return;
        }
        AdapterForTLog.loge(TAG_PRE + str, str2);
    }

    public static void eTag(String str, String str2, Throwable th) {
        if (!isUseTlog || sIsDebug) {
            ILogger iLogger = sILogger;
            iLogger.log(6, TAG_PRE + str, str2, th);
            return;
        }
        AdapterForTLog.loge(TAG_PRE + str, str2, th);
    }
}
