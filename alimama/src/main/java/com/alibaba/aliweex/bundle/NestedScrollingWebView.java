package com.alibaba.aliweex.bundle;

import android.content.Context;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import com.uc.webview.export.extension.UCExtension;

public class NestedScrollingWebView extends WVUCWebView implements NestedScrollingChild {
    private NestedScrollingChildHelper childHelper;
    private int lastY;
    private int nestedOffsetY;
    private final int[] scrollConsumed;
    private final int[] scrollOffset;

    public NestedScrollingWebView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NestedScrollingWebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842885);
    }

    public NestedScrollingWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.scrollOffset = new int[2];
        this.scrollConsumed = new int[2];
        this.childHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public boolean coreDispatchTouchEvent(MotionEvent motionEvent) {
        UCExtension uCExtension = getUCExtension();
        if (uCExtension != null) {
            if (uCExtension.ignoreTouchEvent()) {
                requestDisallowInterceptTouchEvent(true);
            } else {
                requestDisallowInterceptTouchEvent(false);
            }
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        int actionMasked = MotionEventCompat.getActionMasked(obtain);
        if (actionMasked == 0) {
            this.nestedOffsetY = 0;
        }
        int y = (int) obtain.getY();
        obtain.offsetLocation(0.0f, (float) this.nestedOffsetY);
        switch (actionMasked) {
            case 0:
                boolean coreDispatchTouchEvent = super.coreDispatchTouchEvent(obtain);
                this.lastY = y;
                startNestedScroll(2);
                return coreDispatchTouchEvent;
            case 1:
            case 3:
                boolean coreDispatchTouchEvent2 = super.coreDispatchTouchEvent(obtain);
                stopNestedScroll();
                return coreDispatchTouchEvent2;
            case 2:
                int i = this.lastY - y;
                if (dispatchNestedPreScroll(0, i, this.scrollConsumed, this.scrollOffset)) {
                    i -= this.scrollConsumed[1];
                    this.lastY = y - this.scrollOffset[1];
                    obtain.offsetLocation(0.0f, (float) (-this.scrollOffset[1]));
                    this.nestedOffsetY += this.scrollOffset[1];
                }
                int i2 = i;
                boolean coreDispatchTouchEvent3 = super.coreDispatchTouchEvent(obtain);
                if (!dispatchNestedScroll(0, this.scrollOffset[1], 0, i2, this.scrollOffset)) {
                    return coreDispatchTouchEvent3;
                }
                obtain.offsetLocation(0.0f, (float) this.scrollOffset[1]);
                this.nestedOffsetY += this.scrollOffset[1];
                return coreDispatchTouchEvent3;
            default:
                return false;
        }
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.childHelper.setNestedScrollingEnabled(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.childHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int i) {
        return this.childHelper.startNestedScroll(i);
    }

    public void stopNestedScroll() {
        this.childHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return this.childHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.childHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.childHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.childHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.childHelper.dispatchNestedPreFling(f, f2);
    }
}
