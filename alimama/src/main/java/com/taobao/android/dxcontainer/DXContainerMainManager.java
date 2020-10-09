package com.taobao.android.dxcontainer;

import android.util.SparseArray;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.notification.DXNotificationResult;
import com.taobao.android.dinamicx.notification.DXTemplateUpdateRequest;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.dxcontainer.life.EngineLoadMoreListener;

class DXContainerMainManager extends DXContainerBaseClass {
    /* access modifiers changed from: private */
    public String bizType = getContainerEngineConfig().getBizType();
    private DinamicXEngine dxEngine;
    /* access modifiers changed from: private */
    public DXContainerSingleRVManager singleCManager;

    DXContainerMainManager(DXContainerEngineContext dXContainerEngineContext) {
        super(dXContainerEngineContext);
        this.dxEngine = dXContainerEngineContext.getDinamicXEngine(this.bizType);
        registerDefaultNotification();
        this.singleCManager = new DXContainerSingleRVManager(dXContainerEngineContext);
        IDXContainerRecyclerViewInterface recyclerViewInterface = dXContainerEngineContext.getRecyclerViewInterface();
        DXContainerRecyclerViewOption dXContainerRecyclerViewOption = new DXContainerRecyclerViewOption();
        dXContainerRecyclerViewOption.setSub(false);
        this.singleCManager.init(dXContainerEngineContext.getContext(), recyclerViewInterface.newRecyclerView(dXContainerEngineContext.getContext(), dXContainerRecyclerViewOption), getContainerEngineConfig().getBizType(), this.dxEngine, (Integer) null);
    }

    private void registerDefaultNotification() {
        if (this.dxEngine != null) {
            this.dxEngine.registerNotificationListener(new IDXNotificationListener() {
                public void onNotificationListener(DXNotificationResult dXNotificationResult) {
                    boolean z = false;
                    boolean z2 = dXNotificationResult.templateUpdateRequestList.size() > 0;
                    boolean z3 = dXNotificationResult.finishedTemplateItems.size() > 0;
                    if (dXNotificationResult.failedTemplateItems.size() > 0) {
                        z = true;
                    }
                    if (z2 || z3) {
                        if (z2) {
                            DXContainerError dXContainerError = new DXContainerError(DXContainerMainManager.this.bizType);
                            for (DXTemplateUpdateRequest next : dXNotificationResult.templateUpdateRequestList) {
                                if (next.reason == 1000) {
                                    dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, DXContainerErrorConstant.DX_CONTAINER_ERROR_DX_TEMPLATE_DOWNGRADE, "Template downgrade template info=" + next.item.toString()));
                                    DXContainerMainManager.this.singleCManager.getViewTypeGenerator().removeViewType(next.item.getIdentifier());
                                }
                            }
                            DXContainerAppMonitor.trackerError(dXContainerError);
                        }
                        DXContainerMainManager.this.refreshByNotification();
                    }
                    if (z) {
                        DXContainerError dXContainerError2 = new DXContainerError(DXContainerMainManager.this.bizType);
                        for (DXTemplateItem dXTemplateItem : dXNotificationResult.failedTemplateItems) {
                            dXContainerError2.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, DXContainerErrorConstant.DX_CONTAINER_ERROR_DX_TEMPLATE_DOWN_FAIL, "Template down failed=" + dXTemplateItem.toString()));
                        }
                        DXContainerAppMonitor.trackerError(dXContainerError2);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void refreshByNotification() {
        this.singleCManager.refresh4UpdateTemplates();
        if (getContainerEngineContext().bizTypeSame()) {
            getContainerEngineContext().getEngine().getTabManager().refreshByNotification();
        }
    }

    /* access modifiers changed from: package-private */
    public void setPreLoadMoreListener(EngineLoadMoreListener engineLoadMoreListener, SparseArray<String> sparseArray) {
        this.singleCManager.setPreLoadMoreListener(engineLoadMoreListener, sparseArray);
    }

    /* access modifiers changed from: package-private */
    public boolean initData(DXContainerModel dXContainerModel) {
        getContainerEngineContext().getModelManager().clear();
        return this.singleCManager.initData(dXContainerModel);
    }

    /* access modifiers changed from: package-private */
    public DXContainerSingleRVManager getSingleCManager() {
        return this.singleCManager;
    }
}
