package com.taobao.android.tlog.protocol;

import alimama.com.unweventparse.constants.EventConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.utils.Base64;

public class TLogReply {
    private String TAG;

    private TLogReply() {
        this.TAG = "TLogReply";
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static TLogReply instance = new TLogReply();

        private CreateInstance() {
        }
    }

    public static synchronized TLogReply getInstance() {
        TLogReply access$100;
        synchronized (TLogReply.class) {
            access$100 = CreateInstance.instance;
        }
        return access$100;
    }

    public String parseContent(String str, String str2, String str3, byte[] bArr) throws Exception {
        return new String(Base64.decode(bArr), "utf-8");
    }

    public CommandInfo parseCommandInfo(byte[] bArr, String str, String str2, String str3) throws Exception {
        JSONObject parseObject = JSON.parseObject(str);
        CommandInfo commandInfo = new CommandInfo();
        commandInfo.forward = bArr;
        commandInfo.serviceId = str3;
        commandInfo.userId = str2;
        if (parseObject.containsKey("type")) {
            commandInfo.msgType = parseObject.getString("type");
        }
        if (parseObject.containsKey(EventConstants.Mtop.HEADERS)) {
            JSONObject jSONObject = (JSONObject) parseObject.get(EventConstants.Mtop.HEADERS);
            if (jSONObject.containsKey(Constants.appKeyName)) {
                commandInfo.appKey = jSONObject.getString(Constants.appKeyName);
            }
            if (jSONObject.containsKey(Constants.appIdName)) {
                commandInfo.appId = jSONObject.getString(Constants.appIdName);
            }
            if (jSONObject.containsKey(Constants.requestIdName)) {
                commandInfo.requestId = jSONObject.getString(Constants.requestIdName);
            }
            if (jSONObject.containsKey(Constants.opCodeName)) {
                commandInfo.opCode = jSONObject.getString(Constants.opCodeName);
            }
            if (jSONObject.containsKey(Constants.replyIdName)) {
                commandInfo.replyId = jSONObject.getString(Constants.replyIdName);
            }
            if (jSONObject.containsKey(Constants.replyCode)) {
                commandInfo.replyCode = jSONObject.getString(Constants.replyCode);
            }
            if (jSONObject.containsKey(Constants.sessionIdName)) {
                commandInfo.sessionId = jSONObject.getString(Constants.sessionIdName);
            }
            if (jSONObject.containsKey(Constants.replyMsg)) {
                commandInfo.replyMessage = jSONObject.getString(Constants.replyMsg);
            }
        }
        if (parseObject.containsKey("data")) {
            commandInfo.data = parseObject.getJSONObject("data");
        }
        return commandInfo;
    }
}
