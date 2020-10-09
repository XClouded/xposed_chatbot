package com.taobao.monitor.impl.data;

public interface IInteractiveDetector extends IExecutor {

    public interface IDetectorCallback {
        void completed(long j);
    }
}
