package com.taobao.uikit.feature.features.cellanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerCellAnimatorAdapter extends RecyclerView.Adapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String ALPHA = "alpha";
    private static final String SAVEDINSTANCESTATE_VIEWANIMATOR = "savedinstancestate_viewanimator";
    private RecyclerCellAnimatorController mCellAnimatorController;
    private RecyclerView.Adapter mDelegateAdapter;
    private boolean mIsRootAdapter = true;

    public abstract Animator[] getAnimators(ViewGroup viewGroup, View view);

    protected RecyclerCellAnimatorAdapter(RecyclerView.Adapter adapter) {
        this.mDelegateAdapter = adapter;
        if (adapter instanceof RecyclerCellAnimatorAdapter) {
            ((RecyclerCellAnimatorAdapter) adapter).mIsRootAdapter = false;
        }
        super.setHasStableIds(adapter.hasStableIds());
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return this.mDelegateAdapter.onCreateViewHolder(viewGroup, i);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        this.mDelegateAdapter.onBindViewHolder(viewHolder, i);
        if (this.mIsRootAdapter) {
            animateViewIfNecessary(i, viewHolder.itemView, (ViewGroup) null);
        }
    }

    public int getItemViewType(int i) {
        return this.mDelegateAdapter.getItemViewType(i);
    }

    public void setHasStableIds(boolean z) {
        this.mDelegateAdapter.setHasStableIds(z);
    }

    public long getItemId(int i) {
        return this.mDelegateAdapter.getItemId(i);
    }

    public int getItemCount() {
        return this.mDelegateAdapter.getItemCount();
    }

    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        this.mDelegateAdapter.onViewRecycled(viewHolder);
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        this.mDelegateAdapter.onViewAttachedToWindow(viewHolder);
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        this.mDelegateAdapter.onViewDetachedFromWindow(viewHolder);
    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        this.mDelegateAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        this.mDelegateAdapter.unregisterAdapterDataObserver(adapterDataObserver);
    }

    public void reset() {
        this.mCellAnimatorController.reset();
        if (this.mDelegateAdapter instanceof RecyclerCellAnimatorAdapter) {
            ((RecyclerCellAnimatorAdapter) this.mDelegateAdapter).reset();
        }
    }

    public void setAnimatorEnable(boolean z) {
        if (this.mCellAnimatorController != null) {
            this.mCellAnimatorController.setAnimatorEnable(z);
        }
    }

    public RecyclerCellAnimatorController getRecyclerCellAnimatorController() {
        return this.mCellAnimatorController;
    }

    public void setRecyclerCellAnimatorController(RecyclerCellAnimatorController recyclerCellAnimatorController) {
        this.mCellAnimatorController = recyclerCellAnimatorController;
    }

    private void animateViewIfNecessary(int i, View view, ViewGroup viewGroup) {
        Animator[] animators = this.mDelegateAdapter instanceof RecyclerCellAnimatorAdapter ? ((RecyclerCellAnimatorAdapter) this.mDelegateAdapter).getAnimators(viewGroup, view) : null;
        Animator[] animators2 = getAnimators(viewGroup, view);
        if (animators2 != null || animators != null) {
            this.mCellAnimatorController.animateViewIfNecessary(i, view, concatAnimators(animators, animators2, ObjectAnimator.ofFloat(view, ALPHA, new float[]{0.0f, 1.0f})));
        }
    }

    public static Animator[] concatAnimators(Animator[] animatorArr, Animator[] animatorArr2, Animator animator) {
        int i;
        int length = animatorArr == null ? 0 : animatorArr.length;
        if (animatorArr2 == null) {
            i = 0;
        } else {
            i = animatorArr2.length;
        }
        Animator[] animatorArr3 = new Animator[(length + i + 1)];
        if (animatorArr != null && length > 0) {
            System.arraycopy(animatorArr, 0, animatorArr3, 0, length);
        }
        if (animatorArr2 != null && i > 0) {
            System.arraycopy(animatorArr2, 0, animatorArr3, length, i);
        }
        animatorArr3[animatorArr3.length - 1] = animator;
        return animatorArr3;
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (this.mCellAnimatorController != null) {
            bundle.putParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR, this.mCellAnimatorController.onSaveInstanceState());
        }
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            if (this.mCellAnimatorController != null) {
                this.mCellAnimatorController.onRestoreInstanceState(bundle.getParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR));
            }
        }
    }
}
