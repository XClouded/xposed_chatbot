package com.alibaba.android.enhance.svg;

import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Brush {
    public abstract void setupPaint(Paint paint, RectF rectF, float f, float f2);

    public enum SpreadMethod {
        PAD(0),
        REFLECT(1),
        REPEAT(2);
        
        final int nativeInt;

        private SpreadMethod(int i) {
            this.nativeInt = i;
        }
    }

    public enum BrushType {
        LINEAR_GRADIENT(0),
        RADIAL_GRADIENT(1),
        PATTERN(2);
        
        final int nativeInt;

        private BrushType(int i) {
            this.nativeInt = i;
        }
    }

    public enum BrushUnits {
        OBJECT_BOUNDING_BOX(0),
        USER_SPACE_ON_USE(1);
        
        final int nativeInt;

        private BrushUnits(int i) {
            this.nativeInt = i;
        }
    }
}
