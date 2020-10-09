package com.alibaba.ut.abtest.bucketing.expression;

public class RegularExpressionOperator extends BinaryOperator {
    public String getOperatorSymbol() {
        return "$re";
    }

    public boolean apply(Object obj, Object obj2) {
        if (obj == null || obj2 == null || !(obj instanceof String)) {
            return false;
        }
        return ExpressionUtils.coerceToString(obj).matches(ExpressionUtils.coerceToString(obj2));
    }
}
