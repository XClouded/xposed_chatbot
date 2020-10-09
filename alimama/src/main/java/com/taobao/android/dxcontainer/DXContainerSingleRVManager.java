package com.taobao.android.dxcontainer;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.dxcontainer.adapter.DXContainerBaseAdapter;
import com.taobao.android.dxcontainer.adapter.DXContainerBaseLayoutManager;
import com.taobao.android.dxcontainer.life.EngineLoadMoreListener;
import com.taobao.android.dxcontainer.reload.DXContainerReloadUtils;
import com.taobao.android.dxcontainer.render.DXContainerViewTypeGenerator;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DXContainerSingleRVManager extends DXContainerBaseClass {
    /* access modifiers changed from: private */
    public DXContainerBaseAdapter adapter;
    private String bizType;
    private Context context;
    private DinamicXEngine dxEngine;
    private boolean isSubContainer;
    private DXContainerBaseLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DXContainerModel rootModel;
    private DXContainerViewTypeGenerator viewTypeGenerator;

    /* access modifiers changed from: package-private */
    public DXContainerViewTypeGenerator getViewTypeGenerator() {
        return this.viewTypeGenerator;
    }

    /* access modifiers changed from: package-private */
    public String getBizType() {
        return this.bizType;
    }

    public DXContainerSingleRVManager(DXContainerEngineContext dXContainerEngineContext) {
        super(dXContainerEngineContext);
    }

    public void init(Context context2, RecyclerView recyclerView2, String str, DinamicXEngine dinamicXEngine, Integer num) {
        this.context = context2;
        this.recyclerView = recyclerView2;
        this.bizType = str;
        this.dxEngine = dinamicXEngine;
        this.layoutManager = new DXContainerBaseLayoutManager(context2);
        this.viewTypeGenerator = new DXContainerViewTypeGenerator(this.containerEngineContext.getRenderManager(str), this.containerEngineContext.getDxcLayoutManager());
        this.adapter = new DXContainerBaseAdapter(str, this.layoutManager, this.containerEngineContext.getRenderManager(str), this.viewTypeGenerator, num);
        this.adapter.setProLoadMoreListener(this.containerEngineContext.getEngine().getLoadMoreListener(), this.containerEngineContext.getEngine().getLoadMoreViewTexts());
        recyclerView2.setNestedScrollingEnabled(true);
        recyclerView2.setItemViewCacheSize(0);
        recyclerView2.setLayoutManager(this.layoutManager);
        recyclerView2.setAdapter(this.adapter);
    }

    public DXContainerModel getRootModel() {
        return this.rootModel;
    }

    public void setRootModel(DXContainerModel dXContainerModel) {
        this.rootModel = dXContainerModel;
    }

    public void smoothScrollToPosition(int i, int i2, DXContainerScrollFinishedListener dXContainerScrollFinishedListener) {
        if (i != -1) {
            new DXContainerPracticalRecyclerViewFlinger(this.recyclerView, i, i2, dXContainerScrollFinishedListener).postOnAnimation();
        }
    }

    public void scrollToPosition(int i, int i2) {
        View findViewByPosition;
        int top;
        DXContainerBaseLayoutManager dXContainerBaseLayoutManager = (DXContainerBaseLayoutManager) this.recyclerView.getLayoutManager();
        dXContainerBaseLayoutManager.scrollToPositionWithOffset(i, i2);
        if ((i >= dXContainerBaseLayoutManager.findFirstVisibleItemPosition() && i <= dXContainerBaseLayoutManager.findLastVisibleItemPosition()) && (findViewByPosition = dXContainerBaseLayoutManager.findViewByPosition(i)) != null && (top = findViewByPosition.getTop() - i2) != 0) {
            this.recyclerView.scrollBy(0, top);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSubContainer(boolean z) {
        this.isSubContainer = z;
    }

    public DXContainerModel getDXCModelByID(String str) {
        if (!TextUtils.isEmpty(str)) {
            return getContainerEngineContext().getModelManager().getDXCModelByID(str);
        }
        return null;
    }

    public void refresh4UpdateTemplates() {
        if (this.adapter != null) {
            if (updateViewType(this.viewTypeGenerator, this.adapter.getData())) {
                for (LayoutHelper next : this.adapter.getLayoutHelpers()) {
                    if (next instanceof StaggeredGridLayoutHelper) {
                        ((StaggeredGridLayoutHelper) next).forceClearSpanLookup();
                    }
                }
                this.recyclerView.post(new Runnable() {
                    public void run() {
                        DXContainerSingleRVManager.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private boolean updateViewType(DXContainerViewTypeGenerator dXContainerViewTypeGenerator, List<DXContainerModel> list) {
        if (dXContainerViewTypeGenerator == null || list == null || list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            dXContainerViewTypeGenerator.modelToViewType(i, list.get(i));
        }
        return true;
    }

    public void updateAllMap(boolean z) {
        if (this.rootModel != null && this.rootModel.getChildren() != null && this.recyclerView != null && this.recyclerView.getVisibility() == 0) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            DXContainerModelManager modelManager = getContainerEngineContext().getModelManager();
            modelManager.traverseModel(this, arrayList2, hashMap, hashMap2, this.rootModel);
            if (z) {
                this.dxEngine.downLoadTemplates(arrayList2);
            }
            modelManager.updateDXCModelId(hashMap);
            modelManager.updateDXCModelTag(hashMap2);
            List<LayoutHelper> traversalTree = DXContainerReloadUtils.traversalTree(this.context, this.rootModel, arrayList, getContainerEngineContext().getDxcLayoutManager(), this.viewTypeGenerator);
            List<LayoutHelper> layoutHelpers = this.adapter.getLayoutHelpers();
            if (this.adapter.hasLoadMoreLayoutHelper()) {
                traversalTree.add(layoutHelpers.get(layoutHelpers.size() - 1));
            }
            layoutHelpers.clear();
            layoutHelpers.addAll(traversalTree);
            updateLayoutHelpersRange(layoutHelpers);
            this.adapter.setData(arrayList);
        }
    }

    private void updateLayoutHelpersRange(List<LayoutHelper> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            LayoutHelper layoutHelper = list.get(i2);
            int itemCount = (layoutHelper.getItemCount() + i) - 1;
            layoutHelper.setRange(i, itemCount);
            i += itemCount + 1;
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshAll() {
        if (this.rootModel != null && this.rootModel.getChildren() != null && this.recyclerView != null && this.recyclerView.getVisibility() == 0) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            DXContainerModelManager modelManager = getContainerEngineContext().getModelManager();
            modelManager.traverseModel(this, arrayList2, hashMap, hashMap2, this.rootModel);
            this.dxEngine.downLoadTemplates(arrayList2);
            modelManager.updateDXCModelId(hashMap);
            modelManager.updateDXCModelTag(hashMap2);
            refreshAll(DXContainerReloadUtils.traversalTree(this.context, this.rootModel, arrayList, getContainerEngineContext().getDxcLayoutManager(), this.viewTypeGenerator), arrayList);
        }
    }

    public void refreshIncrement() {
        refreshAll();
    }

    private void refreshAll(List<LayoutHelper> list, List<DXContainerModel> list2) {
        this.adapter.setHelpers(list);
        this.adapter.setData(list2);
        try {
            if (this.recyclerView.getScrollState() != 0 || this.recyclerView.isComputingLayout()) {
                this.recyclerView.post(new Runnable() {
                    public void run() {
                        DXContainerSingleRVManager.this.adapter.notifyDataSetChanged();
                    }
                });
            } else {
                this.adapter.notifyDataSetChanged();
            }
        } catch (Exception unused) {
        }
    }

    public void notifyItemRangeInsert(int i, int i2) {
        this.adapter.notifyItemRangeInserted(i, i2);
    }

    public void notifyItemRangeRemoved(int i, int i2) {
        this.adapter.notifyItemRangeRemoved(i, i2);
    }

    public void notifyItemRangeChange(int i, int i2) {
        this.adapter.notifyItemRangeChanged(i, i2);
    }

    /* access modifiers changed from: package-private */
    public void setPreLoadMoreListener(EngineLoadMoreListener engineLoadMoreListener, SparseArray<String> sparseArray) {
        this.adapter.setProLoadMoreListener(engineLoadMoreListener, sparseArray);
    }

    /* access modifiers changed from: package-private */
    public boolean initData(DXContainerModel dXContainerModel) {
        String str = "mainContainer";
        if (this.isSubContainer) {
            str = "subContainer";
        }
        if (dXContainerModel == null) {
            DXContainerError dXContainerError = new DXContainerError(getContainerEngineConfig().getBizType());
            dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_INIT_DM_ROOT_EMPTY, str + "initData root model is null, check json data"));
            DXContainerAppMonitor.trackerError(dXContainerError);
            return false;
        } else if (getRootModel() == dXContainerModel) {
            return false;
        } else {
            this.viewTypeGenerator.reset();
            setRootModel(dXContainerModel);
            this.adapter.resetLoadMoreState();
            refreshAll();
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public DXContainerBaseAdapter getAdapter() {
        return this.adapter;
    }

    /* access modifiers changed from: package-private */
    public int getRecyclerViewContentHeight() {
        return this.recyclerView.getHeight() - this.recyclerView.getPaddingTop();
    }

    /* access modifiers changed from: package-private */
    public int getRecyclerViewPaddingTop() {
        return this.recyclerView.getPaddingTop();
    }

    /* access modifiers changed from: package-private */
    public RecyclerView getContentView() {
        return this.recyclerView;
    }

    /* access modifiers changed from: package-private */
    public DXContainerBaseLayoutManager getLayoutManager() {
        return this.layoutManager;
    }
}
