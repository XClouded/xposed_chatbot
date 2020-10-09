package com.alibaba.taffy.core.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUCacheMap<K, V> implements Map<K, V> {
    private int cacheSize;
    private LinkedHashMap<K, V> instance;
    private CacheMapListener<K, V> listener;

    public void clear() {
        this.instance.clear();
    }

    public boolean containsKey(Object obj) {
        return this.instance.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.instance.containsValue(obj);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.instance.entrySet();
    }

    public V get(Object obj) {
        return this.instance.get(obj);
    }

    public boolean isEmpty() {
        return this.instance.isEmpty();
    }

    public Set<K> keySet() {
        return this.instance.keySet();
    }

    public V put(K k, V v) {
        return this.instance.put(k, v);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        this.instance.putAll(map);
    }

    public V remove(Object obj) {
        return this.instance.remove(obj);
    }

    public int size() {
        return this.instance.size();
    }

    public Collection<V> values() {
        return this.instance.values();
    }

    public LRUCacheMap(int i) {
        setCacheSize(i);
    }

    public LRUCacheMap(int i, CacheMapListener<K, V> cacheMapListener) {
        this(i);
        this.listener = cacheMapListener;
    }

    public void setCacheSize(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("cache size must be > 0");
        } else if (this.cacheSize != i) {
            this.cacheSize = i;
            double d = (double) i;
            Double.isNaN(d);
            AnonymousClass1 r0 = new LinkedHashMap<K, V>(((int) Math.ceil(d / 0.75d)) + 1, 0.75f, true) {
                /* access modifiers changed from: protected */
                public boolean removeEldestEntry(Map.Entry<K, V> entry) {
                    return LRUCacheMap.this.onExpire(entry);
                }
            };
            if (this.instance != null && !this.instance.isEmpty()) {
                r0.putAll(this.instance);
                this.instance.clear();
            }
            this.instance = r0;
        }
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    /* access modifiers changed from: protected */
    public boolean onExpire(Map.Entry<K, V> entry) {
        boolean z = size() > this.cacheSize;
        if (z && this.listener != null) {
            this.listener.onExpire(entry.getKey(), entry.getValue());
        }
        return z;
    }

    public void setListener(CacheMapListener<K, V> cacheMapListener) {
        this.listener = cacheMapListener;
    }
}
