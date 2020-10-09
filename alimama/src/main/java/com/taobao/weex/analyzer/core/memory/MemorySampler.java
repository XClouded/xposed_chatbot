package com.taobao.weex.analyzer.core.memory;

import androidx.annotation.WorkerThread;
import com.taobao.weex.analyzer.WeexDevOptions;

public class MemorySampler {
    private MemorySampler() {
    }

    public static double getMemoryUsage() {
        return (double) MemoryTracker.getDalvikPss(WeexDevOptions.sApplicationContext);
    }

    public static double maxMemory() {
        return (double) MemoryTracker.getTotalPss(WeexDevOptions.sApplicationContext);
    }

    public static double totalMemory() {
        return (double) MemoryTracker.getTotalPss(WeexDevOptions.sApplicationContext);
    }

    @WorkerThread
    public static void tryForceGC() {
        Runtime.getRuntime().gc();
        enqueueReferences();
        System.runFinalization();
    }

    private static void enqueueReferences() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException unused) {
            throw new AssertionError();
        }
    }
}
