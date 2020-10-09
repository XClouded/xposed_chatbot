package com.taobao.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.LongLifeCycleUserTrack;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.SSOMasterParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.security.biz.R;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.alibaba.fastjson.JSON;
import com.taobao.accs.common.Constants;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.android.sso.v2.model.SSOIPCConstants;
import com.taobao.android.sso.v2.model.SSOV2SsoLoginResponseData;
import com.taobao.android.sso.v2.model.SsoLoginRequest;
import com.taobao.login4android.biz.alipaysso.AlipayConstant;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.SessionManager;
import java.util.HashMap;
import java.util.Properties;

public class TBSsoLogin {
    public static final String TAG = "Login.TBSsoLogin";

    public static void login(Activity activity, Bundle bundle, ISsoRemoteParam iSsoRemoteParam) {
        if (bundle != null && bundle.getInt(SSOIPCConstants.APPLY_SSO_RESULT, 0) == 500) {
            loginWithToken(activity, bundle, iSsoRemoteParam);
            UserTrackAdapter.sendUT("Taobao_AuthCode_Login");
            LongLifeCycleUserTrack.setResultScene("Taobao_AuthCode_Login");
        }
    }

    public static void loginWithAuthCode(final Activity activity, final String str, final String str2) {
        UserTrackAdapter.sendUT("Alipay_AuthCode_Login");
        LongLifeCycleUserTrack.setResultScene("Alipay_AuthCode_Login");
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse<LoginReturnData>>() {
            /* access modifiers changed from: protected */
            public RpcResponse<LoginReturnData> doInBackground(Object... objArr) {
                try {
                    HashMap hashMap = new HashMap();
                    hashMap.put("auth_code", str);
                    hashMap.put(AlipayConstant.SSO_ALIPAY_DES_KEY, str2);
                    hashMap.put(AlipayConstant.SSO_ALIPAY_ENABLE_KEY, true);
                    hashMap.put("uuid", AlipayInfo.getInstance().getApdidToken());
                    return UserLoginServiceImpl.getInstance().loginByAlipaySSOToken((String) null, hashMap);
                } catch (RpcException | Exception unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            /* JADX WARNING: Can't wrap try/catch for region: R(4:10|11|12|13) */
            /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x002c */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onPostExecute(com.ali.user.mobile.rpc.RpcResponse<com.ali.user.mobile.rpc.login.model.LoginReturnData> r12) {
                /*
                    r11 = this;
                    if (r12 == 0) goto L_0x0057
                    T r0 = r12.returnValue     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x0057
                    int r0 = r12.code     // Catch:{ RpcException -> 0x0054 }
                    r1 = 3000(0xbb8, float:4.204E-42)
                    if (r0 != r1) goto L_0x0057
                    T r0 = r12.returnValue     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.rpc.login.model.LoginReturnData r0 = (com.ali.user.mobile.rpc.login.model.LoginReturnData) r0     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x0037
                    java.lang.String r0 = r0.data     // Catch:{ RpcException -> 0x0054 }
                    java.lang.Class<com.ali.user.mobile.rpc.login.model.AliUserResponseData> r1 = com.ali.user.mobile.rpc.login.model.AliUserResponseData.class
                    java.lang.Object r0 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r0, r1)     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.rpc.login.model.AliUserResponseData r0 = (com.ali.user.mobile.rpc.login.model.AliUserResponseData) r0     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x0037
                    com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x002c }
                    java.lang.String r2 = r0.nick     // Catch:{ Throwable -> 0x002c }
                    java.lang.String r3 = r0.userId     // Catch:{ Throwable -> 0x002c }
                    java.lang.String r4 = r0.uidDigest     // Catch:{ Throwable -> 0x002c }
                    r1.updateUserAccount(r2, r3, r4)     // Catch:{ Throwable -> 0x002c }
                    goto L_0x0037
                L_0x002c:
                    com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r2 = r0.nick     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r0 = r0.userId     // Catch:{ RpcException -> 0x0054 }
                    r1.updateUserAccount(r2, r0)     // Catch:{ RpcException -> 0x0054 }
                L_0x0037:
                    android.content.Context r0 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ RpcException -> 0x0054 }
                    com.taobao.login4android.session.SessionManager r0 = com.taobao.login4android.session.SessionManager.getInstance(r0)     // Catch:{ RpcException -> 0x0054 }
                    T r12 = r12.returnValue     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.rpc.login.model.LoginReturnData r12 = (com.ali.user.mobile.rpc.login.model.LoginReturnData) r12     // Catch:{ RpcException -> 0x0054 }
                    com.taobao.login4android.login.LoginResultHelper.saveLoginData(r12, r0)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r12 = "Alipay_AuthCode_Login_SUCCESS"
                    com.ali.user.mobile.log.LongLifeCycleUserTrack.sendUT(r12)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r12 = "Page_Member_SSO"
                    java.lang.String r0 = "AlipaySSO_Login"
                    com.ali.user.mobile.log.AppMonitorAdapter.commitSuccess(r12, r0)     // Catch:{ RpcException -> 0x0054 }
                    goto L_0x0111
                L_0x0054:
                    r12 = move-exception
                    goto L_0x00fd
                L_0x0057:
                    if (r12 == 0) goto L_0x0080
                    java.lang.String r0 = "H5"
                    java.lang.String r1 = r12.actionType     // Catch:{ RpcException -> 0x0054 }
                    boolean r0 = r0.equals(r1)     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x0080
                    T r0 = r12.returnValue     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x0080
                    T r12 = r12.returnValue     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.rpc.login.model.LoginReturnData r12 = (com.ali.user.mobile.rpc.login.model.LoginReturnData) r12     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.model.LoginParam r0 = new com.ali.user.mobile.model.LoginParam     // Catch:{ RpcException -> 0x0054 }
                    r0.<init>()     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r1 = "AlipaySSO"
                    r0.tokenType = r1     // Catch:{ RpcException -> 0x0054 }
                    android.app.Activity r1 = r2     // Catch:{ RpcException -> 0x0054 }
                    com.taobao.login4android.login.LoginResultHelper.gotoH5PlaceHolder(r1, r12, r0)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r12 = "Alipay_AuthCode_Login_H5"
                    com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r12)     // Catch:{ RpcException -> 0x0054 }
                    goto L_0x0111
                L_0x0080:
                    if (r12 == 0) goto L_0x00db
                    java.lang.String r0 = r12.actionType     // Catch:{ RpcException -> 0x0054 }
                    if (r0 == 0) goto L_0x00db
                    java.lang.String r0 = r12.message     // Catch:{ RpcException -> 0x0054 }
                    boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ RpcException -> 0x0054 }
                    if (r0 != 0) goto L_0x00db
                    java.util.Properties r0 = new java.util.Properties     // Catch:{ RpcException -> 0x0054 }
                    r0.<init>()     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r1 = "code"
                    int r2 = r12.code     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ RpcException -> 0x0054 }
                    r0.setProperty(r1, r2)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r1 = "message"
                    java.lang.String r2 = r12.message     // Catch:{ RpcException -> 0x0054 }
                    r0.setProperty(r1, r2)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r1 = "Alipay_AuthCode_Login_FAILURE"
                    com.ali.user.mobile.log.LongLifeCycleUserTrack.sendUT(r1, r0)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r0 = "Page_Member_SSO"
                    java.lang.String r1 = "AlipaySSO_Login"
                    java.lang.String r2 = "0"
                    int r3 = r12.code     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.log.AppMonitorAdapter.commitFail(r0, r1, r2, r3)     // Catch:{ RpcException -> 0x0054 }
                    com.ali.user.mobile.helper.ActivityUIHelper r4 = new com.ali.user.mobile.helper.ActivityUIHelper     // Catch:{ RpcException -> 0x0054 }
                    android.app.Activity r0 = r2     // Catch:{ RpcException -> 0x0054 }
                    r4.<init>(r0)     // Catch:{ RpcException -> 0x0054 }
                    android.app.Activity r0 = r2     // Catch:{ RpcException -> 0x0054 }
                    android.content.res.Resources r0 = r0.getResources()     // Catch:{ RpcException -> 0x0054 }
                    int r1 = com.ali.user.mobile.security.biz.R.string.aliuser_confirm     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r7 = r0.getString(r1)     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r5 = ""
                    java.lang.String r6 = r12.message     // Catch:{ RpcException -> 0x0054 }
                    com.taobao.android.TBSsoLogin$1$1 r8 = new com.taobao.android.TBSsoLogin$1$1     // Catch:{ RpcException -> 0x0054 }
                    r8.<init>(r4)     // Catch:{ RpcException -> 0x0054 }
                    r9 = 0
                    r10 = 0
                    r4.alert(r5, r6, r7, r8, r9, r10)     // Catch:{ RpcException -> 0x0054 }
                    goto L_0x0111
                L_0x00db:
                    java.lang.String r0 = "-1"
                    if (r12 == 0) goto L_0x00e5
                    int r12 = r12.code     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r0 = java.lang.String.valueOf(r12)     // Catch:{ RpcException -> 0x0054 }
                L_0x00e5:
                    java.lang.String r12 = "Page_Member_SSO"
                    java.lang.String r1 = "AlipaySSO_Login"
                    java.lang.String r2 = "0"
                    com.ali.user.mobile.log.AppMonitorAdapter.commitFail(r12, r1, r2, r0)     // Catch:{ RpcException -> 0x0054 }
                    r12 = -1
                    android.content.Context r0 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ RpcException -> 0x0054 }
                    int r1 = com.ali.user.mobile.security.biz.R.string.aliuser_network_error     // Catch:{ RpcException -> 0x0054 }
                    java.lang.String r0 = r0.getString(r1)     // Catch:{ RpcException -> 0x0054 }
                    com.taobao.android.TBSsoLogin.sendFailBroadcast(r12, r0)     // Catch:{ RpcException -> 0x0054 }
                    goto L_0x0111
                L_0x00fd:
                    r12.printStackTrace()
                    com.ali.user.mobile.base.helper.SDKExceptionHelper r0 = com.ali.user.mobile.base.helper.SDKExceptionHelper.getInstance()
                    r0.rpcExceptionHandler(r12)
                    android.content.Intent r12 = new android.content.Intent
                    java.lang.String r0 = "com.ali.user.sdk.login.NETWORK_ERROR"
                    r12.<init>(r0)
                    com.ali.user.mobile.base.helper.BroadCastHelper.sendLocalBroadCast(r12)
                L_0x0111:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.TBSsoLogin.AnonymousClass1.onPostExecute(com.ali.user.mobile.rpc.RpcResponse):void");
            }
        }, new Object[0]);
    }

    public static SSOV2SsoLoginResponseData ssologin(Context context, ISsoRemoteParam iSsoRemoteParam, SsoLoginRequest ssoLoginRequest) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.SSO_LOGIN;
        rpcRequest.VERSION = "2.0";
        rpcRequest.addParam(ApiConstants.ApiField.HID, ssoLoginRequest.hid);
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(ssoLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(new HashMap()));
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        return (SSOV2SsoLoginResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, SSOV2SsoLoginResponseData.class, ssoLoginRequest.hid);
    }

    public static void loginWithToken(final Activity activity, final Bundle bundle, final ISsoRemoteParam iSsoRemoteParam) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, SSOV2SsoLoginResponseData>() {
            /* access modifiers changed from: protected */
            public SSOV2SsoLoginResponseData doInBackground(Object... objArr) {
                try {
                    SsoLoginRequest ssoLoginRequest = new SsoLoginRequest();
                    ssoLoginRequest.masterAppKey = bundle.getString(SSOIPCConstants.IPC_MASTER_APPKEY);
                    ssoLoginRequest.slaveAppKey = iSsoRemoteParam.getAppKey();
                    ssoLoginRequest.ssoToken = bundle.getString("ssoToken");
                    ssoLoginRequest.ssoVersion = bundle.getString("ssoVersion");
                    SSOMasterParam sSOMasterParam = new SSOMasterParam();
                    sSOMasterParam.appKey = ssoLoginRequest.masterAppKey;
                    sSOMasterParam.ssoToken = ssoLoginRequest.ssoToken;
                    ssoLoginRequest.sign = bundle.getString("sign");
                    ssoLoginRequest.uuid = activity.getSharedPreferences("uuid", 0).getString("uuid", "");
                    ssoLoginRequest.masterT = bundle.getLong(SSOIPCConstants.IPC_MASTER_T, 0);
                    ssoLoginRequest.appName = ssoLoginRequest.slaveAppKey;
                    ssoLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
                    ssoLoginRequest.hid = bundle.getString("userId");
                    ssoLoginRequest.site = DataProviderFactory.getDataProvider().getSite();
                    HashMap hashMap = new HashMap();
                    String string = bundle.getString(SSOIPCConstants.IPC_MASTER_KIDS_STATUS);
                    if (!TextUtils.isEmpty(string)) {
                        hashMap.put(SSOIPCConstants.IPC_MASTER_KIDS_STATUS, string);
                    }
                    String string2 = bundle.getString(SSOIPCConstants.IPC_MASTER_KIDS_USERID);
                    if (!TextUtils.isEmpty(string2)) {
                        hashMap.put(SSOIPCConstants.IPC_MASTER_KIDS_USERID, string2);
                    }
                    ssoLoginRequest.ext = hashMap;
                    return TBSsoLogin.ssologin(activity.getApplicationContext(), iSsoRemoteParam, ssoLoginRequest);
                } catch (RpcException unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(SSOV2SsoLoginResponseData sSOV2SsoLoginResponseData) {
                if (sSOV2SsoLoginResponseData != null) {
                    try {
                        if (sSOV2SsoLoginResponseData.returnValue != null && sSOV2SsoLoginResponseData.code == 3000) {
                            SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
                            if (((LoginReturnData) sSOV2SsoLoginResponseData.returnValue).extMap == null) {
                                ((LoginReturnData) sSOV2SsoLoginResponseData.returnValue).extMap = new HashMap();
                            }
                            ((LoginReturnData) sSOV2SsoLoginResponseData.returnValue).extMap.put(LoginConstant.LOGIN_TYPE, LoginType.AUTH_ACCOUNT.getType());
                            LoginResultHelper.saveLoginData((LoginReturnData) sSOV2SsoLoginResponseData.returnValue, instance);
                            LongLifeCycleUserTrack.sendUT("Taobao_AuthCode_Login_SUCCESS");
                            AppMonitorAdapter.commitSuccess("Page_Member_SSO", "TaobaoSSO_Login");
                            return;
                        }
                    } catch (RpcException e) {
                        e.printStackTrace();
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(e);
                        TBSsoLogin.sendFailBroadcast(-1, DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                        return;
                    }
                }
                if (sSOV2SsoLoginResponseData == null || !ApiConstants.ResultActionType.H5.equals(sSOV2SsoLoginResponseData.actionType) || sSOV2SsoLoginResponseData.returnValue == null) {
                    String string = DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error);
                    if (sSOV2SsoLoginResponseData != null && !TextUtils.isEmpty(sSOV2SsoLoginResponseData.message)) {
                        string = sSOV2SsoLoginResponseData.message;
                    }
                    TBSsoLogin.sendFailBroadcast(-2, string);
                    LongLifeCycleUserTrack.sendUT("Taobao_AuthCode_Login_FAILURE");
                    String str = "-1";
                    if (sSOV2SsoLoginResponseData != null) {
                        str = String.valueOf(sSOV2SsoLoginResponseData.code);
                    }
                    AppMonitorAdapter.commitFail("Page_Member_SSO", "TaobaoSSO_Login", "0", str);
                    return;
                }
                LoginParam loginParam = new LoginParam();
                loginParam.tokenType = TokenType.TAOBAO_SSO;
                LoginResultHelper.gotoH5PlaceHolder(activity, (LoginReturnData) sSOV2SsoLoginResponseData.returnValue, loginParam);
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public static void sendFailBroadcast(int i, final String str) {
        MainThreadExecutor.execute(new Runnable() {
            public void run() {
                LoginStatus.setLastRefreshCookieTime(0);
                Intent intent = new Intent(LoginResActions.LOGIN_NETWORK_ERROR);
                if (!TextUtils.isEmpty(str)) {
                    intent.putExtra("message", str);
                }
                BroadCastHelper.sendLocalBroadCast(intent);
            }
        });
    }

    public static void loginAfterH5(final Activity activity, final LoginParam loginParam) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse<LoginReturnData>>() {
            /* access modifiers changed from: protected */
            public RpcResponse<LoginReturnData> doInBackground(Object... objArr) {
                try {
                    if (loginParam.externParams == null) {
                        loginParam.externParams = new HashMap();
                    }
                    loginParam.externParams.put("apiReferer", "SSOV2_H5_tokenLogin");
                    return TBSsoLogin.unifyLogin(loginParam);
                } catch (RpcException e) {
                    try {
                        Properties properties = new Properties();
                        properties.setProperty("username", loginParam.loginAccount);
                        properties.setProperty("errorCode", String.valueOf(e.getCode()));
                        properties.setProperty(Constants.KEY_TARGET, "RPCException");
                        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                            properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
                        }
                        UserTrackAdapter.sendUT("Event_LoginFail", properties);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    return null;
                } catch (Exception unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse<LoginReturnData> rpcResponse) {
                if (rpcResponse == null) {
                    try {
                        TBSsoLogin.sendFailBroadcast(-1, DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    } catch (RpcException unused) {
                        LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_FAILURE");
                        TBSsoLogin.sendFailBroadcast(-1, DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error));
                    }
                } else {
                    LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
                    if ("SUCCESS".equals(rpcResponse.actionType) && loginReturnData != null) {
                        if (((LoginReturnData) rpcResponse.returnValue).extMap == null) {
                            ((LoginReturnData) rpcResponse.returnValue).extMap = new HashMap();
                        }
                        ((LoginReturnData) rpcResponse.returnValue).extMap.put(LoginConstant.LOGIN_TYPE, LoginType.AUTH_ACCOUNT.getType());
                        LoginResultHelper.saveLoginData((LoginReturnData) rpcResponse.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
                        LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_SUCCESS");
                    } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType)) {
                        LoginParam loginParam = new LoginParam();
                        loginParam.tokenType = TokenType.TAOBAO_SSO;
                        LoginResultHelper.gotoH5PlaceHolder(activity, loginReturnData, loginParam);
                    } else {
                        LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_FAILURE");
                        String string = DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error);
                        if (!TextUtils.isEmpty(rpcResponse.message)) {
                            string = rpcResponse.message;
                        }
                        TBSsoLogin.sendFailBroadcast(-2, string);
                    }
                }
            }
        }, new Object[0]);
    }

    protected static RpcResponse unifyLogin(LoginParam loginParam) {
        if (loginParam.token != null) {
            return UserLoginServiceImpl.getInstance().loginByToken(loginParam);
        }
        return null;
    }
}
