package com.taobao.weex.analyzer.core.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Process;

public class MemoryTracker {
    private static Debug.MemoryInfo getMemoryInfo(Context context) {
        Debug.MemoryInfo memoryInfo;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
                if (processMemoryInfo == null) {
                    return null;
                }
                memoryInfo = processMemoryInfo[0];
            } else {
                memoryInfo = new Debug.MemoryInfo();
                try {
                    Debug.getMemoryInfo(memoryInfo);
                    if (memoryInfo.getTotalPrivateDirty() == 0) {
                        Debug.getMemoryInfo(memoryInfo);
                    }
                } catch (Exception unused) {
                }
            }
            return memoryInfo;
        } catch (Exception unused2) {
            return null;
        }
    }

    public static long getDalvikPss(Context context) {
        Debug.MemoryInfo memoryInfo = getMemoryInfo(context);
        if (memoryInfo == null) {
            return 0;
        }
        long j = (long) (memoryInfo.dalvikPss / 1024);
        return j == 0 ? (Runtime.getRuntime().totalMemory() / 1024) / 1024 : j;
    }

    public static long getNativePss(Context context) {
        Debug.MemoryInfo memoryInfo = getMemoryInfo(context);
        if (memoryInfo == null) {
            return 0;
        }
        long j = (long) (memoryInfo.nativePss / 1024);
        return j == 0 ? (Debug.getNativeHeapSize() / 1024) / 1024 : j;
    }

    public static long getTotalPss(Context context) {
        Debug.MemoryInfo memoryInfo = getMemoryInfo(context);
        if (memoryInfo != null) {
            return (long) (memoryInfo.getTotalPss() / 1024);
        }
        return 0;
    }
}
