package com.taobao.android.dinamic.expressionv2;

import java.math.BigDecimal;

public class NumberUtil {
    public static boolean parseBoolean(String str) {
        return str != null && !str.equals("false") && !str.equalsIgnoreCase("0") && !str.isEmpty();
    }

    public static boolean toBoolean(Object obj) {
        if (obj != null) {
            return toBooleanObj(obj).booleanValue();
        }
        return false;
    }

    public static Boolean toBooleanObj(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.valueOf((String) obj);
        }
        return null;
    }

    public static int toInteger(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                if ("".equals(obj)) {
                    return 0;
                }
                return Integer.parseInt((String) obj);
            } else if (obj instanceof Character) {
                return ((Character) obj).charValue();
            } else {
                if (obj instanceof Boolean) {
                    throw new IllegalArgumentException("Boolean->Integer coercion exception");
                } else if (obj instanceof Number) {
                    return ((Number) obj).intValue();
                }
            }
        }
        return 0;
    }

    public static long toLong(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                if ("".equals(obj)) {
                    return 0;
                }
                return Long.parseLong((String) obj);
            } else if (obj instanceof Character) {
                return (long) ((Character) obj).charValue();
            } else {
                if (!(obj instanceof Boolean) && (obj instanceof Number)) {
                    return ((Number) obj).longValue();
                }
            }
        }
        return 0;
    }

    public static BigDecimal toBigDecimal(Object obj) {
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj == null) {
            throw new IllegalArgumentException("BigDecimal coercion exception. arg is null");
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str.trim())) {
                return BigDecimal.valueOf(0);
            }
            return new BigDecimal(str);
        } else if (obj instanceof Number) {
            return new BigDecimal(obj.toString());
        } else {
            if (obj instanceof Character) {
                return new BigDecimal(((Character) obj).charValue());
            }
            return null;
        }
    }

    public static double toDouble(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                String str = (String) obj;
                if ("".equals(str.trim())) {
                    return 0.0d;
                }
                return Double.parseDouble(str);
            } else if (obj instanceof Character) {
                return (double) ((Character) obj).charValue();
            } else {
                if (obj instanceof Number) {
                    return toDouble(obj);
                }
                if (obj instanceof Boolean) {
                    throw new IllegalArgumentException("Boolean->Double coercion exception");
                }
            }
        }
        return 0.0d;
    }

    public static boolean isFloatingPointNumber(Object obj) {
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return true;
        }
        if (!(obj instanceof String)) {
            return false;
        }
        String str = (String) obj;
        if (str.indexOf(46) == -1 && str.indexOf(101) == -1 && str.indexOf(69) == -1) {
            return false;
        }
        return true;
    }
}
