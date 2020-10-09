package com.ali.user.mobile.url.service.impl;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.rpc.h5.MtopCanChangeNickResponseData;
import com.ali.user.mobile.rpc.h5.MtopFoundPasswordResponseData;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.url.model.AccountCenterParam;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;

public class UrlFetchServiceImpl {
    private static UrlFetchServiceImpl instance;

    public static UrlFetchServiceImpl getInstance() {
        if (instance == null) {
            instance = new UrlFetchServiceImpl();
        }
        return instance;
    }

    private UrlFetchServiceImpl() {
    }

    public MtopAccountCenterUrlResponseData foundH5urls(AccountCenterParam accountCenterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_HAVANA_ACCOUNT_URL;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam(ApiConstants.ApiField.APDID, AlipayInfo.getInstance().getApdid());
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("taobao", SecurityGuardManagerWraper.getWUA());
            rpcRequest.addParam(ApiConstants.ApiField.RDS_INFO, JSON.toJSONString(hashMap));
        } catch (Exception unused) {
        }
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.requestSite = accountCenterParam.fromSite;
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        rpcRequest.addParam("version", "android:new");
        rpcRequest.addParam(ApiConstants.ApiField.TRUST_LOGIN, "true");
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        if (!TextUtils.isEmpty(accountCenterParam.userInputName)) {
            rpcRequest.addParam(ApiConstants.ApiField.USER_INPUT_NAME, accountCenterParam.userInputName);
        }
        return (MtopAccountCenterUrlResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopAccountCenterUrlResponseData.class);
    }

    public MtopAccountCenterUrlResponseData foundHavanaUrls(AccountCenterParam accountCenterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_HAVANA_ACCOUNT_URL;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam(ApiConstants.ApiField.APDID, AlipayInfo.getInstance().getApdid());
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("taobao", SecurityGuardManagerWraper.getWUA());
            rpcRequest.addParam(ApiConstants.ApiField.RDS_INFO, JSON.toJSON(hashMap));
        } catch (Exception unused) {
        }
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.requestSite = accountCenterParam.fromSite;
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        rpcRequest.addParam("version", "android:new");
        rpcRequest.addParam(ApiConstants.ApiField.TRUST_LOGIN, "true");
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam("sdkVersion", AppInfo.getInstance().getSdkVersion());
        rpcRequest.addParam("appVersion", AppInfo.getInstance().getAndroidAppVersion());
        if (!TextUtils.isEmpty(accountCenterParam.havanaId)) {
            rpcRequest.addParam("havanaId", accountCenterParam.havanaId);
        }
        if (!TextUtils.isEmpty(accountCenterParam.userInputName)) {
            rpcRequest.addParam(ApiConstants.ApiField.USER_INPUT_NAME, accountCenterParam.userInputName);
        }
        return (MtopAccountCenterUrlResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopAccountCenterUrlResponseData.class);
    }

    public MtopFoundPasswordResponseData foundPassword(AccountCenterParam accountCenterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.requestSite = accountCenterParam.fromSite;
        rpcRequest.API_NAME = ApiConstants.ApiName.GENERATE_URL;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam("appVersion", AppInfo.getInstance().getAndroidAppVersion());
        rpcRequest.addParam("sdkVersion", AppInfo.getInstance().getSdkVersion());
        rpcRequest.addParam(ApiConstants.ApiField.DEVICE_TOKEN_KEY, accountCenterParam.deviceTokenKey);
        String valueOf = String.valueOf(accountCenterParam.havanaId);
        rpcRequest.addParam(ApiConstants.ApiField.HID, valueOf);
        String valueOf2 = String.valueOf(System.currentTimeMillis());
        rpcRequest.addParam("timestamp", valueOf2);
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        HashMap hashMap = new HashMap();
        hashMap.put("taobao", SecurityGuardManagerWraper.getWUA());
        rpcRequest.addParam(ApiConstants.ApiField.WIRELESS_ENVM, JSON.toJSONString(hashMap));
        DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
        if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
        }
        deviceTokenSignParam.addAppVersion(AppInfo.getInstance().getAndroidAppVersion());
        deviceTokenSignParam.addHavanaId(valueOf);
        deviceTokenSignParam.addTimestamp(valueOf2);
        deviceTokenSignParam.addSDKVersion(AppInfo.getInstance().getSdkVersion());
        if (!TextUtils.isEmpty(accountCenterParam.deviceTokenKey)) {
            rpcRequest.addParam(ApiConstants.ApiField.LOGIN_SIGN, AlibabaSecurityTokenService.sign(accountCenterParam.deviceTokenKey, deviceTokenSignParam.build()));
        }
        return (MtopFoundPasswordResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopFoundPasswordResponseData.class, accountCenterParam.havanaId);
    }

    public MtopAccountCenterUrlResponseData navByScene(AccountCenterParam accountCenterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_URL_WITH_SESSION;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam(ApiConstants.ApiField.APDID, AlipayInfo.getInstance().getApdid());
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("taobao", SecurityGuardManagerWraper.getWUA());
            rpcRequest.addParam(ApiConstants.ApiField.RDS_INFO, hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        rpcRequest.addParam("appVersion", AppInfo.getInstance().getAppVersion());
        rpcRequest.addParam("sdkVersion", AppInfo.getInstance().getSdkVersion());
        rpcRequest.addParam(ApiConstants.ApiField.TRUST_LOGIN, "true");
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        return (MtopAccountCenterUrlResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopAccountCenterUrlResponseData.class);
    }

    public void navBySceneRemote(AccountCenterParam accountCenterParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_URL_WITH_SESSION;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam(ApiConstants.ApiField.APDID, AlipayInfo.getInstance().getApdid());
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("taobao", SecurityGuardManagerWraper.getWUA());
            rpcRequest.addParam(ApiConstants.ApiField.RDS_INFO, hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        rpcRequest.addParam("appVersion", AppInfo.getInstance().getAppVersion());
        rpcRequest.addParam("sdkVersion", AppInfo.getInstance().getSdkVersion());
        rpcRequest.addParam(ApiConstants.ApiField.TRUST_LOGIN, "true");
        rpcRequest.addParam("appKey", DataProviderFactory.getDataProvider().getAppkey());
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, MtopAccountCenterUrlResponseData.class, rpcRequestCallback);
    }

    public void checkNickModifiable(RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.CAN_CHANGE_NICK;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, MtopCanChangeNickResponseData.class, rpcRequestCallback);
    }

    public void sendSMSCode(AccountCenterParam accountCenterParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.MEMBER_CENTER_SEND_SMS_CODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.addParam("countryCode", accountCenterParam.countryCode);
        rpcRequest.addParam(ApiConstants.ApiField.MOBILE, accountCenterParam.userInputName);
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, MtopAccountCenterUrlResponseData.class, rpcRequestCallback);
    }

    public void validateSMSCode(AccountCenterParam accountCenterParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.MEMBER_CENTER_VALIDATE_SMS;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        rpcRequest.addParam("scene", accountCenterParam.scene);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(accountCenterParam.fromSite));
        rpcRequest.addParam("mobileCode", accountCenterParam.mobileCode);
        rpcRequest.addParam("countryCode", accountCenterParam.countryCode);
        rpcRequest.addParam(ApiConstants.ApiField.MOBILE, accountCenterParam.userInputName);
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, MtopAccountCenterUrlResponseData.class, rpcRequestCallback);
    }
}
