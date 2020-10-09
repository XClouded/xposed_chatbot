package com.taobao.android.tlog.protocol.model.request;

import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.android.tlog.protocol.model.request.base.LogRequestBase;
import com.taobao.android.tlog.protocol.utils.RandomIdUtils;
import java.util.Map;

public class ApplyUploadRequest extends LogRequestBase {
    private String TAG = "TLOG.Protocol.ApplyUploadRequestInfo";
    public String bizCode;
    public String bizType;
    public String debugType;
    public Map<String, Object> extraInfo;
    public FileInfo[] fileInfos;
    private String requestType = "REQUEST";

    public RequestResult build() throws Exception {
        String randomId = RandomIdUtils.getRandomId();
        String randomId2 = RandomIdUtils.getRandomId();
        String randomId3 = RandomIdUtils.getRandomId();
        JSONObject buildRequestHeader = BuilderHelper.buildRequestHeader(this, randomId, randomId2);
        JSONObject jSONObject = new JSONObject();
        if (this.debugType != null) {
            jSONObject.put("debugType", (Object) this.debugType);
        }
        if (this.bizType != null) {
            jSONObject.put("bizType", (Object) this.bizType);
        }
        if (this.bizCode != null) {
            jSONObject.put("bizCode", (Object) this.bizCode);
        }
        if (this.tokenType != null) {
            jSONObject.put(WebConstant.WEB_LOGIN_TOKEN_TYPE, (Object) this.tokenType);
        }
        if (this.tokenInfo != null) {
            jSONObject.put(ApiConstants.ApiField.TOKEN_INFO, (Object) this.tokenInfo);
        }
        if (this.fileInfos != null) {
            jSONObject.put("fileInfos", (Object) BuilderHelper.buildFileInfos(this.fileInfos));
        }
        if (this.extraInfo != null) {
            jSONObject.put("extraInfo", (Object) this.extraInfo);
        }
        return BuilderHelper.buildRequestResult(jSONObject, buildRequestHeader, this.requestType, randomId, randomId2, randomId3);
    }
}
