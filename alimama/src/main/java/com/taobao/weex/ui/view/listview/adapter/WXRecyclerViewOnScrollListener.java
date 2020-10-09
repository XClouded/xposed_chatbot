package com.taobao.weex.ui.view.listview.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;

public class WXRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    private WeakReference<IOnLoadMoreListener> listener;
    private int mCurrentScrollState = 0;
    private int[] mFirstPositions;
    private int mFirstVisibleItemPosition;
    private int[] mLastPositions;
    private int mLastVisibleItemPosition;

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public WXRecyclerViewOnScrollListener(IOnLoadMoreListener iOnLoadMoreListener) {
        this.listener = new WeakReference<>(iOnLoadMoreListener);
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        super.onScrollStateChanged(recyclerView, i);
        this.mCurrentScrollState = i;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        int itemCount = layoutManager.getItemCount();
        if (childCount != 0) {
            int height = (((itemCount - this.mLastVisibleItemPosition) - 1) * recyclerView.getHeight()) / childCount;
            if (childCount > 0 && this.mCurrentScrollState == 0 && this.listener != null && this.listener.get() != null) {
                ((IOnLoadMoreListener) this.listener.get()).onLoadMore(height);
            }
        }
    }

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        IOnLoadMoreListener iOnLoadMoreListener;
        super.onScrolled(recyclerView, i, i2);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (this.listener != null && (iOnLoadMoreListener = (IOnLoadMoreListener) this.listener.get()) != null) {
            iOnLoadMoreListener.onBeforeScroll(i, i2);
            if (layoutManager instanceof LinearLayoutManager) {
                this.layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                this.mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                iOnLoadMoreListener.notifyAppearStateChange(linearLayoutManager.findFirstVisibleItemPosition(), this.mLastVisibleItemPosition, i, i2);
            } else if (layoutManager instanceof GridLayoutManager) {
                this.layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                this.mLastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                iOnLoadMoreListener.notifyAppearStateChange(gridLayoutManager.findFirstVisibleItemPosition(), this.mLastVisibleItemPosition, i, i2);
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                this.layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int spanCount = staggeredGridLayoutManager.getSpanCount();
                if (this.mLastPositions == null || spanCount != this.mLastPositions.length) {
                    this.mLastPositions = new int[spanCount];
                }
                if (this.mFirstPositions == null || spanCount != this.mFirstPositions.length) {
                    this.mFirstPositions = new int[spanCount];
                }
                try {
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(this.mFirstPositions);
                    this.mFirstVisibleItemPosition = findMin(this.mFirstPositions);
                    staggeredGridLayoutManager.findLastVisibleItemPositions(this.mLastPositions);
                    this.mLastVisibleItemPosition = findMax(this.mLastPositions);
                    iOnLoadMoreListener.notifyAppearStateChange(this.mFirstVisibleItemPosition, this.mLastVisibleItemPosition, i, i2);
                } catch (Exception e) {
                    e.printStackTrace();
                    WXLogUtils.e(e.toString());
                }
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
    }

    private int findMax(int[] iArr) {
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 > i) {
                i = i2;
            }
        }
        return i;
    }

    private int findMin(int[] iArr) {
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 < i) {
                i = i2;
            }
        }
        return i;
    }
}
