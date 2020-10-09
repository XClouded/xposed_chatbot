package com.taobao.weex.ui.view.listview.adapter;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXHeader;
import java.lang.ref.WeakReference;

public class ListBaseViewHolder extends RecyclerView.ViewHolder {
    private boolean isRecycled;
    private WeakReference<WXComponent> mComponent;
    private int mViewType;

    public ListBaseViewHolder(WXComponent wXComponent, int i) {
        super(wXComponent.getHostView());
        this.mViewType = i;
        this.mComponent = new WeakReference<>(wXComponent);
        this.isRecycled = wXComponent.canRecycled();
    }

    public ListBaseViewHolder(WXComponent wXComponent, int i, boolean z) {
        this(wXComponent, i);
        this.isRecycled = this.isRecycled || z;
    }

    public ListBaseViewHolder(View view, int i) {
        super(view);
        this.mViewType = i;
    }

    public boolean isRecycled() {
        return this.isRecycled;
    }

    public void recycled() {
        if (this.mComponent != null && this.mComponent.get() != null) {
            ((WXComponent) this.mComponent.get()).recycled();
            this.isRecycled = true;
        }
    }

    public void bindData(WXComponent wXComponent) {
        if (this.mComponent != null && this.mComponent.get() != null) {
            ((WXComponent) this.mComponent.get()).bindData(wXComponent);
            this.isRecycled = false;
        }
    }

    public boolean isFullSpan() {
        return this.mComponent != null && (this.mComponent.get() instanceof WXHeader);
    }

    public boolean canRecycled() {
        if (this.mComponent == null || this.mComponent.get() == null) {
            return true;
        }
        return ((WXComponent) this.mComponent.get()).canRecycled();
    }

    public View getView() {
        return this.itemView;
    }

    public int getViewType() {
        return this.mViewType;
    }

    public void setComponentUsing(boolean z) {
        if (this.mComponent != null && this.mComponent.get() != null) {
            ((WXComponent) this.mComponent.get()).setUsing(z);
        }
    }

    public WXComponent getComponent() {
        if (this.mComponent != null) {
            return (WXComponent) this.mComponent.get();
        }
        return null;
    }
}
