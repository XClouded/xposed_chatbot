package com.alibaba.android.bindingx.plugin.android.model;

import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import java.util.List;
import java.util.Map;

public class BindingXSpec {
    public String anchor;
    public String eventType;
    public ExpressionPair exitExpression;
    public List<BindingXPropSpec> expressionProps;
    public Map<String, Object> options;
}
