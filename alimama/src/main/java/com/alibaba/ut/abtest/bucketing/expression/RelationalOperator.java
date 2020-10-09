package com.alibaba.ut.abtest.bucketing.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class RelationalOperator extends BinaryOperator {
    public abstract boolean apply(double d, double d2);

    public abstract boolean apply(long j, long j2);

    public abstract boolean apply(String str, String str2);

    public abstract boolean apply(BigDecimal bigDecimal, BigDecimal bigDecimal2);

    public abstract boolean apply(BigInteger bigInteger, BigInteger bigInteger2);

    /* access modifiers changed from: protected */
    public boolean isEqual(int i) {
        return i == 0;
    }

    /* access modifiers changed from: protected */
    public boolean isGreater(int i) {
        return i > 0;
    }

    /* access modifiers changed from: protected */
    public boolean isLess(int i) {
        return i < 0;
    }

    public boolean apply(Object obj, Object obj2) throws ExpressionException {
        return ExpressionUtils.applyRelationalOperator(obj, obj2, this);
    }
}
