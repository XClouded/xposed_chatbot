package com.taobao.network.lifecycle;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MtopLifecycleManager implements IMtopLifecycle {
    private IMtopLifecycle lifecycle;
    private Lock readLock;
    private Lock writeLock;

    private MtopLifecycleManager() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    public static MtopLifecycleManager instance() {
        return Holder.INSTANCE;
    }

    public void setLifecycle(IMtopLifecycle iMtopLifecycle) {
        this.writeLock.lock();
        try {
            if (this.lifecycle == null) {
                this.lifecycle = iMtopLifecycle;
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public void removeLifecycle(IMtopLifecycle iMtopLifecycle) {
        this.writeLock.lock();
        try {
            this.lifecycle = null;
        } finally {
            this.writeLock.unlock();
        }
    }

    public void onMtopRequest(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onMtopRequest(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onMtopError(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onMtopError(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onMtopCancel(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onMtopCancel(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onMtopFinished(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onMtopFinished(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onMtopEvent(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onMtopEvent(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    private static final class Holder {
        /* access modifiers changed from: private */
        public static final MtopLifecycleManager INSTANCE = new MtopLifecycleManager();

        private Holder() {
        }
    }
}
