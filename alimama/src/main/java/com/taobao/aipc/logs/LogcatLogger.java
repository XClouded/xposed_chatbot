package com.taobao.aipc.logs;

import android.util.Log;
import androidx.annotation.NonNull;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogcatLogger implements ILogger {
    private static final int MAX_LOG_LENGTH = 4000;

    public void log(int i, String str, String str2, Throwable th) {
        if (str2 != null && str2.length() == 0) {
            str2 = null;
        }
        if (str2 == null) {
            if (th != null) {
                str2 = getStackTraceString(th);
            } else {
                return;
            }
        } else if (th != null) {
            str2 = str2 + "\n" + getStackTraceString(th);
        }
        log(i, str, str2);
    }

    private String getStackTraceString(Throwable th) {
        StringWriter stringWriter = new StringWriter(256);
        PrintWriter printWriter = new PrintWriter(stringWriter, false);
        th.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }

    public void log(int i, String str, String str2) {
        int length = str2.length() / 4000;
        if (length > 0) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = i3 + 4000;
                logSub(i, str, str2.substring(i3, i4));
                i2++;
                i3 = i4;
            }
            logSub(i, str, str2.substring(i3, str2.length()));
            return;
        }
        logSub(i, str, str2);
    }

    private void logSub(int i, @NonNull String str, @NonNull String str2) {
        switch (i) {
            case 2:
                Log.v(str, str2);
                return;
            case 3:
                Log.d(str, str2);
                return;
            case 4:
                Log.i(str, str2);
                return;
            case 5:
                Log.w(str, str2);
                return;
            case 6:
                Log.e(str, str2);
                return;
            case 7:
                Log.wtf(str, str2);
                return;
            default:
                Log.v(str, str2);
                return;
        }
    }
}
