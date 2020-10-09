package com.taobao.android.dxcontainer;

public class DXContainerBaseClass {
    protected DXContainerEngineConfig containerEngineConfig;
    protected DXContainerEngineContext containerEngineContext;

    public DXContainerBaseClass(DXContainerEngineContext dXContainerEngineContext) {
        this.containerEngineContext = dXContainerEngineContext;
        this.containerEngineConfig = dXContainerEngineContext.engineConfig;
    }

    public DXContainerEngineContext getContainerEngineContext() {
        return this.containerEngineContext;
    }

    public DXContainerEngineConfig getContainerEngineConfig() {
        return this.containerEngineConfig;
    }
}
