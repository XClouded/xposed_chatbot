package com.alimama.union.app.share.flutter.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class ShareInfoRequest2 extends RxMtopRequest<ShareInfoResponse2> {
    private static final String PARAM_KEY_SPM = "spm";
    private static final String PARAM_KEY_URL = "url";
    private static final String TAG = "ShareInfoRequest";

    public ShareInfoRequest2(@NonNull String str) {
        setApiInfo(ApiInfo.SHARE_INFO_V2);
        enablePost(true);
        String appendUrlUnionLens = UnionLensUtil.appendUrlUnionLens(str);
        appendParam("url", appendUrlUnionLens);
        SafeJSONObject safeJSONObject = new SafeJSONObject();
        safeJSONObject.put(UTHelper.SCM_URI_PARAMETER_KEY, "20140618.1.01010001.s101c6");
        try {
            Uri parse = Uri.parse(appendUrlUnionLens);
            String queryParameter = parse.getQueryParameter("spm");
            if (!TextUtils.isEmpty(queryParameter)) {
                safeJSONObject.put("spm", queryParameter);
            }
            String host = parse.getHost();
            if (!TextUtils.isEmpty(host) && host.contains("uland")) {
                safeJSONObject.put("src", "tblm_lmapp");
            }
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "parsing url failed", e);
        }
        appendParam("extendParam", safeJSONObject.toString());
    }

    public ShareInfoResponse2 decodeResult(SafeJSONObject safeJSONObject) {
        return ShareInfoResponse2.fromJson(safeJSONObject.optJSONObject("data"));
    }
}
