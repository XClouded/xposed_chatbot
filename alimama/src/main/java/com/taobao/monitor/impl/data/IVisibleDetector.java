package com.taobao.monitor.impl.data;

public interface IVisibleDetector extends IExecutor {

    public interface IDetectorCallback {
        void changed(long j);

        void completed(long j);

        void validElement(int i);
    }
}
