package com.taobao.monitor.impl.data.gc;

public class GCDetector {
    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        GCSignalSender.sendGCSignal();
    }
}
