package com.alibaba.ut.abtest.internal.bucketing.model;

import android.net.Uri;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.ut.abtest.bucketing.expression.Expression;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentGroup implements Serializable {
    private static final long serialVersionUID = 6233534186241353504L;
    @JSONField(name = "beginTime")
    private long beginTime;
    @JSONField(name = "cognation")
    private ExperimentCognation cognation;
    @JSONField(name = "endTime")
    private long endTime;
    @JSONField(name = "experimentId")
    private long experimentId;
    @JSONField(name = "featureCondition")
    private String featureCondition;
    @JSONField(serialize = false)
    private Expression featureConditionExpression;
    @JSONField(name = "greyEndTime")
    private long greyEndTime;
    @JSONField(name = "greyPhase")
    private int[] greyPhase;
    @JSONField(name = "greyRoutingFactor")
    private String greyRoutingFactor;
    @JSONField(name = "id")
    private long id;
    @JSONField(serialize = false)
    private Set<Uri> ignoreUris;
    @JSONField(name = "ignoreUrls")
    private Set<String> ignoreUrls;
    @JSONField(name = "key")
    private String key;
    @JSONField(name = "ratioRange")
    private int[][] ratioRange;
    @JSONField(name = "releaseId")
    private long releaseId;
    @JSONField(name = "tracks")
    private List<ExperimentTrack> tracks;
    @JSONField(name = "type")
    private ExperimentType type;
    @JSONField(serialize = false)
    private Uri uri;
    @JSONField(name = "variations")
    private Map<String, String> variations;

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public long getReleaseId() {
        return this.releaseId;
    }

    public void setReleaseId(long j) {
        this.releaseId = j;
    }

    public long getExperimentId() {
        return this.experimentId;
    }

    public void setExperimentId(long j) {
        this.experimentId = j;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public ExperimentType getType() {
        return this.type;
    }

    public void setType(ExperimentType experimentType) {
        this.type = experimentType;
    }

    public long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(long j) {
        this.beginTime = j;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long j) {
        this.endTime = j;
    }

    public List<ExperimentTrack> getTracks() {
        return this.tracks;
    }

    public void setTracks(List<ExperimentTrack> list) {
        this.tracks = list;
    }

    public Expression getFeatureConditionExpression() {
        return this.featureConditionExpression;
    }

    public void setFeatureConditionExpression(Expression expression) {
        this.featureConditionExpression = expression;
    }

    public String getFeatureCondition() {
        return this.featureCondition;
    }

    public void setFeatureCondition(String str) {
        this.featureCondition = str;
    }

    public int[][] getRatioRange() {
        return this.ratioRange;
    }

    public void setRatioRange(int[][] iArr) {
        this.ratioRange = iArr;
    }

    public int[] getGreyPhase() {
        return this.greyPhase;
    }

    public void setGreyPhase(int[] iArr) {
        this.greyPhase = iArr;
    }

    public long getGreyEndTime() {
        return this.greyEndTime;
    }

    public void setGreyEndTime(long j) {
        this.greyEndTime = j;
    }

    public String getGreyRoutingFactor() {
        return this.greyRoutingFactor;
    }

    public void setGreyRoutingFactor(String str) {
        this.greyRoutingFactor = str;
    }

    public Map<String, String> getVariations() {
        return this.variations;
    }

    public void setVariations(Map<String, String> map) {
        this.variations = map;
    }

    public ExperimentCognation getCognation() {
        return this.cognation;
    }

    public void setCognation(ExperimentCognation experimentCognation) {
        this.cognation = experimentCognation;
    }

    public Set<String> getIgnoreUrls() {
        return this.ignoreUrls;
    }

    public void setIgnoreUrls(Set<String> set) {
        this.ignoreUrls = set;
    }

    public Set<Uri> getIgnoreUris() {
        return this.ignoreUris;
    }

    public void setIgnoreUris(Set<Uri> set) {
        this.ignoreUris = set;
    }
}
