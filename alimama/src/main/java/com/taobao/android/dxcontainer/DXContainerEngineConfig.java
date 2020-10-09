package com.taobao.android.dxcontainer;

import androidx.annotation.NonNull;
import com.taobao.android.dxcontainer.loadmore.AbsDXContainerLoadMoreViewBuilder;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreStateText;

public class DXContainerEngineConfig {
    private String bizType;
    private int defaultSelectedTab;
    private boolean enableDXCRootView;
    private AbsDXContainerLoadMoreViewBuilder loadMoreViewBuilder;
    private IDXContainerRecyclerViewInterface recyclerViewInterface;
    private IDXContainerLoadMoreStateText stateText;
    private String subBizType;

    public String getBizType() {
        return this.bizType;
    }

    public String getSubBizType() {
        return this.subBizType;
    }

    public int getDefaultSelectedTab() {
        return this.defaultSelectedTab;
    }

    public boolean isEnableDXCRootView() {
        return this.enableDXCRootView;
    }

    public IDXContainerLoadMoreStateText getStateText() {
        return this.stateText;
    }

    public AbsDXContainerLoadMoreViewBuilder getLoadMoreViewBuilder() {
        return this.loadMoreViewBuilder;
    }

    public DXContainerEngineConfig(@NonNull String str) {
        this(str, new Builder(str));
    }

    public IDXContainerRecyclerViewInterface getRecyclerViewInterface() {
        return this.recyclerViewInterface;
    }

    private DXContainerEngineConfig(@NonNull String str, Builder builder) {
        this.loadMoreViewBuilder = null;
        this.bizType = str;
        this.subBizType = builder.subBizType;
        this.defaultSelectedTab = builder.defaultSelectedTab;
        this.enableDXCRootView = builder.enableDXCRootView;
        this.loadMoreViewBuilder = builder.loadMoreViewBuilder;
        this.recyclerViewInterface = builder.recyclerViewInterface;
        this.stateText = builder.stateText;
    }

    public static final class Builder {
        private String bizType;
        /* access modifiers changed from: private */
        public int defaultSelectedTab;
        /* access modifiers changed from: private */
        public boolean enableDXCRootView;
        /* access modifiers changed from: private */
        public AbsDXContainerLoadMoreViewBuilder loadMoreViewBuilder = null;
        /* access modifiers changed from: private */
        public IDXContainerRecyclerViewInterface recyclerViewInterface;
        /* access modifiers changed from: private */
        public IDXContainerLoadMoreStateText stateText;
        /* access modifiers changed from: private */
        public String subBizType;

        public Builder(String str) {
            this.bizType = str;
            this.subBizType = str;
        }

        public Builder withSubBizType(String str) {
            this.subBizType = str;
            return this;
        }

        public Builder withDefaultSelectedTab(int i) {
            this.defaultSelectedTab = i;
            return this;
        }

        public Builder withRecyclerViewBuilder(IDXContainerRecyclerViewInterface iDXContainerRecyclerViewInterface) {
            this.recyclerViewInterface = iDXContainerRecyclerViewInterface;
            return this;
        }

        public Builder withEnableDXCRootView(boolean z) {
            this.enableDXCRootView = z;
            return this;
        }

        public Builder withIDXCLoadMoreStateText(IDXContainerLoadMoreStateText iDXContainerLoadMoreStateText) {
            this.stateText = iDXContainerLoadMoreStateText;
            return this;
        }

        public Builder withDXCLoadMoreViewBuilder(AbsDXContainerLoadMoreViewBuilder absDXContainerLoadMoreViewBuilder) {
            this.loadMoreViewBuilder = absDXContainerLoadMoreViewBuilder;
            return this;
        }

        public DXContainerEngineConfig build() {
            return new DXContainerEngineConfig(this.bizType, this);
        }
    }
}
