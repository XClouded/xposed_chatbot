package com.alibaba.taffy.bus.dispatcher;

import android.os.SystemClock;
import android.util.Log;
import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.TBus;
import com.alibaba.taffy.bus.event.Event;
import com.alibaba.taffy.bus.event.ExceptionEvent;
import com.alibaba.taffy.bus.event.NopEvent;
import java.util.concurrent.ExecutorService;

public abstract class AbstractDispatcher implements EventDispatcher {
    private static final String TAG = "AbstractTransfer";
    protected TBus bus;
    protected ExecutorService executor;
    protected AbstractDispatcher parent;

    /* access modifiers changed from: protected */
    public abstract EventStatus handle(Event event, Subscriber subscriber);

    public void stop() {
    }

    public AbstractDispatcher() {
    }

    public AbstractDispatcher(AbstractDispatcher abstractDispatcher) {
        if (abstractDispatcher != null) {
            this.parent = abstractDispatcher;
            this.bus = abstractDispatcher.bus;
            this.executor = abstractDispatcher.executor;
        }
    }

    public AbstractDispatcher(AbstractDispatcher abstractDispatcher, TBus tBus, ExecutorService executorService) {
        this.parent = abstractDispatcher;
        this.bus = tBus;
        this.executor = executorService;
    }

    public EventDispatcher parent() {
        return this.parent;
    }

    public EventStatus fire(Event event, Subscriber subscriber) {
        if (accept(subscriber)) {
            return handle(event, subscriber);
        }
        if (this.parent != null) {
            return this.parent.fire(event, subscriber);
        }
        return EventStatus.ABORT;
    }

    /* access modifiers changed from: protected */
    public EventStatus _fire(Event event, Subscriber subscriber) {
        event.setSendTimestamp(SystemClock.elapsedRealtime());
        if (NopEvent.instanceOf(event)) {
            return EventStatus.IGNORE;
        }
        try {
            return subscriber.getListener().onEvent(event);
        } catch (Throwable th) {
            Log.d(TAG, th.getMessage(), th);
            if (this.bus.isUseSendExceptionEvent() && !ExceptionEvent.instanceOf(event)) {
                this.bus.fire((Event) new ExceptionEvent(event, subscriber, th));
            }
            return EventStatus.FAIL;
        }
    }
}
