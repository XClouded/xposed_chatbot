package com.taobao.aipc.core.sender;

import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import java.lang.reflect.Method;

public interface ISender {
    MethodWrapper getMethodWrapper(Method method, String str, ParameterWrapper[] parameterWrapperArr) throws IPCException;
}
