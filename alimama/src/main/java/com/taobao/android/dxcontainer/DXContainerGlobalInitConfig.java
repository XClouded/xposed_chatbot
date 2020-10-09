package com.taobao.android.dxcontainer;

public final class DXContainerGlobalInitConfig {
    protected IDXContainerAppMonitor idxContainerAppMonitor;
    protected boolean isDebug;
    protected IDXContainerRecyclerViewInterface recyclerViewInterface;

    private DXContainerGlobalInitConfig(Builder builder) {
        this.recyclerViewInterface = builder.recyclerViewInterface;
        this.idxContainerAppMonitor = builder.idxContainerAppMonitor;
        this.isDebug = builder.isDebug;
    }

    public static final class Builder {
        protected IDXContainerAppMonitor idxContainerAppMonitor;
        /* access modifiers changed from: private */
        public boolean isDebug;
        /* access modifiers changed from: private */
        public IDXContainerRecyclerViewInterface recyclerViewInterface;

        public Builder withRecyclerViewBuilder(IDXContainerRecyclerViewInterface iDXContainerRecyclerViewInterface) {
            this.recyclerViewInterface = iDXContainerRecyclerViewInterface;
            return this;
        }

        public Builder withIDXContainerAppMonitor(IDXContainerAppMonitor iDXContainerAppMonitor) {
            this.idxContainerAppMonitor = iDXContainerAppMonitor;
            return this;
        }

        public Builder withIsDebug(boolean z) {
            this.isDebug = z;
            return this;
        }

        public DXContainerGlobalInitConfig build() {
            return new DXContainerGlobalInitConfig(this);
        }
    }
}
