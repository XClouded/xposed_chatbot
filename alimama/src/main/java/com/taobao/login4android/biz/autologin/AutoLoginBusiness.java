package com.taobao.login4android.biz.autologin;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.DefaultLoginResponseData;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.login.model.TokenLoginRequest;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import com.ali.user.mobile.utils.EnvUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.session.SessionManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

public class AutoLoginBusiness {
    public static final String TAG = "loginsdk.AutoLoginBusiness";

    public RpcResponse<LoginReturnData> autoLogin(String str, String str2, int i, boolean z, String str3) {
        return autoLogin(str, str2, i, str3);
    }

    public RpcResponse<LoginReturnData> autoLogin(String str, String str2, int i, String str3) {
        RpcResponse<LoginReturnData> rpcResponse;
        String str4;
        HistoryAccount findHistoryAccount;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        if (str != null) {
            boolean isEmpty = str.isEmpty();
        }
        try {
            RpcRequest rpcRequest = new RpcRequest();
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(str3)) {
                hashMap.put("apiReferer", str3);
            }
            if (i == 17) {
                rpcRequest.API_NAME = ApiConstants.ApiName.GUC_AUTO_LOGIN;
                rpcRequest.VERSION = "1.0";
                hashMap.put(ApiConstants.ApiField.MTOP_APPKEY, ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(EnvUtil.getAlimmsdk_env()));
            } else if (i == 4) {
                rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_AUTO_LOGIN;
                rpcRequest.VERSION = "1.0";
                hashMap.put(ApiConstants.ApiField.OCEAN_APPKEY, DataProviderFactory.getDataProvider().getOceanAppkey());
            } else if (i == 100) {
                rpcRequest.API_NAME = ApiConstants.ApiName.AUTO_LOGIN_COMMON;
                rpcRequest.VERSION = "1.0";
            } else {
                rpcRequest.API_NAME = ApiConstants.ApiName.AUTO_LOGIN;
                rpcRequest.VERSION = "1.0";
            }
            if (i == 13 && SessionManager.getInstance(DataProviderFactory.getApplicationContext()).getExtJson() != null) {
                try {
                    String string = JSON.parseObject(SessionManager.getInstance(DataProviderFactory.getApplicationContext()).getExtJson()).getJSONObject("aeExt").getString("refreshToken");
                    if (!TextUtils.isEmpty(string)) {
                        hashMap.put("refreshToken", string);
                    }
                } catch (Throwable unused) {
                }
            }
            rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
            rpcRequest.NEED_SESSION = true;
            rpcRequest.addParam("userId", Long.valueOf(Long.parseLong(str2)));
            TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
            tokenLoginRequest.token = str;
            tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
            tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.site = i;
            tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
            rpcRequest.requestSite = i;
            String locale = Locale.SIMPLIFIED_CHINESE.toString();
            if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
                locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
            }
            tokenLoginRequest.locale = locale;
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.t = System.currentTimeMillis();
            if (!TextUtils.isEmpty(str2) && (findHistoryAccount = SecurityGuardManagerWraper.findHistoryAccount(Long.parseLong(str2))) != null) {
                tokenLoginRequest.deviceTokenKey = findHistoryAccount.tokenKey;
                tokenLoginRequest.appVersion = AppInfo.getInstance().getAndroidAppVersion();
                DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
                deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
                deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
                deviceTokenSignParam.addHavanaId(str2);
                deviceTokenSignParam.addTimestamp(String.valueOf(tokenLoginRequest.t));
                deviceTokenSignParam.addAutoLoginToken(str);
                deviceTokenSignParam.addSDKVersion(AppInfo.getInstance().getSdkVersion());
                if (!TextUtils.isEmpty(findHistoryAccount.tokenKey)) {
                    tokenLoginRequest.deviceTokenSign = AlibabaSecurityTokenService.sign(tokenLoginRequest.deviceTokenKey, deviceTokenSignParam.build());
                }
            }
            rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
            rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
            rpcResponse = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(str2));
        } catch (RpcException e) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = e.getCode();
            rpcResponse.message = e.getMsg();
        } catch (Exception e2) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = 405;
            TLogAdapter.e(TAG, "MtopResponse error", e2);
            e2.printStackTrace();
        }
        try {
            Properties properties = new Properties();
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            UserTrackAdapter.sendUT("Event_AutoLoginCost", properties);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        if (rpcResponse == null || !"SUCCESS".equals(rpcResponse.actionType)) {
            try {
                Properties properties2 = new Properties();
                properties2.setProperty("autologintoken", str);
                properties2.setProperty("type", "AutoLoginFailure");
                properties2.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                String valueOf = String.valueOf(405);
                if (rpcResponse != null) {
                    valueOf = String.valueOf(rpcResponse.code);
                }
                UserTrackAdapter.sendUT("Page_Extend", "Event_AutoLoginFail", valueOf, properties2);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            if (rpcResponse == null) {
                str4 = "";
            } else {
                str4 = rpcResponse.code + "";
            }
            AppMonitorAdapter.commitFail("Page_Member_Login", "Login_Auto", "0", str4);
            if (rpcResponse != null && !RpcException.isSystemError(rpcResponse.code)) {
                TLogAdapter.e(TAG, "clear SessionInfoin auto login fail");
                if (TextUtils.equals(str2, instance.getUserId())) {
                    instance.clearSessionInfo();
                    instance.clearAutoLoginInfo();
                    TLogAdapter.e(TAG, "call mtop logout");
                    ((RpcService) ServiceFactory.getService(RpcService.class)).logout();
                }
                SecurityGuardManagerWraper.clearAutologinTokenFromFile(str2);
            }
            appendRefer(rpcResponse, instance);
        } else {
            AppMonitorAdapter.commitSuccess("Page_Member_Login", "Login_Auto");
            Properties properties3 = new Properties();
            properties3.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
            properties3.setProperty("type", "AutoLoginSuccess");
            UserTrackAdapter.sendUT("Page_Extend", ApiConstants.UTConstants.UT_LOGIN_RESULT, properties3);
            UserTrackAdapter.sendUT("Event_AutoLoginSuccess");
        }
        return rpcResponse;
    }

    public static void appendRefer(RpcResponse<LoginReturnData> rpcResponse, SessionManager sessionManager) {
        ApiReferer.Refer refer = new ApiReferer.Refer();
        refer.eventName = "autologinFailed";
        if (rpcResponse != null) {
            refer.errorCode = String.valueOf(rpcResponse.code);
        }
        sessionManager.appendEventTrace(JSON.toJSONString(refer));
    }

    public RpcResponse<LoginReturnData> oldLogin(String str, String str2, int i, String str3) {
        RpcResponse<LoginReturnData> rpcResponse;
        String str4;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            RpcRequest rpcRequest = new RpcRequest();
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(str3)) {
                hashMap.put("apiReferer", str3);
            }
            rpcRequest.API_NAME = ApiConstants.ApiName.OLD_TO_NEW_AUTO_LOGIN;
            rpcRequest.VERSION = "1.0";
            rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
            rpcRequest.NEED_SESSION = true;
            TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
            tokenLoginRequest.token = str;
            tokenLoginRequest.tokenType = str2;
            tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
            tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.site = i;
            tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
            rpcRequest.requestSite = i;
            String locale = Locale.SIMPLIFIED_CHINESE.toString();
            if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
                locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
            }
            tokenLoginRequest.locale = locale;
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.t = System.currentTimeMillis();
            rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
            rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
            rpcResponse = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class);
        } catch (RpcException e) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = e.getCode();
            rpcResponse.message = e.getMsg();
            rpcResponse.msgCode = e.getExtCode();
        } catch (Exception e2) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = 405;
            TLogAdapter.e(TAG, "MtopResponse error", e2);
            e2.printStackTrace();
        }
        try {
            Properties properties = new Properties();
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            UserTrackAdapter.sendUT("Event_AutoLoginCost", properties);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        if (rpcResponse == null || !"SUCCESS".equals(rpcResponse.actionType)) {
            try {
                Properties properties2 = new Properties();
                properties2.setProperty("autologintoken", str);
                properties2.setProperty("type", "AutoLoginFailure");
                properties2.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                String valueOf = String.valueOf(405);
                String str5 = "";
                if (rpcResponse != null) {
                    valueOf = String.valueOf(rpcResponse.code);
                    str5 = rpcResponse.msgCode;
                }
                UserTrackAdapter.sendUT("Page_Extend", "Event_AutoLoginFail", valueOf, str5, properties2);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            if (rpcResponse == null) {
                str4 = "";
            } else {
                str4 = rpcResponse.code + "";
            }
            AppMonitorAdapter.commitFail("Page_Member_Login", "Login_Auto", "0", str4);
            if (rpcResponse != null && !RpcException.isSystemError(rpcResponse.code)) {
                TLogAdapter.e(TAG, "clear SessionInfoin auto login fail");
                instance.clearSessionInfo();
                instance.clearAutoLoginInfo();
            }
            appendRefer(rpcResponse, instance);
        } else {
            AppMonitorAdapter.commitSuccess("Page_Member_Login", "Login_Auto");
            Properties properties3 = new Properties();
            properties3.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
            properties3.setProperty("type", "AutoLoginSuccess");
            UserTrackAdapter.sendUT("Page_Extend", ApiConstants.UTConstants.UT_LOGIN_RESULT, properties3);
            UserTrackAdapter.sendUT("Event_AutoLoginSuccess");
        }
        return rpcResponse;
    }
}
