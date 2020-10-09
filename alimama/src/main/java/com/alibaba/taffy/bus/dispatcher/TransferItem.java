package com.alibaba.taffy.bus.dispatcher;

import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.event.Event;
import java.io.Serializable;

public class TransferItem implements Serializable {
    public Event event;
    public Subscriber subscriber;

    public TransferItem(Event event2, Subscriber subscriber2) {
        this.event = event2;
        this.subscriber = subscriber2;
    }
}
