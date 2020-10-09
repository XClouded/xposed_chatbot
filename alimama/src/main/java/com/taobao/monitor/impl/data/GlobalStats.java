package com.taobao.monitor.impl.data;

import java.util.HashMap;

public class GlobalStats {
    public static ActivityStatusManager activityStatusManager = new ActivityStatusManager();
    public static String appVersion = "unknown";
    public static int createdPageCount = 0;
    public static volatile boolean hasSplash = false;
    public static String installType = "unknown";
    public static boolean isBackground = false;
    public static boolean isDebug = true;
    public static boolean isFirstInstall = false;
    public static boolean isFirstLaunch = false;
    public static long jumpTime = -1;
    public static long lastProcessStartTime = -1;
    public static String lastTopActivity = "";
    public static long lastTouchTime = -1;
    public static String lastValidPage = "background";
    public static long lastValidTime = -1;
    public static long launchStartTime = -1;
    public static String oppoCPUResource = "false";
    public static long processStartTime = -1;

    public static class ActivityStatusManager {
        HashMap<String, Boolean> names = new HashMap<>();

        public boolean isFirst(String str) {
            Boolean bool = this.names.get(str);
            if (bool == null) {
                return true;
            }
            return bool.booleanValue();
        }

        public void setFirst(String str) {
            if (this.names.get(str) == null) {
                this.names.put(str, true);
            } else {
                this.names.put(str, false);
            }
        }
    }
}
