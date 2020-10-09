package com.alibaba.taffy.core.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RotationBlockingMap<K, V> implements Serializable {
    private static final int DEFAULT_NUM_BUCKETS = 3;
    private static final long serialVersionUID = -8837572949829754803L;
    private LinkedList<Map<K, V>> buckets;
    private int capacity;
    private CacheMapListener<K, V> listener;
    private final ReentrantReadWriteLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public RotationBlockingMap(int i, int i2, CacheMapListener<K, V> cacheMapListener) {
        if (i2 >= 2) {
            this.capacity = i;
            this.lock = new ReentrantReadWriteLock();
            this.notFull = this.lock.writeLock().newCondition();
            this.notEmpty = this.lock.writeLock().newCondition();
            this.buckets = new LinkedList<>();
            for (int i3 = 0; i3 < i2; i3++) {
                this.buckets.add(new HashMap());
            }
            this.listener = cacheMapListener;
            return;
        }
        throw new IllegalArgumentException("buckets must be >= 2");
    }

    public RotationBlockingMap(CacheMapListener<K, V> cacheMapListener) {
        this(-1, 3, cacheMapListener);
    }

    public RotationBlockingMap(int i) {
        this(-1, i, (CacheMapListener) null);
    }

    private static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    /* JADX INFO: finally extract failed */
    public Map<K, V> rotate() throws InterruptedException {
        this.lock.writeLock().lockInterruptibly();
        while (isEmpty()) {
            try {
                this.notEmpty.await();
            } catch (Throwable th) {
                this.lock.writeLock().unlock();
                throw th;
            }
        }
        Map<K, V> extract = extract();
        this.lock.writeLock().unlock();
        if (this.listener != null) {
            for (Map.Entry next : extract.entrySet()) {
                this.listener.onExpire(next.getKey(), next.getValue());
            }
        }
        return extract;
    }

    /* JADX INFO: finally extract failed */
    public int rotateTo(Map<? super K, ? super V> map) throws InterruptedException {
        checkNotNull(map);
        if (map != this) {
            this.lock.writeLock().lockInterruptibly();
            while (isEmpty()) {
                try {
                    this.notEmpty.await();
                } catch (Throwable th) {
                    this.lock.writeLock().unlock();
                    throw th;
                }
            }
            Map extract = extract();
            this.lock.writeLock().unlock();
            map.putAll(extract);
            return extract.size();
        }
        throw new IllegalArgumentException();
    }

    public boolean containsKey(K k) {
        this.lock.readLock().lock();
        try {
            Iterator it = this.buckets.iterator();
            while (it.hasNext()) {
                if (((Map) it.next()).containsKey(k)) {
                    return true;
                }
            }
            this.lock.readLock().unlock();
            return false;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        this.lock.readLock().lock();
        try {
            Iterator it = this.buckets.iterator();
            while (it.hasNext()) {
                if (!((Map) it.next()).isEmpty()) {
                    return false;
                }
            }
            this.lock.readLock().unlock();
            return true;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public V get(K k) {
        this.lock.readLock().lock();
        try {
            Iterator it = this.buckets.iterator();
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.containsKey(k)) {
                    return map.get(k);
                }
            }
            this.lock.readLock().unlock();
            return null;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void put(K k, V v) throws InterruptedException {
        checkNotNull(k);
        this.lock.writeLock().lockInterruptibly();
        try {
            if (containsKey(k)) {
                insert(k, v);
            } else {
                while (this.capacity > 0 && size() == this.capacity) {
                    this.notFull.await();
                }
                insert(k, v);
                this.notEmpty.signal();
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private void insert(K k, V v) {
        Iterator it = this.buckets.iterator();
        ((Map) it.next()).put(k, v);
        while (it.hasNext()) {
            ((Map) it.next()).remove(k);
        }
    }

    private void insertReverse(K k, V v) {
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

    private Map<K, V> extract() {
        Map<K, V> removeLast = this.buckets.removeLast();
        this.buckets.addFirst(new HashMap());
        if (!removeLast.isEmpty()) {
            this.notFull.signal();
        }
        return removeLast;
    }

    public void offer(K k, V v) throws InterruptedException {
        checkNotNull(k);
        this.lock.writeLock().lockInterruptibly();
        try {
            if (containsKey(k)) {
                insertReverse(k, v);
            } else {
                while (this.capacity > 0 && size() == this.capacity) {
                    this.notFull.await();
                }
                insertReverse(k, v);
                this.notEmpty.signal();
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Object remove(K k) {
        this.lock.writeLock().lock();
        try {
            Iterator it = this.buckets.iterator();
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.containsKey(k)) {
                    return map.remove(k);
                }
            }
            this.lock.writeLock().unlock();
            return null;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public int size() {
        this.lock.readLock().lock();
        try {
            Iterator it = this.buckets.iterator();
            int i = 0;
            while (it.hasNext()) {
                i += ((Map) it.next()).size();
            }
            return i;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void setListener(CacheMapListener<K, V> cacheMapListener) {
        this.listener = cacheMapListener;
    }
}
