package com.alibaba.android.anynetwork.core.utils;

public class Utils {
    private static final String TAG = "Utils";

    public static int getInt(Object obj, int i) {
        boolean z;
        if (obj == null || !((z = obj instanceof Integer))) {
            return i;
        }
        if (z) {
            return ((Integer) obj).intValue();
        }
        ANLog.e(TAG, "getInt with wrong type:" + obj.getClass() + ",  " + obj);
        return i;
    }

    public static boolean getBoolean(Object obj, boolean z) {
        boolean z2;
        if (obj == null || !((z2 = obj instanceof Boolean))) {
            return z;
        }
        if (z2) {
            return ((Boolean) obj).booleanValue();
        }
        ANLog.e(TAG, "getBoolean with wrong type:" + obj.getClass() + ",  " + obj);
        return z;
    }

    public static long getLong(Object obj, long j) {
        boolean z;
        if (obj == null || !((z = obj instanceof Long))) {
            return j;
        }
        if (z) {
            return ((Long) obj).longValue();
        }
        ANLog.e(TAG, "getLong with wrong type:" + obj.getClass() + ",  " + obj);
        return j;
    }
}
