package com.alibaba.ut.abtest;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.ut.abtest.UTABConfiguration;
import com.alibaba.ut.abtest.bucketing.decision.DataUpdateService;
import com.alibaba.ut.abtest.event.Event;
import com.alibaba.ut.abtest.event.EventType;
import com.alibaba.ut.abtest.event.LoginUser;
import com.alibaba.ut.abtest.event.internal.ExperimentDataEventListener;
import com.alibaba.ut.abtest.event.internal.UserEventListener;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.DefaultVariationSet;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup;
import com.alibaba.ut.abtest.internal.database.ABDatabase;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.PreconditionUtils;
import com.alibaba.ut.abtest.internal.util.ProcessUtils;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import com.alibaba.ut.abtest.internal.windvane.UTABTestApiPlugin;
import com.alibaba.ut.abtest.internal.windvane.UTABTestApiPluginV2;
import com.alibaba.ut.abtest.track.TrackMtopMonitor;
import com.alibaba.ut.abtest.track.TrackUTPlugin;
import java.util.Map;
import mtopsdk.mtop.stat.MtopMonitor;

@Keep
public final class UTABTest {
    public static final String COMPONENT_NAV = "UTABTestNav";
    public static final String COMPONENT_URI = "Rewrite";
    private static final VariationSet EMPTY_VARIATION_SET = new DefaultVariationSet((ExperimentActivateGroup) null);
    private static final String TAG = "UTABTest";
    /* access modifiers changed from: private */
    public static volatile boolean fullInitialized = false;
    private static volatile boolean preInitialized = false;

    private UTABTest() {
    }

    public static UTABConfiguration.Builder newConfigurationBuilder() {
        return new UTABConfiguration.Builder();
    }

    public static synchronized void initializeSync(Context context, UTABConfiguration uTABConfiguration) {
        synchronized (UTABTest.class) {
            if (isPreInitialized()) {
                LogUtils.logW(TAG, "SDK已初始化或正在进行中。");
            } else {
                initializeInternal(context, uTABConfiguration, true);
            }
        }
    }

    public static synchronized void initialize(Context context, UTABConfiguration uTABConfiguration) {
        synchronized (UTABTest.class) {
            if (isPreInitialized()) {
                LogUtils.logW(TAG, "SDK已初始化或正在进行中。");
            } else {
                initializeInternal(context, uTABConfiguration, false);
            }
        }
    }

    private static void initializeInternal(Context context, UTABConfiguration uTABConfiguration, boolean z) {
        LogUtils.setTlogEnabled(false);
        StringBuilder sb = new StringBuilder();
        sb.append("SDK开始初始化。是否同步初始化：");
        sb.append(z ? "是" : "否");
        LogUtils.logD(TAG, sb.toString());
        long nanoTime = System.nanoTime();
        PreconditionUtils.checkNotNull(context, "context is null");
        PreconditionUtils.checkNotNull(uTABConfiguration, "configuration is null");
        boolean isMainProcess = ProcessUtils.isMainProcess(context);
        if (isMainProcess || uTABConfiguration.isMultiProcessEnable()) {
            ABContext.getInstance().initialize(context);
            ABContext.getInstance().setEnvironment(uTABConfiguration.getEnvironment());
            ABContext.getInstance().setDebugMode(uTABConfiguration.isDebugEnable());
            ABContext.getInstance().setMultiProcessEnable(uTABConfiguration.isMultiProcessEnable());
            if (isMainProcess || !uTABConfiguration.isMultiProcessEnable()) {
                ABContext.getInstance().getEventService().subscribeEvent(EventType.ExperimentData, new ExperimentDataEventListener());
                ABContext.getInstance().getEventService().subscribeEvent(EventType.User, new UserEventListener());
            }
            LogUtils.logD(TAG, "当前环境：" + ABContext.getInstance().getEnvironment());
            preInitialized = true;
            if (z) {
                new BackgroundInit(uTABConfiguration, isMainProcess).run();
            } else {
                TaskExecutor.executeBackground(new BackgroundInit(uTABConfiguration, isMainProcess));
            }
            long nanoTime2 = System.nanoTime() - nanoTime;
            LogUtils.logD(TAG, "SDK初始化耗时：" + nanoTime2 + "ns");
            return;
        }
        LogUtils.logW(TAG, "未开启多进程支持，只允许主进程初始化SDK");
    }

    protected static boolean isPreInitialized() {
        return preInitialized;
    }

    public static boolean isInitialized() {
        return isPreInitialized() && fullInitialized;
    }

    public static VariationSet activate(String str, String str2) {
        return activate(str, str2, (Map<String, Object>) null, (Object) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00fe A[Catch:{ Throwable -> 0x021a }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x010a A[Catch:{ Throwable -> 0x021a }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x010c A[Catch:{ Throwable -> 0x021a }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0118 A[Catch:{ Throwable -> 0x021a }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x015a A[Catch:{ Throwable -> 0x021a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.ut.abtest.VariationSet activate(java.lang.String r9, java.lang.String r10, java.util.Map<java.lang.String, java.lang.Object> r11, java.lang.Object r12) {
        /*
            long r0 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x021a }
            boolean r2 = isInitialized()     // Catch:{ Throwable -> 0x021a }
            if (r2 != 0) goto L_0x0014
            java.lang.String r9 = "UTABTest"
            java.lang.String r10 = "activate方法调用，需要先调用 UTABTest.initialize() 方法初始化SDK。"
            com.alibaba.ut.abtest.internal.util.LogUtils.logW(r9, r10)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
            return r9
        L_0x0014:
            com.alibaba.ut.abtest.internal.ABContext r2 = com.alibaba.ut.abtest.internal.ABContext.getInstance()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.config.ConfigService r2 = r2.getConfigService()     // Catch:{ Throwable -> 0x021a }
            boolean r2 = r2.isSdkEnabled()     // Catch:{ Throwable -> 0x021a }
            if (r2 != 0) goto L_0x002d
            java.lang.String r9 = "UTABTest"
            java.lang.String r10 = "一休已禁止使用。"
            com.alibaba.ut.abtest.internal.util.LogUtils.logWAndReport(r9, r10)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
            return r9
        L_0x002d:
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x021a }
            if (r2 != 0) goto L_0x020f
            boolean r2 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Throwable -> 0x021a }
            if (r2 == 0) goto L_0x003b
            goto L_0x020f
        L_0x003b:
            java.lang.String r2 = "UTABTestNav"
            boolean r2 = android.text.TextUtils.equals(r2, r9)     // Catch:{ Throwable -> 0x021a }
            if (r2 == 0) goto L_0x005c
            com.alibaba.ut.abtest.internal.ABContext r2 = com.alibaba.ut.abtest.internal.ABContext.getInstance()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.config.ConfigService r2 = r2.getConfigService()     // Catch:{ Throwable -> 0x021a }
            boolean r2 = r2.isNavEnabled()     // Catch:{ Throwable -> 0x021a }
            if (r2 != 0) goto L_0x005c
            java.lang.String r9 = "UTABTest"
            java.lang.String r10 = "统一跳转已禁止使用。"
            com.alibaba.ut.abtest.internal.util.LogUtils.logWAndReport(r9, r10)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
            return r9
        L_0x005c:
            java.lang.String r2 = "UTABTestNav"
            boolean r2 = android.text.TextUtils.equals(r2, r9)     // Catch:{ Throwable -> 0x021a }
            if (r2 == 0) goto L_0x00b6
            com.alibaba.ut.abtest.internal.ABContext r2 = com.alibaba.ut.abtest.internal.ABContext.getInstance()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.config.ConfigService r2 = r2.getConfigService()     // Catch:{ Throwable -> 0x021a }
            boolean r2 = r2.isNavIgnored(r10)     // Catch:{ Throwable -> 0x021a }
            if (r2 == 0) goto L_0x008c
            java.lang.String r9 = "UTABTest"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r11.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r12 = "已禁止处理当前URL，来源URL="
            r11.append(r12)     // Catch:{ Throwable -> 0x021a }
            r11.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r10 = r11.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logWAndReport(r9, r10)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
            return r9
        L_0x008c:
            java.lang.String r2 = "UTABTest"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r3.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r4 = "开始激活实验方案，来源URL="
            r3.append(r4)     // Catch:{ Throwable -> 0x021a }
            r3.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r2, r3)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.ABContext r2 = com.alibaba.ut.abtest.internal.ABContext.getInstance()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.multiprocess.MultiProcessService r3 = r2.getMultiProcessService()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r4 = "Rewrite"
            r7 = 1
            r5 = r10
            r6 = r11
            r8 = r12
            com.alibaba.ut.abtest.VariationSet r11 = r3.getVariations(r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x021a }
            goto L_0x00e6
        L_0x00b6:
            java.lang.String r2 = "UTABTest"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r3.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r4 = "开始激活实验方案，命名空间="
            r3.append(r4)     // Catch:{ Throwable -> 0x021a }
            r3.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r4 = ", 实验标识="
            r3.append(r4)     // Catch:{ Throwable -> 0x021a }
            r3.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r2, r3)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.ABContext r2 = com.alibaba.ut.abtest.internal.ABContext.getInstance()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.multiprocess.MultiProcessService r3 = r2.getMultiProcessService()     // Catch:{ Throwable -> 0x021a }
            r7 = 1
            r4 = r9
            r5 = r10
            r6 = r11
            r8 = r12
            com.alibaba.ut.abtest.VariationSet r11 = r3.getVariations(r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x021a }
        L_0x00e6:
            java.lang.String r12 = "UTABTestNav"
            boolean r12 = android.text.TextUtils.equals(r9, r12)     // Catch:{ Throwable -> 0x021a }
            if (r12 != 0) goto L_0x00fa
            java.lang.String r12 = "Rewrite"
            boolean r12 = android.text.TextUtils.equals(r9, r12)     // Catch:{ Throwable -> 0x021a }
            if (r12 == 0) goto L_0x00f7
            goto L_0x00fa
        L_0x00f7:
            java.lang.String r12 = "variation"
            goto L_0x00fc
        L_0x00fa:
            java.lang.String r12 = "url"
        L_0x00fc:
            if (r11 != 0) goto L_0x0100
            com.alibaba.ut.abtest.VariationSet r11 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
        L_0x0100:
            long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x021a }
            int r4 = r11.size()     // Catch:{ Throwable -> 0x021a }
            if (r4 <= 0) goto L_0x010c
            r4 = 1
            goto L_0x010d
        L_0x010c:
            r4 = 0
        L_0x010d:
            r5 = 0
            long r2 = r2 - r0
            com.alibaba.ut.abtest.internal.util.Analytics.commitExperimentActivateStat(r12, r4, r2)     // Catch:{ Throwable -> 0x021a }
            int r0 = r11.size()     // Catch:{ Throwable -> 0x021a }
            if (r0 != 0) goto L_0x015a
            java.lang.String r0 = "UTABTestNav"
            boolean r0 = android.text.TextUtils.equals(r0, r9)     // Catch:{ Throwable -> 0x021a }
            if (r0 == 0) goto L_0x0139
            java.lang.String r9 = "UTABTest"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r0.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r1 = "未激活实验方案，来源URL="
            r0.append(r1)     // Catch:{ Throwable -> 0x021a }
            r0.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r10 = r0.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r9, r10)     // Catch:{ Throwable -> 0x021a }
            goto L_0x0209
        L_0x0139:
            java.lang.String r0 = "UTABTest"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r1.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r2 = "未激活实验方案，命名空间="
            r1.append(r2)     // Catch:{ Throwable -> 0x021a }
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 实验标识="
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            r1.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = r1.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r0, r9)     // Catch:{ Throwable -> 0x021a }
            goto L_0x0209
        L_0x015a:
            java.lang.String r0 = "ExperimentEffectiveCounter"
            com.alibaba.ut.abtest.internal.util.Analytics.commitCounter(r0, r12)     // Catch:{ Throwable -> 0x021a }
            boolean r0 = com.alibaba.ut.abtest.internal.util.LogUtils.isLogDebugEnable()     // Catch:{ Throwable -> 0x021a }
            if (r0 == 0) goto L_0x01d2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r0.<init>()     // Catch:{ Throwable -> 0x021a }
            java.util.Iterator r1 = r11.iterator()     // Catch:{ Throwable -> 0x021a }
        L_0x016e:
            boolean r2 = r1.hasNext()     // Catch:{ Throwable -> 0x021a }
            if (r2 == 0) goto L_0x019a
            java.lang.Object r2 = r1.next()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.Variation r2 = (com.alibaba.ut.abtest.Variation) r2     // Catch:{ Throwable -> 0x021a }
            int r3 = r0.length()     // Catch:{ Throwable -> 0x021a }
            if (r3 <= 0) goto L_0x0185
            java.lang.String r3 = ", "
            r0.append(r3)     // Catch:{ Throwable -> 0x021a }
        L_0x0185:
            java.lang.String r3 = r2.getName()     // Catch:{ Throwable -> 0x021a }
            r0.append(r3)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r3 = "="
            r0.append(r3)     // Catch:{ Throwable -> 0x021a }
            r3 = 0
            java.lang.Object r2 = r2.getValue(r3)     // Catch:{ Throwable -> 0x021a }
            r0.append(r2)     // Catch:{ Throwable -> 0x021a }
            goto L_0x016e
        L_0x019a:
            java.lang.String r1 = "UTABTest"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r2.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r3 = "激活实验方案，命名空间="
            r2.append(r3)     // Catch:{ Throwable -> 0x021a }
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 实验标识="
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            r2.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 实验分组ID="
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            long r9 = r11.getExperimentBucketId()     // Catch:{ Throwable -> 0x021a }
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 变量内容="
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = r0.toString()     // Catch:{ Throwable -> 0x021a }
            r2.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = r2.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r1, r9)     // Catch:{ Throwable -> 0x021a }
            goto L_0x0209
        L_0x01d2:
            java.lang.String r0 = "UTABTest"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x021a }
            r1.<init>()     // Catch:{ Throwable -> 0x021a }
            java.lang.String r2 = "激活实验方案，命名空间="
            r1.append(r2)     // Catch:{ Throwable -> 0x021a }
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 实验标识="
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            r1.append(r10)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 实验分组ID="
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            long r9 = r11.getExperimentBucketId()     // Catch:{ Throwable -> 0x021a }
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = ", 变量数量="
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            int r9 = r11.size()     // Catch:{ Throwable -> 0x021a }
            r1.append(r9)     // Catch:{ Throwable -> 0x021a }
            java.lang.String r9 = r1.toString()     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.internal.util.LogUtils.logResultAndReport(r0, r9)     // Catch:{ Throwable -> 0x021a }
        L_0x0209:
            java.lang.String r9 = "ExperimentActivateCounter"
            com.alibaba.ut.abtest.internal.util.Analytics.commitCounter(r9, r12)     // Catch:{ Throwable -> 0x021a }
            return r11
        L_0x020f:
            java.lang.String r9 = "UTABTest"
            java.lang.String r10 = "参数不合法，命名空间或实验标识为空！"
            com.alibaba.ut.abtest.internal.util.LogUtils.logWAndReport(r9, r10)     // Catch:{ Throwable -> 0x021a }
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET     // Catch:{ Throwable -> 0x021a }
            return r9
        L_0x021a:
            r9 = move-exception
            java.lang.String r10 = "UTABTest"
            java.lang.String r11 = "activate failure"
            com.alibaba.ut.abtest.internal.util.LogUtils.logE(r10, r11, r9)
            com.alibaba.ut.abtest.VariationSet r9 = EMPTY_VARIATION_SET
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.UTABTest.activate(java.lang.String, java.lang.String, java.util.Map, java.lang.Object):com.alibaba.ut.abtest.VariationSet");
    }

    public static VariationSet getVariations(String str, String str2) {
        return getVariations(str, str2, (Map<String, Object>) null);
    }

    public static VariationSet getVariations(String str, String str2, Map<String, Object> map) {
        try {
            long nanoTime = System.nanoTime();
            if (!isInitialized()) {
                LogUtils.logW(TAG, "getVariations方法调用，需要先调用 UTABTest.initialize() 方法初始化SDK。");
                return EMPTY_VARIATION_SET;
            } else if (!ABContext.getInstance().getConfigService().isSdkEnabled()) {
                LogUtils.logWAndReport(TAG, "一休已禁止使用。");
                return EMPTY_VARIATION_SET;
            } else {
                if (!TextUtils.isEmpty(str)) {
                    if (!TextUtils.isEmpty(str2)) {
                        LogUtils.logResultAndReport(TAG, "开始获取实验变量，命名空间=" + str + ", 实验标识=" + str2);
                        VariationSet variations = ABContext.getInstance().getMultiProcessService().getVariations(str, str2, map, false, (Object) null);
                        if (variations == null) {
                            variations = EMPTY_VARIATION_SET;
                        }
                        Analytics.commitExperimentActivateStat(Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_VARIATION, variations.size() > 0, System.nanoTime() - nanoTime);
                        if (variations.size() == 0) {
                            LogUtils.logResultAndReport(TAG, "未获取到实验变量，命名空间=" + str + ", 实验标识=" + str2);
                        } else {
                            Analytics.commitCounter(Analytics.EXPERIMENT_EFFECTIVE_COUNTER, Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_VARIATION);
                            LogUtils.logResultAndReport(TAG, "获取实验变量，命名空间=" + str + ", 实验标识=" + str2 + ", 实验分组ID=" + variations.getExperimentBucketId() + ", 变量数量=" + variations.size());
                        }
                        Analytics.commitCounter(Analytics.EXPERIMENT_ACTIVATE_COUNTER, Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_VARIATION);
                        return variations;
                    }
                }
                LogUtils.logWAndReport(TAG, "参数不合法，组件名称或模块名称为空！");
                return EMPTY_VARIATION_SET;
            }
        } catch (Throwable th) {
            LogUtils.logE(TAG, "getVariations failure", th);
            return EMPTY_VARIATION_SET;
        }
    }

    public static void activateServer(String str) {
        activateServerInternal(str, (Object) null, false);
    }

    public static void activateServerSync(String str) {
        activateServerInternal(str, (Object) null, true);
    }

    public static void activateServer(String str, Object obj) {
        activateServerInternal(str, obj, false);
    }

    public static void activateServerSync(String str, Object obj) {
        activateServerInternal(str, obj, true);
    }

    private static void activateServerInternal(final String str, final Object obj, boolean z) {
        try {
            long nanoTime = System.nanoTime();
            if (!ABContext.getInstance().getConfigService().isSdkEnabled()) {
                LogUtils.logWAndReport(TAG, "一休已禁止使用。");
            } else if (TextUtils.isEmpty(str)) {
                LogUtils.logWAndReport(TAG, "激活服务端实验取消，数据为空。");
            } else {
                if (z) {
                    ABContext.getInstance().getMultiProcessService().addActivateServerExperimentGroup(str, obj);
                } else {
                    TaskExecutor.executeBackground(new Runnable() {
                        public void run() {
                            try {
                                ABContext.getInstance().getMultiProcessService().addActivateServerExperimentGroup(str, obj);
                            } catch (Throwable th) {
                                LogUtils.logE(UTABTest.TAG, th.getMessage(), th);
                            }
                        }
                    });
                }
                long nanoTime2 = System.nanoTime();
                if (z) {
                    Analytics.commitExperimentActivateStat(Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER_SYNC, !TextUtils.isEmpty(str), nanoTime2 - nanoTime);
                } else {
                    Analytics.commitExperimentActivateStat(Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER, !TextUtils.isEmpty(str), nanoTime2 - nanoTime);
                }
                LogUtils.logResultAndReport(TAG, "激活服务端实验，data=" + str);
                Analytics.commitCounter(Analytics.EXPERIMENT_ACTIVATE_COUNTER, Analytics.EXPERIMENT_ACTIVATE_STAT_TYPE_ACTIVATE_SERVER);
            }
        } catch (Throwable th) {
            LogUtils.logE(TAG, "activateServer failure", th);
        }
    }

    @Deprecated
    public static void addDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        try {
            if (!isInitialized()) {
                LogUtils.logW(TAG, "请先调用 UTABTest.initialize() 方法初始化SDK。");
                return;
            }
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (uTABDataListener != null) {
                    LogUtils.logD(TAG, "addDataListener. component=" + str + ", module=" + str2 + ", listener=" + uTABDataListener);
                    ABContext.getInstance().getDecisionService().addDataListener(str, str2, uTABDataListener);
                    return;
                }
            }
            LogUtils.logWAndReport(TAG, "参数不合法，组件名称，模块名称或监听回调为空！");
        } catch (Throwable th) {
            LogUtils.logE(TAG, "addDataListener failure", th);
        }
    }

    @Deprecated
    public static void removeDataListener(String str, String str2) {
        removeDataListener(str, str2, (UTABDataListener) null);
    }

    @Deprecated
    public static void removeDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        try {
            if (!isInitialized()) {
                LogUtils.logW(TAG, "请先调用 UTABTest.initialize() 方法初始化SDK。");
                return;
            }
            if (!TextUtils.isEmpty(str)) {
                if (!TextUtils.isEmpty(str2)) {
                    LogUtils.logD(TAG, "removeDataListener. component=" + str + ", module=" + str2 + ", listener=" + uTABDataListener);
                    ABContext.getInstance().getDecisionService().removeDataListener(str, str2, uTABDataListener);
                    return;
                }
            }
            LogUtils.logWAndReport(TAG, "参数不合法，组件名称或模块名称为空！");
        } catch (Throwable th) {
            LogUtils.logE(TAG, "removeDataListener failure", th);
        }
    }

    public static synchronized void updateUserAccount(String str, String str2) {
        synchronized (UTABTest.class) {
            try {
                if (!isPreInitialized()) {
                    LogUtils.logW(TAG, "updateUserAccount方法调用，需要先调用 UTABTest.initialize() 方法初始化SDK。");
                    return;
                }
                LogUtils.logDAndReport(TAG, "更新用户帐号信息。用户昵称=" + str + ", 用户ID=" + str2);
                if (!TextUtils.equals(ABContext.getInstance().getUserId(), str2)) {
                    LogUtils.logDAndReport(TAG, "用户帐号信息发生变化。原用户昵称=" + ABContext.getInstance().getUserNick() + ", 原用户ID=" + ABContext.getInstance().getUserId());
                    ABContext.getInstance().setUserId(str2);
                    ABContext.getInstance().setUserNick(str);
                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserId(str2);
                    loginUser.setUserNick(str);
                    ABContext.getInstance().getEventService().publishEvent(new Event(EventType.User, loginUser));
                } else {
                    LogUtils.logDAndReport(TAG, "用户帐号信息未变化。用户昵称=" + str + ", 用户ID=" + str2);
                }
            } catch (Throwable th) {
                LogUtils.logE(TAG, "updateUserAccount failure", th);
            }
        }
        return;
    }

    public static String getAppActivateTrackId() {
        return ABContext.getInstance().getMultiProcessService().getAppActivateTrackId();
    }

    private static class BackgroundInit implements Runnable {
        private UTABConfiguration configuration;
        private boolean isMainProcess;

        public BackgroundInit(UTABConfiguration uTABConfiguration, boolean z) {
            this.configuration = uTABConfiguration;
            this.isMainProcess = z;
        }

        public void run() {
            LogUtils.logD(UTABTest.TAG, "开始后台初始化任务");
            try {
                ABContext.getInstance().getConfigService().initialize();
                try {
                    ABDatabase.getInstance();
                    if (this.configuration.getMethod() != null) {
                        ABContext.getInstance().getConfigService().setMethod(this.configuration.getMethod());
                    }
                    ABContext.getInstance().getMultiProcessService().initialize();
                    if (this.isMainProcess || !this.configuration.isMultiProcessEnable()) {
                        ABContext.getInstance().getDecisionService().initialize();
                        ABContext.getInstance().setCurrentApiMethod(ABContext.getInstance().getConfigService().getMethod());
                        TrackUTPlugin.register();
                        DataUpdateService.register();
                        ABContext.getInstance().getExpressionService();
                    }
                    WVPluginManager.registerPlugin(UTABTestApiPlugin.API_NAME, (Class<? extends WVApiPlugin>) UTABTestApiPlugin.class);
                    WVPluginManager.registerPlugin(UTABTestApiPluginV2.API_NAME, (Class<? extends WVApiPlugin>) UTABTestApiPluginV2.class);
                } catch (Throwable th) {
                    LogUtils.logE(UTABTest.TAG, "初始化数据库失败", th);
                    ABContext.getInstance().getConfigService().setSdkDowngrade(true);
                    boolean unused = UTABTest.fullInitialized = true;
                    return;
                }
            } catch (Throwable th2) {
                try {
                    LogUtils.logE(UTABTest.TAG, "后台初始化失败", th2);
                } catch (Throwable th3) {
                    boolean unused2 = UTABTest.fullInitialized = true;
                    throw th3;
                }
            }
            try {
                MtopMonitor.addHeaderMonitor(new TrackMtopMonitor());
            } catch (Throwable th4) {
                if (this.isMainProcess) {
                    LogUtils.logEAndReport(UTABTest.TAG, "初始化MTOP监听失败，不依赖无痕埋点功能请忽略。" + th4.getMessage());
                } else {
                    LogUtils.logE(UTABTest.TAG, "初始化MTOP监听失败，不依赖无痕埋点功能请忽略。" + th4.getMessage());
                }
            }
            boolean unused3 = UTABTest.fullInitialized = true;
            if (this.isMainProcess || !this.configuration.isMultiProcessEnable()) {
                ABContext.getInstance().getDecisionService().syncExperiments(true);
            }
            LogUtils.logD(UTABTest.TAG, "结束后台初始化任务");
            TaskExecutor.executeBackgroundDelayed(new BackgroundDelayedInit(), 4000);
            boolean unused4 = UTABTest.fullInitialized = true;
        }
    }

    private static class BackgroundDelayedInit implements Runnable {
        private BackgroundDelayedInit() {
        }

        public void run() {
            if (ABContext.getInstance().isDebugMode()) {
                LogUtils.setTlogEnabled(ABContext.getInstance().isDebugMode());
            }
            ABContext.getInstance().getDebugService();
            Analytics.registerExperimentActivateStat();
            Analytics.registerTrackStat();
        }
    }
}
