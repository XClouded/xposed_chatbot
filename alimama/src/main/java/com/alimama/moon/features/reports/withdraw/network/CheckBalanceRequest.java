package com.alimama.moon.features.reports.withdraw.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class CheckBalanceRequest extends RxMtopRequest<CheckBalanceResponse> {
    public CheckBalanceRequest(double d) {
        setApiInfo(ApiInfo.WITHDRAW_CHECK_BALANCE);
        appendParam("count", String.valueOf(d));
    }

    public CheckBalanceResponse decodeResult(SafeJSONObject safeJSONObject) {
        return CheckBalanceResponse.fromJson(safeJSONObject);
    }
}
