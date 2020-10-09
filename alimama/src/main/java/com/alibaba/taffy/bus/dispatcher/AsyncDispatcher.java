package com.alibaba.taffy.bus.dispatcher;

import android.util.Log;
import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.TBus;
import com.alibaba.taffy.bus.event.Event;
import com.alibaba.taffy.bus.event.NopEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncDispatcher extends AbstractDispatcher implements Runnable {
    private static final String TAG = "AsyncTransfer";
    private AtomicBoolean isWorking = new AtomicBoolean(false);
    private BlockingQueue<TransferItem> queue = new LinkedBlockingQueue();

    public boolean accept(Subscriber subscriber) {
        return subscriber.getThread() == 1;
    }

    public AsyncDispatcher() {
    }

    public AsyncDispatcher(AbstractDispatcher abstractDispatcher) {
        super(abstractDispatcher);
    }

    public AsyncDispatcher(AbstractDispatcher abstractDispatcher, TBus tBus, ExecutorService executorService) {
        super(abstractDispatcher, tBus, executorService);
    }

    public EventStatus handle(Event event, Subscriber subscriber) {
        this.queue.offer(new TransferItem(event, subscriber));
        if (this.isWorking.compareAndSet(false, true)) {
            this.executor.execute(this);
        }
        return EventStatus.SUCCESS;
    }

    public void stop() {
        if (this.isWorking.compareAndSet(true, false)) {
            this.queue.offer(new TransferItem(new NopEvent(), (Subscriber) null));
        }
    }

    public void run() {
        while (this.isWorking.get()) {
            try {
                TransferItem take = this.queue.take();
                if (take != null) {
                    _fire(take.event, take.subscriber);
                }
            } catch (Exception e) {
                this.isWorking.set(false);
                Log.d(TAG, e.getMessage(), e);
                return;
            }
        }
    }
}
