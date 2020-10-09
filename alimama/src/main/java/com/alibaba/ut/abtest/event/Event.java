package com.alibaba.ut.abtest.event;

public class Event<T> {
    private EventType eventType;
    private T eventValue;

    public Event() {
    }

    public Event(EventType eventType2, T t) {
        this.eventType = eventType2;
        this.eventValue = t;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType2) {
        this.eventType = eventType2;
    }

    public T getEventValue() {
        return this.eventValue;
    }

    public void setEventValue(T t) {
        this.eventValue = t;
    }
}
