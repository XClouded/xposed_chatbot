package com.alipay.literpc.android.phone.mrpc.core;

public abstract class RpcClient {
    public abstract <T> T getRpcProxy(Class<T> cls, RpcParams rpcParams);
}
