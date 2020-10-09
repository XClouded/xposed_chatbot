package com.taobao.aipc.core.receiver.impl;

import com.taobao.aipc.core.receiver.Receiver;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceCreatingReceiver extends Receiver {
    private Constructor<?> mConstructor;
    private Class<?> mObjectClass;

    public InstanceCreatingReceiver(ObjectWrapper objectWrapper) throws IPCException {
        super(objectWrapper);
        Class<?> classType = TYPE_CENTER.getClassType(objectWrapper);
        TypeUtils.validateAccessible(classType);
        this.mObjectClass = classType;
    }

    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException {
        Constructor<?> constructor = TypeUtils.getConstructor(this.mObjectClass, TYPE_CENTER.getClassTypes(parameterWrapperArr));
        TypeUtils.validateAccessible(constructor);
        this.mConstructor = constructor;
    }

    public Object invokeMethod() throws IPCException {
        Object obj;
        try {
            Object[] parameters = getParameters();
            if (parameters == null) {
                obj = this.mConstructor.newInstance(new Object[0]);
            } else {
                obj = this.mConstructor.newInstance(parameters);
            }
            OBJECT_CENTER.putObject(getObjectTimeStamp(), obj);
            return null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IPCException(18, "Error occurs when invoking constructor to create an instance of " + this.mObjectClass.getName(), e);
        }
    }
}
