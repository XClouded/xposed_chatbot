package com.alibaba.aliweex.adapter.component;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class RoundCornerDrawable extends Drawable {
    private Paint mPaint;
    int radius;
    private RectF rect;

    public int getOpacity() {
        return 0;
    }

    public RoundCornerDrawable(Paint paint, int i) {
        this.mPaint = paint;
        this.radius = i;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        this.rect = new RectF((float) i, (float) i2, (float) i3, (float) i4);
    }

    public void draw(Canvas canvas) {
        canvas.drawRoundRect(this.rect, (float) this.radius, (float) this.radius, this.mPaint);
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
