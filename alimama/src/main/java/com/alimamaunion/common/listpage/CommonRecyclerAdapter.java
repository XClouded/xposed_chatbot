package com.alimamaunion.common.listpage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CommonRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "CommonRecyclerAdapter";
    private boolean isEnableFooter = false;
    protected boolean isFinish = false;
    protected List<CommonItemInfo> mCommonItemInfos = new ArrayList();
    private IFooterProcess mFooterProcesser;
    protected CommonItemInfo mItemInfo;

    public void notifyResult(boolean z, List<CommonItemInfo> list) {
        if (list != null) {
            if (z) {
                this.mCommonItemInfos.clear();
            }
            this.mCommonItemInfos.addAll(list);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        CommonBaseViewHolder createHomeItemViewHolder = CommonItemInfo.createHomeItemViewHolder(i);
        if (createHomeItemViewHolder != null) {
            return new CommonRecyclerViewHolder(createHomeItemViewHolder.createView(from, viewGroup), createHomeItemViewHolder);
        }
        return new CommonDefaultViewHolder(from.inflate(R.layout.common_default_view_holder, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CommonRecyclerViewHolder) {
            CommonRecyclerViewHolder commonRecyclerViewHolder = (CommonRecyclerViewHolder) viewHolder;
            if (!this.isEnableFooter || this.mFooterProcesser == null || !this.mFooterProcesser.isFooter(commonRecyclerViewHolder.mBaseViewHolder)) {
                this.mItemInfo = this.mCommonItemInfos.get(i);
                CommonBaseItem commonBaseItem = this.mItemInfo.commonBaseItem;
                if (commonBaseItem != null) {
                    commonBaseItem.notifyUpdate(i, this, this.mItemInfo);
                    commonRecyclerViewHolder.mBaseViewHolder.onBindViewHolder(i, commonBaseItem);
                    return;
                }
                return;
            }
            this.mFooterProcesser.process(commonRecyclerViewHolder.mBaseViewHolder, this.isFinish);
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
    }

    public int getItemCount() {
        return this.mCommonItemInfos.size() + (this.isEnableFooter ? 1 : 0);
    }

    public List<CommonItemInfo> getCommonItemInfos() {
        return this.mCommonItemInfos;
    }

    public int getItemViewType(int i) {
        if (this.isEnableFooter && i == getItemCount() - 1) {
            return CommonItemInfo.FOOT_TYPE;
        }
        if (i < 0 || i >= this.mCommonItemInfos.size()) {
            return 0;
        }
        return this.mCommonItemInfos.get(i).viewType;
    }

    public void setFooterProcesser(IFooterProcess iFooterProcess) {
        this.mFooterProcesser = iFooterProcess;
    }

    public void setEnableFooter(boolean z) {
        this.isEnableFooter = z;
    }

    public void setFinish(boolean z) {
        this.isFinish = z;
    }

    public boolean isFinish() {
        return this.isFinish;
    }
}
