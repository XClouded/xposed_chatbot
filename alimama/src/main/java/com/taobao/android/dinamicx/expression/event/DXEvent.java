package com.taobao.android.dinamicx.expression.event;

public class DXEvent {
    protected long eventId;
    protected boolean isPrepareBind;

    public DXEvent(long j) {
        this.eventId = j;
    }

    public long getEventId() {
        return this.eventId;
    }

    public void setEventId(long j) {
        this.eventId = j;
    }

    public boolean isPrepareBind() {
        return this.isPrepareBind;
    }

    public void setPrepareBind(boolean z) {
        this.isPrepareBind = z;
    }
}
