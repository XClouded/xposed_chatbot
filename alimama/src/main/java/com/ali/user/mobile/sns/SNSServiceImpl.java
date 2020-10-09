package com.ali.user.mobile.sns;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.login.model.SNSSignInAccount;
import com.ali.user.mobile.login.ui.AliUserLoginFragment;
import com.ali.user.mobile.login.ui.UserLoginActivity;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.service.SNSService;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.SessionManager;

public class SNSServiceImpl implements SNSService {
    public void dismissLoading(Activity activity) {
    }

    public void onError(Activity activity, RpcResponse<LoginReturnData> rpcResponse) {
    }

    public void onError(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse) {
    }

    public void onFastRegOrLoginBind(Activity activity, String str, String str2, String str3) {
    }

    public void onLoginBind(Activity activity, String str, String str2, String str3, String str4) {
    }

    public void onRebind(Activity activity, String str, String str2, String str3) {
    }

    public void onRebind(Fragment fragment, String str, String str2, String str3) {
    }

    public void onRegBind(Activity activity, String str) {
    }

    public void onToast(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse) {
    }

    public void showLoading(Activity activity) {
    }

    public void showLoading(Fragment fragment) {
        BaseFragment baseFragment;
        if (fragment != null && (fragment instanceof BaseFragment) && (baseFragment = (BaseFragment) fragment) != null) {
            baseFragment.showLoading();
        }
    }

    public void dismissLoading(Fragment fragment) {
        BaseFragment baseFragment;
        if (fragment != null && (fragment instanceof BaseFragment) && (baseFragment = (BaseFragment) fragment) != null) {
            baseFragment.dismissLoading();
        }
    }

    public void toast(Fragment fragment, String str) {
        BaseFragment baseFragment;
        if (fragment != null && (fragment instanceof BaseFragment) && (baseFragment = (BaseFragment) fragment) != null && baseFragment.getActivity() != null && !baseFragment.getActivity().isFinishing()) {
            baseFragment.toast(str, 0);
        }
    }

    public void onLoginSuccess(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse) {
        if (fragment != null) {
            LoginResultHelper.saveLoginData((LoginReturnData) rpcResponse.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
        }
    }

    public void onH5(Fragment fragment, RpcResponse<LoginReturnData> rpcResponse) {
        String str = ((LoginReturnData) rpcResponse.returnValue).h5Url;
        if (fragment != null && fragment.getActivity() != null && !TextUtils.isEmpty(str)) {
            LoginParam loginParam = new LoginParam();
            loginParam.tokenType = TokenType.SNS;
            NavigatorManager.getInstance().navToWebViewPage(fragment.getActivity(), str, loginParam, (LoginReturnData) rpcResponse.returnValue);
        }
    }

    public void onRegBind(Fragment fragment, String str) {
        SNSSignInAccount sNSSignInAccount = (SNSSignInAccount) JSON.parseObject(str, SNSSignInAccount.class);
        NavigatorManager.getInstance().navToRegisterPage(fragment.getActivity(), new RegistParam());
    }

    public void onLoginBind(Fragment fragment, String str, String str2, String str3, String str4) {
        if (fragment != null && (fragment instanceof AliUserLoginFragment)) {
            AliUserLoginFragment aliUserLoginFragment = (AliUserLoginFragment) fragment;
            aliUserLoginFragment.setSnsToken(str);
            aliUserLoginFragment.hideForSNS();
        } else if (fragment != null && fragment.getActivity() != null && (fragment.getActivity() instanceof UserLoginActivity)) {
            Intent intent = new Intent();
            LoginParam loginParam = new LoginParam();
            loginParam.loginAccount = str2;
            loginParam.snsToken = str;
            loginParam.headImg = str3;
            loginParam.snsType = str4;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
            ((UserLoginActivity) fragment.getActivity()).gotoAuthCheckFragmentFromGuide(intent);
        }
    }

    public void onFastRegOrLoginBind(Fragment fragment, String str, String str2, String str3) {
        if (fragment.getActivity() != null && (fragment.getActivity() instanceof UserLoginActivity)) {
            Intent intent = new Intent();
            LoginParam loginParam = new LoginParam();
            loginParam.loginAccount = str2;
            loginParam.snsToken = str;
            loginParam.snsType = str3;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
            ((UserLoginActivity) fragment.getActivity()).gotoFastRegOrLoginBind(intent);
        }
    }

    public void onTokenLogin(Fragment fragment, String str, String str2) {
        if (fragment != null && (fragment instanceof AliUserLoginFragment)) {
            AliUserLoginFragment aliUserLoginFragment = (AliUserLoginFragment) fragment;
            aliUserLoginFragment.mUserLoginPresenter.mLoginParam.token = str;
            aliUserLoginFragment.mUserLoginPresenter.mLoginParam.scene = "1045";
            aliUserLoginFragment.mUserLoginPresenter.onStart();
        } else if (fragment != null && fragment.getActivity() != null && (fragment.getActivity() instanceof UserLoginActivity)) {
            Intent intent = new Intent();
            LoginParam loginParam = new LoginParam();
            loginParam.token = str;
            loginParam.scene = "1045";
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
            ((UserLoginActivity) fragment.getActivity()).gotoLoginFragmentFromGuide(intent);
        }
    }

    public void toast(final Activity activity, final String str) {
        if (activity != null && !activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Toast.makeText(activity, str, 0).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onLoginSuccess(Activity activity, RpcResponse<LoginReturnData> rpcResponse) {
        LoginResultHelper.saveLoginData((LoginReturnData) rpcResponse.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
    }

    public void onToast(final Activity activity, final RpcResponse<LoginReturnData> rpcResponse) {
        if (activity != null && !activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Toast.makeText(activity, rpcResponse.message, 0).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onH5(Activity activity, RpcResponse<LoginReturnData> rpcResponse) {
        String str = ((LoginReturnData) rpcResponse.returnValue).h5Url;
        if (activity != null && !TextUtils.isEmpty(str)) {
            LoginParam loginParam = new LoginParam();
            loginParam.tokenType = TokenType.SNS;
            NavigatorManager.getInstance().navToWebViewPage(activity, str, loginParam, (LoginReturnData) rpcResponse.returnValue);
        }
    }

    public void onTokenLogin(Activity activity, String str) {
        if (!(activity instanceof UserLoginActivity)) {
            Intent intent = new Intent();
            LoginParam loginParam = new LoginParam();
            loginParam.token = str;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
            activity.startActivity(intent);
        }
    }
}
