package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import com.taobao.android.dinamicx.view.DXNativeAdaptiveLinearLayout;

public class DXAdaptiveLinearLayoutWidgetNode extends DXLinearLayoutWidgetNode {

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXAdaptiveLinearLayoutWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXAdaptiveLinearLayoutWidgetNode();
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeAdaptiveLinearLayout(context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int r22, int r23) {
        /*
            r21 = this;
            r6 = r21
            r7 = r23
            int r8 = android.view.View.MeasureSpec.getMode(r22)
            int r9 = android.view.View.MeasureSpec.getMode(r23)
            int r10 = android.view.View.MeasureSpec.getSize(r23)
            r11 = 0
            r6.mTotalLength = r11
            int r12 = r21.getVirtualChildCount()
            r5 = 0
            r14 = 0
            r15 = 0
            r16 = 1
            r17 = 0
        L_0x001e:
            r4 = 1073741824(0x40000000, float:2.0)
            if (r14 >= r12) goto L_0x009e
            com.taobao.android.dinamicx.widget.DXWidgetNode r3 = r6.getVirtualChildAt(r14)
            if (r3 == 0) goto L_0x0098
            int r0 = r3.getVisibility()
            r1 = 2
            if (r0 != r1) goto L_0x0031
            goto L_0x0098
        L_0x0031:
            r18 = 0
            r19 = 0
            r0 = r21
            r1 = r3
            r2 = r22
            r20 = r3
            r3 = r18
            r13 = 1073741824(0x40000000, float:2.0)
            r4 = r23
            r11 = r5
            r5 = r19
            r0.measureChildWithMargins(r1, r2, r3, r4, r5)
            int r0 = r20.getMeasuredHeight()
            int r1 = r6.mTotalLength
            int r2 = r6.mTotalLength
            int r2 = r2 + r0
            int r3 = r20.getMarginTop()
            int r2 = r2 + r3
            int r3 = r20.getMarginBottom()
            int r2 = r2 + r3
            int r1 = java.lang.Math.max(r1, r2)
            r6.mTotalLength = r1
            r1 = -1
            if (r8 == r13) goto L_0x006e
            r2 = r20
            int r3 = r2.layoutWidth
            if (r3 != r1) goto L_0x0070
            r3 = 1
            r17 = 1
            goto L_0x0071
        L_0x006e:
            r2 = r20
        L_0x0070:
            r3 = 0
        L_0x0071:
            int r4 = r2.getMarginLeft()
            int r5 = r2.getMarginRight()
            int r4 = r4 + r5
            int r5 = r2.getMeasuredWidth()
            int r5 = r5 + r4
            int r5 = java.lang.Math.max(r15, r5)
            if (r16 == 0) goto L_0x008b
            int r2 = r2.layoutHeight
            if (r2 != r1) goto L_0x008b
            r1 = 1
            goto L_0x008c
        L_0x008b:
            r1 = 0
        L_0x008c:
            if (r3 == 0) goto L_0x008f
            r0 = r4
        L_0x008f:
            int r0 = java.lang.Math.max(r11, r0)
            r16 = r1
            r15 = r5
            r5 = r0
            goto L_0x009a
        L_0x0098:
            r11 = r5
            r5 = r11
        L_0x009a:
            int r14 = r14 + 1
            r11 = 0
            goto L_0x001e
        L_0x009e:
            r11 = r5
            r13 = 1073741824(0x40000000, float:2.0)
            int r0 = r6.mTotalLength
            int r1 = r6.paddingTop
            int r2 = r6.paddingBottom
            int r1 = r1 + r2
            int r0 = r0 + r1
            r6.mTotalLength = r0
            int r0 = r6.mTotalLength
            int r1 = r21.getSuggestedMinimumHeight()
            int r0 = java.lang.Math.max(r0, r1)
            if (r0 <= r10) goto L_0x00d8
            int r1 = r12 + -1
        L_0x00b9:
            if (r1 < 0) goto L_0x00d8
            com.taobao.android.dinamicx.widget.DXWidgetNode r2 = r6.getVirtualChildAt(r1)
            int r3 = r2.getMeasuredHeight()
            int r0 = r0 - r3
            int r3 = r2.getMarginTop()
            int r0 = r0 - r3
            int r3 = r2.getMarginBottom()
            int r0 = r0 - r3
            r3 = 0
            r2.setMeasuredDimension(r3, r3)
            if (r0 > r10) goto L_0x00d5
            goto L_0x00d8
        L_0x00d5:
            int r1 = r1 + -1
            goto L_0x00b9
        L_0x00d8:
            if (r16 != 0) goto L_0x00dd
            if (r9 == r13) goto L_0x00dd
            r15 = r11
        L_0x00dd:
            int r1 = r6.paddingLeft
            int r2 = r6.paddingRight
            int r1 = r1 + r2
            int r15 = r15 + r1
            int r1 = r21.getSuggestedMinimumWidth()
            int r1 = java.lang.Math.max(r15, r1)
            r2 = r22
            int r1 = resolveSize(r1, r2)
            int r2 = resolveSize(r0, r7)
            r6.setMeasuredDimension(r1, r2)
            r6.mTotalLength = r0
            if (r17 == 0) goto L_0x00ff
            r6.forceUniformWidth(r12, r7)
        L_0x00ff:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXAdaptiveLinearLayoutWidgetNode.measureVertical(int, int):void");
    }

    /* access modifiers changed from: protected */
    public void measureHorizontal(int i, int i2) {
        boolean z;
        int i3 = i;
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i2);
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int i4 = 0;
        int i5 = 0;
        boolean z2 = true;
        boolean z3 = false;
        for (int i6 = 0; i6 < virtualChildCount; i6++) {
            DXWidgetNode virtualChildAt = getVirtualChildAt(i6);
            if (virtualChildAt == null || virtualChildAt.getVisibility() == 2) {
                i4 = i4;
            } else {
                DXWidgetNode dXWidgetNode = virtualChildAt;
                int i7 = i4;
                measureChildWithMargins(virtualChildAt, i, 0, i2, 0);
                int measuredWidth = dXWidgetNode.getMeasuredWidth();
                int i8 = this.mTotalLength;
                int i9 = this.mTotalLength + measuredWidth;
                DXWidgetNode dXWidgetNode2 = dXWidgetNode;
                this.mTotalLength = Math.max(i8, i9 + dXWidgetNode2.marginLeft + dXWidgetNode2.marginRight);
                if (mode == 1073741824 || dXWidgetNode2.layoutHeight != -1) {
                    z = false;
                } else {
                    z = true;
                    z3 = true;
                }
                int i10 = dXWidgetNode2.marginTop + dXWidgetNode2.marginBottom;
                int measuredHeight = dXWidgetNode2.getMeasuredHeight() + i10;
                int max = Math.max(i5, measuredHeight);
                boolean z4 = z2 && dXWidgetNode2.layoutHeight == -1;
                if (!z) {
                    i10 = measuredHeight;
                }
                z2 = z4;
                i5 = max;
                i4 = Math.max(i7, i10);
            }
        }
        int i11 = i4;
        this.mTotalLength += this.paddingLeft + this.paddingRight;
        int max2 = Math.max(this.mTotalLength, getSuggestedMinimumWidth());
        if (max2 > size) {
            for (int i12 = virtualChildCount - 1; i12 >= 0; i12--) {
                DXWidgetNode virtualChildAt2 = getVirtualChildAt(i12);
                max2 = ((max2 - virtualChildAt2.getMeasuredWidth()) - virtualChildAt2.getMarginLeft()) - virtualChildAt2.getMarginRight();
                virtualChildAt2.setMeasuredDimension(0, 0);
                if (max2 <= size) {
                    break;
                }
            }
        }
        if (!z2 && mode != 1073741824) {
            i5 = i11;
        }
        setMeasuredDimension(resolveSize(max2, i3), resolveSize(Math.max(i5 + this.paddingTop + this.paddingBottom, getSuggestedMinimumHeight()), i2));
        this.mTotalLength = max2;
        if (z3) {
            forceUniformHeight(virtualChildCount, i3);
        }
    }
}
