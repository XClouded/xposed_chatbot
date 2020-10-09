package com.taobao.android.dxcontainer.vlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class ExposeLinearLayoutManagerEx extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    static final int FLAG_INVALID = 4;
    static final int FLAG_UPDATED = 2;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33f;
    private static final String TAG = "ExposeLLManagerEx";
    public static final int VERTICAL = 1;
    private static Field vhField;
    private static Method vhSetFlags;
    private Object[] emptyArgs;
    private LayoutChunkResult layoutChunkResultCache;
    private final AnchorInfo mAnchorInfo;
    private final ChildHelperWrapper mChildHelperWrapper;
    protected Bundle mCurrentPendingSavedState;
    private int mCurrentPendingScrollPosition;
    private final Method mEnsureLayoutStateMethod;
    private boolean mLastStackFromEnd;
    protected LayoutState mLayoutState;
    /* access modifiers changed from: private */
    public OrientationHelperEx mOrientationHelper;
    private int mPendingScrollPositionOffset;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    private boolean mShouldReverseLayoutExpose;
    protected int recycleOffset;

    /* access modifiers changed from: protected */
    public int computeAlignOffset(int i, boolean z, boolean z2) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int computeAlignOffset(View view, boolean z, boolean z2) {
        return 0;
    }

    public boolean isEnableMarginOverLap() {
        return false;
    }

    public void onAnchorReady(RecyclerView.State state, AnchorInfo anchorInfo) {
    }

    public ExposeLinearLayoutManagerEx(Context context) {
        this(context, 1, false);
    }

    public ExposeLinearLayoutManagerEx(Context context, int i, boolean z) {
        super(context, i, z);
        this.mShouldReverseLayoutExpose = false;
        this.mCurrentPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mCurrentPendingSavedState = null;
        this.emptyArgs = new Object[0];
        this.layoutChunkResultCache = new LayoutChunkResult();
        this.mAnchorInfo = new AnchorInfo();
        setOrientation(i);
        setReverseLayout(z);
        this.mChildHelperWrapper = new ChildHelperWrapper(this);
        try {
            this.mEnsureLayoutStateMethod = LinearLayoutManager.class.getDeclaredMethod("ensureLayoutState", new Class[0]);
            this.mEnsureLayoutStateMethod.setAccessible(true);
            Class<RecyclerView.LayoutManager> cls = RecyclerView.LayoutManager.class;
            try {
                Method declaredMethod = cls.getDeclaredMethod("setItemPrefetchEnabled", new Class[]{Boolean.TYPE});
                if (declaredMethod != null) {
                    declaredMethod.invoke(this, new Object[]{false});
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Parcelable onSaveInstanceState() {
        if (this.mCurrentPendingSavedState != null) {
            return new Bundle(this.mCurrentPendingSavedState);
        }
        Bundle bundle = new Bundle();
        if (getChildCount() > 0) {
            boolean z = this.mLastStackFromEnd ^ this.mShouldReverseLayoutExpose;
            bundle.putBoolean("AnchorLayoutFromEnd", z);
            if (z) {
                View childClosestToEndExpose = getChildClosestToEndExpose();
                bundle.putInt("AnchorOffset", this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(childClosestToEndExpose));
                bundle.putInt("AnchorPosition", getPosition(childClosestToEndExpose));
            } else {
                View childClosestToStartExpose = getChildClosestToStartExpose();
                bundle.putInt("AnchorPosition", getPosition(childClosestToStartExpose));
                bundle.putInt("AnchorOffset", this.mOrientationHelper.getDecoratedStart(childClosestToStartExpose) - this.mOrientationHelper.getStartAfterPadding());
            }
        } else {
            bundle.putInt("AnchorPosition", -1);
        }
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            this.mCurrentPendingSavedState = (Bundle) parcelable;
            requestLayout();
        }
    }

    public void setOrientation(int i) {
        super.setOrientation(i);
        this.mOrientationHelper = null;
    }

    public void setRecycleOffset(int i) {
        this.recycleOffset = i;
    }

    private void myResolveShouldLayoutReverse() {
        if (getOrientation() == 1 || !isLayoutRTL()) {
            this.mShouldReverseLayoutExpose = getReverseLayout();
        } else {
            this.mShouldReverseLayoutExpose = !getReverseLayout();
        }
    }

    public PointF computeScrollVectorForPosition(int i) {
        if (getChildCount() == 0) {
            return null;
        }
        boolean z = false;
        int i2 = 1;
        if (i < getPosition(getChildAt(0))) {
            z = true;
        }
        if (z != this.mShouldReverseLayoutExpose) {
            i2 = -1;
        }
        if (getOrientation() == 0) {
            return new PointF((float) i2, 0.0f);
        }
        return new PointF(0.0f, (float) i2);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i;
        int i2;
        int i3;
        View findViewByPosition;
        int i4;
        if (this.mCurrentPendingSavedState != null && this.mCurrentPendingSavedState.getInt("AnchorPosition") >= 0) {
            this.mCurrentPendingScrollPosition = this.mCurrentPendingSavedState.getInt("AnchorPosition");
        }
        ensureLayoutStateExpose();
        this.mLayoutState.mRecycle = false;
        myResolveShouldLayoutReverse();
        this.mAnchorInfo.reset();
        this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayoutExpose ^ getStackFromEnd();
        updateAnchorInfoForLayoutExpose(state, this.mAnchorInfo);
        int extraLayoutSpace = getExtraLayoutSpace(state);
        if ((state.getTargetScrollPosition() < this.mAnchorInfo.mPosition) == this.mShouldReverseLayoutExpose) {
            i = extraLayoutSpace;
            extraLayoutSpace = 0;
        } else {
            i = 0;
        }
        int startAfterPadding = extraLayoutSpace + this.mOrientationHelper.getStartAfterPadding();
        int endPadding = i + this.mOrientationHelper.getEndPadding();
        if (!(!state.isPreLayout() || this.mCurrentPendingScrollPosition == -1 || this.mPendingScrollPositionOffset == Integer.MIN_VALUE || (findViewByPosition = findViewByPosition(this.mCurrentPendingScrollPosition)) == null)) {
            if (this.mShouldReverseLayoutExpose) {
                i4 = (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(findViewByPosition)) - this.mPendingScrollPositionOffset;
            } else {
                i4 = this.mPendingScrollPositionOffset - (this.mOrientationHelper.getDecoratedStart(findViewByPosition) - this.mOrientationHelper.getStartAfterPadding());
            }
            if (i4 > 0) {
                startAfterPadding += i4;
            } else {
                endPadding -= i4;
            }
        }
        onAnchorReady(state, this.mAnchorInfo);
        detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        this.mLayoutState.mOnRefresLayout = true;
        if (this.mAnchorInfo.mLayoutFromEnd) {
            updateLayoutStateToFillStartExpose(this.mAnchorInfo);
            this.mLayoutState.mExtra = startAfterPadding;
            fill(recycler, this.mLayoutState, state, false);
            i3 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
                endPadding += this.mLayoutState.mAvailable;
            }
            updateLayoutStateToFillEndExpose(this.mAnchorInfo);
            this.mLayoutState.mExtra = endPadding;
            this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            fill(recycler, this.mLayoutState, state, false);
            i2 = this.mLayoutState.mOffset;
        } else {
            updateLayoutStateToFillEndExpose(this.mAnchorInfo);
            this.mLayoutState.mExtra = endPadding;
            fill(recycler, this.mLayoutState, state, false);
            i2 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
                startAfterPadding += this.mLayoutState.mAvailable;
            }
            updateLayoutStateToFillStartExpose(this.mAnchorInfo);
            this.mLayoutState.mExtra = startAfterPadding;
            this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            fill(recycler, this.mLayoutState, state, false);
            i3 = this.mLayoutState.mOffset;
        }
        if (getChildCount() > 0) {
            if (this.mShouldReverseLayoutExpose ^ getStackFromEnd()) {
                int fixLayoutEndGapExpose = fixLayoutEndGapExpose(i2, recycler, state, true);
                int i5 = i3 + fixLayoutEndGapExpose;
                int fixLayoutStartGapExpose = fixLayoutStartGapExpose(i5, recycler, state, false);
                i3 = i5 + fixLayoutStartGapExpose;
                i2 = i2 + fixLayoutEndGapExpose + fixLayoutStartGapExpose;
            } else {
                int fixLayoutStartGapExpose2 = fixLayoutStartGapExpose(i3, recycler, state, true);
                int i6 = i2 + fixLayoutStartGapExpose2;
                int fixLayoutEndGapExpose2 = fixLayoutEndGapExpose(i6, recycler, state, false);
                i3 = i3 + fixLayoutStartGapExpose2 + fixLayoutEndGapExpose2;
                i2 = i6 + fixLayoutEndGapExpose2;
            }
        }
        layoutForPredictiveAnimationsExpose(recycler, state, i3, i2);
        if (!state.isPreLayout()) {
            this.mCurrentPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            this.mOrientationHelper.onLayoutComplete();
        }
        this.mLastStackFromEnd = getStackFromEnd();
        this.mCurrentPendingSavedState = null;
    }

    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        this.mRecyclerView = null;
    }

    public int findFirstVisibleItemPosition() {
        ensureLayoutStateExpose();
        return super.findFirstVisibleItemPosition();
    }

    public int findLastVisibleItemPosition() {
        ensureLayoutStateExpose();
        try {
            return super.findLastVisibleItemPosition();
        } catch (Exception e) {
            Log.d("LastItem", "itemCount: " + getItemCount());
            Log.d("LastItem", "childCount: " + getChildCount());
            Log.d("LastItem", "child: " + getChildAt(getChildCount() + -1));
            Log.d("LastItem", "RV childCount: " + this.mRecyclerView.getChildCount());
            Log.d("LastItem", "RV child: " + this.mRecyclerView.getChildAt(this.mRecyclerView.getChildCount() + -1));
            throw e;
        }
    }

    private View myFindReferenceChildClosestToEnd(RecyclerView.State state) {
        return this.mShouldReverseLayoutExpose ? myFindFirstReferenceChild(state.getItemCount()) : myFindLastReferenceChild(state.getItemCount());
    }

    private View myFindReferenceChildClosestToStart(RecyclerView.State state) {
        return this.mShouldReverseLayoutExpose ? myFindLastReferenceChild(state.getItemCount()) : myFindFirstReferenceChild(state.getItemCount());
    }

    private View myFindFirstReferenceChild(int i) {
        return findReferenceChildInternal(0, getChildCount(), i);
    }

    private View myFindLastReferenceChild(int i) {
        return findReferenceChildInternal(getChildCount() - 1, -1, i);
    }

    private View findReferenceChildInternal(int i, int i2, int i3) {
        ensureLayoutStateExpose();
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int i4 = i2 > i ? 1 : -1;
        View view = null;
        View view2 = null;
        while (i != i2) {
            View childAt = getChildAt(i);
            int position = getPosition(childAt);
            if (position >= 0 && position < i3) {
                if (((RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (view2 == null) {
                        view2 = childAt;
                    }
                } else if (this.mOrientationHelper.getDecoratedStart(childAt) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(childAt) >= startAfterPadding) {
                    return childAt;
                } else {
                    if (view == null) {
                        view = childAt;
                    }
                }
            }
            i += i4;
        }
        return view != null ? view : view2;
    }

    private void layoutForPredictiveAnimationsExpose(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2) {
        int i3;
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        if (state.willRunPredictiveAnimations() && getChildCount() != 0 && !state.isPreLayout() && supportsPredictiveItemAnimations()) {
            List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
            int size = scrapList.size();
            int position = getPosition(getChildAt(0));
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            while (true) {
                i3 = -1;
                char c = 1;
                if (i4 >= size) {
                    break;
                }
                RecyclerView.ViewHolder viewHolder = scrapList.get(i4);
                if ((viewHolder.getPosition() < position) != this.mShouldReverseLayoutExpose) {
                    c = 65535;
                }
                if (c == 65535) {
                    i5 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                } else {
                    i6 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                }
                i4++;
            }
            this.mLayoutState.mScrapList = scrapList;
            if (i5 > 0) {
                updateLayoutStateToFillStartExpose(getPosition(getChildClosestToStartExpose()), i);
                this.mLayoutState.mExtra = i5;
                this.mLayoutState.mAvailable = 0;
                this.mLayoutState.mCurrentPosition += this.mShouldReverseLayoutExpose ? 1 : -1;
                this.mLayoutState.mOnRefresLayout = true;
                fill(recycler2, this.mLayoutState, state2, false);
            }
            if (i6 > 0) {
                updateLayoutStateToFillEndExpose(getPosition(getChildClosestToEndExpose()), i2);
                this.mLayoutState.mExtra = i6;
                this.mLayoutState.mAvailable = 0;
                LayoutState layoutState = this.mLayoutState;
                int i7 = layoutState.mCurrentPosition;
                if (!this.mShouldReverseLayoutExpose) {
                    i3 = 1;
                }
                layoutState.mCurrentPosition = i7 + i3;
                this.mLayoutState.mOnRefresLayout = true;
                fill(recycler2, this.mLayoutState, state2, false);
            }
            this.mLayoutState.mScrapList = null;
        }
    }

    private void updateAnchorInfoForLayoutExpose(RecyclerView.State state, AnchorInfo anchorInfo) {
        if (!updateAnchorFromPendingDataExpose(state, anchorInfo) && !updateAnchorFromChildrenExpose(state, anchorInfo)) {
            anchorInfo.assignCoordinateFromPadding();
            anchorInfo.mPosition = getStackFromEnd() ? state.getItemCount() - 1 : 0;
        }
    }

    private boolean updateAnchorFromChildrenExpose(RecyclerView.State state, AnchorInfo anchorInfo) {
        View view;
        int i;
        boolean z = false;
        if (getChildCount() == 0) {
            return false;
        }
        View focusedChild = getFocusedChild();
        if (focusedChild != null && anchorInfo.assignFromViewIfValid(focusedChild, state)) {
            return true;
        }
        if (this.mLastStackFromEnd != getStackFromEnd()) {
            return false;
        }
        if (anchorInfo.mLayoutFromEnd) {
            view = myFindReferenceChildClosestToEnd(state);
        } else {
            view = myFindReferenceChildClosestToStart(state);
        }
        if (view == null) {
            return false;
        }
        anchorInfo.assignFromView(view);
        if (!state.isPreLayout() && supportsPredictiveItemAnimations()) {
            if (this.mOrientationHelper.getDecoratedStart(view) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view) < this.mOrientationHelper.getStartAfterPadding()) {
                z = true;
            }
            if (z) {
                if (anchorInfo.mLayoutFromEnd) {
                    i = this.mOrientationHelper.getEndAfterPadding();
                } else {
                    i = this.mOrientationHelper.getStartAfterPadding();
                }
                anchorInfo.mCoordinate = i;
            }
        }
        return true;
    }

    private boolean updateAnchorFromPendingDataExpose(RecyclerView.State state, AnchorInfo anchorInfo) {
        int i;
        boolean z = false;
        if (state.isPreLayout() || this.mCurrentPendingScrollPosition == -1) {
            return false;
        }
        if (this.mCurrentPendingScrollPosition < 0 || this.mCurrentPendingScrollPosition >= state.getItemCount()) {
            this.mCurrentPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mCurrentPendingScrollPosition;
        if (this.mCurrentPendingSavedState != null && this.mCurrentPendingSavedState.getInt("AnchorPosition") >= 0) {
            anchorInfo.mLayoutFromEnd = this.mCurrentPendingSavedState.getBoolean("AnchorLayoutFromEnd");
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mCurrentPendingSavedState.getInt("AnchorOffset");
            } else {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mCurrentPendingSavedState.getInt("AnchorOffset");
            }
            return true;
        } else if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            View findViewByPosition = findViewByPosition(this.mCurrentPendingScrollPosition);
            if (findViewByPosition == null) {
                if (getChildCount() > 0) {
                    if ((this.mCurrentPendingScrollPosition < getPosition(getChildAt(0))) == this.mShouldReverseLayoutExpose) {
                        z = true;
                    }
                    anchorInfo.mLayoutFromEnd = z;
                }
                anchorInfo.assignCoordinateFromPadding();
            } else if (this.mOrientationHelper.getDecoratedMeasurement(findViewByPosition) > this.mOrientationHelper.getTotalSpace()) {
                anchorInfo.assignCoordinateFromPadding();
                return true;
            } else if (this.mOrientationHelper.getDecoratedStart(findViewByPosition) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                anchorInfo.mLayoutFromEnd = false;
                return true;
            } else if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(findViewByPosition) < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                anchorInfo.mLayoutFromEnd = true;
                return true;
            } else {
                if (anchorInfo.mLayoutFromEnd) {
                    i = this.mOrientationHelper.getDecoratedEnd(findViewByPosition) + this.mOrientationHelper.getTotalSpaceChange();
                } else {
                    i = this.mOrientationHelper.getDecoratedStart(findViewByPosition);
                }
                anchorInfo.mCoordinate = i;
            }
            return true;
        } else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayoutExpose;
            if (this.mShouldReverseLayoutExpose) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
            } else {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
            }
            return true;
        }
    }

    private int fixLayoutEndGapExpose(int i, RecyclerView.Recycler recycler, RecyclerView.State state, boolean z) {
        int endAfterPadding;
        int endAfterPadding2 = this.mOrientationHelper.getEndAfterPadding() - i;
        if (endAfterPadding2 <= 0) {
            return 0;
        }
        int i2 = -scrollInternalBy(-endAfterPadding2, recycler, state);
        int i3 = i + i2;
        if (!z || (endAfterPadding = this.mOrientationHelper.getEndAfterPadding() - i3) <= 0) {
            return i2;
        }
        this.mOrientationHelper.offsetChildren(endAfterPadding);
        return endAfterPadding + i2;
    }

    private int fixLayoutStartGapExpose(int i, RecyclerView.Recycler recycler, RecyclerView.State state, boolean z) {
        int startAfterPadding;
        int startAfterPadding2 = i - this.mOrientationHelper.getStartAfterPadding();
        if (startAfterPadding2 <= 0) {
            return 0;
        }
        int i2 = -scrollInternalBy(startAfterPadding2, recycler, state);
        int i3 = i + i2;
        if (!z || (startAfterPadding = i3 - this.mOrientationHelper.getStartAfterPadding()) <= 0) {
            return i2;
        }
        this.mOrientationHelper.offsetChildren(-startAfterPadding);
        return i2 - startAfterPadding;
    }

    private void updateLayoutStateToFillEndExpose(AnchorInfo anchorInfo) {
        updateLayoutStateToFillEndExpose(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillEndExpose(int i, int i2) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - i2;
        this.mLayoutState.mItemDirection = this.mShouldReverseLayoutExpose ? -1 : 1;
        this.mLayoutState.mCurrentPosition = i;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = i2;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillStartExpose(AnchorInfo anchorInfo) {
        updateLayoutStateToFillStartExpose(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillStartExpose(int i, int i2) {
        this.mLayoutState.mAvailable = i2 - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = i;
        this.mLayoutState.mItemDirection = this.mShouldReverseLayoutExpose ? 1 : -1;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = i2;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    /* access modifiers changed from: protected */
    public void ensureLayoutStateExpose() {
        if (this.mLayoutState == null) {
            this.mLayoutState = new LayoutState();
        }
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelperEx.createOrientationHelper(this, getOrientation());
        }
        try {
            this.mEnsureLayoutStateMethod.invoke(this, this.emptyArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    public void scrollToPosition(int i) {
        this.mCurrentPendingScrollPosition = i;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        if (this.mCurrentPendingSavedState != null) {
            this.mCurrentPendingSavedState.putInt("AnchorPosition", -1);
        }
        requestLayout();
    }

    public void scrollToPositionWithOffset(int i, int i2) {
        this.mCurrentPendingScrollPosition = i;
        this.mPendingScrollPositionOffset = i2;
        if (this.mCurrentPendingSavedState != null) {
            this.mCurrentPendingSavedState.putInt("AnchorPosition", -1);
        }
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void updateLayoutStateExpose(int i, int i2, boolean z, RecyclerView.State state) {
        int i3;
        this.mLayoutState.mExtra = getExtraLayoutSpace(state);
        this.mLayoutState.mLayoutDirection = i;
        int i4 = -1;
        if (i == 1) {
            this.mLayoutState.mExtra += this.mOrientationHelper.getEndPadding();
            View childClosestToEndExpose = getChildClosestToEndExpose();
            LayoutState layoutState = this.mLayoutState;
            if (!this.mShouldReverseLayoutExpose) {
                i4 = 1;
            }
            layoutState.mItemDirection = i4;
            this.mLayoutState.mCurrentPosition = getPosition(childClosestToEndExpose) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(childClosestToEndExpose) + computeAlignOffset(childClosestToEndExpose, true, false);
            i3 = this.mLayoutState.mOffset - this.mOrientationHelper.getEndAfterPadding();
        } else {
            View childClosestToStartExpose = getChildClosestToStartExpose();
            this.mLayoutState.mExtra += this.mOrientationHelper.getStartAfterPadding();
            LayoutState layoutState2 = this.mLayoutState;
            if (this.mShouldReverseLayoutExpose) {
                i4 = 1;
            }
            layoutState2.mItemDirection = i4;
            this.mLayoutState.mCurrentPosition = getPosition(childClosestToStartExpose) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStartExpose) + computeAlignOffset(childClosestToStartExpose, false, false);
            i3 = (-this.mLayoutState.mOffset) + this.mOrientationHelper.getStartAfterPadding();
        }
        this.mLayoutState.mAvailable = i2;
        if (z) {
            this.mLayoutState.mAvailable -= i3;
        }
        this.mLayoutState.mScrollingOffset = i3;
    }

    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == 1) {
            return 0;
        }
        return scrollInternalBy(i, recycler, state);
    }

    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == 0) {
            return 0;
        }
        return scrollInternalBy(i, recycler, state);
    }

    /* access modifiers changed from: protected */
    public int scrollInternalBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || i == 0) {
            return 0;
        }
        this.mLayoutState.mRecycle = true;
        ensureLayoutStateExpose();
        int i2 = i > 0 ? 1 : -1;
        int abs = Math.abs(i);
        updateLayoutStateExpose(i2, abs, true, state);
        int i3 = this.mLayoutState.mScrollingOffset;
        this.mLayoutState.mOnRefresLayout = false;
        int fill = i3 + fill(recycler, this.mLayoutState, state, false);
        if (fill < 0) {
            return 0;
        }
        if (abs > fill) {
            i = i2 * fill;
        }
        this.mOrientationHelper.offsetChildren(-i);
        return i;
    }

    public void assertNotInLayoutOrScroll(String str) {
        if (this.mCurrentPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(str);
        }
    }

    /* access modifiers changed from: protected */
    public void recycleChildren(RecyclerView.Recycler recycler, int i, int i2) {
        if (i != i2) {
            if (i2 > i) {
                for (int i3 = i2 - 1; i3 >= i; i3--) {
                    removeAndRecycleViewAt(i3, recycler);
                }
                return;
            }
            while (i > i2) {
                removeAndRecycleViewAt(i, recycler);
                i--;
            }
        }
    }

    private void recycleViewsFromStartExpose(RecyclerView.Recycler recycler, int i) {
        if (i >= 0) {
            int childCount = getChildCount();
            if (this.mShouldReverseLayoutExpose) {
                int i2 = childCount - 1;
                for (int i3 = i2; i3 >= 0; i3--) {
                    if (this.mOrientationHelper.getDecoratedEnd(getChildAt(i3)) + this.recycleOffset > i) {
                        recycleChildren(recycler, i2, i3);
                        return;
                    }
                }
                return;
            }
            for (int i4 = 0; i4 < childCount; i4++) {
                if (this.mOrientationHelper.getDecoratedEnd(getChildAt(i4)) + this.recycleOffset > i) {
                    recycleChildren(recycler, 0, i4);
                    return;
                }
            }
        }
    }

    private void recycleViewsFromEndExpose(RecyclerView.Recycler recycler, int i) {
        int childCount = getChildCount();
        if (i >= 0) {
            int end = this.mOrientationHelper.getEnd() - i;
            if (this.mShouldReverseLayoutExpose) {
                for (int i2 = 0; i2 < childCount; i2++) {
                    if (this.mOrientationHelper.getDecoratedStart(getChildAt(i2)) - this.recycleOffset < end) {
                        recycleChildren(recycler, 0, i2);
                        return;
                    }
                }
                return;
            }
            int i3 = childCount - 1;
            for (int i4 = i3; i4 >= 0; i4--) {
                if (this.mOrientationHelper.getDecoratedStart(getChildAt(i4)) - this.recycleOffset < end) {
                    recycleChildren(recycler, i3, i4);
                    return;
                }
            }
        }
    }

    private void recycleByLayoutStateExpose(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (layoutState.mRecycle) {
            if (layoutState.mLayoutDirection == -1) {
                recycleViewsFromEndExpose(recycler, layoutState.mScrollingOffset);
            } else {
                recycleViewsFromStartExpose(recycler, layoutState.mScrollingOffset);
            }
        }
    }

    /* access modifiers changed from: protected */
    public int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, boolean z) {
        int i = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            recycleByLayoutStateExpose(recycler, layoutState);
        }
        int i2 = layoutState.mAvailable + layoutState.mExtra + this.recycleOffset;
        while (i2 > 0 && layoutState.hasMore(state)) {
            this.layoutChunkResultCache.resetInternal();
            layoutChunk(recycler, state, layoutState, this.layoutChunkResultCache);
            if (!this.layoutChunkResultCache.mFinished) {
                layoutState.mOffset += this.layoutChunkResultCache.mConsumed * layoutState.mLayoutDirection;
                if (!this.layoutChunkResultCache.mIgnoreConsumed || this.mLayoutState.mScrapList != null || !state.isPreLayout()) {
                    layoutState.mAvailable -= this.layoutChunkResultCache.mConsumed;
                    i2 -= this.layoutChunkResultCache.mConsumed;
                }
                if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
                    layoutState.mScrollingOffset += this.layoutChunkResultCache.mConsumed;
                    if (layoutState.mAvailable < 0) {
                        layoutState.mScrollingOffset += layoutState.mAvailable;
                    }
                    recycleByLayoutStateExpose(recycler, layoutState);
                }
                if (z && this.layoutChunkResultCache.mFocusable) {
                    break;
                }
            } else {
                break;
            }
        }
        return i - layoutState.mAvailable;
    }

    /* access modifiers changed from: protected */
    public void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        View next = layoutState.next(recycler);
        if (next == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) next.getLayoutParams();
        if (layoutState.mScrapList == null) {
            if (this.mShouldReverseLayoutExpose == (layoutState.mLayoutDirection == -1)) {
                addView(next);
            } else {
                addView(next, 0);
            }
        } else {
            if (this.mShouldReverseLayoutExpose == (layoutState.mLayoutDirection == -1)) {
                addDisappearingView(next);
            } else {
                addDisappearingView(next, 0);
            }
        }
        measureChildWithMargins(next, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(next);
        if (getOrientation() == 1) {
            if (isLayoutRTL()) {
                i7 = getWidth() - getPaddingRight();
                i2 = i7 - this.mOrientationHelper.getDecoratedMeasurementInOther(next);
            } else {
                i2 = getPaddingLeft();
                i7 = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + i2;
            }
            if (layoutState.mLayoutDirection == -1) {
                i3 = layoutState.mOffset;
                int i8 = i7;
                i4 = layoutState.mOffset - layoutChunkResult.mConsumed;
                i = i8;
            } else {
                int i9 = layoutState.mOffset;
                i3 = layoutState.mOffset + layoutChunkResult.mConsumed;
                i = i7;
                i4 = i9;
            }
        } else {
            i4 = getPaddingTop();
            int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + i4;
            if (layoutState.mLayoutDirection == -1) {
                int i10 = layoutState.mOffset;
                i5 = decoratedMeasurementInOther;
                i6 = layoutState.mOffset - layoutChunkResult.mConsumed;
                i = i10;
            } else {
                int i11 = layoutState.mOffset;
                i = layoutState.mOffset + layoutChunkResult.mConsumed;
                i5 = decoratedMeasurementInOther;
                i6 = i11;
            }
            i3 = i5;
        }
        layoutDecorated(next, i2 + layoutParams.leftMargin, layoutParams.topMargin + i4, i - layoutParams.rightMargin, i3 - layoutParams.bottomMargin);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = next.isFocusable();
    }

    private int convertFocusDirectionToLayoutDirectionExpose(int i) {
        int orientation = getOrientation();
        if (i == 17) {
            return orientation == 0 ? -1 : Integer.MIN_VALUE;
        }
        if (i == 33) {
            return orientation == 1 ? -1 : Integer.MIN_VALUE;
        }
        if (i == 66) {
            return orientation == 0 ? 1 : Integer.MIN_VALUE;
        }
        if (i == 130) {
            return orientation == 1 ? 1 : Integer.MIN_VALUE;
        }
        switch (i) {
            case 1:
                return -1;
            case 2:
                return 1;
            default:
                return Integer.MIN_VALUE;
        }
    }

    private View getChildClosestToStartExpose() {
        return getChildAt(this.mShouldReverseLayoutExpose ? getChildCount() - 1 : 0);
    }

    private View getChildClosestToEndExpose() {
        return getChildAt(this.mShouldReverseLayoutExpose ? 0 : getChildCount() - 1);
    }

    public View onFocusSearchFailed(View view, int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int convertFocusDirectionToLayoutDirectionExpose;
        View view2;
        View view3;
        myResolveShouldLayoutReverse();
        if (getChildCount() == 0 || (convertFocusDirectionToLayoutDirectionExpose = convertFocusDirectionToLayoutDirectionExpose(i)) == Integer.MIN_VALUE) {
            return null;
        }
        if (convertFocusDirectionToLayoutDirectionExpose == -1) {
            view2 = myFindReferenceChildClosestToStart(state);
        } else {
            view2 = myFindReferenceChildClosestToEnd(state);
        }
        if (view2 == null) {
            return null;
        }
        ensureLayoutStateExpose();
        updateLayoutStateExpose(convertFocusDirectionToLayoutDirectionExpose, (int) (((float) this.mOrientationHelper.getTotalSpace()) * MAX_SCROLL_FACTOR), false, state);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        this.mLayoutState.mOnRefresLayout = false;
        fill(recycler, this.mLayoutState, state, true);
        if (convertFocusDirectionToLayoutDirectionExpose == -1) {
            view3 = getChildClosestToStartExpose();
        } else {
            view3 = getChildClosestToEndExpose();
        }
        if (view3 == view2 || !view3.isFocusable()) {
            return null;
        }
        return view3;
    }

    private void logChildren() {
        Log.d(TAG, "internal representation of views on the screen");
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Log.d(TAG, "item " + getPosition(childAt) + ", coord:" + this.mOrientationHelper.getDecoratedStart(childAt));
        }
        Log.d(TAG, "==============");
    }

    private void validateChildOrderExpose() {
        Log.d(TAG, "validating child count " + getChildCount());
        if (getChildCount() >= 1) {
            boolean z = false;
            int position = getPosition(getChildAt(0));
            int decoratedStart = this.mOrientationHelper.getDecoratedStart(getChildAt(0));
            if (this.mShouldReverseLayoutExpose) {
                int i = 1;
                while (i < getChildCount()) {
                    View childAt = getChildAt(i);
                    int position2 = getPosition(childAt);
                    int decoratedStart2 = this.mOrientationHelper.getDecoratedStart(childAt);
                    if (position2 < position) {
                        logChildren();
                        StringBuilder sb = new StringBuilder();
                        sb.append("detected invalid position. loc invalid? ");
                        if (decoratedStart2 < decoratedStart) {
                            z = true;
                        }
                        sb.append(z);
                        throw new RuntimeException(sb.toString());
                    } else if (decoratedStart2 <= decoratedStart) {
                        i++;
                    } else {
                        logChildren();
                        throw new RuntimeException("detected invalid location");
                    }
                }
                return;
            }
            int i2 = 1;
            while (i2 < getChildCount()) {
                View childAt2 = getChildAt(i2);
                int position3 = getPosition(childAt2);
                int decoratedStart3 = this.mOrientationHelper.getDecoratedStart(childAt2);
                if (position3 < position) {
                    logChildren();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("detected invalid position. loc invalid? ");
                    if (decoratedStart3 < decoratedStart) {
                        z = true;
                    }
                    sb2.append(z);
                    throw new RuntimeException(sb2.toString());
                } else if (decoratedStart3 >= decoratedStart) {
                    i2++;
                } else {
                    logChildren();
                    throw new RuntimeException("detected invalid location");
                }
            }
        }
    }

    public boolean supportsPredictiveItemAnimations() {
        return this.mCurrentPendingSavedState == null && this.mLastStackFromEnd == getStackFromEnd();
    }

    /* access modifiers changed from: protected */
    public void addHiddenView(View view, boolean z) {
        addView(view, z ? 0 : -1);
        this.mChildHelperWrapper.hide(view);
    }

    /* access modifiers changed from: protected */
    public void hideView(View view) {
        this.mChildHelperWrapper.hide(view);
    }

    /* access modifiers changed from: protected */
    public void showView(View view) {
        this.mChildHelperWrapper.show(view);
    }

    /* access modifiers changed from: protected */
    public View findHiddenView(int i) {
        return this.mChildHelperWrapper.findHiddenNonRemovedView(i, -1);
    }

    /* access modifiers changed from: protected */
    public boolean isHidden(View view) {
        return this.mChildHelperWrapper.isHidden(view);
    }

    protected static boolean isViewHolderUpdated(RecyclerView.ViewHolder viewHolder) {
        return new ViewHolderWrapper(viewHolder).requireUpdated();
    }

    protected static void attachViewHolder(RecyclerView.LayoutParams layoutParams, RecyclerView.ViewHolder viewHolder) {
        try {
            if (vhField == null) {
                vhField = RecyclerView.LayoutParams.class.getDeclaredField("mViewHolder");
            }
            vhField.setAccessible(true);
            vhField.set(layoutParams, viewHolder);
            if (vhSetFlags == null) {
                vhSetFlags = RecyclerView.ViewHolder.class.getDeclaredMethod("setFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                vhSetFlags.setAccessible(true);
            }
            vhSetFlags.invoke(viewHolder, new Object[]{4, 4});
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    public static class LayoutState {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        public static final int ITEM_DIRECTION_HEAD = -1;
        public static final int ITEM_DIRECTION_TAIL = 1;
        public static final int LAYOUT_END = 1;
        public static final int LAYOUT_START = -1;
        static final int SCOLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "_ExposeLLayoutManager#LayoutState";
        public int mAvailable;
        public int mCurrentPosition;
        public int mExtra = 0;
        public int mFixOffset = 0;
        public boolean mIsPreLayout = false;
        public int mItemDirection;
        public int mLayoutDirection;
        public int mOffset;
        public boolean mOnRefresLayout = false;
        public boolean mRecycle = true;
        public List<RecyclerView.ViewHolder> mScrapList = null;
        public int mScrollingOffset;
        private Method vhIsRemoved = null;

        public LayoutState() {
            try {
                this.vhIsRemoved = RecyclerView.ViewHolder.class.getDeclaredMethod("isRemoved", new Class[0]);
                this.vhIsRemoved.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public boolean hasMore(RecyclerView.State state) {
            return this.mCurrentPosition >= 0 && this.mCurrentPosition < state.getItemCount();
        }

        public View next(RecyclerView.Recycler recycler) {
            if (this.mScrapList != null) {
                return nextFromLimitedList();
            }
            View viewForPosition = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return viewForPosition;
        }

        @SuppressLint({"LongLogTag"})
        private View nextFromLimitedList() {
            boolean z;
            int size = this.mScrapList.size();
            RecyclerView.ViewHolder viewHolder = null;
            int i = 0;
            int i2 = Integer.MAX_VALUE;
            while (true) {
                if (i >= size) {
                    break;
                }
                RecyclerView.ViewHolder viewHolder2 = this.mScrapList.get(i);
                if (!this.mIsPreLayout) {
                    try {
                        z = ((Boolean) this.vhIsRemoved.invoke(viewHolder2, new Object[0])).booleanValue();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        z = false;
                        i++;
                    } catch (InvocationTargetException e2) {
                        e2.printStackTrace();
                        z = false;
                        i++;
                    }
                    if (!this.mIsPreLayout && z) {
                        i++;
                    }
                }
                int position = (viewHolder2.getPosition() - this.mCurrentPosition) * this.mItemDirection;
                if (position >= 0 && position < i2) {
                    if (position == 0) {
                        viewHolder = viewHolder2;
                        break;
                    }
                    viewHolder = viewHolder2;
                    i2 = position;
                }
                i++;
            }
            if (viewHolder == null) {
                return null;
            }
            this.mCurrentPosition = viewHolder.getPosition() + this.mItemDirection;
            return viewHolder.itemView;
        }

        /* access modifiers changed from: package-private */
        @SuppressLint({"LongLogTag"})
        public void log() {
            Log.d(TAG, "avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection);
        }
    }

    protected class AnchorInfo {
        public int mCoordinate;
        public boolean mLayoutFromEnd;
        public int mPosition;

        protected AnchorInfo() {
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
        }

        /* access modifiers changed from: package-private */
        public void assignCoordinateFromPadding() {
            int i;
            if (this.mLayoutFromEnd) {
                i = ExposeLinearLayoutManagerEx.this.mOrientationHelper.getEndAfterPadding();
            } else {
                i = ExposeLinearLayoutManagerEx.this.mOrientationHelper.getStartAfterPadding();
            }
            this.mCoordinate = i;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + '}';
        }

        public boolean assignFromViewIfValid(View view, RecyclerView.State state) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            if (layoutParams.isItemRemoved() || layoutParams.getViewPosition() < 0 || layoutParams.getViewPosition() >= state.getItemCount()) {
                return false;
            }
            assignFromView(view);
            return true;
        }

        public void assignFromView(View view) {
            if (this.mLayoutFromEnd) {
                this.mCoordinate = ExposeLinearLayoutManagerEx.this.mOrientationHelper.getDecoratedEnd(view) + ExposeLinearLayoutManagerEx.this.computeAlignOffset(view, this.mLayoutFromEnd, true) + ExposeLinearLayoutManagerEx.this.mOrientationHelper.getTotalSpaceChange();
            } else {
                this.mCoordinate = ExposeLinearLayoutManagerEx.this.mOrientationHelper.getDecoratedStart(view) + ExposeLinearLayoutManagerEx.this.computeAlignOffset(view, this.mLayoutFromEnd, true);
            }
            this.mPosition = ExposeLinearLayoutManagerEx.this.getPosition(view);
        }
    }

    static class ViewHolderWrapper {
        private static Method mIsChanged;
        private static Method mIsInvalid;
        private static Method mIsRemoved;
        private static Method mSetFlags;
        private static Method mShouldIgnore;
        private RecyclerView.ViewHolder mHolder;

        static {
            try {
                mShouldIgnore = RecyclerView.ViewHolder.class.getDeclaredMethod("shouldIgnore", new Class[0]);
                mShouldIgnore.setAccessible(true);
                mIsInvalid = RecyclerView.ViewHolder.class.getDeclaredMethod("isInvalid", new Class[0]);
                mIsInvalid.setAccessible(true);
                mIsRemoved = RecyclerView.ViewHolder.class.getDeclaredMethod("isRemoved", new Class[0]);
                mIsRemoved.setAccessible(true);
                mSetFlags = RecyclerView.ViewHolder.class.getDeclaredMethod("setFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                mSetFlags.setAccessible(true);
                try {
                    mIsChanged = RecyclerView.ViewHolder.class.getDeclaredMethod("isChanged", new Class[0]);
                } catch (NoSuchMethodException unused) {
                    mIsChanged = RecyclerView.ViewHolder.class.getDeclaredMethod("isUpdated", new Class[0]);
                }
                mIsChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public static void setFlags(RecyclerView.ViewHolder viewHolder, int i, int i2) {
            try {
                mSetFlags.invoke(viewHolder, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
        }

        public ViewHolderWrapper(RecyclerView.ViewHolder viewHolder) {
            this.mHolder = viewHolder;
        }

        /* access modifiers changed from: package-private */
        public boolean isInvalid() {
            if (mIsInvalid == null) {
                return true;
            }
            try {
                return ((Boolean) mIsInvalid.invoke(this.mHolder, new Object[0])).booleanValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return true;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isRemoved() {
            if (mIsRemoved == null) {
                return true;
            }
            try {
                return ((Boolean) mIsRemoved.invoke(this.mHolder, new Object[0])).booleanValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return true;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isChanged() {
            if (mIsChanged == null) {
                return true;
            }
            try {
                return ((Boolean) mIsChanged.invoke(this.mHolder, new Object[0])).booleanValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return true;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public void setFlags(int i, int i2) {
            try {
                mSetFlags.invoke(this.mHolder, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
        }

        public boolean requireUpdated() {
            return isInvalid() || isRemoved() || isChanged();
        }
    }

    class ChildHelperWrapper {
        private Object[] args = new Object[1];
        private Field mChildHelperField;
        private Method mClearMethod;
        private Method mFindHiddenNonRemovedViewMethod;
        private Method mFindHiddenNonRemovedViewMethod25;
        private Field mHiddenViewField;
        private Method mHideMethod;
        private Object mInnerBucket;
        private Object mInnerChildHelper;
        private List mInnerHiddenView;
        private Method mIsHideMethod;
        private RecyclerView.LayoutManager mLayoutManager;

        /* access modifiers changed from: package-private */
        public void ensureChildHelper() {
            try {
                if (this.mInnerChildHelper == null) {
                    this.mInnerChildHelper = this.mChildHelperField.get(this.mLayoutManager);
                    if (this.mInnerChildHelper != null) {
                        Class<?> cls = this.mInnerChildHelper.getClass();
                        this.mHideMethod = cls.getDeclaredMethod("hide", new Class[]{View.class});
                        this.mHideMethod.setAccessible(true);
                        try {
                            this.mFindHiddenNonRemovedViewMethod = cls.getDeclaredMethod("findHiddenNonRemovedView", new Class[]{Integer.TYPE, Integer.TYPE});
                            this.mFindHiddenNonRemovedViewMethod.setAccessible(true);
                        } catch (NoSuchMethodException unused) {
                            this.mFindHiddenNonRemovedViewMethod25 = cls.getDeclaredMethod("findHiddenNonRemovedView", new Class[]{Integer.TYPE});
                            this.mFindHiddenNonRemovedViewMethod25.setAccessible(true);
                        }
                        this.mIsHideMethod = cls.getDeclaredMethod("isHidden", new Class[]{View.class});
                        this.mIsHideMethod.setAccessible(true);
                        Field declaredField = cls.getDeclaredField("mBucket");
                        declaredField.setAccessible(true);
                        this.mInnerBucket = declaredField.get(this.mInnerChildHelper);
                        this.mClearMethod = this.mInnerBucket.getClass().getDeclaredMethod("clear", new Class[]{Integer.TYPE});
                        this.mClearMethod.setAccessible(true);
                        this.mHiddenViewField = cls.getDeclaredField("mHiddenViews");
                        this.mHiddenViewField.setAccessible(true);
                        this.mInnerHiddenView = (List) this.mHiddenViewField.get(this.mInnerChildHelper);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ChildHelperWrapper(RecyclerView.LayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            try {
                this.mChildHelperField = RecyclerView.LayoutManager.class.getDeclaredField("mChildHelper");
                this.mChildHelperField.setAccessible(true);
                ensureChildHelper();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: package-private */
        public void hide(View view) {
            try {
                ensureChildHelper();
                if (this.mInnerHiddenView.indexOf(view) < 0) {
                    this.args[0] = view;
                    this.mHideMethod.invoke(this.mInnerChildHelper, this.args);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: package-private */
        public void show(View view) {
            try {
                ensureChildHelper();
                this.args[0] = Integer.valueOf(ExposeLinearLayoutManagerEx.this.mRecyclerView.indexOfChild(view));
                this.mClearMethod.invoke(this.mInnerBucket, this.args);
                if (this.mInnerHiddenView != null) {
                    this.mInnerHiddenView.remove(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* access modifiers changed from: package-private */
        public View findHiddenNonRemovedView(int i, int i2) {
            try {
                ensureChildHelper();
                if (this.mFindHiddenNonRemovedViewMethod != null) {
                    return (View) this.mFindHiddenNonRemovedViewMethod.invoke(this.mInnerChildHelper, new Object[]{Integer.valueOf(i), -1});
                } else if (this.mFindHiddenNonRemovedViewMethod25 == null) {
                    return null;
                } else {
                    return (View) this.mFindHiddenNonRemovedViewMethod25.invoke(this.mInnerChildHelper, new Object[]{Integer.valueOf(i)});
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return null;
            } catch (Exception e3) {
                e3.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isHidden(View view) {
            try {
                ensureChildHelper();
                this.args[0] = view;
                return ((Boolean) this.mIsHideMethod.invoke(this.mInnerChildHelper, this.args)).booleanValue();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
                return false;
            } catch (Exception e3) {
                e3.printStackTrace();
                return false;
            }
        }
    }
}
