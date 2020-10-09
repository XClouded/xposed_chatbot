package com.taobao.uikit.feature.features;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.internal.pullrefresh.IViewEdgeJudge;
import com.taobao.uikit.feature.features.internal.pullrefresh.RefreshController;

@Deprecated
public class PullToRefreshFeature extends AbsFeature<ListView> implements TouchEventCallback, IViewEdgeJudge, ScrollCallback {
    private boolean isAuto = false;
    private Context mContext;
    /* access modifiers changed from: private */
    public RefreshController mRefreshController;
    private Scroller mScroller;

    public interface OnPullToRefreshListener {
        void onPullDownToRefresh();

        void onPullUpToRefresh();
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

    public PullToRefreshFeature(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        this.mRefreshController = new RefreshController(this, context, this.mScroller);
        this.mContext = context;
    }

    public void setIsDownRefreshing() {
        if (this.mRefreshController != null) {
            this.mRefreshController.setIsDownRefreshing();
        }
    }

    public void setIsUpRefreshing() {
        if (this.mRefreshController != null) {
            this.mRefreshController.setIsUpRefreshing();
        }
    }

    public void setHost(ListView listView) {
        super.setHost(listView);
        this.mRefreshController.addFooterView();
        this.mRefreshController.addHeaderView();
        if (this.isAuto) {
            addScrollListener(listView);
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        if (this.mRefreshController != null) {
            this.mRefreshController.onTouchEvent(motionEvent);
        }
    }

    public boolean hasArrivedTopEdge() {
        return ((ListView) this.mHost).getFirstVisiblePosition() == 0;
    }

    public boolean hasArrivedBottomEdge() {
        if (Build.VERSION.SDK_INT > 10) {
            if (((ListView) this.mHost).getLastVisiblePosition() != ((ListView) this.mHost).getCount() - 1 || ((ListView) this.mHost).getFirstVisiblePosition() == 0) {
                return false;
            }
            return true;
        } else if (((ListView) this.mHost).getLastVisiblePosition() < ((ListView) this.mHost).getCount() - 2 || ((ListView) this.mHost).getFirstVisiblePosition() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setHeadView(View view) {
        if (this.mHost != null) {
            ((ListView) this.mHost).addHeaderView(view);
        }
    }

    public void setFooterView(View view) {
        if (this.mHost != null) {
            ((ListView) this.mHost).addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (this.mHost != null) {
            ((ListView) this.mHost).removeHeaderView(view);
        }
    }

    public void removeFooterView(View view) {
        if (this.mHost != null) {
            ((ListView) this.mHost).removeFooterView(view);
        }
    }

    public void keepTop() {
        ((ListView) this.mHost).setSelection(0);
    }

    public void keepBottom() {
        ((ListView) this.mHost).setSelection(((ListView) this.mHost).getCount());
    }

    public void trigger() {
        ((ListView) this.mHost).computeScroll();
    }

    public void setRefreshViewColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setRefreshViewColor(i);
        }
    }

    public void setUpRefreshBackgroundColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setUpRefreshBackgroundColor(i);
        }
    }

    public void setDownRefreshBackgroundColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setDownRefreshBackgroundColor(i);
        }
    }

    public void enablePullDownToRefresh(boolean z) {
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageResource(R.drawable.uik_list_logo);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        enablePullDownToRefresh(z, imageView);
    }

    public void enablePullUpToRefresh(boolean z) {
        enablePullUpToRefresh(z, (View) null);
    }

    public void enablePullDownToRefresh(boolean z, View view) {
        enablePullDownToRefresh(z, R.string.uik_refresh_arrow, view);
    }

    public void enablePullUpToRefresh(boolean z, View view) {
        enablePullUpToRefresh(z, R.string.uik_refresh_arrow, view);
    }

    public void enablePullDownToRefresh(boolean z, int i, View view) {
        if (z) {
            this.mRefreshController.enablePullDownRefresh(true, i, view);
        } else {
            this.mRefreshController.enablePullDownRefresh(false, 0, (View) null);
        }
    }

    public void enablePullUpToRefresh(boolean z, int i, View view) {
        if (this.mRefreshController != null) {
            if (z) {
                this.mRefreshController.enablePullUpRefresh(true, i, view);
            } else {
                this.mRefreshController.enablePullUpRefresh(false, 0, (View) null);
            }
        }
    }

    public void enablePullDownToRefresh(boolean z, View view, boolean z2) {
        if (z) {
            this.mRefreshController.enablePullDownRefresh(true, R.string.uik_refresh_arrow, view, z2);
        } else {
            this.mRefreshController.enablePullDownRefresh(false, 0, (View) null);
        }
    }

    @Deprecated
    public void enablePullDownToRefresh(boolean z, int i, int i2, View view) {
        enablePullDownToRefresh(z, view);
    }

    @Deprecated
    public void enablePullUpToRefresh(boolean z, int i, int i2, View view) {
        enablePullUpToRefresh(z, view);
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (this.mRefreshController != null) {
            this.mRefreshController.isHeadViewHeightContainImage(z);
        }
    }

    public void setPullUpToRefreshAuto(boolean z) {
        this.mRefreshController.setPullUpRefreshAuto(z);
        this.isAuto = z;
        if (getHost() != null && z) {
            addScrollListener((ListView) getHost());
        }
    }

    private void addScrollListener(ListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (PullToRefreshFeature.this.mRefreshController.isScrollStop() && PullToRefreshFeature.this.mRefreshController.getState() == 3 && PullToRefreshFeature.this.hasArrivedBottomEdge()) {
                    PullToRefreshFeature.this.mRefreshController.autoLoadingData();
                }
            }
        });
    }

    public void setPullDownRefreshTips(String[] strArr) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setDownRefreshTips(strArr);
        }
    }

    public void setPullUpRefreshTips(String[] strArr) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setUpRefreshTips(strArr);
        }
    }

    public void setOnPullToRefreshListener(OnPullToRefreshListener onPullToRefreshListener) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setOnRefreshListener(onPullToRefreshListener);
        }
    }

    public void onPullRefreshComplete() {
        if (this.mRefreshController != null) {
            this.mRefreshController.onRefreshComplete();
        }
    }

    public int getPullDownDistance() {
        if (this.mRefreshController != null) {
            return this.mRefreshController.getPullDownDistance();
        }
        return -1;
    }

    public void beforeComputeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            if (this.mRefreshController != null) {
                this.mRefreshController.onScrollerStateChanged(this.mScroller.getCurrY(), true);
            }
            ((ListView) this.mHost).invalidate();
        } else if (this.mRefreshController != null && this.mScroller != null) {
            this.mRefreshController.onScrollerStateChanged(this.mScroller.getCurrY(), false);
        }
    }

    public void setDownRefreshFinish(boolean z) {
        this.mRefreshController.setDownRefreshFinish(z);
    }

    public void setUpRefreshFinish(boolean z) {
        this.mRefreshController.setUpRefreshFinish(z);
    }

    public boolean isScrollStop() {
        return this.mRefreshController != null && this.mRefreshController.isScrollStop();
    }

    public int getPullDirection() {
        if (this.mRefreshController != null) {
            return this.mRefreshController.getPullDirection();
        }
        return -1;
    }
}
