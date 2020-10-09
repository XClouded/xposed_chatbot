package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class RoundFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback {
    private boolean mFastEnable = false;
    private Paint mPaint;
    private Path mPath;
    private Path mPathExtraA;
    private Path mPathExtraB;
    private Path mPathExtraC;
    private Path mPathExtraD;
    private float mRadius = 0.0f;
    private RectF mRectF;
    private Rect mRectOld;
    private Rect mRectOut;
    private Drawable mShadowDrawable;
    private int mShadowOffset;
    private int mShadowOffsetPixel;

    public void afterDispatchDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        int i2 = -1;
        if (!(attributeSet == null || (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundFeature)) == null)) {
            this.mShadowDrawable = obtainStyledAttributes.getDrawable(R.styleable.RoundFeature_uik_shadowDrawable);
            this.mShadowOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RoundFeature_uik_shadowOffset, 0);
            this.mFastEnable = obtainStyledAttributes.getBoolean(R.styleable.RoundFeature_uik_fastEnable, false);
            i2 = obtainStyledAttributes.getColor(R.styleable.RoundFeature_uik_fastColor, -1);
            this.mRadius = (float) obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RoundFeature_uik_radius, 0);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(i2);
        if (!this.mFastEnable) {
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        this.mRectF = new RectF();
        this.mRectOut = new Rect();
        this.mPath = new Path();
        this.mRectOld = new Rect();
        this.mPathExtraA = new Path();
        this.mPathExtraB = new Path();
        this.mPathExtraC = new Path();
        this.mPathExtraD = new Path();
    }

    public void beforeDraw(Canvas canvas) {
        if (!this.mFastEnable) {
            canvas.saveLayerAlpha(this.mRectF, 255, 31);
        }
    }

    public void afterDraw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.drawPath(this.mPathExtraA, this.mPaint);
        canvas.drawPath(this.mPathExtraB, this.mPaint);
        canvas.drawPath(this.mPathExtraC, this.mPaint);
        canvas.drawPath(this.mPathExtraD, this.mPaint);
        drawShadow(canvas, this.mRectOut);
        if (!this.mFastEnable) {
            canvas.restore();
        }
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        update();
    }

    private void update() {
        if (this.mHost != null) {
            float measuredWidth = (float) this.mHost.getMeasuredWidth();
            float measuredHeight = (float) this.mHost.getMeasuredHeight();
            float f = measuredWidth > measuredHeight ? measuredHeight / 2.0f : measuredWidth / 2.0f;
            float f2 = (float) this.mShadowOffsetPixel;
            if (this.mRadius > 0.0f && this.mRadius < f) {
                f2 += f - this.mRadius;
                f = this.mRadius;
            }
            float f3 = f2;
            calcRect(this.mRectOut);
            this.mRectF.set(0.0f, 0.0f, measuredWidth, measuredHeight);
            this.mPath.reset();
            this.mPathExtraA.reset();
            this.mPathExtraB.reset();
            this.mPathExtraC.reset();
            this.mPathExtraD.reset();
            this.mPath.addCircle(measuredWidth / 2.0f, measuredHeight / 2.0f, f - ((float) this.mShadowOffsetPixel), Path.Direction.CCW);
            this.mPath.setFillType(Path.FillType.INVERSE_WINDING);
            if (measuredHeight > measuredWidth) {
                float f4 = ((measuredHeight - measuredWidth) / 2.0f) + f3;
                float f5 = measuredWidth;
                this.mPathExtraA.addRect(0.0f, 0.0f, f5, f4, Path.Direction.CW);
                float f6 = ((measuredHeight + measuredWidth) / 2.0f) - f3;
                this.mPathExtraB.addRect(0.0f, f6, f5, measuredHeight, Path.Direction.CW);
                this.mPathExtraC.addRect(0.0f, f4, f3, f6, Path.Direction.CW);
                this.mPathExtraD.addRect(measuredWidth - f3, f4, f5, f6, Path.Direction.CW);
                return;
            }
            float f7 = ((measuredWidth - measuredHeight) / 2.0f) + f3;
            this.mPathExtraA.addRect(0.0f, 0.0f, f7, measuredHeight, Path.Direction.CW);
            float f8 = ((measuredHeight + measuredWidth) / 2.0f) - f3;
            this.mPathExtraB.addRect(f8, 0.0f, measuredWidth, measuredHeight, Path.Direction.CW);
            this.mPathExtraC.addRect(f7, 0.0f, f8, f3, Path.Direction.CW);
            this.mPathExtraD.addRect(f7, measuredHeight - f3, f8, measuredHeight, Path.Direction.CW);
        }
    }

    private void calcRect(Rect rect) {
        int i;
        int i2 = 0;
        if (this.mShadowDrawable != null) {
            float intrinsicWidth = ((float) this.mShadowDrawable.getIntrinsicWidth()) / ((float) this.mShadowDrawable.getIntrinsicHeight());
            int measuredWidth = this.mHost.getMeasuredWidth();
            int measuredHeight = this.mHost.getMeasuredHeight();
            int i3 = (int) (((float) measuredHeight) * intrinsicWidth);
            if (i3 <= measuredWidth) {
                i = (measuredWidth - i3) / 2;
                measuredWidth = i + i3;
            } else {
                int i4 = (int) (((float) measuredWidth) / intrinsicWidth);
                int i5 = (measuredHeight - i4) / 2;
                i2 = i5;
                measuredHeight = i4 + i5;
                i = 0;
            }
            this.mShadowOffsetPixel = ((measuredWidth - i) * this.mShadowOffset) / this.mShadowDrawable.getIntrinsicWidth();
            rect.set(i, i2, measuredWidth, measuredHeight);
            return;
        }
        rect.set(0, 0, 0, 0);
    }

    private void drawShadow(Canvas canvas, Rect rect) {
        if (this.mShadowDrawable != null) {
            this.mRectOld.set(this.mShadowDrawable.getBounds());
            this.mShadowDrawable.setBounds(rect);
            this.mShadowDrawable.draw(canvas);
            this.mShadowDrawable.setBounds(this.mRectOld);
        }
    }

    public void setFastEnable(boolean z) {
        this.mFastEnable = z;
        if (!this.mFastEnable) {
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            this.mPaint.setXfermode((Xfermode) null);
        }
        invalidateHost();
    }

    public void setFastColor(int i) {
        this.mPaint.setColor(i);
        invalidateHost();
    }

    public void setShadowDrawable(Drawable drawable) {
        this.mShadowDrawable = drawable;
        invalidateHost();
    }

    public void setShadowOffset(int i) {
        this.mShadowOffset = i;
        invalidateHost();
    }

    private void invalidateHost() {
        if (this.mHost != null) {
            this.mHost.invalidate();
        }
    }

    public void setHost(View view) {
        super.setHost(view);
        view.requestLayout();
    }

    public void setRadius(float f) {
        if (f != this.mRadius) {
            this.mRadius = f;
            update();
            invalidateHost();
        }
    }

    public float getRadius() {
        return this.mRadius;
    }
}
