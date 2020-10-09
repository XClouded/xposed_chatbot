package com.ali.user.mobile.rpc;

public class RpcResponse<T> {
    public String actionType;
    public int code;
    public String codeGroup;
    public String message;
    public String msgCode;
    public String msgInfo;
    public T returnValue;
}
