package com.ali.alihadeviceevaluator.util;

import java.math.BigDecimal;

public class BigNumUtils {
    public static double mul(double d, double d2) {
        return new BigDecimal(Double.toString(d)).multiply(new BigDecimal(Double.toString(d2))).doubleValue();
    }

    public static double div(double d, double d2, int i) {
        return new BigDecimal(Double.toString(d)).divide(new BigDecimal(Double.toString(d2)), i, 1).doubleValue();
    }

    public static float div(float f, float f2, int i) {
        return new BigDecimal(Float.toString(f)).divide(new BigDecimal(Float.toString(f2)), i, 1).floatValue();
    }
}
