package com.alibaba.android.anynetwork.plugin.allinone;

import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.anynetwork.core.IANAsyncCallback;
import com.alibaba.android.anynetwork.core.utils.ANLog;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MtopResponse;

public class AllInOneMtopAsyncCallbackProxy extends BaseAsyncCallbackProxy implements MtopCallback.MtopFinishListener {
    private static final String TAG = "AllInOneMtopAsyncCallbackProxy";

    public AllInOneMtopAsyncCallbackProxy(IANAsyncCallback iANAsyncCallback, IAllInOneConverter iAllInOneConverter) {
        super(iANAsyncCallback, iAllInOneConverter);
    }

    public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
        ANResponse aNResponse;
        MtopResponse mtopResponse = mtopFinishEvent.getMtopResponse();
        if (mtopResponse == null) {
            this.mANCallback.onCallback(ANResponse.generateFailResponse("mtop", -1001, "unknow error"));
            return;
        }
        if (mtopResponse.isApiSuccess()) {
            ANLog.d(TAG, "MtopResponse: " + mtopResponse.toString());
            aNResponse = this.mConverter.convertMtopResponse2ANResponse(mtopResponse);
            StringBuilder sb = new StringBuilder();
            sb.append("ANResponse: ");
            sb.append(aNResponse == null ? null : aNResponse.toString());
            ANLog.d(TAG, sb.toString());
        } else if (mtopResponse.isSessionInvalid()) {
            aNResponse = ANResponse.generateFailResponse("mtop", -2004, "Session is invalid");
        } else if (mtopResponse.isSystemError()) {
            aNResponse = ANResponse.generateFailResponse("mtop", NetErrorRes.ERROR_CODE_SYSTEM_ERROR, mtopResponse.getRetMsg());
        } else if (mtopResponse.isNetworkError()) {
            aNResponse = ANResponse.generateFailResponse("mtop", NetErrorRes.ERROR_CODE_NEWTWORK_ERROR, mtopResponse.getRetMsg());
        } else if (mtopResponse.isExpiredRequest()) {
            aNResponse = ANResponse.generateFailResponse("mtop", -2010, mtopResponse.getRetMsg());
        } else if (mtopResponse.is41XResult()) {
            aNResponse = ANResponse.generateFailResponse("mtop", -2012, mtopResponse.getRetMsg());
        } else if (mtopResponse.isApiLockedResult()) {
            aNResponse = ANResponse.generateFailResponse("mtop", -2014, mtopResponse.getRetMsg());
        } else if (mtopResponse.isMtopSdkError()) {
            aNResponse = ANResponse.generateFailResponse("mtop", -2016, mtopResponse.getRetMsg());
        } else {
            aNResponse = ANResponse.generateFailResponse("mtop", mtopResponse.getResponseCode(), mtopResponse.getRetMsg());
        }
        this.mANCallback.onCallback(aNResponse);
    }
}
