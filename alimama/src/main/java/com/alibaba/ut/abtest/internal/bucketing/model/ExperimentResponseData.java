package com.alibaba.ut.abtest.internal.bucketing.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.List;

public class ExperimentResponseData implements Serializable {
    private static final long serialVersionUID = -3645512069349237184L;
    @JSONField(name = "groups")
    public List<ExperimentGroupPO> groups;
    @JSONField(name = "sign")
    public String sign;
    @JSONField(name = "version")
    public long version;
}
