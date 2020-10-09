package com.ali.user.mobile.scan.impl;

import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.login.model.TokenLoginRequest;
import com.ali.user.mobile.scan.ScanService;
import com.ali.user.mobile.scan.model.CommonScanParam;
import com.ali.user.mobile.scan.model.CommonScanResponse;
import com.ali.user.mobile.scan.model.ScanParam;
import com.ali.user.mobile.scan.model.ScanResponse;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;

public class ScanServiceImpl implements ScanService {
    private static ScanServiceImpl instance;

    public static ScanServiceImpl getInstance() {
        if (instance == null) {
            instance = new ScanServiceImpl();
        }
        return instance;
    }

    public ScanResponse cancel(ScanParam scanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.requestSite = scanParam.currentSite;
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_CANCEL_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("appName", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam("havanaId", scanParam.havanaId);
        rpcRequest.addParam("key", scanParam.key);
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("locale", locale);
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        return (ScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, ScanResponse.class);
    }

    public ScanResponse confirm(ScanParam scanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.requestSite = scanParam.currentSite;
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_CONFIRM_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("appName", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam("havanaId", scanParam.havanaId);
        rpcRequest.addParam("key", scanParam.key);
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("locale", locale);
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        return (ScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, ScanResponse.class);
    }

    public ScanResponse scan(ScanParam scanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_SCAN_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = scanParam.currentSite;
        rpcRequest.addParam("appName", DataProviderFactory.getDataProvider().getAppkey());
        rpcRequest.addParam("havanaId", scanParam.havanaId);
        rpcRequest.addParam("key", scanParam.key);
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("locale", locale);
        rpcRequest.addParam(ApiConstants.ApiField.EXT, JSON.toJSONString(hashMap));
        return (ScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, ScanResponse.class);
    }

    public CommonScanResponse commonScan(CommonScanParam commonScanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.COMMON_SCAN_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = commonScanParam.currentSite;
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.token = commonScanParam.key;
        tokenLoginRequest.hid = commonScanParam.havanaId;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        tokenLoginRequest.locale = locale;
        tokenLoginRequest.sid = commonScanParam.sid;
        tokenLoginRequest.appName = commonScanParam.appName;
        tokenLoginRequest.site = commonScanParam.currentSite;
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (CommonScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, CommonScanResponse.class);
    }

    public CommonScanResponse commonConfirm(CommonScanParam commonScanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.COMMON_CONFIRM_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = commonScanParam.currentSite;
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.token = commonScanParam.key;
        tokenLoginRequest.hid = commonScanParam.havanaId;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        tokenLoginRequest.locale = locale;
        tokenLoginRequest.sid = commonScanParam.sid;
        tokenLoginRequest.appName = commonScanParam.appName;
        tokenLoginRequest.site = commonScanParam.currentSite;
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (CommonScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, CommonScanResponse.class);
    }

    public CommonScanResponse commonCancel(CommonScanParam commonScanParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.COMMON_CANCEL_QRCODE;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = commonScanParam.currentSite;
        TokenLoginRequest tokenLoginRequest = new TokenLoginRequest();
        tokenLoginRequest.token = commonScanParam.key;
        tokenLoginRequest.hid = commonScanParam.havanaId;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        tokenLoginRequest.locale = locale;
        tokenLoginRequest.sid = commonScanParam.sid;
        tokenLoginRequest.appName = commonScanParam.appName;
        tokenLoginRequest.site = commonScanParam.currentSite;
        tokenLoginRequest.sdkVersion = AppInfo.getInstance().getSdkVersion();
        tokenLoginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
        tokenLoginRequest.utdid = AppInfo.getInstance().getUtdid();
        tokenLoginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        rpcRequest.addParam(ApiConstants.ApiField.TOKEN_INFO, JSON.toJSONString(tokenLoginRequest));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (CommonScanResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, CommonScanResponse.class);
    }
}
