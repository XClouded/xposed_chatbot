package com.alimama.union.app.toolCenter.data;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimamaunion.base.safejson.SafeJSONObject;
import org.greenrobot.eventbus.EventBus;

public class HomeToolFragDataModel extends RxMtopRequest<HomeToolFragDataResult> implements RxMtopRequest.RxMtopResult<HomeToolFragDataResult> {
    private static final int CACHE_TIME = 86400;
    private static final String PAGE_NAME = "pageName";
    private static final String PAGE_NAME_VALUE = "tools-page";

    public HomeToolFragDataModel() {
        setApiInfo(ApiInfo.TOOL_FRAGMENT);
        getParams().put("pageName", PAGE_NAME_VALUE);
        setEnableCache(true).setCacheStrategy(1).setCacheTime(CACHE_TIME);
        setRxMtopResult(this);
    }

    public HomeToolFragDataResult decodeResult(SafeJSONObject safeJSONObject) {
        return new HomeToolFragDataResult(safeJSONObject);
    }

    public void result(RxMtopResponse<HomeToolFragDataResult> rxMtopResponse) {
        ToolTabDataEvent toolTabDataEvent = new ToolTabDataEvent();
        toolTabDataEvent.isSuccess = rxMtopResponse.isReqSuccess;
        toolTabDataEvent.dataResult = (HomeToolFragDataResult) rxMtopResponse.result;
        EventBus.getDefault().post(toolTabDataEvent);
    }
}
