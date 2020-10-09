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

public class UtilityReceiver extends Receiver {
    private Class<?> mClass;
    private Method mMethod;

    public UtilityReceiver(ObjectWrapper objectWrapper) throws IPCException {
        super(objectWrapper);
        Class<?> classType = TYPE_CENTER.getClassType(objectWrapper);
        TypeUtils.validateAccessible(classType);
        this.mClass = classType;
    }

    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException {
        Method method = TYPE_CENTER.getMethod(this.mClass, methodWrapper);
        if (Modifier.isStatic(method.getModifiers())) {
            TypeUtils.validateAccessible(method);
            this.mMethod = method;
            return;
        }
        throw new IPCException(5, "Only static methods can be invoked on the utility class " + this.mClass.getName() + ". Please modify the method: " + this.mMethod);
    }

    public Object invokeMethod() throws IPCException {
        try {
            return this.mMethod.invoke((Object) null, getParameters());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IPCException(18, "Error occurs when invoking method " + this.mMethod + ".", e);
        }
    }
}
