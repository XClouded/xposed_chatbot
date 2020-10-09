package android.taobao.windvane.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 2230704826523879449L;
    private Object lock = new Object();
    private long maxSize = 10;

    public FixedSizeLinkedHashMap() {
    }

    public FixedSizeLinkedHashMap(long j) {
        this.maxSize = j;
    }

    public void setMaxSize(long j) {
        this.maxSize = j;
    }

    public long getMaxSize() {
        return this.maxSize;
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return ((long) size()) > this.maxSize;
    }

    public V get(Object obj) {
        V v;
        synchronized (this.lock) {
            v = super.get(obj);
        }
        return v;
    }

    public V put(K k, V v) {
        V put;
        synchronized (this.lock) {
            put = super.put(k, v);
        }
        return put;
    }
}
