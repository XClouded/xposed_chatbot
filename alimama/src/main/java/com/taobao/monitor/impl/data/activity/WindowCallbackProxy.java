package com.taobao.monitor.impl.data.activity;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WindowCallbackProxy implements InvocationHandler {
    final List<DispatchEventListener> listeners = new ArrayList(2);
    private final Window.Callback real;

    public interface DispatchEventListener {
        void dispatchKeyEvent(KeyEvent keyEvent);

        void dispatchTouchEvent(MotionEvent motionEvent);
    }

    WindowCallbackProxy(Window.Callback callback) {
        this.real = callback;
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        String name = method.getName();
        if ("dispatchTouchEvent".equals(name)) {
            if (objArr != null && objArr.length >= 1) {
                MotionEvent motionEvent = objArr[0];
                if (motionEvent instanceof MotionEvent) {
                    for (DispatchEventListener dispatchTouchEvent : this.listeners) {
                        dispatchTouchEvent.dispatchTouchEvent(motionEvent);
                    }
                }
            }
        } else if ("dispatchKeyEvent".equals(name) && objArr != null && objArr.length >= 1) {
            KeyEvent keyEvent = objArr[0];
            if (keyEvent instanceof KeyEvent) {
                for (DispatchEventListener dispatchKeyEvent : this.listeners) {
                    dispatchKeyEvent.dispatchKeyEvent(keyEvent);
                }
            }
        }
        return method.invoke(this.real, objArr);
    }

    public void addListener(DispatchEventListener dispatchEventListener) {
        this.listeners.add(dispatchEventListener);
    }

    public void removeListener(DispatchEventListener dispatchEventListener) {
        this.listeners.remove(dispatchEventListener);
    }
}
