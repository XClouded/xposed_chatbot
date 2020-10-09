package com.taobao.uikit.feature.features;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.internal.pullrefresh.IViewEdgeJudge;
import com.taobao.uikit.feature.features.internal.pullrefresh.RefreshControllerExt;

@Deprecated
public class PullToRefreshFeatureExt extends AbsFeature<ScrollView> implements TouchEventCallback, IViewEdgeJudge, ScrollCallback {
    private Context mContext;
    private RefreshControllerExt mRefreshController;
    private Scroller mScroller = null;

    public interface OnPullToRefreshListenerExt {
        void onPullDownToRefresh();

        void onReadyToJump(boolean z, float f);

        void onRefreshStateChanged(int i, boolean z);
    }

    public void afterComputeScroll() {
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    public boolean hasArrivedBottomEdge() {
        return false;
    }

    public void keepBottom() {
    }

    public void removeFooterView(View view) {
    }

    public void setFooterView(View view) {
    }

    public PullToRefreshFeatureExt(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        this.mContext = context;
        this.mRefreshController = new RefreshControllerExt(this.mContext, this.mScroller, this);
    }

    public void setHost(ScrollView scrollView) {
        super.setHost(scrollView);
        this.mRefreshController.addHeaderView();
    }

    public void enablePullDownToRefresh(boolean z) {
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageResource(R.drawable.uik_list_logo);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        enablePullDownToRefresh(z, imageView);
    }

    public void enablePullDownToRefresh(boolean z, View view) {
        enablePullDownToRefresh(z, R.string.uik_refresh_arrow, view);
    }

    public void enablePullDownToRefresh(boolean z, int i, View view) {
        if (z) {
            this.mRefreshController.enablePullDownRefresh(true, i, view);
        } else {
            this.mRefreshController.enablePullDownRefresh(false, 0, (View) null);
        }
    }

    public void beforeComputeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            if (this.mRefreshController != null) {
                this.mRefreshController.onScrollerStateChanged(this.mScroller.getCurrY(), true);
            }
            ((ScrollView) this.mHost).invalidate();
        } else if (this.mRefreshController != null && this.mScroller != null) {
            this.mRefreshController.onScrollerStateChanged(this.mScroller.getCurrY(), false);
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        if (this.mRefreshController != null) {
            this.mRefreshController.onTouchEvent(motionEvent);
        }
    }

    public boolean hasArrivedTopEdge() {
        return ((ScrollView) this.mHost).getScrollY() <= 0;
    }

    public void setHeadView(View view) {
        if (this.mHost != null) {
            ((ScrollView) this.mHost).setOverScrollMode(2);
            view.setId(R.id.uik_refresh_header);
            View childAt = ((ScrollView) this.mHost).getChildAt(0);
            if (!(childAt instanceof ViewGroup)) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                ((ScrollView) this.mHost).removeViewAt(0);
                LinearLayout linearLayout = new LinearLayout(((ScrollView) this.mHost).getContext());
                linearLayout.setOrientation(1);
                linearLayout.setId(R.id.uik_refresh_layout);
                linearLayout.addView(view);
                linearLayout.addView(childAt);
                ((ScrollView) this.mHost).addView(linearLayout, layoutParams);
            } else if (childAt instanceof LinearLayout) {
                LinearLayout linearLayout2 = (LinearLayout) childAt;
                if (linearLayout2.getOrientation() == 1) {
                    linearLayout2.addView(view, 0);
                }
            }
        }
    }

    public void setHeadViewBackground(Drawable drawable) {
        View findViewById;
        if (this.mHost != null && (findViewById = ((ScrollView) this.mHost).findViewById(R.id.uik_refresh_header)) != null) {
            findViewById.setBackgroundDrawable(drawable);
        }
    }

    public void setHeadViewBackgroundColor(int i) {
        View findViewById;
        if (this.mHost != null && (findViewById = ((ScrollView) this.mHost).findViewById(R.id.uik_refresh_header)) != null) {
            findViewById.setBackgroundColor(i);
        }
    }

    public void removeHeaderView(View view) {
        if (this.mHost != null) {
            LinearLayout linearLayout = (LinearLayout) ((ScrollView) this.mHost).findViewById(R.id.uik_refresh_layout);
            View findViewById = ((ScrollView) this.mHost).findViewById(R.id.uik_refresh_header);
            if (linearLayout != null) {
                View childAt = linearLayout.getChildAt(1);
                ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
                ((ScrollView) this.mHost).removeView(linearLayout);
                ((ScrollView) this.mHost).addView(childAt, layoutParams);
            } else if (findViewById != null) {
                ((ScrollView) this.mHost).removeView(findViewById);
            }
        }
    }

    public void keepTop() {
        ((ScrollView) this.mHost).setScrollY(0);
    }

    public void trigger() {
        ((ScrollView) this.mHost).computeScroll();
    }

    public void setPullDownRefreshTips(String[] strArr) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setDownRefreshTips(strArr);
        }
    }

    public void setDownRefreshFinish(boolean z) {
        this.mRefreshController.setDownRefreshFinish(z);
    }

    public int getPullDownDistance() {
        if (this.mRefreshController != null) {
            return this.mRefreshController.getPullDownDistance();
        }
        return -1;
    }

    public void setOnPullToRefreshListener(OnPullToRefreshListenerExt onPullToRefreshListenerExt) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setOnRefreshListener(onPullToRefreshListenerExt);
        }
    }

    public void onPullRefreshComplete() {
        if (this.mRefreshController != null) {
            this.mRefreshController.onRefreshComplete();
        }
    }

    public void setRefreshViewColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setRefreshViewColor(i);
        }
    }

    public void enableJump(boolean z) {
        if (this.mRefreshController != null) {
            this.mRefreshController.enableJump(z);
        }
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (this.mRefreshController != null) {
            this.mRefreshController.isHeadViewHeightContainImage(z);
        }
    }

    public void setDistanceToJump(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.mDistanceToJump = i;
        }
    }

    public View getRefreshView() {
        if (this.mRefreshController != null) {
            return this.mRefreshController.getRefreshView();
        }
        return null;
    }

    public void reset() {
        if (this.mRefreshController != null) {
            this.mRefreshController.reset();
        }
    }
}
