package com.taobao.monitor.procedure.model;

import java.util.Map;

public class Event {
    private final String name;
    private Map<String, Object> properties;
    private final long timestamp;

    public Event(String str, Map<String, Object> map) {
        this(str, System.currentTimeMillis());
        this.properties = map;
    }

    public Event(String str, long j) {
        this.name = str;
        this.timestamp = j;
    }

    public String name() {
        return this.name;
    }

    public long timestamp() {
        return this.timestamp;
    }

    public Map<String, Object> properties() {
        return this.properties;
    }

    public Event setProperties(Map<String, Object> map) {
        this.properties = map;
        return this;
    }

    public String toString() {
        return "Event{name='" + this.name + '\'' + ", timestamp=" + this.timestamp + '}';
    }
}
