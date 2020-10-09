package com.taobao.aipc.core.sender.impl;

import com.taobao.aipc.core.channel.Channel;
import com.taobao.aipc.core.entity.Message;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.utils.TimeStampGenerator;
import java.lang.reflect.Method;

public class InstanceGettingSender extends Sender {
    InstanceGettingSender(ObjectWrapper objectWrapper) {
        super(objectWrapper);
    }

    public MethodWrapper getMethodWrapper(Method method, String str, ParameterWrapper[] parameterWrapperArr) {
        Class<?> cls;
        int length = parameterWrapperArr.length;
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
        return MethodWrapper.obtain(str, clsArr);
    }

    public Reply send(String str, Object[] objArr) throws IPCException {
        this.mTimeStamp = TimeStampGenerator.getTimeStamp();
        if (objArr == null) {
            objArr = new Object[0];
        }
        ParameterWrapper[] parameterWrappers = getParameterWrappers((Method) null, objArr);
        MethodWrapper methodWrapper = getMethodWrapper((Method) null, str, parameterWrappers);
        registerClass((Method) null);
        return Channel.getInstance().send(Message.obtain(this.mTimeStamp, this.mObject, methodWrapper, parameterWrappers));
    }
}
