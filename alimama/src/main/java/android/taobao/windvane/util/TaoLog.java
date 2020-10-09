package android.taobao.windvane.util;

import android.taobao.windvane.util.log.AndroidLog;
import android.taobao.windvane.util.log.ILog;

import java.util.HashMap;
import java.util.Map;

public final class TaoLog {
    public static Map<String, Integer> LogLevel = new HashMap();
    private static boolean enabled = false;
    private static ILog impl = new AndroidLog();
    private static final String tagPre = "WindVane.";

    static {
        setImpl(new AndroidLog());
        for (ILog.LogLevelEnum logLevelEnum : ILog.LogLevelEnum.values()) {
            LogLevel.put(logLevelEnum.getLogLevelName(), Integer.valueOf(logLevelEnum.getLogLevel()));
        }
    }

    public static void setImpl(ILog iLog) {
        if (EnvUtil.isAppDebug()) {
            w("TaoLog", "Ignore set log impl on debug mode");
        } else {
            impl = iLog;
        }
    }

    public static boolean getLogStatus() {
        return impl != null && enabled;
    }

    public static void setLogSwitcher(boolean z) {
        enabled = z;
    }

    public static void d(String str, String str2, Object... objArr) {
        if (shouldPrintDebug() && impl != null) {
            ILog iLog = impl;
            iLog.d(tagPre + str, format(str2, objArr));
        }
    }

    public static void d(String str, String str2, Throwable th, Object... objArr) {
        if (shouldPrintDebug() && impl != null) {
            ILog iLog = impl;
            iLog.d(tagPre + str, format(str2, objArr), th);
        }
    }

    public static void v(String str, String str2) {
        if (shouldPrintVerbose() && impl != null) {
            ILog iLog = impl;
            iLog.v(tagPre + str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (shouldPrintDebug() && impl != null) {
            ILog iLog = impl;
            iLog.d(tagPre + str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (shouldPrintInfo() && impl != null) {
            ILog iLog = impl;
            iLog.i(tagPre + str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (shouldPrintWarn() && impl != null) {
            ILog iLog = impl;
            iLog.w(tagPre + str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (shouldPrintError() && impl != null) {
            ILog iLog = impl;
            iLog.e(tagPre + str, str2);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (shouldPrintError() && impl != null) {
            ILog iLog = impl;
            iLog.e(tagPre + str, format(str2, objArr));
        }
    }

    public static void e(String str, String str2, Throwable th, Object... objArr) {
        if (shouldPrintError() && impl != null) {
            ILog iLog = impl;
            iLog.e(tagPre + str, format(str2, objArr), th);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (shouldPrintInfo() && impl != null) {
            ILog iLog = impl;
            iLog.i(tagPre + str, format(str2, objArr));
        }
    }

    public static void i(String str, String str2, Throwable th, Object... objArr) {
        if (shouldPrintInfo() && impl != null) {
            ILog iLog = impl;
            iLog.i(tagPre + str, format(str2, objArr), th);
        }
    }

    public static void v(String str, String str2, Object... objArr) {
        if (shouldPrintVerbose() && impl != null) {
            ILog iLog = impl;
            iLog.v(tagPre + str, format(str2, objArr));
        }
    }

    public static void v(String str, String str2, Throwable th, Object... objArr) {
        if (shouldPrintVerbose() && impl != null) {
            ILog iLog = impl;
            iLog.v(tagPre + str, format(str2, objArr), th);
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (shouldPrintWarn() && impl != null) {
            ILog iLog = impl;
            iLog.w(tagPre + str, format(str2, objArr));
        }
    }

    public static void w(String str, String str2, Throwable th, Object... objArr) {
        if (shouldPrintWarn() && impl != null) {
            ILog iLog = impl;
            iLog.w(tagPre + str, format(str2, objArr), th);
        }
    }

    public static boolean shouldPrintDebug() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.DEBUG.getLogLevel());
    }

    public static boolean shouldPrintError() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.ERROR.getLogLevel());
    }

    public static boolean shouldPrintInfo() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.INFO.getLogLevel());
    }

    public static boolean shouldPrintVerbose() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.VERBOSE.getLogLevel());
    }

    public static boolean shouldPrintWarn() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.WARNING.getLogLevel());
    }

    private static String format(String str, Object[] objArr) {
        return (objArr == null || objArr.length == 0) ? str : String.format(str, objArr);
    }
}
