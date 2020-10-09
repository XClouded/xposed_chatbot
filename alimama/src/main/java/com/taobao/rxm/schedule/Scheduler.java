package com.taobao.rxm.schedule;

public interface Scheduler {
    int getQueueSize();

    String getStatus();

    boolean isScheduleMainThread();

    void schedule(ScheduledAction scheduledAction);
}
