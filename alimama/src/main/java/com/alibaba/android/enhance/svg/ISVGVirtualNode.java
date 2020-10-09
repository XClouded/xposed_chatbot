package com.alibaba.android.enhance.svg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

public interface ISVGVirtualNode {
    void draw(Canvas canvas, Paint paint, @FloatRange(from = 0.0d, to = 1.0d) float f);

    void draw(Canvas canvas, Paint paint, @FloatRange(from = 0.0d, to = 1.0d) float f, @Nullable RectF rectF);

    Path getPath(Canvas canvas, Paint paint);

    Path getPath(Canvas canvas, Paint paint, RectF rectF);
}
