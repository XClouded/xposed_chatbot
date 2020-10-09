package com.alimama.union.app.share.network;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class ShareInfoRequest extends RxMtopRequest<ShareInfoResponse> {
    private static final String PARAM_KEY_SPM = "spm";
    private static final String PARAM_KEY_URL = "url";
    private static final String TAG = "ShareInfoRequest";
    @Nullable
    private String mSpmVal;

    public ShareInfoRequest(@NonNull String str) {
        setApiInfo(ApiInfo.SHARE_INFO);
        enablePost(true);
        appendParam("url", str);
        try {
            this.mSpmVal = Uri.parse(str).getQueryParameter("spm");
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "parsing url failed", e);
        }
    }

    public ShareInfoResponse decodeResult(SafeJSONObject safeJSONObject) {
        return ShareInfoResponse.fromJson(safeJSONObject.optJSONObject("data"), this.mSpmVal);
    }
}
