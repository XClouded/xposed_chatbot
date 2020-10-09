package com.alimama.union.app.messagelist.network;

import com.alimamaunion.base.safejson.SafeJSONObject;

public class MessageListCountResponse {
    public int unRead;

    public MessageListCountResponse(SafeJSONObject safeJSONObject) {
        this.unRead = safeJSONObject.optInt("unRead");
    }
}
