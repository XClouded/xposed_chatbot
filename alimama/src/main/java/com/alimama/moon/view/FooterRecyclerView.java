package com.alimama.moon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FooterRecyclerView extends RecyclerView implements IFooterLoading {
    private FooterAdapter mAdapterWrapper;
    @Nullable
    private View mFooterView;

    public FooterRecyclerView(Context context) {
        super(context);
    }

    public FooterRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FooterRecyclerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            super.setAdapter((RecyclerView.Adapter) null);
            return;
        }
        this.mAdapterWrapper = new FooterAdapter(adapter, this.mFooterView);
        super.setAdapter(this.mAdapterWrapper);
    }

    public void onLoadingMore() {
        if (this.mFooterView instanceof IFooterLoading) {
            ((IFooterLoading) this.mFooterView).onLoadingMore();
        }
    }

    public void onNoMoreItems() {
        if (this.mFooterView instanceof IFooterLoading) {
            ((IFooterLoading) this.mFooterView).onNoMoreItems();
        }
    }

    public void setFooterView(@Nullable View view) {
        this.mFooterView = view;
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.setFooterView(view);
        }
    }

    private static class FooterAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_FOOTER = Integer.MIN_VALUE;
        @NonNull
        private final RecyclerView.Adapter mDataAdapter;
        @Nullable
        private View mFooterView;

        FooterAdapter(@NonNull RecyclerView.Adapter adapter, @Nullable View view) {
            this.mDataAdapter = adapter;
            this.mFooterView = view;
        }

        /* access modifiers changed from: package-private */
        public void setFooterView(View view) {
            this.mFooterView = view;
        }

        private boolean shouldShowFooter() {
            return this.mFooterView != null;
        }

        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (!isFooter(i)) {
                this.mDataAdapter.onBindViewHolder(viewHolder, i);
            }
        }

        @NonNull
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == Integer.MIN_VALUE) {
                return new FooterViewHolder(this.mFooterView);
            }
            return this.mDataAdapter.onCreateViewHolder(viewGroup, i);
        }

        public int getItemCount() {
            return shouldShowFooter() ? this.mDataAdapter.getItemCount() + 1 : this.mDataAdapter.getItemCount();
        }

        public int getItemViewType(int i) {
            if (!shouldShowFooter() || i != this.mDataAdapter.getItemCount()) {
                return this.mDataAdapter.getItemViewType(i);
            }
            return Integer.MIN_VALUE;
        }

        public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mDataAdapter.registerAdapterDataObserver(adapterDataObserver);
            super.registerAdapterDataObserver(adapterDataObserver);
        }

        public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mDataAdapter.unregisterAdapterDataObserver(adapterDataObserver);
            super.unregisterAdapterDataObserver(adapterDataObserver);
        }

        private boolean isFooter(int i) {
            return Integer.MIN_VALUE == getItemViewType(i);
        }

        private static class FooterViewHolder extends RecyclerView.ViewHolder {
            FooterViewHolder(View view) {
                super(view);
            }
        }
    }
}
