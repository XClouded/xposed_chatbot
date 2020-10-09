package com.taobao.tao.remotebusiness.listener;

import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import com.taobao.tao.remotebusiness.handler.HandlerParam;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopStatistics;

class MtopCacheListenerImpl extends MtopBaseListener implements MtopCallback.MtopCacheListener {
    private static final String TAG = "mtopsdk.MtopCacheListenerImpl";

    public MtopCacheListenerImpl(MtopBusiness mtopBusiness, MtopListener mtopListener) {
        super(mtopBusiness, mtopListener);
    }

    public void onCached(MtopCacheEvent mtopCacheEvent, Object obj) {
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onCached event received. apiKey=" + this.mtopBusiness.request.getKey());
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is cancelled.");
            }
        } else if (this.listener == null) {
            TBSdkLog.e(TAG, seqNo, "The listener of MtopBusiness is null.");
        } else if (mtopCacheEvent == null) {
            TBSdkLog.e(TAG, seqNo, "MtopCacheEvent is null.");
        } else {
            MtopResponse mtopResponse = mtopCacheEvent.getMtopResponse();
            if (mtopResponse == null) {
                TBSdkLog.e(TAG, seqNo, "The MtopResponse of MtopCacheEvent is null.");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            long currentTimeMillis2 = System.currentTimeMillis();
            MtopStatistics.RbStatisticData rbStatisticData = null;
            BaseOutDo mtopResponseToOutputDO = (!mtopResponse.isApiSuccess() || this.mtopBusiness.clazz == null) ? null : MtopConvert.mtopResponseToOutputDO(mtopResponse, this.mtopBusiness.clazz);
            long currentTimeMillis3 = System.currentTimeMillis();
            this.mtopBusiness.onBgFinishTime = currentTimeMillis3;
            MtopStatistics mtopStat = mtopResponse.getMtopStat();
            if (mtopStat != null) {
                rbStatisticData = mtopStat.getRbStatData();
                rbStatisticData.jsonParseTime = currentTimeMillis3 - currentTimeMillis2;
                rbStatisticData.jsonTime = rbStatisticData.jsonParseTime;
                rbStatisticData.isCache = 1;
                rbStatisticData.mtopReqTime = currentTimeMillis - this.mtopBusiness.sendStartTime;
                rbStatisticData.rbReqTime = this.mtopBusiness.onBgFinishTime - this.mtopBusiness.reqStartTime;
                rbStatisticData.totalTime = rbStatisticData.rbReqTime;
            }
            HandlerParam handlerMsg = HandlerMgr.getHandlerMsg(this.listener, mtopCacheEvent, this.mtopBusiness);
            handlerMsg.pojo = mtopResponseToOutputDO;
            handlerMsg.mtopResponse = mtopResponse;
            this.mtopBusiness.isCached = true;
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_CACHED in self-defined handler.");
                }
                if (mtopStat != null) {
                    if (rbStatisticData != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(TAG, seqNo, rbStatisticData.toString());
                    }
                    mtopStat.commitStatData(true);
                }
                try {
                    if (handlerMsg.listener instanceof IRemoteCacheListener) {
                        TBSdkLog.i(TAG, seqNo, "listener onCached callback");
                        ((IRemoteCacheListener) handlerMsg.listener).onCached(mtopCacheEvent, handlerMsg.pojo, obj);
                        return;
                    }
                    TBSdkLog.i(TAG, seqNo, "listener onCached transfer to onSuccess callback");
                    ((IRemoteListener) handlerMsg.listener).onSuccess(handlerMsg.mtopBusiness.getRequestType(), handlerMsg.mtopResponse, handlerMsg.pojo, obj);
                } catch (Throwable th) {
                    TBSdkLog.e(TAG, seqNo, "listener onCached callback error in self-defined handler.", th);
                }
            } else {
                HandlerMgr.instance().obtainMessage(4, handlerMsg).sendToTarget();
            }
        }
    }
}
