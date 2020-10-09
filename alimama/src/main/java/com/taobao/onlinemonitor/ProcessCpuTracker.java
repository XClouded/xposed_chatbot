package com.taobao.onlinemonitor;

import android.os.Process;
import android.system.OsConstants;
import android.util.Log;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProcessCpuTracker implements Serializable {
    static final boolean Debug = false;
    private static final int[] LOAD_AVERAGE_FORMAT = {16416, 16416, 16416};
    static final int[] PROCESS_STATS_FORMAT = {32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224, 32, 32, 32, 32, 8224, 32, 8224, 32};
    static final int[] PROCESS_STATS_FORMAT_NAME = {4128};
    static final int PROCESS_STAT_MAJOR_FAULTS = 1;
    static final int PROCESS_STAT_MINOR_FAULTS = 0;
    static final int PROCESS_STAT_STARTTIME = 5;
    static final int PROCESS_STAT_STIME = 3;
    static final int PROCESS_STAT_THREADCOUNT = 4;
    static final int PROCESS_STAT_UTIME = 2;
    public static final int PROC_COMBINE = 256;
    public static final int PROC_OUT_FLOAT = 16384;
    public static final int PROC_OUT_LONG = 8192;
    public static final int PROC_OUT_STRING = 4096;
    public static final int PROC_PARENS = 512;
    public static final int PROC_QUOTES = 1024;
    public static final int PROC_SPACE_TERM = 32;
    public static final int PROC_TAB_TERM = 9;
    public static final int PROC_TERM_MASK = 255;
    public static final int PROC_ZERO_TERM = 0;
    static final int[] SYSTEM_CPU_FORMAT = {288, 8224, 8224, 8224, 8224, 8224, 8224, 8224};
    static final String TAG = "ProcessCpu";
    long mBaseIdleTime;
    long mBaseIoWaitTime;
    long mBaseIrqTime;
    long mBaseSoftIrqTime;
    long mBaseSystemTime;
    long mBaseUserTime;
    int mCpuCount = 4;
    long mIoWaitTime;
    int mJiffyMillis;
    long mLastCheckTime;
    final float[] mLoadAverageData = new float[3];
    float[] mLoadAverageDataTemp = new float[3];
    long mMajorFault;
    int mMyPidPercent;
    long mPidJiffyTime;
    volatile long mPidRunCpuTime;
    long mPidRunCpuTimeInterval;
    long mPidStartTime;
    String mPidStatFile;
    long mProcessBaseSystemTime;
    long mProcessBaseUserTime;
    int mProcessRelSystemTime;
    int mProcessRelUserTime;
    long mProcessSystemTime;
    long mProcessUserTime;
    Method mReadProcFile;
    int mRelIdleTime;
    int mRelIoWaitTime;
    int mRelIrqTime;
    int mRelSoftIrqTime;
    int mRelSystemTime;
    int mRelUserTime;
    final long[] mStatsData = new long[6];
    final long[] mSysCpu = new long[7];
    float mSystemIoWaitPercent;
    volatile long mSystemRunCpuTime;
    long mSystemRunCpuTimeInterval;
    volatile long mSystemTotalCpuTime;
    long mSystemTotalCpuTimeInterval;
    long mThreadCount;
    int mTotalIoWaitPercent;
    int mTotalSysPercent;

    public ProcessCpuTracker(int i) {
        try {
            this.mPidStatFile = "/proc/" + i + "/stat";
            this.mReadProcFile = Process.class.getMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class});
            this.mReadProcFile.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (OnLineMonitor.sApiLevel < 23 || this.mJiffyMillis != 0) {
                this.mJiffyMillis = 10;
            } else {
                Class<?> cls = Class.forName("libcore.io.Libcore");
                Class<?> cls2 = Class.forName("libcore.io.Os");
                Field declaredField = cls.getDeclaredField("os");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                Method method = cls2.getMethod("sysconf", new Class[]{Integer.TYPE});
                method.setAccessible(true);
                this.mJiffyMillis = (int) (1000 / ((Long) method.invoke(obj, new Object[]{Integer.valueOf(OsConstants._SC_CLK_TCK)})).longValue());
            }
            if (this.mJiffyMillis == 0) {
                this.mJiffyMillis = 10;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            this.mJiffyMillis = 10;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long loadTaskTime(int r9) {
        /*
            r8 = this;
            r0 = 6
            r1 = 0
            long[] r0 = new long[r0]     // Catch:{ Throwable -> 0x0052 }
            java.lang.reflect.Method r3 = r8.mReadProcFile     // Catch:{ Throwable -> 0x0052 }
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0052 }
            r5 = 0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0052 }
            r6.<init>()     // Catch:{ Throwable -> 0x0052 }
            java.lang.String r7 = "/proc/"
            r6.append(r7)     // Catch:{ Throwable -> 0x0052 }
            int r7 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0052 }
            r6.append(r7)     // Catch:{ Throwable -> 0x0052 }
            java.lang.String r7 = "/task/"
            r6.append(r7)     // Catch:{ Throwable -> 0x0052 }
            r6.append(r9)     // Catch:{ Throwable -> 0x0052 }
            java.lang.String r9 = "/stat"
            r6.append(r9)     // Catch:{ Throwable -> 0x0052 }
            java.lang.String r9 = r6.toString()     // Catch:{ Throwable -> 0x0052 }
            r4[r5] = r9     // Catch:{ Throwable -> 0x0052 }
            r9 = 1
            int[] r5 = PROCESS_STATS_FORMAT     // Catch:{ Throwable -> 0x0052 }
            r4[r9] = r5     // Catch:{ Throwable -> 0x0052 }
            r9 = 2
            r5 = 0
            r4[r9] = r5     // Catch:{ Throwable -> 0x0052 }
            r6 = 3
            r4[r6] = r0     // Catch:{ Throwable -> 0x0052 }
            r7 = 4
            r4[r7] = r5     // Catch:{ Throwable -> 0x0052 }
            java.lang.Object r3 = r3.invoke(r5, r4)     // Catch:{ Throwable -> 0x0052 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ Throwable -> 0x0052 }
            boolean r3 = r3.booleanValue()     // Catch:{ Throwable -> 0x0052 }
            if (r3 != 0) goto L_0x004b
            return r1
        L_0x004b:
            r3 = r0[r9]     // Catch:{ Throwable -> 0x0052 }
            r5 = r0[r6]     // Catch:{ Throwable -> 0x0052 }
            r9 = 0
            long r3 = r3 + r5
            return r3
        L_0x0052:
            r9 = move-exception
            r9.printStackTrace()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.ProcessCpuTracker.loadTaskTime(int):long");
    }

    /* access modifiers changed from: package-private */
    public boolean getProcInfo(int i, int i2, String[] strArr, long[] jArr, float[] fArr) {
        boolean z;
        boolean booleanValue;
        boolean z2 = true;
        if (i2 == 0) {
            if (jArr != null) {
                try {
                    Method method = this.mReadProcFile;
                    booleanValue = ((Boolean) method.invoke((Object) null, new Object[]{"/proc/" + i + "/stat", PROCESS_STATS_FORMAT, null, jArr, null})).booleanValue();
                } catch (Throwable th) {
                    th = th;
                    th.printStackTrace();
                    return z2;
                }
            } else {
                booleanValue = true;
            }
            if (booleanValue && strArr != null) {
                try {
                    Method method2 = this.mReadProcFile;
                    return ((Boolean) method2.invoke((Object) null, new Object[]{"/proc/" + i + "/comm", PROCESS_STATS_FORMAT_NAME, strArr, null, null})).booleanValue();
                } catch (Throwable th2) {
                    th = th2;
                    z2 = booleanValue;
                    th.printStackTrace();
                    return z2;
                }
            }
        } else {
            if (jArr != null) {
                Method method3 = this.mReadProcFile;
                z = ((Boolean) method3.invoke((Object) null, new Object[]{"/proc/" + i + "/task/" + i2 + "/stat", PROCESS_STATS_FORMAT, null, jArr, null})).booleanValue();
            } else {
                z = true;
            }
            if (!z || strArr == null) {
                return z;
            }
            try {
                Method method4 = this.mReadProcFile;
                booleanValue = ((Boolean) method4.invoke((Object) null, new Object[]{"/proc/" + i + "/task/" + i2 + "/comm", PROCESS_STATS_FORMAT_NAME, strArr, null, null})).booleanValue();
            } catch (Throwable th3) {
                th = th3;
                z2 = z;
                th.printStackTrace();
                return z2;
            }
        }
        return booleanValue;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadAvgages() {
        /*
            r8 = this;
            float[] r0 = r8.mLoadAverageDataTemp
            r1 = 0
            r2 = 0
            r0[r2] = r1     // Catch:{ Exception -> 0x005d }
            r3 = 1
            r0[r3] = r1     // Catch:{ Exception -> 0x005d }
            r4 = 2
            r0[r4] = r1     // Catch:{ Exception -> 0x005d }
            java.lang.reflect.Method r1 = r8.mReadProcFile     // Catch:{ Exception -> 0x005d }
            r5 = 5
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x005d }
            java.lang.String r6 = "/proc/loadavg"
            r5[r2] = r6     // Catch:{ Exception -> 0x005d }
            int[] r6 = LOAD_AVERAGE_FORMAT     // Catch:{ Exception -> 0x005d }
            r5[r3] = r6     // Catch:{ Exception -> 0x005d }
            r6 = 0
            r5[r4] = r6     // Catch:{ Exception -> 0x005d }
            r7 = 3
            r5[r7] = r6     // Catch:{ Exception -> 0x005d }
            r7 = 4
            r5[r7] = r0     // Catch:{ Exception -> 0x005d }
            java.lang.Object r1 = r1.invoke(r6, r5)     // Catch:{ Exception -> 0x005d }
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch:{ Exception -> 0x005d }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x005d }
            if (r1 == 0) goto L_0x0061
            float[] r1 = r8.mLoadAverageDataTemp     // Catch:{ Exception -> 0x005d }
            r1 = r1[r2]     // Catch:{ Exception -> 0x005d }
            r5 = 1128792064(0x43480000, float:200.0)
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 > 0) goto L_0x005c
            float[] r1 = r8.mLoadAverageDataTemp     // Catch:{ Exception -> 0x005d }
            r1 = r1[r3]     // Catch:{ Exception -> 0x005d }
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 > 0) goto L_0x005c
            float[] r1 = r8.mLoadAverageDataTemp     // Catch:{ Exception -> 0x005d }
            r1 = r1[r4]     // Catch:{ Exception -> 0x005d }
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 <= 0) goto L_0x0049
            goto L_0x005c
        L_0x0049:
            float[] r1 = r8.mLoadAverageData     // Catch:{ Exception -> 0x005d }
            r5 = r0[r2]     // Catch:{ Exception -> 0x005d }
            r1[r2] = r5     // Catch:{ Exception -> 0x005d }
            float[] r1 = r8.mLoadAverageData     // Catch:{ Exception -> 0x005d }
            r2 = r0[r3]     // Catch:{ Exception -> 0x005d }
            r1[r3] = r2     // Catch:{ Exception -> 0x005d }
            float[] r1 = r8.mLoadAverageData     // Catch:{ Exception -> 0x005d }
            r0 = r0[r4]     // Catch:{ Exception -> 0x005d }
            r1[r4] = r0     // Catch:{ Exception -> 0x005d }
            goto L_0x0061
        L_0x005c:
            return
        L_0x005d:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.ProcessCpuTracker.loadAvgages():void");
    }

    public int update() {
        long j;
        long j2;
        int i;
        if (this.mReadProcFile == null || this.mPidStatFile == null) {
            Log.w(TAG, "readProcFile : " + this.mReadProcFile + ", mPidStatFile : " + this.mPidStatFile);
        } else {
            try {
                if (!((Boolean) this.mReadProcFile.invoke((Object) null, new Object[]{"/proc/stat", SYSTEM_CPU_FORMAT, null, this.mSysCpu, null})).booleanValue()) {
                    return 0;
                }
                boolean booleanValue = ((Boolean) this.mReadProcFile.invoke((Object) null, new Object[]{this.mPidStatFile, PROCESS_STATS_FORMAT, null, this.mStatsData, null})).booleanValue();
                loadAvgages();
                if (!booleanValue) {
                    return 0;
                }
                this.mProcessUserTime = this.mStatsData[2];
                this.mProcessSystemTime = this.mStatsData[3];
                this.mMajorFault = this.mStatsData[1];
                this.mThreadCount = this.mStatsData[4];
                this.mPidStartTime = this.mStatsData[5] * ((long) this.mJiffyMillis);
                this.mPidJiffyTime = this.mProcessUserTime + this.mProcessSystemTime;
                if (this.mProcessUserTime < this.mProcessBaseUserTime || this.mProcessSystemTime < this.mProcessBaseSystemTime) {
                    this.mProcessRelUserTime = 0;
                    this.mProcessRelSystemTime = 0;
                } else {
                    this.mProcessRelUserTime = (int) (this.mProcessUserTime - this.mProcessBaseUserTime);
                    this.mProcessRelSystemTime = (int) (this.mProcessSystemTime - this.mProcessBaseSystemTime);
                }
                this.mPidRunCpuTime = this.mProcessUserTime + this.mProcessSystemTime;
                long j3 = this.mSysCpu[0] + this.mSysCpu[1];
                long j4 = this.mSysCpu[2];
                long j5 = this.mSysCpu[3];
                long j6 = this.mSysCpu[4];
                long j7 = this.mSysCpu[5];
                long j8 = this.mSysCpu[6];
                long j9 = j3 + j4;
                this.mSystemRunCpuTime = j9;
                this.mSystemTotalCpuTime = j9 + j5 + j6 + j7 + j8;
                this.mSystemIoWaitPercent = (((float) j6) * 100.0f) / ((float) this.mSystemRunCpuTime);
                this.mIoWaitTime = j6;
                if (j3 < this.mBaseUserTime || j4 < this.mBaseSystemTime || j6 < this.mBaseIoWaitTime || j7 < this.mBaseIrqTime || j8 < this.mBaseSoftIrqTime || j5 < this.mBaseIdleTime) {
                    j2 = j5;
                    j = j8;
                    this.mRelUserTime = 0;
                    this.mRelSystemTime = 0;
                    this.mRelIoWaitTime = 0;
                    this.mRelIrqTime = 0;
                    this.mRelSoftIrqTime = 0;
                    this.mRelIdleTime = 0;
                } else {
                    this.mRelUserTime = (int) (j3 - this.mBaseUserTime);
                    this.mRelSystemTime = (int) (j4 - this.mBaseSystemTime);
                    this.mRelIoWaitTime = (int) (j6 - this.mBaseIoWaitTime);
                    this.mRelIrqTime = (int) (j7 - this.mBaseIrqTime);
                    this.mRelSoftIrqTime = (int) (j8 - this.mBaseSoftIrqTime);
                    this.mRelIdleTime = (int) (j5 - this.mBaseIdleTime);
                    long j10 = (long) (this.mRelUserTime + this.mRelSystemTime + this.mRelIoWaitTime + this.mRelIrqTime + this.mRelSoftIrqTime + this.mRelIdleTime);
                    this.mSystemTotalCpuTimeInterval = j10;
                    j2 = j5;
                    this.mSystemRunCpuTimeInterval = (long) (this.mRelUserTime + this.mRelSystemTime);
                    j = j8;
                    this.mPidRunCpuTimeInterval = ((this.mProcessUserTime + this.mProcessSystemTime) - this.mProcessBaseUserTime) - this.mProcessBaseSystemTime;
                    if (j10 > 1) {
                        int i2 = (int) (((long) ((this.mProcessRelUserTime + this.mProcessRelSystemTime) * 100)) / j10);
                        this.mTotalSysPercent = (int) (((long) ((this.mRelUserTime + this.mRelSystemTime) * 100)) / j10);
                        this.mMyPidPercent = i2;
                        this.mTotalIoWaitPercent = (int) (((long) (this.mRelIoWaitTime * 100)) / j10);
                        if (this.mTotalSysPercent > 100) {
                            this.mTotalSysPercent = 100;
                        }
                        if (this.mMyPidPercent > this.mTotalSysPercent) {
                            this.mMyPidPercent = this.mTotalSysPercent;
                        }
                        i = i2;
                        this.mProcessBaseUserTime = this.mProcessUserTime;
                        this.mProcessBaseSystemTime = this.mProcessSystemTime;
                        this.mBaseUserTime = j3;
                        this.mBaseSystemTime = j4;
                        this.mBaseIoWaitTime = j6;
                        this.mBaseIrqTime = j7;
                        this.mBaseSoftIrqTime = j;
                        this.mBaseIdleTime = j2;
                        return i;
                    }
                }
                i = 0;
                this.mProcessBaseUserTime = this.mProcessUserTime;
                this.mProcessBaseSystemTime = this.mProcessSystemTime;
                this.mBaseUserTime = j3;
                this.mBaseSystemTime = j4;
                this.mBaseIoWaitTime = j6;
                this.mBaseIrqTime = j7;
                this.mBaseSoftIrqTime = j;
                this.mBaseIdleTime = j2;
                return i;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
