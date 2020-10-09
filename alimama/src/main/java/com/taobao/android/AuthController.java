package com.taobao.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.url.model.AccountCenterParam;
import com.ali.user.mobile.url.service.impl.UrlFetchServiceImpl;
import com.ali.user.mobile.utils.SiteUtil;
import com.taobao.android.login.AuthActivity;
import com.taobao.android.sso.R;
import com.taobao.login4android.biz.alipaysso.AlipayConstant;
import com.taobao.login4android.biz.autologin.AutoLoginBusiness;
import com.taobao.login4android.biz.logout.LogoutBusiness;
import com.taobao.login4android.biz.unifysso.UnifySsoLogin;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.login.AliuserActionReceiver;
import com.taobao.login4android.session.ISession;
import com.taobao.login4android.thread.LoginTask;
import com.taobao.login4android.thread.LoginThreadHelper;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class AuthController {
    public static final String TAG = "login.auth";
    private static AuthController controller;
    public String browserRefUrl;
    private AtomicBoolean isAliuserSDKInited = new AtomicBoolean(false);
    private boolean isNotifyLogout = false;

    private AuthController() {
    }

    public static synchronized AuthController getInstance() {
        AuthController authController;
        synchronized (AuthController.class) {
            if (controller == null) {
                controller = new AuthController();
            }
            authController = controller;
        }
        return authController;
    }

    public void initAuthSDK(TaobaoAppProvider taobaoAppProvider) {
        if (this.isAliuserSDKInited.compareAndSet(false, true) || DataProviderFactory.getApplicationContext() == null) {
            registerAliuserActionReceiver();
            TLogAdapter.d(TAG, "start init authSDK | isAuthSDKInited: " + this.isAliuserSDKInited);
            LaunchInit.init(taobaoAppProvider);
        }
    }

    public void autoLogin(boolean z, Bundle bundle) {
        String userId = Auth.getUserId();
        String loginToken = Auth.getLoginToken();
        int defaultLoginSite = SiteUtil.getDefaultLoginSite();
        if (bundle != null) {
            boolean z2 = bundle.getBoolean("easylogin2", false);
            String string = bundle.getString(AlipayConstant.LOGIN_ALIPAY_TOKEN_KEY, "");
            String string2 = bundle.getString(LoginConstant.LOGIN_BUNDLE_UNIFY_SSO_TOKEN, "");
            boolean z3 = bundle.getBoolean(LoginConstant.CHANGE_ACCOUNT_FLAG);
            if (z2) {
                easyLogin2(bundle);
                return;
            } else if (!TextUtils.isEmpty(string2)) {
                UnifySsoLogin.tokenLogin(bundle.getInt(LoginConstant.ALIUSER_LOGIN_SITE, 0), string2, Auth.session);
                return;
            } else if (TextUtils.isEmpty(string) || !DataProviderFactory.getDataProvider().enableAlipaySSO()) {
                if (z3) {
                    userId = bundle.getString(LoginConstant.CHANGE_ACCOUNT_USER_ID, "");
                    loginToken = bundle.getString(LoginConstant.CHANGE_ACCOUNT_AUTOLOGIN_TOKEN, "");
                    defaultLoginSite = bundle.getInt(LoginConstant.ALIUSER_LOGIN_SITE, 0);
                    LoginStatus.compareAndSetFromChangeAccount(false, true);
                    Auth.session.appendEventTrace(", change to account " + userId + ", targetSite= " + defaultLoginSite);
                }
                String str = "," + bundle.getString("apiReferer");
                if (!TextUtils.isEmpty(this.browserRefUrl)) {
                    str = str + ", redirectUrl:" + this.browserRefUrl;
                    Properties properties = new Properties();
                    properties.setProperty("url", this.browserRefUrl);
                    UserTrackAdapter.sendUT("LoginConstants.BROWSER_REF_URL", properties);
                }
                Auth.session.appendEventTrace(str);
            } else {
                return;
            }
        }
        String str2 = userId;
        String str3 = loginToken;
        int i = defaultLoginSite;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (Auth.session != null) {
                Auth.session.appendEventTrace(",autoLoginToken=null trySdkLogin");
            }
            try {
                Properties properties2 = new Properties();
                properties2.put("action", "autologin token null trySdkLogin");
                UserTrackAdapter.sendUT("NullAutoLoginToken", properties2);
            } catch (Exception unused) {
            }
            userLogin(z, true, bundle);
            return;
        }
        handleTrojan("autologin");
        autoLoginTargetAccount(str2, str3, i, z, bundle);
    }

    public void autoLoginTargetAccount(String str, String str2, int i, boolean z, Bundle bundle) {
        try {
            processAutoLoginResponse(new AutoLoginBusiness().autoLogin(str2, str, i, z, ApiReferer.generateApiReferer()), z, true, bundle);
        } catch (Exception e) {
            if (TextUtils.equals(Auth.getUserId(), str) && Auth.session != null) {
                ISession iSession = Auth.session;
                iSession.appendEventTrace(",autoLoginFailed. Exception=" + e.getMessage());
            }
            userLogin(z, true, bundle);
        } catch (Throwable th) {
            if (TextUtils.equals(Auth.getUserId(), str)) {
                ISession iSession2 = Auth.session;
                iSession2.appendEventTrace(",autoLoginFailed. Exception=" + th.getMessage());
            }
            userLogin(z, true, bundle);
        }
    }

    public boolean processAutoLoginResponse(RpcResponse<LoginReturnData> rpcResponse, boolean z, boolean z2, Bundle bundle) {
        if (rpcResponse == null || !"SUCCESS".equals(rpcResponse.actionType)) {
            processNetworkError(rpcResponse, z, z2, bundle);
            return false;
        }
        try {
            LoginDataHelper.processLoginReturnData(z2, (LoginReturnData) rpcResponse.returnValue, LoginConstants.LoginSuccessType.TBLoginTypeAutoLogin.getType(), this.browserRefUrl);
            return true;
        } catch (Exception unused) {
            if (z) {
                userLogin(z, z2, bundle);
            } else if (z2) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, (Map<String, String>) null, this.browserRefUrl);
            }
            return false;
        }
    }

    public void openLoginPage(Context context, Boolean bool) {
        try {
            LoginTLogAdapter.d(TAG, "start sdk openLoginPage");
            if (context != null) {
                Intent callingIntent = AuthActivity.getCallingIntent(context, "");
                if (!(context instanceof Activity)) {
                    callingIntent.addFlags(268435456);
                }
                context.startActivity(callingIntent);
            }
            TLogAdapter.i(TAG, "aliuserAuth.openLoginPage");
        } catch (Exception e) {
            e.printStackTrace();
            if (bool.booleanValue()) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "Exception", this.browserRefUrl);
            }
            LoginTLogAdapter.d(TAG, "open register page failed: Exception:" + e.getMessage());
        }
    }

    public void userLogin(boolean z, final boolean z2, Bundle bundle) {
        if (z) {
            LoginTLogAdapter.d(TAG, "try sdkLogin");
            LoginThreadHelper.runOnUIThread(new LoginTask() {
                public void excuteTask() {
                    if (DataProviderFactory.getApplicationContext() != null) {
                        try {
                            Auth.session.clearSessionOnlyCookie();
                            LoginTLogAdapter.d(LoginTask.TAG, "start sdk login");
                            AuthController.this.openLoginPage(DataProviderFactory.getApplicationContext(), Boolean.valueOf(z2));
                            LoginTLogAdapter.i(LoginTask.TAG, "aliuserLogin.openLoginPage");
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (z2) {
                                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -1, "Exception", AuthController.this.browserRefUrl);
                            }
                            LoginTLogAdapter.d(LoginTask.TAG, "login failed: Exception:" + e.getMessage());
                        }
                    } else {
                        if (z2) {
                            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -2, "ContextNull", AuthController.this.browserRefUrl);
                        }
                        LoginTLogAdapter.d(LoginTask.TAG, "DataProviderFactory.getApplicationContext() is null");
                    }
                }
            });
        } else if (z2) {
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, -3, "showUI=false", this.browserRefUrl);
        }
    }

    public synchronized void logout(int i, String str, String str2, String str3, boolean z) {
        this.isNotifyLogout = false;
        new LogoutBusiness().logout(i, str, str2, str3);
        logoutClean(str3);
        if (z) {
            emptySessionList();
        }
    }

    public synchronized void logout() {
        logout(Auth.getLoginSite(), Auth.getSid(), Auth.getLoginToken(), Auth.getUserId(), false);
    }

    public void emptySessionList() {
        SecurityGuardManagerWraper.emptySessionListFromFile();
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

    public void clearLoginInfo(String str) {
        LoginTLogAdapter.d(TAG, "clearLoginInfo");
        try {
            SecurityGuardManagerWraper.removeSessionModelFromFile(str);
            Auth.session.setSsoToken((String) null);
            Auth.session.setOneTimeToken((String) null);
            Auth.session.clearSessionInfo();
            Auth.session.clearAutoLoginInfo();
            LoginTLogAdapter.e(TAG, "clear sessionInfo in LoginController.clearLoginInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleTrojan(final String str) {
        new CoordinatorWrapper().execute(new Runnable() {
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.put("action", str);
                    UserTrackAdapter.sendUT("IAntiTrojan", properties);
                    String nick = Auth.getNick();
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

    private void registerAliuserActionReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LoginResActions.LOGIN_CANCEL_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_FAIL_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_SUCCESS_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_OPEN_ACTION);
            intentFilter.addAction(LoginResActions.LOGIN_NETWORK_ERROR);
            intentFilter.addAction(LoginResActions.WEB_ACTIVITY_CANCEL);
            intentFilter.addAction(AppInfo.INITED_ACTION);
            intentFilter.addAction("NOTIFY_LOGIN_STATUS_RESET");
            intentFilter.setPriority(1000);
            LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).registerReceiver(new AliuserActionReceiver(), intentFilter);
            LoginTLogAdapter.d("AliuserActionReceiver", "register receiver");
        } catch (Throwable th) {
            th.printStackTrace();
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
        try {
            processPwdLoginResponse(UserLoginServiceImpl.getInstance().easyLogin(loginParam), true);
        } catch (Exception unused) {
            LoginStatus.resetLoginFlag();
        }
    }

    private void processNetworkError(RpcResponse<LoginReturnData> rpcResponse, boolean z, boolean z2, Bundle bundle) {
        if (RpcException.isSystemError(rpcResponse.code)) {
            if (LoginStatus.isFromChangeAccount()) {
                Auth.session.recoverCookie();
            }
            if (z2) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED, false, rpcResponse.code, rpcResponse.message, this.browserRefUrl);
            }
            Toast.makeText(DataProviderFactory.getApplicationContext(), DataProviderFactory.getApplicationContext().getResources().getString(R.string.aliuser_network_error), 0).show();
            return;
        }
        userLogin(z, z2, bundle);
    }

    private boolean processPwdLoginResponse(RpcResponse<LoginReturnData> rpcResponse, boolean z) {
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

    private void logoutClean(String str) {
        if (!this.isNotifyLogout) {
            clearLoginInfo(str);
            Auth.session.appendEventTrace(", EVENT:USER_LOGOUT");
            LoginStatus.setLastRefreshCookieTime(0);
            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGOUT);
            LoginTLogAdapter.d(TAG, "logout finish");
            this.isNotifyLogout = true;
        }
    }
}
