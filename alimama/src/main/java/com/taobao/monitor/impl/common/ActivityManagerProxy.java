package com.taobao.monitor.impl.common;

import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* compiled from: ActivityManagerHook */
class ActivityManagerProxy implements InvocationHandler {
    private final Object real;

    ActivityManagerProxy(Object obj) {
        this.real = obj;
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        if (method.getName().contains("startActivity")) {
            GlobalStats.jumpTime = TimeUtils.currentTimeMillis();
            if (DynamicConstants.needStartActivityTrace) {
                printStack();
            }
        }
        try {
            return method.invoke(this.real, objArr);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private void printStack() {
        try {
            Throwable th = new Throwable();
            Thread currentThread = Thread.currentThread();
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                sb.append("\tat " + stackTraceElement);
            }
            DataLoggerUtils.log("ActivityManagerProxy", currentThread.getName(), sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
