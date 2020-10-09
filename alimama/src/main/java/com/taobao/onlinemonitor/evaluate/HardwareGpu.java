package com.taobao.onlinemonitor.evaluate;

public class HardwareGpu implements CalScore {
    public float mCpuMaxFreq;
    public float mCpuMinFreq;
    public String mGpuBrand;
    public String mGpuName;

    /* JADX WARNING: Code restructure failed: missing block: B:118:0x0255, code lost:
        if (r13.mGpuName.contains("T720") != false) goto L_0x01fb;
     */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0319 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0328 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScore(com.taobao.onlinemonitor.HardWareInfo r14) {
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
            if (r14 != 0) goto L_0x002a
            r14 = 0
        L_0x0019:
            java.lang.String r1 = r13.mGpuName     // Catch:{ Exception -> 0x0029 }
            if (r1 != 0) goto L_0x002a
            r1 = 100
            if (r14 >= r1) goto L_0x002a
            r1 = 1
            java.lang.Thread.sleep(r1)     // Catch:{ Exception -> 0x0029 }
            int r14 = r14 + 1
            goto L_0x0019
        L_0x0029:
        L_0x002a:
            java.lang.String r14 = r13.mGpuName
            r1 = 9
            r2 = 7
            r3 = 1
            r4 = 4
            r5 = 8
            r6 = 10
            r7 = 3
            r8 = 2
            r9 = 5
            r10 = 6
            if (r14 == 0) goto L_0x0335
            java.lang.String r14 = r13.mGpuName
            java.lang.String r11 = "Adreno"
            boolean r14 = r14.contains(r11)
            r11 = 1073741824(0x40000000, float:2.0)
            if (r14 == 0) goto L_0x0190
            java.lang.String r14 = "高通"
            r13.mGpuBrand = r14
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "540"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0179
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "530"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0179
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "53"
            boolean r14 = r14.contains(r12)
            if (r14 != 0) goto L_0x0179
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Adreno (TM) 5"
            boolean r14 = r14.startsWith(r12)
            if (r14 != 0) goto L_0x0179
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Adreno (TM) 6"
            boolean r14 = r14.startsWith(r12)
            if (r14 == 0) goto L_0x007f
            goto L_0x0179
        L_0x007f:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 5"
            boolean r14 = r14.startsWith(r1)
            if (r14 != 0) goto L_0x017f
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 6"
            boolean r14 = r14.startsWith(r1)
            if (r14 == 0) goto L_0x0095
            goto L_0x017f
        L_0x0095:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "430"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x00a1
            goto L_0x030d
        L_0x00a1:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "420"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0176
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "418"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x00b7
            goto L_0x0176
        L_0x00b7:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "510"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0319
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "506"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0319
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "505"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x00d7
            goto L_0x0319
        L_0x00d7:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "330"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x00ec
            float r14 = r13.mCpuMaxFreq
            r0 = 1075000115(0x40133333, float:2.3)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 <= 0) goto L_0x0336
            goto L_0x0319
        L_0x00ec:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "405"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x00f8
            goto L_0x0336
        L_0x00f8:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "320"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0104
            goto L_0x0336
        L_0x0104:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "225"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0110
            goto L_0x0328
        L_0x0110:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "305"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0328
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "306"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0328
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "308"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0130
            goto L_0x0328
        L_0x0130:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "220"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x013c
            goto L_0x02f8
        L_0x013c:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "205"
            boolean r14 = r14.contains(r1)
            if (r14 != 0) goto L_0x0281
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "203"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0152
            goto L_0x0281
        L_0x0152:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "200"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x015e
            goto L_0x0274
        L_0x015e:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 4"
            boolean r14 = r14.startsWith(r1)
            if (r14 == 0) goto L_0x016a
            goto L_0x0319
        L_0x016a:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Adreno 3"
            boolean r14 = r14.startsWith(r1)
            if (r14 == 0) goto L_0x0335
            goto L_0x0328
        L_0x0176:
            r9 = 7
            goto L_0x0336
        L_0x0179:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r11 ? 1 : (r14 == r11 ? 0 : -1))
            if (r14 <= 0) goto L_0x0183
        L_0x017f:
            r9 = 10
            goto L_0x0336
        L_0x0183:
            float r14 = r13.mCpuMinFreq
            r0 = 1069547520(0x3fc00000, float:1.5)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 <= 0) goto L_0x018c
            goto L_0x017f
        L_0x018c:
            r9 = 9
            goto L_0x0336
        L_0x0190:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r12 = "Mali"
            boolean r14 = r14.contains(r12)
            if (r14 == 0) goto L_0x0260
            java.lang.String r14 = android.os.Build.HARDWARE
            r14.toLowerCase()
            java.lang.String r14 = r13.mGpuName
            java.lang.String r3 = "G71"
            boolean r14 = r14.contains(r3)
            if (r14 != 0) goto L_0x025b
            java.lang.String r14 = r13.mGpuName
            java.lang.String r3 = "G72"
            boolean r14 = r14.contains(r3)
            if (r14 == 0) goto L_0x01b5
            goto L_0x025b
        L_0x01b5:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r3 = "T880 MP"
            boolean r14 = r14.contains(r3)
            if (r14 != 0) goto L_0x0258
            java.lang.String r14 = r13.mGpuName
            java.lang.String r3 = "T880"
            boolean r14 = r14.contains(r3)
            if (r14 == 0) goto L_0x01cb
            goto L_0x0258
        L_0x01cb:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T860"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01d9
            r0 = 8
            goto L_0x025d
        L_0x01d9:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T830"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01e6
        L_0x01e3:
            r0 = 7
            goto L_0x025d
        L_0x01e6:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T820"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01f1
            goto L_0x01e3
        L_0x01f1:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "400 MP"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x01fd
        L_0x01fb:
            r0 = 6
            goto L_0x025d
        L_0x01fd:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "400"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0209
        L_0x0207:
            r0 = 2
            goto L_0x025d
        L_0x0209:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "450"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0214
            goto L_0x0207
        L_0x0214:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T624"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0220
        L_0x021e:
            r0 = 5
            goto L_0x025d
        L_0x0220:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T678"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x022b
            goto L_0x021e
        L_0x022b:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T628"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0236
        L_0x0235:
            goto L_0x01fb
        L_0x0236:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T604"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0242
            r0 = 3
            goto L_0x025d
        L_0x0242:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T760"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x024d
            goto L_0x0235
        L_0x024d:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "T720"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x025d
            goto L_0x01fb
        L_0x0258:
            r0 = 9
            goto L_0x025d
        L_0x025b:
            r0 = 10
        L_0x025d:
            r9 = r0
            goto L_0x0336
        L_0x0260:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "PowerVR"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x02fa
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 530"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x0277
        L_0x0274:
            r9 = 1
            goto L_0x0336
        L_0x0277:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 535"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x0284
        L_0x0281:
            r9 = 2
            goto L_0x0336
        L_0x0284:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 531"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x028f
            goto L_0x0281
        L_0x028f:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 544"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x02f8
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "SGX 543"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x02a4
            goto L_0x02f8
        L_0x02a4:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6200"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x0336
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6200"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x02ba
            goto L_0x0336
        L_0x02ba:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6400"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x0336
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6430"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x0336
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "G6"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x0336
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x02e3
            goto L_0x0336
        L_0x02e3:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "6450"
            boolean r14 = r14.contains(r0)
            if (r14 != 0) goto L_0x0319
            java.lang.String r14 = r13.mGpuName
            java.lang.String r0 = "7"
            boolean r14 = r14.contains(r0)
            if (r14 == 0) goto L_0x02f8
            goto L_0x0319
        L_0x02f8:
            r9 = 3
            goto L_0x0336
        L_0x02fa:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "NVIDIA"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x032a
            float r14 = r13.mCpuMaxFreq
            r0 = 1072064102(0x3fe66666, float:1.8)
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 < 0) goto L_0x0310
        L_0x030d:
            r9 = 8
            goto L_0x0336
        L_0x0310:
            float r14 = r13.mCpuMaxFreq
            r1 = 1074580685(0x400ccccd, float:2.2)
            int r14 = (r14 > r1 ? 1 : (r14 == r1 ? 0 : -1))
            if (r14 < 0) goto L_0x031b
        L_0x0319:
            r9 = 6
            goto L_0x0336
        L_0x031b:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r11 ? 1 : (r14 == r11 ? 0 : -1))
            if (r14 < 0) goto L_0x0322
            goto L_0x0336
        L_0x0322:
            float r14 = r13.mCpuMaxFreq
            int r14 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r14 < 0) goto L_0x02f8
        L_0x0328:
            r9 = 4
            goto L_0x0336
        L_0x032a:
            java.lang.String r14 = r13.mGpuName
            java.lang.String r1 = "Android Emulator"
            boolean r14 = r14.contains(r1)
            if (r14 == 0) goto L_0x0335
            goto L_0x030d
        L_0x0335:
            r9 = 0
        L_0x0336:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.evaluate.HardwareGpu.getScore(com.taobao.onlinemonitor.HardWareInfo):int");
    }
}
