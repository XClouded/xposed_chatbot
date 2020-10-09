package com.ali.alihadeviceevaluator.old;

public class HardwareUseTime implements CalScore {
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScore(com.ali.alihadeviceevaluator.old.HardWareInfo r8) {
        /*
            r7 = this;
            java.lang.String r8 = "/sdcard/Android/"
            r0 = 1084227584(0x40a00000, float:5.0)
            r1 = 0
            r2 = 1088421888(0x40e00000, float:7.0)
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0046 }
            r3.<init>(r8)     // Catch:{ Throwable -> 0x0046 }
            boolean r8 = r3.exists()     // Catch:{ Throwable -> 0x0046 }
            if (r8 == 0) goto L_0x0046
            long r3 = r3.lastModified()     // Catch:{ Throwable -> 0x0046 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0046 }
            r8 = 0
            long r5 = r5 - r3
            long r3 = java.lang.Math.abs(r5)     // Catch:{ Throwable -> 0x0046 }
            r5 = 2592000000(0x9a7ec800, double:1.280618154E-314)
            long r3 = r3 / r5
            r5 = 100
            int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r8 <= 0) goto L_0x002f
            r8 = 1084227584(0x40a00000, float:5.0)
            goto L_0x0048
        L_0x002f:
            r5 = 50
            int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r8 <= 0) goto L_0x0037
            r8 = 0
            goto L_0x0048
        L_0x0037:
            r8 = 1092616192(0x41200000, float:10.0)
            r5 = 1045220557(0x3e4ccccd, float:0.2)
            float r3 = (float) r3     // Catch:{ Throwable -> 0x0046 }
            float r3 = r3 * r5
            float r8 = r8 - r3
            int r8 = java.lang.Math.round(r8)     // Catch:{ Throwable -> 0x0046 }
            float r8 = (float) r8
            goto L_0x0048
        L_0x0046:
            r8 = 1088421888(0x40e00000, float:7.0)
        L_0x0048:
            int r1 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r1 >= 0) goto L_0x004e
            r8 = 1084227584(0x40a00000, float:5.0)
        L_0x004e:
            int r8 = (int) r8
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardwareUseTime.getScore(com.ali.alihadeviceevaluator.old.HardWareInfo):int");
    }
}
