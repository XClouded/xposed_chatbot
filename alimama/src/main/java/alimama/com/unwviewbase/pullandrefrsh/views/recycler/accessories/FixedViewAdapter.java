package alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;

public class FixedViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter mAdapter;
    private ArrayList<FixedViewInfo> mEndViews = new ArrayList<>();
    private ArrayList<FixedViewInfo> mStartViews = new ArrayList<>();

    public FixedViewAdapter(ArrayList<FixedViewInfo> arrayList, ArrayList<FixedViewInfo> arrayList2, RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            setHasStableIds(this.mAdapter.hasStableIds());
        }
        if (arrayList != null) {
            this.mStartViews = arrayList;
        }
        if (arrayList2 != null) {
            this.mEndViews = arrayList2;
        }
    }

    public int getStartViewsCount() {
        return this.mStartViews.size();
    }

    public int getEndViewsCount() {
        return this.mEndViews.size();
    }

    public RecyclerView.Adapter getAdapter() {
        return this.mAdapter;
    }

    public boolean removeStart(View view) {
        for (int i = 0; i < this.mStartViews.size(); i++) {
            if (this.mStartViews.get(i).getHashCode() == view.hashCode()) {
                this.mStartViews.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeAllStarts() {
        if (this.mStartViews.size() <= 0) {
            return false;
        }
        this.mStartViews.clear();
        return true;
    }

    public boolean removeEnd(View view) {
        for (int i = 0; i < this.mEndViews.size(); i++) {
            if (this.mEndViews.get(i).getHashCode() == view.hashCode()) {
                this.mEndViews.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeAllEnds() {
        if (this.mEndViews.size() <= 0) {
            return false;
        }
        this.mEndViews.clear();
        return true;
    }

    private boolean isFixedView(int i) {
        Iterator<FixedViewInfo> it = this.mStartViews.iterator();
        while (it.hasNext()) {
            if (it.next().getHashCode() == i) {
                return true;
            }
        }
        Iterator<FixedViewInfo> it2 = this.mEndViews.iterator();
        while (it2.hasNext()) {
            if (it2.next().getHashCode() == i) {
                return true;
            }
        }
        return false;
    }

    private View findFixedView(int i) {
        Iterator<FixedViewInfo> it = this.mStartViews.iterator();
        while (it.hasNext()) {
            FixedViewInfo next = it.next();
            if (next.getHashCode() == i) {
                return next.getView();
            }
        }
        Iterator<FixedViewInfo> it2 = this.mEndViews.iterator();
        while (it2.hasNext()) {
            FixedViewInfo next2 = it2.next();
            if (next2.getHashCode() == i) {
                return next2.getView();
            }
        }
        return null;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (!isFixedView(i)) {
            return this.mAdapter.onCreateViewHolder(viewGroup, i);
        }
        View findFixedView = findFixedView(i);
        if (findFixedView.getParent() != null) {
            return new FixedViewHolder(new View(findFixedView.getContext()));
        }
        return new FixedViewHolder(findFixedView);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int startViewsCount = getStartViewsCount();
        if (i >= startViewsCount) {
            int i2 = i - startViewsCount;
            if (this.mAdapter != null && i2 < this.mAdapter.getItemCount()) {
                this.mAdapter.onBindViewHolder(viewHolder, i2);
            }
        }
    }

    public int getItemCount() {
        int startViewsCount = getStartViewsCount() + getEndViewsCount();
        return this.mAdapter != null ? startViewsCount + this.mAdapter.getItemCount() : startViewsCount;
    }

    public long getItemId(int i) {
        int startViewsCount = getStartViewsCount();
        if (i < startViewsCount) {
            return -1;
        }
        int i2 = i - startViewsCount;
        if (this.mAdapter == null || i < startViewsCount || i2 >= this.mAdapter.getItemCount()) {
            return -1;
        }
        return this.mAdapter.getItemId(i2);
    }

    public int getItemViewType(int i) {
        int startViewsCount = getStartViewsCount();
        if (i < startViewsCount) {
            return this.mStartViews.get(i).getHashCode();
        }
        int i2 = i - startViewsCount;
        int i3 = 0;
        if (this.mAdapter == null || i < startViewsCount || i2 >= (i3 = this.mAdapter.getItemCount())) {
            return this.mEndViews.get(i2 - i3).getHashCode();
        }
        return this.mAdapter.getItemViewType(i2);
    }

    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FixedViewHolder) {
            super.onViewRecycled(viewHolder);
        } else if (this.mAdapter != null) {
            this.mAdapter.onViewRecycled(viewHolder);
        } else {
            super.onViewRecycled(viewHolder);
        }
    }

    public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FixedViewHolder) {
            return super.onFailedToRecycleView(viewHolder);
        }
        if (this.mAdapter != null) {
            return this.mAdapter.onFailedToRecycleView(viewHolder);
        }
        return super.onFailedToRecycleView(viewHolder);
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FixedViewHolder) {
            super.onViewAttachedToWindow(viewHolder);
        } else if (this.mAdapter != null) {
            this.mAdapter.onViewAttachedToWindow(viewHolder);
        } else {
            super.onViewAttachedToWindow(viewHolder);
        }
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FixedViewHolder) {
            super.onViewDetachedFromWindow(viewHolder);
        } else if (this.mAdapter != null) {
            this.mAdapter.onViewDetachedFromWindow(viewHolder);
        } else {
            super.onViewDetachedFromWindow(viewHolder);
        }
    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        if (this.mAdapter != null) {
            this.mAdapter.registerAdapterDataObserver(adapterDataObserver);
        }
        super.registerAdapterDataObserver(adapterDataObserver);
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(adapterDataObserver);
        }
        super.unregisterAdapterDataObserver(adapterDataObserver);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.mAdapter != null) {
            this.mAdapter.onAttachedToRecyclerView(recyclerView);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (this.mAdapter != null) {
            this.mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public static final class FixedViewHolder extends RecyclerView.ViewHolder {
        public FixedViewHolder(View view) {
            super(view);
        }
    }
}
