package com.taobao.onlinemonitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

abstract class BaseDynamicProxy implements InvocationHandler {
    protected OnLineMonitor mOnLineMonitor;
    protected Object mTargetObject;

    /* access modifiers changed from: protected */
    public void doProxy() {
    }

    public BaseDynamicProxy(OnLineMonitor onLineMonitor) {
        this.mOnLineMonitor = onLineMonitor;
    }

    /* access modifiers changed from: protected */
    public Object newProxyInstance(Object obj) {
        this.mTargetObject = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        return method.invoke(this.mTargetObject, objArr);
    }
}
