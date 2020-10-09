package com.uploader.implement;

import android.util.Log;
import com.uploader.export.IUploaderLog;

/* compiled from: LogTool */
public class a {
    private static volatile IUploaderLog a = null;
    private static volatile int b = 31;

    static final void a(IUploaderLog iUploaderLog) {
        a = iUploaderLog;
    }

    public static final int a(int i, String str, String str2) {
        return a(i, str, str2, (Throwable) null);
    }

    public static final int a(int i, String str, String str2, Throwable th) {
        if (str != null) {
            str = "<aus>" + str;
        }
        IUploaderLog iUploaderLog = a;
        if (iUploaderLog != null) {
            return iUploaderLog.print(i, str, str2, th);
        }
        if ((b & i) == 0) {
            return 0;
        }
        if (i == 4) {
            if (th != null) {
                str2 = str2 + 10 + Log.getStackTraceString(th);
            }
            return Log.println(4, str, str2);
        } else if (i == 8) {
            if (th != null) {
                str2 = str2 + 10 + Log.getStackTraceString(th);
            }
            return Log.println(5, str, str2);
        } else if (i != 16) {
            switch (i) {
                case 1:
                    if (th != null) {
                        str2 = str2 + 10 + Log.getStackTraceString(th);
                    }
                    return Log.println(2, str, str2);
                case 2:
                    if (th != null) {
                        str2 = str2 + 10 + Log.getStackTraceString(th);
                    }
                    return Log.println(3, str, str2);
                default:
                    return 0;
            }
        } else {
            if (th != null) {
                str2 = str2 + 10 + Log.getStackTraceString(th);
            }
            return Log.println(6, str, str2);
        }
    }

    public static final boolean a(int i) {
        IUploaderLog iUploaderLog = a;
        if (iUploaderLog != null) {
            return iUploaderLog.isEnabled(i);
        }
        return (i & b) != 0;
    }
}
