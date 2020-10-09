package com.taobao.phenix.compat;

import com.taobao.fresco.disk.cache.CacheEventListener;

public class NoOpCacheEventListener implements CacheEventListener {
    private static NoOpCacheEventListener sInstance;

    public void onEviction(CacheEventListener.EvictionReason evictionReason, int i, long j) {
    }

    public void onHit() {
    }

    public void onMiss() {
    }

    public void onReadException() {
    }

    public void onWriteAttempt() {
    }

    public void onWriteException() {
    }

    private NoOpCacheEventListener() {
    }

    public static synchronized NoOpCacheEventListener instance() {
        NoOpCacheEventListener noOpCacheEventListener;
        synchronized (NoOpCacheEventListener.class) {
            if (sInstance == null) {
                sInstance = new NoOpCacheEventListener();
            }
            noOpCacheEventListener = sInstance;
        }
        return noOpCacheEventListener;
    }
}
