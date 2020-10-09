package com.ali.telescope.internal.plugins.memleak;

import androidx.annotation.Keep;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

@Keep
public class KeyedWeakReference<T> extends WeakReference<T> implements Serializable {
    public byte[] key;

    public KeyedWeakReference(T t, String str) {
        super(t);
        this.key = str.getBytes();
    }

    public KeyedWeakReference(T t, ReferenceQueue<? super T> referenceQueue, String str) {
        super(t, referenceQueue);
        this.key = str.getBytes();
    }
}
