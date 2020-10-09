package com.alimama.union.app.share;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimama.union.app.share.network.TaoCodeRequest;
import com.alimama.union.app.share.network.TaoCodeResponse;

public class TaoCodeReqManager implements RxMtopRequest.RxMtopResult<TaoCodeResponse> {
    public static final String ERROR_CODE_TITLE_ILLEGAL = "PARAMETER_ERROR_TITLE_ILLEGAL";
    public static final String ERROR_CODE_TITLE_TOO_SHORT = "PARAMETER_ERROR_TITLE_LENGTH_TOO_SHORT";
    @Nullable
    private Result<String> mQrCodeTarget;
    @NonNull
    private final String mShareUrl;
    @Nullable
    private TaoCodeResponse mTaoCodeInfo;
    @NonNull
    private final TaoCodeReqProvider mTaoCodeReqProvider = new TaoCodeReqProvider();
    @Nullable
    private Result<TaoCodeResponse> mTaoCodeTarget;

    public class TaoCodeReqProvider {
        /* access modifiers changed from: private */
        @Nullable
        public TaoCodeRequest.ReqParam mReqParam;
        @Nullable
        private TaoCodeRequest mTaoCodeRequest;

        public TaoCodeReqProvider() {
        }

        /* access modifiers changed from: package-private */
        public TaoCodeRequest genTaoCodeReq(@NonNull TaoCodeRequest.ReqParam reqParam) {
            this.mReqParam = reqParam;
            if (this.mTaoCodeRequest != null) {
                this.mTaoCodeRequest.setRxMtopResult((RxMtopRequest.RxMtopResult) null);
            }
            this.mTaoCodeRequest = new TaoCodeRequest(reqParam);
            return this.mTaoCodeRequest;
        }
    }

    TaoCodeReqManager(@NonNull String str) {
        this.mShareUrl = str;
    }

    public void getTaoCode(@NonNull TaoCodeRequest.ReqParam reqParam, @NonNull Result<TaoCodeResponse> result) {
        if (isCacheValid(reqParam)) {
            result.onResult(this.mTaoCodeInfo);
            return;
        }
        this.mTaoCodeTarget = result;
        TaoCodeRequest genTaoCodeReq = this.mTaoCodeReqProvider.genTaoCodeReq(reqParam);
        genTaoCodeReq.setRxMtopResult(this);
        genTaoCodeReq.sendRequest();
    }

    public void genQrCodeUrl(@NonNull TaoCodeRequest.ReqParam reqParam, @NonNull Result<String> result) {
        if (isCacheValid(reqParam)) {
            result.onResult(this.mTaoCodeInfo.getTinyLink());
            return;
        }
        this.mQrCodeTarget = result;
        TaoCodeRequest genTaoCodeReq = this.mTaoCodeReqProvider.genTaoCodeReq(reqParam);
        genTaoCodeReq.setRxMtopResult(this);
        genTaoCodeReq.sendRequest();
    }

    public String getSharedImageKey() {
        if (this.mTaoCodeInfo == null) {
            return this.mShareUrl;
        }
        return this.mTaoCodeInfo.getTinyLink();
    }

    public void cleanup() {
        this.mQrCodeTarget = null;
        this.mTaoCodeTarget = null;
    }

    public TaoCodeResponse getTaoCodeInfo() {
        return this.mTaoCodeInfo;
    }

    public void result(RxMtopResponse<TaoCodeResponse> rxMtopResponse) {
        if (!rxMtopResponse.isReqSuccess || rxMtopResponse.result == null) {
            notifyTaoCodeTarget(rxMtopResponse);
            notifyQrCodeTarget(this.mShareUrl);
            return;
        }
        this.mTaoCodeInfo = (TaoCodeResponse) rxMtopResponse.result;
        notifyTaoCodeTarget(rxMtopResponse);
        notifyQrCodeTarget(this.mTaoCodeInfo.getTinyLink());
    }

    private boolean isCacheValid(@NonNull TaoCodeRequest.ReqParam reqParam) {
        TaoCodeRequest.ReqParam access$000;
        if (this.mTaoCodeInfo == null || (access$000 = this.mTaoCodeReqProvider.mReqParam) == null || !access$000.equals(reqParam)) {
            return false;
        }
        return true;
    }

    private void notifyQrCodeTarget(@NonNull String str) {
        if (this.mQrCodeTarget != null) {
            this.mQrCodeTarget.onResult(str);
            this.mQrCodeTarget = null;
        }
    }

    private void notifyTaoCodeTarget(@NonNull RxMtopResponse<TaoCodeResponse> rxMtopResponse) {
        if (this.mTaoCodeTarget != null) {
            if (!rxMtopResponse.isReqSuccess || rxMtopResponse.result == null) {
                this.mTaoCodeTarget.onFailure(rxMtopResponse.retCode);
            } else {
                this.mTaoCodeTarget.onResult(rxMtopResponse.result);
            }
            this.mTaoCodeTarget = null;
        }
    }
}
