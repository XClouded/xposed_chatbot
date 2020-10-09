package com.alibaba.android.enhance.nested.nested;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.enhance.nested.nested.WXNestedHeader;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayDeque;

public class WXNestedHeaderHelper {
    private static final int INVALID_POINTER = -1;
    private static final String TAG = "WXNestedHeaderHelper";
    private int mActivePointerId = -1;
    /* access modifiers changed from: private */
    public WXNestedHeader.FlingBehavior mFlingBehavior;
    private Runnable mFlingRunnable;
    OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    public WXNestedHeaderHelper(WXNestedHeader.FlingBehavior flingBehavior) {
        this.mFlingBehavior = flingBehavior;
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 3) {
            switch (actionMasked) {
                case 0:
                    forceStopRecyclerViewScroll(findRecyclerViewRecursively(coordinatorLayout));
                    if (coordinatorLayout.isPointInChildBounds(appBarLayout, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        ensureVelocityTracker();
                        break;
                    }
                    break;
                case 1:
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        fling(coordinatorLayout, appBarLayout, Integer.MIN_VALUE, 0, this.mVelocityTracker.getYVelocity(this.mActivePointerId));
                        break;
                    }
                    break;
            }
        }
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (this.mVelocityTracker == null) {
            return true;
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return true;
    }

    public boolean onStartNestedScroll(AppBarLayout appBarLayout) {
        stopFling(appBarLayout);
        return true;
    }

    private void stopFling(AppBarLayout appBarLayout) {
        if (!(appBarLayout == null || this.mFlingRunnable == null)) {
            appBarLayout.removeCallbacks(this.mFlingRunnable);
        }
        if (this.mScroller != null) {
            this.mScroller.abortAnimation();
        }
    }

    private void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /* access modifiers changed from: private */
    public RecyclerView findRecyclerViewRecursively(@Nullable ViewGroup viewGroup) {
        if (viewGroup == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(viewGroup);
        while (!arrayDeque.isEmpty()) {
            View view = (View) arrayDeque.removeFirst();
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup2 = (ViewGroup) view;
                int childCount = viewGroup2.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(viewGroup2.getChildAt(i));
                }
            }
        }
        return null;
    }

    private void forceStopRecyclerViewScroll(RecyclerView recyclerView) {
        try {
            recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 3, 0.0f, 0.0f, 0));
        } catch (Throwable unused) {
            WXLogUtils.e(TAG, "forceStopRecyclerViewScroll error");
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean fling(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i, int i2, float f) {
        AppBarLayout appBarLayout2 = appBarLayout;
        if (this.mFlingRunnable != null) {
            appBarLayout2.removeCallbacks(this.mFlingRunnable);
            this.mFlingRunnable = null;
        }
        if (f > 0.0f) {
            return false;
        }
        if (this.mScroller == null) {
            this.mScroller = new OverScroller(appBarLayout.getContext());
        }
        this.mScroller.fling(0, this.mFlingBehavior.getTopAndBottomOffset(), 0, Math.round(f), 0, 0, i, i2);
        if (!this.mScroller.computeScrollOffset()) {
            return false;
        }
        CoordinatorLayout coordinatorLayout2 = coordinatorLayout;
        this.mFlingRunnable = new FlingRunnable(coordinatorLayout, appBarLayout2);
        ViewCompat.postOnAnimation(appBarLayout2, this.mFlingRunnable);
        return true;
    }

    private class FlingRunnable implements Runnable {
        private int mLastOffset = -1;
        private int mLastScrollerY;
        private final AppBarLayout mLayout;
        private final CoordinatorLayout mParent;
        private RecyclerView mRecyclerView;

        FlingRunnable(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            this.mParent = coordinatorLayout;
            this.mLayout = appBarLayout;
        }

        public void run() {
            if (this.mLayout != null && WXNestedHeaderHelper.this.mScroller != null) {
                int currY = WXNestedHeaderHelper.this.mScroller.getCurrY();
                int i = this.mLastScrollerY != 0 ? currY - this.mLastScrollerY : 0;
                if (WXNestedHeaderHelper.this.mScroller.computeScrollOffset()) {
                    if (WXNestedHeaderHelper.this.mFlingBehavior.getTopAndBottomOffset() == this.mLastOffset) {
                        if (this.mRecyclerView == null) {
                            this.mRecyclerView = WXNestedHeaderHelper.this.findRecyclerViewRecursively(this.mParent);
                        }
                        if (this.mRecyclerView != null) {
                            this.mRecyclerView.scrollBy(0, -i);
                        }
                    }
                    this.mLastOffset = WXNestedHeaderHelper.this.mFlingBehavior.getTopAndBottomOffset();
                    ViewCompat.postOnAnimation(this.mLayout, this);
                    this.mLastScrollerY = currY;
                    return;
                }
                this.mLastScrollerY = 0;
                this.mLastOffset = -1;
            }
        }
    }
}
