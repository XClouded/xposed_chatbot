package com.taobao.android;

import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;

class AliImageSuccListenerAdapter implements IPhenixListener<SuccPhenixEvent> {
    private final AliImageListener<AliImageSuccEvent> mAliImageListener;

    public AliImageSuccListenerAdapter(AliImageListener<AliImageSuccEvent> aliImageListener) {
        if (aliImageListener != null) {
            this.mAliImageListener = aliImageListener;
            return;
        }
        throw new IllegalArgumentException("aliImageListener must not be null.");
    }

    public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
        return this.mAliImageListener.onHappen(new SuccPhenixEventAdapter(succPhenixEvent));
    }
}
