package com.ali.telescope.util.process;

import android.os.Process;
import android.system.OsConstants;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.internal.plugins.cpu.CpuRecord;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CpuTracker {
    static final int[] PROCESS_STATS_FORMAT = {32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224, 32, 32, 32, 32, 8224, 32, 8224, 32};
    static final int PROCESS_STAT_MAJOR_FAULTS = 1;
    static final int PROCESS_STAT_MINOR_FAULTS = 0;
    static final int PROCESS_STAT_STARTTIME = 5;
    static final int PROCESS_STAT_STIME = 3;
    static final int PROCESS_STAT_THREADCOUNT = 4;
    static final int PROCESS_STAT_UTIME = 2;
    public static final int PROC_COMBINE = 256;
    public static final int PROC_OUT_LONG = 8192;
    public static final int PROC_PARENS = 512;
    public static final int PROC_SPACE_TERM = 32;
    static final int[] SYSTEM_CPU_FORMAT = {288, 8224, 8224, 8224, 8224, 8224, 8224, 8224};
    static final String TAG = "CpuTracker";
    static long mBaseIdleTime;
    static long mBaseIoWaitTime;
    static long mBaseIrqTime;
    static long mBaseSoftIrqTime;
    static long mBaseSystemTime;
    static long mBaseUserTime;
    static int mJiffyMillis;
    public static long mPidStartTime = 0;
    static String mPidStatFile;
    static long mProcessBaseSystemTime;
    static long mProcessBaseUserTime;
    static Method mReadProcFile;
    static long mThreadCount;
    public static volatile CpuRecord sCpuRecord;

    static {
        try {
            int myPid = Process.myPid();
            mPidStatFile = "/proc/" + myPid + "/stat";
            mReadProcFile = Process.class.getMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class});
            mReadProcFile.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (DeviceInfoManager.instance().getApiLevel() < 23 || mJiffyMillis != 0) {
                mJiffyMillis = 10;
            } else {
                Class<?> cls = Class.forName("libcore.io.Libcore");
                Class<?> cls2 = Class.forName("libcore.io.Os");
                Field declaredField = cls.getDeclaredField("os");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                Method method = cls2.getMethod("sysconf", new Class[]{Integer.TYPE});
                method.setAccessible(true);
                mJiffyMillis = (int) (1000 / ((Long) method.invoke(obj, new Object[]{Integer.valueOf(OsConstants._SC_CLK_TCK)})).longValue());
            }
            if (mJiffyMillis == 0) {
                mJiffyMillis = 10;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            mJiffyMillis = 10;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.ali.telescope.internal.plugins.cpu.CpuRecord generateCpuStat() {
        /*
            java.lang.Class<com.ali.telescope.util.process.CpuTracker> r1 = com.ali.telescope.util.process.CpuTracker.class
            monitor-enter(r1)
            com.ali.telescope.internal.plugins.cpu.CpuRecord r2 = new com.ali.telescope.internal.plugins.cpu.CpuRecord     // Catch:{ all -> 0x0175 }
            r2.<init>()     // Catch:{ all -> 0x0175 }
            long r3 = com.ali.telescope.util.TimeUtils.getTime()     // Catch:{ all -> 0x0175 }
            r2.timeStamp = r3     // Catch:{ all -> 0x0175 }
            java.lang.reflect.Method r0 = mReadProcFile     // Catch:{ all -> 0x0175 }
            if (r0 == 0) goto L_0x014b
            java.lang.String r0 = mPidStatFile     // Catch:{ all -> 0x0175 }
            if (r0 != 0) goto L_0x0018
            goto L_0x014b
        L_0x0018:
            r0 = 7
            long[] r0 = new long[r0]     // Catch:{ all -> 0x0175 }
            r3 = 6
            long[] r4 = new long[r3]     // Catch:{ all -> 0x0175 }
            java.lang.reflect.Method r5 = mReadProcFile     // Catch:{ Exception -> 0x0141 }
            r6 = 5
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x0141 }
            java.lang.String r8 = "/proc/stat"
            r9 = 0
            r7[r9] = r8     // Catch:{ Exception -> 0x0141 }
            int[] r8 = SYSTEM_CPU_FORMAT     // Catch:{ Exception -> 0x0141 }
            r10 = 1
            r7[r10] = r8     // Catch:{ Exception -> 0x0141 }
            r8 = 2
            r11 = 0
            r7[r8] = r11     // Catch:{ Exception -> 0x0141 }
            r12 = 3
            r7[r12] = r0     // Catch:{ Exception -> 0x0141 }
            r13 = 4
            r7[r13] = r11     // Catch:{ Exception -> 0x0141 }
            java.lang.Object r5 = r5.invoke(r11, r7)     // Catch:{ Exception -> 0x0141 }
            java.lang.Boolean r5 = (java.lang.Boolean) r5     // Catch:{ Exception -> 0x0141 }
            boolean r5 = r5.booleanValue()     // Catch:{ Exception -> 0x0141 }
            if (r5 == 0) goto L_0x005f
            java.lang.reflect.Method r5 = mReadProcFile     // Catch:{ Exception -> 0x0141 }
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x0141 }
            java.lang.String r14 = mPidStatFile     // Catch:{ Exception -> 0x0141 }
            r7[r9] = r14     // Catch:{ Exception -> 0x0141 }
            int[] r14 = PROCESS_STATS_FORMAT     // Catch:{ Exception -> 0x0141 }
            r7[r10] = r14     // Catch:{ Exception -> 0x0141 }
            r7[r8] = r11     // Catch:{ Exception -> 0x0141 }
            r7[r12] = r4     // Catch:{ Exception -> 0x0141 }
            r7[r13] = r11     // Catch:{ Exception -> 0x0141 }
            java.lang.Object r5 = r5.invoke(r11, r7)     // Catch:{ Exception -> 0x0141 }
            java.lang.Boolean r5 = (java.lang.Boolean) r5     // Catch:{ Exception -> 0x0141 }
            boolean r5 = r5.booleanValue()     // Catch:{ Exception -> 0x0141 }
        L_0x005f:
            if (r5 == 0) goto L_0x013b
            r14 = r4[r8]     // Catch:{ all -> 0x0175 }
            r16 = r4[r12]     // Catch:{ all -> 0x0175 }
            r18 = r4[r13]     // Catch:{ all -> 0x0175 }
            mThreadCount = r18     // Catch:{ all -> 0x0175 }
            r18 = r4[r6]     // Catch:{ all -> 0x0175 }
            int r4 = mJiffyMillis     // Catch:{ all -> 0x0175 }
            long r4 = (long) r4     // Catch:{ all -> 0x0175 }
            long r18 = r18 * r4
            mPidStartTime = r18     // Catch:{ all -> 0x0175 }
            long r4 = mProcessBaseUserTime     // Catch:{ all -> 0x0175 }
            int r7 = (r14 > r4 ? 1 : (r14 == r4 ? 0 : -1))
            if (r7 < 0) goto L_0x008d
            long r4 = mProcessBaseSystemTime     // Catch:{ all -> 0x0175 }
            int r7 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r7 < 0) goto L_0x008d
            long r4 = mProcessBaseUserTime     // Catch:{ all -> 0x0175 }
            r7 = 0
            long r4 = r14 - r4
            int r4 = (int) r4     // Catch:{ all -> 0x0175 }
            long r18 = mProcessBaseSystemTime     // Catch:{ all -> 0x0175 }
            r5 = 0
            r20 = r4
            long r3 = r16 - r18
            int r3 = (int) r3     // Catch:{ all -> 0x0175 }
            goto L_0x0090
        L_0x008d:
            r3 = 0
            r20 = 0
        L_0x0090:
            r4 = r0[r9]     // Catch:{ all -> 0x0175 }
            r10 = r0[r10]     // Catch:{ all -> 0x0175 }
            r7 = 0
            long r4 = r4 + r10
            r7 = r0[r8]     // Catch:{ all -> 0x0175 }
            r10 = r0[r12]     // Catch:{ all -> 0x0175 }
            r12 = r0[r13]     // Catch:{ all -> 0x0175 }
            r18 = r0[r6]     // Catch:{ all -> 0x0175 }
            r6 = 6
            r21 = r0[r6]     // Catch:{ all -> 0x0175 }
            long r23 = mBaseUserTime     // Catch:{ all -> 0x0175 }
            int r0 = (r4 > r23 ? 1 : (r4 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseSystemTime     // Catch:{ all -> 0x0175 }
            int r0 = (r7 > r23 ? 1 : (r7 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseIoWaitTime     // Catch:{ all -> 0x0175 }
            int r0 = (r12 > r23 ? 1 : (r12 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseIrqTime     // Catch:{ all -> 0x0175 }
            int r0 = (r18 > r23 ? 1 : (r18 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseSoftIrqTime     // Catch:{ all -> 0x0175 }
            int r0 = (r21 > r23 ? 1 : (r21 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseIdleTime     // Catch:{ all -> 0x0175 }
            int r0 = (r10 > r23 ? 1 : (r10 == r23 ? 0 : -1))
            if (r0 < 0) goto L_0x0120
            long r23 = mBaseUserTime     // Catch:{ all -> 0x0175 }
            r0 = 0
            r25 = r1
            long r0 = r4 - r23
            int r0 = (int) r0
            long r23 = mBaseSystemTime     // Catch:{ all -> 0x0173 }
            r1 = 0
            r26 = r4
            long r4 = r7 - r23
            int r1 = (int) r4     // Catch:{ all -> 0x0173 }
            long r4 = mBaseIoWaitTime     // Catch:{ all -> 0x0173 }
            r6 = 0
            long r4 = r12 - r4
            int r4 = (int) r4     // Catch:{ all -> 0x0173 }
            long r5 = mBaseIrqTime     // Catch:{ all -> 0x0173 }
            r9 = 0
            long r5 = r18 - r5
            int r5 = (int) r5     // Catch:{ all -> 0x0173 }
            long r23 = mBaseSoftIrqTime     // Catch:{ all -> 0x0173 }
            r6 = 0
            r28 = r12
            long r12 = r21 - r23
            int r6 = (int) r12     // Catch:{ all -> 0x0173 }
            long r12 = mBaseIdleTime     // Catch:{ all -> 0x0173 }
            r9 = 0
            long r12 = r10 - r12
            int r9 = (int) r12     // Catch:{ all -> 0x0173 }
            int r0 = r0 + r1
            int r4 = r4 + r0
            int r4 = r4 + r5
            int r4 = r4 + r6
            int r4 = r4 + r9
            long r4 = (long) r4     // Catch:{ all -> 0x0173 }
            r12 = 1
            int r1 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r1 <= 0) goto L_0x012a
            int r20 = r20 + r3
            r1 = 100
            int r3 = r20 * 100
            long r12 = (long) r3     // Catch:{ all -> 0x0173 }
            long r12 = r12 / r4
            int r3 = (int) r12     // Catch:{ all -> 0x0173 }
            short r3 = (short) r3     // Catch:{ all -> 0x0173 }
            r2.myPidCpuPercent = r3     // Catch:{ all -> 0x0173 }
            int r0 = r0 * 100
            long r12 = (long) r0     // Catch:{ all -> 0x0173 }
            long r12 = r12 / r4
            int r0 = (int) r12     // Catch:{ all -> 0x0173 }
            short r0 = (short) r0     // Catch:{ all -> 0x0173 }
            r2.sysTotalCpuPercent = r0     // Catch:{ all -> 0x0173 }
            short r0 = r2.sysTotalCpuPercent     // Catch:{ all -> 0x0173 }
            if (r0 <= r1) goto L_0x0115
            r2.sysTotalCpuPercent = r1     // Catch:{ all -> 0x0173 }
        L_0x0115:
            short r0 = r2.myPidCpuPercent     // Catch:{ all -> 0x0173 }
            short r1 = r2.sysTotalCpuPercent     // Catch:{ all -> 0x0173 }
            if (r0 <= r1) goto L_0x012a
            short r0 = r2.sysTotalCpuPercent     // Catch:{ all -> 0x0173 }
            r2.myPidCpuPercent = r0     // Catch:{ all -> 0x0173 }
            goto L_0x012a
        L_0x0120:
            r25 = r1
            r26 = r4
            r28 = r12
            r2.myPidCpuPercent = r9     // Catch:{ all -> 0x0173 }
            r2.sysTotalCpuPercent = r9     // Catch:{ all -> 0x0173 }
        L_0x012a:
            mProcessBaseUserTime = r14     // Catch:{ all -> 0x0173 }
            mProcessBaseSystemTime = r16     // Catch:{ all -> 0x0173 }
            mBaseUserTime = r26     // Catch:{ all -> 0x0173 }
            mBaseSystemTime = r7     // Catch:{ all -> 0x0173 }
            mBaseIoWaitTime = r28     // Catch:{ all -> 0x0173 }
            mBaseIrqTime = r18     // Catch:{ all -> 0x0173 }
            mBaseSoftIrqTime = r21     // Catch:{ all -> 0x0173 }
            mBaseIdleTime = r10     // Catch:{ all -> 0x0173 }
            goto L_0x013d
        L_0x013b:
            r25 = r1
        L_0x013d:
            sCpuRecord = r2     // Catch:{ all -> 0x0173 }
            monitor-exit(r25)
            return r2
        L_0x0141:
            r0 = move-exception
            r25 = r1
            r0.printStackTrace()     // Catch:{ all -> 0x0173 }
            sCpuRecord = r2     // Catch:{ all -> 0x0173 }
            monitor-exit(r25)
            return r2
        L_0x014b:
            r25 = r1
            java.lang.String r0 = "CpuTracker"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0173 }
            r1.<init>()     // Catch:{ all -> 0x0173 }
            java.lang.String r3 = "readProcFile : "
            r1.append(r3)     // Catch:{ all -> 0x0173 }
            java.lang.reflect.Method r3 = mReadProcFile     // Catch:{ all -> 0x0173 }
            r1.append(r3)     // Catch:{ all -> 0x0173 }
            java.lang.String r3 = ", mPidStatFile : "
            r1.append(r3)     // Catch:{ all -> 0x0173 }
            java.lang.String r3 = mPidStatFile     // Catch:{ all -> 0x0173 }
            r1.append(r3)     // Catch:{ all -> 0x0173 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0173 }
            android.util.Log.w(r0, r1)     // Catch:{ all -> 0x0173 }
            sCpuRecord = r2     // Catch:{ all -> 0x0173 }
            monitor-exit(r25)
            return r2
        L_0x0173:
            r0 = move-exception
            goto L_0x0178
        L_0x0175:
            r0 = move-exception
            r25 = r1
        L_0x0178:
            monitor-exit(r25)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.util.process.CpuTracker.generateCpuStat():com.ali.telescope.internal.plugins.cpu.CpuRecord");
    }

    public static synchronized CpuRecord getCpuStat() {
        CpuRecord cpuRecord;
        synchronized (CpuTracker.class) {
            cpuRecord = sCpuRecord;
        }
        return cpuRecord;
    }
}
