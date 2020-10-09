package com.alibaba.ut.abtest.internal.bucketing;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.bucketing.expression.Expression;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentCognation;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentCognationPO;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentCognationType;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroup;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrack;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentTrackPO;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentType;
import com.alibaba.ut.abtest.internal.track.ExperimentServerTrackPO;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.Utils;
import com.alibaba.ut.abtest.track.UriUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class ExperimentBuilder {
    private static final String TAG = "ExperimentBuilder";

    private ExperimentBuilder() {
    }

    public static List<ExperimentGroup> createExperimentGroups(List<ExperimentGroupPO> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ExperimentGroupPO next : list) {
            ExperimentGroup experimentGroup = new ExperimentGroup();
            if (TextUtils.equals(ExperimentGroupPO.TYPE_AB_EXPERIMENT, next.type)) {
                if (TextUtils.equals(next.component, UTABTest.COMPONENT_URI)) {
                    experimentGroup.setType(ExperimentType.AbUri);
                } else {
                    experimentGroup.setType(ExperimentType.AbComponent);
                }
            } else if (TextUtils.equals("dy", next.type)) {
                experimentGroup.setType(ExperimentType.Redirect);
            } else {
                experimentGroup.setType(ExperimentType.AbComponent);
            }
            if (experimentGroup.getType() == ExperimentType.Redirect || experimentGroup.getType() == ExperimentType.AbUri) {
                Uri parseURI = UriUtils.parseURI(next.module);
                if (parseURI == null) {
                    LogUtils.logEAndReport(TAG, "实验分组" + next.id + "解析URL错误。url=" + next.module);
                } else {
                    experimentGroup.setUri(parseURI);
                    experimentGroup.setKey(next.module);
                }
            } else {
                experimentGroup.setKey(Utils.getExperimentComponentKey(next.component, next.module));
            }
            experimentGroup.setId(next.id);
            experimentGroup.setReleaseId(next.releaseId);
            experimentGroup.setExperimentId(next.experimentId);
            experimentGroup.setBeginTime(next.beginTime);
            experimentGroup.setEndTime(next.endTime);
            if (!TextUtils.isEmpty(next.featureCondition)) {
                experimentGroup.setFeatureCondition(next.featureCondition);
                experimentGroup.setFeatureConditionExpression((Expression) JsonUtil.fromJson(next.featureCondition, Expression.class));
            }
            experimentGroup.setRatioRange(next.ratioRange);
            experimentGroup.setGreyEndTime(next.greyEndTime);
            experimentGroup.setGreyPhase(next.greyPhase);
            experimentGroup.setGreyRoutingFactor(next.greyRoutingFactor);
            if (next.variations != null) {
                experimentGroup.setVariations(new HashMap(next.variations));
            }
            if (next.cognation != null) {
                experimentGroup.setCognation(createExperimentCognation(next.cognation, experimentGroup));
            }
            if (next.tracks != null && !next.tracks.isEmpty()) {
                experimentGroup.setTracks(createExperimentTracks(next.tracks, experimentGroup));
            }
            experimentGroup.setIgnoreUrls(next.ignoreUrls);
            if (next.ignoreUrls != null && !next.ignoreUrls.isEmpty()) {
                HashSet hashSet = new HashSet(next.ignoreUrls.size());
                for (String parseURI2 : next.ignoreUrls) {
                    Uri parseURI3 = UriUtils.parseURI(parseURI2);
                    if (parseURI3 != null) {
                        hashSet.add(parseURI3);
                    }
                }
                if (!hashSet.isEmpty()) {
                    experimentGroup.setIgnoreUris(hashSet);
                }
            }
            arrayList.add(experimentGroup);
        }
        return arrayList;
    }

    public static List<ExperimentGroup> createExperimentGroups(List<ExperimentServerTrackPO> list, String str) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ExperimentServerTrackPO next : list) {
            if (next == null || TextUtils.isEmpty(next.trackConfigs) || next.releaseId <= 0 || next.bucketId <= 0 || TextUtils.isEmpty(next.component) || TextUtils.isEmpty(next.module)) {
                LogUtils.logW(TAG, "服务端实验埋点规则内容不合法！埋点规则=" + str);
            } else if (TextUtils.equals(next.type, ExperimentGroupPO.TYPE_INTELLIGENT_EXPERIMENT)) {
                LogUtils.logW(TAG, "服务端智能实验埋点暂不处理。");
            } else {
                List list2 = (List) JsonUtil.fromJson(next.trackConfigs, new TypeReference<List<ExperimentTrackPO>>() {
                }.getType());
                if (list2 == null || list2.isEmpty()) {
                    LogUtils.logW(TAG, "服务端实验埋点规则内容为空或不合法！埋点规则=" + next.trackConfigs);
                } else {
                    ExperimentGroup experimentGroup = new ExperimentGroup();
                    experimentGroup.setId(next.bucketId);
                    experimentGroup.setReleaseId(next.releaseId);
                    experimentGroup.setExperimentId(next.experimentId);
                    if (TextUtils.equals(ExperimentGroupPO.TYPE_AB_EXPERIMENT, next.type)) {
                        if (TextUtils.equals(next.component, UTABTest.COMPONENT_URI)) {
                            experimentGroup.setType(ExperimentType.AbUri);
                        } else {
                            experimentGroup.setType(ExperimentType.AbComponent);
                        }
                    } else if (TextUtils.equals("dy", next.type)) {
                        experimentGroup.setType(ExperimentType.Redirect);
                    } else {
                        experimentGroup.setType(ExperimentType.AbComponent);
                    }
                    if (experimentGroup.getType() == ExperimentType.Redirect || experimentGroup.getType() == ExperimentType.AbUri) {
                        Uri parseURI = UriUtils.parseURI(next.module);
                        if (parseURI == null) {
                            LogUtils.logW(TAG, "服务端实验埋点规则内容不合法，URI无法解析！URI=" + next.module);
                        } else {
                            experimentGroup.setUri(parseURI);
                            experimentGroup.setKey(next.module);
                        }
                    } else {
                        experimentGroup.setKey(Utils.getExperimentComponentKey(next.component, next.module));
                    }
                    experimentGroup.setTracks(createExperimentTracks(list2, (ExperimentGroup) null));
                    arrayList.add(experimentGroup);
                }
            }
        }
        return arrayList;
    }

    protected static List<ExperimentTrack> createExperimentTracks(List<ExperimentTrackPO> list, ExperimentGroup experimentGroup) {
        ArrayList arrayList = new ArrayList();
        for (ExperimentTrackPO createExperimentTrack : list) {
            arrayList.add(createExperimentTrack(createExperimentTrack));
        }
        return arrayList;
    }

    public static ExperimentTrack createExperimentTrack(ExperimentTrackPO experimentTrackPO) {
        ExperimentTrack experimentTrack = new ExperimentTrack();
        experimentTrack.setPageNames(experimentTrackPO.pageNames);
        experimentTrack.setEventIds(experimentTrackPO.eventIds);
        experimentTrack.setAppScope(experimentTrackPO.appScope);
        return experimentTrack;
    }

    public static ExperimentGroup createExperimentGroup(ExperimentGroupDO experimentGroupDO) {
        ExperimentGroup experimentGroup = (ExperimentGroup) JsonUtil.fromJson(experimentGroupDO.getData(), ExperimentGroup.class);
        if (experimentGroup == null || experimentGroup.getId() <= 0 || experimentGroup.getReleaseId() <= 0 || experimentGroup.getExperimentId() <= 0 || TextUtils.isEmpty(experimentGroup.getKey()) || experimentGroup.getType() == null) {
            return null;
        }
        if (experimentGroup.getType() == ExperimentType.AbUri || experimentGroup.getType() == ExperimentType.Redirect) {
            Uri parseURI = UriUtils.parseURI(experimentGroupDO.getKey());
            if (parseURI == null) {
                return null;
            }
            experimentGroup.setUri(parseURI);
        }
        if (!TextUtils.isEmpty(experimentGroup.getFeatureCondition())) {
            experimentGroup.setFeatureConditionExpression((Expression) JsonUtil.fromJson(experimentGroup.getFeatureCondition(), Expression.class));
        }
        if (experimentGroup.getIgnoreUrls() != null && !experimentGroup.getIgnoreUrls().isEmpty()) {
            HashSet hashSet = new HashSet(experimentGroup.getIgnoreUrls().size());
            for (String parseURI2 : experimentGroup.getIgnoreUrls()) {
                Uri parseURI3 = UriUtils.parseURI(parseURI2);
                if (parseURI3 != null) {
                    hashSet.add(parseURI3);
                }
            }
            if (!hashSet.isEmpty()) {
                experimentGroup.setIgnoreUris(hashSet);
            }
        }
        return experimentGroup;
    }

    public static ExperimentGroupDO createExperimentGroupDO(ExperimentGroup experimentGroup) {
        ExperimentGroupDO experimentGroupDO = new ExperimentGroupDO();
        experimentGroupDO.setId(experimentGroup.getId());
        experimentGroupDO.setKey(experimentGroup.getKey());
        if (experimentGroup.getType() == null) {
            experimentGroupDO.setType(ExperimentType.AbComponent.getValue());
        } else {
            experimentGroupDO.setType(experimentGroup.getType().getValue());
        }
        experimentGroupDO.setBeginTime(experimentGroup.getBeginTime());
        experimentGroupDO.setEndTime(experimentGroup.getEndTime());
        experimentGroupDO.setData(JsonUtil.toJson(experimentGroup));
        return experimentGroupDO;
    }

    public static ExperimentCognation createExperimentCognation(ExperimentCognationPO experimentCognationPO, ExperimentGroup experimentGroup) {
        ExperimentCognation experimentCognation = new ExperimentCognation();
        experimentCognation.setId(experimentCognationPO.id);
        experimentCognation.setCode(experimentCognationPO.code);
        experimentCognation.setFeatureCondition(experimentCognationPO.featureCondition);
        experimentCognation.setRatioRange(experimentCognationPO.ratioRange);
        experimentCognation.setRoutingFactor(experimentCognationPO.routingFactor);
        if (experimentCognationPO.routingType == 2) {
            experimentCognation.setRoutingType(ExperimentRoutingType.UserId);
        } else if (experimentCognationPO.routingType == 1) {
            experimentCognation.setRoutingType(ExperimentRoutingType.Utdid);
        } else {
            experimentCognation.setRoutingType(ExperimentRoutingType.Utdid);
        }
        if (TextUtils.equals(ExperimentCognationPO.TYPE_ROOT_DOMAIN, experimentCognationPO.type)) {
            experimentCognation.setType(ExperimentCognationType.RootDomain);
        } else if (TextUtils.equals("domain", experimentCognationPO.type)) {
            experimentCognation.setType(ExperimentCognationType.Domain);
        } else if (TextUtils.equals(ExperimentCognationPO.TYPE_LAYER, experimentCognationPO.type)) {
            experimentCognation.setType(ExperimentCognationType.Layer);
        } else if (TextUtils.equals(ExperimentCognationPO.TYPE_LAUNCH_LAYER, experimentCognationPO.type)) {
            experimentCognation.setType(ExperimentCognationType.LaunchLayer);
        } else {
            experimentCognation.setType(ExperimentCognationType.Unknown);
        }
        if (experimentCognationPO.child != null) {
            ExperimentCognation createExperimentCognation = createExperimentCognation(experimentCognationPO.child, experimentGroup);
            createExperimentCognation.setParent(experimentCognation);
            experimentCognation.setChild(createExperimentCognation);
        }
        return experimentCognation;
    }
}
