package com.ali.user.mobile.login.filter;

import com.ali.user.mobile.rpc.RpcResponse;

public interface LoginResultFilter {
    void onError(RpcResponse rpcResponse);

    void onPwdError();

    void onSuccess(RpcResponse rpcResponse, boolean z);
}
