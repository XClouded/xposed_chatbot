package com.alibaba.analytics.utils;

import android.content.Context;
import java.util.Map;

@Deprecated
public class DeviceUtil {
    public static String getAppVersion(Context context) {
        return "";
    }

    public static String getUUID() {
        return "";
    }

    public static boolean isYunOSSystem() {
        return false;
    }

    public static Map<String, String> getDeviceInfo(Context context) {
        return UTMCDevice.getDeviceInfo(context);
    }
}
