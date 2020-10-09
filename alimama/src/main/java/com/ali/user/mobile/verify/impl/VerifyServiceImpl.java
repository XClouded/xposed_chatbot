package com.ali.user.mobile.verify.impl;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.model.DeviceTokenSignParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.security.AlibabaSecurityTokenService;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.verify.model.GetVerifyUrlResponse;
import com.ali.user.mobile.verify.model.VerifyParam;
import com.ali.user.mobile.verify.model.VerifyTokenConsumedResponse;
import java.util.Locale;

public class VerifyServiceImpl {
    private static VerifyServiceImpl instance;

    public static VerifyServiceImpl getInstance() {
        if (instance == null) {
            instance = new VerifyServiceImpl();
        }
        return instance;
    }

    private VerifyServiceImpl() {
    }

    public GetVerifyUrlResponse getNonLoginVerfiyUrl(VerifyParam verifyParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.IV_NONLOGIN_VERFIY;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam(ApiConstants.ApiField.MEMBER_ID, verifyParam.userId);
        rpcRequest.addParam("actionType", verifyParam.actionType);
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(verifyParam.fromSite));
        rpcRequest.requestSite = verifyParam.fromSite;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        return (GetVerifyUrlResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, GetVerifyUrlResponse.class);
    }

    public VerifyTokenConsumedResponse goNonLoginConsume(VerifyParam verifyParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.IV_NONLOGIN_CONSUME_IVTOKEN;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam(ApiConstants.ApiField.MEMBER_ID, verifyParam.userId);
        rpcRequest.addParam(ApiConstants.ApiField.IV_TOKEN, verifyParam.ivToken);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(verifyParam.fromSite));
        rpcRequest.requestSite = verifyParam.fromSite;
        rpcRequest.addParam("actionType", verifyParam.actionType);
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        return (VerifyTokenConsumedResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, VerifyTokenConsumedResponse.class);
    }

    public void getIdentityVerificationUrl(VerifyParam verifyParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.IV_VERFIY_URL;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_ECODE = true;
        rpcRequest.NEED_SESSION = true;
        rpcRequest.addParam(ApiConstants.ApiField.MEMBER_ID, verifyParam.userId);
        rpcRequest.addParam("actionType", verifyParam.actionType);
        rpcRequest.addParam(ApiConstants.ApiField.FROM_SITE, Integer.valueOf(verifyParam.fromSite));
        rpcRequest.requestSite = verifyParam.fromSite;
        rpcRequest.addParam("loginId", verifyParam.loginId);
        rpcRequest.addParam("sdkVersion", AppInfo.getInstance().getSdkVersion());
        long currentTimeMillis = System.currentTimeMillis();
        rpcRequest.addParam("t", Long.valueOf(currentTimeMillis));
        if (!TextUtils.isEmpty(verifyParam.deviceTokenKey)) {
            rpcRequest.addParam(ApiConstants.ApiField.DEVICE_TOKEN_KEY, verifyParam.deviceTokenKey);
            DeviceTokenSignParam deviceTokenSignParam = new DeviceTokenSignParam();
            deviceTokenSignParam.addActionType(verifyParam.actionType);
            deviceTokenSignParam.addAppKey(DataProviderFactory.getDataProvider().getAppkey());
            deviceTokenSignParam.addSDKVersion(AppInfo.getInstance().getSdkVersion());
            deviceTokenSignParam.addTimestamp(String.valueOf(currentTimeMillis));
            rpcRequest.addParam("deviceTokenSign", AlibabaSecurityTokenService.sign(verifyParam.deviceTokenKey, deviceTokenSignParam.build()));
        }
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        rpcRequest.addParam("locale", locale);
        rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, GetVerifyUrlResponse.class, rpcRequestCallback);
    }
}
