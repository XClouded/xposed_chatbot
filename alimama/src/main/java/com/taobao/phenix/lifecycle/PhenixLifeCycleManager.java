package com.taobao.phenix.lifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PhenixLifeCycleManager implements IPhenixLifeCycle {
    private List<IPhenixLifeCycle> lifeCycles;
    private Lock readLock;
    private Lock writeLock;

    private PhenixLifeCycleManager() {
        this.lifeCycles = new ArrayList(2);
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    public static PhenixLifeCycleManager instance() {
        return Holder.INSTANCE;
    }

    public void addLifeCycle(IPhenixLifeCycle iPhenixLifeCycle) {
        this.writeLock.lock();
        if (iPhenixLifeCycle != null) {
            try {
                if (!this.lifeCycles.contains(iPhenixLifeCycle)) {
                    this.lifeCycles.add(iPhenixLifeCycle);
                }
            } catch (Throwable th) {
                this.writeLock.unlock();
                throw th;
            }
        }
        this.writeLock.unlock();
    }

    public void removeLifeCycle(IPhenixLifeCycle iPhenixLifeCycle) {
        this.writeLock.lock();
        try {
            this.lifeCycles.remove(iPhenixLifeCycle);
        } finally {
            this.writeLock.unlock();
        }
    }

    public void onRequest(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            for (IPhenixLifeCycle onRequest : this.lifeCycles) {
                onRequest.onRequest(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onError(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            for (IPhenixLifeCycle onError : this.lifeCycles) {
                onError.onError(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onCancel(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            for (IPhenixLifeCycle onCancel : this.lifeCycles) {
                onCancel.onCancel(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onFinished(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            for (IPhenixLifeCycle onFinished : this.lifeCycles) {
                onFinished.onFinished(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onEvent(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            for (IPhenixLifeCycle onEvent : this.lifeCycles) {
                onEvent.onEvent(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static final PhenixLifeCycleManager INSTANCE = new PhenixLifeCycleManager();

        private Holder() {
        }
    }
}
