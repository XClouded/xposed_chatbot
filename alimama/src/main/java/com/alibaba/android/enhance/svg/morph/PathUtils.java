package com.alibaba.android.enhance.svg.morph;

import android.util.Pair;
import com.taobao.weex.common.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PathUtils {
    private static final double TAU = 6.283185307179586d;

    PathUtils() {
    }

    static List<Map<String, Double>> arcToBezier(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        ArrayList<List> arrayList = new ArrayList<>();
        if (d5 == 0.0d || d6 == 0.0d) {
            return Collections.emptyList();
        }
        double d10 = (TAU * d7) / 360.0d;
        double sin = Math.sin(d10);
        double cos = Math.cos(d10);
        double d11 = d - d3;
        double d12 = d2 - d4;
        double d13 = ((cos * d11) / 2.0d) + ((sin * d12) / 2.0d);
        double d14 = (((-sin) * d11) / 2.0d) + ((d12 * cos) / 2.0d);
        if (d13 == 0.0d && d14 == 0.0d) {
            return Collections.emptyList();
        }
        double abs = Math.abs(d5);
        double abs2 = Math.abs(d6);
        double pow = (Math.pow(d13, 2.0d) / Math.pow(abs, 2.0d)) + (Math.pow(d14, 2.0d) / Math.pow(abs2, 2.0d));
        if (pow > 1.0d) {
            abs *= Math.sqrt(pow);
            abs2 *= Math.sqrt(pow);
        }
        double d15 = abs2;
        double d16 = abs;
        double d17 = sin;
        List<Double> arcCenter = getArcCenter(d, d2, d3, d4, d16, d15, d8, d9, d17, cos, d13, d14);
        double doubleValue = arcCenter.get(0).doubleValue();
        double doubleValue2 = arcCenter.get(1).doubleValue();
        double doubleValue3 = arcCenter.get(2).doubleValue();
        double doubleValue4 = arcCenter.get(3).doubleValue();
        double d18 = doubleValue2;
        double max = Math.max(Math.ceil(Math.abs(doubleValue4) / 1.5707963267948966d), 1.0d);
        double d19 = doubleValue4 / max;
        double d20 = doubleValue;
        double d21 = doubleValue3;
        for (int i = 0; ((double) i) < max; i++) {
            arrayList.add(approxUnitArc(d21, d19));
            d21 += d19;
        }
        ArrayList arrayList2 = new ArrayList();
        for (List list : arrayList) {
            double d22 = d16;
            double d23 = d15;
            double d24 = cos;
            double d25 = d17;
            double d26 = d20;
            double d27 = d18;
            Pair<Double, Double> mapToEllipse = mapToEllipse((Pair) list.get(0), d22, d23, d24, d25, d26, d27);
            Pair<Double, Double> mapToEllipse2 = mapToEllipse((Pair) list.get(1), d22, d23, d24, d25, d26, d27);
            Pair<Double, Double> mapToEllipse3 = mapToEllipse((Pair) list.get(2), d22, d23, d24, d25, d26, d27);
            HashMap hashMap = new HashMap(6);
            hashMap.put("x1", mapToEllipse.first);
            hashMap.put("y1", mapToEllipse.second);
            hashMap.put("x2", mapToEllipse2.first);
            hashMap.put("y2", mapToEllipse2.second);
            hashMap.put(Constants.Name.X, mapToEllipse3.first);
            hashMap.put(Constants.Name.Y, mapToEllipse3.second);
            arrayList2.add(hashMap);
        }
        return arrayList2;
    }

    private static Pair<Double, Double> mapToEllipse(Pair<Double, Double> pair, double d, double d2, double d3, double d4, double d5, double d6) {
        Pair<Double, Double> pair2 = pair;
        double doubleValue = ((Double) pair2.first).doubleValue() * d;
        double doubleValue2 = ((Double) pair2.second).doubleValue() * d2;
        return new Pair<>(Double.valueOf(((d3 * doubleValue) - (d4 * doubleValue2)) + d5), Double.valueOf((d4 * doubleValue) + (d3 * doubleValue2) + d6));
    }

    private static List<Pair<Double, Double>> approxUnitArc(double d, double d2) {
        double tan = Math.tan(d2 / 4.0d) * 1.3333333333333333d;
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        double d3 = d + d2;
        double cos2 = Math.cos(d3);
        double sin2 = Math.sin(d3);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Pair(Double.valueOf(cos - (sin * tan)), Double.valueOf(sin + (cos * tan))));
        arrayList.add(new Pair(Double.valueOf((sin2 * tan) + cos2), Double.valueOf(sin2 - (tan * cos2))));
        arrayList.add(new Pair(Double.valueOf(cos2), Double.valueOf(sin2)));
        return arrayList;
    }

    private static List<Double> getArcCenter(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        double d13 = d5;
        double d14 = d6;
        double d15 = d11;
        double d16 = d12;
        double pow = Math.pow(d13, 2.0d);
        double pow2 = Math.pow(d14, 2.0d);
        double pow3 = Math.pow(d15, 2.0d);
        double d17 = pow * pow2;
        double pow4 = pow * Math.pow(d16, 2.0d);
        double d18 = pow2 * pow3;
        double d19 = (d17 - pow4) - d18;
        if (d19 < 0.0d) {
            d19 = 0.0d;
        }
        double sqrt = Math.sqrt(d19 / (pow4 + d18));
        double d20 = (double) (d7 == d8 ? -1 : 1);
        Double.isNaN(d20);
        double d21 = sqrt * d20;
        double d22 = ((d21 * d13) / d14) * d16;
        double d23 = ((d21 * (-d14)) / d13) * d15;
        double d24 = ((d10 * d22) - (d9 * d23)) + ((d + d3) / 2.0d);
        double d25 = (d9 * d22) + (d10 * d23) + ((d2 + d4) / 2.0d);
        double d26 = (d15 - d22) / d13;
        double d27 = (d16 - d23) / d14;
        double vectorAngle = vectorAngle(1.0d, 0.0d, d26, d27);
        double vectorAngle2 = vectorAngle(d26, d27, ((-d15) - d22) / d13, ((-d16) - d23) / d14);
        if (d8 == 0.0d && vectorAngle2 > 0.0d) {
            vectorAngle2 -= TAU;
        }
        if (d8 == 1.0d && vectorAngle2 < 0.0d) {
            vectorAngle2 += TAU;
        }
        return Arrays.asList(new Double[]{Double.valueOf(d24), Double.valueOf(d25), Double.valueOf(vectorAngle), Double.valueOf(vectorAngle2)});
    }

    private static double vectorAngle(double d, double d2, double d3, double d4) {
        double d5 = 1.0d;
        double d6 = (d * d4) - (d2 * d3) < 0.0d ? -1.0d : 1.0d;
        double d7 = (d * d) + (d2 * d2);
        double sqrt = ((d * d3) + (d2 * d4)) / (Math.sqrt(d7) * Math.sqrt(d7));
        if (sqrt <= 1.0d) {
            d5 = sqrt;
        }
        if (d5 < -1.0d) {
            d5 = -1.0d;
        }
        return d6 * Math.acos(d5);
    }
}
