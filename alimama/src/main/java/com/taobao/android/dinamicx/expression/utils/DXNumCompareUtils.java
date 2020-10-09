package com.taobao.android.dinamicx.expression.utils;

public class DXNumCompareUtils {
    public static final Object DEFAULT_VALUE = null;
    public static final int TYPE_GREATER = 1;
    public static final int TYPE_GREATER_EQUAL = 3;
    public static final int TYPE_LESS = 2;
    public static final int TYPE_LESS_EQUAL = 4;

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0062 A[Catch:{ Throwable -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b0 A[Catch:{ Throwable -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00b7 A[Catch:{ Throwable -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00bf A[Catch:{ Throwable -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x00eb A[Catch:{ Throwable -> 0x011a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object evalWithArgs(java.lang.Object[] r12, int r13) {
        /*
            if (r12 == 0) goto L_0x011c
            int r0 = r12.length     // Catch:{ Throwable -> 0x011a }
            r1 = 2
            if (r0 == r1) goto L_0x0008
            goto L_0x011c
        L_0x0008:
            r0 = 0
            r1 = r12[r0]     // Catch:{ Throwable -> 0x011a }
            r2 = 1
            r12 = r12[r2]     // Catch:{ Throwable -> 0x011a }
            boolean r3 = r1 instanceof java.lang.Integer     // Catch:{ Throwable -> 0x011a }
            r4 = 0
            r6 = 0
            if (r3 != 0) goto L_0x0056
            boolean r3 = r1 instanceof java.lang.Long     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x001b
            goto L_0x0056
        L_0x001b:
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.isFloatPointNum(r1)     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x002b
            java.lang.Number r1 = (java.lang.Number) r1     // Catch:{ Throwable -> 0x011a }
            double r8 = r1.doubleValue()     // Catch:{ Throwable -> 0x011a }
        L_0x0027:
            r10 = r8
            r1 = 1
            r8 = r6
            goto L_0x005e
        L_0x002b:
            boolean r3 = r1 instanceof java.lang.String     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x003f
            r3 = r1
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x011a }
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.hasDigit(r3)     // Catch:{ Throwable -> 0x011a }
            if (r3 != 0) goto L_0x003f
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x011a }
            long r8 = com.taobao.android.dinamicx.expression.DXNumberUtil.parseLong(r1)     // Catch:{ Throwable -> 0x011a }
            goto L_0x005c
        L_0x003f:
            boolean r3 = r1 instanceof java.lang.String     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x0053
            r3 = r1
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x011a }
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.hasDigit(r3)     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x0053
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x011a }
            double r8 = com.taobao.android.dinamicx.expression.DXNumberUtil.parseDouble(r1)     // Catch:{ Throwable -> 0x011a }
            goto L_0x0027
        L_0x0053:
            r10 = r4
            r8 = r6
            goto L_0x005d
        L_0x0056:
            java.lang.Number r1 = (java.lang.Number) r1     // Catch:{ Throwable -> 0x011a }
            long r8 = r1.longValue()     // Catch:{ Throwable -> 0x011a }
        L_0x005c:
            r10 = r4
        L_0x005d:
            r1 = 0
        L_0x005e:
            boolean r3 = r12 instanceof java.lang.Integer     // Catch:{ Throwable -> 0x011a }
            if (r3 != 0) goto L_0x00ae
            boolean r3 = r12 instanceof java.lang.Long     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x0067
            goto L_0x00ae
        L_0x0067:
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.isFloatPointNum(r12)     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x0079
            if (r1 != 0) goto L_0x0071
            double r3 = (double) r8     // Catch:{ Throwable -> 0x011a }
            r10 = r3
        L_0x0071:
            java.lang.Number r12 = (java.lang.Number) r12     // Catch:{ Throwable -> 0x011a }
            double r4 = r12.doubleValue()     // Catch:{ Throwable -> 0x011a }
        L_0x0077:
            r1 = 1
            goto L_0x00bd
        L_0x0079:
            boolean r3 = r12 instanceof java.lang.String     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x0096
            r3 = r12
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x011a }
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.hasDigit(r3)     // Catch:{ Throwable -> 0x011a }
            if (r3 != 0) goto L_0x0096
            if (r1 != 0) goto L_0x008f
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Throwable -> 0x011a }
            long r6 = com.taobao.android.dinamicx.expression.DXNumberUtil.parseLong(r12)     // Catch:{ Throwable -> 0x011a }
            goto L_0x00bd
        L_0x008f:
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Throwable -> 0x011a }
            double r4 = com.taobao.android.dinamicx.expression.DXNumberUtil.parseDouble(r12)     // Catch:{ Throwable -> 0x011a }
            goto L_0x00bd
        L_0x0096:
            boolean r3 = r12 instanceof java.lang.String     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x00bd
            r3 = r12
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x011a }
            boolean r3 = com.taobao.android.dinamicx.expression.DXNumberUtil.hasDigit(r3)     // Catch:{ Throwable -> 0x011a }
            if (r3 == 0) goto L_0x00bd
            if (r1 != 0) goto L_0x00a7
            double r3 = (double) r8     // Catch:{ Throwable -> 0x011a }
            r10 = r3
        L_0x00a7:
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Throwable -> 0x011a }
            double r4 = com.taobao.android.dinamicx.expression.DXNumberUtil.parseDouble(r12)     // Catch:{ Throwable -> 0x011a }
            goto L_0x0077
        L_0x00ae:
            if (r1 != 0) goto L_0x00b7
            java.lang.Number r12 = (java.lang.Number) r12     // Catch:{ Throwable -> 0x011a }
            long r6 = r12.longValue()     // Catch:{ Throwable -> 0x011a }
            goto L_0x00bd
        L_0x00b7:
            java.lang.Number r12 = (java.lang.Number) r12     // Catch:{ Throwable -> 0x011a }
            double r4 = r12.doubleValue()     // Catch:{ Throwable -> 0x011a }
        L_0x00bd:
            if (r1 == 0) goto L_0x00eb
            switch(r13) {
                case 1: goto L_0x00e1;
                case 2: goto L_0x00d7;
                case 3: goto L_0x00cd;
                case 4: goto L_0x00c3;
                default: goto L_0x00c2;
            }     // Catch:{ Throwable -> 0x011a }
        L_0x00c2:
            goto L_0x0117
        L_0x00c3:
            int r12 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r12 > 0) goto L_0x00c8
            r0 = 1
        L_0x00c8:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x00cd:
            int r12 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r12 < 0) goto L_0x00d2
            r0 = 1
        L_0x00d2:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x00d7:
            int r12 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r12 >= 0) goto L_0x00dc
            r0 = 1
        L_0x00dc:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x00e1:
            int r12 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r12 <= 0) goto L_0x00e6
            r0 = 1
        L_0x00e6:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x00eb:
            switch(r13) {
                case 1: goto L_0x010d;
                case 2: goto L_0x0103;
                case 3: goto L_0x00f9;
                case 4: goto L_0x00ef;
                default: goto L_0x00ee;
            }     // Catch:{ Throwable -> 0x011a }
        L_0x00ee:
            goto L_0x0117
        L_0x00ef:
            int r12 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r12 > 0) goto L_0x00f4
            r0 = 1
        L_0x00f4:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x00f9:
            int r12 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r12 < 0) goto L_0x00fe
            r0 = 1
        L_0x00fe:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x0103:
            int r12 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r12 >= 0) goto L_0x0108
            r0 = 1
        L_0x0108:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x010d:
            int r12 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r12 <= 0) goto L_0x0112
            r0 = 1
        L_0x0112:
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x0117:
            java.lang.Object r12 = DEFAULT_VALUE
            return r12
        L_0x011a:
            r12 = move-exception
            goto L_0x011f
        L_0x011c:
            java.lang.Object r12 = DEFAULT_VALUE     // Catch:{ Throwable -> 0x011a }
            return r12
        L_0x011f:
            com.taobao.android.dinamicx.exception.DXExceptionUtil.printStack(r12)
            java.lang.Object r12 = DEFAULT_VALUE
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.expression.utils.DXNumCompareUtils.evalWithArgs(java.lang.Object[], int):java.lang.Object");
    }
}
