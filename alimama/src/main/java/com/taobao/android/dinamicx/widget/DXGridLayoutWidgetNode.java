package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.view.DXNativeGridLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXGridLayoutWidgetNode extends DXFrameLayoutWidgetNode {
    public static final long DX_GRID_LAYOUT = 7789579202915247118L;
    public static final long DX_GRID_LAYOUT_COLUMN_COUNT = 4480460401770252962L;
    public static final long DX_GRID_LAYOUT_COLUMN_SPACING = -7076735627431451296L;
    public static final long DX_GRID_LAYOUT_ITEM_HEIGHT = -889779179579457774L;
    public static final long DX_GRID_LAYOUT_ITEM_WIDTH = -5480582194049152328L;
    public static final long DX_GRID_LAYOUT_LINE_COLOR = -1442755333969665872L;
    public static final long DX_GRID_LAYOUT_LINE_WIDTH = -1442710627541559887L;
    public static final long DX_GRID_LAYOUT_NEED_SEPARATOR = -7975214338005072550L;
    public static final long DX_GRID_LAYOUT_ROW_COUNT = 6173497815537313897L;
    public static final long DX_GRID_LAYOUT_ROW_SPACING = -5965488911581852121L;
    private int columnCount = 0;
    private int columnSpacing;
    private int itemHeight;
    private int itemWidth;
    private int lineColor = -8421505;
    private float[] linePts;
    private int lineWidth = DXScreenTool.getPx(DinamicXEngine.getApplicationContext(), "0.5np", 0);
    private int measuredRowCount = 0;
    private boolean needSeparator = false;
    private int rowCount = 0;
    private int rowSpacing;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXGridLayoutWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXGridLayoutWidgetNode();
    }

    public View onCreateView(Context context) {
        return new DXNativeGridLayout(context);
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXGridLayoutWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            DXGridLayoutWidgetNode dXGridLayoutWidgetNode = (DXGridLayoutWidgetNode) dXWidgetNode;
            this.columnCount = dXGridLayoutWidgetNode.columnCount;
            this.columnSpacing = dXGridLayoutWidgetNode.columnSpacing;
            this.itemHeight = dXGridLayoutWidgetNode.itemHeight;
            this.itemWidth = dXGridLayoutWidgetNode.itemWidth;
            this.lineColor = dXGridLayoutWidgetNode.lineColor;
            this.lineWidth = dXGridLayoutWidgetNode.lineWidth;
            this.needSeparator = dXGridLayoutWidgetNode.needSeparator;
            this.rowCount = dXGridLayoutWidgetNode.rowCount;
            this.rowSpacing = dXGridLayoutWidgetNode.rowSpacing;
            this.linePts = dXGridLayoutWidgetNode.linePts;
            this.measuredRowCount = dXGridLayoutWidgetNode.measuredRowCount;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824);
        int makeMeasureSpec2 = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
        int i5 = 0;
        for (int i6 = 0; i6 < virtualChildCount; i6++) {
            measureChildWithMargins(getChildAt(i6), makeMeasureSpec, 0, makeMeasureSpec2, 0);
        }
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = mode != 1073741824;
        boolean z2 = mode2 != 1073741824;
        if (this.columnCount > 0) {
            if (this.rowCount > 0) {
                i5 = this.rowCount;
            } else if (virtualChildCount % this.columnCount == 0) {
                i5 = virtualChildCount / this.columnCount;
            } else {
                i5 = (virtualChildCount / this.columnCount) + 1;
            }
        }
        this.measuredRowCount = i5;
        if (z || z2) {
            if (!z) {
                i4 = DXWidgetNode.DXMeasureSpec.getSize(i);
            } else if (this.columnCount > 0) {
                i4 = (this.columnCount * this.itemWidth) + (this.columnSpacing * (this.columnCount - 1)) + this.paddingLeft + this.paddingRight;
            } else {
                i4 = this.paddingLeft + this.paddingRight;
            }
            if (!z2) {
                i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
            } else if (i5 > 0) {
                i3 = (this.itemHeight * i5) + (this.rowSpacing * (i5 - 1)) + this.paddingTop + this.paddingBottom;
            } else {
                i3 = this.paddingTop + this.paddingBottom;
            }
        } else {
            i4 = DXWidgetNode.DXMeasureSpec.getSize(i);
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(resolveSize(i4, i), resolveSize(i3, i2));
    }

    public void measureChildWithMargins(DXWidgetNode dXWidgetNode, int i, int i2, int i3, int i4) {
        dXWidgetNode.measure(getChildMeasureSpec(i, dXWidgetNode.marginLeft + dXWidgetNode.marginRight + i2, dXWidgetNode.layoutWidth), getChildMeasureSpec(i3, dXWidgetNode.marginTop + dXWidgetNode.marginBottom + i4, dXWidgetNode.layoutHeight));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        if (this.columnCount <= 0 || this.itemWidth <= 0 || this.itemHeight <= 0) {
            this.linePts = null;
            return;
        }
        int virtualChildCount = getVirtualChildCount();
        int i7 = 0;
        for (int i8 = 0; i8 < virtualChildCount; i8++) {
            DXWidgetNode virtualChildAt = getVirtualChildAt(i8);
            int i9 = i8 / this.columnCount;
            int i10 = i8 % this.columnCount;
            int i11 = (this.itemHeight * i9) + (i9 * this.rowSpacing) + this.paddingTop;
            int i12 = (this.itemWidth * i10) + (i10 * this.columnSpacing) + this.paddingLeft;
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 2)) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                int i13 = virtualChildAt.layoutGravity;
                if (i13 == 0 && (1 & virtualChildAt.propertyInitFlag) == 0) {
                    i13 = this.childGravity;
                }
                int absoluteGravity = getAbsoluteGravity(i13, getDirection());
                switch (absoluteGravity) {
                    case 3:
                    case 4:
                    case 5:
                        i5 = i12 + ((((this.itemWidth - measuredWidth) / 2) + virtualChildAt.getLeftMarginWithDirection()) - virtualChildAt.getRightMarginWithDirection());
                        break;
                    case 6:
                    case 7:
                    case 8:
                        i5 = i12 + ((this.itemWidth - measuredWidth) - virtualChildAt.getRightMarginWithDirection());
                        break;
                    default:
                        i5 = i12 + virtualChildAt.getLeftMarginWithDirection();
                        break;
                }
                switch (absoluteGravity) {
                    case 1:
                    case 4:
                    case 7:
                        i6 = i11 + ((((this.itemHeight - measuredHeight) / 2) + virtualChildAt.marginTop) - virtualChildAt.marginBottom);
                        break;
                    case 2:
                    case 5:
                    case 8:
                        i6 = i11 + ((this.itemHeight - measuredHeight) - virtualChildAt.marginBottom);
                        break;
                    default:
                        i6 = i11 + virtualChildAt.marginTop;
                        break;
                }
                virtualChildAt.layout(i5, i6, measuredWidth + i5, measuredHeight + i6);
            }
        }
        if (this.needSeparator) {
            float[] fArr = new float[(((this.columnCount - 1) + (this.measuredRowCount - 1)) * 4)];
            int i14 = this.measuredRowCount - 1;
            int i15 = 0;
            int i16 = 0;
            while (i15 < i14) {
                int i17 = i16 + 1;
                fArr[i16] = (float) this.paddingLeft;
                int i18 = i17 + 1;
                int i19 = i15 + 1;
                fArr[i17] = (float) ((this.itemHeight * i19) + (this.rowSpacing * i15) + (this.rowSpacing / 2) + this.paddingTop);
                int i20 = i18 + 1;
                fArr[i18] = (float) (getMeasuredWidth() - this.paddingRight);
                i16 = i20 + 1;
                fArr[i20] = (float) ((this.itemHeight * i19) + (i15 * this.rowSpacing) + (this.rowSpacing / 2) + this.paddingTop);
                i15 = i19;
            }
            int i21 = this.columnCount - 1;
            while (i7 < i21) {
                int i22 = i16 + 1;
                int i23 = i7 + 1;
                fArr[i16] = (float) ((this.itemWidth * i23) + (this.columnSpacing * i7) + (this.columnSpacing / 2) + this.paddingLeft);
                int i24 = i22 + 1;
                fArr[i22] = (float) this.paddingTop;
                int i25 = i24 + 1;
                fArr[i24] = (float) ((this.itemWidth * i23) + (i7 * this.columnSpacing) + (this.columnSpacing / 2) + this.paddingLeft);
                i16 = i25 + 1;
                fArr[i25] = (float) (getMeasuredHeight() - this.paddingBottom);
                i7 = i23;
            }
            this.linePts = fArr;
            int min = Math.min(this.columnSpacing, this.rowSpacing);
            if (this.lineWidth > min) {
                this.lineWidth = min;
            }
            setDisableFlatten(true);
            return;
        }
        this.linePts = null;
    }

    public void onSetIntAttribute(long j, int i) {
        if (j == DX_GRID_LAYOUT_COLUMN_COUNT) {
            this.columnCount = i;
        } else if (j == DX_GRID_LAYOUT_COLUMN_SPACING) {
            this.columnSpacing = i;
        } else if (j == DX_GRID_LAYOUT_ITEM_HEIGHT) {
            this.itemHeight = i;
        } else if (j == DX_GRID_LAYOUT_ITEM_WIDTH) {
            this.itemWidth = i;
        } else if (j == DX_GRID_LAYOUT_LINE_COLOR) {
            this.lineColor = i;
        } else if (j == DX_GRID_LAYOUT_LINE_WIDTH) {
            this.lineWidth = i;
        } else if (j == DX_GRID_LAYOUT_NEED_SEPARATOR) {
            this.needSeparator = i != 0;
        } else if (j == DX_GRID_LAYOUT_ROW_COUNT) {
            this.rowCount = i;
        } else if (j == DX_GRID_LAYOUT_ROW_SPACING) {
            this.rowSpacing = i;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == DX_GRID_LAYOUT_COLUMN_COUNT) {
            return 0;
        }
        if (j == DX_GRID_LAYOUT_LINE_COLOR) {
            return -8421505;
        }
        if (j == DX_GRID_LAYOUT_NEED_SEPARATOR || j == DX_GRID_LAYOUT_ROW_COUNT) {
            return 0;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        ((DXNativeGridLayout) view).setLines(this.needSeparator, this.lineColor, this.lineWidth, this.linePts);
    }
}
