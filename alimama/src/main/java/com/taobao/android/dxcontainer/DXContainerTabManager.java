package com.taobao.android.dxcontainer;

import android.util.SparseArray;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.notification.DXNotificationResult;
import com.taobao.android.dinamicx.notification.DXTemplateUpdateRequest;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.dxcontainer.adapter.DXContainerBaseLayoutManager;

public class DXContainerTabManager extends DXContainerBaseClass {
    /* access modifiers changed from: private */
    public String bizType = getContainerEngineConfig().getSubBizType();
    private DinamicXEngine dinamicXEngine;
    /* access modifiers changed from: private */
    public SparseArray<DXContainerSingleRVManager> singleContainerManagers = new SparseArray<>();
    private DXContainerViewPager viewPager;

    DXContainerTabManager(DXContainerEngineContext dXContainerEngineContext) {
        super(dXContainerEngineContext);
        this.dinamicXEngine = dXContainerEngineContext.getDinamicXEngine(this.bizType);
        registerDefaultNotification();
    }

    /* access modifiers changed from: package-private */
    public DXContainerSingleRVManager getDXNContainerSingleCManagerByViewPage(int i) {
        DXContainerSingleRVManager dXContainerSingleRVManager = this.singleContainerManagers.get(i);
        if (dXContainerSingleRVManager != null) {
            return dXContainerSingleRVManager;
        }
        DXContainerSingleRVManager dXContainerSingleRVManager2 = new DXContainerSingleRVManager(this.containerEngineContext);
        dXContainerSingleRVManager2.setSubContainer(true);
        IDXContainerRecyclerViewInterface recyclerViewInterface = this.containerEngineContext.getRecyclerViewInterface();
        DXContainerRecyclerViewOption dXContainerRecyclerViewOption = new DXContainerRecyclerViewOption();
        dXContainerRecyclerViewOption.setSub(true);
        DXContainerSingleRVManager dXContainerSingleRVManager3 = dXContainerSingleRVManager2;
        dXContainerSingleRVManager3.init(this.containerEngineContext.getContext(), recyclerViewInterface.newRecyclerView(this.containerEngineContext.getContext(), dXContainerRecyclerViewOption), this.bizType, this.dinamicXEngine, Integer.valueOf(i));
        DXContainerEngine engine = getContainerEngineContext().getEngine();
        dXContainerSingleRVManager2.setPreLoadMoreListener(engine.getLoadMoreListener(), engine.getLoadMoreViewTexts());
        this.singleContainerManagers.put(i, dXContainerSingleRVManager2);
        return dXContainerSingleRVManager2;
    }

    private void registerDefaultNotification() {
        if (!getContainerEngineContext().bizTypeSame()) {
            this.dinamicXEngine.registerNotificationListener(new IDXNotificationListener() {
                public void onNotificationListener(DXNotificationResult dXNotificationResult) {
                    boolean z = true;
                    boolean z2 = dXNotificationResult.templateUpdateRequestList.size() > 0;
                    boolean z3 = dXNotificationResult.finishedTemplateItems.size() > 0;
                    if (dXNotificationResult.failedTemplateItems.size() <= 0) {
                        z = false;
                    }
                    if (z2 || z3) {
                        if (z2) {
                            DXContainerError dXContainerError = new DXContainerError(DXContainerTabManager.this.bizType);
                            for (DXTemplateUpdateRequest next : dXNotificationResult.templateUpdateRequestList) {
                                if (next.reason == 1000) {
                                    dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, DXContainerErrorConstant.DX_CONTAINER_ERROR_DX_TEMPLATE_DOWNGRADE, "Template downgrade template info=" + next.item.toString()));
                                    for (int i = 0; i < DXContainerTabManager.this.singleContainerManagers.size(); i++) {
                                        ((DXContainerSingleRVManager) DXContainerTabManager.this.singleContainerManagers.valueAt(i)).getViewTypeGenerator().removeViewType(next.item.getIdentifier());
                                    }
                                }
                            }
                            DXContainerAppMonitor.trackerError(dXContainerError);
                        }
                        DXContainerTabManager.this.refreshByNotification();
                    }
                    if (z) {
                        DXContainerError dXContainerError2 = new DXContainerError(DXContainerTabManager.this.bizType);
                        for (DXTemplateItem dXTemplateItem : dXNotificationResult.failedTemplateItems) {
                            dXContainerError2.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, DXContainerErrorConstant.DX_CONTAINER_ERROR_DX_TEMPLATE_DOWN_FAIL, "Template down failed=" + dXTemplateItem.toString()));
                        }
                        DXContainerAppMonitor.trackerError(dXContainerError2);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshByNotification() {
        for (int i = 0; i < this.singleContainerManagers.size(); i++) {
            DXContainerSingleRVManager valueAt = this.singleContainerManagers.valueAt(i);
            if (valueAt != null) {
                valueAt.refresh4UpdateTemplates();
            }
        }
    }

    public DinamicXEngine getDinamicXEngine() {
        return this.dinamicXEngine;
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        if (this.viewPager != null) {
            this.viewPager.needRefresh(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshAll() {
        for (int i = 0; i < this.singleContainerManagers.size(); i++) {
            this.singleContainerManagers.valueAt(i).refreshAll();
        }
    }

    /* access modifiers changed from: package-private */
    public DXContainerViewPager getViewPager() {
        return this.viewPager;
    }

    /* access modifiers changed from: package-private */
    public void setViewPager(DXContainerViewPager dXContainerViewPager) {
        this.viewPager = dXContainerViewPager;
    }

    /* access modifiers changed from: package-private */
    public DXContainerModel getTabRootDXCModel(int i) {
        DXContainerSingleRVManager dXContainerSingleRVManager = this.singleContainerManagers.get(i);
        if (dXContainerSingleRVManager != null) {
            return dXContainerSingleRVManager.getRootModel();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public DXContainerBaseLayoutManager getTabLayoutManager(int i) {
        DXContainerSingleRVManager dXContainerSingleRVManager = this.singleContainerManagers.get(i);
        if (dXContainerSingleRVManager != null) {
            return dXContainerSingleRVManager.getLayoutManager();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void scrollToTop() {
        for (int i = 0; i < this.singleContainerManagers.size(); i++) {
            this.singleContainerManagers.valueAt(i).scrollToPosition(0, 0);
        }
    }
}
