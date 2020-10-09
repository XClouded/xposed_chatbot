package com.taobao.phenix.request;

public interface ImageFlowMonitor {
    int getMinimumScheduleTime2StatWaitSize();

    void onCancel(ImageStatistics imageStatistics);

    void onFail(ImageStatistics imageStatistics, Throwable th);

    void onStart(ImageStatistics imageStatistics);

    void onSuccess(ImageStatistics imageStatistics);
}
