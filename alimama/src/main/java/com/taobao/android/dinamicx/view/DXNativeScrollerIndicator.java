package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class DXNativeScrollerIndicator extends View {
    private boolean isHorizontal = true;
    private float radii;
    private Paint rectPaint = new Paint();
    private RectF scrollBarRectF = new RectF();
    private int scrollBarThumbColor = -37590;
    private int scrollBarTrackColor = -2828066;
    private RectF thumbRectF = new RectF();

    public boolean isHorizontal() {
        return this.isHorizontal;
    }

    public void setHorizontal(boolean z) {
        this.isHorizontal = z;
    }

    public DXNativeScrollerIndicator(Context context) {
        super(context);
        this.rectPaint.setAntiAlias(true);
        this.rectPaint.setStyle(Paint.Style.FILL);
    }

    public void setScrollBarTrackColor(int i) {
        this.scrollBarTrackColor = i;
    }

    public void setScrollBarThumbColor(int i) {
        this.scrollBarThumbColor = i;
    }

    public void refreshScrollIndicator(double d, double d2, int i, int i2) {
        double max = Math.max(Math.min(d, 1.0d), 0.0d);
        double max2 = Math.max(Math.min(d2, 1.0d), 0.0d);
        double d3 = (double) i;
        Double.isNaN(d3);
        int i3 = (int) (d3 * max2);
        double d4 = (double) (i - i3);
        Double.isNaN(d4);
        int i4 = (int) (d4 * max);
        float f = (float) i2;
        this.thumbRectF.set((float) i4, 0.0f, (float) (i4 + i3), f);
        this.scrollBarRectF.set(0.0f, 0.0f, (float) i, f);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.rectPaint.setColor(this.scrollBarTrackColor);
        canvas.drawRoundRect(this.scrollBarRectF, this.radii, this.radii, this.rectPaint);
        this.rectPaint.setColor(this.scrollBarThumbColor);
        canvas.drawRoundRect(this.thumbRectF, this.radii, this.radii, this.rectPaint);
    }

    public float getRadii() {
        return this.radii;
    }

    public void setRadii(float f) {
        this.radii = f;
    }
}
