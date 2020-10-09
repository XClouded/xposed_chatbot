package com.ali.user.mobile.data;

import android.os.Build;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.DefaultLoginResponseData;
import com.ali.user.mobile.rpc.login.model.LoginRequestBase;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.login.model.SMSLoginRequest;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class LoginComponent {
    private static LoginComponent mRegisterComponent;
    private final String TAG = "login.RegisterComponent";

    private LoginComponent() {
    }

    public static LoginComponent getInstance() {
        if (mRegisterComponent == null) {
            synchronized (LoginComponent.class) {
                if (mRegisterComponent == null) {
                    mRegisterComponent = new LoginComponent();
                }
            }
        }
        return mRegisterComponent;
    }

    public RpcResponse sendSmsByLogin(LoginParam loginParam) {
        Map map;
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_LOGIN_SEND_SMS;
        rpcRequest.VERSION = "1.0";
        SMSLoginRequest sMSLoginRequest = new SMSLoginRequest();
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
        if (loginParam.enableVoiceSMS) {
            map.put("sendAudio", "true");
        }
        if (loginParam.h5QueryString != null) {
            map.put("aliusersdk_h5querystring", loginParam.h5QueryString);
        }
        sMSLoginRequest.loginType = loginParam.loginType;
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.requestSite = loginParam.loginSite;
        sMSLoginRequest.site = loginParam.loginSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        sMSLoginRequest.locale = locale;
        sMSLoginRequest.loginId = loginParam.loginAccount;
        sMSLoginRequest.countryCode = loginParam.countryCode;
        sMSLoginRequest.phoneCode = loginParam.phoneCode;
        sMSLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        sMSLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        sMSLoginRequest.slideCheckcodeSid = loginParam.slideCheckcodeSid;
        sMSLoginRequest.slideCheckcodeSig = loginParam.slideCheckcodeSig;
        sMSLoginRequest.slideCheckcodeToken = loginParam.slideCheckcodeToken;
        sMSLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        sMSLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        sMSLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        sMSLoginRequest.t = System.currentTimeMillis();
        if (!TextUtils.isEmpty(loginParam.deviceTokenKey)) {
            sMSLoginRequest.deviceTokenKey = loginParam.deviceTokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(String.valueOf(loginParam.havanaId));
            deviceTokenSignParam.addTimestamp(String.valueOf(sMSLoginRequest.t));
            deviceTokenSignParam.addSDKVersion(sMSLoginRequest.sdkVersion);
            sMSLoginRequest.deviceTokenSign = AlibabaSecurityTokenService.sign(sMSLoginRequest.deviceTokenKey, deviceTokenSignParam.build());
            if (Debuggable.isDebug()) {
                TLogAdapter.d("login.RegisterComponent", "mtop key=" + sMSLoginRequest.deviceTokenKey);
                TLogAdapter.d("login.RegisterComponent", "mtop sign=" + sMSLoginRequest.deviceTokenSign);
            }
            sMSLoginRequest.hid = loginParam.havanaId + "";
            sMSLoginRequest.alipayHid = loginParam.alipayHid;
        }
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(sMSLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        if (!(post == null || post.returnValue == null)) {
            ((LoginReturnData) post.returnValue).loginType = loginParam.loginType;
        }
        sendSMSUT(loginParam, post);
        return post;
    }

    private void sendSMSUT(LoginParam loginParam, RpcResponse rpcResponse) {
        try {
            String str = loginParam.isFromAccount ? ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN2 : ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN1;
            if (rpcResponse == null || rpcResponse.actionType == null) {
                sendSMSFailUT(loginParam, rpcResponse);
            } else if ("SUCCESS".equals(rpcResponse.actionType)) {
                Properties properties = new Properties();
                properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
                properties.setProperty("result", ApiConstants.UTConstants.UT_SEND_RESULT_SUCCESS);
                UserTrackAdapter.sendUT(str, ApiConstants.UTConstants.UT_SEND_SMS_RESULT, (String) null, (String) null, properties);
            } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType)) {
                LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
                if (loginReturnData == null || !"true".equals(loginReturnData.showNativeMachineVerify)) {
                    sendSMSFailUT(loginParam, rpcResponse);
                    return;
                }
                Properties properties2 = new Properties();
                properties2.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                properties2.setProperty("result", ApiConstants.UTConstants.UT_SEND_RESULT_SLIDE);
                UserTrackAdapter.sendUT(str, ApiConstants.UTConstants.UT_SEND_SMS_RESULT, (String) null, (String) null, properties2);
            } else {
                sendSMSFailUT(loginParam, rpcResponse);
            }
        } catch (Exception unused) {
            sendSMSFailUT(loginParam, rpcResponse);
        }
    }

    private void sendSMSFailUT(LoginParam loginParam, RpcResponse rpcResponse) {
        Properties properties = new Properties();
        properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
        properties.setProperty("result", ApiConstants.UTConstants.UT_SEND_RESULT_FAIL);
        String str = ApiConstants.UTConstants.UT_NETWORK_FAIL;
        if (rpcResponse != null) {
            str = String.valueOf(rpcResponse.code);
        }
        UserTrackAdapter.sendUT(loginParam.isFromAccount ? ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN2 : ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN1, ApiConstants.UTConstants.UT_SEND_SMS_RESULT, str, (String) null, properties);
    }

    public RpcResponse smsLogin(LoginParam loginParam) {
        Map map;
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_SMS_LOGIN;
        rpcRequest.VERSION = "1.0";
        SMSLoginRequest sMSLoginRequest = new SMSLoginRequest();
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
        if (loginParam.enableVoiceSMS) {
            map.put("checkAudio", "true");
        }
        sMSLoginRequest.loginType = loginParam.loginType;
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(map));
        rpcRequest.requestSite = loginParam.loginSite;
        sMSLoginRequest.site = loginParam.loginSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        sMSLoginRequest.locale = locale;
        sMSLoginRequest.loginId = loginParam.loginAccount;
        sMSLoginRequest.countryCode = loginParam.countryCode;
        sMSLoginRequest.phoneCode = loginParam.phoneCode;
        sMSLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        sMSLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        sMSLoginRequest.smsCode = loginParam.smsCode;
        sMSLoginRequest.smsSid = loginParam.smsSid;
        sMSLoginRequest.slideCheckcodeSid = loginParam.slideCheckcodeSid;
        sMSLoginRequest.slideCheckcodeSig = loginParam.slideCheckcodeSig;
        sMSLoginRequest.slideCheckcodeToken = loginParam.slideCheckcodeToken;
        sMSLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        sMSLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        sMSLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        sMSLoginRequest.t = System.currentTimeMillis();
        if (!TextUtils.isEmpty(loginParam.deviceTokenKey)) {
            sMSLoginRequest.deviceTokenKey = loginParam.deviceTokenKey;
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
            deviceTokenSignParam.addHavanaId(String.valueOf(loginParam.havanaId));
            deviceTokenSignParam.addTimestamp(String.valueOf(sMSLoginRequest.t));
            deviceTokenSignParam.addSDKVersion(sMSLoginRequest.sdkVersion);
            sMSLoginRequest.deviceTokenSign = AlibabaSecurityTokenService.sign(sMSLoginRequest.deviceTokenKey, deviceTokenSignParam.build());
            if (Debuggable.isDebug()) {
                TLogAdapter.d("login.RegisterComponent", "mtop key=" + sMSLoginRequest.deviceTokenKey);
                TLogAdapter.d("login.RegisterComponent", "mtop sign=" + sMSLoginRequest.deviceTokenSign);
            }
            sMSLoginRequest.hid = loginParam.havanaId + "";
            sMSLoginRequest.alipayHid = loginParam.alipayHid;
        }
        rpcRequest.addParam(ApiConstants.ApiField.LOGIN_INFO, JSON.toJSONString(sMSLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        RpcResponse post = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(loginParam.havanaId));
        if (!(post == null || post.returnValue == null)) {
            ((LoginReturnData) post.returnValue).loginType = loginParam.loginType;
        }
        smsLoginUT(loginParam, post);
        return post;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        com.ut.mini.UTAnalytics.getInstance().updateUserAccount(r0.nick, r0.userId);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x004f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void smsLoginUT(com.ali.user.mobile.model.LoginParam r7, com.ali.user.mobile.rpc.RpcResponse r8) {
        /*
            r6 = this;
            boolean r0 = r7.isFromAccount     // Catch:{ Exception -> 0x0113 }
            if (r0 == 0) goto L_0x0007
            java.lang.String r0 = "Page_SMSLogin2"
            goto L_0x0009
        L_0x0007:
            java.lang.String r0 = "Page_SMSLogin1"
        L_0x0009:
            if (r8 == 0) goto L_0x010f
            java.lang.String r1 = r8.actionType     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x010f
            java.lang.String r1 = "SUCCESS"
            java.lang.String r2 = r8.actionType     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r1.equals(r2)     // Catch:{ Exception -> 0x0113 }
            r2 = 0
            if (r1 == 0) goto L_0x00b1
            T r0 = r8.returnValue     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.rpc.login.model.LoginReturnData r0 = (com.ali.user.mobile.rpc.login.model.LoginReturnData) r0     // Catch:{ Exception -> 0x0113 }
            if (r0 == 0) goto L_0x005a
            java.lang.String r0 = r0.data     // Catch:{ Exception -> 0x0113 }
            java.lang.Class<com.ali.user.mobile.rpc.login.model.AliUserResponseData> r1 = com.ali.user.mobile.rpc.login.model.AliUserResponseData.class
            java.lang.Object r0 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r0, r1)     // Catch:{ Exception -> 0x0113 }
            com.ali.user.mobile.rpc.login.model.AliUserResponseData r0 = (com.ali.user.mobile.rpc.login.model.AliUserResponseData) r0     // Catch:{ Exception -> 0x0113 }
            if (r0 == 0) goto L_0x005a
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Throwable -> 0x004f }
            java.lang.String r3 = r0.nick     // Catch:{ Throwable -> 0x004f }
            java.lang.String r4 = r0.userId     // Catch:{ Throwable -> 0x004f }
            java.lang.String r5 = r0.uidDigest     // Catch:{ Throwable -> 0x004f }
            r1.updateUserAccount(r3, r4, r5)     // Catch:{ Throwable -> 0x004f }
            java.lang.String r1 = r0.userId     // Catch:{ Throwable -> 0x004f }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x004f }
            if (r1 != 0) goto L_0x005a
            java.lang.String r1 = r0.uidDigest     // Catch:{ Throwable -> 0x004f }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x004f }
            if (r1 == 0) goto L_0x005a
            java.lang.String r1 = "UT_OPENID_IS_NULL"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT((java.lang.String) r1)     // Catch:{ Throwable -> 0x004f }
            goto L_0x005a
        L_0x004f:
            com.ut.mini.UTAnalytics r1 = com.ut.mini.UTAnalytics.getInstance()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = r0.nick     // Catch:{ Exception -> 0x0113 }
            java.lang.String r0 = r0.userId     // Catch:{ Exception -> 0x0113 }
            r1.updateUserAccount(r3, r0)     // Catch:{ Exception -> 0x0113 }
        L_0x005a:
            java.util.Properties r0 = new java.util.Properties     // Catch:{ Exception -> 0x0113 }
            r0.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "is_success"
            java.lang.String r3 = "T"
            r0.setProperty(r1, r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "type"
            java.lang.String r3 = "SMSLoginSuccess"
            r0.setProperty(r1, r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = r7.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0113 }
            if (r1 != 0) goto L_0x0098
            java.lang.String r1 = "Page_Login5-Reg"
            java.lang.String r3 = r7.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.equals(r1, r3)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x0087
            java.lang.String r1 = "source"
            java.lang.String r3 = "Page_Login5-RegistSuc"
            r0.setProperty(r1, r3)     // Catch:{ Exception -> 0x0113 }
            goto L_0x0098
        L_0x0087:
            java.lang.String r1 = "Page_Login5-Login"
            java.lang.String r3 = r7.source     // Catch:{ Exception -> 0x0113 }
            boolean r1 = android.text.TextUtils.equals(r1, r3)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x0098
            java.lang.String r1 = "source"
            java.lang.String r3 = "Page_Login5-LoginSuc"
            r0.setProperty(r1, r3)     // Catch:{ Exception -> 0x0113 }
        L_0x0098:
            boolean r1 = r7.isFromAccount     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x00a7
            java.lang.String r1 = "Page_SMSLogin2"
            java.lang.String r3 = "LoginResult"
            java.lang.String r4 = "LoginType_Mobile"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r3, r2, r4, r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x00a7:
            java.lang.String r1 = "Page_SMSLogin1"
            java.lang.String r3 = "LoginResult"
            java.lang.String r4 = "LoginType_Mobile"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r3, r2, r4, r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x00b1:
            java.lang.String r1 = "H5"
            java.lang.String r3 = r8.actionType     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r1.equals(r3)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x00f2
            java.util.Properties r0 = new java.util.Properties     // Catch:{ Exception -> 0x0113 }
            r0.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "is_success"
            java.lang.String r2 = "F"
            r0.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r1 = "type"
            java.lang.String r2 = "SMSLoginH5"
            r0.setProperty(r1, r2)     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r7.isFromAccount     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x00e2
            java.lang.String r1 = "Page_SMSLogin2"
            java.lang.String r2 = "LoginResult"
            int r3 = r8.code     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = "LoginType_Mobile"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r2, r3, r4, r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x00e2:
            java.lang.String r1 = "Page_SMSLogin1"
            java.lang.String r2 = "LoginResult"
            int r3 = r8.code     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = "LoginType_Mobile"
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r1, r2, r3, r4, r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x00f2:
            java.lang.String r1 = "REGISTER"
            java.lang.String r3 = r8.actionType     // Catch:{ Exception -> 0x0113 }
            boolean r1 = r1.equals(r3)     // Catch:{ Exception -> 0x0113 }
            if (r1 == 0) goto L_0x010b
            r1 = 14054(0x36e6, float:1.9694E-41)
            int r3 = r8.code     // Catch:{ Exception -> 0x0113 }
            if (r1 != r3) goto L_0x0105
            java.lang.String r1 = "FamilyLoginToReg"
            goto L_0x0107
        L_0x0105:
            java.lang.String r1 = "LoginToReg"
        L_0x0107:
            com.ali.user.mobile.log.UserTrackAdapter.sendUT(r0, r1, r2, r2, r2)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x010b:
            r6.smsLoginFailureUT(r7, r8)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x010f:
            r6.smsLoginFailureUT(r7, r8)     // Catch:{ Exception -> 0x0113 }
            goto L_0x011a
        L_0x0113:
            r0 = move-exception
            r0.printStackTrace()
            r6.smsLoginFailureUT(r7, r8)
        L_0x011a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.data.LoginComponent.smsLoginUT(com.ali.user.mobile.model.LoginParam, com.ali.user.mobile.rpc.RpcResponse):void");
    }

    private void smsLoginFailureUT(LoginParam loginParam, RpcResponse rpcResponse) {
        Properties properties = new Properties();
        properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
        properties.setProperty("type", ApiConstants.UTConstants.UT_TYPE_SMS_FAILURE);
        String str = ApiConstants.UTConstants.UT_NETWORK_FAIL;
        if (rpcResponse != null) {
            str = String.valueOf(rpcResponse.code);
        }
        UserTrackAdapter.sendUT(loginParam.isFromAccount ? ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN2 : ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN1, ApiConstants.UTConstants.UT_LOGIN_RESULT, str, ApiConstants.UTConstants.UT_LOGIN_TYPE_PHONE, properties);
    }

    public RpcResponse getCountryList() {
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        return getCountryList(locale);
    }

    public RpcResponse getCountryList(String str) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_LOGIN_COUNTRY;
        rpcRequest.VERSION = "1.0";
        LoginRequestBase loginRequestBase = new LoginRequestBase();
        HashMap hashMap = new HashMap();
        hashMap.put("apiVersion", "2.0");
        try {
            hashMap.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        loginRequestBase.locale = str;
        loginRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        loginRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        loginRequestBase.site = DataProviderFactory.getDataProvider().getSite();
        loginRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        loginRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        loginRequestBase.utdid = AppInfo.getInstance().getUtdid();
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(loginRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopRegisterInitcontextResponseData.class);
    }
}
