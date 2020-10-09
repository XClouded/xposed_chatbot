package com.taobao.android.dxcontainer.loadmore;

import android.util.SparseArray;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.utils.DXContainerLoadMoreModelUtils;

public class DXContainerLoadMoreState implements IDXContainerLoadMoreController {
    private int currentState = 0;
    private boolean hasInit = false;
    private IDXContainerLoadMoreView listener;
    private DXContainerModel model;
    private SparseArray<String> status;

    public DXContainerLoadMoreState(SparseArray<String> sparseArray) {
        this.status = sparseArray;
    }

    public void setDXCLoadMoreListener(IDXContainerLoadMoreView iDXContainerLoadMoreView, DXContainerModel dXContainerModel) {
        this.listener = iDXContainerLoadMoreView;
        this.model = dXContainerModel;
        this.hasInit = true;
        DXContainerLoadMoreModelUtils.updateState(dXContainerModel, this.status, this.currentState);
    }

    public void reset() {
        this.currentState = 0;
        this.listener = null;
        this.model = null;
        this.hasInit = false;
    }

    public boolean isInit() {
        return this.hasInit;
    }

    public void setState(int i) {
        this.currentState = i;
        if (this.listener != null) {
            DXContainerLoadMoreModelUtils.updateState(this.model, this.status, this.currentState);
            this.listener.setViewState(this.status.get(this.currentState), i);
        }
    }

    public int getState() {
        return this.currentState;
    }
}
