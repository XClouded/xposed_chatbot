package com.alibaba.taffy.bus.listener;

import com.alibaba.taffy.bus.EventStatus;
import com.alibaba.taffy.bus.event.Event;
import com.alibaba.taffy.bus.exception.EventTransferException;
import com.alibaba.taffy.core.util.TAssert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationEventListener implements EventListener {
    private final Class<?> eventClazz;
    private final Method method;
    private final Object target;

    public AnnotationEventListener(Object obj, Method method2, Class<?> cls) {
        TAssert.notNull(obj, "target cannot be null.");
        TAssert.notNull(method2, "method cannot be null.");
        this.target = obj;
        this.method = method2;
        this.eventClazz = cls;
        method2.setAccessible(true);
    }

    public EventStatus onEvent(Event event) {
        Object obj;
        try {
            if (!this.eventClazz.isAssignableFrom(event.getClass())) {
                obj = this.method.invoke(this.target, new Object[]{event.getData()});
            } else {
                obj = this.method.invoke(this.target, new Object[]{event});
            }
            if (obj instanceof EventStatus) {
                return (EventStatus) obj;
            }
            return EventStatus.SUCCESS;
        } catch (IllegalAccessException e) {
            throw new EventTransferException((Throwable) e);
        } catch (InvocationTargetException e2) {
            throw new EventTransferException((Throwable) e2);
        }
    }
}
