package com.taobao.android.dxcontainer.vlayout.layout;

import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class LinearLayoutHelper extends BaseLayoutHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "LinearLayoutHelper";
    private int mDividerHeight;
    private boolean mLayoutWithAnchor;

    public LinearLayoutHelper() {
        this(0);
    }

    public LinearLayoutHelper(int i) {
        this(i, 0);
    }

    public LinearLayoutHelper(int i, int i2) {
        this.mDividerHeight = 0;
        this.mLayoutWithAnchor = false;
        setItemCount(i2);
        setDividerHeight(i);
    }

    public void setDividerHeight(int i) {
        if (i < 0) {
            i = 0;
        }
        this.mDividerHeight = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0161  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0165  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x017d  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01d8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutViews(androidx.recyclerview.widget.RecyclerView.Recycler r21, androidx.recyclerview.widget.RecyclerView.State r22, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutStateWrapper r23, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult r24, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r25) {
        /*
            r20 = this;
            r7 = r20
            r8 = r24
            r6 = r25
            int r0 = r23.getCurrentPosition()
            boolean r0 = r7.isOutOfRange(r0)
            if (r0 == 0) goto L_0x0011
            return
        L_0x0011:
            int r0 = r23.getCurrentPosition()
            r1 = r21
            r2 = r23
            android.view.View r9 = r7.nextView(r1, r2, r6, r8)
            if (r9 != 0) goto L_0x0020
            return
        L_0x0020:
            boolean r1 = r25.isEnableMarginOverLap()
            android.view.ViewGroup$LayoutParams r3 = r9.getLayoutParams()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r3 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r3
            int r4 = r25.getOrientation()
            r5 = 1
            if (r4 != r5) goto L_0x0033
            r4 = 1
            goto L_0x0034
        L_0x0033:
            r4 = 0
        L_0x0034:
            int r11 = r23.getLayoutDirection()
            if (r11 != r5) goto L_0x003c
            r11 = 1
            goto L_0x003d
        L_0x003c:
            r11 = 0
        L_0x003d:
            if (r11 == 0) goto L_0x0053
            com.taobao.android.dxcontainer.vlayout.Range r12 = r20.getRange()
            java.lang.Comparable r12 = r12.getLower()
            java.lang.Integer r12 = (java.lang.Integer) r12
            int r12 = r12.intValue()
            if (r0 != r12) goto L_0x0051
        L_0x004f:
            r12 = 1
            goto L_0x0064
        L_0x0051:
            r12 = 0
            goto L_0x0064
        L_0x0053:
            com.taobao.android.dxcontainer.vlayout.Range r12 = r20.getRange()
            java.lang.Comparable r12 = r12.getUpper()
            java.lang.Integer r12 = (java.lang.Integer) r12
            int r12 = r12.intValue()
            if (r0 != r12) goto L_0x0051
            goto L_0x004f
        L_0x0064:
            if (r11 == 0) goto L_0x007a
            com.taobao.android.dxcontainer.vlayout.Range r13 = r20.getRange()
            java.lang.Comparable r13 = r13.getUpper()
            java.lang.Integer r13 = (java.lang.Integer) r13
            int r13 = r13.intValue()
            if (r0 != r13) goto L_0x0078
        L_0x0076:
            r13 = 1
            goto L_0x008b
        L_0x0078:
            r13 = 0
            goto L_0x008b
        L_0x007a:
            com.taobao.android.dxcontainer.vlayout.Range r13 = r20.getRange()
            java.lang.Comparable r13 = r13.getLower()
            java.lang.Integer r13 = (java.lang.Integer) r13
            int r13 = r13.intValue()
            if (r0 != r13) goto L_0x0078
            goto L_0x0076
        L_0x008b:
            if (r12 == 0) goto L_0x0092
            int r14 = r7.computeStartSpace(r6, r4, r11, r1)
            goto L_0x0093
        L_0x0092:
            r14 = 0
        L_0x0093:
            if (r13 == 0) goto L_0x009a
            int r13 = r7.computeEndSpace(r6, r4, r11, r1)
            goto L_0x009b
        L_0x009a:
            r13 = 0
        L_0x009b:
            if (r12 != 0) goto L_0x00e5
            if (r1 != 0) goto L_0x00a7
            boolean r0 = r7.mLayoutWithAnchor
            if (r0 == 0) goto L_0x00a4
            goto L_0x00e5
        L_0x00a4:
            int r0 = r7.mDividerHeight
            goto L_0x00e6
        L_0x00a7:
            if (r11 == 0) goto L_0x00c7
            int r11 = r3.topMargin
            int r0 = r0 - r5
            android.view.View r0 = r6.findViewByPosition(r0)
            if (r0 == 0) goto L_0x00bb
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r0 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r0
            int r0 = r0.bottomMargin
            goto L_0x00bc
        L_0x00bb:
            r0 = 0
        L_0x00bc:
            if (r0 < 0) goto L_0x00c5
            if (r11 < 0) goto L_0x00c5
            int r0 = java.lang.Math.max(r0, r11)
            goto L_0x00e6
        L_0x00c5:
            int r0 = r0 + r11
            goto L_0x00e6
        L_0x00c7:
            int r11 = r3.bottomMargin
            int r0 = r0 + r5
            android.view.View r0 = r6.findViewByPosition(r0)
            if (r0 == 0) goto L_0x00d9
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r0 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r0
            int r0 = r0.topMargin
            goto L_0x00da
        L_0x00d9:
            r0 = 0
        L_0x00da:
            if (r11 < 0) goto L_0x00e3
            if (r0 < 0) goto L_0x00e3
            int r0 = java.lang.Math.max(r11, r0)
            goto L_0x00e6
        L_0x00e3:
            int r0 = r0 + r11
            goto L_0x00e6
        L_0x00e5:
            r0 = 0
        L_0x00e6:
            int r11 = r25.getContentWidth()
            int r15 = r25.getPaddingLeft()
            int r11 = r11 - r15
            int r15 = r25.getPaddingRight()
            int r11 = r11 - r15
            int r15 = r20.getHorizontalMargin()
            int r11 = r11 - r15
            int r15 = r20.getHorizontalPadding()
            int r11 = r11 - r15
            int r15 = r3.width
            r10 = r4 ^ 1
            int r10 = r6.getChildMeasureSpec(r11, r15, r10)
            float r15 = r3.mAspectRatio
            boolean r16 = java.lang.Float.isNaN(r15)
            r5 = 1073741824(0x40000000, float:2.0)
            r17 = 0
            if (r16 != 0) goto L_0x0121
            int r16 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r16 <= 0) goto L_0x0121
            float r3 = (float) r11
            float r3 = r3 / r15
            r4 = 1056964608(0x3f000000, float:0.5)
            float r3 = r3 + r4
            int r3 = (int) r3
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r5)
            goto L_0x015f
        L_0x0121:
            float r15 = r7.mAspectRatio
            boolean r15 = java.lang.Float.isNaN(r15)
            if (r15 != 0) goto L_0x0141
            float r15 = r7.mAspectRatio
            int r15 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r15 <= 0) goto L_0x0141
            float r3 = (float) r11
            float r4 = r7.mAspectRatio
            float r3 = r3 / r4
            double r3 = (double) r3
            r17 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Double.isNaN(r3)
            double r3 = r3 + r17
            int r3 = (int) r3
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r5)
            goto L_0x015f
        L_0x0141:
            int r5 = r25.getContentHeight()
            int r11 = r25.getPaddingTop()
            int r5 = r5 - r11
            int r11 = r25.getPaddingBottom()
            int r5 = r5 - r11
            int r11 = r20.getVerticalMargin()
            int r5 = r5 - r11
            int r11 = r20.getVerticalPadding()
            int r5 = r5 - r11
            int r3 = r3.height
            int r3 = r6.getChildMeasureSpec(r5, r3, r4)
        L_0x015f:
            if (r1 != 0) goto L_0x0165
            r6.measureChildWithMargins(r9, r10, r3)
            goto L_0x0168
        L_0x0165:
            r6.measureChild(r9, r10, r3)
        L_0x0168:
            com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r1 = r25.getMainOrientationHelper()
            int r3 = r1.getDecoratedMeasurement(r9)
            int r3 = r3 + r14
            int r3 = r3 + r13
            int r3 = r3 + r0
            r8.mConsumed = r3
            int r3 = r25.getOrientation()
            r4 = -1
            r5 = 1
            if (r3 != r5) goto L_0x01d8
            boolean r3 = r25.isDoLayoutRTL()
            if (r3 == 0) goto L_0x0199
            int r3 = r25.getContentWidth()
            int r5 = r25.getPaddingRight()
            int r3 = r3 - r5
            int r5 = r7.mMarginRight
            int r3 = r3 - r5
            int r5 = r7.mPaddingRight
            int r3 = r3 - r5
            int r5 = r1.getDecoratedMeasurementInOther(r9)
            int r5 = r3 - r5
            goto L_0x01a8
        L_0x0199:
            int r3 = r25.getPaddingLeft()
            int r5 = r7.mMarginLeft
            int r3 = r3 + r5
            int r5 = r7.mPaddingLeft
            int r5 = r5 + r3
            int r3 = r1.getDecoratedMeasurementInOther(r9)
            int r3 = r3 + r5
        L_0x01a8:
            int r10 = r23.getLayoutDirection()
            if (r10 != r4) goto L_0x01c5
            int r2 = r23.getOffset()
            int r2 = r2 - r14
            if (r12 == 0) goto L_0x01b6
            r0 = 0
        L_0x01b6:
            int r2 = r2 - r0
            int r0 = r1.getDecoratedMeasurement(r9)
            int r0 = r2 - r0
            r4 = r3
            r3 = r0
            r19 = r5
            r5 = r2
            r2 = r19
            goto L_0x020e
        L_0x01c5:
            int r2 = r23.getOffset()
            int r2 = r2 + r14
            if (r12 == 0) goto L_0x01cd
            r0 = 0
        L_0x01cd:
            int r2 = r2 + r0
            int r0 = r1.getDecoratedMeasurement(r9)
            int r0 = r0 + r2
            r4 = r3
            r3 = r2
            r2 = r5
            r5 = r0
            goto L_0x020e
        L_0x01d8:
            int r3 = r25.getPaddingTop()
            int r5 = r7.mMarginTop
            int r3 = r3 + r5
            int r5 = r7.mPaddingTop
            int r3 = r3 + r5
            int r5 = r1.getDecoratedMeasurementInOther(r9)
            int r5 = r5 + r3
            int r10 = r23.getLayoutDirection()
            if (r10 != r4) goto L_0x01ff
            int r2 = r23.getOffset()
            int r2 = r2 - r14
            if (r12 == 0) goto L_0x01f5
            r0 = 0
        L_0x01f5:
            int r2 = r2 - r0
            int r0 = r1.getDecoratedMeasurement(r9)
            int r0 = r2 - r0
            r4 = r2
            r2 = r0
            goto L_0x020e
        L_0x01ff:
            int r2 = r23.getOffset()
            int r2 = r2 + r14
            if (r12 == 0) goto L_0x0207
            r0 = 0
        L_0x0207:
            int r2 = r2 + r0
            int r0 = r1.getDecoratedMeasurement(r9)
            int r0 = r0 + r2
            r4 = r0
        L_0x020e:
            r0 = r20
            r1 = r9
            r6 = r25
            r0.layoutChildWithMargin(r1, r2, r3, r4, r5, r6)
            r7.handleStateOnResult((com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult) r8, (android.view.View) r9)
            r0 = 0
            r7.mLayoutWithAnchor = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.LinearLayoutHelper.layoutViews(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutStateWrapper, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):void");
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        super.checkAnchorInfo(state, anchorInfoWrapper, layoutManagerHelper);
        this.mLayoutWithAnchor = true;
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z3 = layoutManagerHelper.getOrientation() == 1;
        if (z) {
            if (i == getItemCount() - 1) {
                if (z3) {
                    i4 = this.mMarginBottom;
                    i5 = this.mPaddingBottom;
                } else {
                    i4 = this.mMarginRight;
                    i5 = this.mPaddingRight;
                }
                return i4 + i5;
            }
        } else if (i == 0) {
            if (z3) {
                i2 = -this.mMarginTop;
                i3 = this.mPaddingTop;
            } else {
                i2 = -this.mMarginLeft;
                i3 = this.mPaddingLeft;
            }
            return i2 - i3;
        }
        return super.computeAlignOffset(i, z, z2, layoutManagerHelper);
    }
}
