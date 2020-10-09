package com.taobao.uikit.feature.features;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.AdapterCallback;
import com.taobao.uikit.feature.features.cellanimator.CellAnimatorAdapter;
import com.taobao.uikit.feature.features.cellanimator.CellAnimatorController;

public class CellAnimatorFeature extends AbsFeature<ListView> implements AdapterCallback {
    private AnimatorAdapter mAnimatorAdapter;
    private int mAnimatorDelayMillis = 30;
    private int mAnimatorDurationMillis = 400;
    /* access modifiers changed from: private */
    public CustomAnimatorFactory mCustomAnimatorFactory;
    private int mInitialDelayMillis = 100;
    private boolean mIsAnimatorEnable = true;

    public interface CustomAnimatorFactory {
        Animator[] generateAnimators(ViewGroup viewGroup, View view);
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CellAnimatorFeature, i, 0)) != null) {
            this.mInitialDelayMillis = obtainStyledAttributes.getInt(R.styleable.CellAnimatorFeature_uik_initialDelay, this.mInitialDelayMillis);
            this.mAnimatorDelayMillis = obtainStyledAttributes.getInt(R.styleable.CellAnimatorFeature_uik_animatorDelay, this.mAnimatorDelayMillis);
            this.mAnimatorDurationMillis = obtainStyledAttributes.getInt(R.styleable.CellAnimatorFeature_uik_animatorDuration, this.mAnimatorDurationMillis);
            obtainStyledAttributes.recycle();
        }
    }

    public ListAdapter wrapAdapter(ListAdapter listAdapter) {
        if (listAdapter == null || (listAdapter instanceof AnimatorAdapter) || !(listAdapter instanceof BaseAdapter)) {
            return listAdapter;
        }
        CellAnimatorController cellAnimatorController = new CellAnimatorController((ListView) getHost());
        cellAnimatorController.setInitialDelayMillis(this.mInitialDelayMillis);
        cellAnimatorController.setAnimationDelayMillis(this.mAnimatorDelayMillis);
        cellAnimatorController.setAnimationDurationMillis(this.mAnimatorDurationMillis);
        this.mAnimatorAdapter = new AnimatorAdapter((BaseAdapter) listAdapter);
        this.mAnimatorAdapter.setCellAnimatorController(cellAnimatorController);
        this.mAnimatorAdapter.setAnimatorEnable(this.mIsAnimatorEnable);
        return this.mAnimatorAdapter;
    }

    public void resetAnimators() {
        if (getHost() != null && this.mAnimatorAdapter != null) {
            this.mAnimatorAdapter.reset();
        }
    }

    public void setAnimatorEnable(boolean z) {
        this.mIsAnimatorEnable = z;
        if (getHost() != null && this.mAnimatorAdapter != null) {
            this.mAnimatorAdapter.setAnimatorEnable(z);
        }
    }

    public boolean isAnimatorEnable() {
        return this.mIsAnimatorEnable;
    }

    public void setCustomAnimatorFactory(CustomAnimatorFactory customAnimatorFactory) {
        this.mCustomAnimatorFactory = customAnimatorFactory;
    }

    public int getInitialDelayMillis() {
        return this.mInitialDelayMillis;
    }

    public void setInitialDelayMillis(int i) {
        this.mInitialDelayMillis = i;
    }

    public int getAnimatorDelayMillis() {
        return this.mAnimatorDelayMillis;
    }

    public void setAnimatorDelayMillis(int i) {
        this.mAnimatorDelayMillis = i;
    }

    public int getAnimatorDurationMillis() {
        return this.mAnimatorDurationMillis;
    }

    public void setAnimatorDurationMillis(int i) {
        this.mAnimatorDurationMillis = i;
    }

    private class AnimatorAdapter extends CellAnimatorAdapter {
        protected AnimatorAdapter(BaseAdapter baseAdapter) {
            super(baseAdapter);
        }

        public Animator[] getAnimators(ViewGroup viewGroup, View view) {
            if (CellAnimatorFeature.this.mCustomAnimatorFactory != null) {
                return CellAnimatorFeature.this.mCustomAnimatorFactory.generateAnimators(viewGroup, view);
            }
            return new Animator[]{ObjectAnimator.ofFloat(view, "translationY", new float[]{400.0f, 0.0f}), ObjectAnimator.ofFloat(view, "rotationX", new float[]{15.0f, 0.0f})};
        }
    }
}
