package com.alibaba.ut.abtest;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public class UTABExperimentGroup implements Serializable {
    private static final long serialVersionUID = 3009834165387339552L;
    @JSONField(name = "experimentGroupId")
    private long experimentGroupId;
    @JSONField(name = "experimentId")
    private long experimentId;
    @JSONField(name = "experimentReleaseId")
    private long experimentReleaseId;

    public long getExperimentId() {
        return this.experimentId;
    }

    public void setExperimentId(long j) {
        this.experimentId = j;
    }

    public long getExperimentReleaseId() {
        return this.experimentReleaseId;
    }

    public void setExperimentReleaseId(long j) {
        this.experimentReleaseId = j;
    }

    public long getExperimentGroupId() {
        return this.experimentGroupId;
    }

    public void setExperimentGroupId(long j) {
        this.experimentGroupId = j;
    }
}
