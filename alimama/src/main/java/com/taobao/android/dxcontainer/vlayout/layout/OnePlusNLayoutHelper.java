package com.taobao.android.dxcontainer.vlayout.layout;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import java.util.Arrays;

public class OnePlusNLayoutHelper extends AbstractFullFillLayoutHelper {
    private static final String TAG = "OnePlusNLayoutHelper";
    private Rect mAreaRect;
    private View[] mChildrenViews;
    private float[] mColWeights;
    private float mRowWeight;

    public OnePlusNLayoutHelper() {
        this.mAreaRect = new Rect();
        this.mColWeights = new float[0];
        this.mRowWeight = Float.NaN;
        setItemCount(0);
    }

    public OnePlusNLayoutHelper(int i) {
        this(i, 0, 0, 0, 0);
    }

    public OnePlusNLayoutHelper(int i, int i2, int i3, int i4, int i5) {
        this.mAreaRect = new Rect();
        this.mColWeights = new float[0];
        this.mRowWeight = Float.NaN;
        setItemCount(i);
    }

    public void onRangeChange(int i, int i2) {
        if (i2 - i > 6) {
            throw new IllegalArgumentException("OnePlusNLayoutHelper only supports maximum 7 children now");
        }
    }

    public void setColWeights(float[] fArr) {
        if (fArr != null) {
            this.mColWeights = Arrays.copyOf(fArr, fArr.length);
        } else {
            this.mColWeights = new float[0];
        }
    }

    public void setRowWeight(float f) {
        this.mRowWeight = f;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        RecyclerView.Recycler recycler2 = recycler;
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper2 = layoutStateWrapper;
        LayoutChunkResult layoutChunkResult2 = layoutChunkResult;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            boolean z = layoutManagerHelper.getOrientation() == 1;
            boolean z2 = layoutStateWrapper.getLayoutDirection() == -1;
            int contentWidth = layoutManagerHelper.getContentWidth();
            int contentHeight = layoutManagerHelper.getContentHeight();
            int paddingLeft = layoutManagerHelper.getPaddingLeft() + layoutManagerHelper.getPaddingRight() + getHorizontalMargin() + getHorizontalPadding();
            int paddingTop = layoutManagerHelper.getPaddingTop() + layoutManagerHelper.getPaddingBottom() + getVerticalMargin() + getVerticalPadding();
            int currentPosition = layoutStateWrapper.getCurrentPosition();
            if (this.hasHeader && currentPosition == getRange().getLower().intValue()) {
                View nextView = nextView(recycler2, layoutStateWrapper2, layoutManagerHelper2, layoutChunkResult2);
                View view = nextView;
                int handleHeader = handleHeader(nextView, layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                if (view != null) {
                    if (z) {
                        if (z2) {
                            i17 = layoutStateWrapper.getOffset();
                            i16 = i17 - handleHeader;
                        } else {
                            i16 = (this.mLayoutWithAnchor ? 0 : this.mMarginTop + this.mPaddingTop) + layoutStateWrapper.getOffset();
                            i17 = i16 + handleHeader;
                        }
                        i13 = layoutManagerHelper.getPaddingLeft() + this.mMarginLeft + this.mPaddingLeft;
                        i10 = i17;
                        i11 = mainOrientationHelper.getDecoratedMeasurementInOther(view) + i13;
                        i12 = i16;
                    } else {
                        if (z2) {
                            i15 = layoutStateWrapper.getOffset();
                            i14 = i15 - handleHeader;
                        } else {
                            i14 = (this.mLayoutWithAnchor ? 0 : this.mMarginLeft + this.mPaddingLeft) + layoutStateWrapper.getOffset();
                            i15 = i14 + handleHeader;
                        }
                        int paddingTop2 = layoutManagerHelper.getPaddingTop() + this.mMarginTop + this.mPaddingTop;
                        i11 = i15;
                        i10 = mainOrientationHelper.getDecoratedMeasurementInOther(view) + paddingTop2;
                        i12 = paddingTop2;
                        i13 = i14;
                    }
                    layoutChildWithMargin(view, i13, i12, i11, i10, layoutManagerHelper);
                }
                layoutChunkResult2.mConsumed = handleHeader;
                handleStateOnResult(layoutChunkResult2, view);
            } else if (!this.hasFooter || currentPosition != getRange().getUpper().intValue()) {
                int itemCount = (getItemCount() - (this.hasHeader ? 1 : 0)) - (this.hasFooter ? 1 : 0);
                if (this.mChildrenViews == null || this.mChildrenViews.length != itemCount) {
                    this.mChildrenViews = new View[itemCount];
                }
                int allChildren = getAllChildren(this.mChildrenViews, recycler, layoutStateWrapper, layoutChunkResult, layoutManagerHelper);
                if (allChildren != 0 && allChildren >= itemCount) {
                    if (itemCount == 1) {
                        i = handleOne(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                    } else if (itemCount == 2) {
                        i = handleTwo(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                    } else if (itemCount == 3) {
                        i = handleThree(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                    } else if (itemCount == 4) {
                        i = handleFour(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                    } else {
                        i = itemCount == 5 ? handleFive(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop) : 0;
                    }
                    layoutChunkResult2.mConsumed = i;
                    Arrays.fill(this.mChildrenViews, (Object) null);
                }
            } else {
                View nextView2 = nextView(recycler2, layoutStateWrapper2, layoutManagerHelper2, layoutChunkResult2);
                int handleFooter = handleFooter(nextView2, layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
                if (nextView2 != null) {
                    if (z) {
                        if (z2) {
                            i9 = layoutStateWrapper.getOffset() - (this.mLayoutWithAnchor ? 0 : this.mMarginBottom + this.mPaddingBottom);
                            i8 = i9 - handleFooter;
                        } else {
                            i8 = layoutStateWrapper.getOffset();
                            i9 = i8 + handleFooter;
                        }
                        i5 = layoutManagerHelper.getPaddingLeft() + this.mMarginLeft + this.mPaddingLeft;
                        i2 = i9;
                        i3 = mainOrientationHelper.getDecoratedMeasurementInOther(nextView2) + i5;
                        i4 = i8;
                    } else {
                        if (z2) {
                            i7 = layoutStateWrapper.getOffset() - (this.mLayoutWithAnchor ? 0 : this.mMarginRight + this.mPaddingRight);
                            i6 = i7 - handleFooter;
                        } else {
                            i6 = layoutStateWrapper.getOffset();
                            i7 = i6 + handleFooter;
                        }
                        int paddingTop3 = layoutManagerHelper.getPaddingTop() + this.mMarginTop + this.mPaddingTop;
                        i3 = i7;
                        i2 = mainOrientationHelper.getDecoratedMeasurementInOther(nextView2) + paddingTop3;
                        i4 = paddingTop3;
                        i5 = i6;
                    }
                    layoutChildWithMargin(nextView2, i5, i4, i3, i2, layoutManagerHelper);
                }
                layoutChunkResult2.mConsumed = handleFooter;
                handleStateOnResult(layoutChunkResult2, nextView2);
            }
        }
    }

    private float getViewMainWeight(int i) {
        if (this.mColWeights.length > i) {
            return this.mColWeights[i];
        }
        return Float.NaN;
    }

    /* access modifiers changed from: protected */
    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        super.checkAnchorInfo(state, anchorInfoWrapper, layoutManagerHelper);
        this.mLayoutWithAnchor = true;
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z3 = layoutManagerHelper.getOrientation() == 1;
        if (z2) {
            return 0;
        }
        if (z) {
            if (i == getItemCount() - 1) {
                if (z3) {
                    i4 = this.mMarginBottom;
                    i5 = this.mPaddingBottom;
                } else {
                    i4 = this.mMarginRight;
                    i5 = this.mPaddingRight;
                }
                return i4 + i5;
            }
        } else if (i == 0) {
            if (z3) {
                i2 = -this.mMarginTop;
                i3 = this.mPaddingTop;
            } else {
                i2 = -this.mMarginLeft;
                i3 = this.mPaddingLeft;
            }
            return i2 - i3;
        }
        return 0;
    }

    private int handleHeader(View view, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        if (view == null) {
            return 0;
        }
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        int i6 = i - i3;
        if (z) {
            i5 = -1;
        } else {
            i5 = layoutParams.width;
        }
        layoutManagerHelper.measureChildWithMargins(view, layoutManagerHelper.getChildMeasureSpec(i6, i5, !z), layoutManagerHelper.getChildMeasureSpec(i2 - i4, z ? layoutParams.height : 1073741824, z));
        return mainOrientationHelper.getDecoratedMeasurement(view);
    }

    private int handleFooter(View view, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        if (view == null) {
            return 0;
        }
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        int i6 = i - i3;
        if (z) {
            i5 = -1;
        } else {
            i5 = layoutParams.width;
        }
        layoutManagerHelper.measureChildWithMargins(view, layoutManagerHelper.getChildMeasureSpec(i6, i5, !z), layoutManagerHelper.getChildMeasureSpec(i2 - i4, z ? layoutParams.height : 1073741824, z));
        return mainOrientationHelper.getDecoratedMeasurement(view);
    }

    private int handleOne(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        boolean z2 = z;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        int i5 = 0;
        View view = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        if (!Float.isNaN(this.mAspectRatio)) {
            if (z2) {
                layoutParams.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                layoutParams.width = (int) (((float) (i2 - i4)) * this.mAspectRatio);
            }
        }
        float viewMainWeight = getViewMainWeight(0);
        layoutManagerHelper2.measureChildWithMargins(view, layoutManagerHelper2.getChildMeasureSpec(Float.isNaN(viewMainWeight) ? i - i3 : (int) (((float) (i - i3)) * viewMainWeight), z2 ? -1 : layoutParams.width, !z2), layoutManagerHelper2.getChildMeasureSpec(i2 - i4, z2 ? layoutParams.height : 1073741824, z2));
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper2 = layoutStateWrapper;
        calculateRect(mainOrientationHelper.getDecoratedMeasurement(view) + 0, this.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
        layoutChildWithMargin(view, this.mAreaRect.left, this.mAreaRect.top, this.mAreaRect.right, this.mAreaRect.bottom, layoutManagerHelper);
        LayoutChunkResult layoutChunkResult2 = layoutChunkResult;
        handleStateOnResult(layoutChunkResult, view);
        int i6 = (this.mAreaRect.bottom - this.mAreaRect.top) + (this.hasHeader ? 0 : this.mMarginTop + this.mPaddingTop);
        if (!this.hasFooter) {
            i5 = this.mMarginBottom + this.mPaddingBottom;
        }
        return i6 + i5;
    }

    private int handleTwo(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper2 = layoutStateWrapper;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        View view2 = this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams2 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        if (z) {
            if (!Float.isNaN(this.mAspectRatio)) {
                int i6 = (int) (((float) (i - i3)) / this.mAspectRatio);
                layoutParams2.height = i6;
                layoutParams.height = i6;
            }
            layoutParams2.topMargin = layoutParams.topMargin;
            layoutParams2.bottomMargin = layoutParams.bottomMargin;
            int i7 = ((((i - i3) - layoutParams.leftMargin) - layoutParams.rightMargin) - layoutParams2.leftMargin) - layoutParams2.rightMargin;
            int i8 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i7) / 2.0f : (((float) i7) * viewMainWeight) / 100.0f) + 0.5f);
            int i9 = Float.isNaN(viewMainWeight2) ? i7 - i8 : (int) (((((float) i7) * viewMainWeight2) / 100.0f) + 0.5f);
            layoutManagerHelper2.measureChildWithMargins(view, View.MeasureSpec.makeMeasureSpec(i8 + layoutParams.leftMargin + layoutParams.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams.height, true));
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i9 + layoutParams2.leftMargin + layoutParams2.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams2.height, true));
            calculateRect(Math.max(mainOrientationHelper.getDecoratedMeasurement(view), mainOrientationHelper.getDecoratedMeasurement(view2)) + 0, this.mAreaRect, layoutStateWrapper2, layoutManagerHelper2);
            int decoratedMeasurementInOther = this.mAreaRect.left + mainOrientationHelper.getDecoratedMeasurementInOther(view);
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view, this.mAreaRect.left, this.mAreaRect.top, decoratedMeasurementInOther, this.mAreaRect.bottom, layoutManagerHelper3);
            layoutChildWithMargin(view2, decoratedMeasurementInOther, this.mAreaRect.top, decoratedMeasurementInOther + mainOrientationHelper.getDecoratedMeasurementInOther(view2), this.mAreaRect.bottom, layoutManagerHelper3);
            i5 = (this.mAreaRect.bottom - this.mAreaRect.top) + (this.hasHeader ? 0 : this.mMarginTop + this.mPaddingTop) + (this.hasFooter ? 0 : this.mMarginBottom + this.mPaddingBottom);
        } else {
            if (!Float.isNaN(this.mAspectRatio)) {
                int i10 = (int) (((float) (i2 - i4)) * this.mAspectRatio);
                layoutParams2.width = i10;
                layoutParams.width = i10;
            }
            int i11 = ((((i2 - i4) - layoutParams.topMargin) - layoutParams.bottomMargin) - layoutParams2.topMargin) - layoutParams2.bottomMargin;
            int i12 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i11) / 2.0f : (((float) i11) * viewMainWeight) / 100.0f) + 0.5f);
            int i13 = Float.isNaN(viewMainWeight2) ? i11 - i12 : (int) (((((float) i11) * viewMainWeight2) / 100.0f) + 0.5f);
            layoutManagerHelper2.measureChildWithMargins(view, layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentWidth(), layoutParams.width, true), View.MeasureSpec.makeMeasureSpec(i12 + layoutParams.topMargin + layoutParams.bottomMargin, 1073741824));
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i13 + layoutParams2.topMargin + layoutParams2.bottomMargin, 1073741824));
            int i14 = 0;
            calculateRect(Math.max(mainOrientationHelper.getDecoratedMeasurement(view), mainOrientationHelper.getDecoratedMeasurement(view2)) + 0, this.mAreaRect, layoutStateWrapper2, layoutManagerHelper2);
            int decoratedMeasurementInOther2 = this.mAreaRect.top + mainOrientationHelper.getDecoratedMeasurementInOther(view);
            LayoutManagerHelper layoutManagerHelper4 = layoutManagerHelper;
            layoutChildWithMargin(view, this.mAreaRect.left, this.mAreaRect.top, this.mAreaRect.right, decoratedMeasurementInOther2, layoutManagerHelper4);
            layoutChildWithMargin(view2, this.mAreaRect.left, decoratedMeasurementInOther2, this.mAreaRect.right, decoratedMeasurementInOther2 + mainOrientationHelper.getDecoratedMeasurementInOther(view2), layoutManagerHelper4);
            int i15 = (this.mAreaRect.right - this.mAreaRect.left) + (this.hasHeader ? 0 : this.mMarginLeft + this.mPaddingRight);
            if (!this.hasFooter) {
                i14 = this.mMarginRight + this.mPaddingRight;
            }
            i5 = i15 + i14;
        }
        handleStateOnResult(layoutChunkResult, this.mChildrenViews);
        return i5;
    }

    private int handleThree(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        View view;
        int i6;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        int i7 = 0;
        View view2 = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        View view3 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[2] : this.mChildrenViews[1];
        View view4 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[1] : this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams2 = (VirtualLayoutManager.LayoutParams) view3.getLayoutParams();
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        if (z) {
            if (!Float.isNaN(this.mAspectRatio)) {
                layoutParams.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            }
            layoutParams2.topMargin = layoutParams.topMargin;
            layoutParams3.bottomMargin = layoutParams.bottomMargin;
            layoutParams3.leftMargin = layoutParams2.leftMargin;
            layoutParams3.rightMargin = layoutParams2.rightMargin;
            int i8 = ((((i - i3) - layoutParams.leftMargin) - layoutParams.rightMargin) - layoutParams2.leftMargin) - layoutParams2.rightMargin;
            int i9 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i8) / 2.0f : (((float) i8) * viewMainWeight) / 100.0f) + 0.5f);
            if (Float.isNaN(viewMainWeight2)) {
                i5 = i8 - i9;
            } else {
                double d = (double) ((((float) i8) * viewMainWeight2) / 100.0f);
                Double.isNaN(d);
                i5 = (int) (d + 0.5d);
            }
            if (Float.isNaN(viewMainWeight3)) {
                view = view3;
                i6 = i5;
            } else {
                view = view3;
                double d2 = (double) ((((float) i8) * viewMainWeight3) / 100.0f);
                Double.isNaN(d2);
                i6 = (int) (d2 + 0.5d);
            }
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i9 + layoutParams.leftMargin + layoutParams.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams.height, true));
            int measuredHeight = view2.getMeasuredHeight();
            int i10 = Float.isNaN(this.mRowWeight) ? (int) ((((float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin)) / 2.0f) + 0.5f) : (int) (((((float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin)) * this.mRowWeight) / 100.0f) + 0.5f);
            int i11 = ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin) - i10;
            View view5 = view;
            layoutManagerHelper2.measureChildWithMargins(view5, View.MeasureSpec.makeMeasureSpec(i5 + layoutParams2.leftMargin + layoutParams2.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams2.topMargin + i10 + layoutParams2.bottomMargin, 1073741824));
            layoutManagerHelper2.measureChildWithMargins(view4, View.MeasureSpec.makeMeasureSpec(i6 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams3.topMargin + i11 + layoutParams3.bottomMargin, 1073741824));
            int i12 = 0;
            calculateRect(Math.max(measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin, i10 + layoutParams2.topMargin + layoutParams2.bottomMargin + i11 + layoutParams3.topMargin + layoutParams3.bottomMargin) + 0, this.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
            int decoratedMeasurementInOther = this.mAreaRect.left + mainOrientationHelper.getDecoratedMeasurementInOther(view2);
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view2, this.mAreaRect.left, this.mAreaRect.top, decoratedMeasurementInOther, this.mAreaRect.bottom, layoutManagerHelper3);
            int i13 = decoratedMeasurementInOther;
            layoutChildWithMargin(view5, i13, this.mAreaRect.top, decoratedMeasurementInOther + mainOrientationHelper.getDecoratedMeasurementInOther(view5), this.mAreaRect.top + view5.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin, layoutManagerHelper3);
            layoutChildWithMargin(view4, i13, this.mAreaRect.bottom - mainOrientationHelper.getDecoratedMeasurement(view4), decoratedMeasurementInOther + mainOrientationHelper.getDecoratedMeasurementInOther(view4), this.mAreaRect.bottom, layoutManagerHelper3);
            int i14 = (this.mAreaRect.bottom - this.mAreaRect.top) + (this.hasHeader ? 0 : this.mMarginTop + this.mPaddingTop);
            if (!this.hasFooter) {
                i12 = this.mMarginBottom + this.mPaddingBottom;
            }
            i7 = i12 + i14;
        }
        handleStateOnResult(layoutChunkResult, this.mChildrenViews);
        return i7;
    }

    private int handleFour(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        OnePlusNLayoutHelper onePlusNLayoutHelper;
        int i5;
        OrientationHelperEx orientationHelperEx;
        float f;
        float f2;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        View view2 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[3] : this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams2 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        View view3 = this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view3.getLayoutParams();
        View view4 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[1] : this.mChildrenViews[3];
        VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        float viewMainWeight4 = getViewMainWeight(3);
        if (z) {
            layoutParams2.topMargin = layoutParams.topMargin;
            int i6 = layoutParams.bottomMargin;
            layoutParams4.bottomMargin = i6;
            layoutParams3.bottomMargin = i6;
            layoutParams3.leftMargin = layoutParams2.leftMargin;
            layoutParams4.rightMargin = layoutParams2.rightMargin;
            if (!Float.isNaN(this.mAspectRatio)) {
                orientationHelperEx = mainOrientationHelper;
                layoutParams.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                orientationHelperEx = mainOrientationHelper;
            }
            int i7 = ((((i - i3) - layoutParams.leftMargin) - layoutParams.rightMargin) - layoutParams2.leftMargin) - layoutParams2.rightMargin;
            int i8 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i7) / 2.0f : (((float) i7) * viewMainWeight) / 100.0f) + 0.5f);
            int i9 = Float.isNaN(viewMainWeight2) ? i7 - i8 : (int) (((((float) i7) * viewMainWeight2) / 100.0f) + 0.5f);
            int i10 = (int) ((Float.isNaN(viewMainWeight3) ? ((float) ((i9 - layoutParams3.rightMargin) - layoutParams4.leftMargin)) / 2.0f : (((float) i7) * viewMainWeight3) / 100.0f) + 0.5f);
            int i11 = Float.isNaN(viewMainWeight4) ? ((i9 - layoutParams3.rightMargin) - layoutParams4.leftMargin) - i10 : (int) (((((float) i7) * viewMainWeight4) / 100.0f) + 0.5f);
            layoutManagerHelper2.measureChildWithMargins(view, View.MeasureSpec.makeMeasureSpec(i8 + layoutParams.leftMargin + layoutParams.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams.height, true));
            int measuredHeight = view.getMeasuredHeight();
            if (Float.isNaN(this.mRowWeight)) {
                f = (float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin);
                f2 = 2.0f;
            } else {
                f = ((float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin)) * this.mRowWeight;
                f2 = 100.0f;
            }
            int i12 = (int) ((f / f2) + 0.5f);
            int i13 = ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin) - i12;
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i9 + layoutParams2.leftMargin + layoutParams2.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams2.topMargin + i12 + layoutParams2.bottomMargin, 1073741824));
            layoutManagerHelper2.measureChildWithMargins(view3, View.MeasureSpec.makeMeasureSpec(i10 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams3.topMargin + i13 + layoutParams3.bottomMargin, 1073741824));
            layoutManagerHelper2.measureChildWithMargins(view4, View.MeasureSpec.makeMeasureSpec(i11 + layoutParams4.leftMargin + layoutParams4.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams4.topMargin + i13 + layoutParams4.bottomMargin, 1073741824));
            int i14 = 0;
            onePlusNLayoutHelper = this;
            onePlusNLayoutHelper.calculateRect(Math.max(measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin, i12 + layoutParams2.topMargin + layoutParams2.bottomMargin + Math.max(layoutParams3.topMargin + i13 + layoutParams3.bottomMargin, i13 + layoutParams4.topMargin + layoutParams4.bottomMargin)) + 0, onePlusNLayoutHelper.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
            OrientationHelperEx orientationHelperEx2 = orientationHelperEx;
            View view5 = view;
            int decoratedMeasurementInOther = onePlusNLayoutHelper.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view5);
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view5, onePlusNLayoutHelper.mAreaRect.left, onePlusNLayoutHelper.mAreaRect.top, decoratedMeasurementInOther, onePlusNLayoutHelper.mAreaRect.bottom, layoutManagerHelper3);
            int i15 = decoratedMeasurementInOther;
            layoutChildWithMargin(view2, i15, onePlusNLayoutHelper.mAreaRect.top, decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view2), onePlusNLayoutHelper.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view2), layoutManagerHelper3);
            int decoratedMeasurementInOther2 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view3);
            layoutChildWithMargin(view3, i15, onePlusNLayoutHelper.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view3), decoratedMeasurementInOther2, onePlusNLayoutHelper.mAreaRect.bottom, layoutManagerHelper3);
            layoutChildWithMargin(view4, decoratedMeasurementInOther2, onePlusNLayoutHelper.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view4), decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view4), onePlusNLayoutHelper.mAreaRect.bottom, layoutManagerHelper3);
            int i16 = (onePlusNLayoutHelper.mAreaRect.bottom - onePlusNLayoutHelper.mAreaRect.top) + (onePlusNLayoutHelper.hasHeader ? 0 : onePlusNLayoutHelper.mMarginTop + onePlusNLayoutHelper.mPaddingTop);
            if (!onePlusNLayoutHelper.hasFooter) {
                i14 = onePlusNLayoutHelper.mMarginBottom + onePlusNLayoutHelper.mPaddingBottom;
            }
            i5 = i16 + i14;
        } else {
            onePlusNLayoutHelper = this;
            i5 = 0;
        }
        onePlusNLayoutHelper.handleStateOnResult(layoutChunkResult, onePlusNLayoutHelper.mChildrenViews);
        return i5;
    }

    private int handleFive(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        VirtualLayoutManager.LayoutParams layoutParams;
        float f;
        float f2;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams2 = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        View view2 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[4] : this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        View view3 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[3] : this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view3.getLayoutParams();
        View view4 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[2] : this.mChildrenViews[3];
        VirtualLayoutManager.LayoutParams layoutParams5 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        View view5 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[1] : this.mChildrenViews[4];
        VirtualLayoutManager.LayoutParams layoutParams6 = (VirtualLayoutManager.LayoutParams) view5.getLayoutParams();
        OrientationHelperEx orientationHelperEx = mainOrientationHelper;
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        View view6 = view5;
        float viewMainWeight4 = getViewMainWeight(3);
        View view7 = view4;
        float viewMainWeight5 = getViewMainWeight(4);
        if (z) {
            View view8 = view3;
            layoutParams3.topMargin = layoutParams2.topMargin;
            int i6 = layoutParams2.bottomMargin;
            layoutParams5.bottomMargin = i6;
            layoutParams4.bottomMargin = i6;
            layoutParams4.leftMargin = layoutParams3.leftMargin;
            layoutParams5.rightMargin = layoutParams3.rightMargin;
            layoutParams6.rightMargin = layoutParams3.rightMargin;
            if (!Float.isNaN(this.mAspectRatio)) {
                layoutParams = layoutParams6;
                layoutParams2.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                layoutParams = layoutParams6;
            }
            int i7 = ((((i - i3) - layoutParams2.leftMargin) - layoutParams2.rightMargin) - layoutParams3.leftMargin) - layoutParams3.rightMargin;
            int i8 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i7) / 2.0f : (((float) i7) * viewMainWeight) / 100.0f) + 0.5f);
            int i9 = Float.isNaN(viewMainWeight2) ? i7 - i8 : (int) (((((float) i7) * viewMainWeight2) / 100.0f) + 0.5f);
            int i10 = (int) ((Float.isNaN(viewMainWeight3) ? ((float) ((i9 - layoutParams4.rightMargin) - layoutParams5.leftMargin)) / 3.0f : (((float) i7) * viewMainWeight3) / 100.0f) + 0.5f);
            int i11 = (int) ((Float.isNaN(viewMainWeight4) ? ((float) ((i9 - layoutParams4.rightMargin) - layoutParams5.leftMargin)) / 3.0f : (((float) i7) * viewMainWeight4) / 100.0f) + 0.5f);
            int i12 = Float.isNaN(viewMainWeight5) ? (((i9 - layoutParams4.rightMargin) - layoutParams5.leftMargin) - i10) - i11 : (int) (((((float) i7) * viewMainWeight5) / 100.0f) + 0.5f);
            layoutManagerHelper2.measureChildWithMargins(view, View.MeasureSpec.makeMeasureSpec(i8 + layoutParams2.leftMargin + layoutParams2.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams2.height, true));
            int measuredHeight = view.getMeasuredHeight();
            if (Float.isNaN(this.mRowWeight)) {
                f = (float) ((measuredHeight - layoutParams3.bottomMargin) - layoutParams4.topMargin);
                f2 = 2.0f;
            } else {
                f = ((float) ((measuredHeight - layoutParams3.bottomMargin) - layoutParams4.topMargin)) * this.mRowWeight;
                f2 = 100.0f;
            }
            int i13 = (int) ((f / f2) + 0.5f);
            int i14 = ((measuredHeight - layoutParams3.bottomMargin) - layoutParams4.topMargin) - i13;
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i9 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams3.topMargin + i13 + layoutParams3.bottomMargin, 1073741824));
            View view9 = view8;
            layoutManagerHelper2.measureChildWithMargins(view9, View.MeasureSpec.makeMeasureSpec(i10 + layoutParams4.leftMargin + layoutParams4.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams4.topMargin + i14 + layoutParams4.bottomMargin, 1073741824));
            View view10 = view7;
            layoutManagerHelper2.measureChildWithMargins(view10, View.MeasureSpec.makeMeasureSpec(i11 + layoutParams5.leftMargin + layoutParams5.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams5.topMargin + i14 + layoutParams5.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams7 = layoutParams;
            View view11 = view6;
            layoutManagerHelper2.measureChildWithMargins(view11, View.MeasureSpec.makeMeasureSpec(i12 + layoutParams7.leftMargin + layoutParams7.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams7.topMargin + i14 + layoutParams7.bottomMargin, 1073741824));
            int max = Math.max(measuredHeight + layoutParams2.topMargin + layoutParams2.bottomMargin, i13 + layoutParams3.topMargin + layoutParams3.bottomMargin + Math.max(layoutParams4.topMargin + i14 + layoutParams4.bottomMargin, i14 + layoutParams5.topMargin + layoutParams5.bottomMargin));
            int i15 = 0;
            calculateRect(max + 0, this.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
            OrientationHelperEx orientationHelperEx2 = orientationHelperEx;
            View view12 = view;
            int decoratedMeasurementInOther = this.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view12);
            View view13 = view11;
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view12, this.mAreaRect.left, this.mAreaRect.top, decoratedMeasurementInOther, this.mAreaRect.bottom, layoutManagerHelper3);
            int i16 = decoratedMeasurementInOther;
            layoutChildWithMargin(view2, i16, this.mAreaRect.top, decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view2), this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view2), layoutManagerHelper3);
            int decoratedMeasurementInOther2 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view9);
            layoutChildWithMargin(view9, i16, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view9), decoratedMeasurementInOther2, this.mAreaRect.bottom, layoutManagerHelper3);
            int decoratedMeasurementInOther3 = decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view10);
            layoutChildWithMargin(view10, decoratedMeasurementInOther2, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view10), decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view10), this.mAreaRect.bottom, layoutManagerHelper3);
            layoutChildWithMargin(view13, decoratedMeasurementInOther3, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view13), decoratedMeasurementInOther3 + orientationHelperEx2.getDecoratedMeasurementInOther(view13), this.mAreaRect.bottom, layoutManagerHelper3);
            int i17 = (this.mAreaRect.bottom - this.mAreaRect.top) + (this.hasHeader ? 0 : this.mMarginTop + this.mPaddingTop);
            if (!this.hasFooter) {
                i15 = this.mMarginBottom + this.mPaddingBottom;
            }
            i5 = i15 + i17;
        } else {
            i5 = 0;
        }
        handleStateOnResult(layoutChunkResult, this.mChildrenViews);
        return i5;
    }
}
