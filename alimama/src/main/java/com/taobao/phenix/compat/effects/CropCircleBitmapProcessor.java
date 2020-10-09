package com.taobao.phenix.compat.effects;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.annotation.NonNull;
import com.taobao.pexode.PexodeOptions;
import com.taobao.phenix.bitmap.BitmapProcessor;

public class CropCircleBitmapProcessor implements BitmapProcessor {
    private final int mStrokeColor;
    private final float mStrokeRatio;

    public CropCircleBitmapProcessor() {
        this(0.0f, 0);
    }

    public CropCircleBitmapProcessor(float f, int i) {
        this.mStrokeRatio = f;
        this.mStrokeColor = i;
    }

    public String getId() {
        return "W" + this.mStrokeRatio + "$C" + this.mStrokeColor;
    }

    public Bitmap process(@NonNull String str, @NonNull BitmapProcessor.BitmapSupplier bitmapSupplier, @NonNull Bitmap bitmap) {
        float f;
        float f2;
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int width = (min - bitmap.getWidth()) / 2;
        int height = (min - bitmap.getHeight()) / 2;
        float f3 = ((float) min) / 2.0f;
        if (this.mStrokeRatio > 0.0f) {
            f2 = f3 / this.mStrokeRatio;
            f = f3 + f2;
        } else {
            f = f3;
            f2 = 0.0f;
        }
        int i = (int) (f * 2.0f);
        Bitmap bitmap2 = bitmapSupplier.get(i, i, bitmap.getConfig() != null ? bitmap.getConfig() : PexodeOptions.CONFIG);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        if (!(width == 0 && height == 0)) {
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) width, (float) height);
            bitmapShader.setLocalMatrix(matrix);
        }
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        canvas.drawCircle(f, f, f3, paint);
        if (this.mStrokeRatio > 0.0f) {
            Path path = new Path();
            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setAntiAlias(true);
            paint2.setColor(this.mStrokeColor);
            paint2.setStrokeWidth(f2);
            path.addCircle(f, f, f - (f2 / 2.0f), Path.Direction.CCW);
            canvas.drawPath(path, paint2);
        }
        return bitmap2;
    }
}
