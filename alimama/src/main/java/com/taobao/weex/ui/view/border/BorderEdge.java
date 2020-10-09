package com.taobao.weex.ui.view.border;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.taobao.weex.dom.CSSShorthand;

class BorderEdge {
    private float mBorderWidth;
    private CSSShorthand.EDGE mEdge;
    @NonNull
    private BorderCorner mPostCorner;
    @NonNull
    private BorderCorner mPreCorner;

    BorderEdge() {
    }

    /* access modifiers changed from: package-private */
    public BorderEdge set(@NonNull BorderCorner borderCorner, @NonNull BorderCorner borderCorner2, float f, CSSShorthand.EDGE edge) {
        this.mPreCorner = borderCorner;
        this.mPostCorner = borderCorner2;
        this.mBorderWidth = f;
        this.mEdge = edge;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void drawEdge(@NonNull Canvas canvas, @NonNull Paint paint) {
        paint.setStrokeWidth(this.mBorderWidth);
        this.mPreCorner.drawRoundedCorner(canvas, paint, this.mPreCorner.getAngleBisectorDegree());
        paint.setStrokeWidth(this.mBorderWidth);
        canvas.drawLine(this.mPreCorner.getRoundCornerEndX(), this.mPreCorner.getRoundCornerEndY(), this.mPostCorner.getRoundCornerStartX(), this.mPostCorner.getRoundCornerStartY(), paint);
        this.mPostCorner.drawRoundedCorner(canvas, paint, this.mPostCorner.getAngleBisectorDegree() - 45.0f);
    }

    public CSSShorthand.EDGE getEdge() {
        return this.mEdge;
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }
}
