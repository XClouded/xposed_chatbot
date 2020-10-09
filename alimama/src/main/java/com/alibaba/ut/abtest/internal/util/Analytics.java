package com.alibaba.ut.abtest.internal.util;

import android.text.TextUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.alibaba.ut.abtest.UTABVersion;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public final class Analytics {
    private static final String ANALYTICS_MODULE = "Yixiu";
    public static final String CROWD_EFFECTIVE_COUNTER = "CrowdEffectiveCounter";
    public static final String CROWD_INVOKE_COUNTER = "CrowdInvokeCounter";
    public static final String CROWD_STAT = "CrowdStat";
    public static final String DOWNLOAD_ALARM = "DownloadAlarm";
    public static final String EXPERIMENT_ACTIVATE_COUNTER = "ExperimentActivateCounter";
    public static final String EXPERIMENT_ACTIVATE_STAT = "ExperimentActivateStat";
    public static final String EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER = "activateServer";
    public static final String EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER_SYNC = "activateServerSync";
    public static final String EXPERIMENT_ACTIVATE_STAT_TYPE_URL = "url";
    public static final String EXPERIMENT_ACTIVATE_STAT_TYPE_VARIATION = "variation";
    public static final String EXPERIMENT_DATA_REACH_API = "ExperimentDataReachApi";
    public static final String EXPERIMENT_DATA_REACH_FILE = "ExperimentDataReachFile";
    public static final String EXPERIMENT_DATA_REACH_ORANGE = "ExperimentDataReachOrange";
    public static final String EXPERIMENT_EFFECTIVE_COUNTER = "ExperimentEffectiveCounter";
    public static final String MULTI_PROCESS_ALARM = "MultiProcessAlarm";
    public static final String SERVICE_ALARM = "ServiceAlarm";
    private static final String TAG = "Analytics";
    public static final String TRACK_STAT = "TrackStat";
    public static final String TRACK_TYPE_ROUTING_RESULT = "routing_result";
    public static final String TRACK_TYPE_ROUTING_VALUE = "routing_value";

    private Analytics() {
    }

    public static void commitSuccess(String str) {
        AppMonitor.Alarm.commitSuccess(ANALYTICS_MODULE, str);
    }

    public static void commitSuccess(String str, String str2) {
        AppMonitor.Alarm.commitSuccess(ANALYTICS_MODULE, str, str2);
    }

    public static void commitFail(String str, String str2, String str3, String str4) {
        AppMonitor.Alarm.commitFail(ANALYTICS_MODULE, str, str2, str3, str4);
    }

    public static void commitFail(String str, String str2, String str3, String str4, boolean z) {
        if (!z || Utils.isNetworkConnected(ABContext.getInstance().getContext())) {
            AppMonitor.Alarm.commitFail(ANALYTICS_MODULE, str, str2, str3, str4);
        }
    }

    public static void commitFail(String str, String str2, String str3, boolean z) {
        if (!z || Utils.isNetworkConnected(ABContext.getInstance().getContext())) {
            AppMonitor.Alarm.commitFail(ANALYTICS_MODULE, str, str2, str3);
        }
    }

    public static void commitCounter(String str, String str2) {
        AppMonitor.Counter.commit(ANALYTICS_MODULE, str, str2, 1.0d);
    }

    public static void registerExperimentActivateStat() {
        DimensionSet addDimension = DimensionSet.create().addDimension("type").addDimension("result");
        Measure measure = new Measure("time");
        measure.setRange(Double.valueOf(0.0d), Double.valueOf(120000.0d));
        register(EXPERIMENT_ACTIVATE_STAT, MeasureSet.create().addMeasure(measure), addDimension);
    }

    public static void commitExperimentActivateStat(String str, boolean z, long j) {
        if (!TextUtils.isEmpty(str)) {
            commitStat(EXPERIMENT_ACTIVATE_STAT, DimensionValueSet.create().setValue("type", str).setValue("result", String.valueOf(z)), MeasureValueSet.create().setValue("time", (double) j));
        }
    }

    public static void registerCrowdStat() {
        DimensionSet addDimension = DimensionSet.create().addDimension("crowd");
        Measure measure = new Measure("time");
        measure.setRange(Double.valueOf(0.0d), Double.valueOf(120000.0d));
        register(CROWD_STAT, MeasureSet.create().addMeasure(measure), addDimension);
    }

    public static void commitCrowdStat(String str, long j) {
        if (str != null) {
            commitStat(CROWD_STAT, DimensionValueSet.create().setValue("crowd", str), MeasureValueSet.create().setValue("time", (double) j));
        }
    }

    public static void registerTrackStat() {
        DimensionSet addDimension = DimensionSet.create().addDimension("result");
        Measure measure = new Measure("time");
        measure.setRange(Double.valueOf(0.0d), Double.valueOf(120000.0d));
        register(TRACK_STAT, MeasureSet.create().addMeasure(measure), addDimension);
    }

    public static void commitTrackStat(boolean z, long j) {
        commitStat(TRACK_STAT, DimensionValueSet.create().setValue("result", String.valueOf(z)), MeasureValueSet.create().setValue("time", (double) j));
    }

    public static void register(String str, MeasureSet measureSet, DimensionSet dimensionSet) {
        AppMonitor.register(ANALYTICS_MODULE, str, measureSet, dimensionSet);
    }

    public static void commitStat(String str, DimensionValueSet dimensionValueSet, MeasureValueSet measureValueSet) {
        AppMonitor.Stat.commit(ANALYTICS_MODULE, str, dimensionValueSet, measureValueSet);
    }

    public static void track(String str, Map<String, String> map) {
        try {
            UTHitBuilders.UTHitBuilder uTHitBuilder = new UTHitBuilders.UTHitBuilder();
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "19999");
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG1, ABConstants.BasicConstants.URI_PARAMNAME_ABTEST);
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, UTABVersion.VERSION_NAME);
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG3, str);
            if (map != null) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry next : map.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append((String) next.getKey());
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append((String) next.getValue());
                }
                uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARGS, sb.toString());
            }
            UTAnalytics.getInstance().getDefaultTracker().send(uTHitBuilder.build());
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage());
        }
    }

    public static void trackAuge(String str, boolean z) {
        try {
            UTHitBuilders.UTHitBuilder uTHitBuilder = new UTHitBuilders.UTHitBuilder();
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "19999");
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_PAGE, ANALYTICS_MODULE);
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG1, "Auge");
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, str);
            uTHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG3, String.valueOf(z));
            UTAnalytics.getInstance().getDefaultTracker().send(uTHitBuilder.build());
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage());
        }
    }
}
