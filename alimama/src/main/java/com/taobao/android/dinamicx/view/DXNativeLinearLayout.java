package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXNativeLinearLayout extends LinearLayout {
    private CLipRadiusHandler cLipRadiusHandler;

    public DXNativeLinearLayout(Context context) {
        super(context);
    }

    public DXNativeLinearLayout(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DXNativeLinearLayout(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            DXWidgetNode dXWidgetNode = (DXWidgetNode) childAt.getTag(DXWidgetNode.TAG_WIDGET_NODE);
            childAt.layout(dXWidgetNode.getLeft(), dXWidgetNode.getTop(), dXWidgetNode.getLeft() + dXWidgetNode.getMeasuredWidth(), dXWidgetNode.getTop() + dXWidgetNode.getMeasuredHeight());
        }
    }

    public void setClipRadiusHandler(CLipRadiusHandler cLipRadiusHandler2) {
        this.cLipRadiusHandler = cLipRadiusHandler2;
    }

    public CLipRadiusHandler getCLipRadiusHandler() {
        return this.cLipRadiusHandler;
    }

    public void dispatchDraw(Canvas canvas) {
        if (this.cLipRadiusHandler == null) {
            super.dispatchDraw(canvas);
        } else if (this.cLipRadiusHandler.isUseClipOutLine()) {
            super.dispatchDraw(canvas);
        } else {
            this.cLipRadiusHandler.beforeDispatchDraw(this, canvas);
            super.dispatchDraw(canvas);
            this.cLipRadiusHandler.afterDispatchDraw(this, canvas);
        }
    }
}
