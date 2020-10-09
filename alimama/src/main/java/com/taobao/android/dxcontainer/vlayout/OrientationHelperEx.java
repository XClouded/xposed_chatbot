package com.taobao.android.dxcontainer.vlayout;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OrientationHelperEx {
    public static final int HORIZONTAL = 0;
    private static final int INVALID_SIZE = Integer.MIN_VALUE;
    public static final int VERTICAL = 1;
    private int mLastTotalSpace;
    protected final ExposeLinearLayoutManagerEx mLayoutManager;

    public abstract int getDecoratedEnd(View view);

    public abstract int getDecoratedMeasurement(View view);

    public abstract int getDecoratedMeasurementInOther(View view);

    public abstract int getDecoratedStart(View view);

    public abstract int getEnd();

    public abstract int getEndAfterPadding();

    public abstract int getEndPadding();

    public abstract int getStartAfterPadding();

    public abstract int getTotalSpace();

    public abstract void offsetChild(View view, int i);

    public abstract void offsetChildren(int i);

    private OrientationHelperEx(ExposeLinearLayoutManagerEx exposeLinearLayoutManagerEx) {
        this.mLastTotalSpace = Integer.MIN_VALUE;
        this.mLayoutManager = exposeLinearLayoutManagerEx;
    }

    public void onLayoutComplete() {
        this.mLastTotalSpace = getTotalSpace();
    }

    public int getTotalSpaceChange() {
        if (Integer.MIN_VALUE == this.mLastTotalSpace) {
            return 0;
        }
        return getTotalSpace() - this.mLastTotalSpace;
    }

    public static OrientationHelperEx createOrientationHelper(ExposeLinearLayoutManagerEx exposeLinearLayoutManagerEx, int i) {
        switch (i) {
            case 0:
                return createHorizontalHelper(exposeLinearLayoutManagerEx);
            case 1:
                return createVerticalHelper(exposeLinearLayoutManagerEx);
            default:
                throw new IllegalArgumentException("invalid orientation");
        }
    }

    public static OrientationHelperEx createHorizontalHelper(ExposeLinearLayoutManagerEx exposeLinearLayoutManagerEx) {
        return new OrientationHelperEx(exposeLinearLayoutManagerEx) {
            public int getEndAfterPadding() {
                return this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingRight();
            }

            public int getEnd() {
                return this.mLayoutManager.getWidth();
            }

            public void offsetChildren(int i) {
                this.mLayoutManager.offsetChildrenHorizontal(i);
            }

            public int getStartAfterPadding() {
                return this.mLayoutManager.getPaddingLeft();
            }

            public int getDecoratedMeasurement(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedMeasuredWidth(view) + layoutParams.leftMargin + layoutParams.rightMargin;
                }
                return this.mLayoutManager.getDecoratedMeasuredWidth(view);
            }

            public int getDecoratedMeasurementInOther(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredHeight(view) + layoutParams.topMargin + layoutParams.bottomMargin;
            }

            public int getDecoratedEnd(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedRight(view) + layoutParams.rightMargin;
                }
                return this.mLayoutManager.getDecoratedRight(view);
            }

            public int getDecoratedStart(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedLeft(view) - layoutParams.leftMargin;
                }
                return this.mLayoutManager.getDecoratedLeft(view);
            }

            public int getTotalSpace() {
                return (this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingLeft()) - this.mLayoutManager.getPaddingRight();
            }

            public void offsetChild(View view, int i) {
                view.offsetLeftAndRight(i);
            }

            public int getEndPadding() {
                return this.mLayoutManager.getPaddingRight();
            }
        };
    }

    public static OrientationHelperEx createVerticalHelper(ExposeLinearLayoutManagerEx exposeLinearLayoutManagerEx) {
        return new OrientationHelperEx(exposeLinearLayoutManagerEx) {
            public int getEndAfterPadding() {
                return this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingBottom();
            }

            public int getEnd() {
                return this.mLayoutManager.getHeight();
            }

            public void offsetChildren(int i) {
                this.mLayoutManager.offsetChildrenVertical(i);
            }

            public int getStartAfterPadding() {
                return this.mLayoutManager.getPaddingTop();
            }

            public int getDecoratedMeasurement(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedMeasuredHeight(view) + layoutParams.topMargin + layoutParams.bottomMargin;
                }
                return this.mLayoutManager.getDecoratedMeasuredHeight(view);
            }

            public int getDecoratedMeasurementInOther(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                return this.mLayoutManager.getDecoratedMeasuredWidth(view) + layoutParams.leftMargin + layoutParams.rightMargin;
            }

            public int getDecoratedEnd(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedBottom(view) + layoutParams.bottomMargin;
                }
                return this.mLayoutManager.getDecoratedBottom(view);
            }

            public int getDecoratedStart(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!this.mLayoutManager.isEnableMarginOverLap()) {
                    return this.mLayoutManager.getDecoratedTop(view) - layoutParams.topMargin;
                }
                return this.mLayoutManager.getDecoratedTop(view);
            }

            public int getTotalSpace() {
                return (this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingTop()) - this.mLayoutManager.getPaddingBottom();
            }

            public void offsetChild(View view, int i) {
                view.offsetTopAndBottom(i);
            }

            public int getEndPadding() {
                return this.mLayoutManager.getPaddingBottom();
            }
        };
    }
}
