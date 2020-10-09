package com.taobao.login4android.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.taobao.windvane.WindVaneSDK;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.extra.jsbridge.TBUploadService;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.api.WVCamera;
import android.taobao.windvane.util.PhoneInfo;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.Trojan;
import com.ali.user.mobile.app.common.init.LaunchInit;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.login.dataprovider.TaobaoAppProvider;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.helper.LoginDataHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.OnBindCaller;
import com.ali.user.mobile.common.api.OnLoginCaller;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.db.LoginHistoryOperater;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.AutoLoginCallback;
import com.ali.user.mobile.model.BindParam;
import com.ali.user.mobile.model.CommonCallback;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.SSOMasterParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.rpc.h5.MtopCanChangeNickResponseData;
import com.ali.user.mobile.rpc.login.model.ApplyTokenRequest;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.login.model.LoginTokenResponseData;
import com.ali.user.mobile.rpc.login.model.MLoginTokenReturnValue;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.url.model.AccountCenterParam;
import com.ali.user.mobile.url.service.impl.UrlFetchServiceImpl;
import com.ali.user.mobile.utils.BackgroundExecutor;
import com.ali.user.mobile.utils.MD5Util;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.ali.user.mobile.utils.SiteUtil;
import com.ali.user.mobile.verify.impl.VerifyServiceImpl;
import com.ali.user.mobile.verify.model.GetVerifyUrlResponse;
import com.ali.user.mobile.verify.model.GetVerifyUrlReturnData;
import com.ali.user.mobile.verify.model.VerifyParam;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.cons.c;
import com.taobao.android.scancode.common.jsbridge.ScancodeCallback;
import com.taobao.android.sso.v2.model.ApplySsoTokenRequest;
import com.taobao.android.sso.v2.model.SSOIPCConstants;
import com.taobao.android.sso.v2.model.SSOV2ApplySsoTokenResponseData;
import com.taobao.android.sso.v2.security.SSOSecurityService;
import com.taobao.login4android.Login;
import com.taobao.login4android.auto.ICBUAutoLoginBusiness;
import com.taobao.login4android.biz.alipaysso.AlipayConstant;
import com.taobao.login4android.biz.alipaysso.AlipaySSOLogin;
import com.taobao.login4android.biz.autologin.AutoLoginBusiness;
import com.taobao.login4android.biz.getWapCookies.GetWapLoginCookiesBusiness;
import com.taobao.login4android.biz.logout.LogoutBusiness;
import com.taobao.login4android.biz.unifysso.UnifySsoLogin;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.config.LoginSwitch;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.jsbridge.JSBridgeService;
import com.taobao.login4android.jsbridge.SDKJSBridgeService;
import com.taobao.login4android.jsbridge.WindVaneSDKForDefault;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.receiver.LoginTestBroadcastReceiver;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.thread.LoginAsyncTask;
import com.taobao.login4android.thread.LoginTask;
import com.taobao.login4android.thread.LoginThreadHelper;
import com.taobao.login4android.video.VerifyJsbridge;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.WXGlobalEventReceiver;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {
    private static final String TAG = "loginsdk.LoginController";
    private static LoginController controller;
    public String browserRefUrl;
    private AtomicBoolean isAliuserSDKInited = new AtomicBoolean(false);
    private boolean isNotifyLogout = false;

    private LoginController() {
    }

    public static synchronized LoginController getInstance() {
        LoginController loginController;
        synchronized (LoginController.class) {
            if (controller == null) {
                controller = new LoginController();
            }
            loginController = controller;
        }
        return loginController;
    }

    private void processNetworkError(RpcResponse<LoginReturnData> rpcResponse, boolean z, boolean z2, Bundle bundle) {
        if (RpcException.isSystemError(rpcResponse.code)) {
            if (LoginStatus.isFromChangeAccount()) {
                Login.session.recoverCookie();
            }
            if (z2) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, rpcResponse.code, rpcResponse.message, this.browserRefUrl);
                return;
            }
            return;
        }
        userLogin(z, z2, bundle);
    }

    public void autoLogin(boolean z, Bundle bundle) {
        boolean z2 = z;
        Bundle bundle2 = bundle;
        if (Login.session == null) {
            Login.session = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        }
        String userId = Login.getUserId();
        String loginToken = Login.getLoginToken();
        int defaultLoginSite = SiteUtil.getDefaultLoginSite();
        if (bundle2 != null) {
            BroadCastHelper.sLoginBundle = bundle2;
            boolean z3 = bundle2.getBoolean("easylogin2", false);
            String string = bundle2.getString(AlipayConstant.LOGIN_ALIPAY_TOKEN_KEY, "");
            String string2 = bundle2.getString(LoginConstant.LOGIN_BUNDLE_UNIFY_SSO_TOKEN, "");
            boolean z4 = bundle2.getBoolean(LoginConstant.CHANGE_ACCOUNT_FLAG);
            String string3 = bundle2.getString(LoginConstant.OUTTER_LOGIN_TOKEN, "");
            String string4 = bundle2.getString(LoginConstant.OUTTER_LOGIN_TOKEN_TYPE, "");
            TLogAdapter.d(TAG, "isEasyLogin2:" + z3);
            if (z3) {
                easyLogin2(bundle2);
                return;
            } else if (!TextUtils.isEmpty(string2)) {
                UnifySsoLogin.tokenLogin(bundle2.getInt(LoginConstant.ALIUSER_LOGIN_SITE, 0), string2, Login.session);
                return;
            } else if (!TextUtils.isEmpty(string) && DataProviderFactory.getDataProvider().enableAlipaySSO()) {
                alipaySsoLogin(bundle2);
                return;
            } else if (!TextUtils.isEmpty(bundle2.getString("trustLogin4Tmall", ""))) {
                TmallSsoLogin.getInstance().jumpToTmallWithLoginToken(DataProviderFactory.getApplicationContext(), bundle2.getString("trustLogin4Tmall", ""));
                return;
            } else {
                if (z4) {
                    userId = bundle2.getString(LoginConstant.CHANGE_ACCOUNT_USER_ID, "");
                    loginToken = bundle2.getString(LoginConstant.CHANGE_ACCOUNT_AUTOLOGIN_TOKEN, "");
                    defaultLoginSite = bundle2.getInt(LoginConstant.ALIUSER_LOGIN_SITE, 0);
                    LoginStatus.compareAndSetFromChangeAccount(false, true);
                    ApiReferer.Refer refer = new ApiReferer.Refer();
                    refer.eventName = "changeAccount";
                    refer.userId = userId;
                    refer.site = String.valueOf(defaultLoginSite);
                    Login.session.appendEventTrace(JSON.toJSONString(refer));
                } else if (getOldRefreshToken(bundle2) != null) {
                    if (TextUtils.isEmpty(loginToken)) {
                        String[] oldRefreshToken = getOldRefreshToken(bundle2);
                        icbuAutoLogin(oldRefreshToken[1], oldRefreshToken[0], z2);
                        return;
                    }
                } else if (!TextUtils.isEmpty(string3)) {
                    old2NewAutoLogin(string3, string4, DataProviderFactory.getDataProvider().getSite(), z, bundle);
                    return;
                }
                this.browserRefUrl = bundle2.getString(LoginConstants.BROWSER_REF_URL);
                TLogAdapter.d(TAG, "autologin with bundle. browserRefUrl = " + this.browserRefUrl);
                String string5 = bundle2.getString("apiReferer");
                apiReferUT(string5);
                if (!TextUtils.isEmpty(string5)) {
                    try {
                        new JSONObject(string5);
                        Login.session.appendEventTrace(string5);
                    } catch (Throwable unused) {
                        ApiReferer.Refer refer2 = new ApiReferer.Refer();
                        refer2.eventName = string5;
                        Login.session.appendEventTrace(JSON.toJSONString(refer2));
                    }
                }
                UserTrackAdapter.sendUT("apiReferer", string5);
                if (!TextUtils.isEmpty(this.browserRefUrl)) {
                    ApiReferer.Refer refer3 = new ApiReferer.Refer();
                    refer3.eventName = this.browserRefUrl;
                    Login.session.appendEventTrace(JSON.toJSONString(refer3));
                    Properties properties = new Properties();
                    properties.setProperty("url", this.browserRefUrl);
                    UserTrackAdapter.sendUT("LoginConstants.BROWSER_REF_URL", properties);
                }
                if (refreshCookiesFromMtop(bundle2.getBoolean(LoginConstants.REFRESH_COOKIES_FIRST), true)) {
                    sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS);
                    return;
                }
            }
        }
        int i = defaultLoginSite;
        String str = loginToken;
        String str2 = userId;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) {
            ApiReferer.Refer refer4 = new ApiReferer.Refer();
            refer4.eventName = "autoLoginToken=null|trySdkLogin";
            Login.session.appendEventTrace(JSON.toJSONString(refer4));
            try {
                Properties properties2 = new Properties();
                properties2.put("action", "autologin token null trySdkLogin");
                UserTrackAdapter.sendUT("NullAutoLoginToken", properties2);
            } catch (Exception unused2) {
            }
            userLogin(z2, true, bundle2);
            return;
        }
        handleTrojan("autologin");
        autoLoginTargetAccount(str2, str, i, z, bundle);
    }

    private String[] getOldRefreshToken(Bundle bundle) {
        String[] strArr = new String[2];
        if (bundle == null) {
            return null;
        }
        String string = bundle.getString(LoginConstant.ICBU_LOGIN_TOKEN, "");
        String string2 = bundle.getString(LoginConstant.ICBU_LOGIN_USERID, "");
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            return null;
        }
        strArr[0] = string;
        strArr[1] = string2;
        return strArr;
    }

    public void icbuAutoLogin(String str, String str2, boolean z) {
        try {
            processAutoLoginResponse(new ICBUAutoLoginBusiness().autoLogin(str2, str, z, ApiReferer.generateApiReferer()), z);
        } catch (Throwable th) {
            ApiReferer.Refer refer = new ApiReferer.Refer();
            refer.eventName = "icbuAutoLoginFailed";
            refer.errorMessage = th.getMessage();
            Login.session.appendEventTrace(JSON.toJSONString(refer));
            userLogin(z);
        }
    }

    public void doAutoLoginWithCallback(String str, String str2, int i, String str3, boolean z, AutoLoginCallback autoLoginCallback) {
        if (autoLoginCallback != null) {
            final String str4 = str;
            final String str5 = str2;
            final int i2 = i;
            final String str6 = str3;
            final AutoLoginCallback autoLoginCallback2 = autoLoginCallback;
            final boolean z2 = z;
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse<LoginReturnData>>() {
                /* access modifiers changed from: protected */
                public RpcResponse<LoginReturnData> doInBackground(Object... objArr) {
                    try {
                        return new AutoLoginBusiness().autoLogin(str4, str5, i2, str6);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(RpcResponse<LoginReturnData> rpcResponse) {
                    if (rpcResponse == null) {
                        autoLoginCallback2.onBizFail(-2, "Null Response");
                    } else if (RpcException.isSystemError(rpcResponse.code)) {
                        autoLoginCallback2.onNetworkError();
                    } else if ("SUCCESS".equals(rpcResponse.actionType)) {
                        LoginController.this.processAutoLoginResponse(rpcResponse, false, z2, (Bundle) null);
                        autoLoginCallback2.onSuccess();
                    } else {
                        autoLoginCallback2.onBizFail(rpcResponse.code, rpcResponse.message);
                    }
                }
            }, new Object[0]);
        }
    }

    private void apiReferUT(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString(c.n);
                UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(jSONObject.optString(WXGlobalEventReceiver.EVENT_NAME, "NO_SESSION"));
                uTCustomHitBuilder.setEventPage("Page_Extend");
                if (!TextUtils.isEmpty(optString)) {
                    uTCustomHitBuilder.setProperty(c.n, optString);
                }
                String optString2 = jSONObject.optString("msgCode");
                if (!TextUtils.isEmpty(optString2)) {
                    uTCustomHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, optString2);
                }
                UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
            } catch (JSONException unused) {
            }
        }
    }

    public void handleTrojan(final String str) {
        new CoordinatorWrapper().execute(new Runnable() {
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.put("action", str);
                    UserTrackAdapter.sendUT("IAntiTrojan", properties);
                    String nick = Login.getNick();
                    if (nick == null) {
                        nick = "";
                    }
                    Trojan.antiTrojan(nick, false);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    public void autoLoginTargetAccount(String str, String str2, int i, boolean z, Bundle bundle) {
        try {
            processAutoLoginResponse(new AutoLoginBusiness().autoLogin(str2, str, i, z, ApiReferer.generateApiReferer()), z, true, bundle);
        } catch (Throwable th) {
            if (TextUtils.equals(Login.getUserId(), str)) {
                ApiReferer.Refer refer = new ApiReferer.Refer();
                refer.eventName = "autoLoginFailed";
                refer.errorMessage = th.getMessage();
                Login.session.appendEventTrace(JSON.toJSONString(refer));
            }
            userLogin(z, true, bundle);
        }
    }

    public void old2NewAutoLogin(String str, String str2, int i, boolean z, Bundle bundle) {
        try {
            processAutoLoginResponse(new AutoLoginBusiness().oldLogin(str, str2, i, ApiReferer.generateApiReferer()), z, true, bundle);
        } catch (Throwable unused) {
            userLogin(z, true, bundle);
        }
    }

    private void easyLogin2(Bundle bundle) {
        String string = bundle.getString("username", "");
        String string2 = bundle.getString("password", "");
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "username or password is null", this.browserRefUrl);
            return;
        }
        LoginParam loginParam = new LoginParam();
        loginParam.loginAccount = string;
        loginParam.loginPassword = string2;
        loginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
        try {
            processPwdLoginResponse(UserLoginServiceImpl.getInstance().easyLogin(loginParam), true);
        } catch (Exception unused) {
            LoginStatus.resetLoginFlag();
        }
    }

    private void alipaySsoLogin(Bundle bundle) {
        TLogAdapter.e(TAG, "alipay login");
        String string = bundle.getString(AlipayConstant.LOGIN_ALIPAY_TOKEN_KEY, "");
        HashMap hashMap = new HashMap();
        hashMap.put("source", bundle.getString("source", ""));
        hashMap.put("version", bundle.getString("version", ""));
        hashMap.put("app_id", bundle.getString("app_id", ""));
        hashMap.put("auth_code", bundle.getString("auth_code", ""));
        hashMap.put(AlipayConstant.LOGIN_ALIPAY_CLIENT_VERSION_KEY, bundle.getString(AlipayConstant.LOGIN_ALIPAY_CLIENT_VERSION_KEY, ""));
        hashMap.put(AlipayConstant.LOGIN_ALIPAY_USER_ID_KEY, bundle.getString(AlipayConstant.LOGIN_ALIPAY_USER_ID_KEY, ""));
        AlipaySSOLogin.alipayLogin(string, hashMap);
    }

    public void userLogin(boolean z) {
        userLogin(z, true, (Bundle) null);
    }

    public void userLogin(boolean z, final boolean z2, final Bundle bundle) {
        if (z) {
            LoginTLogAdapter.d(TAG, "try sdkLogin");
            LoginThreadHelper.runOnUIThread(new LoginTask() {
                public void excuteTask() {
                    if (DataProviderFactory.getApplicationContext() != null) {
                        try {
                            Login.session.clearSessionOnlyCookie();
                            LoginTLogAdapter.d(LoginController.TAG, "start sdk login");
                            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openLoginPage(DataProviderFactory.getApplicationContext(), ApiReferer.generateApiReferer(), bundle);
                            LoginTLogAdapter.i(LoginController.TAG, "aliuserLogin.openLoginPage");
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (z2) {
                                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "Exception", LoginController.this.browserRefUrl);
                            }
                            LoginTLogAdapter.d(LoginController.TAG, "login failed: Exception:" + e.getMessage());
                        }
                    } else {
                        if (z2) {
                            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "ContextNull", LoginController.this.browserRefUrl);
                        }
                        LoginTLogAdapter.d(LoginController.TAG, "DataProviderFactory.getApplicationContext() is null");
                    }
                }
            });
        } else if (z2) {
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -3, "showUI=false", this.browserRefUrl);
        }
    }

    public boolean refreshCookies(boolean z, boolean z2) {
        if (!z) {
            return false;
        }
        if ((DataProviderFactory.getDataProvider() instanceof TaobaoAppProvider) && DataProviderFactory.getDataProvider().isRefreshCookiesDegrade()) {
            return refreshCookies(z2);
        }
        if (Login.session != null) {
            return Login.session.recoverCookie();
        }
        return false;
    }

    public boolean refreshCookiesFromMtop(boolean z, boolean z2) {
        if (!z || !(DataProviderFactory.getDataProvider() instanceof TaobaoAppProvider) || DataProviderFactory.getDataProvider().isForbiddenRefreshCookieInAutoLogin()) {
            return false;
        }
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
            properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
        }
        UserTrackAdapter.sendUT("Event_AutoLoginViaRefreshCookie", properties);
        return refreshCookies(z2);
    }

    private boolean refreshCookies(boolean z) {
        try {
            String[] wapCookies = new GetWapLoginCookiesBusiness().getWapCookies(ApiReferer.generateApiReferer(), z);
            if (wapCookies == null || wapCookies.length <= 0) {
                return false;
            }
            Login.session.injectCookie(wapCookies, (String[]) null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void bindAlipay(String str, String str2) {
        if (LoginSwitch.getSwitch(LoginSwitch.BIND_ALIPAY_SWITCH, "true")) {
            try {
                BindParam bindParam = new BindParam();
                bindParam.bizSence = str;
                bindParam.signData = str2;
                LoginTLogAdapter.d(TAG, "bind alipay. bizSence=" + str + ", signData=" + str2);
                AliUserLogin.getInstance().bind(DataProviderFactory.getApplicationContext(), bindParam, new OnBindCaller() {
                    public void onBindSuccess(Bundle bundle) {
                        LoginController.this.sendBroadcast(LoginAction.BIND_ALIPAY_SUCCESS);
                    }

                    public void onBindError(Bundle bundle) {
                        LoginController.this.sendBroadcast(LoginAction.BIND_ALIPAY_FAILED);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                LoginTLogAdapter.d(TAG, "bind alipay failed");
            }
        } else {
            sendBroadcast(LoginAction.BIND_ALIPAY_FAILED);
        }
    }

    public void openLoginPage(Context context) {
        try {
            LoginTLogAdapter.d(TAG, "start sdk register");
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openLoginPage(context, ApiReferer.generateApiReferer(), (Bundle) null);
            TLogAdapter.i(TAG, "aliuserLogin.openLoginPage");
        } catch (Exception e) {
            e.printStackTrace();
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "Exception", this.browserRefUrl);
            LoginTLogAdapter.d(TAG, "open register page failed: Exception:" + e.getMessage());
        }
    }

    public void openRegisterPage(Context context, RegistParam registParam) {
        try {
            LoginTLogAdapter.d(TAG, "start sdk register");
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openRegisterPage(context, registParam);
            LoginTLogAdapter.i(TAG, "aliuserLogin.openRegisterPage");
        } catch (Exception e) {
            e.printStackTrace();
            LoginTLogAdapter.d(TAG, "open register page failed: Exception:" + e.getMessage());
        }
    }

    public void openRegisterPage(Context context) {
        openRegisterPage(context, (RegistParam) null);
    }

    public void sendBroadcast(LoginAction loginAction) {
        BroadCastHelper.sendBroadcast(loginAction, false, this.browserRefUrl);
    }

    public synchronized void logout(int i, String str, String str2, String str3, boolean z) {
        this.isNotifyLogout = false;
        new LogoutBusiness().logout(i, str, str2, str3);
        if (z) {
            emptySessionList();
        }
        logoutClean(str3);
    }

    public synchronized void logout() {
        logout(Login.getLoginSite(), Login.getSid(), Login.getLoginToken(), Login.getUserId(), false);
    }

    public void emptySessionList() {
        SecurityGuardManagerWraper.emptySessionListFromFile();
    }

    public void clearLoginInfo(String str) {
        LoginTLogAdapter.d(TAG, "clearLoginInfo");
        try {
            SecurityGuardManagerWraper.removeSessionModelFromFile(str);
            Login.session.setSsoToken((String) null);
            Login.session.setOneTimeToken((String) null);
            Login.session.clearSessionInfo();
            Login.session.clearAutoLoginInfo();
            LoginTLogAdapter.e(TAG, "clear sessionInfo in LoginController.clearLoginInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearHistoryNames() {
        LoginHistoryOperater.getInstance().deleteAllLoginHistory();
    }

    private void logoutClean(String str) {
        if (!this.isNotifyLogout) {
            clearLoginInfo(str);
            ApiReferer.Refer refer = new ApiReferer.Refer();
            refer.eventName = "USER_LOGOUT";
            Login.session.appendEventTrace(JSON.toJSONString(refer));
            LoginStatus.setLastRefreshCookieTime(0);
            sendBroadcast(LoginAction.NOTIFY_LOGOUT);
            LoginTLogAdapter.d(TAG, "logout finish");
            this.isNotifyLogout = true;
        }
    }

    public void navToWebViewByScene(final Context context, final String str, int i) {
        AccountCenterParam accountCenterParam = new AccountCenterParam();
        accountCenterParam.scene = str;
        accountCenterParam.fromSite = i;
        UrlFetchServiceImpl.getInstance().navBySceneRemote(accountCenterParam, new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (rpcResponse == null) {
                    AppMonitorAdapter.commitFail("Page_Member_Account", "Account_" + str + "_URL", "0", "");
                    return;
                }
                Context context = context;
                if (context == null) {
                    context = DataProviderFactory.getApplicationContext();
                }
                MtopAccountCenterUrlResponseData mtopAccountCenterUrlResponseData = (MtopAccountCenterUrlResponseData) rpcResponse;
                if (mtopAccountCenterUrlResponseData == null || TextUtils.isEmpty(mtopAccountCenterUrlResponseData.h5Url)) {
                    AppMonitorAdapter.commitFail("Page_Member_Account", "Account_" + str + "_URL", "0", String.valueOf(mtopAccountCenterUrlResponseData.code));
                    Toast.makeText(context, mtopAccountCenterUrlResponseData.errorMessage, 0).show();
                    return;
                }
                AppMonitorAdapter.commitSuccess("Page_Member_Account", "Account_" + str + "_URL");
                UrlParam urlParam = new UrlParam();
                urlParam.scene = str;
                urlParam.url = mtopAccountCenterUrlResponseData.h5Url;
                urlParam.site = DataProviderFactory.getDataProvider().getSite();
                ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openWebViewPage(context, urlParam);
            }

            public void onSystemError(RpcResponse rpcResponse) {
                AppMonitorAdapter.commitFail("Page_Member_Account", "Account_" + str + "_URL", "0", "");
            }

            public void onError(RpcResponse rpcResponse) {
                AppMonitorAdapter.commitFail("Page_Member_Account", "Account_" + str + "_URL", "0", "");
            }
        });
    }

    public void navToIVByScene(Context context, String str, int i) {
        navToIVByScene(context, str, i, (Bundle) null);
    }

    public void navToIVByScene(Context context, String str, int i, Bundle bundle) {
        final VerifyParam verifyParam = new VerifyParam();
        verifyParam.fromSite = i;
        verifyParam.actionType = str;
        verifyParam.userId = Login.getUserId();
        final String str2 = str;
        final Bundle bundle2 = bundle;
        final Context context2 = context;
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(Object[] objArr) {
                return Login.getDeviceTokenKey(Login.getUserId());
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                verifyParam.deviceTokenKey = str;
                VerifyServiceImpl.getInstance().getIdentityVerificationUrl(verifyParam, new RpcRequestCallback() {
                    public void onSuccess(RpcResponse rpcResponse) {
                        GetVerifyUrlResponse getVerifyUrlResponse = (GetVerifyUrlResponse) rpcResponse;
                        if (getVerifyUrlResponse.returnValue != null) {
                            String str = ((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).token;
                            if (!TextUtils.isEmpty(str)) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("token", str);
                                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_SUCCESS, false, 0, "", hashMap, "");
                                return;
                            }
                            String str2 = ((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url;
                            UrlParam urlParam = new UrlParam();
                            urlParam.ivScene = str2;
                            urlParam.url = str2;
                            if (bundle2 != null) {
                                urlParam.showSkipButton = bundle2.getBoolean(LoginConstant.ICBU_IV_SKIP, false);
                            }
                            LoginController.this.openUrl(context2, urlParam);
                        }
                    }

                    public void onSystemError(RpcResponse rpcResponse) {
                        AnonymousClass6.this.errorBroadcast(rpcResponse);
                    }

                    public void onError(RpcResponse rpcResponse) {
                        AnonymousClass6.this.errorBroadcast(rpcResponse);
                    }
                });
            }

            /* access modifiers changed from: private */
            public void errorBroadcast(RpcResponse rpcResponse) {
                int i;
                String str = "";
                if (rpcResponse != null) {
                    int i2 = rpcResponse.code;
                    str = rpcResponse.message;
                    i = i2;
                } else {
                    i = 1100;
                }
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, i, str, "");
            }
        }, new Object[0]);
    }

    public void checkNickModifiable(final CheckResultCallback checkResultCallback) {
        if (checkResultCallback != null) {
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, String>() {
                /* access modifiers changed from: protected */
                public String doInBackground(Object[] objArr) {
                    return Login.getDeviceTokenKey(Login.getUserId());
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(String str) {
                    UrlFetchServiceImpl.getInstance().checkNickModifiable(new RpcRequestCallback() {
                        public void onSuccess(RpcResponse rpcResponse) {
                            MtopCanChangeNickResponseData mtopCanChangeNickResponseData = (MtopCanChangeNickResponseData) rpcResponse;
                            if (mtopCanChangeNickResponseData != null) {
                                checkResultCallback.result(mtopCanChangeNickResponseData.success);
                            }
                        }

                        public void onSystemError(RpcResponse rpcResponse) {
                            checkResultCallback.result(false);
                        }

                        public void onError(RpcResponse rpcResponse) {
                            checkResultCallback.result(false);
                        }
                    });
                }
            }, new Object[0]);
        }
    }

    public void navToIVWithUserId(Context context, String str, String str2) {
        navToIVWithUserId(context, str, DataProviderFactory.getDataProvider().getSite(), str2);
    }

    public void navToIVWithUserId(Context context, String str, int i, String str2) {
        if (TextUtils.isEmpty(str)) {
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -3, LoginConstant.FETCH_IV_FAIL_INVALID_SCENE, "");
        } else if (TextUtils.isEmpty(str2)) {
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -2, LoginConstant.FETCH_IV_FAIL_INVALID_USERID, "");
        } else {
            final int i2 = i;
            final String str3 = str2;
            final String str4 = str;
            final Context context2 = context;
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, GetVerifyUrlResponse>() {
                /* access modifiers changed from: protected */
                public GetVerifyUrlResponse doInBackground(Object[] objArr) {
                    VerifyParam verifyParam = new VerifyParam();
                    verifyParam.fromSite = i2;
                    verifyParam.userId = str3;
                    verifyParam.actionType = str4;
                    verifyParam.deviceTokenKey = Login.getDeviceTokenKey(str3);
                    try {
                        return VerifyServiceImpl.getInstance().getNonLoginVerfiyUrl(verifyParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(GetVerifyUrlResponse getVerifyUrlResponse) {
                    if (getVerifyUrlResponse == null) {
                        BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -1, "RET_NULL", "");
                    } else if (getVerifyUrlResponse.code != 3000) {
                        BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, getVerifyUrlResponse.code, getVerifyUrlResponse.message, "");
                    } else if (getVerifyUrlResponse.returnValue == null) {
                        BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -4, LoginConstant.FETCH_IV_FAIL_INVALID_RETURNVALUE, "");
                    } else if (!TextUtils.isEmpty(((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url)) {
                        UrlParam urlParam = new UrlParam();
                        urlParam.ivScene = str4;
                        urlParam.url = ((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url;
                        urlParam.userid = str3;
                        LoginController.this.openUrl(context2, urlParam);
                    }
                }
            }, new Object[0]);
        }
    }

    public void openUrl(Context context, UrlParam urlParam) {
        if (context == null) {
            context = DataProviderFactory.getApplicationContext();
        }
        if (urlParam == null || TextUtils.isEmpty(urlParam.url)) {
            TLogAdapter.e(TAG, "openUrl fail ,url=" + urlParam.url);
            return;
        }
        ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openWebViewPage(context, urlParam);
    }

    public void openScheme(Context context, UrlParam urlParam) {
        if (context == null) {
            context = DataProviderFactory.getApplicationContext();
        }
        if (urlParam == null || TextUtils.isEmpty(urlParam.url)) {
            TLogAdapter.e(TAG, "openScheme fail ,url=" + urlParam.url);
            return;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse(urlParam.url));
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoginSDKInited() {
        return this.isAliuserSDKInited.get();
    }

    public void initAliuserSDK(DefaultTaobaoAppProvider defaultTaobaoAppProvider) {
        if (this.isAliuserSDKInited.compareAndSet(false, true) || DataProviderFactory.getApplicationContext() == null) {
            TLogAdapter.e(TAG, "start init AliuserSDK | isAliuserSDKInited:" + this.isAliuserSDKInited.get());
            registerAliuserActionReceiver();
            LaunchInit.init(defaultTaobaoAppProvider);
            AliUserLogin.registOnLoginCaller(DataProviderFactory.getApplicationContext(), new TaobaoLoginCaller());
            TLogAdapter.e(TAG, "end init AliuserSDK");
            if (defaultTaobaoAppProvider.isNeedWindVaneInit() && !WindVaneSDK.isInitialized()) {
                initWindVa();
            }
            MainThreadExecutor.execute(new Runnable() {
                public void run() {
                    LoginController.this.addLoginPlugin();
                }
            });
        }
    }

    public static void initWindVa() {
        TLogAdapter.e(TAG, "login sdk init windvane");
        WVAppParams wVAppParams = new WVAppParams();
        try {
            if (selfPermissionGranted("android.permission.READ_PHONE_STATE")) {
                wVAppParams.imei = PhoneInfo.getImei(DataProviderFactory.getApplicationContext());
                wVAppParams.imsi = PhoneInfo.getImsi(DataProviderFactory.getApplicationContext());
            }
        } catch (Exception unused) {
        }
        wVAppParams.appKey = DataProviderFactory.getDataProvider().getAppkey();
        wVAppParams.ttid = DataProviderFactory.getDataProvider().getTTID();
        wVAppParams.appTag = "TB";
        wVAppParams.appVersion = AppInfo.getInstance().getAppVersion();
        switch (DataProviderFactory.getDataProvider().getEnvType()) {
            case 0:
            case 1:
                WindVaneSDK.setEnvMode(EnvEnum.DAILY);
                break;
            case 2:
                WindVaneSDK.setEnvMode(EnvEnum.PRE);
                break;
            case 3:
                WindVaneSDK.setEnvMode(EnvEnum.ONLINE);
                break;
            default:
                WindVaneSDK.setEnvMode(EnvEnum.ONLINE);
                break;
        }
        WindVaneSDKForDefault.init(DataProviderFactory.getApplicationContext(), wVAppParams);
        WindVaneSDK.openLog(true);
        WVCamera.registerUploadService(TBUploadService.class);
    }

    @TargetApi(23)
    private static boolean selfPermissionGranted(String str) {
        int i;
        try {
            i = DataProviderFactory.getApplicationContext().getPackageManager().getPackageInfo(DataProviderFactory.getApplicationContext().getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception unused) {
        }
        if (Build.VERSION.SDK_INT >= 23 || i < 23 || DataProviderFactory.getApplicationContext().checkSelfPermission(str) == 0) {
            return true;
        }
        return false;
        i = 0;
        return Build.VERSION.SDK_INT >= 23 ? true : true;
    }

    /* access modifiers changed from: private */
    public void addLoginPlugin() {
        TLogAdapter.d(TAG, "add aluWVJSbridge");
        WVPluginManager.registerPlugin("SDKJSBridgeService", (Class<? extends WVApiPlugin>) SDKJSBridgeService.class);
        try {
            WVPluginManager.registerPlugin("Scancode", (Class<? extends WVApiPlugin>) ScancodeCallback.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        WVPluginManager.registerPlugin("aluWVJSBridge", (Class<? extends WVApiPlugin>) JSBridgeService.class);
        WVPluginManager.registerPlugin("aluVerifyJSBridge", (Class<? extends WVApiPlugin>) VerifyJsbridge.class);
    }

    public boolean processAutoLoginResponse(RpcResponse<LoginReturnData> rpcResponse, boolean z) {
        return processAutoLoginResponse(rpcResponse, z, true, (Bundle) null);
    }

    public boolean processAutoLoginResponse(RpcResponse<LoginReturnData> rpcResponse, boolean z, boolean z2, Bundle bundle) {
        if (rpcResponse == null || !"SUCCESS".equals(rpcResponse.actionType)) {
            processNetworkError(rpcResponse, z, z2, bundle);
            return false;
        } else if (SiteUtil.getDefaultLoginSite() != 4 || DataProviderFactory.getDataProvider().isAccountProfileExist()) {
            try {
                LoginDataHelper.processLoginReturnData(z2, (LoginReturnData) rpcResponse.returnValue, LoginConstants.LoginSuccessType.TBLoginTypeAutoLogin.getType(), this.browserRefUrl);
                return true;
            } catch (Exception unused) {
                if (z) {
                    userLogin(z, z2, bundle);
                } else if (z2) {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, this.browserRefUrl);
                }
                return false;
            }
        } else {
            UserTrackAdapter.sendUT("ICBU_Profile_NoExist");
            logout();
            if (z2) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, this.browserRefUrl);
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean processPwdLoginResponse(RpcResponse<LoginReturnData> rpcResponse, boolean z) {
        if (rpcResponse == null) {
            return false;
        }
        LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
        if (rpcResponse == null || !LoginDataHelper.processLoginReturnData(z, loginReturnData, this.browserRefUrl)) {
            LoginStatus.resetLoginFlag();
            return false;
        }
        handleTrojan("userLogin");
        return true;
    }

    public class TaobaoLoginCaller implements OnLoginCaller {
        public TaobaoLoginCaller() {
        }

        public void filterLogin(final RpcResponse rpcResponse) {
            new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
                /* access modifiers changed from: protected */
                public void onPostExecute(Void voidR) {
                }

                public Void excuteTask(Object... objArr) {
                    boolean unused = LoginController.this.processPwdLoginResponse(rpcResponse, true);
                    return null;
                }
            }, new Object[0]);
        }

        public void failLogin() {
            LoginStatus.setLastRefreshCookieTime(0);
        }
    }

    private void registerAliuserActionReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LoginResActions.LOGIN_CANCEL_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_FAIL_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_SUCCESS_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_OPEN_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_NETWORK_ERROR);
            intentFilter.addAction(LoginResActions.WEB_ACTIVITY_CANCEL);
            intentFilter.addAction(LoginResActions.WEB_ACTIVITY_RESULT);
            intentFilter.addAction(LoginResActions.REG_CANCEL);
            intentFilter.addAction(AppInfo.INITED_ACTION);
            intentFilter.addAction("NOTIFY_LOGIN_STATUS_RESET");
            intentFilter.setPriority(1000);
            LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).registerReceiver(new AliuserActionReceiver(), intentFilter);
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.ali.user.sdk.login.TEST_ACCOUNT_SSO");
            DataProviderFactory.getApplicationContext().registerReceiver(new LoginTestBroadcastReceiver(), intentFilter2);
            LoginTLogAdapter.d("AliuserActionReceiver", "register receiver");
            LoginTLogAdapter.d("LoginTestBroadcastReceiver", "register receiver");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void applyToken(InternalTokenCallback internalTokenCallback) {
        applyToken(DataProviderFactory.getDataProvider().getSite(), internalTokenCallback);
    }

    public void applyToken(final int i, final InternalTokenCallback internalTokenCallback) {
        if (internalTokenCallback == null) {
            TLogAdapter.d(TAG, "Callback is null ");
            return;
        }
        try {
            if (Login.session != null) {
                BackgroundExecutor.execute(new Runnable() {
                    public void run() {
                        long j;
                        final String oneTimeToken = Login.getOneTimeToken();
                        if (oneTimeToken != null) {
                            MainThreadExecutor.execute(new Runnable() {
                                public void run() {
                                    internalTokenCallback.onSucess(oneTimeToken);
                                }
                            });
                        } else if (TextUtils.isEmpty(Login.session.getUserId())) {
                            internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_SESSION_INVALID, "Session is null or Session is invalid.");
                        } else {
                            try {
                                final RpcResponse<MLoginTokenReturnValue> applyToken = UserLoginServiceImpl.getInstance().applyToken(i, Login.session.getUserId(), (Map<String, String>) null);
                                if (applyToken == null) {
                                    AppMonitorAdapter.commitFail("Page_Member_Other", "GetHavanaSSOtoken", "0", "");
                                    MainThreadExecutor.execute(new Runnable() {
                                        public void run() {
                                            internalTokenCallback.onFail("RET_NULL", "apply token return null.");
                                        }
                                    });
                                } else if (applyToken.code != 3000 || applyToken.returnValue == null) {
                                    AppMonitorAdapter.commitFail("Page_Member_Other", "GetHavanaSSOtoken", "0", String.valueOf(applyToken.code));
                                    if (applyToken.code != 13032) {
                                        internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_NOT_SESSION_INVALID, "网络获取旺信token失败，非session失效");
                                        Login.setHavanaSsoTokenExpiredTime(0);
                                        Login.setOneTimeToken((String) null);
                                        return;
                                    }
                                    RpcResponse<LoginReturnData> autoLogin = new AutoLoginBusiness().autoLogin(Login.getLoginToken(), Login.getUserId(), SiteUtil.getDefaultLoginSite(), ApiReferer.generateApiReferer());
                                    if (autoLogin != null) {
                                        if ("SUCCESS".equals(autoLogin.actionType)) {
                                            LoginController.this.processAutoLoginResponse(autoLogin, false);
                                            final String oneTimeToken2 = Login.getOneTimeToken();
                                            MainThreadExecutor.execute(new Runnable() {
                                                public void run() {
                                                    internalTokenCallback.onSucess(oneTimeToken2);
                                                }
                                            });
                                            return;
                                        }
                                    }
                                    MainThreadExecutor.execute(new Runnable() {
                                        public void run() {
                                            InternalTokenCallback internalTokenCallback = internalTokenCallback;
                                            internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_AUTO_LOGIN_FAIL, applyToken.message + ", mtop autoLoginFail");
                                        }
                                    });
                                } else {
                                    AppMonitorAdapter.commitSuccess("Page_Member_Other", "GetHavanaSSOtoken");
                                    int i = ((MLoginTokenReturnValue) applyToken.returnValue).expireTime;
                                    if (i == 0) {
                                        j = (System.currentTimeMillis() / 1000) + 900;
                                    } else {
                                        j = (System.currentTimeMillis() / 1000) + ((long) i);
                                    }
                                    Login.setHavanaSsoTokenExpiredTime(j);
                                    Login.setOneTimeToken(((MLoginTokenReturnValue) applyToken.returnValue).token);
                                    MainThreadExecutor.execute(new Runnable() {
                                        public void run() {
                                            internalTokenCallback.onSucess(((MLoginTokenReturnValue) applyToken.returnValue).token);
                                        }
                                    });
                                }
                            } catch (RpcException e) {
                                e.printStackTrace();
                                LoginTLogAdapter.e(LoginController.TAG, (Throwable) e);
                                MainThreadExecutor.execute(new Runnable() {
                                    public void run() {
                                        InternalTokenCallback internalTokenCallback = internalTokenCallback;
                                        internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_EXCEPTION, e.getMessage() + "|" + e.getExtCode());
                                    }
                                });
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                LoginTLogAdapter.e(LoginController.TAG, (Throwable) e2);
                                MainThreadExecutor.execute(new Runnable() {
                                    public void run() {
                                        internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_EXCEPTION, e2.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
            } else {
                internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_SESSION_INVALID, "Session is null or Session is invalid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoginTLogAdapter.e(TAG, (Throwable) e);
            internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_EXCEPTION, e.getMessage());
        }
    }

    public void applyTokenWithRemoteBiz(int i, String str, InternalTokenCallback internalTokenCallback) {
        applyTokenWithRemoteBiz(i, str, (Map<String, String>) null, true, internalTokenCallback);
    }

    public void applyTokenWithRemoteBiz(int i, String str, Map<String, String> map, boolean z, final InternalTokenCallback internalTokenCallback) {
        HistoryAccount findHistoryAccount;
        if (internalTokenCallback == null) {
            TLogAdapter.d(TAG, "Callback is null ");
            return;
        }
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.APPLY_SSO_LOGIN;
        rpcRequest.VERSION = ApiConstants.ApiField.VERSION_1_1;
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(new HashMap()));
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        if (map != null) {
            rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        }
        ApplyTokenRequest applyTokenRequest = new ApplyTokenRequest();
        applyTokenRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        applyTokenRequest.t = System.currentTimeMillis();
        applyTokenRequest.appVersion = AppInfo.getInstance().getAndroidAppVersion();
        applyTokenRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        applyTokenRequest.site = i;
        rpcRequest.requestSite = i;
        if (!TextUtils.isEmpty(str) && (findHistoryAccount = SecurityGuardManagerWraper.findHistoryAccount(Long.parseLong(str))) != null) {
            applyTokenRequest.deviceTokenKey = findHistoryAccount.tokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            }
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(str);
            deviceTokenSignParam.addTimestamp(String.valueOf(applyTokenRequest.t));
            deviceTokenSignParam.addSDKVersion(AppInfo.getInstance().getSdkVersion());
            if (!TextUtils.isEmpty(findHistoryAccount.tokenKey)) {
                applyTokenRequest.deviceTokenSign = AlibabaSecurityTokenService.sign(findHistoryAccount.tokenKey, deviceTokenSignParam.build());
            }
        }
        rpcRequest.addParam("request", JSON.toJSONString(applyTokenRequest));
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, LoginTokenResponseData.class, (RpcRequestCallback) new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                long j;
                if (rpcResponse instanceof LoginTokenResponseData) {
                    final LoginTokenResponseData loginTokenResponseData = (LoginTokenResponseData) rpcResponse;
                    AppMonitorAdapter.commitSuccess("Page_Member_Other", "GetHavanaSSOtoken");
                    int i = ((MLoginTokenReturnValue) loginTokenResponseData.returnValue).expireTime;
                    if (i == 0) {
                        j = (System.currentTimeMillis() / 1000) + 900;
                    } else {
                        j = (System.currentTimeMillis() / 1000) + ((long) i);
                    }
                    Login.setHavanaSsoTokenExpiredTime(j);
                    Login.setOneTimeToken(((MLoginTokenReturnValue) loginTokenResponseData.returnValue).token);
                    MainThreadExecutor.execute(new Runnable() {
                        public void run() {
                            internalTokenCallback.onSucess(((MLoginTokenReturnValue) loginTokenResponseData.returnValue).token);
                        }
                    });
                    return;
                }
                internalTokenCallback.onFail("RET_NULL", rpcResponse.message);
            }

            public void onSystemError(RpcResponse rpcResponse) {
                internalTokenCallback.onFail("RET_NULL", rpcResponse.message);
            }

            public void onError(RpcResponse rpcResponse) {
                internalTokenCallback.onFail("RET_NULL", rpcResponse.message);
            }
        }, z);
    }

    public void applyUnifySSOToken(final int i, final String str, final InternalTokenCallback internalTokenCallback) {
        if (internalTokenCallback == null) {
            TLogAdapter.d(TAG, "Callback is null ");
        } else if (!Login.checkSessionValid()) {
            internalTokenCallback.onFail(LoginConstant.FETCH_TOKEN_FAIL_SESSION_INVALID, "session invalid");
        } else {
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginTokenResponseData>() {
                /* access modifiers changed from: protected */
                public LoginTokenResponseData doInBackground(Object... objArr) {
                    try {
                        return UserLoginServiceImpl.getInstance().applyUnifySSOToken(i, Login.getUserId(), Login.getSid(), str, new HashMap());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(LoginTokenResponseData loginTokenResponseData) {
                    if (loginTokenResponseData == null) {
                        internalTokenCallback.onFail("RET_NULL", "apply token return null.");
                    } else if (loginTokenResponseData.code != 3000 || loginTokenResponseData.returnValue == null) {
                        internalTokenCallback.onFail(String.valueOf(loginTokenResponseData.code), loginTokenResponseData.message);
                    } else {
                        internalTokenCallback.onSucess(((MLoginTokenReturnValue) loginTokenResponseData.returnValue).token);
                    }
                }
            }, new Object[0]);
        }
    }

    public void navByScheme(String str, String str2, String str3, String str4, CommonCallback commonCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = "com.taobao.mtop.mLoginTokenService.applySsoTokenV2";
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        final ApplySsoTokenRequest applySsoTokenRequest = new ApplySsoTokenRequest();
        applySsoTokenRequest.slaveAppKey = str;
        applySsoTokenRequest.masterAppKey = DataProviderFactory.getDataProvider().getAppkey();
        applySsoTokenRequest.ssoVersion = SSOIPCConstants.CURRENT_SSO_MINI_PROGRAM;
        applySsoTokenRequest.targetUrl = "hap://app/" + str2 + "/SsoLoginMid?visa=8617ab96f88d12c0";
        applySsoTokenRequest.slaveBundleId = applySsoTokenRequest.targetUrl;
        applySsoTokenRequest.hid = Login.getUserId();
        rpcRequest.addParam(ApiConstants.ApiField.SSO_TOKEN_APPLY_REQUEST, JSON.toJSONString(applySsoTokenRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(new HashMap()));
        final String str5 = str3;
        final String str6 = str4;
        final CommonCallback commonCallback2 = commonCallback;
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, SSOV2ApplySsoTokenResponseData.class, (RpcRequestCallback) new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                ResolveInfo next;
                if (rpcResponse != null) {
                    SSOV2ApplySsoTokenResponseData sSOV2ApplySsoTokenResponseData = (SSOV2ApplySsoTokenResponseData) rpcResponse;
                    Intent intent = new Intent();
                    String str = applySsoTokenRequest.targetUrl;
                    SSOMasterParam sSOMasterParam = new SSOMasterParam();
                    sSOMasterParam.appKey = DataProviderFactory.getDataProvider().getAppkey();
                    sSOMasterParam.ssoToken = (String) sSOV2ApplySsoTokenResponseData.returnValue;
                    sSOMasterParam.t = System.currentTimeMillis();
                    sSOMasterParam.userId = Login.getUserId();
                    sSOMasterParam.ssoVersion = SSOIPCConstants.CURRENT_SSO_MINI_PROGRAM;
                    try {
                        sSOMasterParam.sign = SSOSecurityService.getInstace(DataProviderFactory.getApplicationContext()).sign(sSOMasterParam.appKey, sSOMasterParam.toMap());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.setData(Uri.parse(((str + "&resultCode=500") + "&ssoToken=" + ((String) sSOV2ApplySsoTokenResponseData.returnValue)) + "&sourceAppKey=" + sSOMasterParam.appKey + "&" + "ssoVersion" + SymbolExpUtil.SYMBOL_EQUAL + sSOMasterParam.ssoVersion + "&" + SSOIPCConstants.IPC_MASTER_T + SymbolExpUtil.SYMBOL_EQUAL + sSOMasterParam.t + "&" + "userId" + SymbolExpUtil.SYMBOL_EQUAL + sSOMasterParam.userId + "&" + "sign" + SymbolExpUtil.SYMBOL_EQUAL + sSOMasterParam.sign + "&" + SSOIPCConstants.IPC_JUMP_URL + SymbolExpUtil.SYMBOL_EQUAL + Uri.encode(str5)));
                    if (!TextUtils.isEmpty(str6)) {
                        intent.setAction(str6);
                    } else {
                        intent.setAction("android.intent.action.VIEW");
                    }
                    List<ResolveInfo> queryIntentActivities = DataProviderFactory.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
                    ResolveInfo resolveInfo = null;
                    if (queryIntentActivities != null) {
                        Iterator<ResolveInfo> it = queryIntentActivities.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            next = it.next();
                            String str2 = next.activityInfo.packageName;
                            MD5Util.getApkPublicKeyDigest(str2);
                            String config = OrangeConfig.getInstance().getConfig("login4android", LoginSwitch.SUPPORT_MINI_PROGRAME, "");
                            if (TextUtils.isEmpty(config)) {
                                break;
                            }
                            try {
                                if (new JSONObject(config).optBoolean(str2)) {
                                    break;
                                }
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        resolveInfo = next;
                    }
                    if (resolveInfo != null) {
                        intent.setFlags(268435456);
                        intent.setPackage(resolveInfo.activityInfo.packageName);
                        try {
                            DataProviderFactory.getApplicationContext().startActivity(intent);
                            if (commonCallback2 != null) {
                                commonCallback2.onSuccess();
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                            if (commonCallback2 != null) {
                                commonCallback2.onFail(1001, "start activity failed");
                            }
                        }
                    } else if (commonCallback2 != null) {
                        commonCallback2.onFail(1002, "can't find packageName");
                    }
                }
            }

            public void onSystemError(RpcResponse rpcResponse) {
                if (commonCallback2 != null) {
                    int i = 1001;
                    String str = "unknown error";
                    if (rpcResponse != null) {
                        i = rpcResponse.code;
                        str = rpcResponse.message;
                    }
                    commonCallback2.onFail(i, str);
                }
            }

            public void onError(RpcResponse rpcResponse) {
                if (commonCallback2 != null) {
                    int i = 1001;
                    String str = "unknown error";
                    if (rpcResponse != null) {
                        i = rpcResponse.code;
                        str = rpcResponse.message;
                    }
                    commonCallback2.onFail(i, str);
                }
            }
        });
    }
}
