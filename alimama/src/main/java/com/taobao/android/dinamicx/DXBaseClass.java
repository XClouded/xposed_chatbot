package com.taobao.android.dinamicx;

import androidx.annotation.NonNull;

public class DXBaseClass {
    protected String bizType;
    protected DXEngineConfig config;
    protected DXEngineContext engineContext;

    public DXBaseClass(@NonNull DXEngineContext dXEngineContext) {
        if (dXEngineContext == null) {
            this.config = new DXEngineConfig(DXEngineConfig.DX_DEFAULT_BIZTYPE);
            this.bizType = this.config.bizType;
            this.engineContext = new DXEngineContext(this.config);
            return;
        }
        this.engineContext = dXEngineContext;
        this.config = dXEngineContext.config;
        this.bizType = this.config.bizType;
    }

    protected DXBaseClass(@NonNull DXEngineConfig dXEngineConfig) {
        if (dXEngineConfig == null) {
            this.config = new DXEngineConfig(DXEngineConfig.DX_DEFAULT_BIZTYPE);
            this.bizType = this.config.bizType;
            return;
        }
        this.config = dXEngineConfig;
        this.bizType = dXEngineConfig.bizType;
    }

    public String getBizType() {
        return this.bizType;
    }

    public DXEngineConfig getConfig() {
        return this.config;
    }

    public DXEngineContext getEngineContext() {
        return this.engineContext;
    }
}
