package com.alibaba.ut.abtest.event;

public interface EventService {
    void publishEvent(Event event);

    void subscribeEvent(EventType eventType, EventListener eventListener);

    void unSubscribeEvent(EventType eventType);

    void unSubscribeEvent(EventType eventType, EventListener eventListener);
}
