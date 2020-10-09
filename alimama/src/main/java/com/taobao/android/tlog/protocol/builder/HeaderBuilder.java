package com.taobao.android.tlog.protocol.builder;

import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.base.LogReplyBaseInfo;
import com.taobao.android.tlog.protocol.utils.RandomIdUtils;
import java.util.LinkedHashMap;
import java.util.Map;

public class HeaderBuilder {
    public static Map<String, String> buildReplyHeaders(CommandInfo commandInfo, LogReplyBaseInfo logReplyBaseInfo) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (logReplyBaseInfo.appKey != null) {
            linkedHashMap.put(Constants.appKeyName, logReplyBaseInfo.appKey);
        }
        if (logReplyBaseInfo.appId != null) {
            linkedHashMap.put(Constants.appIdName, logReplyBaseInfo.appId);
        }
        if (logReplyBaseInfo.utdid != null) {
            linkedHashMap.put(Constants.deviceIdName, logReplyBaseInfo.utdid);
        }
        if (commandInfo.requestId != null) {
            linkedHashMap.put(Constants.requestIdName, commandInfo.requestId);
        }
        linkedHashMap.put(Constants.replyIdName, RandomIdUtils.getRandomId());
        linkedHashMap.put(Constants.sessionIdName, RandomIdUtils.getRandomId());
        linkedHashMap.put(Constants.opCodeName, logReplyBaseInfo.replyOpCode);
        if (logReplyBaseInfo.replyCode != null) {
            linkedHashMap.put(Constants.replyCode, logReplyBaseInfo.replyCode);
        }
        if (logReplyBaseInfo.replyMsg != null) {
            linkedHashMap.put(Constants.replyMsg, logReplyBaseInfo.replyMsg);
        }
        return linkedHashMap;
    }
}
