package com.alibaba.ut.abtest.bucketing.expression;

import java.util.Map;

public class ExpressionServiceImpl implements ExpressionService {
    private ExpressionEvaluator mExpressionEvaluator = new ExpressionEvaluator();

    public boolean evaluate(Expression expression, Map<String, Object> map) {
        return expression == null || this.mExpressionEvaluator.evaluate(expression, map);
    }
}
