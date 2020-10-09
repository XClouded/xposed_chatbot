package anet.channel.analysis;

import android.text.TextUtils;
import anet.channel.fulltrace.IFullTraceAnalysis;
import anet.channel.fulltrace.SceneInfo;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import com.taobao.analysis.fulltrace.FullTraceAnalysis;
import com.taobao.analysis.fulltrace.RequestInfo;
import com.taobao.analysis.scene.SceneIdentifier;

public class DefaultFullTraceAnalysis implements IFullTraceAnalysis {
    private static final String REQ_TYPE = "network";
    private static final String TAG = "awcn.DefaultFullTraceAnalysis";
    private boolean isAnalysisValid;

    public DefaultFullTraceAnalysis() {
        try {
            Class.forName("com.taobao.analysis.fulltrace.FullTraceAnalysis");
            this.isAnalysisValid = true;
        } catch (Exception unused) {
            this.isAnalysisValid = false;
            ALog.e(TAG, "not supoort FullTraceAnalysis", (String) null, new Object[0]);
        }
    }

    public String createRequest() {
        if (this.isAnalysisValid) {
            return FullTraceAnalysis.getInstance().createRequest(REQ_TYPE);
        }
        return null;
    }

    public void commitRequest(String str, RequestStatistic requestStatistic) {
        if (this.isAnalysisValid && requestStatistic != null && !TextUtils.isEmpty(str)) {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.host = requestStatistic.host;
            requestInfo.bizId = requestStatistic.bizId;
            requestInfo.url = requestStatistic.url;
            requestInfo.retryTimes = requestStatistic.retryTimes;
            requestInfo.netType = requestStatistic.netType;
            requestInfo.protocolType = requestStatistic.protocolType;
            requestInfo.ret = requestStatistic.ret;
            requestInfo.isCbMain = false;
            requestInfo.isReqMain = requestStatistic.isReqMain;
            requestInfo.isReqSync = requestStatistic.isReqSync;
            requestInfo.netReqStart = requestStatistic.netReqStart;
            requestInfo.netReqServiceBindEnd = requestStatistic.reqServiceTransmissionEnd;
            requestInfo.netReqProcessStart = requestStatistic.reqStart;
            requestInfo.netReqSendStart = requestStatistic.sendStart;
            requestInfo.netRspRecvEnd = requestStatistic.rspEnd;
            requestInfo.netRspCbDispatch = requestStatistic.rspCbDispatch;
            requestInfo.netRspCbStart = requestStatistic.rspCbStart;
            requestInfo.netRspCbEnd = requestStatistic.rspCbEnd;
            requestInfo.reqDeflateSize = requestStatistic.reqHeadDeflateSize + requestStatistic.reqBodyDeflateSize;
            requestInfo.reqInflateSize = requestStatistic.reqHeadInflateSize + requestStatistic.reqBodyInflateSize;
            requestInfo.rspDeflateSize = requestStatistic.rspHeadDeflateSize + requestStatistic.rspBodyDeflateSize;
            requestInfo.rspInflateSize = requestStatistic.rspHeadInflateSize + requestStatistic.rspBodyInflateSize;
            requestInfo.serverRT = requestStatistic.serverRT;
            requestInfo.sendDataTime = requestStatistic.sendDataTime;
            requestInfo.firstDataTime = requestStatistic.firstDataTime;
            requestInfo.recvDataTime = requestStatistic.recDataTime;
            FullTraceAnalysis.getInstance().commitRequest(str, REQ_TYPE, requestInfo);
        }
    }

    public SceneInfo getSceneInfo() {
        if (!this.isAnalysisValid) {
            return null;
        }
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.isUrlLaunch = SceneIdentifier.isUrlLaunch();
        sceneInfo.appLaunchTime = SceneIdentifier.getAppLaunchTime();
        sceneInfo.lastLaunchTime = SceneIdentifier.getLastLaunchTime();
        sceneInfo.deviceLevel = SceneIdentifier.getDeviceLevel();
        sceneInfo.startType = SceneIdentifier.getStartType();
        sceneInfo.speedBucket = SceneIdentifier.getBucketInfo();
        return sceneInfo;
    }
}
