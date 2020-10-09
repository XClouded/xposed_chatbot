package com.taobao.android.dinamicx.expression.event;

import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamicx.ItemSize;

public class DXScrollEvent extends DXEvent {
    private ItemSize containerSize;
    private ItemSize contentSize;
    private int offsetX;
    private int offsetY;
    private RecyclerView recyclerView;

    public DXScrollEvent(long j) {
        super(j);
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView2) {
        this.recyclerView = recyclerView2;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(int i) {
        this.offsetX = i;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(int i) {
        this.offsetY = i;
    }

    public ItemSize getContentSize() {
        return this.contentSize;
    }

    public void setContentSize(ItemSize itemSize) {
        this.contentSize = itemSize;
    }

    public ItemSize getScrollerSize() {
        return this.containerSize;
    }

    public void setScrollerSize(ItemSize itemSize) {
        this.containerSize = itemSize;
    }
}
