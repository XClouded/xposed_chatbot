package com.ali.user.mobile.model;

public interface AutoLoginCallback {
    void onBizFail(int i, String str);

    void onNetworkError();

    void onSuccess();
}
