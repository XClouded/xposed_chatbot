package com.taobao.onlinemonitor.evaluate;

import android.util.Log;

public class HardwareCpu implements CalScore {
    String mCpuInfo;
    int mCpuProcessCount = 4;
    private String mCpuScore0 = "K3V2E,K3V2,MT6589,EXYNOS4210,EXYNOS4212,MSM8X25Q,MSM8X26,PXA1088,PXA1L88,MSM8260,MSM8660,MSM8625,MSM8225,MSM8655,APQ8055,MSM7230,MSM7630,GOLDFISH,MSM8255T,MSM8655T,MSM7627A,MSM7227A,MSM7627T,MSM7227T,MT6577T,MT6572M,MT6515M,MT6575,QSD8650,QSD8250,OMAP4470,SP8810,SC8810MT6516,MT6573,MT6513,S5PC100,S5L8900,HI3611,HI3620,OMAP4460,OMAP4440,OMAP4430,EXYNOS3475,EXYNOS3110";
    private String mCpuScore1 = "MT6595,MT6592,MT6582,MSM8936,MSM8909,MSM8909V2,MSM8916V2,MSM8208,MSM8960T,MSM8260A,MSM8660A,MSM8960,MSM8X12,MSM8X10,MSM8X30,LC1860";
    private String mCpuScore10 = "SDM845,KIRIN970,MSM8998,EXYNOS8895";
    private String mCpuScore2 = "EXYNOS5260,EXYNOS5250,MT6750,MT6735,MSM8939V2,MSM8937,MSM8929,APQ8064,MSM8917,EXYNOS52,K3V2+,REDHOOKBAY,PXA1908,SC9860,HI6620OEM";
    private String mCpuScore3 = "MT675X,MT6795,MT6755,MT6752,MT6753,EXYNOS5800,EXYNOS5422,EXYNOS5410,MSM8952,MSM8940,PXA1936,HI6210SFT";
    private String mCpuScore4 = "MSM8X74,MSM8X74AA,MSM8X74AB,MSM8X74AC,MSM8674,MSM8274,MSM8074,EXYNOS5430,EXYNOS7870,EXYNOS7580,EXYNOS5433,MT679X,MT6797T,MT6797,EXYNOS5420,UNIVERSAL5420,RANCHU";
    private String mCpuScore5 = "APQ8084,MSM8084,MSM8953,HI3630,EXYNOS5433,HI3635,EXYNOS5";
    private String mCpuScore6 = "MSM8956,MSM8946,MT6797X,MT6797X,MT6797T,MT6797D";
    private String mCpuScore7 = "SDM660,SDM630,MSM8994,MSM8992,HI3650,EXYNOS7420,VBOX86";
    private String mCpuScore8 = "MSM8996,MSM8996PRO,MSM8996 PRO,EXYNOS8890,MT6799";
    private String mCpuScore9 = "MSM8997,HI3660";
    String[] mCpuScoreAry = {this.mCpuScore0, this.mCpuScore1, this.mCpuScore2, this.mCpuScore3, this.mCpuScore4, this.mCpuScore5, this.mCpuScore6, this.mCpuScore7, this.mCpuScore8, this.mCpuScore9, this.mCpuScore10};

    /* access modifiers changed from: package-private */
    public int findIndex(String str) {
        if (this.mCpuInfo == null) {
            return -1;
        }
        int length = this.mCpuScoreAry.length - 1;
        while (length >= 0) {
            String str2 = this.mCpuScoreAry[length];
            if (str2 == null || !str2.contains(str)) {
                length--;
            } else {
                Log.e("OnlineMonitor", "cpuModel=" + str + ",score=" + length);
                return length;
            }
        }
        return -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x01cb, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x01e1, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x01f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x01f6, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x01f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x0211, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x0215;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0113, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0171, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x0214;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01b6, code lost:
        if (r13.mCpuProcessCount >= 2) goto L_0x0115;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScore(com.taobao.onlinemonitor.HardWareInfo r14) {
        /*
            r13 = this;
            r0 = 0
            if (r14 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r1 = r14.mCpuCount
            r13.mCpuProcessCount = r1
            java.lang.String r1 = r14.mCpuName
            r13.mCpuInfo = r1
            java.lang.String r1 = r13.mCpuInfo
            if (r1 == 0) goto L_0x0220
            java.lang.String r1 = r13.mCpuInfo
            int r1 = r13.findIndex(r1)
            r2 = 1
            r3 = 5
            r4 = 4
            if (r1 >= 0) goto L_0x0093
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r6 = "MSM"
            boolean r5 = r5.startsWith(r6)
            if (r5 == 0) goto L_0x0052
            java.lang.String r5 = r13.mCpuInfo
            int r5 = r5.length()
            if (r5 <= r3) goto L_0x0052
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r5 = r5.substring(r0, r4)
            r1.append(r5)
            java.lang.String r5 = "X"
            r1.append(r5)
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r5 = r5.substring(r3)
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            int r1 = r13.findIndex(r1)
            goto L_0x0093
        L_0x0052:
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r6 = "MT"
            boolean r5 = r5.startsWith(r6)
            if (r5 == 0) goto L_0x0093
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r6 = r13.mCpuInfo
            int r6 = r6.length()
            int r6 = r6 - r2
            char r5 = r5.charAt(r6)
            r6 = 48
            if (r5 < r6) goto L_0x0093
            r6 = 57
            if (r5 > r6) goto L_0x0093
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = r13.mCpuInfo
            java.lang.String r6 = r13.mCpuInfo
            int r6 = r6.length()
            int r6 = r6 - r2
            java.lang.String r5 = r5.substring(r0, r6)
            r1.append(r5)
            java.lang.String r5 = "X"
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            int r1 = r13.findIndex(r1)
        L_0x0093:
            if (r1 >= 0) goto L_0x021f
            java.lang.String r1 = android.os.Build.HARDWARE
            java.lang.String r1 = r1.toUpperCase()
            if (r1 != 0) goto L_0x009f
            java.lang.String r1 = ""
        L_0x009f:
            java.lang.String r5 = "MSM"
            boolean r5 = r1.contains(r5)
            r6 = 9
            r7 = 6
            r8 = 7
            r9 = 3
            r10 = 10
            r11 = 8
            r12 = 2
            if (r5 != 0) goto L_0x01fa
            java.lang.String r5 = "EXYNOS8"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01fa
            java.lang.String r5 = "KIRIN"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x00c3
            goto L_0x01fa
        L_0x00c3:
            java.lang.String r5 = "SDM"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01e4
            java.lang.String r5 = "EXYNOS7"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01e4
            java.lang.String r5 = "HI"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x00dd
            goto L_0x01e4
        L_0x00dd:
            java.lang.String r5 = "QCOM"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01cf
            java.lang.String r5 = "QUALCOMM"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01cf
            java.lang.String r5 = "APQ"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x00f7
            goto L_0x01cf
        L_0x00f7:
            java.lang.String r5 = "MOOREFIELD"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x0118
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x0105
        L_0x0103:
            goto L_0x01f2
        L_0x0105:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x010b
            goto L_0x01dd
        L_0x010b:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x0111
            goto L_0x0215
        L_0x0111:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
        L_0x0115:
            r3 = 3
            goto L_0x0215
        L_0x0118:
            java.lang.String r5 = "MERRIFIELD"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01b9
            java.lang.String r5 = "CLOVERTRAIL"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01b9
            java.lang.String r5 = "REDHOOKBAY"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01b9
            java.lang.String r5 = "TEGRA"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01b9
            java.lang.String r5 = "NVIDIA"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01b9
            java.lang.String r5 = "K3"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x014a
            goto L_0x01b9
        L_0x014a:
            java.lang.String r5 = "SMDK"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x01a4
            java.lang.String r5 = "MT"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x015b
            goto L_0x01a4
        L_0x015b:
            java.lang.String r5 = "PXA"
            boolean r5 = r1.contains(r5)
            if (r5 == 0) goto L_0x0178
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x0169
            goto L_0x0215
        L_0x0169:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x016f
            goto L_0x01cd
        L_0x016f:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0175
            goto L_0x0214
        L_0x0175:
            r3 = 1
            goto L_0x0215
        L_0x0178:
            java.lang.String r5 = "SP"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x0190
            java.lang.String r5 = "SC"
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x0190
            java.lang.String r5 = "OMAP"
            boolean r1 = r1.contains(r5)
            if (r1 == 0) goto L_0x0215
        L_0x0190:
            int r1 = r13.mCpuProcessCount
            if (r1 < r11) goto L_0x0196
            goto L_0x01f8
        L_0x0196:
            int r1 = r13.mCpuProcessCount
            if (r1 < r4) goto L_0x019c
            goto L_0x0214
        L_0x019c:
            int r1 = r13.mCpuProcessCount
            if (r1 < r12) goto L_0x01a1
            goto L_0x0175
        L_0x01a1:
            r3 = 0
            goto L_0x0215
        L_0x01a4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x01a9
            goto L_0x01f2
        L_0x01a9:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x01ae
            goto L_0x01dd
        L_0x01ae:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x01b4
            goto L_0x0215
        L_0x01b4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
            goto L_0x01cd
        L_0x01b9:
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x01bf
            goto L_0x0103
        L_0x01bf:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x01c4
            goto L_0x01dd
        L_0x01c4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x01c9
            goto L_0x0215
        L_0x01c9:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
        L_0x01cd:
            goto L_0x0115
        L_0x01cf:
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x01d4
            goto L_0x020c
        L_0x01d4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x01d9
            goto L_0x01f2
        L_0x01d9:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x01df
        L_0x01dd:
            r3 = 6
            goto L_0x0215
        L_0x01df:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
            goto L_0x01f8
        L_0x01e4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x01e9
            goto L_0x0205
        L_0x01e9:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x01ee
            goto L_0x020c
        L_0x01ee:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x01f4
        L_0x01f2:
            r3 = 7
            goto L_0x0215
        L_0x01f4:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
        L_0x01f8:
            r3 = 4
            goto L_0x0215
        L_0x01fa:
            int r0 = r13.mCpuProcessCount
            if (r0 < r10) goto L_0x0201
            r3 = 10
            goto L_0x0215
        L_0x0201:
            int r0 = r13.mCpuProcessCount
            if (r0 < r11) goto L_0x0208
        L_0x0205:
            r3 = 9
            goto L_0x0215
        L_0x0208:
            int r0 = r13.mCpuProcessCount
            if (r0 < r4) goto L_0x020f
        L_0x020c:
            r3 = 8
            goto L_0x0215
        L_0x020f:
            int r0 = r13.mCpuProcessCount
            if (r0 < r12) goto L_0x0214
            goto L_0x0215
        L_0x0214:
            r3 = 2
        L_0x0215:
            int r3 = r3 * 2
            int r14 = r13.getCpuHzScore(r14)
            int r3 = r3 + r14
            int r0 = r3 / 3
            goto L_0x0220
        L_0x021f:
            r0 = r1
        L_0x0220:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.evaluate.HardwareCpu.getScore(com.taobao.onlinemonitor.HardWareInfo):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006d, code lost:
        if (r0.mCpuMaxFreq >= 1.0f) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00e8, code lost:
        if (r4 >= 1.0f) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00ea, code lost:
        r2 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0120, code lost:
        if (r4 >= 1.0f) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0123, code lost:
        r2 = 1;
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00ec  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCpuHzScore(com.taobao.onlinemonitor.HardWareInfo r18) {
        /*
            r17 = this;
            r0 = r18
            if (r0 != 0) goto L_0x0006
            r0 = 0
            return r0
        L_0x0006:
            int r1 = r0.mCpuCount
            r3 = 1068708659(0x3fb33333, float:1.4)
            r5 = 1072064102(0x3fe66666, float:1.8)
            r6 = 3
            r7 = 1065353216(0x3f800000, float:1.0)
            r8 = 1067030938(0x3f99999a, float:1.2)
            r9 = 5
            r10 = 1067869798(0x3fa66666, float:1.3)
            r11 = 6
            r12 = 2
            r13 = 1069547520(0x3fc00000, float:1.5)
            r14 = 9
            r15 = 10
            r2 = 8
            if (r1 < r2) goto L_0x0070
            float r1 = r0.mCpuMaxFreq
            r16 = 1072902963(0x3ff33333, float:1.9)
            int r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r1 < 0) goto L_0x0031
        L_0x002d:
            r1 = 10
            goto L_0x00b3
        L_0x0031:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 < 0) goto L_0x003b
        L_0x0037:
            r1 = 9
            goto L_0x00b3
        L_0x003b:
            float r1 = r0.mCpuMaxFreq
            r16 = 1071225242(0x3fd9999a, float:1.7)
            int r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r1 < 0) goto L_0x0048
        L_0x0044:
            r1 = 8
            goto L_0x00b3
        L_0x0048:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r13 ? 1 : (r1 == r13 ? 0 : -1))
            if (r1 < 0) goto L_0x0051
        L_0x004e:
            r1 = 7
            goto L_0x00b3
        L_0x0051:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L_0x0059
        L_0x0057:
            r1 = 6
            goto L_0x00b3
        L_0x0059:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            if (r1 < 0) goto L_0x0061
        L_0x005f:
            r1 = 5
            goto L_0x00b3
        L_0x0061:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x0069
            r1 = 4
            goto L_0x00b3
        L_0x0069:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r1 < 0) goto L_0x00b2
            goto L_0x00a8
        L_0x0070:
            float r1 = r0.mCpuMaxFreq
            r16 = 1075419546(0x4019999a, float:2.4)
            int r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r1 < 0) goto L_0x007a
            goto L_0x002d
        L_0x007a:
            float r1 = r0.mCpuMaxFreq
            r16 = 1074580685(0x400ccccd, float:2.2)
            int r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r1 < 0) goto L_0x0084
            goto L_0x0037
        L_0x0084:
            float r1 = r0.mCpuMaxFreq
            r16 = 1073741824(0x40000000, float:2.0)
            int r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1))
            if (r1 < 0) goto L_0x008d
            goto L_0x0044
        L_0x008d:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 < 0) goto L_0x0094
            goto L_0x004e
        L_0x0094:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r13 ? 1 : (r1 == r13 ? 0 : -1))
            if (r1 < 0) goto L_0x009b
            goto L_0x0057
        L_0x009b:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            if (r1 < 0) goto L_0x00a2
            goto L_0x005f
        L_0x00a2:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x00aa
        L_0x00a8:
            r1 = 3
            goto L_0x00b3
        L_0x00aa:
            float r1 = r0.mCpuMaxFreq
            int r1 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r1 < 0) goto L_0x00b2
            r1 = 2
            goto L_0x00b3
        L_0x00b2:
            r1 = 1
        L_0x00b3:
            float r4 = r0.mCpuMinFreq
            int r0 = r0.mCpuCount
            if (r0 < r2) goto L_0x00ec
            r0 = 1070386381(0x3fcccccd, float:1.6)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x00c4
        L_0x00c0:
            r2 = 10
            goto L_0x0124
        L_0x00c4:
            int r0 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1))
            if (r0 < 0) goto L_0x00cc
        L_0x00c8:
            r2 = 9
            goto L_0x0124
        L_0x00cc:
            int r0 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r0 < 0) goto L_0x00d1
            goto L_0x0124
        L_0x00d1:
            int r0 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r0 < 0) goto L_0x00d7
        L_0x00d5:
            r2 = 6
            goto L_0x0124
        L_0x00d7:
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 < 0) goto L_0x00dd
        L_0x00db:
            r2 = 5
            goto L_0x0124
        L_0x00dd:
            r0 = 1066192077(0x3f8ccccd, float:1.1)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x00e6
        L_0x00e4:
            r2 = 3
            goto L_0x0124
        L_0x00e6:
            int r0 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r0 < 0) goto L_0x0123
        L_0x00ea:
            r2 = 2
            goto L_0x0124
        L_0x00ec:
            r0 = 1073741824(0x40000000, float:2.0)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x00f3
            goto L_0x00c0
        L_0x00f3:
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r0 < 0) goto L_0x00f8
            goto L_0x00c8
        L_0x00f8:
            r0 = 1070386381(0x3fcccccd, float:1.6)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x0100
            goto L_0x0124
        L_0x0100:
            int r0 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1))
            if (r0 < 0) goto L_0x0106
            r2 = 7
            goto L_0x0124
        L_0x0106:
            int r0 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r0 < 0) goto L_0x010b
            goto L_0x00d5
        L_0x010b:
            int r0 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r0 < 0) goto L_0x0110
            goto L_0x00db
        L_0x0110:
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 < 0) goto L_0x0116
            r2 = 4
            goto L_0x0124
        L_0x0116:
            r0 = 1066192077(0x3f8ccccd, float:1.1)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x011e
            goto L_0x00e4
        L_0x011e:
            int r0 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r0 < 0) goto L_0x0123
            goto L_0x00ea
        L_0x0123:
            r2 = 1
        L_0x0124:
            int r1 = r1 + r2
            int r1 = r1 / r12
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.evaluate.HardwareCpu.getCpuHzScore(com.taobao.onlinemonitor.HardWareInfo):int");
    }
}
