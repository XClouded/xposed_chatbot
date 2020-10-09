package com.taobao.android.dxcontainer.vlayout.layout;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import java.util.Arrays;

public class OnePlusNLayoutHelperEx extends AbstractFullFillLayoutHelper {
    private static final String TAG = "OnePlusNLayoutHelper";
    private Rect mAreaRect;
    private View[] mChildrenViews;
    private float[] mColWeights;
    private float mRowWeight;

    public OnePlusNLayoutHelperEx() {
        this.mAreaRect = new Rect();
        this.mColWeights = new float[0];
        this.mRowWeight = Float.NaN;
        setItemCount(0);
    }

    public OnePlusNLayoutHelperEx(int i) {
        this(i, 0, 0, 0, 0);
    }

    public OnePlusNLayoutHelperEx(int i, int i2, int i3, int i4, int i5) {
        this.mAreaRect = new Rect();
        this.mColWeights = new float[0];
        this.mRowWeight = Float.NaN;
        setItemCount(i);
    }

    public void onRangeChange(int i, int i2) {
        int i3 = i2 - i;
        if (i3 < 4) {
            throw new IllegalArgumentException("pls use OnePlusNLayoutHelper instead of OnePlusNLayoutHelperEx which childcount <= 5");
        } else if (i3 > 6) {
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
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            if (this.mChildrenViews == null || this.mChildrenViews.length != getItemCount()) {
                this.mChildrenViews = new View[getItemCount()];
            }
            int allChildren = getAllChildren(this.mChildrenViews, recycler, layoutStateWrapper, layoutChunkResult, layoutManagerHelper);
            if (allChildren != getItemCount()) {
                Log.w(TAG, "The real number of children is not match with range of LayoutHelper");
            }
            int i = 0;
            boolean z = layoutManagerHelper.getOrientation() == 1;
            int contentWidth = layoutManagerHelper.getContentWidth();
            int contentHeight = layoutManagerHelper.getContentHeight();
            int paddingLeft = layoutManagerHelper.getPaddingLeft() + layoutManagerHelper.getPaddingRight() + getHorizontalMargin() + getHorizontalPadding();
            int paddingTop = layoutManagerHelper.getPaddingTop() + layoutManagerHelper.getPaddingBottom() + getVerticalMargin() + getVerticalPadding();
            if (allChildren == 5) {
                i = handleFive(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
            } else if (allChildren == 6) {
                i = handSix(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
            } else if (allChildren == 7) {
                i = handSeven(layoutStateWrapper, layoutChunkResult, layoutManagerHelper, z, contentWidth, contentHeight, paddingLeft, paddingTop);
            }
            layoutChunkResult.mConsumed = i;
            Arrays.fill(this.mChildrenViews, (Object) null);
        }
    }

    private float getViewMainWeight(int i) {
        if (this.mColWeights.length > i) {
            return this.mColWeights[i];
        }
        return Float.NaN;
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        if (getItemCount() == 3) {
            if (i == 1 && z) {
                Log.w(TAG, "Should not happen after adjust anchor");
                return 0;
            }
        } else if (getItemCount() == 4 && i == 1 && z) {
            return 0;
        }
        if (layoutManagerHelper.getOrientation() == 1) {
            if (z) {
                return this.mMarginBottom + this.mPaddingBottom;
            }
            return (-this.mMarginTop) - this.mPaddingTop;
        } else if (z) {
            return this.mMarginRight + this.mPaddingRight;
        } else {
            return (-this.mMarginLeft) - this.mPaddingLeft;
        }
    }

    private int handleFive(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        int i5;
        VirtualLayoutManager.LayoutParams layoutParams;
        float f;
        int i6;
        View view;
        int i7;
        VirtualLayoutManager.LayoutParams layoutParams2;
        int i8;
        float f2;
        float f3;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view2 = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        View view3 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[4] : this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view3.getLayoutParams();
        View view4 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[3] : this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams5 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        View view5 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[2] : this.mChildrenViews[3];
        VirtualLayoutManager.LayoutParams layoutParams6 = (VirtualLayoutManager.LayoutParams) view5.getLayoutParams();
        View view6 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[1] : this.mChildrenViews[4];
        VirtualLayoutManager.LayoutParams layoutParams7 = (VirtualLayoutManager.LayoutParams) view6.getLayoutParams();
        OrientationHelperEx orientationHelperEx = mainOrientationHelper;
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        float viewMainWeight4 = getViewMainWeight(3);
        float viewMainWeight5 = getViewMainWeight(4);
        if (z) {
            View view7 = view6;
            layoutParams4.topMargin = layoutParams3.topMargin;
            int i9 = layoutParams3.bottomMargin;
            layoutParams6.bottomMargin = i9;
            layoutParams5.bottomMargin = i9;
            layoutParams5.leftMargin = layoutParams4.leftMargin;
            layoutParams6.rightMargin = layoutParams4.rightMargin;
            layoutParams7.rightMargin = layoutParams5.rightMargin;
            if (!Float.isNaN(this.mAspectRatio)) {
                layoutParams = layoutParams7;
                layoutParams3.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                layoutParams = layoutParams7;
            }
            int i10 = ((((((i - i3) - layoutParams3.leftMargin) - layoutParams3.rightMargin) - layoutParams4.leftMargin) - layoutParams4.rightMargin) - layoutParams5.leftMargin) - layoutParams5.rightMargin;
            int i11 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i10) / 3.0f : (((float) i10) * viewMainWeight) / 100.0f) + 0.5f);
            if (Float.isNaN(viewMainWeight2)) {
                i6 = (i10 - i11) / 2;
                f = 0.5f;
            } else {
                f = 0.5f;
                i6 = (int) (((((float) i10) * viewMainWeight2) / 100.0f) + 0.5f);
            }
            if (Float.isNaN(viewMainWeight3)) {
                view = view5;
                i7 = i6;
            } else {
                view = view5;
                i7 = (int) (((((float) i10) * viewMainWeight3) / 100.0f) + f);
            }
            if (Float.isNaN(viewMainWeight4)) {
                layoutParams2 = layoutParams6;
                i8 = i6;
            } else {
                layoutParams2 = layoutParams6;
                i8 = (int) (((((float) i10) * viewMainWeight4) / 100.0f) + f);
            }
            int i12 = Float.isNaN(viewMainWeight5) ? i6 : (int) (((((float) i10) * viewMainWeight5) / 100.0f) + f);
            layoutManagerHelper2.measureChildWithMargins(view2, View.MeasureSpec.makeMeasureSpec(i11 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams3.height, true));
            int measuredHeight = view2.getMeasuredHeight();
            if (Float.isNaN(this.mRowWeight)) {
                f3 = ((float) ((measuredHeight - layoutParams4.bottomMargin) - layoutParams5.topMargin)) / 2.0f;
                f2 = 0.5f;
            } else {
                f2 = 0.5f;
                f3 = (((float) ((measuredHeight - layoutParams4.bottomMargin) - layoutParams5.topMargin)) * this.mRowWeight) / 100.0f;
            }
            int i13 = (int) (f3 + f2);
            int i14 = ((measuredHeight - layoutParams4.bottomMargin) - layoutParams5.topMargin) - i13;
            layoutManagerHelper2.measureChildWithMargins(view3, View.MeasureSpec.makeMeasureSpec(i6 + layoutParams4.leftMargin + layoutParams4.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams4.topMargin + i13 + layoutParams4.bottomMargin, 1073741824));
            layoutManagerHelper2.measureChildWithMargins(view4, View.MeasureSpec.makeMeasureSpec(i7 + layoutParams5.leftMargin + layoutParams5.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams5.topMargin + i14 + layoutParams5.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams8 = layoutParams2;
            View view8 = view;
            layoutManagerHelper2.measureChildWithMargins(view8, View.MeasureSpec.makeMeasureSpec(i8 + layoutParams8.leftMargin + layoutParams8.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams8.topMargin + i14 + layoutParams8.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams9 = layoutParams;
            View view9 = view7;
            layoutManagerHelper2.measureChildWithMargins(view9, View.MeasureSpec.makeMeasureSpec(i12 + layoutParams9.leftMargin + layoutParams9.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams9.topMargin + i14 + layoutParams9.bottomMargin, 1073741824));
            i5 = Math.max(measuredHeight + layoutParams3.topMargin + layoutParams3.bottomMargin, i13 + layoutParams4.topMargin + layoutParams4.bottomMargin + Math.max(layoutParams5.topMargin + i14 + layoutParams5.bottomMargin, i14 + layoutParams8.topMargin + layoutParams8.bottomMargin)) + getVerticalMargin() + getVerticalPadding();
            calculateRect((i5 - getVerticalMargin()) - getVerticalPadding(), this.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
            OrientationHelperEx orientationHelperEx2 = orientationHelperEx;
            View view10 = view2;
            int decoratedMeasurementInOther = this.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view10);
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view10, this.mAreaRect.left, this.mAreaRect.top, decoratedMeasurementInOther, this.mAreaRect.bottom, layoutManagerHelper3);
            int decoratedMeasurementInOther2 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view3);
            layoutChildWithMargin(view3, decoratedMeasurementInOther, this.mAreaRect.top, decoratedMeasurementInOther2, this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view3), layoutManagerHelper3);
            layoutChildWithMargin(view4, decoratedMeasurementInOther2, this.mAreaRect.top, decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view4), this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view4), layoutManagerHelper3);
            int decoratedMeasurementInOther3 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view8);
            layoutChildWithMargin(view8, decoratedMeasurementInOther, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view8), decoratedMeasurementInOther3, this.mAreaRect.bottom, layoutManagerHelper3);
            layoutChildWithMargin(view9, decoratedMeasurementInOther3, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view9), decoratedMeasurementInOther3 + orientationHelperEx2.getDecoratedMeasurementInOther(view9), this.mAreaRect.bottom, layoutManagerHelper3);
        } else {
            i5 = 0;
        }
        handleStateOnResult(layoutChunkResult, this.mChildrenViews);
        return i5;
    }

    private int handSix(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        OrientationHelperEx orientationHelperEx;
        View view;
        OnePlusNLayoutHelperEx onePlusNLayoutHelperEx;
        int i5;
        View view2;
        int i6;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view3 = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view3.getLayoutParams();
        View view4 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[5] : this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams2 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        View view5 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[4] : this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view5.getLayoutParams();
        View view6 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[3] : this.mChildrenViews[3];
        VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view6.getLayoutParams();
        View view7 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[2] : this.mChildrenViews[4];
        VirtualLayoutManager.LayoutParams layoutParams5 = (VirtualLayoutManager.LayoutParams) view7.getLayoutParams();
        if (layoutManagerHelper.getReverseLayout()) {
            orientationHelperEx = mainOrientationHelper;
            view = this.mChildrenViews[1];
        } else {
            orientationHelperEx = mainOrientationHelper;
            view = this.mChildrenViews[5];
        }
        VirtualLayoutManager.LayoutParams layoutParams6 = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        View view8 = view;
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        float viewMainWeight4 = getViewMainWeight(3);
        View view9 = view7;
        float viewMainWeight5 = getViewMainWeight(4);
        View view10 = view6;
        float viewMainWeight6 = getViewMainWeight(5);
        if (z) {
            View view11 = view5;
            layoutParams2.topMargin = layoutParams.topMargin;
            int i7 = layoutParams.bottomMargin;
            layoutParams4.bottomMargin = i7;
            layoutParams3.bottomMargin = i7;
            layoutParams3.leftMargin = layoutParams2.leftMargin;
            layoutParams4.rightMargin = layoutParams2.rightMargin;
            layoutParams5.rightMargin = layoutParams2.rightMargin;
            if (!Float.isNaN(this.mAspectRatio)) {
                view2 = view4;
                layoutParams.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                view2 = view4;
            }
            int i8 = i - i3;
            int i9 = (((i8 - layoutParams.leftMargin) - layoutParams.rightMargin) - layoutParams2.leftMargin) - layoutParams2.rightMargin;
            int i10 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i9) / 2.0f : (((float) i9) * viewMainWeight) / 100.0f) + 0.5f);
            int i11 = Float.isNaN(viewMainWeight2) ? i9 - i10 : (int) (((((float) i9) * viewMainWeight2) / 100.0f) + 0.5f);
            if (Float.isNaN(viewMainWeight3)) {
                i6 = i11;
            } else {
                i6 = i11;
                double d = (double) ((((float) i9) * viewMainWeight3) / 100.0f);
                Double.isNaN(d);
                i11 = (int) (d + 0.5d);
            }
            int i12 = (int) ((Float.isNaN(viewMainWeight4) ? ((float) ((((((i8 - layoutParams4.leftMargin) - layoutParams4.rightMargin) - layoutParams5.leftMargin) - layoutParams5.rightMargin) - layoutParams6.leftMargin) - layoutParams6.rightMargin)) / 3.0f : (((float) i9) * viewMainWeight4) / 100.0f) + 0.5f);
            int i13 = Float.isNaN(viewMainWeight5) ? i12 : (int) (((((float) i9) * viewMainWeight5) / 100.0f) + 0.5f);
            int i14 = Float.isNaN(viewMainWeight6) ? i12 : (int) (((((float) i9) * viewMainWeight6) / 100.0f) + 0.5f);
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutManagerHelper3.measureChildWithMargins(view3, View.MeasureSpec.makeMeasureSpec(i10 + layoutParams.leftMargin + layoutParams.rightMargin, 1073741824), layoutManagerHelper3.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams.height, true));
            int measuredHeight = view3.getMeasuredHeight();
            int i15 = (int) ((Float.isNaN(this.mRowWeight) ? ((float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin)) / 2.0f : (((float) ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin)) * this.mRowWeight) / 100.0f) + 0.5f);
            int i16 = ((measuredHeight - layoutParams2.bottomMargin) - layoutParams3.topMargin) - i15;
            int i17 = i15;
            View view12 = view2;
            layoutManagerHelper3.measureChildWithMargins(view12, View.MeasureSpec.makeMeasureSpec(i6 + layoutParams2.leftMargin + layoutParams2.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams2.topMargin + i15 + layoutParams2.bottomMargin, 1073741824));
            View view13 = view11;
            layoutManagerHelper3.measureChildWithMargins(view13, View.MeasureSpec.makeMeasureSpec(i11 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams3.topMargin + i16 + layoutParams3.bottomMargin, 1073741824));
            View view14 = view10;
            layoutManagerHelper3.measureChildWithMargins(view14, View.MeasureSpec.makeMeasureSpec(i12 + layoutParams4.leftMargin + layoutParams4.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams4.topMargin + i16 + layoutParams4.bottomMargin, 1073741824));
            View view15 = view9;
            layoutManagerHelper3.measureChildWithMargins(view15, View.MeasureSpec.makeMeasureSpec(i13 + layoutParams5.leftMargin + layoutParams5.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams5.topMargin + i16 + layoutParams5.bottomMargin, 1073741824));
            View view16 = view8;
            layoutManagerHelper3.measureChildWithMargins(view16, View.MeasureSpec.makeMeasureSpec(i14 + layoutParams6.leftMargin + layoutParams6.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams6.topMargin + i16 + layoutParams6.bottomMargin, 1073741824));
            i5 = Math.max(measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin, (i17 + layoutParams2.topMargin + layoutParams2.bottomMargin) * 2) + Math.max(layoutParams4.topMargin + i16 + layoutParams4.bottomMargin, Math.max(layoutParams5.topMargin + i16 + layoutParams5.bottomMargin, i16 + layoutParams6.topMargin + layoutParams6.bottomMargin)) + getVerticalMargin() + getVerticalPadding();
            onePlusNLayoutHelperEx = this;
            onePlusNLayoutHelperEx.calculateRect((i5 - getVerticalMargin()) - getVerticalPadding(), onePlusNLayoutHelperEx.mAreaRect, layoutStateWrapper, layoutManagerHelper3);
            OrientationHelperEx orientationHelperEx2 = orientationHelperEx;
            View view17 = view3;
            int decoratedMeasurementInOther = onePlusNLayoutHelperEx.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view17);
            LayoutManagerHelper layoutManagerHelper4 = layoutManagerHelper;
            layoutChildWithMargin(view17, onePlusNLayoutHelperEx.mAreaRect.left, onePlusNLayoutHelperEx.mAreaRect.top, decoratedMeasurementInOther, onePlusNLayoutHelperEx.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view14), layoutManagerHelper4);
            int i18 = decoratedMeasurementInOther;
            layoutChildWithMargin(view12, i18, onePlusNLayoutHelperEx.mAreaRect.top, decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view12), onePlusNLayoutHelperEx.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view12), layoutManagerHelper4);
            layoutChildWithMargin(view13, i18, onePlusNLayoutHelperEx.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view13), decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view13), onePlusNLayoutHelperEx.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view14), layoutManagerHelper4);
            int decoratedMeasurementInOther2 = onePlusNLayoutHelperEx.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view14);
            layoutChildWithMargin(view14, onePlusNLayoutHelperEx.mAreaRect.left, onePlusNLayoutHelperEx.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view14), decoratedMeasurementInOther2, onePlusNLayoutHelperEx.mAreaRect.bottom, layoutManagerHelper4);
            int decoratedMeasurementInOther3 = decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view15);
            layoutChildWithMargin(view15, decoratedMeasurementInOther2, onePlusNLayoutHelperEx.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view15), decoratedMeasurementInOther3, onePlusNLayoutHelperEx.mAreaRect.bottom, layoutManagerHelper4);
            layoutChildWithMargin(view16, decoratedMeasurementInOther3, onePlusNLayoutHelperEx.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view16), decoratedMeasurementInOther3 + orientationHelperEx2.getDecoratedMeasurementInOther(view16), onePlusNLayoutHelperEx.mAreaRect.bottom, layoutManagerHelper4);
        } else {
            onePlusNLayoutHelperEx = this;
            i5 = 0;
        }
        onePlusNLayoutHelperEx.handleStateOnResult(layoutChunkResult, onePlusNLayoutHelperEx.mChildrenViews);
        return i5;
    }

    private int handSeven(VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper, boolean z, int i, int i2, int i3, int i4) {
        VirtualLayoutManager.LayoutParams layoutParams;
        View view;
        View view2;
        int i5;
        VirtualLayoutManager.LayoutParams layoutParams2;
        float f;
        View view3;
        int i6;
        int i7;
        float f2;
        float f3;
        float f4;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View view4 = this.mChildrenViews[0];
        VirtualLayoutManager.LayoutParams layoutParams3 = (VirtualLayoutManager.LayoutParams) view4.getLayoutParams();
        View view5 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[6] : this.mChildrenViews[1];
        VirtualLayoutManager.LayoutParams layoutParams4 = (VirtualLayoutManager.LayoutParams) view5.getLayoutParams();
        View view6 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[5] : this.mChildrenViews[2];
        VirtualLayoutManager.LayoutParams layoutParams5 = (VirtualLayoutManager.LayoutParams) view6.getLayoutParams();
        View view7 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[4] : this.mChildrenViews[3];
        VirtualLayoutManager.LayoutParams layoutParams6 = (VirtualLayoutManager.LayoutParams) view7.getLayoutParams();
        View view8 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[3] : this.mChildrenViews[4];
        VirtualLayoutManager.LayoutParams layoutParams7 = (VirtualLayoutManager.LayoutParams) view8.getLayoutParams();
        View view9 = layoutManagerHelper.getReverseLayout() ? this.mChildrenViews[2] : this.mChildrenViews[5];
        OrientationHelperEx orientationHelperEx = mainOrientationHelper;
        VirtualLayoutManager.LayoutParams layoutParams8 = (VirtualLayoutManager.LayoutParams) view9.getLayoutParams();
        if (layoutManagerHelper.getReverseLayout()) {
            view = view9;
            layoutParams = layoutParams8;
            view2 = this.mChildrenViews[1];
        } else {
            view = view9;
            layoutParams = layoutParams8;
            view2 = this.mChildrenViews[6];
        }
        View view10 = view2;
        float viewMainWeight = getViewMainWeight(0);
        float viewMainWeight2 = getViewMainWeight(1);
        float viewMainWeight3 = getViewMainWeight(2);
        float viewMainWeight4 = getViewMainWeight(3);
        VirtualLayoutManager.LayoutParams layoutParams9 = (VirtualLayoutManager.LayoutParams) view2.getLayoutParams();
        float viewMainWeight5 = getViewMainWeight(4);
        View view11 = view8;
        float viewMainWeight6 = getViewMainWeight(5);
        VirtualLayoutManager.LayoutParams layoutParams10 = layoutParams7;
        float viewMainWeight7 = getViewMainWeight(6);
        if (z) {
            View view12 = view7;
            if (!Float.isNaN(this.mAspectRatio)) {
                layoutParams2 = layoutParams6;
                layoutParams3.height = (int) (((float) (i - i3)) / this.mAspectRatio);
            } else {
                layoutParams2 = layoutParams6;
            }
            int i8 = ((((((i - i3) - layoutParams3.leftMargin) - layoutParams3.rightMargin) - layoutParams4.leftMargin) - layoutParams4.rightMargin) - layoutParams5.leftMargin) - layoutParams5.rightMargin;
            int i9 = (int) ((Float.isNaN(viewMainWeight) ? ((float) i8) / 3.0f : (((float) i8) * viewMainWeight) / 100.0f) + 0.5f);
            int i10 = Float.isNaN(viewMainWeight2) ? (i8 - i9) / 2 : (int) (((((float) i8) * viewMainWeight2) / 100.0f) + 0.5f);
            if (Float.isNaN(viewMainWeight3)) {
                view3 = view6;
                f = viewMainWeight6;
                i6 = i10;
            } else {
                view3 = view6;
                f = viewMainWeight6;
                double d = (double) ((((float) i8) * viewMainWeight3) / 100.0f);
                Double.isNaN(d);
                i6 = (int) (d + 0.5d);
            }
            if (Float.isNaN(viewMainWeight4)) {
                i7 = i10;
                f2 = 100.0f;
            } else {
                float f5 = ((float) i8) * viewMainWeight4;
                f2 = 100.0f;
                i7 = (int) ((f5 / 100.0f) + 0.5f);
            }
            int i11 = Float.isNaN(viewMainWeight5) ? i10 : (int) (((((float) i8) * viewMainWeight5) / f2) + 0.5f);
            int i12 = Float.isNaN(f) ? i10 : (int) (((((float) i8) * f) / f2) + 0.5f);
            int i13 = Float.isNaN(f) ? i10 : (int) (((((float) i8) * viewMainWeight7) / f2) + 0.5f);
            VirtualLayoutManager.LayoutParams layoutParams11 = layoutParams3;
            layoutManagerHelper2.measureChildWithMargins(view4, View.MeasureSpec.makeMeasureSpec(i9 + layoutParams3.leftMargin + layoutParams3.rightMargin, 1073741824), layoutManagerHelper2.getChildMeasureSpec(layoutManagerHelper.getContentHeight(), layoutParams3.height, true));
            int measuredHeight = view4.getMeasuredHeight();
            if (Float.isNaN(this.mRowWeight)) {
                f3 = (float) ((measuredHeight - layoutParams4.bottomMargin) - layoutParams5.topMargin);
                f4 = 3.0f;
            } else {
                f3 = ((float) ((measuredHeight - layoutParams4.bottomMargin) - layoutParams5.topMargin)) * this.mRowWeight;
                f4 = 100.0f;
            }
            int i14 = (int) ((f3 / f4) + 0.5f);
            View view13 = view4;
            layoutManagerHelper2.measureChildWithMargins(view5, View.MeasureSpec.makeMeasureSpec(i10 + layoutParams4.leftMargin + layoutParams4.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams4.topMargin + i14 + layoutParams4.bottomMargin, 1073741824));
            View view14 = view3;
            layoutManagerHelper2.measureChildWithMargins(view14, View.MeasureSpec.makeMeasureSpec(i6 + layoutParams5.leftMargin + layoutParams5.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams5.topMargin + i14 + layoutParams5.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams12 = layoutParams2;
            View view15 = view12;
            layoutManagerHelper2.measureChildWithMargins(view15, View.MeasureSpec.makeMeasureSpec(i7 + layoutParams12.leftMargin + layoutParams12.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams12.topMargin + i14 + layoutParams12.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams13 = layoutParams10;
            View view16 = view15;
            View view17 = view11;
            layoutManagerHelper2.measureChildWithMargins(view17, View.MeasureSpec.makeMeasureSpec(i11 + layoutParams13.leftMargin + layoutParams13.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams13.topMargin + i14 + layoutParams13.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams14 = layoutParams;
            View view18 = view17;
            View view19 = view;
            layoutManagerHelper2.measureChildWithMargins(view19, View.MeasureSpec.makeMeasureSpec(i12 + layoutParams14.leftMargin + layoutParams14.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams14.topMargin + i14 + layoutParams14.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams15 = layoutParams9;
            View view20 = view19;
            View view21 = view10;
            layoutManagerHelper2.measureChildWithMargins(view21, View.MeasureSpec.makeMeasureSpec(i13 + layoutParams15.leftMargin + layoutParams15.rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams15.topMargin + i14 + layoutParams15.bottomMargin, 1073741824));
            VirtualLayoutManager.LayoutParams layoutParams16 = layoutParams11;
            i5 = Math.max(measuredHeight + layoutParams16.topMargin + layoutParams16.bottomMargin, Math.max(layoutParams4.topMargin + i14 + layoutParams4.bottomMargin, layoutParams5.topMargin + i14 + layoutParams5.bottomMargin) + Math.max(layoutParams12.topMargin + i14 + layoutParams12.bottomMargin, layoutParams13.topMargin + i14 + layoutParams13.bottomMargin) + Math.max(layoutParams14.topMargin + i14 + layoutParams14.bottomMargin, i14 + layoutParams15.topMargin + layoutParams15.bottomMargin)) + getVerticalMargin() + getVerticalPadding();
            calculateRect((i5 - getVerticalMargin()) - getVerticalPadding(), this.mAreaRect, layoutStateWrapper, layoutManagerHelper2);
            OrientationHelperEx orientationHelperEx2 = orientationHelperEx;
            View view22 = view13;
            int decoratedMeasurementInOther = this.mAreaRect.left + orientationHelperEx2.getDecoratedMeasurementInOther(view22);
            View view23 = view21;
            View view24 = view20;
            LayoutManagerHelper layoutManagerHelper3 = layoutManagerHelper;
            layoutChildWithMargin(view22, this.mAreaRect.left, this.mAreaRect.top, decoratedMeasurementInOther, this.mAreaRect.bottom, layoutManagerHelper3);
            int decoratedMeasurementInOther2 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view5);
            layoutChildWithMargin(view5, decoratedMeasurementInOther, this.mAreaRect.top, decoratedMeasurementInOther2, this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view5), layoutManagerHelper3);
            layoutChildWithMargin(view14, decoratedMeasurementInOther2, this.mAreaRect.top, decoratedMeasurementInOther2 + orientationHelperEx2.getDecoratedMeasurementInOther(view14), this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view14), layoutManagerHelper3);
            View view25 = view16;
            int decoratedMeasurementInOther3 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view25);
            layoutChildWithMargin(view25, decoratedMeasurementInOther, this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view5), decoratedMeasurementInOther3, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view24), layoutManagerHelper3);
            View view26 = view18;
            layoutChildWithMargin(view26, decoratedMeasurementInOther3, this.mAreaRect.top + orientationHelperEx2.getDecoratedMeasurement(view5), decoratedMeasurementInOther3 + orientationHelperEx2.getDecoratedMeasurementInOther(view26), this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view23), layoutManagerHelper3);
            int decoratedMeasurementInOther4 = decoratedMeasurementInOther + orientationHelperEx2.getDecoratedMeasurementInOther(view24);
            layoutChildWithMargin(view24, decoratedMeasurementInOther, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view24), decoratedMeasurementInOther4, this.mAreaRect.bottom, layoutManagerHelper3);
            layoutChildWithMargin(view23, decoratedMeasurementInOther4, this.mAreaRect.bottom - orientationHelperEx2.getDecoratedMeasurement(view23), decoratedMeasurementInOther4 + orientationHelperEx2.getDecoratedMeasurementInOther(view23), this.mAreaRect.bottom, layoutManagerHelper3);
        } else {
            i5 = 0;
        }
        handleStateOnResult(layoutChunkResult, this.mChildrenViews);
        return i5;
    }
}
