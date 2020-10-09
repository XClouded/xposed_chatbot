package com.taobao.uikit.feature.features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class RotateFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback {
    private static final String TAG = "RotateFeature";
    private static final int sDefaultFrameColor = -16777216;
    private static final int sDefaultFrameWidth = 6;
    private static final float sDefaultRoundX = 20.0f;
    private static final float sDefaultRoundY = 20.0f;
    private static final float sRotateDegree = 45.0f;
    private Region dirtyRegion;
    private boolean mFrameEnable = false;
    private Paint mPaint;
    private Path mPath;
    private float mRoundX = 20.0f;
    private float mRoundY = 20.0f;

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
        float f = 6.0f;
        int i2 = -16777216;
        if (!(attributeSet == null || (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RotateFeature)) == null)) {
            this.mRoundX = obtainStyledAttributes.getDimension(R.styleable.RotateFeature_uik_roundX, 20.0f);
            this.mRoundY = obtainStyledAttributes.getDimension(R.styleable.RotateFeature_uik_roundY, 20.0f);
            this.mFrameEnable = obtainStyledAttributes.getBoolean(R.styleable.RotateFeature_uik_frameEnable, false);
            i2 = obtainStyledAttributes.getColor(R.styleable.RotateFeature_uik_frameColor, -16777216);
            f = obtainStyledAttributes.getDimension(R.styleable.RotateFeature_uik_frameWidth, 6.0f);
            obtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setColor(i2);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(f);
        this.mPaint.setAntiAlias(true);
    }

    @SuppressLint({"NewApi"})
    public void setHost(View view) {
        super.setHost(view);
        if (Build.VERSION.SDK_INT >= 11) {
            getHost().setLayerType(1, (Paint) null);
        }
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mPath = new Path();
        float measuredWidth = ((float) (this.mHost.getMeasuredWidth() < this.mHost.getMeasuredHeight() ? this.mHost.getMeasuredWidth() : this.mHost.getMeasuredHeight())) / 2.0f;
        float f = measuredWidth * measuredWidth;
        RectF rectF = new RectF();
        float sqrt = ((float) Math.sqrt((double) (f + f))) / 2.0f;
        float f2 = measuredWidth - sqrt;
        float f3 = sqrt + measuredWidth;
        rectF.set(f2, f2, f3, f3);
        this.mPath.addRoundRect(rectF, this.mRoundX, this.mRoundY, Path.Direction.CCW);
        this.mPath.setFillType(Path.FillType.INVERSE_WINDING);
        Matrix matrix = new Matrix();
        matrix.postRotate(sRotateDegree, measuredWidth, measuredWidth);
        this.mPath.transform(matrix);
        this.dirtyRegion = new Region();
        this.dirtyRegion.setPath(this.mPath, new Region(0, 0, this.mHost.getMeasuredWidth(), this.mHost.getMeasuredHeight()));
    }

    public boolean contain(int i, int i2) {
        return this.dirtyRegion.contains(i, i2);
    }

    public void setRoundX(float f) {
        this.mRoundX = f;
    }

    public void setRoundY(float f) {
        this.mRoundY = f;
    }

    public void setFrameEnable(boolean z) {
        this.mFrameEnable = z;
    }

    public void setFrameWidth(float f) {
        this.mPaint.setStrokeWidth(f);
    }

    public void setFrameColor(int i) {
        this.mPaint.setColor(i);
    }

    public void beforeDraw(Canvas canvas) {
        try {
            canvas.clipPath(this.mPath, Region.Op.DIFFERENCE);
        } catch (Exception unused) {
        }
    }

    public void afterDraw(Canvas canvas) {
        if (this.mFrameEnable) {
            Path path = new Path(this.mPath);
            path.setFillType(Path.FillType.WINDING);
            canvas.drawPath(path, this.mPaint);
        }
    }
}
