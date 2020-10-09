package com.taobao.onlinemonitor;

import android.os.Debug;
import android.util.Log;

public class EvaluateScore {
    int mNoDeviceSystemScore;
    int mTotalGcCount;

    public int evaluatePidScore(OnLineMonitor onLineMonitor) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        float f;
        OnLineMonitor onLineMonitor2 = onLineMonitor;
        if (onLineMonitor2 == null) {
            return 0;
        }
        long nanoTime = System.nanoTime() / 1000000;
        for (int i9 = 0; i9 < 100; i9++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException unused) {
            }
        }
        onLineMonitor2.mTestSleepTime = (System.nanoTime() / 1000000) - nanoTime;
        int i10 = 10 - ((((int) onLineMonitor2.mTestSleepTime) - 100) / 20);
        if (i10 < 0) {
            i10 = 0;
        }
        onLineMonitor2.mTest = 0;
        long nanoTime2 = System.nanoTime();
        long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
        for (int i11 = 0; i11 < 100000; i11++) {
            onLineMonitor2.mTest = (short) (onLineMonitor2.mTest + i11);
        }
        long threadCpuTimeNanos2 = Debug.threadCpuTimeNanos() - threadCpuTimeNanos;
        onLineMonitor2.mTestForTime = System.nanoTime() - nanoTime2;
        long j = 10 - ((onLineMonitor2.mTestForTime - threadCpuTimeNanos2) / ((long) (((int) threadCpuTimeNanos2) / 5)));
        if (j < 0) {
            j = 0;
        }
        float f2 = 0.0f;
        if (!onLineMonitor2.mFileSchedIsNotExists) {
            float f3 = onLineMonitor2.mPidPerCpuLoad / ((float) onLineMonitor2.mCpuProcessCount);
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            int round = 10 - Math.round(f3);
            if (round < 0) {
                round = 0;
            }
            int i12 = onLineMonitor2.mPidIoWaitCount - onLineMonitor2.mPidIoWaitCountOld;
            int i13 = onLineMonitor2.mPidIoWaitSum - onLineMonitor2.mPidIoWaitSumOld;
            int i14 = 10 - (i12 / 10);
            if (i14 < 0) {
                i14 = 0;
            }
            i = 10 - (i13 / 100);
            if (i < 0) {
                i = 0;
            }
            int i15 = i14;
            i2 = round;
            i3 = i15;
        } else {
            i3 = 10 - (onLineMonitor2.mProcessCpuTracker.mTotalIoWaitPercent / 10);
            if (i3 < 0) {
                i3 = 0;
            }
            float f4 = onLineMonitor2.mSystemLoadAvg[0] / ((float) onLineMonitor2.mCpuProcessCount);
            if (f4 >= 0.0f) {
                f2 = f4;
            }
            i2 = 10 - Math.round(f2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = 0;
        }
        int i16 = onLineMonitor2.mRuntimeThreadCount - 10;
        if (i16 < 0) {
            i16 = 0;
        }
        int i17 = 10 - (i16 / 10);
        if (i17 < 0) {
            i17 = 0;
        }
        int i18 = 10 - (onLineMonitor2.mRunningThreadCount / 5);
        if (i18 < 0) {
            i18 = 0;
        }
        int i19 = 10 - (onLineMonitor2.mMyPidCPUPercent / 10);
        if (i19 < 0) {
            i19 = 0;
        }
        int i20 = onLineMonitor2.mTotalGcCount;
        int i21 = this.mTotalGcCount;
        if (OnLineMonitor.sApiLevel >= 23) {
            i4 = 10;
        } else if (OnLineMonitor.sApiLevel >= 21) {
            i4 = 6;
        } else if (OnLineMonitor.sApiLevel >= 19) {
            i4 = 4;
        } else {
            i4 = OnLineMonitor.sApiLevel >= 17 ? 2 : 1;
        }
        this.mTotalGcCount = onLineMonitor2.mTotalGcCount;
        int i22 = i2;
        int i23 = i10;
        int i24 = 10 - (((int) ((onLineMonitor2.mTotalUsedMemory * 100) / onLineMonitor2.mOnLineStat.deviceInfo.deviceTotalAvailMemory)) / 10);
        if (i24 < 0) {
            i24 = 0;
        }
        if (i24 >= 2) {
            if (onLineMonitor2.mIsLowMemroy && i24 >= 3) {
                i24 -= 3;
            } else if (onLineMonitor2.mTrimMemoryLevel >= 60 && i24 >= 3) {
                i24 -= 3;
            } else if (onLineMonitor2.mTrimMemoryLevel >= 60) {
                i24 -= 2;
            }
        }
        float f5 = (((float) i17) * 0.4f) + (((float) i18) * 0.4f) + (((float) i24) * 0.4f) + (((float) i19) * 0.5f) + (((float) i4) * 0.3f);
        if (!onLineMonitor2.mFileSchedIsNotExists) {
            double d = (double) f5;
            double d2 = (double) ((((float) i3) * 0.9f) + (((float) i) * 0.7f));
            i7 = i22;
            double d3 = (double) i7;
            Double.isNaN(d3);
            Double.isNaN(d2);
            double d4 = d2 + (d3 * 0.6d);
            i8 = i23;
            i6 = i;
            i5 = i17;
            double d5 = (double) (((float) i8) * 0.5f);
            Double.isNaN(d5);
            double d6 = d4 + d5;
            double d7 = (double) (((float) j) * 0.3f);
            Double.isNaN(d7);
            Double.isNaN(d);
            f = (float) (d + d6 + d7);
            i3 = i3;
        } else {
            i6 = i;
            i5 = i17;
            i7 = i22;
            i8 = i23;
            f = (((float) i3) * 1.5f) + (((float) i7) * 0.5f) + (((float) i8) * 0.6f) + (((float) j) * 0.5f) + f5;
        }
        int round2 = (int) ((float) Math.round(f + (((float) onLineMonitor2.mDevicesScore) * 0.3f) + (((float) this.mNoDeviceSystemScore) * 0.3333f)));
        int i25 = 100;
        if (round2 <= 100) {
            i25 = round2;
        }
        if (OnLineMonitor.sIsDetailDebug) {
            Log.e("OnLineMonitor", "进程分=" + i25 + ",sheduleScore=" + i8 + ",runScore=" + j + ",cpuPercentScore=" + i19 + ",runningThreadCountScore=" + i18 + ",vmThreadCountScore=" + i5 + ", gcCountScore=" + i4 + ",ioScore=" + i3 + ", sysloadavgScore=" + i7 + ",sheduleScore=" + i8 + ",ioTimeScore=" + i6);
        }
        return i25;
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:0x0168  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0124  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x012c  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x0162  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int evaluateSystemScore(com.taobao.onlinemonitor.OnLineMonitor r18) {
        /*
            r17 = this;
            r0 = r18
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            short r2 = r0.mDevicesScore
            long r2 = r0.mDeviceTotalMemory
            r4 = 0
            r6 = 5
            r7 = 6
            r8 = 7
            r9 = 8
            r10 = 9
            r11 = 2
            r12 = 1
            r13 = 3
            r14 = 10
            int r15 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r15 <= 0) goto L_0x00b1
            long r2 = r0.mAvailMemory
            r4 = 100
            long r2 = r2 * r4
            long r4 = r0.mDeviceTotalMemory
            long r2 = r2 / r4
            int r2 = (int) r2
            r3 = 60
            if (r2 >= r3) goto L_0x00ae
            long r3 = r0.mAvailMemory
            r15 = 1500(0x5dc, double:7.41E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0034
            goto L_0x00ae
        L_0x0034:
            r3 = 55
            if (r2 >= r3) goto L_0x00ab
            long r3 = r0.mAvailMemory
            r15 = 1300(0x514, double:6.423E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0042
            goto L_0x00ab
        L_0x0042:
            r3 = 50
            if (r2 >= r3) goto L_0x00a8
            long r3 = r0.mAvailMemory
            r15 = 1100(0x44c, double:5.435E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x004f
            goto L_0x00a8
        L_0x004f:
            r3 = 45
            if (r2 >= r3) goto L_0x00a6
            long r3 = r0.mAvailMemory
            r15 = 900(0x384, double:4.447E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x005c
            goto L_0x00a6
        L_0x005c:
            r3 = 40
            if (r2 >= r3) goto L_0x00a4
            long r3 = r0.mAvailMemory
            r15 = 800(0x320, double:3.953E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0069
            goto L_0x00a4
        L_0x0069:
            r3 = 35
            if (r2 >= r3) goto L_0x00a2
            long r3 = r0.mAvailMemory
            r15 = 700(0x2bc, double:3.46E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0076
            goto L_0x00a2
        L_0x0076:
            r3 = 30
            if (r2 >= r3) goto L_0x00a0
            long r3 = r0.mAvailMemory
            r15 = 600(0x258, double:2.964E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0083
            goto L_0x00a0
        L_0x0083:
            r3 = 25
            if (r2 >= r3) goto L_0x009e
            long r3 = r0.mAvailMemory
            r15 = 500(0x1f4, double:2.47E-321)
            int r5 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r5 < 0) goto L_0x0090
            goto L_0x009e
        L_0x0090:
            r3 = 20
            if (r2 >= r3) goto L_0x009c
            long r2 = r0.mAvailMemory
            r4 = 400(0x190, double:1.976E-321)
            int r15 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r15 < 0) goto L_0x00b1
        L_0x009c:
            r2 = 2
            goto L_0x00b2
        L_0x009e:
            r2 = 3
            goto L_0x00b2
        L_0x00a0:
            r2 = 4
            goto L_0x00b2
        L_0x00a2:
            r2 = 5
            goto L_0x00b2
        L_0x00a4:
            r2 = 6
            goto L_0x00b2
        L_0x00a6:
            r2 = 7
            goto L_0x00b2
        L_0x00a8:
            r2 = 8
            goto L_0x00b2
        L_0x00ab:
            r2 = 9
            goto L_0x00b2
        L_0x00ae:
            r2 = 10
            goto L_0x00b2
        L_0x00b1:
            r2 = 1
        L_0x00b2:
            boolean r3 = r0.mIsLowMemroy
            if (r3 == 0) goto L_0x00ba
            if (r2 <= r11) goto L_0x00ba
            int r2 = r2 + -2
        L_0x00ba:
            int r3 = r0.mSysCPUPercent
            int r3 = r3 / r14
            int r3 = 10 - r3
            int r3 = r3 + r13
            if (r3 >= 0) goto L_0x00c3
            r3 = 0
        L_0x00c3:
            if (r3 <= r14) goto L_0x00c7
            r3 = 10
        L_0x00c7:
            com.taobao.onlinemonitor.ProcessCpuTracker r4 = r0.mProcessCpuTracker
            int r4 = r4.mTotalIoWaitPercent
            int r4 = r4 / r14
            int r4 = 10 - r4
            if (r4 >= 0) goto L_0x00d1
            r4 = 0
        L_0x00d1:
            float[] r5 = r0.mSystemLoadAvg
            r1 = r5[r1]
            short r5 = r0.mCpuProcessCount
            float r5 = (float) r5
            float r1 = r1 / r5
            r5 = 0
            int r11 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r11 >= 0) goto L_0x00df
            r1 = 0
        L_0x00df:
            r5 = 1065353216(0x3f800000, float:1.0)
            int r11 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r11 > 0) goto L_0x00e8
            r6 = 10
            goto L_0x0122
        L_0x00e8:
            r11 = 1073741824(0x40000000, float:2.0)
            int r11 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r11 > 0) goto L_0x00f1
            r6 = 9
            goto L_0x0122
        L_0x00f1:
            r10 = 1075838976(0x40200000, float:2.5)
            int r10 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            if (r10 > 0) goto L_0x00fa
            r6 = 8
            goto L_0x0122
        L_0x00fa:
            r9 = 1077936128(0x40400000, float:3.0)
            int r9 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r9 > 0) goto L_0x0102
            r6 = 7
            goto L_0x0122
        L_0x0102:
            r8 = 1080033280(0x40600000, float:3.5)
            int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r8 > 0) goto L_0x010a
            r6 = 6
            goto L_0x0122
        L_0x010a:
            r7 = 1082130432(0x40800000, float:4.0)
            int r7 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r7 > 0) goto L_0x0111
            goto L_0x0122
        L_0x0111:
            r6 = 1083179008(0x40900000, float:4.5)
            int r6 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x0119
            r6 = 4
            goto L_0x0122
        L_0x0119:
            r6 = 1084227584(0x40a00000, float:5.0)
            int r1 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r1 > 0) goto L_0x0121
            r6 = 3
            goto L_0x0122
        L_0x0121:
            r6 = 1
        L_0x0122:
            if (r3 <= 0) goto L_0x012a
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r1 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r1 = r1.performanceInfo
            r1.cpuPercentScore = r3
        L_0x012a:
            if (r2 <= 0) goto L_0x0132
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r1 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r1 = r1.performanceInfo
            r1.memPercentScore = r2
        L_0x0132:
            if (r4 <= 0) goto L_0x013a
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r1 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r1 = r1.performanceInfo
            r1.ioWaitScore = r4
        L_0x013a:
            float r1 = (float) r2
            r7 = 1069547520(0x3fc00000, float:1.5)
            float r1 = r1 * r7
            float r8 = (float) r4
            float r8 = r8 * r5
            float r1 = r1 + r8
            float r8 = (float) r3
            float r8 = r8 * r7
            float r1 = r1 + r8
            float r7 = (float) r6
            float r7 = r7 * r5
            float r1 = r1 + r7
            int r5 = (int) r1
            r7 = r17
            r7.mNoDeviceSystemScore = r5
            short r5 = r0.mDevicesScore
            float r5 = (float) r5
            r8 = 1056964608(0x3f000000, float:0.5)
            float r5 = r5 * r8
            float r1 = r1 + r5
            int r1 = java.lang.Math.round(r1)
            float r1 = (float) r1
            int r1 = (int) r1
            r5 = 100
            if (r1 <= r5) goto L_0x0164
            r1 = 100
        L_0x0164:
            boolean r5 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug
            if (r5 == 0) goto L_0x01ab
            java.lang.String r5 = "OnLineMonitor"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "系统分="
            r9.append(r10)
            r9.append(r1)
            java.lang.String r10 = ",DevicesScore="
            r9.append(r10)
            short r0 = r0.mDevicesScore
            float r0 = (float) r0
            float r0 = r0 * r8
            r9.append(r0)
            java.lang.String r0 = ",memUseScore="
            r9.append(r0)
            r9.append(r2)
            java.lang.String r0 = ",ioScore="
            r9.append(r0)
            r9.append(r4)
            java.lang.String r0 = ",cpuPercentScore="
            r9.append(r0)
            r9.append(r3)
            java.lang.String r0 = ",sysloadavgScore="
            r9.append(r0)
            r9.append(r6)
            java.lang.String r0 = r9.toString()
            android.util.Log.e(r5, r0)
        L_0x01ab:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.EvaluateScore.evaluateSystemScore(com.taobao.onlinemonitor.OnLineMonitor):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00fd  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x018b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int evaluateDeviceScore(com.taobao.onlinemonitor.OnLineMonitor r21, com.taobao.onlinemonitor.HardWareInfo r22) {
        /*
            r20 = this;
            r0 = r21
            r1 = r22
            if (r0 != 0) goto L_0x0008
            r0 = 0
            return r0
        L_0x0008:
            com.taobao.onlinemonitor.evaluate.HardwareBrand r2 = new com.taobao.onlinemonitor.evaluate.HardwareBrand
            r2.<init>()
            int r2 = r2.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareCpuCount r3 = new com.taobao.onlinemonitor.evaluate.HardwareCpuCount
            r3.<init>()
            int r3 = r3.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareCpu r4 = new com.taobao.onlinemonitor.evaluate.HardwareCpu
            r4.<init>()
            int r5 = r4.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareOsVersion r6 = new com.taobao.onlinemonitor.evaluate.HardwareOsVersion
            r6.<init>()
            int r6 = r6.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareTotalMemory r7 = new com.taobao.onlinemonitor.evaluate.HardwareTotalMemory
            r7.<init>()
            long r8 = r0.mDeviceTotalMemory
            r7.mDeviceTotalMemory = r8
            int r7 = r7.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareJavaMemory r8 = new com.taobao.onlinemonitor.evaluate.HardwareJavaMemory
            r8.<init>()
            int r9 = r0.mJavaHeapLimitMemory
            r8.mJavaHeapLimitMemory = r9
            int r9 = r0.mJavaHeapLimitLargeMemory
            r8.mJavaHeapLimitLargeMemory = r9
            int r8 = r8.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareScreen r9 = new com.taobao.onlinemonitor.evaluate.HardwareScreen
            r9.<init>()
            int r9 = r9.getScore(r1)
            java.io.File r11 = android.os.Environment.getDataDirectory()     // Catch:{ Exception -> 0x0097 }
            java.lang.String r11 = r11.getPath()     // Catch:{ Exception -> 0x0097 }
            android.os.StatFs r12 = new android.os.StatFs     // Catch:{ Exception -> 0x0097 }
            r12.<init>(r11)     // Catch:{ Exception -> 0x0097 }
            int r11 = r12.getBlockSize()     // Catch:{ Exception -> 0x0097 }
            long r13 = (long) r11     // Catch:{ Exception -> 0x0097 }
            int r11 = r12.getBlockCount()     // Catch:{ Exception -> 0x0097 }
            long r10 = (long) r11     // Catch:{ Exception -> 0x0097 }
            long r10 = r10 * r13
            r16 = 1024(0x400, double:5.06E-321)
            long r10 = r10 / r16
            long r10 = r10 / r16
            long r10 = r10 / r16
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r15 = r0.mOnLineStat     // Catch:{ Exception -> 0x0097 }
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r15 = r15.deviceInfo     // Catch:{ Exception -> 0x0097 }
            int r10 = (int) r10     // Catch:{ Exception -> 0x0097 }
            r15.storeTotalSize = r10     // Catch:{ Exception -> 0x0097 }
            int r11 = r12.getAvailableBlocks()     // Catch:{ Exception -> 0x0097 }
            long r11 = (long) r11     // Catch:{ Exception -> 0x0097 }
            long r13 = r13 * r11
            long r13 = r13 / r16
            long r13 = r13 / r16
            long r13 = r13 / r16
            com.taobao.onlinemonitor.evaluate.HardwareStorage r11 = new com.taobao.onlinemonitor.evaluate.HardwareStorage     // Catch:{ Exception -> 0x0097 }
            r11.<init>()     // Catch:{ Exception -> 0x0097 }
            r11.mInnerSize = r10     // Catch:{ Exception -> 0x0097 }
            int r10 = (int) r13     // Catch:{ Exception -> 0x0097 }
            r11.mInnerFree = r10     // Catch:{ Exception -> 0x0097 }
            int r10 = r11.getScore(r1)     // Catch:{ Exception -> 0x0097 }
            goto L_0x0098
        L_0x0097:
            r10 = 1
        L_0x0098:
            java.lang.String r12 = r0.mOpenGlVersion     // Catch:{ Exception -> 0x00a3 }
            if (r12 == 0) goto L_0x00a3
            java.lang.String r12 = r0.mOpenGlVersion     // Catch:{ Exception -> 0x00a3 }
            float r12 = java.lang.Float.parseFloat(r12)     // Catch:{ Exception -> 0x00a3 }
            goto L_0x00a5
        L_0x00a3:
            r12 = 1073741824(0x40000000, float:2.0)
        L_0x00a5:
            com.taobao.onlinemonitor.evaluate.HardwareOpenGL r13 = new com.taobao.onlinemonitor.evaluate.HardwareOpenGL
            r13.<init>()
            r13.mOpenglv = r12
            int r12 = r13.getScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareUseTime r13 = new com.taobao.onlinemonitor.evaluate.HardwareUseTime
            r13.<init>()
            int r13 = r13.getScore(r1)
            int r4 = r4.getCpuHzScore(r1)
            com.taobao.onlinemonitor.evaluate.HardwareGpu r14 = new com.taobao.onlinemonitor.evaluate.HardwareGpu
            r14.<init>()
            int r14 = r14.getScore(r1)
            r15 = 1056964608(0x3f000000, float:0.5)
            if (r14 != 0) goto L_0x00d8
            float r14 = (float) r5
            float r14 = r14 * r15
            float r11 = (float) r3
            float r11 = r11 * r15
            float r14 = r14 + r11
            float r11 = (float) r4
            r16 = 1048576000(0x3e800000, float:0.25)
            float r11 = r11 * r16
            float r14 = r14 + r11
            int r14 = (int) r14
        L_0x00d8:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r11 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r11 = r11.performanceInfo
            int r15 = r5 * 2
            float r15 = (float) r15
            float r1 = (float) r3
            r16 = 1056964608(0x3f000000, float:0.5)
            float r1 = r1 * r16
            float r15 = r15 + r1
            r18 = r3
            float r3 = (float) r4
            float r3 = r3 * r16
            float r15 = r15 + r3
            int r3 = java.lang.Math.round(r15)
            int r3 = r3 / 3
            r11.cpuScore = r3
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            int r3 = r3.cpuScore
            r11 = 10
            if (r3 <= r11) goto L_0x0103
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            r3.cpuScore = r11
        L_0x0103:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            float r15 = (float) r7
            r16 = 1069547520(0x3fc00000, float:1.5)
            float r15 = r15 * r16
            float r11 = (float) r8
            r16 = 1056964608(0x3f000000, float:0.5)
            float r11 = r11 * r16
            float r16 = r15 + r11
            int r16 = java.lang.Math.round(r16)
            r19 = r4
            int r4 = r16 / 2
            r3.memScore = r4
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            int r3 = r3.memScore
            r4 = 10
            if (r3 <= r4) goto L_0x012d
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            r3.memScore = r4
        L_0x012d:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            r3.gpuScore = r14
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            int r3 = r3.gpuScore
            if (r3 <= r4) goto L_0x0141
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            r3.gpuScore = r4
        L_0x0141:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            r3.eglScore = r12
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r3 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$PerformanceInfo r3 = r3.performanceInfo
            java.lang.String r4 = r0.mOpenGlVersion
            r3.openglVersion = r4
            int r2 = r2 + r14
            float r2 = (float) r2
            float r2 = r2 + r1
            float r1 = (float) r5
            r3 = 1073741824(0x40000000, float:2.0)
            float r1 = r1 * r3
            float r2 = r2 + r1
            float r1 = (float) r6
            float r2 = r2 + r1
            float r2 = r2 + r15
            float r2 = r2 + r11
            float r1 = (float) r9
            r3 = 1056964608(0x3f000000, float:0.5)
            float r1 = r1 * r3
            float r2 = r2 + r1
            float r1 = (float) r10
            float r1 = r1 * r3
            float r2 = r2 + r1
            float r1 = (float) r12
            float r1 = r1 * r3
            float r2 = r2 + r1
            float r1 = (float) r13
            float r2 = r2 + r1
            int r1 = java.lang.Math.round(r2)
            float r1 = (float) r1
            int r1 = (int) r1
            r2 = 100
            if (r1 <= r2) goto L_0x0178
            r1 = 100
        L_0x0178:
            com.taobao.onlinemonitor.OnLineMonitor$OnLineStat r2 = r0.mOnLineStat
            com.taobao.onlinemonitor.OnLineMonitor$DeviceInfo r2 = r2.deviceInfo
            java.lang.String r3 = r22.getCpuArch()
            r2.cpuArch = r3
            com.taobao.onlinemonitor.HardWareInfo r0 = r0.mHardWareInfo
            r0.saveCpuAndGpuInfo()
            boolean r0 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug
            if (r0 == 0) goto L_0x01fd
            java.lang.String r0 = "OnLineMonitor"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "设备分="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r3 = ",apiScore="
            r2.append(r3)
            r2.append(r6)
            java.lang.String r3 = ",memScore="
            r2.append(r3)
            r2.append(r7)
            java.lang.String r3 = ",memLimitScore="
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = ", cpuModelScore="
            r2.append(r3)
            r2.append(r5)
            java.lang.String r3 = ",cpuCountScore="
            r2.append(r3)
            r3 = r18
            r2.append(r3)
            java.lang.String r3 = ", CpuHzScore="
            r2.append(r3)
            r3 = r19
            r2.append(r3)
            java.lang.String r3 = ",GpuScore="
            r2.append(r3)
            r2.append(r14)
            java.lang.String r3 = ",screenScore="
            r2.append(r3)
            r2.append(r9)
            java.lang.String r3 = ", openglScore="
            r2.append(r3)
            r2.append(r12)
            java.lang.String r3 = ",storeScore="
            r2.append(r3)
            r2.append(r10)
            java.lang.String r3 = ",useTimeScore="
            r2.append(r3)
            r2.append(r13)
            java.lang.String r2 = r2.toString()
            android.util.Log.e(r0, r2)
        L_0x01fd:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.EvaluateScore.evaluateDeviceScore(com.taobao.onlinemonitor.OnLineMonitor, com.taobao.onlinemonitor.HardWareInfo):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0179, code lost:
        if (r4 >= 0) goto L_0x017c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a1, code lost:
        if (r3 < 0) goto L_0x00ad;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0160 A[LOOP:0: B:98:0x015b->B:100:0x0160, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0103  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0114  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x013b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int evaluateActivityScore(com.taobao.onlinemonitor.OnLineMonitor r37, com.taobao.onlinemonitor.OnLineMonitor.ActivityRuntimeInfo r38) {
        /*
            r36 = this;
            r0 = r37
            r1 = r38
            if (r0 == 0) goto L_0x03e9
            if (r1 == 0) goto L_0x03e9
            int r3 = r1.activityVisibleViewCount
            if (r3 == 0) goto L_0x03e9
            int r3 = r1.loadTime
            if (r3 == 0) goto L_0x03e9
            short r3 = r1.maxLayoutDepth
            if (r3 != 0) goto L_0x0016
            goto L_0x03e9
        L_0x0016:
            int r3 = r1.activityViewCount
            int r3 = r3 + -20
            int r3 = r3 / 50
            r4 = 10
            int r3 = 10 - r3
            if (r3 >= 0) goto L_0x0023
            r3 = 0
        L_0x0023:
            short r5 = r0.mDevicesScore
            r6 = 100
            int r5 = 100 - r5
            short r7 = r0.mAvgSystemRunningScore
            int r7 = 100 - r7
            int r8 = r5 * 10
            int r9 = r8 + 300
            int r10 = r7 * 10
            int r9 = r9 + r10
            r11 = 1000(0x3e8, float:1.401E-42)
            int r8 = r8 + r11
            int r8 = r8 + r10
            int r10 = r1.loadTime
            r12 = 6
            r15 = 2
            if (r10 <= 0) goto L_0x0050
            if (r9 > 0) goto L_0x0042
            r9 = 300(0x12c, float:4.2E-43)
        L_0x0042:
            int r10 = r1.loadTime
            int r10 = r10 / r9
            int r9 = 10 - r10
            if (r9 >= 0) goto L_0x004c
            r17 = r3
            goto L_0x005b
        L_0x004c:
            r17 = r3
            r2 = r9
            goto L_0x0094
        L_0x0050:
            r17 = r3
            long r2 = r1.stayTime
            int r10 = r9 * 5
            long r13 = (long) r10
            int r10 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1))
            if (r10 < 0) goto L_0x005d
        L_0x005b:
            r2 = 0
            goto L_0x0094
        L_0x005d:
            long r2 = r1.stayTime
            int r10 = r9 * 4
            long r13 = (long) r10
            int r10 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1))
            if (r10 < 0) goto L_0x0068
            r2 = 2
            goto L_0x0094
        L_0x0068:
            long r2 = r1.stayTime
            int r10 = r9 * 3
            long r13 = (long) r10
            int r10 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1))
            if (r10 < 0) goto L_0x0073
            r2 = 4
            goto L_0x0094
        L_0x0073:
            long r2 = r1.stayTime
            int r10 = r9 * 2
            long r13 = (long) r10
            int r10 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1))
            if (r10 < 0) goto L_0x007e
            r2 = 6
            goto L_0x0094
        L_0x007e:
            long r2 = r1.stayTime
            long r9 = (long) r9
            int r13 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r13 < 0) goto L_0x0088
            r2 = 8
            goto L_0x0094
        L_0x0088:
            long r2 = r1.stayTime
            r9 = 1000(0x3e8, double:4.94E-321)
            int r13 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r13 < 0) goto L_0x0093
            r2 = 10
            goto L_0x0094
        L_0x0093:
            r2 = 1
        L_0x0094:
            int r3 = r1.idleTime
            if (r3 <= 0) goto L_0x00a4
            if (r8 > 0) goto L_0x009c
            r8 = 1000(0x3e8, float:1.401E-42)
        L_0x009c:
            int r3 = r1.idleTime
            int r3 = r3 / r8
            int r3 = 10 - r3
            if (r3 >= 0) goto L_0x00e4
            goto L_0x00ad
        L_0x00a4:
            long r9 = r1.stayTime
            int r3 = r8 * 5
            long r13 = (long) r3
            int r3 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r3 < 0) goto L_0x00af
        L_0x00ad:
            r3 = 0
            goto L_0x00e4
        L_0x00af:
            long r9 = r1.stayTime
            int r3 = r8 * 4
            long r13 = (long) r3
            int r3 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r3 < 0) goto L_0x00ba
            r3 = 2
            goto L_0x00e4
        L_0x00ba:
            long r9 = r1.stayTime
            int r3 = r8 * 3
            long r13 = (long) r3
            int r3 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r3 < 0) goto L_0x00c5
        L_0x00c3:
            r3 = 4
            goto L_0x00e4
        L_0x00c5:
            long r9 = r1.stayTime
            int r3 = r8 * 2
            long r13 = (long) r3
            int r3 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r3 < 0) goto L_0x00d0
            r3 = 6
            goto L_0x00e4
        L_0x00d0:
            long r9 = r1.stayTime
            long r13 = (long) r8
            int r3 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r3 < 0) goto L_0x00da
            r3 = 8
            goto L_0x00e4
        L_0x00da:
            long r8 = r1.stayTime
            r10 = 1000(0x3e8, double:4.94E-321)
            int r3 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r3 < 0) goto L_0x00c3
            r3 = 10
        L_0x00e4:
            short r8 = r1.maxIdleDelayTime
            int r8 = r8 / 50
            int r8 = 10 - r8
            if (r8 >= 0) goto L_0x00ed
            r8 = 0
        L_0x00ed:
            short r9 = r1.maxLayoutDepth
            int r9 = r9 + -9
            int r9 = 10 - r9
            if (r9 >= r4) goto L_0x00f6
            r9 = 0
        L_0x00f6:
            int r10 = r1.activityVisibleViewCount
            int r10 = r10 / r6
            short r11 = r1.layoutTimesOnLoad
            int r10 = r10 - r11
            if (r10 >= 0) goto L_0x00ff
            r10 = 0
        L_0x00ff:
            int r10 = 10 - r10
            if (r10 >= r4) goto L_0x0104
            r10 = 0
        L_0x0104:
            short r11 = r1.totalLayoutCount
            int r11 = r11 / r12
            int r11 = 10 - r11
            if (r11 >= r4) goto L_0x010c
            r11 = 0
        L_0x010c:
            short r12 = r1.redundantLayout
            int r12 = r12 / 5
            int r12 = 10 - r12
            if (r12 >= 0) goto L_0x0115
            r12 = 0
        L_0x0115:
            short r13 = r1.maxRelativeLayoutDepth
            int r13 = r13 * 2
            int r13 = 10 - r13
            if (r13 >= 0) goto L_0x011e
            r13 = 0
        L_0x011e:
            short r14 = r1.memEnd
            short r6 = r1.memStart
            int r14 = r14 - r6
            int r14 = r14 / r15
            int r6 = 10 - r14
            if (r6 >= 0) goto L_0x0129
            r6 = 0
        L_0x0129:
            short r14 = r1.memMax
            short r15 = r1.memStart
            int r14 = r14 - r15
            r15 = 4
            int r14 = r14 / r15
            int r14 = 10 - r14
            if (r14 >= 0) goto L_0x0135
            r14 = 0
        L_0x0135:
            int r15 = r1.threadInterval
            int r15 = 10 - r15
            if (r15 >= 0) goto L_0x013c
            r15 = 0
        L_0x013c:
            int r4 = r1.dragFlingCount
            r21 = r7
            r7 = 3
            r19 = 0
            if (r4 <= 0) goto L_0x017b
            int r4 = r1.avgSm
            if (r4 <= 0) goto L_0x017b
            int r4 = r1.avgSm
            int r4 = 60 - r4
            int r4 = r4 / r7
            r20 = 10
            int r4 = 10 - r4
            r4 = 1028443341(0x3d4ccccd, float:0.05)
            r4 = 0
            r22 = 1028443341(0x3d4ccccd, float:0.05)
            r23 = 0
        L_0x015b:
            int[] r7 = r1.activityBadSmoothStepCount
            int r7 = r7.length
            if (r4 >= r7) goto L_0x0171
            int[] r7 = r1.activityBadSmoothStepCount
            r7 = r7[r4]
            float r7 = (float) r7
            float r7 = r7 * r22
            float r23 = r23 + r7
            r7 = 1028443341(0x3d4ccccd, float:0.05)
            float r22 = r22 + r7
            int r4 = r4 + 1
            goto L_0x015b
        L_0x0171:
            int r4 = java.lang.Math.round(r23)
            r7 = 10
            int r4 = 10 - r4
            if (r4 >= 0) goto L_0x017c
        L_0x017b:
            r4 = 0
        L_0x017c:
            int r7 = r1.activityVisibleViewCount
            if (r7 <= 0) goto L_0x019f
            short r7 = r1.measureTimes
            r24 = r5
            int r5 = r1.activityVisibleViewCount
            int r7 = r7 / r5
            r5 = 1
            int r7 = r7 - r5
            float r5 = (float) r7
            int r7 = (r5 > r19 ? 1 : (r5 == r19 ? 0 : -1))
            if (r7 >= 0) goto L_0x018f
            r5 = 0
        L_0x018f:
            r7 = 1077936128(0x40400000, float:3.0)
            float r5 = r5 * r7
            int r5 = java.lang.Math.round(r5)
            r7 = 10
            int r5 = 10 - r5
            if (r5 >= 0) goto L_0x01a2
            r5 = 0
            goto L_0x01a2
        L_0x019f:
            r24 = r5
            r5 = 1
        L_0x01a2:
            int r7 = r1.dragFlingCount
            if (r7 <= 0) goto L_0x01c2
            int r5 = r1.activityTotalSmLayoutTimes
            int r7 = r1.dragFlingCount
            int r5 = r5 / r7
            r7 = 1
            int r5 = r5 - r7
            float r5 = (float) r5
            int r7 = (r5 > r19 ? 1 : (r5 == r19 ? 0 : -1))
            if (r7 >= 0) goto L_0x01b3
            r5 = 0
        L_0x01b3:
            r7 = 1077936128(0x40400000, float:3.0)
            float r5 = r5 * r7
            int r5 = java.lang.Math.round(r5)
            r7 = 10
            int r5 = 10 - r5
            if (r5 >= 0) goto L_0x01c2
            r5 = 0
        L_0x01c2:
            boolean r7 = r0.mFileSchedIsNotExists
            if (r7 != 0) goto L_0x0206
            float r7 = r0.mPidPerCpuLoad
            r25 = r4
            short r4 = r0.mCpuProcessCount
            float r4 = (float) r4
            float r4 = r7 / r4
            int r7 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r7 >= 0) goto L_0x01d4
            goto L_0x01d6
        L_0x01d4:
            r19 = r4
        L_0x01d6:
            int r4 = java.lang.Math.round(r19)
            r7 = 10
            int r4 = 10 - r4
            if (r4 >= 0) goto L_0x01e1
            r4 = 0
        L_0x01e1:
            int r7 = r0.mPidIoWaitCountOld
            r26 = r4
            int r4 = r0.mPidIoWaitCount
            int r7 = r7 - r4
            int r4 = r0.mPidIoWaitSum
            r27 = r8
            int r8 = r0.mPidIoWaitSumOld
            int r4 = r4 - r8
            r8 = 10
            int r7 = r7 / r8
            int r7 = 10 - r7
            if (r7 >= 0) goto L_0x01f7
            r7 = 0
        L_0x01f7:
            r18 = 100
            int r4 = r4 / 100
            int r4 = 10 - r4
            if (r4 >= 0) goto L_0x0200
            r4 = 0
        L_0x0200:
            int r7 = r7 + r4
            r4 = 2
            int r7 = r7 / r4
            r4 = r26
            goto L_0x0230
        L_0x0206:
            r25 = r4
            r27 = r8
            r8 = 10
            com.taobao.onlinemonitor.ProcessCpuTracker r4 = r0.mProcessCpuTracker
            int r4 = r4.mRelIoWaitTime
            int r4 = r4 / 20
            int r7 = 10 - r4
            float[] r4 = r0.mSystemLoadAvg
            r16 = 0
            r4 = r4[r16]
            short r8 = r0.mCpuProcessCount
            float r8 = (float) r8
            float r4 = r4 / r8
            int r8 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r8 >= 0) goto L_0x0223
            goto L_0x0225
        L_0x0223:
            r19 = r4
        L_0x0225:
            int r4 = java.lang.Math.round(r19)
            r8 = 10
            int r4 = 10 - r4
            if (r4 >= 0) goto L_0x0230
            r4 = 0
        L_0x0230:
            int r0 = r0.mMyPidCPUPercent
            int r0 = r0 / r8
            int r0 = 10 - r0
            if (r0 >= 0) goto L_0x023b
            r28 = r7
            r0 = 0
            goto L_0x023d
        L_0x023b:
            r28 = r7
        L_0x023d:
            int r7 = r1.smoothViewOutRevLayoutDepth
            r18 = 3
            int r7 = r7 * 3
            int r7 = 10 - r7
            if (r7 >= 0) goto L_0x024d
            r29 = r3
            r7 = r17
            r8 = 0
            goto L_0x0252
        L_0x024d:
            r29 = r3
            r8 = r7
            r7 = r17
        L_0x0252:
            float r3 = (float) r7
            r16 = 1050253722(0x3e99999a, float:0.3)
            float r3 = r3 * r16
            r30 = r7
            float r7 = (float) r12
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r8
            r16 = 1053609165(0x3ecccccd, float:0.4)
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r10
            r16 = 1056964608(0x3f000000, float:0.5)
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r11
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r13
            r17 = 1058642330(0x3f19999a, float:0.6)
            float r7 = r7 * r17
            float r3 = r3 + r7
            float r7 = (float) r9
            r17 = 1053609165(0x3ecccccd, float:0.4)
            float r7 = r7 * r17
            float r3 = r3 + r7
            float r7 = (float) r5
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r15
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r0
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r4
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r6
            float r7 = r7 * r16
            float r3 = r3 + r7
            float r7 = (float) r14
            float r7 = r7 * r16
            float r3 = r3 + r7
            int r7 = r1.dragFlingCount
            if (r7 <= 0) goto L_0x02c1
            int r1 = r1.avgSm
            if (r1 <= 0) goto L_0x02c1
            float r1 = (float) r2
            float r3 = r3 + r1
            r1 = r29
            float r7 = (float) r1
            float r7 = r7 * r16
            float r3 = r3 + r7
            r31 = r14
            r7 = r27
            float r14 = (float) r7
            float r14 = r14 * r16
            float r3 = r3 + r14
            r32 = r6
            r14 = 3
            float r6 = (float) r14
            float r6 = r6 * r16
            float r3 = r3 + r6
            r6 = r25
            float r14 = (float) r6
            float r14 = r14 * r16
            float r3 = r3 + r14
            r33 = r4
            r14 = r28
            float r4 = (float) r14
            float r3 = r3 + r4
            goto L_0x02e3
        L_0x02c1:
            r33 = r4
            r32 = r6
            r31 = r14
            r6 = r25
            r7 = r27
            r14 = r28
            r1 = r29
            float r4 = (float) r2
            r17 = 1067869798(0x3fa66666, float:1.3)
            float r4 = r4 * r17
            float r3 = r3 + r4
            float r4 = (float) r1
            float r3 = r3 + r4
            float r4 = (float) r7
            float r4 = r4 * r16
            float r3 = r3 + r4
            float r4 = (float) r14
            r16 = 1067030938(0x3f99999a, float:1.2)
            float r4 = r4 * r16
            float r3 = r3 + r4
        L_0x02e3:
            int r3 = java.lang.Math.round(r3)
            float r3 = (float) r3
            boolean r4 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug
            if (r4 == 0) goto L_0x0307
            java.lang.String r4 = "OnLineMonitor"
            r34 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r35 = r15
            java.lang.String r15 = "界面分初始="
            r0.append(r15)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            android.util.Log.e(r4, r0)
            goto L_0x030b
        L_0x0307:
            r34 = r0
            r35 = r15
        L_0x030b:
            r0 = r24
            float r0 = (float) r0
            r4 = 1041865114(0x3e19999a, float:0.15)
            float r0 = r0 * r4
            float r3 = r3 + r0
            r0 = r21
            float r0 = (float) r0
            float r0 = r0 * r4
            float r3 = r3 + r0
            int r0 = (int) r3
            r3 = 100
            if (r0 <= r3) goto L_0x0321
            r0 = 100
        L_0x0321:
            boolean r3 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug
            if (r3 == 0) goto L_0x03e8
            java.lang.String r3 = "OnLineMonitor"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r15 = "界面分="
            r4.append(r15)
            r4.append(r0)
            java.lang.String r15 = ", loadScore="
            r4.append(r15)
            r4.append(r2)
            java.lang.String r2 = ", idleScore="
            r4.append(r2)
            r4.append(r1)
            java.lang.String r1 = ",idleMaxDelayScore="
            r4.append(r1)
            r4.append(r7)
            java.lang.String r1 = ",badLayoutTimesScore="
            r4.append(r1)
            r1 = 3
            r4.append(r1)
            java.lang.String r1 = ",badSmScore="
            r4.append(r1)
            r4.append(r6)
            java.lang.String r1 = ",ioScore="
            r4.append(r1)
            r4.append(r14)
            java.lang.String r1 = ",viewCountScore="
            r4.append(r1)
            r2 = r30
            r4.append(r2)
            java.lang.String r1 = ",redundantLayoutScore="
            r4.append(r1)
            r4.append(r12)
            java.lang.String r1 = ",outReLayoutDepthScore="
            r4.append(r1)
            r4.append(r8)
            java.lang.String r1 = ",layoutTimesScore="
            r4.append(r1)
            r4.append(r10)
            java.lang.String r1 = ",totalLayoutCountScore="
            r4.append(r1)
            r4.append(r11)
            java.lang.String r1 = ",maxRelativeLayoutDepthScore="
            r4.append(r1)
            r4.append(r13)
            java.lang.String r1 = ",layoutDepthScore="
            r4.append(r1)
            r4.append(r9)
            java.lang.String r1 = ",layoutDepthScore="
            r4.append(r1)
            r4.append(r9)
            java.lang.String r1 = ",measureTimesScore="
            r4.append(r1)
            r4.append(r5)
            java.lang.String r1 = ",threadScore="
            r4.append(r1)
            r15 = r35
            r4.append(r15)
            java.lang.String r1 = ",cpuPercentScore="
            r4.append(r1)
            r1 = r34
            r4.append(r1)
            java.lang.String r1 = ",sysLoadavgScore="
            r4.append(r1)
            r1 = r33
            r4.append(r1)
            java.lang.String r1 = ",memScore="
            r4.append(r1)
            r6 = r32
            r4.append(r6)
            java.lang.String r1 = ",memMaxScore="
            r4.append(r1)
            r14 = r31
            r4.append(r14)
            java.lang.String r1 = r4.toString()
            android.util.Log.e(r3, r1)
        L_0x03e8:
            return r0
        L_0x03e9:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.EvaluateScore.evaluateActivityScore(com.taobao.onlinemonitor.OnLineMonitor, com.taobao.onlinemonitor.OnLineMonitor$ActivityRuntimeInfo):int");
    }
}
