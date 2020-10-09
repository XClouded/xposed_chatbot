package com.alibaba.ut.abtest.event;

public interface EventListener<T> {
    void onEvent(Event<T> event) throws Exception;
}
