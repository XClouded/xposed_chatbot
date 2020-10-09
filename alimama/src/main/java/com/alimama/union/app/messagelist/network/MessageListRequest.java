package com.alimama.union.app.messagelist.network;

import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.messagelist.MessageListDataEvent;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.rxnetwork.RxPageRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.android.dinamicx.DXMsgConstant;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

public class MessageListRequest extends RxPageRequest<MessageListResponse> implements RxMtopRequest.RxMtopResult<MessageListResponse> {
    private static final int PAGE_SIZE = 20;
    private static final String PAGING_PARAM_NUM = "pageSize";
    private static final String PAGING_PARAM_START_INDEX = "toPage";
    public static final String SOURCE_ID = "2";

    public MessageListRequest() {
        super(ApiInfo.API_MESSAGE_LIST);
        appendParam(DXMsgConstant.DX_MSG_SOURCE_ID, "2");
        setRxMtopResult(this);
    }

    /* access modifiers changed from: protected */
    public void prepareFirstParams(Map<String, String> map) {
        map.put(PAGING_PARAM_START_INDEX, "0");
        map.put("pageSize", String.valueOf(20));
    }

    /* access modifiers changed from: protected */
    public void prepareNextParams(Map<String, String> map) {
        map.put(PAGING_PARAM_START_INDEX, String.valueOf(CommonUtils.getSafeIntValue(map.get(PAGING_PARAM_START_INDEX)) + 1));
    }

    public MessageListResponse decodeResult(SafeJSONObject safeJSONObject) {
        return new MessageListResponse(safeJSONObject);
    }

    public void result(RxMtopResponse<MessageListResponse> rxMtopResponse) {
        MessageListDataEvent messageListDataEvent = new MessageListDataEvent();
        messageListDataEvent.isSuccess = rxMtopResponse.isReqSuccess;
        messageListDataEvent.dataResult = (MessageListResponse) rxMtopResponse.result;
        messageListDataEvent.mRetCode = rxMtopResponse.retCode;
        EventBus.getDefault().post(messageListDataEvent);
    }
}
