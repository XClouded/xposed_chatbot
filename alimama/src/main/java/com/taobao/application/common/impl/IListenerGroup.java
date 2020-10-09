package com.taobao.application.common.impl;

public interface IListenerGroup<T> {
    void addListener(T t);

    void removeListener(T t);
}
