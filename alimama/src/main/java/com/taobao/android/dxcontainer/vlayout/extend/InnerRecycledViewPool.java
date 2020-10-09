package com.taobao.android.dxcontainer.vlayout.extend;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Closeable;

public final class InnerRecycledViewPool extends RecyclerView.RecycledViewPool {
    private static int DEFAULT_MAX_SIZE = 20;
    private static final String TAG = "InnerRecycledViewPool";
    private RecyclerView.RecycledViewPool mInnerPool;
    private SparseIntArray mMaxScrap;
    private SparseIntArray mScrapLength;

    public InnerRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool) {
        this.mScrapLength = new SparseIntArray();
        this.mMaxScrap = new SparseIntArray();
        this.mInnerPool = recycledViewPool;
    }

    public InnerRecycledViewPool() {
        this(new RecyclerView.RecycledViewPool());
    }

    public void clear() {
        int size = this.mScrapLength.size();
        for (int i = 0; i < size; i++) {
            int keyAt = this.mScrapLength.keyAt(i);
            RecyclerView.ViewHolder recycledView = this.mInnerPool.getRecycledView(keyAt);
            while (recycledView != null) {
                destroyViewHolder(recycledView);
                recycledView = this.mInnerPool.getRecycledView(keyAt);
            }
        }
        this.mScrapLength.clear();
        super.clear();
    }

    public void setMaxRecycledViews(int i, int i2) {
        RecyclerView.ViewHolder recycledView = this.mInnerPool.getRecycledView(i);
        while (recycledView != null) {
            destroyViewHolder(recycledView);
            recycledView = this.mInnerPool.getRecycledView(i);
        }
        this.mMaxScrap.put(i, i2);
        this.mScrapLength.put(i, 0);
        this.mInnerPool.setMaxRecycledViews(i, i2);
    }

    public RecyclerView.ViewHolder getRecycledView(int i) {
        RecyclerView.ViewHolder recycledView = this.mInnerPool.getRecycledView(i);
        if (recycledView != null) {
            int i2 = this.mScrapLength.indexOfKey(i) >= 0 ? this.mScrapLength.get(i) : 0;
            if (i2 > 0) {
                this.mScrapLength.put(i, i2 - 1);
            }
        }
        return recycledView;
    }

    public int size() {
        int size = this.mScrapLength.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += this.mScrapLength.valueAt(i2);
        }
        return i;
    }

    public void putRecycledView(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        if (this.mMaxScrap.indexOfKey(itemViewType) < 0) {
            this.mMaxScrap.put(itemViewType, DEFAULT_MAX_SIZE);
            setMaxRecycledViews(itemViewType, DEFAULT_MAX_SIZE);
        }
        int i = this.mScrapLength.indexOfKey(itemViewType) >= 0 ? this.mScrapLength.get(itemViewType) : 0;
        if (this.mMaxScrap.get(itemViewType) > i) {
            this.mInnerPool.putRecycledView(viewHolder);
            this.mScrapLength.put(itemViewType, i + 1);
            return;
        }
        destroyViewHolder(viewHolder);
    }

    private void destroyViewHolder(RecyclerView.ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        if (view instanceof Closeable) {
            try {
                ((Closeable) view).close();
            } catch (Exception e) {
                Log.w(TAG, Log.getStackTraceString(e), e);
            }
        }
        if (viewHolder instanceof Closeable) {
            try {
                ((Closeable) viewHolder).close();
            } catch (Exception e2) {
                Log.w(TAG, Log.getStackTraceString(e2), e2);
            }
        }
    }

    public void setDefaultMaxSize(int i) {
        DEFAULT_MAX_SIZE = i;
    }
}
