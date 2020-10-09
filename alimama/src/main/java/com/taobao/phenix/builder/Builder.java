package com.taobao.phenix.builder;

public interface Builder<T> {
    T build();

    Builder<T> with(T t);
}
