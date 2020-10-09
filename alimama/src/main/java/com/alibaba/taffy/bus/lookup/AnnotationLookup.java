package com.alibaba.taffy.bus.lookup;

import com.alibaba.taffy.bus.Subscriber;
import com.alibaba.taffy.bus.annotation.Subscribe;
import com.alibaba.taffy.bus.exception.IllegalSubscriberException;
import com.alibaba.taffy.bus.listener.AnnotationEventListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class AnnotationLookup implements SubscriberLookup {
    private final WeakHashMap<Class<?>, Collection<CacheItem>> cache = new WeakHashMap<>();

    public Map<String, Collection<Subscriber>> findAll(Object obj, LookupListener lookupListener) {
        Class<?> cls = obj.getClass();
        Collection collection = this.cache.get(cls);
        if (collection == null) {
            return _findAll(obj, cls, lookupListener);
        }
        return _findAll(obj, (Collection<CacheItem>) collection, lookupListener);
    }

    private Map<String, Collection<Subscriber>> _findAll(Object obj, Collection<CacheItem> collection, LookupListener lookupListener) {
        HashMap hashMap = new HashMap();
        for (CacheItem next : collection) {
            Collection collection2 = (Collection) hashMap.get(next.eventType.getName());
            if (collection2 == null) {
                collection2 = new ArrayList();
                hashMap.put(next.eventType.getName(), collection2);
            }
            collection2.add(makeSubscriber(lookupListener, next.eventType, obj, next.method, next.priority, next.thread, next.filter, next.status, next.group));
        }
        return hashMap;
    }

    private Map<String, Collection<Subscriber>> _findAll(Object obj, Class<?> cls, LookupListener lookupListener) {
        Subscribe subscribe;
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        Method[] declaredMethods = cls.getDeclaredMethods();
        int length = declaredMethods.length;
        char c = 0;
        int i = 0;
        while (i < length) {
            Method method = declaredMethods[i];
            if (!method.isBridge() && (subscribe = (Subscribe) method.getAnnotation(Subscribe.class)) != null) {
                Class[] parameterTypes = method.getParameterTypes();
                if ((method.getModifiers() & 1) == 0) {
                    throw new IllegalSubscriberException("the binder listener must be public");
                } else if (parameterTypes.length == 1) {
                    Class cls2 = parameterTypes[c];
                    CacheItem cacheItem = r6;
                    CacheItem cacheItem2 = new CacheItem(cls2, subscribe.thread(), subscribe.priority(), method, subscribe.filter(), subscribe.status(), subscribe.group());
                    arrayList.add(cacheItem);
                    Collection collection = (Collection) hashMap.get(cls2.getName());
                    if (collection == null) {
                        collection = new ArrayList();
                        hashMap.put(cls2.getName(), collection);
                    }
                    LookupListener lookupListener2 = lookupListener;
                    Class cls3 = cls2;
                    Object obj2 = obj;
                    Method method2 = method;
                    collection.add(makeSubscriber(lookupListener2, cls3, obj2, method2, subscribe.priority(), subscribe.thread(), subscribe.filter(), subscribe.status(), subscribe.group()));
                } else {
                    throw new IllegalSubscriberException("the binder listener method [" + method + "] signature is not illegal：must be EventStatus method(Object e)");
                }
            }
            i++;
            c = 0;
        }
        this.cache.put(cls, arrayList);
        return hashMap;
    }

    private Subscriber makeSubscriber(LookupListener lookupListener, Class cls, Object obj, Method method, int i, int i2, String str, int i3, String str2) {
        LookupListener lookupListener2 = lookupListener;
        Class cls2 = cls;
        Object obj2 = obj;
        Subscriber subscriber = new Subscriber(-1, cls.getName(), str, i, i2, i3, str2, new AnnotationEventListener(obj, method, cls));
        return lookupListener2 != null ? lookupListener.onLookup(subscriber) : subscriber;
    }

    private static class CacheItem {
        public Class<?> eventType;
        public String filter;
        public String group;
        public Method method;
        public int priority;
        public int status;
        public int thread;

        public CacheItem(Class<?> cls, int i, int i2, Method method2, String str, int i3, String str2) {
            this.eventType = cls;
            this.thread = i;
            this.priority = i2;
            this.method = method2;
            this.filter = str;
            this.status = i3;
            this.group = str2;
        }
    }
}
