package com.taobao.login4android.biz.unifysso;

import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.ISession;

public class UnifySsoLogin {
    public static final String TAG = "Login.UnifySsoLogin";

    public static void tokenLogin(int i, String str, ISession iSession) {
        String str2;
        try {
            LoginParam loginParam = new LoginParam();
            loginParam.token = str;
            loginParam.loginSite = i;
            RpcResponse unifySsoTokenLogin = UserLoginServiceImpl.getInstance().unifySsoTokenLogin(loginParam);
            if (unifySsoTokenLogin != null && unifySsoTokenLogin.returnValue != null && unifySsoTokenLogin.code == 3000) {
                LoginResultHelper.saveLoginData((LoginReturnData) unifySsoTokenLogin.returnValue, iSession);
            } else if (unifySsoTokenLogin == null || !ApiConstants.ResultActionType.H5.equals(unifySsoTokenLogin.actionType) || unifySsoTokenLogin.returnValue == null || ((LoginReturnData) unifySsoTokenLogin.returnValue).token == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("unifySsoLoginFail : code!= 3000 && actionType!= h5");
                if (unifySsoTokenLogin != null) {
                    str2 = unifySsoTokenLogin.code + "," + unifySsoTokenLogin.message;
                } else {
                    str2 = "";
                }
                sb.append(str2);
                TLogAdapter.e(TAG, sb.toString());
                LoginStatus.resetLoginFlag();
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
            } else {
                LoginResultHelper.gotoH5PlaceHolder(DataProviderFactory.getApplicationContext(), (LoginReturnData) unifySsoTokenLogin.returnValue, loginParam);
            }
        } catch (Throwable th) {
            LoginStatus.resetLoginFlag();
            th.printStackTrace();
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
        }
    }
}
