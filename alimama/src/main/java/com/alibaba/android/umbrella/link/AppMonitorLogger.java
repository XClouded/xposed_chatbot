package com.alibaba.android.umbrella.link;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMDimKey;
import com.alibaba.android.umbrella.link.util.UMLinkLogUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.taobao.weex.BuildConfig;
import java.util.HashMap;
import java.util.Map;

@Deprecated
class AppMonitorLogger {
    private static final String CHILD_BIZ_NAME = "umb2";
    private static final String[] DEFAULT_MEASURE_SET = {"value"};
    private static final String ERROR_CODE = "umb8";
    private static final String ERROR_MSG = "umb9";
    private static final String EXT_DATA = "umb20";
    private static final String FEATURE_TYPE = "umb7";
    private static final String LAUNCH_ID = "umb3";
    private static final String LOG_LEVEL = "umb10";
    private static final String LOG_STAGE = "umb11";
    private static final String MAIN_BIZ_NAME = "umb1";
    private static final String MODULE_PAGE_UMBRELLA_GOVERN = "Page_Umbrella_Govern";
    private static final String PAGE_NAME = "umb5";
    private static final String POINT_MONITOR_UMBRELLA_LINK = "Monitor_Umbrella_Link";
    private static final String RESERVED_13 = "umb13";
    private static final String RESERVED_14 = "umb14";
    private static final String RESERVED_15 = "umb15";
    private static final String RESERVED_16 = "umb16";
    private static final String RESERVED_17 = "umb17";
    private static final String RESERVED_18 = "umb18";
    private static final String RESERVED_19 = "umb19";
    private static final String THREAD_ID = "umb6";
    private static final String TIMESTAMP_12 = "umb12";
    private static final String U_LINK_ID = "umb4";
    private static boolean hasRegistered = false;
    private static final String[] sDimensionSet = {MAIN_BIZ_NAME, CHILD_BIZ_NAME, LAUNCH_ID, U_LINK_ID, PAGE_NAME, THREAD_ID, FEATURE_TYPE, ERROR_CODE, ERROR_MSG, LOG_LEVEL, LOG_STAGE, TIMESTAMP_12, RESERVED_13, RESERVED_14, RESERVED_15, RESERVED_16, RESERVED_17, RESERVED_18, RESERVED_19, EXT_DATA};

    AppMonitorLogger() {
    }

    static void logAppMonitor(LinkLogEntity linkLogEntity) {
        if (!hasRegistered) {
            hasRegistered = true;
            DimensionSet create = DimensionSet.create(sDimensionSet);
            for (UMDimKey umbName : UMDimKey.values()) {
                create.addDimension(umbName.getUmbName());
            }
            AppMonitor.register("Page_Umbrella_Govern", POINT_MONITOR_UMBRELLA_LINK, MeasureSet.create(DEFAULT_MEASURE_SET), create);
        }
        AppMonitor.Stat.commit("Page_Umbrella_Govern", POINT_MONITOR_UMBRELLA_LINK, DimensionValueSet.fromStringMap(toMap(linkLogEntity)), 0.0d);
    }

    private static Map<String, String> toMap(@NonNull LinkLogEntity linkLogEntity) {
        HashMap hashMap = new HashMap();
        putNonNullValue(hashMap, MAIN_BIZ_NAME, linkLogEntity.getMainBizName());
        putNonNullValue(hashMap, CHILD_BIZ_NAME, linkLogEntity.getChildBizName());
        putNonNullValue(hashMap, LAUNCH_ID, linkLogEntity.getLaunchId());
        putNonNullValue(hashMap, U_LINK_ID, linkLogEntity.getLinkId());
        putNonNullValue(hashMap, PAGE_NAME, linkLogEntity.getPageName());
        putNonNullValue(hashMap, THREAD_ID, linkLogEntity.getThreadId());
        putNonNullValue(hashMap, FEATURE_TYPE, linkLogEntity.getFeatureType());
        putNonNullValue(hashMap, ERROR_CODE, linkLogEntity.getErrorCode());
        putNonNullValue(hashMap, ERROR_MSG, linkLogEntity.getErrorMsg());
        putNonNullValue(hashMap, LOG_LEVEL, Integer.valueOf(linkLogEntity.getLogLevel()));
        putNonNullValue(hashMap, LOG_STAGE, Integer.valueOf(linkLogEntity.getLogStage()));
        putNonNullValue(hashMap, TIMESTAMP_12, linkLogEntity.getTimestamp());
        Map<UMDimKey, Object> dimMap = linkLogEntity.getDimMap();
        if (dimMap != null && !dimMap.isEmpty()) {
            for (Map.Entry next : dimMap.entrySet()) {
                putNonNullValue(hashMap, ((UMDimKey) next.getKey()).getUmbName(), next.getValue());
            }
        }
        LinkLogExtData extData = linkLogEntity.getExtData();
        if (extData != null) {
            putNonNullValue(hashMap, EXT_DATA, extData.toClipExtMap());
        }
        return hashMap;
    }

    private static void putNonNullValue(@NonNull Map<String, String> map, @Nullable String str, @Nullable Object obj) {
        if (!UMStringUtils.isEmpty(str)) {
            if (obj == null || BuildConfig.buildJavascriptFrameworkVersion.equals(obj)) {
                map.put(str, "");
            } else {
                map.put(str, UMLinkLogUtils.toUnifiedString(obj));
            }
        }
    }
}
