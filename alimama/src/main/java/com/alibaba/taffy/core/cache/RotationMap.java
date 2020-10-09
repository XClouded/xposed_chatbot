package com.alibaba.taffy.core.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class RotationMap<K, V> implements Serializable {
    private static final int DEFAULT_NUM_BUCKETS = 3;
    private static final long serialVersionUID = -3338540531940954473L;
    private LinkedList<Map<K, V>> buckets;
    private CacheMapListener<K, V> listener;

    public RotationMap(int i, CacheMapListener<K, V> cacheMapListener) {
        if (i >= 2) {
            this.buckets = new LinkedList<>();
            for (int i2 = 0; i2 < i; i2++) {
                this.buckets.add(new HashMap());
            }
            this.listener = cacheMapListener;
            return;
        }
        throw new IllegalArgumentException("buckets must be >= 2");
    }

    public RotationMap(CacheMapListener<K, V> cacheMapListener) {
        this(3, cacheMapListener);
    }

    public RotationMap(int i) {
        this(i, (CacheMapListener) null);
    }

    public Map<K, V> rotate() {
        Map<K, V> removeLast = this.buckets.removeLast();
        if (removeLast == null || removeLast.isEmpty()) {
            this.buckets.addFirst(removeLast);
        } else {
            this.buckets.addFirst(new HashMap());
        }
        if (!(this.listener == null || removeLast == null)) {
            for (Map.Entry next : removeLast.entrySet()) {
                this.listener.onExpire(next.getKey(), next.getValue());
            }
        }
        return removeLast;
    }

    public boolean containsKey(K k) {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            if (((Map) it.next()).containsKey(k)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            if (!((Map) it.next()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public V get(K k) {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            Map map = (Map) it.next();
            if (map.containsKey(k)) {
                return map.get(k);
            }
        }
        return null;
    }

    public void put(K k, V v) {
        Iterator it = this.buckets.iterator();
        ((Map) it.next()).put(k, v);
        while (it.hasNext()) {
            ((Map) it.next()).remove(k);
        }
    }

    public void offer(K k, V v) {
        Iterator<Map<K, V>> descendingIterator = this.buckets.descendingIterator();
        Map map = null;
        while (descendingIterator.hasNext()) {
            map = descendingIterator.next();
            if (map.containsKey(k)) {
                map.put(k, v);
                return;
            }
        }
        if (map != null) {
            map.put(k, v);
        }
    }

    public Object remove(K k) {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            Map map = (Map) it.next();
            if (map.containsKey(k)) {
                return map.remove(k);
            }
        }
        return null;
    }

    public int size() {
        Iterator it = this.buckets.iterator();
        int i = 0;
        while (it.hasNext()) {
            i += ((Map) it.next()).size();
        }
        return i;
    }

    public void setListener(CacheMapListener<K, V> cacheMapListener) {
        this.listener = cacheMapListener;
    }
}
