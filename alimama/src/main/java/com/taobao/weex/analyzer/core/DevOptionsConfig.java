package com.taobao.weex.analyzer.core;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.List;

public class DevOptionsConfig {
    private static final String CONFIG_CPU_CHART = "config_cpu_chart";
    private static final String CONFIG_EXCEPTION_NOTIFICATION = "config_exception_notification";
    private static final String CONFIG_FPS_CHART = "config_fps_chart";
    private static final String CONFIG_INSPECTOR_VIEW_SIZE = "config_inspector_view_size";
    private static final String CONFIG_JS_EXCEPTION = "config_js_exception";
    private static final String CONFIG_LOG_FILTER = "config_log_filter";
    private static final String CONFIG_LOG_LEVEL = "config_log_level";
    private static final String CONFIG_LOG_OUTPUT = "config_log_output";
    private static final String CONFIG_LOG_VIEW_SIZE = "config_log_view_size";
    private static final String CONFIG_MEMORY_CHART = "config_mem_chart";
    private static final String CONFIG_NETWORK_INSPECTOR = "config_network_inspector";
    private static final String CONFIG_PERF_COMMON = "config_perf_common";
    private static final String CONFIG_TRAFFIC_CHART = "config_traffic_chart";
    private static final String CONFIG_UPLOAD_LOG = "config_weex_upload_log";
    private static final String CONFIG_VDOM_DEPTH = "config_vdom_depth";
    private static final String CONFIG_VIEW_INSPECTOR = "config_view_inspector";
    private static final String CONFIG_WEEX_PERFORMANCE_V2 = "config_weex_performance_v2";
    private static final String DEV_CONFIG_NAME = "weex_dev_config";
    public static final String TAG = "weex-analyzer";
    public static final List<String> WHITE_SCALPEL_VIEW_NAMES = Arrays.asList(new String[]{"WXRecyclerView", "WXScrollView", "WXFrameLayout"});
    private static DevOptionsConfig sConfig;
    private SharedPreferences mSharedPreferences;

    private DevOptionsConfig(@NonNull Context context) {
        this.mSharedPreferences = context.getSharedPreferences(DEV_CONFIG_NAME, 0);
    }

    public static DevOptionsConfig getInstance(@NonNull Context context) {
        if (sConfig == null) {
            synchronized (DevOptionsConfig.class) {
                if (sConfig == null) {
                    sConfig = new DevOptionsConfig(context);
                }
            }
        }
        return sConfig;
    }

    public void setPerfCommonEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_PERF_COMMON, z).apply();
    }

    public boolean isPerfCommonEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_PERF_COMMON, false);
    }

    public void setVdomDepthEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_VDOM_DEPTH, z).apply();
    }

    public boolean isVDomDepthEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_VDOM_DEPTH, false);
    }

    public void setWeexPerformanceV2Enabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_WEEX_PERFORMANCE_V2, z).apply();
    }

    public boolean isWeexPerformanceV2Enabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_WEEX_PERFORMANCE_V2, false);
    }

    public void setViewInspectorEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_VIEW_INSPECTOR, z).apply();
    }

    public boolean isViewInspectorEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_VIEW_INSPECTOR, false);
    }

    public void setUploadLogViewEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_UPLOAD_LOG, z).apply();
    }

    public boolean isUploadLogViewEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_UPLOAD_LOG, false);
    }

    public boolean isNetworkInspectorEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_NETWORK_INSPECTOR, false);
    }

    public void setNetworkInspectorEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_NETWORK_INSPECTOR, z).apply();
    }

    public void setLogOutputEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_LOG_OUTPUT, z).apply();
    }

    public boolean isLogOutputEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_LOG_OUTPUT, false);
    }

    public void setMemoryChartEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_MEMORY_CHART, z).apply();
    }

    public boolean isMemoryChartEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_MEMORY_CHART, false);
    }

    public void setCpuChartEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_CPU_CHART, z).apply();
    }

    public boolean isCPUChartEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_CPU_CHART, false);
    }

    public void setTrafficChartEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_TRAFFIC_CHART, z).apply();
    }

    public boolean isTrafficChartEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_TRAFFIC_CHART, false);
    }

    public void setFpsChartEnabled(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_FPS_CHART, z).apply();
    }

    public boolean isFpsChartEnabled() {
        return this.mSharedPreferences.getBoolean(CONFIG_FPS_CHART, false);
    }

    public void setShownJSException(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_JS_EXCEPTION, z).apply();
    }

    public boolean isShownJSException() {
        return this.mSharedPreferences.getBoolean(CONFIG_JS_EXCEPTION, true);
    }

    public void setAllowExceptionNotification(boolean z) {
        this.mSharedPreferences.edit().putBoolean(CONFIG_EXCEPTION_NOTIFICATION, z).apply();
    }

    public boolean isAllowExceptionNotification() {
        return this.mSharedPreferences.getBoolean(CONFIG_EXCEPTION_NOTIFICATION, true);
    }

    public void setLogLevel(int i) {
        this.mSharedPreferences.edit().putInt(CONFIG_LOG_LEVEL, i).apply();
    }

    public int getLogLevel() {
        return this.mSharedPreferences.getInt(CONFIG_LOG_LEVEL, 2);
    }

    public void setLogFilter(String str) {
        this.mSharedPreferences.edit().putString(CONFIG_LOG_FILTER, str).apply();
    }

    public String getLogFilter() {
        return this.mSharedPreferences.getString(CONFIG_LOG_FILTER, (String) null);
    }

    public void setLogViewSize(int i) {
        this.mSharedPreferences.edit().putInt(CONFIG_LOG_VIEW_SIZE, i).apply();
    }

    public int getLogViewSize() {
        return this.mSharedPreferences.getInt(CONFIG_LOG_VIEW_SIZE, 1);
    }

    public void setNetworkInspectorViewSize(int i) {
        this.mSharedPreferences.edit().putInt(CONFIG_INSPECTOR_VIEW_SIZE, i).apply();
    }

    public int getNetworkInspectorViewSize() {
        return this.mSharedPreferences.getInt(CONFIG_INSPECTOR_VIEW_SIZE, 1);
    }
}
