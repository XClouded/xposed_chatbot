package com.taobao.aipc.core.receiver.impl;

import com.taobao.aipc.core.receiver.Receiver;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectReceiver extends Receiver {
    private Method mMethod;
    private Object mObject = OBJECT_CENTER.getObject(getObjectTimeStamp());

    public ObjectReceiver(ObjectWrapper objectWrapper) {
        super(objectWrapper);
    }

    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException {
        Method method = TYPE_CENTER.getMethod(this.mObject.getClass(), methodWrapper);
        TypeUtils.validateAccessible(method);
        this.mMethod = method;
    }

    public Object invokeMethod() throws IPCException {
        try {
            return this.mMethod.invoke(this.mObject, getParameters());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IPCException(18, "Error occurs when invoking method " + this.mMethod + " on " + this.mObject, e);
        }
    }
}
