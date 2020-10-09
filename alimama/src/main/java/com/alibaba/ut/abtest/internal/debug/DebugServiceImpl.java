package com.alibaba.ut.abtest.internal.debug;

import android.os.Bundle;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import com.alibaba.ut.abtest.pipeline.Request;
import com.alibaba.ut.abtest.pipeline.Response;
import com.alibaba.ut.abtest.pipeline.request.RequestFactory;
import com.alibaba.ut.abtest.push.UTABPushConfiguration;
import com.taobao.weex.el.parse.Operators;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DebugServiceImpl implements DebugService {
    private static final String TAG = "DebugServiceImpl";
    /* access modifiers changed from: private */
    public static AtomicBoolean isReportLogRunning = new AtomicBoolean(false);
    private static BlockingQueue<ReportLog> logQueue = new LinkedBlockingQueue();
    private DebugKey currentDebugKey;
    private int logMaxReportSize = 5;
    private ConcurrentHashMap<Long, Long> whitelistGroupIds = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, Long> whitelistLayerIds = new ConcurrentHashMap<>();

    public DebugServiceImpl() {
        try {
            WVPluginManager.registerPlugin(DebugWindVanePlugin.API_SERVER_NAME, (Class<? extends WVApiPlugin>) DebugWindVanePlugin.class);
        } catch (Throwable th) {
            LogUtils.logE(TAG, "注册WindVane失败", th);
        }
    }

    public void reportLog(int i, String str, String str2, String str3, String str4) {
        if (this.currentDebugKey != null) {
            try {
                ReportLog reportLog = new ReportLog();
                reportLog.setTime(System.currentTimeMillis());
                reportLog.setLevel(str);
                reportLog.setType(str2);
                reportLog.setContent(Operators.ARRAY_START_STR + i + Operators.ARRAY_END_STR + str4);
                logQueue.offer(reportLog);
                if (isReportLogRunning.compareAndSet(false, true)) {
                    TaskExecutor.executeBackground(new Runnable() {
                        public void run() {
                            try {
                                DebugServiceImpl.this.handleLogDataQueue();
                            } catch (Exception e) {
                                LogUtils.logE(DebugServiceImpl.TAG, e.getMessage(), e);
                            }
                            DebugServiceImpl.isReportLogRunning.set(false);
                        }
                    });
                }
            } catch (Throwable th) {
                LogUtils.logE(TAG, th.getMessage(), th);
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void handleLogDataQueue() {
        /*
            r6 = this;
            monitor-enter(r6)
            r0 = 1
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            r1.<init>()     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
        L_0x0007:
            if (r0 == 0) goto L_0x002b
            java.util.concurrent.BlockingQueue<com.alibaba.ut.abtest.internal.debug.ReportLog> r2 = logQueue     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            r3 = 2
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            java.lang.Object r2 = r2.poll(r3, r5)     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            com.alibaba.ut.abtest.internal.debug.ReportLog r2 = (com.alibaba.ut.abtest.internal.debug.ReportLog) r2     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            if (r2 == 0) goto L_0x0029
            r1.add(r2)     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            int r2 = r1.size()     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            int r3 = r6.logMaxReportSize     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            if (r2 <= r3) goto L_0x0007
            r6.reportLog(r1)     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            r1.clear()     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            goto L_0x0007
        L_0x0029:
            r0 = 0
            goto L_0x0007
        L_0x002b:
            int r0 = r1.size()     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            if (r0 <= 0) goto L_0x0038
            r6.reportLog(r1)     // Catch:{ InterruptedException -> 0x0038, all -> 0x0035 }
            goto L_0x0038
        L_0x0035:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L_0x0038:
            monitor-exit(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.internal.debug.DebugServiceImpl.handleLogDataQueue():void");
    }

    private void reportLog(List<ReportLog> list) {
        Request createLogReportRequest = RequestFactory.createLogReportRequest(list, this.currentDebugKey == null ? "" : this.currentDebugKey.getKey());
        Response executeRequest = ABContext.getInstance().getPipelineService().executeRequest(createLogReportRequest);
        if (executeRequest == null) {
            LogUtils.logW(TAG, "Response is null, request=" + createLogReportRequest);
        } else if (!executeRequest.isSuccess()) {
            LogUtils.logW(TAG, "Response is failure, code=" + executeRequest.getCode() + ", message=" + executeRequest.getMessage() + ", httpCode=" + executeRequest.getHttpResponseCode() + ", request=" + createLogReportRequest);
        }
    }

    public void startRealTimeDebug(Debug debug) {
        if (debug != null && !TextUtils.isEmpty(debug.debugKey) && !TextUtils.isEmpty(debug.debugSamplingOption)) {
            ABContext.getInstance().getMultiProcessService().sendMsgToAllSubProcess(1000, (Bundle) null);
            this.currentDebugKey = new DebugKey(debug.debugKey);
            LogUtils.logDAndReport(TAG, "开启实时调试模式。");
            LogUtils.logDAndReport(TAG, "当前环境：" + ABContext.getInstance().getEnvironment());
            LogUtils.logDAndReport(TAG, "数据获取方式：" + ABContext.getInstance().getCurrentApiMethod());
            LogUtils.logDAndReport(TAG, "数据版本：" + ABContext.getInstance().getDecisionService().getExperimentDataVersion());
            LogUtils.logDAndReport(TAG, "数据签名：" + ABContext.getInstance().getDecisionService().getExperimentDataSignature());
            LogUtils.logDAndReport(TAG, "UTDID：" + SystemInformation.getInstance().getUtdid());
            LogUtils.logDAndReport(TAG, "UserId：" + ABContext.getInstance().getUserId());
            LogUtils.logDAndReport(TAG, "UserNick：" + ABContext.getInstance().getUserNick());
            if (ABContext.getInstance().getCurrentApiMethod() == UTABMethod.Pull) {
                ABContext.getInstance().getDecisionService().syncExperiments(true);
            } else {
                ABContext.getInstance().getPushService().destory();
                ABContext.getInstance().getPushService().initialize(new UTABPushConfiguration.Builder().create());
            }
            HashMap hashMap = new HashMap();
            hashMap.put(Constants.RealTimeDebug.DEBUG_API_URL, "http://usertrack.alibaba-inc.com");
            hashMap.put("debug_key", debug.debugKey);
            hashMap.put(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION, debug.debugSamplingOption);
            UTTeamWork.getInstance().turnOnRealTimeDebug(hashMap);
        }
    }

    public void setWhitelist(Map<Long, Long> map) {
        this.whitelistGroupIds.clear();
        this.whitelistLayerIds.clear();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry next : map.entrySet()) {
                this.whitelistGroupIds.put(Long.valueOf(((Long) next.getValue()).longValue()), next.getKey());
                this.whitelistLayerIds.put(Long.valueOf(((Long) next.getKey()).longValue()), next.getValue());
                Long experimentId = ABContext.getInstance().getDecisionService().getExperimentId(((Long) next.getValue()).longValue());
                if (experimentId != null) {
                    ABContext.getInstance().getTrackService().removeActivateExperiment(String.valueOf(experimentId));
                }
            }
            LogUtils.logDAndReport(TAG, "当前设备共生效" + this.whitelistGroupIds.size() + "个实验分组白名单。" + JsonUtil.toJson(this.whitelistGroupIds.keys()));
        }
    }

    public void setLogMaxReportSize(int i) {
        this.logMaxReportSize = i;
    }

    public Long getWhitelistGroupIdByLayerId(long j) {
        return this.whitelistLayerIds.get(Long.valueOf(j));
    }

    public boolean isWhitelistExperimentGroup(ExperimentGroup experimentGroup) {
        return this.whitelistGroupIds.containsKey(Long.valueOf(experimentGroup.getId()));
    }
}
