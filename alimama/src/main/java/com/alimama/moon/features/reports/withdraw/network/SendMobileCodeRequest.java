package com.alimama.moon.features.reports.withdraw.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class SendMobileCodeRequest extends RxMtopRequest<Void> {
    public Void decodeResult(SafeJSONObject safeJSONObject) {
        return null;
    }

    public SendMobileCodeRequest() {
        setApiInfo(ApiInfo.SEND_MOBILE_CODE);
        enablePost(true);
    }
}
