package com.taobao.weex.ui.view.listview.adapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.weex.ui.view.listview.adapter.ListBaseViewHolder;

public class RecyclerViewBaseAdapter<T extends ListBaseViewHolder> extends RecyclerView.Adapter<T> {
    private IRecyclerAdapterListener iRecyclerAdapterListener;

    public RecyclerViewBaseAdapter(IRecyclerAdapterListener iRecyclerAdapterListener2) {
        this.iRecyclerAdapterListener = iRecyclerAdapterListener2;
    }

    public T onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.iRecyclerAdapterListener != null) {
            return (ListBaseViewHolder) this.iRecyclerAdapterListener.onCreateViewHolder(viewGroup, i);
        }
        return null;
    }

    public void onViewAttachedToWindow(T t) {
        ViewGroup.LayoutParams layoutParams;
        super.onViewAttachedToWindow(t);
        if (t != null && t.isFullSpan() && (layoutParams = t.itemView.getLayoutParams()) != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    public void onViewDetachedFromWindow(T t) {
        super.onViewDetachedFromWindow(t);
        if (t != null) {
            t.setComponentUsing(false);
        }
    }

    public void onBindViewHolder(T t, int i) {
        if (this.iRecyclerAdapterListener != null) {
            this.iRecyclerAdapterListener.onBindViewHolder(t, i);
        }
    }

    public int getItemViewType(int i) {
        return this.iRecyclerAdapterListener != null ? this.iRecyclerAdapterListener.getItemViewType(i) : i;
    }

    public long getItemId(int i) {
        return this.iRecyclerAdapterListener.getItemId(i);
    }

    public int getItemCount() {
        if (this.iRecyclerAdapterListener != null) {
            return this.iRecyclerAdapterListener.getItemCount();
        }
        return 0;
    }

    public void onViewRecycled(T t) {
        if (this.iRecyclerAdapterListener != null) {
            this.iRecyclerAdapterListener.onViewRecycled(t);
        }
        super.onViewRecycled(t);
    }

    public boolean onFailedToRecycleView(T t) {
        if (this.iRecyclerAdapterListener != null) {
            return this.iRecyclerAdapterListener.onFailedToRecycleView(t);
        }
        return super.onFailedToRecycleView(t);
    }
}
