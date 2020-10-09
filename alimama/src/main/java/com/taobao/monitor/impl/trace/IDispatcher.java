package com.taobao.monitor.impl.trace;

public interface IDispatcher<LISTENER> {
    void addListener(LISTENER listener);

    void removeListener(LISTENER listener);
}
