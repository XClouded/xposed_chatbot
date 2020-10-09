package com.taobao.alivfssdk.fresco.cache.common;

import androidx.annotation.Nullable;
import com.taobao.alivfssdk.fresco.cache.common.CacheErrorLogger;

public class NoOpCacheErrorLogger implements CacheErrorLogger {
    private static NoOpCacheErrorLogger sInstance;

    public void logError(CacheErrorLogger.CacheErrorCategory cacheErrorCategory, String str, String str2, @Nullable Throwable th) {
    }

    private NoOpCacheErrorLogger() {
    }

    public static synchronized NoOpCacheErrorLogger getInstance() {
        NoOpCacheErrorLogger noOpCacheErrorLogger;
        synchronized (NoOpCacheErrorLogger.class) {
            if (sInstance == null) {
                sInstance = new NoOpCacheErrorLogger();
            }
            noOpCacheErrorLogger = sInstance;
        }
        return noOpCacheErrorLogger;
    }
}
