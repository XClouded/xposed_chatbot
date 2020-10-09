package com.ali.user.mobile.login.ui;

import com.ali.user.mobile.rpc.RpcResponse;

public interface UserMobileLoginView extends BaseLoginView {
    String getCountryCode();

    String getPhoneCode();

    void onCheckCodeError();

    void onNeedReg(String str, String str2);

    void onNeedShowFamilyAccount(String str, String str2);

    void onSMSSendFail(RpcResponse rpcResponse);

    void onSendSMSSuccess(long j, boolean z);
}
