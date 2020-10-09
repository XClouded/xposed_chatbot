package com.taobao.phenix.intf.event;

import com.taobao.phenix.intf.PhenixTicket;

public class ProgressPhenixEvent extends PhenixEvent {
    private final float mProgress;

    public ProgressPhenixEvent(PhenixTicket phenixTicket, float f) {
        super(phenixTicket);
        this.mProgress = f;
    }

    public float getProgress() {
        return this.mProgress;
    }
}
