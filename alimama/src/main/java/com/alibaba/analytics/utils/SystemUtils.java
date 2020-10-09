package com.alibaba.analytics.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class SystemUtils {
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x006d A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCpuInfo() {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ Exception -> 0x003f }
            java.lang.String r2 = "/proc/cpuinfo"
            r1.<init>(r2)     // Catch:{ Exception -> 0x003f }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0023 }
            r3 = 1024(0x400, float:1.435E-42)
            r2.<init>(r1, r3)     // Catch:{ IOException -> 0x0023 }
            java.lang.String r3 = r2.readLine()     // Catch:{ IOException -> 0x0023 }
            r2.close()     // Catch:{ IOException -> 0x001f, Exception -> 0x001b }
            r1.close()     // Catch:{ IOException -> 0x001f, Exception -> 0x001b }
            r0 = r3
            goto L_0x005a
        L_0x001b:
            r0 = move-exception
            r1 = r0
            r0 = r3
            goto L_0x0040
        L_0x001f:
            r0 = move-exception
            r1 = r0
            r0 = r3
            goto L_0x0024
        L_0x0023:
            r1 = move-exception
        L_0x0024:
            java.lang.String r2 = "SystemUtils"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x003f }
            r3.<init>()     // Catch:{ Exception -> 0x003f }
            java.lang.String r4 = "Could not read from file /proc/cpuinfo :"
            r3.append(r4)     // Catch:{ Exception -> 0x003f }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x003f }
            r3.append(r1)     // Catch:{ Exception -> 0x003f }
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x003f }
            android.util.Log.e(r2, r1)     // Catch:{ Exception -> 0x003f }
            goto L_0x005a
        L_0x003f:
            r1 = move-exception
        L_0x0040:
            java.lang.String r2 = "SystemUtils"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "BaseParameter-Could not open file /proc/cpuinfo :"
            r3.append(r4)
            java.lang.String r1 = r1.toString()
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            android.util.Log.e(r2, r1)
        L_0x005a:
            if (r0 == 0) goto L_0x006d
            r1 = 58
            int r1 = r0.indexOf(r1)
            int r1 = r1 + 1
            java.lang.String r0 = r0.substring(r1)
            java.lang.String r0 = r0.trim()
            return r0
        L_0x006d:
            java.lang.String r0 = ""
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.SystemUtils.getCpuInfo():java.lang.String");
    }

    public static double getSystemFreeSize() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            double availableBytes = (double) statFs.getAvailableBytes();
            Double.isNaN(availableBytes);
            return (availableBytes / 1024.0d) / 1024.0d;
        }
        double freeBytes = (double) statFs.getFreeBytes();
        Double.isNaN(freeBytes);
        return (freeBytes / 1024.0d) / 1024.0d;
    }
}
