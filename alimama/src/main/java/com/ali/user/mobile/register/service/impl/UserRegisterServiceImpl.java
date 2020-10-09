package com.ali.user.mobile.register.service.impl;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.register.service.UserRegisterService;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.login.model.MemberRequestBase;
import com.ali.user.mobile.rpc.login.model.WSecurityData;
import com.ali.user.mobile.rpc.login.model.WUAData;
import com.ali.user.mobile.rpc.register.model.MtopMobilePreCheckRequest;
import com.ali.user.mobile.rpc.register.model.MtopRegisterCheckMobileResponseData;
import com.ali.user.mobile.rpc.register.model.MtopRegisterH5ResponseData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;

public class UserRegisterServiceImpl implements UserRegisterService {
    private static UserRegisterService instance;
    private final String TAG = "login.UserRegisterServiceImpl";

    public static UserRegisterService getInstance() {
        if (instance == null) {
            instance = new UserRegisterServiceImpl();
        }
        return instance;
    }

    private UserRegisterServiceImpl() {
    }

    public MtopRegisterInitcontextResponseData countryCodeRes(BaseRegistRequest baseRegistRequest) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.REGISTER_INIT;
        rpcRequest.VERSION = "1.0";
        MemberRequestBase memberRequestBase = new MemberRequestBase();
        memberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        memberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        memberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        memberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        memberRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        memberRequestBase.site = baseRegistRequest.registSite;
        rpcRequest.requestSite = baseRegistRequest.registSite;
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
        if (memberRequestBase.ext == null) {
            memberRequestBase.ext = new HashMap();
            if (!TextUtils.isEmpty(baseRegistRequest.regFrom)) {
                memberRequestBase.ext.put("regFrom", baseRegistRequest.regFrom);
            }
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(memberRequestBase.ext));
        return (MtopRegisterInitcontextResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopRegisterInitcontextResponseData.class);
    }

    public MtopRegisterCheckMobileResponseData verifyMobileAndCaptcha(BaseRegistRequest baseRegistRequest) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.REGISTER_CAPTCHA_CHECKCODE;
        rpcRequest.VERSION = "1.0";
        MtopMobilePreCheckRequest mtopMobilePreCheckRequest = new MtopMobilePreCheckRequest();
        mtopMobilePreCheckRequest.appName = DataProviderFactory.getDataProvider().getAppkey();
        mtopMobilePreCheckRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        mtopMobilePreCheckRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        mtopMobilePreCheckRequest.utdid = AppInfo.getInstance().getUtdid();
        mtopMobilePreCheckRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        mtopMobilePreCheckRequest.site = baseRegistRequest.registSite;
        rpcRequest.requestSite = baseRegistRequest.registSite;
        mtopMobilePreCheckRequest.countryCode = baseRegistRequest.countryCode;
        mtopMobilePreCheckRequest.mobileNum = baseRegistRequest.mobileNo;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        mtopMobilePreCheckRequest.locale = locale;
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(mtopMobilePreCheckRequest));
        rpcRequest.addParam("sessionId", baseRegistRequest.sessionId);
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(baseRegistRequest.regFrom)) {
            hashMap.put("regFrom", baseRegistRequest.regFrom);
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(hashMap));
        return (MtopRegisterCheckMobileResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopRegisterCheckMobileResponseData.class);
    }

    public MtopRegisterH5ResponseData getRegisterH5Url(BaseRegistRequest baseRegistRequest) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.REGISTER_QUERY_REGISTER_LINK;
        rpcRequest.VERSION = "1.0";
        MemberRequestBase memberRequestBase = new MemberRequestBase();
        memberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        memberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        memberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        memberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        memberRequestBase.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        memberRequestBase.site = baseRegistRequest.registSite;
        rpcRequest.requestSite = baseRegistRequest.registSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        memberRequestBase.locale = locale;
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(baseRegistRequest.regFrom)) {
            hashMap.put("regFrom", baseRegistRequest.regFrom);
        }
        memberRequestBase.ext = hashMap;
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(memberRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(hashMap));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (MtopRegisterH5ResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopRegisterH5ResponseData.class);
    }
}
