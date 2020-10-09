package com.taobao.phenix.cache.memory;

import com.taobao.phenix.cache.LruCache;

public class NonOpMemoryCache implements LruCache<String, CachedRootImage> {
    public void clear() {
    }

    public int count() {
        return 0;
    }

    public CachedRootImage get(String str) {
        return null;
    }

    public float hotPercent() {
        return 0.0f;
    }

    public boolean isEmpty() {
        return true;
    }

    public int maxSize() {
        return 0;
    }

    public boolean put(int i, String str, CachedRootImage cachedRootImage) {
        return false;
    }

    public boolean put(String str, CachedRootImage cachedRootImage) {
        return false;
    }

    public CachedRootImage remove(String str) {
        return null;
    }

    public void resize(int i, float f) {
    }

    public int size() {
        return 0;
    }

    public boolean trimTo(int i) {
        return false;
    }
}
