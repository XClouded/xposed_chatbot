package com.taobao.weex.devtools.inspector.helper;

import com.taobao.weex.devtools.common.ThreadBound;
import com.taobao.weex.devtools.common.UncheckedCallable;
import com.taobao.weex.devtools.common.Util;

public abstract class ThreadBoundProxy implements ThreadBound {
    private final ThreadBound mEnforcer;

    public ThreadBoundProxy(ThreadBound threadBound) {
        this.mEnforcer = (ThreadBound) Util.throwIfNull(threadBound);
    }

    public final boolean checkThreadAccess() {
        return this.mEnforcer.checkThreadAccess();
    }

    public final void verifyThreadAccess() {
        this.mEnforcer.verifyThreadAccess();
    }

    public final <V> V postAndWait(UncheckedCallable<V> uncheckedCallable) {
        return this.mEnforcer.postAndWait(uncheckedCallable);
    }

    public final void postAndWait(Runnable runnable) {
        this.mEnforcer.postAndWait(runnable);
    }

    public final void postDelayed(Runnable runnable, long j) {
        this.mEnforcer.postDelayed(runnable, j);
    }

    public final void removeCallbacks(Runnable runnable) {
        this.mEnforcer.removeCallbacks(runnable);
    }
}
