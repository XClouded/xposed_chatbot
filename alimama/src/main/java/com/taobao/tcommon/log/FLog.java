package com.taobao.tcommon.log;

import java.util.Locale;

public class FLog {
    private static FormatLog sFormatLog;

    public static void setFormatLog(FormatLog formatLog) {
        sFormatLog = formatLog;
    }

    private static void ensureFormatLog() {
        if (sFormatLog == null) {
            sFormatLog = new DefaultFormatLog();
        }
    }

    public static void setMinLevel(int i) {
        ensureFormatLog();
        sFormatLog.setMinLevel(i);
    }

    public static String unitSize(long j) {
        String str;
        if (j <= 0) {
            return String.valueOf(j);
        }
        float f = (float) j;
        String str2 = "B";
        if (f > 900.0f) {
            str2 = "KB";
            f /= 1024.0f;
        }
        if (f > 900.0f) {
            str2 = "MB";
            f /= 1024.0f;
        }
        if (f > 900.0f) {
            str2 = "GB";
            f /= 1024.0f;
        }
        if (f > 900.0f) {
            str2 = "TB";
            f /= 1024.0f;
        }
        if (f > 900.0f) {
            str2 = "PB";
            f /= 1024.0f;
        }
        if (f < 1.0f) {
            str = String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f)});
        } else if (f < 10.0f) {
            str = String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f)});
        } else if (f < 100.0f) {
            str = String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f)});
        } else {
            str = String.format(Locale.getDefault(), "%.0f", new Object[]{Float.valueOf(f)});
        }
        return str + str2;
    }

    public static String formatFileSize(String str, Object... objArr) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int indexOf = str.indexOf(37, i);
            if (indexOf >= 0 && indexOf < length - 1) {
                i = indexOf + 1;
                char charAt = str.charAt(i);
                if (Character.isLetter(charAt)) {
                    if (charAt == 'K' && i2 < objArr.length) {
                        objArr[i2] = unitSize((long) objArr[i2].intValue());
                        bytes[i] = 115;
                    }
                    i2++;
                }
            }
        }
        return new String(bytes);
    }

    public static boolean isLoggable(int i) {
        ensureFormatLog();
        return sFormatLog.isLoggable(i);
    }

    public static void v(String str, String str2, Object... objArr) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(2)) {
            sFormatLog.v(str, formatFileSize(str2, objArr), objArr);
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(3)) {
            sFormatLog.d(str, formatFileSize(str2, objArr), objArr);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(4)) {
            sFormatLog.i(str, str2, objArr);
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(5)) {
            sFormatLog.w(str, str2, objArr);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(6)) {
            sFormatLog.e(str, str2, objArr);
        }
    }

    public static void e(int i, String str, String str2) {
        ensureFormatLog();
        if (sFormatLog.isLoggable(6)) {
            sFormatLog.e(6, str, str2);
        }
    }
}
