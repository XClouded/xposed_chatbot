package com.taobao.aipc.core.sender.impl;

import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import java.lang.reflect.Method;

public class ObjectSender extends Sender {
    ObjectSender(ObjectWrapper objectWrapper) {
        super(objectWrapper);
    }

    public MethodWrapper getMethodWrapper(Method method, String str, ParameterWrapper[] parameterWrapperArr) {
        return MethodWrapper.obtain(method);
    }
}
