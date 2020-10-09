package com.alibaba.android.common;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface IThreadPool {
    public static final int PRIORITY_HEIGH = -10;
    public static final int PRIORITY_LOW = 10;
    public static final int PRIORITY_MID = 0;

    void shutdown();

    Future<?> submit(Runnable runnable, int i);

    <T> Future<T> submit(Callable<T> callable, int i);
}
