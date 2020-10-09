package com.alimama.union.app.taotokenConvert;

import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.union.app.network.response.TaoCodeItemInfo;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class TaoCodeItemInfoRequest extends RxMtopRequest<TaoCodeItemInfo> {
    public TaoCodeItemInfoRequest(String str) {
        setApiInfo(ApiInfo.TAO_CODE_ITEM_INFO_API);
        enablePost(true);
        appendParam("url", str);
        appendParam("extendParam", UnionLensUtil.geneRidPvidJsonStr());
    }

    public TaoCodeItemInfo decodeResult(SafeJSONObject safeJSONObject) {
        TaoCodeItemInfo taoCodeItemInfo = new TaoCodeItemInfo();
        taoCodeItemInfo.fromJson(safeJSONObject.optJSONObject("data"));
        return taoCodeItemInfo;
    }
}
