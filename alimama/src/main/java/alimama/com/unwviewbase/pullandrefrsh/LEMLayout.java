package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class LEMLayout extends FrameLayout {
    private static final String STATE_LEM_STATE = "lem_state";
    private static final String STATE_SUPER = "lem_super";
    private View mContentView;
    private int mEmptyResId;
    private View mEmptyView;
    private int mErrorResId;
    private View mErrorView;
    private LemState mLemState = LemState.getDefault();
    private int mLoadingResId;
    private View mLoadingView;

    public LEMLayout(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public LEMLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public LEMLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LEMLayout);
        if (obtainStyledAttributes.hasValue(R.styleable.LEMLayout_lemLayoutLoading)) {
            this.mLoadingResId = obtainStyledAttributes.getResourceId(R.styleable.LEMLayout_lemLayoutLoading, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LEMLayout_lemLayoutEmpty)) {
            this.mEmptyResId = obtainStyledAttributes.getResourceId(R.styleable.LEMLayout_lemLayoutEmpty, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LEMLayout_lemLayoutError)) {
            this.mErrorResId = obtainStyledAttributes.getResourceId(R.styleable.LEMLayout_lemLayoutError, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LEMLayout_lemLayoutState)) {
            setState(LemState.mapIntToValue(obtainStyledAttributes.getInteger(R.styleable.LEMLayout_lemLayoutState, LemState.getDefault().getIntValue())));
        }
    }

    private void superAddView(View view) {
        if (view != null && view.getParent() == null) {
            super.addView(view, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    /* access modifiers changed from: protected */
    public final void hideView(View view) {
        if (view != null && view.getParent() == this && view.getVisibility() == 0) {
            view.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public final void showView(View view) {
        if (view != null && view.getParent() == this && view.getVisibility() == 8) {
            view.setVisibility(0);
        }
    }

    public final void setState(LemState lemState) {
        this.mLemState = lemState;
        if (lemState == LemState.CONTENT) {
            showContentViewInternal();
        } else if (lemState == LemState.EMPTY) {
            showEmptyViewInternal();
        } else if (lemState == LemState.ERROR) {
            showErrorViewInternal();
        } else if (lemState == LemState.LOADING) {
            showLoadingViewInternal();
        }
    }

    public final void showLoadingView() {
        setState(LemState.LOADING);
    }

    private void showLoadingViewInternal() {
        if (this.mContentView != null) {
            inflateLoading();
            superAddView(this.mLoadingView);
            showLoading();
        }
    }

    private void initAndHideLoading() {
        inflateLoading();
        superAddView(this.mLoadingView);
        hideView(this.mLoadingView);
    }

    private void inflateLoading() {
        if (this.mLoadingView == null && this.mLoadingResId > 0) {
            this.mLoadingView = LayoutInflater.from(getContext()).inflate(this.mLoadingResId, this, false);
        }
    }

    /* access modifiers changed from: protected */
    public void showLoading() {
        hideView(this.mEmptyView);
        hideView(this.mErrorView);
        hideView(this.mContentView);
        showView(this.mLoadingView);
    }

    public final void showEmptyView() {
        setState(LemState.EMPTY);
    }

    private void showEmptyViewInternal() {
        if (this.mContentView != null) {
            inflateEmpty();
            superAddView(this.mEmptyView);
            showEmpty();
        }
    }

    private void initAndHideEmpty() {
        inflateEmpty();
        superAddView(this.mEmptyView);
        hideView(this.mEmptyView);
    }

    private void inflateEmpty() {
        if (this.mEmptyView == null && this.mEmptyResId > 0) {
            this.mEmptyView = LayoutInflater.from(getContext()).inflate(this.mEmptyResId, this, false);
        }
    }

    /* access modifiers changed from: protected */
    public void showEmpty() {
        hideView(this.mLoadingView);
        hideView(this.mErrorView);
        hideView(this.mContentView);
        showView(this.mEmptyView);
    }

    public final void showErrorView() {
        setState(LemState.ERROR);
    }

    private void showErrorViewInternal() {
        if (this.mContentView != null) {
            inflateError();
            superAddView(this.mErrorView);
            showError();
        }
    }

    private void initAndHideError() {
        inflateError();
        superAddView(this.mErrorView);
        hideView(this.mErrorView);
    }

    private void inflateError() {
        if (this.mErrorView == null && this.mErrorResId > 0) {
            this.mErrorView = LayoutInflater.from(getContext()).inflate(this.mErrorResId, this, false);
        }
    }

    /* access modifiers changed from: protected */
    public void showError() {
        hideView(this.mLoadingView);
        hideView(this.mEmptyView);
        hideView(this.mContentView);
        showView(this.mErrorView);
    }

    public final void showContentView() {
        setState(LemState.CONTENT);
    }

    private void showContentViewInternal() {
        showContent();
    }

    /* access modifiers changed from: protected */
    public void showContent() {
        hideView(this.mLoadingView);
        hideView(this.mEmptyView);
        hideView(this.mErrorView);
        showView(this.mContentView);
    }

    public final void setLoadingView(View view) {
        if (!(this.mLoadingView == null || this.mLoadingView == view)) {
            removeView(this.mLoadingView);
        }
        this.mLoadingView = view;
        if (getLemState() == LemState.LOADING) {
            showLoadingViewInternal();
        }
    }

    public final void setEmptyView(View view) {
        if (!(this.mEmptyView == null || this.mEmptyView == view)) {
            removeView(this.mEmptyView);
        }
        this.mEmptyView = view;
        if (getLemState() == LemState.EMPTY) {
            showEmptyViewInternal();
        }
    }

    public final void setErrorView(View view) {
        if (!(this.mErrorView == null || this.mErrorView == view)) {
            removeView(this.mErrorView);
        }
        this.mErrorView = view;
        if (getLemState() == LemState.ERROR) {
            showErrorViewInternal();
        }
    }

    public final void addView(View view) {
        checkChild(view);
        super.addView(view);
    }

    public final void addView(View view, int i) {
        checkChild(view);
        super.addView(view, i);
    }

    public final void addView(View view, ViewGroup.LayoutParams layoutParams) {
        checkChild(view);
        super.addView(view, layoutParams);
    }

    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        checkChild(view);
        super.addView(view, i, layoutParams);
    }

    private void checkChild(View view) {
        if (view != this.mLoadingView && view != this.mEmptyView && view != this.mErrorView) {
            if (getChildCount() <= 0) {
                this.mContentView = view;
                return;
            }
            throw new IllegalStateException("LEMLayout can host only one direct child");
        }
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view != null && view == this.mContentView) {
            initAndHideLoading();
            initAndHideEmpty();
            initAndHideError();
            setState(getLemState());
        }
    }

    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view != null && view == this.mContentView) {
            removeAllViews();
            this.mContentView = null;
        }
    }

    public final LemState getLemState() {
        return this.mLemState;
    }

    public final View getLoadingView() {
        return this.mLoadingView;
    }

    public final View getErrorView() {
        return this.mErrorView;
    }

    public final View getEmptyView() {
        return this.mEmptyView;
    }

    /* access modifiers changed from: protected */
    public final Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(STATE_LEM_STATE, this.mLemState.getIntValue());
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        return bundle;
    }

    /* access modifiers changed from: protected */
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            setState(LemState.mapIntToValue(bundle.getInt(STATE_LEM_STATE, LemState.getDefault().getIntValue())));
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public enum LemState {
        LOADING(0),
        ERROR(1),
        EMPTY(2),
        CONTENT(3);
        
        private int mIntValue;

        public static LemState getDefault() {
            return CONTENT;
        }

        public static boolean AbnormalState(LemState lemState) {
            return lemState != null && lemState.getIntValue() < CONTENT.getIntValue();
        }

        public static LemState mapIntToValue(int i) {
            for (LemState lemState : values()) {
                if (i == lemState.getIntValue()) {
                    return lemState;
                }
            }
            return getDefault();
        }

        private LemState(int i) {
            this.mIntValue = i;
        }

        /* access modifiers changed from: package-private */
        public int getIntValue() {
            return this.mIntValue;
        }
    }
}
