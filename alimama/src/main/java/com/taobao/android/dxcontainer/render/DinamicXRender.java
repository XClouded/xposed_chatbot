package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRenderOptions;
import com.taobao.android.dinamicx.DXResult;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dxcontainer.DXContainerAppMonitor;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.taobao.android.dxcontainer.DXContainerGlobalCenter;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.DXContainerRootView;
import com.taobao.android.dxcontainer.DXContainerUserContext;
import com.taobao.android.dxcontainer.utils.DXContainerExceptionUtil;
import java.lang.ref.WeakReference;

public class DinamicXRender extends IDXContainerRender {
    public static final String DEFAULT_RENDER_TYPE = "dinamicx";
    private DinamicXEngine dxEngine;
    private boolean enableDXCRootView;

    public DinamicXRender(DXContainerEngine dXContainerEngine, DinamicXEngine dinamicXEngine, boolean z) {
        super(dXContainerEngine);
        this.dxEngine = dinamicXEngine;
        this.enableDXCRootView = z;
    }

    public View createView(ViewGroup viewGroup, String str, Object obj) {
        View view = (View) this.dxEngine.createView(viewGroup.getContext(), (DXTemplateItem) obj).result;
        if (this.enableDXCRootView) {
            if (view != null) {
                DXContainerRootView dXContainerRootView = new DXContainerRootView(viewGroup.getContext());
                dXContainerRootView.addView(view, -2, -2);
                view = dXContainerRootView;
            } else {
                view = null;
            }
        }
        return view == null ? new Space(viewGroup.getContext()) : view;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        if ((r4 instanceof com.taobao.android.dinamicx.DXRootView) != false) goto L_0x0019;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x001d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.dxcontainer.render.DXContainerRenderResult renderView(com.taobao.android.dxcontainer.DXContainerModel r3, android.view.View r4, int r5) {
        /*
            r2 = this;
            boolean r0 = r4 instanceof com.taobao.android.dxcontainer.DXContainerRootView
            r1 = 0
            if (r0 == 0) goto L_0x0013
            com.taobao.android.dxcontainer.DXContainerRootView r4 = (com.taobao.android.dxcontainer.DXContainerRootView) r4
            int r0 = r4.getChildCount()
            if (r0 <= 0) goto L_0x0018
            r0 = 0
            android.view.View r4 = r4.getChildAt(r0)
            goto L_0x0019
        L_0x0013:
            boolean r0 = r4 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x0018
            goto L_0x0019
        L_0x0018:
            r4 = r1
        L_0x0019:
            boolean r0 = r4 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x0022
            com.taobao.android.dinamicx.DXRootView r4 = (com.taobao.android.dinamicx.DXRootView) r4
            r2.renderDXRootView(r3, r4, r5)
        L_0x0022:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.render.DinamicXRender.renderView(com.taobao.android.dxcontainer.DXContainerModel, android.view.View, int):com.taobao.android.dxcontainer.render.DXContainerRenderResult");
    }

    private void renderDXRootView(DXContainerModel dXContainerModel, DXRootView dXRootView, int i) {
        DXContainerUserContext dXContainerUserContext = new DXContainerUserContext();
        dXContainerUserContext.dxcModelWeakReference = new WeakReference<>(dXContainerModel);
        dXContainerUserContext.engineWeakReference = new WeakReference<>(this.engine);
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(this.engine.getContentView().getMeasuredWidth(), 1073741824);
        int defaultHeightSpec = DXScreenTool.getDefaultHeightSpec();
        JSONObject data = dXContainerModel.getData();
        if (data != null ? data.getBooleanValue("useOldStructure") : false) {
            data = dXContainerModel.getFields();
        }
        DXResult<DXRootView> renderTemplate = this.dxEngine.renderTemplate(dXRootView.getContext(), dXRootView, dXRootView.getDxTemplateItem(), data, i, new DXRenderOptions.Builder().withWidthSpec(makeMeasureSpec).withHeightSpec(defaultHeightSpec).withUserContext(dXContainerUserContext).build());
        if (renderTemplate.hasError()) {
            DXError dxError = renderTemplate.getDxError();
            if (DXContainerGlobalCenter.isDebug()) {
                DXContainerAppMonitor.logi(dxError.toString());
            }
            DXContainerAppMonitor.trackerError(this.dxEngine.getBizType(), dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3005, dxError.toString());
        }
        dXContainerModel.setRenderObject(dXRootView.getDxTemplateItem());
        try {
            this.dxEngine.onRootViewAppear(dXRootView);
        } catch (Throwable th) {
            DXContainerError dXContainerError = new DXContainerError(this.dxEngine.getBizType());
            dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_DX_RENDER_ON_ROOT_VIEW_APPEAR, 3006, DXContainerExceptionUtil.getStackTrace(th)));
            DXContainerAppMonitor.trackerError(dXContainerError, dXContainerModel);
        }
    }

    public void onViewRecycled(View view, DXContainerModel dXContainerModel, String str, String str2, Object obj) {
        super.onViewRecycled(view, dXContainerModel, str, str2, obj);
        try {
            if (view instanceof DXRootView) {
                this.dxEngine.onRootViewDisappear((DXRootView) view);
            }
        } catch (Throwable th) {
            DXContainerError dXContainerError = new DXContainerError(this.dxEngine.getBizType());
            dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_DX_RENDER_ON_VIEW_RECYCLED, 3007, DXContainerExceptionUtil.getStackTrace(th)));
            DXContainerAppMonitor.trackerError(dXContainerError, dXContainerModel);
        }
    }

    public String getViewTypeId(DXContainerModel dXContainerModel) {
        if (dXContainerModel != null && dXContainerModel.getTemplateItem() != null) {
            return dXContainerModel.getTemplateItem().getIdentifier();
        }
        DXContainerAppMonitor.trackerError(this.dxEngine.getBizType(), dXContainerModel, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3008, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_DX_RENDER_GET_VIEW_TYPE_ID_MODEL_TEMPLATE_NULL);
        return null;
    }

    public Object getRenderObject(DXContainerModel dXContainerModel) {
        return this.dxEngine.fetchTemplate(dXContainerModel.getTemplateItem());
    }

    public String getViewTypeIdByRenderObject(Object obj) {
        if (obj instanceof DXTemplateItem) {
            return ((DXTemplateItem) obj).getIdentifier();
        }
        if (obj == null) {
            return null;
        }
        String bizType = this.dxEngine.getBizType();
        DXContainerAppMonitor.trackerError(bizType, (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3009, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_DX_RENDER_GET_VIEW_TYPE_ID_BYRENDER_OBJECT_RO_NO_TEMPLATE + obj.toString());
        return null;
    }
}
