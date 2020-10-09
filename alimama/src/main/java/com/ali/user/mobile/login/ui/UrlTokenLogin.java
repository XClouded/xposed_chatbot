package com.ali.user.mobile.login.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.utils.BundleUtil;
import com.ali.user.mobile.utils.StringUtil;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.SessionManager;
import java.util.HashMap;

public class UrlTokenLogin {
    public static final String Tag = "login.UrlTokenLogin";

    public static boolean handleUrl(String str, Context context, Activity activity, String str2, String str3, String str4, String str5, boolean z) {
        Uri parse = Uri.parse(str);
        Bundle serialBundle = BundleUtil.serialBundle(parse.getQuery());
        if (serialBundle == null) {
            serialBundle = new Bundle();
        }
        if (str.startsWith(WVUCWebViewClient.SCHEME_SMS)) {
            try {
                context.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(str)));
            } catch (Exception unused) {
                TLogAdapter.e("PlaceHolderActivity", "sms exception" + str);
            }
            return true;
        } else if (!StringUtil.checkWebviewBridge(str)) {
            return false;
        } else {
            if (!TextUtils.isEmpty(str5)) {
                serialBundle.putString("securityId", str5);
            }
            String string = serialBundle.getString("action");
            String string2 = serialBundle.getString("token");
            serialBundle.getInt(WebConstant.SITE);
            if (TextUtils.isEmpty(string) || "quit".equals(string)) {
                LoginParam loginParam = new LoginParam();
                loginParam.token = str2;
                loginParam.scene = str4;
                loginParam.tokenType = str3;
                loginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
                doTokenLogin(context, activity, loginParam);
                return true;
            } else if ("bridgeTrustLogin".equals(string)) {
                activity.setResult(WebConstant.OPEN_WEV_H5_BIND_RESPONSE);
                activity.finish();
                return true;
            } else if ("relogin".equals(string)) {
                LoginParam loginParam2 = new LoginParam();
                loginParam2.scene = str4;
                loginParam2.token = str2;
                loginParam2.tokenType = str3;
                loginParam2.h5QueryString = parse.getQuery();
                loginParam2.loginSite = DataProviderFactory.getDataProvider().getSite();
                doTokenLogin(context, activity, loginParam2);
                return true;
            } else if ("mobile_confirm_login".equals(string)) {
                LoginParam loginParam3 = new LoginParam();
                loginParam3.token = string2;
                loginParam3.tokenType = str3;
                loginParam3.scene = str4;
                loginParam3.loginSite = DataProviderFactory.getDataProvider().getSite();
                doTokenLogin(context, activity, loginParam3);
                return true;
            } else if (ApiConstants.ApiField.TRUST_LOGIN.equals(string)) {
                LoginParam loginParam4 = new LoginParam();
                loginParam4.scene = str4;
                loginParam4.token = string2;
                loginParam4.tokenType = str3;
                loginParam4.loginSite = DataProviderFactory.getDataProvider().getSite();
                doTokenLogin(context, activity, loginParam4);
                return true;
            } else if (!TextUtils.equals(LoginConstant.ACTION_CONTINUELOGIN, string)) {
                return false;
            } else {
                if (z) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("aliusersdk_h5querystring", parse.getQuery());
                    bundle.putString("token", str2);
                    bundle.putString("scene", str4);
                    intent.putExtras(bundle);
                    activity.setResult(-1, intent);
                    activity.finish();
                } else {
                    LoginParam loginParam5 = new LoginParam();
                    loginParam5.token = str2;
                    loginParam5.tokenType = str3;
                    loginParam5.scene = str4;
                    loginParam5.loginSite = DataProviderFactory.getDataProvider().getSite();
                    loginParam5.h5QueryString = parse.getQuery();
                    doTokenLogin(context, activity, loginParam5);
                }
                return true;
            }
        }
    }

    private static void doTokenLogin(Context context, Activity activity, LoginParam loginParam) {
        try {
            RpcResponse loginByToken = UserLoginServiceImpl.getInstance().loginByToken(loginParam);
            if (loginByToken == null || loginByToken.returnValue == null || loginByToken.code != 3000) {
                if (loginByToken == null || !ApiConstants.ResultActionType.H5.equals(loginByToken.actionType) || loginByToken.returnValue == null) {
                    TLogAdapter.e(Tag, "token login fail");
                    BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_FAIL_ACTION));
                    resetLoginFlag();
                } else {
                    LoginResultHelper.gotoH5PlaceHolder(context, (LoginReturnData) loginByToken.returnValue, loginParam);
                }
                activity.finish();
            }
            if (TokenType.isAuthType(loginParam.tokenType)) {
                if (((LoginReturnData) loginByToken.returnValue).extMap == null) {
                    ((LoginReturnData) loginByToken.returnValue).extMap = new HashMap();
                }
                ((LoginReturnData) loginByToken.returnValue).extMap.put(LoginConstant.LOGIN_TYPE, LoginType.AUTH_ACCOUNT.getType());
            }
            LoginResultHelper.saveLoginData((LoginReturnData) loginByToken.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
            activity.finish();
        } catch (Exception e) {
            resetLoginFlag();
            BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_FAIL_ACTION));
            e.printStackTrace();
        } catch (Throwable th) {
            activity.finish();
            throw th;
        }
    }

    public static void resetLoginFlag() {
        LoginStatus.resetLoginFlag();
    }
}
