package com.taobao.android.dxcontainer.vlayout;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcelable;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dxcontainer.vlayout.ExposeLinearLayoutManagerEx;
import com.taobao.android.dxcontainer.vlayout.extend.LayoutManagerCanScrollListener;
import com.taobao.android.dxcontainer.vlayout.extend.PerformanceMonitor;
import com.taobao.android.dxcontainer.vlayout.extend.ViewLifeCycleHelper;
import com.taobao.android.dxcontainer.vlayout.extend.ViewLifeCycleListener;
import com.taobao.android.dxcontainer.vlayout.layout.BaseLayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.DefaultLayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.FixAreaAdjuster;
import com.taobao.android.dxcontainer.vlayout.layout.FixAreaLayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.LayoutChunkResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VirtualLayoutManager extends ExposeLinearLayoutManagerEx implements LayoutManagerHelper {
    private static LayoutHelper DEFAULT_LAYOUT_HELPER = new DefaultLayoutHelper();
    public static final int HORIZONTAL = 0;
    private static final int MAX_NO_SCROLLING_SIZE = 134217727;
    private static final String PHASE_LAYOUT = "layout";
    private static final String PHASE_MEASURE = "measure";
    protected static final String TAG = "VirtualLayoutManager";
    private static final String TRACE_LAYOUT = "VLM onLayoutChildren";
    private static final String TRACE_SCROLL = "VLM scroll";
    public static final int VERTICAL = 1;
    public static boolean sDebuggable = false;
    private LayoutManagerCanScrollListener layoutManagerCanScrollListener;
    private boolean mCanScrollHorizontally;
    private boolean mCanScrollVertically;
    private Rect mDecorInsets;
    private LayoutHelper mDefaultLayoutHelper;
    private boolean mEnableMarginOverlapping;
    private FixAreaAdjuster mFixAreaAdjustor;
    private LayoutHelperFinder mHelperFinder;
    private BaseLayoutHelper.LayoutViewBindListener mLayoutViewBindListener;
    private LayoutViewFactory mLayoutViewFatory;
    private int mMaxMeasureSize;
    private int mMeasuredFullSpace;
    private int mNested;
    private boolean mNestedScrolling;
    private boolean mNoScrolling;
    protected OrientationHelperEx mOrientationHelper;
    private PerformanceMonitor mPerformanceMonitor;
    private Comparator<Pair<Range<Integer>, Integer>> mRangeComparator;
    private List<Pair<Range<Integer>, Integer>> mRangeLengths;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    protected OrientationHelperEx mSecondaryOrientationHelper;
    private boolean mSpaceMeasured;
    private boolean mSpaceMeasuring;
    private AnchorInfoWrapper mTempAnchorInfoWrapper;
    private LayoutStateWrapper mTempLayoutStateWrapper;
    private ViewLifeCycleHelper mViewLifeCycleHelper;
    private HashMap<Integer, LayoutHelper> newHelpersSet;
    private HashMap<Integer, LayoutHelper> oldHelpersSet;

    public interface CacheViewHolder {
        boolean needCached();
    }

    public /* bridge */ /* synthetic */ void assertNotInLayoutOrScroll(String str) {
        super.assertNotInLayoutOrScroll(str);
    }

    public /* bridge */ /* synthetic */ PointF computeScrollVectorForPosition(int i) {
        return super.computeScrollVectorForPosition(i);
    }

    public /* bridge */ /* synthetic */ int findFirstVisibleItemPosition() {
        return super.findFirstVisibleItemPosition();
    }

    public /* bridge */ /* synthetic */ int findLastVisibleItemPosition() {
        return super.findLastVisibleItemPosition();
    }

    public /* bridge */ /* synthetic */ View onFocusSearchFailed(View view, int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.onFocusSearchFailed(view, i, recycler, state);
    }

    public /* bridge */ /* synthetic */ void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
    }

    public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    public /* bridge */ /* synthetic */ int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    public /* bridge */ /* synthetic */ int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(i, recycler, state);
    }

    public /* bridge */ /* synthetic */ void setRecycleOffset(int i) {
        super.setRecycleOffset(i);
    }

    public static void enableDebugging(boolean z) {
        sDebuggable = z;
    }

    public VirtualLayoutManager(@NonNull Context context) {
        this(context, 1);
    }

    public VirtualLayoutManager(@NonNull Context context, int i) {
        this(context, i, false);
    }

    public VirtualLayoutManager(@NonNull Context context, int i, boolean z) {
        super(context, i, z);
        int i2 = 0;
        this.mNoScrolling = false;
        this.mNestedScrolling = false;
        this.mEnableMarginOverlapping = false;
        this.mMaxMeasureSize = -1;
        this.mRangeComparator = new Comparator<Pair<Range<Integer>, Integer>>() {
            public int compare(Pair<Range<Integer>, Integer> pair, Pair<Range<Integer>, Integer> pair2) {
                if (pair == null && pair2 == null) {
                    return 0;
                }
                if (pair == null) {
                    return -1;
                }
                if (pair2 == null) {
                    return 1;
                }
                return ((Integer) ((Range) pair.first).getLower()).intValue() - ((Integer) ((Range) pair2.first).getLower()).intValue();
            }
        };
        this.mFixAreaAdjustor = FixAreaAdjuster.mDefaultAdjuster;
        this.newHelpersSet = new HashMap<>();
        this.oldHelpersSet = new HashMap<>();
        this.mTempAnchorInfoWrapper = new AnchorInfoWrapper();
        this.mNested = 0;
        this.mTempLayoutStateWrapper = new LayoutStateWrapper();
        this.mRangeLengths = new ArrayList();
        this.mDefaultLayoutHelper = DEFAULT_LAYOUT_HELPER;
        this.mLayoutViewFatory = new LayoutViewFactory() {
            public View generateLayoutView(@NonNull Context context) {
                return new LayoutView(context);
            }
        };
        this.mDecorInsets = new Rect();
        this.mSpaceMeasured = false;
        this.mMeasuredFullSpace = 0;
        this.mSpaceMeasuring = false;
        this.mOrientationHelper = OrientationHelperEx.createOrientationHelper(this, i);
        this.mSecondaryOrientationHelper = OrientationHelperEx.createOrientationHelper(this, i != 1 ? 1 : i2);
        this.mCanScrollVertically = super.canScrollVertically();
        this.mCanScrollHorizontally = super.canScrollHorizontally();
        setHelperFinder(new RangeLayoutHelperFinder());
    }

    public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
        this.mPerformanceMonitor = performanceMonitor;
    }

    public void setNoScrolling(boolean z) {
        this.mNoScrolling = z;
        this.mSpaceMeasured = false;
        this.mMeasuredFullSpace = 0;
        this.mSpaceMeasuring = false;
    }

    public void setCanScrollVertically(boolean z) {
        this.mCanScrollVertically = z;
    }

    public void setCanScrollHorizontally(boolean z) {
        this.mCanScrollHorizontally = z;
    }

    public void setLayoutManagerCanScrollListener(LayoutManagerCanScrollListener layoutManagerCanScrollListener2) {
        this.layoutManagerCanScrollListener = layoutManagerCanScrollListener2;
    }

    public void setNestedScrolling(boolean z) {
        setNestedScrolling(z, -1);
    }

    public void setNestedScrolling(boolean z, int i) {
        this.mNestedScrolling = z;
        this.mSpaceMeasured = false;
        this.mSpaceMeasuring = false;
        this.mMeasuredFullSpace = 0;
    }

    public void setHelperFinder(@NonNull LayoutHelperFinder layoutHelperFinder) {
        if (layoutHelperFinder != null) {
            LinkedList linkedList = new LinkedList();
            if (this.mHelperFinder != null) {
                for (LayoutHelper add : this.mHelperFinder.getLayoutHelpers()) {
                    linkedList.add(add);
                }
            }
            this.mHelperFinder = layoutHelperFinder;
            if (linkedList.size() > 0) {
                this.mHelperFinder.setLayouts(linkedList);
            }
            this.mSpaceMeasured = false;
            return;
        }
        throw new IllegalArgumentException("finder is null");
    }

    public void setFixOffset(int i, int i2, int i3, int i4) {
        this.mFixAreaAdjustor = new FixAreaAdjuster(i, i2, i3, i4);
    }

    public void setLayoutHelpers(@Nullable List<LayoutHelper> list) {
        for (LayoutHelper next : this.mHelperFinder.getLayoutHelpers()) {
            this.oldHelpersSet.put(Integer.valueOf(System.identityHashCode(next)), next);
        }
        if (list != null) {
            int i = 0;
            for (LayoutHelper next2 : list) {
                if (next2 instanceof FixAreaLayoutHelper) {
                    ((FixAreaLayoutHelper) next2).setAdjuster(this.mFixAreaAdjustor);
                }
                if ((next2 instanceof BaseLayoutHelper) && this.mLayoutViewBindListener != null) {
                    ((BaseLayoutHelper) next2).setLayoutViewBindListener(this.mLayoutViewBindListener);
                }
                if (next2.getItemCount() > 0) {
                    next2.setRange(i, (next2.getItemCount() + i) - 1);
                } else {
                    next2.setRange(-1, -1);
                }
                i += next2.getItemCount();
            }
        }
        this.mHelperFinder.setLayouts(list);
        for (LayoutHelper next3 : this.mHelperFinder.getLayoutHelpers()) {
            this.newHelpersSet.put(Integer.valueOf(System.identityHashCode(next3)), next3);
        }
        Iterator<Map.Entry<Integer, LayoutHelper>> it = this.oldHelpersSet.entrySet().iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next().getKey();
            if (this.newHelpersSet.containsKey(num)) {
                this.newHelpersSet.remove(num);
                it.remove();
            }
        }
        for (LayoutHelper clear : this.oldHelpersSet.values()) {
            clear.clear(this);
        }
        if (!this.oldHelpersSet.isEmpty() || !this.newHelpersSet.isEmpty()) {
            this.mSpaceMeasured = false;
        }
        this.oldHelpersSet.clear();
        this.newHelpersSet.clear();
        requestLayout();
    }

    @NonNull
    public List<LayoutHelper> getLayoutHelpers() {
        return this.mHelperFinder.getLayoutHelpers();
    }

    public void setEnableMarginOverlapping(boolean z) {
        this.mEnableMarginOverlapping = z;
    }

    public boolean isEnableMarginOverLap() {
        return this.mEnableMarginOverlapping;
    }

    public int getOrientation() {
        return super.getOrientation();
    }

    public void setOrientation(int i) {
        this.mOrientationHelper = OrientationHelperEx.createOrientationHelper(this, i);
        super.setOrientation(i);
    }

    public void setReverseLayout(boolean z) {
        if (!z) {
            super.setReverseLayout(false);
            return;
        }
        throw new UnsupportedOperationException("VirtualLayoutManager does not support reverse layout in current version.");
    }

    public void setStackFromEnd(boolean z) {
        if (!z) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("VirtualLayoutManager does not support stack from end.");
    }

    public void onAnchorReady(RecyclerView.State state, ExposeLinearLayoutManagerEx.AnchorInfo anchorInfo) {
        super.onAnchorReady(state, anchorInfo);
        boolean z = true;
        while (z) {
            this.mTempAnchorInfoWrapper.position = anchorInfo.mPosition;
            this.mTempAnchorInfoWrapper.coordinate = anchorInfo.mCoordinate;
            this.mTempAnchorInfoWrapper.layoutFromEnd = anchorInfo.mLayoutFromEnd;
            LayoutHelper layoutHelper = this.mHelperFinder.getLayoutHelper(anchorInfo.mPosition);
            if (layoutHelper != null) {
                layoutHelper.checkAnchorInfo(state, this.mTempAnchorInfoWrapper, this);
            }
            if (this.mTempAnchorInfoWrapper.position == anchorInfo.mPosition) {
                z = false;
            } else {
                anchorInfo.mPosition = this.mTempAnchorInfoWrapper.position;
            }
            anchorInfo.mCoordinate = this.mTempAnchorInfoWrapper.coordinate;
            this.mTempAnchorInfoWrapper.position = -1;
        }
        this.mTempAnchorInfoWrapper.position = anchorInfo.mPosition;
        this.mTempAnchorInfoWrapper.coordinate = anchorInfo.mCoordinate;
        for (LayoutHelper onRefreshLayout : this.mHelperFinder.getLayoutHelpers()) {
            onRefreshLayout.onRefreshLayout(state, this.mTempAnchorInfoWrapper, this);
        }
    }

    public LayoutHelper findNeighbourNonfixLayoutHelper(LayoutHelper layoutHelper, boolean z) {
        List<LayoutHelper> layoutHelpers;
        int indexOf;
        LayoutHelper layoutHelper2;
        if (layoutHelper == null || (indexOf = layoutHelpers.indexOf(layoutHelper)) == -1) {
            return null;
        }
        int i = z ? indexOf - 1 : indexOf + 1;
        if (i < 0 || i >= (layoutHelpers = this.mHelperFinder.getLayoutHelpers()).size() || (layoutHelper2 = layoutHelpers.get(i)) == null || layoutHelper2.isFixLayout()) {
            return null;
        }
        return layoutHelper2;
    }

    /* access modifiers changed from: protected */
    public int computeAlignOffset(View view, boolean z, boolean z2) {
        return computeAlignOffset(getPosition(view), z, z2);
    }

    /* access modifiers changed from: protected */
    public int computeAlignOffset(int i, boolean z, boolean z2) {
        LayoutHelper layoutHelper;
        if (i == -1 || (layoutHelper = this.mHelperFinder.getLayoutHelper(i)) == null) {
            return 0;
        }
        return layoutHelper.computeAlignOffset(i - layoutHelper.getRange().getLower().intValue(), z, z2, this);
    }

    public int obtainExtraMargin(View view, boolean z) {
        return obtainExtraMargin(view, z, true);
    }

    public int obtainExtraMargin(View view, boolean z, boolean z2) {
        if (view != null) {
            return computeAlignOffset(view, z, z2);
        }
        return 0;
    }

    private void runPreLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mNested == 0) {
            for (LayoutHelper beforeLayout : this.mHelperFinder.reverse()) {
                beforeLayout.beforeLayout(recycler, state, this);
            }
        }
        this.mNested++;
    }

    private void runPostLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        this.mNested--;
        if (this.mNested <= 0) {
            this.mNested = 0;
            int findFirstVisibleItemPosition = findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = findLastVisibleItemPosition();
            for (LayoutHelper afterLayout : this.mHelperFinder.getLayoutHelpers()) {
                try {
                    afterLayout.afterLayout(recycler, state, findFirstVisibleItemPosition, findLastVisibleItemPosition, i, this);
                } catch (Exception e) {
                    if (sDebuggable) {
                        throw e;
                    }
                }
            }
            if (this.mViewLifeCycleHelper != null) {
                this.mViewLifeCycleHelper.checkViewStatusInScreen();
            }
        }
    }

    public void runAdjustLayout() {
        int findFirstVisibleItemPosition = findFirstVisibleItemPosition();
        LayoutHelper layoutHelper = this.mHelperFinder.getLayoutHelper(findFirstVisibleItemPosition);
        int findLastVisibleItemPosition = findLastVisibleItemPosition();
        LayoutHelper layoutHelper2 = this.mHelperFinder.getLayoutHelper(findLastVisibleItemPosition);
        List<LayoutHelper> layoutHelpers = this.mHelperFinder.getLayoutHelpers();
        int indexOf = layoutHelpers.indexOf(layoutHelper2);
        for (int indexOf2 = layoutHelpers.indexOf(layoutHelper); indexOf2 <= indexOf; indexOf2++) {
            try {
                layoutHelpers.get(indexOf2).adjustLayout(findFirstVisibleItemPosition, findLastVisibleItemPosition, this);
            } catch (Exception e) {
                if (sDebuggable) {
                    throw e;
                }
            }
        }
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection(TRACE_LAYOUT);
        }
        if (this.mNoScrolling && state.didStructureChange()) {
            this.mSpaceMeasured = false;
            this.mSpaceMeasuring = true;
        }
        runPreLayout(recycler, state);
        try {
            super.onLayoutChildren(recycler, state);
            runPostLayout(recycler, state, Integer.MAX_VALUE);
            if ((this.mNestedScrolling || this.mNoScrolling) && this.mSpaceMeasuring) {
                this.mSpaceMeasured = true;
                View childAt = getChildAt(getChildCount() - 1);
                if (childAt != null) {
                    this.mMeasuredFullSpace = getDecoratedBottom(childAt) + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin + computeAlignOffset(childAt, true, false);
                    if (this.mRecyclerView != null && this.mNestedScrolling) {
                        ViewParent parent = this.mRecyclerView.getParent();
                        if (parent instanceof View) {
                            this.mMeasuredFullSpace = Math.min(this.mMeasuredFullSpace, ((View) parent).getMeasuredHeight());
                        }
                    }
                } else {
                    this.mSpaceMeasuring = false;
                }
                this.mSpaceMeasuring = false;
                if (this.mRecyclerView != null && getItemCount() > 0) {
                    this.mRecyclerView.post(new Runnable() {
                        public void run() {
                            if (VirtualLayoutManager.this.mRecyclerView != null) {
                                VirtualLayoutManager.this.mRecyclerView.requestLayout();
                            }
                        }
                    });
                }
            }
            if (Build.VERSION.SDK_INT >= 18) {
                Trace.endSection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } catch (Throwable th) {
            runPostLayout(recycler, state, Integer.MAX_VALUE);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public int scrollInternalBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection(TRACE_SCROLL);
        }
        runPreLayout(recycler, state);
        int i2 = 0;
        try {
            if (!this.mNoScrolling) {
                i = super.scrollInternalBy(i, recycler, state);
            } else {
                if (getChildCount() != 0) {
                    if (i != 0) {
                        this.mLayoutState.mRecycle = true;
                        ensureLayoutStateExpose();
                        int i3 = i > 0 ? 1 : -1;
                        int abs = Math.abs(i);
                        updateLayoutStateExpose(i3, abs, true, state);
                        int fill = this.mLayoutState.mScrollingOffset + fill(recycler, this.mLayoutState, state, false);
                        if (fill < 0) {
                            runPostLayout(recycler, state, 0);
                            return 0;
                        } else if (abs > fill) {
                            i = i3 * fill;
                        }
                    }
                }
                runPostLayout(recycler, state, 0);
                return 0;
            }
            i2 = i;
        } catch (Exception e) {
            Log.w(TAG, Log.getStackTraceString(e), e);
            if (sDebuggable) {
                throw e;
            }
        } catch (Throwable th) {
            runPostLayout(recycler, state, 0);
            throw th;
        }
        runPostLayout(recycler, state, i2);
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
        return i2;
    }

    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        int findFirstVisibleItemPosition = findFirstVisibleItemPosition();
        int findLastVisibleItemPosition = findLastVisibleItemPosition();
        for (LayoutHelper onScrollStateChanged : this.mHelperFinder.getLayoutHelpers()) {
            onScrollStateChanged.onScrollStateChanged(i, findFirstVisibleItemPosition, findLastVisibleItemPosition, this);
        }
    }

    public void offsetChildrenHorizontal(int i) {
        super.offsetChildrenHorizontal(i);
        for (LayoutHelper onOffsetChildrenHorizontal : this.mHelperFinder.getLayoutHelpers()) {
            onOffsetChildrenHorizontal.onOffsetChildrenHorizontal(i, this);
        }
    }

    public void offsetChildrenVertical(int i) {
        super.offsetChildrenVertical(i);
        for (LayoutHelper onOffsetChildrenVertical : this.mHelperFinder.getLayoutHelpers()) {
            onOffsetChildrenVertical.onOffsetChildrenVertical(i, this);
        }
        if (this.mViewLifeCycleHelper != null) {
            this.mViewLifeCycleHelper.checkViewStatusInScreen();
        }
    }

    public void setViewLifeCycleListener(@NonNull ViewLifeCycleListener viewLifeCycleListener) {
        if (viewLifeCycleListener != null) {
            this.mViewLifeCycleHelper = new ViewLifeCycleHelper(this, viewLifeCycleListener);
            return;
        }
        throw new IllegalArgumentException("ViewLifeCycleListener should not be null!");
    }

    public int getVirtualLayoutDirection() {
        return this.mLayoutState.mLayoutDirection;
    }

    @Nullable
    private int findRangeLength(@NonNull Range<Integer> range) {
        Pair pair;
        Pair pair2;
        int size = this.mRangeLengths.size();
        if (size == 0) {
            return -1;
        }
        int i = 0;
        int i2 = size - 1;
        int i3 = -1;
        while (true) {
            pair = null;
            if (i > i2) {
                break;
            }
            i3 = (i + i2) / 2;
            pair2 = this.mRangeLengths.get(i3);
            Range range2 = (Range) pair2.first;
            if (range2 == null) {
                break;
            } else if (range2.contains(range.getLower()) || range2.contains(range.getUpper()) || range.contains((Range<Integer>) range2)) {
                pair = pair2;
            } else if (((Integer) range2.getLower()).intValue() > range.getUpper().intValue()) {
                i2 = i3 - 1;
            } else if (((Integer) range2.getUpper()).intValue() < range.getLower().intValue()) {
                i = i3 + 1;
            }
        }
        pair = pair2;
        if (pair == null) {
            return -1;
        }
        return i3;
    }

    /* access modifiers changed from: protected */
    public void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, ExposeLinearLayoutManagerEx.LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int i = layoutState.mCurrentPosition;
        ExposeLinearLayoutManagerEx.LayoutState unused = this.mTempLayoutStateWrapper.mLayoutState = layoutState;
        LayoutHelper layoutHelper = this.mHelperFinder == null ? null : this.mHelperFinder.getLayoutHelper(i);
        if (layoutHelper == null) {
            layoutHelper = this.mDefaultLayoutHelper;
        }
        layoutHelper.doLayout(recycler, state, this.mTempLayoutStateWrapper, layoutChunkResult, this);
        ExposeLinearLayoutManagerEx.LayoutState unused2 = this.mTempLayoutStateWrapper.mLayoutState = null;
        if (layoutState.mCurrentPosition == i) {
            if (sDebuggable) {
                Log.w(TAG, "layoutHelper[" + layoutHelper.getClass().getSimpleName() + DinamicConstant.DINAMIC_PREFIX_AT + layoutHelper.toString() + "] consumes no item!");
            }
            layoutChunkResult.mFinished = true;
            return;
        }
        int i2 = layoutState.mCurrentPosition - layoutState.mItemDirection;
        int i3 = layoutChunkResult.mIgnoreConsumed ? 0 : layoutChunkResult.mConsumed;
        Range range = new Range(Integer.valueOf(Math.min(i, i2)), Integer.valueOf(Math.max(i, i2)));
        int findRangeLength = findRangeLength(range);
        if (findRangeLength >= 0) {
            Pair pair = this.mRangeLengths.get(findRangeLength);
            if (pair == null || !((Range) pair.first).equals(range) || ((Integer) pair.second).intValue() != i3) {
                this.mRangeLengths.remove(findRangeLength);
            } else {
                return;
            }
        }
        this.mRangeLengths.add(Pair.create(range, Integer.valueOf(i3)));
        Collections.sort(this.mRangeLengths, this.mRangeComparator);
    }

    public int getOffsetToStart() {
        if (getChildCount() == 0) {
            return -1;
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return -1;
        }
        int position = getPosition(childAt);
        int findRangeLength = findRangeLength(Range.create(Integer.valueOf(position), Integer.valueOf(position)));
        if (findRangeLength < 0 || findRangeLength >= this.mRangeLengths.size()) {
            return -1;
        }
        int i = -this.mOrientationHelper.getDecoratedStart(childAt);
        for (int i2 = 0; i2 < findRangeLength; i2++) {
            Pair pair = this.mRangeLengths.get(i2);
            if (pair != null) {
                i += ((Integer) pair.second).intValue();
            }
        }
        return i;
    }

    private void setDefaultLayoutHelper(@NonNull LayoutHelper layoutHelper) {
        if (layoutHelper != null) {
            this.mDefaultLayoutHelper = layoutHelper;
            return;
        }
        throw new IllegalArgumentException("layoutHelper should not be null");
    }

    public void scrollToPosition(int i) {
        super.scrollToPosition(i);
    }

    public void scrollToPositionWithOffset(int i, int i2) {
        super.scrollToPositionWithOffset(i, i2);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        super.smoothScrollToPosition(recyclerView, state, i);
    }

    public boolean supportsPredictiveItemAnimations() {
        return this.mCurrentPendingSavedState == null;
    }

    public void onItemsAdded(RecyclerView recyclerView, int i, int i2) {
        onItemsChanged(recyclerView);
    }

    public void onItemsRemoved(RecyclerView recyclerView, int i, int i2) {
        onItemsChanged(recyclerView);
    }

    public void onItemsUpdated(RecyclerView recyclerView, int i, int i2) {
        onItemsChanged(recyclerView);
    }

    public void onItemsMoved(RecyclerView recyclerView, int i, int i2, int i3) {
        onItemsChanged(recyclerView);
    }

    public void onItemsChanged(RecyclerView recyclerView) {
        for (LayoutHelper onItemsChanged : this.mHelperFinder.getLayoutHelpers()) {
            onItemsChanged.onItemsChanged(this);
        }
    }

    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) (LayoutParams) layoutParams);
        }
        if (layoutParams instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new InflateLayoutParams(context, attributeSet);
    }

    public void onAdapterChanged(RecyclerView.Adapter adapter, RecyclerView.Adapter adapter2) {
        super.onAdapterChanged(adapter, adapter2);
    }

    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        for (LayoutHelper clear : this.mHelperFinder.getLayoutHelpers()) {
            clear.clear(this);
        }
        this.mRecyclerView = null;
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public static final int INVALIDE_SIZE = Integer.MIN_VALUE;
        public float mAspectRatio = Float.NaN;
        private int mOriginHeight = Integer.MIN_VALUE;
        private int mOriginWidth = Integer.MIN_VALUE;
        public int zIndex = 0;

        public void storeOriginWidth() {
            if (this.mOriginWidth == Integer.MIN_VALUE) {
                this.mOriginWidth = this.width;
            }
        }

        public void storeOriginHeight() {
            if (this.mOriginHeight == Integer.MIN_VALUE) {
                this.mOriginHeight = this.height;
            }
        }

        public void restoreOriginWidth() {
            if (this.mOriginWidth != Integer.MIN_VALUE) {
                this.width = this.mOriginWidth;
            }
        }

        public void restoreOriginHeight() {
            if (this.mOriginHeight != Integer.MIN_VALUE) {
                this.height = this.mOriginHeight;
            }
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public static class InflateLayoutParams extends LayoutParams {
        public InflateLayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    public static class AnchorInfoWrapper {
        public int coordinate;
        public boolean layoutFromEnd;
        public int position;

        AnchorInfoWrapper() {
        }
    }

    public static class LayoutStateWrapper {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        public static final int ITEM_DIRECTION_HEAD = -1;
        public static final int ITEM_DIRECTION_TAIL = 1;
        public static final int LAYOUT_END = 1;
        public static final int LAYOUT_START = -1;
        static final int SCOLLING_OFFSET_NaN = Integer.MIN_VALUE;
        /* access modifiers changed from: private */
        public ExposeLinearLayoutManagerEx.LayoutState mLayoutState;

        LayoutStateWrapper() {
        }

        LayoutStateWrapper(ExposeLinearLayoutManagerEx.LayoutState layoutState) {
            this.mLayoutState = layoutState;
        }

        public int getOffset() {
            return this.mLayoutState.mOffset;
        }

        public int getCurrentPosition() {
            return this.mLayoutState.mCurrentPosition;
        }

        public boolean hasScrapList() {
            return this.mLayoutState.mScrapList != null;
        }

        public void skipCurrentPosition() {
            this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
        }

        public boolean isRecycle() {
            return this.mLayoutState.mRecycle;
        }

        public boolean isRefreshLayout() {
            return this.mLayoutState.mOnRefresLayout;
        }

        public int getAvailable() {
            return this.mLayoutState.mAvailable;
        }

        public int getItemDirection() {
            return this.mLayoutState.mItemDirection;
        }

        public int getLayoutDirection() {
            return this.mLayoutState.mLayoutDirection;
        }

        public int getScrollingOffset() {
            return this.mLayoutState.mScrollingOffset;
        }

        public int getExtra() {
            return this.mLayoutState.mExtra;
        }

        public boolean isPreLayout() {
            return this.mLayoutState.mIsPreLayout;
        }

        public boolean hasMore(RecyclerView.State state) {
            return this.mLayoutState.hasMore(state);
        }

        public View next(RecyclerView.Recycler recycler) {
            return this.mLayoutState.next(recycler);
        }

        public View retrieve(RecyclerView.Recycler recycler, int i) {
            int i2 = this.mLayoutState.mCurrentPosition;
            this.mLayoutState.mCurrentPosition = i;
            View next = next(recycler);
            this.mLayoutState.mCurrentPosition = i2;
            return next;
        }
    }

    private static class LayoutViewHolder extends RecyclerView.ViewHolder {
        public LayoutViewHolder(View view) {
            super(view);
        }
    }

    public List<View> getFixedViews() {
        if (this.mRecyclerView == null) {
            return Collections.emptyList();
        }
        LinkedList linkedList = new LinkedList();
        for (LayoutHelper fixedView : this.mHelperFinder.getLayoutHelpers()) {
            View fixedView2 = fixedView.getFixedView();
            if (fixedView2 != null) {
                linkedList.add(fixedView2);
            }
        }
        return linkedList;
    }

    public void setLayoutViewFactory(@NonNull LayoutViewFactory layoutViewFactory) {
        if (layoutViewFactory != null) {
            this.mLayoutViewFatory = layoutViewFactory;
            return;
        }
        throw new IllegalArgumentException("factory should not be null");
    }

    public final View generateLayoutView() {
        if (this.mRecyclerView == null) {
            return null;
        }
        View generateLayoutView = this.mLayoutViewFatory.generateLayoutView(this.mRecyclerView.getContext());
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        attachViewHolder(layoutParams, new LayoutViewHolder(generateLayoutView));
        generateLayoutView.setLayoutParams(layoutParams);
        return generateLayoutView;
    }

    public void addChildView(View view, int i) {
        super.addView(view, i);
    }

    public void moveView(int i, int i2) {
        super.moveView(i, i2);
    }

    public void addChildView(LayoutStateWrapper layoutStateWrapper, View view) {
        addChildView(layoutStateWrapper, view, layoutStateWrapper.getItemDirection() == 1 ? -1 : 0);
    }

    public void addChildView(LayoutStateWrapper layoutStateWrapper, View view, int i) {
        showView(view);
        if (!layoutStateWrapper.hasScrapList()) {
            addView(view, i);
        } else {
            addDisappearingView(view, i);
        }
    }

    public void addOffFlowView(View view, boolean z) {
        showView(view);
        addHiddenView(view, z);
    }

    public void addBackgroundView(View view, boolean z) {
        showView(view);
        addView(view, z ? 0 : -1);
    }

    public void addFixedView(View view) {
        addOffFlowView(view, false);
    }

    public void hideView(View view) {
        super.hideView(view);
    }

    public void showView(View view) {
        super.showView(view);
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public RecyclerView.ViewHolder getChildViewHolder(View view) {
        if (this.mRecyclerView != null) {
            return this.mRecyclerView.getChildViewHolder(view);
        }
        return null;
    }

    public boolean isViewHolderUpdated(View view) {
        RecyclerView.ViewHolder childViewHolder = getChildViewHolder(view);
        return childViewHolder == null || isViewHolderUpdated(childViewHolder);
    }

    public void removeChildView(View view) {
        removeView(view);
    }

    public OrientationHelperEx getMainOrientationHelper() {
        return this.mOrientationHelper;
    }

    public OrientationHelperEx getSecondaryOrientationHelper() {
        return this.mSecondaryOrientationHelper;
    }

    public void measureChild(View view, int i, int i2) {
        measureChildWithDecorations(view, i, i2);
    }

    public void measureChildWithMargins(View view, int i, int i2) {
        measureChildWithDecorationsAndMargin(view, i, i2);
    }

    public int getChildMeasureSpec(int i, int i2, boolean z) {
        return getChildMeasureSpec(i, 0, i2, z);
    }

    public boolean canScrollHorizontally() {
        boolean z = this.layoutManagerCanScrollListener == null || this.layoutManagerCanScrollListener.canScrollHorizontally();
        if (!this.mCanScrollHorizontally || this.mNoScrolling || !z) {
            return false;
        }
        return true;
    }

    public boolean canScrollVertically() {
        boolean z = this.layoutManagerCanScrollListener == null || this.layoutManagerCanScrollListener.canScrollVertically();
        if (!this.mCanScrollVertically || this.mNoScrolling || !z) {
            return false;
        }
        return true;
    }

    public void layoutChildWithMargins(View view, int i, int i2, int i3, int i4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordStart("layout", view);
        }
        layoutDecorated(view, i + marginLayoutParams.leftMargin, i2 + marginLayoutParams.topMargin, i3 - marginLayoutParams.rightMargin, i4 - marginLayoutParams.bottomMargin);
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordEnd("layout", view);
        }
    }

    public void layoutChild(View view, int i, int i2, int i3, int i4) {
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordStart("layout", view);
        }
        layoutDecorated(view, i, i2, i3, i4);
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordEnd("layout", view);
        }
    }

    /* access modifiers changed from: protected */
    public void recycleChildren(RecyclerView.Recycler recycler, int i, int i2) {
        if (i != i2) {
            if (sDebuggable) {
                Log.d(TAG, "Recycling " + Math.abs(i - i2) + " items");
            }
            if (i2 > i) {
                View childAt = getChildAt(i2 - 1);
                int position = getPosition(getChildAt(i));
                int position2 = getPosition(childAt);
                int i3 = i;
                while (i < i2) {
                    int position3 = getPosition(getChildAt(i3));
                    if (position3 != -1) {
                        LayoutHelper layoutHelper = this.mHelperFinder.getLayoutHelper(position3);
                        if (layoutHelper == null || layoutHelper.isRecyclable(position3, position, position2, this, true)) {
                            removeAndRecycleViewAt(i3, recycler);
                        } else {
                            i3++;
                        }
                    } else {
                        removeAndRecycleViewAt(i3, recycler);
                    }
                    i++;
                }
                return;
            }
            View childAt2 = getChildAt(i);
            int position4 = getPosition(getChildAt(i2 + 1));
            int position5 = getPosition(childAt2);
            while (i > i2) {
                int position6 = getPosition(getChildAt(i));
                if (position6 != -1) {
                    LayoutHelper layoutHelper2 = this.mHelperFinder.getLayoutHelper(position6);
                    if (layoutHelper2 == null || layoutHelper2.isRecyclable(position6, position4, position5, this, false)) {
                        removeAndRecycleViewAt(i, recycler);
                    }
                } else {
                    removeAndRecycleViewAt(i, recycler);
                }
                i--;
            }
        }
    }

    public void detachAndScrapAttachedViews(RecyclerView.Recycler recycler) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            RecyclerView.ViewHolder childViewHolder = getChildViewHolder(getChildAt(childCount));
            if ((childViewHolder instanceof CacheViewHolder) && ((CacheViewHolder) childViewHolder).needCached()) {
                ExposeLinearLayoutManagerEx.ViewHolderWrapper.setFlags(childViewHolder, 0, 6);
            }
        }
        super.detachAndScrapAttachedViews(recycler);
    }

    public void detachAndScrapViewAt(int i, RecyclerView.Recycler recycler) {
        RecyclerView.ViewHolder childViewHolder = getChildViewHolder(getChildAt(i));
        if ((childViewHolder instanceof CacheViewHolder) && ((CacheViewHolder) childViewHolder).needCached()) {
            ExposeLinearLayoutManagerEx.ViewHolderWrapper.setFlags(childViewHolder, 0, 4);
        }
        super.detachAndScrapViewAt(i, recycler);
    }

    public void detachAndScrapView(View view, RecyclerView.Recycler recycler) {
        super.detachAndScrapView(view, recycler);
    }

    public int getContentWidth() {
        return super.getWidth();
    }

    public int getContentHeight() {
        return super.getHeight();
    }

    public boolean isDoLayoutRTL() {
        return isLayoutRTL();
    }

    private void measureChildWithDecorations(View view, int i, int i2) {
        calculateItemDecorationsForChild(view, this.mDecorInsets);
        int updateSpecWithExtra = updateSpecWithExtra(i, this.mDecorInsets.left, this.mDecorInsets.right);
        int updateSpecWithExtra2 = updateSpecWithExtra(i2, this.mDecorInsets.top, this.mDecorInsets.bottom);
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordStart(PHASE_MEASURE, view);
        }
        view.measure(updateSpecWithExtra, updateSpecWithExtra2);
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordEnd(PHASE_MEASURE, view);
        }
    }

    private void measureChildWithDecorationsAndMargin(View view, int i, int i2) {
        calculateItemDecorationsForChild(view, this.mDecorInsets);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (getOrientation() == 1) {
            i = updateSpecWithExtra(i, layoutParams.leftMargin + this.mDecorInsets.left, layoutParams.rightMargin + this.mDecorInsets.right);
        }
        if (getOrientation() == 0) {
            i2 = updateSpecWithExtra(i2, this.mDecorInsets.top, this.mDecorInsets.bottom);
        }
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordStart(PHASE_MEASURE, view);
        }
        view.measure(i, i2);
        if (this.mPerformanceMonitor != null) {
            this.mPerformanceMonitor.recordEnd(PHASE_MEASURE, view);
        }
    }

    private int updateSpecWithExtra(int i, int i2, int i3) {
        if (i2 == 0 && i3 == 0) {
            return i;
        }
        int mode = View.MeasureSpec.getMode(i);
        if (mode != Integer.MIN_VALUE && mode != 1073741824) {
            return i;
        }
        if ((View.MeasureSpec.getSize(i) - i2) - i3 < 0) {
            return View.MeasureSpec.makeMeasureSpec(0, mode);
        }
        return View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(i) - i2) - i3, mode);
    }

    public View findViewByPosition(int i) {
        View findViewByPosition = super.findViewByPosition(i);
        if (findViewByPosition != null && getPosition(findViewByPosition) == i) {
            return findViewByPosition;
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt != null && getPosition(childAt) == i) {
                return childAt;
            }
        }
        return null;
    }

    public void recycleView(View view) {
        ViewParent parent;
        if (this.mRecyclerView != null && (parent = view.getParent()) != null && parent == this.mRecyclerView) {
            this.mRecyclerView.getRecycledViewPool().putRecycledView(this.mRecyclerView.getChildViewHolder(view));
        }
    }

    public LayoutHelper findLayoutHelperByPosition(int i) {
        return this.mHelperFinder.getLayoutHelper(i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(androidx.recyclerview.widget.RecyclerView.Recycler r9, androidx.recyclerview.widget.RecyclerView.State r10, int r11, int r12) {
        /*
            r8 = this;
            boolean r0 = r8.mNoScrolling
            if (r0 != 0) goto L_0x000c
            boolean r0 = r8.mNestedScrolling
            if (r0 != 0) goto L_0x000c
            super.onMeasure(r9, r10, r11, r12)
            return
        L_0x000c:
            androidx.recyclerview.widget.RecyclerView r0 = r8.mRecyclerView
            r1 = 134217727(0x7ffffff, float:3.8518597E-34)
            if (r0 == 0) goto L_0x002f
            boolean r0 = r8.mNestedScrolling
            if (r0 == 0) goto L_0x002f
            int r0 = r8.mMaxMeasureSize
            if (r0 <= 0) goto L_0x001e
            int r0 = r8.mMaxMeasureSize
            goto L_0x0032
        L_0x001e:
            androidx.recyclerview.widget.RecyclerView r0 = r8.mRecyclerView
            android.view.ViewParent r0 = r0.getParent()
            boolean r2 = r0 instanceof android.view.View
            if (r2 == 0) goto L_0x002f
            android.view.View r0 = (android.view.View) r0
            int r0 = r0.getMeasuredHeight()
            goto L_0x0032
        L_0x002f:
            r0 = 134217727(0x7ffffff, float:3.8518597E-34)
        L_0x0032:
            boolean r2 = r8.mSpaceMeasured
            if (r2 == 0) goto L_0x0038
            int r0 = r8.mMeasuredFullSpace
        L_0x0038:
            boolean r2 = r8.mNoScrolling
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x0096
            boolean r2 = r8.mSpaceMeasured
            r2 = r2 ^ r4
            r8.mSpaceMeasuring = r2
            int r2 = r8.getChildCount()
            if (r2 > 0) goto L_0x0060
            int r2 = r8.getChildCount()
            int r5 = r8.getItemCount()
            if (r2 == r5) goto L_0x0054
            goto L_0x0060
        L_0x0054:
            int r1 = r8.getItemCount()
            if (r1 != 0) goto L_0x0096
            r8.mSpaceMeasured = r4
            r8.mSpaceMeasuring = r3
            r0 = 0
            goto L_0x0096
        L_0x0060:
            int r2 = r8.getChildCount()
            int r2 = r2 - r4
            android.view.View r2 = r8.getChildAt(r2)
            int r5 = r8.mMeasuredFullSpace
            if (r2 == 0) goto L_0x007f
            android.view.ViewGroup$LayoutParams r5 = r2.getLayoutParams()
            androidx.recyclerview.widget.RecyclerView$LayoutParams r5 = (androidx.recyclerview.widget.RecyclerView.LayoutParams) r5
            int r6 = r8.getDecoratedBottom(r2)
            int r5 = r5.bottomMargin
            int r6 = r6 + r5
            int r5 = r8.computeAlignOffset((android.view.View) r2, (boolean) r4, (boolean) r3)
            int r5 = r5 + r6
        L_0x007f:
            int r6 = r8.getChildCount()
            int r7 = r8.getItemCount()
            if (r6 != r7) goto L_0x008f
            if (r2 == 0) goto L_0x0096
            int r2 = r8.mMeasuredFullSpace
            if (r5 == r2) goto L_0x0096
        L_0x008f:
            r8.mSpaceMeasured = r3
            r8.mSpaceMeasuring = r4
            r0 = 134217727(0x7ffffff, float:3.8518597E-34)
        L_0x0096:
            int r1 = r8.getOrientation()
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r1 != r4) goto L_0x00a6
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r2)
            super.onMeasure(r9, r10, r11, r12)
            goto L_0x00ad
        L_0x00a6:
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r2)
            super.onMeasure(r9, r10, r11, r12)
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager.onMeasure(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, int, int):void");
    }
}
