package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.logger.Logger;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AbsDispatcher<LISTENER> implements IDispatcher<LISTENER> {
    private static final String TAG = "AbsDispatcher";
    /* access modifiers changed from: private */
    public final List<LISTENER> listeners = new ArrayList();

    protected interface ListenerCaller<LISTENER> {
        void callListener(LISTENER listener);
    }

    protected AbsDispatcher() {
        Logger.i(TAG, getClass().getSimpleName(), " init");
    }

    public final void addListener(final LISTENER listener) {
        if (!(this instanceof EmptyDispatcher) && listener != null && checkValid(listener)) {
            async(new Runnable() {
                public void run() {
                    if (!AbsDispatcher.this.listeners.contains(listener)) {
                        AbsDispatcher.this.listeners.add(listener);
                    }
                }
            });
        }
    }

    private boolean checkValid(LISTENER listener) {
        return isValidListener(listener, getTClass());
    }

    private boolean isValidListener(LISTENER listener, Class cls) {
        if (cls == null) {
            return false;
        }
        return cls.isInstance(listener);
    }

    private Class getTClass() {
        Type[] actualTypeArguments;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType) || (actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments()) == null || actualTypeArguments.length == 0) {
            return Object.class;
        }
        return (Class) actualTypeArguments[0];
    }

    public final void removeListener(final LISTENER listener) {
        if (!(this instanceof EmptyDispatcher) && listener != null) {
            async(new Runnable() {
                public void run() {
                    AbsDispatcher.this.listeners.remove(listener);
                }
            });
        }
    }

    private void async(Runnable runnable) {
        Global.instance().handler().post(runnable);
    }

    /* access modifiers changed from: protected */
    public final void foreach(final ListenerCaller<LISTENER> listenerCaller) {
        async(new Runnable() {
            public void run() {
                for (Object callListener : AbsDispatcher.this.listeners) {
                    listenerCaller.callListener(callListener);
                }
            }
        });
    }
}
