package com.ali.user.mobile.register.service.icbu.impl;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.register.model.OceanRegisterInitcontextResponseData;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.login.model.OceanRegisterMemberRequestBase;
import com.ali.user.mobile.rpc.register.model.OceanRegisterApplySMSResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResponseData;
import com.ali.user.mobile.rpc.safe.RSAKey;
import com.ali.user.mobile.rpc.safe.Rsa;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.StorageService;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;

public class RegisterComponent {
    private static RegisterComponent mRegisterComponent;
    private final String TAG = "login.RegisterComponent";

    private RegisterComponent() {
    }

    public static RegisterComponent getInstance() {
        if (mRegisterComponent == null) {
            synchronized (RegisterComponent.class) {
                if (mRegisterComponent == null) {
                    mRegisterComponent = new RegisterComponent();
                }
            }
        }
        return mRegisterComponent;
    }

    public OceanRegisterInitcontextResponseData countryList(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_REGISTER_INIT;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = 4;
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterInitcontextResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterInitcontextResponseData.class);
    }

    public OceanRegisterResponseData register(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_REGISTER_JOIN;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = 4;
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        oceanRegisterMemberRequestBase.ext = new HashMap();
        oceanRegisterMemberRequestBase.ext.put("ncAppkey", ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0));
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        if (!TextUtils.isEmpty(oceanRegisterParam.password)) {
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (!TextUtils.isEmpty(rsaPubkey)) {
                    String encrypt = Rsa.encrypt(oceanRegisterParam.password, rsaPubkey);
                    oceanRegisterParam.password = encrypt;
                    TLogAdapter.d("login.RegisterComponent", encrypt);
                } else {
                    TLogAdapter.e("login.RegisterComponent", "RSAKey == null");
                    throw new RpcException("getRsaKeyResult is null");
                }
            } catch (RpcException e) {
                throw new RpcException("get RSA exception===> " + e.getMessage());
            }
        }
        rpcRequest.addParam("userInfo", JSON.toJSONString(oceanRegisterParam));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterResponseData.class);
    }

    public OceanRegisterApplySMSResponseData sendSMS(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_SEND_SMS;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = 4;
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        rpcRequest.addParam("applySmsRequest", JSON.toJSON(oceanRegisterParam.toSendOverSeaSMS()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterApplySMSResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterApplySMSResponseData.class);
    }

    public OceanRegisterApplySMSResponseData resendSMS(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_RESEND_SMS;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = 4;
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        rpcRequest.addParam("userInfo", JSON.toJSON(oceanRegisterParam.toSendOverSeaSMS()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterApplySMSResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterApplySMSResponseData.class);
    }

    public OceanRegisterResponseData verifySMS(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_VERIFY_SMS;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = 4;
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        rpcRequest.addParam("userInfo", JSON.toJSONString(oceanRegisterParam.toSendOverSeaSMS()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_CONTROL_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterResponseData.class);
    }
}
