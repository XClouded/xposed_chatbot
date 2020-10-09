package com.alibaba.ut.abtest.bucketing.expression;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;

public class ExpressionCriterion {
    @JSONField(name = "criterions")
    public List<ExpressionCriterion> criterions;
    @JSONField(name = "fieldName")
    public String name;
    @JSONField(name = "type")
    public String operator;
    @JSONField(name = "fieldValue")
    public String value;
}
