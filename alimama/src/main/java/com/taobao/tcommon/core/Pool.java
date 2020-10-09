package com.taobao.tcommon.core;

public interface Pool<T> {
    T offer();

    boolean recycle(T t);
}
