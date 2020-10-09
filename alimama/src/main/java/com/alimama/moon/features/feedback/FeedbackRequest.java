package com.alimama.moon.features.feedback;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class FeedbackRequest extends RxMtopRequest<Void> {
    private static final String APP_TYPE = "moon_android";

    public Void decodeResult(SafeJSONObject safeJSONObject) {
        return null;
    }

    public FeedbackRequest(String str, String str2) {
        setApiInfo(ApiInfo.FEEDBACK_API);
        appendParam("content", str);
        appendParam("appInfo", str2);
        appendParam("apptype", APP_TYPE);
    }
}
