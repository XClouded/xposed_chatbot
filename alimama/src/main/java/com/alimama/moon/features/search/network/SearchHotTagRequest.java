package com.alimama.moon.features.search.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimamaunion.base.safejson.SafeJSONObject;
import org.greenrobot.eventbus.EventBus;

public class SearchHotTagRequest extends RxMtopRequest<SearchHotTagResponse> implements RxMtopRequest.RxMtopResult<SearchHotTagResponse> {
    private static final String SERVICE_ID = "2019021101";

    public SearchHotTagRequest() {
        setApiInfo(ApiInfo.API_SEARCH_HOT_TAG);
        appendParam("serviceId", SERVICE_ID);
        setRxMtopResult(this);
    }

    public SearchHotTagResponse decodeResult(SafeJSONObject safeJSONObject) {
        return new SearchHotTagResponse(safeJSONObject.optJSONObject("data").optJSONObject(SERVICE_ID));
    }

    public void result(RxMtopResponse<SearchHotTagResponse> rxMtopResponse) {
        SearchHotTagEvent searchHotTagEvent = new SearchHotTagEvent();
        searchHotTagEvent.isSuccess = rxMtopResponse.isReqSuccess;
        searchHotTagEvent.dataResult = (SearchHotTagResponse) rxMtopResponse.result;
        EventBus.getDefault().post(searchHotTagEvent);
    }
}
