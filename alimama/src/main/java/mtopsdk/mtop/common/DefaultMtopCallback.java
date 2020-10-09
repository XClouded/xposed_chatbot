package mtopsdk.mtop.common;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCallback;

public class DefaultMtopCallback implements MtopCallback.MtopProgressListener, MtopCallback.MtopFinishListener, MtopCallback.MtopHeaderListener {
    private static final String TAG = "mtopsdk.DefaultMtopCallback";

    public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
        if (mtopFinishEvent != null && mtopFinishEvent.getMtopResponse() != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            String str = mtopFinishEvent.seqNo;
            TBSdkLog.d(TAG, str, "[onFinished]" + mtopFinishEvent.getMtopResponse().toString());
        }
    }

    public void onDataReceived(MtopProgressEvent mtopProgressEvent, Object obj) {
        if (mtopProgressEvent != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            String str = mtopProgressEvent.seqNo;
            TBSdkLog.d(TAG, str, "[onDataReceived]" + mtopProgressEvent.toString());
        }
    }

    public void onHeader(MtopHeaderEvent mtopHeaderEvent, Object obj) {
        if (mtopHeaderEvent != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            String str = mtopHeaderEvent.seqNo;
            TBSdkLog.d(TAG, str, "[onHeader]" + mtopHeaderEvent.toString());
        }
    }
}
