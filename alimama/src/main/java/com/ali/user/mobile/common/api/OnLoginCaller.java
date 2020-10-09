package com.ali.user.mobile.common.api;

import com.ali.user.mobile.rpc.RpcResponse;

public interface OnLoginCaller {
    void failLogin();

    void filterLogin(RpcResponse rpcResponse);
}
