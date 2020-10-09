package com.ali.user.mobile.model;

public interface NumAuthCallback {
    public static final int RPC_ERROR = -102;
    public static final String RPC_ERROR_MSG = "rpc fail";
    public static final int RPC_PARSE_ERROR = -101;
    public static final String RPC_PARSE_ERROR_MSG = "rpc parse data fail";

    void onGetAuthTokenFail(int i, String str);

    void onGetAuthTokenSuccess(String str);

    void onInit(boolean z);
}
