package com.alibaba.taffy.bus.event;

import com.alibaba.taffy.bus.Subscriber;

public class ExceptionEvent extends Event {
    private final Event source;
    private final Subscriber subscriber;
    private final Throwable throwable;

    public ExceptionEvent(Event event, Subscriber subscriber2, Throwable th) {
        this.source = event;
        this.subscriber = subscriber2;
        this.throwable = th;
        this.topic = ExceptionEvent.class.getName();
    }

    public Object getData() {
        if (this.source != null) {
            return this.source.getData();
        }
        return null;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public Subscriber getSubscriber() {
        return this.subscriber;
    }

    public static boolean instanceOf(Object obj) {
        return obj instanceof ExceptionEvent;
    }
}
