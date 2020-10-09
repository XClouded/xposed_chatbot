package com.taobao.android.dxcontainer;

import android.annotation.SuppressLint;
import android.content.Context;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXEngineConfig;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.IDXEventHandler;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dxcontainer.event.DXContainerEventCallback;
import com.taobao.android.dxcontainer.event.DXContainerEventHandler;
import com.taobao.android.dxcontainer.event.DXContainerFLongTapEventHandler;
import com.taobao.android.dxcontainer.event.DXContainerFTapEventHandler;
import com.taobao.android.dxcontainer.event.DXContainerFbindEventHandler;
import com.taobao.android.dxcontainer.layout.DXContainerLayoutManager;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.render.DXContainerRenderManager;
import com.taobao.android.dxcontainer.render.DinamicXRender;
import com.taobao.android.dxcontainer.render.IDXContainerComponentRender;
import com.taobao.android.dxcontainer.render.IDXContainerRender;
import com.taobao.android.dxcontainer.render.LoadMoreRender;
import com.taobao.android.dxcontainer.render.NativeXRender;
import com.taobao.android.dxcontainer.render.TabContentRender;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DXContainerEngineContext {
    private Context context;
    private HashMap<String, DinamicXEngine> dinamicXEngines = new HashMap<>();
    private DXContainerLayoutManager dxcLayoutManager;
    DXContainerEngineConfig engineConfig;
    private WeakReference<DXContainerEngine> engineWeakReference;
    @SuppressLint({"UseSparseArrays"})
    Map<Long, DXContainerEventHandler> eventHandlerMap = new HashMap();
    private DXContainerModelManager modelManager;
    private HashMap<String, DXContainerRenderManager> renderManagers = new HashMap<>();

    public void setEngine(DXContainerEngine dXContainerEngine) {
        this.engineWeakReference = new WeakReference<>(dXContainerEngine);
    }

    public DXContainerEngine getEngine() {
        if (this.engineWeakReference == null) {
            return null;
        }
        return (DXContainerEngine) this.engineWeakReference.get();
    }

    DXContainerEngineContext(Context context2, DXContainerEngineConfig dXContainerEngineConfig) {
        this.engineConfig = dXContainerEngineConfig;
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        String bizType = this.engineConfig.getBizType();
        DXContainerFTapEventHandler dXContainerFTapEventHandler = new DXContainerFTapEventHandler();
        dXContainerFTapEventHandler.setContainerEngine(getEngine());
        DXContainerFLongTapEventHandler dXContainerFLongTapEventHandler = new DXContainerFLongTapEventHandler();
        dXContainerFLongTapEventHandler.setContainerEngine(getEngine());
        DXContainerFbindEventHandler dXContainerFbindEventHandler = new DXContainerFbindEventHandler();
        dXContainerFbindEventHandler.setContainerEngine(getEngine());
        this.eventHandlerMap.put(Long.valueOf(DXContainerFTapEventHandler.DX_EVENT_FTAP), dXContainerFTapEventHandler);
        this.eventHandlerMap.put(Long.valueOf(DXContainerFLongTapEventHandler.DX_EVENT_FLONGTAP), dXContainerFLongTapEventHandler);
        this.eventHandlerMap.put(Long.valueOf(DXContainerFbindEventHandler.DX_EVENT_FBIND), dXContainerFbindEventHandler);
        appendBizType(bizType);
        appendSubBizType(this.engineConfig.getSubBizType());
        this.modelManager = new DXContainerModelManager(bizType);
        this.dxcLayoutManager = new DXContainerLayoutManager();
    }

    private void appendSubBizType(String str) {
        if (!this.engineConfig.getBizType().equals(str)) {
            appendBizType(str);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean bizTypeSame() {
        return this.engineConfig.getBizType().equals(this.engineConfig.getSubBizType());
    }

    private void appendBizType(String str) {
        DinamicXEngine initDinamicX = initDinamicX(str);
        DXContainerRenderManager dXContainerRenderManager = new DXContainerRenderManager();
        dXContainerRenderManager.register("dinamicx", new DinamicXRender(getEngine(), initDinamicX, this.engineConfig.isEnableDXCRootView()));
        dXContainerRenderManager.register(LoadMoreRender.RENDER_TYPE, new LoadMoreRender(getEngine(), this.engineConfig.getLoadMoreViewBuilder()));
        dXContainerRenderManager.register(TabContentRender.RENDER_TYPE, new TabContentRender(getEngine()));
        dXContainerRenderManager.register(NativeXRender.DEFAULT_RENDER_TYPE, new NativeXRender(getEngine(), dXContainerRenderManager.getComponentRenderManager()));
        this.dinamicXEngines.put(str, initDinamicX);
        this.renderManagers.put(str, dXContainerRenderManager);
    }

    /* access modifiers changed from: package-private */
    public DXContainerRenderManager getRenderManager(String str) {
        return this.renderManagers.get(str);
    }

    /* access modifiers changed from: package-private */
    public DinamicXEngine getDinamicXEngine(String str) {
        return this.dinamicXEngines.get(str);
    }

    /* access modifiers changed from: package-private */
    public DXContainerLayoutManager getDxcLayoutManager() {
        return this.dxcLayoutManager;
    }

    /* access modifiers changed from: package-private */
    public DXContainerModelManager getModelManager() {
        return this.modelManager;
    }

    /* access modifiers changed from: package-private */
    public Context getContext() {
        return this.context;
    }

    private DinamicXEngine initDinamicX(String str) {
        DinamicXEngine dinamicXEngine = new DinamicXEngine(new DXEngineConfig(str));
        for (Map.Entry next : this.eventHandlerMap.entrySet()) {
            if (next != null) {
                dinamicXEngine.registerEventHandler(((Long) next.getKey()).longValue(), (IDXEventHandler) next.getValue());
            }
        }
        return dinamicXEngine;
    }

    /* access modifiers changed from: package-private */
    public boolean registerEventHandler(long j, DXAbsEventHandler dXAbsEventHandler) {
        for (Map.Entry<String, DinamicXEngine> value : this.dinamicXEngines.entrySet()) {
            if (!((DinamicXEngine) value.getValue()).registerEventHandler(j, dXAbsEventHandler)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void registerDefaultEventHandler(long j, DXContainerEventCallback dXContainerEventCallback) {
        DXContainerEventHandler dXContainerEventHandler = this.eventHandlerMap.get(Long.valueOf(j));
        if (dXContainerEventHandler != null) {
            dXContainerEventHandler.setEventCallback(dXContainerEventCallback);
        }
    }

    public boolean registerDXWidget(long j, IDXBuilderWidgetNode iDXBuilderWidgetNode) {
        for (Map.Entry<String, DinamicXEngine> value : this.dinamicXEngines.entrySet()) {
            if (!((DinamicXEngine) value.getValue()).registerWidget(j, iDXBuilderWidgetNode)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void registerRender(String str, IDXContainerRender iDXContainerRender) {
        for (Map.Entry<String, DXContainerRenderManager> value : this.renderManagers.entrySet()) {
            ((DXContainerRenderManager) value.getValue()).register(str, iDXContainerRender);
        }
    }

    /* access modifiers changed from: package-private */
    public void registerNativeComponent(String str, IDXContainerComponentRender iDXContainerComponentRender) {
        for (Map.Entry<String, DXContainerRenderManager> value : this.renderManagers.entrySet()) {
            ((DXContainerRenderManager) value.getValue()).getComponentRenderManager().register(str, iDXContainerComponentRender);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean registerDXDataParser(long j, IDXDataParser iDXDataParser) {
        for (Map.Entry<String, DinamicXEngine> value : this.dinamicXEngines.entrySet()) {
            if (!((DinamicXEngine) value.getValue()).registerDataParser(j, iDXDataParser)) {
                return false;
            }
        }
        return true;
    }

    public void registerLayout(String str, IDXContainerLayout iDXContainerLayout) {
        this.dxcLayoutManager.registerIDXCLayout(iDXContainerLayout);
    }

    /* access modifiers changed from: package-private */
    public IDXContainerRecyclerViewInterface getRecyclerViewInterface() {
        IDXContainerRecyclerViewInterface recyclerViewInterface = this.engineConfig.getRecyclerViewInterface();
        return recyclerViewInterface == null ? DXContainerGlobalCenter.getRecyclerViewInterface() : recyclerViewInterface;
    }
}
