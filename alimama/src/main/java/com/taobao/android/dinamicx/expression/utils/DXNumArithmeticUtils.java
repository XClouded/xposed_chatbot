package com.taobao.android.dinamicx.expression.utils;

import com.taobao.android.dinamicx.expression.DXNumberUtil;

public class DXNumArithmeticUtils {
    public static long DEFAULT_VALUE = 0;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_DIV = 4;
    public static final int TYPE_MOD = 5;
    public static final int TYPE_MUL = 3;
    public static final int TYPE_SUB = 2;

    public static Object evalWithArgs(Object[] objArr, int i) {
        double d;
        long j;
        long longValue;
        Object[] objArr2 = objArr;
        if (objArr2 != null) {
            try {
                if (objArr2.length != 0) {
                    int length = objArr2.length;
                    double d2 = 0.0d;
                    long j2 = 0;
                    boolean z = false;
                    for (int i2 = 0; i2 < length; i2++) {
                        Object obj = objArr2[i2];
                        boolean z2 = true;
                        if (obj instanceof String) {
                            String str = (String) obj;
                            if (!z) {
                                if (!DXNumberUtil.hasDigit(str)) {
                                    j = DXNumberUtil.parseLong(str);
                                    d = 0.0d;
                                    z2 = z;
                                    z = z2;
                                }
                            }
                            d = DXNumberUtil.parseDouble(str);
                            j = 0;
                            z = z2;
                        } else {
                            if (!z) {
                                if (!DXNumberUtil.isFloatPointNum(obj)) {
                                    if (!(obj instanceof Integer)) {
                                        if (!(obj instanceof Long)) {
                                            longValue = DEFAULT_VALUE;
                                            d = 0.0d;
                                        }
                                    }
                                    longValue = ((Number) obj).longValue();
                                    d = 0.0d;
                                }
                            }
                            d = ((Number) obj).doubleValue();
                            z = true;
                            j = 0;
                        }
                        if (!z) {
                            if (i2 != 0) {
                                switch (i) {
                                    case 1:
                                        j2 += j;
                                        break;
                                    case 2:
                                        j2 -= j;
                                        break;
                                    case 3:
                                        j2 *= j;
                                        break;
                                    case 4:
                                        if (j != 0) {
                                            j2 /= j;
                                            break;
                                        } else {
                                            return Long.valueOf(DEFAULT_VALUE);
                                        }
                                    case 5:
                                        if (j != 0) {
                                            j2 %= j;
                                            break;
                                        } else {
                                            return Long.valueOf(DEFAULT_VALUE);
                                        }
                                }
                            } else {
                                j2 = j;
                            }
                        } else {
                            if (j2 != 0) {
                                d2 = (double) j2;
                                j2 = 0;
                            }
                            if (i2 != 0) {
                                switch (i) {
                                    case 1:
                                        d2 += d;
                                        break;
                                    case 2:
                                        d2 -= d;
                                        break;
                                    case 3:
                                        d2 *= d;
                                        break;
                                    case 4:
                                        if (d != 0.0d) {
                                            d2 /= d;
                                            break;
                                        } else {
                                            return Long.valueOf(DEFAULT_VALUE);
                                        }
                                    case 5:
                                        if (d != 0.0d) {
                                            d2 %= d;
                                            break;
                                        } else {
                                            return Long.valueOf(DEFAULT_VALUE);
                                        }
                                }
                            } else {
                                d2 = d;
                            }
                        }
                        if (z && (d2 == Double.POSITIVE_INFINITY || d2 == Double.NEGATIVE_INFINITY || Double.NaN == d2)) {
                            return Double.valueOf(0.0d);
                        }
                    }
                    if (z) {
                        return Double.valueOf(d2);
                    }
                    return Long.valueOf(j2);
                }
            } catch (Throwable unused) {
                return Long.valueOf(DEFAULT_VALUE);
            }
        }
        return Long.valueOf(DEFAULT_VALUE);
    }
}
