package com.alibaba.android.prefetchx.config;

import androidx.annotation.NonNull;
import java.util.List;
import java.util.Map;

public class RemoteConfigSpec {

    public interface IConfigChangeListener {
        void onConfigChange(String str, boolean z, String str2);
    }

    public interface IDataModuleRemoteConfig {
        int getConfigMapMaxAgeInMemory();

        String getConfigMapUrl();

        int getInitMtopConfigProcessDelay();

        boolean isDataEnable();

        boolean isDataStatueReportEnable();

        boolean isRefreshGeoWhenInit();

        int refreshGeoDelay();

        boolean useRegexWayForMerge();
    }

    public interface IFileModuleRemoteConfig {
        long getDelay();

        @NonNull
        List<String> getIgnoreParamsBlackList();

        int getMaxCacheNum();

        boolean isSwitchOn();
    }

    public interface IImageModuleRemoteConfig {
        int getConfigMapMaxAgeInMemory();

        int getDefaultImageCount();

        int getDefaultImageSizeDenominator();

        Map<String, String> getImageConfig();

        boolean isImageByDefault();

        boolean isImageEnable();

        boolean isLoadToMemory();
    }

    public interface IJSModuleRemoteConfig {
        int cdnComboCount();

        int delayBetweenEachJSDownload();

        int getInitOrangeConfigProcessDelay();

        int getInitOrangeConfigThreadCount();

        Map<String, String> getMappingUrls();

        Map<String, String> getOrangeConfigJSModulePOJO();

        int hostPathMaxSize();

        boolean isJSModuleEnable();

        int jsModuleMaxSize();

        int lowMemoryPercent();

        int maxCacheAge();

        void registerJSModuleListener(IConfigChangeListener iConfigChangeListener);

        int retryDownloadDelay();

        int retryDownloadTimes();

        boolean runOnLowDevices();

        boolean unloadAllJSModuleOnLowMemory();
    }

    public interface IResourceModuleRemoteConfig {
    }
}
