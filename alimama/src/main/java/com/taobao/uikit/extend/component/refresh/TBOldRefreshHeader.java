package com.taobao.uikit.extend.component.refresh;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.refresh.TBRefreshHeader;
import com.taobao.uikit.feature.features.internal.pullrefresh.CustomProgressBar;
import com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadView;

public class TBOldRefreshHeader extends TBRefreshHeader {
    private static Typeface sIconfont;
    private static int sReference;
    private TextView mArrowTextView;
    private String[] mDefaultRefreshTips = {getContext().getString(R.string.uik_pull_to_refresh), getContext().getString(R.string.uik_release_to_refresh), getContext().getString(R.string.uik_refreshing), getContext().getString(R.string.uik_refresh_finished)};
    private ObjectAnimator mFadeAnimationSet;
    private RefreshHeadView mRefreshHeadView;
    private FrameLayout mRefreshHeaderView;
    private CustomProgressBar mRefreshProgressView;
    private TextView mRefreshTipView;
    private String[] mRefreshTips = {getContext().getString(R.string.uik_pull_to_refresh), getContext().getString(R.string.uik_release_to_refresh), getContext().getString(R.string.uik_refreshing), getContext().getString(R.string.uik_refresh_finished)};
    private View mSecondFloorView;

    public void setRefreshAnimation(String[] strArr, @Nullable String str) {
    }

    public TBOldRefreshHeader(Context context) {
        super(context);
        this.mRefreshHeaderView = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.mRefreshHeaderView.setId(R.id.uik_refresh_header);
        addView(this.mRefreshHeaderView, layoutParams);
        this.mSecondFloorView = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        this.mSecondFloorView.setId(R.id.uik_refresh_header_second_floor);
        this.mRefreshHeaderView.addView(this.mSecondFloorView, layoutParams2);
        setBackgroundResource(R.color.uik_refresh_head_bg);
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-2, -2);
        layoutParams3.gravity = 81;
        this.mRefreshHeadView = new RefreshHeadView(context, R.string.uik_refresh_arrow, (View) null, false);
        this.mRefreshHeaderView.addView(this.mRefreshHeadView, layoutParams3);
        this.mRefreshTipView = this.mRefreshHeadView.getRefreshStateText();
        this.mRefreshProgressView = this.mRefreshHeadView.getProgressbar();
        this.mArrowTextView = this.mRefreshHeadView.getArrow();
        changeToState(TBRefreshHeader.RefreshState.NONE);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mRefreshProgressView.setPullDownDistance(getMeasuredHeight());
    }

    public void setProgress(float f) {
        if (this.mState == TBRefreshHeader.RefreshState.PULL_TO_REFRESH) {
            if (getMeasuredHeight() != 0) {
                this.mRefreshProgressView.changeProgressBarState((int) (((float) getMeasuredHeight()) * f));
            }
            changeHeaderAlpha(f);
        }
    }

    public void changeToState(TBRefreshHeader.RefreshState refreshState) {
        if (this.mRefreshProgressView != null && this.mState != refreshState) {
            if (this.mPullRefreshListener != null) {
                this.mPullRefreshListener.onRefreshStateChanged(this.mState, refreshState);
            }
            this.mState = refreshState;
            switch (this.mState) {
                case NONE:
                    this.mRefreshProgressView.stopLoadingAnimation();
                    this.mRefreshTipView.setText(this.mRefreshTips == null ? this.mDefaultRefreshTips[3] : this.mRefreshTips[3]);
                    this.mRefreshProgressView.stopLoadingAnimation();
                    changeHeaderAlpha(1.0f);
                    return;
                case PULL_TO_REFRESH:
                    showArrow();
                    this.mRefreshTipView.setVisibility(0);
                    this.mRefreshTipView.setText(this.mRefreshTips == null ? this.mDefaultRefreshTips[0] : this.mRefreshTips[0]);
                    this.mRefreshHeadView.setVisibility(0);
                    return;
                case RELEASE_TO_REFRESH:
                    startArrowAnim();
                    this.mRefreshTipView.setText(this.mRefreshTips == null ? this.mDefaultRefreshTips[1] : this.mRefreshTips[1]);
                    this.mRefreshHeadView.setVisibility(0);
                    return;
                case REFRESHING:
                    this.mRefreshProgressView.startLoadingAnimaton();
                    this.mRefreshProgressView.setVisibility(0);
                    this.mArrowTextView.setVisibility(4);
                    this.mRefreshTipView.setText(this.mRefreshTips == null ? this.mDefaultRefreshTips[2] : this.mRefreshTips[2]);
                    this.mRefreshHeadView.setVisibility(0);
                    return;
                case PREPARE_TO_SECOND_FLOOR:
                    this.mRefreshHeadView.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    }

    public View getRefreshView() {
        return this.mRefreshHeadView;
    }

    public View getSecondFloorView() {
        return this.mSecondFloorView;
    }

    public void setSecondFloorView(View view) {
        FrameLayout.LayoutParams layoutParams;
        FrameLayout.LayoutParams layoutParams2;
        if (this.mSecondFloorView != null) {
            if (this.mRefreshHeaderView != null) {
                if (view.getLayoutParams() == null) {
                    layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
                } else {
                    layoutParams2 = new FrameLayout.LayoutParams(view.getLayoutParams());
                }
                layoutParams2.gravity = 80;
                this.mRefreshHeaderView.removeView(this.mSecondFloorView);
                this.mRefreshHeaderView.addView(view, 0, layoutParams2);
                this.mSecondFloorView = view;
                this.mSecondFloorView.setId(R.id.uik_refresh_header_second_floor);
            }
        } else if (this.mRefreshHeaderView != null) {
            if (view.getLayoutParams() == null) {
                layoutParams = new FrameLayout.LayoutParams(-2, -2);
            } else {
                layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
            }
            layoutParams.gravity = 80;
            this.mRefreshHeaderView.addView(view, 0, layoutParams);
            this.mSecondFloorView = view;
            this.mSecondFloorView.setId(R.id.uik_refresh_header_second_floor);
        }
    }

    public void setRefreshTips(String[] strArr) {
        if (strArr == null || strArr.length != 4) {
            this.mRefreshTips = null;
        }
        this.mRefreshTips = strArr;
    }

    public void setRefreshTipColor(@ColorInt int i) {
        if (this.mRefreshTipView != null) {
            this.mRefreshTipView.setTextColor(i);
        }
    }

    public void changeHeaderAlpha(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        this.mRefreshHeadView.setAlpha(f);
    }

    private void startArrowAnim() {
        if (this.mFadeAnimationSet == null) {
            PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.0f});
            PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.0f});
            PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, 0.0f});
            this.mFadeAnimationSet = ObjectAnimator.ofPropertyValuesHolder(this.mArrowTextView, new PropertyValuesHolder[]{ofFloat, ofFloat2, ofFloat3});
            this.mFadeAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            this.mFadeAnimationSet.setDuration(200);
        }
        this.mFadeAnimationSet.start();
    }

    private void showArrow() {
        if (this.mFadeAnimationSet != null) {
            this.mFadeAnimationSet.cancel();
        }
        this.mArrowTextView.setScaleX(1.0f);
        this.mArrowTextView.setScaleY(1.0f);
        this.mArrowTextView.setAlpha(1.0f);
        this.mArrowTextView.setVisibility(0);
    }
}
