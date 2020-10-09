package com.alimama.union.app.messagelist.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.android.dinamicx.DXMsgConstant;

public class MessageListReadRequest extends RxMtopRequest<Void> implements RxMtopRequest.RxMtopResult<Void> {
    public Void decodeResult(SafeJSONObject safeJSONObject) {
        return null;
    }

    public void result(RxMtopResponse<Void> rxMtopResponse) {
    }

    public MessageListReadRequest() {
        setApiInfo(ApiInfo.API_MESSAGE_READ);
        appendParam(DXMsgConstant.DX_MSG_SOURCE_ID, "2");
    }
}
