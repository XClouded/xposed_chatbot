package com.taobao.rxm.schedule;

public interface ExecutorStateInspector {
    String getStatus();

    boolean isNotFull();
}
