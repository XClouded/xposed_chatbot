package com.alibaba.taffy.bus.dispatcher;

import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.event.Event;

public interface EventDispatcher {
    boolean accept(Subscriber subscriber);

    EventStatus fire(Event event, Subscriber subscriber);

    EventDispatcher parent();

    void stop();
}
