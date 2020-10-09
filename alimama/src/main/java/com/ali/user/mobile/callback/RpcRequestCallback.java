package com.ali.user.mobile.callback;

import com.ali.user.mobile.rpc.RpcResponse;

public interface RpcRequestCallback {
    void onError(RpcResponse rpcResponse);

    void onSuccess(RpcResponse rpcResponse);

    void onSystemError(RpcResponse rpcResponse);
}
