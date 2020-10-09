package com.taobao.application.common.impl;

public interface ICallbackGroup<T> {
    void addCallback(T t);

    void removeCallback(T t);
}
