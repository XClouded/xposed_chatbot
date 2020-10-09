package com.alibaba.ut.abtest.bucketing.expression;

public class ContainsOperator extends BinaryOperator {
    public String getOperatorSymbol() {
        return "$ct";
    }

    public boolean apply(Object obj, Object obj2) {
        if (obj == null || obj2 == null || !(obj instanceof String)) {
            return false;
        }
        return ExpressionUtils.coerceToString(obj).contains(ExpressionUtils.coerceToString(obj2));
    }
}
