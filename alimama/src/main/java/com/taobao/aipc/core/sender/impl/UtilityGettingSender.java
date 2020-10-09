package com.taobao.aipc.core.sender.impl;

import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import java.lang.reflect.Method;

public class UtilityGettingSender extends Sender {
    public MethodWrapper getMethodWrapper(Method method, String str, ParameterWrapper[] parameterWrapperArr) {
        return null;
    }

    UtilityGettingSender(ObjectWrapper objectWrapper) {
        super(objectWrapper);
    }
}
