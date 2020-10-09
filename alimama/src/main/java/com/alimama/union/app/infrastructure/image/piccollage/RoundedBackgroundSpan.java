package com.alimama.union.app.infrastructure.image.piccollage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private static int CORNER_RADIUS = 3;
    private int backgroundColor = 0;
    private int textColor = 0;

    public RoundedBackgroundSpan(int i, int i2) {
        this.backgroundColor = i;
        this.textColor = i2;
    }

    public int getSize(@NonNull Paint paint, CharSequence charSequence, @IntRange(from = 0) int i, @IntRange(from = 0) int i2, @Nullable Paint.FontMetricsInt fontMetricsInt) {
        return Math.round(measureText(paint, charSequence, i, i2));
    }

    private float measureText(Paint paint, CharSequence charSequence, int i, int i2) {
        return paint.measureText(charSequence, i, i2);
    }

    public void draw(@NonNull Canvas canvas, CharSequence charSequence, @IntRange(from = 0) int i, @IntRange(from = 0) int i2, float f, int i3, int i4, int i5, @NonNull Paint paint) {
        float f2 = f;
        Paint paint2 = paint;
        CharSequence charSequence2 = charSequence;
        int i6 = i;
        int i7 = i2;
        RectF rectF = new RectF(f, (float) i3, measureText(paint2, charSequence, i, i2) + f2, (float) i5);
        paint2.setColor(this.backgroundColor);
        Canvas canvas2 = canvas;
        canvas.drawRoundRect(rectF, (float) CORNER_RADIUS, (float) CORNER_RADIUS, paint2);
        paint2.setColor(this.textColor);
        canvas.drawText(charSequence, i, i2, f2, (float) i4, paint2);
    }
}
