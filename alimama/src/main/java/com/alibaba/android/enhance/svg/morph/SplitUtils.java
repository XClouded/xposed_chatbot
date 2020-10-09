package com.alibaba.android.enhance.svg.morph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SplitUtils {
    SplitUtils() {
    }

    static void splitCurves(List<List<Double>> list, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Map<String, List<Double>> split = split(list.get(i2), 0.5f);
            list.remove(i2);
            list.add(i2, split.get("left"));
            list.add(i2 + 1, split.get("right"));
            i2 += 2;
            if (i2 >= list.size() - 1) {
                i2 = 0;
            }
        }
    }

    private static Map<String, List<Double>> split(List<Double> list, float f) {
        List<Double> list2 = list;
        double doubleValue = list2.get(0).doubleValue();
        double doubleValue2 = list2.get(2).doubleValue();
        double d = doubleValue2;
        double d2 = doubleValue2;
        double doubleValue3 = list2.get(4).doubleValue();
        double d3 = doubleValue3;
        double d4 = doubleValue3;
        double doubleValue4 = list2.get(6).doubleValue();
        double d5 = doubleValue4;
        double d6 = doubleValue4;
        double doubleValue5 = list2.get(1).doubleValue();
        double doubleValue6 = list2.get(3).doubleValue();
        double doubleValue7 = list2.get(5).doubleValue();
        double d7 = doubleValue7;
        double doubleValue8 = list2.get(7).doubleValue();
        double d8 = doubleValue8;
        HashMap hashMap = new HashMap(2);
        Object obj = "left";
        hashMap.put(obj, split(doubleValue, doubleValue5, d2, doubleValue6, d4, doubleValue7, d6, doubleValue8, f, false));
        hashMap.put("right", split(d5, d8, d3, d7, d, doubleValue6, doubleValue, doubleValue5, 1.0f - f, true));
        return hashMap;
    }

    private static List<Double> split(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, float f, boolean z) {
        double d9 = (double) f;
        Double.isNaN(d9);
        double d10 = ((d3 - d) * d9) + d;
        Double.isNaN(d9);
        double d11 = ((d4 - d2) * d9) + d2;
        Double.isNaN(d9);
        double d12 = ((d5 - d3) * d9) + d3;
        Double.isNaN(d9);
        double d13 = ((d6 - d4) * d9) + d4;
        Double.isNaN(d9);
        Double.isNaN(d9);
        Double.isNaN(d9);
        double d14 = ((d12 - d10) * d9) + d10;
        Double.isNaN(d9);
        double d15 = ((d13 - d11) * d9) + d11;
        Double.isNaN(d9);
        Double.isNaN(d9);
        Double.isNaN(d9);
        double d16 = ((((((((d7 - d5) * d9) + d5) - d12) * d9) + d12) - d14) * d9) + d14;
        Double.isNaN(d9);
        double d17 = ((((((((d8 - d6) * d9) + d6) - d13) * d9) + d13) - d15) * d9) + d15;
        if (z) {
            return Arrays.asList(new Double[]{Double.valueOf(d16), Double.valueOf(d17), Double.valueOf(d14), Double.valueOf(d15), Double.valueOf(d10), Double.valueOf(d11), Double.valueOf(d), Double.valueOf(d2)});
        }
        return Arrays.asList(new Double[]{Double.valueOf(d), Double.valueOf(d2), Double.valueOf(d10), Double.valueOf(d11), Double.valueOf(d14), Double.valueOf(d15), Double.valueOf(d16), Double.valueOf(d17)});
    }
}
