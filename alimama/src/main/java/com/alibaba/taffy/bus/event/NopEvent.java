package com.alibaba.taffy.bus.event;

public class NopEvent extends Event {
    public NopEvent() {
        this.topic = NopEvent.class.getName();
    }

    public static boolean instanceOf(Object obj) {
        return obj instanceof NopEvent;
    }
}
