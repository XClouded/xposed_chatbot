package com.taobao.rxm.request;

import com.taobao.rxm.produce.ProducerListener;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RequestContext {
    private static final AtomicInteger sAtomicId = new AtomicInteger(1);
    private Set<RequestCancelListener> mCancelListeners;
    private volatile boolean mCancelled;
    private volatile boolean mCancelledInMultiplex;
    private final boolean mEnableGenericTypeCheck;
    private final int mId;
    private MultiplexCancelListener mMultiplexCancelListener;
    private volatile int mMultiplexPipeline;
    private ProducerListener mProducerListener;
    private int mSchedulePriority;

    public abstract String getMultiplexKey();

    public abstract void syncFrom(RequestContext requestContext);

    public RequestContext(boolean z) {
        this.mSchedulePriority = 2;
        synchronized (sAtomicId) {
            if (sAtomicId.get() < 0) {
                sAtomicId.set(1);
            }
            this.mId = sAtomicId.getAndIncrement();
        }
        this.mEnableGenericTypeCheck = z;
    }

    public RequestContext() {
        this(true);
    }

    public int getId() {
        return this.mId;
    }

    public int getSchedulePriority() {
        return this.mSchedulePriority;
    }

    public void setSchedulePriority(int i) {
        this.mSchedulePriority = i;
    }

    public void cancel() {
        this.mCancelledInMultiplex = true;
        if (this.mMultiplexCancelListener != null) {
            this.mMultiplexCancelListener.onCancelRequest(this);
        }
        if (!isMultiplexPipeline()) {
            cancelInMultiplex(true);
        }
    }

    public boolean isCancelled() {
        return this.mCancelled;
    }

    public void cancelInMultiplex(boolean z) {
        this.mCancelled = z;
        if (z) {
            callCancelListeners();
        }
    }

    private void callCancelListeners() {
        HashSet<RequestCancelListener> hashSet;
        int size;
        synchronized (this) {
            if (this.mCancelListeners == null || (size = this.mCancelListeners.size()) <= 0) {
                hashSet = null;
            } else {
                hashSet = new HashSet<>(size);
                hashSet.addAll(this.mCancelListeners);
            }
        }
        if (hashSet != null) {
            for (RequestCancelListener onCancel : hashSet) {
                onCancel.onCancel(this);
            }
            hashSet.clear();
        }
    }

    public boolean isCancelledInMultiplex() {
        return this.mCancelledInMultiplex;
    }

    public boolean isMultiplexPipeline() {
        return this.mMultiplexPipeline == this.mId;
    }

    public int getMultiplexPipeline() {
        return this.mMultiplexPipeline;
    }

    /* access modifiers changed from: protected */
    public synchronized void reset() {
        this.mMultiplexPipeline = 0;
        if (this.mCancelListeners != null) {
            this.mCancelListeners.clear();
        }
    }

    public void setMultiplexPipeline(int i) {
        this.mMultiplexPipeline = i;
    }

    public void setMultiplexCancelListener(MultiplexCancelListener multiplexCancelListener) {
        this.mMultiplexCancelListener = multiplexCancelListener;
    }

    public boolean registerCancelListener(RequestCancelListener requestCancelListener) {
        boolean add;
        if (this.mEnableGenericTypeCheck) {
            Class cls = (Class) ((ParameterizedType) requestCancelListener.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            if (!cls.isInstance(this)) {
                throw new RuntimeException("this[" + getClass() + "] CANNOT be assigned to generic[" + cls + "] of RequestCancelListener");
            }
        }
        synchronized (this) {
            if (this.mCancelListeners == null) {
                this.mCancelListeners = new HashSet();
            }
            add = this.mCancelListeners.add(requestCancelListener);
        }
        return add;
    }

    public synchronized boolean unregisterCancelListener(RequestCancelListener requestCancelListener) {
        return this.mCancelListeners != null && this.mCancelListeners.remove(requestCancelListener);
    }

    public ProducerListener getProducerListener() {
        return this.mProducerListener;
    }

    public void setProducerListener(ProducerListener producerListener) {
        this.mProducerListener = producerListener;
    }
}
