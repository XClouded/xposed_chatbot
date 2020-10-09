package com.taobao.android.dinamicx.expression.event;

public class DXSwitchEvent extends DXEvent {
    protected boolean isOn;

    public DXSwitchEvent(long j) {
        super(j);
    }

    public boolean isOn() {
        return this.isOn;
    }

    public void setOn(boolean z) {
        this.isOn = z;
    }
}
