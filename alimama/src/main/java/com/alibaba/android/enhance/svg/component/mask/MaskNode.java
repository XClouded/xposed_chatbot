package com.alibaba.android.enhance.svg.component.mask;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MaskNode {
    private static final ColorMatrix LUMINANCE_TO_ALPHA = new ColorMatrix(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2125f, 0.7154f, 0.0721f, 0.0f, 0.0f});
    private final MaskContentDrawer mContentDrawer;
    private float mHeight;
    private Units mMaskContentUnits = Units.USER_SPACE_ON_USE;
    private Paint mMaskPaint1;
    private Paint mMaskPaint2;
    private Units mMaskUnits = Units.OBJECT_BOUNDING_BOX;
    private PorterDuffXfermode mMixedMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Paint mMixedPaint;
    private PorterDuffXfermode mPaint2Xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private float mWidth;
    private float mX;
    private float mY;

    interface MaskContentDrawer {
        void drawMaskContent(Canvas canvas, Paint paint, @Nullable RectF rectF);
    }

    public enum Units {
        OBJECT_BOUNDING_BOX(0),
        USER_SPACE_ON_USE(1);
        
        final int nativeInt;

        private Units(int i) {
            this.nativeInt = i;
        }
    }

    MaskNode(float f, float f2, float f3, float f4, @NonNull MaskContentDrawer maskContentDrawer) {
        this.mContentDrawer = maskContentDrawer;
        this.mX = f;
        this.mY = f2;
        this.mWidth = f3;
        this.mHeight = f4;
    }

    public void clipBoundingBox(@NonNull Canvas canvas, @NonNull RectF rectF, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        if (this.mWidth > 0.0f && this.mHeight > 0.0f) {
            if (this.mMaskUnits == Units.USER_SPACE_ON_USE) {
                f5 = this.mWidth * f;
                f4 = this.mHeight * f;
                f3 = this.mX * f;
                f2 = this.mY * f;
            } else {
                f5 = this.mWidth * rectF.width();
                f4 = this.mHeight * rectF.height();
                f3 = this.mX * rectF.width();
                f2 = this.mY * rectF.height();
            }
            float f6 = f3 + rectF.left;
            float f7 = f2 + rectF.top;
            canvas.clipRect(f6, f7, f5 + f6, f4 + f7);
        }
    }

    public void renderMask(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull RectF rectF, float f) {
        RectF rectF2;
        if (this.mMaskContentUnits == Units.USER_SPACE_ON_USE) {
            rectF2 = null;
        } else {
            rectF2 = new RectF(0.0f, 0.0f, rectF.width(), rectF.height());
        }
        this.mMixedPaint = getOrCreatePaint(this.mMixedPaint);
        this.mMixedPaint.setXfermode(this.mMixedMode);
        int saveLayer = canvas.saveLayer((RectF) null, this.mMixedPaint, 31);
        this.mMaskPaint1 = getOrCreatePaint(this.mMaskPaint1);
        this.mMaskPaint1.setColorFilter(new ColorMatrixColorFilter(LUMINANCE_TO_ALPHA));
        int saveLayer2 = canvas.saveLayer((RectF) null, this.mMaskPaint1, 31);
        this.mContentDrawer.drawMaskContent(canvas, paint, rectF2);
        canvas.restoreToCount(saveLayer2);
        this.mMaskPaint2 = getOrCreatePaint(this.mMaskPaint2);
        this.mMaskPaint2.setXfermode(this.mPaint2Xfermode);
        int saveLayer3 = canvas.saveLayer((RectF) null, this.mMaskPaint2, 31);
        this.mContentDrawer.drawMaskContent(canvas, paint, rectF2);
        canvas.restoreToCount(saveLayer3);
        canvas.restoreToCount(saveLayer);
    }

    private Paint getOrCreatePaint(Paint paint) {
        return paint == null ? new Paint(1) : paint;
    }

    /* access modifiers changed from: package-private */
    public void setMaskUnits(Units units) {
        this.mMaskUnits = units;
    }

    /* access modifiers changed from: package-private */
    public void setMaskContentUnits(Units units) {
        this.mMaskContentUnits = units;
    }
}
