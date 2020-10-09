package com.alibaba.ut.abtest.bucketing.decision;

import com.alibaba.ut.abtest.internal.ABContext;
import java.util.ArrayList;
import java.util.List;

public class DebugTrack {
    private List<String> tracks = new ArrayList();

    public void addTrack(String str) {
        this.tracks.add(str);
    }

    public List<String> getTracks() {
        return this.tracks;
    }

    public String getTrackText() {
        StringBuilder sb = new StringBuilder();
        sb.append("dataVersion=");
        sb.append(ABContext.getInstance().getDecisionService().getExperimentDataVersion());
        sb.append(",dataSignature=");
        sb.append(ABContext.getInstance().getDecisionService().getExperimentDataSignature());
        for (String append : this.tracks) {
            sb.append(",");
            sb.append(append);
        }
        return sb.toString();
    }
}
