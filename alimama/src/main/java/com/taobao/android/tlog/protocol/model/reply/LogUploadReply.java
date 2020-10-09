package com.taobao.android.tlog.protocol.model.reply;

import alimama.com.unweventparse.constants.EventConstants;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.builder.HeaderBuilder;
import com.taobao.android.tlog.protocol.builder.UploadDataBuilder;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.base.LogReplyBaseInfo;
import com.taobao.android.tlog.protocol.model.reply.base.RemoteFileInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogUploadReply {
    private String TAG = "TLOG.Protocol.LogUploadReply";
    public Map<String, String> extraInfo;
    public RemoteFileInfo[] remoteFileInfos;
    private String replyType = "REPLY";
    public UploadTokenInfo tokenInfo;
    public String tokenType;
    public String uploadId;

    public String build(CommandInfo commandInfo, LogReplyBaseInfo logReplyBaseInfo) throws Exception {
        if (logReplyBaseInfo == null) {
            return null;
        }
        Map<String, String> buildReplyHeaders = HeaderBuilder.buildReplyHeaders(commandInfo, logReplyBaseInfo);
        JSONObject jSONObject = new JSONObject();
        if (this.uploadId != null) {
            jSONObject.put("uploadId", (Object) this.uploadId);
        }
        if (this.remoteFileInfos != null) {
            jSONObject.put("remoteFileInfos", (Object) this.remoteFileInfos);
        }
        if (this.extraInfo != null) {
            jSONObject.put("extraInfo", (Object) this.extraInfo);
        }
        if (this.tokenType != null) {
            jSONObject.put(WebConstant.WEB_LOGIN_TOKEN_TYPE, (Object) this.tokenType);
        }
        if (this.tokenInfo != null) {
            jSONObject.put(ApiConstants.ApiField.TOKEN_INFO, (Object) this.tokenInfo);
        }
        JSONObject jSONObject2 = new JSONObject();
        if (commandInfo.forward != null) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("content", new String(commandInfo.forward, "utf-8"));
            jSONObject2.put("forward", (Object) linkedHashMap);
        }
        jSONObject2.put("version", (Object) Constants.version);
        jSONObject2.put("type", (Object) this.replyType);
        jSONObject2.put(EventConstants.Mtop.HEADERS, (Object) buildReplyHeaders);
        jSONObject2.put("data", (Object) jSONObject);
        return UploadDataBuilder.buildLogUploadContent(jSONObject2.toString());
    }
}
