package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class RoundRectFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback {
    private static float sDefaultRadius = 6.0f;
    private boolean mFastEnable = false;
    private Paint mPaint;
    private Path mPath;
    private float mRadiusX = sDefaultRadius;
    private float mRadiusY = sDefaultRadius;
    private RectF mRectF;
    private boolean mStrokeEnable = false;
    private Paint mStrokePaint;
    private Path mStrokePath;

    public void afterDispatchDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        int i2 = -1;
        int i3 = -7829368;
        float f = 1.0f;
        if (!(attributeSet == null || (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundRectFeature)) == null)) {
            this.mRadiusX = obtainStyledAttributes.getDimension(R.styleable.RoundRectFeature_uik_radiusX, sDefaultRadius);
            this.mRadiusY = obtainStyledAttributes.getDimension(R.styleable.RoundRectFeature_uik_radiusY, sDefaultRadius);
            this.mFastEnable = obtainStyledAttributes.getBoolean(R.styleable.RoundRectFeature_uik_fastEnable, false);
            this.mStrokeEnable = obtainStyledAttributes.getBoolean(R.styleable.RoundRectFeature_uik_strokeEnable, false);
            i2 = obtainStyledAttributes.getColor(R.styleable.RoundFeature_uik_fastColor, -1);
            i3 = obtainStyledAttributes.getColor(R.styleable.RoundRectFeature_uik_strokeColor, -7829368);
            f = obtainStyledAttributes.getDimension(R.styleable.RoundRectFeature_uik_strokeWidth, 1.0f);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(i2);
        if (!this.mFastEnable) {
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        this.mStrokePaint = new Paint();
        this.mStrokePaint.setAntiAlias(true);
        this.mStrokePaint.setColor(i3);
        this.mStrokePaint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint.setStrokeWidth(f);
        this.mPath = new Path();
        this.mStrokePath = new Path();
        this.mRectF = new RectF();
    }

    public void beforeDraw(Canvas canvas) {
        if (!this.mFastEnable) {
            canvas.saveLayerAlpha(this.mRectF, 255, 31);
        }
    }

    public void afterDraw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mPaint);
        if (this.mStrokeEnable) {
            canvas.drawPath(this.mStrokePath, this.mStrokePaint);
        }
        if (!this.mFastEnable) {
            canvas.restore();
        }
    }

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mPath.reset();
        this.mStrokePath.reset();
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mRectF.set(0.0f, 0.0f, (float) this.mHost.getMeasuredWidth(), (float) this.mHost.getMeasuredHeight());
        this.mPath.addRoundRect(this.mRectF, this.mRadiusX, this.mRadiusY, Path.Direction.CCW);
        this.mPath.setFillType(Path.FillType.INVERSE_WINDING);
        this.mStrokePath.addRoundRect(this.mRectF, this.mRadiusX, this.mRadiusY, Path.Direction.CCW);
    }

    public void setRadiusX(float f) {
        this.mRadiusX = f;
        invalidateHost();
    }

    public void setRadiusY(float f) {
        this.mRadiusY = f;
        invalidateHost();
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

    public void setStrokeEnable(boolean z) {
        this.mStrokeEnable = z;
        invalidateHost();
    }

    public void setStrokeColor(int i) {
        this.mStrokePaint.setColor(i);
        invalidateHost();
    }

    public void setStrokeWidth(float f) {
        this.mStrokePaint.setStrokeWidth(f);
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
}
