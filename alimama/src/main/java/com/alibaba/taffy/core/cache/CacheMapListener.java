package com.alibaba.taffy.core.cache;

public interface CacheMapListener<K, V> {
    void onExpire(K k, V v);
}
