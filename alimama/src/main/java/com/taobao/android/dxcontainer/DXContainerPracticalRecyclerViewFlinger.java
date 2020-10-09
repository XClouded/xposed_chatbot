package com.taobao.android.dxcontainer;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

class DXContainerPracticalRecyclerViewFlinger implements Runnable {
    private int direction = 1;
    private int lastTop;
    private VirtualLayoutManager layoutManager;
    private DXContainerScrollFinishedListener mFinishedListener;
    private RecyclerView mRecyclerView;
    private int offset;
    private int step;
    private int targetPosition;

    DXContainerPracticalRecyclerViewFlinger(RecyclerView recyclerView, int i, int i2, DXContainerScrollFinishedListener dXContainerScrollFinishedListener) {
        int i3 = 1;
        this.mRecyclerView = recyclerView;
        this.targetPosition = i;
        this.offset = i2;
        this.mFinishedListener = dXContainerScrollFinishedListener;
        if (this.mRecyclerView != null) {
            this.layoutManager = (VirtualLayoutManager) this.mRecyclerView.getLayoutManager();
            this.direction = this.layoutManager.findFirstVisibleItemPosition() >= i ? -1 : i3;
            this.step = this.mRecyclerView.getMeasuredHeight() / 2;
        }
    }

    public void run() {
        if (this.mRecyclerView != null) {
            if (this.targetPosition >= this.layoutManager.findFirstVisibleItemPosition() && this.targetPosition <= this.layoutManager.findLastVisibleItemPosition()) {
                View findViewByPosition = this.mRecyclerView.getLayoutManager().findViewByPosition(this.targetPosition);
                if (findViewByPosition != null) {
                    int top = findViewByPosition.getTop();
                    this.mRecyclerView.smoothScrollBy(0, top - this.offset);
                    if (this.lastTop != top) {
                        this.lastTop = top;
                        postOnAnimation();
                    } else if (this.mFinishedListener != null) {
                        this.mFinishedListener.onPostExecute(findViewByPosition);
                        stop();
                    }
                }
            } else {
                this.mRecyclerView.smoothScrollBy(0, this.step * this.direction);
                postOnAnimation();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void postOnAnimation() {
        if (this.mRecyclerView != null) {
            ViewCompat.postOnAnimation(this.mRecyclerView, this);
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        this.mFinishedListener = null;
        if (this.mRecyclerView != null) {
            this.mRecyclerView.removeCallbacks(this);
        }
    }
}
