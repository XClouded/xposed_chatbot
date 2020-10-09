package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerModel;

public abstract class IDXContainerRender {
    protected String TAG = "DXCRender";
    protected DXContainerEngine engine;

    public abstract View createView(ViewGroup viewGroup, String str, Object obj);

    public abstract String getViewTypeId(DXContainerModel dXContainerModel);

    public void onViewRecycled(View view, DXContainerModel dXContainerModel, String str, String str2, Object obj) {
    }

    public abstract DXContainerRenderResult renderView(DXContainerModel dXContainerModel, View view, int i);

    public IDXContainerRender(DXContainerEngine dXContainerEngine) {
        this.engine = dXContainerEngine;
    }

    public Object getRenderObject(DXContainerModel dXContainerModel) {
        return getViewTypeId(dXContainerModel);
    }

    public String getViewTypeIdByRenderObject(Object obj) {
        return (String) obj;
    }
}
