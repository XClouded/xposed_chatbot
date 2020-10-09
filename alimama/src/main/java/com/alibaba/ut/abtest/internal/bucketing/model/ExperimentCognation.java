package com.alibaba.ut.abtest.internal.bucketing.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.ut.abtest.internal.bucketing.ExperimentRoutingType;
import java.io.Serializable;

public class ExperimentCognation implements Serializable {
    private static final long serialVersionUID = -9063276129655812466L;
    @JSONField(name = "child")
    private ExperimentCognation child;
    @JSONField(name = "code")
    private String code;
    @JSONField(name = "featureCondition")
    private String featureCondition;
    @JSONField(name = "id")
    private long id;
    @JSONField(name = "parent")
    private ExperimentCognation parent;
    @JSONField(name = "ratioRange")
    private int[][] ratioRange;
    @JSONField(name = "routingFactor")
    private String routingFactor;
    @JSONField(name = "routingType")
    private ExperimentRoutingType routingType;
    @JSONField(name = "type")
    private ExperimentCognationType type;

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public ExperimentCognationType getType() {
        return this.type;
    }

    public void setType(ExperimentCognationType experimentCognationType) {
        this.type = experimentCognationType;
    }

    public String getFeatureCondition() {
        return this.featureCondition;
    }

    public void setFeatureCondition(String str) {
        this.featureCondition = str;
    }

    public ExperimentRoutingType getRoutingType() {
        return this.routingType;
    }

    public void setRoutingType(ExperimentRoutingType experimentRoutingType) {
        this.routingType = experimentRoutingType;
    }

    public String getRoutingFactor() {
        return this.routingFactor;
    }

    public void setRoutingFactor(String str) {
        this.routingFactor = str;
    }

    public int[][] getRatioRange() {
        return this.ratioRange;
    }

    public void setRatioRange(int[][] iArr) {
        this.ratioRange = iArr;
    }

    public ExperimentCognation getParent() {
        return this.parent;
    }

    public void setParent(ExperimentCognation experimentCognation) {
        this.parent = experimentCognation;
    }

    public ExperimentCognation getChild() {
        return this.child;
    }

    public void setChild(ExperimentCognation experimentCognation) {
        this.child = experimentCognation;
    }
}
