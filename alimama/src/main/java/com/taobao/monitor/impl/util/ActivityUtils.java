package com.taobao.monitor.impl.util;

import android.app.Activity;

public class ActivityUtils {
    public static String getPageName(Activity activity) {
        return activity == null ? "" : activity.getClass().getName();
    }

    public static String getSimpleName(Activity activity) {
        return activity == null ? "" : activity.getClass().getSimpleName();
    }
}
