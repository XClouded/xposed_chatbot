package com.taobao.android.dxcontainer.vlayout.layout;

import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class StickyLayoutHelper extends FixAreaLayoutHelper {
    private static final String TAG = "StickyStartLayoutHelper";
    private boolean isLastStatusSticking;
    private boolean mDoNormalHandle;
    private View mFixView;
    private int mOffset;
    private int mPos;
    private boolean mStickyStart;
    private StickyListener stickyListener;

    public interface StickyListener {
        void onSticky(int i, View view);

        void onUnSticky(int i, View view);
    }

    public boolean requireLayoutView() {
        return false;
    }

    public StickyLayoutHelper() {
        this(true);
    }

    public StickyLayoutHelper(boolean z) {
        this.mPos = -1;
        this.mStickyStart = true;
        this.mOffset = 0;
        this.mFixView = null;
        this.mDoNormalHandle = false;
        this.isLastStatusSticking = false;
        this.mStickyStart = z;
        setItemCount(1);
    }

    public void setStickyStart(boolean z) {
        this.mStickyStart = z;
    }

    public void setOffset(int i) {
        this.mOffset = i;
    }

    public boolean isStickyNow() {
        return !this.mDoNormalHandle && this.mFixView != null;
    }

    public void setItemCount(int i) {
        if (i > 0) {
            super.setItemCount(1);
        } else {
            super.setItemCount(0);
        }
    }

    public void onRangeChange(int i, int i2) {
        this.mPos = i;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper2 = layoutStateWrapper;
        LayoutChunkResult layoutChunkResult2 = layoutChunkResult;
        LayoutManagerHelper layoutManagerHelper2 = layoutManagerHelper;
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            View view = this.mFixView;
            if (view == null) {
                view = layoutStateWrapper2.next(recycler);
            } else {
                layoutStateWrapper.skipCurrentPosition();
            }
            View view2 = view;
            if (view2 == null) {
                layoutChunkResult2.mFinished = true;
                return;
            }
            doMeasure(view2, layoutManagerHelper2);
            boolean z = layoutManagerHelper.getOrientation() == 1;
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            layoutChunkResult2.mConsumed = mainOrientationHelper.getDecoratedMeasurement(view2);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view2.getLayoutParams();
            this.mDoNormalHandle = true;
            int available = (layoutStateWrapper.getAvailable() - layoutChunkResult2.mConsumed) + layoutStateWrapper.getExtra();
            if (layoutManagerHelper.getOrientation() == 1) {
                if (layoutManagerHelper.isDoLayoutRTL()) {
                    i7 = (layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mMarginRight;
                    i6 = i7 - mainOrientationHelper.getDecoratedMeasurementInOther(view2);
                } else {
                    i6 = this.mMarginLeft + layoutManagerHelper.getPaddingLeft();
                    i7 = mainOrientationHelper.getDecoratedMeasurementInOther(view2) + i6;
                }
                if (layoutStateWrapper.getLayoutDirection() == -1) {
                    i9 = layoutStateWrapper.getOffset() - this.mMarginBottom;
                    i8 = layoutStateWrapper.getOffset() - layoutChunkResult2.mConsumed;
                } else if (this.mStickyStart) {
                    i8 = this.mMarginTop + layoutStateWrapper.getOffset();
                    i9 = layoutStateWrapper.getOffset() + layoutChunkResult2.mConsumed;
                } else {
                    i9 = ((mainOrientationHelper.getEndAfterPadding() - this.mMarginBottom) - this.mOffset) - this.mAdjuster.bottom;
                    i8 = i9 - layoutChunkResult2.mConsumed;
                }
                if (layoutManagerHelper.getReverseLayout() || !this.mStickyStart) {
                    if ((available < this.mOffset + this.mAdjuster.bottom && layoutStateWrapper.getItemDirection() == 1) || i9 > this.mMarginBottom + this.mOffset + this.mAdjuster.bottom) {
                        this.mDoNormalHandle = false;
                        this.mFixView = view2;
                        int endAfterPadding = ((mainOrientationHelper.getEndAfterPadding() - this.mMarginBottom) - this.mOffset) - this.mAdjuster.bottom;
                        i2 = i7;
                        i4 = i6;
                        i = endAfterPadding;
                        i3 = endAfterPadding - layoutChunkResult2.mConsumed;
                    }
                } else if ((available < this.mOffset + this.mAdjuster.top && layoutStateWrapper.getItemDirection() == -1) || i8 < this.mMarginTop + this.mOffset + this.mAdjuster.top) {
                    this.mDoNormalHandle = false;
                    this.mFixView = view2;
                    int startAfterPadding = mainOrientationHelper.getStartAfterPadding() + this.mMarginTop + this.mOffset + this.mAdjuster.top;
                    i2 = i7;
                    i4 = i6;
                    i3 = startAfterPadding;
                    i = layoutChunkResult2.mConsumed + startAfterPadding;
                } else if (VirtualLayoutManager.sDebuggable) {
                    Log.i("Sticky", "remainingSpace: " + available + "    offset: " + this.mOffset);
                }
                i2 = i7;
                i4 = i6;
                i = i9;
                i3 = i8;
            } else {
                i3 = layoutManagerHelper.getPaddingTop();
                i = mainOrientationHelper.getDecoratedMeasurementInOther(view2) + i3 + this.mMarginTop;
                if (layoutStateWrapper.getLayoutDirection() == -1) {
                    i2 = layoutStateWrapper.getOffset() - this.mMarginRight;
                    i5 = layoutStateWrapper.getOffset() - layoutChunkResult2.mConsumed;
                } else {
                    i5 = this.mMarginLeft + layoutStateWrapper.getOffset();
                    i2 = layoutStateWrapper.getOffset() + layoutChunkResult2.mConsumed;
                }
                if (layoutManagerHelper.getReverseLayout() || !this.mStickyStart) {
                    if (available < this.mOffset + this.mAdjuster.right) {
                        this.mDoNormalHandle = false;
                        this.mFixView = view2;
                        int endAfterPadding2 = (mainOrientationHelper.getEndAfterPadding() - this.mOffset) - this.mAdjuster.right;
                        i2 = endAfterPadding2;
                        i4 = endAfterPadding2 - layoutChunkResult2.mConsumed;
                    }
                } else if (available < this.mOffset + this.mAdjuster.left) {
                    this.mDoNormalHandle = false;
                    this.mFixView = view2;
                    i4 = mainOrientationHelper.getStartAfterPadding() + this.mOffset + this.mAdjuster.left;
                    i2 = layoutChunkResult2.mConsumed;
                }
                i4 = i5;
            }
            layoutChildWithMargin(view2, i4, i3, i2, i, layoutManagerHelper);
            layoutChunkResult2.mConsumed += z ? getVerticalMargin() : getHorizontalMargin();
            if (state.isPreLayout()) {
                this.mDoNormalHandle = true;
            }
            if (this.mDoNormalHandle) {
                layoutManagerHelper2.addChildView(layoutStateWrapper2, view2);
                handleStateOnResult(layoutChunkResult2, view2);
                this.mFixView = null;
            }
        }
    }

    public void beforeLayout(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutManagerHelper layoutManagerHelper) {
        super.beforeLayout(recycler, state, layoutManagerHelper);
        if (this.mFixView != null && layoutManagerHelper.isViewHolderUpdated(this.mFixView)) {
            layoutManagerHelper.removeChildView(this.mFixView);
            recycler.recycleView(this.mFixView);
            this.mFixView = null;
        }
        this.mDoNormalHandle = false;
    }

    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        super.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
        if (this.mPos >= 0) {
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            if (!this.mDoNormalHandle && this.mPos >= i && this.mPos <= i2) {
                fixLayoutStateFromAbnormal2Normal(mainOrientationHelper, recycler, i, i2, layoutManagerHelper);
            }
            if (this.mDoNormalHandle || state.isPreLayout()) {
                state.isPreLayout();
                if (this.mFixView != null) {
                    layoutManagerHelper.removeChildView(this.mFixView);
                } else {
                    return;
                }
            }
            View view = this.mFixView;
            if (this.mDoNormalHandle || this.mFixView == null) {
                fixLayoutStateInCase2(mainOrientationHelper, recycler, i, i2, layoutManagerHelper);
            } else if (this.mFixView.getParent() == null) {
                layoutManagerHelper.addFixedView(this.mFixView);
            } else {
                fixLayoutStateInCase1(mainOrientationHelper, recycler, i, i2, layoutManagerHelper);
            }
            if (this.mPos == i) {
                if (this.stickyListener != null && !this.isLastStatusSticking) {
                    this.stickyListener.onSticky(this.mPos, this.mFixView);
                }
                this.isLastStatusSticking = true;
                return;
            }
            if (this.stickyListener != null && this.isLastStatusSticking) {
                this.stickyListener.onUnSticky(this.mPos, view);
            }
            this.isLastStatusSticking = false;
        }
    }

    private void fixLayoutStateFromAbnormal2Normal(OrientationHelperEx orientationHelperEx, RecyclerView.Recycler recycler, int i, int i2, LayoutManagerHelper layoutManagerHelper) {
        if (VirtualLayoutManager.sDebuggable) {
            Log.i(TAG, "abnormal pos: " + this.mPos + " start: " + i + " end: " + i2);
        }
        if (this.mFixView == null) {
            return;
        }
        if (this.mStickyStart) {
            for (int childCount = layoutManagerHelper.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = layoutManagerHelper.getChildAt(childCount);
                int position = layoutManagerHelper.getPosition(childAt);
                if (position < this.mPos) {
                    int decoratedEnd = orientationHelperEx.getDecoratedEnd(childAt);
                    LayoutHelper findLayoutHelperByPosition = layoutManagerHelper.findLayoutHelperByPosition(position);
                    if (findLayoutHelperByPosition instanceof RangeGridLayoutHelper) {
                        decoratedEnd += ((RangeGridLayoutHelper) findLayoutHelperByPosition).getBorderEndSpace(layoutManagerHelper);
                    } else if (findLayoutHelperByPosition instanceof MarginLayoutHelper) {
                        MarginLayoutHelper marginLayoutHelper = (MarginLayoutHelper) findLayoutHelperByPosition;
                        decoratedEnd = decoratedEnd + marginLayoutHelper.getMarginBottom() + marginLayoutHelper.getPaddingBottom();
                    }
                    if (decoratedEnd >= this.mOffset + this.mAdjuster.top) {
                        this.mDoNormalHandle = true;
                        return;
                    }
                    return;
                }
            }
            return;
        }
        for (int i3 = 0; i3 < layoutManagerHelper.getChildCount(); i3++) {
            View childAt2 = layoutManagerHelper.getChildAt(i3);
            int position2 = layoutManagerHelper.getPosition(childAt2);
            if (position2 > this.mPos) {
                int decoratedStart = orientationHelperEx.getDecoratedStart(childAt2);
                LayoutHelper findLayoutHelperByPosition2 = layoutManagerHelper.findLayoutHelperByPosition(position2);
                if (findLayoutHelperByPosition2 instanceof RangeGridLayoutHelper) {
                    decoratedStart -= ((RangeGridLayoutHelper) findLayoutHelperByPosition2).getBorderStartSpace(layoutManagerHelper);
                } else if (findLayoutHelperByPosition2 instanceof MarginLayoutHelper) {
                    MarginLayoutHelper marginLayoutHelper2 = (MarginLayoutHelper) findLayoutHelperByPosition2;
                    decoratedStart = (decoratedStart - marginLayoutHelper2.getMarginTop()) - marginLayoutHelper2.getPaddingTop();
                }
                if (decoratedStart >= this.mOffset + this.mAdjuster.bottom) {
                    this.mDoNormalHandle = true;
                    return;
                }
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x0104  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0121  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fixLayoutStateInCase1(com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r17, androidx.recyclerview.widget.RecyclerView.Recycler r18, int r19, int r20, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r21) {
        /*
            r16 = this;
            r7 = r16
            r0 = r17
            r8 = r21
            boolean r1 = r7.mStickyStart
            r9 = 0
            if (r1 == 0) goto L_0x0011
            int r1 = r7.mPos
            r2 = r20
            if (r2 >= r1) goto L_0x001b
        L_0x0011:
            boolean r1 = r7.mStickyStart
            if (r1 != 0) goto L_0x01ed
            int r1 = r7.mPos
            r2 = r19
            if (r2 > r1) goto L_0x01ed
        L_0x001b:
            android.view.View r1 = r7.mFixView
            int r1 = r0.getDecoratedMeasurement(r1)
            int r2 = r21.getOrientation()
            r3 = 1
            r4 = 0
            if (r2 != r3) goto L_0x002b
            r2 = 1
            goto L_0x002c
        L_0x002b:
            r2 = 0
        L_0x002c:
            if (r2 == 0) goto L_0x0033
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r5 = r7.mAdjuster
            int r5 = r5.top
            goto L_0x0037
        L_0x0033:
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r5 = r7.mAdjuster
            int r5 = r5.left
        L_0x0037:
            if (r2 == 0) goto L_0x003e
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r6 = r7.mAdjuster
            int r6 = r6.bottom
            goto L_0x0042
        L_0x003e:
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r6 = r7.mAdjuster
            int r6 = r6.right
        L_0x0042:
            r10 = -1
            if (r2 == 0) goto L_0x0148
            boolean r2 = r21.isDoLayoutRTL()
            if (r2 == 0) goto L_0x005d
            int r2 = r21.getContentWidth()
            int r11 = r21.getPaddingRight()
            int r2 = r2 - r11
            android.view.View r11 = r7.mFixView
            int r11 = r0.getDecoratedMeasurementInOther(r11)
            int r11 = r2 - r11
            goto L_0x0068
        L_0x005d:
            int r11 = r21.getPaddingLeft()
            android.view.View r2 = r7.mFixView
            int r2 = r0.getDecoratedMeasurementInOther(r2)
            int r2 = r2 + r11
        L_0x0068:
            boolean r12 = r7.mStickyStart
            if (r12 == 0) goto L_0x00ad
            int r12 = r21.getChildCount()
            int r12 = r12 - r3
            r13 = r9
        L_0x0072:
            if (r12 < 0) goto L_0x00f1
            android.view.View r13 = r8.getChildAt(r12)
            int r14 = r8.getPosition(r13)
            int r15 = r7.mPos
            if (r14 >= r15) goto L_0x00aa
            int r10 = r0.getDecoratedEnd(r13)
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r14 = r8.findLayoutHelperByPosition(r14)
            boolean r15 = r14 instanceof com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper
            if (r15 == 0) goto L_0x0094
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper r14 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper) r14
            int r14 = r14.getBorderEndSpace(r8)
            int r10 = r10 + r14
            goto L_0x00a4
        L_0x0094:
            boolean r15 = r14 instanceof com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper
            if (r15 == 0) goto L_0x00a4
            com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper r14 = (com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper) r14
            int r15 = r14.getMarginBottom()
            int r10 = r10 + r15
            int r14 = r14.getPaddingBottom()
            int r10 = r10 + r14
        L_0x00a4:
            int r14 = r10 + r1
            r7.mDoNormalHandle = r3
            r3 = r10
            goto L_0x00ec
        L_0x00aa:
            int r12 = r12 + -1
            goto L_0x0072
        L_0x00ad:
            r13 = r9
            r12 = 0
        L_0x00af:
            int r14 = r21.getChildCount()
            if (r12 >= r14) goto L_0x00f1
            android.view.View r13 = r8.getChildAt(r12)
            int r14 = r8.getPosition(r13)
            int r15 = r7.mPos
            if (r14 <= r15) goto L_0x00ee
            int r10 = r0.getDecoratedStart(r13)
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r14 = r8.findLayoutHelperByPosition(r14)
            boolean r15 = r14 instanceof com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper
            if (r15 == 0) goto L_0x00d5
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper r14 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper) r14
            int r14 = r14.getBorderStartSpace(r8)
            int r10 = r10 - r14
            goto L_0x00e5
        L_0x00d5:
            boolean r15 = r14 instanceof com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper
            if (r15 == 0) goto L_0x00e5
            com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper r14 = (com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper) r14
            int r15 = r14.getMarginTop()
            int r10 = r10 - r15
            int r14 = r14.getPaddingTop()
            int r10 = r10 - r14
        L_0x00e5:
            int r14 = r10 - r1
            int r12 = r12 + r3
            r7.mDoNormalHandle = r3
            r3 = r14
            r14 = r10
        L_0x00ec:
            r10 = r12
            goto L_0x00f3
        L_0x00ee:
            int r12 = r12 + 1
            goto L_0x00af
        L_0x00f1:
            r3 = 0
            r14 = 0
        L_0x00f3:
            if (r13 == 0) goto L_0x00f7
            if (r10 >= 0) goto L_0x00f9
        L_0x00f7:
            r7.mDoNormalHandle = r4
        L_0x00f9:
            boolean r12 = r21.getReverseLayout()
            if (r12 != 0) goto L_0x0111
            boolean r12 = r7.mStickyStart
            if (r12 != 0) goto L_0x0104
            goto L_0x0111
        L_0x0104:
            int r12 = r17.getStartAfterPadding()
            int r13 = r7.mOffset
            int r12 = r12 + r13
            int r12 = r12 + r5
            if (r3 >= r12) goto L_0x011d
            r7.mDoNormalHandle = r4
            goto L_0x011d
        L_0x0111:
            int r12 = r17.getEndAfterPadding()
            int r13 = r7.mOffset
            int r12 = r12 - r13
            int r12 = r12 - r6
            if (r14 <= r12) goto L_0x011d
            r7.mDoNormalHandle = r4
        L_0x011d:
            boolean r4 = r7.mDoNormalHandle
            if (r4 != 0) goto L_0x0143
            boolean r3 = r21.getReverseLayout()
            if (r3 != 0) goto L_0x0138
            boolean r3 = r7.mStickyStart
            if (r3 != 0) goto L_0x012c
            goto L_0x0138
        L_0x012c:
            int r0 = r17.getStartAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 + r3
            int r3 = r0 + r5
            int r14 = r3 + r1
            goto L_0x0143
        L_0x0138:
            int r0 = r17.getEndAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 - r3
            int r14 = r0 - r6
            int r3 = r14 - r1
        L_0x0143:
            r4 = r2
            r2 = r11
            r5 = r14
            goto L_0x01c3
        L_0x0148:
            int r2 = r21.getPaddingTop()
            android.view.View r11 = r7.mFixView
            int r11 = r0.getDecoratedMeasurementInOther(r11)
            int r11 = r11 + r2
            boolean r12 = r7.mDoNormalHandle
            if (r12 == 0) goto L_0x019c
            boolean r5 = r7.mStickyStart
            if (r5 == 0) goto L_0x0177
            int r5 = r21.getChildCount()
            int r5 = r5 - r3
        L_0x0160:
            if (r5 < 0) goto L_0x0196
            android.view.View r3 = r8.getChildAt(r5)
            int r6 = r8.getPosition(r3)
            int r12 = r7.mPos
            if (r6 >= r12) goto L_0x0174
            int r0 = r0.getDecoratedEnd(r3)
            int r1 = r1 + r0
            goto L_0x0191
        L_0x0174:
            int r5 = r5 + -1
            goto L_0x0160
        L_0x0177:
            r3 = 0
        L_0x0178:
            int r5 = r21.getChildCount()
            if (r3 >= r5) goto L_0x0196
            android.view.View r5 = r8.getChildAt(r3)
            int r6 = r8.getPosition(r5)
            int r12 = r7.mPos
            if (r6 <= r12) goto L_0x0193
            int r4 = r0.getDecoratedStart(r5)
            int r0 = r4 - r1
            r1 = r4
        L_0x0191:
            r4 = r0
            goto L_0x0197
        L_0x0193:
            int r3 = r3 + 1
            goto L_0x0178
        L_0x0196:
            r1 = 0
        L_0x0197:
            r3 = r2
            r2 = r4
            r5 = r11
            r4 = r1
            goto L_0x01c3
        L_0x019c:
            boolean r3 = r21.getReverseLayout()
            if (r3 != 0) goto L_0x01b5
            boolean r3 = r7.mStickyStart
            if (r3 != 0) goto L_0x01a7
            goto L_0x01b5
        L_0x01a7:
            int r0 = r17.getStartAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 + r3
            int r0 = r0 + r5
            int r1 = r1 + r0
            r4 = r1
            r3 = r2
            r5 = r11
            r2 = r0
            goto L_0x01c3
        L_0x01b5:
            int r0 = r17.getEndAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 - r3
            int r0 = r0 - r6
            int r1 = r0 - r1
            r4 = r0
            r3 = r2
            r5 = r11
            r2 = r1
        L_0x01c3:
            android.view.View r1 = r7.mFixView
            r0 = r16
            r6 = r21
            r0.layoutChildWithMargin(r1, r2, r3, r4, r5, r6)
            boolean r0 = r7.mDoNormalHandle
            if (r0 == 0) goto L_0x01e2
            if (r10 < 0) goto L_0x01f9
            android.view.View r0 = r7.mFixView
            android.view.ViewParent r0 = r0.getParent()
            if (r0 != 0) goto L_0x01df
            android.view.View r0 = r7.mFixView
            r8.addChildView((android.view.View) r0, (int) r10)
        L_0x01df:
            r7.mFixView = r9
            goto L_0x01f9
        L_0x01e2:
            android.view.View r0 = r7.mFixView
            r8.showView(r0)
            android.view.View r0 = r7.mFixView
            r8.addFixedView(r0)
            goto L_0x01f9
        L_0x01ed:
            android.view.View r0 = r7.mFixView
            r8.removeChildView(r0)
            android.view.View r0 = r7.mFixView
            r8.recycleView(r0)
            r7.mFixView = r9
        L_0x01f9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper.fixLayoutStateInCase1(com.taobao.android.dxcontainer.vlayout.OrientationHelperEx, androidx.recyclerview.widget.RecyclerView$Recycler, int, int, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:137:0x024e  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x0260  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0097  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fixLayoutStateInCase2(com.taobao.android.dxcontainer.vlayout.OrientationHelperEx r19, androidx.recyclerview.widget.RecyclerView.Recycler r20, int r21, int r22, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper r23) {
        /*
            r18 = this;
            r7 = r18
            r0 = r19
            r8 = r23
            android.view.View r1 = r7.mFixView
            if (r1 != 0) goto L_0x0010
            int r1 = r7.mPos
            android.view.View r1 = r8.findViewByPosition(r1)
        L_0x0010:
            int r2 = r23.getOrientation()
            r3 = 1
            if (r2 != r3) goto L_0x0019
            r2 = 1
            goto L_0x001a
        L_0x0019:
            r2 = 0
        L_0x001a:
            if (r2 == 0) goto L_0x0021
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r5 = r7.mAdjuster
            int r5 = r5.top
            goto L_0x0025
        L_0x0021:
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r5 = r7.mAdjuster
            int r5 = r5.left
        L_0x0025:
            if (r2 == 0) goto L_0x002c
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r6 = r7.mAdjuster
            int r6 = r6.bottom
            goto L_0x0030
        L_0x002c:
            com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster r6 = r7.mAdjuster
            int r6 = r6.right
        L_0x0030:
            boolean r9 = r7.mStickyStart
            if (r9 == 0) goto L_0x003a
            int r9 = r7.mPos
            r10 = r22
            if (r10 >= r9) goto L_0x0044
        L_0x003a:
            boolean r9 = r7.mStickyStart
            if (r9 != 0) goto L_0x0092
            int r9 = r7.mPos
            r10 = r21
            if (r10 > r9) goto L_0x0092
        L_0x0044:
            if (r1 != 0) goto L_0x0065
            int r1 = r7.mOffset
            boolean r9 = r7.mStickyStart
            if (r9 == 0) goto L_0x004e
            r9 = r5
            goto L_0x004f
        L_0x004e:
            r9 = r6
        L_0x004f:
            int r1 = r1 + r9
            if (r1 < 0) goto L_0x0054
            r1 = 1
            goto L_0x0055
        L_0x0054:
            r1 = 0
        L_0x0055:
            int r9 = r7.mPos
            r10 = r20
            android.view.View r9 = r10.getViewForPosition(r9)
            r7.mFixView = r9
            android.view.View r9 = r7.mFixView
            r7.doMeasure(r9, r8)
            goto L_0x0093
        L_0x0065:
            boolean r9 = r7.mStickyStart
            if (r9 == 0) goto L_0x007b
            int r9 = r0.getDecoratedStart(r1)
            int r10 = r19.getStartAfterPadding()
            int r11 = r7.mOffset
            int r10 = r10 + r11
            int r10 = r10 + r5
            if (r9 < r10) goto L_0x007b
            r7.mFixView = r1
        L_0x0079:
            r1 = 1
            goto L_0x0093
        L_0x007b:
            boolean r9 = r7.mStickyStart
            if (r9 != 0) goto L_0x0090
            int r9 = r0.getDecoratedEnd(r1)
            int r10 = r19.getEndAfterPadding()
            int r11 = r7.mOffset
            int r10 = r10 - r11
            int r10 = r10 - r6
            if (r9 > r10) goto L_0x0090
            r7.mFixView = r1
            goto L_0x0079
        L_0x0090:
            r7.mFixView = r1
        L_0x0092:
            r1 = 0
        L_0x0093:
            android.view.View r9 = r7.mFixView
            if (r9 == 0) goto L_0x0266
            android.view.View r9 = r7.mFixView
            android.view.ViewGroup$LayoutParams r9 = r9.getLayoutParams()
            androidx.recyclerview.widget.RecyclerView$LayoutParams r9 = (androidx.recyclerview.widget.RecyclerView.LayoutParams) r9
            boolean r9 = r9.isItemRemoved()
            if (r9 == 0) goto L_0x00a6
            return
        L_0x00a6:
            android.view.View r9 = r7.mFixView
            int r9 = r0.getDecoratedMeasurement(r9)
            r10 = 0
            r11 = -1
            if (r2 == 0) goto L_0x01ba
            boolean r2 = r23.isDoLayoutRTL()
            if (r2 == 0) goto L_0x00c8
            int r2 = r23.getContentWidth()
            int r12 = r23.getPaddingRight()
            int r2 = r2 - r12
            android.view.View r12 = r7.mFixView
            int r12 = r0.getDecoratedMeasurementInOther(r12)
            int r12 = r2 - r12
            goto L_0x00d3
        L_0x00c8:
            int r12 = r23.getPaddingLeft()
            android.view.View r2 = r7.mFixView
            int r2 = r0.getDecoratedMeasurementInOther(r2)
            int r2 = r2 + r12
        L_0x00d3:
            if (r1 == 0) goto L_0x0189
            boolean r13 = r7.mStickyStart
            if (r13 == 0) goto L_0x011b
            int r13 = r23.getChildCount()
            int r13 = r13 - r3
            r14 = r10
        L_0x00df:
            if (r13 < 0) goto L_0x0160
            android.view.View r14 = r8.getChildAt(r13)
            int r15 = r8.getPosition(r14)
            int r4 = r7.mPos
            if (r15 >= r4) goto L_0x0118
            int r4 = r0.getDecoratedEnd(r14)
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r11 = r8.findLayoutHelperByPosition(r15)
            boolean r15 = r11 instanceof com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper
            if (r15 == 0) goto L_0x0101
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper r11 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper) r11
            int r11 = r11.getBorderEndSpace(r8)
            int r4 = r4 + r11
            goto L_0x0111
        L_0x0101:
            boolean r15 = r11 instanceof com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper
            if (r15 == 0) goto L_0x0111
            com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper r11 = (com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper) r11
            int r15 = r11.getMarginBottom()
            int r4 = r4 + r15
            int r11 = r11.getPaddingBottom()
            int r4 = r4 + r11
        L_0x0111:
            int r11 = r4 + r9
            int r13 = r13 + r3
            r3 = r4
            r4 = r11
            r11 = r13
            goto L_0x0162
        L_0x0118:
            int r13 = r13 + -1
            goto L_0x00df
        L_0x011b:
            r14 = r10
            r3 = 0
        L_0x011d:
            int r4 = r23.getChildCount()
            if (r3 >= r4) goto L_0x0160
            android.view.View r14 = r8.getChildAt(r3)
            int r4 = r8.getPosition(r14)
            int r13 = r7.mPos
            if (r4 <= r13) goto L_0x015d
            int r11 = r0.getDecoratedStart(r14)
            com.taobao.android.dxcontainer.vlayout.LayoutHelper r4 = r8.findLayoutHelperByPosition(r4)
            boolean r13 = r4 instanceof com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper
            if (r13 == 0) goto L_0x0144
            com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper r4 = (com.taobao.android.dxcontainer.vlayout.layout.RangeGridLayoutHelper) r4
            int r4 = r4.getBorderStartSpace(r8)
            int r11 = r11 - r4
        L_0x0142:
            r4 = r11
            goto L_0x0155
        L_0x0144:
            boolean r13 = r4 instanceof com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper
            if (r13 == 0) goto L_0x0142
            com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper r4 = (com.taobao.android.dxcontainer.vlayout.layout.MarginLayoutHelper) r4
            int r13 = r4.getMarginTop()
            int r11 = r11 - r13
            int r4 = r4.getPaddingTop()
            int r11 = r11 - r4
            goto L_0x0142
        L_0x0155:
            int r11 = r4 - r9
            r17 = r11
            r11 = r3
            r3 = r17
            goto L_0x0162
        L_0x015d:
            int r3 = r3 + 1
            goto L_0x011d
        L_0x0160:
            r3 = 0
            r4 = 0
        L_0x0162:
            if (r14 == 0) goto L_0x0166
            if (r11 >= 0) goto L_0x0167
        L_0x0166:
            r1 = 0
        L_0x0167:
            boolean r13 = r23.getReverseLayout()
            if (r13 != 0) goto L_0x017d
            boolean r13 = r7.mStickyStart
            if (r13 != 0) goto L_0x0172
            goto L_0x017d
        L_0x0172:
            int r13 = r19.getStartAfterPadding()
            int r14 = r7.mOffset
            int r13 = r13 + r14
            int r13 = r13 + r5
            if (r3 >= r13) goto L_0x018b
            goto L_0x0187
        L_0x017d:
            int r13 = r19.getEndAfterPadding()
            int r14 = r7.mOffset
            int r13 = r13 - r14
            int r13 = r13 - r6
            if (r4 <= r13) goto L_0x018b
        L_0x0187:
            r1 = 0
            goto L_0x018b
        L_0x0189:
            r3 = 0
            r4 = 0
        L_0x018b:
            if (r1 != 0) goto L_0x01b4
            boolean r3 = r23.getReverseLayout()
            if (r3 != 0) goto L_0x01a7
            boolean r3 = r7.mStickyStart
            if (r3 != 0) goto L_0x0198
            goto L_0x01a7
        L_0x0198:
            int r0 = r19.getStartAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 + r3
            int r0 = r0 + r5
            int r9 = r9 + r0
            r3 = r0
            r4 = r2
            r5 = r9
            r2 = r12
            goto L_0x022f
        L_0x01a7:
            int r0 = r19.getEndAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 - r3
            int r0 = r0 - r6
            int r3 = r0 - r9
            r5 = r0
            r9 = r1
            goto L_0x01b6
        L_0x01b4:
            r9 = r1
            r5 = r4
        L_0x01b6:
            r4 = r2
            r2 = r12
            goto L_0x0243
        L_0x01ba:
            int r2 = r23.getPaddingTop()
            android.view.View r4 = r7.mFixView
            int r4 = r0.getDecoratedMeasurementInOther(r4)
            int r4 = r4 + r2
            if (r1 == 0) goto L_0x0217
            boolean r5 = r7.mStickyStart
            if (r5 == 0) goto L_0x01ee
            int r5 = r23.getChildCount()
            int r5 = r5 - r3
        L_0x01d0:
            if (r5 < 0) goto L_0x01e9
            android.view.View r3 = r8.getChildAt(r5)
            int r6 = r8.getPosition(r3)
            int r12 = r7.mPos
            if (r6 >= r12) goto L_0x01e6
            int r0 = r0.getDecoratedEnd(r3)
            int r9 = r9 + r0
            r16 = r0
            goto L_0x01ec
        L_0x01e6:
            int r5 = r5 + -1
            goto L_0x01d0
        L_0x01e9:
            r9 = 0
            r16 = 0
        L_0x01ec:
            r0 = r9
            goto L_0x0210
        L_0x01ee:
            r3 = 0
        L_0x01ef:
            int r5 = r23.getChildCount()
            if (r3 >= r5) goto L_0x020d
            android.view.View r5 = r8.getChildAt(r3)
            int r6 = r8.getPosition(r5)
            int r12 = r7.mPos
            if (r6 <= r12) goto L_0x020a
            int r0 = r0.getDecoratedStart(r5)
            int r3 = r0 - r9
            r16 = r3
            goto L_0x0210
        L_0x020a:
            int r3 = r3 + 1
            goto L_0x01ef
        L_0x020d:
            r0 = 0
            r16 = 0
        L_0x0210:
            r9 = r1
            r3 = r2
            r5 = r4
            r2 = r16
            r4 = r0
            goto L_0x0243
        L_0x0217:
            boolean r3 = r23.getReverseLayout()
            if (r3 != 0) goto L_0x0231
            boolean r3 = r7.mStickyStart
            if (r3 != 0) goto L_0x0222
            goto L_0x0231
        L_0x0222:
            int r0 = r19.getStartAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 + r3
            int r0 = r0 + r5
            int r9 = r9 + r0
            r3 = r2
            r5 = r4
            r4 = r9
            r2 = r0
        L_0x022f:
            r9 = r1
            goto L_0x0243
        L_0x0231:
            int r0 = r19.getEndAfterPadding()
            int r3 = r7.mOffset
            int r0 = r0 - r3
            int r0 = r0 - r6
            int r3 = r0 - r9
            r9 = r1
            r5 = r4
            r4 = r0
            r17 = r3
            r3 = r2
            r2 = r17
        L_0x0243:
            android.view.View r1 = r7.mFixView
            r0 = r18
            r6 = r23
            r0.layoutChildWithMargin(r1, r2, r3, r4, r5, r6)
            if (r9 == 0) goto L_0x0260
            if (r11 < 0) goto L_0x0265
            android.view.View r0 = r7.mFixView
            android.view.ViewParent r0 = r0.getParent()
            if (r0 != 0) goto L_0x025d
            android.view.View r0 = r7.mFixView
            r8.addChildView((android.view.View) r0, (int) r11)
        L_0x025d:
            r7.mFixView = r10
            goto L_0x0265
        L_0x0260:
            android.view.View r0 = r7.mFixView
            r8.addFixedView(r0)
        L_0x0265:
            r1 = r9
        L_0x0266:
            r7.mDoNormalHandle = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper.fixLayoutStateInCase2(com.taobao.android.dxcontainer.vlayout.OrientationHelperEx, androidx.recyclerview.widget.RecyclerView$Recycler, int, int, com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper):void");
    }

    @Nullable
    public View getFixedView() {
        return this.mFixView;
    }

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
        if (this.mFixView != null) {
            layoutManagerHelper.recycleView(this.mFixView);
            layoutManagerHelper.removeChildView(this.mFixView);
            this.mFixView = null;
        }
    }

    private void doMeasure(View view, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        boolean z = layoutManagerHelper.getOrientation() == 1;
        int contentWidth = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight()) - getHorizontalMargin();
        int contentHeight = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom()) - getVerticalMargin();
        float f = layoutParams.mAspectRatio;
        if (z) {
            int childMeasureSpec = layoutManagerHelper.getChildMeasureSpec(contentWidth, layoutParams.width, false);
            if (!Float.isNaN(f) && f > 0.0f) {
                i2 = View.MeasureSpec.makeMeasureSpec((int) ((((float) contentWidth) / f) + 0.5f), 1073741824);
            } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
                i2 = layoutManagerHelper.getChildMeasureSpec(contentHeight, layoutParams.height, true);
            } else {
                double d = (double) (((float) contentWidth) / this.mAspectRatio);
                Double.isNaN(d);
                i2 = View.MeasureSpec.makeMeasureSpec((int) (d + 0.5d), 1073741824);
            }
            layoutManagerHelper.measureChildWithMargins(view, childMeasureSpec, i2);
            return;
        }
        int childMeasureSpec2 = layoutManagerHelper.getChildMeasureSpec(contentHeight, layoutParams.height, false);
        if (!Float.isNaN(f) && f > 0.0f) {
            double d2 = (double) (((float) contentHeight) * f);
            Double.isNaN(d2);
            i = View.MeasureSpec.makeMeasureSpec((int) (d2 + 0.5d), 1073741824);
        } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
            i = layoutManagerHelper.getChildMeasureSpec(contentWidth, layoutParams.width, true);
        } else {
            double d3 = (double) (((float) contentHeight) * this.mAspectRatio);
            Double.isNaN(d3);
            i = View.MeasureSpec.makeMeasureSpec((int) (d3 + 0.5d), 1073741824);
        }
        layoutManagerHelper.measureChildWithMargins(view, i, childMeasureSpec2);
    }

    public void setStickyListener(StickyListener stickyListener2) {
        this.stickyListener = stickyListener2;
    }
}
