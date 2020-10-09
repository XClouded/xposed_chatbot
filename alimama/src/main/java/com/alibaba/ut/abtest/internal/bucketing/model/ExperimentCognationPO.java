package com.alibaba.ut.abtest.internal.bucketing.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public class ExperimentCognationPO implements Serializable {
    public static final int ROUTING_TYPE_USERID = 2;
    public static final int ROUTING_TYPE_UTDID = 1;
    public static final String TYPE_DOMAIN = "domain";
    public static final String TYPE_LAUNCH_LAYER = "launch_layer";
    public static final String TYPE_LAYER = "layer";
    public static final String TYPE_ROOT_DOMAIN = "root_domain";
    private static final long serialVersionUID = 2344647453768709254L;
    @JSONField(name = "node")
    public ExperimentCognationPO child;
    @JSONField(name = "code")
    public String code;
    @JSONField(name = "featureCondition")
    public String featureCondition;
    @JSONField(name = "id")
    public long id;
    @JSONField(name = "isRouting")
    public boolean needRouting = true;
    @JSONField(name = "ratioRanges")
    public int[][] ratioRange;
    @JSONField(name = "randomFactor")
    public String routingFactor;
    @JSONField(name = "routingType")
    public int routingType;
    @JSONField(name = "type")
    public String type;
}
