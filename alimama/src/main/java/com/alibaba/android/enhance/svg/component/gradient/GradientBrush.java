package com.alibaba.android.enhance.svg.component.gradient;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import com.alibaba.android.enhance.svg.Brush;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import java.util.List;

class GradientBrush extends Brush {
    private Matrix mMatrix;
    private final List<String> mPoints;
    private Brush.SpreadMethod mSpreadMethod;
    private int[] mStopColors;
    private float[] mStops;
    private Brush.BrushType mType;
    private final boolean mUseObjectBoundingBox;
    private Rect mUserSpaceBoundingBox;

    GradientBrush(Brush.BrushType brushType, List<String> list, Brush.BrushUnits brushUnits) {
        this.mType = brushType;
        this.mPoints = list;
        this.mUseObjectBoundingBox = brushUnits == Brush.BrushUnits.OBJECT_BOUNDING_BOX;
    }

    /* access modifiers changed from: package-private */
    public void setUserSpaceBoundingBox(Rect rect) {
        this.mUserSpaceBoundingBox = rect;
    }

    /* access modifiers changed from: package-private */
    public void setStopAndStopColors(float[] fArr, int[] iArr) {
        if (fArr != null && iArr != null && fArr.length == iArr.length) {
            this.mStops = fArr;
            this.mStopColors = iArr;
        }
    }

    /* access modifiers changed from: package-private */
    public void setGradientTransform(Matrix matrix) {
        this.mMatrix = matrix;
    }

    /* access modifiers changed from: package-private */
    public void setSpreadMethod(Brush.SpreadMethod spreadMethod) {
        this.mSpreadMethod = spreadMethod;
    }

    private RectF getPaintRect(RectF rectF) {
        float f;
        if (!this.mUseObjectBoundingBox) {
            rectF = new RectF(this.mUserSpaceBoundingBox);
        }
        float width = rectF.width();
        float height = rectF.height();
        float f2 = 0.0f;
        if (this.mUseObjectBoundingBox) {
            f2 = rectF.left;
            f = rectF.top;
        } else {
            f = 0.0f;
        }
        return new RectF(f2, f, width + f2, height + f);
    }

    public void setupPaint(Paint paint, RectF rectF, float f, float f2) {
        Paint paint2 = paint;
        float f3 = f;
        RectF paintRect = getPaintRect(rectF);
        float width = paintRect.width();
        float height = paintRect.height();
        float f4 = paintRect.left;
        float f5 = paintRect.top;
        int[] iArr = this.mStopColors;
        int[] iArr2 = new int[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = Color.argb((int) ((((float) Color.alpha(iArr[i])) / 255.0f) * f2 * 255.0f), Color.red(iArr[i]), Color.green(iArr[i]), Color.blue(iArr[i]));
        }
        float[] fArr = this.mStops;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        if (this.mSpreadMethod == Brush.SpreadMethod.PAD) {
            tileMode = Shader.TileMode.CLAMP;
        } else if (this.mSpreadMethod == Brush.SpreadMethod.REPEAT) {
            tileMode = Shader.TileMode.REPEAT;
        } else if (this.mSpreadMethod == Brush.SpreadMethod.REFLECT) {
            tileMode = Shader.TileMode.MIRROR;
        }
        Shader.TileMode tileMode2 = tileMode;
        if (this.mPoints != null) {
            if (this.mType == Brush.BrushType.LINEAR_GRADIENT) {
                double d = (double) width;
                double d2 = (double) f4;
                double d3 = (double) f3;
                float f6 = f4;
                double d4 = (double) height;
                float f7 = width;
                float f8 = height;
                double d5 = (double) f5;
                double d6 = d3;
                float f9 = f5;
                LinearGradient linearGradient = new LinearGradient((float) PropHelper.fromRelative(this.mPoints.get(0), d, d2, d3, (double) paint.getTextSize()), (float) PropHelper.fromRelative(this.mPoints.get(1), d4, d5, d6, (double) paint.getTextSize()), (float) PropHelper.fromRelative(this.mPoints.get(2), d, d2, d6, (double) paint.getTextSize()), (float) PropHelper.fromRelative(this.mPoints.get(3), d4, d5, d6, (double) paint.getTextSize()), iArr2, fArr, tileMode2);
                if (this.mMatrix != null) {
                    Matrix matrix = new Matrix();
                    float[] fArr2 = new float[9];
                    this.mMatrix.getValues(fArr2);
                    fArr2[2] = fArr2[2] * f7;
                    fArr2[5] = fArr2[5] * f8;
                    Matrix matrix2 = new Matrix();
                    matrix2.setValues(fArr2);
                    float f10 = f6;
                    float f11 = f9;
                    matrix.preTranslate(f10, f11);
                    matrix.preConcat(matrix2);
                    matrix.preTranslate(-f10, -f11);
                    linearGradient.setLocalMatrix(matrix);
                }
                paint.setShader(linearGradient);
                return;
            }
            float f12 = width;
            float f13 = height;
            float f14 = f4;
            float[] fArr3 = fArr;
            Paint paint3 = paint2;
            float f15 = f5;
            if (this.mType == Brush.BrushType.RADIAL_GRADIENT) {
                float f16 = f12;
                double d7 = (double) f16;
                double d8 = (double) f14;
                double d9 = (double) f3;
                float f17 = f14;
                PropHelper.fromRelative(this.mPoints.get(0), d7, d8, d9, (double) paint.getTextSize());
                double d10 = (double) f15;
                double d11 = d7;
                double d12 = d9;
                PropHelper.fromRelative(this.mPoints.get(1), d11, d10, d12, (double) paint.getTextSize());
                double fromRelative = PropHelper.fromRelative(this.mPoints.get(2), d11, 0.0d, d12, (double) paint.getTextSize());
                RadialGradient radialGradient = r9;
                RadialGradient radialGradient2 = new RadialGradient((float) PropHelper.fromRelative(this.mPoints.get(3), d11, d8, d12, (double) paint.getTextSize()), (float) PropHelper.fromRelative(this.mPoints.get(4), d11, d10, d12, (double) paint.getTextSize()), (float) fromRelative, iArr2, fArr3, tileMode2);
                Matrix matrix3 = new Matrix();
                if (this.mMatrix != null) {
                    float[] fArr4 = new float[9];
                    this.mMatrix.getValues(fArr4);
                    fArr4[2] = fArr4[2] * f16;
                    fArr4[5] = fArr4[5] * f13;
                    Matrix matrix4 = new Matrix();
                    matrix4.setValues(fArr4);
                    float f18 = f17;
                    matrix3.preTranslate(f18, f15);
                    matrix3.preConcat(matrix4);
                    matrix3.preTranslate(-f18, -f15);
                }
                radialGradient.setLocalMatrix(matrix3);
                paint3.setShader(radialGradient);
            }
        }
    }
}
