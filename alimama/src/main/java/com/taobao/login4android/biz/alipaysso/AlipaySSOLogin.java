package com.taobao.login4android.biz.alipaysso;

import android.content.Intent;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.SessionManager;
import java.util.Map;

public class AlipaySSOLogin {
    public static final String TAG = "Login.AlipaySSOLogin";

    public static void alipayLogin(String str, Map<String, Object> map) {
        String str2;
        String str3;
        try {
            RpcResponse<LoginReturnData> loginByAlipaySSOToken = UserLoginServiceImpl.getInstance().loginByAlipaySSOToken(str, map);
            if (loginByAlipaySSOToken != null && loginByAlipaySSOToken.returnValue != null && loginByAlipaySSOToken.code == 3000) {
                AppMonitorAdapter.commitSuccess("Page_Member_SSO", "AlipayASO_Login");
                LoginResultHelper.saveLoginData((LoginReturnData) loginByAlipaySSOToken.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
            } else if (loginByAlipaySSOToken == null || !ApiConstants.ResultActionType.H5.equals(loginByAlipaySSOToken.actionType) || loginByAlipaySSOToken.returnValue == null || ((LoginReturnData) loginByAlipaySSOToken.returnValue).token == null) {
                if (loginByAlipaySSOToken == null) {
                    str2 = "";
                } else {
                    str2 = String.valueOf(loginByAlipaySSOToken.code);
                }
                AppMonitorAdapter.commitFail("Page_Member_SSO", "AlipayASO_Login", "0", str2);
                StringBuilder sb = new StringBuilder();
                sb.append("alipayLoginFail : code!= 3000 && actionType!= h5");
                if (loginByAlipaySSOToken != null) {
                    str3 = loginByAlipaySSOToken.code + "," + loginByAlipaySSOToken.message;
                } else {
                    str3 = "";
                }
                sb.append(str3);
                TLogAdapter.e(TAG, sb.toString());
                BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_FAIL_ACTION));
                LoginStatus.resetLoginFlag();
            } else {
                LoginParam loginParam = new LoginParam();
                loginParam.tokenType = TokenType.ALIPAY_SSO;
                LoginResultHelper.gotoH5PlaceHolder(DataProviderFactory.getApplicationContext(), (LoginReturnData) loginByAlipaySSOToken.returnValue, loginParam);
            }
        } catch (Throwable th) {
            BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_FAIL_ACTION));
            LoginStatus.resetLoginFlag();
            th.printStackTrace();
        }
    }
}
