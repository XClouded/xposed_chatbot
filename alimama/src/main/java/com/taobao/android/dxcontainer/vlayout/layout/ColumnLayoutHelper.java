package com.taobao.android.dxcontainer.vlayout.layout;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import java.util.Arrays;

public class ColumnLayoutHelper extends AbstractFullFillLayoutHelper {
    private View[] mEqViews;
    private Rect mTempArea = new Rect();
    private View[] mViews;
    private float[] mWeights = new float[0];

    public void setWeights(float[] fArr) {
        if (fArr != null) {
            this.mWeights = Arrays.copyOf(fArr, fArr.length);
        } else {
            this.mWeights = new float[0];
        }
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            boolean z = true;
            boolean z2 = layoutManagerHelper.getOrientation() == 1;
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            int itemCount = getItemCount();
            if (this.mViews == null || this.mViews.length != itemCount) {
                this.mViews = new View[itemCount];
            }
            if (this.mEqViews == null || this.mEqViews.length != itemCount) {
                this.mEqViews = new View[itemCount];
            } else {
                Arrays.fill(this.mEqViews, (Object) null);
            }
            int allChildren = getAllChildren(this.mViews, recycler, layoutStateWrapper, layoutChunkResult, layoutManagerHelper);
            if (z2) {
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                for (int i7 = 0; i7 < allChildren; i7++) {
                    ViewGroup.LayoutParams layoutParams = this.mViews[i7].getLayoutParams();
                    if (layoutParams instanceof RecyclerView.LayoutParams) {
                        RecyclerView.LayoutParams layoutParams2 = (RecyclerView.LayoutParams) layoutParams;
                        layoutParams2.leftMargin = Math.max(i5, layoutParams2.leftMargin);
                        i4 += layoutParams2.leftMargin;
                        if (i7 != allChildren - 1) {
                            i5 = layoutParams2.rightMargin;
                            layoutParams2.rightMargin = 0;
                        } else {
                            i4 += layoutParams2.rightMargin;
                        }
                        i6 = Math.max(i6, layoutParams2.topMargin + layoutParams2.bottomMargin);
                    }
                }
                int contentWidth = (((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight()) - getHorizontalMargin()) - getHorizontalPadding();
                int i8 = contentWidth - i4;
                int i9 = -1;
                if (!Float.isNaN(this.mAspectRatio)) {
                    i9 = (int) ((((float) contentWidth) / this.mAspectRatio) + 0.5f);
                }
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                int i13 = Integer.MAX_VALUE;
                while (i10 < allChildren) {
                    View view = this.mViews[i10];
                    VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
                    int childMeasureSpec = layoutManagerHelper2.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), i9 > 0 ? i9 : layoutParams3.height, z);
                    if (this.mWeights == null || i10 >= this.mWeights.length || Float.isNaN(this.mWeights[i10]) || this.mWeights[i10] < 0.0f) {
                        this.mEqViews[i12] = view;
                        i12++;
                    } else {
                        int i14 = (int) ((((this.mWeights[i10] * 1.0f) / 100.0f) * ((float) i8)) + 0.5f);
                        if (!Float.isNaN(layoutParams3.mAspectRatio)) {
                            i3 = 1073741824;
                            childMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) ((((float) i14) / layoutParams3.mAspectRatio) + 0.5f), 1073741824);
                        } else {
                            i3 = 1073741824;
                        }
                        layoutManagerHelper2.measureChildWithMargins(view, View.MeasureSpec.makeMeasureSpec(i14, i3), childMeasureSpec);
                        i11 += i14;
                        i13 = Math.min(i13, view.getMeasuredHeight());
                    }
                    i10++;
                    z = true;
                }
                for (int i15 = 0; i15 < i12; i15++) {
                    View view2 = this.mEqViews[i15];
                    VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
                    int i16 = (int) (((((float) (i8 - i11)) * 1.0f) / ((float) i12)) + 0.5f);
                    if (!Float.isNaN(layoutParams4.mAspectRatio)) {
                        i = 1073741824;
                        i2 = View.MeasureSpec.makeMeasureSpec((int) ((((float) i16) / layoutParams4.mAspectRatio) + 0.5f), 1073741824);
                    } else {
                        i2 = layoutManagerHelper2.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), i9 > 0 ? i9 : layoutParams4.height, true);
                        i = 1073741824;
                    }
                    layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i16, i), i2);
                    i13 = Math.min(i13, view2.getMeasuredHeight());
                }
                for (int i17 = 0; i17 < allChildren; i17++) {
                    View view3 = this.mViews[i17];
                    if (view3.getMeasuredHeight() != i13) {
                        layoutManagerHelper2.measureChildWithMargins(view3, View.MeasureSpec.makeMeasureSpec(view3.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i13, 1073741824));
                    }
                }
                int i18 = i13 + i6;
                layoutChunkResult.mConsumed = getVerticalMargin() + i18 + getVerticalPadding();
                calculateRect(i18, this.mTempArea, layoutStateWrapper, layoutManagerHelper2);
                int i19 = this.mTempArea.left;
                int i20 = 0;
                while (i20 < allChildren) {
                    View view4 = this.mViews[i20];
                    int i21 = this.mTempArea.top;
                    int i22 = this.mTempArea.bottom;
                    int decoratedMeasurementInOther = i19 + mainOrientationHelper.getDecoratedMeasurementInOther(view4);
                    layoutChildWithMargin(view4, i19, i21, decoratedMeasurementInOther, i22, layoutManagerHelper);
                    i20++;
                    i19 = decoratedMeasurementInOther;
                }
            }
            Arrays.fill(this.mViews, (Object) null);
            Arrays.fill(this.mEqViews, (Object) null);
        }
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        if (anchorInfoWrapper.layoutFromEnd) {
            anchorInfoWrapper.position = getRange().getUpper().intValue();
        } else {
            anchorInfoWrapper.position = getRange().getLower().intValue();
        }
    }
}
