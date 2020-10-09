package com.taobao.rxm.consume;

import com.taobao.rxm.schedule.Scheduler;

public interface Consumer<OUT, CONTEXT> {
    Consumer<OUT, CONTEXT> consumeOn(Scheduler scheduler);

    CONTEXT getContext();

    void onCancellation();

    void onFailure(Throwable th);

    void onNewResult(OUT out, boolean z);

    void onProgressUpdate(float f);
}
