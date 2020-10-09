package com.alibaba.ut.abtest.bucketing.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EqualsOperator extends BinaryOperator {
    public String getOperatorSymbol() {
        return "$eq";
    }

    public boolean apply(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (ExpressionUtils.isBigDecimal(obj)) {
            return ((BigDecimal) ExpressionUtils.coerceToPrimitiveNumber(obj, BigDecimal.class)).equals((BigDecimal) ExpressionUtils.coerceToPrimitiveNumber(obj2, BigDecimal.class));
        }
        if (ExpressionUtils.isFloatingPointType(obj)) {
            if (ExpressionUtils.coerceToPrimitiveNumber(obj, Double.class).doubleValue() == ExpressionUtils.coerceToPrimitiveNumber(obj2, Double.class).doubleValue()) {
                return true;
            }
            return false;
        } else if (ExpressionUtils.isBigInteger(obj)) {
            return ((BigInteger) ExpressionUtils.coerceToPrimitiveNumber(obj, BigInteger.class)).equals((BigInteger) ExpressionUtils.coerceToPrimitiveNumber(obj2, BigInteger.class));
        } else {
            if (ExpressionUtils.isIntegerType(obj)) {
                if (ExpressionUtils.coerceToPrimitiveNumber(obj, Long.class).longValue() == ExpressionUtils.coerceToPrimitiveNumber(obj2, Long.class).longValue()) {
                    return true;
                }
                return false;
            } else if (obj instanceof Boolean) {
                if (ExpressionUtils.coerceToBoolean(obj).booleanValue() == ExpressionUtils.coerceToBoolean(obj2).booleanValue()) {
                    return true;
                }
                return false;
            } else if (obj instanceof String) {
                return ExpressionUtils.coerceToString(obj).equals(ExpressionUtils.coerceToString(obj2));
            } else {
                try {
                    return obj.equals(obj2);
                } catch (Exception unused) {
                    return Boolean.FALSE.booleanValue();
                }
            }
        }
    }
}
