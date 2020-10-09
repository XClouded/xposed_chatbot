package com.taobao.android.dxcontainer.vlayout.layout;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class SingleLayoutHelper extends ColumnLayoutHelper {
    private static final String TAG = "SingleLayoutHelper";
    private int mPos = -1;

    public SingleLayoutHelper() {
        setItemCount(1);
    }

    public void setItemCount(int i) {
        if (i > 0) {
            super.setItemCount(1);
        } else {
            super.setItemCount(0);
        }
    }

    public void onRangeChange(int i, int i2) {
        this.mPos = i;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            View next = layoutStateWrapper.next(recycler);
            boolean z = true;
            if (next == null) {
                layoutChunkResult.mFinished = true;
                return;
            }
            layoutManagerHelper.addChildView(layoutStateWrapper, next);
            VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) next.getLayoutParams();
            boolean z2 = layoutManagerHelper.getOrientation() == 1;
            int contentWidth = (((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight()) - getHorizontalMargin()) - getHorizontalPadding();
            int contentHeight = (((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom()) - getVerticalMargin()) - getVerticalPadding();
            if (!Float.isNaN(this.mAspectRatio)) {
                if (z2) {
                    contentHeight = (int) ((((float) contentWidth) / this.mAspectRatio) + 0.5f);
                } else {
                    contentWidth = (int) ((((float) contentHeight) * this.mAspectRatio) + 0.5f);
                }
            }
            if (z2) {
                int childMeasureSpec = layoutManagerHelper.getChildMeasureSpec(contentWidth, Float.isNaN(this.mAspectRatio) ? layoutParams.width : contentWidth, !z2 && Float.isNaN(this.mAspectRatio));
                int i7 = Float.isNaN(layoutParams.mAspectRatio) ? Float.isNaN(this.mAspectRatio) ? layoutParams.height : contentHeight : (int) ((((float) contentWidth) / layoutParams.mAspectRatio) + 0.5f);
                if (!z2 || !Float.isNaN(this.mAspectRatio)) {
                    z = false;
                }
                layoutManagerHelper.measureChildWithMargins(next, childMeasureSpec, layoutManagerHelper.getChildMeasureSpec(contentHeight, i7, z));
            } else {
                int childMeasureSpec2 = layoutManagerHelper.getChildMeasureSpec(contentWidth, Float.isNaN(layoutParams.mAspectRatio) ? Float.isNaN(this.mAspectRatio) ? layoutParams.width : contentWidth : (int) ((((float) contentHeight) * layoutParams.mAspectRatio) + 0.5f), !z2 && Float.isNaN(this.mAspectRatio));
                int i8 = Float.isNaN(this.mAspectRatio) ? layoutParams.height : contentHeight;
                if (!z2 || !Float.isNaN(this.mAspectRatio)) {
                    z = false;
                }
                layoutManagerHelper.measureChildWithMargins(next, childMeasureSpec2, layoutManagerHelper.getChildMeasureSpec(contentHeight, i8, z));
            }
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            layoutChunkResult.mConsumed = mainOrientationHelper.getDecoratedMeasurement(next);
            if (z2) {
                int decoratedMeasurementInOther = contentWidth - mainOrientationHelper.getDecoratedMeasurementInOther(next);
                if (decoratedMeasurementInOther < 0) {
                    decoratedMeasurementInOther = 0;
                }
                int i9 = decoratedMeasurementInOther / 2;
                i4 = this.mMarginLeft + this.mPaddingLeft + layoutManagerHelper.getPaddingLeft() + i9;
                int contentWidth2 = (((layoutManagerHelper.getContentWidth() - this.mMarginRight) - this.mPaddingRight) - layoutManagerHelper.getPaddingRight()) - i9;
                if (layoutStateWrapper.getLayoutDirection() == -1) {
                    i6 = (layoutStateWrapper.getOffset() - this.mMarginBottom) - this.mPaddingBottom;
                    i5 = i6 - layoutChunkResult.mConsumed;
                } else {
                    i5 = this.mPaddingTop + layoutStateWrapper.getOffset() + this.mMarginTop;
                    i6 = layoutChunkResult.mConsumed + i5;
                }
                i = i6;
                i2 = contentWidth2;
                i3 = i5;
            } else {
                int decoratedMeasurementInOther2 = contentHeight - mainOrientationHelper.getDecoratedMeasurementInOther(next);
                if (decoratedMeasurementInOther2 < 0) {
                    decoratedMeasurementInOther2 = 0;
                }
                int i10 = decoratedMeasurementInOther2 / 2;
                int paddingTop = layoutManagerHelper.getPaddingTop() + this.mMarginTop + this.mPaddingTop + i10;
                int contentHeight2 = (((layoutManagerHelper.getContentHeight() - (-this.mMarginBottom)) - this.mPaddingBottom) - layoutManagerHelper.getPaddingBottom()) - i10;
                if (layoutStateWrapper.getLayoutDirection() == -1) {
                    int offset = (layoutStateWrapper.getOffset() - this.mMarginRight) - this.mPaddingRight;
                    i2 = offset;
                    i = contentHeight2;
                    i3 = paddingTop;
                    i4 = offset - layoutChunkResult.mConsumed;
                } else {
                    int offset2 = layoutStateWrapper.getOffset() + this.mMarginLeft + this.mPaddingLeft;
                    i2 = layoutChunkResult.mConsumed + offset2;
                    i = contentHeight2;
                    i3 = paddingTop;
                    i4 = offset2;
                }
            }
            if (z2) {
                layoutChunkResult.mConsumed += getVerticalMargin() + getVerticalPadding();
            } else {
                layoutChunkResult.mConsumed += getHorizontalMargin() + getHorizontalPadding();
            }
            layoutChildWithMargin(next, i4, i3, i2, i, layoutManagerHelper);
        }
    }
}
