package com.alibaba.android.umbrella.link;

import androidx.collection.LruCache;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
final class LinkLogCache<T> {
    private final AtomicInteger listIndex = new AtomicInteger(0);
    private final LruCache<Integer, T> lruCache = new LruCache<>(300);

    interface LinkLogConsumer<T> {
        void consume(T t);
    }

    LinkLogCache() {
    }

    /* access modifiers changed from: package-private */
    public void add(T t) {
        this.lruCache.put(Integer.valueOf(this.listIndex.getAndIncrement()), t);
    }

    /* access modifiers changed from: package-private */
    public void consumeAndEraseLocked(LinkLogConsumer<T> linkLogConsumer) {
        for (T consume : this.lruCache.snapshot().values()) {
            linkLogConsumer.consume(consume);
        }
        this.lruCache.evictAll();
        this.listIndex.set(0);
    }

    /* access modifiers changed from: package-private */
    public void resizeCacheIfNeed(int i) {
        if (this.lruCache.maxSize() != i) {
            this.lruCache.resize(i);
        }
    }
}
