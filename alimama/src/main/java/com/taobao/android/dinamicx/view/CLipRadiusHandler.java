package com.taobao.android.dinamicx.view;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CLipRadiusHandler {
    private float bottomLeftRadius;
    private float bottomRightRadius;
    private Paint imagePaint;
    private Paint roundPaint;
    private float topLeftRadius;
    private float topRightRadius;
    private boolean useClipOutLine = false;

    public boolean isUseClipOutLine() {
        return this.useClipOutLine;
    }

    private void prepare(View view) {
        if (isSupportClipOutline()) {
            if (this.topLeftRadius == this.topRightRadius && this.topLeftRadius == this.bottomLeftRadius && this.bottomLeftRadius == this.bottomRightRadius) {
                final int i = (int) this.topLeftRadius;
                view.setOutlineProvider(new ViewOutlineProvider() {
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) i);
                    }
                });
                view.setClipToOutline(true);
                this.useClipOutLine = true;
                return;
            } else if (this.topLeftRadius == this.topRightRadius && this.bottomLeftRadius == 0.0f && this.bottomRightRadius == 0.0f) {
                final int i2 = (int) this.topLeftRadius;
                view.setOutlineProvider(new ViewOutlineProvider() {
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, -i2, view.getWidth(), view.getHeight(), (float) i2);
                        outline.offset(0, i2);
                    }
                });
                view.setClipToOutline(true);
                this.useClipOutLine = true;
                return;
            } else if (this.bottomLeftRadius == this.bottomRightRadius && this.topLeftRadius == 0.0f && this.topRightRadius == 0.0f) {
                final int i3 = (int) this.bottomLeftRadius;
                view.setOutlineProvider(new ViewOutlineProvider() {
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + i3, (float) i3);
                        outline.offset(0, -i3);
                    }
                });
                view.setClipToOutline(true);
                this.useClipOutLine = true;
                return;
            } else if (this.topLeftRadius == this.bottomLeftRadius && this.topRightRadius == 0.0f && this.bottomRightRadius == 0.0f) {
                final int i4 = (int) this.topLeftRadius;
                view.setOutlineProvider(new ViewOutlineProvider() {
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(-i4, 0, view.getWidth(), view.getHeight(), (float) i4);
                        outline.offset(i4, 0);
                    }
                });
                view.setClipToOutline(true);
                this.useClipOutLine = true;
                return;
            } else if (this.topRightRadius == this.bottomRightRadius && this.topLeftRadius == 0.0f && this.bottomLeftRadius == 0.0f) {
                final int i5 = (int) this.topRightRadius;
                view.setOutlineProvider(new ViewOutlineProvider() {
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth() + i5, view.getHeight(), (float) i5);
                        outline.offset(-i5, 0);
                    }
                });
                view.setClipToOutline(true);
                this.useClipOutLine = true;
                return;
            }
        }
        this.roundPaint = new Paint();
        this.roundPaint.setColor(-1);
        this.roundPaint.setAntiAlias(true);
        this.roundPaint.setStyle(Paint.Style.FILL);
        this.roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.imagePaint = new Paint();
        this.imagePaint.setXfermode((Xfermode) null);
    }

    public void setRadius(View view, float f) {
        setRadius(view, f, f, f, f);
    }

    public void setRadius(View view, float f, float f2, float f3, float f4) {
        this.topLeftRadius = f;
        this.topRightRadius = f2;
        this.bottomLeftRadius = f3;
        this.bottomRightRadius = f4;
        prepare(view);
    }

    public void beforeDispatchDraw(View view, Canvas canvas) {
        canvas.saveLayer(new RectF(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight()), this.imagePaint, 31);
    }

    public void afterDispatchDraw(View view, Canvas canvas) {
        drawTopLeft(view, canvas);
        drawTopRight(view, canvas);
        drawBottomLeft(view, canvas);
        drawBottomRight(view, canvas);
        canvas.restore();
    }

    private void drawTopLeft(View view, Canvas canvas) {
        if (this.topLeftRadius > 0.0f) {
            Path path = new Path();
            path.moveTo(0.0f, this.topLeftRadius);
            path.lineTo(0.0f, 0.0f);
            path.lineTo(this.topLeftRadius, 0.0f);
            path.arcTo(new RectF(0.0f, 0.0f, this.topLeftRadius * 2.0f, this.topLeftRadius * 2.0f), -90.0f, -90.0f);
            path.close();
            canvas.drawPath(path, this.roundPaint);
        }
    }

    private void drawTopRight(View view, Canvas canvas) {
        if (this.topRightRadius > 0.0f) {
            int width = view.getWidth();
            Path path = new Path();
            float f = (float) width;
            path.moveTo(f - this.topRightRadius, 0.0f);
            path.lineTo(f, 0.0f);
            path.lineTo(f, this.topRightRadius);
            path.arcTo(new RectF(f - (this.topRightRadius * 2.0f), 0.0f, f, this.topRightRadius * 2.0f), 0.0f, -90.0f);
            path.close();
            canvas.drawPath(path, this.roundPaint);
        }
    }

    private void drawBottomLeft(View view, Canvas canvas) {
        if (this.bottomLeftRadius > 0.0f) {
            int height = view.getHeight();
            Path path = new Path();
            float f = (float) height;
            path.moveTo(0.0f, f - this.bottomLeftRadius);
            path.lineTo(0.0f, f);
            path.lineTo(this.bottomLeftRadius, f);
            path.arcTo(new RectF(0.0f, f - (this.bottomLeftRadius * 2.0f), this.bottomLeftRadius * 2.0f, f), 90.0f, 90.0f);
            path.close();
            canvas.drawPath(path, this.roundPaint);
        }
    }

    private void drawBottomRight(View view, Canvas canvas) {
        if (this.bottomRightRadius > 0.0f) {
            int height = view.getHeight();
            int width = view.getWidth();
            Path path = new Path();
            float f = (float) width;
            float f2 = (float) height;
            path.moveTo(f - this.bottomRightRadius, f2);
            path.lineTo(f, f2);
            path.lineTo(f, f2 - this.bottomRightRadius);
            path.arcTo(new RectF(f - (this.bottomRightRadius * 2.0f), f2 - (this.bottomRightRadius * 2.0f), f, f2), 0.0f, 90.0f);
            path.close();
            canvas.drawPath(path, this.roundPaint);
        }
    }

    public boolean isSupportClipOutline() {
        return Build.VERSION.SDK_INT >= 22;
    }
}
