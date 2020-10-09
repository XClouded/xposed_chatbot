package com.taobao.weex.analyzer.core.cpu;

import android.os.Process;

class CpuSampler {
    private CpuSampler() {
    }

    static String sampleCpuRate() {
        return doSample("/proc/stat");
    }

    static String samplePidCpuRate() {
        int myPid = Process.myPid();
        return doSample("/proc/" + myPid + "/stat");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x002f A[SYNTHETIC, Splitter:B:18:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003b A[SYNTHETIC, Splitter:B:24:0x003b] */
    @androidx.annotation.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String doSample(@androidx.annotation.NonNull java.lang.String r5) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0029 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0029 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0029 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0029 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0029 }
            r5 = 1024(0x400, float:1.435E-42)
            r2.<init>(r3, r5)     // Catch:{ Exception -> 0x0029 }
            java.lang.String r5 = r2.readLine()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r2.close()     // Catch:{ IOException -> 0x001c }
            goto L_0x0038
        L_0x001c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0038
        L_0x0021:
            r5 = move-exception
            r1 = r2
            goto L_0x0039
        L_0x0024:
            r5 = move-exception
            r1 = r2
            goto L_0x002a
        L_0x0027:
            r5 = move-exception
            goto L_0x0039
        L_0x0029:
            r5 = move-exception
        L_0x002a:
            r5.printStackTrace()     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x0037
            r1.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0037
        L_0x0033:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0037:
            r5 = r0
        L_0x0038:
            return r5
        L_0x0039:
            if (r1 == 0) goto L_0x0043
            r1.close()     // Catch:{ IOException -> 0x003f }
            goto L_0x0043
        L_0x003f:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0043:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.cpu.CpuSampler.doSample(java.lang.String):java.lang.String");
    }
}
