package com.alibaba.android.enhance.svg.component.pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.Brush;

class PatternBrush extends Brush {
    private float mHeight;
    private final Paint mPaint = new Paint();
    private Brush.BrushUnits mPatternContentUnits;
    @NonNull
    private final PatternDrawer mPatternDrawer;
    private Matrix mPatternMatrix;
    private Brush.BrushUnits mPatternUnits;
    private Canvas mPooledCanvas;
    private float mWidth;
    private float mX;
    private float mY;

    interface PatternDrawer {
        void drawPattern(Canvas canvas, Paint paint, @Nullable RectF rectF);
    }

    PatternBrush(float f, float f2, float f3, float f4, @NonNull PatternDrawer patternDrawer) {
        this.mX = f;
        this.mY = f2;
        this.mWidth = f3;
        this.mHeight = f4;
        this.mPatternDrawer = patternDrawer;
    }

    /* access modifiers changed from: package-private */
    public void setPatternUnits(Brush.BrushUnits brushUnits) {
        this.mPatternUnits = brushUnits;
    }

    /* access modifiers changed from: package-private */
    public void setPatternContentUnits(Brush.BrushUnits brushUnits) {
        this.mPatternContentUnits = brushUnits;
    }

    public void setupPaint(Paint paint, RectF rectF, float f, float f2) {
        int i;
        int i2;
        int i3;
        int i4;
        if (this.mWidth > 0.0f && this.mHeight > 0.0f && rectF != null) {
            Matrix matrix = new Matrix();
            if (this.mPatternUnits == Brush.BrushUnits.USER_SPACE_ON_USE) {
                i4 = (int) (this.mWidth * f);
                i3 = (int) (this.mHeight * f);
                i2 = (int) (this.mX * f);
                i = (int) (this.mY * f);
            } else {
                i4 = (int) (rectF.width() * this.mWidth);
                i3 = (int) (rectF.height() * this.mHeight);
                i2 = (int) (rectF.width() * this.mX);
                i = (int) (rectF.height() * this.mY);
                matrix.preTranslate(rectF.left, rectF.top);
            }
            matrix.preTranslate((float) i2, (float) i);
            RectF rectF2 = null;
            if (this.mPatternContentUnits != Brush.BrushUnits.USER_SPACE_ON_USE) {
                rectF2 = new RectF(0.0f, 0.0f, rectF.width(), rectF.height());
            }
            Bitmap createBitmap = Bitmap.createBitmap(i4, i3, Bitmap.Config.ARGB_8888);
            if (this.mPooledCanvas == null) {
                this.mPooledCanvas = new Canvas(createBitmap);
            } else {
                this.mPooledCanvas.setBitmap(createBitmap);
            }
            if (this.mPatternMatrix != null) {
                matrix.postConcat(this.mPatternMatrix);
            }
            this.mPooledCanvas.save();
            this.mPatternDrawer.drawPattern(this.mPooledCanvas, this.mPaint, rectF2);
            this.mPooledCanvas.restore();
            BitmapShader bitmapShader = new BitmapShader(createBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            bitmapShader.setLocalMatrix(matrix);
            paint.setShader(bitmapShader);
        }
    }

    /* access modifiers changed from: package-private */
    public void setPatternTransform(@NonNull Matrix matrix) {
        this.mPatternMatrix = matrix;
    }
}
