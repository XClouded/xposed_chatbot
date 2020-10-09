package com.alimama.unionwl.uiframe.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.alimama.unionwl.uiframe.R;
import com.alimama.unionwl.utils.LocalDisplay;

public class IconFontWithDot extends ISIconFontTextView {
    private int mDotColor;
    private int mDotRadius;
    private int mDotStrokeColor;
    private int mDotStrokeWidth;
    private int mDotVisibility;
    private Paint mEraserPaint;
    private Paint mPaint;

    public IconFontWithDot(Context context) {
        this(context, (AttributeSet) null);
    }

    public IconFontWithDot(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDotRadius = 3;
        this.mDotVisibility = 4;
        this.mDotStrokeWidth = 2;
        this.mDotStrokeColor = -1;
        this.mDotColor = context.getResources().getColor(R.color.is_main_color);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.IconFontWithDot, 0, 0);
        if (obtainStyledAttributes != null) {
            this.mDotColor = obtainStyledAttributes.getColor(R.styleable.IconFontWithDot_dotColor, this.mDotColor);
            this.mDotVisibility = obtainStyledAttributes.getInt(R.styleable.IconFontWithDot_dotVisibility, this.mDotVisibility);
            this.mDotRadius = obtainStyledAttributes.getInt(R.styleable.IconFontWithDot_dotRadius, this.mDotRadius);
            this.mDotStrokeWidth = (int) obtainStyledAttributes.getDimension(R.styleable.IconFontWithDot_dotStrokeWidth, (float) this.mDotStrokeWidth);
            this.mDotStrokeColor = obtainStyledAttributes.getColor(R.styleable.IconFontWithDot_dotStrokeColor, this.mDotStrokeColor);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mEraserPaint = new Paint();
        this.mEraserPaint.setAlpha(0);
    }

    public void setDotVisibility(int i) {
        this.mDotVisibility = i;
        invalidate();
    }

    public int getDotVisibility() {
        return this.mDotVisibility;
    }

    public void setDotColor(int i) {
        this.mDotColor = i;
        invalidate();
    }

    public int getDotColor() {
        return this.mDotColor;
    }

    public void setDocRadius(int i) {
        this.mDotRadius = i;
        invalidate();
    }

    public int getDocRadius() {
        return this.mDotRadius;
    }

    public void setDotStrokeColor(int i) {
        this.mDotStrokeColor = i;
        invalidate();
    }

    public int getDocStrokeColor() {
        return this.mDotStrokeColor;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = (float) this.mDotRadius;
        float width = (float) ((getWidth() - LocalDisplay.dp2px(f)) - getPaddingRight());
        float dp2px = (float) (LocalDisplay.dp2px(f) + getPaddingTop());
        if (this.mDotVisibility == 4 || this.mDotVisibility == 8) {
            canvas.drawCircle(width, dp2px, (float) LocalDisplay.dp2px((float) this.mDotRadius), this.mEraserPaint);
            return;
        }
        this.mPaint.setColor(this.mDotColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width, dp2px, (float) LocalDisplay.dp2px((float) this.mDotRadius), this.mPaint);
        this.mPaint.setColor(this.mDotStrokeColor);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(0.0f);
        canvas.drawCircle(width, dp2px, (float) LocalDisplay.dp2px((float) this.mDotRadius), this.mPaint);
    }
}
