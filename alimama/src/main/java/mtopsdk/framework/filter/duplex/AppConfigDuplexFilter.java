package mtopsdk.framework.filter.duplex;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopUnitStrategy;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.mtop.util.MtopStatistics;
import mtopsdk.mtop.xcommand.XcmdEventMgr;

public class AppConfigDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.AppConfigDuplexFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        Map<String, List<String>> headerFields = mtopContext.mtopResponse.getHeaderFields();
        MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(headerFields, HttpHeaderConstant.X_COMMAND_ORANGE);
        if (StringUtils.isNotBlank(singleHeaderFieldByKey) && StringUtils.isNotBlank(singleHeaderFieldByKey)) {
            try {
                XcmdEventMgr.getInstance().onOrangeEvent(URLDecoder.decode(singleHeaderFieldByKey, "utf-8"));
            } catch (Exception e) {
                String str = mtopContext.seqNo;
                TBSdkLog.w(TAG, str, "parse XCommand header field x-orange-p error,xcmdOrange=" + singleHeaderFieldByKey, e);
            }
        }
        String singleHeaderFieldByKey2 = HeaderHandlerUtil.getSingleHeaderFieldByKey(headerFields, HttpHeaderConstant.X_APP_CONF_V);
        if (StringUtils.isBlank(singleHeaderFieldByKey2)) {
            return FilterResult.CONTINUE;
        }
        long j = 0;
        try {
            j = Long.parseLong(singleHeaderFieldByKey2);
        } catch (NumberFormatException e2) {
            String str2 = mtopContext.seqNo;
            TBSdkLog.e(TAG, str2, "parse remoteAppConfigVersion error.appConfigVersion=" + singleHeaderFieldByKey2, e2);
        }
        if (j <= mtopConfig.xAppConfigVersion) {
            return FilterResult.CONTINUE;
        }
        updateAppConf(j, mtopContext);
        return FilterResult.CONTINUE;
    }

    private void updateAppConf(long j, MtopContext mtopContext) {
        final MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        final long j2 = j;
        final MtopContext mtopContext2 = mtopContext;
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:30:0x008d A[SYNTHETIC, Splitter:B:30:0x008d] */
            /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r8 = this;
                    mtopsdk.mtop.global.MtopConfig r0 = r3
                    byte[] r0 = r0.lock
                    monitor-enter(r0)
                    long r1 = r4     // Catch:{ all -> 0x00fe }
                    mtopsdk.mtop.global.MtopConfig r3 = r3     // Catch:{ all -> 0x00fe }
                    long r3 = r3.xAppConfigVersion     // Catch:{ all -> 0x00fe }
                    int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                    if (r5 > 0) goto L_0x0011
                    monitor-exit(r0)     // Catch:{ all -> 0x00fe }
                    return
                L_0x0011:
                    mtopsdk.framework.domain.MtopContext r1 = r6     // Catch:{ all -> 0x00fe }
                    mtopsdk.mtop.domain.MtopResponse r1 = r1.mtopResponse     // Catch:{ all -> 0x00fe }
                    byte[] r1 = r1.getBytedata()     // Catch:{ all -> 0x00fe }
                    if (r1 != 0) goto L_0x001d
                    monitor-exit(r0)     // Catch:{ all -> 0x00fe }
                    return
                L_0x001d:
                    r2 = 0
                    r3 = 0
                    java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x007d }
                    java.lang.String r5 = "utf-8"
                    r4.<init>(r1, r5)     // Catch:{ Exception -> 0x007d }
                    org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x007d }
                    r1.<init>(r4)     // Catch:{ Exception -> 0x007d }
                    java.lang.String r4 = "appConf"
                    java.lang.String r1 = r1.optString(r4)     // Catch:{ Exception -> 0x007d }
                    boolean r4 = mtopsdk.common.util.StringUtils.isNotBlank(r1)     // Catch:{ Exception -> 0x007b }
                    if (r4 == 0) goto L_0x0044
                    mtopsdk.config.AppConfigManager r4 = mtopsdk.config.AppConfigManager.getInstance()     // Catch:{ Exception -> 0x007b }
                    mtopsdk.framework.domain.MtopContext r5 = r6     // Catch:{ Exception -> 0x007b }
                    java.lang.String r5 = r5.seqNo     // Catch:{ Exception -> 0x007b }
                    boolean r4 = r4.parseAppConfig(r1, r5)     // Catch:{ Exception -> 0x007b }
                    r3 = r4
                L_0x0044:
                    if (r3 == 0) goto L_0x008a
                    mtopsdk.mtop.global.MtopConfig r4 = r3     // Catch:{ Exception -> 0x007b }
                    long r5 = r4     // Catch:{ Exception -> 0x007b }
                    r4.xAppConfigVersion = r5     // Catch:{ Exception -> 0x007b }
                    mtopsdk.common.util.TBSdkLog$LogEnable r4 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ Exception -> 0x007b }
                    boolean r4 = mtopsdk.common.util.TBSdkLog.isLogEnable(r4)     // Catch:{ Exception -> 0x007b }
                    if (r4 == 0) goto L_0x008a
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x007b }
                    r5 = 64
                    r4.<init>(r5)     // Catch:{ Exception -> 0x007b }
                    java.lang.String r5 = "[updateAppConf]update AppConf succeed!appConfVersion="
                    r4.append(r5)     // Catch:{ Exception -> 0x007b }
                    long r5 = r4     // Catch:{ Exception -> 0x007b }
                    r4.append(r5)     // Catch:{ Exception -> 0x007b }
                    java.lang.String r5 = ", appConf="
                    r4.append(r5)     // Catch:{ Exception -> 0x007b }
                    r4.append(r1)     // Catch:{ Exception -> 0x007b }
                    java.lang.String r5 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r6 = r6     // Catch:{ Exception -> 0x007b }
                    java.lang.String r6 = r6.seqNo     // Catch:{ Exception -> 0x007b }
                    java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x007b }
                    mtopsdk.common.util.TBSdkLog.i((java.lang.String) r5, (java.lang.String) r6, (java.lang.String) r4)     // Catch:{ Exception -> 0x007b }
                    goto L_0x008a
                L_0x007b:
                    r4 = move-exception
                    goto L_0x007f
                L_0x007d:
                    r4 = move-exception
                    r1 = r2
                L_0x007f:
                    java.lang.String r5 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r6 = r6     // Catch:{ all -> 0x00fe }
                    java.lang.String r6 = r6.seqNo     // Catch:{ all -> 0x00fe }
                    java.lang.String r7 = "[updateAppConf]parse and persist AppConf in data error"
                    mtopsdk.common.util.TBSdkLog.e(r5, r6, r7, r4)     // Catch:{ all -> 0x00fe }
                L_0x008a:
                    monitor-exit(r0)     // Catch:{ all -> 0x00fe }
                    if (r3 == 0) goto L_0x00fd
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e0 }
                    r0.<init>()     // Catch:{ Exception -> 0x00e0 }
                    mtopsdk.mtop.global.MtopConfig r3 = r3     // Catch:{ Exception -> 0x00e0 }
                    android.content.Context r3 = r3.context     // Catch:{ Exception -> 0x00e0 }
                    java.io.File r2 = r3.getExternalFilesDir(r2)     // Catch:{ Exception -> 0x00e0 }
                    java.io.File r2 = r2.getAbsoluteFile()     // Catch:{ Exception -> 0x00e0 }
                    r0.append(r2)     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r2 = "/mtop"
                    r0.append(r2)     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00e0 }
                    mtopsdk.mtop.cache.domain.AppConfigDo r2 = new mtopsdk.mtop.cache.domain.AppConfigDo     // Catch:{ Exception -> 0x00e0 }
                    long r3 = r4     // Catch:{ Exception -> 0x00e0 }
                    r2.<init>(r1, r3)     // Catch:{ Exception -> 0x00e0 }
                    java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x00e0 }
                    r1.<init>(r0)     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r0 = "appConf"
                    mtopsdk.common.util.MtopUtils.writeObject(r2, r1, r0)     // Catch:{ Exception -> 0x00e0 }
                    mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ Exception -> 0x00e0 }
                    boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)     // Catch:{ Exception -> 0x00e0 }
                    if (r0 == 0) goto L_0x00fd
                    java.lang.String r0 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r1 = r6     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r1 = r1.seqNo     // Catch:{ Exception -> 0x00e0 }
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e0 }
                    r2.<init>()     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r3 = "[updateAppConf] store appConf succeed. appConfVersion="
                    r2.append(r3)     // Catch:{ Exception -> 0x00e0 }
                    long r3 = r4     // Catch:{ Exception -> 0x00e0 }
                    r2.append(r3)     // Catch:{ Exception -> 0x00e0 }
                    java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00e0 }
                    mtopsdk.common.util.TBSdkLog.i((java.lang.String) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Exception -> 0x00e0 }
                    goto L_0x00fd
                L_0x00e0:
                    r0 = move-exception
                    java.lang.String r1 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r2 = r6
                    java.lang.String r2 = r2.seqNo
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder
                    r3.<init>()
                    java.lang.String r4 = "[updateAppConf] store appConf error. appConfVersion="
                    r3.append(r4)
                    long r4 = r4
                    r3.append(r4)
                    java.lang.String r3 = r3.toString()
                    mtopsdk.common.util.TBSdkLog.e(r1, r2, r3, r0)
                L_0x00fd:
                    return
                L_0x00fe:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00fe }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.duplex.AppConfigDuplexFilter.AnonymousClass1.run():void");
            }
        });
    }

    public String doBefore(MtopContext mtopContext) {
        EnvModeEnum envModeEnum;
        Mtop mtop = mtopContext.mtopInstance;
        MtopStatistics mtopStatistics = mtopContext.stats;
        MtopNetworkProp mtopNetworkProp = mtopContext.property;
        try {
            StringBuilder sb = new StringBuilder(64);
            sb.append(mtop.getMtopConfig().utdid);
            sb.append(System.currentTimeMillis());
            sb.append(new DecimalFormat("0000").format((long) (mtopStatistics.intSeqNo % 10000)));
            sb.append("1");
            sb.append(mtop.getMtopConfig().processId);
            mtopNetworkProp.clientTraceId = sb.toString();
            mtopStatistics.clientTraceId = mtopNetworkProp.clientTraceId;
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "generate client-trace-id failed.", e);
        }
        try {
            if (!AppConfigManager.getInstance().isTradeUnitApi(mtopContext.mtopRequest.getKey()) || (envModeEnum = mtop.getMtopConfig().envMode) == null) {
                return FilterResult.CONTINUE;
            }
            switch (envModeEnum) {
                case ONLINE:
                    mtopNetworkProp.customOnlineDomain = MtopUnitStrategy.TRADE_ONLINE_DOMAIN;
                    return FilterResult.CONTINUE;
                case PREPARE:
                    mtopNetworkProp.customPreDomain = MtopUnitStrategy.TRADE_PRE_DOMAIN;
                    return FilterResult.CONTINUE;
                case TEST:
                case TEST_SANDBOX:
                    mtopNetworkProp.customDailyDomain = MtopUnitStrategy.TRADE_DAILY_DOMAIN;
                    return FilterResult.CONTINUE;
                default:
                    return FilterResult.CONTINUE;
            }
        } catch (Exception e2) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "setCustomDomain for trade unit api error", e2);
            return FilterResult.CONTINUE;
        }
    }
}
