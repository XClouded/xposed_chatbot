package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class TextWidget extends BaseWidget {
    private Layout mLayout;

    public /* bridge */ /* synthetic */ void setBackgroundAndBorder(@NonNull BorderDrawable borderDrawable) {
        super.setBackgroundAndBorder(borderDrawable);
    }

    public /* bridge */ /* synthetic */ void setContentBox(int i, int i2, int i3, int i4) {
        super.setContentBox(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        super.setLayout(i, i2, i3, i4, i5, i6, point);
    }

    public TextWidget(@NonNull FlatGUIContext flatGUIContext) {
        super(flatGUIContext);
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (this.mLayout != null) {
            this.mLayout.draw(canvas);
        }
    }

    public void updateTextDrawable(Layout layout) {
        this.mLayout = layout;
        invalidate();
    }
}
