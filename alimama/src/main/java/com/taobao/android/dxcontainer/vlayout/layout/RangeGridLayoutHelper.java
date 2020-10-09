package com.taobao.android.dxcontainer.vlayout.layout;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.Range;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import com.taobao.android.dxcontainer.vlayout.layout.BaseLayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper;
import java.util.Arrays;

public class RangeGridLayoutHelper extends BaseLayoutHelper {
    private static boolean DEBUG = false;
    private static final int MAIN_DIR_SPEC = View.MeasureSpec.makeMeasureSpec(0, 0);
    private static final String TAG = "RGLayoutHelper";
    private boolean mLayoutWithAnchor;
    private GridRangeStyle mRangeStyle;
    private int mTotalSize;

    public RangeGridLayoutHelper(int i) {
        this(i, -1, -1);
    }

    public RangeGridLayoutHelper(int i, int i2) {
        this(i, i2, 0);
    }

    public RangeGridLayoutHelper(int i, int i2, int i3) {
        this(i, i2, i3, i3);
    }

    public RangeGridLayoutHelper(int i, int i2, int i3, int i4) {
        this.mTotalSize = 0;
        this.mLayoutWithAnchor = false;
        this.mRangeStyle = new GridRangeStyle(this);
        this.mRangeStyle.setSpanCount(i);
        this.mRangeStyle.setVGap(i3);
        this.mRangeStyle.setHGap(i4);
        setItemCount(i2);
    }

    public void addRangeStyle(int i, int i2, GridRangeStyle gridRangeStyle) {
        this.mRangeStyle.addChildRangeStyle(i, i2, gridRangeStyle);
    }

    public GridRangeStyle getRootRangeStyle() {
        return this.mRangeStyle;
    }

    public void setMargin(int i, int i2, int i3, int i4) {
        super.setMargin(i, i2, i3, i4);
        this.mRangeStyle.setMargin(i, i2, i3, i4);
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
        this.mRangeStyle.setPadding(i, i2, i3, i4);
    }

    public void setWeights(float[] fArr) {
        this.mRangeStyle.setWeights(fArr);
    }

    public void setSpanSizeLookup(GridLayoutHelper.SpanSizeLookup spanSizeLookup) {
        this.mRangeStyle.setSpanSizeLookup(spanSizeLookup);
    }

    public void setAutoExpand(boolean z) {
        this.mRangeStyle.setAutoExpand(z);
    }

    public void setIgnoreExtra(boolean z) {
        this.mRangeStyle.setIgnoreExtra(z);
    }

    public void setSpanCount(int i) {
        this.mRangeStyle.setSpanCount(i);
    }

    public int getSpanCount() {
        return this.mRangeStyle.getSpanCount();
    }

    public void onRangeChange(int i, int i2) {
        this.mRangeStyle.setRange(i, i2);
    }

    public void setGap(int i) {
        setVGap(i);
        setHGap(i);
    }

    public void setVGap(int i) {
        this.mRangeStyle.setVGap(i);
    }

    public void setHGap(int i) {
        this.mRangeStyle.setHGap(i);
    }

    public void setAspectRatio(float f) {
        this.mRangeStyle.setAspectRatio(f);
    }

    public float getAspectRatio() {
        return this.mRangeStyle.getAspectRatio();
    }

    public void setBgColor(int i) {
        this.mRangeStyle.setBgColor(i);
    }

    public void setLayoutViewHelper(BaseLayoutHelper.DefaultLayoutViewHelper defaultLayoutViewHelper) {
        this.mRangeStyle.setLayoutViewHelper(defaultLayoutViewHelper);
    }

    public void setLayoutViewBindListener(BaseLayoutHelper.LayoutViewBindListener layoutViewBindListener) {
        this.mRangeStyle.setLayoutViewBindListener(layoutViewBindListener);
    }

    public void setLayoutViewUnBindListener(BaseLayoutHelper.LayoutViewUnBindListener layoutViewUnBindListener) {
        this.mRangeStyle.setLayoutViewUnBindListener(layoutViewUnBindListener);
    }

    public boolean requireLayoutView() {
        return this.mRangeStyle.requireLayoutView();
    }

    public void beforeLayout(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutManagerHelper layoutManagerHelper) {
        this.mRangeStyle.beforeLayout(recycler, state, layoutManagerHelper);
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x0371 A[LOOP:2: B:62:0x01fa->B:122:0x0371, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x03bb A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x03bc  */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x07f0  */
    /* JADX WARNING: Removed duplicated region for block: B:303:0x07f4  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x07fb  */
    /* JADX WARNING: Removed duplicated region for block: B:330:0x08ab  */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x08b2  */
    /* JADX WARNING: Removed duplicated region for block: B:340:0x0909  */
    /* JADX WARNING: Removed duplicated region for block: B:344:0x0929  */
    /* JADX WARNING: Removed duplicated region for block: B:349:0x094e  */
    /* JADX WARNING: Removed duplicated region for block: B:387:0x0212 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:390:0x03b6 A[EDGE_INSN: B:390:0x03b6->B:127:0x03b6 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0235  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutViews(androidx.recyclerview.widget.RecyclerView.Recycler r40, androidx.recyclerview.widget.RecyclerView.State r41, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutStateWrapper r42, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult r43, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r44) {
        /*
            r39 = this;
            r8 = r39
            r9 = r40
            r10 = r41
            r11 = r42
            r12 = r43
            r13 = r44
            int r0 = r42.getCurrentPosition()
            boolean r0 = r8.isOutOfRange(r0)
            if (r0 == 0) goto L_0x0017
            return
        L_0x0017:
            int r14 = r42.getCurrentPosition()
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r0 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r15 = r0.findRangeStyleByPosition(r14)
            int r6 = r42.getItemDirection()
            r7 = 1
            r5 = 0
            if (r6 != r7) goto L_0x002c
            r16 = 1
            goto L_0x002e
        L_0x002c:
            r16 = 0
        L_0x002e:
            com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r4 = r44.getMainOrientationHelper()
            int r0 = r44.getOrientation()
            if (r0 != r7) goto L_0x003a
            r3 = 1
            goto L_0x003b
        L_0x003a:
            r3 = 0
        L_0x003b:
            r17 = 1056964608(0x3f000000, float:0.5)
            r18 = 1065353216(0x3f800000, float:1.0)
            if (r3 == 0) goto L_0x0079
            int r0 = r44.getContentWidth()
            int r1 = r44.getPaddingRight()
            int r0 = r0 - r1
            int r1 = r44.getPaddingLeft()
            int r0 = r0 - r1
            int r1 = r15.getFamilyHorizontalMargin()
            int r0 = r0 - r1
            int r1 = r15.getFamilyHorizontalPadding()
            int r0 = r0 - r1
            r8.mTotalSize = r0
            int r0 = r8.mTotalSize
            int r1 = r15.mSpanCount
            int r1 = r1 - r7
            int r2 = r15.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            float r0 = (float) r0
            float r0 = r0 * r18
            int r1 = r15.mSpanCount
            float r1 = (float) r1
            float r0 = r0 / r1
            float r0 = r0 + r17
            int r0 = (int) r0
            int unused = r15.mSizePerSpan = r0
            goto L_0x00b0
        L_0x0079:
            int r0 = r44.getContentHeight()
            int r1 = r44.getPaddingBottom()
            int r0 = r0 - r1
            int r1 = r44.getPaddingTop()
            int r0 = r0 - r1
            int r1 = r15.getFamilyVerticalMargin()
            int r0 = r0 - r1
            int r1 = r15.getFamilyVerticalPadding()
            int r0 = r0 - r1
            r8.mTotalSize = r0
            int r0 = r8.mTotalSize
            int r1 = r15.mSpanCount
            int r1 = r1 - r7
            int r2 = r15.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            float r0 = (float) r0
            float r0 = r0 * r18
            int r1 = r15.mSpanCount
            float r1 = (float) r1
            float r0 = r0 / r1
            float r0 = r0 + r17
            int r0 = (int) r0
            int unused = r15.mSizePerSpan = r0
        L_0x00b0:
            int r0 = r15.mSpanCount
            r15.ensureSpanCount()
            if (r16 != 0) goto L_0x01ee
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r1 = r15.mSpanSizeLookup
            int r2 = r15.mSpanCount
            r0 = r39
            r19 = r3
            r3 = r40
            r20 = r4
            r4 = r41
            r5 = r14
            int r0 = r0.getSpanIndex(r1, r2, r3, r4, r5)
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r1 = r15.mSpanSizeLookup
            int r1 = r8.getSpanSize(r1, r9, r10, r14)
            int r1 = r1 + r0
            int r2 = r15.mSpanCount
            int r2 = r2 - r7
            if (r0 == r2) goto L_0x01eb
            int r0 = r42.getCurrentPosition()
            int r2 = r15.mSpanCount
            int r2 = r2 - r1
            r3 = r0
            r0 = 0
            r4 = 0
            r5 = 0
            r22 = 0
        L_0x00ef:
            int r7 = r15.mSpanCount
            if (r0 >= r7) goto L_0x01bc
            if (r2 <= 0) goto L_0x01bc
            int r3 = r3 - r6
            boolean r7 = r15.isOutOfRange(r3)
            if (r7 == 0) goto L_0x0100
            goto L_0x01bc
        L_0x0100:
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r7 = r15.mSpanSizeLookup
            int r7 = r8.getSpanSize(r7, r9, r10, r3)
            r23 = r1
            int r1 = r15.mSpanCount
            if (r7 > r1) goto L_0x018c
            android.view.View r1 = r11.retrieve(r9, r3)
            if (r1 != 0) goto L_0x0118
            goto L_0x01be
        L_0x0118:
            if (r4 != 0) goto L_0x0149
            boolean r4 = r44.getReverseLayout()
            if (r4 == 0) goto L_0x0136
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r4 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r4 = r4.getRange()
            java.lang.Comparable r4 = r4.getUpper()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r3 != r4) goto L_0x0134
        L_0x0132:
            r4 = 1
            goto L_0x0149
        L_0x0134:
            r4 = 0
            goto L_0x0149
        L_0x0136:
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r4 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r4 = r4.getRange()
            java.lang.Comparable r4 = r4.getLower()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r3 != r4) goto L_0x0134
            goto L_0x0132
        L_0x0149:
            if (r5 != 0) goto L_0x017a
            boolean r5 = r44.getReverseLayout()
            if (r5 == 0) goto L_0x0167
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r5 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r5 = r5.getRange()
            java.lang.Comparable r5 = r5.getLower()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            if (r3 != r5) goto L_0x0165
        L_0x0163:
            r5 = 1
            goto L_0x017a
        L_0x0165:
            r5 = 0
            goto L_0x017a
        L_0x0167:
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r5 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r5 = r5.getRange()
            java.lang.Comparable r5 = r5.getUpper()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            if (r3 != r5) goto L_0x0165
            goto L_0x0163
        L_0x017a:
            int r2 = r2 - r7
            if (r2 >= 0) goto L_0x017e
            goto L_0x01be
        L_0x017e:
            int r22 = r22 + r7
            android.view.View[] r7 = r15.mSet
            r7[r0] = r1
            int r0 = r0 + 1
            r1 = r23
            goto L_0x00ef
        L_0x018c:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Item at position "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r2 = " requires "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = " spans but RangeGridLayoutHelper has only "
            r1.append(r2)
            int r2 = r15.mSpanCount
            r1.append(r2)
            java.lang.String r2 = " spans."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x01bc:
            r23 = r1
        L_0x01be:
            r1 = r5
            r5 = r4
            if (r0 <= 0) goto L_0x01e5
            int r2 = r0 + -1
            r3 = r2
            r2 = 0
        L_0x01c6:
            if (r2 >= r3) goto L_0x01e5
            android.view.View[] r4 = r15.mSet
            r4 = r4[r2]
            android.view.View[] r6 = r15.mSet
            android.view.View[] r7 = r15.mSet
            r7 = r7[r3]
            r6[r2] = r7
            android.view.View[] r6 = r15.mSet
            r6[r3] = r4
            int r2 = r2 + 1
            int r3 = r3 + -1
            goto L_0x01c6
        L_0x01e5:
            r7 = r0
            r6 = r22
            r0 = 0
            r2 = 0
            goto L_0x01fa
        L_0x01eb:
            r23 = r1
            goto L_0x01f4
        L_0x01ee:
            r19 = r3
            r20 = r4
            r23 = r0
        L_0x01f4:
            r0 = 0
            r1 = 0
            r2 = 0
            r5 = 0
            r6 = 0
            r7 = 0
        L_0x01fa:
            int r3 = r15.mSpanCount
            if (r7 >= r3) goto L_0x03b2
            boolean r3 = r11.hasMore(r10)
            if (r3 == 0) goto L_0x03b2
            if (r23 <= 0) goto L_0x03b2
            int r3 = r42.getCurrentPosition()
            boolean r4 = r15.isOutOfRange(r3)
            if (r4 == 0) goto L_0x0235
            boolean r4 = DEBUG
            if (r4 == 0) goto L_0x03b2
            java.lang.String r4 = "RGLayoutHelper"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r24 = r14
            java.lang.String r14 = "pos ["
            r12.append(r14)
            r12.append(r3)
            java.lang.String r3 = "] is out of range"
            r12.append(r3)
            java.lang.String r3 = r12.toString()
            android.util.Log.d(r4, r3)
            goto L_0x03b4
        L_0x0235:
            r24 = r14
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r4 = r15.mSpanSizeLookup
            int r4 = r8.getSpanSize(r4, r9, r10, r3)
            int r12 = r15.mSpanCount
            if (r4 > r12) goto L_0x0382
            int r23 = r23 - r4
            if (r23 >= 0) goto L_0x024b
            goto L_0x03b4
        L_0x024b:
            if (r5 != 0) goto L_0x027c
            boolean r5 = r44.getReverseLayout()
            if (r5 == 0) goto L_0x0269
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r5 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r5 = r5.getRange()
            java.lang.Comparable r5 = r5.getUpper()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            if (r3 != r5) goto L_0x0267
        L_0x0265:
            r5 = 1
            goto L_0x027c
        L_0x0267:
            r5 = 0
            goto L_0x027c
        L_0x0269:
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r5 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r5 = r5.getRange()
            java.lang.Comparable r5 = r5.getLower()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            if (r3 != r5) goto L_0x0267
            goto L_0x0265
        L_0x027c:
            if (r0 != 0) goto L_0x02b1
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r12 = r8.mRangeStyle
            boolean r12 = r15.equals(r12)
            if (r12 != 0) goto L_0x02b1
            boolean r0 = r44.getReverseLayout()
            if (r0 == 0) goto L_0x02a0
            com.taobao.android.dxcontainer.vlayout.Range r0 = r15.getRange()
            java.lang.Comparable r0 = r0.getUpper()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r3 != r0) goto L_0x029e
        L_0x029c:
            r0 = 1
            goto L_0x02b1
        L_0x029e:
            r0 = 0
            goto L_0x02b1
        L_0x02a0:
            com.taobao.android.dxcontainer.vlayout.Range r0 = r15.getRange()
            java.lang.Comparable r0 = r0.getLower()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r3 != r0) goto L_0x029e
            goto L_0x029c
        L_0x02b1:
            if (r1 != 0) goto L_0x02e2
            boolean r1 = r44.getReverseLayout()
            if (r1 == 0) goto L_0x02cf
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r1 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r1 = r1.getRange()
            java.lang.Comparable r1 = r1.getLower()
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r3 != r1) goto L_0x02cd
        L_0x02cb:
            r1 = 1
            goto L_0x02e2
        L_0x02cd:
            r1 = 0
            goto L_0x02e2
        L_0x02cf:
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r1 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.Range r1 = r1.getRange()
            java.lang.Comparable r1 = r1.getUpper()
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r3 != r1) goto L_0x02cd
            goto L_0x02cb
        L_0x02e2:
            if (r2 != 0) goto L_0x0368
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r12 = r8.mRangeStyle
            boolean r12 = r15.equals(r12)
            if (r12 != 0) goto L_0x0368
            boolean r2 = r44.getReverseLayout()
            if (r2 == 0) goto L_0x0306
            com.taobao.android.dxcontainer.vlayout.Range r2 = r15.getRange()
            java.lang.Comparable r2 = r2.getLower()
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            if (r3 != r2) goto L_0x0304
        L_0x0302:
            r2 = 1
            goto L_0x0317
        L_0x0304:
            r2 = 0
            goto L_0x0317
        L_0x0306:
            com.taobao.android.dxcontainer.vlayout.Range r2 = r15.getRange()
            java.lang.Comparable r2 = r2.getUpper()
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            if (r3 != r2) goto L_0x0304
            goto L_0x0302
        L_0x0317:
            boolean r12 = DEBUG
            if (r12 == 0) goto L_0x0368
            java.lang.String r12 = "RGLayoutHelper"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r25 = r0
            java.lang.String r0 = "isSecondEndLineLogic:"
            r14.append(r0)
            r14.append(r2)
            java.lang.String r0 = "  helper.getReverseLayout()="
            r14.append(r0)
            boolean r0 = r44.getReverseLayout()
            r14.append(r0)
            java.lang.String r0 = " pos="
            r14.append(r0)
            r14.append(r3)
            java.lang.String r0 = " rangeStyle.getRange().getLower()="
            r14.append(r0)
            com.taobao.android.dxcontainer.vlayout.Range r0 = r15.getRange()
            java.lang.Comparable r0 = r0.getLower()
            r14.append(r0)
            java.lang.String r0 = " rangeStyle.getRange().getUpper()="
            r14.append(r0)
            com.taobao.android.dxcontainer.vlayout.Range r0 = r15.getRange()
            java.lang.Comparable r0 = r0.getUpper()
            r14.append(r0)
            java.lang.String r0 = r14.toString()
            android.util.Log.d(r12, r0)
            goto L_0x036a
        L_0x0368:
            r25 = r0
        L_0x036a:
            android.view.View r0 = r11.next(r9)
            if (r0 != 0) goto L_0x0371
            goto L_0x03b6
        L_0x0371:
            int r6 = r6 + r4
            android.view.View[] r3 = r15.mSet
            r3[r7] = r0
            int r7 = r7 + 1
            r14 = r24
            r0 = r25
            r12 = r43
            goto L_0x01fa
        L_0x0382:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Item at position "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r2 = " requires "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r2 = " spans but GridLayoutManager has only "
            r1.append(r2)
            int r2 = r15.mSpanCount
            r1.append(r2)
            java.lang.String r2 = " spans."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x03b2:
            r24 = r14
        L_0x03b4:
            r25 = r0
        L_0x03b6:
            r14 = r1
            r12 = r5
            r5 = r2
            if (r7 != 0) goto L_0x03bc
            return
        L_0x03bc:
            r0 = r39
            r1 = r15
            r2 = r40
            r3 = r41
            r4 = r7
            r26 = r5
            r5 = r6
            r27 = r14
            r14 = r6
            r6 = r16
            r28 = r12
            r12 = r7
            r7 = r44
            r0.assignSpans(r1, r2, r3, r4, r5, r6, r7)
            if (r23 <= 0) goto L_0x0402
            if (r12 != r14) goto L_0x0402
            boolean r0 = r15.mIsAutoExpand
            if (r0 == 0) goto L_0x0402
            r7 = r19
            if (r7 == 0) goto L_0x03f2
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r12
            int unused = r15.mSizePerSpan = r0
            goto L_0x0431
        L_0x03f2:
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r12
            int unused = r15.mSizePerSpan = r0
            goto L_0x0431
        L_0x0402:
            r7 = r19
            if (r16 != 0) goto L_0x0431
            if (r23 != 0) goto L_0x0431
            if (r12 != r14) goto L_0x0431
            boolean r0 = r15.mIsAutoExpand
            if (r0 == 0) goto L_0x0431
            if (r7 == 0) goto L_0x0422
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r12
            int unused = r15.mSizePerSpan = r0
            goto L_0x0431
        L_0x0422:
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r12
            int unused = r15.mSizePerSpan = r0
        L_0x0431:
            float[] r0 = r15.mWeights
            if (r0 == 0) goto L_0x04cd
            float[] r0 = r15.mWeights
            int r0 = r0.length
            if (r0 <= 0) goto L_0x04cd
            if (r7 == 0) goto L_0x044c
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            goto L_0x0457
        L_0x044c:
            int r0 = r8.mTotalSize
            int r1 = r12 + -1
            int r2 = r15.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
        L_0x0457:
            if (r23 <= 0) goto L_0x0461
            boolean r1 = r15.mIsAutoExpand
            if (r1 == 0) goto L_0x0461
            r1 = r12
            goto L_0x0465
        L_0x0461:
            int r1 = r15.mSpanCount
        L_0x0465:
            r4 = r0
            r2 = 0
            r3 = 0
        L_0x0468:
            if (r2 >= r1) goto L_0x04b4
            float[] r5 = r15.mWeights
            int r5 = r5.length
            if (r2 >= r5) goto L_0x04a8
            float[] r5 = r15.mWeights
            r5 = r5[r2]
            boolean r5 = java.lang.Float.isNaN(r5)
            if (r5 != 0) goto L_0x04a8
            float[] r5 = r15.mWeights
            r5 = r5[r2]
            r14 = 0
            int r5 = (r5 > r14 ? 1 : (r5 == r14 ? 0 : -1))
            if (r5 < 0) goto L_0x04a8
            float[] r5 = r15.mWeights
            r5 = r5[r2]
            int[] r14 = r15.mSpanCols
            float r5 = r5 * r18
            r19 = 1120403456(0x42c80000, float:100.0)
            float r5 = r5 / r19
            float r6 = (float) r0
            float r5 = r5 * r6
            float r5 = r5 + r17
            int r5 = (int) r5
            r14[r2] = r5
            int[] r5 = r15.mSpanCols
            r5 = r5[r2]
            int r4 = r4 - r5
            goto L_0x04b1
        L_0x04a8:
            int r3 = r3 + 1
            int[] r5 = r15.mSpanCols
            r6 = -1
            r5[r2] = r6
        L_0x04b1:
            int r2 = r2 + 1
            goto L_0x0468
        L_0x04b4:
            if (r3 <= 0) goto L_0x04cb
            int r4 = r4 / r3
            r0 = 0
        L_0x04b8:
            if (r0 >= r1) goto L_0x04cb
            int[] r2 = r15.mSpanCols
            r2 = r2[r0]
            if (r2 >= 0) goto L_0x04c8
            int[] r2 = r15.mSpanCols
            r2[r0] = r4
        L_0x04c8:
            int r0 = r0 + 1
            goto L_0x04b8
        L_0x04cb:
            r14 = 1
            goto L_0x04ce
        L_0x04cd:
            r14 = 0
        L_0x04ce:
            r5 = 0
            r6 = 0
        L_0x04d0:
            if (r6 >= r12) goto L_0x059f
            android.view.View[] r0 = r15.mSet
            r3 = r0[r6]
            if (r16 == 0) goto L_0x04dc
            r0 = -1
            goto L_0x04dd
        L_0x04dc:
            r0 = 0
        L_0x04dd:
            r13.addChildView(r11, r3, r0)
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r0 = r15.mSpanSizeLookup
            int r1 = r13.getPosition(r3)
            int r0 = r8.getSpanSize(r0, r9, r10, r1)
            if (r14 == 0) goto L_0x0512
            int[] r1 = r15.mSpanIndices
            r1 = r1[r6]
            r2 = 0
            r4 = 0
        L_0x04f6:
            if (r2 >= r0) goto L_0x0505
            int[] r17 = r15.mSpanCols
            int r18 = r2 + r1
            r17 = r17[r18]
            int r4 = r4 + r17
            int r2 = r2 + 1
            goto L_0x04f6
        L_0x0505:
            r2 = 0
            int r0 = java.lang.Math.max(r2, r4)
            r1 = 1073741824(0x40000000, float:2.0)
            int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1)
        L_0x0510:
            r4 = r0
            goto L_0x0534
        L_0x0512:
            r2 = 0
            int r1 = r15.mSizePerSpan
            int r1 = r1 * r0
            int r0 = r0 + -1
            int r0 = java.lang.Math.max(r2, r0)
            if (r7 == 0) goto L_0x0526
            int r4 = r15.mHGap
            goto L_0x052a
        L_0x0526:
            int r4 = r15.mVGap
        L_0x052a:
            int r0 = r0 * r4
            int r1 = r1 + r0
            r4 = 1073741824(0x40000000, float:2.0)
            int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r4)
            goto L_0x0510
        L_0x0534:
            android.view.ViewGroup$LayoutParams r0 = r3.getLayoutParams()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r0 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r0
            int r1 = r44.getOrientation()
            r11 = 1
            if (r1 != r11) goto L_0x056b
            int r1 = r0.height
            int r11 = r8.mTotalSize
            int r17 = android.view.View.MeasureSpec.getSize(r4)
            float r0 = r0.mAspectRatio
            r18 = r0
            r0 = r39
            r19 = r1
            r1 = r15
            r29 = r7
            r7 = 0
            r2 = r19
            r7 = r3
            r3 = r11
            r11 = r4
            r4 = r17
            r30 = r14
            r14 = r5
            r5 = r18
            int r0 = r0.getMainDirSpec(r1, r2, r3, r4, r5)
            r13.measureChildWithMargins(r7, r11, r0)
        L_0x0568:
            r11 = r20
            goto L_0x058b
        L_0x056b:
            r11 = r4
            r29 = r7
            r30 = r14
            r7 = r3
            r14 = r5
            int r2 = r0.width
            int r3 = r8.mTotalSize
            int r4 = android.view.View.MeasureSpec.getSize(r11)
            float r5 = r0.mAspectRatio
            r0 = r39
            r1 = r15
            int r0 = r0.getMainDirSpec(r1, r2, r3, r4, r5)
            int r1 = android.view.View.MeasureSpec.getSize(r11)
            r13.measureChildWithMargins(r7, r0, r1)
            goto L_0x0568
        L_0x058b:
            int r5 = r11.getDecoratedMeasurement(r7)
            if (r5 <= r14) goto L_0x0592
            goto L_0x0593
        L_0x0592:
            r5 = r14
        L_0x0593:
            int r6 = r6 + 1
            r20 = r11
            r7 = r29
            r14 = r30
            r11 = r42
            goto L_0x04d0
        L_0x059f:
            r29 = r7
            r30 = r14
            r11 = r20
            r4 = 1073741824(0x40000000, float:2.0)
            r14 = r5
            int r3 = r8.mTotalSize
            r5 = 0
            r6 = 2143289344(0x7fc00000, float:NaN)
            r0 = r39
            r1 = r15
            r2 = r14
            r7 = 1073741824(0x40000000, float:2.0)
            r4 = r5
            r5 = r6
            int r0 = r0.getMainDirSpec(r1, r2, r3, r4, r5)
            r1 = 0
        L_0x05ba:
            if (r1 >= r12) goto L_0x0629
            android.view.View[] r2 = r15.mSet
            r2 = r2[r1]
            int r3 = r11.getDecoratedMeasurement(r2)
            if (r3 == r14) goto L_0x0625
            com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper$SpanSizeLookup r3 = r15.mSpanSizeLookup
            int r4 = r13.getPosition(r2)
            int r3 = r8.getSpanSize(r3, r9, r10, r4)
            if (r30 == 0) goto L_0x05f7
            int[] r4 = r15.mSpanIndices
            r4 = r4[r1]
            r5 = 0
            r6 = 0
        L_0x05de:
            if (r5 >= r3) goto L_0x05ed
            int[] r16 = r15.mSpanCols
            int r17 = r5 + r4
            r16 = r16[r17]
            int r6 = r6 + r16
            int r5 = r5 + 1
            goto L_0x05de
        L_0x05ed:
            r5 = 0
            int r3 = java.lang.Math.max(r5, r6)
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r7)
            goto L_0x0616
        L_0x05f7:
            r5 = 0
            int r4 = r15.mSizePerSpan
            int r4 = r4 * r3
            int r3 = r3 + -1
            int r3 = java.lang.Math.max(r5, r3)
            if (r29 == 0) goto L_0x060b
            int r5 = r15.mHGap
            goto L_0x060f
        L_0x060b:
            int r5 = r15.mVGap
        L_0x060f:
            int r3 = r3 * r5
            int r4 = r4 + r3
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r7)
        L_0x0616:
            int r4 = r44.getOrientation()
            r5 = 1
            if (r4 != r5) goto L_0x0621
            r13.measureChildWithMargins(r2, r3, r0)
            goto L_0x0626
        L_0x0621:
            r13.measureChildWithMargins(r2, r0, r3)
            goto L_0x0626
        L_0x0625:
            r5 = 1
        L_0x0626:
            int r1 = r1 + 1
            goto L_0x05ba
        L_0x0629:
            r5 = 1
            int r0 = r42.getLayoutDirection()
            if (r0 != r5) goto L_0x0632
            r0 = 1
            goto L_0x0633
        L_0x0632:
            r0 = 0
        L_0x0633:
            boolean r1 = r44.isEnableMarginOverLap()
            if (r28 == 0) goto L_0x0641
            r7 = r29
            int r5 = r8.computeStartSpace(r13, r7, r0, r1)
            r9 = r5
            goto L_0x0644
        L_0x0641:
            r7 = r29
            r9 = 0
        L_0x0644:
            if (r25 == 0) goto L_0x065f
            if (r7 == 0) goto L_0x0653
            int r0 = r15.getMarginTop()
            int r1 = r15.getPaddingTop()
            int r0 = r0 + r1
        L_0x0651:
            r5 = r0
            goto L_0x065d
        L_0x0653:
            int r0 = r15.getMarginLeft()
            int r1 = r15.getPaddingLeft()
            int r0 = r0 + r1
            goto L_0x0651
        L_0x065d:
            r10 = r5
            goto L_0x0660
        L_0x065f:
            r10 = 0
        L_0x0660:
            if (r27 == 0) goto L_0x0684
            if (r7 == 0) goto L_0x0673
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r0 = r8.mRangeStyle
            int r0 = r0.getMarginBottom()
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r1 = r8.mRangeStyle
            int r1 = r1.getPaddingBottom()
            int r0 = r0 + r1
        L_0x0671:
            r5 = r0
            goto L_0x0681
        L_0x0673:
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r0 = r8.mRangeStyle
            int r0 = r0.getMarginRight()
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r1 = r8.mRangeStyle
            int r1 = r1.getPaddingRight()
            int r0 = r0 + r1
            goto L_0x0671
        L_0x0681:
            r2 = r26
            goto L_0x0687
        L_0x0684:
            r2 = r26
            r5 = 0
        L_0x0687:
            if (r2 == 0) goto L_0x06ce
            if (r7 == 0) goto L_0x0695
            int r0 = r15.getMarginBottom()
            int r1 = r15.getPaddingBottom()
            int r0 = r0 + r1
            goto L_0x069e
        L_0x0695:
            int r0 = r15.getMarginRight()
            int r1 = r15.getPaddingRight()
            int r0 = r0 + r1
        L_0x069e:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x06cb
            java.lang.String r1 = "RGLayoutHelper"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "isSecondEndLineLogic:"
            r3.append(r4)
            r3.append(r2)
            java.lang.String r4 = " pos="
            r3.append(r4)
            r4 = r24
            r3.append(r4)
            java.lang.String r6 = " secondEndSpace="
            r3.append(r6)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r1, r3)
            goto L_0x06d1
        L_0x06cb:
            r4 = r24
            goto L_0x06d1
        L_0x06ce:
            r4 = r24
            r0 = 0
        L_0x06d1:
            int r1 = r14 + r9
            int r1 = r1 + r5
            int r1 = r1 + r10
            int r1 = r1 + r0
            r6 = r43
            r6.mConsumed = r1
            int r1 = r42.getLayoutDirection()
            r3 = -1
            if (r1 != r3) goto L_0x06e3
            r1 = 1
            goto L_0x06e4
        L_0x06e3:
            r1 = 0
        L_0x06e4:
            boolean r3 = r8.mLayoutWithAnchor
            if (r3 != 0) goto L_0x07e2
            if (r1 != 0) goto L_0x076b
            if (r28 != 0) goto L_0x07e2
            if (r25 == 0) goto L_0x0734
            if (r7 == 0) goto L_0x06f9
            com.taobao.android.dxcontainer.vlayout.layout.RangeStyle r3 = r15.mParent
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r3 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle) r3
            int r3 = r3.mVGap
            goto L_0x0701
        L_0x06f9:
            com.taobao.android.dxcontainer.vlayout.layout.RangeStyle r3 = r15.mParent
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r3 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle) r3
            int r3 = r3.mHGap
        L_0x0701:
            boolean r16 = DEBUG
            if (r16 == 0) goto L_0x072d
            java.lang.String r13 = "RGLayoutHelper"
            r31 = r11
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r32 = r12
            java.lang.String r12 = "⬇ "
            r11.append(r12)
            r11.append(r4)
            java.lang.String r12 = " 1 "
            r11.append(r12)
            r11.append(r3)
            java.lang.String r12 = " gap"
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            android.util.Log.d(r13, r11)
            goto L_0x0731
        L_0x072d:
            r31 = r11
            r32 = r12
        L_0x0731:
            r11 = r3
            goto L_0x07e7
        L_0x0734:
            r31 = r11
            r32 = r12
            if (r7 == 0) goto L_0x073f
            int r3 = r15.mVGap
            goto L_0x0743
        L_0x073f:
            int r3 = r15.mHGap
        L_0x0743:
            boolean r11 = DEBUG
            if (r11 == 0) goto L_0x0731
            java.lang.String r11 = "RGLayoutHelper"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "⬇ "
            r12.append(r13)
            r12.append(r4)
            java.lang.String r13 = " 2 "
            r12.append(r13)
            r12.append(r3)
            java.lang.String r13 = " gap"
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r11, r12)
            goto L_0x0731
        L_0x076b:
            r31 = r11
            r32 = r12
            if (r27 != 0) goto L_0x07e6
            if (r2 == 0) goto L_0x07ae
            if (r7 == 0) goto L_0x077e
            com.taobao.android.dxcontainer.vlayout.layout.RangeStyle r3 = r15.mParent
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r3 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle) r3
            int r3 = r3.mVGap
            goto L_0x0786
        L_0x077e:
            com.taobao.android.dxcontainer.vlayout.layout.RangeStyle r3 = r15.mParent
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r3 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper.GridRangeStyle) r3
            int r3 = r3.mHGap
        L_0x0786:
            boolean r11 = DEBUG
            if (r11 == 0) goto L_0x0731
            java.lang.String r11 = "RGLayoutHelper"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "⬆ "
            r12.append(r13)
            r12.append(r4)
            java.lang.String r13 = " 3 "
            r12.append(r13)
            r12.append(r3)
            java.lang.String r13 = " gap"
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r11, r12)
            goto L_0x0731
        L_0x07ae:
            if (r7 == 0) goto L_0x07b5
            int r3 = r15.mVGap
            goto L_0x07b9
        L_0x07b5:
            int r3 = r15.mHGap
        L_0x07b9:
            boolean r11 = DEBUG
            if (r11 == 0) goto L_0x0731
            java.lang.String r11 = "RGLayoutHelper"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "⬆ "
            r12.append(r13)
            r12.append(r4)
            java.lang.String r13 = " 4 "
            r12.append(r13)
            r12.append(r3)
            java.lang.String r13 = " gap"
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r11, r12)
            goto L_0x0731
        L_0x07e2:
            r31 = r11
            r32 = r12
        L_0x07e6:
            r11 = 0
        L_0x07e7:
            int r3 = r6.mConsumed
            int r3 = r3 + r11
            r6.mConsumed = r3
            int r3 = r6.mConsumed
            if (r3 > 0) goto L_0x07f4
            r12 = 0
            r6.mConsumed = r12
            goto L_0x07f5
        L_0x07f4:
            r12 = 0
        L_0x07f5:
            boolean r3 = r42.isRefreshLayout()
            if (r3 != 0) goto L_0x08ab
            if (r1 == 0) goto L_0x0858
            int r3 = r4 + 1
            boolean r13 = r8.isOutOfRange(r3)
            if (r13 != 0) goto L_0x0853
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r13 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r13 = r13.findRangeStyleByPosition(r3)
            boolean r3 = r13.isFirstPosition(r3)
            if (r3 == 0) goto L_0x0853
            if (r7 == 0) goto L_0x081d
            int r3 = r13.getMarginTop()
            int r13 = r13.getPaddingTop()
            int r3 = r3 + r13
            goto L_0x0826
        L_0x081d:
            int r3 = r13.getMarginLeft()
            int r13 = r13.getPaddingLeft()
            int r3 = r3 + r13
        L_0x0826:
            boolean r13 = DEBUG
            if (r13 == 0) goto L_0x0850
            java.lang.String r13 = "RGLayoutHelper"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r33 = r15
            java.lang.String r15 = "⬆ "
            r12.append(r15)
            r12.append(r4)
            java.lang.String r15 = " 1 "
            r12.append(r15)
            r12.append(r3)
            java.lang.String r15 = " last"
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r13, r12)
            goto L_0x0856
        L_0x0850:
            r33 = r15
            goto L_0x0856
        L_0x0853:
            r33 = r15
            r3 = 0
        L_0x0856:
            r12 = r3
            goto L_0x08ae
        L_0x0858:
            r33 = r15
            int r3 = r4 + -1
            boolean r12 = r8.isOutOfRange(r3)
            if (r12 != 0) goto L_0x08ad
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r12 = r8.mRangeStyle
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper$GridRangeStyle r12 = r12.findRangeStyleByPosition(r3)
            boolean r3 = r12.isLastPosition(r3)
            if (r3 == 0) goto L_0x08ad
            if (r7 == 0) goto L_0x087a
            int r3 = r12.getMarginBottom()
            int r12 = r12.getPaddingBottom()
            int r3 = r3 + r12
            goto L_0x0883
        L_0x087a:
            int r3 = r12.getMarginRight()
            int r12 = r12.getPaddingRight()
            int r3 = r3 + r12
        L_0x0883:
            boolean r12 = DEBUG
            if (r12 == 0) goto L_0x0856
            java.lang.String r12 = "RGLayoutHelper"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r15 = "⬇ "
            r13.append(r15)
            r13.append(r4)
            java.lang.String r15 = " 2 "
            r13.append(r15)
            r13.append(r3)
            java.lang.String r15 = " last"
            r13.append(r15)
            java.lang.String r13 = r13.toString()
            android.util.Log.d(r12, r13)
            goto L_0x0856
        L_0x08ab:
            r33 = r15
        L_0x08ad:
            r12 = 0
        L_0x08ae:
            boolean r3 = DEBUG
            if (r3 == 0) goto L_0x0907
            java.lang.String r3 = "RGLayoutHelper"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            if (r1 == 0) goto L_0x08be
            java.lang.String r15 = "⬆ "
            goto L_0x08c0
        L_0x08be:
            java.lang.String r15 = "⬇ "
        L_0x08c0:
            r13.append(r15)
            r13.append(r4)
            java.lang.String r4 = " consumed "
            r13.append(r4)
            int r4 = r6.mConsumed
            r13.append(r4)
            java.lang.String r4 = " startSpace "
            r13.append(r4)
            r13.append(r9)
            java.lang.String r4 = " endSpace "
            r13.append(r4)
            r13.append(r5)
            java.lang.String r4 = " secondStartSpace "
            r13.append(r4)
            r13.append(r10)
            java.lang.String r4 = " secondEndSpace "
            r13.append(r4)
            r13.append(r0)
            java.lang.String r4 = " lastUnconsumedSpace "
            r13.append(r4)
            r13.append(r12)
            java.lang.String r4 = " isSecondEndLine="
            r13.append(r4)
            r13.append(r2)
            java.lang.String r2 = r13.toString()
            android.util.Log.d(r3, r2)
        L_0x0907:
            if (r7 == 0) goto L_0x0929
            if (r1 == 0) goto L_0x091b
            int r1 = r42.getOffset()
            int r1 = r1 - r5
            int r1 = r1 - r0
            int r1 = r1 - r11
            int r5 = r1 - r12
            int r0 = r5 - r14
            r1 = r0
            r2 = r5
        L_0x0918:
            r0 = 0
            r5 = 0
            goto L_0x0949
        L_0x091b:
            int r0 = r42.getOffset()
            int r0 = r0 + r9
            int r0 = r0 + r10
            int r0 = r0 + r11
            int r5 = r0 + r12
            int r0 = r5 + r14
            r2 = r0
            r1 = r5
            goto L_0x0918
        L_0x0929:
            if (r1 == 0) goto L_0x0938
            int r0 = r42.getOffset()
            int r0 = r0 - r5
            int r0 = r0 - r11
            int r5 = r0 - r12
            int r0 = r5 - r14
            r1 = 0
            r2 = 0
            goto L_0x0949
        L_0x0938:
            int r0 = r42.getOffset()
            int r0 = r0 + r9
            int r0 = r0 + r11
            int r5 = r0 + r12
            int r0 = r5 + r14
            r1 = 0
            r2 = 0
            r38 = r5
            r5 = r0
            r0 = r38
        L_0x0949:
            r13 = r32
            r14 = 0
        L_0x094c:
            if (r14 >= r13) goto L_0x0ad4
            android.view.View[] r3 = r33.mSet
            r15 = r3[r14]
            int[] r3 = r33.mSpanIndices
            r3 = r3[r14]
            android.view.ViewGroup$LayoutParams r4 = r15.getLayoutParams()
            r16 = r4
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r16 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r16
            if (r7 == 0) goto L_0x09b4
            if (r30 == 0) goto L_0x0988
            int r0 = r44.getPaddingLeft()
            int r4 = r33.getFamilyMarginLeft()
            int r0 = r0 + r4
            int r4 = r33.getFamilyPaddingLeft()
            int r0 = r0 + r4
            r4 = r0
            r0 = 0
        L_0x0976:
            if (r0 >= r3) goto L_0x09a4
            int[] r5 = r33.mSpanCols
            r5 = r5[r0]
            int r17 = r33.mHGap
            int r5 = r5 + r17
            int r4 = r4 + r5
            int r0 = r0 + 1
            goto L_0x0976
        L_0x0988:
            int r0 = r44.getPaddingLeft()
            int r4 = r33.getFamilyMarginLeft()
            int r0 = r0 + r4
            int r4 = r33.getFamilyPaddingLeft()
            int r0 = r0 + r4
            int r4 = r33.mSizePerSpan
            int r4 = r4 * r3
            int r0 = r0 + r4
            int r4 = r33.mHGap
            int r4 = r4 * r3
            int r4 = r4 + r0
        L_0x09a4:
            r0 = r4
            r4 = r31
            int r5 = r4.getDecoratedMeasurementInOther(r15)
            int r5 = r5 + r0
            r38 = r5
            r5 = r0
            r0 = r2
            r2 = r1
        L_0x09b1:
            r1 = r38
            goto L_0x0a01
        L_0x09b4:
            r4 = r31
            if (r30 == 0) goto L_0x09db
            int r1 = r44.getPaddingTop()
            int r2 = r33.getFamilyMarginTop()
            int r1 = r1 + r2
            int r2 = r33.getFamilyPaddingTop()
            int r1 = r1 + r2
            r2 = r1
            r1 = 0
        L_0x09c8:
            if (r1 >= r3) goto L_0x09f7
            int[] r17 = r33.mSpanCols
            r17 = r17[r1]
            int r18 = r33.mVGap
            int r17 = r17 + r18
            int r2 = r2 + r17
            int r1 = r1 + 1
            goto L_0x09c8
        L_0x09db:
            int r1 = r44.getPaddingTop()
            int r2 = r33.getFamilyMarginTop()
            int r1 = r1 + r2
            int r2 = r33.getFamilyPaddingTop()
            int r1 = r1 + r2
            int r2 = r33.mSizePerSpan
            int r2 = r2 * r3
            int r1 = r1 + r2
            int r2 = r33.mVGap
            int r2 = r2 * r3
            int r2 = r2 + r1
        L_0x09f7:
            int r1 = r4.getDecoratedMeasurementInOther(r15)
            int r1 = r1 + r2
            r38 = r5
            r5 = r0
            r0 = r1
            goto L_0x09b1
        L_0x0a01:
            boolean r17 = DEBUG
            if (r17 == 0) goto L_0x0a7d
            r34 = r4
            java.lang.String r4 = "RGLayoutHelper"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r35 = r7
            java.lang.String r7 = "layout item in position: "
            r6.append(r7)
            int r7 = r16.getViewPosition()
            r6.append(r7)
            java.lang.String r7 = " with text with SpanIndex: "
            r6.append(r7)
            r6.append(r3)
            java.lang.String r3 = " into ("
            r6.append(r3)
            r6.append(r5)
            java.lang.String r3 = ", "
            r6.append(r3)
            r6.append(r2)
            java.lang.String r3 = ", "
            r6.append(r3)
            r6.append(r1)
            java.lang.String r3 = ", "
            r6.append(r3)
            r6.append(r0)
            java.lang.String r3 = "), topInfo=[layoutState.getOffset()="
            r6.append(r3)
            int r3 = r42.getOffset()
            r6.append(r3)
            java.lang.String r3 = " startSpace="
            r6.append(r3)
            r6.append(r9)
            java.lang.String r3 = " secondStartSpace="
            r6.append(r3)
            r6.append(r10)
            java.lang.String r3 = " consumedGap="
            r6.append(r3)
            r6.append(r11)
            java.lang.String r3 = " lastUnconsumedSpace="
            r6.append(r3)
            r6.append(r12)
            java.lang.String r3 = "]"
            r6.append(r3)
            java.lang.String r3 = r6.toString()
            android.util.Log.d(r4, r3)
            goto L_0x0a81
        L_0x0a7d:
            r34 = r4
            r35 = r7
        L_0x0a81:
            r7 = 0
            r17 = r0
            r0 = r33
            r18 = r1
            r1 = r15
            r19 = r2
            r2 = r5
            r3 = r19
            r20 = r34
            r4 = r18
            r21 = r5
            r5 = r17
            r36 = r9
            r9 = r43
            r6 = r44
            r37 = r10
            r22 = r35
            r10 = 0
            r0.layoutChild(r1, r2, r3, r4, r5, r6, r7)
            boolean r0 = r16.isItemRemoved()
            if (r0 != 0) goto L_0x0ab3
            boolean r0 = r16.isItemChanged()
            if (r0 == 0) goto L_0x0ab1
            goto L_0x0ab3
        L_0x0ab1:
            r0 = 1
            goto L_0x0ab6
        L_0x0ab3:
            r0 = 1
            r9.mIgnoreConsumed = r0
        L_0x0ab6:
            boolean r1 = r9.mFocusable
            boolean r2 = r15.isFocusable()
            r1 = r1 | r2
            r9.mFocusable = r1
            int r14 = r14 + 1
            r6 = r9
            r2 = r17
            r5 = r18
            r1 = r19
            r31 = r20
            r0 = r21
            r7 = r22
            r9 = r36
            r10 = r37
            goto L_0x094c
        L_0x0ad4:
            r10 = 0
            r8.mLayoutWithAnchor = r10
            android.view.View[] r0 = r33.mSet
            r1 = 0
            java.util.Arrays.fill(r0, r1)
            int[] r0 = r33.mSpanIndices
            java.util.Arrays.fill(r0, r10)
            int[] r0 = r33.mSpanCols
            java.util.Arrays.fill(r0, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper.layoutViews(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutStateWrapper, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):void");
    }

    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        this.mRangeStyle.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
    }

    public void adjustLayout(int i, int i2, LayoutManagerHelper layoutManagerHelper) {
        this.mRangeStyle.adjustLayout(i, i2, layoutManagerHelper);
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = layoutManagerHelper.getOrientation() == 1;
        if (z) {
            if (i == getItemCount() - 1) {
                return GridRangeStyle.computeEndAlignOffset(this.mRangeStyle, z3);
            }
        } else if (i == 0) {
            return GridRangeStyle.computeStartAlignOffset(this.mRangeStyle, z3);
        }
        return super.computeAlignOffset(i, z, z2, layoutManagerHelper);
    }

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
        this.mRangeStyle.onClear(layoutManagerHelper);
        this.mRangeStyle.onInvalidateSpanIndexCache();
    }

    public void onItemsChanged(LayoutManagerHelper layoutManagerHelper) {
        super.onItemsChanged(layoutManagerHelper);
        this.mRangeStyle.onInvalidateSpanIndexCache();
    }

    private int getMainDirSpec(GridRangeStyle gridRangeStyle, int i, int i2, int i3, float f) {
        if (!Float.isNaN(f) && f > 0.0f && i3 > 0) {
            return View.MeasureSpec.makeMeasureSpec((int) ((((float) i3) / f) + 0.5f), 1073741824);
        }
        if (!Float.isNaN(gridRangeStyle.mAspectRatio) && gridRangeStyle.mAspectRatio > 0.0f) {
            return View.MeasureSpec.makeMeasureSpec((int) ((((float) i2) / gridRangeStyle.mAspectRatio) + 0.5f), 1073741824);
        }
        if (i < 0) {
            return MAIN_DIR_SPEC;
        }
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        if (state.getItemCount() > 0) {
            GridRangeStyle findRangeStyleByPosition = this.mRangeStyle.findRangeStyleByPosition(anchorInfoWrapper.position);
            int cachedSpanIndex = findRangeStyleByPosition.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, findRangeStyleByPosition.mSpanCount);
            if (anchorInfoWrapper.layoutFromEnd) {
                while (cachedSpanIndex < findRangeStyleByPosition.mSpanCount - 1 && anchorInfoWrapper.position < getRange().getUpper().intValue()) {
                    anchorInfoWrapper.position++;
                    cachedSpanIndex = findRangeStyleByPosition.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, findRangeStyleByPosition.mSpanCount);
                }
            } else {
                while (cachedSpanIndex > 0 && anchorInfoWrapper.position > 0) {
                    anchorInfoWrapper.position--;
                    cachedSpanIndex = findRangeStyleByPosition.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, findRangeStyleByPosition.mSpanCount);
                }
            }
            this.mLayoutWithAnchor = true;
        }
    }

    private int getSpanIndex(GridLayoutHelper.SpanSizeLookup spanSizeLookup, int i, RecyclerView.Recycler recycler, RecyclerView.State state, int i2) {
        if (!state.isPreLayout()) {
            return spanSizeLookup.getCachedSpanIndex(i2, i);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i2);
        if (convertPreLayoutPositionToPostLayout == -1) {
            return 0;
        }
        return spanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, i);
    }

    private int getSpanSize(GridLayoutHelper.SpanSizeLookup spanSizeLookup, RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        if (!state.isPreLayout()) {
            return spanSizeLookup.getSpanSize(i);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            return 0;
        }
        return spanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }

    private void assignSpans(GridRangeStyle gridRangeStyle, RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, boolean z, LayoutManagerHelper layoutManagerHelper) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 0;
        if (z) {
            i3 = i;
            i4 = 0;
            i5 = 1;
        } else {
            i4 = i - 1;
            i3 = -1;
            i5 = -1;
        }
        if (layoutManagerHelper.getOrientation() != 1 || !layoutManagerHelper.isDoLayoutRTL()) {
            i6 = 1;
        } else {
            i7 = i2 - 1;
            i6 = -1;
        }
        while (i4 != i3) {
            int spanSize = getSpanSize(gridRangeStyle.mSpanSizeLookup, recycler, state, layoutManagerHelper.getPosition(gridRangeStyle.mSet[i4]));
            if (i6 != -1 || spanSize <= 1) {
                gridRangeStyle.mSpanIndices[i4] = i7;
            } else {
                gridRangeStyle.mSpanIndices[i4] = i7 - (spanSize - 1);
            }
            i7 += spanSize * i6;
            i4 += i5;
        }
    }

    public int getBorderStartSpace(LayoutManagerHelper layoutManagerHelper) {
        GridRangeStyle findRangeStyleByPosition = this.mRangeStyle.findRangeStyleByPosition(getRange().getLower().intValue());
        if (layoutManagerHelper.getOrientation() == 1) {
            return findRangeStyleByPosition.getFamilyMarginTop() + findRangeStyleByPosition.getFamilyPaddingTop();
        }
        return findRangeStyleByPosition.getFamilyMarginLeft() + findRangeStyleByPosition.getFamilyPaddingLeft();
    }

    public int getBorderEndSpace(LayoutManagerHelper layoutManagerHelper) {
        GridRangeStyle findRangeStyleByPosition = this.mRangeStyle.findRangeStyleByPosition(getRange().getUpper().intValue());
        if (layoutManagerHelper.getOrientation() == 1) {
            return findRangeStyleByPosition.getFamilyMarginBottom() + findRangeStyleByPosition.getFamilyPaddingBottom();
        }
        return findRangeStyleByPosition.getFamilyMarginRight() + findRangeStyleByPosition.getFamilyPaddingRight();
    }

    public static class GridRangeStyle extends RangeStyle<GridRangeStyle> {
        /* access modifiers changed from: private */
        public float mAspectRatio = Float.NaN;
        /* access modifiers changed from: private */
        public int mHGap = 0;
        private boolean mIgnoreExtra = false;
        /* access modifiers changed from: private */
        public boolean mIsAutoExpand = true;
        /* access modifiers changed from: private */
        public View[] mSet;
        /* access modifiers changed from: private */
        public int mSizePerSpan = 0;
        /* access modifiers changed from: private */
        public int[] mSpanCols;
        /* access modifiers changed from: private */
        public int mSpanCount = 4;
        /* access modifiers changed from: private */
        public int[] mSpanIndices;
        /* access modifiers changed from: private */
        @NonNull
        public GridLayoutHelper.SpanSizeLookup mSpanSizeLookup = new GridLayoutHelper.DefaultSpanSizeLookup();
        /* access modifiers changed from: private */
        public int mVGap = 0;
        /* access modifiers changed from: private */
        public float[] mWeights = new float[0];

        public GridRangeStyle(RangeGridLayoutHelper rangeGridLayoutHelper) {
            super(rangeGridLayoutHelper);
            this.mSpanSizeLookup.setSpanIndexCacheEnabled(true);
        }

        public GridRangeStyle() {
            this.mSpanSizeLookup.setSpanIndexCacheEnabled(true);
        }

        public GridRangeStyle findRangeStyleByPosition(int i) {
            return findRangeStyle(this, i);
        }

        private GridRangeStyle findRangeStyle(GridRangeStyle gridRangeStyle, int i) {
            int size = gridRangeStyle.mChildren.size();
            for (int i2 = 0; i2 < size; i2++) {
                GridRangeStyle gridRangeStyle2 = (GridRangeStyle) gridRangeStyle.mChildren.valueAt(i2);
                Range range = (Range) gridRangeStyle.mChildren.keyAt(i2);
                if (!gridRangeStyle2.isChildrenEmpty()) {
                    return findRangeStyle(gridRangeStyle2, i);
                }
                if (range.contains(Integer.valueOf(i))) {
                    return (GridRangeStyle) gridRangeStyle.mChildren.valueAt(i2);
                }
            }
            return gridRangeStyle;
        }

        public GridRangeStyle findSiblingStyleByPosition(int i) {
            if (this.mParent != null) {
                ArrayMap arrayMap = ((GridRangeStyle) this.mParent).mChildren;
                int i2 = 0;
                int size = arrayMap.size();
                while (true) {
                    if (i2 >= size) {
                        break;
                    } else if (((Range) arrayMap.keyAt(i2)).contains(Integer.valueOf(i))) {
                        GridRangeStyle gridRangeStyle = (GridRangeStyle) arrayMap.valueAt(i2);
                        if (!gridRangeStyle.equals(this)) {
                            return gridRangeStyle;
                        }
                    } else {
                        i2++;
                    }
                }
            }
            return null;
        }

        public void onInvalidateSpanIndexCache() {
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                ((GridRangeStyle) this.mChildren.valueAt(i)).onInvalidateSpanIndexCache();
            }
        }

        public static int computeEndAlignOffset(GridRangeStyle gridRangeStyle, boolean z) {
            int i;
            int i2;
            int i3;
            int i4;
            if (z) {
                i = gridRangeStyle.mMarginBottom;
                i2 = gridRangeStyle.mPaddingBottom;
            } else {
                i = gridRangeStyle.mMarginRight;
                i2 = gridRangeStyle.mPaddingRight;
            }
            int i5 = i + i2;
            int intValue = gridRangeStyle.getRange().getUpper().intValue();
            int size = gridRangeStyle.mChildren.size();
            for (int i6 = 0; i6 < size; i6++) {
                GridRangeStyle gridRangeStyle2 = (GridRangeStyle) gridRangeStyle.mChildren.valueAt(i6);
                if (!gridRangeStyle2.isChildrenEmpty()) {
                    i5 += computeEndAlignOffset(gridRangeStyle2, z);
                } else if (((Integer) gridRangeStyle2.mRange.getUpper()).intValue() == intValue) {
                    if (z) {
                        i3 = gridRangeStyle2.mMarginBottom;
                        i4 = gridRangeStyle2.mPaddingBottom;
                    } else {
                        i3 = gridRangeStyle2.mMarginRight;
                        i4 = gridRangeStyle2.mPaddingRight;
                    }
                    return i5 + i3 + i4;
                }
            }
            return i5;
        }

        public static int computeStartAlignOffset(GridRangeStyle gridRangeStyle, boolean z) {
            int i;
            int i2;
            int i3;
            int i4;
            if (z) {
                i = -gridRangeStyle.mMarginTop;
                i2 = gridRangeStyle.mPaddingTop;
            } else {
                i = -gridRangeStyle.mMarginLeft;
                i2 = gridRangeStyle.mPaddingLeft;
            }
            int i5 = i - i2;
            int intValue = gridRangeStyle.getRange().getLower().intValue();
            int size = gridRangeStyle.mChildren.size();
            for (int i6 = 0; i6 < size; i6++) {
                GridRangeStyle gridRangeStyle2 = (GridRangeStyle) gridRangeStyle.mChildren.valueAt(i6);
                if (!gridRangeStyle2.isChildrenEmpty()) {
                    i5 += computeStartAlignOffset(gridRangeStyle2, z);
                } else if (((Integer) gridRangeStyle2.mRange.getLower()).intValue() == intValue) {
                    if (z) {
                        i3 = -gridRangeStyle2.mMarginTop;
                        i4 = gridRangeStyle2.mPaddingTop;
                    } else {
                        i3 = -gridRangeStyle2.mMarginLeft;
                        i4 = gridRangeStyle2.mPaddingLeft;
                    }
                    return i5 + (i3 - i4);
                }
            }
            return i5;
        }

        public void setAspectRatio(float f) {
            this.mAspectRatio = f;
        }

        public float getAspectRatio() {
            return this.mAspectRatio;
        }

        public void setRange(int i, int i2) {
            super.setRange(i, i2);
            this.mSpanSizeLookup.setStartPosition(i);
            this.mSpanSizeLookup.invalidateSpanIndexCache();
        }

        public void setGap(int i) {
            setVGap(i);
            setHGap(i);
        }

        public void setVGap(int i) {
            if (i < 0) {
                i = 0;
            }
            this.mVGap = i;
        }

        public void setHGap(int i) {
            if (i < 0) {
                i = 0;
            }
            this.mHGap = i;
        }

        public void setWeights(float[] fArr) {
            if (fArr != null) {
                this.mWeights = Arrays.copyOf(fArr, fArr.length);
            } else {
                this.mWeights = new float[0];
            }
        }

        public void setSpanSizeLookup(GridLayoutHelper.SpanSizeLookup spanSizeLookup) {
            if (spanSizeLookup != null) {
                spanSizeLookup.setStartPosition(this.mSpanSizeLookup.getStartPosition());
                this.mSpanSizeLookup = spanSizeLookup;
            }
        }

        public void setAutoExpand(boolean z) {
            this.mIsAutoExpand = z;
        }

        public void setIgnoreExtra(boolean z) {
            this.mIgnoreExtra = z;
        }

        public void setSpanCount(int i) {
            if (i != this.mSpanCount) {
                if (i >= 1) {
                    this.mSpanCount = i;
                    this.mSpanSizeLookup.invalidateSpanIndexCache();
                    ensureSpanCount();
                    return;
                }
                throw new IllegalArgumentException("Span count should be at least 1. Provided " + i);
            }
        }

        public int getSpanCount() {
            return this.mSpanCount;
        }

        /* access modifiers changed from: private */
        public void ensureSpanCount() {
            if (this.mSet == null || this.mSet.length != this.mSpanCount) {
                this.mSet = new View[this.mSpanCount];
            }
            if (this.mSpanIndices == null || this.mSpanIndices.length != this.mSpanCount) {
                this.mSpanIndices = new int[this.mSpanCount];
            }
            if (this.mSpanCols == null || this.mSpanCols.length != this.mSpanCount) {
                this.mSpanCols = new int[this.mSpanCount];
            }
        }
    }
}
