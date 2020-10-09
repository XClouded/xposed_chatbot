package com.taobao.uikit.feature.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.internal.pullrefresh.IViewEdgeJudge;
import com.taobao.uikit.feature.features.internal.pullrefresh.RefreshController;
import com.taobao.uikit.feature.view.TRecyclerView;

@Deprecated
public class DragToRefreshFeature extends AbsFeature<RecyclerView> implements TouchEventCallback, IViewEdgeJudge, ScrollCallback {
    private RecyclerView.OnScrollListener mAutoLoadScrollListener;
    private boolean mEnableNegative;
    private boolean mEnablePositive;
    private int[] mIntArray;
    private int[] mIntArray2;
    private boolean mIsAuto = false;
    private int mOrientation = 1;
    /* access modifiers changed from: private */
    public RefreshController mRefreshController;
    private Scroller mScroller;

    public interface OnDragToRefreshListener {
        void onDragNegative();

        void onDragPositive();
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

    public DragToRefreshFeature(Context context, int i) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        this.mRefreshController = new RefreshController(this, context, this.mScroller, i);
        this.mOrientation = i;
    }

    public void setIsPositiveRefreshing() {
        if (this.mRefreshController != null) {
            this.mRefreshController.setIsDownRefreshing();
        }
    }

    public void setIsNegativeRefreshing() {
        if (this.mRefreshController != null) {
            this.mRefreshController.setIsUpRefreshing();
        }
    }

    public void setHost(RecyclerView recyclerView) {
        super.setHost(recyclerView);
        this.mRefreshController.addFooterView();
        this.mRefreshController.addHeaderView();
        recyclerView.setOverScrollMode(2);
        if (this.mIsAuto) {
            addAutoLoadScrollListener(recyclerView);
        }
    }

    private void addAutoLoadScrollListener(RecyclerView recyclerView) {
        if (this.mAutoLoadScrollListener == null) {
            this.mAutoLoadScrollListener = new RecyclerView.OnScrollListener() {
                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    if (!(i == 0 && i2 == 0) && DragToRefreshFeature.this.mRefreshController.isScrollStop() && DragToRefreshFeature.this.mRefreshController.getState() == 3 && DragToRefreshFeature.this.hasArrivedBottomEdgeOffset(DragToRefreshFeature.this.getSpanCount(recyclerView))) {
                        DragToRefreshFeature.this.mRefreshController.autoLoadingData();
                    }
                }
            };
            recyclerView.setOnScrollListener(this.mAutoLoadScrollListener);
        }
    }

    /* access modifiers changed from: private */
    public int getSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        if (this.mRefreshController != null) {
            this.mRefreshController.onTouchEvent(motionEvent);
        }
    }

    private void ensureIntArray(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        if (this.mIntArray == null) {
            this.mIntArray = new int[staggeredGridLayoutManager.getSpanCount()];
        } else if (this.mIntArray.length < staggeredGridLayoutManager.getSpanCount()) {
            this.mIntArray = new int[staggeredGridLayoutManager.getSpanCount()];
        }
        if (this.mIntArray2 == null) {
            this.mIntArray2 = new int[staggeredGridLayoutManager.getSpanCount()];
        } else if (this.mIntArray2.length < staggeredGridLayoutManager.getSpanCount()) {
            this.mIntArray2 = new int[staggeredGridLayoutManager.getSpanCount()];
        }
    }

    public boolean hasArrivedTopEdge() {
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) this.mHost).getLayoutManager();
        int childAdapterPosition = ((RecyclerView) this.mHost).getChildAdapterPosition(((RecyclerView) this.mHost).getChildAt(0));
        if (childAdapterPosition == 0) {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (-1 == findFirstVisibleItemPosition || findFirstVisibleItemPosition < findFirstCompletelyVisibleItemPosition) {
                    return false;
                }
                return true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                ensureIntArray(staggeredGridLayoutManager);
                staggeredGridLayoutManager.findFirstVisibleItemPositions(this.mIntArray);
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(this.mIntArray2);
                int spanCount = staggeredGridLayoutManager.getSpanCount();
                for (int i = 0; i < spanCount; i++) {
                    if (-1 != this.mIntArray[i] && this.mIntArray[i] >= this.mIntArray2[i]) {
                        return true;
                    }
                }
            }
        } else if (-1 == childAdapterPosition) {
            if ((((TRecyclerView) this.mHost).getTotalCount() - (this.mEnablePositive ? 1 : 0)) - (this.mEnableNegative ? 1 : 0) == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean hasArrivedBottomEdge() {
        return hasArrivedBottomEdgeOffset(0);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x005e A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasArrivedBottomEdgeOffset(int r9) {
        /*
            r8 = this;
            android.view.View r0 = r8.mHost
            androidx.recyclerview.widget.RecyclerView r0 = (androidx.recyclerview.widget.RecyclerView) r0
            androidx.recyclerview.widget.RecyclerView$LayoutManager r0 = r0.getLayoutManager()
            boolean r1 = r8.mEnableNegative
            boolean r2 = r0 instanceof androidx.recyclerview.widget.LinearLayoutManager
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x0025
            android.view.View r2 = r8.mHost
            com.taobao.uikit.feature.view.TRecyclerView r2 = (com.taobao.uikit.feature.view.TRecyclerView) r2
            int r2 = r2.getTotalCount()
            int r2 = r2 - r4
            int r2 = r2 - r1
            androidx.recyclerview.widget.LinearLayoutManager r0 = (androidx.recyclerview.widget.LinearLayoutManager) r0
            int r0 = r0.findLastVisibleItemPosition()
            int r0 = r0 + r9
            if (r2 > r0) goto L_0x0055
            r9 = 1
            goto L_0x0056
        L_0x0025:
            boolean r2 = r0 instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager
            if (r2 == 0) goto L_0x0055
            androidx.recyclerview.widget.StaggeredGridLayoutManager r0 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r0
            r8.ensureIntArray(r0)
            int[] r2 = r8.mIntArray
            r0.findLastVisibleItemPositions(r2)
            int r0 = r0.getSpanCount()
            r2 = 0
            r5 = 0
        L_0x0039:
            if (r2 >= r0) goto L_0x0053
            int[] r6 = r8.mIntArray
            r6 = r6[r2]
            r7 = -1
            if (r7 == r6) goto L_0x0050
            android.view.View r7 = r8.mHost
            com.taobao.uikit.feature.view.TRecyclerView r7 = (com.taobao.uikit.feature.view.TRecyclerView) r7
            int r7 = r7.getTotalCount()
            int r7 = r7 - r4
            int r7 = r7 - r1
            int r6 = r6 + r9
            if (r7 > r6) goto L_0x0050
            r5 = 1
        L_0x0050:
            int r2 = r2 + 1
            goto L_0x0039
        L_0x0053:
            r9 = r5
            goto L_0x0056
        L_0x0055:
            r9 = 0
        L_0x0056:
            if (r9 == 0) goto L_0x005f
            boolean r9 = r8.hasArrivedTopEdge()
            if (r9 != 0) goto L_0x005f
            r3 = 1
        L_0x005f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.feature.features.DragToRefreshFeature.hasArrivedBottomEdgeOffset(int):boolean");
    }

    public void setHeadView(View view) {
        if (this.mHost != null) {
            ((TRecyclerView) this.mHost).addHeaderView(view);
        }
    }

    public void setFooterView(View view) {
        if (this.mHost != null) {
            ((TRecyclerView) this.mHost).addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (this.mHost != null) {
            ((TRecyclerView) this.mHost).removeHeaderView(view);
        }
    }

    public void removeFooterView(View view) {
        if (this.mHost != null) {
            ((TRecyclerView) this.mHost).removeFooterView(view);
        }
    }

    public void keepTop() {
        ((RecyclerView) this.mHost).scrollToPosition(0);
    }

    public void keepBottom() {
        RecyclerView.Adapter adapter = ((RecyclerView) this.mHost).getAdapter();
        if (adapter != null) {
            ((RecyclerView) this.mHost).scrollToPosition(adapter.getItemCount());
        } else {
            ((RecyclerView) this.mHost).scrollToPosition(0);
        }
    }

    public void trigger() {
        ((RecyclerView) this.mHost).computeScroll();
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

    public void enablePositiveDrag(boolean z, int i, View view) {
        if (this.mRefreshController != null) {
            this.mEnablePositive = z;
            this.mRefreshController.enablePullDownRefresh(z, i, view);
        }
    }

    public void enablePositiveDrag(boolean z, View view, boolean z2) {
        if (this.mRefreshController != null) {
            this.mEnablePositive = z;
            this.mRefreshController.enablePullDownRefresh(z, R.string.uik_refresh_arrow, view, z2);
        }
    }

    public void enablePositiveDrag(boolean z) {
        enablePositiveDrag(z, R.string.uik_refresh_arrow, (View) null);
    }

    public void enableNegativeDrag(boolean z, int i, View view) {
        if (this.mRefreshController != null) {
            this.mEnableNegative = z;
            this.mRefreshController.enablePullUpRefresh(z, i, view);
        }
    }

    public void enableNegativeDrag(boolean z) {
        enableNegativeDrag(z, R.string.uik_refresh_arrow, (View) null);
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (this.mRefreshController != null) {
            this.mRefreshController.isHeadViewHeightContainImage(z);
        }
    }

    public void setPositiveDragTips(String[] strArr) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setDownRefreshTips(strArr);
        }
    }

    public void setNegativeTips(String[] strArr) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setUpRefreshTips(strArr);
        }
    }

    public void setOnDragToRefreshListener(OnDragToRefreshListener onDragToRefreshListener) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setOnRefreshListener(onDragToRefreshListener);
        }
    }

    public void onDragRefreshComplete() {
        if (this.mRefreshController != null) {
            this.mRefreshController.onRefreshComplete();
            ((RecyclerView) this.mHost).invalidate();
        }
    }

    public int getPositiveDragDistance() {
        if (this.mRefreshController != null) {
            return this.mRefreshController.getPullDownDistance();
        }
        return -1;
    }

    public void setNegativeDragAuto(boolean z) {
        this.mRefreshController.setPullUpRefreshAuto(z);
        this.mIsAuto = z;
        if (getHost() == null) {
            return;
        }
        if (z) {
            addAutoLoadScrollListener((RecyclerView) getHost());
        } else if (this.mAutoLoadScrollListener != null) {
            ((TRecyclerView) getHost()).removeOnScrollListener(this.mAutoLoadScrollListener);
            this.mAutoLoadScrollListener = null;
        }
    }

    public void setPositiveRefreshBackgroundColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setDownRefreshBackgroundColor(i);
        }
    }

    public void setNegativeRefreshBackgroundColor(int i) {
        if (this.mRefreshController != null) {
            this.mRefreshController.setUpRefreshBackgroundColor(i);
        }
    }

    public void beforeComputeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            if (this.mRefreshController != null) {
                this.mRefreshController.onScrollerStateChanged(this.mOrientation == 1 ? this.mScroller.getCurrY() : this.mScroller.getCurrX(), true);
            }
            ((RecyclerView) this.mHost).invalidate();
        } else if (this.mRefreshController != null && this.mScroller != null) {
            this.mRefreshController.onScrollerStateChanged(this.mOrientation == 1 ? this.mScroller.getCurrY() : this.mScroller.getCurrX(), false);
        }
    }

    public void setPositiveRefreshFinish(boolean z) {
        this.mRefreshController.setDownRefreshFinish(z);
    }

    public void setNegativeRefreshFinish(boolean z) {
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
