package com.alibaba.ut;

import android.app.Activity;
import com.ut.mini.UTAnalytics;
import java.util.Map;

public class UTTrackerAdapter {
    public static void updateNextPageProperties(Map<String, String> map) {
        try {
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(map);
        } catch (Throwable unused) {
        }
    }

    public static void pageAppear(Activity activity) {
        try {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(activity);
        } catch (Throwable unused) {
        }
    }

    public static void updatePageProperties(Activity activity, Map<String, String> map) {
        if (map != null) {
            map.put("produceBy", "ut4aplus");
        }
        UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, map);
    }

    public static void updatePageUtparam(Activity activity, String str) {
        UTAnalytics.getInstance().getDefaultTracker().updatePageUtparam(activity, str);
    }

    public static void pageDisAppear(Activity activity) {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
    }
}
