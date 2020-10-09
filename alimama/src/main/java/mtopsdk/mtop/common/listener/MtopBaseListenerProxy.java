package mtopsdk.mtop.common.listener;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopProgressEvent;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopBaseListenerProxy extends DefaultMtopCallback {
    private static final String TAG = "mtopsdk.MtopListenerProxy";
    protected boolean isCached = false;
    protected MtopListener listener;
    public Object reqContext = null;
    public MtopResponse response = null;

    public MtopBaseListenerProxy(MtopListener mtopListener) {
        this.listener = mtopListener;
    }

    public void onFinished(MtopFinishEvent mtopFinishEvent, Object obj) {
        if (!(mtopFinishEvent == null || mtopFinishEvent.getMtopResponse() == null)) {
            this.response = mtopFinishEvent.getMtopResponse();
            this.reqContext = obj;
        }
        synchronized (this) {
            try {
                notifyAll();
            } catch (Exception unused) {
                TBSdkLog.e(TAG, "[onFinished] notify error");
            }
        }
        if (!(this.listener instanceof MtopCallback.MtopFinishListener)) {
            return;
        }
        if (!this.isCached || (this.response != null && this.response.isApiSuccess())) {
            ((MtopCallback.MtopFinishListener) this.listener).onFinished(mtopFinishEvent, obj);
        }
    }

    public void onDataReceived(MtopProgressEvent mtopProgressEvent, Object obj) {
        if (this.listener instanceof MtopCallback.MtopProgressListener) {
            ((MtopCallback.MtopProgressListener) this.listener).onDataReceived(mtopProgressEvent, obj);
        }
    }

    public void onHeader(MtopHeaderEvent mtopHeaderEvent, Object obj) {
        if (this.listener instanceof MtopCallback.MtopHeaderListener) {
            ((MtopCallback.MtopHeaderListener) this.listener).onHeader(mtopHeaderEvent, obj);
        }
    }
}
