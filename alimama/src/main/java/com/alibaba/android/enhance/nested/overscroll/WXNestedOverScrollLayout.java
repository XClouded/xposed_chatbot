package com.alibaba.android.enhance.nested.overscroll;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import java.util.ArrayList;
import java.util.List;

public class WXNestedOverScrollLayout extends FrameLayout implements NestedScrollingChild2, NestedScrollingParent2 {
    private final NestedScrollingChildHelper childHelper;
    private int lastDy;
    private List<OnScrollChangeListener> listeners;
    private ObjectAnimator mCurrAnim;
    @Nullable
    private WXNestedOverScrollHelper overScrollHelper;
    private final NestedScrollingParentHelper parentHelper;
    private ScrollState scrollState;

    public interface OnScrollChangeListener {
        void onScrollChange(View view, int i, int i2, int i3, int i4);
    }

    private enum ScrollState {
        IDLE,
        SCROLLING,
        STOPPED
    }

    public WXNestedOverScrollLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public WXNestedOverScrollLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WXNestedOverScrollLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.scrollState = ScrollState.IDLE;
        this.mCurrAnim = null;
        this.listeners = new ArrayList();
        this.lastDy = 0;
        this.parentHelper = new NestedScrollingParentHelper(this);
        this.childHelper = new NestedScrollingChildHelper(this);
        this.childHelper.setNestedScrollingEnabled(true);
    }

    public void setOverScrollHelper(WXNestedOverScrollHelper wXNestedOverScrollHelper) {
        this.overScrollHelper = wXNestedOverScrollHelper;
    }

    public void addOnScrollChangeListener(@NonNull OnScrollChangeListener onScrollChangeListener) {
        if (!this.listeners.contains(onScrollChangeListener)) {
            this.listeners.add(onScrollChangeListener);
        }
    }

    public void removeOnScrollChangeListener(@NonNull OnScrollChangeListener onScrollChangeListener) {
        if (this.listeners.contains(onScrollChangeListener)) {
            this.listeners.remove(onScrollChangeListener);
        }
    }

    public int getNestedScrollAxes() {
        return this.parentHelper.getNestedScrollAxes();
    }

    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view2, int i, int i2) {
        return startNestedScroll(i, i2);
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        return startNestedScroll(i, 0);
    }

    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view2, int i, int i2) {
        this.parentHelper.onNestedScrollAccepted(view, view2, i, i2);
        this.scrollState = ScrollState.SCROLLING;
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        onNestedScrollAccepted(view, view2, i, 0);
    }

    public void onStopNestedScroll(@NonNull View view, int i) {
        this.scrollState = ScrollState.STOPPED;
        this.parentHelper.onStopNestedScroll(view, i);
        stopNestedScroll(i);
        if (this.overScrollHelper != null && !this.overScrollHelper.isOverScrollDisabled()) {
            resetPullDown();
        }
    }

    public void onStopNestedScroll(View view) {
        onStopNestedScroll(view, 0);
    }

    private void resetPullDown() {
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "ScrollY", new int[]{getScrollY(), 0});
        ofInt.setInterpolator(new DecelerateInterpolator());
        this.mCurrAnim = ofInt;
        ofInt.start();
    }

    public void onNestedPreScroll(@NonNull View view, int i, int i2, @Nullable int[] iArr, int i3) {
        if (this.overScrollHelper == null || this.overScrollHelper.isOverScrollDisabled()) {
            dispatchNestedPreScroll(i, i2, iArr, (int[]) null, i3);
            return;
        }
        if (this.lastDy * i2 < 0) {
            dispatchNestedPreScroll(i, i2, iArr, (int[]) null, i3);
        } else if (this.scrollState != ScrollState.SCROLLING || i2 <= 0 || getScrollY() >= 0) {
            dispatchNestedPreScroll(i, i2, iArr, (int[]) null, i3);
        } else {
            scrollWithDamping(i2);
            if (iArr != null) {
                iArr[0] = i;
                iArr[1] = i2;
            }
        }
        this.lastDy = i2;
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        onNestedPreScroll(view, i, i2, iArr, 0);
    }

    public void onNestedScroll(@NonNull View view, int i, int i2, int i3, int i4, int i5) {
        if (this.overScrollHelper == null || this.overScrollHelper.isOverScrollDisabled()) {
            dispatchNestedScroll(i, i2, i3, i4, (int[]) null, i5);
        } else if (this.scrollState != ScrollState.SCROLLING || i2 != 0) {
            dispatchNestedScroll(i, i2, i3, i4, (int[]) null, i5);
        } else if (i4 < 0) {
            scrollWithDamping(i4);
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0);
    }

    private void scrollWithDamping(int i) {
        scrollTo(0, Math.min(0, getScrollY() + calculateDistanceY(this, i)));
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        return dispatchNestedPreFling(f, f2);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mCurrAnim != null) {
            this.mCurrAnim.cancel();
            this.mCurrAnim = null;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean startNestedScroll(int i, int i2) {
        return this.childHelper.startNestedScroll(i, i2);
    }

    public void stopNestedScroll(int i) {
        this.childHelper.stopNestedScroll(i);
    }

    public boolean hasNestedScrollingParent(int i) {
        return this.childHelper.hasNestedScrollingParent(i);
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5) {
        return this.childHelper.dispatchNestedScroll(i, i2, i3, i4, iArr, i5);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
        return this.childHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.childHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.childHelper.dispatchNestedPreFling(f, f2);
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.childHelper.setNestedScrollingEnabled(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.childHelper.isNestedScrollingEnabled();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if (this.overScrollHelper != null && !this.overScrollHelper.isOverScrollDisabled()) {
            for (OnScrollChangeListener onScrollChange : this.listeners) {
                onScrollChange.onScrollChange(this, i, i, i3, i4);
            }
        }
    }

    private int calculateDistanceY(View view, int i) {
        int measuredHeight = view.getMeasuredHeight();
        double abs = (double) (((float) measuredHeight) - Math.abs(view.getY()));
        Double.isNaN(abs);
        double d = (double) measuredHeight;
        Double.isNaN(d);
        double d2 = ((abs / 1.0d) / d) * 0.4000000059604645d;
        if (d2 <= 0.01d) {
            d2 = 0.01d;
        }
        double d3 = (double) i;
        Double.isNaN(d3);
        return (int) (d2 * d3);
    }
}
