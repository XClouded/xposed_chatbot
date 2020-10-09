package com.alimama.moon.features.reports.withdraw.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class ChangeMobileRequest extends RxMtopRequest<Void> {
    public Void decodeResult(SafeJSONObject safeJSONObject) {
        return null;
    }

    public ChangeMobileRequest() {
        setApiInfo(ApiInfo.CHANGE_MOBILE);
        enablePost(true);
    }

    public void sendRequest(RxMtopRequest.RxMtopResult<Void> rxMtopResult) {
        super.sendRequest(rxMtopResult);
    }
}
