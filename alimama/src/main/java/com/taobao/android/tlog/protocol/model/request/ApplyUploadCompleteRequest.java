package com.taobao.android.tlog.protocol.model.request;

import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.reply.base.RemoteFileInfo;
import com.taobao.android.tlog.protocol.model.request.base.LogRequestBase;
import com.taobao.android.tlog.protocol.utils.RandomIdUtils;

public class ApplyUploadCompleteRequest extends LogRequestBase {
    private String TAG = "TLOG.Protocol.ApplyUploadCompleteRequest";
    public RemoteFileInfo[] remoteFileInfos;
    private String requestType = "REQUEST";
    public String uploadId;

    public RequestResult build() throws Exception {
        String randomId = RandomIdUtils.getRandomId();
        String randomId2 = RandomIdUtils.getRandomId();
        String randomId3 = RandomIdUtils.getRandomId();
        JSONObject buildRequestHeader = BuilderHelper.buildRequestHeader(this, randomId, randomId2);
        JSONObject jSONObject = new JSONObject();
        if (this.uploadId != null) {
            jSONObject.put("uploadId", (Object) this.uploadId);
        }
        if (this.remoteFileInfos != null) {
            jSONObject.put("remoteFileInfos", (Object) this.remoteFileInfos);
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
