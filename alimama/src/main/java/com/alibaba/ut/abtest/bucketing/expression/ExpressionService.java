package com.alibaba.ut.abtest.bucketing.expression;

import java.util.Map;

public interface ExpressionService {
    boolean evaluate(Expression expression, Map<String, Object> map);
}
