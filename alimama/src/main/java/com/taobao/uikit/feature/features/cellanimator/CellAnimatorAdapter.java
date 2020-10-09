package com.taobao.uikit.feature.features.cellanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CellAnimatorAdapter extends BaseAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String ALPHA = "alpha";
    private static final String SAVEDINSTANCESTATE_VIEWANIMATOR = "savedinstancestate_viewanimator";
    private CellAnimatorController mCellAnimatorController;
    private BaseAdapter mDelegateAdapter;
    private boolean mIsRootAdapter = true;

    public abstract Animator[] getAnimators(ViewGroup viewGroup, View view);

    protected CellAnimatorAdapter(BaseAdapter baseAdapter) {
        this.mDelegateAdapter = baseAdapter;
        if (baseAdapter instanceof CellAnimatorAdapter) {
            ((CellAnimatorAdapter) baseAdapter).mIsRootAdapter = false;
        }
    }

    public int getCount() {
        return this.mDelegateAdapter.getCount();
    }

    public Object getItem(int i) {
        return this.mDelegateAdapter.getItem(i);
    }

    public long getItemId(int i) {
        return this.mDelegateAdapter.getItemId(i);
    }

    public boolean areAllItemsEnabled() {
        return this.mDelegateAdapter.areAllItemsEnabled();
    }

    public int getItemViewType(int i) {
        return this.mDelegateAdapter.getItemViewType(i);
    }

    public int getViewTypeCount() {
        return this.mDelegateAdapter.getViewTypeCount();
    }

    public boolean hasStableIds() {
        return this.mDelegateAdapter.hasStableIds();
    }

    public boolean isEmpty() {
        return this.mDelegateAdapter.isEmpty();
    }

    public boolean isEnabled(int i) {
        return this.mDelegateAdapter.isEnabled(i);
    }

    public final View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = this.mDelegateAdapter.getView(i, view, viewGroup);
        if (this.mIsRootAdapter) {
            animateViewIfNecessary(i, view2, viewGroup);
        }
        return view2;
    }

    public void notifyDataSetChanged() {
        this.mDelegateAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetInvalidated() {
        this.mDelegateAdapter.notifyDataSetInvalidated();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegateAdapter.registerDataSetObserver(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegateAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    public void reset() {
        this.mCellAnimatorController.reset();
        if (this.mDelegateAdapter instanceof CellAnimatorAdapter) {
            ((CellAnimatorAdapter) this.mDelegateAdapter).reset();
        }
    }

    public void setAnimatorEnable(boolean z) {
        if (this.mCellAnimatorController != null) {
            this.mCellAnimatorController.setAnimatorEnable(z);
        }
    }

    public CellAnimatorController getCellAnimatorController() {
        return this.mCellAnimatorController;
    }

    public void setCellAnimatorController(CellAnimatorController cellAnimatorController) {
        this.mCellAnimatorController = cellAnimatorController;
    }

    private void animateViewIfNecessary(int i, View view, ViewGroup viewGroup) {
        Animator[] animators = this.mDelegateAdapter instanceof CellAnimatorAdapter ? ((CellAnimatorAdapter) this.mDelegateAdapter).getAnimators(viewGroup, view) : null;
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
