package com.alibaba.ut.abtest.internal.windvane;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Map;

public class WVActivateApiResponseData implements Serializable {
    @JSONField(name = "experimentBucketId")
    private long experimentBucketId;
    @JSONField(name = "experimentId")
    private long experimentId;
    @JSONField(name = "experimentReleaseId")
    private long experimentReleaseId;
    @JSONField(name = "variations")
    private Map<String, Object> variations;

    public Map<String, Object> getVariations() {
        return this.variations;
    }

    public void setVariations(Map<String, Object> map) {
        this.variations = map;
    }

    @Deprecated
    public long getExperimentBucketId() {
        return this.experimentBucketId;
    }

    @Deprecated
    public void setExperimentBucketId(long j) {
        this.experimentBucketId = j;
    }

    @Deprecated
    public long getExperimentId() {
        return this.experimentId;
    }

    @Deprecated
    public void setExperimentId(long j) {
        this.experimentId = j;
    }

    @Deprecated
    public long getExperimentReleaseId() {
        return this.experimentReleaseId;
    }

    @Deprecated
    public void setExperimentReleaseId(long j) {
        this.experimentReleaseId = j;
    }
}
