package com.huawei.hianalytics.v2;

import android.content.Context;
import com.huawei.hianalytics.process.HiAnalyticsInstance;
import com.huawei.hianalytics.process.HiAnalyticsManager;
import com.huawei.hianalytics.process.b;
import java.util.LinkedHashMap;

public abstract class HiAnalytics {
    private static HiAnalyticsInstance defaultInstance;

    public static void clearCachedData() {
        HiAnalyticsManager.clearCachedData();
    }

    private static synchronized HiAnalyticsInstance getDefaultInstance() {
        HiAnalyticsInstance hiAnalyticsInstance;
        synchronized (HiAnalytics.class) {
            if (defaultInstance == null) {
                defaultInstance = HiAnalyticsManager.getInstanceByTag("_default_config_tag");
            }
            hiAnalyticsInstance = defaultInstance;
        }
        return hiAnalyticsInstance;
    }

    public static boolean getInitFlag() {
        return HiAnalyticsManager.getInitFlag("_default_config_tag");
    }

    public static void handleV1Cache() {
        b.a().a("_default_config_tag");
    }

    public static void onBackground(long j) {
        if (getDefaultInstance() != null) {
            defaultInstance.onBackground(j);
        }
    }

    public static void onEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onEvent(i, str, linkedHashMap);
        }
    }

    @Deprecated
    public static void onEvent(Context context, String str, String str2) {
        if (getDefaultInstance() != null) {
            defaultInstance.onEvent(context, str, str2);
        }
    }

    public static void onEvent(String str, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onEvent(0, str, linkedHashMap);
        }
    }

    public static void onForeground(long j) {
        if (getDefaultInstance() != null) {
            defaultInstance.onForeground(j);
        }
    }

    public static void onPause(Context context) {
        if (getDefaultInstance() != null) {
            defaultInstance.onPause(context);
        }
    }

    public static void onPause(Context context, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onPause(context, linkedHashMap);
        }
    }

    public static void onPause(String str, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onPause(str, linkedHashMap);
        }
    }

    public static void onReport() {
        if (getDefaultInstance() != null) {
            defaultInstance.onReport(-1);
        }
    }

    @Deprecated
    public static void onReport(Context context) {
        if (getDefaultInstance() != null) {
            defaultInstance.onReport(context, -1);
        }
    }

    public static void onResume(Context context) {
        if (getDefaultInstance() != null) {
            defaultInstance.onResume(context);
        }
    }

    public static void onResume(Context context, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onResume(context, linkedHashMap);
        }
    }

    public static void onResume(String str, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onResume(str, linkedHashMap);
        }
    }

    public static void onStreamEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap) {
        if (getDefaultInstance() != null) {
            defaultInstance.onStreamEvent(i, str, linkedHashMap);
        }
    }

    public static void setIsOaidTracking(boolean z) {
        if (getDefaultInstance() != null) {
            defaultInstance.setOAIDTrackingFlag(1, z);
            defaultInstance.setOAIDTrackingFlag(0, z);
            defaultInstance.setOAIDTrackingFlag(3, z);
            defaultInstance.setOAIDTrackingFlag(2, z);
        }
    }

    public static void setOAID(String str) {
        if (getDefaultInstance() != null) {
            defaultInstance.setOAID(1, str);
            defaultInstance.setOAID(0, str);
            defaultInstance.setOAID(3, str);
            defaultInstance.setOAID(2, str);
        }
    }

    public static void setUPID(String str) {
        if (getDefaultInstance() != null) {
            defaultInstance.setUpid(1, str);
            defaultInstance.setUpid(0, str);
            defaultInstance.setUpid(3, str);
            defaultInstance.setUpid(2, str);
        }
    }
}
