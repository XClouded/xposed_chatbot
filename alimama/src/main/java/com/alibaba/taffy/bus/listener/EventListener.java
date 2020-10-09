package com.alibaba.taffy.bus.listener;

import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.event.Event;

public interface EventListener {
    EventStatus onEvent(Event event);
}
