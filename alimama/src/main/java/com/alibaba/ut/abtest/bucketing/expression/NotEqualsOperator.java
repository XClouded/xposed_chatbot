package com.alibaba.ut.abtest.bucketing.expression;

public class NotEqualsOperator extends EqualsOperator {
    public String getOperatorSymbol() {
        return "$ne";
    }

    public boolean apply(Object obj, Object obj2) {
        return !super.apply(obj, obj2);
    }
}
