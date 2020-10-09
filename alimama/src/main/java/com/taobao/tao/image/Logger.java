package com.taobao.tao.image;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.tlog.adapter.AdapterForTLog;
import java.util.Formatter;
import java.util.Locale;

public class Logger {
    public static final String COMMON_TAG = "STRATEGY.ALL";
    private static final Object FORMAT_LOCK = new Object();
    public static final char LEVEL_D = 'D';
    public static final char LEVEL_E = 'E';
    public static final char LEVEL_I = 'I';
    public static final char LEVEL_L = 'L';
    public static final char LEVEL_V = 'V';
    public static final char LEVEL_W = 'W';
    private static Formatter sFormatter;
    public static final char[] sLogTypes = {LEVEL_V, LEVEL_D, LEVEL_I, LEVEL_W, LEVEL_E, LEVEL_L};
    private static Integer sMinLogLevel;
    private static StringBuilder sSB;
    private static boolean sTLogValid = AdapterForTLog.isValid();

    private static int getLogTypeIndex(char c) {
        for (int i = 0; i < sLogTypes.length; i++) {
            if (sLogTypes[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static void setMinLogLevel(int i) {
        switch (i) {
            case 2:
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_V));
                return;
            case 3:
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_D));
                return;
            case 4:
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_I));
                return;
            case 5:
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_W));
                return;
            case 6:
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_E));
                return;
            default:
                return;
        }
    }

    public static void setTLogValid(boolean z) {
        sTLogValid = z;
    }

    public static boolean isLoggable(char c) {
        if (sMinLogLevel == null) {
            if (sTLogValid) {
                String logLevel = AdapterForTLog.getLogLevel();
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(TextUtils.isEmpty(logLevel) ? LEVEL_L : logLevel.charAt(0)));
            } else {
                sMinLogLevel = Integer.valueOf(getLogTypeIndex(LEVEL_V));
            }
        }
        if (getLogTypeIndex(c) >= sMinLogLevel.intValue()) {
            return true;
        }
        return false;
    }

    public static void v(String str, String str2, Object... objArr) {
        if (!isLoggable(LEVEL_V)) {
            return;
        }
        if (sTLogValid) {
            AdapterForTLog.logv(str, fastFormat(str2, objArr));
        } else {
            Log.v(str, fastFormat(str2, objArr));
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (!isLoggable(LEVEL_D)) {
            return;
        }
        if (sTLogValid) {
            AdapterForTLog.logd(str, fastFormat(str2, objArr));
        } else {
            Log.d(str, fastFormat(str2, objArr));
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (!isLoggable(LEVEL_I)) {
            return;
        }
        if (sTLogValid) {
            AdapterForTLog.logi(str, fastFormat(str2, objArr));
        } else {
            Log.i(str, fastFormat(str2, objArr));
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (!isLoggable(LEVEL_W)) {
            return;
        }
        if (sTLogValid) {
            AdapterForTLog.logw(str, fastFormat(str2, objArr));
        } else {
            Log.w(str, fastFormat(str2, objArr));
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (!isLoggable(LEVEL_E)) {
            return;
        }
        if (sTLogValid) {
            AdapterForTLog.loge(str, fastFormat(str2, objArr));
        } else {
            Log.e(str, fastFormat(str2, objArr));
        }
    }

    private static String fastFormat(String str, Object... objArr) {
        String substring;
        synchronized (FORMAT_LOCK) {
            if (sSB == null) {
                sSB = new StringBuilder(250);
            } else {
                sSB.setLength(0);
            }
            if (sFormatter == null) {
                sFormatter = new Formatter(sSB, Locale.getDefault());
            }
            sFormatter.format(str, objArr);
            substring = sSB.substring(0);
        }
        return substring;
    }
}
