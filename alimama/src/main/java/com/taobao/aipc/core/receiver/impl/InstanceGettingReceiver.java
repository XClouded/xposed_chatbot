package com.taobao.aipc.core.receiver.impl;

import com.taobao.aipc.core.receiver.Receiver;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InstanceGettingReceiver extends Receiver {
    private Method mMethod;
    private Class<?> mObjectClass;

    public InstanceGettingReceiver(ObjectWrapper objectWrapper) throws IPCException {
        super(objectWrapper);
        Class<?> classType = TYPE_CENTER.getClassType(objectWrapper);
        TypeUtils.validateAccessible(classType);
        this.mObjectClass = classType;
    }

    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException {
        int length = parameterWrapperArr.length;
        Class[] clsArr = new Class[length];
        for (int i = 0; i < length; i++) {
            clsArr[i] = TYPE_CENTER.getClassType(parameterWrapperArr[i]);
        }
        Method methodForGettingInstance = TypeUtils.getMethodForGettingInstance(this.mObjectClass, methodWrapper.getName(), clsArr);
        if (Modifier.isStatic(methodForGettingInstance.getModifiers())) {
            TypeUtils.validateAccessible(methodForGettingInstance);
            this.mMethod = methodForGettingInstance;
            return;
        }
        throw new IPCException(21, "Method " + methodForGettingInstance.getName() + " of class " + this.mObjectClass.getName() + " is not static. " + "Only the static method can be invoked to get an instance.");
    }

    public Object invokeMethod() throws IPCException {
        try {
            OBJECT_CENTER.putObject(getObjectTimeStamp(), this.mMethod.invoke((Object) null, getParameters()));
            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IPCException(18, "Error occurs when invoking method " + this.mMethod + " to get an instance of " + this.mObjectClass.getName(), e);
        }
    }
}
