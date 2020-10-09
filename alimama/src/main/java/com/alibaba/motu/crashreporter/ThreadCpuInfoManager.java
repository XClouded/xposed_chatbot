package com.alibaba.motu.crashreporter;

import android.os.Process;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ThreadCpuInfoManager {
    private static final int[] PROCESS_STATS_FORMAT = {32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224, 32, 32, 32, 32, 8224, 32, 8224, 32};
    private static final int[] PROCESS_STATS_FORMAT_NAME = {4128};
    public static final int PROC_COMBINE = 256;
    public static final int PROC_OUT_LONG = 8192;
    public static final int PROC_SPACE_TERM = 32;
    private static final int[] SYSTEM_CPU_FORMAT = {288, 8224, 8224, 8224, 8224, 8224, 8224, 8224};
    private Method mReadProcFile;
    private int pid;
    private File processFile;

    public static ThreadCpuInfoManager instance() {
        return Holder.INSTANCE;
    }

    private ThreadCpuInfoManager() {
        init();
    }

    private void init() {
        this.pid = Process.myPid();
        try {
            this.processFile = new File("/proc/" + this.pid + "/task/");
            this.mReadProcFile = Process.class.getMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class});
            this.mReadProcFile.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ThreadCpuInfo> doCpuCheck() {
        String[] list;
        ArrayList arrayList = new ArrayList();
        try {
            if (this.processFile.isDirectory() && (list = this.processFile.list()) != null) {
                int i = 0;
                for (String tid : list) {
                    int tid2 = getTid(tid);
                    if (tid2 != -1) {
                        ThreadCpuInfo threadCpuInfo = new ThreadCpuInfo(tid2, loadThreadName(tid2), loadThreadCreateTime(tid2));
                        int i2 = i + 1;
                        threadCpuInfo.setIndex(i);
                        threadCpuInfo.setCpuTime(loadTaskTime(tid2));
                        calculateCpu(threadCpuInfo);
                        arrayList.add(threadCpuInfo);
                        i = i2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private int getTid(String str) {
        if (str == null) {
            return -1;
        }
        int lastIndexOf = str.lastIndexOf("\\.");
        if (lastIndexOf != -1) {
            str = str.substring(lastIndexOf + 1);
        }
        return Integer.valueOf(str).intValue();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long loadTaskTime(int r9) {
        /*
            r8 = this;
            r0 = 6
            r1 = 0
            long[] r0 = new long[r0]     // Catch:{ Throwable -> 0x0053 }
            java.lang.reflect.Method r3 = r8.mReadProcFile     // Catch:{ Throwable -> 0x0053 }
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0053 }
            r5 = 0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0053 }
            r6.<init>()     // Catch:{ Throwable -> 0x0053 }
            java.lang.String r7 = "/proc/"
            r6.append(r7)     // Catch:{ Throwable -> 0x0053 }
            int r7 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0053 }
            r6.append(r7)     // Catch:{ Throwable -> 0x0053 }
            java.lang.String r7 = "/task/"
            r6.append(r7)     // Catch:{ Throwable -> 0x0053 }
            r6.append(r9)     // Catch:{ Throwable -> 0x0053 }
            java.lang.String r9 = "/stat"
            r6.append(r9)     // Catch:{ Throwable -> 0x0053 }
            java.lang.String r9 = r6.toString()     // Catch:{ Throwable -> 0x0053 }
            r4[r5] = r9     // Catch:{ Throwable -> 0x0053 }
            r9 = 1
            int[] r5 = PROCESS_STATS_FORMAT     // Catch:{ Throwable -> 0x0053 }
            r4[r9] = r5     // Catch:{ Throwable -> 0x0053 }
            r9 = 2
            r5 = 0
            r4[r9] = r5     // Catch:{ Throwable -> 0x0053 }
            r6 = 3
            r4[r6] = r0     // Catch:{ Throwable -> 0x0053 }
            r7 = 4
            r4[r7] = r5     // Catch:{ Throwable -> 0x0053 }
            java.lang.Object r3 = r3.invoke(r5, r4)     // Catch:{ Throwable -> 0x0053 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ Throwable -> 0x0053 }
            boolean r3 = r3.booleanValue()     // Catch:{ Throwable -> 0x0053 }
            if (r3 != 0) goto L_0x004b
            goto L_0x0052
        L_0x004b:
            r3 = r0[r9]     // Catch:{ Throwable -> 0x0053 }
            r5 = r0[r6]     // Catch:{ Throwable -> 0x0053 }
            r9 = 0
            long r1 = r3 + r5
        L_0x0052:
            return r1
        L_0x0053:
            r9 = move-exception
            r9.printStackTrace()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ThreadCpuInfoManager.loadTaskTime(int):long");
    }

    private void calculateCpu(ThreadCpuInfo threadCpuInfo) {
        loadCpuStat(threadCpuInfo);
        long utime = (threadCpuInfo.getUtime() - threadCpuInfo.getOldUtime()) + (threadCpuInfo.getStime() - threadCpuInfo.getOldStime());
        long utime2 = ((((((threadCpuInfo.getUtime() + threadCpuInfo.getNtime()) + threadCpuInfo.getStime()) + threadCpuInfo.getItime()) + threadCpuInfo.getIowtime()) + threadCpuInfo.getIrqtime()) + threadCpuInfo.getSirqtime()) - ((((((threadCpuInfo.getOldUtime() + threadCpuInfo.getOldNtime()) + threadCpuInfo.getOldStime()) + threadCpuInfo.getOldItime()) + threadCpuInfo.getOldIowtime()) + threadCpuInfo.getOldIrqtime()) + threadCpuInfo.getOldSirqtime());
        if (utime2 != 0) {
            threadCpuInfo.user = ((((threadCpuInfo.getUtime() + threadCpuInfo.getNtime()) - (threadCpuInfo.getOldUtime() + threadCpuInfo.getOldNtime())) * 100) / utime2) + Operators.MOD;
            threadCpuInfo.system = (((threadCpuInfo.getStime() - threadCpuInfo.getOldStime()) * 100) / utime2) + Operators.MOD;
            threadCpuInfo.iow = (((threadCpuInfo.getIowtime() - threadCpuInfo.getOldIowtime()) * 100) / utime2) + Operators.MOD;
            threadCpuInfo.irq = ((((threadCpuInfo.getIrqtime() + threadCpuInfo.getSirqtime()) - (threadCpuInfo.getOldIrqtime() + threadCpuInfo.getOldSirqtime())) * 100) / utime2) + Operators.MOD;
            threadCpuInfo.pidstring = Process.myPid() + "";
            threadCpuInfo.tidstring = threadCpuInfo.getTid() + "";
            threadCpuInfo.cpupercent = ((utime * 100) / utime2) + Operators.MOD;
            threadCpuInfo.thread = threadCpuInfo.getThreadName();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadCpuStat(com.alibaba.motu.crashreporter.ThreadCpuInfo r11) {
        /*
            r10 = this;
            r0 = 7
            long[] r0 = new long[r0]
            r1 = 4
            r2 = 3
            r3 = 2
            r4 = 1
            r5 = 5
            r6 = 0
            java.lang.reflect.Method r7 = r10.mReadProcFile     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.Object[] r8 = new java.lang.Object[r5]     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.String r9 = "/proc/stat"
            r8[r6] = r9     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            int[] r9 = SYSTEM_CPU_FORMAT     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r8[r4] = r9     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r9 = 0
            r8[r3] = r9     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r8[r2] = r0     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            r8[r1] = r9     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.Object r7 = r7.invoke(r9, r8)     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            java.lang.Boolean r7 = (java.lang.Boolean) r7     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            boolean r7 = r7.booleanValue()     // Catch:{ IllegalAccessException -> 0x002c, InvocationTargetException -> 0x0027 }
            goto L_0x0031
        L_0x0027:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0030
        L_0x002c:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0030:
            r7 = 0
        L_0x0031:
            if (r7 == 0) goto L_0x0057
            r6 = r0[r6]
            r11.setUtime(r6)
            r6 = r0[r4]
            r11.setNtime(r6)
            r3 = r0[r3]
            r11.setStime(r3)
            r2 = r0[r2]
            r11.setItime(r2)
            r1 = r0[r1]
            r11.setIowtime(r1)
            r1 = r0[r5]
            r11.setIrqtime(r1)
            r1 = 6
            r1 = r0[r1]
            r11.setSirqtime(r1)
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ThreadCpuInfoManager.loadCpuStat(com.alibaba.motu.crashreporter.ThreadCpuInfo):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String loadThreadName(int r8) {
        /*
            r7 = this;
            r0 = 1
            java.lang.String[] r1 = new java.lang.String[r0]
            r2 = 0
            java.lang.reflect.Method r3 = r7.mReadProcFile     // Catch:{ Exception -> 0x0042 }
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0042 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0042 }
            r5.<init>()     // Catch:{ Exception -> 0x0042 }
            java.lang.String r6 = "/proc/"
            r5.append(r6)     // Catch:{ Exception -> 0x0042 }
            int r6 = r7.pid     // Catch:{ Exception -> 0x0042 }
            r5.append(r6)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r6 = "/task/"
            r5.append(r6)     // Catch:{ Exception -> 0x0042 }
            r5.append(r8)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r8 = "/comm"
            r5.append(r8)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r8 = r5.toString()     // Catch:{ Exception -> 0x0042 }
            r4[r2] = r8     // Catch:{ Exception -> 0x0042 }
            int[] r8 = PROCESS_STATS_FORMAT_NAME     // Catch:{ Exception -> 0x0042 }
            r4[r0] = r8     // Catch:{ Exception -> 0x0042 }
            r8 = 2
            r4[r8] = r1     // Catch:{ Exception -> 0x0042 }
            r8 = 3
            r5 = 0
            r4[r8] = r5     // Catch:{ Exception -> 0x0042 }
            r8 = 4
            r4[r8] = r5     // Catch:{ Exception -> 0x0042 }
            java.lang.Object r8 = r3.invoke(r5, r4)     // Catch:{ Exception -> 0x0042 }
            java.lang.Boolean r8 = (java.lang.Boolean) r8     // Catch:{ Exception -> 0x0042 }
            r8.booleanValue()     // Catch:{ Exception -> 0x0042 }
        L_0x0042:
            r8 = r1[r2]
            r3 = r1[r2]
            int r3 = r3.length()
            int r3 = r3 - r0
            char r8 = r8.charAt(r3)
            r3 = 10
            if (r8 != r3) goto L_0x0061
            r8 = r1[r2]
            r1 = r1[r2]
            int r1 = r1.length()
            int r1 = r1 - r0
            java.lang.String r8 = r8.substring(r2, r1)
            return r8
        L_0x0061:
            r8 = r1[r2]
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.crashreporter.ThreadCpuInfoManager.loadThreadName(int):java.lang.String");
    }

    private long loadThreadCreateTime(int i) {
        File file = new File("/proc/" + this.pid + "/task/" + i + "/comm");
        if (file.exists()) {
            return file.lastModified();
        }
        return -1;
    }

    private static class Holder {
        static final ThreadCpuInfoManager INSTANCE = new ThreadCpuInfoManager();

        private Holder() {
        }
    }
}
