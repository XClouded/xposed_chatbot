package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.expression.event.DXPageChangeEvent;
import com.taobao.android.dinamicx.view.DXNativePageIndicator;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXPageIndicator extends DXWidgetNode {
    public static final int DEFAULT_OFF_COLOR = 14606046;
    public static final int DEFAULT_ON_COLOR = 16742144;
    public static final long DX_PAGE_INDICATOR = -4649639459667590873L;
    public static final long DX_PAGE_INDICATOR_HIDES_FOR_SINGLE_PAGE = 5486881853309576485L;
    public static final long DX_PAGE_INDICATOR_OFF_COLOR = 5279668588453924930L;
    public static final long DX_PAGE_INDICATOR_ON_COLOR = 5176469557014791523L;
    public static final long DX_PAGE_INDICATOR_PAGE_COUNT = 7816476278377541039L;
    private boolean hidesForSinglePage;
    private int itemMargin = DXScreenTool.getPx(DinamicXEngine.getApplicationContext(), "3ap", 9);
    private int itemRoundDiameter = DXScreenTool.getPx(DinamicXEngine.getApplicationContext(), "8ap", 16);
    private int offColor = DEFAULT_OFF_COLOR;
    private int onColor = DEFAULT_ON_COLOR;
    private int pageCount;
    private int pageIndex;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXPageIndicator();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXPageIndicator();
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativePageIndicator(context);
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXPageIndicator) {
            DXPageIndicator dXPageIndicator = (DXPageIndicator) dXWidgetNode;
            this.hidesForSinglePage = dXPageIndicator.hidesForSinglePage;
            this.pageCount = dXPageIndicator.pageCount;
            this.pageIndex = dXPageIndicator.pageIndex;
            this.offColor = dXPageIndicator.offColor;
            this.onColor = dXPageIndicator.onColor;
            this.itemMargin = dXPageIndicator.itemMargin;
            this.itemRoundDiameter = dXPageIndicator.itemRoundDiameter;
        }
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        DXPageIndicator dXPageIndicator = (DXPageIndicator) getDXRuntimeContext().getWidgetNode();
        DXNativePageIndicator dXNativePageIndicator = (DXNativePageIndicator) view;
        dXNativePageIndicator.setItemRoundDiameter(dXPageIndicator.itemRoundDiameter);
        dXNativePageIndicator.setItemMargin(dXPageIndicator.itemMargin);
        int tryFetchDarkModeColor = tryFetchDarkModeColor("onColor", 1, dXPageIndicator.onColor);
        int tryFetchDarkModeColor2 = tryFetchDarkModeColor("offColor", 1, dXPageIndicator.offColor);
        dXNativePageIndicator.setSelectedDrawable(tryFetchDarkModeColor);
        dXNativePageIndicator.setUnselectedDrawable(tryFetchDarkModeColor2);
        if ((!dXPageIndicator.hidesForSinglePage || dXPageIndicator.pageCount != 1) && dXPageIndicator.pageCount > 0) {
            this.pageIndex = dXPageIndicator.pageIndex;
            dXNativePageIndicator.addChildViews(dXPageIndicator.pageCount, dXPageIndicator.pageIndex);
            return;
        }
        dXNativePageIndicator.addChildViews(0, 0);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = mode != 1073741824;
        boolean z2 = mode2 != 1073741824;
        if (z || z2) {
            if (!z) {
                i4 = DXWidgetNode.DXMeasureSpec.getSize(i);
            } else if (this.pageCount > 0) {
                i4 = 0;
                for (int i5 = 0; i5 < this.pageCount; i5++) {
                    i4 += this.itemRoundDiameter;
                    if (i5 != this.pageCount - 1) {
                        i4 += this.itemMargin;
                    }
                }
            } else {
                i4 = 0;
            }
            if (z2) {
                i3 = this.itemRoundDiameter;
            } else {
                i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
            }
        } else {
            i4 = DXWidgetNode.DXMeasureSpec.getSize(i);
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(resolveSize(i4, i), resolveSize(i3, i2));
    }

    /* access modifiers changed from: protected */
    public boolean onEvent(DXEvent dXEvent) {
        if (super.onEvent(dXEvent)) {
            return true;
        }
        if (dXEvent.getEventId() != DXSliderLayout.DX_SLIDER_LAYOUT_ON_PAGE_CHANGE) {
            return false;
        }
        if (this.pageCount <= 0) {
            return true;
        }
        DXPageChangeEvent dXPageChangeEvent = (DXPageChangeEvent) dXEvent;
        DXNativePageIndicator dXNativePageIndicator = (DXNativePageIndicator) getDXRuntimeContext().getNativeView();
        if (dXNativePageIndicator != null) {
            dXNativePageIndicator.setSelectedView(dXPageChangeEvent.pageIndex);
        }
        this.pageIndex = dXPageChangeEvent.pageIndex;
        return true;
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == 5176469557014791523L) {
            return DEFAULT_ON_COLOR;
        }
        return j == 5279668588453924930L ? DEFAULT_OFF_COLOR : super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
        if (j == 5176469557014791523L) {
            this.onColor = i;
        } else if (j == 5279668588453924930L) {
            this.offColor = i;
        } else if (j == DX_PAGE_INDICATOR_PAGE_COUNT) {
            this.pageCount = i;
        } else if (j == DX_PAGE_INDICATOR_HIDES_FOR_SINGLE_PAGE) {
            this.hidesForSinglePage = i != 0;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getOnColor() {
        return this.onColor;
    }

    public void setOnColor(int i) {
        this.onColor = i;
    }

    public int getOffColor() {
        return this.offColor;
    }

    public void setOffColor(int i) {
        this.offColor = i;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int i) {
        this.pageIndex = i;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int i) {
        this.pageCount = i;
    }

    public boolean isHidesForSinglePage() {
        return this.hidesForSinglePage;
    }

    public void setHidesForSinglePage(boolean z) {
        this.hidesForSinglePage = z;
    }

    public int getItemRoundDiameter() {
        return this.itemRoundDiameter;
    }

    public void setItemRoundDiameter(int i) {
        this.itemRoundDiameter = i;
    }

    public int getItemMargin() {
        return this.itemMargin;
    }

    public void setItemMargin(int i) {
        this.itemMargin = i;
    }
}
