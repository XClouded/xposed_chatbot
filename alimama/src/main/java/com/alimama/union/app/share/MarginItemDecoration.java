package com.alimama.union.app.share;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private final int mRightMarginPixels;

    public MarginItemDecoration(int i) {
        this.mRightMarginPixels = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            rect.right = this.mRightMarginPixels;
        } else if (recyclerView.getChildAdapterPosition(view) == adapter.getItemCount() - 1) {
            rect.right = 0;
        } else {
            rect.right = this.mRightMarginPixels;
        }
    }
}
