package com.alimama.union.app.configcenter;

import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class ConfigCenterRequest extends RxMtopRequest<ConfigCenterResponse> {
    public ConfigCenterRequest() {
        setApiInfo(ApiInfo.API_CONFIG_QUERY);
        appendParam("appKey", "union");
    }

    public ConfigCenterResponse decodeResult(SafeJSONObject safeJSONObject) {
        return new ConfigCenterResponse(safeJSONObject.optJSONObject("data"));
    }
}
