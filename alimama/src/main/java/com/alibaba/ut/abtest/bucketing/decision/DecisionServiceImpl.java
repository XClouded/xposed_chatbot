package com.alibaba.ut.abtest.bucketing.decision;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ut.abtest.UTABDataListener;
import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.DefaultVariationSet;
import com.alibaba.ut.abtest.internal.bucketing.ExperimentManager;
import com.alibaba.ut.abtest.internal.bucketing.ExperimentRoutingType;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentActivateGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentCognation;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentCognationType;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentResponseData;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentType;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.ServerClock;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import com.alibaba.ut.abtest.internal.util.Utils;
import com.alibaba.ut.abtest.internal.util.hash.Hashing;
import com.alibaba.ut.abtest.pipeline.Request;
import com.alibaba.ut.abtest.pipeline.Response;
import com.alibaba.ut.abtest.pipeline.request.RequestFactory;
import com.alibaba.ut.abtest.track.UriUtils;
import com.taobao.weex.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.SymbolExpUtil;

public class DecisionServiceImpl implements DecisionService {
    private static final String TAG = "DecisionServiceImpl";
    private Comparator<ExperimentGroup> hitGroupComparator = new Comparator<ExperimentGroup>() {
        public int compare(ExperimentGroup experimentGroup, ExperimentGroup experimentGroup2) {
            if (experimentGroup.getExperimentId() == experimentGroup2.getExperimentId()) {
                return (int) (experimentGroup.getId() - experimentGroup2.getId());
            }
            return (int) (experimentGroup.getExperimentId() - experimentGroup2.getExperimentId());
        }
    };
    /* access modifiers changed from: private */
    public AtomicBoolean isSyncExperiments = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public volatile long lastRequestTimestamp = 0;

    public void initialize() {
        LogUtils.logD(TAG, "initialize");
        try {
            ExperimentManager.getInstance().loadMemoryCache();
        } catch (Throwable th) {
            Analytics.commitFail(Analytics.SERVICE_ALARM, "DecisionService.initialize", th.getMessage(), Log.getStackTraceString(th));
            LogUtils.logE(TAG, "initialize failure", th);
        }
    }

    public void syncExperiments(final boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("开始同步实验数据，立即开始：");
        sb.append(z ? "是" : "否");
        LogUtils.logDAndReport(TAG, sb.toString());
        if (!ABContext.getInstance().getConfigService().isSdkEnabled()) {
            LogUtils.logWAndReport(TAG, "UTABTest SDK 已关闭.");
        } else if (this.isSyncExperiments.compareAndSet(false, true)) {
            TaskExecutor.executeBackground(new Runnable() {
                public void run() {
                    try {
                        long unused = DecisionServiceImpl.this.lastRequestTimestamp = System.currentTimeMillis();
                        if (ABContext.getInstance().getCurrentApiMethod() != UTABMethod.Pull) {
                            ABContext.getInstance().getPushService().syncExperiments(false);
                        } else if (z) {
                            TaskExecutor.removeBackgroundCallbacks(1002);
                            int unused2 = DecisionServiceImpl.this._syncExperiments();
                        } else if (TaskExecutor.hasBackgroundCallbacks(1002)) {
                            LogUtils.logDAndReport(DecisionServiceImpl.TAG, "实验数据同步任务已存在，忽略本次同步请求。");
                            DecisionServiceImpl.this.isSyncExperiments.set(false);
                            return;
                        } else {
                            long access$200 = DecisionServiceImpl.this.getSyncExperimentsDelayTime();
                            if (access$200 <= 0) {
                                int unused3 = DecisionServiceImpl.this._syncExperiments();
                            } else {
                                LogUtils.logDAndReport(DecisionServiceImpl.TAG, access$200 + "毫秒后通过API请求实验数据，最大延迟时间配置：" + ABContext.getInstance().getConfigService().getDownloadExperimentDataDelayTime());
                                DecisionServiceImpl.this.delaySyncExperiments(access$200);
                            }
                        }
                    } catch (Throwable th) {
                        DecisionServiceImpl.this.isSyncExperiments.set(false);
                        throw th;
                    }
                    DecisionServiceImpl.this.isSyncExperiments.set(false);
                }
            });
        } else {
            LogUtils.logDAndReport(TAG, "实验数据正在同步，忽略本次同步请求。");
        }
    }

    /* access modifiers changed from: private */
    public long getSyncExperimentsDelayTime() {
        long downloadExperimentDataDelayTime = ABContext.getInstance().getConfigService().getDownloadExperimentDataDelayTime();
        if (ABContext.getInstance().getDecisionService().getExperimentDataVersion() == 0 || downloadExperimentDataDelayTime == 0) {
            return 0;
        }
        return (long) Utils.nextRandomInt((int) downloadExperimentDataDelayTime);
    }

    /* access modifiers changed from: private */
    public void delaySyncExperiments(long j) {
        TaskExecutor.executeBackgroundDelayed(1002, new Runnable() {
            public void run() {
                try {
                    int unused = DecisionServiceImpl.this._syncExperiments();
                } catch (Throwable th) {
                    Analytics.commitFail(Analytics.SERVICE_ALARM, "DecisionService.delaySyncExperiments", th.getMessage(), Log.getStackTraceString(th));
                    LogUtils.logE(DecisionServiceImpl.TAG, "syncExperiments failure.", th);
                }
            }
        }, j);
    }

    /* access modifiers changed from: private */
    public int _syncExperiments() throws Exception {
        LogUtils.logDAndReport(TAG, "通过API请求实验数据");
        Request createExperimentRequest = RequestFactory.createExperimentRequest();
        Response executeRequest = ABContext.getInstance().getPipelineService().executeRequest(createExperimentRequest);
        if (executeRequest == null) {
            if (ABContext.getInstance().isDebugMode()) {
                LogUtils.logWAndReport(TAG, "同步实验数据失败，返回内容为空。request=" + createExperimentRequest);
            } else {
                LogUtils.logWAndReport(TAG, "同步实验数据失败，返回内容为空。");
            }
            return -1;
        } else if (!executeRequest.isSuccess()) {
            if (ABContext.getInstance().isDebugMode()) {
                LogUtils.logWAndReport(TAG, "同步实验数据失败。code=" + executeRequest.getCode() + ", message=" + executeRequest.getMessage() + ", httpCode=" + executeRequest.getHttpResponseCode() + ", request=" + createExperimentRequest);
            } else {
                LogUtils.logWAndReport(TAG, "同步实验数据失败。code=" + executeRequest.getCode() + ", message=" + executeRequest.getMessage() + ", httpCode=" + executeRequest.getHttpResponseCode());
            }
            return 0;
        } else if (executeRequest.getDataJsonObject() == null || executeRequest.getData() == null) {
            LogUtils.logWAndReport(TAG, "同步实验数据失败，返回结果为空。");
            return -1;
        } else {
            if (ABContext.getInstance().isDebugMode()) {
                LogUtils.logResultAndReport(TAG, "同步实验数据成功。request=" + createExperimentRequest + ", response=" + new String(executeRequest.getByteData(), "UTF-8"));
            }
            ExperimentResponseData experimentResponseData = (ExperimentResponseData) executeRequest.getData();
            if (TextUtils.equals(experimentResponseData.sign, ABContext.getInstance().getDecisionService().getExperimentDataSignature())) {
                LogUtils.logDAndReport(TAG, "同步实验数据完成，数据未发生变化。数据签名=" + experimentResponseData.sign + ", 数据版本=" + experimentResponseData.version + ", request=" + createExperimentRequest);
                return 0;
            }
            Analytics.commitCounter(Analytics.EXPERIMENT_DATA_REACH_API, String.valueOf(experimentResponseData.version));
            saveExperiments(experimentResponseData.groups, experimentResponseData.version, experimentResponseData.sign);
            if (experimentResponseData.groups == null) {
                return 0;
            }
            return experimentResponseData.groups.size();
        }
    }

    public void addDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        ExperimentManager.getInstance().addDataListener(str, str2, uTABDataListener);
    }

    public void removeDataListener(String str, String str2, UTABDataListener uTABDataListener) {
        ExperimentManager.getInstance().removeDataListener(str, str2, uTABDataListener);
    }

    public long getExperimentDataVersion() {
        return ExperimentManager.getInstance().getExperimentDataVersion();
    }

    public String getExperimentDataSignature() {
        return ExperimentManager.getInstance().getExperimentDataSignature();
    }

    public void saveExperiments(List<ExperimentGroupPO> list, long j, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("保存实验数据。数据签名=");
        sb.append(str);
        sb.append(", 数据版本=");
        sb.append(j);
        sb.append(", 分组数量=");
        sb.append(list == null ? 0 : list.size());
        LogUtils.logDAndReport(TAG, sb.toString());
        try {
            ExperimentManager.getInstance().saveExperiments(list, j, str);
        } catch (Throwable th) {
            Analytics.commitFail(Analytics.SERVICE_ALARM, "DecisionService.saveExperiments", th.getMessage(), Log.getStackTraceString(th));
            LogUtils.logE(TAG, "saveExperiments failure.", th);
        }
    }

    public void saveBetaExperiments(List<ExperimentGroupPO> list) {
        if (list != null && !list.isEmpty()) {
            try {
                ExperimentManager.getInstance().saveBetaExperiments(list);
            } catch (Throwable th) {
                Analytics.commitFail(Analytics.SERVICE_ALARM, "DecisionService.saveBetaExperiments", th.getMessage(), Log.getStackTraceString(th));
                LogUtils.logE(TAG, "saveExperiments failure.", th);
            }
        }
    }

    public void clearExperimentsCache() {
        ExperimentManager.getInstance().clearExperimentsCache();
    }

    public VariationSet getVariations(String str, String str2, Map<String, Object> map, boolean z, final Object obj) {
        final DebugTrack debugTrack = new DebugTrack();
        final ExperimentActivateGroup activateGroup = getActivateGroup(str, str2, map, debugTrack);
        if (activateGroup == null || activateGroup.getVariations() == null || activateGroup.getVariations().isEmpty()) {
            return null;
        }
        if (z) {
            TaskExecutor.executeBackground(new Runnable() {
                public void run() {
                    ABContext.getInstance().getTrackService().addActivateExperimentGroup(activateGroup, obj);
                    ABContext.getInstance().getTrackService().traceActivate(activateGroup, debugTrack);
                }
            });
        }
        return new DefaultVariationSet(activateGroup);
    }

    /* access modifiers changed from: protected */
    public ExperimentActivateGroup getActivateGroup(String str, String str2, Map<String, Object> map, DebugTrack debugTrack) {
        List<ExperimentGroup> list;
        Uri uri;
        String[] split;
        Uri parseURI;
        String str3 = null;
        if (TextUtils.equals(UTABTest.COMPONENT_URI, str)) {
            uri = UriUtils.parseURI(str2);
            if (uri == null) {
                return null;
            }
            list = ExperimentManager.getInstance().getExperimentGroups(uri);
            LinkedHashMap<String, String> queryParameters = UriUtils.getQueryParameters(uri);
            if (queryParameters != null) {
                if (map == null) {
                    map = new HashMap<>();
                }
                map.putAll(queryParameters);
            }
        } else {
            list = ExperimentManager.getInstance().getExperimentGroups(Utils.getExperimentComponentKey(str, str2));
            uri = null;
        }
        if (list == null) {
            LogUtils.logWAndReport(TAG, "未找到实验分组，组件名称=" + str + "，模块名称=" + str2);
            return null;
        }
        LogUtils.logDAndReport(TAG, "查找到" + list.size() + "个实验分组，组件名称=" + str + "，模块名称=" + str2);
        if (!(debugTrack == null || map == null || map.isEmpty())) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry next : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((String) next.getKey());
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append(next.getValue());
            }
            debugTrack.addTrack("attributes={" + sb.toString() + "}");
        }
        List<ExperimentGroup> hitGroups = getHitGroups(list, map, debugTrack);
        if (LogUtils.isLogDebugEnable()) {
            StringBuilder sb2 = new StringBuilder();
            if (hitGroups != null) {
                for (ExperimentGroup id : hitGroups) {
                    sb2.append(id.getId());
                    sb2.append(",");
                }
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("查找到");
            sb3.append(hitGroups == null ? 0 : hitGroups.size());
            sb3.append("个实验分组符合条件，实验KEY=");
            sb3.append(Utils.getExperimentComponentKey(str, str2));
            sb3.append(", 实验分组ID=");
            sb3.append(sb2.toString());
            LogUtils.logDAndReport(TAG, sb3.toString());
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("查找到");
            sb4.append(hitGroups == null ? 0 : hitGroups.size());
            sb4.append("个实验分组符合条件，实验KEY=");
            sb4.append(Utils.getExperimentComponentKey(str, str2));
            LogUtils.logDAndReport(TAG, sb4.toString());
        }
        if (hitGroups == null || hitGroups.isEmpty()) {
            return null;
        }
        ExperimentActivateGroup experimentActivateGroup = new ExperimentActivateGroup();
        if (TextUtils.equals(UTABTest.COMPONENT_URI, str)) {
            String uriKey = Utils.getUriKey(uri);
            LinkedHashMap<String, String> queryParameters2 = UriUtils.getQueryParameters(uri);
            Uri uri2 = null;
            for (ExperimentGroup next2 : hitGroups) {
                if (next2.getVariations() != null) {
                    String str4 = next2.getVariations().get(ABConstants.BasicConstants.DEFAULT_VARIATION_NAME);
                    if (!TextUtils.isEmpty(str4) && (parseURI = UriUtils.parseURI(str4)) != null) {
                        if (TextUtils.indexOf(str4, ABConstants.Operator.URI_ANY) >= 0) {
                            if (!TextUtils.equals(Utils.getUriKey(next2.getUri()), Utils.getUriKey(parseURI))) {
                                if (uri2 != null) {
                                    LogUtils.logWAndReport(TAG, "最多只能重定向一次，实验分组" + next2.getId() + "会被忽略。");
                                } else {
                                    Uri redirectUrlOperatorAny = Utils.getRedirectUrlOperatorAny(next2.getUri(), parseURI, uri);
                                    if (redirectUrlOperatorAny != null) {
                                        uri2 = redirectUrlOperatorAny;
                                    }
                                }
                            }
                        } else if (!TextUtils.equals(uriKey, Utils.getUriKey(parseURI))) {
                            if (uri2 == null) {
                                uri2 = parseURI;
                            } else {
                                LogUtils.logWAndReport(TAG, "最多只能重定向一次，实验分组" + next2.getId() + "会被忽略。");
                            }
                        }
                        queryParameters2 = UriUtils.mergeParameters(queryParameters2, parseURI);
                        experimentActivateGroup.addGroup(next2);
                    }
                }
            }
            if (queryParameters2 != null) {
                Iterator<Map.Entry<String, String>> it = queryParameters2.entrySet().iterator();
                while (it.hasNext()) {
                    if (TextUtils.equals((CharSequence) it.next().getValue(), ABConstants.Operator.PARAMETER_DELETE)) {
                        it.remove();
                    }
                }
                String str5 = queryParameters2.get(ABConstants.BasicConstants.URI_PARAMNAME_ABTEST);
                if (!TextUtils.isEmpty(str5) && (split = TextUtils.split(str5, "\\.")) != null) {
                    for (String str6 : split) {
                        if (!TextUtils.isEmpty(str6)) {
                            experimentActivateGroup.addTrackId(str6);
                        }
                    }
                }
            }
            if (experimentActivateGroup.getTrackIds() != null) {
                str3 = Utils.join((Collection) experimentActivateGroup.getTrackIds(), ".");
            }
            if (!TextUtils.isEmpty(str3)) {
                queryParameters2.put(ABConstants.BasicConstants.URI_PARAMNAME_ABTEST, str3);
            }
            if (uri2 != null) {
                uri = uri2;
            }
            try {
                String uri3 = UriUtils.createURI(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), UriUtils.formatQueryParameters(queryParameters2, "UTF-8"), uri.getFragment()).toString();
                HashMap hashMap = new HashMap();
                if (!TextUtils.isEmpty(uri3)) {
                    hashMap.put(ABConstants.BasicConstants.DEFAULT_VARIATION_NAME, uri3);
                }
                experimentActivateGroup.setVariations(hashMap);
            } catch (Exception e) {
                LogUtils.logE(TAG, e.getMessage(), e);
            }
        } else {
            ExperimentGroup experimentGroup = hitGroups.get(0);
            experimentActivateGroup.addGroup(experimentGroup);
            experimentActivateGroup.setVariations(experimentGroup.getVariations());
        }
        return experimentActivateGroup;
    }

    /* access modifiers changed from: protected */
    public List<ExperimentGroup> getHitGroups(List<ExperimentGroup> list, Map<String, Object> map, DebugTrack debugTrack) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (ExperimentGroup next : list) {
            if (next != null && !hashSet.contains(Long.valueOf(next.getExperimentId()))) {
                if (!isExperimentGroupEffectiveTime(next)) {
                    LogUtils.logWAndReport(TAG, "实验已过期，实验KEY=" + next.getKey());
                } else if (ABContext.getInstance().getDebugService().isWhitelistExperimentGroup(next)) {
                    LogUtils.logWAndReport(TAG, "实验分组" + next.getId() + "命中白名单, 实验KEY=" + next.getKey());
                    if (debugTrack != null) {
                        debugTrack.addTrack("whitelist=true");
                    }
                    hashSet.add(Long.valueOf(next.getExperimentId()));
                    arrayList.add(next);
                }
            }
        }
        for (ExperimentGroup next2 : list) {
            if (next2 != null && !hashSet.contains(Long.valueOf(next2.getExperimentId()))) {
                if (!isExperimentGroupEffectiveTime(next2)) {
                    LogUtils.logWAndReport(TAG, "实验已过期，实验KEY=" + next2.getKey());
                } else if (isInGroupRange(next2, debugTrack)) {
                    if (ABContext.getInstance().getExpressionService().evaluate(next2.getFeatureConditionExpression(), map)) {
                        if (isInGreyTime(next2)) {
                            LogUtils.logDAndReport(TAG, "实验分组" + next2.getId() + "在灰度期间内, 实验KEY=" + next2.getKey());
                            if (!isInGreyRange(next2)) {
                                LogUtils.logDAndReport(TAG, "实验分组" + next2.getId() + "未命中灰度，实验KEY=" + next2.getKey());
                            }
                        }
                        hashSet.add(Long.valueOf(next2.getExperimentId()));
                        arrayList.add(next2);
                    } else if (next2.getType() == ExperimentType.Redirect) {
                        LogUtils.logDAndReport(TAG, "实验分组" + next2.getId() + "不满足特征条件，定投实验继续查看其它分组, 实验KEY=" + next2.getKey());
                    } else {
                        hashSet.add(Long.valueOf(next2.getExperimentId()));
                        LogUtils.logDAndReport(TAG, "实验分组" + next2.getId() + "不满足特征条件，实验KEY=" + next2.getKey());
                    }
                }
            }
        }
        Collections.sort(arrayList, this.hitGroupComparator);
        return arrayList;
    }

    private int getRatio(String str) {
        return Math.abs(Hashing.getMurmur3_32().hashString(str, ABConstants.BasicConstants.DEFAULT_CHARSET).asInt()) % 1000000;
    }

    /* access modifiers changed from: protected */
    public boolean isExperimentGroupEffectiveTime(ExperimentGroup experimentGroup) {
        long now = ServerClock.now();
        return now >= experimentGroup.getBeginTime() && now <= experimentGroup.getEndTime();
    }

    private String getRatioSeed(ExperimentRoutingType experimentRoutingType, String str) {
        if (experimentRoutingType == ExperimentRoutingType.UserId) {
            String userId = ABContext.getInstance().getUserId();
            if (TextUtils.isEmpty(userId)) {
                return null;
            }
            return userId + str;
        }
        String utdid = SystemInformation.getInstance().getUtdid();
        if (TextUtils.isEmpty(utdid)) {
            return null;
        }
        return utdid + str;
    }

    private int getExperimentRatio(ExperimentRoutingType experimentRoutingType, String str, DebugTrack debugTrack) {
        String str2;
        String ratioSeed = getRatioSeed(experimentRoutingType, str);
        if (TextUtils.isEmpty(ratioSeed)) {
            if (experimentRoutingType == null) {
                str2 = BuildConfig.buildJavascriptFrameworkVersion;
            } else {
                str2 = experimentRoutingType.name();
            }
            Analytics.commitFail(Analytics.SERVICE_ALARM, "DecisionService.routingSeed", str2, "");
            return -1;
        }
        int ratio = getRatio(ratioSeed);
        if (debugTrack != null) {
            debugTrack.addTrack("routingSeed=" + ratioSeed);
            debugTrack.addTrack("routingValue=" + ratio);
        }
        return ratio;
    }

    private boolean isInGroupRange(ExperimentGroup experimentGroup, DebugTrack debugTrack) {
        if (debugTrack != null) {
            debugTrack.addTrack("groupId=" + experimentGroup.getId());
            debugTrack.addTrack("groupRoutingRange=" + Arrays.deepToString(experimentGroup.getRatioRange()));
        }
        return isInGroupRange(experimentGroup, experimentGroup.getCognation(), debugTrack);
    }

    private boolean isInGroupRange(ExperimentGroup experimentGroup, ExperimentCognation experimentCognation, DebugTrack debugTrack) {
        if (experimentCognation == null) {
            return false;
        }
        if (experimentCognation.getType() == ExperimentCognationType.RootDomain) {
            if (debugTrack != null) {
                debugTrack.addTrack("rootDomain=" + experimentCognation.getCode());
            }
            return isInGroupRange(experimentGroup, experimentCognation.getChild(), debugTrack);
        } else if (experimentCognation.getType() == ExperimentCognationType.Domain) {
            if (experimentCognation.getParent() == null || experimentCognation.getParent().getType() != ExperimentCognationType.Layer) {
                return false;
            }
            if (debugTrack != null) {
                debugTrack.addTrack("domainId=" + experimentCognation.getId());
            }
            if (isInRange(getExperimentRatio(experimentCognation.getParent().getRoutingType(), experimentCognation.getParent().getRoutingFactor(), debugTrack), experimentCognation.getRatioRange())) {
                return isInGroupRange(experimentGroup, experimentCognation.getChild(), debugTrack);
            }
            return false;
        } else if (experimentCognation.getType() == ExperimentCognationType.Layer) {
            if (experimentCognation.getChild() != null) {
                return isInGroupRange(experimentGroup, experimentCognation.getChild(), debugTrack);
            }
            Long whitelistGroupIdByLayerId = ABContext.getInstance().getDebugService().getWhitelistGroupIdByLayerId(experimentCognation.getId());
            if (whitelistGroupIdByLayerId == null || whitelistGroupIdByLayerId.longValue() <= 0 || whitelistGroupIdByLayerId.longValue() == experimentGroup.getId()) {
                if (debugTrack != null) {
                    debugTrack.addTrack("layerId=" + experimentCognation.getId());
                    debugTrack.addTrack("layerRoutingType=" + experimentCognation.getRoutingType().name());
                    debugTrack.addTrack("layerRoutingFactor=" + experimentCognation.getRoutingFactor());
                }
                int experimentRatio = getExperimentRatio(experimentCognation.getRoutingType(), experimentCognation.getRoutingFactor(), debugTrack);
                boolean isInRange = isInRange(experimentRatio, experimentGroup.getRatioRange());
                StringBuilder sb = new StringBuilder();
                sb.append("实验分组");
                sb.append(experimentGroup.getId());
                sb.append("所在层分流计算，结果：");
                sb.append(isInRange ? "命中" : "未命中");
                sb.append("，分流方式：");
                sb.append(experimentCognation.getRoutingType());
                sb.append("，分流因子：");
                sb.append(experimentCognation.getRoutingFactor());
                sb.append("，分流值：");
                sb.append(experimentRatio);
                LogUtils.logDAndReport(TAG, sb.toString());
                HashMap hashMap = new HashMap(7);
                hashMap.put("groupId", String.valueOf(experimentGroup.getId()));
                hashMap.put("layerRouting", "true");
                hashMap.put("routingType", experimentCognation.getRoutingType().name());
                hashMap.put("routingFactor", experimentCognation.getRoutingFactor());
                hashMap.put("routingValue", String.valueOf(experimentRatio));
                hashMap.put("routingRange", Arrays.deepToString(experimentGroup.getRatioRange()));
                hashMap.put("success", String.valueOf(isInRange));
                hashMap.put("layerId", String.valueOf(experimentCognation.getId()));
                Analytics.track(Analytics.TRACK_TYPE_ROUTING_RESULT, hashMap);
                return isInRange;
            }
            LogUtils.logWAndReport(TAG, "同层中已有实验分组" + whitelistGroupIdByLayerId + "加入白名单，取消激活当前实验，实验KEY=" + experimentGroup.getKey());
            return false;
        } else if (experimentCognation.getType() != ExperimentCognationType.LaunchLayer) {
            return false;
        } else {
            int experimentRatio2 = getExperimentRatio(experimentCognation.getRoutingType(), experimentCognation.getRoutingFactor(), debugTrack);
            boolean isInRange2 = isInRange(experimentRatio2, experimentGroup.getRatioRange());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("实验分组");
            sb2.append(experimentGroup.getId());
            sb2.append("所在发布层分流计算，结果：");
            sb2.append(isInRange2 ? "命中" : "未命中");
            sb2.append("，分流方式：");
            sb2.append(experimentCognation.getRoutingType());
            sb2.append("，分流因子：");
            sb2.append(experimentCognation.getRoutingFactor());
            sb2.append("，分流值：");
            sb2.append(experimentRatio2);
            LogUtils.logDAndReport(TAG, sb2.toString());
            HashMap hashMap2 = new HashMap(7);
            hashMap2.put("groupId", String.valueOf(experimentGroup.getId()));
            hashMap2.put("launchLayerRouting", "true");
            hashMap2.put("routingType", experimentCognation.getRoutingType().name());
            hashMap2.put("routingFactor", experimentCognation.getRoutingFactor());
            hashMap2.put("routingValue", String.valueOf(experimentRatio2));
            hashMap2.put("routingRange", Arrays.deepToString(experimentGroup.getRatioRange()));
            hashMap2.put("success", String.valueOf(isInRange2));
            hashMap2.put("launchLayerId", String.valueOf(experimentCognation.getId()));
            Analytics.track(Analytics.TRACK_TYPE_ROUTING_RESULT, hashMap2);
            return isInRange2;
        }
    }

    private boolean isInGreyTime(ExperimentGroup experimentGroup) {
        return ServerClock.now() < experimentGroup.getGreyEndTime() && experimentGroup.getGreyPhase() != null && experimentGroup.getGreyPhase().length > 0;
    }

    private boolean isInGreyRange(ExperimentGroup experimentGroup) {
        double now = (double) (ServerClock.now() - experimentGroup.getBeginTime());
        double greyEndTime = (double) (experimentGroup.getGreyEndTime() - experimentGroup.getBeginTime());
        Double.isNaN(now);
        Double.isNaN(greyEndTime);
        double d = now / greyEndTime;
        double length = (double) experimentGroup.getGreyPhase().length;
        Double.isNaN(length);
        boolean z = false;
        int max = Math.max(Math.min((int) (d * length), experimentGroup.getGreyPhase().length - 1), 0);
        String utdid = SystemInformation.getInstance().getUtdid();
        if (getRatio(utdid + experimentGroup.getGreyRoutingFactor()) % 10000 <= experimentGroup.getGreyPhase()[max]) {
            z = true;
        }
        if (!z) {
            LogUtils.logWAndReport(TAG, "当前设备未命中灰度. 当前灰度阶段=" + max + ", 当前阶段范围=" + experimentGroup.getGreyPhase()[max]);
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean isInRange(int i, int[][] iArr) {
        if (i < 0 || iArr == null || iArr.length == 0) {
            return false;
        }
        for (int[] isInRange : iArr) {
            if (isInRange(i, isInRange)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isInRange(int i, int[] iArr) {
        if (iArr == null || iArr.length < 2 || i < iArr[0] || i > iArr[1]) {
            return false;
        }
        return true;
    }

    public Long getExperimentId(long j) {
        return ExperimentManager.getInstance().getExperimentId(j);
    }

    public long getLastRequestTimestamp() {
        return this.lastRequestTimestamp;
    }
}
