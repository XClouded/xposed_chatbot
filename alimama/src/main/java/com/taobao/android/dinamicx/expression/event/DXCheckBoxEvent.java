package com.taobao.android.dinamicx.expression.event;

public class DXCheckBoxEvent extends DXEvent {
    protected boolean isChecked;

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public DXCheckBoxEvent(long j) {
        super(j);
    }
}
