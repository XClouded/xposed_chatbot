package com.alibaba.ut.abtest.event.internal;

import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentGroupPO;
import java.io.Serializable;
import java.util.List;

public class ExperimentData implements Serializable {
    private List<ExperimentGroupPO> betaExperimentGroups;
    private String experimentData;

    public ExperimentData() {
    }

    public ExperimentData(String str, List<ExperimentGroupPO> list) {
        this.experimentData = str;
        this.betaExperimentGroups = list;
    }

    public String getExperimentData() {
        return this.experimentData;
    }

    public void setExperimentData(String str) {
        this.experimentData = str;
    }

    public List<ExperimentGroupPO> getBetaExperimentGroups() {
        return this.betaExperimentGroups;
    }

    public void setBetaExperimentGroups(List<ExperimentGroupPO> list) {
        this.betaExperimentGroups = list;
    }
}
