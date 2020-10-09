package com.taobao.android.dxcontainer.vlayout.layout;

import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;

public abstract class MarginLayoutHelper extends LayoutHelper {
    protected int mMarginBottom;
    protected int mMarginLeft;
    protected int mMarginRight;
    protected int mMarginTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mPaddingTop;

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        return 0;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingRight = i3;
        this.mPaddingTop = i2;
        this.mPaddingBottom = i4;
    }

    public void setMargin(int i, int i2, int i3, int i4) {
        this.mMarginLeft = i;
        this.mMarginTop = i2;
        this.mMarginRight = i3;
        this.mMarginBottom = i4;
    }

    public int computeMarginStart(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = true;
        if (layoutManagerHelper.getOrientation() != 1) {
            z3 = false;
        }
        if (z3) {
            return this.mMarginTop;
        }
        return this.mMarginLeft;
    }

    public int computeMarginEnd(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = true;
        if (layoutManagerHelper.getOrientation() != 1) {
            z3 = false;
        }
        if (z3) {
            return this.mMarginBottom;
        }
        return this.mMarginRight;
    }

    public int computePaddingStart(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = true;
        if (layoutManagerHelper.getOrientation() != 1) {
            z3 = false;
        }
        if (z3) {
            return this.mPaddingTop;
        }
        return this.mPaddingLeft;
    }

    public int computePaddingEnd(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = true;
        if (layoutManagerHelper.getOrientation() != 1) {
            z3 = false;
        }
        if (z3) {
            return this.mPaddingBottom;
        }
        return this.mPaddingRight;
    }

    public int getHorizontalMargin() {
        return this.mMarginLeft + this.mMarginRight;
    }

    public int getVerticalMargin() {
        return this.mMarginTop + this.mMarginBottom;
    }

    public int getHorizontalPadding() {
        return this.mPaddingLeft + this.mPaddingRight;
    }

    public int getVerticalPadding() {
        return this.mPaddingTop + this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        return this.mPaddingLeft;
    }

    public int getPaddingRight() {
        return this.mPaddingRight;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getMarginLeft() {
        return this.mMarginLeft;
    }

    public int getMarginRight() {
        return this.mMarginRight;
    }

    public int getMarginTop() {
        return this.mMarginTop;
    }

    public int getMarginBottom() {
        return this.mMarginBottom;
    }

    public void setPaddingLeft(int i) {
        this.mPaddingLeft = i;
    }

    public void setPaddingRight(int i) {
        this.mPaddingRight = i;
    }

    public void setPaddingTop(int i) {
        this.mPaddingTop = i;
    }

    public void setPaddingBottom(int i) {
        this.mPaddingBottom = i;
    }

    public void setMarginLeft(int i) {
        this.mMarginLeft = i;
    }

    public void setMarginRight(int i) {
        this.mMarginRight = i;
    }

    public void setMarginTop(int i) {
        this.mMarginTop = i;
    }

    public void setMarginBottom(int i) {
        this.mMarginBottom = i;
    }
}
