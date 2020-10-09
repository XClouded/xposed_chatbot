package com.alibaba.android.umbrella.trace;

import android.text.TextUtils;
import com.alibaba.android.umbrella.utils.KVConfigItem;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.android.umbrella.utils.converter.StringListConfigConverter;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import java.util.HashMap;
import java.util.List;

public class UmbrellaSimple {
    private static final String BIZ_INFO_INIT_VALUE_TAOBAO_BUY_BUY = "taobao.buy,buy";
    private static final String BOOLEAN_FALSE = "false";
    private static final String BOOLEAN_TRUE = "true";
    public static final double DEFAULT_FAIL_SAMPLE_RATING = 1.0d;
    public static final double DEFAULT_PERFORMANCE_SAMPLE_RATING = 0.001d;
    public static final double DEFAULT_SUCCESS_SAMPLE_RATING = 5.0E-5d;
    public static final String FORCE_CLOSE_FAILURE_KEY = "ForceCloseFailure";
    public static final String FORCE_CLOSE_PERFORMANCE_PAGE_KEY = "ForceClosePerformancePage";
    public static final String FORCE_CLOSE_PERFORMANCE_POINT_KEY = "ForceClosePerformancePoint";
    public static final String FORCE_CLOSE_SUCCESS_KEY = "ForceCloseSuccess";
    public static final String OPEN_CRASH_REPORT_KEY = "isPointReportToCrash";
    public static final String OPEN_GRAY_REPORT_KEY = "isGrayReport";
    public static final String ORANGE_GROUP_NAME = "umbrella_trace";
    private static final String UM_UNIFORM_ERROR_REPORT = "UMUniformErrorReport";
    /* access modifiers changed from: private */
    public static HashMap<String, Double> sFailSampleRating = new HashMap<>();
    /* access modifiers changed from: private */
    public static boolean sForceCloseFailure = false;
    /* access modifiers changed from: private */
    public static boolean sForceClosePerformancePage = false;
    /* access modifiers changed from: private */
    public static boolean sForceClosePerformancePoint = false;
    /* access modifiers changed from: private */
    public static boolean sForceCloseSuccess = false;
    private static boolean sIsGrayVersion = false;
    /* access modifiers changed from: private */
    public static boolean sOpenCrashReport = true;
    /* access modifiers changed from: private */
    public static boolean sOpenGrayReport = true;
    /* access modifiers changed from: private */
    public static HashMap<String, Double> sPerformanceSampleRating = new HashMap<>();
    /* access modifiers changed from: private */
    public static HashMap<String, Double> sSampleRating = new HashMap<>();
    static final KVConfigItem<List<String>> sUMUniformErrorReport = new KVConfigItem<>(ORANGE_GROUP_NAME, UM_UNIFORM_ERROR_REPORT, BIZ_INFO_INIT_VALUE_TAOBAO_BUY_BUY, new StringListConfigConverter());

    private static boolean isGrayVersion() {
        return false;
    }

    static {
        init();
    }

    public static boolean getSampleResult(UmbrellaInfo umbrellaInfo) {
        if (umbrellaInfo == null || TextUtils.isEmpty(umbrellaInfo.mainBizName) || TextUtils.isEmpty(umbrellaInfo.tagId)) {
            return false;
        }
        if (getOrangeSampleRating(umbrellaInfo.mainBizName + '_' + umbrellaInfo.tagId) > Math.random()) {
            return true;
        }
        return false;
    }

    public static boolean getFailSampleResult(UmbrellaInfo umbrellaInfo, String str) {
        if (umbrellaInfo == null || TextUtils.isEmpty(umbrellaInfo.mainBizName) || TextUtils.isEmpty(str)) {
            return false;
        }
        if (getOrangeFailSampleRating(umbrellaInfo.mainBizName + '_' + str) > Math.random()) {
            return true;
        }
        return false;
    }

    public static boolean getPerformanceSampleResult(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        if (getPerformanceOrangeSampleRating(UmbrellaConstants.PERFORMANCE_ORANGE_PRE + str + "_" + str2) > Math.random()) {
            return true;
        }
        return false;
    }

    public static boolean getPerformancePageSampleResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (getPerformanceOrangeSampleRating(UmbrellaConstants.PERFORMANCE_PAGE_ORANGE_PRE + str) > Math.random()) {
            return true;
        }
        return false;
    }

    public static boolean isForceCloseSuccess() {
        return sForceCloseSuccess;
    }

    public static boolean isForceCloseFailure() {
        return sForceCloseFailure;
    }

    public static boolean isOpenCrashReport() {
        return sOpenCrashReport;
    }

    public static boolean isOpenGrayReport() {
        return sOpenGrayReport;
    }

    public static boolean getIsGrayVersion() {
        return sIsGrayVersion;
    }

    public static boolean isForceClosePerformancePoint() {
        return sForceClosePerformancePoint;
    }

    public static boolean isForceClosePerformancePage() {
        return sForceClosePerformancePage;
    }

    private static boolean getForceCloseSuccess() {
        return "true".equals(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, FORCE_CLOSE_SUCCESS_KEY, "false"));
    }

    private static boolean getForceCloseFailure() {
        return "true".equals(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, FORCE_CLOSE_FAILURE_KEY, "false"));
    }

    /* access modifiers changed from: private */
    public static boolean getForceCloseOrange(String str) {
        if (!TextUtils.isEmpty(str) && "true".equals(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, str, "false"))) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static boolean getOpenCrashReport() {
        return "true".equals(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, OPEN_CRASH_REPORT_KEY, "true"));
    }

    /* access modifiers changed from: private */
    public static boolean getOpenGrayReport() {
        return "true".equals(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, OPEN_GRAY_REPORT_KEY, "true"));
    }

    public static double getOrangeSampleRating(String str) {
        if (TextUtils.isEmpty(str)) {
            return 5.0E-5d;
        }
        Double d = sSampleRating.get(str);
        if (d != null) {
            return d.doubleValue();
        }
        try {
            double parseDouble = Double.parseDouble(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, str, String.valueOf(5.0E-5d)));
            sSampleRating.put(str, Double.valueOf(parseDouble));
            return parseDouble;
        } catch (NumberFormatException unused) {
            sSampleRating.put(str, Double.valueOf(5.0E-5d));
            return 5.0E-5d;
        }
    }

    public static double getOrangeFailSampleRating(String str) {
        if (TextUtils.isEmpty(str)) {
            return 1.0d;
        }
        Double d = sFailSampleRating.get(str);
        if (d != null) {
            return d.doubleValue();
        }
        try {
            double parseDouble = Double.parseDouble(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, str, String.valueOf(1.0d)));
            sFailSampleRating.put(str, Double.valueOf(parseDouble));
            return parseDouble;
        } catch (NumberFormatException unused) {
            sFailSampleRating.put(str, Double.valueOf(1.0d));
            return 1.0d;
        }
    }

    public static double getPerformanceOrangeSampleRating(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.001d;
        }
        Double d = sPerformanceSampleRating.get(str);
        if (d != null) {
            return d.doubleValue();
        }
        try {
            double parseDouble = Double.parseDouble(OrangeConfig.getInstance().getConfig(ORANGE_GROUP_NAME, str, String.valueOf(0.001d)));
            sPerformanceSampleRating.put(str, Double.valueOf(parseDouble));
            return parseDouble;
        } catch (NumberFormatException unused) {
            sPerformanceSampleRating.put(str, Double.valueOf(0.001d));
            return 0.001d;
        }
    }

    public static void init() {
        sForceCloseSuccess = getForceCloseOrange(FORCE_CLOSE_SUCCESS_KEY);
        sForceCloseFailure = getForceCloseOrange(FORCE_CLOSE_FAILURE_KEY);
        sOpenCrashReport = getOpenCrashReport();
        sForceClosePerformancePoint = getForceCloseOrange(FORCE_CLOSE_PERFORMANCE_POINT_KEY);
        sForceClosePerformancePage = getForceCloseOrange(FORCE_CLOSE_PERFORMANCE_PAGE_KEY);
        sOpenGrayReport = getOpenGrayReport();
        sIsGrayVersion = isGrayVersion();
        sUMUniformErrorReport.refreshValue();
        OrangeConfig.getInstance().registerListener(new String[]{ORANGE_GROUP_NAME}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
            public void onConfigUpdate(String str, boolean z) {
                if (UmbrellaSimple.ORANGE_GROUP_NAME.equals(str)) {
                    for (String str2 : UmbrellaSimple.sSampleRating.keySet()) {
                        try {
                            UmbrellaSimple.sSampleRating.put(str2, Double.valueOf(Double.parseDouble(OrangeConfig.getInstance().getConfig(UmbrellaSimple.ORANGE_GROUP_NAME, str2, String.valueOf(5.0E-5d)))));
                        } catch (NumberFormatException unused) {
                            UmbrellaSimple.sSampleRating.put(str2, Double.valueOf(5.0E-5d));
                        }
                    }
                    for (String str3 : UmbrellaSimple.sFailSampleRating.keySet()) {
                        try {
                            UmbrellaSimple.sFailSampleRating.put(str3, Double.valueOf(Double.parseDouble(OrangeConfig.getInstance().getConfig(UmbrellaSimple.ORANGE_GROUP_NAME, str3, String.valueOf(1.0d)))));
                        } catch (NumberFormatException unused2) {
                            UmbrellaSimple.sFailSampleRating.put(str3, Double.valueOf(1.0d));
                        }
                    }
                    for (String str4 : UmbrellaSimple.sPerformanceSampleRating.keySet()) {
                        try {
                            UmbrellaSimple.sFailSampleRating.put(str4, Double.valueOf(Double.parseDouble(OrangeConfig.getInstance().getConfig(UmbrellaSimple.ORANGE_GROUP_NAME, str4, String.valueOf(0.001d)))));
                        } catch (NumberFormatException unused3) {
                            UmbrellaSimple.sFailSampleRating.put(str4, Double.valueOf(0.001d));
                        }
                    }
                    boolean unused4 = UmbrellaSimple.sForceCloseSuccess = UmbrellaSimple.getForceCloseOrange(UmbrellaSimple.FORCE_CLOSE_SUCCESS_KEY);
                    boolean unused5 = UmbrellaSimple.sForceCloseFailure = UmbrellaSimple.getForceCloseOrange(UmbrellaSimple.FORCE_CLOSE_FAILURE_KEY);
                    boolean unused6 = UmbrellaSimple.sForceClosePerformancePoint = UmbrellaSimple.getForceCloseOrange(UmbrellaSimple.FORCE_CLOSE_PERFORMANCE_POINT_KEY);
                    boolean unused7 = UmbrellaSimple.sForceClosePerformancePage = UmbrellaSimple.getForceCloseOrange(UmbrellaSimple.FORCE_CLOSE_PERFORMANCE_PAGE_KEY);
                    boolean unused8 = UmbrellaSimple.sOpenCrashReport = UmbrellaSimple.getOpenCrashReport();
                    boolean unused9 = UmbrellaSimple.sOpenGrayReport = UmbrellaSimple.getOpenGrayReport();
                    UmbrellaSimple.sUMUniformErrorReport.refreshValue();
                }
            }
        });
    }
}
