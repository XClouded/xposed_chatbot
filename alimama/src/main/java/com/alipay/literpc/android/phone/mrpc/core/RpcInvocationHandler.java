package com.alipay.literpc.android.phone.mrpc.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvocationHandler implements InvocationHandler {
    protected Class<?> mClazz;
    protected Config mConfig;
    protected RpcInvoker mRpcInvoker;

    public RpcInvocationHandler(Config config, Class<?> cls, RpcInvoker rpcInvoker) {
        this.mConfig = config;
        this.mClazz = cls;
        this.mRpcInvoker = rpcInvoker;
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws RpcException {
        return this.mRpcInvoker.invoke(obj, this.mClazz, method, objArr);
    }
}
