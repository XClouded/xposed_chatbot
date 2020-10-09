package com.alibaba.ut.abtest.internal.debug;

import com.alibaba.ut.abtest.internal.util.Utils;

public class DebugKey {
    private long effectiveTimeInSecond;
    private long experimentId;
    private long groupId;
    private String key;
    private long layerId;

    public DebugKey(String str) {
        this.key = str;
        String[] split = str.split("_");
        if (split != null && split.length >= 6) {
            this.layerId = Utils.toLong(split[1]);
            this.experimentId = Utils.toLong(split[2]);
            this.groupId = Utils.toLong(split[3]);
            this.effectiveTimeInSecond = Utils.toLong(split[4]);
        }
    }

    public String getKey() {
        return this.key;
    }

    public long getLayerId() {
        return this.layerId;
    }

    public long getExperimentId() {
        return this.experimentId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public long getEffectiveTimeInSecond() {
        return this.effectiveTimeInSecond;
    }
}
