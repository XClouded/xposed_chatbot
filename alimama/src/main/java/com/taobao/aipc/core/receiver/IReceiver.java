package com.taobao.aipc.core.receiver;

import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;

public interface IReceiver {
    Object invokeMethod() throws IPCException;

    void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrapperArr) throws IPCException;
}
