package com.ali.telescope.base.plugin;

public class PluginIDContant {
    public static final String KEY_ALARMMANAGERPLUGIN = "AlarmManagerPlugin";
    public static final String KEY_APP_EVENT_DETECT_PLUGIN = "AppEventDetectPlugin";
    public static final String KEY_APP_PERFORMANCE = "KEY_APP_PERFORMANCE";
    public static final String KEY_BITMAP_HOLDER_PLUGIN = "BitmapHolderPlugin";
    public static final String KEY_CPUPLUGIN = "CpuPlugin";
    public static final String KEY_CRASH_REPORT_PLUGIN = "CrashReportPlugin";
    public static final String KEY_FDOVERFLOWPLUGIN = "FdOverflowPlugin";
    public static final String KEY_FPS_PLUGIN = "FpsPlugin";
    public static final String KEY_MAINTHREADBLOCKPLUGIN = "MainThreadBlockPlugin";
    public static final String KEY_MAINTHREADIOPLUGIN = "MainThreadIoPlugin";
    public static final String KEY_MEMBITMAPPLUGIN = "MemBitmapPlugin";
    public static final String KEY_MEMLEAKPLUGIN = "MemoryLeakPlugin";
    public static final String KEY_MEMORYPLUGIN = "MemoryPlugin";
    public static final String KEY_OVER_DRAW_PLUGIN = "OverDrawPlugin";
    public static final String KEY_OVER_LAYOUT_PLUGIN = "OverLayoutPlugin";
    public static final String KEY_PAGE_LOAD_PLUGIN = "PageLoadPlugin";
    public static final String KEY_PAGE_PERFORMANCE = "KEY_PAGE_PERFORMANCE";
    public static final String KEY_RESOURCELEAKPLUGIN = "ResourceLeakPlugin";
    public static final String KEY_SMOOTHPREF = "SmoothPlugin";
    public static final String KEY_STARTPREF = "StartPrefPlugin";
    public static final String KEY_SYSTEMCOMPONENTPLUGIN = "SystemComponentPlugin";
    public static final String KEY_SYSTEM_PERFORMANCE = "KEY_SYSTEM_PERFORMANCE";
    public static final String KEY_THREADPLUGIN = "ThreadPlugin";
    public static final String KEY_TOO_LARGE_BITMAP_PLUGIN = "TooLargeBitmapPlugin";
    public static final String KEY_TRAFFIC_PLUGIN = "TrafficPlugin";
    public static final String KEY_UPLOADPLUGIN = "UploadPlugin";

    public static boolean isOuterPlugin(String str) {
        return KEY_CRASH_REPORT_PLUGIN.equals(str);
    }
}
