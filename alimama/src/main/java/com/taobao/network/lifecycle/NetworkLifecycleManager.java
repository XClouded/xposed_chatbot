package com.taobao.network.lifecycle;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkLifecycleManager implements INetworkLifecycle {
    private INetworkLifecycle lifecycle;
    private Lock readLock;
    private Lock writeLock;

    private NetworkLifecycleManager() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    public static NetworkLifecycleManager instance() {
        return Holder.INSTANCE;
    }

    public void setLifecycle(INetworkLifecycle iNetworkLifecycle) {
        this.writeLock.lock();
        try {
            if (this.lifecycle == null) {
                this.lifecycle = iNetworkLifecycle;
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public void removeLifecycle(INetworkLifecycle iNetworkLifecycle) {
        this.writeLock.lock();
        try {
            this.lifecycle = null;
        } finally {
            this.writeLock.unlock();
        }
    }

    public void onRequest(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onRequest(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onValidRequest(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onValidRequest(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onError(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onError(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onCancel(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onCancel(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onFinished(String str, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onFinished(str, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public void onEvent(String str, String str2, Map<String, Object> map) {
        this.readLock.lock();
        try {
            if (this.lifecycle != null) {
                this.lifecycle.onEvent(str, str2, map);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    private static final class Holder {
        /* access modifiers changed from: private */
        public static final NetworkLifecycleManager INSTANCE = new NetworkLifecycleManager();

        private Holder() {
        }
    }
}
