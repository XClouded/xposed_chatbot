package com.ali.user.mobile.login.ui;

import android.content.DialogInterface;
import com.ali.user.mobile.base.BaseView;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcResponse;

public interface BaseLoginView extends BaseView {
    void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2);

    void alertList(String[] strArr, DialogInterface.OnClickListener onClickListener, boolean z);

    void dismissAlertDialog();

    BaseActivity getBaseActivity();

    HistoryAccount getHistoryAccount();

    int getLoginSite();

    LoginType getLoginType();

    boolean isHistoryMode();

    void onError(RpcResponse rpcResponse);

    void onNeedVerification(String str, int i);

    void onSuccess(LoginParam loginParam, RpcResponse rpcResponse);

    void setLoginAccountInfo(String str);
}
