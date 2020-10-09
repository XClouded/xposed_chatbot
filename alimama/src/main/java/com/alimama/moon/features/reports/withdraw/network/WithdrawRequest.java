package com.alimama.moon.features.reports.withdraw.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class WithdrawRequest extends RxMtopRequest<WithdrawResponse> {
    public WithdrawRequest(double d, String str) {
        setApiInfo(ApiInfo.WITHDRAW);
        enablePost(true);
        appendParam("amount", String.valueOf(d));
        appendParam("checkCode", str);
    }

    public WithdrawResponse decodeResult(SafeJSONObject safeJSONObject) {
        return WithdrawResponse.fromJson(safeJSONObject);
    }
}
