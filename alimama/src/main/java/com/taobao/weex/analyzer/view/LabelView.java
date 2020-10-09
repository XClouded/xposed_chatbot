package com.taobao.weex.analyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.rd.animation.ColorAnimation;
import com.taobao.weex.analyzer.R;

public class LabelView extends View {
    private static final int DEFAULT_DEGREES = 45;
    private int mBackgroundColor;
    private Paint mBackgroundPaint;
    private boolean mFillTriangle;
    private int mGravity;
    private float mMinSize;
    private float mPadding;
    private Path mPath;
    private boolean mTextAllCaps;
    private boolean mTextBold;
    private int mTextColor;
    private String mTextContent;
    private Paint mTextPaint;
    private float mTextSize;

    public LabelView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LabelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTextPaint = new Paint(1);
        this.mBackgroundPaint = new Paint(1);
        this.mPath = new Path();
        obtainAttributes(context, attributeSet);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void obtainAttributes(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LabelView);
        this.mTextContent = obtainStyledAttributes.getString(R.styleable.LabelView_lv_text);
        this.mTextColor = obtainStyledAttributes.getColor(R.styleable.LabelView_lv_text_color, Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR));
        this.mTextSize = obtainStyledAttributes.getDimension(R.styleable.LabelView_lv_text_size, (float) sp2px(11.0f));
        this.mTextBold = obtainStyledAttributes.getBoolean(R.styleable.LabelView_lv_text_bold, true);
        this.mTextAllCaps = obtainStyledAttributes.getBoolean(R.styleable.LabelView_lv_text_all_caps, true);
        this.mFillTriangle = obtainStyledAttributes.getBoolean(R.styleable.LabelView_lv_fill_triangle, false);
        this.mBackgroundColor = obtainStyledAttributes.getColor(R.styleable.LabelView_lv_background_color, Color.parseColor("#FF4081"));
        this.mMinSize = obtainStyledAttributes.getDimension(R.styleable.LabelView_lv_min_size, (float) dp2px(this.mFillTriangle ? 35.0f : 50.0f));
        this.mPadding = obtainStyledAttributes.getDimension(R.styleable.LabelView_lv_padding, (float) dp2px(3.5f));
        this.mGravity = obtainStyledAttributes.getInt(R.styleable.LabelView_lv_gravity, 51);
        obtainStyledAttributes.recycle();
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        invalidate();
    }

    public void setText(String str) {
        this.mTextContent = str;
        invalidate();
    }

    public void setTextSize(float f) {
        this.mTextSize = (float) sp2px(f);
        invalidate();
    }

    public void setTextBold(boolean z) {
        this.mTextBold = z;
        invalidate();
    }

    public void setFillTriangle(boolean z) {
        this.mFillTriangle = z;
        invalidate();
    }

    public void setTextAllCaps(boolean z) {
        this.mTextAllCaps = z;
        invalidate();
    }

    public void setBgColor(int i) {
        this.mBackgroundColor = i;
        invalidate();
    }

    public void setMinSize(float f) {
        this.mMinSize = (float) dp2px(f);
        invalidate();
    }

    public void setPadding(float f) {
        this.mPadding = (float) dp2px(f);
        invalidate();
    }

    public void setGravity(int i) {
        this.mGravity = i;
    }

    public String getText() {
        return this.mTextContent;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public float getTextSize() {
        return this.mTextSize;
    }

    public boolean isTextBold() {
        return this.mTextBold;
    }

    public boolean isFillTriangle() {
        return this.mFillTriangle;
    }

    public boolean isTextAllCaps() {
        return this.mTextAllCaps;
    }

    public int getBgColor() {
        return this.mBackgroundColor;
    }

    public float getMinSize() {
        return this.mMinSize;
    }

    public float getPadding() {
        return this.mPadding;
    }

    public int getGravity() {
        return this.mGravity;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int height = getHeight();
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setFakeBoldText(this.mTextBold);
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        float descent = this.mTextPaint.descent() - this.mTextPaint.ascent();
        if (!this.mFillTriangle) {
            double d = (double) ((this.mPadding * 2.0f) + descent);
            double sqrt = Math.sqrt(2.0d);
            Double.isNaN(d);
            double d2 = d * sqrt;
            if (this.mGravity == 51) {
                this.mPath.reset();
                Path path = this.mPath;
                double d3 = (double) height;
                Double.isNaN(d3);
                float f = (float) (d3 - d2);
                path.moveTo(0.0f, f);
                float f2 = (float) height;
                this.mPath.lineTo(0.0f, f2);
                this.mPath.lineTo(f2, 0.0f);
                this.mPath.lineTo(f, 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                drawText(height, -45.0f, canvas, descent, true);
            } else if (this.mGravity == 53) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, 0.0f);
                this.mPath.lineTo((float) d2, 0.0f);
                Path path2 = this.mPath;
                float f3 = (float) height;
                double d4 = (double) height;
                Double.isNaN(d4);
                path2.lineTo(f3, (float) (d4 - d2));
                this.mPath.lineTo(f3, f3);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                drawText(height, 45.0f, canvas, descent, true);
            } else if (this.mGravity == 83) {
                this.mPath.reset();
                this.mPath.moveTo(0.0f, 0.0f);
                this.mPath.lineTo(0.0f, (float) d2);
                Path path3 = this.mPath;
                double d5 = (double) height;
                Double.isNaN(d5);
                float f4 = (float) height;
                path3.lineTo((float) (d5 - d2), f4);
                this.mPath.lineTo(f4, f4);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                drawText(height, 45.0f, canvas, descent, false);
            } else if (this.mGravity == 85) {
                this.mPath.reset();
                float f5 = (float) height;
                this.mPath.moveTo(0.0f, f5);
                float f6 = (float) d2;
                this.mPath.lineTo(f6, f5);
                this.mPath.lineTo(f5, f6);
                this.mPath.lineTo(f5, 0.0f);
                this.mPath.close();
                canvas.drawPath(this.mPath, this.mBackgroundPaint);
                drawText(height, -45.0f, canvas, descent, false);
            }
        } else if (this.mGravity == 51) {
            this.mPath.reset();
            this.mPath.moveTo(0.0f, 0.0f);
            float f7 = (float) height;
            this.mPath.lineTo(0.0f, f7);
            this.mPath.lineTo(f7, 0.0f);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            drawTextWhenFill(height, -45.0f, canvas, true);
        } else if (this.mGravity == 53) {
            this.mPath.reset();
            float f8 = (float) height;
            this.mPath.moveTo(f8, 0.0f);
            this.mPath.lineTo(0.0f, 0.0f);
            this.mPath.lineTo(f8, f8);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            drawTextWhenFill(height, 45.0f, canvas, true);
        } else if (this.mGravity == 83) {
            this.mPath.reset();
            float f9 = (float) height;
            this.mPath.moveTo(0.0f, f9);
            this.mPath.lineTo(0.0f, 0.0f);
            this.mPath.lineTo(f9, f9);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            drawTextWhenFill(height, 45.0f, canvas, false);
        } else if (this.mGravity == 85) {
            this.mPath.reset();
            float f10 = (float) height;
            this.mPath.moveTo(f10, f10);
            this.mPath.lineTo(0.0f, f10);
            this.mPath.lineTo(f10, 0.0f);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            drawTextWhenFill(height, -45.0f, canvas, false);
        }
    }

    private void drawText(int i, float f, Canvas canvas, float f2, boolean z) {
        canvas.save();
        float f3 = ((float) i) / 2.0f;
        canvas.rotate(f, f3, f3);
        canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float) (getPaddingLeft() + (((i - getPaddingLeft()) - getPaddingRight()) / 2)), (((float) (i / 2)) - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f)) + (z ? (-(f2 + (this.mPadding * 2.0f))) / 2.0f : (f2 + (this.mPadding * 2.0f)) / 2.0f), this.mTextPaint);
        canvas.restore();
    }

    private void drawTextWhenFill(int i, float f, Canvas canvas, boolean z) {
        canvas.save();
        float f2 = ((float) i) / 2.0f;
        canvas.rotate(f, f2, f2);
        canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float) (getPaddingLeft() + (((i - getPaddingLeft()) - getPaddingRight()) / 2)), (((float) (i / 2)) - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f)) + ((float) (z ? (-i) / 4 : i / 4)), this.mTextPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int measureWidth = measureWidth(i);
        setMeasuredDimension(measureWidth, measureWidth);
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        Paint paint = this.mTextPaint;
        double measureText = (double) (paddingLeft + ((int) paint.measureText(this.mTextContent + "")));
        double sqrt = Math.sqrt(2.0d);
        Double.isNaN(measureText);
        int i2 = (int) (measureText * sqrt);
        if (mode == Integer.MIN_VALUE) {
            i2 = Math.min(i2, size);
        }
        return Math.max((int) this.mMinSize, i2);
    }

    /* access modifiers changed from: protected */
    public int dp2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* access modifiers changed from: protected */
    public int sp2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
