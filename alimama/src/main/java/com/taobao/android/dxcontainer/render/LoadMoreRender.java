package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.loadmore.AbsDXContainerLoadMoreViewBuilder;
import com.taobao.android.dxcontainer.loadmore.DXContainerDefaultLoadMoreView;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreView;
import com.taobao.android.dxcontainer.utils.DXContainerLoadMoreModelUtils;

public class LoadMoreRender extends IDXContainerRender {
    public static final String RENDER_TYPE = "LoadMoreRender";
    private AbsDXContainerLoadMoreViewBuilder builder;

    public String getViewTypeId(DXContainerModel dXContainerModel) {
        return "container_default_load_more";
    }

    public LoadMoreRender(DXContainerEngine dXContainerEngine, AbsDXContainerLoadMoreViewBuilder absDXContainerLoadMoreViewBuilder) {
        super(dXContainerEngine);
        this.builder = absDXContainerLoadMoreViewBuilder;
    }

    public View createView(ViewGroup viewGroup, String str, Object obj) {
        View view;
        IDXContainerLoadMoreView createLoadMoreView = this.builder != null ? this.builder.createLoadMoreView(viewGroup.getContext()) : null;
        if (createLoadMoreView instanceof View) {
            view = (View) createLoadMoreView;
        } else {
            view = new DXContainerDefaultLoadMoreView(viewGroup.getContext());
        }
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, 100));
        return view;
    }

    public DXContainerRenderResult renderView(DXContainerModel dXContainerModel, View view, int i) {
        if (!(view instanceof IDXContainerLoadMoreView)) {
            return null;
        }
        ((IDXContainerLoadMoreView) view).setViewState(DXContainerLoadMoreModelUtils.getText(dXContainerModel), DXContainerLoadMoreModelUtils.getState(dXContainerModel));
        return null;
    }
}
