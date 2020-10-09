package com.huawei.android.hms.agent.common;

import com.huawei.android.hms.agent.common.handler.ICallbackCode;

public class CallbackCodeRunnable implements Runnable {
    private ICallbackCode handlerInner;
    private int rtnCodeInner;

    public CallbackCodeRunnable(ICallbackCode iCallbackCode, int i) {
        this.handlerInner = iCallbackCode;
        this.rtnCodeInner = i;
    }

    public void run() {
        if (this.handlerInner != null) {
            this.handlerInner.onResult(this.rtnCodeInner);
        }
    }
}
