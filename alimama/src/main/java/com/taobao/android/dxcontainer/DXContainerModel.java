package com.taobao.android.dxcontainer;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXContainerModel implements Cloneable {
    private List<DXContainerModel> children;
    private JSONObject data;
    private String id;
    private boolean isDirty;
    private Map<String, LayoutHelper> layoutHelperMap;
    private String layoutType;
    private JSONObject originData;
    private DXContainerModel parent;
    private Object renderObject;
    private String renderType;
    private WeakReference<DXContainerSingleRVManager> singleCManagerWeakReference;
    private JSONObject styleModel;
    private String tag;
    private DXTemplateItem templateItem;
    private Object willRenderObject;

    public void setSingleCManager(DXContainerSingleRVManager dXContainerSingleRVManager) {
        this.singleCManagerWeakReference = new WeakReference<>(dXContainerSingleRVManager);
    }

    /* access modifiers changed from: package-private */
    public DXContainerSingleRVManager getSingleCManager() {
        if (this.singleCManagerWeakReference == null) {
            return null;
        }
        return (DXContainerSingleRVManager) this.singleCManagerWeakReference.get();
    }

    public LayoutHelper getLayoutHelper() {
        if (this.layoutHelperMap == null || TextUtils.isEmpty(this.layoutType)) {
            return null;
        }
        return this.layoutHelperMap.get(this.layoutType);
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        if (layoutHelper != null && !TextUtils.isEmpty(this.layoutType)) {
            if (this.layoutHelperMap == null) {
                this.layoutHelperMap = new HashMap();
            }
            this.layoutHelperMap.put(this.layoutType, layoutHelper);
        }
    }

    public Object getRenderObject() {
        return this.renderObject;
    }

    public void setRenderObject(Object obj) {
        this.renderObject = obj;
    }

    public Object getWillRenderObject() {
        return this.willRenderObject;
    }

    public void setWillRenderObject(Object obj) {
        this.willRenderObject = obj;
    }

    public void setLayoutType(String str) {
        this.layoutType = str;
    }

    public void setRenderType(String str) {
        this.renderType = str;
    }

    public void setStyleModel(JSONObject jSONObject) {
        this.styleModel = jSONObject;
    }

    public void setData(JSONObject jSONObject) {
        this.originData = jSONObject;
        this.data = new JSONObject();
        if (jSONObject != null) {
            this.data.putAll(jSONObject);
        }
    }

    public String getRenderType() {
        return this.renderType;
    }

    public String getLayoutType() {
        return this.layoutType;
    }

    public JSONObject getStyleModel() {
        return this.styleModel;
    }

    public DXTemplateItem getTemplateItem() {
        return this.templateItem;
    }

    public void setTemplateItem(DXTemplateItem dXTemplateItem) {
        this.templateItem = dXTemplateItem;
    }

    public void setParent(DXContainerModel dXContainerModel) {
        this.parent = dXContainerModel;
    }

    public void setChildren(List<DXContainerModel> list) {
        this.children = list;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public String getId() {
        return this.id;
    }

    public String getTag() {
        return this.tag;
    }

    public DXContainerModel getParent() {
        return this.parent;
    }

    public List<DXContainerModel> getChildren() {
        return this.children;
    }

    public void makeDirty() {
        if (this.data != null) {
            if (this.data.getBooleanValue("useOldStructure")) {
                JSONObject jSONObject = this.data.getJSONObject(ProtocolConst.KEY_FIELDS);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.putAll(jSONObject);
                this.data.put(ProtocolConst.KEY_FIELDS, (Object) jSONObject2);
            } else {
                this.data = new JSONObject();
                if (this.originData != null) {
                    this.data.putAll(this.originData);
                }
            }
            this.isDirty = true;
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void clearDirty() {
        this.isDirty = false;
    }

    public JSONObject getData() {
        return this.data;
    }

    public DXContainerModel getDXCModelByID(String str) {
        if (getModeManager() != null) {
            return getModeManager().getDXCModelByID(str);
        }
        return null;
    }

    public void removeFromParent() {
        DXContainerModel parent2 = getParent();
        if (parent2 == null) {
            DXContainerAppMonitor.trackerError(getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_REMOVE_PARENT_MODEL_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_REMOVE_PARENT_MODEL_NOT_EXIST);
        } else if (parent2 != null && parent2.getChildren() != null) {
            parent2.getChildren().remove(this);
            if (getModeManager() != null) {
                getModeManager().removeIdAndTag(this);
            }
        }
    }

    public void insertChildWithDXCModel(DXContainerModel dXContainerModel, int i) {
        if (dXContainerModel != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            dXContainerModel.setParent(this);
            try {
                this.children.add(i, dXContainerModel);
            } catch (Throwable unused) {
                trackIndexOut(i);
            }
            if (getModeManager() != null) {
                getModeManager().addIdAndTag(dXContainerModel);
            }
        }
    }

    public void addChild(DXContainerModel dXContainerModel) {
        if (dXContainerModel != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            dXContainerModel.setParent(this);
            this.children.add(dXContainerModel);
        }
    }

    public void removeChildAtIndex(int i) {
        if (this.children == null || this.children.size() <= i) {
            trackIndexOut(i);
        } else {
            this.children.remove(i);
        }
    }

    private void trackIndexOut(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("index", "index=" + i);
        if (this.children != null) {
            hashMap.put("childrenCount", "count=" + this.children.size());
        } else {
            hashMap.put("childrenCount", "children == null");
        }
        DXContainerAppMonitor.trackerError(getBizType(), this, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_CHILD_INDEX_OUT_OF_RANGE, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_CHILD_INDEX_OUT_OF_RANGE, hashMap);
    }

    private String getBizType() {
        DXContainerSingleRVManager singleCManager = getSingleCManager();
        if (singleCManager == null) {
            return "no singleCManager";
        }
        return singleCManager.getBizType();
    }

    public void refresh() {
        if (getSingleCManager() != null) {
            getSingleCManager().refreshAll();
        }
    }

    public DXContainerEngine getEngine() {
        if (getSingleCManager() == null) {
            return null;
        }
        return getSingleCManager().getContainerEngineContext().getEngine();
    }

    /* access modifiers changed from: package-private */
    public DXContainerModelManager getModeManager() {
        if (getSingleCManager() == null) {
            return null;
        }
        return getSingleCManager().getContainerEngineContext().getModelManager();
    }

    public JSONObject getFields() {
        if (this.data == null && getSingleCManager() == null) {
            return null;
        }
        return this.data.getJSONObject(ProtocolConst.KEY_FIELDS);
    }
}
