package com.alibaba.taffy.bus.dispatcher;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.TBus;
import com.alibaba.taffy.bus.event.Event;
import com.alibaba.taffy.bus.exception.EventTransferException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class MainDispatcher extends AbstractDispatcher implements EventDispatcher {
    private Handler handler = new MyHandle(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public BlockingQueue<TransferItem> queue = new LinkedBlockingQueue();

    public MainDispatcher() {
    }

    public MainDispatcher(AbstractDispatcher abstractDispatcher) {
        super(abstractDispatcher);
    }

    public MainDispatcher(AbstractDispatcher abstractDispatcher, TBus tBus, ExecutorService executorService) {
        super(abstractDispatcher, tBus, executorService);
    }

    public boolean accept(Subscriber subscriber) {
        return subscriber.getThread() == 2;
    }

    public EventStatus handle(Event event, Subscriber subscriber) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            return _fire(event, subscriber);
        }
        this.queue.offer(new TransferItem(event, subscriber));
        if (this.handler.sendMessage(this.handler.obtainMessage())) {
            return EventStatus.SUCCESS;
        }
        throw new EventTransferException("Dispatcher Event Fail");
    }

    private class MyHandle extends Handler {
        public MyHandle(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            while (!MainDispatcher.this.queue.isEmpty()) {
                TransferItem transferItem = (TransferItem) MainDispatcher.this.queue.poll();
                if (transferItem != null) {
                    MainDispatcher.this._fire(transferItem.event, transferItem.subscriber);
                }
            }
        }
    }
}
