package alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories;

import alimama.com.unwviewbase.pullandrefrsh.views.recycler.PtrRecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;

public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private WeakReference<GridLayoutManager> mGridLayoutManagerRef;
    private WeakReference<PtrRecyclerView> mPtrRecyclerViewRef;
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    public GridSpanSizeLookup(PtrRecyclerView ptrRecyclerView, GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.mPtrRecyclerViewRef = new WeakReference<>(ptrRecyclerView);
        this.mGridLayoutManagerRef = new WeakReference<>(gridLayoutManager);
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public boolean isSpanIndexCacheEnabled() {
        if (this.mSpanSizeLookup != null) {
            return this.mSpanSizeLookup.isSpanIndexCacheEnabled();
        }
        return super.isSpanIndexCacheEnabled();
    }

    public int getSpanIndex(int i, int i2) {
        if (this.mSpanSizeLookup != null) {
            return this.mSpanSizeLookup.getSpanIndex(i, i2);
        }
        return super.getSpanIndex(i, i2);
    }

    public int getSpanGroupIndex(int i, int i2) {
        if (this.mSpanSizeLookup != null) {
            return this.mSpanSizeLookup.getSpanGroupIndex(i, i2);
        }
        return super.getSpanGroupIndex(i, i2);
    }

    public int getSpanSize(int i) {
        if (!(this.mPtrRecyclerViewRef == null || this.mPtrRecyclerViewRef.get() == null || this.mGridLayoutManagerRef == null || this.mGridLayoutManagerRef.get() == null)) {
            PtrRecyclerView ptrRecyclerView = (PtrRecyclerView) this.mPtrRecyclerViewRef.get();
            RecyclerView.Adapter adapter = ((PtrRecyclerView) this.mPtrRecyclerViewRef.get()).getAdapter();
            if (adapter != null && (adapter instanceof FixedViewAdapter)) {
                int startViewsCount = ptrRecyclerView.getStartViewsCount() - 1;
                int itemCount = adapter.getItemCount() - ptrRecyclerView.getEndViewsCount();
                if (i >= 0 && (i <= startViewsCount || i >= itemCount)) {
                    return ((GridLayoutManager) this.mGridLayoutManagerRef.get()).getSpanCount();
                }
            }
        }
        if (this.mSpanSizeLookup != null) {
            return this.mSpanSizeLookup.getSpanSize(i);
        }
        return 1;
    }
}
