package com.alimama.union.app.sceneCenter.data;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimamaunion.base.safejson.SafeJSONObject;
import org.greenrobot.eventbus.EventBus;

public class SceneFragDataModel extends RxMtopRequest<SceneTabDataResult> implements RxMtopRequest.RxMtopResult<SceneTabDataResult> {
    private static final int CACHE_TIME = 86400;
    private static final String PAGE_NAME = "pageName";
    private static final String PAGE_NAME_VALUE = "scenes-page";

    public SceneFragDataModel() {
        setApiInfo(ApiInfo.SCENE_FRAGMENT);
        getParams().put("pageName", PAGE_NAME_VALUE);
        setEnableCache(true).setCacheStrategy(1).setCacheTime(CACHE_TIME);
        setRxMtopResult(this);
    }

    public SceneTabDataResult decodeResult(SafeJSONObject safeJSONObject) {
        return new SceneTabDataResult(safeJSONObject);
    }

    public void result(RxMtopResponse<SceneTabDataResult> rxMtopResponse) {
        SceneTabDataModelEvent sceneTabDataModelEvent = new SceneTabDataModelEvent();
        sceneTabDataModelEvent.isSuccess = rxMtopResponse.isReqSuccess;
        sceneTabDataModelEvent.dataResult = (SceneTabDataResult) rxMtopResponse.result;
        EventBus.getDefault().post(sceneTabDataModelEvent);
    }
}
