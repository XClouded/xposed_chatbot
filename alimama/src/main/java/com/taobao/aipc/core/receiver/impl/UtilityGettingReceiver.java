package com.taobao.aipc.core.receiver.impl;

import com.taobao.aipc.core.receiver.Receiver;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.TypeUtils;

public class UtilityGettingReceiver extends Receiver {
    public Object invokeMethod() {
        return null;
    }

    public void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) {
    }

    public UtilityGettingReceiver(ObjectWrapper objectWrapper) throws IPCException {
        super(objectWrapper);
        TypeUtils.validateAccessible(TYPE_CENTER.getClassType(objectWrapper));
    }
}
