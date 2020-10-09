package com.alibaba.ut.abtest.bucketing.expression;

public abstract class BinaryOperator {
    public abstract boolean apply(Object obj, Object obj2);

    public abstract String getOperatorSymbol();
}
