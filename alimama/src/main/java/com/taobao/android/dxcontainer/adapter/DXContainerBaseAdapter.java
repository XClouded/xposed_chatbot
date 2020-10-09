package com.taobao.android.dxcontainer.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dxcontainer.DXContainerAppMonitor;
import com.taobao.android.dxcontainer.DXContainerError;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.taobao.android.dxcontainer.DXContainerGlobalCenter;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.R;
import com.taobao.android.dxcontainer.life.EngineLifeStateListener;
import com.taobao.android.dxcontainer.life.EngineLoadMoreListener;
import com.taobao.android.dxcontainer.life.EngineMainLoadMoreListener;
import com.taobao.android.dxcontainer.life.EngineTabLoadMoreListener;
import com.taobao.android.dxcontainer.loadmore.DXContainerLoadMoreState;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreView;
import com.taobao.android.dxcontainer.render.DXContainerRenderManager;
import com.taobao.android.dxcontainer.render.DXContainerViewTypeGenerator;
import com.taobao.android.dxcontainer.render.IDXContainerRender;
import com.taobao.android.dxcontainer.utils.DXContainerLoadMoreModelUtils;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutAdapter;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import com.taobao.android.dxcontainer.vlayout.layout.LinearLayoutHelper;
import java.util.List;

public class DXContainerBaseAdapter extends VirtualLayoutAdapter<DXCViewHolder> {
    private static final String TAG = "DXCRVAdapter";
    private String bizType;
    private List<DXContainerModel> data;
    private boolean isSubEngine;
    private DXContainerModel lastModel;
    private EngineLifeStateListener lifeListener;
    private DXContainerLoadMoreState loadMoreState;
    private EngineLoadMoreListener proLoadMoreListener;
    private DXContainerRenderManager renderManager;
    /* access modifiers changed from: private */
    public Integer tabIndex;
    private DXContainerViewTypeGenerator viewTypeGenerator;

    public void resetLoadMoreState() {
        if (this.loadMoreState != null) {
            this.loadMoreState.reset();
        }
    }

    public boolean hasLoadMoreLayoutHelper() {
        return this.proLoadMoreListener != null && this.proLoadMoreListener.isShowBottomView();
    }

    public void setLifeListener(EngineLifeStateListener engineLifeStateListener) {
        this.lifeListener = engineLifeStateListener;
    }

    public DXContainerBaseAdapter(String str, VirtualLayoutManager virtualLayoutManager, DXContainerRenderManager dXContainerRenderManager, DXContainerViewTypeGenerator dXContainerViewTypeGenerator, Integer num) {
        this(virtualLayoutManager);
        this.bizType = str;
        this.renderManager = dXContainerRenderManager;
        this.viewTypeGenerator = dXContainerViewTypeGenerator;
        this.isSubEngine = num != null;
        this.tabIndex = num;
    }

    public void setProLoadMoreListener(EngineLoadMoreListener engineLoadMoreListener, SparseArray<String> sparseArray) {
        this.proLoadMoreListener = engineLoadMoreListener;
        this.loadMoreState = new DXContainerLoadMoreState(sparseArray);
    }

    DXContainerBaseAdapter(@NonNull VirtualLayoutManager virtualLayoutManager) {
        super(virtualLayoutManager);
        this.isSubEngine = false;
        this.lastModel = null;
    }

    public void setData(List<DXContainerModel> list) {
        if (!(list == null || this.proLoadMoreListener == null || !this.proLoadMoreListener.isShowBottomView())) {
            if (this.proLoadMoreListener instanceof EngineMainLoadMoreListener) {
                if (!this.isSubEngine) {
                    if (this.lastModel == null) {
                        this.lastModel = DXContainerLoadMoreModelUtils.getLoadMoreModel(1);
                    }
                    this.viewTypeGenerator.modelToViewType(list.size(), this.lastModel);
                    list.add(this.lastModel);
                }
            } else if ((this.proLoadMoreListener instanceof EngineTabLoadMoreListener) && this.isSubEngine && this.tabIndex != null) {
                if (this.lastModel == null) {
                    this.lastModel = DXContainerLoadMoreModelUtils.getLoadMoreModel(1);
                }
                this.viewTypeGenerator.modelToViewType(list.size(), this.lastModel);
                list.add(this.lastModel);
            }
        }
        this.data = list;
    }

    public List<DXContainerModel> getData() {
        return this.data;
    }

    public void setHelpers(List<LayoutHelper> list) {
        if (list == null || list.isEmpty()) {
            DXContainerError dXContainerError = new DXContainerError(this.bizType);
            dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3017, "setHelperError"));
            DXContainerAppMonitor.trackerError(dXContainerError);
            return;
        }
        if (this.isSubEngine && this.proLoadMoreListener != null && this.proLoadMoreListener.isShowBottomView()) {
            LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
            linearLayoutHelper.setItemCount(1);
            list.add(linearLayoutHelper);
        }
        setLayoutHelpers(list);
    }

    public DXCViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        String renderType = this.viewTypeGenerator.getRenderType(i);
        String viewTypeId = this.viewTypeGenerator.getViewTypeId(i);
        Object renderObject = this.viewTypeGenerator.getRenderObject(i);
        if (this.lifeListener != null) {
            this.lifeListener.beforeCreateViewHolder(viewGroup, i, renderType, viewTypeId, renderObject);
        }
        IDXContainerRender render = this.renderManager.getRender(renderType);
        if (render == null) {
            view = new Space(viewGroup.getContext());
        } else {
            view = render.createView(viewGroup, viewTypeId, renderObject);
        }
        if (view instanceof Space) {
            view.setVisibility(8);
            String str = null;
            if (renderObject instanceof DXTemplateItem) {
                str = renderObject.toString();
            }
            switch (i) {
                case -4:
                    trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3002, "onCreateViewHolder render is nullrenderObject=" + str);
                    break;
                case -3:
                    trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3015, "onCreateViewHolder custom renderType error");
                    break;
                case -2:
                    trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3014, "onCreateViewHolder model is null");
                    break;
                case -1:
                    view.setTag(R.id.tag_no_template_view_type, -1);
                    break;
                default:
                    trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3016, "onCreateViewHolder other space view error=" + renderType + "renderObject=" + str);
                    break;
            }
        }
        DXCViewHolder dXCViewHolder = new DXCViewHolder(view);
        dXCViewHolder.renderType = renderType;
        dXCViewHolder.viewTypeId = viewTypeId;
        dXCViewHolder.renderObject = renderObject;
        if (this.lifeListener != null) {
            this.lifeListener.afterCreateViewHolder(viewGroup, i, renderType, viewTypeId, renderObject);
        }
        return dXCViewHolder;
    }

    public void onBindViewHolder(DXCViewHolder dXCViewHolder, int i) {
        DXContainerModel findDXCModelByTotalPosition = findDXCModelByTotalPosition(i);
        String renderTypeByPosition = this.viewTypeGenerator.getRenderTypeByPosition(i);
        if (this.lifeListener != null) {
            this.lifeListener.beforeBindViewHolder(dXCViewHolder.itemView, i, findDXCModelByTotalPosition, renderTypeByPosition);
        }
        if (findDXCModelByTotalPosition == null) {
            trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3003, "model is null" + i);
            return;
        }
        IDXContainerRender render = this.renderManager.getRender(renderTypeByPosition);
        if (render != null) {
            if (this.loadMoreState != null && !this.loadMoreState.isInit() && (dXCViewHolder.itemView instanceof IDXContainerLoadMoreView)) {
                this.loadMoreState.setDXCLoadMoreListener((IDXContainerLoadMoreView) dXCViewHolder.itemView, findDXCModelByTotalPosition);
                dXCViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DXContainerBaseAdapter.this.manualLoadMore(DXContainerBaseAdapter.this.tabIndex);
                    }
                });
            }
            if (!this.isSubEngine || this.tabIndex == null) {
                preLoadMore(i);
            } else {
                preLoadMore(i, this.tabIndex);
            }
            dXCViewHolder.model = findDXCModelByTotalPosition;
            render.renderView(findDXCModelByTotalPosition, dXCViewHolder.itemView, i);
            if (this.lifeListener != null) {
                this.lifeListener.afterBindViewHolder(dXCViewHolder.itemView, i, findDXCModelByTotalPosition, renderTypeByPosition);
            }
        } else if (dXCViewHolder.itemView instanceof Space) {
            int i2 = 0;
            Object tag = dXCViewHolder.itemView.getTag(R.id.tag_no_template_view_type);
            if (tag instanceof Integer) {
                i2 = ((Integer) tag).intValue();
            }
            if (i2 != -1) {
                trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3004, "on bind data view is space view model id = " + findDXCModelByTotalPosition.getId());
            } else if (DXContainerGlobalCenter.isDebug()) {
                DXContainerAppMonitor.logi("模板还没下载，渲染为空view");
            }
        } else {
            trackError(DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, 3004, "on bind data render null model id = " + findDXCModelByTotalPosition.getId());
        }
    }

    /* access modifiers changed from: private */
    public void manualLoadMore(Integer num) {
        if (this.loadMoreState.getState() != 3) {
            return;
        }
        if (this.proLoadMoreListener instanceof EngineMainLoadMoreListener) {
            EngineMainLoadMoreListener engineMainLoadMoreListener = (EngineMainLoadMoreListener) this.proLoadMoreListener;
            if (engineMainLoadMoreListener.isEnableLoadMore()) {
                engineMainLoadMoreListener.onLoadMore(this.loadMoreState);
            }
        } else if (this.proLoadMoreListener instanceof EngineTabLoadMoreListener) {
            EngineTabLoadMoreListener engineTabLoadMoreListener = (EngineTabLoadMoreListener) this.proLoadMoreListener;
            if (num != null && engineTabLoadMoreListener.isEnableLoadMoreWithTabIndex(num.intValue())) {
                engineTabLoadMoreListener.onLoadMoreWithTabIndex(num.intValue(), this.loadMoreState);
            }
        }
    }

    private void preLoadMore(int i) {
        if (this.proLoadMoreListener instanceof EngineMainLoadMoreListener) {
            EngineMainLoadMoreListener engineMainLoadMoreListener = (EngineMainLoadMoreListener) this.proLoadMoreListener;
            if (i > this.data.size() - 4 && i >= 0 && engineMainLoadMoreListener.isEnableLoadMore() && this.loadMoreState.getState() == 0) {
                engineMainLoadMoreListener.onLoadMore(this.loadMoreState);
            }
        }
    }

    private void preLoadMore(int i, Integer num) {
        if (this.proLoadMoreListener instanceof EngineTabLoadMoreListener) {
            EngineTabLoadMoreListener engineTabLoadMoreListener = (EngineTabLoadMoreListener) this.proLoadMoreListener;
            if (i > this.data.size() - 4 && i >= 0 && num != null && engineTabLoadMoreListener.isEnableLoadMoreWithTabIndex(num.intValue()) && this.loadMoreState.getState() == 0) {
                engineTabLoadMoreListener.onLoadMoreWithTabIndex(num.intValue(), this.loadMoreState);
            }
        }
    }

    private DXContainerModel findDXCModelByTotalPosition(int i) {
        if (this.data == null || i >= this.data.size()) {
            return null;
        }
        return this.data.get(i);
    }

    public int getItemCount() {
        if (this.data != null) {
            return this.data.size();
        }
        return 0;
    }

    public int getItemViewType(int i) {
        return this.viewTypeGenerator.getViewType(i);
    }

    static class DXCViewHolder extends RecyclerView.ViewHolder {
        public DXContainerModel model;
        public Object renderObject;
        public String renderType;
        public String viewTypeId;

        DXCViewHolder(View view) {
            super(view);
        }
    }

    private void trackError(String str, int i, String str2) {
        DXContainerError dXContainerError = new DXContainerError(this.bizType);
        dXContainerError.dxErrorInfoList.add(new DXContainerError.DXContainerErrorInfo(str, i, str2));
        DXContainerAppMonitor.trackerError(dXContainerError);
    }

    public void onViewRecycled(DXCViewHolder dXCViewHolder) {
        super.onViewRecycled(dXCViewHolder);
        IDXContainerRender render = this.renderManager.getRender(dXCViewHolder.renderType);
        if (render != null) {
            render.onViewRecycled(dXCViewHolder.itemView, dXCViewHolder.model, dXCViewHolder.renderType, dXCViewHolder.viewTypeId, dXCViewHolder.renderObject);
        }
        if (this.lifeListener != null) {
            this.lifeListener.onViewRecycled(dXCViewHolder.itemView, dXCViewHolder.model, dXCViewHolder.renderType, dXCViewHolder.viewTypeId, dXCViewHolder.renderObject);
        }
    }
}
