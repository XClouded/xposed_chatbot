package com.alibaba.ut.abtest.internal.bucketing.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Set;

public class ExperimentBetaDevice implements Serializable {
    public static final int TYPE_CROWD = 2;
    public static final int TYPE_USER = 1;
    private static final long serialVersionUID = -3185662541082160249L;
    @JSONField(name = "list")
    public Set<String> data;
    @JSONField(name = "type")
    public int type;
}
