package com.ali.user.mobile.data;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.data.model.RegisterUserInfo;
import com.ali.user.mobile.data.model.SmsApplyResponse;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.model.AliValidRequest;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.login.model.OceanRegisterMemberRequestBase;
import com.ali.user.mobile.rpc.register.model.NumAuthFastRegisterResponseData;
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

    public OceanRegisterResponseData register(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_REGISTER;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.addParam("clientInfo", JSON.toJSONString(getOceanRegisterMemberRequestBase()));
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
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(oceanRegisterParam.toInfo()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(oceanRegisterParam.voice)) {
            hashMap.put("checkAudio", oceanRegisterParam.voice);
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(hashMap));
        return (OceanRegisterResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterResponseData.class);
    }

    public OceanRegisterResponseData directRegister(String str) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_DIRECT_REGISTER;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.addParam("clientInfo", JSON.toJSONString(getOceanRegisterMemberRequestBase()));
        rpcRequest.addParam("token", str);
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(new HashMap()));
        return (OceanRegisterResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterResponseData.class);
    }

    public SmsApplyResponse sendSMS(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_SEND_SMS;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        rpcRequest.addParam("clientInfo", JSON.toJSONString(getOceanRegisterMemberRequestBase()));
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSON(oceanRegisterParam.toSendOverSeaSMS()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(oceanRegisterParam.voice)) {
            hashMap.put("sendAudio", oceanRegisterParam.voice);
        }
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(hashMap));
        return (SmsApplyResponse) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, SmsApplyResponse.class);
    }

    public OceanRegisterResponseData verify(OceanRegisterParam oceanRegisterParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_CAPTCHA_CHECK;
        rpcRequest.VERSION = "1.0";
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        getOceanRegisterMemberRequestBase();
        HashMap hashMap = new HashMap();
        hashMap.put("ncAppkey", ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0));
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSON(oceanRegisterParam.toInfo()));
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSON(hashMap));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        return (OceanRegisterResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, OceanRegisterResponseData.class);
    }

    @NonNull
    private OceanRegisterMemberRequestBase getOceanRegisterMemberRequestBase() {
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = new OceanRegisterMemberRequestBase();
        oceanRegisterMemberRequestBase.appName = DataProviderFactory.getDataProvider().getAppkey();
        oceanRegisterMemberRequestBase.sdkVersion = AppInfo.getInstance().getSdkVersion();
        oceanRegisterMemberRequestBase.ttid = DataProviderFactory.getDataProvider().getTTID();
        oceanRegisterMemberRequestBase.utdid = AppInfo.getInstance().getUtdid();
        oceanRegisterMemberRequestBase.site = DataProviderFactory.getDataProvider().getSite();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (DataProviderFactory.getDataProvider().getCurrentLanguage() != null) {
            locale = DataProviderFactory.getDataProvider().getCurrentLanguage().toString();
        }
        oceanRegisterMemberRequestBase.locale = locale;
        return oceanRegisterMemberRequestBase;
    }

    public void canAuthNumRegister(OceanRegisterParam oceanRegisterParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_CHECK_VENDOR_REG;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("clientInfo", JSON.toJSONString(getOceanRegisterMemberRequestBase()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam("registerUserInfo", JSON.toJSON(oceanRegisterParam.toInfo()));
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, NumAuthFastRegisterResponseData.class, rpcRequestCallback);
    }

    public void numAuthRegister(AliValidRequest aliValidRequest, OceanRegisterParam oceanRegisterParam, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_AUTH_NUM_REIGSTER;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("vendorRequest", JSON.toJSONString(aliValidRequest));
        rpcRequest.addParam("clientInfo", JSON.toJSONString(getOceanRegisterMemberRequestBase()));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam("registerUserInfo", JSON.toJSON(oceanRegisterParam.toInfo()));
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, NumAuthFastRegisterResponseData.class, rpcRequestCallback);
    }

    public void fastRegister(String str, RegisterUserInfo registerUserInfo, boolean z, RpcRequestCallback rpcRequestCallback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.API_FAST_REGISTER;
        rpcRequest.VERSION = "1.0";
        OceanRegisterMemberRequestBase oceanRegisterMemberRequestBase = getOceanRegisterMemberRequestBase();
        new HashMap().put("ncAppkey", ((StorageService) ServiceFactory.getService(StorageService.class)).getAppKey(0));
        rpcRequest.addParam("clientInfo", JSON.toJSONString(oceanRegisterMemberRequestBase));
        rpcRequest.addParam(ApiConstants.ApiField.INFO, JSON.toJSONString(registerUserInfo));
        rpcRequest.addParam(ApiConstants.ApiField.RISK_INFO, JSON.toJSONString(SecurityGuardManagerWraper.buildWSecurityData()));
        rpcRequest.addParam("token", str);
        HashMap hashMap = new HashMap();
        hashMap.put("isThirdEmail", z + "");
        rpcRequest.addParam(ApiConstants.ApiField.EXTRA, JSON.toJSONString(hashMap));
        ((RpcService) ServiceFactory.getService(RpcService.class)).remoteBusiness(rpcRequest, OceanRegisterResponseData.class, rpcRequestCallback);
    }
}
