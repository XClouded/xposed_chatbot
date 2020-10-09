package com.taobao.android.dxcontainer.life;

import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreController;

public interface EngineMainLoadMoreListener extends EngineLoadMoreListener {
    boolean isEnableLoadMore();

    void onLoadMore(IDXContainerLoadMoreController iDXContainerLoadMoreController);
}
