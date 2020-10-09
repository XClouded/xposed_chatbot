package com.taobao.uikit.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.taobao.uikit.R;

public class IndicatorView extends View {
    private int mDiameter;
    private int mFocusColor;
    private int mGapMargin;
    private int mIndex;
    private Paint mPaint;
    private int mRadius;
    private int mStrokeColor;
    private float mStrokeWidth;
    private int mTotal;
    private int mUnfocusColor;

    public IndicatorView(Context context) {
        this(context, (AttributeSet) null);
    }

    public IndicatorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IndicatorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTotal = 1;
        this.mIndex = 0;
        this.mFocusColor = Color.parseColor("#ff5000");
        this.mUnfocusColor = Color.parseColor("#7fffffff");
        this.mStrokeColor = Color.parseColor("#7f666666");
        this.mRadius = 4;
        this.mDiameter = this.mRadius * 2;
        this.mGapMargin = 8;
        this.mStrokeWidth = 1.0f;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.IndicatorView);
            this.mFocusColor = obtainStyledAttributes.getColor(R.styleable.IndicatorView_uik_focusColor, this.mFocusColor);
            this.mUnfocusColor = obtainStyledAttributes.getColor(R.styleable.IndicatorView_uik_unfocusColor, this.mUnfocusColor);
            this.mStrokeColor = obtainStyledAttributes.getColor(R.styleable.IndicatorView_uik_strokeColor, this.mStrokeColor);
            this.mStrokeWidth = obtainStyledAttributes.getDimension(R.styleable.IndicatorView_uik_strokeWidth, this.mStrokeWidth);
            this.mPaint.setStrokeWidth(this.mStrokeWidth);
            this.mTotal = obtainStyledAttributes.getInt(R.styleable.IndicatorView_uik_total, 1);
            this.mIndex = obtainStyledAttributes.getInt(R.styleable.IndicatorView_uik_index, 0);
            judgeIndex();
            this.mRadius = (int) TypedValue.applyDimension(1, (float) this.mRadius, context.getResources().getDisplayMetrics());
            this.mRadius = obtainStyledAttributes.getDimensionPixelSize(R.styleable.IndicatorView_uik_indicatorRadius, this.mRadius);
            this.mDiameter = this.mRadius * 2;
            this.mGapMargin = (int) TypedValue.applyDimension(1, (float) this.mGapMargin, context.getResources().getDisplayMetrics());
            this.mGapMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.IndicatorView_uik_gapMargin, this.mGapMargin);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            i3 = Math.max(getDesiredWidth(), size);
        } else {
            int desiredWidth = getDesiredWidth();
            i3 = mode == Integer.MIN_VALUE ? Math.min(desiredWidth, size) : desiredWidth;
        }
        if (mode2 == 1073741824) {
            i4 = Math.max(getDesiredHeight(), size2);
        } else {
            int desiredHeight = getDesiredHeight();
            i4 = mode2 == Integer.MIN_VALUE ? Math.min(desiredHeight, size2) : desiredHeight;
        }
        setMeasuredDimension(i3, i4);
    }

    private int getDesiredHeight() {
        return this.mDiameter + getPaddingTop() + getPaddingBottom();
    }

    private int getDesiredWidth() {
        return (this.mTotal * this.mDiameter) + ((this.mTotal - 1) * this.mGapMargin) + getPaddingLeft() + getPaddingRight();
    }

    private void judgeIndex() {
        if (this.mIndex < 0) {
            this.mIndex = 0;
        }
        if (this.mTotal - 1 < this.mIndex) {
            this.mIndex = this.mTotal - 1;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(0);
        if (this.mTotal > 1) {
            int paddingLeft = this.mRadius + getPaddingLeft();
            int paddingTop = this.mRadius + getPaddingTop();
            for (int i = 0; i < this.mTotal; i++) {
                if (i == this.mIndex) {
                    this.mPaint.setColor(this.mFocusColor);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle((float) (((this.mDiameter + this.mGapMargin) * i) + paddingLeft), (float) paddingTop, (float) this.mRadius, this.mPaint);
                } else {
                    this.mPaint.setColor(this.mUnfocusColor);
                    this.mPaint.setStyle(Paint.Style.FILL);
                    float f = (float) paddingTop;
                    canvas.drawCircle((float) (((this.mDiameter + this.mGapMargin) * i) + paddingLeft), f, (float) this.mRadius, this.mPaint);
                    this.mPaint.setColor(this.mStrokeColor);
                    this.mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle((float) (((this.mDiameter + this.mGapMargin) * i) + paddingLeft), f, ((float) this.mRadius) - (this.mStrokeWidth * 0.5f), this.mPaint);
                }
            }
        }
    }

    public int getTotal() {
        return this.mTotal;
    }

    public void setTotal(int i) {
        if (i < 1) {
            i = 1;
        }
        this.mTotal = i;
        requestLayout();
        invalidate();
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setIndex(int i) {
        this.mIndex = i;
        judgeIndex();
        invalidate();
    }

    public void setFocusColor(int i) {
        this.mFocusColor = i;
    }

    public void setUnfocusColor(int i) {
        this.mUnfocusColor = i;
    }

    public void setRadius(int i) {
        this.mRadius = i;
    }

    public int getGapMargin() {
        return this.mGapMargin;
    }

    public void setGapMargin(int i) {
        this.mGapMargin = i;
    }
}
