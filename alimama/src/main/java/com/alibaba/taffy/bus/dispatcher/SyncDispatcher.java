package com.alibaba.taffy.bus.dispatcher;

import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.TBus;
import com.alibaba.taffy.bus.event.Event;
import java.util.concurrent.ExecutorService;

public class SyncDispatcher extends AbstractDispatcher {
    public SyncDispatcher() {
    }

    public SyncDispatcher(AbstractDispatcher abstractDispatcher) {
        super(abstractDispatcher);
    }

    public SyncDispatcher(AbstractDispatcher abstractDispatcher, TBus tBus, ExecutorService executorService) {
        super(abstractDispatcher, tBus, executorService);
    }

    public boolean accept(Subscriber subscriber) {
        return subscriber.getThread() == 0;
    }

    public EventStatus handle(Event event, Subscriber subscriber) {
        return _fire(event, subscriber);
    }
}
