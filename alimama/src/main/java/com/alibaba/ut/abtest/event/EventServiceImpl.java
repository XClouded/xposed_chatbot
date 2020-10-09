package com.alibaba.ut.abtest.event;

import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventServiceImpl implements EventService {
    private static final String TAG = "EventServiceImpl";
    /* access modifiers changed from: private */
    public static ConcurrentLinkedQueue<Event> eventQueue = new ConcurrentLinkedQueue<>();
    /* access modifiers changed from: private */
    public static AtomicBoolean isNotifyEvent = new AtomicBoolean(false);
    private Map<EventType, Set<EventListener>> listeners = new HashMap();

    public void subscribeEvent(EventType eventType, EventListener eventListener) {
        if (eventType != null && eventListener != null) {
            synchronized (this) {
                Set set = this.listeners.get(eventType);
                if (set == null) {
                    set = new HashSet();
                    this.listeners.put(eventType, set);
                }
                set.add(eventListener);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unSubscribeEvent(com.alibaba.ut.abtest.event.EventType r2, com.alibaba.ut.abtest.event.EventListener r3) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x002a
            if (r3 != 0) goto L_0x0005
            goto L_0x002a
        L_0x0005:
            monitor-enter(r1)
            java.util.Map<com.alibaba.ut.abtest.event.EventType, java.util.Set<com.alibaba.ut.abtest.event.EventListener>> r0 = r1.listeners     // Catch:{ all -> 0x0027 }
            java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x0027 }
            java.util.Set r2 = (java.util.Set) r2     // Catch:{ all -> 0x0027 }
            if (r2 == 0) goto L_0x0025
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0027 }
        L_0x0014:
            boolean r0 = r2.hasNext()     // Catch:{ all -> 0x0027 }
            if (r0 == 0) goto L_0x0025
            java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x0027 }
            if (r0 != r3) goto L_0x0014
            r2.remove()     // Catch:{ all -> 0x0027 }
            monitor-exit(r1)     // Catch:{ all -> 0x0027 }
            return
        L_0x0025:
            monitor-exit(r1)     // Catch:{ all -> 0x0027 }
            return
        L_0x0027:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0027 }
            throw r2
        L_0x002a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.event.EventServiceImpl.unSubscribeEvent(com.alibaba.ut.abtest.event.EventType, com.alibaba.ut.abtest.event.EventListener):void");
    }

    public void unSubscribeEvent(EventType eventType) {
        if (eventType != null) {
            synchronized (this) {
                this.listeners.remove(eventType);
            }
        }
    }

    public void publishEvent(Event event) {
        if (event != null) {
            eventQueue.offer(event);
            if (isNotifyEvent.compareAndSet(false, true)) {
                TaskExecutor.executeBackground(new Runnable() {
                    public void run() {
                        while (!EventServiceImpl.eventQueue.isEmpty()) {
                            try {
                                Event event = (Event) EventServiceImpl.eventQueue.poll();
                                if (event != null) {
                                    EventServiceImpl.this.notifyEvent(event);
                                }
                            } catch (Throwable th) {
                                LogUtils.logE(EventServiceImpl.TAG, th.getMessage(), th);
                            }
                        }
                        EventServiceImpl.isNotifyEvent.set(false);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void notifyEvent(Event event) {
        Set<EventListener> set = this.listeners.get(event.getEventType());
        if (set != null) {
            for (EventListener onEvent : set) {
                try {
                    onEvent.onEvent(event);
                } catch (Throwable th) {
                    LogUtils.logE(TAG, th.getMessage(), th);
                }
            }
        }
    }
}
