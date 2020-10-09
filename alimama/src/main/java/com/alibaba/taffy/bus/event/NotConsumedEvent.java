package com.alibaba.taffy.bus.event;

public class NotConsumedEvent extends Event {
    private final Event source;

    public NotConsumedEvent(Event event) {
        this.source = event;
        this.topic = NotConsumedEvent.class.getName();
    }

    public Object getData() {
        if (this.source != null) {
            return this.source.getData();
        }
        return null;
    }

    public static boolean instanceOf(Object obj) {
        return obj instanceof NotConsumedEvent;
    }
}
