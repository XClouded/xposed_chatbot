package com.taobao.android.dxcontainer.vlayout.layout;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.Range;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutHelper extends BaseLayoutHelper {
    static final int INVALID_LINE = Integer.MIN_VALUE;
    private static final int INVALID_SPAN_ID = Integer.MIN_VALUE;
    private static final String LOOKUP_BUNDLE_KEY = "StaggeredGridLayoutHelper_LazySpanLookup";
    private static final String TAG = "Staggered";
    private int anchorPosition;
    private final Runnable checkForGapsRunnable;
    private int mColLength;
    private int mEachGap;
    private int mHGap;
    private int mLastGap;
    private WeakReference<VirtualLayoutManager> mLayoutManager;
    private boolean mLayoutWithAnchor;
    private LazySpanLookup mLazySpanLookup;
    private int mNumLanes;
    private BitSet mRemainingSpans;
    private Span[] mSpans;
    private int mVGap;
    private List<View> prelayoutViewList;

    public void onItemsChanged(LayoutManagerHelper layoutManagerHelper) {
    }

    public StaggeredGridLayoutHelper() {
        this(1, 0);
    }

    public StaggeredGridLayoutHelper(int i) {
        this(i, 0);
    }

    public StaggeredGridLayoutHelper(int i, int i2) {
        this.mNumLanes = 0;
        this.mHGap = 0;
        this.mVGap = 0;
        this.mColLength = 0;
        this.mEachGap = 0;
        this.mLastGap = 0;
        this.mRemainingSpans = null;
        this.mLazySpanLookup = new LazySpanLookup();
        this.prelayoutViewList = new ArrayList();
        this.mLayoutManager = null;
        this.checkForGapsRunnable = new Runnable() {
            public void run() {
                StaggeredGridLayoutHelper.this.checkForGaps();
            }
        };
        setLane(i);
        setGap(i2);
    }

    public void setGap(int i) {
        setHGap(i);
        setVGap(i);
    }

    public void setHGap(int i) {
        this.mHGap = i;
    }

    public int getHGap() {
        return this.mHGap;
    }

    public void setVGap(int i) {
        this.mVGap = i;
    }

    public int getVGap() {
        return this.mVGap;
    }

    public void setLane(int i) {
        this.mNumLanes = i;
        ensureLanes();
    }

    public int getLane() {
        return this.mNumLanes;
    }

    public int getColLength() {
        return this.mColLength;
    }

    private void ensureLanes() {
        if (this.mSpans == null || this.mSpans.length != this.mNumLanes || this.mRemainingSpans == null) {
            this.mRemainingSpans = new BitSet(this.mNumLanes);
            this.mSpans = new Span[this.mNumLanes];
            for (int i = 0; i < this.mNumLanes; i++) {
                this.mSpans[i] = new Span(i);
            }
        }
    }

    public void beforeLayout(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutManagerHelper layoutManagerHelper) {
        int i;
        super.beforeLayout(recycler, state, layoutManagerHelper);
        if (layoutManagerHelper.getOrientation() == 1) {
            i = (((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight()) - getHorizontalMargin()) - getHorizontalPadding();
        } else {
            i = (((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom()) - getVerticalMargin()) - getVerticalPadding();
        }
        double d = (double) ((i - (this.mHGap * (this.mNumLanes - 1))) / this.mNumLanes);
        Double.isNaN(d);
        this.mColLength = (int) (d + 0.5d);
        int i2 = i - (this.mColLength * this.mNumLanes);
        if (this.mNumLanes <= 1) {
            this.mLastGap = 0;
            this.mEachGap = 0;
        } else if (this.mNumLanes == 2) {
            this.mEachGap = i2;
            this.mLastGap = i2;
        } else {
            int i3 = layoutManagerHelper.getOrientation() == 1 ? this.mHGap : this.mVGap;
            this.mLastGap = i3;
            this.mEachGap = i3;
        }
        if ((this.mLayoutManager == null || this.mLayoutManager.get() == null || this.mLayoutManager.get() != layoutManagerHelper) && (layoutManagerHelper instanceof VirtualLayoutManager)) {
            this.mLayoutManager = new WeakReference<>((VirtualLayoutManager) layoutManagerHelper);
        }
    }

    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        super.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
        this.mLayoutWithAnchor = false;
        if (i <= getRange().getUpper().intValue() && i2 >= getRange().getLower().intValue() && !state.isPreLayout() && layoutManagerHelper.getChildCount() > 0) {
            ViewCompat.postOnAnimation(layoutManagerHelper.getChildAt(0), this.checkForGapsRunnable);
        }
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        int offset;
        int extra;
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper2;
        int i;
        int i2;
        int i3;
        int i4;
        Span span;
        boolean z;
        int i5;
        int i6;
        int i7;
        View view;
        boolean z2;
        int i8;
        OrientationHelperEx orientationHelperEx;
        int i9;
        Span span2;
        int i10;
        int size;
        int size2;
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper3 = layoutStateWrapper;
        LayoutChunkResult layoutChunkResult2 = layoutChunkResult;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            ensureLanes();
            boolean z3 = layoutManagerHelper.getOrientation() == 1;
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            OrientationHelperEx secondaryOrientationHelper = layoutManagerHelper.getSecondaryOrientationHelper();
            boolean isEnableMarginOverLap = layoutManagerHelper.isEnableMarginOverLap();
            this.mRemainingSpans.set(0, this.mNumLanes, true);
            if (layoutStateWrapper.getLayoutDirection() == 1) {
                offset = layoutStateWrapper.getOffset() + layoutStateWrapper.getAvailable();
                extra = layoutStateWrapper.getExtra() + offset + mainOrientationHelper.getEndPadding();
            } else {
                offset = layoutStateWrapper.getOffset() - layoutStateWrapper.getAvailable();
                extra = (offset - layoutStateWrapper.getExtra()) - mainOrientationHelper.getStartAfterPadding();
            }
            int i11 = offset;
            int i12 = extra;
            updateAllRemainingSpans(layoutStateWrapper.getLayoutDirection(), i12, mainOrientationHelper);
            int offset2 = layoutStateWrapper.getOffset();
            this.prelayoutViewList.clear();
            while (layoutStateWrapper3.hasMore(state2) && !this.mRemainingSpans.isEmpty() && !isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
                int currentPosition = layoutStateWrapper.getCurrentPosition();
                View next = layoutStateWrapper3.next(recycler2);
                if (next == null) {
                    break;
                }
                VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) next.getLayoutParams();
                int viewLayoutPosition = layoutParams.getViewLayoutPosition();
                int i13 = i12;
                int span3 = this.mLazySpanLookup.getSpan(viewLayoutPosition);
                if (span3 == Integer.MIN_VALUE) {
                    span = getNextSpan(offset2, layoutStateWrapper3, layoutManagerHelper2);
                    this.mLazySpanLookup.setSpan(viewLayoutPosition, span);
                } else {
                    span = this.mSpans[span3];
                }
                Span span4 = span;
                boolean z4 = viewLayoutPosition - getRange().getLower().intValue() < this.mNumLanes;
                boolean z5 = getRange().getUpper().intValue() - viewLayoutPosition < this.mNumLanes;
                if (layoutStateWrapper.isPreLayout()) {
                    this.prelayoutViewList.add(next);
                }
                layoutManagerHelper2.addChildView(layoutStateWrapper3, next);
                if (z3) {
                    int childMeasureSpec = layoutManagerHelper2.getChildMeasureSpec(this.mColLength, layoutParams.width, false);
                    int totalSpace = mainOrientationHelper.getTotalSpace();
                    if (Float.isNaN(layoutParams.mAspectRatio)) {
                        size2 = layoutParams.height;
                    } else {
                        size2 = (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec)) / layoutParams.mAspectRatio) + 0.5f);
                    }
                    layoutManagerHelper2.measureChildWithMargins(next, childMeasureSpec, layoutManagerHelper2.getChildMeasureSpec(totalSpace, size2, true));
                    z = true;
                } else {
                    int childMeasureSpec2 = layoutManagerHelper2.getChildMeasureSpec(this.mColLength, layoutParams.height, false);
                    int totalSpace2 = mainOrientationHelper.getTotalSpace();
                    if (Float.isNaN(layoutParams.mAspectRatio)) {
                        size = layoutParams.width;
                    } else {
                        size = (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec2)) * layoutParams.mAspectRatio) + 0.5f);
                    }
                    z = true;
                    layoutManagerHelper2.measureChildWithMargins(next, layoutManagerHelper2.getChildMeasureSpec(totalSpace2, size, true), childMeasureSpec2);
                }
                if (layoutStateWrapper.getLayoutDirection() == z) {
                    i6 = span4.getEndLine(offset2, mainOrientationHelper);
                    if (z4) {
                        i6 += computeStartSpace(layoutManagerHelper2, z3, z, isEnableMarginOverLap);
                    } else if (!this.mLayoutWithAnchor) {
                        i6 += z3 ? this.mVGap : this.mHGap;
                    } else if (Math.abs(currentPosition - this.anchorPosition) >= this.mNumLanes) {
                        i6 += z3 ? this.mVGap : this.mHGap;
                    }
                    i5 = mainOrientationHelper.getDecoratedMeasurement(next) + i6;
                } else {
                    if (z5) {
                        i10 = span4.getStartLine(offset2, mainOrientationHelper) - ((z3 ? this.mMarginBottom : this.mMarginRight) + this.mPaddingRight);
                    } else {
                        i10 = span4.getStartLine(offset2, mainOrientationHelper) - (z3 ? this.mVGap : this.mHGap);
                    }
                    i5 = i10;
                    i6 = i10 - mainOrientationHelper.getDecoratedMeasurement(next);
                }
                if (layoutStateWrapper.getLayoutDirection() == 1) {
                    span4.appendToSpan(next, mainOrientationHelper);
                } else {
                    span4.prependToSpan(next, mainOrientationHelper);
                }
                int startAfterPadding = (span4.mIndex == this.mNumLanes - 1 ? ((span4.mIndex * (this.mColLength + this.mEachGap)) - this.mEachGap) + this.mLastGap : span4.mIndex * (this.mColLength + this.mEachGap)) + secondaryOrientationHelper.getStartAfterPadding();
                if (z3) {
                    i7 = startAfterPadding + this.mMarginLeft + this.mPaddingLeft;
                } else {
                    i7 = startAfterPadding + this.mMarginTop + this.mPaddingTop;
                }
                int i14 = i7;
                int decoratedMeasurementInOther = i14 + mainOrientationHelper.getDecoratedMeasurementInOther(next);
                if (z3) {
                    view = next;
                    i8 = offset2;
                    z2 = isEnableMarginOverLap;
                    layoutChildWithMargin(next, i14, i6, decoratedMeasurementInOther, i5, layoutManagerHelper);
                    i9 = i13;
                    span2 = span4;
                    orientationHelperEx = mainOrientationHelper;
                } else {
                    view = next;
                    i8 = offset2;
                    z2 = isEnableMarginOverLap;
                    span2 = span4;
                    int i15 = i5;
                    i9 = i13;
                    orientationHelperEx = mainOrientationHelper;
                    layoutChildWithMargin(view, i6, i14, i15, decoratedMeasurementInOther, layoutManagerHelper);
                }
                updateRemainingSpans(span2, layoutStateWrapper.getLayoutDirection(), i9, orientationHelperEx);
                recycle(recycler, layoutStateWrapper, span2, i11, layoutManagerHelper);
                handleStateOnResult(layoutChunkResult2, view);
                i12 = i9;
                mainOrientationHelper = orientationHelperEx;
                offset2 = i8;
                isEnableMarginOverLap = z2;
                recycler2 = recycler;
                state2 = state;
                layoutStateWrapper3 = layoutStateWrapper;
            }
            OrientationHelperEx orientationHelperEx2 = mainOrientationHelper;
            if (isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
                if (layoutStateWrapper.getLayoutDirection() == -1) {
                    for (Span span5 : this.mSpans) {
                        if (span5.mCachedStart != Integer.MIN_VALUE) {
                            span5.mLastEdgeStart = span5.mCachedStart;
                        }
                    }
                } else {
                    for (Span span6 : this.mSpans) {
                        if (span6.mCachedEnd != Integer.MIN_VALUE) {
                            span6.mLastEdgeEnd = span6.mCachedEnd;
                        }
                    }
                }
            }
            if (layoutStateWrapper.getLayoutDirection() == -1) {
                if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
                    layoutStateWrapper2 = layoutStateWrapper;
                    if (layoutStateWrapper2.hasMore(state)) {
                        layoutChunkResult2.mConsumed = layoutStateWrapper.getOffset() - getMaxStart(orientationHelperEx2.getStartAfterPadding(), orientationHelperEx2);
                    }
                } else {
                    layoutStateWrapper2 = layoutStateWrapper;
                }
                int offset3 = layoutStateWrapper.getOffset() - getMinStart(orientationHelperEx2.getEndAfterPadding(), orientationHelperEx2);
                if (z3) {
                    i3 = this.mMarginTop;
                    i4 = this.mPaddingTop;
                } else {
                    i3 = this.mMarginLeft;
                    i4 = this.mPaddingLeft;
                }
                layoutChunkResult2.mConsumed = offset3 + i3 + i4;
            } else {
                RecyclerView.State state3 = state;
                layoutStateWrapper2 = layoutStateWrapper;
                if (isOutOfRange(layoutStateWrapper.getCurrentPosition()) || !layoutStateWrapper2.hasMore(state3)) {
                    int maxEnd = getMaxEnd(orientationHelperEx2.getEndAfterPadding(), orientationHelperEx2) - layoutStateWrapper.getOffset();
                    if (z3) {
                        i = this.mMarginBottom;
                        i2 = this.mPaddingBottom;
                    } else {
                        i = this.mMarginRight;
                        i2 = this.mPaddingRight;
                    }
                    layoutChunkResult2.mConsumed = maxEnd + i + i2;
                } else {
                    layoutChunkResult2.mConsumed = getMinEnd(orientationHelperEx2.getEndAfterPadding(), orientationHelperEx2) - layoutStateWrapper.getOffset();
                }
            }
            recycleForPreLayout(recycler, layoutStateWrapper2, layoutManagerHelper2);
        }
    }

    private void recycleForPreLayout(RecyclerView.Recycler recycler, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutManagerHelper layoutManagerHelper) {
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        for (int size = this.prelayoutViewList.size() - 1; size >= 0; size--) {
            View view = this.prelayoutViewList.get(size);
            if (view != null && mainOrientationHelper.getDecoratedStart(view) > mainOrientationHelper.getEndAfterPadding()) {
                Span findSpan = findSpan(((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), view, false);
                if (findSpan != null) {
                    findSpan.popEnd(mainOrientationHelper);
                }
                layoutManagerHelper.removeChildView(view);
                recycler.recycleView(view);
            } else if (view != null) {
                Span findSpan2 = findSpan(((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), view, false);
                if (findSpan2 != null) {
                    findSpan2.popEnd(mainOrientationHelper);
                }
                layoutManagerHelper.removeChildView(view);
                recycler.recycleView(view);
                return;
            }
        }
    }

    public void onScrollStateChanged(int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        if (i2 <= getRange().getUpper().intValue() && i3 >= getRange().getLower().intValue() && i == 0) {
            checkForGaps();
        }
    }

    public int computeAlignOffset(int i, boolean z, boolean z2, LayoutManagerHelper layoutManagerHelper) {
        boolean z3 = layoutManagerHelper.getOrientation() == 1;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        View findViewByPosition = layoutManagerHelper.findViewByPosition(getRange().getLower().intValue() + i);
        if (findViewByPosition == null) {
            return 0;
        }
        ensureLanes();
        if (z3) {
            if (z) {
                if (i == getItemCount() - 1) {
                    return this.mMarginBottom + this.mPaddingBottom + (getMaxEnd(mainOrientationHelper.getDecoratedEnd(findViewByPosition), mainOrientationHelper) - mainOrientationHelper.getDecoratedEnd(findViewByPosition));
                }
                if (!z2) {
                    return getMinEnd(mainOrientationHelper.getDecoratedStart(findViewByPosition), mainOrientationHelper) - mainOrientationHelper.getDecoratedEnd(findViewByPosition);
                }
            } else if (i == 0) {
                return ((-this.mMarginTop) - this.mPaddingTop) - (mainOrientationHelper.getDecoratedStart(findViewByPosition) - getMinStart(mainOrientationHelper.getDecoratedStart(findViewByPosition), mainOrientationHelper));
            } else {
                if (!z2) {
                    return getMaxStart(mainOrientationHelper.getDecoratedEnd(findViewByPosition), mainOrientationHelper) - mainOrientationHelper.getDecoratedStart(findViewByPosition);
                }
            }
        }
        return 0;
    }

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
    }

    public void forceClearSpanLookup() {
        this.mLazySpanLookup.clear();
    }

    public void spanUpdate(int i) {
        while (i < this.mLazySpanLookup.mData.length) {
            this.mLazySpanLookup.setSpan(i, new Span(Integer.MIN_VALUE));
            i++;
        }
        for (Span clear : this.mSpans) {
            clear.clear();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkForGaps() {
        /*
            r11 = this;
            java.lang.ref.WeakReference<com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager> r0 = r11.mLayoutManager
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.ref.WeakReference<com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager> r0 = r11.mLayoutManager
            java.lang.Object r0 = r0.get()
            com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager r0 = (com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager) r0
            if (r0 == 0) goto L_0x0110
            int r1 = r0.getChildCount()
            if (r1 != 0) goto L_0x0017
            goto L_0x0110
        L_0x0017:
            com.taobao.android.dxcontainer.vlayout.Range r1 = r11.getRange()
            boolean r2 = r0.getReverseLayout()
            r3 = 1
            if (r2 == 0) goto L_0x0034
            r0.findLastVisibleItemPosition()
            r0.findFirstVisibleItemPosition()
            java.lang.Comparable r1 = r1.getUpper()
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            int r1 = r1 - r3
            goto L_0x0044
        L_0x0034:
            r0.findFirstVisibleItemPosition()
            r0.findLastCompletelyVisibleItemPosition()
            java.lang.Comparable r1 = r1.getLower()
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
        L_0x0044:
            com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r2 = r0.getMainOrientationHelper()
            int r4 = r0.getChildCount()
            boolean r5 = r0.getReverseLayout()
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            r7 = 0
            if (r5 == 0) goto L_0x0090
            int r4 = r4 - r3
            r5 = r4
        L_0x0057:
            if (r5 < 0) goto L_0x00ed
            android.view.View r8 = r0.getChildAt(r5)
            int r9 = r0.getPosition(r8)
            if (r9 != r1) goto L_0x008d
            if (r5 != r4) goto L_0x006b
            int r1 = r2.getDecoratedEnd(r8)
            goto L_0x00f1
        L_0x006b:
            int r5 = r5 + r3
            android.view.View r1 = r0.getChildAt(r5)
            int r4 = r0.getPosition(r1)
            int r5 = r9 + -1
            if (r4 != r5) goto L_0x0087
            int r2 = r2.getDecoratedStart(r1)
            int r1 = r0.obtainExtraMargin(r1, r7)
            int r2 = r2 - r1
            int r1 = r0.obtainExtraMargin(r8, r3)
            int r2 = r2 + r1
            goto L_0x008b
        L_0x0087:
            int r2 = r2.getDecoratedEnd(r8)
        L_0x008b:
            r1 = r2
            goto L_0x00f1
        L_0x008d:
            int r5 = r5 + -1
            goto L_0x0057
        L_0x0090:
            r5 = 0
        L_0x0091:
            if (r5 >= r4) goto L_0x00ed
            android.view.View r8 = r0.getChildAt(r5)
            int r9 = r0.getPosition(r8)
            if (r9 != r1) goto L_0x00ea
            if (r5 != 0) goto L_0x00a4
            int r1 = r2.getDecoratedStart(r8)
            goto L_0x00f1
        L_0x00a4:
            int r5 = r5 - r3
            android.view.View r4 = r0.getChildAt(r5)
            int r5 = r2.getDecoratedEnd(r4)
            int r10 = r0.obtainExtraMargin(r4, r3, r7)
            int r5 = r5 + r10
            int r10 = r0.obtainExtraMargin(r8, r7, r7)
            int r5 = r5 - r10
            int r2 = r2.getDecoratedStart(r8)
            if (r5 != r2) goto L_0x00c0
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x00e8
        L_0x00c0:
            int r2 = r0.getPosition(r4)
            int r1 = r1 - r3
            if (r2 == r1) goto L_0x00e1
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r1 = r0.findLayoutHelperByPosition(r1)
            if (r1 == 0) goto L_0x00e8
            boolean r2 = r1 instanceof com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper
            if (r2 == 0) goto L_0x00e8
            android.view.View r2 = r1.getFixedView()
            if (r2 == 0) goto L_0x00e8
            android.view.View r1 = r1.getFixedView()
            int r1 = r1.getMeasuredHeight()
            int r5 = r5 + r1
            goto L_0x00e8
        L_0x00e1:
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r1 = r0.findLayoutHelperByPosition(r2)
            r1.getRange()
        L_0x00e8:
            r1 = r5
            goto L_0x00f1
        L_0x00ea:
            int r5 = r5 + 1
            goto L_0x0091
        L_0x00ed:
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x00f1:
            if (r9 != r6) goto L_0x00f4
            return
        L_0x00f4:
            android.view.View r2 = r11.hasGapsToFix(r0, r9, r1)
            if (r2 == 0) goto L_0x010f
            com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper$Span[] r2 = r11.mSpans
            int r2 = r2.length
        L_0x00fd:
            if (r7 >= r2) goto L_0x0109
            com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper$Span[] r3 = r11.mSpans
            r3 = r3[r7]
            r3.setLine(r1)
            int r7 = r7 + 1
            goto L_0x00fd
        L_0x0109:
            r0.requestSimpleAnimationsInNextLayout()
            r0.requestLayout()
        L_0x010f:
            return
        L_0x0110:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper.checkForGaps():void");
    }

    private View hasGapsToFix(VirtualLayoutManager virtualLayoutManager, int i, int i2) {
        if (virtualLayoutManager.findViewByPosition(i) == null) {
            return null;
        }
        View[] viewArr = new View[this.mSpans.length];
        int length = this.mSpans.length;
        int i3 = 0;
        while (true) {
            int i4 = 1;
            if (i3 < length) {
                Span span = this.mSpans[i3];
                if (span.mViews.size() != 0) {
                    if (checkSpanForGap(span, virtualLayoutManager, i2)) {
                        return (View) (virtualLayoutManager.getReverseLayout() ? span.mViews.get(span.mViews.size() - 1) : span.mViews.get(0));
                    }
                    viewArr[i3] = (View) (virtualLayoutManager.getReverseLayout() ? span.mViews.get(span.mViews.size() - 1) : span.mViews.get(0));
                }
                i3++;
            } else {
                if (viewArr[0] != null) {
                    int top = viewArr[0].getTop();
                    while (i4 < viewArr.length) {
                        View view = viewArr[i4];
                        if (view == null) {
                            this.mLazySpanLookup.clear();
                            for (Span span2 : this.mSpans) {
                                if (span2 != null) {
                                    span2.clear();
                                }
                            }
                            return null;
                        } else if (view.getTop() != top) {
                            return viewArr[i4];
                        } else {
                            i4++;
                        }
                    }
                }
                return null;
            }
        }
    }

    private boolean checkSpanForGap(Span span, VirtualLayoutManager virtualLayoutManager, int i) {
        OrientationHelperEx mainOrientationHelper = virtualLayoutManager.getMainOrientationHelper();
        if (virtualLayoutManager.getReverseLayout()) {
            if (span.getEndLine(mainOrientationHelper) < i) {
                return true;
            }
            return false;
        } else if (span.getStartLine(mainOrientationHelper) > i) {
            return true;
        } else {
            return false;
        }
    }

    private void recycle(RecyclerView.Recycler recycler, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, Span span, int i, LayoutManagerHelper layoutManagerHelper) {
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        if (layoutStateWrapper.getLayoutDirection() == -1) {
            recycleFromEnd(recycler, Math.max(i, getMaxStart(span.getStartLine(mainOrientationHelper), mainOrientationHelper)) + (mainOrientationHelper.getEnd() - mainOrientationHelper.getStartAfterPadding()), layoutManagerHelper);
        } else {
            recycleFromStart(recycler, Math.min(i, getMinEnd(span.getEndLine(mainOrientationHelper), mainOrientationHelper)) - (mainOrientationHelper.getEnd() - mainOrientationHelper.getStartAfterPadding()), layoutManagerHelper);
        }
    }

    private void recycleFromStart(RecyclerView.Recycler recycler, int i, LayoutManagerHelper layoutManagerHelper) {
        View childAt;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        boolean z = true;
        while (layoutManagerHelper.getChildCount() > 0 && z && (childAt = layoutManagerHelper.getChildAt(0)) != null && mainOrientationHelper.getDecoratedEnd(childAt) < i) {
            Span findSpan = findSpan(((RecyclerView.LayoutParams) childAt.getLayoutParams()).getViewPosition(), childAt, true);
            if (findSpan != null) {
                findSpan.popStart(mainOrientationHelper);
                layoutManagerHelper.removeChildView(childAt);
                recycler.recycleView(childAt);
            } else {
                z = false;
            }
        }
    }

    private void recycleFromEnd(RecyclerView.Recycler recycler, int i, LayoutManagerHelper layoutManagerHelper) {
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        int childCount = layoutManagerHelper.getChildCount() - 1;
        while (childCount >= 0) {
            View childAt = layoutManagerHelper.getChildAt(childCount);
            if (childAt != null && mainOrientationHelper.getDecoratedStart(childAt) > i) {
                Span findSpan = findSpan(((RecyclerView.LayoutParams) childAt.getLayoutParams()).getViewPosition(), childAt, false);
                if (findSpan != null) {
                    findSpan.popEnd(mainOrientationHelper);
                    layoutManagerHelper.removeChildView(childAt);
                    recycler.recycleView(childAt);
                }
                childCount--;
            } else {
                return;
            }
        }
    }

    private Span findSpan(int i, View view, boolean z) {
        int span = this.mLazySpanLookup.getSpan(i);
        if (span >= 0 && span < this.mSpans.length) {
            Span span2 = this.mSpans[span];
            if (z && span2.findStart(view)) {
                return span2;
            }
            if (!z && span2.findEnd(view)) {
                return span2;
            }
        }
        for (int i2 = 0; i2 < this.mSpans.length; i2++) {
            if (i2 != span) {
                Span span3 = this.mSpans[i2];
                if (z && span3.findStart(view)) {
                    return span3;
                }
                if (!z && span3.findEnd(view)) {
                    return span3;
                }
            }
        }
        return null;
    }

    public boolean isRecyclable(int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper, boolean z) {
        View findViewByPosition;
        boolean isRecyclable = super.isRecyclable(i, i2, i3, layoutManagerHelper, z);
        if (isRecyclable && (findViewByPosition = layoutManagerHelper.findViewByPosition(i)) != null) {
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            int viewLayoutPosition = ((RecyclerView.LayoutParams) findViewByPosition.getLayoutParams()).getViewLayoutPosition();
            if (layoutManagerHelper.getReverseLayout()) {
                if (z) {
                    Span findSpan = findSpan(viewLayoutPosition, findViewByPosition, true);
                    if (findSpan != null) {
                        findSpan.popEnd(mainOrientationHelper);
                    }
                } else {
                    Span findSpan2 = findSpan(viewLayoutPosition, findViewByPosition, false);
                    if (findSpan2 != null) {
                        findSpan2.popStart(mainOrientationHelper);
                    }
                }
            } else if (z) {
                Span findSpan3 = findSpan(viewLayoutPosition, findViewByPosition, true);
                if (findSpan3 != null) {
                    findSpan3.popStart(mainOrientationHelper);
                }
            } else {
                Span findSpan4 = findSpan(viewLayoutPosition, findViewByPosition, false);
                if (findSpan4 != null) {
                    findSpan4.popEnd(mainOrientationHelper);
                }
            }
        }
        return isRecyclable;
    }

    private void updateAllRemainingSpans(int i, int i2, OrientationHelperEx orientationHelperEx) {
        for (int i3 = 0; i3 < this.mNumLanes; i3++) {
            if (!this.mSpans[i3].mViews.isEmpty()) {
                updateRemainingSpans(this.mSpans[i3], i, i2, orientationHelperEx);
            }
        }
    }

    private void updateRemainingSpans(Span span, int i, int i2, OrientationHelperEx orientationHelperEx) {
        int deletedSize = span.getDeletedSize();
        if (i == -1) {
            if (span.getStartLine(orientationHelperEx) + deletedSize < i2) {
                this.mRemainingSpans.set(span.mIndex, false);
            }
        } else if (span.getEndLine(orientationHelperEx) - deletedSize > i2) {
            this.mRemainingSpans.set(span.mIndex, false);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        if (((r8.getLayoutDirection() == -1) == r9.getReverseLayout()) == r9.isDoLayoutRTL()) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001a, code lost:
        if ((r8.getLayoutDirection() == -1) != r9.getReverseLayout()) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001e, code lost:
        r9 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper.Span getNextSpan(int r7, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.LayoutStateWrapper r8, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r9) {
        /*
            r6 = this;
            com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r0 = r9.getMainOrientationHelper()
            int r1 = r9.getOrientation()
            r2 = -1
            r3 = 0
            r4 = 1
            if (r1 != 0) goto L_0x0020
            int r1 = r8.getLayoutDirection()
            if (r1 != r2) goto L_0x0015
            r1 = 1
            goto L_0x0016
        L_0x0015:
            r1 = 0
        L_0x0016:
            boolean r9 = r9.getReverseLayout()
            if (r1 == r9) goto L_0x001e
        L_0x001c:
            r9 = 1
            goto L_0x0039
        L_0x001e:
            r9 = 0
            goto L_0x0039
        L_0x0020:
            int r1 = r8.getLayoutDirection()
            if (r1 != r2) goto L_0x0028
            r1 = 1
            goto L_0x0029
        L_0x0028:
            r1 = 0
        L_0x0029:
            boolean r5 = r9.getReverseLayout()
            if (r1 != r5) goto L_0x0031
            r1 = 1
            goto L_0x0032
        L_0x0031:
            r1 = 0
        L_0x0032:
            boolean r9 = r9.isDoLayoutRTL()
            if (r1 != r9) goto L_0x001e
            goto L_0x001c
        L_0x0039:
            if (r9 == 0) goto L_0x0041
            int r9 = r6.mNumLanes
            int r3 = r9 + -1
            r9 = -1
            goto L_0x0044
        L_0x0041:
            int r2 = r6.mNumLanes
            r9 = 1
        L_0x0044:
            int r8 = r8.getLayoutDirection()
            r1 = 0
            if (r8 != r4) goto L_0x005f
            r8 = 2147483647(0x7fffffff, float:NaN)
        L_0x004e:
            if (r3 == r2) goto L_0x005e
            com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper$Span[] r4 = r6.mSpans
            r4 = r4[r3]
            int r5 = r4.getEndLine(r7, r0)
            if (r5 >= r8) goto L_0x005c
            r1 = r4
            r8 = r5
        L_0x005c:
            int r3 = r3 + r9
            goto L_0x004e
        L_0x005e:
            return r1
        L_0x005f:
            r8 = -2147483648(0xffffffff80000000, float:-0.0)
        L_0x0061:
            if (r3 == r2) goto L_0x0071
            com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper$Span[] r4 = r6.mSpans
            r4 = r4[r3]
            int r5 = r4.getStartLine(r7, r0)
            if (r5 <= r8) goto L_0x006f
            r1 = r4
            r8 = r5
        L_0x006f:
            int r3 = r3 + r9
            goto L_0x0061
        L_0x0071:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper.getNextSpan(int, com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager$LayoutStateWrapper, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper$Span");
    }

    private int getMaxStart(int i, OrientationHelperEx orientationHelperEx) {
        int startLine = this.mSpans[0].getStartLine(i, orientationHelperEx);
        for (int i2 = 1; i2 < this.mNumLanes; i2++) {
            int startLine2 = this.mSpans[i2].getStartLine(i, orientationHelperEx);
            if (startLine2 > startLine) {
                startLine = startLine2;
            }
        }
        return startLine;
    }

    private int getMinStart(int i, OrientationHelperEx orientationHelperEx) {
        int startLine = this.mSpans[0].getStartLine(i, orientationHelperEx);
        for (int i2 = 1; i2 < this.mNumLanes; i2++) {
            int startLine2 = this.mSpans[i2].getStartLine(i, orientationHelperEx);
            if (startLine2 < startLine) {
                startLine = startLine2;
            }
        }
        return startLine;
    }

    private int getMaxEnd(int i, OrientationHelperEx orientationHelperEx) {
        int endLine = this.mSpans[0].getEndLine(i, orientationHelperEx);
        for (int i2 = 1; i2 < this.mNumLanes; i2++) {
            int endLine2 = this.mSpans[i2].getEndLine(i, orientationHelperEx);
            if (endLine2 > endLine) {
                endLine = endLine2;
            }
        }
        return endLine;
    }

    private int getMinEnd(int i, OrientationHelperEx orientationHelperEx) {
        int endLine = this.mSpans[0].getEndLine(i, orientationHelperEx);
        for (int i2 = 1; i2 < this.mNumLanes; i2++) {
            int endLine2 = this.mSpans[i2].getEndLine(i, orientationHelperEx);
            if (endLine2 < endLine) {
                endLine = endLine2;
            }
        }
        return endLine;
    }

    public void onRefreshLayout(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        super.onRefreshLayout(state, anchorInfoWrapper, layoutManagerHelper);
        ensureLanes();
        if (isOutOfRange(anchorInfoWrapper.position)) {
            for (Span clear : this.mSpans) {
                clear.clear();
            }
        }
    }

    public void checkAnchorInfo(RecyclerView.State state, VirtualLayoutManager.AnchorInfoWrapper anchorInfoWrapper, LayoutManagerHelper layoutManagerHelper) {
        int i;
        super.checkAnchorInfo(state, anchorInfoWrapper, layoutManagerHelper);
        ensureLanes();
        Range<Integer> range = getRange();
        boolean z = true;
        if (anchorInfoWrapper.layoutFromEnd) {
            if (anchorInfoWrapper.position < (range.getLower().intValue() + this.mNumLanes) - 1) {
                anchorInfoWrapper.position = Math.min((range.getLower().intValue() + this.mNumLanes) - 1, range.getUpper().intValue());
            }
        } else if (anchorInfoWrapper.position > range.getUpper().intValue() - (this.mNumLanes - 1)) {
            anchorInfoWrapper.position = Math.max(range.getLower().intValue(), range.getUpper().intValue() - (this.mNumLanes - 1));
        }
        View findViewByPosition = layoutManagerHelper.findViewByPosition(anchorInfoWrapper.position);
        int i2 = 0;
        int i3 = layoutManagerHelper.getOrientation() == 1 ? this.mVGap : this.mHGap;
        OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
        if (findViewByPosition == null) {
            int length = this.mSpans.length;
            while (i2 < length) {
                Span span = this.mSpans[i2];
                span.clear();
                span.setLine(anchorInfoWrapper.coordinate);
                i2++;
            }
            return;
        }
        int i4 = Integer.MIN_VALUE;
        int i5 = anchorInfoWrapper.layoutFromEnd ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int i6 = i5;
        for (Span span2 : this.mSpans) {
            if (!span2.mViews.isEmpty()) {
                if (anchorInfoWrapper.layoutFromEnd) {
                    i6 = Math.max(i6, layoutManagerHelper.getPosition((View) span2.mViews.get(span2.mViews.size() - 1)));
                } else {
                    i6 = Math.min(i6, layoutManagerHelper.getPosition((View) span2.mViews.get(0)));
                }
            }
        }
        if (!isOutOfRange(i6)) {
            if (i6 != range.getLower().intValue()) {
                z = false;
            }
            View findViewByPosition2 = layoutManagerHelper.findViewByPosition(i6);
            if (findViewByPosition2 != null) {
                if (anchorInfoWrapper.layoutFromEnd) {
                    anchorInfoWrapper.position = i6;
                    int decoratedEnd = mainOrientationHelper.getDecoratedEnd(findViewByPosition);
                    if (decoratedEnd < anchorInfoWrapper.coordinate) {
                        int i7 = anchorInfoWrapper.coordinate - decoratedEnd;
                        if (z) {
                            i3 = 0;
                        }
                        i = i7 + i3;
                        anchorInfoWrapper.coordinate = mainOrientationHelper.getDecoratedEnd(findViewByPosition2) + i;
                    } else {
                        i = z ? 0 : i3;
                        anchorInfoWrapper.coordinate = mainOrientationHelper.getDecoratedEnd(findViewByPosition2) + i;
                    }
                } else {
                    anchorInfoWrapper.position = i6;
                    int decoratedStart = mainOrientationHelper.getDecoratedStart(findViewByPosition);
                    if (decoratedStart > anchorInfoWrapper.coordinate) {
                        int i8 = anchorInfoWrapper.coordinate - decoratedStart;
                        if (z) {
                            i3 = 0;
                        }
                        i = i8 - i3;
                        anchorInfoWrapper.coordinate = mainOrientationHelper.getDecoratedStart(findViewByPosition2) + i;
                    } else {
                        if (z) {
                            i3 = 0;
                        }
                        int i9 = -i3;
                        anchorInfoWrapper.coordinate = mainOrientationHelper.getDecoratedStart(findViewByPosition2) + i9;
                        i4 = i9;
                    }
                }
                i4 = i;
            }
        } else {
            this.anchorPosition = anchorInfoWrapper.position;
            this.mLayoutWithAnchor = true;
        }
        int length2 = this.mSpans.length;
        while (i2 < length2) {
            this.mSpans[i2].cacheReferenceLineAndClear(layoutManagerHelper.getReverseLayout() ^ anchorInfoWrapper.layoutFromEnd, i4, mainOrientationHelper);
            i2++;
        }
    }

    public void onSaveState(Bundle bundle) {
        super.onSaveState(bundle);
        bundle.putIntArray(LOOKUP_BUNDLE_KEY, this.mLazySpanLookup.mData);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mLazySpanLookup.mData = bundle.getIntArray(LOOKUP_BUNDLE_KEY);
    }

    public void onOffsetChildrenVertical(int i, LayoutManagerHelper layoutManagerHelper) {
        super.onOffsetChildrenVertical(i, layoutManagerHelper);
        if (layoutManagerHelper.getOrientation() == 1) {
            for (Span onOffset : this.mSpans) {
                onOffset.onOffset(i);
            }
        }
    }

    public void onOffsetChildrenHorizontal(int i, LayoutManagerHelper layoutManagerHelper) {
        super.onOffsetChildrenHorizontal(i, layoutManagerHelper);
        if (layoutManagerHelper.getOrientation() == 0) {
            for (Span onOffset : this.mSpans) {
                onOffset.onOffset(i);
            }
        }
    }

    static class Span {
        static final int INVALID_OFFSET = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        int mLastEdgeEnd;
        int mLastEdgeStart;
        /* access modifiers changed from: private */
        public ArrayList<View> mViews;

        private Span(int i) {
            this.mViews = new ArrayList<>();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mLastEdgeStart = Integer.MIN_VALUE;
            this.mLastEdgeEnd = Integer.MIN_VALUE;
            this.mIndex = i;
        }

        /* access modifiers changed from: package-private */
        public void calculateCachedStart(@NonNull OrientationHelperEx orientationHelperEx) {
            if (this.mViews.size() == 0) {
                this.mCachedStart = Integer.MIN_VALUE;
            } else {
                this.mCachedStart = orientationHelperEx.getDecoratedStart(this.mViews.get(0));
            }
        }

        /* access modifiers changed from: package-private */
        public int getStartLine(OrientationHelperEx orientationHelperEx) {
            return getStartLine(Integer.MIN_VALUE, orientationHelperEx);
        }

        /* access modifiers changed from: package-private */
        public int getStartLine(int i, OrientationHelperEx orientationHelperEx) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                return this.mCachedStart;
            }
            if (i != Integer.MIN_VALUE && this.mViews.size() == 0) {
                return this.mLastEdgeEnd != Integer.MIN_VALUE ? this.mLastEdgeEnd : i;
            }
            calculateCachedStart(orientationHelperEx);
            return this.mCachedStart;
        }

        /* access modifiers changed from: package-private */
        public void calculateCachedEnd(OrientationHelperEx orientationHelperEx) {
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            } else {
                this.mCachedEnd = orientationHelperEx.getDecoratedEnd(this.mViews.get(this.mViews.size() - 1));
            }
        }

        /* access modifiers changed from: package-private */
        public int getEndLine(OrientationHelperEx orientationHelperEx) {
            return getEndLine(Integer.MIN_VALUE, orientationHelperEx);
        }

        /* access modifiers changed from: package-private */
        public int getEndLine(int i, OrientationHelperEx orientationHelperEx) {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                return this.mCachedEnd;
            }
            if (i != Integer.MIN_VALUE && this.mViews.size() == 0) {
                return this.mLastEdgeStart != Integer.MIN_VALUE ? this.mLastEdgeStart : i;
            }
            calculateCachedEnd(orientationHelperEx);
            return this.mCachedEnd;
        }

        /* access modifiers changed from: package-private */
        public void prependToSpan(View view, OrientationHelperEx orientationHelperEx) {
            RecyclerView.LayoutParams layoutParams = getLayoutParams(view);
            this.mViews.add(0, view);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += orientationHelperEx.getDecoratedMeasurement(view);
            }
        }

        /* access modifiers changed from: package-private */
        public void appendToSpan(View view, OrientationHelperEx orientationHelperEx) {
            RecyclerView.LayoutParams layoutParams = getLayoutParams(view);
            this.mViews.add(view);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += orientationHelperEx.getDecoratedMeasurement(view);
            }
        }

        /* access modifiers changed from: package-private */
        public void cacheReferenceLineAndClear(boolean z, int i, OrientationHelperEx orientationHelperEx) {
            int i2;
            if (z) {
                i2 = getEndLine(orientationHelperEx);
            } else {
                i2 = getStartLine(orientationHelperEx);
            }
            clear();
            if (i2 != Integer.MIN_VALUE) {
                if ((!z || i2 >= orientationHelperEx.getEndAfterPadding()) && !z) {
                    orientationHelperEx.getStartAfterPadding();
                }
                if (i != Integer.MIN_VALUE) {
                    i2 += i;
                }
                this.mCachedEnd = i2;
                this.mCachedStart = i2;
                this.mLastEdgeEnd = Integer.MIN_VALUE;
                this.mLastEdgeStart = Integer.MIN_VALUE;
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.mViews.clear();
            invalidateCache();
            this.mDeletedSize = 0;
        }

        /* access modifiers changed from: package-private */
        public void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mLastEdgeEnd = Integer.MIN_VALUE;
            this.mLastEdgeStart = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public void setLine(int i) {
            this.mCachedStart = i;
            this.mCachedEnd = i;
            this.mLastEdgeEnd = Integer.MIN_VALUE;
            this.mLastEdgeStart = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public void popEnd(OrientationHelperEx orientationHelperEx) {
            int size = this.mViews.size();
            View remove = this.mViews.remove(size - 1);
            RecyclerView.LayoutParams layoutParams = getLayoutParams(remove);
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= orientationHelperEx.getDecoratedMeasurement(remove);
            }
            if (size == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public boolean findEnd(View view) {
            int size = this.mViews.size();
            if (size <= 0 || this.mViews.get(size - 1) != view) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public void popStart(OrientationHelperEx orientationHelperEx) {
            View remove = this.mViews.remove(0);
            RecyclerView.LayoutParams layoutParams = getLayoutParams(remove);
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= orientationHelperEx.getDecoratedMeasurement(remove);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public boolean findStart(View view) {
            if (this.mViews.size() <= 0 || this.mViews.get(0) != view) {
                return false;
            }
            return true;
        }

        public int getDeletedSize() {
            return this.mDeletedSize;
        }

        /* access modifiers changed from: package-private */
        public RecyclerView.LayoutParams getLayoutParams(View view) {
            return (RecyclerView.LayoutParams) view.getLayoutParams();
        }

        /* access modifiers changed from: package-private */
        public void onOffset(int i) {
            if (this.mLastEdgeStart != Integer.MIN_VALUE) {
                this.mLastEdgeStart += i;
            }
            if (this.mCachedStart != Integer.MIN_VALUE) {
                this.mCachedStart += i;
            }
            if (this.mLastEdgeEnd != Integer.MIN_VALUE) {
                this.mLastEdgeEnd += i;
            }
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                this.mCachedEnd += i;
            }
        }

        /* access modifiers changed from: package-private */
        public int getNormalizedOffset(int i, int i2, int i3, OrientationHelperEx orientationHelperEx) {
            if (this.mViews.size() == 0) {
                return 0;
            }
            if (i < 0) {
                int endLine = getEndLine(0, orientationHelperEx) - i3;
                if (endLine <= 0) {
                    return 0;
                }
                return (-i) > endLine ? -endLine : i;
            }
            int startLine = i2 - getStartLine(0, orientationHelperEx);
            if (startLine <= 0) {
                return 0;
            }
            return startLine < i ? startLine : i;
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty(int i, int i2, OrientationHelperEx orientationHelperEx) {
            int size = this.mViews.size();
            for (int i3 = 0; i3 < size; i3++) {
                View view = this.mViews.get(i3);
                if (orientationHelperEx.getDecoratedStart(view) < i2 && orientationHelperEx.getDecoratedEnd(view) > i) {
                    return false;
                }
            }
            return true;
        }
    }

    static class LazySpanLookup {
        private static final int MIN_SIZE = 10;
        int[] mData;

        LazySpanLookup() {
        }

        /* access modifiers changed from: package-private */
        public int invalidateAfter(int i) {
            if (this.mData == null || i >= this.mData.length) {
                return -1;
            }
            Arrays.fill(this.mData, i, this.mData.length, Integer.MIN_VALUE);
            return this.mData.length;
        }

        /* access modifiers changed from: package-private */
        public int getSpan(int i) {
            if (this.mData == null || i >= this.mData.length || i < 0) {
                return Integer.MIN_VALUE;
            }
            return this.mData[i];
        }

        /* access modifiers changed from: package-private */
        public void setSpan(int i, Span span) {
            ensureSize(i);
            this.mData[i] = span.mIndex;
        }

        /* access modifiers changed from: package-private */
        public int sizeForPosition(int i) {
            int length = this.mData.length;
            while (length <= i) {
                length *= 2;
            }
            return length;
        }

        /* access modifiers changed from: package-private */
        public void ensureSize(int i) {
            if (this.mData == null) {
                this.mData = new int[(Math.max(i, 10) + 1)];
                Arrays.fill(this.mData, Integer.MIN_VALUE);
            } else if (i >= this.mData.length) {
                int[] iArr = this.mData;
                this.mData = new int[sizeForPosition(i)];
                System.arraycopy(iArr, 0, this.mData, 0, iArr.length);
                Arrays.fill(this.mData, iArr.length, this.mData.length, Integer.MIN_VALUE);
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.mData != null) {
                Arrays.fill(this.mData, Integer.MIN_VALUE);
            }
        }

        /* access modifiers changed from: package-private */
        public void offsetForRemoval(int i, int i2) {
            if (this.mData != null && i < this.mData.length) {
                int i3 = i + i2;
                ensureSize(i3);
                System.arraycopy(this.mData, i3, this.mData, i, (this.mData.length - i) - i2);
                Arrays.fill(this.mData, this.mData.length - i2, this.mData.length, Integer.MIN_VALUE);
            }
        }

        /* access modifiers changed from: package-private */
        public void offsetForAddition(int i, int i2) {
            if (this.mData != null && i < this.mData.length) {
                int i3 = i + i2;
                ensureSize(i3);
                System.arraycopy(this.mData, i, this.mData, i3, (this.mData.length - i) - i2);
                Arrays.fill(this.mData, i, i3, Integer.MIN_VALUE);
            }
        }
    }
}
