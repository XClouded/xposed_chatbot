package com.alibaba.android.prefetchx.config;

import android.os.SystemClock;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.config.RemoteConfigSpec;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OrangeRemoteConfigImpl {
    static final String GROUP = "prefetchx_config";
    private static int HOUR = 3600;
    /* access modifiers changed from: private */
    public static int ONE_K = 1024;

    private OrangeRemoteConfigImpl() {
    }

    public static RemoteConfigSpec.IFileModuleRemoteConfig newFileModuleConfig() {
        return new FileModuleRemoteConf();
    }

    public static RemoteConfigSpec.IDataModuleRemoteConfig newDataModuleConfig() {
        return new DataModuleRemoteConf();
    }

    public static RemoteConfigSpec.IJSModuleRemoteConfig newJSModuleConfig() {
        return new JSModuleRemoteConf();
    }

    public static RemoteConfigSpec.IResourceModuleRemoteConfig newResourceModuleConfig() {
        return new ResourceModuleRemoteConf();
    }

    public static RemoteConfigSpec.IImageModuleRemoteConfig newImageModuleConfig() {
        return new ImageModuleRemoteConf();
    }

    /* access modifiers changed from: private */
    public static String readConfigFromOrange(String str, String str2, String str3) {
        try {
            return OrangeConfig.getInstance().getConfig(str, str2, str3);
        } catch (Throwable th) {
            String str4 = "error occurred when getting config of [group:" + str + ", key:" + str2 + "], using default value:" + str3 + ". " + "message is " + th.getMessage();
            PFLog.w("PrefetchX", str4, new Throwable[0]);
            PFMonitor.Data.fail(PFConstant.PF_ORANGE_CONFIG_ERROR, str4, new Object[0]);
            return str3;
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public static Double readConfigFromOrangeNumber(String str, String str2, @NonNull double d) {
        String readConfigFromOrange = readConfigFromOrange(str, str2, String.valueOf(d));
        if (!TextUtils.isEmpty(readConfigFromOrange)) {
            try {
                return Double.valueOf(Double.parseDouble(readConfigFromOrange.trim()));
            } catch (Throwable unused) {
            }
        }
        return Double.valueOf(d);
    }

    /* access modifiers changed from: private */
    public static boolean readConfigFromOrangeBoolean(String str, String str2, boolean z) {
        String readConfigFromOrange = readConfigFromOrange(str, str2, String.valueOf(z));
        if (readConfigFromOrange != null && !TextUtils.isEmpty(readConfigFromOrange)) {
            try {
                return "true".equalsIgnoreCase(readConfigFromOrange.trim()) || DAttrConstant.VIEW_EVENT_FLAG.equalsIgnoreCase(readConfigFromOrange.trim());
            } catch (Throwable unused) {
            }
        }
        return z;
    }

    static class FileModuleRemoteConf implements RemoteConfigSpec.IFileModuleRemoteConfig {
        static final long DEFAULT_DELAY_MILLIS = 1500;
        static final int DEFAULT_MAX_CACHE_NUM = 5;
        private static final String KEY_DELAY_MILLIS = "file_delay_time";
        private static final String KEY_IGNORE_PARAMS_BLACK_LIST = "file_ignore_params_black_list";
        private static final String KEY_MAX_CACHE_NUM = "file_max_cache_num";
        static final String KEY_SWITCH_STATUS = "file_enable";

        FileModuleRemoteConf() {
        }

        public boolean isSwitchOn() {
            boolean access$000 = OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, KEY_SWITCH_STATUS, true);
            if (!access$000) {
                PFLog.Data.w("file is disabled by orange config.", new Throwable[0]);
            }
            return access$000;
        }

        public int getMaxCacheNum() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, KEY_MAX_CACHE_NUM, 5.0d).intValue();
        }

        public long getDelay() {
            return (long) OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, KEY_DELAY_MILLIS, 1500.0d).intValue();
        }

        @NonNull
        public List<String> getIgnoreParamsBlackList() {
            String access$200 = OrangeRemoteConfigImpl.readConfigFromOrange(OrangeRemoteConfigImpl.GROUP, KEY_IGNORE_PARAMS_BLACK_LIST, (String) null);
            if (TextUtils.isEmpty(access$200)) {
                return Collections.emptyList();
            }
            try {
                if (access$200.startsWith(Operators.ARRAY_START_STR) && access$200.endsWith(Operators.ARRAY_END_STR) && access$200.length() > 2) {
                    return Arrays.asList(access$200.substring(1, access$200.length() - 1).split(","));
                }
            } catch (Exception unused) {
            }
            return Collections.emptyList();
        }
    }

    static class DataModuleRemoteConf implements RemoteConfigSpec.IDataModuleRemoteConfig {
        private static String DATA_ORAGNE_KEY_ENABLE = "data_enable";
        private static String DATA_ORAGNE_KEY_INIT_GEO = "data_init_geo";
        private static String DATA_ORAGNE_KEY_INIT_MTOP_CONFIG_DELAY = "data_init_mtop_config_delay";
        private static String DATA_ORAGNE_KEY_JSON_MAPPING_MAXAGE = "data_json_mapping_maxage";
        private static String DATA_ORAGNE_KEY_JSON_MAPPING_URL = "data_json_mapping_url";
        private static String DATA_ORAGNE_KEY_REFRESH_GEO_DELAY = "data_refresh_geo_delay";
        private static String DATA_ORAGNE_KEY_REGEX_WAY_FOR_MERGE = "data_regex_way_for_merge";
        private static String DATA_ORAGNE_KEY_STATUS_REPORT = "data_status_report";
        private volatile boolean isAllowMtopPrefetchStatus = true;
        private volatile long lastRefreshTimeOfIsAllowMtopPrefetchStatus = 0;
        private volatile long lastRefreshTimeOfWhiteUrlList = 0;
        private volatile List<String> whiteUrlList = new ArrayList();

        DataModuleRemoteConf() {
        }

        public boolean isDataEnable() {
            boolean access$000 = OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_ENABLE, true);
            if (!access$000) {
                PFLog.Data.w("data is disabled by orange config.", new Throwable[0]);
            }
            return access$000;
        }

        public boolean isDataStatueReportEnable() {
            if (SystemClock.uptimeMillis() - this.lastRefreshTimeOfIsAllowMtopPrefetchStatus < 60000) {
                return this.isAllowMtopPrefetchStatus;
            }
            boolean access$000 = OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_STATUS_REPORT, false);
            this.lastRefreshTimeOfIsAllowMtopPrefetchStatus = SystemClock.uptimeMillis();
            this.isAllowMtopPrefetchStatus = access$000;
            return access$000;
        }

        public boolean isRefreshGeoWhenInit() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_INIT_GEO, false);
        }

        public int refreshGeoDelay() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_REFRESH_GEO_DELAY, 3600.0d).intValue();
        }

        public String getConfigMapUrl() {
            String access$200 = OrangeRemoteConfigImpl.readConfigFromOrange(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_JSON_MAPPING_URL, PFConstant.Data.PF_DATA_DEFAULT_MAP_URL);
            return TextUtils.isEmpty(access$200) ? PFConstant.Data.PF_DATA_DEFAULT_MAP_URL : access$200;
        }

        public int getConfigMapMaxAgeInMemory() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_JSON_MAPPING_MAXAGE, 300.0d).intValue();
        }

        public int getInitMtopConfigProcessDelay() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_INIT_MTOP_CONFIG_DELAY, 3.0d).intValue() * 1000;
        }

        public boolean useRegexWayForMerge() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, DATA_ORAGNE_KEY_REGEX_WAY_FOR_MERGE, false);
        }
    }

    static class JSModuleRemoteConf implements RemoteConfigSpec.IJSModuleRemoteConfig {
        static final String GROUP_JSMODULE = "prefetchx_jsmodule_content";
        static final String GROUP_MAPPING = "prefetchx_jsmodule_mapping";
        private static final String JSMODULE_ORAGNE_CDN_COMBO_COUNT = "jsmodule_cdn_combo_count";
        private static final String JSMODULE_ORAGNE_DELAY_BETWEEN_EACH_JS = "jsmodule_delay_between_each_js";
        private static final String JSMODULE_ORAGNE_KEY_ENABLE = "jsmodule_enable";
        private static final String JSMODULE_ORAGNE_KEY_INIT_ORANGECONFIGPROCESS_DELAY = "jsmodule_init_config_process_delay";
        private static final String JSMODULE_ORAGNE_KEY_INIT_ORANGECONFIGPROCESS_THREAD = "jsmodule_init_config_process_thread";
        private static final String JSMODULE_ORAGNE_KEY_LOW_MEMORY_PERCENT = "jsmodule_unload_low_memory_percent";
        private static final String JSMODULE_ORAGNE_KEY_MAX_CACHE_AGE = "jsmodule_max_cache_age";
        private static final String JSMODULE_ORAGNE_KEY_MAX_HOSTPATH = "jsmodule_max_hostpath";
        private static final String JSMODULE_ORAGNE_KEY_MAX_JSMODULE = "jsmodule_max_js";
        private static final String JSMODULE_ORAGNE_KEY_RETRY_DOWNLOAD_DELAY = "jsmodule_retry_download_delay";
        private static final String JSMODULE_ORAGNE_KEY_RETRY_DOWNLOAD_TIMES = "jsmodule_retry_download_times";
        private static final String JSMODULE_ORAGNE_KEY_RUN_ON_LOW_DEVICES = "jsmodule_run_on_low_devices";
        private static final String JSMODULE_ORAGNE_KEY_UNLOAD_ON_LOW_MEMORY = "jsmodule_unload_on_low_memory";
        /* access modifiers changed from: private */
        public String lastVersion = "";

        JSModuleRemoteConf() {
        }

        public boolean isJSModuleEnable() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_ENABLE, false);
        }

        public int jsModuleMaxSize() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_MAX_JSMODULE, 1000.0d).intValue() * OrangeRemoteConfigImpl.ONE_K;
        }

        public int hostPathMaxSize() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_MAX_HOSTPATH, 3000.0d).intValue() * OrangeRemoteConfigImpl.ONE_K;
        }

        public int maxCacheAge() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_MAX_CACHE_AGE, 604800.0d).intValue();
        }

        public Map<String, String> getOrangeConfigJSModulePOJO() {
            return OrangeConfig.getInstance().getConfigs(GROUP_JSMODULE);
        }

        public int retryDownloadTimes() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_RETRY_DOWNLOAD_TIMES, 2.0d).intValue();
        }

        public int retryDownloadDelay() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_RETRY_DOWNLOAD_DELAY, 15.0d).intValue() * 1000;
        }

        public int getInitOrangeConfigProcessDelay() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_INIT_ORANGECONFIGPROCESS_DELAY, 15.0d).intValue() * 1000;
        }

        public int getInitOrangeConfigThreadCount() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_INIT_ORANGECONFIGPROCESS_THREAD, 1.0d).intValue();
        }

        public void registerJSModuleListener(final RemoteConfigSpec.IConfigChangeListener iConfigChangeListener) {
            OrangeConfig.getInstance().registerListener(new String[]{GROUP_JSMODULE}, new OConfigListener() {
                public void onConfigUpdate(String str, Map<String, String> map) {
                    if (JSModuleRemoteConf.GROUP_JSMODULE.equals(str)) {
                        boolean equals = "true".equals(map.get("fromCache"));
                        String access$200 = OrangeRemoteConfigImpl.readConfigFromOrange(JSModuleRemoteConf.GROUP_JSMODULE, "configV2", "");
                        String access$2002 = OrangeRemoteConfigImpl.readConfigFromOrange(JSModuleRemoteConf.GROUP_JSMODULE, "version", "");
                        synchronized (this) {
                            if (!TextUtils.equals(JSModuleRemoteConf.this.lastVersion, access$2002)) {
                                iConfigChangeListener.onConfigChange(access$200, equals, access$2002);
                                String unused = JSModuleRemoteConf.this.lastVersion = access$2002;
                            }
                        }
                    }
                }
            }, false);
        }

        public boolean runOnLowDevices() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_RUN_ON_LOW_DEVICES, true);
        }

        public boolean unloadAllJSModuleOnLowMemory() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_UNLOAD_ON_LOW_MEMORY, false);
        }

        public int lowMemoryPercent() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_KEY_LOW_MEMORY_PERCENT, 20.0d).intValue();
        }

        public Map<String, String> getMappingUrls() {
            return OrangeConfig.getInstance().getConfigs(GROUP_MAPPING);
        }

        public int delayBetweenEachJSDownload() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_DELAY_BETWEEN_EACH_JS, 200.0d).intValue();
        }

        public int cdnComboCount() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, JSMODULE_ORAGNE_CDN_COMBO_COUNT, 5.0d).intValue();
        }
    }

    static class ResourceModuleRemoteConf implements RemoteConfigSpec.IResourceModuleRemoteConfig {
        ResourceModuleRemoteConf() {
        }
    }

    public static class ImageModuleRemoteConf implements RemoteConfigSpec.IImageModuleRemoteConfig {
        static final String GROUP_IMAGE_MAPPING = "prefetchx_image_mapping";
        private static final String IMAGE_ORAGNE_KEY_DEFAULT = "image_default_on";
        private static final String IMAGE_ORAGNE_KEY_DEFAULT_COUNT = "image_default_count";
        private static final String IMAGE_ORAGNE_KEY_DEFAULT_DENOMINATOR = "image_default_denominator";
        private static final String IMAGE_ORAGNE_KEY_ENABLE = "image_enable";
        private static final String IMAGE_ORAGNE_KEY_JSON_MAPPING_MAXAGE = "image_config_mapping_maxage";
        private static final String IMAGE_ORAGNE_KEY_LOAD_TO_MEMORY = "image_load_to_memory";

        public boolean isImageEnable() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_ENABLE, true);
        }

        public boolean isImageByDefault() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_DEFAULT, false);
        }

        public int getDefaultImageCount() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_DEFAULT_COUNT, 6.0d).intValue();
        }

        public int getDefaultImageSizeDenominator() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_DEFAULT_DENOMINATOR, 2.0d).intValue();
        }

        public Map<String, String> getImageConfig() {
            return OrangeConfig.getInstance().getConfigs(GROUP_IMAGE_MAPPING);
        }

        public int getConfigMapMaxAgeInMemory() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeNumber(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_JSON_MAPPING_MAXAGE, 300.0d).intValue();
        }

        public boolean isLoadToMemory() {
            return OrangeRemoteConfigImpl.readConfigFromOrangeBoolean(OrangeRemoteConfigImpl.GROUP, IMAGE_ORAGNE_KEY_LOAD_TO_MEMORY, true);
        }
    }
}
