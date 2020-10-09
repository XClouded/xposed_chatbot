package com.ali.user.mobile.service;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;

public interface SNSService {
    void dismissLoading(Activity activity);

    void dismissLoading(Fragment fragment);

    void onError(Activity activity, RpcResponse<LoginReturnData> rpcResponse);

    void onError(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse);

    void onFastRegOrLoginBind(Activity activity, String str, String str2, String str3);

    void onFastRegOrLoginBind(Fragment fragment, String str, String str2, String str3);

    void onH5(Activity activity, RpcResponse<LoginReturnData> rpcResponse);

    void onH5(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse);

    void onLoginBind(Activity activity, String str, String str2, String str3, String str4);

    void onLoginBind(Fragment fragment, String str, String str2, String str3, String str4);

    void onLoginSuccess(Activity activity, RpcResponse<LoginReturnData> rpcResponse);

    void onLoginSuccess(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse);

    void onRebind(Activity activity, String str, String str2, String str3);

    void onRebind(Fragment fragment, String str, String str2, String str3);

    void onRegBind(Activity activity, String str);

    void onRegBind(Fragment fragment, String str);

    void onToast(Activity activity, RpcResponse<LoginReturnData> rpcResponse);

    void onToast(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse);

    void onTokenLogin(Activity activity, String str);

    void onTokenLogin(Fragment fragment, String str, String str2);

    void showLoading(Activity activity);

    void showLoading(Fragment fragment);

    void toast(Activity activity, String str);

    void toast(Fragment fragment, String str);
}
