package com.taobao.android;

import android.graphics.drawable.BitmapDrawable;
import com.taobao.phenix.intf.event.SuccPhenixEvent;

public class SuccPhenixEventAdapter implements AliImageSuccEvent {
    private final SuccPhenixEvent mSuccPhenixEvent;

    public SuccPhenixEventAdapter(SuccPhenixEvent succPhenixEvent) {
        this.mSuccPhenixEvent = succPhenixEvent;
    }

    public BitmapDrawable getDrawable() {
        return this.mSuccPhenixEvent.getDrawable();
    }
}
