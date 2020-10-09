package com.taobao.phenix.compat;

import com.taobao.fresco.disk.cache.CacheKey;

public class SimpleCacheKey implements CacheKey {
    private final String mKey;

    public SimpleCacheKey(String str, int i) {
        this.mKey = str + i;
    }

    public String toString() {
        return this.mKey;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SimpleCacheKey) {
            return this.mKey.equals(((SimpleCacheKey) obj).mKey);
        }
        return false;
    }

    public int hashCode() {
        return this.mKey.hashCode();
    }
}
