package com.taobao.android.dxcontainer.vlayout;

import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.layout.SingleLayoutHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DelegateAdapter extends VirtualLayoutAdapter<RecyclerView.ViewHolder> {
    private long[] cantorReverse;
    /* access modifiers changed from: private */
    @NonNull
    public final List<Pair<AdapterDataObserver, Adapter>> mAdapters;
    private final boolean mHasConsistItemType;
    private int mIndex;
    private final SparseArray<Pair<AdapterDataObserver, Adapter>> mIndexAry;
    @Nullable
    private AtomicInteger mIndexGen;
    private SparseArray<Adapter> mItemTypeAry;
    /* access modifiers changed from: private */
    public int mTotal;

    public void setHasStableIds(boolean z) {
    }

    public DelegateAdapter(VirtualLayoutManager virtualLayoutManager) {
        this(virtualLayoutManager, false, false);
    }

    public DelegateAdapter(VirtualLayoutManager virtualLayoutManager, boolean z) {
        this(virtualLayoutManager, z, false);
    }

    DelegateAdapter(VirtualLayoutManager virtualLayoutManager, boolean z, boolean z2) {
        super(virtualLayoutManager);
        this.mIndex = 0;
        this.mItemTypeAry = new SparseArray<>();
        this.mAdapters = new ArrayList();
        this.mTotal = 0;
        this.mIndexAry = new SparseArray<>();
        this.cantorReverse = new long[2];
        if (z2) {
            this.mIndexGen = new AtomicInteger(0);
        }
        this.mHasConsistItemType = z;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.mHasConsistItemType) {
            Adapter adapter = this.mItemTypeAry.get(i);
            if (adapter != null) {
                return adapter.onCreateViewHolder(viewGroup, i);
            }
            return null;
        }
        Cantor.reverseCantor((long) i, this.cantorReverse);
        int i2 = (int) this.cantorReverse[1];
        int i3 = (int) this.cantorReverse[0];
        Adapter findAdapterByIndex = findAdapterByIndex(i2);
        if (findAdapterByIndex == null) {
            return null;
        }
        return findAdapterByIndex.onCreateViewHolder(viewGroup, i3);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition = findAdapterByPosition(i);
        if (findAdapterByPosition != null) {
            ((Adapter) findAdapterByPosition.second).onBindViewHolder(viewHolder, i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition);
            ((Adapter) findAdapterByPosition.second).onBindViewHolderWithOffset(viewHolder, i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition, i);
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition = findAdapterByPosition(i);
        if (findAdapterByPosition != null) {
            ((Adapter) findAdapterByPosition.second).onBindViewHolder(viewHolder, i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition, list);
            ((Adapter) findAdapterByPosition.second).onBindViewHolderWithOffset(viewHolder, i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition, i, list);
        }
    }

    public int getItemCount() {
        return this.mTotal;
    }

    public int getItemViewType(int i) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition = findAdapterByPosition(i);
        if (findAdapterByPosition == null) {
            return -1;
        }
        int itemViewType = ((Adapter) findAdapterByPosition.second).getItemViewType(i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition);
        if (itemViewType < 0) {
            return itemViewType;
        }
        if (!this.mHasConsistItemType) {
            return (int) Cantor.getCantor((long) itemViewType, (long) ((AdapterDataObserver) findAdapterByPosition.first).mIndex);
        }
        this.mItemTypeAry.put(itemViewType, findAdapterByPosition.second);
        return itemViewType;
    }

    public long getItemId(int i) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition = findAdapterByPosition(i);
        if (findAdapterByPosition == null) {
            return -1;
        }
        long itemId = ((Adapter) findAdapterByPosition.second).getItemId(i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition);
        if (itemId < 0) {
            return -1;
        }
        return Cantor.getCantor((long) ((AdapterDataObserver) findAdapterByPosition.first).mIndex, itemId);
    }

    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition;
        super.onViewRecycled(viewHolder);
        int position = viewHolder.getPosition();
        if (position >= 0 && (findAdapterByPosition = findAdapterByPosition(position)) != null) {
            ((Adapter) findAdapterByPosition.second).onViewRecycled(viewHolder);
        }
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition;
        super.onViewAttachedToWindow(viewHolder);
        int position = viewHolder.getPosition();
        if (position >= 0 && (findAdapterByPosition = findAdapterByPosition(position)) != null) {
            ((Adapter) findAdapterByPosition.second).onViewAttachedToWindow(viewHolder);
        }
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition;
        super.onViewDetachedFromWindow(viewHolder);
        int position = viewHolder.getPosition();
        if (position >= 0 && (findAdapterByPosition = findAdapterByPosition(position)) != null) {
            ((Adapter) findAdapterByPosition.second).onViewDetachedFromWindow(viewHolder);
        }
    }

    @Deprecated
    public void setLayoutHelpers(List<LayoutHelper> list) {
        throw new UnsupportedOperationException("DelegateAdapter doesn't support setLayoutHelpers directly");
    }

    public void setAdapters(@Nullable List<Adapter> list) {
        int i;
        clear();
        if (list == null) {
            list = Collections.emptyList();
        }
        LinkedList linkedList = new LinkedList();
        this.mTotal = 0;
        boolean z = true;
        for (Adapter next : list) {
            int i2 = this.mTotal;
            if (this.mIndexGen == null) {
                i = this.mIndex;
                this.mIndex = i + 1;
            } else {
                i = this.mIndexGen.incrementAndGet();
            }
            AdapterDataObserver adapterDataObserver = new AdapterDataObserver(i2, i);
            next.registerAdapterDataObserver(adapterDataObserver);
            z = z && next.hasStableIds();
            LayoutHelper onCreateLayoutHelper = next.onCreateLayoutHelper();
            onCreateLayoutHelper.setItemCount(next.getItemCount());
            this.mTotal += onCreateLayoutHelper.getItemCount();
            linkedList.add(onCreateLayoutHelper);
            Pair create = Pair.create(adapterDataObserver, next);
            this.mIndexAry.put(adapterDataObserver.mIndex, create);
            this.mAdapters.add(create);
        }
        if (!hasObservers()) {
            super.setHasStableIds(z);
        }
        super.setLayoutHelpers(linkedList);
    }

    public void addAdapters(int i, @Nullable List<Adapter> list) {
        if (list != null && list.size() != 0) {
            if (i < 0) {
                i = 0;
            }
            if (i > this.mAdapters.size()) {
                i = this.mAdapters.size();
            }
            ArrayList arrayList = new ArrayList();
            for (Pair<AdapterDataObserver, Adapter> pair : this.mAdapters) {
                arrayList.add((Adapter) pair.second);
            }
            for (Adapter add : list) {
                arrayList.add(i, add);
                i++;
            }
            setAdapters(arrayList);
        }
    }

    public void addAdapters(@Nullable List<Adapter> list) {
        addAdapters(this.mAdapters.size(), list);
    }

    public void addAdapter(int i, @Nullable Adapter adapter) {
        addAdapters(i, Collections.singletonList(adapter));
    }

    public void addAdapter(@Nullable Adapter adapter) {
        addAdapters(Collections.singletonList(adapter));
    }

    public void removeFirstAdapter() {
        if (this.mAdapters != null && !this.mAdapters.isEmpty()) {
            removeAdapter((Adapter) this.mAdapters.get(0).second);
        }
    }

    public void removeLastAdapter() {
        if (this.mAdapters != null && !this.mAdapters.isEmpty()) {
            removeAdapter((Adapter) this.mAdapters.get(this.mAdapters.size() - 1).second);
        }
    }

    public void removeAdapter(int i) {
        if (i >= 0 && i < this.mAdapters.size()) {
            removeAdapter((Adapter) this.mAdapters.get(i).second);
        }
    }

    public void removeAdapter(@Nullable Adapter adapter) {
        if (adapter != null) {
            removeAdapters(Collections.singletonList(adapter));
        }
    }

    public void removeAdapters(@Nullable List<Adapter> list) {
        if (list != null && !list.isEmpty()) {
            LinkedList linkedList = new LinkedList(super.getLayoutHelpers());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Adapter adapter = list.get(i);
                Iterator<Pair<AdapterDataObserver, Adapter>> it = this.mAdapters.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Pair next = it.next();
                    Adapter adapter2 = (Adapter) next.second;
                    if (adapter2.equals(adapter)) {
                        adapter2.unregisterAdapterDataObserver((RecyclerView.AdapterDataObserver) next.first);
                        int findAdapterPositionByIndex = findAdapterPositionByIndex(((AdapterDataObserver) next.first).mIndex);
                        if (findAdapterPositionByIndex >= 0 && findAdapterPositionByIndex < linkedList.size()) {
                            linkedList.remove(findAdapterPositionByIndex);
                        }
                        it.remove();
                    }
                }
            }
            ArrayList arrayList = new ArrayList();
            for (Pair<AdapterDataObserver, Adapter> pair : this.mAdapters) {
                arrayList.add(pair.second);
            }
            setAdapters(arrayList);
        }
    }

    public void clear() {
        this.mTotal = 0;
        this.mIndex = 0;
        if (this.mIndexGen != null) {
            this.mIndexGen.set(0);
        }
        this.mLayoutManager.setLayoutHelpers((List<LayoutHelper>) null);
        for (Pair next : this.mAdapters) {
            ((Adapter) next.second).unregisterAdapterDataObserver((RecyclerView.AdapterDataObserver) next.first);
        }
        this.mItemTypeAry.clear();
        this.mAdapters.clear();
        this.mIndexAry.clear();
    }

    public int getAdaptersCount() {
        if (this.mAdapters == null) {
            return 0;
        }
        return this.mAdapters.size();
    }

    public int findOffsetPosition(int i) {
        Pair<AdapterDataObserver, Adapter> findAdapterByPosition = findAdapterByPosition(i);
        if (findAdapterByPosition == null) {
            return -1;
        }
        return i - ((AdapterDataObserver) findAdapterByPosition.first).mStartPosition;
    }

    @Nullable
    public Pair<AdapterDataObserver, Adapter> findAdapterByPosition(int i) {
        int size = this.mAdapters.size();
        if (size == 0) {
            return null;
        }
        int i2 = 0;
        int i3 = size - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) / 2;
            Pair<AdapterDataObserver, Adapter> pair = this.mAdapters.get(i4);
            int itemCount = (((AdapterDataObserver) pair.first).mStartPosition + ((Adapter) pair.second).getItemCount()) - 1;
            if (((AdapterDataObserver) pair.first).mStartPosition > i) {
                i3 = i4 - 1;
            } else if (itemCount < i) {
                i2 = i4 + 1;
            } else if (((AdapterDataObserver) pair.first).mStartPosition <= i && itemCount >= i) {
                return pair;
            }
        }
        return null;
    }

    public int findAdapterPositionByIndex(int i) {
        Pair pair = this.mIndexAry.get(i);
        if (pair == null) {
            return -1;
        }
        return this.mAdapters.indexOf(pair);
    }

    public Adapter findAdapterByIndex(int i) {
        return (Adapter) this.mIndexAry.get(i).second;
    }

    protected class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        int mIndex = -1;
        int mStartPosition;

        public AdapterDataObserver(int i, int i2) {
            this.mStartPosition = i;
            this.mIndex = i2;
        }

        public void updateStartPositionAndIndex(int i, int i2) {
            this.mStartPosition = i;
            this.mIndex = i2;
        }

        public int getStartPosition() {
            return this.mStartPosition;
        }

        public int getIndex() {
            return this.mIndex;
        }

        private boolean updateLayoutHelper() {
            int findAdapterPositionByIndex;
            if (this.mIndex < 0 || (findAdapterPositionByIndex = DelegateAdapter.this.findAdapterPositionByIndex(this.mIndex)) < 0) {
                return false;
            }
            Pair pair = (Pair) DelegateAdapter.this.mAdapters.get(findAdapterPositionByIndex);
            LinkedList linkedList = new LinkedList(DelegateAdapter.this.getLayoutHelpers());
            LayoutHelper layoutHelper = (LayoutHelper) linkedList.get(findAdapterPositionByIndex);
            if (layoutHelper.getItemCount() != ((Adapter) pair.second).getItemCount()) {
                layoutHelper.setItemCount(((Adapter) pair.second).getItemCount());
                int unused = DelegateAdapter.this.mTotal = this.mStartPosition + ((Adapter) pair.second).getItemCount();
                for (int i = findAdapterPositionByIndex + 1; i < DelegateAdapter.this.mAdapters.size(); i++) {
                    Pair pair2 = (Pair) DelegateAdapter.this.mAdapters.get(i);
                    ((AdapterDataObserver) pair2.first).mStartPosition = DelegateAdapter.this.mTotal;
                    int unused2 = DelegateAdapter.this.mTotal = DelegateAdapter.this.mTotal + ((Adapter) pair2.second).getItemCount();
                }
                DelegateAdapter.super.setLayoutHelpers(linkedList);
            }
            return true;
        }

        public void onChanged() {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyDataSetChanged();
            }
        }

        public void onItemRangeRemoved(int i, int i2) {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyItemRangeRemoved(this.mStartPosition + i, i2);
            }
        }

        public void onItemRangeInserted(int i, int i2) {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyItemRangeInserted(this.mStartPosition + i, i2);
            }
        }

        public void onItemRangeMoved(int i, int i2, int i3) {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyItemMoved(this.mStartPosition + i, this.mStartPosition + i2);
            }
        }

        public void onItemRangeChanged(int i, int i2) {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyItemRangeChanged(this.mStartPosition + i, i2);
            }
        }

        public void onItemRangeChanged(int i, int i2, Object obj) {
            if (updateLayoutHelper()) {
                DelegateAdapter.this.notifyItemRangeChanged(this.mStartPosition + i, i2, obj);
            }
        }
    }

    public static Adapter<? extends RecyclerView.ViewHolder> simpleAdapter(@NonNull View view) {
        return new SimpleViewAdapter(view);
    }

    public static Adapter<? extends RecyclerView.ViewHolder> simpleAdapter(@NonNull View view, @NonNull LayoutHelper layoutHelper) {
        return new SimpleViewAdapter(view, layoutHelper);
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View view) {
            super(view);
        }
    }

    static class SimpleViewAdapter extends Adapter<RecyclerView.ViewHolder> {
        private LayoutHelper mLayoutHelper;
        private View mView;

        public int getItemCount() {
            return 1;
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public SimpleViewAdapter(@NonNull View view, @NonNull LayoutHelper layoutHelper) {
            this.mView = view;
            this.mLayoutHelper = layoutHelper;
        }

        public SimpleViewAdapter(@NonNull View view) {
            this(view, new SingleLayoutHelper());
        }

        public LayoutHelper onCreateLayoutHelper() {
            return this.mLayoutHelper;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new SimpleViewHolder(this.mView);
        }
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
        /* access modifiers changed from: protected */
        public void onBindViewHolderWithOffset(VH vh, int i, int i2) {
        }

        public abstract LayoutHelper onCreateLayoutHelper();

        /* access modifiers changed from: protected */
        public void onBindViewHolderWithOffset(VH vh, int i, int i2, List<Object> list) {
            onBindViewHolderWithOffset(vh, i, i2);
        }
    }
}
