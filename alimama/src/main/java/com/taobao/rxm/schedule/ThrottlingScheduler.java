package com.taobao.rxm.schedule;

public interface ThrottlingScheduler extends Scheduler {
    void setMaxRunningCount(int i);
}
