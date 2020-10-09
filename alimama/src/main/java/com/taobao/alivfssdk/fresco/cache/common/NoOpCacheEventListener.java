package com.taobao.alivfssdk.fresco.cache.common;

public class NoOpCacheEventListener implements CacheEventListener {
    private static NoOpCacheEventListener sInstance;

    public boolean onEviction(CacheEvent cacheEvent) {
        return false;
    }

    public void onHit(CacheEvent cacheEvent) {
    }

    public void onMiss(CacheEvent cacheEvent) {
    }

    public void onReadException(CacheEvent cacheEvent) {
    }

    public void onRemoveSuccess(CacheEvent cacheEvent) {
    }

    public void onWriteAttempt(CacheEvent cacheEvent) {
    }

    public void onWriteException(CacheEvent cacheEvent) {
    }

    public void onWriteSuccess(CacheEvent cacheEvent) {
    }

    private NoOpCacheEventListener() {
    }

    public static synchronized NoOpCacheEventListener getInstance() {
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
