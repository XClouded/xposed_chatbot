package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public class AndroidViewWidget extends BaseWidget implements Destroyable {
    @Nullable
    private View mView;

    public /* bridge */ /* synthetic */ void setBackgroundAndBorder(@NonNull BorderDrawable borderDrawable) {
        super.setBackgroundAndBorder(borderDrawable);
    }

    public /* bridge */ /* synthetic */ void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        super.setLayout(i, i2, i3, i4, i5, i6, point);
    }

    public AndroidViewWidget(@NonNull FlatGUIContext flatGUIContext) {
        super(flatGUIContext);
    }

    public void setContentView(@Nullable View view) {
        this.mView = view;
    }

    public void setContentBox(int i, int i2, int i3, int i4) {
        if (this.mView != null) {
            this.mView.setPadding(i, i2, i3, i4);
            invalidate();
        }
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (this.mView != null) {
            this.mView.draw(canvas);
        }
    }

    public void invalidate() {
        super.invalidate();
        if (this.mView != null) {
            this.mView.invalidate();
        }
    }

    @Nullable
    public View getView() {
        return this.mView;
    }

    public void destroy() {
        if (this.mView != null) {
            this.mView = null;
        }
    }
}
