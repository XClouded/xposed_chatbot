package com.alimama.moon.features.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.internal.view.SupportMenu;
import com.alimama.moon.R;

public class FlowLayout extends ViewGroup {
    private int mHorizontalSpacing;
    private Paint mPaint;
    private int mVerticalSpacing;

    /* JADX INFO: finally extract failed */
    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout);
        try {
            this.mHorizontalSpacing = obtainStyledAttributes.getDimensionPixelSize(2, 0);
            this.mVerticalSpacing = obtainStyledAttributes.getDimensionPixelSize(3, 0);
            obtainStyledAttributes.recycle();
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            this.mPaint.setStrokeWidth(2.0f);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i) - getPaddingRight();
        int i3 = 0;
        boolean z = View.MeasureSpec.getMode(i) != 0;
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int childCount = getChildCount();
        int i4 = paddingLeft;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        boolean z2 = false;
        while (i3 < childCount) {
            View childAt = getChildAt(i3);
            measureChild(childAt, i, i2);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int i8 = this.mHorizontalSpacing;
            if (layoutParams.horizontalSpacing >= 0) {
                i8 = layoutParams.horizontalSpacing;
            }
            if (z && (z2 || childAt.getMeasuredWidth() + i4 > size)) {
                paddingTop += i5 + this.mVerticalSpacing;
                i5 = childAt.getMeasuredHeight();
                i6 = Math.max(i6, i4 - i8);
                i4 = getPaddingLeft();
            }
            layoutParams.x = i4;
            layoutParams.y = paddingTop;
            i4 += childAt.getMeasuredWidth() + i8;
            i5 = Math.max(i5, childAt.getMeasuredHeight());
            z2 = layoutParams.breakLine;
            i3++;
            i7 = i8;
        }
        setMeasuredDimension(resolveSize(Math.max(i6, i4 - i7) + getPaddingRight(), i), resolveSize(paddingTop + i5 + getPaddingBottom(), i2));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            childAt.layout(layoutParams.x, layoutParams.y, layoutParams.x + childAt.getMeasuredWidth(), layoutParams.y + childAt.getMeasuredHeight());
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild = super.drawChild(canvas, view, j);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams.horizontalSpacing > 0) {
            float right = (float) view.getRight();
            float top = ((float) view.getTop()) + (((float) view.getHeight()) / 2.0f);
            float f = top - 4.0f;
            float f2 = top + 4.0f;
            float f3 = right;
            canvas.drawLine(f3, f, right, f2, this.mPaint);
            canvas.drawLine(f3, top, right + ((float) layoutParams.horizontalSpacing), top, this.mPaint);
            canvas.drawLine(right + ((float) layoutParams.horizontalSpacing), f, right + ((float) layoutParams.horizontalSpacing), f2, this.mPaint);
        }
        if (layoutParams.breakLine) {
            float right2 = (float) view.getRight();
            float top2 = ((float) view.getTop()) + (((float) view.getHeight()) / 2.0f);
            float f4 = top2 + 6.0f;
            Canvas canvas2 = canvas;
            float f5 = right2;
            float f6 = f4;
            canvas2.drawLine(f5, top2, right2, f6, this.mPaint);
            canvas2.drawLine(f5, f4, right2 + 6.0f, f6, this.mPaint);
        }
        return drawChild;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public boolean breakLine = false;
        public int horizontalSpacing = -1;
        int x;
        int y;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout_LayoutParams);
            try {
                this.horizontalSpacing = obtainStyledAttributes.getDimensionPixelSize(1, -1);
                this.breakLine = obtainStyledAttributes.getBoolean(0, false);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }
    }
}
