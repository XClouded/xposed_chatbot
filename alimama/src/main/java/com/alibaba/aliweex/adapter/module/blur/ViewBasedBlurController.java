package com.alibaba.aliweex.adapter.module.blur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.taobao.weex.utils.WXLogUtils;

class ViewBasedBlurController {
    private static final int DEFAULT_BACKGROUND_COLOR = -1;
    private static final float DEFAULT_SCALE_FACTOR = 8.0f;
    private static final int ROUNDING_VALUE = 16;
    @NonNull
    private final BlurAlgorithm mBlurAlgorithm;
    private float mScaleFactor = DEFAULT_SCALE_FACTOR;
    private float roundingHeightScaleFactor = 1.0f;
    private float roundingWidthScaleFactor = 1.0f;

    private ViewBasedBlurController(@NonNull BlurAlgorithm blurAlgorithm) {
        this.mBlurAlgorithm = blurAlgorithm;
        this.mScaleFactor = DEFAULT_SCALE_FACTOR;
    }

    static ViewBasedBlurController create(@NonNull BlurAlgorithm blurAlgorithm) {
        return new ViewBasedBlurController(blurAlgorithm);
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    @Nullable
    public Bitmap createBlurOnTargetView(@NonNull View view, @ColorInt int i, int i2) {
        Bitmap bitmap;
        try {
            bitmap = prepareSnapshot(view, i);
        } catch (Exception e) {
            WXLogUtils.e("WXBlurEXModule", e.getMessage());
            bitmap = null;
        }
        if (bitmap != null) {
            return this.mBlurAlgorithm.blur(bitmap, i2);
        }
        return null;
    }

    @Nullable
    private Bitmap prepareSnapshot(@NonNull View view, @ColorInt int i) {
        Bitmap allocateBitmap = allocateBitmap(view);
        if (allocateBitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(allocateBitmap);
        canvas.save();
        canvas.scale(1.0f / (this.mScaleFactor * this.roundingWidthScaleFactor), 1.0f / (this.mScaleFactor * this.roundingHeightScaleFactor));
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        canvas.drawColor(i);
        canvas.restore();
        return allocateBitmap;
    }

    @Nullable
    private Bitmap allocateBitmap(@NonNull View view) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (measuredHeight <= 0 || measuredWidth <= 0) {
            return null;
        }
        int downScaleSize = downScaleSize((float) measuredWidth);
        int downScaleSize2 = downScaleSize((float) measuredHeight);
        int roundSize = roundSize(downScaleSize);
        int roundSize2 = roundSize(downScaleSize2);
        this.roundingHeightScaleFactor = ((float) downScaleSize2) / ((float) roundSize2);
        this.roundingWidthScaleFactor = ((float) downScaleSize) / ((float) roundSize);
        try {
            return Bitmap.createBitmap(roundSize, roundSize2, this.mBlurAlgorithm.getSupportedBitmapConfig());
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    private int downScaleSize(float f) {
        return (int) Math.ceil((double) (f / this.mScaleFactor));
    }

    private int roundSize(int i) {
        int i2 = i % 16;
        return i2 == 0 ? i : (i - i2) + 16;
    }
}
