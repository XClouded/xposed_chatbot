package com.ali.alihadeviceevaluator.old;

public class OldScoreMaker {
    public int mCpuScore;
    public int mEglScore;
    public int mGpuScore;
    public int mMemScore;
    public int mScore;

    /* JADX WARNING: Removed duplicated region for block: B:17:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x015e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int evaluateDeviceScore(com.ali.alihadeviceevaluator.old.HardWareInfo r21) {
        /*
            r20 = this;
            r0 = r20
            r1 = r21
            com.ali.alihadeviceevaluator.old.HardwareBrand r2 = new com.ali.alihadeviceevaluator.old.HardwareBrand
            r2.<init>()
            int r2 = r2.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareCpuCount r3 = new com.ali.alihadeviceevaluator.old.HardwareCpuCount
            r3.<init>()
            int r3 = r3.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareCpu r4 = new com.ali.alihadeviceevaluator.old.HardwareCpu
            r4.<init>()
            int r5 = r4.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareOsVersion r6 = new com.ali.alihadeviceevaluator.old.HardwareOsVersion
            r6.<init>()
            int r6 = r6.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareTotalMemory r7 = new com.ali.alihadeviceevaluator.old.HardwareTotalMemory
            r7.<init>()
            com.ali.alihadeviceevaluator.AliHAHardware r8 = com.ali.alihadeviceevaluator.AliHAHardware.getInstance()
            com.ali.alihadeviceevaluator.AliHAHardware$MemoryInfo r8 = r8.getMemoryInfo()
            long r8 = r8.deviceTotalMemory
            r7.mDeviceTotalMemory = r8
            int r7 = r7.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareJavaMemory r8 = new com.ali.alihadeviceevaluator.old.HardwareJavaMemory
            r8.<init>()
            com.ali.alihadeviceevaluator.mem.AliHAMemoryTracker r9 = new com.ali.alihadeviceevaluator.mem.AliHAMemoryTracker
            r9.<init>()
            android.app.Application r10 = com.ali.alihadeviceevaluator.util.Global.context
            int[] r9 = r9.getJavaHeapLimit(r10)
            r10 = 0
            r10 = r9[r10]
            r8.mJavaHeapLimitMemory = r10
            r10 = 1
            r9 = r9[r10]
            r8.mJavaHeapLimitLargeMemory = r9
            int r8 = r8.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareScreen r9 = new com.ali.alihadeviceevaluator.old.HardwareScreen
            r9.<init>()
            int r9 = r9.getScore(r1)
            java.io.File r11 = android.os.Environment.getDataDirectory()     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r11 = r11.getPath()     // Catch:{ Exception -> 0x00a7 }
            android.os.StatFs r12 = new android.os.StatFs     // Catch:{ Exception -> 0x00a7 }
            r12.<init>(r11)     // Catch:{ Exception -> 0x00a7 }
            int r11 = r12.getBlockSize()     // Catch:{ Exception -> 0x00a7 }
            long r13 = (long) r11     // Catch:{ Exception -> 0x00a7 }
            int r11 = r12.getBlockCount()     // Catch:{ Exception -> 0x00a7 }
            long r10 = (long) r11     // Catch:{ Exception -> 0x00a7 }
            long r10 = r10 * r13
            r16 = 1024(0x400, double:5.06E-321)
            long r10 = r10 / r16
            long r10 = r10 / r16
            long r10 = r10 / r16
            int r12 = r12.getAvailableBlocks()     // Catch:{ Exception -> 0x00a7 }
            r18 = r8
            r19 = r9
            long r8 = (long) r12
            long r13 = r13 * r8
            long r13 = r13 / r16
            long r13 = r13 / r16
            long r13 = r13 / r16
            com.ali.alihadeviceevaluator.old.HardwareStorage r8 = new com.ali.alihadeviceevaluator.old.HardwareStorage     // Catch:{ Exception -> 0x00ab }
            r8.<init>()     // Catch:{ Exception -> 0x00ab }
            int r9 = (int) r10     // Catch:{ Exception -> 0x00ab }
            r8.mInnerSize = r9     // Catch:{ Exception -> 0x00ab }
            int r9 = (int) r13     // Catch:{ Exception -> 0x00ab }
            r8.mInnerFree = r9     // Catch:{ Exception -> 0x00ab }
            int r10 = r8.getScore(r1)     // Catch:{ Exception -> 0x00ab }
            r15 = r10
            goto L_0x00ac
        L_0x00a7:
            r18 = r8
            r19 = r9
        L_0x00ab:
            r15 = 1
        L_0x00ac:
            com.ali.alihadeviceevaluator.AliHAHardware r9 = com.ali.alihadeviceevaluator.AliHAHardware.getInstance()     // Catch:{ Exception -> 0x00bb }
            com.ali.alihadeviceevaluator.AliHAHardware$DisplayInfo r9 = r9.getDisplayInfo()     // Catch:{ Exception -> 0x00bb }
            java.lang.String r9 = r9.mOpenGLVersion     // Catch:{ Exception -> 0x00bb }
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ Exception -> 0x00bb }
            goto L_0x00bd
        L_0x00bb:
            r9 = 1073741824(0x40000000, float:2.0)
        L_0x00bd:
            com.ali.alihadeviceevaluator.old.HardwareOpenGL r10 = new com.ali.alihadeviceevaluator.old.HardwareOpenGL
            r10.<init>()
            r10.mOpenglv = r9
            int r9 = r10.getScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareUseTime r10 = new com.ali.alihadeviceevaluator.old.HardwareUseTime
            r10.<init>()
            int r10 = r10.getScore(r1)
            int r4 = r4.getCpuHzScore(r1)
            com.ali.alihadeviceevaluator.old.HardwareGpu r11 = new com.ali.alihadeviceevaluator.old.HardwareGpu
            r11.<init>()
            int r11 = r11.getScore(r1)
            r12 = 1056964608(0x3f000000, float:0.5)
            if (r11 != 0) goto L_0x00f0
            float r11 = (float) r5
            float r11 = r11 * r12
            float r13 = (float) r3
            float r13 = r13 * r12
            float r11 = r11 + r13
            float r13 = (float) r4
            r14 = 1048576000(0x3e800000, float:0.25)
            float r13 = r13 * r14
            float r11 = r11 + r13
            int r11 = (int) r11
        L_0x00f0:
            int r13 = r5 * 2
            float r13 = (float) r13
            float r14 = (float) r3
            float r14 = r14 * r12
            float r13 = r13 + r14
            float r8 = (float) r4
            float r8 = r8 * r12
            float r13 = r13 + r8
            int r8 = java.lang.Math.round(r13)
            int r8 = r8 / 3
            r0.mCpuScore = r8
            int r8 = r0.mCpuScore
            r13 = 10
            if (r8 <= r13) goto L_0x010b
            r0.mCpuScore = r13
        L_0x010b:
            float r7 = (float) r7
            r8 = 1069547520(0x3fc00000, float:1.5)
            float r7 = r7 * r8
            r8 = r18
            float r13 = (float) r8
            float r13 = r13 * r12
            float r16 = r7 + r13
            int r16 = java.lang.Math.round(r16)
            int r12 = r16 / 2
            r0.mMemScore = r12
            int r12 = r0.mMemScore
            r1 = 10
            if (r12 <= r1) goto L_0x0127
            r0.mMemScore = r1
        L_0x0127:
            r0.mGpuScore = r11
            int r12 = r0.mGpuScore
            if (r12 <= r1) goto L_0x012f
            r0.mGpuScore = r1
        L_0x012f:
            r0.mEglScore = r9
            int r2 = r2 + r11
            float r1 = (float) r2
            float r1 = r1 + r14
            float r2 = (float) r5
            r11 = 1073741824(0x40000000, float:2.0)
            float r2 = r2 * r11
            float r1 = r1 + r2
            float r2 = (float) r6
            float r1 = r1 + r2
            float r1 = r1 + r7
            float r1 = r1 + r13
            r2 = r19
            float r7 = (float) r2
            r11 = 1056964608(0x3f000000, float:0.5)
            float r7 = r7 * r11
            float r1 = r1 + r7
            float r7 = (float) r15
            float r7 = r7 * r11
            float r1 = r1 + r7
            float r7 = (float) r9
            float r7 = r7 * r11
            float r1 = r1 + r7
            float r7 = (float) r10
            float r1 = r1 + r7
            int r1 = java.lang.Math.round(r1)
            float r1 = (float) r1
            int r1 = (int) r1
            r0.mScore = r1
            int r1 = r0.mScore
            r7 = 100
            if (r1 <= r7) goto L_0x0160
            r0.mScore = r7
        L_0x0160:
            r21.saveCpuAndGpuInfo()
            java.lang.String r1 = "DeviceEvaluator"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r9 = "设备分="
            r7.append(r9)
            int r9 = r0.mScore
            r7.append(r9)
            java.lang.String r9 = ", apiScore="
            r7.append(r9)
            r7.append(r6)
            java.lang.String r6 = ",memScore="
            r7.append(r6)
            int r6 = r0.mMemScore
            r7.append(r6)
            java.lang.String r6 = ",memLimitScore="
            r7.append(r6)
            r7.append(r8)
            java.lang.String r6 = ", cpuModelScore="
            r7.append(r6)
            r7.append(r5)
            java.lang.String r5 = ",cpuCountScore="
            r7.append(r5)
            r7.append(r3)
            java.lang.String r3 = ",mCpuScore="
            r7.append(r3)
            int r3 = r0.mCpuScore
            r7.append(r3)
            java.lang.String r3 = ", CpuHzScore="
            r7.append(r3)
            r7.append(r4)
            java.lang.String r3 = ",GpuScore="
            r7.append(r3)
            int r3 = r0.mGpuScore
            r7.append(r3)
            java.lang.String r3 = ",screenScore="
            r7.append(r3)
            r7.append(r2)
            java.lang.String r2 = ", openglScore="
            r7.append(r2)
            int r2 = r0.mEglScore
            r7.append(r2)
            java.lang.String r2 = ",storeScore="
            r7.append(r2)
            r7.append(r15)
            java.lang.String r2 = ",useTimeScore="
            r7.append(r2)
            r7.append(r10)
            java.lang.String r2 = r7.toString()
            android.util.Log.d(r1, r2)
            int r1 = r0.mScore
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.OldScoreMaker.evaluateDeviceScore(com.ali.alihadeviceevaluator.old.HardWareInfo):int");
    }
}
