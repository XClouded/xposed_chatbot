package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.DXNativeScrollerIndicator;

public class DXScrollerIndicator extends DXWidgetNode {
    public static final int DEFAULT_INDICATOR_BG_COLOR = -2171170;
    public static final int DEFAULT_INDICATOR_COLOR = -35072;
    public static final double DEFAULT_INDICATOR_RATIO = 0.5d;
    public static final long DX_SCROLLER_INDICATOR = 4185989886676328692L;
    public static final long DX_SCROLLER_INDICATOR_COLOR = -5151416374116397110L;
    public static final long DX_SCROLLER_INDICATOR_INDICATOR_RATIO = -5150348073123091510L;
    private int indicatorColor = DEFAULT_INDICATOR_COLOR;
    private double indicatorRatio = 0.5d;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXScrollerIndicator();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXScrollerIndicator();
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeScrollerIndicator(context);
    }

    public DXScrollerIndicator() {
        this.backGroundColor = DEFAULT_INDICATOR_BG_COLOR;
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXScrollerIndicator) {
            DXScrollerIndicator dXScrollerIndicator = (DXScrollerIndicator) dXWidgetNode;
            this.indicatorColor = dXScrollerIndicator.indicatorColor;
            this.indicatorRatio = dXScrollerIndicator.indicatorRatio;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        r11 = (com.taobao.android.dinamicx.expression.event.DXScrollEvent) r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onEvent(com.taobao.android.dinamicx.expression.event.DXEvent r11) {
        /*
            r10 = this;
            boolean r0 = super.onEvent(r11)
            r1 = 1
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            long r2 = r11.getEventId()
            r4 = 5288751146867425108(0x49656b25a678ff54, double:3.821195803932153E45)
            r0 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0069
            com.taobao.android.dinamicx.DXRuntimeContext r2 = r10.getDXRuntimeContext()
            android.view.View r2 = r2.getNativeView()
            r3 = r2
            com.taobao.android.dinamicx.view.DXNativeScrollerIndicator r3 = (com.taobao.android.dinamicx.view.DXNativeScrollerIndicator) r3
            if (r3 != 0) goto L_0x0024
            return r0
        L_0x0024:
            com.taobao.android.dinamicx.expression.event.DXScrollEvent r11 = (com.taobao.android.dinamicx.expression.event.DXScrollEvent) r11
            androidx.recyclerview.widget.RecyclerView r2 = r11.getRecyclerView()
            if (r2 != 0) goto L_0x002d
            return r0
        L_0x002d:
            androidx.recyclerview.widget.RecyclerView$LayoutManager r2 = r2.getLayoutManager()
            androidx.recyclerview.widget.LinearLayoutManager r2 = (androidx.recyclerview.widget.LinearLayoutManager) r2
            int r2 = r2.getOrientation()
            if (r2 != r1) goto L_0x003d
            r3.setHorizontal(r0)
            return r1
        L_0x003d:
            com.taobao.android.dinamicx.ItemSize r0 = r11.getScrollerSize()
            int r0 = r0.width
            com.taobao.android.dinamicx.ItemSize r2 = r11.getContentSize()
            int r2 = r2.width
            int r2 = r2 - r0
            int r11 = r11.getOffsetX()
            r4 = 0
            if (r2 <= 0) goto L_0x005b
            double r4 = (double) r11
            double r6 = (double) r2
            java.lang.Double.isNaN(r4)
            java.lang.Double.isNaN(r6)
            double r4 = r4 / r6
        L_0x005b:
            double r6 = r10.indicatorRatio
            int r8 = r10.getMeasuredWidth()
            int r9 = r10.getMeasuredHeight()
            r3.refreshScrollIndicator(r4, r6, r8, r9)
            return r1
        L_0x0069:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXScrollerIndicator.onEvent(com.taobao.android.dinamicx.expression.event.DXEvent):boolean");
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        DXScrollerIndicator dXScrollerIndicator = (DXScrollerIndicator) getDXRuntimeContext().getWidgetNode();
        DXNativeScrollerIndicator dXNativeScrollerIndicator = (DXNativeScrollerIndicator) view;
        dXNativeScrollerIndicator.setScrollBarThumbColor(tryFetchDarkModeColor("indicatorColor", 2, dXScrollerIndicator.indicatorColor));
        dXNativeScrollerIndicator.refreshScrollIndicator(0.0d, dXScrollerIndicator.indicatorRatio, dXScrollerIndicator.getMeasuredWidth(), dXScrollerIndicator.getMeasuredHeight());
    }

    public void setBackground(View view) {
        DXNativeScrollerIndicator dXNativeScrollerIndicator = (DXNativeScrollerIndicator) view;
        double measuredHeight = (double) getMeasuredHeight();
        Double.isNaN(measuredHeight);
        dXNativeScrollerIndicator.setRadii((float) (measuredHeight * 0.5d));
        dXNativeScrollerIndicator.setScrollBarTrackColor(this.backGroundColor);
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == DX_SCROLLER_INDICATOR_COLOR) {
            return DEFAULT_INDICATOR_COLOR;
        }
        return j == DXHashConstant.DX_VIEW_BACKGROUNDCOLOR ? DEFAULT_INDICATOR_BG_COLOR : super.getDefaultValueForIntAttr(j);
    }

    public double getDefaultValueForDoubleAttr(long j) {
        if (j == DX_SCROLLER_INDICATOR_INDICATOR_RATIO) {
            return 0.5d;
        }
        return super.getDefaultValueForDoubleAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
        if (j == DX_SCROLLER_INDICATOR_COLOR) {
            this.indicatorColor = i;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetDoubleAttribute(long j, double d) {
        if (j == DX_SCROLLER_INDICATOR_INDICATOR_RATIO) {
            this.indicatorRatio = d > 0.0d ? Math.min(1.0d, d) : 0.5d;
        } else {
            super.onSetDoubleAttribute(j, d);
        }
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorColor(int i) {
        this.indicatorColor = i;
    }

    public double getIndicatorRatio() {
        return this.indicatorRatio;
    }

    public void setIndicatorRatio(double d) {
        this.indicatorRatio = d;
    }
}
