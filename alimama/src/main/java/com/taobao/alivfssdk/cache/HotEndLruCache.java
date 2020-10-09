package com.taobao.alivfssdk.cache;

import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Locale;

public class HotEndLruCache<K, V> implements LruCache<K, V> {
    private static final int UPGRADE_HOTEND_THRESHOLD = 2;
    private int HOT_LIMIT_SIZE;
    private int MAX_LIMIT_SIZE;
    private int MAX_PRE_EVICTED_SIZE;
    private LruNode<K, V> mColdHead;
    private int mCurrSize;
    private int mEvictCount;
    private int mHitCount;
    private LruNode<K, V> mHotHead;
    private int mHotSize;
    private final HashMap<K, LruNode<K, V>> mLocationMap = new HashMap<>();
    private int mMissCount;
    private int mPreEvictedSize;
    private int mSizeWhenLastPreEvict;

    private void checkMaxSizes() {
    }

    public void board(String str) {
    }

    /* access modifiers changed from: protected */
    public int getSize(V v) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public void onNodeRemoved(boolean z, K k, V v) {
    }

    /* access modifiers changed from: protected */
    public void onPreEvictedStateChange(boolean z, K k, V v) {
    }

    public HotEndLruCache(int i, float f) {
        resize(i, f);
    }

    public void resize(int i, float f) {
        if (i < 2 || f < 0.0f || f >= 1.0f) {
            throw new RuntimeException("HotEndLruCache size parameters error");
        }
        synchronized (this) {
            this.MAX_LIMIT_SIZE = i;
            this.HOT_LIMIT_SIZE = (int) (((float) i) * f);
            if (this.HOT_LIMIT_SIZE < 1) {
                this.HOT_LIMIT_SIZE = 1;
            } else if (i - this.HOT_LIMIT_SIZE < 1) {
                this.HOT_LIMIT_SIZE = i - 1;
            }
        }
        checkMaxSizes();
        trimTo(this.MAX_LIMIT_SIZE);
    }

    public void resize(int i, float f, int i2) {
        synchronized (this) {
            this.MAX_PRE_EVICTED_SIZE = i2;
        }
        resize(i, f);
        preTrimToMaxSize(true);
    }

    public final synchronized boolean isEmpty() {
        return this.mHotHead == null;
    }

    public final synchronized int count() {
        return this.mLocationMap.size();
    }

    public final synchronized int size() {
        return this.mCurrSize;
    }

    public final synchronized int maxSize() {
        return this.MAX_LIMIT_SIZE;
    }

    /* access modifiers changed from: protected */
    public final synchronized int maxPreEvictedSize() {
        return this.MAX_PRE_EVICTED_SIZE;
    }

    public final synchronized float hotPercent() {
        return ((float) this.HOT_LIMIT_SIZE) / ((float) this.MAX_LIMIT_SIZE);
    }

    public synchronized void setPreEvictedMaxSize(int i) {
        this.MAX_PRE_EVICTED_SIZE = i;
        checkMaxSizes();
        preTrimToMaxSize(true);
    }

    private void onRemoveNode(LruNode<K, V> lruNode) {
        if (lruNode != null) {
            this.mCurrSize -= lruNode.size;
            if (!lruNode.isColdNode) {
                this.mHotSize -= lruNode.size;
            }
        }
    }

    private void onAddNewNode(LruNode<K, V> lruNode) {
        if (lruNode != null) {
            this.mCurrSize += lruNode.size;
        }
    }

    private void onNodeRemoved(boolean z, LruNode<K, V> lruNode, boolean z2) {
        onPreEvictedStateChange(false, lruNode, z2);
        onNodeRemoved(z, lruNode.key, lruNode.value);
    }

    private void onPreEvictedStateChange(boolean z, LruNode<K, V> lruNode, boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = z != lruNode.isPreEvicted;
            if (z3) {
                lruNode.isPreEvicted = z;
                if (z) {
                    this.mPreEvictedSize += lruNode.size;
                } else {
                    this.mPreEvictedSize -= lruNode.size;
                }
            }
        }
        if (z3 && z2) {
            onPreEvictedStateChange(z, lruNode.key, lruNode.value);
        }
    }

    private void remove(LruNode<K, V> lruNode) {
        if (lruNode.next == lruNode) {
            resetHotHead((LruNode) null);
            resetColdHead((LruNode) null);
        } else {
            lruNode.next.prev = lruNode.prev;
            lruNode.prev.next = lruNode.next;
            if (this.mHotHead == lruNode) {
                resetHotHead(lruNode.next);
            }
            if (this.mColdHead == lruNode) {
                resetColdHead(lruNode.next);
            }
        }
        onRemoveNode(lruNode);
    }

    /* access modifiers changed from: protected */
    public V remove(K k, boolean z) {
        LruNode remove;
        synchronized (this) {
            remove = this.mLocationMap.remove(k);
            if (remove != null) {
                remove.visitCount = -1;
                if (remove.prev != null) {
                    remove(remove);
                }
            }
        }
        if (remove == null) {
            return null;
        }
        onNodeRemoved(false, remove, z);
        return remove.value;
    }

    public final V remove(K k) {
        return remove(k, true);
    }

    public synchronized void clear() {
        this.mLocationMap.clear();
        resetHotHead((LruNode) null);
        resetColdHead((LruNode) null);
        this.mCurrSize = 0;
        this.mHotSize = 0;
        this.mPreEvictedSize = 0;
        this.mSizeWhenLastPreEvict = 0;
    }

    public final synchronized boolean contains(K k) {
        return this.mLocationMap.containsKey(k);
    }

    public V get(K k) {
        LruNode lruNode;
        synchronized (this) {
            lruNode = this.mLocationMap.get(k);
            if (lruNode != null) {
                lruNode.visitCount = lruNode.visitCount < 0 ? 1 : lruNode.visitCount + 1;
            }
        }
        if (lruNode != null) {
            onPreEvictedStateChange(false, lruNode, true);
            this.mHitCount++;
            return lruNode.value;
        }
        this.mMissCount++;
        return null;
    }

    private void makeNewHotHead(LruNode<K, V> lruNode) {
        if (this.mHotHead != null) {
            lruNode.insertBefore(this.mHotHead);
        } else {
            lruNode.prev = lruNode;
            lruNode.next = lruNode;
        }
        boolean z = this.mColdHead == this.mHotHead;
        resetHotHead(lruNode, true);
        if (this.mHotSize > this.HOT_LIMIT_SIZE && this.mColdHead != null) {
            if (z && this.mColdHead.prev != this.mColdHead) {
                this.mHotSize -= this.mColdHead.size;
                this.mColdHead.isColdNode = true;
            }
            resetColdHead(this.mColdHead.prev);
        }
    }

    private void makeNewColdHead(LruNode<K, V> lruNode) {
        if (this.mColdHead != null) {
            lruNode.insertBefore(this.mColdHead);
        }
        resetColdHead(lruNode, true);
    }

    private void resetHotHead(LruNode<K, V> lruNode) {
        resetHotHead(lruNode, false);
    }

    private void resetHotHead(LruNode<K, V> lruNode, boolean z) {
        if (lruNode != null) {
            if (z || lruNode.isColdNode) {
                this.mHotSize += lruNode.size;
            }
            lruNode.isColdNode = false;
        }
        this.mHotHead = lruNode;
    }

    private boolean resetColdHead(LruNode<K, V> lruNode) {
        return resetColdHead(lruNode, false);
    }

    private boolean resetColdHead(LruNode<K, V> lruNode, boolean z) {
        this.mColdHead = lruNode;
        if (lruNode == null || this.mHotHead == lruNode) {
            return false;
        }
        if (!z && !lruNode.isColdNode) {
            this.mHotSize -= lruNode.size;
        }
        lruNode.isColdNode = true;
        return true;
    }

    public boolean put(K k, V v) {
        return put(17, k, v);
    }

    public boolean put(int i, K k, V v) {
        LruNode put;
        if (k == null || v == null) {
            return false;
        }
        LruNode lruNode = new LruNode(k, v, getSize(v));
        if (i == 34) {
            lruNode.visitCount = 2;
        }
        if (lruNode.size > this.MAX_LIMIT_SIZE) {
            return false;
        }
        synchronized (this) {
            put = this.mLocationMap.put(k, lruNode);
            if (put != null) {
                int i2 = put.visitCount;
                remove(put);
                lruNode.visitCount = i2 + 1;
            }
        }
        if (put != null) {
            onNodeRemoved(true, put, true);
        }
        boolean trimTo = trimTo(this.MAX_LIMIT_SIZE - lruNode.size);
        synchronized (this) {
            if (!(this.mHotHead == null || this.mColdHead == null)) {
                if (trimTo) {
                    makeNewColdHead(lruNode);
                    onAddNewNode(lruNode);
                }
            }
            makeNewHotHead(lruNode);
            onAddNewNode(lruNode);
            if (this.mColdHead == null && this.mCurrSize > this.HOT_LIMIT_SIZE) {
                resetColdHead(this.mHotHead.prev);
            }
        }
        preTrimToMaxSize(trimTo);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000c, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean trimTo(int r6) {
        /*
            r5 = this;
            r0 = 0
        L_0x0001:
            monitor-enter(r5)
            int r1 = r5.mCurrSize     // Catch:{ all -> 0x0050 }
            r2 = 0
            r3 = 1
            if (r1 > r6) goto L_0x000d
            if (r0 == 0) goto L_0x000b
            r2 = 1
        L_0x000b:
            monitor-exit(r5)     // Catch:{ all -> 0x0050 }
            return r2
        L_0x000d:
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r5.mHotHead     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x0050 }
            int r0 = r0.visitCount     // Catch:{ all -> 0x0050 }
            r1 = 2
            if (r0 < r1) goto L_0x0038
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r5.mHotHead     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x0050 }
            r0.visitCount = r3     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r5.mHotHead     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x0050 }
            r5.resetHotHead(r0)     // Catch:{ all -> 0x0050 }
        L_0x0023:
            int r0 = r5.HOT_LIMIT_SIZE     // Catch:{ all -> 0x0050 }
            if (r0 <= 0) goto L_0x000d
            int r0 = r5.mHotSize     // Catch:{ all -> 0x0050 }
            int r1 = r5.HOT_LIMIT_SIZE     // Catch:{ all -> 0x0050 }
            if (r0 <= r1) goto L_0x000d
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r5.mColdHead     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x0050 }
            boolean r0 = r5.resetColdHead(r0)     // Catch:{ all -> 0x0050 }
            if (r0 == 0) goto L_0x000d
            goto L_0x0023
        L_0x0038:
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r5.mHotHead     // Catch:{ all -> 0x0050 }
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x0050 }
            java.util.HashMap<K, com.taobao.alivfssdk.cache.LruNode<K, V>> r1 = r5.mLocationMap     // Catch:{ all -> 0x0050 }
            K r4 = r0.key     // Catch:{ all -> 0x0050 }
            r1.remove(r4)     // Catch:{ all -> 0x0050 }
            r5.remove(r0)     // Catch:{ all -> 0x0050 }
            int r1 = r5.mEvictCount     // Catch:{ all -> 0x0050 }
            int r1 = r1 + r3
            r5.mEvictCount = r1     // Catch:{ all -> 0x0050 }
            monitor-exit(r5)     // Catch:{ all -> 0x0050 }
            r5.onNodeRemoved((boolean) r2, r0, (boolean) r3)
            goto L_0x0001
        L_0x0050:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0050 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.HotEndLruCache.trimTo(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void preTrimToMaxSize(boolean r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r3.mHotHead     // Catch:{ all -> 0x002e }
            if (r0 == 0) goto L_0x002c
            if (r4 != 0) goto L_0x000e
            int r4 = r3.mCurrSize     // Catch:{ all -> 0x002e }
            int r0 = r3.mSizeWhenLastPreEvict     // Catch:{ all -> 0x002e }
            if (r4 > r0) goto L_0x000e
            goto L_0x002c
        L_0x000e:
            com.taobao.alivfssdk.cache.LruNode<K, V> r4 = r3.mHotHead     // Catch:{ all -> 0x002e }
            com.taobao.alivfssdk.cache.LruNode<K, V> r4 = r4.prev     // Catch:{ all -> 0x002e }
            r0 = r4
        L_0x0013:
            int r1 = r3.mPreEvictedSize     // Catch:{ all -> 0x002e }
            int r2 = r3.MAX_PRE_EVICTED_SIZE     // Catch:{ all -> 0x002e }
            if (r1 >= r2) goto L_0x0026
            int r1 = r0.visitCount     // Catch:{ all -> 0x002e }
            r2 = 2
            if (r1 >= r2) goto L_0x0022
            r1 = 1
            r3.onPreEvictedStateChange((boolean) r1, r0, (boolean) r1)     // Catch:{ all -> 0x002e }
        L_0x0022:
            com.taobao.alivfssdk.cache.LruNode<K, V> r0 = r0.prev     // Catch:{ all -> 0x002e }
            if (r0 != r4) goto L_0x0013
        L_0x0026:
            int r4 = r3.mCurrSize     // Catch:{ all -> 0x002e }
            r3.mSizeWhenLastPreEvict = r4     // Catch:{ all -> 0x002e }
            monitor-exit(r3)
            return
        L_0x002c:
            monitor-exit(r3)
            return
        L_0x002e:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.HotEndLruCache.preTrimToMaxSize(boolean):void");
    }

    public synchronized String report() {
        return String.format(Locale.getDefault(), "[HotEndLruCache] %d/%d, hotSize:%d, preEvicted:%d, count:%d, hits:%d, misses:%d, evicts:%d", new Object[]{Integer.valueOf(this.mCurrSize), Integer.valueOf(this.MAX_LIMIT_SIZE), Integer.valueOf(this.mHotSize), Integer.valueOf(this.mPreEvictedSize), Integer.valueOf(count()), Integer.valueOf(this.mHitCount), Integer.valueOf(this.mMissCount), Integer.valueOf(this.mEvictCount)});
    }

    public final String traverse(int i) {
        return traverseFromHotHead(i);
    }

    public final String traverseFromHotHead(int i) {
        return traverse(this.mHotHead, i);
    }

    public final String traverseFromColdHead(int i) {
        return traverse(this.mColdHead, i);
    }

    private synchronized String traverse(LruNode lruNode, int i) {
        if (isEmpty()) {
            return "[NO ELEMENT]";
        }
        StringBuilder sb = new StringBuilder();
        LruNode lruNode2 = lruNode;
        int i2 = 0;
        while (true) {
            if (lruNode2 == null || i2 >= i) {
                break;
            }
            if (i2 != 0) {
                sb.append(" -> ");
            }
            sb.append(lruNode2.key);
            sb.append(Operators.ARRAY_START_STR);
            sb.append(lruNode2.size);
            sb.append(",");
            sb.append(lruNode2.isColdNode ? "cold" : "hot");
            sb.append(Operators.ARRAY_END_STR);
            if (lruNode2.next == lruNode) {
                break;
            }
            lruNode2 = lruNode2.next;
            i2++;
        }
        return sb.toString();
    }

    public final synchronized int traverseSize() {
        int i;
        LruNode<K, V> lruNode = this.mHotHead;
        i = 0;
        while (true) {
            if (lruNode == null) {
                break;
            }
            i += lruNode.size;
            if (lruNode.next == this.mHotHead) {
                break;
            }
            lruNode = lruNode.next;
        }
        return i;
    }

    public final synchronized int traverseCount() {
        int i;
        LruNode<K, V> lruNode = this.mHotHead;
        i = 0;
        while (true) {
            if (lruNode == null) {
                break;
            }
            i++;
            if (lruNode.next == this.mHotHead) {
                break;
            }
            lruNode = lruNode.next;
        }
        return i;
    }

    public final synchronized int getColdEndCount() {
        int i;
        i = 0;
        LruNode<K, V> lruNode = this.mColdHead;
        while (lruNode != null && lruNode != this.mHotHead) {
            i++;
            lruNode = lruNode.next;
        }
        return i;
    }

    public final int getHotEndCount() {
        return count() - getColdEndCount();
    }
}
