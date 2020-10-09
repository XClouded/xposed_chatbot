package com.taobao.android.dxcontainer.loadmore;

public class DXContainerDefaultLoadMoreStateText implements IDXContainerLoadMoreStateText {
    public String getFailedText() {
        return "加载失败，请点击重试";
    }

    public String getLoadingText() {
        return "正在加载更多";
    }

    public String getNoMoreText() {
        return "亲，到底啦";
    }

    public String getNormalText() {
        return "加载更多";
    }
}
