package com.huawei.android.hms.agent.common;

import com.huawei.android.hms.agent.common.handler.ICallbackResult;

public class CallbackResultRunnable<R> implements Runnable {
    private ICallbackResult<R> handlerInner;
    private R resultInner;
    private int rtnCodeInner;

    public CallbackResultRunnable(ICallbackResult<R> iCallbackResult, int i, R r) {
        this.handlerInner = iCallbackResult;
        this.rtnCodeInner = i;
        this.resultInner = r;
    }

    public void run() {
        if (this.handlerInner != null) {
            this.handlerInner.onResult(this.rtnCodeInner, this.resultInner);
        }
    }
}
