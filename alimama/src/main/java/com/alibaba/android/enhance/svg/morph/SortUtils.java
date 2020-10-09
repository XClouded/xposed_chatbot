package com.alibaba.android.enhance.svg.morph;

import androidx.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class SortUtils {
    SortUtils() {
    }

    static void sort(List<List<List<Double>>> list, List<List<List<Double>>> list2) {
        ArrayList<ArrayList<Integer>> permuteNum = permuteNum(list.size());
        ArrayList arrayList = new ArrayList();
        Iterator<ArrayList<Integer>> it = permuteNum.iterator();
        while (it.hasNext()) {
            ArrayList next = it.next();
            double d = 0.0d;
            Iterator it2 = next.iterator();
            while (it2.hasNext()) {
                Integer num = (Integer) it2.next();
                d += boxDistance(shapeBox(list.get(num.intValue())), shapeBox(list2.get(num.intValue())));
            }
            HashMap hashMap = new HashMap();
            hashMap.put("index", next);
            hashMap.put("distance", Double.valueOf(d));
            arrayList.add(hashMap);
        }
        Collections.sort(arrayList, new Comparator<Map>() {
            public int compare(Map map, Map map2) {
                return (int) (((Double) map.get("distance")).doubleValue() - ((Double) map2.get("distance")).doubleValue());
            }
        });
        ArrayList arrayList2 = new ArrayList();
        Iterator it3 = ((ArrayList) ((Map) arrayList.get(0)).get("index")).iterator();
        while (it3.hasNext()) {
            arrayList2.add(list.get(((Integer) it3.next()).intValue()));
        }
        list.clear();
        list.addAll(arrayList2);
    }

    static List<List<Double>> sortCurves(List<List<Double>> list, List<List<Double>> list2) {
        List<List<Integer>> permuteCurveNum = permuteCurveNum(list.size());
        ArrayList arrayList = new ArrayList();
        Iterator<List<Integer>> it = permuteCurveNum.iterator();
        while (true) {
            int i = 0;
            if (!it.hasNext()) {
                break;
            }
            List<Integer> next = it.next();
            double d = 0.0d;
            for (Integer intValue : next) {
                d += curveDistance(list.get(intValue.intValue()), list2.get(i));
                i++;
            }
            HashMap hashMap = new HashMap(2);
            hashMap.put("index", next);
            hashMap.put("distance", Double.valueOf(d));
            arrayList.add(hashMap);
        }
        Collections.sort(arrayList, new Comparator<Map>() {
            public int compare(Map map, Map map2) {
                return (int) (((Double) map.get("distance")).doubleValue() - ((Double) map2.get("distance")).doubleValue());
            }
        });
        ArrayList arrayList2 = new ArrayList();
        for (Integer intValue2 : (List) ((Map) arrayList.get(0)).get("index")) {
            arrayList2.add(list.get(intValue2.intValue()));
        }
        return arrayList2;
    }

    @VisibleForTesting
    static double curveDistance(List<Double> list, List<Double> list2) {
        List<Double> list3 = list;
        List<Double> list4 = list2;
        double doubleValue = list3.get(0).doubleValue();
        double doubleValue2 = list3.get(2).doubleValue();
        double doubleValue3 = list3.get(4).doubleValue();
        double doubleValue4 = list3.get(6).doubleValue();
        double doubleValue5 = list3.get(1).doubleValue();
        double doubleValue6 = list3.get(3).doubleValue();
        double doubleValue7 = list3.get(5).doubleValue();
        double doubleValue8 = list3.get(7).doubleValue();
        double doubleValue9 = list4.get(0).doubleValue();
        double doubleValue10 = list4.get(2).doubleValue();
        double doubleValue11 = list4.get(4).doubleValue();
        double doubleValue12 = list4.get(6).doubleValue();
        return Math.sqrt(Math.pow(doubleValue9 - doubleValue, 2.0d) + Math.pow(list4.get(1).doubleValue() - doubleValue5, 2.0d)) + Math.sqrt(Math.pow(doubleValue10 - doubleValue2, 2.0d) + Math.pow(list4.get(3).doubleValue() - doubleValue6, 2.0d)) + Math.sqrt(Math.pow(doubleValue11 - doubleValue3, 2.0d) + Math.pow(list4.get(5).doubleValue() - doubleValue7, 2.0d)) + Math.sqrt(Math.pow(doubleValue12 - doubleValue4, 2.0d) + Math.pow(list4.get(7).doubleValue() - doubleValue8, 2.0d));
    }

    @VisibleForTesting
    static List<List<Integer>> permuteCurveNum(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < i; i2++) {
            ArrayList arrayList2 = new ArrayList();
            for (int i3 = 0; i3 < i; i3++) {
                arrayList2.add(0);
            }
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = i4 + i2;
                if (i5 > i - 1) {
                    i5 -= i;
                }
                arrayList2.set(i5, Integer.valueOf(i4));
            }
            arrayList.add(arrayList2);
        }
        return arrayList;
    }

    @VisibleForTesting
    static List<Double> shapeBox(List<List<Double>> list) {
        List<List<Double>> list2 = list;
        int i = 0;
        double doubleValue = ((Double) list2.get(0).get(0)).doubleValue();
        int i2 = 1;
        double doubleValue2 = ((Double) list2.get(0).get(1)).doubleValue();
        Iterator<List<Double>> it = list.iterator();
        double d = doubleValue;
        double d2 = doubleValue2;
        while (it.hasNext()) {
            List next = it.next();
            double doubleValue3 = ((Double) next.get(i)).doubleValue();
            double doubleValue4 = ((Double) next.get(i2)).doubleValue();
            double doubleValue5 = ((Double) next.get(2)).doubleValue();
            double d3 = d2;
            double doubleValue6 = ((Double) next.get(3)).doubleValue();
            double doubleValue7 = ((Double) next.get(4)).doubleValue();
            double doubleValue8 = ((Double) next.get(5)).doubleValue();
            double d4 = doubleValue4;
            double doubleValue9 = ((Double) next.get(6)).doubleValue();
            double doubleValue10 = ((Double) next.get(7)).doubleValue();
            Iterator<List<Double>> it2 = it;
            double min = Math.min(doubleValue, Math.min(doubleValue3, Math.min(doubleValue5, Math.min(doubleValue7, doubleValue9))));
            double d5 = doubleValue6;
            double min2 = Math.min(d5, Math.min(doubleValue8, doubleValue10));
            double d6 = d5;
            double d7 = d4;
            double d8 = d7;
            double min3 = Math.min(doubleValue2, Math.min(d7, min2));
            double max = Math.max(d, Math.max(doubleValue3, Math.max(doubleValue5, Math.max(doubleValue7, doubleValue9))));
            d2 = Math.max(d3, Math.max(d8, Math.max(d6, Math.max(doubleValue8, doubleValue10))));
            d = max;
            doubleValue = min;
            i2 = 1;
            doubleValue2 = min3;
            it = it2;
            i = 0;
        }
        return Arrays.asList(new Double[]{Double.valueOf(doubleValue), Double.valueOf(doubleValue2), Double.valueOf(d), Double.valueOf(d2)});
    }

    @VisibleForTesting
    static double boxDistance(List<Double> list, List<Double> list2) {
        return Math.sqrt(Math.pow(list.get(0).doubleValue() - list2.get(0).doubleValue(), 2.0d) + Math.pow(list.get(1).doubleValue() - list2.get(1).doubleValue(), 2.0d)) + Math.sqrt(Math.pow(list.get(2).doubleValue() - list2.get(2).doubleValue(), 2.0d) + Math.pow(list.get(3).doubleValue() - list2.get(3).doubleValue(), 2.0d));
    }

    @VisibleForTesting
    static ArrayList<ArrayList<Integer>> permuteNum(int i) {
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = i2;
        }
        return permute(iArr);
    }

    private static ArrayList<ArrayList<Integer>> permute(int[] iArr) {
        ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>();
        permute(iArr, 0, arrayList);
        return arrayList;
    }

    private static void permute(int[] iArr, int i, ArrayList<ArrayList<Integer>> arrayList) {
        if (i >= iArr.length) {
            arrayList.add(convertArrayToList(iArr));
        }
        for (int i2 = i; i2 <= iArr.length - 1; i2++) {
            swap(iArr, i, i2);
            permute(iArr, i + 1, arrayList);
            swap(iArr, i, i2);
        }
    }

    private static ArrayList<Integer> convertArrayToList(int[] iArr) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int valueOf : iArr) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        return arrayList;
    }

    private static void swap(int[] iArr, int i, int i2) {
        int i3 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = i3;
    }
}
