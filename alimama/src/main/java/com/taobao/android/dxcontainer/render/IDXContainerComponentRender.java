package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerAppMonitor;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.taobao.android.dxcontainer.DXContainerModel;

public abstract class IDXContainerComponentRender {
    protected String TAG = "IDXContainerComponentRender";
    protected DXContainerEngine engine;

    public abstract View createView(ViewGroup viewGroup, String str, Object obj);

    public abstract String getViewTypeId(DXContainerModel dXContainerModel);

    public void onViewRecycled(View view, DXContainerModel dXContainerModel, String str, String str2, Object obj) {
    }

    public abstract DXContainerRenderResult renderView(DXContainerModel dXContainerModel, View view, int i);

    public IDXContainerComponentRender(DXContainerEngine dXContainerEngine) {
        this.engine = dXContainerEngine;
    }

    public Object getRenderObject(DXContainerModel dXContainerModel) {
        DXTemplateItem templateItem = dXContainerModel.getTemplateItem();
        if (templateItem != null) {
            return templateItem;
        }
        String bizType = this.engine.getContainerEngineConfig().getBizType();
        DXContainerAppMonitor.trackerError(bizType, dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3010, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_RENDER_OBJECT_TEMPLATE_NULL + dXContainerModel.getId());
        return "unknow";
    }

    public String getViewTypeIdByRenderObject(Object obj) {
        if (obj instanceof DXTemplateItem) {
            return ((DXTemplateItem) obj).name;
        }
        if (obj == null) {
            return null;
        }
        String bizType = this.engine.getContainerEngineConfig().getBizType();
        DXContainerAppMonitor.trackerError(bizType, (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3011, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_VIEW_TYPE_ID_BY_RENDER_OBJECT_RO_NO_TEMPLATE + obj.toString());
        return "unknow";
    }
}
