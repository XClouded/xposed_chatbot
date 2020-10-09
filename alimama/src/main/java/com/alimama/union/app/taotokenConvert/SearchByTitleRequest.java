package com.alimama.union.app.taotokenConvert;

import com.alimama.union.app.configproperties.EnvHelper;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class SearchByTitleRequest extends RxMtopRequest<Boolean> {
    public SearchByTitleRequest(String str) {
        setApiInfo(ApiInfo.SEARCH_API);
        enablePost(true);
        appendParam("pNum", "0");
        appendParam("pSize", "10");
        if (EnvHelper.getInstance().isOnLineEnv()) {
            appendParam("qieId", "211");
        } else {
            appendParam("qieId", "15328");
        }
        SafeJSONObject safeJSONObject = new SafeJSONObject();
        safeJSONObject.put("querytype", "title");
        safeJSONObject.put("q", str);
        appendParam("variableMap", safeJSONObject.toString());
    }

    public Boolean decodeResult(SafeJSONObject safeJSONObject) {
        return Boolean.valueOf(safeJSONObject.optJSONObject("data").optJSONObject("recommend").optInt("totalCount") > 0);
    }
}
