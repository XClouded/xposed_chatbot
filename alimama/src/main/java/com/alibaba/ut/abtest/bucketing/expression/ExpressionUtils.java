package com.alibaba.ut.abtest.bucketing.expression;

import com.alibaba.ut.abtest.internal.util.LogUtils;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class ExpressionUtils {
    private static final String TAG = "ExpressionUtils";
    private static final Number ZERO = new Integer(0);

    private ExpressionUtils() {
    }

    public static Object coerce(Object obj, Class cls) throws ExpressionException {
        if (cls == String.class) {
            return coerceToString(obj);
        }
        if (isNumberClass(cls)) {
            return coerceToPrimitiveNumber(obj, cls);
        }
        if (cls == Character.class || cls == Character.TYPE) {
            return coerceToCharacter(obj);
        }
        if (cls == Boolean.class || cls == Boolean.TYPE) {
            return coerceToBoolean(obj);
        }
        throw new ExpressionException("不支持的类型");
    }

    static boolean isNumberClass(Class cls) {
        return cls == Byte.class || cls == Byte.TYPE || cls == Short.class || cls == Short.TYPE || cls == Integer.class || cls == Integer.TYPE || cls == Long.class || cls == Long.TYPE || cls == Float.class || cls == Float.TYPE || cls == Double.class || cls == Double.TYPE || cls == BigInteger.class || cls == BigDecimal.class;
    }

    public static String coerceToString(Object obj) throws ExpressionException {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return obj.toString();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
            return "";
        }
    }

    public static Number coerceToPrimitiveNumber(Object obj, Class cls) throws ExpressionException {
        if (obj == null || "".equals(obj)) {
            return coerceToPrimitiveNumber(ZERO, cls);
        }
        if (obj instanceof Character) {
            return coerceToPrimitiveNumber((Number) new Short((short) ((Character) obj).charValue()), cls);
        }
        if (obj instanceof Boolean) {
            return coerceToPrimitiveNumber(ZERO, cls);
        }
        if (obj.getClass() == cls) {
            return (Number) obj;
        }
        if (obj instanceof Number) {
            return coerceToPrimitiveNumber((Number) obj, cls);
        }
        if (obj instanceof String) {
            try {
                return coerceToPrimitiveNumber((String) obj, cls);
            } catch (Exception e) {
                LogUtils.logW(TAG, e.getMessage(), e);
                return coerceToPrimitiveNumber(ZERO, cls);
            }
        } else {
            LogUtils.logW(TAG, "不支持的类型，valueClass=" + obj.getClass().getName());
            return coerceToPrimitiveNumber((Number) 0, cls);
        }
    }

    static Number coerceToPrimitiveNumber(Number number, Class cls) throws ExpressionException {
        if (cls == Byte.class || cls == Byte.TYPE) {
            return new Byte(number.byteValue());
        }
        if (cls == Short.class || cls == Short.TYPE) {
            return new Short(number.shortValue());
        }
        if (cls == Integer.class || cls == Integer.TYPE) {
            return new Integer(number.intValue());
        }
        if (cls == Long.class || cls == Long.TYPE) {
            return new Long(number.longValue());
        }
        if (cls == Float.class || cls == Float.TYPE) {
            return new Float(number.floatValue());
        }
        if (cls == Double.class || cls == Double.TYPE) {
            return new Double(number.doubleValue());
        }
        if (cls == BigInteger.class) {
            if (number instanceof BigDecimal) {
                return ((BigDecimal) number).toBigInteger();
            }
            return BigInteger.valueOf(number.longValue());
        } else if (cls != BigDecimal.class) {
            return new Integer(0);
        } else {
            if (number instanceof BigInteger) {
                return new BigDecimal((BigInteger) number);
            }
            return new BigDecimal(number.doubleValue());
        }
    }

    static Number coerceToPrimitiveNumber(String str, Class cls) throws ExpressionException {
        if (cls == Byte.class || cls == Byte.TYPE) {
            return Byte.valueOf(str);
        }
        if (cls == Short.class || cls == Short.TYPE) {
            return Short.valueOf(str);
        }
        if (cls == Integer.class || cls == Integer.TYPE) {
            return Integer.valueOf(str);
        }
        if (cls == Long.class || cls == Long.TYPE) {
            return Long.valueOf(str);
        }
        if (cls == Float.class || cls == Float.TYPE) {
            return Float.valueOf(str);
        }
        if (cls == Double.class || cls == Double.TYPE) {
            return Double.valueOf(str);
        }
        if (cls == BigInteger.class) {
            return new BigInteger(str);
        }
        if (cls == BigDecimal.class) {
            return new BigDecimal(str);
        }
        return new Integer(0);
    }

    public static Character coerceToCharacter(Object obj) throws ExpressionException {
        if (obj == null || "".equals(obj)) {
            return new Character(0);
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof Boolean) {
            return new Character(0);
        }
        if (obj instanceof Number) {
            return new Character((char) ((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            return new Character(((String) obj).charAt(0));
        }
        return new Character(0);
    }

    public static Boolean coerceToBoolean(Object obj) throws ExpressionException {
        if (obj == null || "".equals(obj)) {
            return Boolean.FALSE;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (!(obj instanceof String)) {
            return Boolean.TRUE;
        }
        try {
            return Boolean.valueOf((String) obj);
        } catch (Exception unused) {
            return Boolean.FALSE;
        }
    }

    public static boolean isBigInteger(Object obj) {
        return obj != null && (obj instanceof BigInteger);
    }

    public static boolean isBigDecimal(Object obj) {
        return obj != null && (obj instanceof BigDecimal);
    }

    public static boolean isFloatingPointType(Object obj) {
        return obj != null && isFloatingPointType((Class) obj.getClass());
    }

    public static boolean isFloatingPointType(Class cls) {
        return cls == Float.class || cls == Float.TYPE || cls == Double.class || cls == Double.TYPE;
    }

    public static boolean isIntegerType(Object obj) {
        return obj != null && isIntegerType((Class) obj.getClass());
    }

    public static boolean isIntegerType(Class cls) {
        return cls == Byte.class || cls == Byte.TYPE || cls == Short.class || cls == Short.TYPE || cls == Character.class || cls == Character.TYPE || cls == Integer.class || cls == Integer.TYPE || cls == Long.class || cls == Long.TYPE;
    }

    public static boolean applyRelationalOperator(Object obj, Object obj2, RelationalOperator relationalOperator) throws ExpressionException {
        if (isBigDecimal(obj)) {
            return relationalOperator.apply((BigDecimal) coerceToPrimitiveNumber(obj, BigDecimal.class), (BigDecimal) coerceToPrimitiveNumber(obj2, BigDecimal.class));
        }
        if (isFloatingPointType(obj)) {
            return relationalOperator.apply(coerceToPrimitiveNumber(obj, Double.class).doubleValue(), coerceToPrimitiveNumber(obj2, Double.class).doubleValue());
        }
        if (isBigInteger(obj)) {
            return relationalOperator.apply((BigInteger) coerceToPrimitiveNumber(obj, BigInteger.class), (BigInteger) coerceToPrimitiveNumber(obj2, BigInteger.class));
        }
        if (isIntegerType(obj)) {
            return relationalOperator.apply(coerceToPrimitiveNumber(obj, Long.class).longValue(), coerceToPrimitiveNumber(obj2, Long.class).longValue());
        }
        if (obj instanceof String) {
            return relationalOperator.apply(coerceToString(obj), coerceToString(obj2));
        }
        if (obj instanceof Comparable) {
            try {
                int compareTo = ((Comparable) obj).compareTo(obj2);
                return relationalOperator.apply((long) compareTo, (long) (-compareTo));
            } catch (Exception e) {
                LogUtils.logW(TAG, e.getMessage(), e);
                return false;
            }
        } else if (obj2 instanceof Comparable) {
            try {
                int compareTo2 = ((Comparable) obj2).compareTo(obj);
                return relationalOperator.apply((long) (-compareTo2), (long) compareTo2);
            } catch (Exception e2) {
                LogUtils.logW(TAG, e2.getMessage(), e2);
                return false;
            }
        } else {
            LogUtils.logW(TAG, "不支持的类型，OperatorSymbol=" + relationalOperator.getOperatorSymbol() + ", leftClass=" + obj.getClass().getName() + ", rightClass=" + obj2.getClass().getName());
            return false;
        }
    }
}
