package com.alimama.unwdinamicxcontainer.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = "EndlessRecyclerOnScrollListener";
    private int current_page = 1;
    int firstVisibleItem;
    private boolean loading = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private int previousTotal = 0;
    int totalItemCount;
    int visibleItemCount;
    private int visibleThreshold = 6;

    public abstract void onLoadMore(int i);

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        super.onScrolled(recyclerView, i, i2);
        this.visibleItemCount = recyclerView.getChildCount();
        this.totalItemCount = this.mLayoutManager.getItemCount();
        if (this.mLayoutManager instanceof LinearLayoutManager) {
            this.firstVisibleItem = ((LinearLayoutManager) this.mLayoutManager).findFirstVisibleItemPosition();
        } else if (this.mLayoutManager instanceof StaggeredGridLayoutManager) {
            int spanCount = ((StaggeredGridLayoutManager) this.mLayoutManager).getSpanCount();
            int[] iArr = new int[spanCount];
            this.firstVisibleItem = ((StaggeredGridLayoutManager) this.mLayoutManager).findFirstVisibleItemPositions(new int[spanCount])[0];
        }
        if (this.totalItemCount - this.visibleItemCount <= this.firstVisibleItem + this.visibleThreshold) {
            this.current_page++;
            onLoadMore(this.current_page);
            this.loading = true;
        }
    }
}
