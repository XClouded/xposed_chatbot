package com.alibaba.analytics.utils;

import android.util.Log;

public class SystemProperties {
    private static final String TAG = "SystemProperties";

    public static String get(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception e) {
            Log.e(TAG, "get() ERROR!!! Exception!", e);
            return "";
        }
    }

    public static String get(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{str, str2});
        } catch (Exception e) {
            Log.e(TAG, "get() ERROR!!! Exception!", e);
            return str2;
        }
    }
}
