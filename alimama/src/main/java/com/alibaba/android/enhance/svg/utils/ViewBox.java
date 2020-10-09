package com.alibaba.android.enhance.svg.utils;

import android.text.TextUtils;

public class ViewBox {
    public static final int MOS_MEET = 0;
    public static final int MOS_NONE = 2;
    public static final int MOS_SLICE = 1;

    public static int parseMeetOrSlice(String str) {
        if (TextUtils.isEmpty(str) || "meet".equals(str)) {
            return 0;
        }
        if ("slice".equals(str)) {
            return 1;
        }
        if ("none".equals(str)) {
            return 2;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0112  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Matrix getTransform(android.graphics.RectF r26, android.graphics.RectF r27, java.lang.String r28, int r29) {
        /*
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            float r4 = r0.left
            double r4 = (double) r4
            float r6 = r0.top
            double r6 = (double) r6
            float r8 = r26.width()
            double r8 = (double) r8
            float r0 = r26.height()
            double r10 = (double) r0
            float r0 = r1.left
            double r12 = (double) r0
            float r0 = r1.top
            double r14 = (double) r0
            float r0 = r27.width()
            double r0 = (double) r0
            float r2 = r27.height()
            r16 = r14
            double r14 = (double) r2
            java.lang.Double.isNaN(r0)
            java.lang.Double.isNaN(r8)
            r18 = r6
            double r6 = r0 / r8
            java.lang.Double.isNaN(r14)
            java.lang.Double.isNaN(r10)
            r20 = r8
            double r8 = r14 / r10
            r22 = 4611686018427387904(0x4000000000000000, double:2.0)
            r2 = 2
            if (r3 != r2) goto L_0x0093
            double r2 = java.lang.Math.min(r6, r8)
            java.lang.Double.isNaN(r4)
            double r4 = r4 * r2
            java.lang.Double.isNaN(r12)
            double r12 = r12 - r4
            java.lang.Double.isNaN(r18)
            double r6 = r18 * r2
            java.lang.Double.isNaN(r16)
            double r4 = r16 - r6
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 <= 0) goto L_0x0078
            java.lang.Double.isNaN(r0)
            double r0 = r0 / r2
            java.lang.Double.isNaN(r20)
            double r0 = r0 - r20
            double r0 = r0 / r22
            double r12 = r12 - r0
            java.lang.Double.isNaN(r14)
            double r14 = r14 / r2
            java.lang.Double.isNaN(r10)
            double r14 = r14 - r10
            double r14 = r14 / r22
            double r4 = r4 - r14
            goto L_0x0090
        L_0x0078:
            java.lang.Double.isNaN(r20)
            double r8 = r20 * r2
            java.lang.Double.isNaN(r0)
            double r0 = r0 - r8
            double r0 = r0 / r22
            double r12 = r12 - r0
            java.lang.Double.isNaN(r10)
            double r10 = r10 * r2
            java.lang.Double.isNaN(r14)
            double r14 = r14 - r10
            double r14 = r14 / r22
            double r4 = r4 - r14
        L_0x0090:
            r6 = r2
            goto L_0x011d
        L_0x0093:
            java.lang.String r2 = "none"
            r24 = r14
            r14 = r28
            boolean r2 = r14.equals(r2)
            if (r2 != 0) goto L_0x00a7
            if (r3 != 0) goto L_0x00a7
            double r6 = java.lang.Math.min(r6, r8)
        L_0x00a5:
            r2 = r6
            goto L_0x00b9
        L_0x00a7:
            java.lang.String r2 = "none"
            boolean r2 = r14.equals(r2)
            if (r2 != 0) goto L_0x00b7
            r2 = 1
            if (r3 != r2) goto L_0x00b7
            double r6 = java.lang.Math.max(r6, r8)
            goto L_0x00a5
        L_0x00b7:
            r2 = r6
            r6 = r8
        L_0x00b9:
            java.lang.Double.isNaN(r4)
            double r4 = r4 * r2
            java.lang.Double.isNaN(r12)
            double r12 = r12 - r4
            java.lang.Double.isNaN(r18)
            double r4 = r18 * r6
            java.lang.Double.isNaN(r16)
            double r4 = r16 - r4
            java.lang.String r8 = "xMid"
            boolean r8 = r14.contains(r8)
            if (r8 == 0) goto L_0x00e2
            java.lang.Double.isNaN(r20)
            double r8 = r20 * r2
            java.lang.Double.isNaN(r0)
            double r8 = r0 - r8
            double r8 = r8 / r22
            double r12 = r12 + r8
        L_0x00e2:
            java.lang.String r8 = "xMax"
            boolean r8 = r14.contains(r8)
            if (r8 == 0) goto L_0x00f5
            java.lang.Double.isNaN(r20)
            double r8 = r20 * r2
            java.lang.Double.isNaN(r0)
            double r0 = r0 - r8
            double r12 = r12 + r0
        L_0x00f5:
            java.lang.String r0 = "YMid"
            boolean r0 = r14.contains(r0)
            if (r0 == 0) goto L_0x010a
            java.lang.Double.isNaN(r10)
            double r0 = r10 * r6
            java.lang.Double.isNaN(r24)
            double r0 = r24 - r0
            double r0 = r0 / r22
            double r4 = r4 + r0
        L_0x010a:
            java.lang.String r0 = "YMax"
            boolean r0 = r14.contains(r0)
            if (r0 == 0) goto L_0x011d
            java.lang.Double.isNaN(r10)
            double r10 = r10 * r6
            java.lang.Double.isNaN(r24)
            double r14 = r24 - r10
            double r4 = r4 + r14
        L_0x011d:
            android.graphics.Matrix r0 = new android.graphics.Matrix
            r0.<init>()
            float r1 = (float) r12
            float r4 = (float) r4
            r0.postTranslate(r1, r4)
            float r1 = (float) r2
            float r2 = (float) r6
            r0.preScale(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.utils.ViewBox.getTransform(android.graphics.RectF, android.graphics.RectF, java.lang.String, int):android.graphics.Matrix");
    }
}
