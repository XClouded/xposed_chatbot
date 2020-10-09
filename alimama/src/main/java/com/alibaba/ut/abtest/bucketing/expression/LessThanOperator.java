package com.alibaba.ut.abtest.bucketing.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LessThanOperator extends RelationalOperator {
    public boolean apply(double d, double d2) {
        return d < d2;
    }

    public boolean apply(long j, long j2) {
        return j < j2;
    }

    public String getOperatorSymbol() {
        return "$lt";
    }

    public boolean apply(Object obj, Object obj2) throws ExpressionException {
        if (obj == obj2 || obj == null || obj2 == null) {
            return false;
        }
        return super.apply(obj, obj2);
    }

    public boolean apply(String str, String str2) {
        return str.compareTo(str2) < 0;
    }

    public boolean apply(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        return isLess(bigDecimal.compareTo(bigDecimal2));
    }

    public boolean apply(BigInteger bigInteger, BigInteger bigInteger2) {
        return isLess(bigInteger.compareTo(bigInteger2));
    }
}
