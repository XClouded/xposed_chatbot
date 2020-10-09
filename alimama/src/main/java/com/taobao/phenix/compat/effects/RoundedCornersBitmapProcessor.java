package com.taobao.phenix.compat.effects;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import com.taobao.pexode.PexodeOptions;
import com.taobao.phenix.bitmap.BitmapProcessor;

public class RoundedCornersBitmapProcessor implements BitmapProcessor {
    private final CornerType mCornerType;
    private final int mMargin;
    private final int mRadius;
    private final int mTargetHeight;
    private final int mTargetWidth;

    public enum CornerType {
        ALL,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public RoundedCornersBitmapProcessor(int i, int i2) {
        this(0, 0, i, i2, CornerType.ALL);
    }

    public RoundedCornersBitmapProcessor(int i, int i2, CornerType cornerType) {
        this(0, 0, i, i2, cornerType);
    }

    public RoundedCornersBitmapProcessor(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, CornerType.ALL);
    }

    public RoundedCornersBitmapProcessor(int i, int i2, int i3, int i4, CornerType cornerType) {
        this.mTargetWidth = i;
        this.mTargetHeight = i2;
        this.mRadius = i3;
        this.mMargin = i4;
        this.mCornerType = cornerType;
    }

    public String getId() {
        return "W" + this.mTargetWidth + "$H" + this.mTargetHeight + "$R" + this.mRadius + "$M" + this.mMargin + "$P" + this.mCornerType.ordinal();
    }

    public Bitmap process(@NonNull String str, @NonNull BitmapProcessor.BitmapSupplier bitmapSupplier, @NonNull Bitmap bitmap) {
        float f;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean z = this.mTargetWidth > 0 && this.mTargetHeight > 0 && !(this.mTargetWidth == width && this.mTargetHeight == height);
        if (!z) {
            f = 1.0f;
        } else if (this.mTargetHeight * width > this.mTargetWidth * height) {
            f = ((float) this.mTargetHeight) / ((float) height);
            double d = (double) (((float) width) * f);
            Double.isNaN(d);
            width = (int) (d + 0.5d);
            height = this.mTargetHeight;
        } else {
            f = ((float) this.mTargetWidth) / ((float) width);
            width = this.mTargetWidth;
            double d2 = (double) (((float) height) * f);
            Double.isNaN(d2);
            height = (int) (d2 + 0.5d);
        }
        Bitmap bitmap2 = bitmapSupplier.get(width, height, bitmap.getConfig() != null ? bitmap.getConfig() : PexodeOptions.CONFIG);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        if (z) {
            Matrix matrix = new Matrix();
            matrix.setScale(f, f);
            bitmapShader.setLocalMatrix(matrix);
        }
        paint.setShader(bitmapShader);
        drawRoundRect(canvas, paint, (float) width, (float) height);
        return bitmap2;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float f, float f2) {
        RectF rectF;
        float f3 = f - ((float) this.mMargin);
        float f4 = f2 - ((float) this.mMargin);
        RectF rectF2 = null;
        switch (this.mCornerType) {
            case ALL:
                rectF2 = new RectF((float) this.mMargin, (float) this.mMargin, f3, f4);
                rectF = null;
                break;
            case TOP:
                rectF2 = new RectF((float) this.mMargin, (float) this.mMargin, f3, (float) (this.mMargin + (this.mRadius * 2)));
                rectF = new RectF((float) this.mMargin, (float) (this.mMargin + this.mRadius), f3, f4);
                break;
            case BOTTOM:
                rectF2 = new RectF((float) this.mMargin, f4 - ((float) (this.mRadius * 2)), f3, f4);
                rectF = new RectF((float) this.mMargin, (float) this.mMargin, f3, f4 - ((float) this.mRadius));
                break;
            case LEFT:
                rectF2 = new RectF((float) this.mMargin, (float) this.mMargin, (float) (this.mMargin + (this.mRadius * 2)), f4);
                rectF = new RectF((float) (this.mMargin + this.mRadius), (float) this.mMargin, f3, f4);
                break;
            case RIGHT:
                rectF2 = new RectF(f3 - ((float) (this.mRadius * 2)), (float) this.mMargin, f3, f4);
                rectF = new RectF((float) this.mMargin, (float) this.mMargin, f3 - ((float) this.mRadius), f4);
                break;
            default:
                rectF = null;
                break;
        }
        canvas.drawRoundRect(rectF2, (float) this.mRadius, (float) this.mRadius, paint);
        if (rectF != null) {
            canvas.drawRect(rectF, paint);
        }
    }
}
