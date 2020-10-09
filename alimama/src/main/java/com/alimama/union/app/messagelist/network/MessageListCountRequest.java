package com.alimama.union.app.messagelist.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.android.dinamicx.DXMsgConstant;

public class MessageListCountRequest extends RxMtopRequest<MessageListCountResponse> {
    public MessageListCountRequest() {
        setApiInfo(ApiInfo.API_MESSAGE_COUNT);
        appendParam(DXMsgConstant.DX_MSG_SOURCE_ID, "2");
    }

    public MessageListCountResponse decodeResult(SafeJSONObject safeJSONObject) {
        return new MessageListCountResponse(safeJSONObject.optJSONObject("data"));
    }
}
