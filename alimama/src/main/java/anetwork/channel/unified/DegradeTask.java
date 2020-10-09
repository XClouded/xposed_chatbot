package anetwork.channel.unified;

import android.text.TextUtils;
import anet.channel.RequestCb;
import anet.channel.bytes.ByteArray;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.session.HttpConnector;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cookie.CookieManager;
import java.util.List;
import java.util.Map;

public class DegradeTask implements IUnifiedTask {
    private static final String TAG = "anet.DegradeTask";
    volatile Cancelable cancelable = null;
    /* access modifiers changed from: private */
    public int contentLength = 0;
    /* access modifiers changed from: private */
    public int dataChunkIndex = 0;
    private volatile boolean isCanceled = false;
    /* access modifiers changed from: private */
    public RequestContext rc;
    /* access modifiers changed from: private */
    public Request request;

    static /* synthetic */ int access$208(DegradeTask degradeTask) {
        int i = degradeTask.dataChunkIndex;
        degradeTask.dataChunkIndex = i + 1;
        return i;
    }

    public DegradeTask(RequestContext requestContext) {
        this.rc = requestContext;
        this.request = requestContext.config.getAwcnRequest();
    }

    public void cancel() {
        this.isCanceled = true;
        if (this.cancelable != null) {
            this.cancelable.cancel();
        }
    }

    public void run() {
        if (!this.isCanceled) {
            if (this.rc.config.isRequestCookieEnabled()) {
                String cookie = CookieManager.getCookie(this.rc.config.getUrlString());
                if (!TextUtils.isEmpty(cookie)) {
                    Request.Builder newBuilder = this.request.newBuilder();
                    String str = this.request.getHeaders().get(HttpConstant.COOKIE);
                    if (!TextUtils.isEmpty(str)) {
                        cookie = StringUtils.concatString(str, "; ", cookie);
                    }
                    newBuilder.addHeader(HttpConstant.COOKIE, cookie);
                    this.request = newBuilder.build();
                }
            }
            this.request.rs.degraded = 2;
            this.request.rs.sendBeforeTime = System.currentTimeMillis() - this.request.rs.reqStart;
            HttpConnector.connect(this.request, new RequestCb() {
                public void onResponseCode(int i, Map<String, List<String>> map) {
                    if (!DegradeTask.this.rc.isDone.get()) {
                        DegradeTask.this.rc.cancelTimeoutTask();
                        CookieManager.setCookie(DegradeTask.this.rc.config.getUrlString(), map);
                        int unused = DegradeTask.this.contentLength = HttpHelper.parseContentLength(map);
                        if (DegradeTask.this.rc.callback != null) {
                            DegradeTask.this.rc.callback.onResponseCode(i, map);
                        }
                    }
                }

                public void onDataReceive(ByteArray byteArray, boolean z) {
                    if (!DegradeTask.this.rc.isDone.get()) {
                        DegradeTask.access$208(DegradeTask.this);
                        if (DegradeTask.this.rc.callback != null) {
                            DegradeTask.this.rc.callback.onDataReceiveSize(DegradeTask.this.dataChunkIndex, DegradeTask.this.contentLength, byteArray);
                        }
                    }
                }

                public void onFinish(int i, String str, RequestStatistic requestStatistic) {
                    if (!DegradeTask.this.rc.isDone.getAndSet(true)) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(DegradeTask.TAG, "[onFinish]", DegradeTask.this.rc.seqNum, "code", Integer.valueOf(i), "msg", str);
                        }
                        DegradeTask.this.rc.cancelTimeoutTask();
                        requestStatistic.isDone.set(true);
                        if (DegradeTask.this.rc.callback != null) {
                            DegradeTask.this.rc.callback.onFinish(new DefaultFinishEvent(i, str, DegradeTask.this.request));
                        }
                    }
                }
            });
        }
    }
}
