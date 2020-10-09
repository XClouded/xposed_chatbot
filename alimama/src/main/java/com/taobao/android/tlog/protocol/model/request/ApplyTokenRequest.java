package com.taobao.android.tlog.protocol.model.request;

import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.android.tlog.protocol.model.request.base.LogRequestBase;
import com.taobao.android.tlog.protocol.utils.RandomIdUtils;

public class ApplyTokenRequest extends LogRequestBase {
    private String TAG = "TLOG.Protocol.ApplyTokenRequestInfo";
    public FileInfo[] fileInfos;
    private String requestType = "REQUEST";
    public String uploadId;

    public RequestResult build() throws Exception {
        String randomId = RandomIdUtils.getRandomId();
        String randomId2 = RandomIdUtils.getRandomId();
        JSONObject buildRequestHeader = BuilderHelper.buildRequestHeader(this, randomId, randomId2);
        JSONObject jSONObject = new JSONObject();
        if (this.uploadId != null) {
            jSONObject.put("uploadId", (Object) this.uploadId);
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
        return BuilderHelper.buildRequestResult(jSONObject, buildRequestHeader, this.requestType, randomId, randomId2, this.uploadId);
    }
}
