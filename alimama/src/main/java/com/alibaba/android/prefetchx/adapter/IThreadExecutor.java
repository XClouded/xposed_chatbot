package com.alibaba.android.prefetchx.adapter;

public interface IThreadExecutor {
    void executeImmediately(Runnable runnable);

    void executeWithDelay(Runnable runnable, int i);
}
