package com.ali.alihadeviceevaluator.old;

public class HardwareGpu implements CalScore {
    public float mCpuMaxFreq;
    public float mCpuMinFreq;
    public String mGpuBrand;
    public String mGpuName;

    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0230, code lost:
        if (r13.mGpuName.contains("T720") != false) goto L_0x01d6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02ef A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02fe A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScore(com.ali.alihadeviceevaluator.old.HardWareInfo r14) {
        /*
            r13 = this;
            r0 = 0
            if (r14 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.String r1 = r14.mGpuName
            r13.mGpuName = r1
            java.lang.String r1 = r14.mGpuBrand
            r13.mGpuBrand = r1
            float r1 = r14.mCpuMaxFreq
            r13.mCpuMaxFreq = r1
            float r14 = r14.mCpuMinFreq
            r13.mCpuMinFreq = r14
            java.lang.String r14 = r13.mGpuName
            r1 = 1
            r2 = 9
            r3 = 7
            r4 = 4
            r5 = 8
            r6 = 10
            r7 = 3
            r8 = 2
            r9 = 5
            r10 = 6
            if (r14 == 0) goto L_0x030b
            java.lang.String r14 = r13.mGpuName
            java.lang.String r11 = "Adreno"
            boolean r14 = r14.contains(r11)
            r11 = 1073741824(0x40000000, float:2.0)
            if (r14 == 0) goto L_0x016f
            java.lang.String r14 = "高通"
            r13.mGpuBrand = r14
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "540"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0158
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "530"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0158
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "53"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0158
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Adreno (TM) 5"
            boolean r14 = r14.startsWith(r12)
            if (r14 != 0) goto L_0x0158
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Adreno (TM) 6"
            boolean r14 = r14.startsWith(r12)
            if (r14 != 0) goto L_0x0158
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "Adreno 5"
            boolean r14 = r14.startsWith(r2)
            if (r14 != 0) goto L_0x015e
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "Adreno 6"
            boolean r14 = r14.startsWith(r2)
            if (r14 != 0) goto L_0x015e
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "430"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x0088
            goto L_0x02e3
        L_0x0088:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "420"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x0155
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "418"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x0155
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "510"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02ef
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "506"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02ef
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "505"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02ef
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "330"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x00cf
            float r14 = r13.mCpuMaxFreq
            r0 = 1075000115(0x40133333, float:2.3)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 <= 0) goto L_0x030c
            goto L_0x02ef
        L_0x00cf:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "405"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x00db
            goto L_0x030c
        L_0x00db:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "320"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x00e7
            goto L_0x030c
        L_0x00e7:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "225"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x00f3
            goto L_0x02fe
        L_0x00f3:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "305"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02fe
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "306"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02fe
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "308"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x02fe
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "220"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x011d
            goto L_0x02ce
        L_0x011d:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "205"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x025c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "203"
            boolean r14 = r14.contains(r2)
            if (r14 != 0) goto L_0x025c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "200"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x013d
            goto L_0x024f
        L_0x013d:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 4"
            boolean r14 = r14.startsWith(r1)
            if (r14 == 0) goto L_0x0149
            goto L_0x02ef
        L_0x0149:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 3"
            boolean r14 = r14.startsWith(r1)
            if (r14 == 0) goto L_0x030b
            goto L_0x02fe
        L_0x0155:
            r9 = 7
            goto L_0x030c
        L_0x0158:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r11 ? 1 : (r14 == r11 ? 0 : -1))
            if (r14 <= 0) goto L_0x0162
        L_0x015e:
            r9 = 10
            goto L_0x030c
        L_0x0162:
            float r14 = r13.mCpuMinFreq
            r0 = 1069547520(0x3fc00000, float:1.5)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 <= 0) goto L_0x016b
            goto L_0x015e
        L_0x016b:
            r9 = 9
            goto L_0x030c
        L_0x016f:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Mali"
            boolean r14 = r14.contains(r12)
            if (r14 == 0) goto L_0x023b
            java.lang.String r14 = android.os.Build.HARDWARE
            r14.toLowerCase()
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "G71"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0236
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "G72"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0236
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T880 MP"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0233
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T880"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0233
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T860"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01b4
            r0 = 8
            goto L_0x0238
        L_0x01b4:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T830"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01c1
        L_0x01be:
            r0 = 7
            goto L_0x0238
        L_0x01c1:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T820"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01cc
            goto L_0x01be
        L_0x01cc:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "400 MP"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01d8
        L_0x01d6:
            r0 = 6
            goto L_0x0238
        L_0x01d8:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "400"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01e4
        L_0x01e2:
            r0 = 2
            goto L_0x0238
        L_0x01e4:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "450"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01ef
            goto L_0x01e2
        L_0x01ef:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T624"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01fb
        L_0x01f9:
            r0 = 5
            goto L_0x0238
        L_0x01fb:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T678"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0206
            goto L_0x01f9
        L_0x0206:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T628"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0211
        L_0x0210:
            goto L_0x01d6
        L_0x0211:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T604"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x021d
            r0 = 3
            goto L_0x0238
        L_0x021d:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T760"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0228
            goto L_0x0210
        L_0x0228:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T720"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0238
            goto L_0x01d6
        L_0x0233:
            r0 = 9
            goto L_0x0238
        L_0x0236:
            r0 = 10
        L_0x0238:
            r9 = r0
            goto L_0x030c
        L_0x023b:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r2 = "PowerVR"
            boolean r14 = r14.contains(r2)
            if (r14 == 0) goto L_0x02d0
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 530"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x0252
        L_0x024f:
            r9 = 1
            goto L_0x030c
        L_0x0252:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 535"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x025f
        L_0x025c:
            r9 = 2
            goto L_0x030c
        L_0x025f:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 531"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x026a
            goto L_0x025c
        L_0x026a:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 544"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x02ce
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 543"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x02ce
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6200"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6200"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6400"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6430"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x030c
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6450"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x02ef
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "7"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x02ef
        L_0x02ce:
            r9 = 3
            goto L_0x030c
        L_0x02d0:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "NVIDIA"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0300
            float r14 = r13.mCpuMaxFreq
            r0 = 1072064102(0x3fe66666, float:1.8)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 < 0) goto L_0x02e6
        L_0x02e3:
            r9 = 8
            goto L_0x030c
        L_0x02e6:
            float r14 = r13.mCpuMaxFreq
            r1 = 1074580685(0x400ccccd, float:2.2)
            int r14 = (r14 > r1 ? 1 : (r14 == r1 ? 0 : -1))
            if (r14 < 0) goto L_0x02f1
        L_0x02ef:
            r9 = 6
            goto L_0x030c
        L_0x02f1:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r11 ? 1 : (r14 == r11 ? 0 : -1))
            if (r14 < 0) goto L_0x02f8
            goto L_0x030c
        L_0x02f8:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 < 0) goto L_0x02ce
        L_0x02fe:
            r9 = 4
            goto L_0x030c
        L_0x0300:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Android Emulator"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x030b
            goto L_0x02e3
        L_0x030b:
            r9 = 0
        L_0x030c:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.old.HardwareGpu.getScore(com.ali.alihadeviceevaluator.old.HardWareInfo):int");
    }
}
