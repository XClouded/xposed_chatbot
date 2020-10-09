package com.alibaba.android.umbrella.trace;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.List;
import java.util.Map;

class UMUniformErrorReporter {
    private static final String[] DEFAULT_DIMENSION_SET = {"pageName", K_PAGE_URL, "title", K_SUBTITLE, "code", K_MAPPING_CODE, K_RESPONSE_CODE, "apiName", K_ERROR_TYPE, "errorMsg", K_TRACE_ID, K_BIZ_INFO};
    private static final String[] DEFAULT_MEASURE_SET = {"value"};
    private static final String K_API_NAME = "apiName";
    private static final String K_BIZ_INFO = "bizInfo";
    private static final String K_CODE = "code";
    private static final String K_ERROR_MSG = "errorMsg";
    private static final String K_ERROR_TYPE = "errorType";
    private static final String K_MAPPING_CODE = "mappingCode";
    private static final String K_PAGE_NAME = "pageName";
    private static final String K_PAGE_URL = "pageURL";
    private static final String K_RESPONSE_CODE = "responseCode";
    private static final String K_SUBTITLE = "subtitle";
    private static final String K_TITLE = "title";
    private static final String K_TRACE_ID = "traceId";
    private static final String MODULE_NAME = "TBErrorView";
    private static final String MONITOR_POINT = "show_error";
    private static final String TAG = "UMUniformError";
    private static boolean isMonitorRegistered = false;

    UMUniformErrorReporter() {
    }

    /* access modifiers changed from: package-private */
    public void commit(@NonNull UmbrellaInfo umbrellaInfo, @NonNull String str, @NonNull String str2) {
        List value = UmbrellaSimple.sUMUniformErrorReport.getValue();
        if (value == null || value.isEmpty() || !value.contains(umbrellaInfo.mainBizName)) {
            Log.w(TAG, "sUMUniformErrorReport == false, do not report uniform error");
            return;
        }
        if (!isMonitorRegistered) {
            isMonitorRegistered = true;
            DimensionSet create = DimensionSet.create();
            for (String addDimension : DEFAULT_DIMENSION_SET) {
                create.addDimension(addDimension);
            }
            MeasureSet create2 = MeasureSet.create();
            for (String addMeasure : DEFAULT_MEASURE_SET) {
                create2.addMeasure(addMeasure);
            }
            AppMonitor.register(MODULE_NAME, MONITOR_POINT, create2, create);
        }
        DimensionValueSet create3 = DimensionValueSet.create();
        setDVSValue(create3, "pageName", umbrellaInfo.args, umbrellaInfo.invokePage);
        setDVSValue(create3, K_PAGE_URL, umbrellaInfo.args, umbrellaInfo.invokePageUrl);
        setDVSValue(create3, "title", umbrellaInfo.args, new String[0]);
        setDVSValue(create3, K_SUBTITLE, umbrellaInfo.args, new String[0]);
        setDVSValue(create3, "code", umbrellaInfo.args, str);
        setDVSValue(create3, K_MAPPING_CODE, umbrellaInfo.args, new String[0]);
        setDVSValue(create3, K_RESPONSE_CODE, umbrellaInfo.args, new String[0]);
        setDVSValue(create3, "apiName", umbrellaInfo.args, new String[0]);
        setDVSValue(create3, K_ERROR_TYPE, umbrellaInfo.args, new String[0]);
        setDVSValue(create3, "errorMsg", umbrellaInfo.args, str2);
        setDVSValue(create3, K_TRACE_ID, umbrellaInfo.args, new String[0]);
        setDVSValue(create3, K_BIZ_INFO, umbrellaInfo.args, umbrellaInfo.mainBizName);
        MeasureValueSet create4 = MeasureValueSet.create();
        create4.setValue("value", 0.0d);
        AppMonitor.Stat.commit(MODULE_NAME, MONITOR_POINT, create3, create4);
    }

    private static void setDVSValue(@NonNull DimensionValueSet dimensionValueSet, @NonNull String str, @Nullable Map<String, String> map, @Nullable String... strArr) {
        String str2 = "Unknown" + str;
        if (strArr != null && strArr.length > 0) {
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str3 = strArr[i];
                if (!TextUtils.isEmpty(str3)) {
                    str2 = str3;
                    break;
                }
                i++;
            }
        }
        String fromNullableMap = getFromNullableMap(map, str);
        if (TextUtils.isEmpty(fromNullableMap)) {
            fromNullableMap = str2;
        }
        dimensionValueSet.setValue(str, fromNullableMap);
    }

    @Nullable
    private static String getFromNullableMap(@Nullable Map<String, String> map, @NonNull String str) {
        if (map == null) {
            return null;
        }
        String str2 = map.get(str);
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        return str2;
    }
}
