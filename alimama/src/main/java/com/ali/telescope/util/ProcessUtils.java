package com.ali.telescope.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public class ProcessUtils {
    private static String sProcessName;

    public static String getProcessName(Context context) {
        if (TextUtils.isEmpty(sProcessName)) {
            String processNameViaAM = getProcessNameViaAM(context);
            if (TextUtils.isEmpty(processNameViaAM)) {
                processNameViaAM = getProcessNameViaLinuxFile();
            }
            sProcessName = processNameViaAM;
        }
        return sProcessName;
    }

    public static String getSimpleProcessName(Context context) {
        String processName = getProcessName(context);
        return !TextUtils.isEmpty(processName) ? processName.replace(context.getPackageName(), "").replace(":", "") : "";
    }

    private static String getProcessNameViaAM(Context context) {
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

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getProcessNameViaLinuxFile() {
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
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            r4.<init>(r0)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            int r0 = r4.read(r1)     // Catch:{ Exception -> 0x002d }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r4)
            goto L_0x0042
        L_0x002d:
            r0 = move-exception
            goto L_0x0034
        L_0x002f:
            r0 = move-exception
            r4 = r2
            goto L_0x004f
        L_0x0032:
            r0 = move-exception
            r4 = r2
        L_0x0034:
            java.lang.String r5 = "ProcessUtils"
            r6 = 1
            java.lang.Exception[] r6 = new java.lang.Exception[r6]     // Catch:{ all -> 0x004e }
            r6[r3] = r0     // Catch:{ all -> 0x004e }
            com.ali.telescope.util.TelescopeLog.w(r5, r6)     // Catch:{ all -> 0x004e }
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r4)
            r0 = 0
        L_0x0042:
            if (r0 <= 0) goto L_0x004d
            java.lang.String r2 = new java.lang.String
            r2.<init>(r1, r3, r0)
            java.lang.String r2 = r2.trim()
        L_0x004d:
            return r2
        L_0x004e:
            r0 = move-exception
        L_0x004f:
            com.ali.telescope.util.IOUtils.closeQuietly((java.io.Closeable) r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.util.ProcessUtils.getProcessNameViaLinuxFile():java.lang.String");
    }
}
