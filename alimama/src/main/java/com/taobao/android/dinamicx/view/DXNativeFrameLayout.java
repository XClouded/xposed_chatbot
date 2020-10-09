package com.taobao.android.dinamicx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXNativeFrameLayout extends FrameLayout {
    private CLipRadiusHandler cLipRadiusHandler;
    private boolean isV2 = false;

    public DXNativeFrameLayout(@NonNull Context context) {
        super(context);
    }

    public DXNativeFrameLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DXNativeFrameLayout(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.isV2) {
            super.onMeasure(i, i2);
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        setMeasuredDimension(layoutParams.width, layoutParams.height);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            ViewGroup.LayoutParams layoutParams2 = childAt.getLayoutParams();
            childAt.measure(View.MeasureSpec.makeMeasureSpec(layoutParams2.width, 1073741824), View.MeasureSpec.makeMeasureSpec(layoutParams2.height, 1073741824));
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.isV2) {
            super.onLayout(z, i, i2, i3, i4);
        } else {
            layoutChildForDX(i, i2, i3, i4, false);
        }
    }

    private void layoutChildForDX(int i, int i2, int i3, int i4, boolean z) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                DXWidgetNode dXWidgetNode = (DXWidgetNode) childAt.getTag(DXWidgetNode.TAG_WIDGET_NODE);
                childAt.layout(dXWidgetNode.getLeft(), dXWidgetNode.getTop(), dXWidgetNode.getLeft() + dXWidgetNode.getMeasuredWidth(), dXWidgetNode.getTop() + dXWidgetNode.getMeasuredHeight());
            }
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

    public boolean isV2() {
        return this.isV2;
    }

    public void setV2(boolean z) {
        this.isV2 = z;
    }
}
