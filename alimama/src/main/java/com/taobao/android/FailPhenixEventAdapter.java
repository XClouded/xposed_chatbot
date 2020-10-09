package com.taobao.android;

import com.taobao.phenix.intf.event.FailPhenixEvent;

public class FailPhenixEventAdapter implements AliImageFailEvent {
    private final FailPhenixEvent mFailPhenixEvent;

    public FailPhenixEventAdapter(FailPhenixEvent failPhenixEvent) {
        this.mFailPhenixEvent = failPhenixEvent;
    }
}
