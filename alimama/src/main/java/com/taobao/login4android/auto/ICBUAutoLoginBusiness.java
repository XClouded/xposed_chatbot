package com.taobao.login4android.auto;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
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
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.login4android.biz.autologin.AutoLoginBusiness;
import com.taobao.login4android.session.SessionManager;
import com.taobao.statistic.TBS;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

public class ICBUAutoLoginBusiness {
    public static final String TAG = "login.ICBUAutoLoginBusiness";

    public RpcResponse<LoginReturnData> autoLogin(String str, String str2, boolean z, String str3) {
        return autoLogin(str, str2, z, false, str3);
    }

    public RpcResponse<LoginReturnData> autoLogin(String str, String str2, boolean z, boolean z2, String str3) {
        RpcResponse<LoginReturnData> rpcResponse;
        String str4;
        HistoryAccount findHistoryAccount;
        if (str != null) {
            boolean isEmpty = str.isEmpty();
        }
        try {
            TBS.Ext.commitEventBegin("Event_AutoLoginCost", (Properties) null);
            RpcRequest rpcRequest = new RpcRequest();
            HashMap hashMap = new HashMap();
            if (DataProviderFactory.getDataProvider().getSite() == 17) {
                rpcRequest.API_NAME = ApiConstants.ApiName.GUC_AUTO_LOGIN;
                rpcRequest.VERSION = "1.0";
                hashMap.put(ApiConstants.ApiField.MTOP_APPKEY, ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(EnvUtil.getAlimmsdk_env()));
            } else {
                rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_ICBU_AUTO_LOGIN;
                rpcRequest.VERSION = "1.0";
            }
            rpcRequest.NEED_SESSION = true;
            rpcRequest.addParam("userId", Long.valueOf(Long.parseLong(str2)));
            TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
            tokenLoginRequest.token = str;
            tokenLoginRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
            tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.site = DataProviderFactory.getDataProvider().getSite();
            rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
            tokenLoginRequest.t = System.currentTimeMillis();
            String locale = Locale.SIMPLIFIED_CHINESE.toString();
            if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
                locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
            }
            tokenLoginRequest.locale = locale;
            tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
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
            if (!TextUtils.isEmpty(str3)) {
                hashMap.put("apiRefer", str3);
            }
            hashMap.put(ApiConstants.ApiField.OCEAN_APPKEY, DataProviderFactory.getDataProvider().getOceanAppkey());
            rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
            rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
            rpcResponse = ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, DefaultLoginResponseData.class, String.valueOf(str2));
        } catch (RpcException e) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = e.getCode();
            rpcResponse.message = e.getMsg();
        } catch (Exception e2) {
            rpcResponse = new RpcResponse<>();
            rpcResponse.code = 406;
            TLogAdapter.e(TAG, "MtopResponse error", e2);
            e2.printStackTrace();
        }
        try {
            Properties properties = new Properties();
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            UserTrackAdapter.sendUT("Event_IcbuAutoLoginCost", properties);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (rpcResponse == null || !"SUCCESS".equals(rpcResponse.actionType)) {
            try {
                Properties properties2 = new Properties();
                properties2.setProperty("autologintoken", str);
                properties2.setProperty("type", "AutoLoginFailure");
                properties2.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                String valueOf = String.valueOf(406);
                if (rpcResponse != null) {
                    valueOf = String.valueOf(rpcResponse.code);
                }
                UserTrackAdapter.sendUT("Page_Extend", "Event_OldTokenLoginFail", valueOf, properties2);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            if (rpcResponse == null) {
                str4 = "";
            } else {
                str4 = rpcResponse.code + "";
            }
            AppMonitor.Alarm.commitFail("Page_AutoLogin", LoginConstant.LOGIN_TYPE_AUTOLOGIN, str4, rpcResponse == null ? "" : rpcResponse.message);
            SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
            if (rpcResponse != null && !RpcException.isSystemError(rpcResponse.code)) {
                TLogAdapter.e(TAG, "clear SessionInfoin auto login fail");
                instance.clearSessionInfo();
                instance.clearAutoLoginInfo();
            }
            AutoLoginBusiness.appendRefer(rpcResponse, instance);
        } else {
            AppMonitor.Alarm.commitSuccess("Page_AutoLogin", LoginConstant.LOGIN_TYPE_AUTOLOGIN);
            Properties properties3 = new Properties();
            properties3.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
            properties3.setProperty("type", "IcbuAutoLoginSuccess");
            UserTrackAdapter.sendUT("Page_Extend", ApiConstants.UTConstants.UT_LOGIN_RESULT, properties3);
            UserTrackAdapter.sendUT("Event_AutoLoginSuccess");
        }
        return rpcResponse;
    }
}
