package com.taobao.weex.analyzer.core;

import androidx.annotation.NonNull;

public interface TaskEntity<T> {
    void onTaskInit();

    @NonNull
    T onTaskRun();

    void onTaskStop();
}
