package com.ali.telescope.internal.plugins.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Process;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeUtils;
import com.taobao.weex.analyzer.Config;

public class MemoryTracker {
    public static volatile MemoryRecord sCachedStatus;

    public static MemoryRecord getRealTimeStatus(Context context) {
        Debug.MemoryInfo memoryInfo = getMemoryInfo(context);
        if (memoryInfo != null) {
            MemoryRecord memoryRecord = new MemoryRecord();
            memoryRecord.timeStamp = TimeUtils.getTime();
            memoryRecord.totalPss = memoryInfo.getTotalPss() / 1024;
            int i = memoryInfo.dalvikPss / 1024;
            if (i == 0) {
                i = (((int) Runtime.getRuntime().totalMemory()) / 1024) / 1024;
            }
            memoryRecord.dalvikPss = i;
            int i2 = memoryInfo.nativePss / 1024;
            if (i2 == 0) {
                i2 = (((int) Debug.getNativeHeapSize()) / 1024) / 1024;
            }
            memoryRecord.nativePss = i2;
            TelescopeLog.i(Config.TYPE_MEMORY, "memoryRecord.dalvikPss:", Integer.valueOf(memoryRecord.dalvikPss), "memoryRecord.nativePss:", Integer.valueOf(memoryRecord.nativePss), "memoryRecord.totalPss:", Integer.valueOf(memoryRecord.totalPss));
            sCachedStatus = memoryRecord;
            return memoryRecord;
        }
        sCachedStatus = null;
        return null;
    }

    public static MemoryRecord getCachedStatus() {
        return sCachedStatus;
    }

    private static Debug.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return null;
        }
        if (DeviceInfoManager.instance().getApiLevel() >= 23) {
            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
            if (processMemoryInfo != null) {
                return processMemoryInfo[0];
            }
            return null;
        }
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        if (memoryInfo.getTotalPrivateDirty() != 0) {
            return memoryInfo;
        }
        Debug.getMemoryInfo(memoryInfo);
        return memoryInfo;
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
