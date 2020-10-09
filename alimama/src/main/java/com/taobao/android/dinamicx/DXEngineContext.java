package com.taobao.android.dinamicx;

import java.lang.ref.WeakReference;

public class DXEngineContext {
    protected DXEngineConfig config;
    private WeakReference<DinamicXEngine> engineWeakReference;

    public DXEngineConfig getConfig() {
        return this.config;
    }

    public DXEngineContext(DXEngineConfig dXEngineConfig) {
        this.config = dXEngineConfig;
    }

    public void setEngine(DinamicXEngine dinamicXEngine) {
        this.engineWeakReference = new WeakReference<>(dinamicXEngine);
    }

    public DinamicXEngine getEngine() {
        if (this.engineWeakReference == null) {
            return null;
        }
        return (DinamicXEngine) this.engineWeakReference.get();
    }

    public void postMessage(DXRootView dXRootView, Object obj) {
        if (getEngine() != null) {
            getEngine().postMessage(dXRootView, obj);
        }
    }

    public long fetchRemoteTimeSync() {
        DXRemoteTimeInterface dxRemoteTimeInterface;
        if (getEngine() == null || (dxRemoteTimeInterface = getEngine().getDxRemoteTimeInterface()) == null) {
            return -1;
        }
        return dxRemoteTimeInterface.fetchRemoteTimeSync();
    }
}
