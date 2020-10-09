package com.taobao.fresco.disk.common;

public class SystemClock implements Clock {
    private static final SystemClock INSTANCE = new SystemClock();

    private SystemClock() {
    }

    public static SystemClock get() {
        return INSTANCE;
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
