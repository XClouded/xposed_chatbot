package com.taobao.uikit.extend.utils;

import android.os.Build;

public class DeviceUtils {
    public static boolean isMIUIDevice() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    public static boolean isMeizuDevice() {
        return "Meizu".endsWith(Build.MANUFACTURER);
    }

    public static boolean isHUWEIDevice() {
        return "HUAWEI".equalsIgnoreCase(Build.MANUFACTURER);
    }
}
