package com.taobao.fresco.disk.common;

public interface Clock {
    public static final long MAX_TIME = Long.MAX_VALUE;

    long now();
}
