package com.taobao.android.dinamicx.expression.event;

public class DXViewEvent extends DXEvent {
    private int itemIndex;

    public int getItemIndex() {
        return this.itemIndex;
    }

    public void setItemIndex(int i) {
        this.itemIndex = i;
    }

    public DXViewEvent(long j) {
        super(j);
    }
}
