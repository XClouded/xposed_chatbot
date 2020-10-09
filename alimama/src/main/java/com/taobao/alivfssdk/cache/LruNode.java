package com.taobao.alivfssdk.cache;

import com.taobao.weex.el.parse.Operators;

public class LruNode<K, V> {
    public boolean isColdNode;
    public boolean isPreEvicted;
    public K key;
    public LruNode<K, V> next;
    public LruNode<K, V> prev;
    public int size;
    public boolean unlinked;
    public V value;
    public int visitCount;

    public LruNode(K k, V v, int i) {
        reset(k, v, i);
    }

    public void insertBefore(LruNode<K, V> lruNode) {
        if (!(this.prev == null || this.prev == this)) {
            this.prev.next = this.next;
        }
        if (!(this.next == null || this.next == this)) {
            this.next.prev = this.prev;
        }
        this.next = lruNode;
        if (lruNode.prev != null) {
            lruNode.prev.next = this;
        }
        this.prev = lruNode.prev;
        lruNode.prev = this;
    }

    public void reset(K k, V v, int i) {
        this.key = k;
        this.value = v;
        this.visitCount = 1;
        this.size = i;
    }

    public void unlink() {
        if (this.next != null) {
            this.next.prev = this.prev;
        }
        if (this.prev != null) {
            this.prev.next = this.next;
        }
        this.prev = null;
        this.next = null;
        this.unlinked = true;
    }

    public String toString() {
        return "LruNode@" + hashCode() + "[key:" + this.key + ", value:" + this.value + ", visitCount:" + this.visitCount + ", size:" + this.size + ", isColdNode:" + this.isColdNode + ", unlinked:" + this.unlinked + Operators.ARRAY_END_STR;
    }
}
