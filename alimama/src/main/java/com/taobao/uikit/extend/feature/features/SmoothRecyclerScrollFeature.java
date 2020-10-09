package com.taobao.uikit.extend.feature.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.uikit.feature.callback.RecyclerAdapterCallback;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.feature.view.TImageView;

public class SmoothRecyclerScrollFeature extends AbsFeature<RecyclerView> implements RecyclerAdapterCallback {
    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    public void setHost(RecyclerView recyclerView) {
        super.setHost(recyclerView);
        recyclerView.setOnScrollListener(new InnerScrollListener());
    }

    public RecyclerView.Adapter wrapAdapter(RecyclerView.Adapter adapter) {
        return (adapter == null || (adapter instanceof SmoothAdapter)) ? adapter : new SmoothAdapter(adapter);
    }

    private ImageLoadFeature getImageLoadFeature(TImageView tImageView) {
        return (ImageLoadFeature) tImageView.findFeature(ImageLoadFeature.class);
    }

    /* access modifiers changed from: private */
    public void resume(View view) {
        ImageLoadFeature imageLoadFeature;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                resume(viewGroup.getChildAt(i));
            }
        } else if ((view instanceof TImageView) && (imageLoadFeature = getImageLoadFeature((TImageView) view)) != null) {
            imageLoadFeature.resume();
        }
    }

    /* access modifiers changed from: private */
    public void pause(View view) {
        ImageLoadFeature imageLoadFeature;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                pause(viewGroup.getChildAt(i));
            }
        } else if ((view instanceof TImageView) && (imageLoadFeature = getImageLoadFeature((TImageView) view)) != null) {
            imageLoadFeature.pause();
        }
    }

    class InnerScrollListener extends RecyclerView.OnScrollListener {
        private int mLastScrollState = 0;

        InnerScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0 || (1 == i && 2 == this.mLastScrollState)) {
                SmoothRecyclerScrollFeature.this.resume(recyclerView);
            }
            this.mLastScrollState = i;
        }
    }

    class SmoothAdapter extends RecyclerView.Adapter {
        private RecyclerView.Adapter mDelegateAdapter;

        protected SmoothAdapter(RecyclerView.Adapter adapter) {
            this.mDelegateAdapter = adapter;
            super.setHasStableIds(adapter.hasStableIds());
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return this.mDelegateAdapter.onCreateViewHolder(viewGroup, i);
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (2 != ((RecyclerView) SmoothRecyclerScrollFeature.this.getHost()).getScrollState()) {
                SmoothRecyclerScrollFeature.this.resume(viewHolder.itemView);
            } else {
                SmoothRecyclerScrollFeature.this.pause(viewHolder.itemView);
            }
            this.mDelegateAdapter.onBindViewHolder(viewHolder, i);
        }

        public int getItemViewType(int i) {
            return this.mDelegateAdapter.getItemViewType(i);
        }

        public long getItemId(int i) {
            return this.mDelegateAdapter.getItemId(i);
        }

        public int getItemCount() {
            return this.mDelegateAdapter.getItemCount();
        }

        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            this.mDelegateAdapter.onViewRecycled(viewHolder);
        }

        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            this.mDelegateAdapter.onViewAttachedToWindow(viewHolder);
        }

        public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
            this.mDelegateAdapter.onViewDetachedFromWindow(viewHolder);
        }

        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mDelegateAdapter.registerAdapterDataObserver(adapterDataObserver);
        }

        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mDelegateAdapter.unregisterAdapterDataObserver(adapterDataObserver);
        }
    }
}
