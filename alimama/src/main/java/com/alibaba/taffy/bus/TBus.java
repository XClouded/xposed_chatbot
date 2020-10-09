package com.alibaba.taffy.bus;

import android.os.SystemClock;
import com.alibaba.taffy.bus.dispatcher.AbstractDispatcher;
import com.alibaba.taffy.bus.dispatcher.AsyncDispatcher;
import com.alibaba.taffy.bus.dispatcher.BackgroundDispatcher;
import com.alibaba.taffy.bus.dispatcher.EventDispatcher;
import com.alibaba.taffy.bus.dispatcher.MainDispatcher;
import com.alibaba.taffy.bus.dispatcher.SyncDispatcher;
import com.alibaba.taffy.bus.event.Event;
import com.alibaba.taffy.bus.event.ExceptionEvent;
import com.alibaba.taffy.bus.event.NotConsumedEvent;
import com.alibaba.taffy.bus.lookup.AnnotationLookup;
import com.alibaba.taffy.bus.lookup.LookupListener;
import com.alibaba.taffy.bus.lookup.SubscriberLookup;
import com.alibaba.taffy.core.collection.PriorityBlockingArraySet;
import com.alibaba.taffy.core.util.TAssert;
import com.alibaba.taffy.core.util.lang.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class TBus implements LookupListener {
    private final EventDispatcher dispatcher = new MainDispatcher(new BackgroundDispatcher(new AsyncDispatcher(new SyncDispatcher((AbstractDispatcher) null, this, this.executor))));
    private boolean enabled = true;
    private SubscriberLookup eventLookup = new AnnotationLookup();
    private final ThreadLocal<EventQueue> eventQueue = new ThreadLocal<EventQueue>() {
        /* access modifiers changed from: protected */
        public EventQueue initialValue() {
            return new EventQueue();
        }
    };
    private final ConcurrentHashMap<Class<?>, List<Class<?>>> eventTypesCache = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final AtomicLong idGenerator = new AtomicLong();
    private final ConcurrentHashMap<Object, Map<String, Collection<Subscriber>>> lookupCache = new ConcurrentHashMap<>();
    private final SubscriberComparator subscriberComparator = new SubscriberComparator();
    private final ConcurrentHashMap<String, Set<Subscriber>> subscriptions = new ConcurrentHashMap<>();
    private boolean useEventInheritance = false;
    private boolean useSendExceptionEvent = true;
    private boolean useSendNotConsumedEvent = true;

    @Deprecated
    public TBus useEventInheritance(boolean z) {
        this.useEventInheritance = z;
        return this;
    }

    public TBus useSendNotConsumedEvent(boolean z) {
        this.useSendNotConsumedEvent = z;
        return this;
    }

    public TBus useSendExceptionEvent(boolean z) {
        this.useSendExceptionEvent = z;
        return this;
    }

    @Deprecated
    public boolean isUseEventInheritance() {
        return this.useEventInheritance;
    }

    public boolean isUseSendNotConsumedEvent() {
        return this.useSendNotConsumedEvent;
    }

    public boolean isUseSendExceptionEvent() {
        return this.useSendExceptionEvent;
    }

    public void setSubscriberStatus(Object obj, int i, String str) {
        Map map = this.lookupCache.get(obj);
        if (map != null) {
            for (Map.Entry value : map.entrySet()) {
                for (Subscriber subscriber : (Collection) value.getValue()) {
                    if (StringUtil.equals(subscriber.getGroup(), str)) {
                        subscriber.setStatus(i);
                    }
                }
            }
        }
    }

    public void setSubscriberStatus(String str, int i, String str2) {
        for (Subscriber subscriber : this.subscriptions.get(str)) {
            if (StringUtil.equals(subscriber.getGroup(), str2)) {
                subscriber.setStatus(i);
            }
        }
    }

    public void bind(Object obj) {
        if (!this.lookupCache.contains(obj)) {
            Map findAll = this.eventLookup.findAll(obj, this);
            if (this.lookupCache.putIfAbsent(obj, findAll) == null) {
                for (Map.Entry entry : findAll.entrySet()) {
                    String str = (String) entry.getKey();
                    Collection collection = (Collection) entry.getValue();
                    Set set = this.subscriptions.get(str);
                    if (set == null) {
                        PriorityBlockingArraySet priorityBlockingArraySet = new PriorityBlockingArraySet(this.subscriberComparator);
                        priorityBlockingArraySet.addAll(collection);
                        Set putIfAbsent = this.subscriptions.putIfAbsent(str, priorityBlockingArraySet);
                        if (putIfAbsent != null) {
                            putIfAbsent.addAll(collection);
                        }
                    } else {
                        set.addAll(collection);
                    }
                }
            }
        }
    }

    public void bind(Subscriber subscriber) {
        if (subscriber != null && subscriber.getId() == -1 && subscriber.getListener() != null) {
            Set set = this.subscriptions.get(subscriber.getTopic());
            subscriber.setId(makeId());
            if (set == null) {
                PriorityBlockingArraySet priorityBlockingArraySet = new PriorityBlockingArraySet(this.subscriberComparator);
                priorityBlockingArraySet.add(subscriber);
                Set putIfAbsent = this.subscriptions.putIfAbsent(subscriber.getTopic(), priorityBlockingArraySet);
                if (putIfAbsent != null) {
                    putIfAbsent.add(subscriber);
                    return;
                }
                return;
            }
            set.add(subscriber);
        }
    }

    public void unbindAll() {
        this.subscriptions.clear();
    }

    public void unbind(String str) {
        this.subscriptions.remove(str);
    }

    public void unbind(Subscriber subscriber) {
        Set set = this.subscriptions.get(subscriber.getTopic());
        if (set != null) {
            set.remove(subscriber);
            if (set.isEmpty()) {
                this.subscriptions.remove(subscriber.getTopic());
            }
        }
    }

    public void unbind(Object obj) {
        Set set;
        Map remove = this.lookupCache.remove(obj);
        if (remove != null) {
            for (Map.Entry entry : remove.entrySet()) {
                String str = (String) entry.getKey();
                Collection collection = (Collection) entry.getValue();
                if (!(collection == null || (set = this.subscriptions.get(str)) == null)) {
                    set.removeAll(collection);
                    if (set.isEmpty()) {
                        this.subscriptions.remove(str);
                    }
                }
            }
        }
    }

    public void unbind(String str, Object obj) {
        Collection collection;
        Set set;
        Map map = this.lookupCache.get(obj);
        if (map != null && (collection = (Collection) map.get(str)) != null && (set = this.subscriptions.get(str)) != null) {
            set.removeAll(collection);
            if (set.isEmpty()) {
                this.subscriptions.remove(str);
            }
        }
    }

    public void destroy() {
        this.enabled = false;
        this.eventTypesCache.clear();
        this.subscriptions.clear();
        for (EventDispatcher eventDispatcher = this.dispatcher; eventDispatcher != null; eventDispatcher = eventDispatcher.parent()) {
            eventDispatcher.stop();
        }
        this.executor.shutdown();
    }

    public void setEventLookup(SubscriberLookup subscriberLookup) {
        this.eventLookup = subscriberLookup;
    }

    public void fire(Object obj) {
        fire(obj, EventMode.BROADCAST);
    }

    public void fire(Object obj, EventMode eventMode) {
        fire(obj, (String) null, eventMode);
    }

    public void fire(Object obj, String str, EventMode eventMode) {
        Class<?> cls = obj.getClass();
        Event event = new Event();
        event.setTopic(cls.getName());
        event.setTag(str);
        event.setData(obj);
        fire(event, eventMode);
    }

    public void fire(Event event) {
        fire(event, EventMode.BROADCAST);
    }

    public void fire(Event event, EventMode eventMode) {
        TAssert.notNull(event);
        if (this.enabled) {
            event.setCreateTimestamp(SystemClock.elapsedRealtime());
            EventQueue eventQueue2 = this.eventQueue.get();
            eventQueue2.offer(event);
            while (!eventQueue2.isEmpty()) {
                Event poll = eventQueue2.poll();
                if (poll != null) {
                    __fire(poll, eventMode);
                }
            }
        }
    }

    private boolean __fire(Event event, EventMode eventMode) {
        Set<Subscriber> set = this.subscriptions.get(event.getTopic());
        boolean z = false;
        if (set != null) {
            for (Subscriber subscriber : set) {
                if (subscriber.getStatus() != 1) {
                    String tag = event.getTag();
                    if (StringUtil.isNotEmpty(tag)) {
                        String filter = subscriber.getFilter();
                        if (StringUtil.isEmpty(filter)) {
                            continue;
                        } else if (!tag.equals(filter)) {
                            continue;
                        }
                    }
                    EventStatus fire = this.dispatcher.fire(event, subscriber);
                    if (fire == EventStatus.SUCCESS) {
                        z = true;
                    }
                    if (eventMode == EventMode.FIRST_ONE && fire != EventStatus.ABORT) {
                        break;
                    }
                }
            }
        }
        if (!z && this.useSendNotConsumedEvent && !NotConsumedEvent.instanceOf(event) && !ExceptionEvent.instanceOf(event)) {
            fire((Event) new NotConsumedEvent(event));
        }
        return z;
    }

    private List<Class<?>> flattenHierarchy(Class<?> cls) {
        List<Class<?>> list = this.eventTypesCache.get(cls);
        if (list == null) {
            list = new ArrayList<>();
            for (Class<?> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
                list.add(cls2);
                flattenInterface(list, cls2.getInterfaces());
            }
            this.eventTypesCache.put(cls, list);
        }
        return list;
    }

    private void flattenInterface(List<Class<?>> list, Class<?>[] clsArr) {
        for (Class<?> cls : clsArr) {
            list.add(cls);
            flattenInterface(list, cls.getInterfaces());
        }
    }

    private long makeId() {
        return this.idGenerator.incrementAndGet();
    }

    public int getTopicCount() {
        return this.subscriptions.size();
    }

    public int getSubscriberCount() {
        int i = 0;
        for (Set<Subscriber> size : this.subscriptions.values()) {
            i += size.size();
        }
        return i;
    }

    public Subscriber onLookup(Subscriber subscriber) {
        subscriber.setId(makeId());
        return subscriber;
    }
}
