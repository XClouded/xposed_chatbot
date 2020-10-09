package com.ali.alihadeviceevaluator;

public final class AliHardware {
    public static final String CONFIG_PEROID = "peroid";
    public static final String CONFIG_REPORT_PEROID = "reportPeroid";
    public static final String CONFIG_SWITCH = "switch";
    public static final int DEVICE_LEVEL_0 = 0;
    public static final int DEVICE_LEVEL_1 = 1;
    public static final int DEVICE_LEVEL_2 = 2;
    public static final int DEVICE_LEVEL_CLOSE = -3;
    public static final int DEVICE_LEVEL_INNER_ERROR = -1;
    public static final int DEVICE_LEVEL_NOT_READY = -2;
    private static HardwareDelegate mDelegate;

    public static int getDeviceLevel() {
        if (mDelegate != null) {
            return mDelegate.getDeviceLevel();
        }
        return -2;
    }

    public static float getDeviceScore() {
        if (mDelegate != null) {
            return (float) mDelegate.getDeviceScore();
        }
        return -2.0f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0060 A[SYNTHETIC, Splitter:B:17:0x0060] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void effectConfig(java.util.HashMap<java.lang.String, java.lang.String> r7) {
        /*
            java.lang.String r0 = "peroid"
            java.lang.Object r0 = r7.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 0
            r3 = 1
            if (r1 != 0) goto L_0x0027
            java.lang.Long r1 = java.lang.Long.valueOf(r0)     // Catch:{ NumberFormatException -> 0x0023 }
            long r4 = r1.longValue()     // Catch:{ NumberFormatException -> 0x0023 }
            android.content.SharedPreferences$Editor r1 = com.ali.alihadeviceevaluator.util.KVStorageUtils.getEditor()     // Catch:{ NumberFormatException -> 0x0023 }
            java.lang.String r6 = "validperiod"
            r1.putLong(r6, r4)     // Catch:{ NumberFormatException -> 0x0023 }
            r1 = 1
            goto L_0x0028
        L_0x0023:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0027:
            r1 = 0
        L_0x0028:
            java.lang.String r4 = "switch"
            java.lang.Object r4 = r7.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x0052
            java.lang.String r1 = "True"
            boolean r1 = r4.equalsIgnoreCase(r1)
            if (r1 == 0) goto L_0x0048
            android.content.SharedPreferences$Editor r1 = com.ali.alihadeviceevaluator.util.KVStorageUtils.getEditor()
            java.lang.String r2 = "switch"
            r1.putBoolean(r2, r3)
            goto L_0x0051
        L_0x0048:
            android.content.SharedPreferences$Editor r1 = com.ali.alihadeviceevaluator.util.KVStorageUtils.getEditor()
            java.lang.String r5 = "switch"
            r1.putBoolean(r5, r2)
        L_0x0051:
            r1 = 1
        L_0x0052:
            java.lang.String r2 = "reportPeroid"
            java.lang.Object r7 = r7.get(r2)
            java.lang.String r7 = (java.lang.String) r7
            boolean r2 = android.text.TextUtils.isEmpty(r7)
            if (r2 != 0) goto L_0x0077
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ NumberFormatException -> 0x0073 }
            long r5 = r7.longValue()     // Catch:{ NumberFormatException -> 0x0073 }
            android.content.SharedPreferences$Editor r7 = com.ali.alihadeviceevaluator.util.KVStorageUtils.getEditor()     // Catch:{ NumberFormatException -> 0x0073 }
            java.lang.String r2 = "report_validperiod"
            r7.putLong(r2, r5)     // Catch:{ NumberFormatException -> 0x0073 }
            r1 = 1
            goto L_0x0077
        L_0x0073:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0077:
            java.lang.String r7 = "DeviceEvaluator"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "effectConfig, strPeroid:"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = ", switchOpen:"
            r2.append(r0)
            r2.append(r4)
            java.lang.String r0 = r2.toString()
            android.util.Log.d(r7, r0)
            if (r1 == 0) goto L_0x009e
            android.content.SharedPreferences$Editor r7 = com.ali.alihadeviceevaluator.util.KVStorageUtils.getEditor()
            r7.commit()
        L_0x009e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.AliHardware.effectConfig(java.util.HashMap):void");
    }

    static void setDelegate(HardwareDelegate hardwareDelegate) {
        mDelegate = hardwareDelegate;
    }
}
