package com.alimama.union.app.toolCenter.view;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    private final int mOffsetInPixels;

    public SimpleItemDecoration(int i) {
        this.mOffsetInPixels = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.top = this.mOffsetInPixels;
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() - 1 == recyclerView.getChildAdapterPosition(view)) {
            rect.bottom = this.mOffsetInPixels;
        }
    }
}
