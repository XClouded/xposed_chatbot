package com.taobao.android.dxcontainer.life;

import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreController;

public interface EngineTabLoadMoreListener extends EngineLoadMoreListener {
    boolean isEnableLoadMoreWithTabIndex(int i);

    void onLoadMoreWithTabIndex(int i, IDXContainerLoadMoreController iDXContainerLoadMoreController);
}
