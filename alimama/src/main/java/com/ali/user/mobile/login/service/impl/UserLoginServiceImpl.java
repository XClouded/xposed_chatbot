package com.ali.user.mobile.login.service.impl;

import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.AppLaunchInfoResponseData;
import com.ali.user.mobile.login.model.GetVerifyTokenResponseData;
import com.ali.user.mobile.login.model.PreCheckResponseData;
import com.ali.user.mobile.login.param.LoginTokenType;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.ApplyTokenRequest;
import com.ali.user.mobile.rpc.login.model.DefaultLoginResponseData;
import com.ali.user.mobile.rpc.login.model.LoginCountryResponseData;
import com.ali.user.mobile.rpc.login.model.LoginRequestBase;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.login.model.LoginTokenResponseData;
import com.ali.user.mobile.rpc.login.model.MLoginTokenReturnValue;
import com.ali.user.mobile.rpc.login.model.MemberRequestBase;
import com.ali.user.mobile.rpc.login.model.PasswordLoginRequest;
import com.ali.user.mobile.rpc.login.model.TokenLoginRequest;
import com.ali.user.mobile.rpc.login.model.WSecurityData;
import com.ali.user.mobile.rpc.login.model.WUAData;
import com.ali.user.mobile.rpc.safe.RSAKey;
import com.ali.user.mobile.rpc.safe.Rsa;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.android.dinamic.DinamicConstant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class UserLoginServiceImpl {
    private static UserLoginServiceImpl instance;
    private final String TAG = "login.UserLoginServiceImpl";
    private final String UT_EVENT_LABEL = ApiConstants.UTConstants.UT_LOGIN_RESULT;

    public static UserLoginServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserLoginServiceImpl();
        }
        return instance;
    }

    private UserLoginServiceImpl() {
    }

    public RpcResponse easyLogin(LoginParam loginParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.EASY_LOGIN;
        rpcRequest.VERSION = "1.0";
        PasswordLoginRequest passwordLoginRequest = new PasswordLoginRequest();
        passwordLoginRequest.loginId = loginParam.loginAccount;
        passwordLoginRequest.password = loginParam.loginPassword;
        passwordLoginRequest.site = loginParam.loginSite;
        if (passwordLoginRequest.site == 4) {
            passwordLoginRequest.loginType = "icbu";
        }
        rpcRequest.requestSite = loginParam.loginSite;
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(passwordLoginRequest));
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
    }

    public RpcResponse unifyLoginWithTaobaoGW(LoginParam loginParam) {
        Map map;
        RpcRequest rpcRequest = new RpcRequest();
        PasswordLoginRequest passwordLoginRequest = new PasswordLoginRequest();
        if (loginParam.externParams == null) {
            map = new HashMap();
        } else {
            map = loginParam.externParams;
        }
        map.put("apiVersion", "2.0");
        try {
            map.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginParam.h5QueryString != null) {
            map.put("aliusersdk_h5querystring", loginParam.h5QueryString);
        }
        if (!TextUtils.isEmpty(loginParam.slideCheckcodeSid)) {
            passwordLoginRequest.slideCheckcodeSid = loginParam.slideCheckcodeSid;
            passwordLoginRequest.slideCheckcodeSig = loginParam.slideCheckcodeSig;
            passwordLoginRequest.slideCheckcodeToken = loginParam.slideCheckcodeToken;
        }
        if (loginParam.loginSite == 4) {
            rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_PW_LOGIN;
            rpcRequest.VERSION = "1.0";
            passwordLoginRequest.loginType = "icbu";
            if (!TextUtils.isEmpty(loginParam.snsToken)) {
                map.put(ApiConstants.ApiField.SNS_TRUST_LOGIN_TOKEN, loginParam.snsToken);
            }
            map.put(ApiConstants.ApiField.OCEAN_APPKEY, DataProviderFactory.getDataProvider().getOceanAppkey());
        } else if (loginParam.loginSite == 100) {
            rpcRequest.API_NAME = ApiConstants.ApiName.PW_LOGIN_COMMON;
            rpcRequest.VERSION = "1.0";
        } else {
            rpcRequest.API_NAME = ApiConstants.ApiName.PW_LOGIN;
            rpcRequest.VERSION = "1.0";
            passwordLoginRequest.loginType = loginParam.loginType;
            if (!TextUtils.isEmpty(loginParam.snsToken)) {
                map.put(ApiConstants.ApiField.SNS_TRUST_LOGIN_TOKEN, loginParam.snsToken);
            }
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.requestSite = loginParam.loginSite;
        passwordLoginRequest.site = loginParam.loginSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        passwordLoginRequest.locale = locale;
        passwordLoginRequest.loginId = loginParam.loginAccount;
        if (!TextUtils.isEmpty(loginParam.loginPassword)) {
            if (loginParam.loginSite != 100) {
                try {
                    String rsaPubkey = RSAKey.getRsaPubkey();
                    if (!TextUtils.isEmpty(rsaPubkey)) {
                        passwordLoginRequest.password = Rsa.encrypt(loginParam.loginPassword, rsaPubkey);
                    } else {
                        TLogAdapter.e("login.UserLoginServiceImpl", "RSAKey == null");
                        throw new RpcException("getRsaKeyResult is null");
                    }
                } catch (RpcException e2) {
                    throw new RpcException("get RSA exception===> " + e2.getMessage());
                }
            } else {
                passwordLoginRequest.password = loginParam.loginPassword;
            }
        }
        passwordLoginRequest.pwdEncrypted = true;
        passwordLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        passwordLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        passwordLoginRequest.ccId = loginParam.checkCodeId;
        passwordLoginRequest.checkCode = loginParam.checkCode;
        passwordLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        passwordLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        passwordLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        passwordLoginRequest.t = System.currentTimeMillis();
        if (!TextUtils.isEmpty(loginParam.deviceTokenKey)) {
            passwordLoginRequest.deviceTokenKey = loginParam.deviceTokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(String.valueOf(loginParam.havanaId));
            deviceTokenSignParam.addTimestamp(String.valueOf(passwordLoginRequest.t));
            deviceTokenSignParam.addSDKVersion(passwordLoginRequest.sdkVersion);
            passwordLoginRequest.deviceTokenSign = AlibabaSecurityTokenService.sign(passwordLoginRequest.deviceTokenKey, deviceTokenSignParam.build());
            if (Debuggable.isDebug()) {
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop key=" + passwordLoginRequest.deviceTokenKey);
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop sign=" + passwordLoginRequest.deviceTokenSign);
            }
            passwordLoginRequest.hid = loginParam.havanaId + "";
            passwordLoginRequest.alipayHid = loginParam.alipayHid;
        }
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(passwordLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        if (!(post == null || post.returnValue == null)) {
            ((LoginReturnData) post.returnValue).loginType = loginParam.loginType;
        }
        pwdLoginUT(loginParam, post);
        return post;
    }

    public void getScanToken(LoginParam loginParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.FETCH_LOING_SCAN_TOKEN;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = false;
        rpcRequest.NEED_SESSION = false;
        LoginRequestBase loginRequestBase = new LoginRequestBase();
        loginRequestBase.hid = String.valueOf(loginParam.havanaId);
        if (!TextUtils.isEmpty(loginParam.deviceTokenKey)) {
            loginRequestBase.deviceTokenKey = loginParam.deviceTokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(String.valueOf(loginParam.havanaId));
            deviceTokenSignParam.addTimestamp(String.valueOf(loginRequestBase.t));
            deviceTokenSignParam.addSDKVersion(loginRequestBase.sdkVersion);
            loginRequestBase.deviceTokenSign = AlibabaSecurityTokenService.sign(loginRequestBase.deviceTokenKey, deviceTokenSignParam.build());
            if (Debuggable.isDebug()) {
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop key=" + loginRequestBase.deviceTokenKey);
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop sign=" + loginRequestBase.deviceTokenSign);
            }
            loginRequestBase.hid = loginParam.havanaId + "";
        }
        HashMap hashMap = new HashMap();
        hashMap.put("apiVersion", "2.0");
        try {
            hashMap.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        loginRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        loginRequestBase.site = DataProviderFactory.getDataProvider().getSite();
        loginRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        loginRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        loginRequestBase.utdid = AppInfo.getInstance().getUtdid();
        loginRequestBase.t = System.currentTimeMillis();
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(loginRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        requestWithRemoteBusiness(rpcRequest, new GetVerifyTokenResponseData(), rpcRequestCallback);
    }

    public RpcResponse precheckScanLogin(LoginParam loginParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.LOGIN_SCAN_PRE_JUDGE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = false;
        rpcRequest.NEED_SESSION = false;
        LoginRequestBase loginRequestBase = new LoginRequestBase();
        loginRequestBase.hid = String.valueOf(loginParam.havanaId);
        if (!TextUtils.isEmpty(loginParam.deviceTokenKey)) {
            loginRequestBase.deviceTokenKey = loginParam.deviceTokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(String.valueOf(loginParam.havanaId));
            deviceTokenSignParam.addTimestamp(String.valueOf(loginRequestBase.t));
            deviceTokenSignParam.addSDKVersion(loginRequestBase.sdkVersion);
            loginRequestBase.deviceTokenSign = AlibabaSecurityTokenService.sign(loginRequestBase.deviceTokenKey, deviceTokenSignParam.build());
            if (Debuggable.isDebug()) {
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop key=" + loginRequestBase.deviceTokenKey);
                TLogAdapter.d("login.UserLoginServiceImpl", "mtop sign=" + loginRequestBase.deviceTokenSign);
            }
            loginRequestBase.hid = loginParam.havanaId + "";
        }
        HashMap hashMap = new HashMap();
        hashMap.put("apiVersion", "2.0");
        try {
            hashMap.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        loginRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        loginRequestBase.site = DataProviderFactory.getDataProvider().getSite();
        loginRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        loginRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        loginRequestBase.utdid = AppInfo.getInstance().getUtdid();
        loginRequestBase.t = System.currentTimeMillis();
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(loginRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildRPSecurityData()));
        return (PreCheckResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, PreCheckResponseData.class, String.valueOf(loginParam.havanaId));
    }

    private static <V extends RpcResponse<?>> void requestWithRemoteBusiness(RpcRequest rpcRequest, V v, RpcRequestCallback rpcRequestCallback) {
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, v.getClass(), rpcRequestCallback);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        com.ut.mini.UTAnalytics.getInstance().updateUserAccount(r7.nick, r7.userId);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void pwdLoginUT(com.ali.user.mobile.model.LoginParam r6, com.ali.user.mobile.rpc.RpcResponse r7) {
        /*
            r5 = this;
            r0 = 0
            if (r7 == 0) goto L_0x0115
            java.lang.String r1 = r7.actionType     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x0115
            java.lang.String r1 = "SUCCESS"
            java.lang.String r2 = r7.actionType     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r1.equals(r2)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x00bf
            T r7 = r7.returnValue     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.rpc.login.model.LoginReturnData r7 = (com.ali.user.mobile.rpc.login.model.LoginReturnData) r7     // Catch:{ Exception -> 0x0113 }
            if (r7 == 0) goto L_0x0051
            java.lang.String r7 = r7.data     // Catch:{ Exception -> 0x0113 }
            java.lang.Class<com.ali.user.mobile.rpc.login.model.AliUserResponseData> r1 = com.ali.user.mobile.rpc.login.model.AliUserResponseData.class
            java.lang.Object r7 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r7, r1)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.rpc.login.model.AliUserResponseData r7 = (com.ali.user.mobile.rpc.login.model.AliUserResponseData) r7     // Catch:{ Exception -> 0x0113 }
            if (r7 == 0) goto L_0x0051
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r2 = r7.nick     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r3 = r7.userId     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r4 = r7.uidDigest     // Catch:{ Throwable -> 0x0046 }
            r1.updateUserAccount(r2, r3, r4)     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r1 = r7.userId     // Catch:{ Throwable -> 0x0046 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0046 }
            if (r1 != 0) goto L_0x0051
            java.lang.String r1 = r7.uidDigest     // Catch:{ Throwable -> 0x0046 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0046 }
            if (r1 == 0) goto L_0x0051
            java.lang.String r1 = "UT_OPENID_IS_NULL"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r1)     // Catch:{ Throwable -> 0x0046 }
            goto L_0x0051
        L_0x0046:
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r2 = r7.nick     // Catch:{ Exception -> 0x0113 }
            java.lang.String r7 = r7.userId     // Catch:{ Exception -> 0x0113 }
            r1.updateUserAccount(r2, r7)     // Catch:{ Exception -> 0x0113 }
        L_0x0051:
            java.util.Properties r7 = new java.util.Properties     // Catch:{ Exception -> 0x0113 }
            r7.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "is_success"
            java.lang.String r2 = "T"
            r7.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = r6.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0113 }
            if (r1 != 0) goto L_0x0088
            java.lang.String r1 = "Page_Login5-Reg"
            java.lang.String r2 = r6.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.equals(r1, r2)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x0077
            java.lang.String r1 = "source"
            java.lang.String r2 = "Page_Login5-RegistSuc"
            r7.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            goto L_0x0088
        L_0x0077:
            java.lang.String r1 = "Page_Login5-Login"
            java.lang.String r2 = r6.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.equals(r1, r2)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x0088
            java.lang.String r1 = "source"
            java.lang.String r2 = "Page_Login5-LoginSuc"
            r7.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
        L_0x0088:
            boolean r1 = r6.isFromAccount     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x00a1
            java.lang.String r1 = "type"
            java.lang.String r2 = "NoFirstLoginSuccessByTb"
            r7.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "Page_Login3"
            java.lang.String r2 = "LoginResult"
            java.lang.String r3 = r6.loginAccount     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = r5.getAccountType(r3)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r2, r0, r3, r7)     // Catch:{ Exception -> 0x0113 }
            goto L_0x00b5
        L_0x00a1:
            java.lang.String r1 = "type"
            java.lang.String r2 = "TbLoginSuccess"
            r7.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "Page_Login1"
            java.lang.String r2 = "LoginResult"
            java.lang.String r3 = r6.loginAccount     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = r5.getAccountType(r3)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r2, r0, r3, r7)     // Catch:{ Exception -> 0x0113 }
        L_0x00b5:
            java.lang.String r7 = "Page_Member_Login"
            java.lang.String r1 = "Login_Pwd"
            java.lang.String r2 = "type=TBLogin"
            com.ali.user.mobile.log.AppMonitorAdapter.commitSuccess(r7, r1, r2)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011f
        L_0x00bf:
            java.lang.String r1 = "H5"
            java.lang.String r2 = r7.actionType     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r1.equals(r2)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x010f
            java.util.Properties r1 = new java.util.Properties     // Catch:{ Exception -> 0x0113 }
            r1.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r2 = "is_success"
            java.lang.String r3 = "F"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x0113 }
            boolean r2 = r6.isFromAccount     // Catch:{ Exception -> 0x0113 }
            if (r2 == 0) goto L_0x00f4
            java.lang.String r2 = "type"
            java.lang.String r3 = "NoFirstLoginH5"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r2 = "Page_Login3"
            java.lang.String r3 = "LoginResult"
            int r7 = r7.code     // Catch:{ Exception -> 0x0113 }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = r6.loginAccount     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = r5.getAccountType(r4)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r2, r3, r7, r4, r1)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011f
        L_0x00f4:
            java.lang.String r2 = "type"
            java.lang.String r3 = "TbLoginH5"
            r1.setProperty(r2, r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r2 = "Page_Login1"
            java.lang.String r3 = "LoginResult"
            int r7 = r7.code     // Catch:{ Exception -> 0x0113 }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = r6.loginAccount     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = r5.getAccountType(r4)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r2, r3, r7, r4, r1)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011f
        L_0x010f:
            r5.pwdFailUT(r6, r7)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011f
        L_0x0113:
            r7 = move-exception
            goto L_0x0119
        L_0x0115:
            r5.pwdFailUT(r6, r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011f
        L_0x0119:
            r7.printStackTrace()
            r5.pwdFailUT(r6, r0)
        L_0x011f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.login.service.impl.UserLoginServiceImpl.pwdLoginUT(com.ali.user.mobile.model.LoginParam, com.ali.user.mobile.rpc.RpcResponse):void");
    }

    private void pwdFailUT(LoginParam loginParam, RpcResponse rpcResponse) {
        String str;
        Properties properties = new Properties();
        properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
        String str2 = ApiConstants.UTConstants.UT_NETWORK_FAIL;
        if (rpcResponse != null) {
            str2 = String.valueOf(rpcResponse.code);
        }
        if (loginParam.isFromAccount) {
            str = ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN;
            properties.setProperty("type", "NoFirstLoginFailure");
        } else {
            str = ApiConstants.UTConstants.UT_PAGE_FIRST_LOGIN;
            properties.setProperty("type", "TbLoginFailure");
        }
        UserTrackAdapter.sendUT(str, ApiConstants.UTConstants.UT_LOGIN_RESULT, str2, getAccountType(loginParam.loginAccount), properties);
        AppMonitorAdapter.commitFail("Page_Member_Login", "Login_Pwd", "0", str2);
    }

    private String getAccountType(String str) {
        if (TextUtils.isEmpty(str)) {
            return "LoginType_Nick";
        }
        if (str.contains(DinamicConstant.DINAMIC_PREFIX_AT)) {
            return "LoginType_Email";
        }
        return (str.length() != 11 || !StringUtil.isInteger(str)) ? "LoginType_Nick" : ApiConstants.UTConstants.UT_LOGIN_TYPE_PHONE;
    }

    public RpcResponse loginByToken(LoginParam loginParam) {
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(getTokenLoginRpcRequest(loginParam), DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        if (!(post == null || post.returnValue == null)) {
            ((LoginReturnData) post.returnValue).loginType = loginParam.loginType;
        }
        tokenLoginUT(post, loginParam);
        return post;
    }

    public RpcResponse loginByQrToken(LoginParam loginParam) {
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(getQrTokenRequest(loginParam), DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        qrTokenLoginUT(post, loginParam);
        return post;
    }

    private RpcRequest getQrTokenRequest(LoginParam loginParam) {
        Map map;
        RpcRequest rpcRequest = new RpcRequest();
        if (loginParam.externParams == null) {
            map = new HashMap();
        } else {
            map = loginParam.externParams;
        }
        try {
            map.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.API_NAME = ApiConstants.ApiName.COMMON_SCANED_LOGIN;
        rpcRequest.VERSION = "1.0";
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.locale = locale;
        tokenLoginRequest.site = loginParam.loginSite;
        tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        tokenLoginRequest.tokenType = "newQRCode";
        tokenLoginRequest.token = loginParam.token;
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return rpcRequest;
    }

    @NonNull
    private RpcRequest getTokenLoginRpcRequest(LoginParam loginParam) {
        Map map;
        RpcRequest rpcRequest = new RpcRequest();
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        if (loginParam.externParams == null) {
            map = new HashMap();
        } else {
            map = loginParam.externParams;
        }
        map.put("apiVersion", "2.0");
        try {
            map.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginParam.h5QueryString != null) {
            map.put("aliusersdk_h5querystring", loginParam.h5QueryString);
        }
        if (loginParam.loginSite == 4) {
            rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_TOKEN_LOGIN;
            rpcRequest.VERSION = "1.0";
            if (!TextUtils.isEmpty(loginParam.snsToken)) {
                map.put(ApiConstants.ApiField.SNS_TRUST_LOGIN_TOKEN, loginParam.snsToken);
            }
            map.put(ApiConstants.ApiField.OCEAN_APPKEY, DataProviderFactory.getDataProvider().getOceanAppkey());
            String locale = Locale.SIMPLIFIED_CHINESE.toString();
            if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
                locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
            }
            tokenLoginRequest.locale = locale;
        } else if (loginParam.loginSite == 100) {
            rpcRequest.API_NAME = ApiConstants.ApiName.TOKEN_LOGIN_COMMON;
            rpcRequest.VERSION = "1.0";
        } else {
            rpcRequest.API_NAME = ApiConstants.ApiName.TOKEN_LOGIN;
            rpcRequest.VERSION = "1.0";
            if (!TextUtils.isEmpty(loginParam.snsToken)) {
                map.put(ApiConstants.ApiField.SNS_TRUST_LOGIN_TOKEN, loginParam.snsToken);
            }
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.requestSite = loginParam.loginSite;
        tokenLoginRequest.site = loginParam.loginSite;
        tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        tokenLoginRequest.tokenType = LoginTokenType.MLOGIN_TOKEN;
        tokenLoginRequest.scene = loginParam.scene;
        tokenLoginRequest.token = loginParam.token;
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return rpcRequest;
    }

    public void loginByTokenRemoteBiz(LoginParam loginParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest tokenLoginRpcRequest = getTokenLoginRpcRequest(loginParam);
        tokenLoginRpcRequest.NEED_SESSION = false;
        tokenLoginRpcRequest.NEED_ECODE = false;
        requestWithRemoteBusiness(tokenLoginRpcRequest, new DefaultLoginResponseData(), rpcRequestCallback);
    }

    private void qrTokenLoginUT(RpcResponse rpcResponse, LoginParam loginParam) {
        if (rpcResponse != null) {
            String str = rpcResponse.actionType;
        }
    }

    private void tokenLoginUT(RpcResponse rpcResponse, LoginParam loginParam) {
        if (rpcResponse != null) {
            try {
                if (rpcResponse.actionType == null) {
                    return;
                }
                if ("SUCCESS".equals(rpcResponse.actionType)) {
                    Properties properties = new Properties();
                    properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
                    properties.setProperty("type", "ContinueLoginSuccess");
                    if (!TextUtils.isEmpty(loginParam.source)) {
                        if (TextUtils.equals("Page_Login5-Reg", loginParam.source)) {
                            properties.setProperty("source", "Page_Login5-RegistSuc");
                        } else if (TextUtils.equals("Page_Login5-Login", loginParam.source)) {
                            properties.setProperty("source", "Page_Login5-LoginSuc");
                        }
                    }
                    String tokenType = getTokenType(loginParam);
                    UserTrackAdapter.sendUT("Page_Extend", ApiConstants.UTConstants.UT_LOGIN_RESULT, (String) null, "type=" + tokenType, properties);
                    AppMonitorAdapter.commitSuccess("Page_Member_Login", "Login_Pwd", "type=" + getTokenType(loginParam));
                } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType)) {
                    Properties properties2 = new Properties();
                    properties2.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                    properties2.setProperty("type", "ContinueLoginH5");
                    UserTrackAdapter.sendUT("Page_Extend", ApiConstants.UTConstants.UT_LOGIN_RESULT, (String) null, "type=" + getTokenType(loginParam), properties2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getTokenType(LoginParam loginParam) {
        if (loginParam == null) {
            return TokenType.LOGIN;
        }
        if (!TextUtils.isEmpty(loginParam.snsToken)) {
            return TokenType.SNS;
        }
        return !TextUtils.isEmpty(loginParam.tokenType) ? loginParam.tokenType : TokenType.LOGIN;
    }

    public RpcResponse<LoginReturnData> loginByAlipaySSOToken(String str, Map<String, Object> map) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.ALIPAY_SSO_LOGIN;
        rpcRequest.VERSION = "2.0";
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        tokenLoginRequest.token = str;
        tokenLoginRequest.ext = map;
        tokenLoginRequest.site = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.requestSite = tokenLoginRequest.site;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        tokenLoginRequest.locale = locale;
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        try {
            HashMap hashMap = new HashMap();
            hashMap.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
            hashMap.put("apiReferer", ApiReferer.getApiRefer());
            rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class);
    }

    public RpcResponse<MLoginTokenReturnValue> applyToken(int i, String str, Map<String, String> map) {
        HistoryAccount findHistoryAccount;
        RpcRequest rpcRequest = new RpcRequest();
        if (map == null) {
            map = new HashMap<>();
        }
        rpcRequest.requestSite = i;
        if (i == 4) {
            rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_APPLY_SSO_TOKEN;
            rpcRequest.VERSION = "1.0";
            rpcRequest.addParam("userId", str);
            map.put(ApiConstants.ApiField.OCEAN_APPKEY, DataProviderFactory.getDataProvider().getOceanAppkey());
        } else if (i == 100) {
            rpcRequest.API_NAME = ApiConstants.ApiName.APPLY_SSO_LOGIN_COMMON;
            rpcRequest.VERSION = "1.0";
        } else {
            rpcRequest.API_NAME = ApiConstants.ApiName.APPLY_SSO_LOGIN;
            rpcRequest.VERSION = ApiConstants.ApiField.VERSION_1_1;
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        ApplyTokenRequest applyTokenRequest = new ApplyTokenRequest();
        applyTokenRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        applyTokenRequest.t = System.currentTimeMillis();
        applyTokenRequest.appVersion = AppInfo.getInstance().getAndroidAppVersion();
        applyTokenRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        applyTokenRequest.site = i;
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
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, LoginTokenResponseData.class, str);
    }

    public LoginTokenResponseData applyUnifySSOToken(int i, String str, String str2, String str3, Map<String, String> map) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.APPLY_UNIFY_SSO_TOKEN;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("oa_userid", Long.valueOf(Long.parseLong(str)));
        rpcRequest.addParam("oa_sid", str2);
        ApplyTokenRequest applyTokenRequest = new ApplyTokenRequest();
        applyTokenRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        applyTokenRequest.site = i;
        rpcRequest.requestSite = i;
        applyTokenRequest.t = System.currentTimeMillis();
        applyTokenRequest.appVersion = AppInfo.getInstance().getAndroidAppVersion();
        applyTokenRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        rpcRequest.addParam("request", JSON.toJSONString(applyTokenRequest));
        HashMap hashMap = new HashMap();
        hashMap.put("havanaId", str3);
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        return (LoginTokenResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, LoginTokenResponseData.class);
    }

    public RpcResponse unifySsoTokenLogin(LoginParam loginParam) {
        RpcRequest rpcRequest = new RpcRequest();
        if (loginParam.loginSite == 100) {
            rpcRequest.API_NAME = ApiConstants.ApiName.UNIFY_SSO_LOGIN_COMMON;
            rpcRequest.VERSION = "1.0";
        } else {
            rpcRequest.API_NAME = ApiConstants.ApiName.UNIFY_SSO_LOGIN;
            rpcRequest.VERSION = "1.0";
        }
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.token = loginParam.token;
        rpcRequest.requestSite = loginParam.loginSite;
        tokenLoginRequest.site = loginParam.loginSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        tokenLoginRequest.locale = locale;
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        if (!(post == null || post.returnValue == null)) {
            ((LoginReturnData) post.returnValue).loginType = loginParam.loginType;
        }
        return post;
    }

    @Deprecated
    public RpcResponse getCountryCodes(int i, Map<String, String> map) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.MOBILE_LOGIN_COUNTRY_LIST;
        rpcRequest.VERSION = "1.0";
        MemberRequestBase memberRequestBase = new MemberRequestBase();
        memberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        memberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        memberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        memberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        memberRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        memberRequestBase.site = i;
        rpcRequest.requestSite = i;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        memberRequestBase.locale = locale;
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(memberRequestBase));
        WSecurityData wSecurityData = new WSecurityData();
        WUAData wua = SecurityGuardManagerWraper.getWUA();
        if (wua != null) {
            wSecurityData.wua = wua.wua;
        }
        wSecurityData.apdId = AlipayInfo.getInstance().getApdid();
        wSecurityData.umidToken = AppInfo.getInstance().getUmidToken();
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(wSecurityData));
        if (map != null) {
            rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(map));
        }
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, LoginCountryResponseData.class);
    }

    public RpcResponse getAppLaunchInfo(LoginParam loginParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_APP_LAUNCH_INFO;
        rpcRequest.VERSION = "1.0";
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, AppLaunchInfoResponseData.class);
    }
}
