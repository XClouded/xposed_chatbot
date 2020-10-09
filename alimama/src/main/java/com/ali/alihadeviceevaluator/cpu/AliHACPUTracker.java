package com.ali.alihadeviceevaluator.cpu;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AliHACPUTracker implements Runnable {
    private static final boolean Debug = false;
    private static final int[] PROCESS_STATS_FORMAT = {32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224};
    static final int PROCESS_STAT_MAJOR_FAULTS = 1;
    static final int PROCESS_STAT_MINOR_FAULTS = 0;
    static final int PROCESS_STAT_STIME = 3;
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
    private static final int[] SYSTEM_CPU_FORMAT = {288, 8224, 8224, 8224, 8224, 8224, 8224, 8224};
    private static final String TAG = "CpuTracker";
    private volatile boolean initCpu = true;
    private int lastRelIdleTime;
    private long mBaseIdleTime;
    private long mBaseIoWaitTime;
    private long mBaseIrqTime;
    private long mBaseSoftIrqTime;
    private long mBaseSystemTime;
    private long mBaseUserTime;
    private Handler mCPUHandler;
    private float mCpuPercent = -1.0f;
    private float mCurProcessCpuPercent = -1.0f;
    public long mDeltaDuration = 7000;
    private long mFirstDeltaDuration = 2000;
    private long mProcessBaseSystemTime;
    private long mProcessBaseUserTime;
    private volatile double o_cpu = 0.0d;
    private volatile double o_idle = 0.0d;
    private volatile boolean open = true;
    private ReadWriteLock peakCpuLock = new ReentrantReadWriteLock();
    private ReadWriteLock peakCurProCpuLock = new ReentrantReadWriteLock();
    private Method readProcFile;
    private String statFile;
    private final long[] statsData = new long[4];
    private long[] sysCpu = new long[7];

    public AliHACPUTracker(int i, Handler handler) {
        if (handler != null) {
            this.mCPUHandler = handler;
        } else {
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mCPUHandler = new Handler(handlerThread.getLooper());
        }
        init(i);
    }

    public AliHACPUTracker(int i) {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mCPUHandler = new Handler(handlerThread.getLooper());
        init(i);
    }

    private void init(int i) {
        try {
            this.statFile = "/proc/" + i + "/stat";
            this.readProcFile = Process.class.getMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class});
            this.readProcFile.setAccessible(true);
            if (Build.VERSION.SDK_INT < 26) {
                this.mCPUHandler.post(this);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void run() {
        try {
            if (this.initCpu) {
                this.mCPUHandler.postDelayed(this, this.mFirstDeltaDuration);
            } else if (this.open) {
                this.mCPUHandler.postDelayed(this, this.mDeltaDuration);
            }
            updateCpuPercent();
            updateCurProcessCpuPercent();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void reset(long j) {
        if (Build.VERSION.SDK_INT < 26) {
            this.mCPUHandler.removeCallbacks(this);
            if (j > 0) {
                this.mDeltaDuration = j;
                this.mCPUHandler.postDelayed(this, this.mDeltaDuration);
                this.open = true;
                return;
            }
            this.open = false;
        }
    }

    public float peakCpuPercent() {
        this.peakCpuLock.readLock().lock();
        float f = this.mCpuPercent;
        this.peakCpuLock.readLock().unlock();
        return f;
    }

    public float peakCurProcessCpuPercent() {
        this.peakCurProCpuLock.readLock().lock();
        float f = this.mCurProcessCpuPercent;
        this.peakCurProCpuLock.readLock().unlock();
        return f;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x011a, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x011b, code lost:
        r8 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x011f, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0121, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0122, code lost:
        r2 = r18;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x011f A[ExcHandler: all (th java.lang.Throwable), PHI: r18 
  PHI: (r18v0 java.io.RandomAccessFile) = (r18v1 java.io.RandomAccessFile), (r18v1 java.io.RandomAccessFile), (r18v1 java.io.RandomAccessFile), (r18v1 java.io.RandomAccessFile), (r18v8 java.io.RandomAccessFile) binds: [B:45:0x010d, B:46:?, B:48:0x0112, B:49:?, B:29:0x00e2] A[DONT_GENERATE, DONT_INLINE], Splitter:B:29:0x00e2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float updateCpuPercent() {
        /*
            r19 = this;
            r1 = r19
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.lock()
            boolean r0 = r1.initCpu
            r2 = 7
            r3 = 8
            r4 = 6
            r5 = 4
            r6 = 3
            r7 = 5
            r8 = 0
            r9 = 2
            if (r0 == 0) goto L_0x0080
            r0 = 0
            r1.initCpu = r0
            java.io.RandomAccessFile r11 = new java.io.RandomAccessFile     // Catch:{ Throwable -> 0x0072 }
            java.lang.String r0 = "/proc/stat"
            java.lang.String r12 = "r"
            r11.<init>(r0, r12)     // Catch:{ Throwable -> 0x0072 }
            java.lang.String r0 = r11.readLine()     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            java.lang.String r8 = " "
            java.lang.String[] r0 = r0.split(r8)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r7 = r0[r7]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r7 = java.lang.Double.parseDouble(r7)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r1.o_idle = r7     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r7 = r0[r9]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r7 = java.lang.Double.parseDouble(r7)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r6 = r0[r6]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r12 = java.lang.Double.parseDouble(r6)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r6 = 0
            double r7 = r7 + r12
            r5 = r0[r5]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r5 = java.lang.Double.parseDouble(r5)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r9 = 0
            double r7 = r7 + r5
            r4 = r0[r4]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r4 = java.lang.Double.parseDouble(r4)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r6 = 0
            double r7 = r7 + r4
            r3 = r0[r3]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r3 = java.lang.Double.parseDouble(r3)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r5 = 0
            double r7 = r7 + r3
            r0 = r0[r2]     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            double r2 = java.lang.Double.parseDouble(r0)     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r0 = 0
            double r7 = r7 + r2
            r1.o_cpu = r7     // Catch:{ Throwable -> 0x006c, all -> 0x006a }
            r1.closeRandomAccessFile(r11)
            goto L_0x0079
        L_0x006a:
            r0 = move-exception
            goto L_0x007c
        L_0x006c:
            r0 = move-exception
            r8 = r11
            goto L_0x0073
        L_0x006f:
            r0 = move-exception
            r11 = r8
            goto L_0x007c
        L_0x0072:
            r0 = move-exception
        L_0x0073:
            r0.printStackTrace()     // Catch:{ all -> 0x006f }
            r1.closeRandomAccessFile(r8)
        L_0x0079:
            r10 = 0
            goto L_0x0137
        L_0x007c:
            r1.closeRandomAccessFile(r11)
            throw r0
        L_0x0080:
            java.io.RandomAccessFile r11 = new java.io.RandomAccessFile     // Catch:{ Throwable -> 0x012f }
            java.lang.String r0 = "/proc/stat"
            java.lang.String r12 = "r"
            r11.<init>(r0, r12)     // Catch:{ Throwable -> 0x012f }
            java.lang.String r0 = r11.readLine()     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            java.lang.String r8 = " "
            java.lang.String[] r0 = r0.split(r8)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r7 = r0[r7]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r7 = java.lang.Double.parseDouble(r7)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r12 = r0[r9]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r12 = java.lang.Double.parseDouble(r12)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r6 = r0[r6]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r14 = java.lang.Double.parseDouble(r6)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r6 = 0
            double r12 = r12 + r14
            r5 = r0[r5]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r5 = java.lang.Double.parseDouble(r5)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r14 = 0
            double r12 = r12 + r5
            r4 = r0[r4]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r4 = java.lang.Double.parseDouble(r4)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r6 = 0
            double r12 = r12 + r4
            r3 = r0[r3]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r3 = java.lang.Double.parseDouble(r3)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r5 = 0
            double r12 = r12 + r3
            r0 = r0[r2]     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r2 = java.lang.Double.parseDouble(r0)     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r0 = 0
            double r12 = r12 + r2
            double r2 = r12 + r7
            double r4 = r1.o_cpu     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            double r14 = r1.o_idle     // Catch:{ Throwable -> 0x0128, all -> 0x0125 }
            r0 = 0
            double r4 = r4 + r14
            double r4 = r2 - r4
            r14 = 4636737291354636288(0x4059000000000000, double:100.0)
            r16 = 0
            int r0 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x0109
            double r4 = r1.o_cpu     // Catch:{ Throwable -> 0x0103, all -> 0x00fd }
            r0 = 0
            double r4 = r12 - r4
            double r4 = r4 * r14
            r18 = r11
            double r10 = r1.o_cpu     // Catch:{ Throwable -> 0x00fb, all -> 0x011f }
            double r14 = r1.o_idle     // Catch:{ Throwable -> 0x00fb, all -> 0x011f }
            r0 = 0
            double r10 = r10 + r14
            double r2 = r2 - r10
            double r14 = com.ali.alihadeviceevaluator.util.BigNumUtils.div((double) r4, (double) r2, (int) r9)     // Catch:{ Throwable -> 0x00fb, all -> 0x011f }
            int r0 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r0 >= 0) goto L_0x00f2
            goto L_0x010b
        L_0x00f2:
            r2 = 4636737291354636288(0x4059000000000000, double:100.0)
            int r0 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x00f9
            goto L_0x010d
        L_0x00f9:
            r2 = r14
            goto L_0x010d
        L_0x00fb:
            r0 = move-exception
            goto L_0x0106
        L_0x00fd:
            r0 = move-exception
            r18 = r11
        L_0x0100:
            r2 = r18
            goto L_0x0141
        L_0x0103:
            r0 = move-exception
            r18 = r11
        L_0x0106:
            r8 = r18
            goto L_0x0130
        L_0x0109:
            r18 = r11
        L_0x010b:
            r2 = r16
        L_0x010d:
            r1.o_cpu = r12     // Catch:{ Throwable -> 0x0121, all -> 0x011f }
            r1.o_idle = r7     // Catch:{ Throwable -> 0x0121, all -> 0x011f }
            float r10 = (float) r2
            r1.mCpuPercent = r10     // Catch:{ Throwable -> 0x011a, all -> 0x011f }
            r2 = r18
            r1.closeRandomAccessFile(r2)
            goto L_0x0137
        L_0x011a:
            r0 = move-exception
            r2 = r18
            r8 = r2
            goto L_0x0131
        L_0x011f:
            r0 = move-exception
            goto L_0x0100
        L_0x0121:
            r0 = move-exception
            r2 = r18
            goto L_0x012a
        L_0x0125:
            r0 = move-exception
            r2 = r11
            goto L_0x0141
        L_0x0128:
            r0 = move-exception
            r2 = r11
        L_0x012a:
            r8 = r2
            goto L_0x0130
        L_0x012c:
            r0 = move-exception
            r2 = r8
            goto L_0x0141
        L_0x012f:
            r0 = move-exception
        L_0x0130:
            r10 = 0
        L_0x0131:
            r0.printStackTrace()     // Catch:{ all -> 0x012c }
            r1.closeRandomAccessFile(r8)
        L_0x0137:
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.unlock()
            return r10
        L_0x0141:
            r1.closeRandomAccessFile(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.cpu.AliHACPUTracker.updateCpuPercent():float");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0061 A[Catch:{ Exception -> 0x0122 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0114  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float updateCurProcessCpuPercent() {
        /*
            r25 = this;
            r1 = r25
            java.lang.reflect.Method r0 = r1.readProcFile
            if (r0 == 0) goto L_0x013d
            java.lang.String r0 = r1.statFile
            if (r0 != 0) goto L_0x000c
            goto L_0x013d
        L_0x000c:
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCurProCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.lock()
            java.lang.reflect.Method r0 = r1.readProcFile     // Catch:{ Exception -> 0x0122 }
            r3 = 5
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0122 }
            java.lang.String r5 = r1.statFile     // Catch:{ Exception -> 0x0122 }
            r6 = 0
            r4[r6] = r5     // Catch:{ Exception -> 0x0122 }
            int[] r5 = PROCESS_STATS_FORMAT     // Catch:{ Exception -> 0x0122 }
            r7 = 1
            r4[r7] = r5     // Catch:{ Exception -> 0x0122 }
            r5 = 2
            r8 = 0
            r4[r5] = r8     // Catch:{ Exception -> 0x0122 }
            long[] r9 = r1.statsData     // Catch:{ Exception -> 0x0122 }
            r10 = 3
            r4[r10] = r9     // Catch:{ Exception -> 0x0122 }
            r9 = 4
            r4[r9] = r8     // Catch:{ Exception -> 0x0122 }
            java.lang.Object r0 = r0.invoke(r8, r4)     // Catch:{ Exception -> 0x0122 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Exception -> 0x0122 }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x0122 }
            if (r0 == 0) goto L_0x005e
            java.lang.reflect.Method r0 = r1.readProcFile     // Catch:{ Exception -> 0x0122 }
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0122 }
            java.lang.String r11 = "/proc/stat"
            r4[r6] = r11     // Catch:{ Exception -> 0x0122 }
            int[] r11 = SYSTEM_CPU_FORMAT     // Catch:{ Exception -> 0x0122 }
            r4[r7] = r11     // Catch:{ Exception -> 0x0122 }
            r4[r5] = r8     // Catch:{ Exception -> 0x0122 }
            long[] r11 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r4[r10] = r11     // Catch:{ Exception -> 0x0122 }
            r4[r9] = r8     // Catch:{ Exception -> 0x0122 }
            java.lang.Object r0 = r0.invoke(r8, r4)     // Catch:{ Exception -> 0x0122 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Exception -> 0x0122 }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x0122 }
            if (r0 == 0) goto L_0x005e
            r0 = 1
            goto L_0x005f
        L_0x005e:
            r0 = 0
        L_0x005f:
            if (r0 == 0) goto L_0x0114
            long[] r0 = r1.statsData     // Catch:{ Exception -> 0x0122 }
            r11 = r0[r5]     // Catch:{ Exception -> 0x0122 }
            long r13 = r1.mProcessBaseUserTime     // Catch:{ Exception -> 0x0122 }
            r0 = 0
            long r11 = r11 - r13
            int r0 = (int) r11     // Catch:{ Exception -> 0x0122 }
            long[] r4 = r1.statsData     // Catch:{ Exception -> 0x0122 }
            r11 = r4[r10]     // Catch:{ Exception -> 0x0122 }
            long r13 = r1.mProcessBaseSystemTime     // Catch:{ Exception -> 0x0122 }
            r4 = 0
            long r11 = r11 - r13
            int r4 = (int) r11     // Catch:{ Exception -> 0x0122 }
            long[] r8 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r11 = r8[r6]     // Catch:{ Exception -> 0x0122 }
            long[] r6 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r13 = r6[r7]     // Catch:{ Exception -> 0x0122 }
            r6 = 0
            long r11 = r11 + r13
            long[] r6 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r13 = r6[r5]     // Catch:{ Exception -> 0x0122 }
            long[] r6 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r5 = r6[r10]     // Catch:{ Exception -> 0x0122 }
            long[] r8 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r9 = r8[r9]     // Catch:{ Exception -> 0x0122 }
            long[] r8 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r2 = r8[r3]     // Catch:{ Exception -> 0x0122 }
            long[] r8 = r1.sysCpu     // Catch:{ Exception -> 0x0122 }
            r17 = 6
            r7 = r8[r17]     // Catch:{ Exception -> 0x0122 }
            r18 = r4
            r19 = r5
            long r4 = r1.mBaseUserTime     // Catch:{ Exception -> 0x0122 }
            r6 = 0
            long r4 = r11 - r4
            int r4 = (int) r4     // Catch:{ Exception -> 0x0122 }
            long r5 = r1.mBaseSystemTime     // Catch:{ Exception -> 0x0122 }
            r17 = 0
            long r5 = r13 - r5
            int r5 = (int) r5     // Catch:{ Exception -> 0x0122 }
            r21 = r13
            long r13 = r1.mBaseIoWaitTime     // Catch:{ Exception -> 0x0122 }
            r6 = 0
            long r13 = r9 - r13
            int r6 = (int) r13     // Catch:{ Exception -> 0x0122 }
            long r13 = r1.mBaseIrqTime     // Catch:{ Exception -> 0x0122 }
            r17 = 0
            long r13 = r2 - r13
            int r13 = (int) r13     // Catch:{ Exception -> 0x0122 }
            long r14 = r1.mBaseSoftIrqTime     // Catch:{ Exception -> 0x0122 }
            r17 = 0
            long r14 = r7 - r14
            int r14 = (int) r14     // Catch:{ Exception -> 0x0122 }
            r23 = r7
            long r7 = r1.mBaseIdleTime     // Catch:{ Exception -> 0x0122 }
            r15 = 0
            long r7 = r19 - r7
            int r7 = (int) r7     // Catch:{ Exception -> 0x0122 }
            r8 = 1
            if (r7 <= r8) goto L_0x00c6
            goto L_0x00c8
        L_0x00c6:
            int r7 = r1.lastRelIdleTime     // Catch:{ Exception -> 0x0122 }
        L_0x00c8:
            int r4 = r4 + r5
            int r4 = r4 + r6
            int r4 = r4 + r13
            int r4 = r4 + r14
            int r4 = r4 + r7
            if (r4 <= r8) goto L_0x00e2
            int r0 = r0 + r18
            int r0 = r0 * 100
            float r0 = (float) r0     // Catch:{ Exception -> 0x0122 }
            float r4 = (float) r4     // Catch:{ Exception -> 0x0122 }
            r5 = 2
            float r4 = com.ali.alihadeviceevaluator.util.BigNumUtils.div((float) r0, (float) r4, (int) r5)     // Catch:{ Exception -> 0x0122 }
            r1.mCurProcessCpuPercent = r4     // Catch:{ Exception -> 0x00df }
            r16 = r4
            goto L_0x00e4
        L_0x00df:
            r0 = move-exception
            r2 = r4
            goto L_0x0124
        L_0x00e2:
            r16 = 0
        L_0x00e4:
            long[] r0 = r1.statsData     // Catch:{ Exception -> 0x0110 }
            r4 = 2
            r4 = r0[r4]     // Catch:{ Exception -> 0x0110 }
            r1.mProcessBaseUserTime = r4     // Catch:{ Exception -> 0x0110 }
            long[] r0 = r1.statsData     // Catch:{ Exception -> 0x0110 }
            r4 = 3
            r4 = r0[r4]     // Catch:{ Exception -> 0x0110 }
            r1.mProcessBaseSystemTime = r4     // Catch:{ Exception -> 0x0110 }
            r1.mBaseUserTime = r11     // Catch:{ Exception -> 0x0110 }
            r4 = r21
            r1.mBaseSystemTime = r4     // Catch:{ Exception -> 0x0110 }
            r4 = r19
            r1.mBaseIdleTime = r4     // Catch:{ Exception -> 0x0110 }
            r1.mBaseIoWaitTime = r9     // Catch:{ Exception -> 0x0110 }
            r1.mBaseIrqTime = r2     // Catch:{ Exception -> 0x0110 }
            r2 = r23
            r1.mBaseSoftIrqTime = r2     // Catch:{ Exception -> 0x0110 }
            r1.lastRelIdleTime = r7     // Catch:{ Exception -> 0x0110 }
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCurProCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.unlock()
            return r16
        L_0x0110:
            r0 = move-exception
            r2 = r16
            goto L_0x0124
        L_0x0114:
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCurProCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.unlock()
            r16 = 0
            goto L_0x0132
        L_0x0120:
            r0 = move-exception
            goto L_0x0133
        L_0x0122:
            r0 = move-exception
            r2 = 0
        L_0x0124:
            r0.printStackTrace()     // Catch:{ all -> 0x0120 }
            java.util.concurrent.locks.ReadWriteLock r0 = r1.peakCurProCpuLock
            java.util.concurrent.locks.Lock r0 = r0.writeLock()
            r0.unlock()
            r16 = r2
        L_0x0132:
            return r16
        L_0x0133:
            java.util.concurrent.locks.ReadWriteLock r2 = r1.peakCurProCpuLock
            java.util.concurrent.locks.Lock r2 = r2.writeLock()
            r2.unlock()
            throw r0
        L_0x013d:
            java.lang.String r0 = "CpuTracker"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "readProcFile : "
            r2.append(r3)
            java.lang.reflect.Method r3 = r1.readProcFile
            r2.append(r3)
            java.lang.String r3 = ", statFile : "
            r2.append(r3)
            java.lang.String r3 = r1.statFile
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            android.util.Log.e(r0, r2)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.cpu.AliHACPUTracker.updateCurProcessCpuPercent():float");
    }

    private void closeRandomAccessFile(RandomAccessFile randomAccessFile) {
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
