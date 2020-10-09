package com.ali.user.mobile.login.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.BasePresenter;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.helper.LoginDataHelper;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.LongLifeCycleUserTrack;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.model.UTLoginFromEnum;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.login.ui.BaseLoginView;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.ResourceUtil;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.accs.common.Constants;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.statistic.TBS;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseLoginPresenter implements BasePresenter {
    protected static final int LOGIN_REQUEST = 1002;
    protected static final int SEND_SMS_REQUEST = 1001;
    protected static final String TAG = ("login." + BaseLoginPresenter.class.getSimpleName());
    public LoginParam mLoginParam;
    protected BaseLoginView mViewer;
    public String registAccount;
    public boolean utFromRegist;

    /* access modifiers changed from: protected */
    public void onActivityResultForSMSMachine(int i, Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onReceiveRegister(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
    }

    public BaseLoginPresenter(BaseLoginView baseLoginView, LoginParam loginParam) {
        this.mViewer = baseLoginView;
        this.mLoginParam = loginParam;
        if (this.mLoginParam != null) {
            this.mLoginParam.loginSite = this.mViewer.getLoginSite();
        }
    }

    public void onStart() {
        if (this.mLoginParam == null) {
            return;
        }
        if (!TextUtils.isEmpty(this.mLoginParam.token)) {
            login();
        } else if (!TextUtils.isEmpty(this.mLoginParam.loginAccount)) {
            this.mViewer.setLoginAccountInfo(this.mLoginParam.loginAccount);
        }
    }

    public void login() {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            unifyLogin(this.mLoginParam, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (BaseLoginPresenter.this.mViewer != null && BaseLoginPresenter.this.mViewer.isActive()) {
                        BaseLoginPresenter.this.updateLoginParam(BaseLoginPresenter.this.mLoginParam, rpcResponse);
                        if (BaseLoginPresenter.this.loginResultAction(BaseLoginPresenter.this.mLoginParam, rpcResponse)) {
                            BaseLoginPresenter.this.mViewer.dismissLoading();
                        } else if (AliUserLogin.mLoginCaller != null) {
                            AliUserLogin.mLoginCaller.failLogin();
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (BaseLoginPresenter.this.mViewer != null && BaseLoginPresenter.this.mViewer.isActive()) {
                        BaseLoginPresenter.this.mViewer.dismissLoading();
                        if (rpcResponse != null) {
                            SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException(Integer.valueOf(rpcResponse.code), rpcResponse.message));
                        } else {
                            SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                        }
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (BaseLoginPresenter.this.mViewer != null && BaseLoginPresenter.this.mViewer.isActive()) {
                        BaseLoginPresenter.this.mViewer.dismissLoading();
                        BaseLoginPresenter.this.updateLoginParam(BaseLoginPresenter.this.mLoginParam, rpcResponse);
                        boolean unused = BaseLoginPresenter.this.loginResultAction(BaseLoginPresenter.this.mLoginParam, rpcResponse);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void unifyLogin(final LoginParam loginParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse<LoginReturnData>>() {
            /* access modifiers changed from: protected */
            public RpcResponse<LoginReturnData> doInBackground(Object... objArr) {
                if (BaseLoginPresenter.this.mViewer != null && BaseLoginPresenter.this.mViewer.isHistoryMode()) {
                    BaseLoginPresenter.this.matchHistoryAccount();
                }
                try {
                    if (loginParam.externParams == null) {
                        loginParam.externParams = new HashMap();
                    }
                    loginParam.externParams.put("apiReferer", ApiReferer.generateApiReferer());
                    loginParam.isFromAccount = BaseLoginPresenter.this.mViewer.isHistoryMode();
                    return BaseLoginPresenter.this.login(loginParam);
                } catch (RpcException e) {
                    e.printStackTrace();
                    BaseLoginPresenter.this.sendLoginErrorUT(BaseLoginPresenter.this.mLoginParam, e);
                    return null;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse<LoginReturnData> rpcResponse) {
                if (rpcRequestCallback != null) {
                    if (rpcResponse == null) {
                        rpcRequestCallback.onSystemError((RpcResponse) null);
                    } else if (rpcResponse.code == 3000) {
                        rpcRequestCallback.onSuccess(rpcResponse);
                    } else {
                        rpcRequestCallback.onError(rpcResponse);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public RpcResponse login(LoginParam loginParam) {
        if (loginParam.token != null) {
            return UserLoginServiceImpl.getInstance().loginByToken(loginParam);
        }
        return UserLoginServiceImpl.getInstance().unifyLoginWithTaobaoGW(loginParam);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 257 && (i2 == 258 || i2 == 0 || i2 == 259)) {
            onActivityResultForWebView(i2, intent);
        } else if (i == 1001) {
            onActivityResultForSMSMachine(i2, intent);
        } else if (i == 1002) {
            onActivityResultForNativeMachine(i2, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResultForWebView(int i, Intent intent) {
        if ((intent != null && "quit".equals(intent.getStringExtra("action"))) || i == 0) {
            cleanDataHodler();
        } else if (intent != null && i == 259) {
            this.mLoginParam.h5QueryString = intent.getStringExtra("aliusersdk_h5querystring");
            login();
        } else if (intent == null) {
        } else {
            if (this.mLoginParam != null && this.mLoginParam.externParams != null && LoginConstant.ACTION_CONTINUELOGIN.equals(this.mLoginParam.externParams.get(LoginConstant.EXT_ACTION))) {
                this.mLoginParam.h5QueryString = intent.getStringExtra("aliusersdk_h5querystring");
                login();
            } else if (this.mViewer != null && this.mLoginParam != null) {
                this.mLoginParam.h5QueryString = intent.getStringExtra("aliusersdk_h5querystring");
                login();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResultForNativeMachine(int i, Intent intent) {
        if (i == -1 && intent != null) {
            this.mLoginParam.slideCheckcodeSid = intent.getStringExtra("sid");
            this.mLoginParam.slideCheckcodeSig = intent.getStringExtra("sig");
            this.mLoginParam.slideCheckcodeToken = intent.getStringExtra("token");
            login();
        }
    }

    /* access modifiers changed from: protected */
    public void matchHistoryAccount() {
        HistoryAccount matchHistoryAccount;
        if (this.mLoginParam != null && (matchHistoryAccount = SecurityGuardManagerWraper.matchHistoryAccount(this.mLoginParam.loginAccount)) != null) {
            this.mLoginParam.deviceTokenKey = matchHistoryAccount.tokenKey;
            this.mLoginParam.havanaId = matchHistoryAccount.userId;
        }
    }

    public void buildTokenParam(String str, String str2, String str3) {
        if (this.mLoginParam == null) {
            this.mLoginParam = new LoginParam();
        }
        this.mLoginParam.loginSite = this.mViewer.getLoginSite();
        this.mLoginParam.token = str;
        this.mLoginParam.tokenType = str2;
        this.mLoginParam.scene = str3;
        if (this.mLoginParam.externParams == null) {
            this.mLoginParam.externParams = new HashMap();
        }
        this.mLoginParam.externParams.put("apiReferer", ApiReferer.generateApiReferer());
        this.mLoginParam.tid = DataProviderFactory.getDataProvider().getTID();
    }

    public void buildLoginParam(String str, String str2) {
        if (this.mLoginParam == null) {
            this.mLoginParam = new LoginParam();
        }
        this.mLoginParam.isFromAccount = this.mViewer.isHistoryMode();
        this.mLoginParam.loginSite = this.mViewer.getLoginSite();
        this.mLoginParam.loginAccount = str;
        this.mLoginParam.loginPassword = str2;
        if (this.mLoginParam.externParams == null) {
            this.mLoginParam.externParams = new HashMap();
        }
        this.mLoginParam.externParams.put("apiReferer", ApiReferer.generateApiReferer());
        this.mLoginParam.tid = DataProviderFactory.getDataProvider().getTID();
        this.mLoginParam.loginType = this.mViewer.getLoginType().getType();
        this.mLoginParam.deviceTokenKey = "";
        this.mLoginParam.havanaId = 0;
    }

    /* access modifiers changed from: private */
    public void updateLoginParam(LoginParam loginParam, RpcResponse rpcResponse) {
        if (rpcResponse != null) {
            LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
            if (loginReturnData != null) {
                loginParam.scene = loginReturnData.scene;
                loginParam.token = loginReturnData.token;
                loginParam.isFromRegister = false;
                loginParam.isFoundPassword = false;
                loginParam.enableVoiceSMS = false;
                loginParam.h5QueryString = null;
                if (loginReturnData.extMap == null) {
                    return;
                }
                if (loginParam.externParams == null) {
                    loginParam.externParams = loginReturnData.extMap;
                    return;
                }
                loginParam.externParams = new HashMap();
                for (Map.Entry next : loginReturnData.extMap.entrySet()) {
                    loginParam.externParams.put(next.getKey(), next.getValue());
                }
                return;
            }
            loginParam.scene = null;
            loginParam.token = null;
            loginParam.tokenType = null;
            loginParam.isFamilyLoginToReg = false;
            loginParam.snsToken = null;
            loginParam.isFromRegister = false;
            loginParam.enableVoiceSMS = false;
            loginParam.h5QueryString = null;
            loginParam.externParams = null;
        }
    }

    /* access modifiers changed from: private */
    public boolean loginResultAction(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        try {
            Properties properties = new Properties();
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            TBS.Ext.commitEventEnd("Event_LoginCost", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mViewer == null || !this.mViewer.isActive()) {
            return true;
        }
        if (rpcResponse != null) {
            String str = rpcResponse.actionType;
            String str2 = TAG;
            TLogAdapter.d(str2, "actionType=" + str + ", msg=" + rpcResponse.message);
            if (str != null) {
                if ("SUCCESS".equals(str)) {
                    return onReceiveSuccess(loginParam, rpcResponse);
                }
                AppMonitor.Alarm.commitFail("Page_Login", "login", String.valueOf(rpcResponse.code), rpcResponse.message == null ? "" : rpcResponse.message);
                if (ApiConstants.ResultActionType.H5.equals(str)) {
                    onReceiveH5(loginParam, rpcResponse);
                } else if (ApiConstants.ResultActionType.TOAST.equals(str)) {
                    onReceiveToast(loginParam, rpcResponse);
                } else if (ApiConstants.ResultActionType.ALERT.equals(str)) {
                    onReceiveAlert(loginParam, rpcResponse);
                } else if (ApiConstants.ResultActionType.REGISTER.equals(str)) {
                    onReceiveRegister(loginParam, rpcResponse);
                } else if (this.mViewer != null && !TextUtils.isEmpty(rpcResponse.message)) {
                    this.mViewer.toast(rpcResponse.message, 0);
                }
            } else if (this.mViewer != null && !TextUtils.isEmpty(rpcResponse.message)) {
                this.mViewer.toast(rpcResponse.message, 0);
            }
        } else if (this.mViewer != null) {
            SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException(this.mViewer.getBaseActivity().getString(R.string.aliuser_tb_login_exception)));
        }
        if (this.mViewer != null) {
            this.mViewer.onError(rpcResponse);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onReceiveSuccess(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        boolean z;
        AppMonitor.Alarm.commitSuccess("Page_Login", "login");
        if (this.mViewer != null) {
            z = false;
            this.mViewer.onSuccess(loginParam, rpcResponse);
        } else {
            z = true;
        }
        if (!TextUtils.isEmpty(LongLifeCycleUserTrack.getResultScene())) {
            LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_SUCEESS");
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void onReceiveH5(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
        if (TextUtils.equals(loginReturnData.showNativeMachineVerify, "true")) {
            this.mViewer.onNeedVerification("", 1002);
        } else if (!TextUtils.isEmpty(loginReturnData.h5Url)) {
            String str = loginReturnData.h5Url;
            loginParam.tokenType = TokenType.LOGIN;
            NavigatorManager.getInstance().navToWebViewPage(this.mViewer.getBaseActivity(), str, loginParam, loginReturnData);
        } else {
            this.mViewer.toast(rpcResponse.message, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onReceiveToast(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        onReceiveAlert(loginParam, rpcResponse);
    }

    /* access modifiers changed from: protected */
    public void onReceiveAlert(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        if (this.mViewer != null) {
            String str = rpcResponse.message;
            if (TextUtils.isEmpty(str)) {
                str = ResourceUtil.getStringById("aliuser_network_error");
            }
            this.mViewer.alert("", str, this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_common_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BaseLoginPresenter.this.mViewer != null && BaseLoginPresenter.this.mViewer.isActive()) {
                        BaseLoginPresenter.this.mViewer.dismissAlertDialog();
                    }
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        }
    }

    public void onLoginSuccess(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        LoginReturnData loginReturnData;
        if (rpcResponse != null && (loginReturnData = (LoginReturnData) rpcResponse.returnValue) != null) {
            LoginDataHelper.processLoginReturnData(false, loginReturnData, LoginStatus.browserRefUrl);
            boolean sendLocalBroadCast = BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_SUCCESS_ACTION));
            String str = TAG;
            TLogAdapter.d(str, "send login success broadcast finish:sendResult = " + sendLocalBroadCast);
        }
    }

    public void onLoginFail(RpcResponse<LoginReturnData> rpcResponse) {
        LoginStatus.setLastRefreshCookieTime(0);
    }

    /* access modifiers changed from: protected */
    public void sendLoginErrorUT(LoginParam loginParam, RpcException rpcException) {
        try {
            Properties properties = new Properties();
            properties.setProperty("username", loginParam.loginAccount);
            properties.setProperty("errorCode", String.valueOf(rpcException.getCode()));
            properties.setProperty(Constants.KEY_TARGET, "RPCException");
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            properties.setProperty(WeexDevOptions.EXTRA_FROM, getTBSFrom());
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("Event_LoginFail");
            uTCustomHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTBSFrom() {
        String name = UTLoginFromEnum.login.name();
        if (!this.utFromRegist) {
            return name;
        }
        if (TextUtils.isEmpty(this.registAccount) || TextUtils.indexOf(this.registAccount, DinamicConstant.DINAMIC_PREFIX_AT) <= 0) {
            return UTLoginFromEnum.mobileReg.name();
        }
        return UTLoginFromEnum.emailReg.name();
    }

    /* access modifiers changed from: protected */
    public void cleanDataHodler() {
        if (this.mLoginParam != null && !this.mLoginParam.isFromRegister && !this.mLoginParam.isFoundPassword) {
            this.mLoginParam.scene = null;
            this.mLoginParam.token = null;
            if (this.mLoginParam.externParams != null) {
                this.mLoginParam.externParams.remove(LoginConstant.EXT_ACTION);
            }
        }
    }

    public LoginParam getLoginParam() {
        return this.mLoginParam;
    }

    public void onDestory() {
        this.mViewer = null;
    }
}
