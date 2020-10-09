package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;
import com.xiaomi.push.dg;
import com.xiaomi.push.dh;
import java.io.File;

public class Logger {
    private static boolean sDisablePushLog = false;
    private static LoggerInterface sUserLogger;

    public static void disablePushFileLog(Context context) {
        sDisablePushLog = true;
        setPushLog(context);
    }

    public static void enablePushFileLog(Context context) {
        sDisablePushLog = false;
        setPushLog(context);
    }

    public static File getLogFile(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] listFiles = file.listFiles();
                    for (int i = 0; i < listFiles.length; i++) {
                        if (listFiles[i].isFile() && !listFiles[i].getName().contains("lock") && listFiles[i].getName().contains("log")) {
                            return listFiles[i];
                        }
                    }
                    return null;
                }
            }
            return null;
        } catch (NullPointerException unused) {
            b.d("null pointer exception while retrieve file.");
        }
    }

    protected static LoggerInterface getUserLogger() {
        return sUserLogger;
    }

    private static boolean hasWritePermission(Context context) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null) {
                for (String equals : strArr) {
                    if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(equals)) {
                        return true;
                    }
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static void setLogger(Context context, LoggerInterface loggerInterface) {
        sUserLogger = loggerInterface;
        setPushLog(context);
    }

    public static void setPushLog(Context context) {
        boolean z = false;
        boolean z2 = sUserLogger != null;
        if (sDisablePushLog) {
            z2 = false;
        } else if (hasWritePermission(context)) {
            z = true;
        }
        dh dhVar = null;
        LoggerInterface loggerInterface = z2 ? sUserLogger : null;
        if (z) {
            dhVar = new dh(context);
        }
        b.a((LoggerInterface) new dg(loggerInterface, dhVar));
    }

    public static void uploadLogFile(Context context, boolean z) {
        ai.a(context).a((Runnable) new w(context, z));
    }
}
