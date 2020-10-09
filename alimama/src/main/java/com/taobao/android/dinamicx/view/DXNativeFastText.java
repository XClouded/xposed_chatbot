package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class DXNativeFastText extends View {
    public StaticLayout staticLayout;
    private float translateX;
    private float translateY;

    public DXNativeFastText(Context context) {
        this(context, (AttributeSet) null);
    }

    public DXNativeFastText(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DXNativeFastText(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(this.translateX, this.translateY);
        if (this.staticLayout != null) {
            this.staticLayout.draw(canvas);
        }
        canvas.restore();
    }

    public void setStaticLayout(StaticLayout staticLayout2) {
        this.staticLayout = staticLayout2;
    }

    public StaticLayout getStaticLayout() {
        return this.staticLayout;
    }

    public float getTranslateY() {
        return this.translateY;
    }

    public void setTranslateY(float f) {
        this.translateY = f;
    }

    public void setTranslateX(float f) {
        this.translateX = f;
    }
}
