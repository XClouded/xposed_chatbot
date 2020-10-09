package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class PencilShapeFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback {
    private static final float sDefaultRadius = 6.0f;
    private static final float sTopRatio = 0.2f;
    protected Paint mPaint;
    private Path mPath;
    private Path mPath1;
    private float mRadiusX = sDefaultRadius;
    private float mRadiusY = sDefaultRadius;
    private RectF mRectF;
    private float mTopRatio = 0.2f;

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
        if (!(attributeSet == null || (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PencilShapeFeature)) == null)) {
            this.mRadiusX = obtainStyledAttributes.getDimension(R.styleable.PencilShapeFeature_uik_radiusX, sDefaultRadius);
            this.mRadiusY = obtainStyledAttributes.getDimension(R.styleable.PencilShapeFeature_uik_radiusY, sDefaultRadius);
            this.mTopRatio = obtainStyledAttributes.getFloat(R.styleable.PencilShapeFeature_uik_topRatio, 0.2f);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPath = new Path();
        this.mPath1 = new Path();
        this.mRectF = new RectF();
    }

    public void beforeDraw(Canvas canvas) {
        canvas.saveLayerAlpha(this.mRectF, 255, 31);
    }

    public void afterDraw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.drawPath(this.mPath1, this.mPaint);
        canvas.restore();
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mRectF.set(0.0f, 0.0f, (float) this.mHost.getWidth(), (float) this.mHost.getHeight());
        int width = this.mHost.getWidth();
        int height = this.mHost.getHeight();
        float f = (float) width;
        this.mPath.moveTo(this.mTopRatio * f, 0.0f);
        this.mPath.lineTo(0.0f, (float) (height / 2));
        float f2 = (float) height;
        this.mPath.lineTo(this.mTopRatio * f, f2);
        this.mPath.lineTo(f, f2);
        this.mPath.lineTo(f, 0.0f);
        this.mPath.lineTo(f * this.mTopRatio, 0.0f);
        this.mPath.setFillType(Path.FillType.INVERSE_WINDING);
        this.mPath1.addRoundRect(this.mRectF, this.mRadiusX, this.mRadiusY, Path.Direction.CCW);
        this.mPath1.setFillType(Path.FillType.INVERSE_WINDING);
    }

    public void setRadiusX(float f) {
        this.mRadiusX = f;
        invalidateHost();
    }

    public void setTopRatio(float f) {
        this.mTopRatio = f;
        invalidateHost();
    }

    public void setRadiusY(float f) {
        this.mRadiusY = f;
        invalidateHost();
    }

    private void invalidateHost() {
        if (this.mHost != null) {
            this.mHost.invalidate();
        }
    }
}
