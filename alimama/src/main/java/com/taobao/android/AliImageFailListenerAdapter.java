package com.taobao.android;

import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;

class AliImageFailListenerAdapter implements IPhenixListener<FailPhenixEvent> {
    private final AliImageListener<AliImageFailEvent> mAliImageListener;

    public AliImageFailListenerAdapter(AliImageListener<AliImageFailEvent> aliImageListener) {
        if (aliImageListener != null) {
            this.mAliImageListener = aliImageListener;
            return;
        }
        throw new IllegalArgumentException("aliImageListener must not be null.");
    }

    public boolean onHappen(FailPhenixEvent failPhenixEvent) {
        return this.mAliImageListener.onHappen(new FailPhenixEventAdapter(failPhenixEvent));
    }
}
