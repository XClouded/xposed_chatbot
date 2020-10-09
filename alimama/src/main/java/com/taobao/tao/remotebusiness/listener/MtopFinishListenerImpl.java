package com.taobao.tao.remotebusiness.listener;

import android.os.Looper;
import com.taobao.tao.remotebusiness.IRemoteParserListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.handler.HandlerMgr;
import com.taobao.tao.remotebusiness.handler.HandlerParam;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopStatistics;

class MtopFinishListenerImpl extends MtopBaseListener implements MtopCallback.MtopFinishListener {
    private static final String TAG = "mtopsdk.MtopFinishListenerImpl";

    public MtopFinishListenerImpl(MtopBusiness mtopBusiness, MtopListener mtopListener) {
        super(mtopBusiness, mtopListener);
    }

    public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
        long j;
        String seqNo = this.mtopBusiness.getSeqNo();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, seqNo, "Mtop onFinished event received.");
        }
        if (this.mtopBusiness.isTaskCanceled()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, seqNo, "The request of MtopBusiness is canceled.");
            }
        } else if (this.listener == null) {
            TBSdkLog.e(TAG, seqNo, "The listener of MtopBusiness is null.");
        } else if (mtopFinishEvent == null) {
            TBSdkLog.e(TAG, seqNo, "MtopFinishEvent is null.");
        } else {
            MtopResponse mtopResponse = mtopFinishEvent.getMtopResponse();
            if (mtopResponse == null) {
                TBSdkLog.e(TAG, seqNo, "The MtopResponse of MtopFinishEvent is null.");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (this.listener instanceof IRemoteParserListener) {
                try {
                    ((IRemoteParserListener) this.listener).parseResponse(mtopResponse);
                } catch (Exception e) {
                    TBSdkLog.e(TAG, seqNo, "listener parseResponse callback error.", e);
                }
            }
            HandlerParam handlerMsg = HandlerMgr.getHandlerMsg(this.listener, mtopFinishEvent, this.mtopBusiness);
            handlerMsg.mtopResponse = mtopResponse;
            long currentTimeMillis2 = System.currentTimeMillis();
            if (!mtopResponse.isApiSuccess() || this.mtopBusiness.clazz == null) {
                j = currentTimeMillis2;
            } else {
                handlerMsg.pojo = MtopConvert.mtopResponseToOutputDO(mtopResponse, this.mtopBusiness.clazz);
                j = System.currentTimeMillis();
            }
            this.mtopBusiness.onBgFinishTime = j;
            MtopStatistics mtopStat = mtopResponse.getMtopStat();
            MtopStatistics.RbStatisticData rbStatisticData = null;
            if (mtopStat != null) {
                rbStatisticData = mtopStat.getRbStatData();
                rbStatisticData.beforeReqTime = this.mtopBusiness.sendStartTime - this.mtopBusiness.reqStartTime;
                rbStatisticData.mtopReqTime = currentTimeMillis - this.mtopBusiness.sendStartTime;
                rbStatisticData.afterReqTime = this.mtopBusiness.onBgFinishTime - currentTimeMillis;
                rbStatisticData.parseTime = currentTimeMillis2 - currentTimeMillis;
                rbStatisticData.jsonParseTime = j - currentTimeMillis2;
                rbStatisticData.jsonTime = rbStatisticData.jsonParseTime;
                rbStatisticData.rbReqTime = this.mtopBusiness.onBgFinishTime - this.mtopBusiness.reqStartTime;
                rbStatisticData.totalTime = rbStatisticData.rbReqTime;
                rbStatisticData.mtopDispatchTime = mtopStat.currentTimeMillis() - mtopStat.startCallbackTime;
            }
            if (this.mtopBusiness.mtopProp.handler != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, seqNo, "onReceive: ON_FINISHED in self-defined handler.");
                }
                long currentTimeMillis3 = System.currentTimeMillis();
                if (mtopStat != null) {
                    mtopStat.rspCbStart = System.currentTimeMillis();
                }
                handlerMsg.mtopBusiness.doFinish(handlerMsg.mtopResponse, handlerMsg.pojo);
                if (mtopStat != null) {
                    mtopStat.rspCbEnd = System.currentTimeMillis();
                    mtopStat.commitFullTrace();
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    long j2 = 0;
                    if (handlerMsg.mtopResponse.getBytedata() != null) {
                        j2 = (long) handlerMsg.mtopResponse.getBytedata().length;
                    }
                    StringBuilder sb = new StringBuilder(128);
                    sb.append("onReceive: ON_FINISHED in self-defined handler.");
                    sb.append("doFinishTime=");
                    sb.append(System.currentTimeMillis() - currentTimeMillis3);
                    sb.append(", dataSize=");
                    sb.append(j2);
                    sb.append("; ");
                    if (rbStatisticData != null) {
                        sb.append(rbStatisticData.toString());
                    }
                    TBSdkLog.i(TAG, seqNo, sb.toString());
                }
                if (mtopStat != null) {
                    mtopStat.isMain = this.mtopBusiness.mtopProp.handler.getLooper().equals(Looper.getMainLooper());
                    mtopStat.commitStatData(true);
                    return;
                }
                return;
            }
            if (mtopStat != null) {
                mtopStat.rspCbDispatch = System.currentTimeMillis();
            }
            HandlerMgr.instance().obtainMessage(3, handlerMsg).sendToTarget();
        }
    }
}
