package com.alibaba.ut.abtest.internal.bucketing.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentGroupPO implements Serializable {
    public static final String TYPE_AB_EXPERIMENT = "expt";
    public static final String TYPE_INTELLIGENT_EXPERIMENT = "intelligent_expt";
    public static final String TYPE_REDIRECT_EXPERIMENT = "dy";
    private static final long serialVersionUID = -8195562545082120204L;
    @JSONField(name = "beginTime")
    public long beginTime;
    @JSONField(name = "betaTestDevices")
    public List<ExperimentBetaDevice> betaDevices;
    @JSONField(name = "cognationNode")
    public ExperimentCognationPO cognation;
    @JSONField(name = "component")
    public String component;
    @JSONField(name = "endTime")
    public long endTime;
    @JSONField(name = "exptId")
    public long experimentId;
    @JSONField(name = "featureCondition")
    public String featureCondition;
    @JSONField(name = "greyEndTime")
    public long greyEndTime;
    @JSONField(name = "greyPhase")
    public int[] greyPhase;
    @JSONField(name = "greyRandomFactor")
    public String greyRoutingFactor;
    @JSONField(name = "id")
    public long id;
    @JSONField(name = "ignoreUrls")
    public Set<String> ignoreUrls;
    @JSONField(name = "module")
    public String module;
    @JSONField(name = "ratioRanges")
    public int[][] ratioRange;
    @JSONField(name = "releaseId")
    public long releaseId;
    @JSONField(name = "tracks")
    public List<ExperimentTrackPO> tracks;
    @JSONField(name = "type")
    public String type;
    @JSONField(name = "variations")
    public Map<String, String> variations;
}
