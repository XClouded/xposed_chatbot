package com.huawei.hianalytics.process;

import java.util.List;

public abstract class HiAnalyticsManager {
    public static void clearCachedData() {
        a.b().f();
    }

    public static List<String> getAllTags() {
        return a.b().c();
    }

    public static boolean getInitFlag(String str) {
        return a.b().b(str);
    }

    public static HiAnalyticsInstance getInstanceByTag(String str) {
        return a.b().a(str);
    }

    public static HiAnalyticsInstanceEx getInstanceEx() {
        return a.b().d();
    }

    public static void setAppid(String str) {
        a.b().e(str);
    }

    public static void setCacheSize(int i) {
        a.b().a(i);
    }

    public static void setUnusualDataIgnored(boolean z) {
        a.b().a(z);
    }
}
