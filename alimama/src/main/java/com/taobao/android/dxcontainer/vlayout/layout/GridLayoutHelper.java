package com.taobao.android.dxcontainer.vlayout.layout;

import android.util.SparseIntArray;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import java.util.Arrays;

public class GridLayoutHelper extends BaseLayoutHelper {
    private static boolean DEBUG = false;
    private static final int MAIN_DIR_SPEC = View.MeasureSpec.makeMeasureSpec(0, 0);
    private static final String TAG = "GridLayoutHelper";
    private int mHGap;
    private boolean mIgnoreExtra;
    private boolean mIsAutoExpand;
    private boolean mLayoutWithAnchor;
    private View[] mSet;
    private int mSizePerSpan;
    private int[] mSpanCols;
    private int mSpanCount;
    private int[] mSpanIndices;
    @NonNull
    private SpanSizeLookup mSpanSizeLookup;
    private int mTotalSize;
    private int mVGap;
    private float[] mWeights;

    public GridLayoutHelper(int i) {
        this(i, -1, -1);
    }

    public GridLayoutHelper(int i, int i2) {
        this(i, i2, 0);
    }

    public GridLayoutHelper(int i, int i2, int i3) {
        this(i, i2, i3, i3);
    }

    public GridLayoutHelper(int i, int i2, int i3, int i4) {
        this.mSpanCount = 4;
        this.mSizePerSpan = 0;
        this.mTotalSize = 0;
        this.mIsAutoExpand = true;
        this.mIgnoreExtra = false;
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mVGap = 0;
        this.mHGap = 0;
        this.mWeights = new float[0];
        this.mLayoutWithAnchor = false;
        setSpanCount(i);
        this.mSpanSizeLookup.setSpanIndexCacheEnabled(true);
        setItemCount(i2);
        setVGap(i3);
        setHGap(i4);
    }

    public void setWeights(float[] fArr) {
        if (fArr != null) {
            this.mWeights = Arrays.copyOf(fArr, fArr.length);
        } else {
            this.mWeights = new float[0];
        }
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
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

    public int getVGap() {
        return this.mVGap;
    }

    public int getHGap() {
        return this.mHGap;
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public void onRangeChange(int i, int i2) {
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

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0101, code lost:
        if (r4 == getRange().getUpper().intValue()) goto L_0x0103;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0105, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0117, code lost:
        if (r4 == getRange().getLower().intValue()) goto L_0x0103;
     */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x02a3 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x02a4  */
    /* JADX WARNING: Removed duplicated region for block: B:290:0x01d5 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01f6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutViews(androidx.recyclerview.widget.RecyclerView.Recycler r28, androidx.recyclerview.widget.RecyclerView.State r29, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutStateWrapper r30, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult r31, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r32) {
        /*
            r27 = this;
            r7 = r27
            r8 = r28
            r9 = r29
            r10 = r30
            r11 = r31
            r12 = r32
            int r0 = r30.getCurrentPosition()
            boolean r0 = r7.isOutOfRange(r0)
            if (r0 == 0) goto L_0x0017
            return
        L_0x0017:
            r30.getCurrentPosition()
            boolean r13 = r32.isEnableMarginOverLap()
            int r0 = r30.getItemDirection()
            r14 = 1
            if (r0 != r14) goto L_0x0028
            r16 = 1
            goto L_0x002a
        L_0x0028:
            r16 = 0
        L_0x002a:
            com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r6 = r32.getMainOrientationHelper()
            int r1 = r32.getOrientation()
            if (r1 != r14) goto L_0x0036
            r5 = 1
            goto L_0x0037
        L_0x0036:
            r5 = 0
        L_0x0037:
            r17 = 1056964608(0x3f000000, float:0.5)
            r18 = 1065353216(0x3f800000, float:1.0)
            if (r5 == 0) goto L_0x006e
            int r1 = r32.getContentWidth()
            int r2 = r32.getPaddingRight()
            int r1 = r1 - r2
            int r2 = r32.getPaddingLeft()
            int r1 = r1 - r2
            int r2 = r27.getHorizontalMargin()
            int r1 = r1 - r2
            int r2 = r27.getHorizontalPadding()
            int r1 = r1 - r2
            r7.mTotalSize = r1
            int r1 = r7.mTotalSize
            int r2 = r7.mSpanCount
            int r2 = r2 - r14
            int r3 = r7.mHGap
            int r2 = r2 * r3
            int r1 = r1 - r2
            float r1 = (float) r1
            float r1 = r1 * r18
            int r2 = r7.mSpanCount
            float r2 = (float) r2
            float r1 = r1 / r2
            float r1 = r1 + r17
            int r1 = (int) r1
            r7.mSizePerSpan = r1
            goto L_0x009e
        L_0x006e:
            int r1 = r32.getContentHeight()
            int r2 = r32.getPaddingBottom()
            int r1 = r1 - r2
            int r2 = r32.getPaddingTop()
            int r1 = r1 - r2
            int r2 = r27.getVerticalMargin()
            int r1 = r1 - r2
            int r2 = r27.getVerticalPadding()
            int r1 = r1 - r2
            r7.mTotalSize = r1
            int r1 = r7.mTotalSize
            int r2 = r7.mSpanCount
            int r2 = r2 - r14
            int r3 = r7.mVGap
            int r2 = r2 * r3
            int r1 = r1 - r2
            float r1 = (float) r1
            float r1 = r1 * r18
            int r2 = r7.mSpanCount
            float r2 = (float) r2
            float r1 = r1 / r2
            float r1 = r1 + r17
            int r1 = (int) r1
            r7.mSizePerSpan = r1
        L_0x009e:
            int r1 = r7.mSpanCount
            r27.ensureSpanCount()
            if (r16 != 0) goto L_0x01b8
            int r1 = r30.getCurrentPosition()
            int r1 = r7.getSpanIndex(r8, r9, r1)
            int r2 = r30.getCurrentPosition()
            int r2 = r7.getSpanSize(r8, r9, r2)
            int r2 = r2 + r1
            int r3 = r7.mSpanCount
            int r3 = r3 - r14
            if (r1 == r3) goto L_0x01b9
            int r1 = r30.getCurrentPosition()
            int r3 = r7.mSpanCount
            int r3 = r3 - r2
            r4 = r1
            r1 = 0
            r19 = 0
            r20 = 0
            r21 = 0
        L_0x00ca:
            int r14 = r7.mSpanCount
            if (r1 >= r14) goto L_0x018c
            if (r3 <= 0) goto L_0x018c
            int r4 = r4 - r0
            boolean r14 = r7.isOutOfRange(r4)
            if (r14 == 0) goto L_0x00d9
            goto L_0x018c
        L_0x00d9:
            int r14 = r7.getSpanSize(r8, r9, r4)
            int r15 = r7.mSpanCount
            if (r14 > r15) goto L_0x015e
            android.view.View r15 = r10.retrieve(r8, r4)
            if (r15 != 0) goto L_0x00e9
            goto L_0x018c
        L_0x00e9:
            if (r19 != 0) goto L_0x011d
            boolean r19 = r32.getReverseLayout()
            if (r19 == 0) goto L_0x0107
            com.taobao.android.dxcontainer.vlayout.Range r19 = r27.getRange()
            java.lang.Comparable r19 = r19.getUpper()
            java.lang.Integer r19 = (java.lang.Integer) r19
            r22 = r0
            int r0 = r19.intValue()
            if (r4 != r0) goto L_0x0105
        L_0x0103:
            r0 = 1
            goto L_0x011a
        L_0x0105:
            r0 = 0
            goto L_0x011a
        L_0x0107:
            r22 = r0
            com.taobao.android.dxcontainer.vlayout.Range r0 = r27.getRange()
            java.lang.Comparable r0 = r0.getLower()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r4 != r0) goto L_0x0105
            goto L_0x0103
        L_0x011a:
            r19 = r0
            goto L_0x011f
        L_0x011d:
            r22 = r0
        L_0x011f:
            if (r21 != 0) goto L_0x014e
            boolean r0 = r32.getReverseLayout()
            if (r0 == 0) goto L_0x013b
            com.taobao.android.dxcontainer.vlayout.Range r0 = r27.getRange()
            java.lang.Comparable r0 = r0.getLower()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r4 != r0) goto L_0x0139
        L_0x0137:
            r0 = 1
            goto L_0x014c
        L_0x0139:
            r0 = 0
            goto L_0x014c
        L_0x013b:
            com.taobao.android.dxcontainer.vlayout.Range r0 = r27.getRange()
            java.lang.Comparable r0 = r0.getUpper()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r4 != r0) goto L_0x0139
            goto L_0x0137
        L_0x014c:
            r21 = r0
        L_0x014e:
            int r3 = r3 - r14
            if (r3 >= 0) goto L_0x0152
            goto L_0x018c
        L_0x0152:
            int r20 = r20 + r14
            android.view.View[] r0 = r7.mSet
            r0[r1] = r15
            int r1 = r1 + 1
            r0 = r22
            goto L_0x00ca
        L_0x015e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Item at position "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r2 = " requires "
            r1.append(r2)
            r1.append(r14)
            java.lang.String r2 = " spans but GridLayoutManager has only "
            r1.append(r2)
            int r2 = r7.mSpanCount
            r1.append(r2)
            java.lang.String r2 = " spans."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x018c:
            r15 = r19
            if (r1 <= 0) goto L_0x01af
            int r0 = r1 + -1
            r3 = r0
            r0 = 0
        L_0x0194:
            if (r0 >= r3) goto L_0x01af
            android.view.View[] r4 = r7.mSet
            r4 = r4[r0]
            android.view.View[] r14 = r7.mSet
            r23 = r1
            android.view.View[] r1 = r7.mSet
            r1 = r1[r3]
            r14[r0] = r1
            android.view.View[] r1 = r7.mSet
            r1[r3] = r4
            int r0 = r0 + 1
            int r3 = r3 + -1
            r1 = r23
            goto L_0x0194
        L_0x01af:
            r23 = r1
            r19 = r15
            r15 = r20
            r14 = r23
            goto L_0x01bf
        L_0x01b8:
            r2 = r1
        L_0x01b9:
            r14 = 0
            r15 = 0
            r19 = 0
            r21 = 0
        L_0x01bf:
            int r0 = r7.mSpanCount
            if (r14 >= r0) goto L_0x029f
            boolean r0 = r10.hasMore(r9)
            if (r0 == 0) goto L_0x029f
            if (r2 <= 0) goto L_0x029f
            int r0 = r30.getCurrentPosition()
            boolean r1 = r7.isOutOfRange(r0)
            if (r1 == 0) goto L_0x01f6
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x029f
            java.lang.String r1 = "GridLayoutHelper"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "pos ["
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = "] is out of range"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.util.Log.d(r1, r0)
            goto L_0x029f
        L_0x01f6:
            int r1 = r7.getSpanSize(r8, r9, r0)
            int r3 = r7.mSpanCount
            if (r1 > r3) goto L_0x0271
            int r2 = r2 - r1
            if (r2 >= 0) goto L_0x0202
            goto L_0x0208
        L_0x0202:
            android.view.View r3 = r10.next(r8)
            if (r3 != 0) goto L_0x020a
        L_0x0208:
            goto L_0x029f
        L_0x020a:
            if (r19 != 0) goto L_0x0239
            boolean r4 = r32.getReverseLayout()
            if (r4 == 0) goto L_0x0226
            com.taobao.android.dxcontainer.vlayout.Range r4 = r27.getRange()
            java.lang.Comparable r4 = r4.getUpper()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r0 != r4) goto L_0x0224
        L_0x0222:
            r4 = 1
            goto L_0x0237
        L_0x0224:
            r4 = 0
            goto L_0x0237
        L_0x0226:
            com.taobao.android.dxcontainer.vlayout.Range r4 = r27.getRange()
            java.lang.Comparable r4 = r4.getLower()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r0 != r4) goto L_0x0224
            goto L_0x0222
        L_0x0237:
            r19 = r4
        L_0x0239:
            if (r21 != 0) goto L_0x0268
            boolean r4 = r32.getReverseLayout()
            if (r4 == 0) goto L_0x0255
            com.taobao.android.dxcontainer.vlayout.Range r4 = r27.getRange()
            java.lang.Comparable r4 = r4.getLower()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r0 != r4) goto L_0x0253
        L_0x0251:
            r0 = 1
            goto L_0x0266
        L_0x0253:
            r0 = 0
            goto L_0x0266
        L_0x0255:
            com.taobao.android.dxcontainer.vlayout.Range r4 = r27.getRange()
            java.lang.Comparable r4 = r4.getUpper()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            if (r0 != r4) goto L_0x0253
            goto L_0x0251
        L_0x0266:
            r21 = r0
        L_0x0268:
            int r15 = r15 + r1
            android.view.View[] r0 = r7.mSet
            r0[r14] = r3
            int r14 = r14 + 1
            goto L_0x01bf
        L_0x0271:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Item at position "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = " requires "
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = " spans but GridLayoutManager has only "
            r3.append(r0)
            int r0 = r7.mSpanCount
            r3.append(r0)
            java.lang.String r0 = " spans."
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.<init>(r0)
            throw r2
        L_0x029f:
            r20 = r2
            if (r14 != 0) goto L_0x02a4
            return
        L_0x02a4:
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r14
            r4 = r15
            r11 = r5
            r5 = r16
            r24 = r13
            r13 = r6
            r6 = r32
            r0.assignSpans(r1, r2, r3, r4, r5, r6)
            if (r20 <= 0) goto L_0x02db
            if (r14 != r15) goto L_0x02db
            boolean r0 = r7.mIsAutoExpand
            if (r0 == 0) goto L_0x02db
            if (r11 == 0) goto L_0x02ce
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r14
            r7.mSizePerSpan = r0
            goto L_0x0300
        L_0x02ce:
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r14
            r7.mSizePerSpan = r0
            goto L_0x0300
        L_0x02db:
            if (r16 != 0) goto L_0x0300
            if (r20 != 0) goto L_0x0300
            if (r14 != r15) goto L_0x0300
            boolean r0 = r7.mIsAutoExpand
            if (r0 == 0) goto L_0x0300
            if (r11 == 0) goto L_0x02f4
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r14
            r7.mSizePerSpan = r0
            goto L_0x0300
        L_0x02f4:
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            int r0 = r0 / r14
            r7.mSizePerSpan = r0
        L_0x0300:
            float[] r0 = r7.mWeights
            if (r0 == 0) goto L_0x037e
            float[] r0 = r7.mWeights
            int r0 = r0.length
            if (r0 <= 0) goto L_0x037e
            if (r11 == 0) goto L_0x0315
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mHGap
            int r1 = r1 * r2
            int r0 = r0 - r1
            goto L_0x031e
        L_0x0315:
            int r0 = r7.mTotalSize
            int r1 = r14 + -1
            int r2 = r7.mVGap
            int r1 = r1 * r2
            int r0 = r0 - r1
        L_0x031e:
            if (r20 <= 0) goto L_0x0326
            boolean r1 = r7.mIsAutoExpand
            if (r1 == 0) goto L_0x0326
            r1 = r14
            goto L_0x0328
        L_0x0326:
            int r1 = r7.mSpanCount
        L_0x0328:
            r4 = r0
            r2 = 0
            r3 = 0
        L_0x032b:
            if (r2 >= r1) goto L_0x0369
            float[] r5 = r7.mWeights
            int r5 = r5.length
            if (r2 >= r5) goto L_0x035f
            float[] r5 = r7.mWeights
            r5 = r5[r2]
            boolean r5 = java.lang.Float.isNaN(r5)
            if (r5 != 0) goto L_0x035f
            float[] r5 = r7.mWeights
            r5 = r5[r2]
            r6 = 0
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 < 0) goto L_0x035f
            float[] r5 = r7.mWeights
            r5 = r5[r2]
            int[] r6 = r7.mSpanCols
            float r5 = r5 * r18
            r20 = 1120403456(0x42c80000, float:100.0)
            float r5 = r5 / r20
            float r15 = (float) r0
            float r5 = r5 * r15
            float r5 = r5 + r17
            int r5 = (int) r5
            r6[r2] = r5
            int[] r5 = r7.mSpanCols
            r5 = r5[r2]
            int r4 = r4 - r5
            goto L_0x0366
        L_0x035f:
            int r3 = r3 + 1
            int[] r5 = r7.mSpanCols
            r6 = -1
            r5[r2] = r6
        L_0x0366:
            int r2 = r2 + 1
            goto L_0x032b
        L_0x0369:
            if (r3 <= 0) goto L_0x037c
            int r4 = r4 / r3
            r0 = 0
        L_0x036d:
            if (r0 >= r1) goto L_0x037c
            int[] r2 = r7.mSpanCols
            r2 = r2[r0]
            if (r2 >= 0) goto L_0x0379
            int[] r2 = r7.mSpanCols
            r2[r0] = r4
        L_0x0379:
            int r0 = r0 + 1
            goto L_0x036d
        L_0x037c:
            r15 = 1
            goto L_0x037f
        L_0x037e:
            r15 = 0
        L_0x037f:
            r0 = 0
            r1 = 0
        L_0x0381:
            if (r0 >= r14) goto L_0x0415
            android.view.View[] r3 = r7.mSet
            r3 = r3[r0]
            if (r16 == 0) goto L_0x038b
            r4 = -1
            goto L_0x038c
        L_0x038b:
            r4 = 0
        L_0x038c:
            r12.addChildView(r10, r3, r4)
            int r4 = r12.getPosition(r3)
            int r4 = r7.getSpanSize(r8, r9, r4)
            if (r15 == 0) goto L_0x03b9
            int[] r5 = r7.mSpanIndices
            r5 = r5[r0]
            r2 = 0
            r6 = 0
        L_0x039f:
            if (r6 >= r4) goto L_0x03ad
            int[] r10 = r7.mSpanCols
            int r17 = r6 + r5
            r10 = r10[r17]
            int r2 = r2 + r10
            int r6 = r6 + 1
            r10 = r30
            goto L_0x039f
        L_0x03ad:
            r6 = 0
            int r2 = java.lang.Math.max(r6, r2)
            r4 = 1073741824(0x40000000, float:2.0)
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
            goto L_0x03d4
        L_0x03b9:
            r6 = 0
            int r2 = r7.mSizePerSpan
            int r2 = r2 * r4
            int r4 = r4 + -1
            int r4 = java.lang.Math.max(r6, r4)
            if (r11 == 0) goto L_0x03c9
            int r5 = r7.mHGap
            goto L_0x03cb
        L_0x03c9:
            int r5 = r7.mVGap
        L_0x03cb:
            int r4 = r4 * r5
            int r2 = r2 + r4
            r4 = 1073741824(0x40000000, float:2.0)
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
        L_0x03d4:
            android.view.ViewGroup$LayoutParams r4 = r3.getLayoutParams()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r4 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r4
            int r5 = r32.getOrientation()
            r6 = 1
            if (r5 != r6) goto L_0x03f3
            int r5 = r4.height
            int r6 = r7.mTotalSize
            int r10 = android.view.View.MeasureSpec.getSize(r2)
            float r4 = r4.mAspectRatio
            int r4 = r7.getMainDirSpec(r5, r6, r10, r4)
            r12.measureChildWithMargins(r3, r2, r4)
            goto L_0x0408
        L_0x03f3:
            int r5 = r4.width
            int r6 = r7.mTotalSize
            int r10 = android.view.View.MeasureSpec.getSize(r2)
            float r4 = r4.mAspectRatio
            int r4 = r7.getMainDirSpec(r5, r6, r10, r4)
            int r2 = android.view.View.MeasureSpec.getSize(r2)
            r12.measureChildWithMargins(r3, r4, r2)
        L_0x0408:
            int r2 = r13.getDecoratedMeasurement(r3)
            if (r2 <= r1) goto L_0x040f
            r1 = r2
        L_0x040f:
            int r0 = r0 + 1
            r10 = r30
            goto L_0x0381
        L_0x0415:
            int r0 = r7.mTotalSize
            r2 = 2143289344(0x7fc00000, float:NaN)
            r3 = 0
            int r0 = r7.getMainDirSpec(r1, r0, r3, r2)
            r2 = 0
        L_0x041f:
            if (r2 >= r14) goto L_0x0489
            android.view.View[] r3 = r7.mSet
            r3 = r3[r2]
            int r4 = r13.getDecoratedMeasurement(r3)
            if (r4 == r1) goto L_0x0482
            int r4 = r12.getPosition(r3)
            int r4 = r7.getSpanSize(r8, r9, r4)
            if (r15 == 0) goto L_0x0458
            int[] r5 = r7.mSpanIndices
            r5 = r5[r2]
            r6 = 0
            r10 = 0
        L_0x043b:
            if (r6 >= r4) goto L_0x0449
            int[] r8 = r7.mSpanCols
            int r16 = r6 + r5
            r8 = r8[r16]
            int r10 = r10 + r8
            int r6 = r6 + 1
            r8 = r28
            goto L_0x043b
        L_0x0449:
            r6 = 0
            int r4 = java.lang.Math.max(r6, r10)
            r5 = 1073741824(0x40000000, float:2.0)
            int r4 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r5)
            r5 = r4
            r4 = 1073741824(0x40000000, float:2.0)
            goto L_0x0473
        L_0x0458:
            r6 = 0
            int r5 = r7.mSizePerSpan
            int r5 = r5 * r4
            int r4 = r4 + -1
            int r4 = java.lang.Math.max(r6, r4)
            if (r11 == 0) goto L_0x0468
            int r6 = r7.mHGap
            goto L_0x046a
        L_0x0468:
            int r6 = r7.mVGap
        L_0x046a:
            int r4 = r4 * r6
            int r5 = r5 + r4
            r4 = 1073741824(0x40000000, float:2.0)
            int r5 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r4)
        L_0x0473:
            int r6 = r32.getOrientation()
            r8 = 1
            if (r6 != r8) goto L_0x047e
            r12.measureChildWithMargins(r3, r5, r0)
            goto L_0x0484
        L_0x047e:
            r12.measureChildWithMargins(r3, r0, r5)
            goto L_0x0484
        L_0x0482:
            r4 = 1073741824(0x40000000, float:2.0)
        L_0x0484:
            int r2 = r2 + 1
            r8 = r28
            goto L_0x041f
        L_0x0489:
            if (r19 == 0) goto L_0x0498
            boolean r0 = r32.getReverseLayout()
            r2 = 1
            r0 = r0 ^ r2
            r3 = r24
            int r0 = r7.computeStartSpace(r12, r11, r0, r3)
            goto L_0x049c
        L_0x0498:
            r3 = r24
            r2 = 1
            r0 = 0
        L_0x049c:
            if (r21 == 0) goto L_0x04a8
            boolean r4 = r32.getReverseLayout()
            r4 = r4 ^ r2
            int r2 = r7.computeEndSpace(r12, r11, r4, r3)
            goto L_0x04a9
        L_0x04a8:
            r2 = 0
        L_0x04a9:
            int r3 = r1 + r0
            int r3 = r3 + r2
            r9 = r11
            r8 = r31
            r8.mConsumed = r3
            int r3 = r30.getLayoutDirection()
            r4 = -1
            if (r3 != r4) goto L_0x04ba
            r3 = 1
            goto L_0x04bb
        L_0x04ba:
            r3 = 0
        L_0x04bb:
            boolean r4 = r7.mLayoutWithAnchor
            if (r4 != 0) goto L_0x04d3
            if (r21 == 0) goto L_0x04c3
            if (r3 != 0) goto L_0x04d3
        L_0x04c3:
            if (r19 == 0) goto L_0x04c7
            if (r3 == 0) goto L_0x04d3
        L_0x04c7:
            int r3 = r8.mConsumed
            if (r9 == 0) goto L_0x04ce
            int r4 = r7.mVGap
            goto L_0x04d0
        L_0x04ce:
            int r4 = r7.mHGap
        L_0x04d0:
            int r3 = r3 + r4
            r8.mConsumed = r3
        L_0x04d3:
            if (r9 == 0) goto L_0x0509
            int r3 = r30.getLayoutDirection()
            r4 = -1
            if (r3 != r4) goto L_0x04f4
            int r0 = r30.getOffset()
            int r0 = r0 - r2
            boolean r2 = r7.mLayoutWithAnchor
            if (r2 != 0) goto L_0x04eb
            if (r21 == 0) goto L_0x04e8
            goto L_0x04eb
        L_0x04e8:
            int r2 = r7.mVGap
            goto L_0x04ec
        L_0x04eb:
            r2 = 0
        L_0x04ec:
            int r0 = r0 - r2
            int r1 = r0 - r1
            r3 = r0
            r2 = r1
        L_0x04f1:
            r0 = 0
            r1 = 0
            goto L_0x053f
        L_0x04f4:
            int r2 = r30.getOffset()
            int r2 = r2 + r0
            boolean r0 = r7.mLayoutWithAnchor
            if (r0 != 0) goto L_0x0503
            if (r19 == 0) goto L_0x0500
            goto L_0x0503
        L_0x0500:
            int r0 = r7.mVGap
            goto L_0x0504
        L_0x0503:
            r0 = 0
        L_0x0504:
            int r0 = r0 + r2
            int r1 = r1 + r0
            r2 = r0
            r3 = r1
            goto L_0x04f1
        L_0x0509:
            int r3 = r30.getLayoutDirection()
            r4 = -1
            if (r3 != r4) goto L_0x052b
            int r0 = r30.getOffset()
            int r0 = r0 - r2
            boolean r2 = r7.mLayoutWithAnchor
            if (r2 != 0) goto L_0x051f
            if (r21 == 0) goto L_0x051c
            goto L_0x051f
        L_0x051c:
            int r2 = r7.mHGap
            goto L_0x0520
        L_0x051f:
            r2 = 0
        L_0x0520:
            int r0 = r0 - r2
            int r1 = r0 - r1
            r2 = 0
            r3 = 0
            r26 = r1
            r1 = r0
            r0 = r26
            goto L_0x053f
        L_0x052b:
            int r2 = r30.getOffset()
            int r2 = r2 + r0
            boolean r0 = r7.mLayoutWithAnchor
            if (r0 != 0) goto L_0x053a
            if (r19 == 0) goto L_0x0537
            goto L_0x053a
        L_0x0537:
            int r0 = r7.mHGap
            goto L_0x053b
        L_0x053a:
            r0 = 0
        L_0x053b:
            int r0 = r0 + r2
            int r1 = r1 + r0
            r2 = 0
            r3 = 0
        L_0x053f:
            r10 = 0
        L_0x0540:
            if (r10 >= r14) goto L_0x065f
            android.view.View[] r4 = r7.mSet
            r11 = r4[r10]
            int[] r4 = r7.mSpanIndices
            r4 = r4[r10]
            android.view.ViewGroup$LayoutParams r5 = r11.getLayoutParams()
            r16 = r5
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutParams r16 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutParams) r16
            if (r9 == 0) goto L_0x058d
            if (r15 == 0) goto L_0x056f
            int r0 = r32.getPaddingLeft()
            int r1 = r7.mMarginLeft
            int r0 = r0 + r1
            int r1 = r7.mPaddingLeft
            int r0 = r0 + r1
            r1 = r0
            r0 = 0
        L_0x0562:
            if (r0 >= r4) goto L_0x0583
            int[] r5 = r7.mSpanCols
            r5 = r5[r0]
            int r6 = r7.mHGap
            int r5 = r5 + r6
            int r1 = r1 + r5
            int r0 = r0 + 1
            goto L_0x0562
        L_0x056f:
            int r0 = r32.getPaddingLeft()
            int r1 = r7.mMarginLeft
            int r0 = r0 + r1
            int r1 = r7.mPaddingLeft
            int r0 = r0 + r1
            int r1 = r7.mSizePerSpan
            int r1 = r1 * r4
            int r0 = r0 + r1
            int r1 = r7.mHGap
            int r1 = r1 * r4
            int r1 = r1 + r0
        L_0x0583:
            int r0 = r13.getDecoratedMeasurementInOther(r11)
            int r0 = r0 + r1
            r6 = r1
            r5 = r2
            r2 = r3
            r3 = r0
            goto L_0x05c4
        L_0x058d:
            if (r15 == 0) goto L_0x05a8
            int r2 = r32.getPaddingTop()
            int r3 = r7.mMarginTop
            int r2 = r2 + r3
            int r3 = r7.mPaddingTop
            int r2 = r2 + r3
            r3 = r2
            r2 = 0
        L_0x059b:
            if (r2 >= r4) goto L_0x05bc
            int[] r5 = r7.mSpanCols
            r5 = r5[r2]
            int r6 = r7.mVGap
            int r5 = r5 + r6
            int r3 = r3 + r5
            int r2 = r2 + 1
            goto L_0x059b
        L_0x05a8:
            int r2 = r32.getPaddingTop()
            int r3 = r7.mMarginTop
            int r2 = r2 + r3
            int r3 = r7.mPaddingTop
            int r2 = r2 + r3
            int r3 = r7.mSizePerSpan
            int r3 = r3 * r4
            int r2 = r2 + r3
            int r3 = r7.mVGap
            int r3 = r3 * r4
            int r3 = r3 + r2
        L_0x05bc:
            int r2 = r13.getDecoratedMeasurementInOther(r11)
            int r2 = r2 + r3
            r6 = r0
            r5 = r3
            r3 = r1
        L_0x05c4:
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0621
            java.lang.String r0 = "GridLayoutHelper"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r25 = r9
            java.lang.String r9 = "layout item in position: "
            r1.append(r9)
            int r9 = r16.getViewPosition()
            r1.append(r9)
            java.lang.String r9 = " with text "
            r1.append(r9)
            r9 = r11
            android.widget.TextView r9 = (android.widget.TextView) r9
            java.lang.CharSequence r9 = r9.getText()
            r1.append(r9)
            java.lang.String r9 = " with SpanIndex: "
            r1.append(r9)
            r1.append(r4)
            java.lang.String r4 = " into ("
            r1.append(r4)
            r1.append(r6)
            java.lang.String r4 = ", "
            r1.append(r4)
            r1.append(r5)
            java.lang.String r4 = ", "
            r1.append(r4)
            r1.append(r3)
            java.lang.String r4 = ", "
            r1.append(r4)
            r1.append(r2)
            java.lang.String r4 = " )"
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            goto L_0x0623
        L_0x0621:
            r25 = r9
        L_0x0623:
            r0 = r27
            r1 = r11
            r9 = r2
            r2 = r6
            r17 = r3
            r3 = r5
            r4 = r17
            r18 = r5
            r5 = r9
            r19 = r6
            r6 = r32
            r0.layoutChildWithMargin(r1, r2, r3, r4, r5, r6)
            boolean r0 = r16.isItemRemoved()
            if (r0 != 0) goto L_0x0646
            boolean r0 = r16.isItemChanged()
            if (r0 == 0) goto L_0x0644
            goto L_0x0646
        L_0x0644:
            r0 = 1
            goto L_0x0649
        L_0x0646:
            r0 = 1
            r8.mIgnoreConsumed = r0
        L_0x0649:
            boolean r1 = r8.mFocusable
            boolean r2 = r11.isFocusable()
            r1 = r1 | r2
            r8.mFocusable = r1
            int r10 = r10 + 1
            r3 = r9
            r1 = r17
            r2 = r18
            r0 = r19
            r9 = r25
            goto L_0x0540
        L_0x065f:
            r1 = 0
            r7.mLayoutWithAnchor = r1
            android.view.View[] r0 = r7.mSet
            r2 = 0
            java.util.Arrays.fill(r0, r2)
            int[] r0 = r7.mSpanIndices
            java.util.Arrays.fill(r0, r1)
            int[] r0 = r7.mSpanCols
            java.util.Arrays.fill(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.GridLayoutHelper.layoutViews(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutStateWrapper, com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):void");
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

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    public void onItemsChanged(LayoutManagerHelper layoutManagerHelper) {
        super.onItemsChanged(layoutManagerHelper);
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    private int getMainDirSpec(int i, int i2, int i3, float f) {
        if (!Float.isNaN(f) && f > 0.0f && i3 > 0) {
            return View.MeasureSpec.makeMeasureSpec((int) ((((float) i3) / f) + 0.5f), 1073741824);
        }
        if (!Float.isNaN(this.mAspectRatio) && this.mAspectRatio > 0.0f) {
            return View.MeasureSpec.makeMeasureSpec((int) ((((float) i2) / this.mAspectRatio) + 0.5f), 1073741824);
        }
        if (i < 0) {
            return MAIN_DIR_SPEC;
        }
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    private void ensureSpanCount() {
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

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            int cachedSpanIndex = this.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, this.mSpanCount);
            if (anchorInfoWrapper.layoutFromEnd) {
                while (cachedSpanIndex < this.mSpanCount - 1 && anchorInfoWrapper.position < getRange().getUpper().intValue()) {
                    anchorInfoWrapper.position++;
                    cachedSpanIndex = this.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, this.mSpanCount);
                }
            } else {
                while (cachedSpanIndex > 0 && anchorInfoWrapper.position > 0) {
                    anchorInfoWrapper.position--;
                    cachedSpanIndex = this.mSpanSizeLookup.getCachedSpanIndex(anchorInfoWrapper.position, this.mSpanCount);
                }
            }
            this.mLayoutWithAnchor = true;
        }
    }

    private int getSpanIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }

    private int getSpanSize(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(i);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            return 0;
        }
        return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }

    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, boolean z, LayoutManagerHelper layoutManagerHelper) {
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
            int spanSize = getSpanSize(recycler, state, layoutManagerHelper.getPosition(this.mSet[i4]));
            if (i6 != -1 || spanSize <= 1) {
                this.mSpanIndices[i4] = i7;
            } else {
                this.mSpanIndices[i4] = i7 - (spanSize - 1);
            }
            i7 += spanSize * i6;
            i4 += i5;
        }
    }

    static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        public int getSpanSize(int i) {
            return 1;
        }

        DefaultSpanSizeLookup() {
        }

        public int getSpanIndex(int i, int i2) {
            return (i - this.mStartPosition) % i2;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanIndexCache = new SparseIntArray();
        int mStartPosition = 0;

        public abstract int getSpanSize(int i);

        public void setSpanIndexCacheEnabled(boolean z) {
            this.mCacheSpanIndices = z;
        }

        public void setStartPosition(int i) {
            this.mStartPosition = i;
        }

        public int getStartPosition() {
            return this.mStartPosition;
        }

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        /* access modifiers changed from: package-private */
        public int getCachedSpanIndex(int i, int i2) {
            if (!this.mCacheSpanIndices) {
                return getSpanIndex(i, i2);
            }
            int i3 = this.mSpanIndexCache.get(i, -1);
            if (i3 != -1) {
                return i3;
            }
            int spanIndex = getSpanIndex(i, i2);
            this.mSpanIndexCache.put(i, spanIndex);
            return spanIndex;
        }

        public int getSpanIndex(int i, int i2) {
            int i3;
            int findReferenceIndexFromCache;
            int spanSize = getSpanSize(i);
            if (spanSize == i2) {
                return 0;
            }
            int i4 = this.mStartPosition;
            if (!this.mCacheSpanIndices || this.mSpanIndexCache.size() <= 0 || (findReferenceIndexFromCache = findReferenceIndexFromCache(i)) < 0) {
                i3 = 0;
            } else {
                int i5 = findReferenceIndexFromCache + 1;
                i3 = this.mSpanIndexCache.get(findReferenceIndexFromCache) + getSpanSize(findReferenceIndexFromCache);
                i4 = i5;
            }
            while (i4 < i) {
                int spanSize2 = getSpanSize(i4);
                int i6 = i3 + spanSize2;
                if (i6 == i2) {
                    i6 = 0;
                } else if (i6 > i2) {
                    i6 = spanSize2;
                }
                i4++;
            }
            if (spanSize + i3 <= i2) {
                return i3;
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public int findReferenceIndexFromCache(int i) {
            int size = this.mSpanIndexCache.size() - 1;
            int i2 = 0;
            while (i2 <= size) {
                int i3 = (i2 + size) >>> 1;
                if (this.mSpanIndexCache.keyAt(i3) < i) {
                    i2 = i3 + 1;
                } else {
                    size = i3 - 1;
                }
            }
            int i4 = i2 - 1;
            if (i4 < 0 || i4 >= this.mSpanIndexCache.size()) {
                return -1;
            }
            return this.mSpanIndexCache.keyAt(i4);
        }

        public int getSpanGroupIndex(int i, int i2) {
            int spanSize = getSpanSize(i);
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                int spanSize2 = getSpanSize(i5);
                i3 += spanSize2;
                if (i3 == i2) {
                    i4++;
                    i3 = 0;
                } else if (i3 > i2) {
                    i4++;
                    i3 = spanSize2;
                }
            }
            return i3 + spanSize > i2 ? i4 + 1 : i4;
        }
    }
}
