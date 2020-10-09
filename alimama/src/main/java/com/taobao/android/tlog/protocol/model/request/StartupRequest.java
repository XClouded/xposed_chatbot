package com.taobao.android.tlog.protocol.model.request;

import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.request.base.LogRequestBase;
import com.taobao.android.tlog.protocol.utils.RandomIdUtils;
import com.taobao.weex.devtools.debug.WXDebugConstants;

public class StartupRequest extends LogRequestBase {
    private String TAG = "TLOG.Protocol.StartupRequest";
    public String appVersion;
    public String brand;
    public Long clientTime;
    public String deviceModel;
    public String geo;
    public String ip;
    public String osPlatform;
    public String osVersion;
    private String requestType = "REQUEST";

    public RequestResult build() throws Exception {
        String randomId = RandomIdUtils.getRandomId();
        String randomId2 = RandomIdUtils.getRandomId();
        String randomId3 = RandomIdUtils.getRandomId();
        JSONObject buildRequestHeader = BuilderHelper.buildRequestHeader(this, randomId, randomId2);
        JSONObject jSONObject = new JSONObject();
        if (this.appVersion != null) {
            jSONObject.put("appVersion", (Object) this.appVersion);
        }
        if (this.deviceModel != null) {
            jSONObject.put(WXDebugConstants.ENV_DEVICE_MODEL, (Object) this.deviceModel);
        }
        if (this.user != null) {
            jSONObject.put("userNick", (Object) this.user);
        }
        if (this.osPlatform != null) {
            jSONObject.put("osPlatform", (Object) this.osPlatform);
        }
        if (this.osVersion != null) {
            jSONObject.put(WXDebugConstants.ENV_OS_VERSION, (Object) this.osVersion);
        }
        if (this.geo != null) {
            jSONObject.put("geo", (Object) this.geo);
        }
        if (this.clientTime != null) {
            jSONObject.put("clientTime", (Object) this.clientTime);
        }
        if (this.brand != null) {
            jSONObject.put("brand", (Object) this.brand);
        }
        if (this.ip != null) {
            jSONObject.put("ip", (Object) this.ip);
        }
        if (this.tokenType != null) {
            jSONObject.put(WebConstant.WEB_LOGIN_TOKEN_TYPE, (Object) this.tokenType);
        }
        if (this.tokenInfo != null) {
            jSONObject.put(ApiConstants.ApiField.TOKEN_INFO, (Object) this.tokenInfo);
        }
        return BuilderHelper.buildRequestResult(jSONObject, buildRequestHeader, this.requestType, randomId, randomId2, randomId3);
    }
}
