package com.alibaba.taffy.core.util.lang;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class SystemUtil {
    public static long getSdCardTotalSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    public static long getSdCardFreeSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getFreeBlocks());
    }

    public static long getRomTotalSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    public static long getRomFreeSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getFreeBlocks());
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0035 A[SYNTHETIC, Splitter:B:16:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003e A[SYNTHETIC, Splitter:B:23:0x003e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long getMemoryTotalSize() {
        /*
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            java.lang.String r4 = "/proc/meminfo"
            r3.<init>(r4)     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x0039, all -> 0x0032 }
            java.lang.String r0 = r1.readLine()     // Catch:{ IOException -> 0x0030, all -> 0x002b }
            java.lang.String r2 = "\\s+"
            java.lang.String[] r0 = r0.split(r2)     // Catch:{ IOException -> 0x0030, all -> 0x002b }
            r2 = 1
            r0 = r0[r2]     // Catch:{ IOException -> 0x0030, all -> 0x002b }
            long r2 = java.lang.Long.parseLong(r0)     // Catch:{ IOException -> 0x0030, all -> 0x002b }
            r4 = 1024(0x400, double:5.06E-321)
            long r2 = r2 * r4
            r1.close()     // Catch:{ IOException -> 0x002a }
        L_0x002a:
            return r2
        L_0x002b:
            r0 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0033
        L_0x0030:
            r0 = r1
            goto L_0x003a
        L_0x0032:
            r1 = move-exception
        L_0x0033:
            if (r0 == 0) goto L_0x0038
            r0.close()     // Catch:{ IOException -> 0x0038 }
        L_0x0038:
            throw r1
        L_0x0039:
        L_0x003a:
            r1 = -1
            if (r0 == 0) goto L_0x0041
            r0.close()     // Catch:{ IOException -> 0x0041 }
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.lang.SystemUtil.getMemoryTotalSize():long");
    }

    @TargetApi(16)
    public static long getMemoryTotalSize(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0038 A[SYNTHETIC, Splitter:B:16:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0041 A[SYNTHETIC, Splitter:B:23:0x0041] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long getMemoryFreeSize() {
        /*
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            java.lang.String r4 = "/proc/meminfo"
            r3.<init>(r4)     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x003c, all -> 0x0035 }
            r1.readLine()     // Catch:{ IOException -> 0x0033, all -> 0x002e }
            java.lang.String r0 = r1.readLine()     // Catch:{ IOException -> 0x0033, all -> 0x002e }
            java.lang.String r2 = "\\s+"
            java.lang.String[] r0 = r0.split(r2)     // Catch:{ IOException -> 0x0033, all -> 0x002e }
            r2 = 1
            r0 = r0[r2]     // Catch:{ IOException -> 0x0033, all -> 0x002e }
            long r2 = java.lang.Long.parseLong(r0)     // Catch:{ IOException -> 0x0033, all -> 0x002e }
            r4 = 1024(0x400, double:5.06E-321)
            long r2 = r2 * r4
            r1.close()     // Catch:{ IOException -> 0x002d }
        L_0x002d:
            return r2
        L_0x002e:
            r0 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
            goto L_0x0036
        L_0x0033:
            r0 = r1
            goto L_0x003d
        L_0x0035:
            r1 = move-exception
        L_0x0036:
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x003b }
        L_0x003b:
            throw r1
        L_0x003c:
        L_0x003d:
            r1 = -1
            if (r0 == 0) goto L_0x0044
            r0.close()     // Catch:{ IOException -> 0x0044 }
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.lang.SystemUtil.getMemoryFreeSize():long");
    }

    public static long getMemoryFreeSize(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static String getUniqueID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (StringUtil.isNotEmpty(telephonyManager.getDeviceId())) {
            return telephonyManager.getDeviceId();
        }
        String macAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        if (StringUtil.isNotEmpty(macAddress)) {
            return macAddress;
        }
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String geDeviceFactoryName() {
        return Build.MANUFACTURER;
    }

    public static String getSimSerialNumber(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
    }
}
