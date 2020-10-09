package com.taobao.android.dinamicx;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.notification.DXNotificationCenter;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.lang.ref.WeakReference;

public class DXRuntimeContext implements Cloneable {
    protected String bizType;
    protected String cacheIdentify;
    protected DXEngineConfig config;
    protected WeakReference<Context> contextWeakReference;
    private WeakReference<JSONObject> dataWRef;
    protected WeakReference<DXControlEventCenter> dxControlEventCenterWeakReference;
    protected DXError dxError;
    protected WeakReference<DXNotificationCenter> dxNotificationCenterWeakReference;
    protected WeakReference<DXRenderPipeline> dxRenderPipelineWeakReference;
    protected DXTemplateItem dxTemplateItem;
    @Deprecated
    protected Object dxUserContext;
    protected DXEngineContext engineContext;
    protected DXLongSparseArray<IDXEventHandler> eventHandlerMap;
    private int parentDirectionSpec = 0;
    protected DXLongSparseArray<IDXDataParser> parserMap;
    protected String pipelineIdentifier;
    int renderType;
    int rootHeightSpec;
    protected WeakReference<DXRootView> rootViewWeakReference;
    int rootWidthSpec;
    protected Object subData;
    protected int subdataIndex;
    protected DXUserContext userContext;
    protected DXWidgetNode widgetNode;
    protected DXLongSparseArray<IDXBuilderWidgetNode> widgetNodeMap;

    public DXRuntimeContext(@NonNull DXEngineContext dXEngineContext) {
        this.engineContext = dXEngineContext;
        this.config = dXEngineContext.config;
        this.bizType = this.config.bizType;
    }

    public void setDxError(DXError dXError) {
        this.dxError = dXError;
    }

    public String getPipelineIdentifier() {
        return this.pipelineIdentifier;
    }

    public void setPipelineIdentifier(String str) {
        this.pipelineIdentifier = str;
    }

    public DXRuntimeContext cloneWithWidgetNode(DXWidgetNode dXWidgetNode) {
        DXRuntimeContext dXRuntimeContext = new DXRuntimeContext(this.engineContext);
        dXRuntimeContext.dxUserContext = this.dxUserContext;
        dXRuntimeContext.dxTemplateItem = this.dxTemplateItem;
        dXRuntimeContext.widgetNode = dXWidgetNode;
        dXRuntimeContext.dataWRef = this.dataWRef;
        dXRuntimeContext.contextWeakReference = this.contextWeakReference;
        dXRuntimeContext.subData = this.subData;
        dXRuntimeContext.subdataIndex = this.subdataIndex;
        dXRuntimeContext.widgetNodeMap = this.widgetNodeMap;
        dXRuntimeContext.eventHandlerMap = this.eventHandlerMap;
        dXRuntimeContext.parserMap = this.parserMap;
        dXRuntimeContext.dxControlEventCenterWeakReference = this.dxControlEventCenterWeakReference;
        dXRuntimeContext.dxRenderPipelineWeakReference = this.dxRenderPipelineWeakReference;
        dXRuntimeContext.dxNotificationCenterWeakReference = this.dxNotificationCenterWeakReference;
        dXRuntimeContext.rootViewWeakReference = this.rootViewWeakReference;
        dXRuntimeContext.dxError = this.dxError;
        dXRuntimeContext.userContext = this.userContext;
        dXRuntimeContext.setParentDirectionSpec(this.parentDirectionSpec);
        dXRuntimeContext.renderType = this.renderType;
        dXRuntimeContext.pipelineIdentifier = this.pipelineIdentifier;
        dXRuntimeContext.rootWidthSpec = this.rootWidthSpec;
        dXRuntimeContext.rootHeightSpec = this.rootHeightSpec;
        return dXRuntimeContext;
    }

    public Object getDxUserContext() {
        return this.dxUserContext;
    }

    public DXUserContext getUserContext() {
        return this.userContext;
    }

    public DXTemplateItem getDxTemplateItem() {
        return this.dxTemplateItem;
    }

    public DXWidgetNode getWidgetNode() {
        if (this.widgetNode == null) {
            return null;
        }
        if (!this.widgetNode.isFlatten()) {
            return this.widgetNode;
        }
        return this.widgetNode.getReferenceNode();
    }

    private DXWidgetNode getFlatten() {
        if (this.widgetNode == null) {
            return null;
        }
        if (this.widgetNode.isFlatten()) {
            return this.widgetNode;
        }
        return this.widgetNode.getReferenceNode();
    }

    public JSONObject getData() {
        if (this.dataWRef != null) {
            return (JSONObject) this.dataWRef.get();
        }
        return null;
    }

    public Object getSubData() {
        return this.subData;
    }

    public void setData(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.dataWRef = new WeakReference<>(jSONObject);
        }
    }

    public void setSubdataIndex(int i) {
        this.subdataIndex = i;
    }

    public void setSubData(Object obj) {
        this.subData = obj;
    }

    public int getSubdataIndex() {
        return this.subdataIndex;
    }

    public DXLongSparseArray<IDXBuilderWidgetNode> getWidgetNodeMap() {
        return this.widgetNodeMap;
    }

    public DXLongSparseArray<IDXEventHandler> getEventHandlerMap() {
        return this.eventHandlerMap;
    }

    public DXLongSparseArray<IDXDataParser> getParserMap() {
        return this.parserMap;
    }

    public void setWidgetNode(DXWidgetNode dXWidgetNode) {
        this.widgetNode = dXWidgetNode;
    }

    public Context getContext() {
        if (this.contextWeakReference == null || this.contextWeakReference.get() == null) {
            return DinamicXEngine.getApplicationContext();
        }
        return (Context) this.contextWeakReference.get();
    }

    public DXError getDxError() {
        return this.dxError;
    }

    public boolean hasError() {
        return (this.dxError == null || this.dxError.dxErrorInfoList == null || this.dxError.dxErrorInfoList.size() <= 0) ? false : true;
    }

    public DXNotificationCenter getDxNotificationCenter() {
        if (this.dxNotificationCenterWeakReference == null) {
            return null;
        }
        return (DXNotificationCenter) this.dxNotificationCenterWeakReference.get();
    }

    public IDXEventHandler getEventHandlerWithId(long j) {
        if (this.eventHandlerMap == null) {
            return null;
        }
        return this.eventHandlerMap.get(j);
    }

    public DXControlEventCenter getDxControlEventCenter() {
        if (this.dxControlEventCenterWeakReference == null) {
            return null;
        }
        return (DXControlEventCenter) this.dxControlEventCenterWeakReference.get();
    }

    public DXRenderPipeline getDxRenderPipeline() {
        if (this.dxRenderPipelineWeakReference == null) {
            return null;
        }
        return (DXRenderPipeline) this.dxRenderPipelineWeakReference.get();
    }

    public DXRootView getRootView() {
        if (this.rootViewWeakReference == null) {
            return null;
        }
        return (DXRootView) this.rootViewWeakReference.get();
    }

    public String getCacheIdentify() {
        if (!(!TextUtils.isEmpty(this.cacheIdentify) || this.dxTemplateItem == null || getData() == null)) {
            this.cacheIdentify = this.dxTemplateItem.name + "_" + this.dxTemplateItem.version + "_" + System.identityHashCode(getData()) + "w:" + getRootWidthSpec() + "h:" + getRootHeightSpec();
        }
        return this.cacheIdentify;
    }

    public View getNativeView() {
        DXWidgetNode flatten = getFlatten();
        if (flatten == null || flatten.getWRView() == null) {
            return null;
        }
        return (View) flatten.getWRView().get();
    }

    public int getParentDirectionSpec() {
        return this.parentDirectionSpec;
    }

    public void setParentDirectionSpec(int i) {
        this.parentDirectionSpec = i;
    }

    public int getRenderType() {
        return this.renderType;
    }

    /* access modifiers changed from: protected */
    public void setDxRenderPipeline(WeakReference<DXRenderPipeline> weakReference) {
        this.dxRenderPipelineWeakReference = weakReference;
    }

    public int getRootWidthSpec() {
        if (this.rootWidthSpec == 0) {
            return DXScreenTool.getDefaultWidthSpec();
        }
        return this.rootWidthSpec;
    }

    public int getRootHeightSpec() {
        if (this.rootHeightSpec == 0) {
            return DXScreenTool.getDefaultHeightSpec();
        }
        return this.rootHeightSpec;
    }

    public String getBizType() {
        return this.bizType;
    }

    public DXEngineConfig getConfig() {
        return this.config;
    }

    public DXEngineContext getEngineContext() {
        return this.engineContext;
    }
}
