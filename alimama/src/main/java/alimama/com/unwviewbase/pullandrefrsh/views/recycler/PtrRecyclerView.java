package alimama.com.unwviewbase.pullandrefrsh.views.recycler;

import alimama.com.unwviewbase.pullandrefrsh.OnLastItemVisibleListener;
import alimama.com.unwviewbase.pullandrefrsh.PtrBase;
import alimama.com.unwviewbase.pullandrefrsh.PtrInterface;
import alimama.com.unwviewbase.pullandrefrsh.PtrLayout;
import alimama.com.unwviewbase.pullandrefrsh.PtrLoadingDelegate;
import alimama.com.unwviewbase.pullandrefrsh.PtrLoadingHelper;
import alimama.com.unwviewbase.pullandrefrsh.PtrProxy;
import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.FixedViewAdapter;
import alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.FixedViewInfo;
import alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.GridSpanSizeLookup;
import alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.ItemClickGestureListener;
import alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.RecyclerViewDetector;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PtrRecyclerView extends RecyclerView implements PullAdapter, PtrInterface, PtrProxy {
    /* access modifiers changed from: private */
    public RecyclerView.Adapter mAdapter;
    private PtrLoadingDelegate mDelegate;
    private View mEndView;
    private ArrayList<FixedViewInfo> mEndViewInfos = new ArrayList<>();
    private GestureDetector mGestureDetector;
    private boolean mIsDisable;
    /* access modifiers changed from: private */
    public OnItemClickListener mItemClickListener;
    /* access modifiers changed from: private */
    public OnItemLongClickListener mItemLongClickListener;
    private Drawable mLoadingDrawable;
    private PtrLoadingHelper mLoadingHelper;
    /* access modifiers changed from: private */
    public OnLastItemVisibleListener mOnLastItemVisibleListener;
    private ArrayList<FixedViewInfo> mStartViewInfos = new ArrayList<>();
    private ColorStateList mTextColor;
    private RecyclerView.OnScrollListener onScrollListener;

    public interface OnItemClickListener {
        boolean onItemClick(RecyclerView recyclerView, View view, int i, long j);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long j);
    }

    public PtrRecyclerView(Context context) {
        super(context);
    }

    public PtrRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private boolean removeFixedViewInfo(View view, ArrayList<FixedViewInfo> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            FixedViewInfo fixedViewInfo = arrayList.get(i);
            if (fixedViewInfo.getView() == view) {
                if (view.getParent() == this) {
                    removeView(view);
                }
                if (arrayList.remove(i) == fixedViewInfo) {
                    return true;
                }
            }
        }
        return false;
    }

    private int indexOfFixedInfos(List<FixedViewInfo> list, View view) {
        if (list == null || view == null) {
            return 0;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getHashCode() == view.hashCode()) {
                return i;
            }
        }
        return 0;
    }

    public int getStartViewsCount() {
        return this.mStartViewInfos.size();
    }

    public int getEndViewsCount() {
        return this.mEndViewInfos.size();
    }

    public boolean hasStartView(View view) {
        if (view == null) {
            return false;
        }
        Iterator<FixedViewInfo> it = this.mStartViewInfos.iterator();
        while (it.hasNext()) {
            FixedViewInfo next = it.next();
            if (next != null && view == next.getView()) {
                return true;
            }
        }
        return false;
    }

    public void addStartView(View view) {
        if (view != null) {
            this.mStartViewInfos.add(new FixedViewInfo(view));
            spanStaggeredGridStartEndViews();
            if (this.mAdapter == null) {
                return;
            }
            if (!(this.mAdapter instanceof FixedViewAdapter)) {
                this.mAdapter = new FixedViewAdapter(this.mStartViewInfos, this.mEndViewInfos, this.mAdapter);
                super.setAdapter(this.mAdapter);
                return;
            }
            this.mAdapter.notifyItemInserted(this.mStartViewInfos.size() - 1);
        }
    }

    public boolean removeStartView(View view) {
        boolean z = false;
        if (this.mStartViewInfos.size() <= 0 || view == null) {
            return false;
        }
        int indexOfFixedInfos = indexOfFixedInfos(this.mStartViewInfos, view);
        if (this.mAdapter != null && ((FixedViewAdapter) this.mAdapter).removeStart(view)) {
            this.mAdapter.notifyItemRemoved(indexOfFixedInfos);
            if (view.getParent() == this) {
                removeView(view);
            }
            z = true;
        }
        return removeFixedViewInfo(view, this.mStartViewInfos) | z;
    }

    public boolean removeAllStartViews() {
        int size = this.mStartViewInfos.size();
        if (size <= 0) {
            return false;
        }
        if (this.mAdapter != null && ((FixedViewAdapter) this.mAdapter).removeAllStarts()) {
            this.mAdapter.notifyItemRangeRemoved(0, size);
            Iterator<FixedViewInfo> it = this.mStartViewInfos.iterator();
            while (it.hasNext()) {
                FixedViewInfo next = it.next();
                if (!(next == null || next.getView() == null || next.getView().getParent() != this)) {
                    removeView(next.getView());
                }
            }
        }
        this.mStartViewInfos.clear();
        return true;
    }

    public boolean hasEndView(View view) {
        if (view == null) {
            return false;
        }
        Iterator<FixedViewInfo> it = this.mEndViewInfos.iterator();
        while (it.hasNext()) {
            FixedViewInfo next = it.next();
            if (next != null && view == next.getView()) {
                return true;
            }
        }
        return false;
    }

    public void addEndView(View view) {
        if (view != null) {
            FixedViewInfo fixedViewInfo = new FixedViewInfo(view);
            int max = Math.max(this.mEndViewInfos.size(), 0);
            if (this.mEndView != null) {
                max = Math.max(max - 1, 0);
            }
            this.mEndViewInfos.add(max, fixedViewInfo);
            spanStaggeredGridStartEndViews();
            if (this.mAdapter == null) {
                return;
            }
            if (!(this.mAdapter instanceof FixedViewAdapter)) {
                this.mAdapter = new FixedViewAdapter(this.mStartViewInfos, this.mEndViewInfos, this.mAdapter);
                super.setAdapter(this.mAdapter);
                return;
            }
            int itemCount = this.mAdapter.getItemCount() - 1;
            if (this.mEndView != null) {
                itemCount = Math.max(itemCount - 1, 0);
            }
            this.mAdapter.notifyItemInserted(itemCount);
        }
    }

    public boolean removeEndView(View view) {
        boolean z = false;
        if (this.mEndViewInfos.size() <= 0 || view == null) {
            return false;
        }
        int size = this.mEndViewInfos.size();
        int indexOfFixedInfos = indexOfFixedInfos(this.mEndViewInfos, view);
        if (this.mAdapter != null) {
            int itemCount = this.mAdapter.getItemCount();
            if (((FixedViewAdapter) this.mAdapter).removeEnd(view)) {
                this.mAdapter.notifyItemRemoved(itemCount - (size - indexOfFixedInfos));
                if (view.getParent() == this) {
                    removeView(view);
                }
                z = true;
            }
        }
        return removeFixedViewInfo(view, this.mEndViewInfos) | z;
    }

    public boolean removeAllEndViews() {
        int size = this.mEndViewInfos.size();
        if (size <= 0) {
            return false;
        }
        if (this.mAdapter != null) {
            int itemCount = this.mAdapter.getItemCount();
            if (((FixedViewAdapter) this.mAdapter).removeAllEnds()) {
                this.mAdapter.notifyItemRangeRemoved(itemCount - size, size);
                Iterator<FixedViewInfo> it = this.mEndViewInfos.iterator();
                while (it.hasNext()) {
                    FixedViewInfo next = it.next();
                    if (!(next == null || next.getView() == null || next.getView().getParent() != this)) {
                        removeView(next.getView());
                    }
                }
            }
        }
        this.mEndViewInfos.clear();
        return true;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridSpanSizeLookup(this, gridLayoutManager, gridLayoutManager.getSpanSizeLookup()));
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanStaggeredGridStartEndViews();
            }
        }
        super.setLayoutManager(layoutManager);
        applyStyle();
        onUpdateDirection(getPullDirection());
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = new FixedViewAdapter(this.mStartViewInfos, this.mEndViewInfos, adapter);
        super.setAdapter(this.mAdapter);
    }

    private void spanStaggeredGridStartEndViews() {
        spanStaggeredGridViews(this.mStartViewInfos);
        spanStaggeredGridViews(this.mEndViewInfos);
    }

    private void spanStaggeredGridViews(List<FixedViewInfo> list) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (list != null && (layoutManager instanceof StaggeredGridLayoutManager)) {
            for (FixedViewInfo view : list) {
                View view2 = view.getView();
                if (view2 != null) {
                    ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                    if (layoutParams == null || !(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
                        StaggeredGridLayoutManager.LayoutParams layoutParams2 = new StaggeredGridLayoutManager.LayoutParams(-2, -2);
                        layoutParams2.setFullSpan(true);
                        view2.setLayoutParams(layoutParams2);
                    } else {
                        ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                    }
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mGestureDetector != null ? this.mGestureDetector.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
    }

    private void addGestureDetectorIfNeed() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), new ItemClickGestureListener(this) {
                public boolean performItemClick(RecyclerView recyclerView, View view, int i, long j) {
                    return PtrRecyclerView.this.mItemClickListener != null && PtrRecyclerView.this.mItemClickListener.onItemClick(recyclerView, view, i, j);
                }

                public boolean performItemLongClick(RecyclerView recyclerView, View view, int i, long j) {
                    return PtrRecyclerView.this.mItemLongClickListener != null && PtrRecyclerView.this.mItemLongClickListener.onItemLongClick(recyclerView, view, i, j);
                }
            });
        }
    }

    private void addScrollDetectorIfNeed() {
        if (this.onScrollListener == null) {
            this.onScrollListener = new RecyclerView.OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                    super.onScrollStateChanged(recyclerView, i);
                    if (i == 0 && PtrRecyclerView.this.mAdapter != null && PtrRecyclerView.this.mAdapter.getItemCount() > 0 && PtrRecyclerView.this.mOnLastItemVisibleListener != null && RecyclerViewDetector.findLastVisibleItemPosition(PtrRecyclerView.this) == PtrRecyclerView.this.mAdapter.getItemCount() - 1) {
                        PtrRecyclerView.this.mOnLastItemVisibleListener.onLastItemVisible();
                    }
                }

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    super.onScrolled(recyclerView, i, i2);
                }
            };
            addOnScrollListener(this.onScrollListener);
        }
    }

    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener onLastItemVisibleListener) {
        this.mOnLastItemVisibleListener = onLastItemVisibleListener;
        if (onLastItemVisibleListener != null) {
            addScrollDetectorIfNeed();
        }
    }

    public final void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
        if (onItemLongClickListener != null) {
            addGestureDetectorIfNeed();
        }
    }

    public final void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
        if (onItemClickListener != null) {
            addGestureDetectorIfNeed();
        }
    }

    public final void setEndLoadingTextColor(ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingTextColor(colorStateList);
        }
    }

    public final void setEndLoadingDrawable(Drawable drawable) {
        this.mLoadingDrawable = drawable;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingDrawable(drawable);
        }
    }

    public final void setEndLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        this.mDelegate = ptrLoadingDelegate;
        if (this.mLoadingHelper != null) {
            this.mLoadingHelper.setLoadingDelegate(ptrLoadingDelegate);
            onUpdateDirection(getPullDirection());
        }
    }

    public final void disableEndPullFeature(boolean z) {
        if (z) {
            onDisable();
        } else {
            onEnable();
        }
    }

    public boolean isDisableIntrinsicPullFeature() {
        return isDisable();
    }

    public boolean isDisable() {
        return this.mIsDisable;
    }

    public final View getEndView() {
        return this.mEndView;
    }

    private void applyStyle() {
        if (getLayoutManager() != null) {
            setEndLoadingTextColor(this.mTextColor);
            setEndLoadingDrawable(this.mLoadingDrawable);
            setEndLoadingDelegate(this.mDelegate);
        }
    }

    /* renamed from: alimama.com.unwviewbase.pullandrefrsh.views.recycler.PtrRecyclerView$3  reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = new int[PullBase.Mode.values().length];

        static {
            try {
                $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[PullBase.Mode.PULL_FROM_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int getReadyToRefreshingValue(PtrBase ptrBase, PullBase.Mode mode, int i) {
        if (AnonymousClass3.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] == 1) {
            if (i != 1) {
                if (this.mEndView != null) {
                    return this.mEndView.getHeight();
                }
            } else if (this.mEndView != null) {
                return this.mEndView.getWidth();
            }
        }
        return -1;
    }

    public int getReleaseTargetValue(PtrBase ptrBase, PullBase.Mode mode, int i) {
        return AnonymousClass3.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] != 1 ? -1 : 0;
    }

    public void onUpdateDirection(int i) {
        View loadingView;
        if (getLayoutManager() != null && this.mLoadingHelper != null) {
            if (this.mLoadingHelper != null) {
                this.mLoadingHelper.onUpdateDirection(i);
            }
            if (!isDisable()) {
                removeEndView(this.mEndView);
                this.mEndView = null;
                if (this.mLoadingHelper != null && (loadingView = this.mLoadingHelper.getLoadingView(this)) != null) {
                    addEndView(loadingView);
                    this.mEndView = loadingView;
                }
            }
        }
    }

    public void onPull(float f) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onPull(f);
        }
    }

    public void onRelease(float f) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onRelease(f);
        }
    }

    public void onRefreshing() {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onRefreshing();
        }
    }

    public void onCompleteUpdate(CharSequence charSequence) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onCompleteUpdate(charSequence);
        }
    }

    public void onReset() {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onReset();
        }
    }

    public void onFreeze(boolean z, CharSequence charSequence) {
        if (this.mLoadingHelper != null && !isDisable()) {
            this.mLoadingHelper.onFreeze(z, charSequence);
        }
    }

    public void onDisable() {
        this.mIsDisable = true;
        removeEndView(this.mEndView);
        this.mEndView = null;
    }

    public void onEnable() {
        this.mIsDisable = false;
        onUpdateDirection(getPullDirection());
    }

    public int getPullDirection() {
        switch (RecyclerViewDetector.getLayoutOrientation(this)) {
            case 0:
                return 1;
            default:
                return 0;
        }
    }

    public boolean isReadyForPullStart() {
        return RecyclerViewDetector.isFirstItemTotallyVisible(this);
    }

    public boolean isReadyForPullEnd() {
        return RecyclerViewDetector.isLastItemTotallyVisible(this);
    }

    public void onPullAdapterAdded(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            PtrBase ptrBase = (PtrBase) pullBase;
            ptrBase.addEndPtrProxy(this);
            PtrLayout endLayout = ptrBase.getEndLayout();
            if (endLayout != null) {
                endLayout.disableIntrinsicPullFeature(true);
            }
            if (pullBase.getMode() == PullBase.Mode.PULL_FROM_START || pullBase.getMode() == PullBase.Mode.DISABLED) {
                this.mIsDisable = true;
            }
            this.mLoadingHelper = new PtrLoadingHelper(endLayout);
            applyStyle();
        }
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            PtrBase ptrBase = (PtrBase) pullBase;
            ptrBase.removeEndPtrProxy(this);
            removeEndView(this.mEndView);
            this.mEndView = null;
            this.mLoadingHelper = null;
            this.mIsDisable = true;
            ptrBase.getEndLayout().disableIntrinsicPullFeature(false);
        }
    }
}
