package com.ali.alihadeviceevaluator.cpu;

public class AliHACPUInfo {
    private float[] m8CoreFreqStage = {1.9f, 1.8f, 1.7f, 1.5f, 1.4f, 1.2f, 1.0f, 0.9f, 0.8f};
    public float mCPUAvgFreq = Float.MAX_VALUE;
    public int mCPUCore;
    public float mCPUMaxFreq = 0.0f;
    public float mCPUMinFreq;
    public int mCPUScore = -1;
    private float[] mCoreFreqStage = {2.4f, 2.2f, 2.0f, 1.8f, 1.5f, 1.3f, 1.2f, 1.0f, 0.9f};

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.io.FileReader} */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00af A[SYNTHETIC, Splitter:B:60:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00b9 A[SYNTHETIC, Splitter:B:65:0x00b9] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00c7 A[SYNTHETIC, Splitter:B:73:0x00c7] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00d1 A[SYNTHETIC, Splitter:B:78:0x00d1] */
    /* JADX WARNING: Removed duplicated region for block: B:89:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fetchCPUInfo() {
        /*
            r9 = this;
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()
            int r0 = r0.availableProcessors()
            r9.mCPUCore = r0
            int r0 = r9.mCPUCore
            if (r0 > 0) goto L_0x000f
            return
        L_0x000f:
            r0 = 0
            int r1 = r9.mCPUCore     // Catch:{ Throwable -> 0x00a8, all -> 0x00a5 }
            float[] r1 = new float[r1]     // Catch:{ Throwable -> 0x00a8, all -> 0x00a5 }
            r2 = 0
            r3 = 0
            r4 = r0
        L_0x0017:
            int r5 = r9.mCPUCore     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            if (r3 >= r5) goto L_0x0083
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            r6.<init>()     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            java.lang.String r7 = "/sys/devices/system/cpu/cpu"
            r6.append(r7)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            r6.append(r3)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            java.lang.String r7 = "/cpufreq/cpuinfo_max_freq"
            r6.append(r7)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            boolean r6 = r5.exists()     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            if (r6 != 0) goto L_0x003d
            goto L_0x0072
        L_0x003d:
            java.io.FileReader r6 = new java.io.FileReader     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            r6.<init>(r5)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0080, all -> 0x007d }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0080, all -> 0x007d }
            java.lang.String r4 = r5.readLine()     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            if (r4 == 0) goto L_0x0069
            long r7 = java.lang.Long.parseLong(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            float r4 = (float) r7     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            r7 = 1232348160(0x49742400, float:1000000.0)
            float r4 = r4 / r7
            r1[r3] = r4     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            float r7 = r9.mCPUMaxFreq     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            int r7 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r7 >= 0) goto L_0x0060
            r9.mCPUMaxFreq = r4     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
        L_0x0060:
            float r7 = r9.mCPUMinFreq     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
            int r7 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r7 <= 0) goto L_0x0068
            r9.mCPUMinFreq = r4     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
        L_0x0068:
            float r2 = r2 + r4
        L_0x0069:
            r6.close()     // Catch:{ IOException -> 0x006d }
            goto L_0x0071
        L_0x006d:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Throwable -> 0x0079, all -> 0x0075 }
        L_0x0071:
            r4 = r5
        L_0x0072:
            int r3 = r3 + 1
            goto L_0x0017
        L_0x0075:
            r0 = move-exception
            r1 = r0
            r4 = r5
            goto L_0x00c4
        L_0x0079:
            r0 = move-exception
            r1 = r0
            r0 = r5
            goto L_0x00aa
        L_0x007d:
            r0 = move-exception
            r1 = r0
            goto L_0x00c4
        L_0x0080:
            r0 = move-exception
            r1 = r0
            goto L_0x00a3
        L_0x0083:
            r1 = 1120403456(0x42c80000, float:100.0)
            float r2 = r2 * r1
            int r1 = r9.mCPUCore     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            float r1 = (float) r1     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            float r2 = r2 / r1
            int r1 = java.lang.Math.round(r2)     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            int r1 = r1 / 100
            float r1 = (float) r1     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            r9.mCPUAvgFreq = r1     // Catch:{ Throwable -> 0x00a1, all -> 0x009f }
            if (r4 == 0) goto L_0x00c1
            r4.close()     // Catch:{ IOException -> 0x009a }
            goto L_0x00c1
        L_0x009a:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00c1
        L_0x009f:
            r1 = move-exception
            goto L_0x00c5
        L_0x00a1:
            r1 = move-exception
            r6 = r0
        L_0x00a3:
            r0 = r4
            goto L_0x00aa
        L_0x00a5:
            r1 = move-exception
            r4 = r0
            goto L_0x00c5
        L_0x00a8:
            r1 = move-exception
            r6 = r0
        L_0x00aa:
            r1.printStackTrace()     // Catch:{ all -> 0x00c2 }
            if (r0 == 0) goto L_0x00b7
            r0.close()     // Catch:{ IOException -> 0x00b3 }
            goto L_0x00b7
        L_0x00b3:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00b7:
            if (r6 == 0) goto L_0x00c1
            r6.close()     // Catch:{ IOException -> 0x00bd }
            goto L_0x00c1
        L_0x00bd:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00c1:
            return
        L_0x00c2:
            r1 = move-exception
            r4 = r0
        L_0x00c4:
            r0 = r6
        L_0x00c5:
            if (r4 == 0) goto L_0x00cf
            r4.close()     // Catch:{ IOException -> 0x00cb }
            goto L_0x00cf
        L_0x00cb:
            r2 = move-exception
            r2.printStackTrace()
        L_0x00cf:
            if (r0 == 0) goto L_0x00d9
            r0.close()     // Catch:{ IOException -> 0x00d5 }
            goto L_0x00d9
        L_0x00d5:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00d9:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.cpu.AliHACPUInfo.fetchCPUInfo():void");
    }

    @Deprecated
    public void evaluateCPUScore() {
        float[] fArr;
        fetchCPUInfo();
        if (this.mCPUCore >= 8) {
            fArr = this.m8CoreFreqStage;
        } else {
            fArr = this.mCoreFreqStage;
        }
        int i = 0;
        while (true) {
            if (i >= fArr.length) {
                i = 9;
                break;
            } else if (this.mCPUMaxFreq >= fArr[i]) {
                break;
            } else {
                i++;
            }
        }
        int i2 = 10;
        int i3 = 10 - i;
        if (this.mCPUCore < 16) {
            if (this.mCPUCore >= 8) {
                i2 = 9;
            } else if (this.mCPUCore >= 6) {
                i2 = 8;
            } else if (this.mCPUCore >= 4) {
                i2 = 6;
            } else {
                i2 = this.mCPUCore >= 2 ? 4 : 0;
            }
        }
        this.mCPUScore = (int) ((((float) i3) * 0.6f) + (((float) i2) * 0.4f));
    }
}
