package com.ali.alihadeviceevaluator.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public final class ProcessUtils {
    private static String sProcessName;

    public static boolean isInMainProcess() {
        if (Global.context == null) {
            return false;
        }
        String currProcessName = getCurrProcessName();
        String packageName = Global.context.getPackageName();
        if (TextUtils.isEmpty(currProcessName) || !currProcessName.equals(packageName)) {
            return false;
        }
        return true;
    }

    public static String getCurrProcessName() {
        Application application = Global.context;
        if (TextUtils.isEmpty(sProcessName)) {
            String currentProcessNameViaLinuxFile = getCurrentProcessNameViaLinuxFile();
            if (TextUtils.isEmpty(currentProcessNameViaLinuxFile) && application != null) {
                currentProcessNameViaLinuxFile = getCurrentProcessNameViaActivityManager(application);
            }
            sProcessName = currentProcessNameViaLinuxFile;
        }
        return sProcessName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0035 A[SYNTHETIC, Splitter:B:12:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c A[SYNTHETIC, Splitter:B:19:0x003c] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getCurrentProcessNameViaLinuxFile() {
        /*
            int r0 = android.os.Process.myPid()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "/proc/"
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = "/cmdline"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r1]
            r2 = 0
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0039, all -> 0x0032 }
            r4.<init>(r0)     // Catch:{ Exception -> 0x0039, all -> 0x0032 }
            int r0 = r4.read(r1)     // Catch:{ Exception -> 0x0030, all -> 0x002d }
            r4.close()     // Catch:{ Exception -> 0x0040 }
            goto L_0x0040
        L_0x002d:
            r0 = move-exception
            r3 = r4
            goto L_0x0033
        L_0x0030:
            goto L_0x003a
        L_0x0032:
            r0 = move-exception
        L_0x0033:
            if (r3 == 0) goto L_0x0038
            r3.close()     // Catch:{ Exception -> 0x0038 }
        L_0x0038:
            throw r0
        L_0x0039:
            r4 = r3
        L_0x003a:
            if (r4 == 0) goto L_0x003f
            r4.close()     // Catch:{ Exception -> 0x003f }
        L_0x003f:
            r0 = 0
        L_0x0040:
            if (r0 <= 0) goto L_0x004b
            java.lang.String r3 = new java.lang.String
            r3.<init>(r1, r2, r0)
            java.lang.String r3 = r3.trim()
        L_0x004b:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.util.ProcessUtils.getCurrentProcessNameViaLinuxFile():java.lang.String");
    }

    private static String getCurrentProcessNameViaActivityManager(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        if (context == null) {
            return null;
        }
        if (sProcessName != null) {
            return sProcessName;
        }
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return null;
        }
        Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
        while (true) {
            if (it.hasNext()) {
                ActivityManager.RunningAppProcessInfo next = it.next();
                if (next != null && next.pid == myPid) {
                    sProcessName = next.processName;
                    break;
                }
            } else {
                break;
            }
        }
        return sProcessName;
    }
}
