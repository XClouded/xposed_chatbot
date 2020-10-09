package com.ali.user.mobile.register.ui;

import com.ali.user.mobile.base.BaseView;
import com.ali.user.mobile.rpc.RpcResponse;

public interface RegisterFormView extends BaseView {
    String getPageName();

    void onCheckAuthNumFail();

    void onCheckAuthNumSuccess();

    void onH5(String str);

    void onNeedVerification(String str, int i);

    void onNumAuthRegisterFail(RpcResponse rpcResponse);

    void onRegisterFail(int i, String str);

    void onRegisterSuccess(String str);

    void onSMSSendFail(RpcResponse rpcResponse);

    void onSendSMSSuccess(long j);
}
