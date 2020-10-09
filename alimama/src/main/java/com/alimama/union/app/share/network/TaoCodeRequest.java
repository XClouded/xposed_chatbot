package com.alimama.union.app.share.network;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.union.app.rxnetwork.ApiInfo;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class TaoCodeRequest extends RxMtopRequest<TaoCodeResponse> {
    private static final String TAO_CODE_BIZ_ID = "lianmeng-app";
    private static final String TAO_CODE_SOURCE_TYPE = "other";
    private static final String TAO_CODE_TYPE = "tao";

    public static class ReqParam {
        /* access modifiers changed from: private */
        public String mImageUrl;
        /* access modifiers changed from: private */
        public String mTargetUrl;
        /* access modifiers changed from: private */
        public String mTitle;

        public ReqParam(@NonNull String str, @Nullable String str2, @NonNull String str3) {
            this.mTargetUrl = str;
            this.mImageUrl = str2;
            this.mTitle = str3;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getImageUrl() {
            return this.mImageUrl;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReqParam)) {
                return false;
            }
            ReqParam reqParam = (ReqParam) obj;
            if (this.mTargetUrl == null ? reqParam.mTargetUrl != null : !this.mTargetUrl.equals(reqParam.mTargetUrl)) {
                return false;
            }
            if (this.mImageUrl == null ? reqParam.mImageUrl != null : !this.mImageUrl.equals(reqParam.mImageUrl)) {
                return false;
            }
            if (this.mTitle != null) {
                return this.mTitle.equals(reqParam.mTitle);
            }
            if (reqParam.mTitle == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = (((this.mTargetUrl != null ? this.mTargetUrl.hashCode() : 0) * 31) + (this.mImageUrl != null ? this.mImageUrl.hashCode() : 0)) * 31;
            if (this.mTitle != null) {
                i = this.mTitle.hashCode();
            }
            return hashCode + i;
        }
    }

    public TaoCodeRequest(@NonNull ReqParam reqParam) {
        setApiInfo(ApiInfo.TAO_CODE_GENERATE);
        appendParam("targetUrl", reqParam.mTargetUrl);
        appendParam("title", reqParam.mTitle);
        if (!TextUtils.isEmpty(reqParam.mImageUrl)) {
            appendParam("picUrl", reqParam.mImageUrl);
        }
        appendParam("passwordType", TAO_CODE_TYPE);
        appendParam("sourceType", "other");
        appendParam("bizId", TAO_CODE_BIZ_ID);
        appendParam("extendInfo", "{\"isCallClient\":\"0\"}");
    }

    public TaoCodeResponse decodeResult(SafeJSONObject safeJSONObject) {
        return TaoCodeResponse.fromJson(safeJSONObject.optJSONObject("data"));
    }
}
