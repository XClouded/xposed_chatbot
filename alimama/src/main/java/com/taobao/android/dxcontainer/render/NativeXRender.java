package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerAppMonitor;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.taobao.android.dxcontainer.DXContainerModel;

public class NativeXRender extends IDXContainerRender {
    public static final String DEFAULT_RENDER_TYPE = "nativex";
    private DXContainerNativeComponentRenderManager componentRenderManager;

    public NativeXRender(DXContainerEngine dXContainerEngine, DXContainerNativeComponentRenderManager dXContainerNativeComponentRenderManager) {
        super(dXContainerEngine);
        this.componentRenderManager = dXContainerNativeComponentRenderManager;
    }

    public View createView(ViewGroup viewGroup, String str, Object obj) {
        IDXContainerComponentRender render;
        if (!(obj instanceof DXTemplateItem) || (render = this.componentRenderManager.getRender(((DXTemplateItem) obj).name)) == null) {
            return new Space(viewGroup.getContext());
        }
        return render.createView(viewGroup, str, obj);
    }

    public DXContainerRenderResult renderView(DXContainerModel dXContainerModel, View view, int i) {
        IDXContainerComponentRender render;
        DXTemplateItem templateItem = dXContainerModel.getTemplateItem();
        if (templateItem == null || (render = this.componentRenderManager.getRender(templateItem.name)) == null) {
            return null;
        }
        render.renderView(dXContainerModel, view, i);
        return null;
    }

    public String getViewTypeId(DXContainerModel dXContainerModel) {
        DXTemplateItem templateItem = dXContainerModel.getTemplateItem();
        if (templateItem == null) {
            DXContainerAppMonitor.trackerError(this.engine.getContainerEngineConfig().getBizType(), dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3012, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_VIEW_TYPE_ID_TEMPLATE_NULL);
            return null;
        }
        IDXContainerComponentRender render = this.componentRenderManager.getRender(templateItem.name);
        if (render != null) {
            return render.getViewTypeId(dXContainerModel);
        }
        DXContainerAppMonitor.trackerError(this.engine.getContainerEngineConfig().getBizType(), dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3013, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_VIEW_TYPE_ID_COMPONENT_NULL);
        return "unknow";
    }

    public void onViewRecycled(View view, DXContainerModel dXContainerModel, String str, String str2, Object obj) {
        DXTemplateItem templateItem;
        IDXContainerComponentRender render;
        if (dXContainerModel != null && (templateItem = dXContainerModel.getTemplateItem()) != null && (render = this.componentRenderManager.getRender(templateItem.name)) != null) {
            render.onViewRecycled(view, dXContainerModel, str, str2, obj);
        }
    }

    public Object getRenderObject(DXContainerModel dXContainerModel) {
        IDXContainerComponentRender render;
        DXTemplateItem templateItem = dXContainerModel.getTemplateItem();
        if (templateItem != null && (render = this.componentRenderManager.getRender(templateItem.name)) != null) {
            return render.getRenderObject(dXContainerModel);
        }
        String bizType = this.engine.getContainerEngineConfig().getBizType();
        DXContainerAppMonitor.trackerError(bizType, dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3010, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_RENDER_OBJECT_TEMPLATE_NULL + dXContainerModel.getId());
        return "unknow";
    }

    public String getViewTypeIdByRenderObject(Object obj) {
        IDXContainerComponentRender render;
        if ((obj instanceof DXTemplateItem) && (render = this.componentRenderManager.getRender(((DXTemplateItem) obj).name)) != null) {
            return render.getViewTypeIdByRenderObject(obj);
        }
        if (obj == null) {
            return null;
        }
        String bizType = this.engine.getContainerEngineConfig().getBizType();
        DXContainerAppMonitor.trackerError(bizType, (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3011, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_NX_RENDER_GET_VIEW_TYPE_ID_BY_RENDER_OBJECT_RO_NO_TEMPLATE + obj.toString());
        return "unknow";
    }
}
