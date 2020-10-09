package com.taobao.aipc.core.sender.impl;

import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import java.lang.reflect.Method;

public class InstanceCreatingSender extends Sender {
    InstanceCreatingSender(ObjectWrapper objectWrapper) {
        super(objectWrapper);
    }

    public MethodWrapper getMethodWrapper(Method method, String str, ParameterWrapper[] parameterWrapperArr) {
        Class<?> cls;
        int length = parameterWrapperArr == null ? 0 : parameterWrapperArr.length;
        Class[] clsArr = new Class[length];
        for (int i = 0; i < length; i++) {
            ParameterWrapper parameterWrapper = parameterWrapperArr[i];
            if (parameterWrapper == null) {
                cls = null;
            } else {
                cls = parameterWrapper.getClassType();
            }
            clsArr[i] = cls;
        }
        return MethodWrapper.obtain((Class<?>[]) clsArr);
    }
}
