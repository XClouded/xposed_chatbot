package com.ali.telescope.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class DeviceStatus {
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception unused) {
            return 75;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long getDeviceTotalAvailMemory(android.content.Context r7) {
        /*
            android.app.ActivityManager$MemoryInfo r0 = new android.app.ActivityManager$MemoryInfo
            r0.<init>()
            java.lang.String r1 = "activity"
            java.lang.Object r7 = r7.getSystemService(r1)
            android.app.ActivityManager r7 = (android.app.ActivityManager) r7
            r7.getMemoryInfo(r0)
            com.ali.telescope.data.DeviceInfoManager r7 = com.ali.telescope.data.DeviceInfoManager.instance()
            int r7 = r7.getApiLevel()
            r1 = 0
            r3 = 16
            if (r7 < r3) goto L_0x0029
            long r3 = r0.availMem     // Catch:{ Throwable -> 0x0025 }
            r5 = 1024(0x400, double:5.06E-321)
            long r3 = r3 / r5
            long r3 = r3 / r5
            goto L_0x002a
        L_0x0025:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0029:
            r3 = r1
        L_0x002a:
            int r7 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r7 != 0) goto L_0x0033
            int r7 = com.ali.telescope.data.DeviceInfoManager.getTotalMemFromFile()
            long r3 = (long) r7
        L_0x0033:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.data.DeviceStatus.getDeviceTotalAvailMemory(android.content.Context):long");
    }

    @TargetApi(18)
    public static int getStoreFreeSize() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return (int) (((statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong()) / 1024) / 1024);
        } catch (Exception unused) {
            return 0;
        }
    }
}
