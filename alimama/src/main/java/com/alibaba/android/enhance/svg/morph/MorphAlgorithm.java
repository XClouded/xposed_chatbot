package com.alibaba.android.enhance.svg.morph;

import android.graphics.Path;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import androidx.core.util.Pair;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MorphAlgorithm {
    private static final int MIN_CURVES_COUNT = 30;
    private static final PathParser PATH_PARSER = new PathParser();
    private static final PathCacheManager mCacheManager = new PathCacheManager(20);

    public static Path morph(String str, String str2, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        float max = Math.max(0.0f, Math.min(1.0f, f));
        long currentTimeMillis = System.currentTimeMillis();
        Path morphImpl = morphImpl(str, str2, max);
        if (WXEnvironment.isApkDebugable()) {
            Log.e(SVGPlugin.TAG, "path morph algorithm elapsed " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
        return morphImpl;
    }

    private static String normalizedPath(String str) {
        if (str.startsWith(DXBindingXConstant.SINGLE_QUOTE) || str.startsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    private static Path morphImpl(@NonNull String str, @NonNull String str2, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        List<List<List<Double>>> list;
        List<List<List<Double>>> list2;
        String normalizedPath = normalizedPath(str);
        String normalizedPath2 = normalizedPath(str2);
        Pair<List, List> pair = mCacheManager.get(normalizedPath, normalizedPath2);
        if (pair == null) {
            if (WXEnvironment.isApkDebugable()) {
                Log.d(SVGPlugin.TAG, "[morph] cache loss, fallback");
            }
            List<List<List<Double>>> path2Shapes = path2Shapes(normalizedPath);
            list2 = path2Shapes(normalizedPath2);
            preProcessing(path2Shapes, list2);
            mCacheManager.put(normalizedPath, normalizedPath2, Pair.create(path2Shapes, list2));
            list = path2Shapes;
        } else {
            list = (List) pair.first;
            list2 = (List) pair.second;
        }
        return shapes2Path(lerp(list, list2, f));
    }

    private static String shapes2PathString(@NonNull List<List<List<Double>>> list) {
        StringBuilder sb = new StringBuilder();
        for (List next : list) {
            for (int i = 0; i < next.size(); i++) {
                List list2 = (List) next.get(i);
                if (i == 0) {
                    sb.append("M");
                    for (int i2 = 0; i2 < list2.size(); i2++) {
                        if (i2 == 2) {
                            sb.append("C");
                        }
                        sb.append(list2.get(i2));
                        sb.append(Operators.SPACE_STR);
                    }
                } else {
                    for (int i3 = 0; i3 < list2.size(); i3++) {
                        if (i3 == 2) {
                            sb.append("C");
                            sb.append(list2.get(i3));
                            sb.append(Operators.SPACE_STR);
                        } else if (i3 > 2) {
                            sb.append(list2.get(i3));
                            sb.append(Operators.SPACE_STR);
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private static Path shapes2Path(List<List<List<Double>>> list) {
        float f = SVGPlugin.GlobalVariablesHolder.DEVICE_SCALE;
        Path path = new Path();
        Iterator<List<List<Double>>> it = list.iterator();
        while (it.hasNext()) {
            List next = it.next();
            if (next.size() > 0) {
                path.moveTo(((float) ((Double) ((List) next.get(0)).get(0)).doubleValue()) * f, ((float) ((Double) ((List) next.get(0)).get(1)).doubleValue()) * f);
                Iterator it2 = next.iterator();
                while (it2.hasNext()) {
                    List list2 = (List) it2.next();
                    double doubleValue = ((Double) list2.get(2)).doubleValue();
                    double d = (double) f;
                    Double.isNaN(d);
                    double doubleValue2 = ((Double) list2.get(3)).doubleValue();
                    Double.isNaN(d);
                    double doubleValue3 = ((Double) list2.get(4)).doubleValue();
                    Double.isNaN(d);
                    double doubleValue4 = ((Double) list2.get(5)).doubleValue();
                    Double.isNaN(d);
                    double doubleValue5 = ((Double) list2.get(6)).doubleValue();
                    Double.isNaN(d);
                    Iterator<List<List<Double>>> it3 = it;
                    Iterator it4 = it2;
                    double d2 = doubleValue5 * d;
                    double doubleValue6 = ((Double) list2.get(7)).doubleValue();
                    Double.isNaN(d);
                    float f2 = (float) (d * doubleValue6);
                    Path path2 = path;
                    path2.cubicTo((float) (doubleValue * d), (float) (doubleValue2 * d), (float) (doubleValue3 * d), (float) (doubleValue4 * d), (float) d2, f2);
                    it = it3;
                    it2 = it4;
                }
            }
            it = it;
        }
        return path;
    }

    private static void preProcessing(List<List<List<Double>>> list, List<List<List<Double>>> list2) {
        int size = list.size();
        int size2 = list2.size();
        if (size > size2) {
            subShapes(list2, size - size2);
        } else if (size < size2) {
            upShapes(list, size2 - size);
        }
        SortUtils.sort(list, list2);
        for (int i = 0; i < list.size(); i++) {
            List list3 = list.get(i);
            int size3 = list3.size();
            int size4 = list2.get(i).size();
            if (size3 > size4) {
                if (size3 < 30) {
                    SplitUtils.splitCurves(list3, 30 - size3);
                    SplitUtils.splitCurves(list2.get(i), 30 - size4);
                } else {
                    SplitUtils.splitCurves(list2.get(i), size3 - size4);
                }
            } else if (size3 < size4) {
                if (size4 < 30) {
                    SplitUtils.splitCurves(list3, 30 - size3);
                    SplitUtils.splitCurves(list2.get(i), 30 - size4);
                } else {
                    SplitUtils.splitCurves(list3, size4 - size3);
                }
            }
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            list.set(i2, SortUtils.sortCurves(list.get(i2), list2.get(i2)));
        }
    }

    private static void subShapes(List<List<List<Double>>> list, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            List list2 = list.get(list.size() - 1);
            LinkedList linkedList = new LinkedList();
            double doubleValue = ((Double) ((List) list2.get(0)).get(0)).doubleValue();
            double doubleValue2 = ((Double) ((List) list2.get(0)).get(1)).doubleValue();
            for (int i3 = 0; i3 < list2.size(); i3++) {
                LinkedList linkedList2 = new LinkedList();
                Collections.addAll(linkedList2, new Double[]{Double.valueOf(doubleValue), Double.valueOf(doubleValue2), Double.valueOf(doubleValue), Double.valueOf(doubleValue2), Double.valueOf(doubleValue), Double.valueOf(doubleValue2), Double.valueOf(doubleValue), Double.valueOf(doubleValue2)});
                linkedList.add(linkedList2);
            }
            list.add(linkedList);
        }
    }

    private static void upShapes(List<List<List<Double>>> list, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            LinkedList linkedList = new LinkedList();
            for (List linkedList2 : list.get(list.size() - 1)) {
                linkedList.add(new LinkedList(linkedList2));
            }
            list.add(linkedList);
        }
    }

    private static List<List<List<Double>>> lerp(List<List<List<Double>>> list, List<List<List<Double>>> list2, float f) {
        if (list == null || list2 == null || list.size() != list2.size()) {
            WXLogUtils.e(SVGPlugin.TAG, "lerp failed");
            return list;
        }
        long currentTimeMillis = System.currentTimeMillis();
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < list.size(); i++) {
            List list3 = list.get(i);
            LinkedList linkedList2 = new LinkedList();
            for (int i2 = 0; i2 < list3.size(); i2++) {
                linkedList2.add(lerpCurve((List) list3.get(i2), (List) list2.get(i).get(i2), f));
            }
            linkedList.add(linkedList2);
        }
        if (WXEnvironment.isApkDebugable()) {
            Log.e(SVGPlugin.TAG, "lerp >>>>>" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
        return linkedList;
    }

    private static List<Double> lerpCurve(List<Double> list, List<Double> list2, float f) {
        List<Double> list3 = list;
        List<Double> list4 = list2;
        LinkedList linkedList = new LinkedList();
        double[] lerpPoints = lerpPoints(list3.get(0).doubleValue(), list3.get(1).doubleValue(), list4.get(0).doubleValue(), list4.get(1).doubleValue(), f);
        linkedList.add(Double.valueOf(lerpPoints[0]));
        linkedList.add(Double.valueOf(lerpPoints[1]));
        float f2 = f;
        double[] lerpPoints2 = lerpPoints(list3.get(2).doubleValue(), list3.get(3).doubleValue(), list4.get(2).doubleValue(), list4.get(3).doubleValue(), f2);
        linkedList.add(Double.valueOf(lerpPoints2[0]));
        linkedList.add(Double.valueOf(lerpPoints2[1]));
        double[] lerpPoints3 = lerpPoints(list3.get(4).doubleValue(), list3.get(5).doubleValue(), list4.get(4).doubleValue(), list4.get(5).doubleValue(), f2);
        linkedList.add(Double.valueOf(lerpPoints3[0]));
        linkedList.add(Double.valueOf(lerpPoints3[1]));
        double[] lerpPoints4 = lerpPoints(list3.get(6).doubleValue(), list3.get(7).doubleValue(), list4.get(6).doubleValue(), list4.get(7).doubleValue(), f2);
        linkedList.add(Double.valueOf(lerpPoints4[0]));
        linkedList.add(Double.valueOf(lerpPoints4[1]));
        return linkedList;
    }

    private static double[] lerpPoints(double d, double d2, double d3, double d4, float f) {
        double d5 = (double) f;
        Double.isNaN(d5);
        Double.isNaN(d5);
        return new double[]{d + ((d3 - d) * d5), d2 + ((d4 - d2) * d5)};
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x0bbe, code lost:
        r25 = r6;
        r27 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x0c4c, code lost:
        r5 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x0c4d, code lost:
        r4 = r4 + 1;
        r6 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0314, code lost:
        r29 = r19;
        r31 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0375, code lost:
        r27 = r6;
        r25 = r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.List<java.util.List<java.util.List<java.lang.Double>>> path2Shapes(@androidx.annotation.NonNull java.lang.String r34) {
        /*
            com.alibaba.android.enhance.svg.morph.MorphAlgorithm$PathParser r0 = PATH_PARSER
            r1 = r34
            java.util.List r0 = r0.parse(r1)
            int r1 = r0.size()
            java.util.LinkedList r2 = new java.util.LinkedList
            r2.<init>()
            r4 = 0
            r6 = 0
            r25 = r4
            r27 = r25
            r29 = r27
            r31 = r29
            r4 = 0
            r5 = 0
        L_0x001e:
            if (r4 >= r1) goto L_0x0c52
            java.lang.Object r7 = r0.get(r4)
            r15 = r7
            java.util.List r15 = (java.util.List) r15
            java.lang.Object r7 = r15.get(r6)
            java.lang.String r7 = (java.lang.String) r7
            if (r4 <= 0) goto L_0x0038
            int r8 = r4 + -1
            java.lang.Object r8 = r0.get(r8)
            java.util.List r8 = (java.util.List) r8
            goto L_0x0039
        L_0x0038:
            r8 = 0
        L_0x0039:
            r9 = -1
            int r10 = r7.hashCode()
            r13 = 8
            r3 = 4
            r14 = 3
            r11 = 2
            r12 = 1
            switch(r10) {
                case 65: goto L_0x011d;
                case 67: goto L_0x0112;
                case 72: goto L_0x0108;
                case 76: goto L_0x00fe;
                case 77: goto L_0x00f4;
                case 81: goto L_0x00e9;
                case 83: goto L_0x00de;
                case 84: goto L_0x00d3;
                case 86: goto L_0x00c9;
                case 90: goto L_0x00be;
                case 97: goto L_0x00b2;
                case 99: goto L_0x00a6;
                case 104: goto L_0x009b;
                case 108: goto L_0x0090;
                case 109: goto L_0x0085;
                case 113: goto L_0x0079;
                case 115: goto L_0x006d;
                case 116: goto L_0x0061;
                case 118: goto L_0x0056;
                case 122: goto L_0x0049;
                default: goto L_0x0047;
            }
        L_0x0047:
            goto L_0x0128
        L_0x0049:
            java.lang.String r10 = "z"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 18
            goto L_0x0129
        L_0x0056:
            java.lang.String r10 = "v"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 6
            goto L_0x0129
        L_0x0061:
            java.lang.String r10 = "t"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 16
            goto L_0x0129
        L_0x006d:
            java.lang.String r10 = "s"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 10
            goto L_0x0129
        L_0x0079:
            java.lang.String r10 = "q"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 14
            goto L_0x0129
        L_0x0085:
            java.lang.String r10 = "m"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 0
            goto L_0x0129
        L_0x0090:
            java.lang.String r10 = "l"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 2
            goto L_0x0129
        L_0x009b:
            java.lang.String r10 = "h"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 4
            goto L_0x0129
        L_0x00a6:
            java.lang.String r10 = "c"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 8
            goto L_0x0129
        L_0x00b2:
            java.lang.String r10 = "a"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 12
            goto L_0x0129
        L_0x00be:
            java.lang.String r10 = "Z"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 19
            goto L_0x0129
        L_0x00c9:
            java.lang.String r10 = "V"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 7
            goto L_0x0129
        L_0x00d3:
            java.lang.String r10 = "T"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 17
            goto L_0x0129
        L_0x00de:
            java.lang.String r10 = "S"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 11
            goto L_0x0129
        L_0x00e9:
            java.lang.String r10 = "Q"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 15
            goto L_0x0129
        L_0x00f4:
            java.lang.String r10 = "M"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 1
            goto L_0x0129
        L_0x00fe:
            java.lang.String r10 = "L"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 3
            goto L_0x0129
        L_0x0108:
            java.lang.String r10 = "H"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 5
            goto L_0x0129
        L_0x0112:
            java.lang.String r10 = "C"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 9
            goto L_0x0129
        L_0x011d:
            java.lang.String r10 = "A"
            boolean r7 = r7.equals(r10)
            if (r7 == 0) goto L_0x0128
            r7 = 13
            goto L_0x0129
        L_0x0128:
            r7 = -1
        L_0x0129:
            switch(r7) {
                case 0: goto L_0x0c28;
                case 1: goto L_0x0c03;
                case 2: goto L_0x0bc5;
                case 3: goto L_0x0b4d;
                case 4: goto L_0x0b13;
                case 5: goto L_0x0ada;
                case 6: goto L_0x0a9f;
                case 7: goto L_0x0a66;
                case 8: goto L_0x09e3;
                case 9: goto L_0x0970;
                case 10: goto L_0x0833;
                case 11: goto L_0x0706;
                case 12: goto L_0x0574;
                case 13: goto L_0x03dd;
                case 14: goto L_0x037b;
                case 15: goto L_0x0323;
                case 16: goto L_0x0244;
                case 17: goto L_0x0170;
                case 18: goto L_0x012e;
                case 19: goto L_0x012e;
                default: goto L_0x012c;
            }
        L_0x012c:
            goto L_0x0bc2
        L_0x012e:
            if (r5 == 0) goto L_0x0bc2
            int r7 = r5.size()
            if (r7 <= 0) goto L_0x0bc2
            java.lang.Object r7 = r5.get(r6)
            java.util.List r7 = (java.util.List) r7
            java.lang.Object r7 = r7.get(r6)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            java.lang.Object r9 = r5.get(r6)
            java.util.List r9 = (java.util.List) r9
            java.lang.Object r9 = r9.get(r12)
            java.lang.Double r9 = (java.lang.Double) r9
            double r9 = r9.doubleValue()
            double[] r13 = new double[r13]
            r13[r6] = r27
            r13[r12] = r25
            r13[r11] = r7
            r13[r14] = r9
            r13[r3] = r7
            r11 = 5
            r13[r11] = r9
            r3 = 6
            r13[r3] = r7
            r3 = 7
            r13[r3] = r9
            addCurve(r5, r13)
            goto L_0x0bc2
        L_0x0170:
            if (r8 == 0) goto L_0x023b
            java.lang.String r7 = "q"
            java.lang.Object r9 = r8.get(r6)
            java.lang.String r9 = (java.lang.String) r9
            boolean r7 = r7.equalsIgnoreCase(r9)
            if (r7 == 0) goto L_0x01dd
            java.lang.Object r7 = r8.get(r14)
            java.lang.Double r7 = (java.lang.Double) r7
            double r9 = r7.doubleValue()
            double r9 = r27 + r9
            java.lang.Object r7 = r8.get(r12)
            java.lang.Double r7 = (java.lang.Double) r7
            double r13 = r7.doubleValue()
            double r19 = r9 - r13
            java.lang.Object r3 = r8.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            double r9 = r25 + r9
            java.lang.Object r3 = r8.get(r11)
            java.lang.Double r3 = (java.lang.Double) r3
            double r7 = r3.doubleValue()
            double r21 = r9 - r7
            java.lang.Object r3 = r15.get(r12)
            java.lang.Double r3 = (java.lang.Double) r3
            double r16 = r3.doubleValue()
            java.lang.Object r3 = r15.get(r11)
            java.lang.Double r3 = (java.lang.Double) r3
            double r23 = r3.doubleValue()
            r7 = r27
            r9 = r25
            r3 = 1
            r13 = 2
            r11 = r19
            r13 = r21
            r3 = r15
            r15 = r16
            r17 = r23
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            r6 = 2
        L_0x01db:
            r7 = 1
            goto L_0x0221
        L_0x01dd:
            r3 = r15
            java.lang.String r7 = "t"
            java.lang.Object r8 = r8.get(r6)
            java.lang.String r8 = (java.lang.String) r8
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x021b
            double r7 = r27 + r27
            double r19 = r7 - r29
            double r7 = r25 + r25
            double r21 = r7 - r31
            r7 = 1
            java.lang.Object r8 = r3.get(r7)
            java.lang.Double r8 = (java.lang.Double) r8
            double r15 = r8.doubleValue()
            r13 = 2
            java.lang.Object r7 = r3.get(r13)
            java.lang.Double r7 = (java.lang.Double) r7
            double r17 = r7.doubleValue()
            r7 = r27
            r9 = r25
            r11 = r19
            r6 = 2
            r13 = r21
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            goto L_0x01db
        L_0x021b:
            r6 = 2
            r19 = r29
            r21 = r31
            goto L_0x01db
        L_0x0221:
            java.lang.Object r7 = r3.get(r7)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            java.lang.Object r3 = r3.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r27 = r7
            r25 = r9
            goto L_0x0314
        L_0x023b:
            java.lang.String r3 = "svg"
            java.lang.String r6 = "unexpected path. prev item is not found"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r6)
            goto L_0x0bc2
        L_0x0244:
            r3 = r15
            r6 = 2
            r13 = 4
            r15 = 1
            if (r8 == 0) goto L_0x031a
            java.lang.String r7 = "q"
            r9 = 0
            java.lang.Object r10 = r8.get(r9)
            java.lang.String r10 = (java.lang.String) r10
            boolean r7 = r7.equalsIgnoreCase(r10)
            if (r7 == 0) goto L_0x02b6
            java.lang.Object r7 = r8.get(r14)
            java.lang.Double r7 = (java.lang.Double) r7
            double r9 = r7.doubleValue()
            double r9 = r27 + r9
            java.lang.Object r7 = r8.get(r15)
            java.lang.Double r7 = (java.lang.Double) r7
            double r11 = r7.doubleValue()
            double r19 = r9 - r11
            java.lang.Object r7 = r8.get(r13)
            java.lang.Double r7 = (java.lang.Double) r7
            double r9 = r7.doubleValue()
            double r9 = r25 + r9
            java.lang.Object r7 = r8.get(r6)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            double r21 = r9 - r7
            java.lang.Object r7 = r3.get(r15)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            double r16 = r27 + r7
            java.lang.Object r7 = r3.get(r6)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            double r23 = r25 + r7
            r7 = r27
            r9 = r25
            r11 = r19
            r13 = r21
            r6 = 1
            r15 = r16
            r17 = r23
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            goto L_0x02fb
        L_0x02b6:
            r6 = 1
            java.lang.String r7 = "t"
            r9 = 0
            java.lang.Object r8 = r8.get(r9)
            java.lang.String r8 = (java.lang.String) r8
            boolean r7 = r7.equalsIgnoreCase(r8)
            if (r7 == 0) goto L_0x02f7
            double r7 = r27 + r27
            double r19 = r7 - r29
            double r7 = r25 + r25
            double r21 = r7 - r31
            java.lang.Object r7 = r3.get(r6)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            double r15 = r27 + r7
            r7 = 2
            java.lang.Object r8 = r3.get(r7)
            java.lang.Double r8 = (java.lang.Double) r8
            double r7 = r8.doubleValue()
            double r17 = r25 + r7
            r7 = r27
            r9 = r25
            r11 = r19
            r13 = r21
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            goto L_0x02fb
        L_0x02f7:
            r19 = r29
            r21 = r31
        L_0x02fb:
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r27 = r27 + r6
            r6 = 2
            java.lang.Object r3 = r3.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r25 = r25 + r6
        L_0x0314:
            r29 = r19
            r31 = r21
            goto L_0x0bc2
        L_0x031a:
            java.lang.String r3 = "svg"
            java.lang.String r6 = "unexpected path. prev item is not found"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r6)
            goto L_0x0bc2
        L_0x0323:
            r3 = r15
            r6 = 1
            r13 = 4
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r11 = r6.doubleValue()
            r6 = 2
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r15 = r6.doubleValue()
            java.lang.Object r6 = r3.get(r14)
            java.lang.Double r6 = (java.lang.Double) r6
            double r17 = r6.doubleValue()
            java.lang.Object r6 = r3.get(r13)
            java.lang.Double r6 = (java.lang.Double) r6
            double r19 = r6.doubleValue()
            r7 = r27
            r9 = r25
            r6 = 3
            r13 = r15
            r15 = r17
            r17 = r19
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            r15 = 4
            java.lang.Object r3 = r3.get(r15)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
        L_0x0375:
            r27 = r6
            r25 = r8
            goto L_0x0bc2
        L_0x037b:
            r3 = r15
            r6 = 1
            r13 = 3
            r15 = 4
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r11 = r27 + r6
            r6 = 2
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r16 = r25 + r6
            java.lang.Object r6 = r3.get(r13)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r18 = r27 + r6
            java.lang.Object r6 = r3.get(r15)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r20 = r25 + r6
            r7 = r27
            r9 = r25
            r6 = 3
            r13 = r16
            r15 = r18
            r17 = r20
            double[] r7 = quad2cubic(r7, r9, r11, r13, r15, r17)
            addCurve(r5, r7)
            java.lang.Object r6 = r3.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r6 = r6.doubleValue()
            double r27 = r27 + r6
            r9 = 4
            java.lang.Object r3 = r3.get(r9)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r25 = r25 + r6
            goto L_0x0bc2
        L_0x03dd:
            r3 = r15
            r6 = 1
            r7 = 6
            r9 = 4
            r10 = 3
            r11 = 5
            java.lang.Object r8 = r3.get(r7)
            java.lang.Double r8 = (java.lang.Double) r8
            double r14 = r8.doubleValue()
            r7 = 5
            r8 = 6
            r11 = r14
            r14 = 7
            java.lang.Object r15 = r3.get(r14)
            java.lang.Double r15 = (java.lang.Double) r15
            double r15 = r15.doubleValue()
            r13 = r15
            java.lang.Object r15 = r3.get(r6)
            java.lang.Double r15 = (java.lang.Double) r15
            double r15 = r15.doubleValue()
            r8 = 2
            java.lang.Object r17 = r3.get(r8)
            java.lang.Double r17 = (java.lang.Double) r17
            double r17 = r17.doubleValue()
            java.lang.Object r8 = r3.get(r10)
            java.lang.Double r8 = (java.lang.Double) r8
            double r19 = r8.doubleValue()
            java.lang.Object r8 = r3.get(r9)
            java.lang.Double r8 = (java.lang.Double) r8
            double r21 = r8.doubleValue()
            java.lang.Object r3 = r3.get(r7)
            java.lang.Double r3 = (java.lang.Double) r3
            double r23 = r3.doubleValue()
            r3 = 5
            r7 = r27
            r3 = 3
            r9 = r25
            java.util.List r7 = com.alibaba.android.enhance.svg.morph.PathUtils.arcToBezier(r7, r9, r11, r13, r15, r17, r19, r21, r23)
            int r8 = r7.size()
            int r8 = r8 - r6
            java.lang.Object r8 = r7.get(r8)
            java.util.Map r8 = (java.util.Map) r8
            r9 = 0
        L_0x0445:
            int r10 = r7.size()
            if (r9 >= r10) goto L_0x0558
            java.lang.Object r10 = r7.get(r9)
            java.util.Map r10 = (java.util.Map) r10
            if (r9 != 0) goto L_0x04c5
            r15 = 8
            double[] r11 = new double[r15]
            r12 = 0
            r11[r12] = r27
            r11[r6] = r25
            java.lang.String r12 = "x1"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            r14 = 2
            r11[r14] = r12
            java.lang.String r12 = "y1"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            r11[r3] = r12
            java.lang.String r12 = "x2"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            r14 = 4
            r11[r14] = r12
            java.lang.String r12 = "y2"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            r16 = 5
            r11[r16] = r12
            java.lang.String r12 = "x"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            r14 = 6
            r11[r14] = r12
            java.lang.String r12 = "y"
            java.lang.Object r10 = r10.get(r12)
            java.lang.Double r10 = (java.lang.Double) r10
            double r12 = r10.doubleValue()
            r10 = 7
            r11[r10] = r12
            addCurve(r5, r11)
            r11 = 5
            r13 = 7
            r17 = 4
            goto L_0x0554
        L_0x04c5:
            r13 = 7
            r14 = 6
            r15 = 8
            int r11 = r9 + -1
            java.lang.Object r11 = r7.get(r11)
            java.util.Map r11 = (java.util.Map) r11
            double[] r12 = new double[r15]
            java.lang.String r15 = "x"
            java.lang.Object r15 = r11.get(r15)
            java.lang.Double r15 = (java.lang.Double) r15
            double r15 = r15.doubleValue()
            r17 = 0
            r12[r17] = r15
            java.lang.String r15 = "y"
            java.lang.Object r11 = r11.get(r15)
            java.lang.Double r11 = (java.lang.Double) r11
            double r15 = r11.doubleValue()
            r12[r6] = r15
            java.lang.String r11 = "x1"
            java.lang.Object r11 = r10.get(r11)
            java.lang.Double r11 = (java.lang.Double) r11
            double r15 = r11.doubleValue()
            r11 = 2
            r12[r11] = r15
            java.lang.String r11 = "y1"
            java.lang.Object r11 = r10.get(r11)
            java.lang.Double r11 = (java.lang.Double) r11
            double r15 = r11.doubleValue()
            r12[r3] = r15
            java.lang.String r11 = "x2"
            java.lang.Object r11 = r10.get(r11)
            java.lang.Double r11 = (java.lang.Double) r11
            double r15 = r11.doubleValue()
            r17 = 4
            r12[r17] = r15
            java.lang.String r11 = "y2"
            java.lang.Object r11 = r10.get(r11)
            java.lang.Double r11 = (java.lang.Double) r11
            double r15 = r11.doubleValue()
            r11 = 5
            r12[r11] = r15
            java.lang.String r15 = "x"
            java.lang.Object r15 = r10.get(r15)
            java.lang.Double r15 = (java.lang.Double) r15
            double r15 = r15.doubleValue()
            r12[r14] = r15
            java.lang.String r15 = "y"
            java.lang.Object r10 = r10.get(r15)
            java.lang.Double r10 = (java.lang.Double) r10
            double r15 = r10.doubleValue()
            r12[r13] = r15
            addCurve(r5, r12)
        L_0x0554:
            int r9 = r9 + 1
            goto L_0x0445
        L_0x0558:
            java.lang.String r3 = "x"
            java.lang.Object r3 = r8.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            java.lang.String r3 = "y"
            java.lang.Object r3 = r8.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            goto L_0x0375
        L_0x0574:
            r7 = r15
            r3 = 3
            r6 = 1
            r9 = 5
            r13 = 7
            r14 = 6
            r17 = 4
            java.lang.Object r8 = r7.get(r14)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            double r11 = r27 + r10
            java.lang.Object r8 = r7.get(r13)
            java.lang.Double r8 = (java.lang.Double) r8
            double r15 = r8.doubleValue()
            double r15 = r27 + r15
            r8 = 6
            r10 = 4
            r33 = 7
            r13 = r15
            java.lang.Object r15 = r7.get(r6)
            java.lang.Double r15 = (java.lang.Double) r15
            double r15 = r15.doubleValue()
            r8 = 2
            java.lang.Object r17 = r7.get(r8)
            java.lang.Double r17 = (java.lang.Double) r17
            double r17 = r17.doubleValue()
            java.lang.Object r8 = r7.get(r3)
            java.lang.Double r8 = (java.lang.Double) r8
            double r19 = r8.doubleValue()
            java.lang.Object r8 = r7.get(r10)
            java.lang.Double r8 = (java.lang.Double) r8
            double r21 = r8.doubleValue()
            java.lang.Object r7 = r7.get(r9)
            java.lang.Double r7 = (java.lang.Double) r7
            double r23 = r7.doubleValue()
            r7 = r27
            r9 = r25
            java.util.List r7 = com.alibaba.android.enhance.svg.morph.PathUtils.arcToBezier(r7, r9, r11, r13, r15, r17, r19, r21, r23)
            int r8 = r7.size()
            int r8 = r8 - r6
            java.lang.Object r8 = r7.get(r8)
            java.util.Map r8 = (java.util.Map) r8
            r9 = 0
        L_0x05e0:
            int r10 = r7.size()
            if (r9 >= r10) goto L_0x06ea
            java.lang.Object r10 = r7.get(r9)
            java.util.Map r10 = (java.util.Map) r10
            if (r9 != 0) goto L_0x065a
            r11 = 8
            double[] r12 = new double[r11]
            r13 = 0
            r12[r13] = r27
            r12[r6] = r25
            java.lang.String r13 = "x1"
            java.lang.Object r13 = r10.get(r13)
            java.lang.Double r13 = (java.lang.Double) r13
            double r13 = r13.doubleValue()
            r15 = 2
            r12[r15] = r13
            java.lang.String r13 = "y1"
            java.lang.Object r13 = r10.get(r13)
            java.lang.Double r13 = (java.lang.Double) r13
            double r13 = r13.doubleValue()
            r12[r3] = r13
            java.lang.String r13 = "x2"
            java.lang.Object r13 = r10.get(r13)
            java.lang.Double r13 = (java.lang.Double) r13
            double r13 = r13.doubleValue()
            r15 = 4
            r12[r15] = r13
            java.lang.String r13 = "y2"
            java.lang.Object r13 = r10.get(r13)
            java.lang.Double r13 = (java.lang.Double) r13
            double r13 = r13.doubleValue()
            r15 = 5
            r12[r15] = r13
            java.lang.String r13 = "x"
            java.lang.Object r13 = r10.get(r13)
            java.lang.Double r13 = (java.lang.Double) r13
            double r13 = r13.doubleValue()
            r15 = 6
            r12[r15] = r13
            java.lang.String r13 = "y"
            java.lang.Object r10 = r10.get(r13)
            java.lang.Double r10 = (java.lang.Double) r10
            double r13 = r10.doubleValue()
            r12[r33] = r13
            addCurve(r5, r12)
            goto L_0x06e6
        L_0x065a:
            r11 = 8
            r15 = 6
            int r12 = r9 + -1
            java.lang.Object r12 = r7.get(r12)
            java.util.Map r12 = (java.util.Map) r12
            double[] r13 = new double[r11]
            java.lang.String r14 = "x"
            java.lang.Object r14 = r12.get(r14)
            java.lang.Double r14 = (java.lang.Double) r14
            double r16 = r14.doubleValue()
            r14 = 0
            r13[r14] = r16
            java.lang.String r14 = "y"
            java.lang.Object r12 = r12.get(r14)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r13[r6] = r16
            java.lang.String r12 = "x1"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r12 = 2
            r13[r12] = r16
            java.lang.String r12 = "y1"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r13[r3] = r16
            java.lang.String r12 = "x2"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r12 = 4
            r13[r12] = r16
            java.lang.String r12 = "y2"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r12 = 5
            r13[r12] = r16
            java.lang.String r12 = "x"
            java.lang.Object r12 = r10.get(r12)
            java.lang.Double r12 = (java.lang.Double) r12
            double r16 = r12.doubleValue()
            r13[r15] = r16
            java.lang.String r12 = "y"
            java.lang.Object r10 = r10.get(r12)
            java.lang.Double r10 = (java.lang.Double) r10
            double r16 = r10.doubleValue()
            r13[r33] = r16
            addCurve(r5, r13)
        L_0x06e6:
            int r9 = r9 + 1
            goto L_0x05e0
        L_0x06ea:
            java.lang.String r3 = "x"
            java.lang.Object r3 = r8.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            java.lang.String r3 = "y"
            java.lang.Object r3 = r8.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            goto L_0x0375
        L_0x0706:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            if (r8 == 0) goto L_0x082a
            java.lang.String r9 = "c"
            r10 = 0
            java.lang.Object r12 = r8.get(r10)
            java.lang.String r12 = (java.lang.String) r12
            boolean r9 = r9.equalsIgnoreCase(r12)
            if (r9 == 0) goto L_0x0794
            double[] r9 = new double[r11]
            r9[r10] = r27
            r9[r6] = r25
            r10 = 5
            java.lang.Object r11 = r8.get(r10)
            java.lang.Double r11 = (java.lang.Double) r11
            double r10 = r11.doubleValue()
            double r27 = r27 + r10
            java.lang.Object r10 = r8.get(r3)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r27 = r27 - r10
            r10 = 2
            r9[r10] = r27
            java.lang.Object r10 = r8.get(r15)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r25 = r25 + r10
            r10 = 4
            java.lang.Object r8 = r8.get(r10)
            java.lang.Double r8 = (java.lang.Double) r8
            double r11 = r8.doubleValue()
            double r25 = r25 - r11
            r9[r3] = r25
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r11 = r6.doubleValue()
            r9[r10] = r11
            r6 = 2
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r11 = r6.doubleValue()
            r6 = 5
            r9[r6] = r11
            java.lang.Object r6 = r7.get(r3)
            java.lang.Double r6 = (java.lang.Double) r6
            double r11 = r6.doubleValue()
            r9[r15] = r11
            java.lang.Object r6 = r7.get(r10)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            r9[r33] = r10
            addCurve(r5, r9)
        L_0x0791:
            r6 = 4
            goto L_0x0814
        L_0x0794:
            java.lang.String r9 = "s"
            r10 = 0
            java.lang.Object r12 = r8.get(r10)
            java.lang.String r12 = (java.lang.String) r12
            boolean r9 = r9.equalsIgnoreCase(r12)
            if (r9 == 0) goto L_0x0791
            double[] r9 = new double[r11]
            r9[r10] = r27
            r9[r6] = r25
            java.lang.Object r10 = r8.get(r3)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r27 = r27 + r10
            java.lang.Object r10 = r8.get(r6)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r27 = r27 - r10
            r10 = 2
            r9[r10] = r27
            r11 = 4
            java.lang.Object r12 = r8.get(r11)
            java.lang.Double r12 = (java.lang.Double) r12
            double r11 = r12.doubleValue()
            double r25 = r25 + r11
            java.lang.Object r8 = r8.get(r10)
            java.lang.Double r8 = (java.lang.Double) r8
            double r11 = r8.doubleValue()
            double r25 = r25 - r11
            r9[r3] = r25
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r11 = r6.doubleValue()
            r6 = 4
            r9[r6] = r11
            java.lang.Object r8 = r7.get(r10)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            r8 = 5
            r9[r8] = r10
            java.lang.Object r8 = r7.get(r3)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            r9[r15] = r10
            java.lang.Object r8 = r7.get(r6)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            r9[r33] = r10
            addCurve(r5, r9)
        L_0x0814:
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            goto L_0x0bbe
        L_0x082a:
            java.lang.String r3 = "svg"
            java.lang.String r6 = "unexpected path. prev item is not found"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r6)
            goto L_0x0bc2
        L_0x0833:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            if (r8 == 0) goto L_0x0967
            java.lang.String r9 = "c"
            r10 = 0
            java.lang.Object r12 = r8.get(r10)
            java.lang.String r12 = (java.lang.String) r12
            boolean r9 = r9.equalsIgnoreCase(r12)
            if (r9 == 0) goto L_0x08c6
            double[] r9 = new double[r11]
            r9[r10] = r27
            r9[r6] = r25
            r10 = 5
            java.lang.Object r11 = r8.get(r10)
            java.lang.Double r11 = (java.lang.Double) r11
            double r10 = r11.doubleValue()
            double r10 = r27 + r10
            java.lang.Object r12 = r8.get(r3)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            double r10 = r10 - r12
            r12 = 2
            r9[r12] = r10
            java.lang.Object r10 = r8.get(r15)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r10 = r25 + r10
            r12 = 4
            java.lang.Object r8 = r8.get(r12)
            java.lang.Double r8 = (java.lang.Double) r8
            double r13 = r8.doubleValue()
            double r10 = r10 - r13
            r9[r3] = r10
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            double r10 = r27 + r10
            r9[r12] = r10
            r6 = 2
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            double r10 = r25 + r10
            r6 = 5
            r9[r6] = r10
            java.lang.Object r6 = r7.get(r3)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            double r10 = r27 + r10
            r9[r15] = r10
            java.lang.Object r6 = r7.get(r12)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            double r10 = r25 + r10
            r9[r33] = r10
            addCurve(r5, r9)
            goto L_0x094c
        L_0x08c6:
            java.lang.String r9 = "s"
            r10 = 0
            java.lang.Object r12 = r8.get(r10)
            java.lang.String r12 = (java.lang.String) r12
            boolean r9 = r9.equalsIgnoreCase(r12)
            if (r9 == 0) goto L_0x094c
            double[] r9 = new double[r11]
            r9[r10] = r27
            r9[r6] = r25
            java.lang.Object r10 = r8.get(r3)
            java.lang.Double r10 = (java.lang.Double) r10
            double r10 = r10.doubleValue()
            double r10 = r27 + r10
            java.lang.Object r12 = r8.get(r6)
            java.lang.Double r12 = (java.lang.Double) r12
            double r12 = r12.doubleValue()
            double r10 = r10 - r12
            r12 = 2
            r9[r12] = r10
            r10 = 4
            java.lang.Object r11 = r8.get(r10)
            java.lang.Double r11 = (java.lang.Double) r11
            double r10 = r11.doubleValue()
            double r10 = r25 + r10
            java.lang.Object r8 = r8.get(r12)
            java.lang.Double r8 = (java.lang.Double) r8
            double r13 = r8.doubleValue()
            double r10 = r10 - r13
            r9[r3] = r10
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r10 = r6.doubleValue()
            double r10 = r27 + r10
            r6 = 4
            r9[r6] = r10
            java.lang.Object r8 = r7.get(r12)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            double r10 = r25 + r10
            r8 = 5
            r9[r8] = r10
            java.lang.Object r8 = r7.get(r3)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            double r10 = r27 + r10
            r9[r15] = r10
            java.lang.Object r8 = r7.get(r6)
            java.lang.Double r8 = (java.lang.Double) r8
            double r10 = r8.doubleValue()
            double r10 = r25 + r10
            r9[r33] = r10
            addCurve(r5, r9)
        L_0x094c:
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            double r27 = r27 + r8
            r3 = 4
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r25 = r25 + r6
            goto L_0x0bc2
        L_0x0967:
            java.lang.String r3 = "svg"
            java.lang.String r6 = "unexpected path. prev item is not found"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r6)
            goto L_0x0bc2
        L_0x0970:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            r6 = 2
            r8[r6] = r9
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r3 = 4
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r3 = 5
            r8[r3] = r9
            java.lang.Object r6 = r7.get(r3)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            r8[r15] = r9
            java.lang.Object r6 = r7.get(r15)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            r8[r33] = r9
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            java.lang.Object r3 = r7.get(r15)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            goto L_0x0bbe
        L_0x09e3:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            double r9 = r27 + r9
            r6 = 2
            r8[r6] = r9
            java.lang.Object r6 = r7.get(r6)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            double r9 = r25 + r9
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            double r9 = r27 + r9
            r3 = 4
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            double r9 = r25 + r9
            r3 = 5
            r8[r3] = r9
            java.lang.Object r6 = r7.get(r3)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            double r9 = r27 + r9
            r8[r15] = r9
            java.lang.Object r6 = r7.get(r15)
            java.lang.Double r6 = (java.lang.Double) r6
            double r9 = r6.doubleValue()
            double r9 = r25 + r9
            r8[r33] = r9
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r3)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            double r27 = r27 + r8
            java.lang.Object r3 = r7.get(r15)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r25 = r25 + r6
            goto L_0x0bc2
        L_0x0a66:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            r9 = 2
            r8[r9] = r27
            r8[r3] = r25
            r3 = 4
            r8[r3] = r27
            r3 = 5
            r8[r3] = r25
            r8[r15] = r27
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r8[r33] = r9
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            r25 = r6
            goto L_0x0bc2
        L_0x0a9f:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            r9 = 2
            r8[r9] = r27
            r8[r3] = r25
            r3 = 4
            r8[r3] = r27
            r3 = 5
            r8[r3] = r25
            r8[r15] = r27
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            double r9 = r25 + r9
            r8[r33] = r9
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r25 = r25 + r6
            goto L_0x0bc2
        L_0x0ada:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            r9 = 2
            r8[r9] = r27
            r8[r3] = r25
            r3 = 4
            r8[r3] = r27
            r3 = 5
            r8[r3] = r25
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r8[r15] = r9
            r8[r33] = r25
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            r27 = r6
            goto L_0x0bc2
        L_0x0b13:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            r9 = 2
            r8[r9] = r27
            r8[r3] = r25
            r3 = 4
            r8[r3] = r27
            r3 = 5
            r8[r3] = r25
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            double r9 = r27 + r9
            r8[r15] = r9
            r8[r33] = r25
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
            double r27 = r27 + r6
            goto L_0x0bc2
        L_0x0b4d:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r15 = 6
            r33 = 7
            double[] r8 = new double[r11]
            r9 = 0
            r8[r9] = r27
            r8[r6] = r25
            java.lang.Object r9 = r7.get(r6)
            java.lang.Double r9 = (java.lang.Double) r9
            double r9 = r9.doubleValue()
            r12 = 2
            r8[r12] = r9
            java.lang.Object r9 = r7.get(r12)
            java.lang.Double r9 = (java.lang.Double) r9
            double r9 = r9.doubleValue()
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r3 = 4
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r12)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r3 = 5
            r8[r3] = r9
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r8[r15] = r9
            java.lang.Object r3 = r7.get(r12)
            java.lang.Double r3 = (java.lang.Double) r3
            double r9 = r3.doubleValue()
            r8[r33] = r9
            addCurve(r5, r8)
            java.lang.Object r3 = r7.get(r6)
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            java.lang.Object r3 = r7.get(r12)
            java.lang.Double r3 = (java.lang.Double) r3
            double r6 = r3.doubleValue()
        L_0x0bbe:
            r25 = r6
            r27 = r8
        L_0x0bc2:
            r10 = 0
            goto L_0x0c4d
        L_0x0bc5:
            r7 = r15
            r3 = 3
            r6 = 1
            r11 = 8
            r12 = 2
            r15 = 6
            r33 = 7
            java.lang.Object r8 = r7.get(r6)
            java.lang.Double r8 = (java.lang.Double) r8
            double r8 = r8.doubleValue()
            double r8 = r27 + r8
            java.lang.Object r7 = r7.get(r12)
            java.lang.Double r7 = (java.lang.Double) r7
            double r13 = r7.doubleValue()
            double r13 = r25 + r13
            double[] r7 = new double[r11]
            r10 = 0
            r7[r10] = r27
            r7[r6] = r25
            r7[r12] = r27
            r7[r3] = r25
            r3 = 4
            r7[r3] = r27
            r3 = 5
            r7[r3] = r25
            r7[r15] = r8
            r7[r33] = r13
            addCurve(r5, r7)
            r27 = r8
            r25 = r13
            goto L_0x0c4d
        L_0x0c03:
            r7 = r15
            r6 = 1
            r10 = 0
            java.util.LinkedList r3 = new java.util.LinkedList
            r3.<init>()
            r2.add(r3)
            java.lang.Object r5 = r7.get(r6)
            java.lang.Double r5 = (java.lang.Double) r5
            double r5 = r5.doubleValue()
            r8 = 2
            java.lang.Object r7 = r7.get(r8)
            java.lang.Double r7 = (java.lang.Double) r7
            double r7 = r7.doubleValue()
            r27 = r5
            r25 = r7
            goto L_0x0c4c
        L_0x0c28:
            r7 = r15
            r6 = 1
            r8 = 2
            r10 = 0
            java.util.LinkedList r3 = new java.util.LinkedList
            r3.<init>()
            r2.add(r3)
            java.lang.Object r5 = r7.get(r6)
            java.lang.Double r5 = (java.lang.Double) r5
            double r5 = r5.doubleValue()
            double r27 = r27 + r5
            java.lang.Object r5 = r7.get(r8)
            java.lang.Double r5 = (java.lang.Double) r5
            double r5 = r5.doubleValue()
            double r25 = r25 + r5
        L_0x0c4c:
            r5 = r3
        L_0x0c4d:
            int r4 = r4 + 1
            r6 = 0
            goto L_0x001e
        L_0x0c52:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.morph.MorphAlgorithm.path2Shapes(java.lang.String):java.util.List");
    }

    private static double[] quad2cubic(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d3 * 2.0d;
        double d8 = d4 * 2.0d;
        return new double[]{d, d2, (d + d7) / 3.0d, (d2 + d8) / 3.0d, (d5 + d7) / 3.0d, (d6 + d8) / 3.0d, d5, d6};
    }

    private static void addCurve(List<List<Double>> list, double... dArr) {
        if (list != null) {
            ArrayList arrayList = new ArrayList(8);
            for (double valueOf : dArr) {
                arrayList.add(Double.valueOf(valueOf));
            }
            list.add(arrayList);
            return;
        }
        throw new RuntimeException("unexpected state. curve array is empty");
    }

    static class PathCacheManager {
        private final int DEFAULT_CACHE_SIZE = 10;
        private final LruCache<String, Pair<List, List>> PATH_CACHE;

        PathCacheManager(int i) {
            this.PATH_CACHE = new LruCache<>(i <= 0 ? 10 : i);
        }

        /* access modifiers changed from: package-private */
        public Pair<List, List> get(String str, String str2) {
            return this.PATH_CACHE.get(encode(str, str2));
        }

        /* access modifiers changed from: package-private */
        public void put(String str, String str2, Pair<List, List> pair) {
            this.PATH_CACHE.put(encode(str, str2), pair);
        }

        private String encode(String str, String str2) {
            return str + "&&&" + str2;
        }
    }

    static class PathParser {
        private static final Map<String, Integer> CMD_TO_ARGS_LENGTH = new HashMap();
        private static Pattern PAT_DIGIT = Pattern.compile("-?[0-9]*\\.?[0-9]+(?:e[-+]?\\d+)?", 2);
        private static Pattern PAT_SEGMENT = Pattern.compile("([astvzqmhlc])([^astvzqmhlc]*)", 2);

        PathParser() {
        }

        static {
            CMD_TO_ARGS_LENGTH.put("a", 7);
            CMD_TO_ARGS_LENGTH.put("c", 6);
            CMD_TO_ARGS_LENGTH.put("h", 1);
            CMD_TO_ARGS_LENGTH.put("l", 2);
            CMD_TO_ARGS_LENGTH.put(WXComponent.PROP_FS_MATCH_PARENT, 2);
            CMD_TO_ARGS_LENGTH.put("q", 4);
            CMD_TO_ARGS_LENGTH.put("s", 4);
            CMD_TO_ARGS_LENGTH.put("t", 2);
            CMD_TO_ARGS_LENGTH.put("v", 1);
            CMD_TO_ARGS_LENGTH.put("z", 0);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x007e, code lost:
            r1 = new java.util.LinkedList();
            r1.add(r2);
            r1.addAll(r4);
            r0.add(r1);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.List<java.util.List<java.lang.Object>> parse(@androidx.annotation.NonNull java.lang.String r12) {
            /*
                r11 = this;
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
                java.util.regex.Pattern r1 = PAT_SEGMENT
                java.util.regex.Matcher r12 = r1.matcher(r12)
            L_0x000b:
                boolean r1 = r12.find()
                if (r1 == 0) goto L_0x00c1
                r1 = 1
                java.lang.String r2 = r12.group(r1)
                r3 = 2
                java.lang.String r4 = r12.group(r3)
                java.util.List r4 = r11.parseNumbers(r4)
                java.lang.String r5 = r2.toLowerCase()
                java.lang.String r6 = "m"
                boolean r6 = r6.equals(r5)
                r7 = 0
                if (r6 == 0) goto L_0x005a
                int r6 = r4.size()
                if (r6 <= r3) goto L_0x005a
                java.util.LinkedList r3 = new java.util.LinkedList
                r3.<init>()
                r3.add(r2)
                java.lang.Object r5 = r4.remove(r7)
                java.lang.Object r6 = r4.remove(r7)
                r3.add(r5)
                r3.add(r6)
                r0.add(r3)
                java.lang.String r5 = "l"
                java.lang.String r3 = "m"
                boolean r2 = r3.equals(r2)
                if (r2 == 0) goto L_0x0058
                java.lang.String r2 = "l"
                goto L_0x005a
            L_0x0058:
                java.lang.String r2 = "L"
            L_0x005a:
                java.util.Map<java.lang.String, java.lang.Integer> r3 = CMD_TO_ARGS_LENGTH
                java.lang.Object r3 = r3.get(r5)
                java.lang.Integer r3 = (java.lang.Integer) r3
                int r3 = r3.intValue()
                java.lang.String r6 = "z"
                boolean r6 = r6.equals(r5)
                if (r6 == 0) goto L_0x0078
                int r6 = r4.size()
                if (r6 <= 0) goto L_0x0078
                r4.clear()
            L_0x0078:
                int r6 = r4.size()
                if (r6 != r3) goto L_0x008e
                java.util.LinkedList r1 = new java.util.LinkedList
                r1.<init>()
                r1.add(r2)
                r1.addAll(r4)
                r0.add(r1)
                goto L_0x000b
            L_0x008e:
                int r6 = r4.size()
                if (r6 < r3) goto L_0x00b9
                java.util.LinkedList r6 = new java.util.LinkedList
                r6.<init>()
                r6.add(r2)
                java.util.Iterator r8 = r4.iterator()
                r9 = 0
            L_0x00a1:
                boolean r10 = r8.hasNext()
                if (r10 == 0) goto L_0x00b5
                int r9 = r9 + r1
                if (r9 > r3) goto L_0x00b5
                java.lang.Object r10 = r8.next()
                r6.add(r10)
                r8.remove()
                goto L_0x00a1
            L_0x00b5:
                r0.add(r6)
                goto L_0x005a
            L_0x00b9:
                java.lang.RuntimeException r12 = new java.lang.RuntimeException
                java.lang.String r0 = "malformed path data"
                r12.<init>(r0)
                throw r12
            L_0x00c1:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.morph.MorphAlgorithm.PathParser.parse(java.lang.String):java.util.List");
        }

        private List<Object> parseNumbers(@NonNull String str) {
            LinkedList linkedList = new LinkedList();
            Matcher matcher = PAT_DIGIT.matcher(str);
            while (matcher.find()) {
                double d = 0.0d;
                try {
                    d = Double.parseDouble(matcher.group());
                } catch (Exception unused) {
                }
                linkedList.add(Double.valueOf(d));
            }
            return linkedList;
        }
    }
}
