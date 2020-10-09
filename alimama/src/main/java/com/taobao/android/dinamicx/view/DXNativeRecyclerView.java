package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DXNativeRecyclerView extends RecyclerView {
    private CLipRadiusHandler cLipRadiusHandler;
    private int contentHorizontalLength;
    private int contentOffsetX;
    private int contentOffsetY;
    private int contentVerticalLength;
    private int mScrolledX;
    private int mScrolledY;
    private boolean needScrollAfterLayout;

    public boolean isSlider() {
        return false;
    }

    public DXNativeRecyclerView(Context context) {
        super(context);
    }

    public DXNativeRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DXNativeRecyclerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setClipRadiusHandler(CLipRadiusHandler cLipRadiusHandler2) {
        this.cLipRadiusHandler = cLipRadiusHandler2;
    }

    public CLipRadiusHandler getCLipRadiusHandler() {
        return this.cLipRadiusHandler;
    }

    public void dispatchDraw(Canvas canvas) {
        if (this.cLipRadiusHandler == null) {
            super.dispatchDraw(canvas);
        } else if (this.cLipRadiusHandler.isUseClipOutLine()) {
            super.dispatchDraw(canvas);
        } else {
            this.cLipRadiusHandler.beforeDispatchDraw(this, canvas);
            super.dispatchDraw(canvas);
            this.cLipRadiusHandler.afterDispatchDraw(this, canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (!isSlider() && this.needScrollAfterLayout) {
            scrollBy(this.contentOffsetX, this.contentOffsetY);
            this.contentOffsetX = 0;
            this.contentOffsetY = 0;
            this.needScrollAfterLayout = false;
        }
    }

    public void onScrolled(int i, int i2) {
        super.onScrolled(i, i2);
        this.mScrolledX += i;
        this.mScrolledY += i2;
    }

    public int getScrolledX() {
        return this.mScrolledX;
    }

    public int getScrolledY() {
        return this.mScrolledY;
    }

    public void needScrollAfterLayout(int i, int i2, int i3, int i4) {
        this.needScrollAfterLayout = true;
        if (i3 < this.contentHorizontalLength) {
            this.contentOffsetX = i;
            this.mScrolledX = 0;
            scrollToPosition(0);
        } else {
            this.contentOffsetX = i - this.mScrolledX;
        }
        if (i4 < this.contentVerticalLength) {
            this.contentOffsetY = i2;
            this.mScrolledY = 0;
            scrollToPosition(0);
        } else {
            this.contentOffsetY = i2 - this.mScrolledY;
        }
        this.contentHorizontalLength = i3;
        this.contentVerticalLength = i4;
    }

    public void setContentHorizontalLength(int i) {
        this.contentHorizontalLength = i;
    }

    public void setContentVerticalLength(int i) {
        this.contentVerticalLength = i;
    }
}
