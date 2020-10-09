package com.ta.audid.permission;

import android.content.Context;

public class PermissionUtils {
    public static boolean checkStoragePermissionGranted(Context context) {
        try {
            return selfPermissionGranted(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean checkReadPhoneStatePermissionGranted(Context context) {
        try {
            return selfPermissionGranted(context, "android.permission.READ_PHONE_STATE");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean checkWifiStatePermissionGranted(Context context) {
        try {
            return selfPermissionGranted(context, "android.permission.ACCESS_WIFI_STATE");
        } catch (Throwable unused) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0017 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean selfPermissionGranted(android.content.Context r5, java.lang.String r6) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r1 = com.ta.audid.utils.AppInfoUtils.getTargetSdkVersion(r5)
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 23
            r4 = 1
            if (r2 < r3) goto L_0x0020
            if (r1 < r3) goto L_0x0019
            int r5 = r5.checkSelfPermission(r6)
            if (r5 != 0) goto L_0x0027
        L_0x0017:
            r0 = 1
            goto L_0x0027
        L_0x0019:
            int r5 = com.ta.audid.permission.PermissionChecker.checkSelfPermission(r5, r6)
            if (r5 != 0) goto L_0x0027
            goto L_0x0017
        L_0x0020:
            int r5 = com.ta.audid.permission.PermissionChecker.checkSelfPermission(r5, r6)
            if (r5 != 0) goto L_0x0027
            goto L_0x0017
        L_0x0027:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.permission.PermissionUtils.selfPermissionGranted(android.content.Context, java.lang.String):boolean");
    }
}
