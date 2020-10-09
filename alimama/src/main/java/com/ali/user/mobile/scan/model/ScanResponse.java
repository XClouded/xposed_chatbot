package com.ali.user.mobile.scan.model;

import com.ali.user.mobile.rpc.RpcResponse;

public class ScanResponse extends RpcResponse<Void> {
    public boolean bizSuccess;
    public int errorCode;
    public String errorMessage;
}
