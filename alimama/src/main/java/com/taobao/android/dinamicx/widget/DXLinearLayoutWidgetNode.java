package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.model.DXLayoutParamAttribute;
import com.taobao.android.dinamicx.view.CLipRadiusHandler;
import com.taobao.android.dinamicx.view.DXNativeLinearLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXLinearLayoutWidgetNode extends DXLayout implements Cloneable {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int mOrientation;
    int mTotalLength;

    /* access modifiers changed from: package-private */
    public int getChildrenSkipCount(DXWidgetNode dXWidgetNode, int i) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getLocationOffset(DXWidgetNode dXWidgetNode) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int measureNullChild(int i) {
        return 0;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXLinearLayoutWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXLinearLayoutWidgetNode();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXLinearLayoutWidgetNode) {
            this.mOrientation = ((DXLinearLayoutWidgetNode) dXWidgetNode).mOrientation;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0205  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0208  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureVertical(int r36, int r37) {
        /*
            r35 = this;
            r7 = r35
            r8 = r36
            r9 = r37
            int r10 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r36)
            int r11 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r37)
            r12 = 0
            r7.mTotalLength = r12
            int r13 = r35.getVirtualChildCount()
            r0 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r16 = 0
            r17 = 0
            r18 = 1
            r19 = 0
        L_0x0022:
            r1 = 2
            r20 = 0
            if (r6 >= r13) goto L_0x0137
            r22 = r4
            com.taobao.android.dinamicx.widget.DXWidgetNode r4 = r7.getChildAt(r6)
            if (r4 != 0) goto L_0x0039
            int r2 = r7.mTotalLength
            int r23 = r7.measureNullChild(r6)
            int r2 = r2 + r23
            r7.mTotalLength = r2
        L_0x0039:
            int r2 = r4.getVisibility()
            if (r2 != r1) goto L_0x004b
            int r1 = r7.getChildrenSkipCount(r4, r6)
            int r6 = r6 + r1
            r30 = r13
            r4 = r22
            r13 = 1
            goto L_0x0131
        L_0x004b:
            double r0 = (double) r0
            double r14 = r4.weight
            java.lang.Double.isNaN(r0)
            double r0 = r0 + r14
            float r14 = (float) r0
            double r0 = r4.weight
            int r2 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1))
            if (r2 <= 0) goto L_0x005b
            r4.layoutHeight = r12
        L_0x005b:
            int r0 = r4.layoutHeight
            if (r0 != 0) goto L_0x0069
            double r0 = r4.weight
            int r2 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1))
            if (r2 <= 0) goto L_0x0069
            r0 = 1073741824(0x40000000, float:2.0)
            r15 = 1
            goto L_0x006c
        L_0x0069:
            r0 = 1073741824(0x40000000, float:2.0)
            r15 = 0
        L_0x006c:
            if (r11 != r0) goto L_0x008c
            if (r15 == 0) goto L_0x008c
            int r0 = r7.mTotalLength
            int r1 = r4.marginTop
            int r1 = r1 + r0
            int r2 = r4.marginBottom
            int r1 = r1 + r2
            int r0 = java.lang.Math.max(r0, r1)
            r7.mTotalLength = r0
            r25 = r3
            r1 = r4
            r28 = r5
            r29 = r6
            r27 = r22
            r0 = 1073741824(0x40000000, float:2.0)
            r17 = 1
            goto L_0x00d8
        L_0x008c:
            if (r15 == 0) goto L_0x0091
            r0 = -2
            r4.layoutHeight = r0
        L_0x0091:
            r0 = 0
            int r1 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1))
            if (r1 != 0) goto L_0x009b
            int r0 = r7.mTotalLength
            r23 = r0
            goto L_0x009d
        L_0x009b:
            r23 = 0
        L_0x009d:
            r24 = 0
            r0 = r35
            r1 = r4
            r2 = r6
            r25 = r3
            r3 = r36
            r26 = r4
            r27 = r22
            r4 = r24
            r28 = r5
            r5 = r37
            r29 = r6
            r6 = r23
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            int r0 = r26.getMeasuredHeight()
            if (r15 == 0) goto L_0x00c5
            r1 = r26
            r1.layoutHeight = r12
            int r16 = r16 + r0
            goto L_0x00c7
        L_0x00c5:
            r1 = r26
        L_0x00c7:
            int r2 = r7.mTotalLength
            int r0 = r0 + r2
            int r3 = r1.marginTop
            int r0 = r0 + r3
            int r3 = r1.marginBottom
            int r0 = r0 + r3
            int r0 = java.lang.Math.max(r2, r0)
            r7.mTotalLength = r0
            r0 = 1073741824(0x40000000, float:2.0)
        L_0x00d8:
            if (r10 == r0) goto L_0x00e3
            int r0 = r1.layoutWidth
            r2 = -1
            if (r0 != r2) goto L_0x00e4
            r0 = 1
            r19 = 1
            goto L_0x00e5
        L_0x00e3:
            r2 = -1
        L_0x00e4:
            r0 = 0
        L_0x00e5:
            int r3 = r1.marginLeft
            int r4 = r1.marginRight
            int r3 = r3 + r4
            int r4 = r1.getMeasuredWidth()
            int r4 = r4 + r3
            r5 = r25
            int r5 = java.lang.Math.max(r5, r4)
            if (r18 == 0) goto L_0x00ff
            int r6 = r1.layoutWidth
            if (r6 != r2) goto L_0x00ff
            r30 = r13
            r2 = 1
            goto L_0x0102
        L_0x00ff:
            r30 = r13
            r2 = 0
        L_0x0102:
            double r12 = r1.weight
            int r6 = (r12 > r20 ? 1 : (r12 == r20 ? 0 : -1))
            if (r6 <= 0) goto L_0x0118
            if (r0 == 0) goto L_0x010d
        L_0x010a:
            r12 = r27
            goto L_0x010f
        L_0x010d:
            r3 = r4
            goto L_0x010a
        L_0x010f:
            int r4 = java.lang.Math.max(r12, r3)
            r0 = r28
        L_0x0115:
            r12 = r29
            goto L_0x0125
        L_0x0118:
            r12 = r27
            if (r0 == 0) goto L_0x011d
            r4 = r3
        L_0x011d:
            r3 = r28
            int r0 = java.lang.Math.max(r3, r4)
            r4 = r12
            goto L_0x0115
        L_0x0125:
            int r1 = r7.getChildrenSkipCount(r1, r12)
            int r6 = r12 + r1
            r18 = r2
            r3 = r5
            r13 = 1
            r5 = r0
            r0 = r14
        L_0x0131:
            int r6 = r6 + r13
            r13 = r30
            r12 = 0
            goto L_0x0022
        L_0x0137:
            r12 = r4
            r30 = r13
            r2 = -1
            r13 = 1
            r34 = r5
            r5 = r3
            r3 = r34
            int r4 = r7.mTotalLength
            int r6 = r7.paddingTop
            int r11 = r7.paddingBottom
            int r6 = r6 + r11
            int r4 = r4 + r6
            r7.mTotalLength = r4
            int r4 = r7.mTotalLength
            int r6 = r35.getSuggestedMinimumHeight()
            int r4 = java.lang.Math.max(r4, r6)
            r6 = 0
            int r4 = resolveSizeAndState(r4, r9, r6)
            r6 = 16777215(0xffffff, float:2.3509886E-38)
            r6 = r6 & r4
            int r11 = r7.mTotalLength
            int r6 = r6 - r11
            int r6 = r6 + r16
            if (r17 != 0) goto L_0x0179
            if (r6 == 0) goto L_0x016d
            r11 = 0
            int r11 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r11 <= 0) goto L_0x016d
            goto L_0x0179
        L_0x016d:
            int r0 = java.lang.Math.max(r3, r12)
            r32 = r4
            r6 = r5
            r5 = r0
            r0 = r30
            goto L_0x0239
        L_0x0179:
            r11 = 0
            r7.mTotalLength = r11
            r12 = r0
            r11 = r6
            r0 = r30
            r6 = r5
            r5 = r3
            r3 = 0
        L_0x0183:
            if (r3 >= r0) goto L_0x022d
            com.taobao.android.dinamicx.widget.DXWidgetNode r14 = r7.getVirtualChildAt(r3)
            int r15 = r14.getVisibility()
            if (r15 != r1) goto L_0x0196
            r33 = r3
            r32 = r4
            r13 = -1
            goto L_0x0224
        L_0x0196:
            double r1 = r14.weight
            int r15 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1))
            if (r15 <= 0) goto L_0x01d6
            r31 = r14
            double r13 = (double) r11
            java.lang.Double.isNaN(r13)
            double r13 = r13 * r1
            r33 = r3
            r32 = r4
            double r3 = (double) r12
            java.lang.Double.isNaN(r3)
            double r13 = r13 / r3
            int r12 = (int) r13
            java.lang.Double.isNaN(r3)
            double r3 = r3 - r1
            float r1 = (float) r3
            int r11 = r11 - r12
            r2 = 0
            int r3 = java.lang.Math.max(r2, r12)
            r2 = 1073741824(0x40000000, float:2.0)
            int r3 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.makeMeasureSpec(r3, r2)
            int r2 = r7.paddingLeft
            int r4 = r7.paddingRight
            int r2 = r2 + r4
            r4 = r31
            int r12 = r4.marginLeft
            int r2 = r2 + r12
            int r12 = r4.marginRight
            int r2 = r2 + r12
            int r12 = r4.layoutWidth
            int r2 = getChildMeasureSpec(r8, r2, r12)
            r4.measure(r2, r3)
            goto L_0x01dc
        L_0x01d6:
            r33 = r3
            r32 = r4
            r4 = r14
            r1 = r12
        L_0x01dc:
            int r2 = r4.marginLeft
            int r3 = r4.marginRight
            int r2 = r2 + r3
            int r3 = r4.getMeasuredWidth()
            int r3 = r3 + r2
            int r6 = java.lang.Math.max(r6, r3)
            r12 = 1073741824(0x40000000, float:2.0)
            if (r10 == r12) goto L_0x01f5
            int r12 = r4.layoutWidth
            r13 = -1
            if (r12 != r13) goto L_0x01f6
            r12 = 1
            goto L_0x01f7
        L_0x01f5:
            r13 = -1
        L_0x01f6:
            r12 = 0
        L_0x01f7:
            if (r12 == 0) goto L_0x01fa
            goto L_0x01fb
        L_0x01fa:
            r2 = r3
        L_0x01fb:
            int r2 = java.lang.Math.max(r5, r2)
            if (r18 == 0) goto L_0x0208
            int r3 = r4.layoutWidth
            if (r3 != r13) goto L_0x0208
            r18 = 1
            goto L_0x020a
        L_0x0208:
            r18 = 0
        L_0x020a:
            int r3 = r7.mTotalLength
            int r5 = r4.getMeasuredHeight()
            int r5 = r5 + r3
            int r12 = r4.marginTop
            int r5 = r5 + r12
            int r12 = r4.marginBottom
            int r5 = r5 + r12
            int r4 = r7.getNextLocationOffset(r4)
            int r5 = r5 + r4
            int r3 = java.lang.Math.max(r3, r5)
            r7.mTotalLength = r3
            r12 = r1
            r5 = r2
        L_0x0224:
            int r3 = r33 + 1
            r4 = r32
            r1 = 2
            r2 = -1
            r13 = 1
            goto L_0x0183
        L_0x022d:
            r32 = r4
            int r1 = r7.mTotalLength
            int r2 = r7.paddingTop
            int r3 = r7.paddingBottom
            int r2 = r2 + r3
            int r1 = r1 + r2
            r7.mTotalLength = r1
        L_0x0239:
            if (r18 != 0) goto L_0x0240
            r1 = 1073741824(0x40000000, float:2.0)
            if (r10 == r1) goto L_0x0240
            goto L_0x0241
        L_0x0240:
            r5 = r6
        L_0x0241:
            int r1 = r7.paddingLeft
            int r2 = r7.paddingRight
            int r1 = r1 + r2
            int r5 = r5 + r1
            int r1 = r35.getSuggestedMinimumWidth()
            int r1 = java.lang.Math.max(r5, r1)
            r2 = 0
            int r1 = resolveSizeAndState(r1, r8, r2)
            r2 = r32
            r7.setMeasuredDimension(r1, r2)
            if (r19 == 0) goto L_0x025e
            r7.forceUniformWidth(r0, r9)
        L_0x025e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXLinearLayoutWidgetNode.measureVertical(int, int):void");
    }

    /* access modifiers changed from: protected */
    public void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            DXWidgetNode virtualChildAt = getVirtualChildAt(i3);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 2 || virtualChildAt.layoutWidth != -1)) {
                int i4 = virtualChildAt.layoutHeight;
                virtualChildAt.layoutHeight = virtualChildAt.getMeasuredHeight();
                measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                virtualChildAt.layoutHeight = i4;
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0130  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureHorizontal(int r34, int r35) {
        /*
            r33 = this;
            r7 = r33
            r8 = r34
            r9 = r35
            r10 = 0
            r7.mTotalLength = r10
            int r11 = r33.getVirtualChildCount()
            int r12 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r34)
            int r13 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r35)
            r14 = 1073741824(0x40000000, float:2.0)
            if (r12 != r14) goto L_0x001c
            r16 = 1
            goto L_0x001e
        L_0x001c:
            r16 = 0
        L_0x001e:
            r17 = 0
            r0 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r18 = 0
            r19 = 0
            r20 = 1
            r21 = 0
        L_0x002d:
            r1 = 2
            r22 = 0
            if (r6 >= r11) goto L_0x0155
            com.taobao.android.dinamicx.widget.DXWidgetNode r15 = r7.getVirtualChildAt(r6)
            if (r15 != 0) goto L_0x0046
            int r1 = r7.mTotalLength
            int r2 = r7.measureNullChild(r6)
            int r1 = r1 + r2
            r7.mTotalLength = r1
        L_0x0041:
            r24 = r11
        L_0x0043:
            r10 = 1
            goto L_0x014f
        L_0x0046:
            int r2 = r15.getVisibility()
            if (r2 != r1) goto L_0x0052
            int r1 = r7.getChildrenSkipCount(r15, r6)
            int r6 = r6 + r1
            goto L_0x0041
        L_0x0052:
            double r0 = (double) r0
            r24 = r11
            double r10 = r15.weight
            java.lang.Double.isNaN(r0)
            double r0 = r0 + r10
            float r10 = (float) r0
            double r0 = r15.weight
            int r2 = (r0 > r22 ? 1 : (r0 == r22 ? 0 : -1))
            if (r2 <= 0) goto L_0x0065
            r0 = 0
            r15.layoutWidth = r0
        L_0x0065:
            int r0 = r15.layoutWidth
            if (r0 != 0) goto L_0x0071
            double r0 = r15.weight
            int r2 = (r0 > r22 ? 1 : (r0 == r22 ? 0 : -1))
            if (r2 <= 0) goto L_0x0071
            r11 = 1
            goto L_0x0072
        L_0x0071:
            r11 = 0
        L_0x0072:
            if (r12 != r14) goto L_0x009c
            if (r11 == 0) goto L_0x009c
            if (r16 == 0) goto L_0x0083
            int r0 = r7.mTotalLength
            int r1 = r15.marginLeft
            int r2 = r15.marginRight
            int r1 = r1 + r2
            int r0 = r0 + r1
            r7.mTotalLength = r0
            goto L_0x0091
        L_0x0083:
            int r0 = r7.mTotalLength
            int r1 = r15.marginLeft
            int r1 = r1 + r0
            int r2 = r15.marginRight
            int r1 = r1 + r2
            int r0 = java.lang.Math.max(r0, r1)
            r7.mTotalLength = r0
        L_0x0091:
            r25 = r3
            r26 = r4
            r27 = r5
            r28 = r6
            r19 = 1
            goto L_0x00f7
        L_0x009c:
            if (r11 == 0) goto L_0x00a1
            r0 = -2
            r15.layoutWidth = r0
        L_0x00a1:
            int r0 = (r10 > r17 ? 1 : (r10 == r17 ? 0 : -1))
            if (r0 != 0) goto L_0x00aa
            int r0 = r7.mTotalLength
            r22 = r0
            goto L_0x00ac
        L_0x00aa:
            r22 = 0
        L_0x00ac:
            r23 = 0
            r0 = r33
            r1 = r15
            r2 = r6
            r25 = r3
            r3 = r34
            r26 = r4
            r4 = r22
            r27 = r5
            r5 = r35
            r28 = r6
            r6 = r23
            r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6)
            int r0 = r15.getMeasuredWidth()
            if (r11 == 0) goto L_0x00d0
            r1 = 0
            r15.layoutWidth = r1
            int r18 = r18 + r0
        L_0x00d0:
            if (r16 == 0) goto L_0x00e3
            int r1 = r7.mTotalLength
            int r2 = r15.marginLeft
            int r0 = r0 + r2
            int r2 = r15.marginRight
            int r0 = r0 + r2
            int r2 = r7.getNextLocationOffset(r15)
            int r0 = r0 + r2
            int r1 = r1 + r0
            r7.mTotalLength = r1
            goto L_0x00f7
        L_0x00e3:
            int r1 = r7.mTotalLength
            int r0 = r0 + r1
            int r2 = r15.marginLeft
            int r0 = r0 + r2
            int r2 = r15.marginRight
            int r0 = r0 + r2
            int r2 = r7.getNextLocationOffset(r15)
            int r0 = r0 + r2
            int r0 = java.lang.Math.max(r1, r0)
            r7.mTotalLength = r0
        L_0x00f7:
            if (r13 == r14) goto L_0x0102
            int r0 = r15.layoutHeight
            r2 = -1
            if (r0 != r2) goto L_0x0103
            r0 = 1
            r21 = 1
            goto L_0x0104
        L_0x0102:
            r2 = -1
        L_0x0103:
            r0 = 0
        L_0x0104:
            int r1 = r15.marginTop
            int r3 = r15.marginBottom
            int r1 = r1 + r3
            int r3 = r15.getMeasuredHeight()
            int r3 = r3 + r1
            r4 = r25
            int r4 = java.lang.Math.max(r4, r3)
            if (r20 == 0) goto L_0x011c
            int r5 = r15.layoutHeight
            if (r5 != r2) goto L_0x011c
            r2 = 1
            goto L_0x011d
        L_0x011c:
            r2 = 0
        L_0x011d:
            int r5 = r15.layoutWidth
            if (r5 <= 0) goto L_0x0130
            if (r0 == 0) goto L_0x0126
        L_0x0123:
            r5 = r26
            goto L_0x0128
        L_0x0126:
            r1 = r3
            goto L_0x0123
        L_0x0128:
            int r0 = java.lang.Math.max(r5, r1)
            r5 = r0
        L_0x012d:
            r0 = r28
            goto L_0x0140
        L_0x0130:
            r5 = r26
            if (r0 == 0) goto L_0x0137
        L_0x0134:
            r3 = r27
            goto L_0x0139
        L_0x0137:
            r1 = r3
            goto L_0x0134
        L_0x0139:
            int r0 = java.lang.Math.max(r3, r1)
            r27 = r0
            goto L_0x012d
        L_0x0140:
            int r1 = r7.getChildrenSkipCount(r15, r0)
            int r6 = r0 + r1
            r20 = r2
            r3 = r4
            r4 = r5
            r0 = r10
            r5 = r27
            goto L_0x0043
        L_0x014f:
            int r6 = r6 + r10
            r11 = r24
            r10 = 0
            goto L_0x002d
        L_0x0155:
            r24 = r11
            r2 = -1
            r10 = 1
            r32 = r4
            r4 = r3
            r3 = r5
            r5 = r32
            int r6 = r7.mTotalLength
            int r11 = r7.paddingLeft
            int r12 = r7.paddingRight
            int r11 = r11 + r12
            int r6 = r6 + r11
            r7.mTotalLength = r6
            int r6 = r7.mTotalLength
            int r11 = r33.getSuggestedMinimumWidth()
            int r6 = java.lang.Math.max(r6, r11)
            r11 = 0
            int r6 = resolveSizeAndState(r6, r8, r11)
            r11 = 16777215(0xffffff, float:2.3509886E-38)
            r11 = r11 & r6
            int r12 = r7.mTotalLength
            int r11 = r11 - r12
            int r11 = r11 + r18
            if (r19 != 0) goto L_0x0193
            if (r11 == 0) goto L_0x018a
            int r12 = (r0 > r17 ? 1 : (r0 == r17 ? 0 : -1))
            if (r12 <= 0) goto L_0x018a
            goto L_0x0193
        L_0x018a:
            int r0 = java.lang.Math.max(r3, r5)
            r1 = r0
            r0 = r24
            goto L_0x026f
        L_0x0193:
            r4 = 0
            r7.mTotalLength = r4
            r12 = r0
            r4 = r3
            r0 = r24
            r3 = 0
            r5 = -1
        L_0x019c:
            if (r3 >= r0) goto L_0x0263
            com.taobao.android.dinamicx.widget.DXWidgetNode r15 = r7.getVirtualChildAt(r3)
            if (r15 == 0) goto L_0x0254
            int r10 = r15.getVisibility()
            if (r10 != r1) goto L_0x01ac
            goto L_0x0254
        L_0x01ac:
            double r1 = r15.weight
            int r10 = (r1 > r22 ? 1 : (r1 == r22 ? 0 : -1))
            if (r10 <= 0) goto L_0x01ec
            r29 = r15
            double r14 = (double) r11
            java.lang.Double.isNaN(r14)
            double r14 = r14 * r1
            r30 = r3
            r31 = r4
            double r3 = (double) r12
            java.lang.Double.isNaN(r3)
            double r14 = r14 / r3
            int r10 = (int) r14
            int r11 = r11 - r10
            java.lang.Double.isNaN(r3)
            double r3 = r3 - r1
            float r12 = (float) r3
            r1 = 0
            int r2 = java.lang.Math.max(r1, r10)
            r1 = 1073741824(0x40000000, float:2.0)
            int r2 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.makeMeasureSpec(r2, r1)
            int r1 = r7.paddingTop
            int r3 = r7.paddingBottom
            int r1 = r1 + r3
            r3 = r29
            int r4 = r3.marginTop
            int r1 = r1 + r4
            int r4 = r3.marginBottom
            int r1 = r1 + r4
            int r4 = r3.layoutHeight
            int r1 = getChildMeasureSpec(r9, r1, r4)
            r3.measure(r2, r1)
            goto L_0x01f1
        L_0x01ec:
            r30 = r3
            r31 = r4
            r3 = r15
        L_0x01f1:
            if (r16 == 0) goto L_0x020a
            int r1 = r7.mTotalLength
            int r2 = r3.getMeasuredWidth()
            int r4 = r3.marginLeft
            int r2 = r2 + r4
            int r4 = r3.marginRight
            int r2 = r2 + r4
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = r1 + r2
            r7.mTotalLength = r1
        L_0x0207:
            r1 = 1073741824(0x40000000, float:2.0)
            goto L_0x0223
        L_0x020a:
            int r1 = r7.mTotalLength
            int r2 = r3.getMeasuredWidth()
            int r2 = r2 + r1
            int r4 = r3.marginLeft
            int r2 = r2 + r4
            int r4 = r3.marginRight
            int r2 = r2 + r4
            int r4 = r7.getNextLocationOffset(r3)
            int r2 = r2 + r4
            int r1 = java.lang.Math.max(r1, r2)
            r7.mTotalLength = r1
            goto L_0x0207
        L_0x0223:
            if (r13 == r1) goto L_0x022c
            int r1 = r3.layoutHeight
            r2 = -1
            if (r1 != r2) goto L_0x022c
            r1 = 1
            goto L_0x022d
        L_0x022c:
            r1 = 0
        L_0x022d:
            int r2 = r3.marginTop
            int r4 = r3.marginBottom
            int r2 = r2 + r4
            int r4 = r3.getMeasuredHeight()
            int r4 = r4 + r2
            int r5 = java.lang.Math.max(r5, r4)
            if (r1 == 0) goto L_0x0240
        L_0x023d:
            r1 = r31
            goto L_0x0242
        L_0x0240:
            r2 = r4
            goto L_0x023d
        L_0x0242:
            int r1 = java.lang.Math.max(r1, r2)
            if (r20 == 0) goto L_0x0250
            int r2 = r3.layoutHeight
            r3 = -1
            if (r2 != r3) goto L_0x0251
            r20 = 1
            goto L_0x0258
        L_0x0250:
            r3 = -1
        L_0x0251:
            r20 = 0
            goto L_0x0258
        L_0x0254:
            r30 = r3
            r1 = r4
            r3 = -1
        L_0x0258:
            r4 = r1
            int r1 = r30 + 1
            r3 = r1
            r1 = 2
            r2 = -1
            r10 = 1
            r14 = 1073741824(0x40000000, float:2.0)
            goto L_0x019c
        L_0x0263:
            r1 = r4
            int r2 = r7.mTotalLength
            int r3 = r7.paddingLeft
            int r4 = r7.paddingRight
            int r3 = r3 + r4
            int r2 = r2 + r3
            r7.mTotalLength = r2
            r4 = r5
        L_0x026f:
            if (r20 != 0) goto L_0x0276
            r2 = 1073741824(0x40000000, float:2.0)
            if (r13 == r2) goto L_0x0276
            goto L_0x0277
        L_0x0276:
            r1 = r4
        L_0x0277:
            int r2 = r7.paddingTop
            int r3 = r7.paddingBottom
            int r2 = r2 + r3
            int r1 = r1 + r2
            int r2 = r33.getSuggestedMinimumHeight()
            int r1 = java.lang.Math.max(r1, r2)
            r2 = 0
            r3 = r6 | 0
            int r1 = resolveSizeAndState(r1, r9, r2)
            r7.setMeasuredDimension(r3, r1)
            if (r21 == 0) goto L_0x0294
            r7.forceUniformHeight(r0, r8)
        L_0x0294:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXLinearLayoutWidgetNode.measureHorizontal(int, int):void");
    }

    /* access modifiers changed from: protected */
    public void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            DXWidgetNode virtualChildAt = getVirtualChildAt(i3);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 2 || virtualChildAt.layoutHeight != -1)) {
                int i4 = virtualChildAt.layoutWidth;
                virtualChildAt.layoutWidth = virtualChildAt.getMeasuredWidth();
                measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                virtualChildAt.layoutWidth = i4;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void measureChildBeforeLayout(DXWidgetNode dXWidgetNode, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(dXWidgetNode, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: package-private */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int paddingLeftWithDirection;
        int direction = getDirection();
        int i6 = i3 - i;
        int paddingRightWithDirection = i6 - getPaddingRightWithDirection();
        int paddingLeftWithDirection2 = (i6 - getPaddingLeftWithDirection()) - getPaddingRightWithDirection();
        int virtualChildCount = getVirtualChildCount();
        switch (this.childGravity) {
            case 1:
            case 4:
            case 7:
                i5 = this.paddingTop + (((i4 - i2) - this.mTotalLength) / 2);
                break;
            case 2:
            case 5:
            case 8:
                i5 = ((this.paddingTop + i4) - i2) - this.mTotalLength;
                break;
            default:
                i5 = this.paddingTop;
                break;
        }
        int i7 = 0;
        while (i7 < virtualChildCount) {
            DXWidgetNode virtualChildAt = getVirtualChildAt(i7);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i7);
            } else if (virtualChildAt.getVisibility() != 2) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                int i8 = virtualChildAt.layoutGravity;
                if ((virtualChildAt.propertyInitFlag & 1) == 0 && i8 == 0) {
                    i8 = this.childGravity;
                }
                switch (getAbsoluteGravity(i8, direction)) {
                    case 3:
                    case 4:
                    case 5:
                        paddingLeftWithDirection = ((getPaddingLeftWithDirection() + ((paddingLeftWithDirection2 - measuredWidth) / 2)) + virtualChildAt.getLeftMarginWithDirection()) - virtualChildAt.getRightMarginWithDirection();
                        break;
                    case 6:
                    case 7:
                    case 8:
                        paddingLeftWithDirection = (paddingRightWithDirection - measuredWidth) - virtualChildAt.getRightMarginWithDirection();
                        break;
                    default:
                        paddingLeftWithDirection = getPaddingLeftWithDirection() + virtualChildAt.getLeftMarginWithDirection();
                        break;
                }
                int i9 = paddingLeftWithDirection;
                int i10 = i5 + virtualChildAt.marginTop;
                setChildFrame(virtualChildAt, i9, i10 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i5 = i10 + measuredHeight + virtualChildAt.marginBottom + getNextLocationOffset(virtualChildAt);
                i7 += getChildrenSkipCount(virtualChildAt, i7);
            }
            i7++;
        }
    }

    private void setChildFrame(DXWidgetNode dXWidgetNode, int i, int i2, int i3, int i4) {
        dXWidgetNode.layout(i, i2, i3 + i, i4 + i2);
    }

    /* access modifiers changed from: package-private */
    public void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean isLayoutRtl = isLayoutRtl();
        int i10 = this.paddingTop;
        int i11 = i4 - i2;
        int i12 = i11 - this.paddingBottom;
        int i13 = (i11 - i10) - this.paddingBottom;
        int virtualChildCount = getVirtualChildCount();
        int i14 = 2;
        switch (getAbsoluteGravity(this.childGravity, getDirection())) {
            case 3:
            case 4:
            case 5:
                i5 = getPaddingLeftWithDirection() + (((i3 - i) - this.mTotalLength) / 2);
                break;
            case 6:
            case 7:
            case 8:
                i5 = ((getPaddingLeftWithDirection() + i3) - i) - this.mTotalLength;
                break;
            default:
                i5 = getPaddingLeftWithDirection();
                break;
        }
        if (isLayoutRtl) {
            i7 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i7 = 0;
            i6 = 1;
        }
        int i15 = 0;
        while (i15 < virtualChildCount) {
            int i16 = i7 + (i6 * i15);
            DXWidgetNode virtualChildAt = getVirtualChildAt(i16);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i16);
            } else if (virtualChildAt.getVisibility() != i14) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                int i17 = virtualChildAt.layoutGravity;
                if ((virtualChildAt.propertyInitFlag & 1) == 0 && i17 == 0) {
                    i17 = this.childGravity;
                }
                switch (i17) {
                    case 0:
                    case 3:
                    case 6:
                        i9 = virtualChildAt.marginTop + i10;
                        break;
                    case 1:
                    case 4:
                    case 7:
                        i9 = ((((i13 - measuredHeight) / i14) + i10) + virtualChildAt.marginTop) - virtualChildAt.marginBottom;
                        break;
                    case 2:
                    case 5:
                    case 8:
                        i9 = (i12 - measuredHeight) - virtualChildAt.marginBottom;
                        break;
                    default:
                        i8 = i10;
                        break;
                }
                i8 = i9;
                int leftMarginWithDirection = i5 + virtualChildAt.getLeftMarginWithDirection();
                DXWidgetNode dXWidgetNode = virtualChildAt;
                setChildFrame(virtualChildAt, leftMarginWithDirection + getLocationOffset(virtualChildAt), i8, measuredWidth, measuredHeight);
                int rightMarginWithDirection = measuredWidth + dXWidgetNode.getRightMarginWithDirection();
                DXWidgetNode dXWidgetNode2 = dXWidgetNode;
                i15 += getChildrenSkipCount(dXWidgetNode2, i16);
                i5 = leftMarginWithDirection + rightMarginWithDirection + getNextLocationOffset(dXWidgetNode2);
            }
            i15++;
            i14 = 2;
        }
    }

    public void onSetIntAttribute(long j, int i) {
        if (-7199229155167727177L == j) {
            this.mOrientation = i;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public ViewGroup.LayoutParams generateLayoutParams(DXLayoutParamAttribute dXLayoutParamAttribute) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dXLayoutParamAttribute.widthAttr, dXLayoutParamAttribute.heightAttr);
        layoutParams.gravity = dXLayoutParamAttribute.layoutGravityAttr;
        return layoutParams;
    }

    public ViewGroup.LayoutParams generateLayoutParams(@NonNull DXLayoutParamAttribute dXLayoutParamAttribute, @NonNull ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).gravity = dXLayoutParamAttribute.layoutGravityAttr;
        }
        layoutParams.width = dXLayoutParamAttribute.widthAttr;
        layoutParams.height = dXLayoutParamAttribute.heightAttr;
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null && (view instanceof LinearLayout)) {
            ((LinearLayout) view).setOrientation(this.mOrientation);
        }
        super.onRenderView(context, view);
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeLinearLayout(context);
    }

    public void setBackground(View view) {
        if (view instanceof DXNativeLinearLayout) {
            if (hasCornerRadius()) {
                DXNativeLinearLayout dXNativeLinearLayout = (DXNativeLinearLayout) view;
                CLipRadiusHandler cLipRadiusHandler = new CLipRadiusHandler();
                if (this.cornerRadius > 0) {
                    cLipRadiusHandler.setRadius(view, (float) this.cornerRadius);
                } else {
                    cLipRadiusHandler.setRadius(view, (float) this.cornerRadiusLeftTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusLeftBottom, (float) this.cornerRadiusRightBottom);
                }
                dXNativeLinearLayout.setClipRadiusHandler(cLipRadiusHandler);
            } else {
                CLipRadiusHandler cLipRadiusHandler2 = ((DXNativeLinearLayout) view).getCLipRadiusHandler();
                if (cLipRadiusHandler2 != null) {
                    cLipRadiusHandler2.setRadius(view, 0.0f);
                }
            }
        }
        super.setBackground(view);
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }
}
