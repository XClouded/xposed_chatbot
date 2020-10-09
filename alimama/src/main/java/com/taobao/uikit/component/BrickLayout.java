package com.taobao.uikit.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.uikit.R;

public class BrickLayout extends ViewGroup {
    private int mGap;
    private int mMaxLines;

    public BrickLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public BrickLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BrickLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGap = 0;
        this.mMaxLines = Integer.MAX_VALUE;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.BrickLayout, i, 0);
        if (obtainStyledAttributes != null) {
            this.mMaxLines = obtainStyledAttributes.getInt(R.styleable.BrickLayout_uik_brickMaxLines, this.mMaxLines);
            this.mGap = obtainStyledAttributes.getDimensionPixelSize(R.styleable.BrickLayout_uik_brickGap, this.mGap);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                BrickLayoutParams brickLayoutParams = (BrickLayoutParams) childAt.getLayoutParams();
                int i6 = brickLayoutParams.x;
                int i7 = brickLayoutParams.y;
                childAt.layout(i6, i7, brickLayoutParams.width + i6, brickLayoutParams.height + i7);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        int childCount = getChildCount();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            childAt.measure(makeMeasureSpec, i2);
            BrickLayoutParams brickLayoutParams = (BrickLayoutParams) childAt.getLayoutParams();
            if (brickLayoutParams.height <= 0) {
                brickLayoutParams.height = childAt.getMeasuredHeight();
            }
            if (brickLayoutParams.width <= 0) {
                brickLayoutParams.width = childAt.getMeasuredWidth();
            }
            if (size - i4 < brickLayoutParams.width) {
                i5 += i6;
                i7++;
                i4 = 0;
                i6 = 0;
            }
            brickLayoutParams.x = i4;
            brickLayoutParams.y = brickLayoutParams.topMargin + i5;
            i6 = Math.max(i6, brickLayoutParams.height + brickLayoutParams.topMargin + brickLayoutParams.bottomMargin + this.mGap);
            i4 += brickLayoutParams.width + this.mGap;
            if (i7 <= this.mMaxLines) {
                i3 = i5 + i6;
            }
        }
        setMeasuredDimension(size, i3);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ViewGroup.LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new BrickLayoutParams(-2, -2);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, new BrickLayoutParams(layoutParams));
    }

    public static class BrickLayoutParams extends ViewGroup.MarginLayoutParams {
        int x = 0;
        int y = 0;

        public BrickLayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public BrickLayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public BrickLayoutParams(int i, int i2) {
            super(i, i2);
        }
    }
}
