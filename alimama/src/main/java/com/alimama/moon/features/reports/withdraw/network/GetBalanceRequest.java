package com.alimama.moon.features.reports.withdraw.network;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class GetBalanceRequest extends RxMtopRequest<GetBalanceResponse> {
    public GetBalanceRequest() {
        setApiInfo(ApiInfo.WITHDRAW_GET_BALANCE);
    }

    public GetBalanceResponse decodeResult(SafeJSONObject safeJSONObject) {
        return GetBalanceResponse.fromJson(safeJSONObject);
    }
}
