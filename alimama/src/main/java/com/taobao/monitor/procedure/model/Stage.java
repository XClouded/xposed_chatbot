package com.taobao.monitor.procedure.model;

public class Stage {
    private final String name;
    private final long timestamp;

    public Stage(String str, long j) {
        this.name = str;
        this.timestamp = j;
    }

    public String name() {
        return this.name;
    }

    public long timestamp() {
        return this.timestamp;
    }

    public String toString() {
        return this.name;
    }
}
