package com.taobao.rxm.schedule;

public interface SchedulerSupplier {
    Scheduler forCpuBound();

    Scheduler forDecode();

    Scheduler forIoBound();

    Scheduler forNetwork();

    Scheduler forUiThread();
}
