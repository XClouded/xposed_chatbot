package com.taobao.android.dxcontainer.render;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutManager;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import java.util.HashMap;
import java.util.Map;

public class DXContainerViewTypeGenerator {
    public static final int DX_NO_TEMPLATE_VIEW_TYPE = -1;
    public static final int DX_VIEW_TYPE_MODEL_ERROR = -2;
    public static final int DX_VIEW_TYPE_MODEL_NO_RENDER_ERROR = -4;
    public static final int DX_VIEW_TYPE_MODEL_RENDER_TYPE_ERROR = -3;
    private DXContainerLayoutManager layoutManager;
    private Map<String, Integer> modelId2Position = new HashMap();
    private SparseIntArray position2viewType = new SparseIntArray();
    private DXContainerRenderManager renderManager;
    private HashMap<String, Integer> viewId2Type = new HashMap<>(128);
    private SparseArray<Object> viewType2RenderObject = new SparseArray<>();
    private SparseArray<String> viewType2RenderType = new SparseArray<>();
    private SparseArray<String> viewType2ViewTypeId = new SparseArray<>();
    private int viewTypeCounter = 0;

    public DXContainerViewTypeGenerator(DXContainerRenderManager dXContainerRenderManager, DXContainerLayoutManager dXContainerLayoutManager) {
        this.renderManager = dXContainerRenderManager;
        this.layoutManager = dXContainerLayoutManager;
    }

    public void modelToViewType(int i, DXContainerModel dXContainerModel) {
        if (dXContainerModel == null) {
            this.position2viewType.put(i, -2);
            return;
        }
        this.modelId2Position.put(dXContainerModel.getId(), Integer.valueOf(i));
        String renderType = dXContainerModel.getRenderType();
        String layoutType = dXContainerModel.getLayoutType();
        if (!TextUtils.isEmpty(layoutType)) {
            IDXContainerLayout iDXCLayout = this.layoutManager.getIDXCLayout(layoutType);
            if (iDXCLayout == null || !iDXCLayout.isRealView() || TextUtils.isEmpty(iDXCLayout.getRenderType())) {
                this.position2viewType.put(i, -3);
                return;
            }
            renderType = iDXCLayout.getRenderType();
        }
        IDXContainerRender render = this.renderManager.getRender(renderType);
        if (render == null) {
            this.position2viewType.put(i, -4);
            return;
        }
        String viewTypeId = render.getViewTypeId(dXContainerModel);
        if (this.viewId2Type.containsKey(viewTypeId)) {
            this.position2viewType.put(i, this.viewId2Type.get(viewTypeId).intValue());
            return;
        }
        Object renderObject = render.getRenderObject(dXContainerModel);
        dXContainerModel.setWillRenderObject(renderObject);
        if (renderObject == null) {
            this.position2viewType.put(i, -1);
            return;
        }
        String viewTypeIdByRenderObject = render.getViewTypeIdByRenderObject(renderObject);
        if (this.viewId2Type.containsKey(viewTypeIdByRenderObject)) {
            this.position2viewType.put(i, this.viewId2Type.get(viewTypeIdByRenderObject).intValue());
            return;
        }
        this.viewTypeCounter++;
        this.viewId2Type.put(viewTypeIdByRenderObject, Integer.valueOf(this.viewTypeCounter));
        this.viewType2RenderType.put(this.viewTypeCounter, renderType);
        this.viewType2ViewTypeId.put(this.viewTypeCounter, viewTypeIdByRenderObject);
        this.viewType2RenderObject.put(this.viewTypeCounter, renderObject);
        this.position2viewType.put(i, this.viewTypeCounter);
    }

    public void removeViewType(String str) {
        this.viewId2Type.remove(str);
    }

    public void reset() {
        this.position2viewType.clear();
        this.modelId2Position.clear();
    }

    public Object getRenderObject(int i) {
        return this.viewType2RenderObject.get(i);
    }

    public int getViewType(int i) {
        return this.position2viewType.get(i);
    }

    public String getRenderType(int i) {
        return this.viewType2RenderType.get(i);
    }

    public String getRenderTypeByPosition(int i) {
        return getRenderType(getViewType(i));
    }

    public String getViewTypeId(int i) {
        return this.viewType2ViewTypeId.get(i);
    }

    public int getPositionByModelId(String str) {
        if (this.modelId2Position.containsKey(str)) {
            return this.modelId2Position.get(str).intValue();
        }
        return -1;
    }
}
