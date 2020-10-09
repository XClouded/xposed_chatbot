package com.ali.alihadeviceevaluator.mem;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

public class AliHAMemoryTracker {
    public long[] getHeapJVM() {
        return new long[]{Runtime.getRuntime().maxMemory() >> 10, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 10};
    }

    public long[] getHeapNative() {
        return new long[]{Debug.getNativeHeapSize() >> 10, (Debug.getNativeHeapAllocatedSize() - Debug.getNativeHeapFreeSize()) >> 10};
    }

    public long[] getDeviceMem() {
        long[] memInfo = getMemInfo();
        return new long[]{memInfo[0], memInfo[0] - ((memInfo[1] + memInfo[2]) + memInfo[3])};
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long[] getMemInfo() {
        /*
            r10 = this;
            r0 = 4
            long[] r1 = new long[r0]
            java.lang.String r2 = "android.os.Process"
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ Exception -> 0x0059 }
            java.lang.String r3 = "readProcLines"
            r4 = 3
            java.lang.Class[] r5 = new java.lang.Class[r4]     // Catch:{ Exception -> 0x0059 }
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            r7 = 0
            r5[r7] = r6     // Catch:{ Exception -> 0x0059 }
            java.lang.Class<java.lang.String[]> r6 = java.lang.String[].class
            r8 = 1
            r5[r8] = r6     // Catch:{ Exception -> 0x0059 }
            java.lang.Class<long[]> r6 = long[].class
            r9 = 2
            r5[r9] = r6     // Catch:{ Exception -> 0x0059 }
            java.lang.reflect.Method r2 = r2.getMethod(r3, r5)     // Catch:{ Exception -> 0x0059 }
            java.lang.Object[] r3 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0059 }
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0059 }
            java.lang.String r5 = "MemTotal:"
            r0[r7] = r5     // Catch:{ Exception -> 0x0059 }
            java.lang.String r5 = "MemFree:"
            r0[r8] = r5     // Catch:{ Exception -> 0x0059 }
            java.lang.String r5 = "Buffers:"
            r0[r9] = r5     // Catch:{ Exception -> 0x0059 }
            java.lang.String r5 = "Cached:"
            r0[r4] = r5     // Catch:{ Exception -> 0x0059 }
            int r4 = r0.length     // Catch:{ Exception -> 0x0059 }
            long[] r4 = new long[r4]     // Catch:{ Exception -> 0x0059 }
            r5 = 30
            r4[r7] = r5     // Catch:{ Exception -> 0x0059 }
            r5 = -30
            r4[r8] = r5     // Catch:{ Exception -> 0x0059 }
            java.lang.String r5 = "/proc/meminfo"
            r3[r7] = r5     // Catch:{ Exception -> 0x0059 }
            r3[r8] = r0     // Catch:{ Exception -> 0x0059 }
            r3[r9] = r4     // Catch:{ Exception -> 0x0059 }
            if (r2 == 0) goto L_0x0058
            r0 = 0
            r2.invoke(r0, r3)     // Catch:{ Exception -> 0x0059 }
        L_0x004e:
            int r0 = r4.length     // Catch:{ Exception -> 0x0059 }
            if (r7 >= r0) goto L_0x0058
            r2 = r4[r7]     // Catch:{ Exception -> 0x0059 }
            r1[r7] = r2     // Catch:{ Exception -> 0x0059 }
            int r7 = r7 + 1
            goto L_0x004e
        L_0x0058:
            return r1
        L_0x0059:
            r0 = move-exception
            r0.printStackTrace()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.alihadeviceevaluator.mem.AliHAMemoryTracker.getMemInfo():long[]");
    }

    public long[] getPrivateDirty(Context context, int i) {
        Debug.MemoryInfo memoryInfo = ((ActivityManager) context.getSystemService("activity")).getProcessMemoryInfo(new int[]{i})[0];
        return new long[]{(long) memoryInfo.dalvikPrivateDirty, (long) memoryInfo.nativePrivateDirty, (long) memoryInfo.getTotalPrivateDirty()};
    }

    public long[] getPSS(Context context, int i) {
        long[] jArr = new long[3];
        if (i >= 0) {
            try {
                Debug.MemoryInfo memoryInfo = ((ActivityManager) context.getSystemService("activity")).getProcessMemoryInfo(new int[]{i})[0];
                jArr[0] = (long) memoryInfo.dalvikPss;
                jArr[1] = (long) memoryInfo.nativePss;
                jArr[2] = (long) memoryInfo.getTotalPss();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } else {
            jArr[0] = 0;
            jArr[1] = 0;
            jArr[2] = 0;
        }
        return jArr;
    }

    public int[] getJavaHeapLimit(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        return new int[]{activityManager.getMemoryClass(), activityManager.getLargeMemoryClass()};
    }
}
