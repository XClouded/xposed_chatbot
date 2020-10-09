package com.taobao.rxm.schedule;

public class ScheduleResultWrapper<OUT> {
    public int consumeType;
    public boolean isLast;
    public OUT newResult;
    public float progress;
    public Throwable throwable;

    public ScheduleResultWrapper(int i, boolean z) {
        this.consumeType = i;
        this.isLast = z;
    }

    public String toString() {
        return "type:" + this.consumeType + ",isLast:" + this.isLast;
    }
}
