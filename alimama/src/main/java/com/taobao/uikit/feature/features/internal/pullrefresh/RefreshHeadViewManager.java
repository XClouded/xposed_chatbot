package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import com.taobao.uikit.R;
import com.taobao.uikit.utils.UIKITLog;

@Deprecated
public class RefreshHeadViewManager {
    private static final String TAG = "RefreshHeadViewManager";
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_FOOTER_HORIZONTAL = 4;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_HEADER_HORIZONTAL = 3;
    private TextView mArrowTextView;
    private ObjectAnimator mFadeAnimationSet;
    private int mHeadContentHeight;
    private int mHeadContentWidth;
    private RefreshHeadView mHeadView;
    private boolean mIsFinish;
    private int mLogoImageHeight;
    private CustomProgressBar mProgressBar;
    private String[] mTipArray;
    private TextView mTipsTextView;
    private int mType;

    public void setUpdatedTextView(String str) {
    }

    public RefreshHeadViewManager(Context context, Drawable drawable, View view, View view2, int i) {
        this(context, R.string.uik_refresh_arrow, view2, true, i);
    }

    public RefreshHeadViewManager(Context context, int i, View view, boolean z, int i2) {
        this.mIsFinish = false;
        this.mLogoImageHeight = 0;
        this.mHeadView = new RefreshHeadView(context, i, view, z);
        this.mArrowTextView = this.mHeadView.getArrow();
        this.mProgressBar = this.mHeadView.getProgressbar();
        this.mTipsTextView = this.mHeadView.getRefreshStateText();
        measureView(this.mHeadView);
        this.mHeadContentHeight = this.mHeadView.getMeasuredHeight();
        this.mHeadContentWidth = this.mHeadView.getMeasuredWidth();
        if (i2 == 1 || i2 == 2) {
            this.mHeadView.setPullDownDistance(this.mHeadContentHeight);
        } else {
            this.mHeadView.setPullDownDistance(this.mHeadContentWidth);
        }
        this.mType = i2;
        if (i2 == 1) {
            this.mHeadView.setPadding(0, this.mHeadContentHeight * -1, 0, 0);
        } else if (i2 == 2) {
            this.mHeadView.setPadding(0, 0, 0, this.mHeadContentHeight * -1);
        } else if (i2 == 3) {
            this.mHeadView.setPadding(this.mHeadContentWidth * -1, 0, 0, 0);
        } else {
            this.mHeadView.setPadding(0, 0, this.mHeadContentWidth * -1, 0);
        }
        this.mHeadView.invalidate();
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (!z) {
            View childAt = this.mHeadView.getChildAt(1);
            if (childAt != null) {
                this.mLogoImageHeight = childAt.getMeasuredHeight();
            }
            this.mHeadView.setPullDownDistance(this.mLogoImageHeight);
        }
    }

    public void setViewPadding(boolean z) {
        if ((this.mType == 2 || this.mType == 4) && z) {
            this.mHeadView.setPadding(0, 0, 0, 0);
        }
    }

    public void setRefreshViewColor(int i) {
        if (this.mHeadView != null) {
            this.mHeadView.setRefreshViewColor(i);
        }
    }

    public void setRefreshBackground(int i) {
        if (this.mHeadView != null) {
            this.mHeadView.setBackgroundColor(i);
        }
    }

    public void setProgressBarInitState(boolean z) {
        if (this.mHeadView != null) {
            this.mHeadView.setProgressBarInitState(z);
        }
    }

    public void setTipArray(String[] strArr) {
        this.mTipArray = strArr;
    }

    public View getView() {
        return this.mHeadView;
    }

    public void measureView(View view) {
        int i;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-1, -2);
        }
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
        int i2 = layoutParams.height;
        if (i2 > 0) {
            i = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
        } else {
            i = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        view.measure(childMeasureSpec, i);
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mHeadView.setPadding(i, i2, i3, i4);
    }

    public int getPaddingTop() {
        return this.mHeadView.getPaddingTop();
    }

    public int getPaddingBottom() {
        return this.mHeadView.getPaddingBottom();
    }

    public int getPaddingLeft() {
        return this.mHeadView.getPaddingLeft();
    }

    public int getPaddingRight() {
        return this.mHeadView.getPaddingRight();
    }

    public int getHeight() {
        return this.mHeadContentHeight;
    }

    public int getWidth() {
        return this.mHeadContentWidth;
    }

    public int getImageHeight() {
        return this.mLogoImageHeight;
    }

    public void setFinish(boolean z) {
        this.mIsFinish = z;
        if (this.mIsFinish) {
            this.mArrowTextView.setVisibility(0);
            this.mTipsTextView.setVisibility(0);
            this.mProgressBar.setVisibility(0);
            this.mProgressBar.stopLoadingAnimation();
            if (this.mTipArray != null && this.mTipArray.length > 3) {
                this.mTipsTextView.setText(this.mTipArray == null ? "加载完成" : this.mTipArray[3]);
                return;
            }
            return;
        }
        this.mArrowTextView.setVisibility(4);
        this.mProgressBar.setVisibility(4);
        this.mTipsTextView.setVisibility(4);
    }

    public void changeHeaderViewByState(int i) {
        if (!this.mIsFinish) {
            if (i != 5) {
                switch (i) {
                    case 0:
                        this.mProgressBar.setVisibility(0);
                        this.mTipsTextView.setVisibility(0);
                        this.mTipsTextView.setText((this.mTipArray == null || this.mTipArray.length < 2) ? "松开刷新" : this.mTipArray[1]);
                        startArrowAnim();
                        UIKITLog.v(TAG, "当前状态，松开刷新", new Object[0]);
                        return;
                    case 1:
                        this.mTipsTextView.setVisibility(0);
                        this.mTipsTextView.setText((this.mTipArray == null || this.mTipArray.length < 1) ? "下拉刷新" : this.mTipArray[0]);
                        showArrow();
                        UIKITLog.v(TAG, "当前状态，下拉刷新", new Object[0]);
                        return;
                    case 2:
                        this.mTipsTextView.setVisibility(0);
                        this.mTipsTextView.setText((this.mTipArray == null || this.mTipArray.length < 3) ? "正在刷新..." : this.mTipArray[2]);
                        this.mProgressBar.setVisibility(0);
                        this.mProgressBar.startLoadingAnimaton();
                        this.mArrowTextView.setVisibility(4);
                        UIKITLog.v(TAG, "当前状态,正在刷新...", new Object[0]);
                        View findViewById = this.mHeadView.findViewById(R.id.uik_refresh_header_view);
                        if (findViewById != null) {
                            findViewById.setAlpha(1.0f);
                            return;
                        }
                        return;
                    case 3:
                        this.mProgressBar.stopLoadingAnimation();
                        this.mTipsTextView.setText((this.mTipArray == null || this.mTipArray.length < 3) ? "数据加载完毕" : this.mTipArray[3]);
                        UIKITLog.v(TAG, "当前状态，done", new Object[0]);
                        View findViewById2 = this.mHeadView.findViewById(R.id.uik_refresh_header_view);
                        if (findViewById2 != null) {
                            findViewById2.setAlpha(1.0f);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else {
                View findViewById3 = this.mHeadView.findViewById(R.id.uik_refresh_header_view);
                if (findViewById3 != null) {
                    findViewById3.setAlpha(0.0f);
                }
            }
        }
    }

    public void changeProgressBarState(int i) {
        this.mProgressBar.changeProgressBarState(i);
    }

    public void changeHeaderAlpha(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        this.mHeadView.findViewById(R.id.uik_refresh_header_view).setAlpha(f);
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

    public View getRefreshView() {
        return this.mHeadView;
    }
}
