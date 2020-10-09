package com.taobao.weex.ui.component.richtext.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import android.view.View;
import com.taobao.weex.adapter.IDrawableLoader;

public class ImgSpan extends ReplacementSpan implements IDrawableLoader.StaticTarget {
    private int height;
    private Drawable mDrawable;
    private View mView;
    private int width;

    public ImgSpan(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        if (fontMetricsInt != null) {
            fontMetricsInt.ascent = -this.height;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = 0;
        }
        return this.width;
    }

    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        if (this.mDrawable != null) {
            canvas.save();
            canvas.translate(f, (float) ((i5 - this.mDrawable.getBounds().bottom) - paint.getFontMetricsInt().descent));
            this.mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public void setDrawable(Drawable drawable, boolean z) {
        this.mDrawable = drawable;
        if (z) {
            this.mDrawable.setBounds(0, 0, this.width, this.height);
        }
        setCallback();
        this.mDrawable.invalidateSelf();
    }

    public void setView(View view) {
        this.mView = view;
        setCallback();
    }

    private void setCallback() {
        if (this.mDrawable != null && this.mView != null) {
            this.mDrawable.setCallback(this.mView);
        }
    }
}
