package mtopsdk.mtop.network;

import androidx.annotation.NonNull;
import java.io.IOException;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopHeaderEvent;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.network.Call;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.Response;

public class NetworkCallbackAdapter implements NetworkCallback {
    private static final String TAG = "mtopsdk.NetworkCallbackAdapter";
    FilterManager filterManager;
    public MtopCallback.MtopFinishListener finishListener;
    public MtopCallback.MtopHeaderListener headerListener;
    final MtopContext mtopContext;

    public NetworkCallbackAdapter(@NonNull MtopContext mtopContext2) {
        this.mtopContext = mtopContext2;
        if (mtopContext2 != null) {
            if (mtopContext2.mtopInstance != null) {
                this.filterManager = mtopContext2.mtopInstance.getMtopConfig().filterManager;
            }
            MtopListener mtopListener = mtopContext2.mtopListener;
            if (mtopListener instanceof MtopCallback.MtopHeaderListener) {
                this.headerListener = (MtopCallback.MtopHeaderListener) mtopListener;
            }
            if (mtopListener instanceof MtopCallback.MtopFinishListener) {
                this.finishListener = (MtopCallback.MtopFinishListener) mtopListener;
            }
        }
    }

    public void onHeader(Response response, Object obj) {
        try {
            if (this.headerListener != null) {
                MtopHeaderEvent mtopHeaderEvent = new MtopHeaderEvent(response.code, response.headers);
                mtopHeaderEvent.seqNo = this.mtopContext.seqNo;
                this.headerListener.onHeader(mtopHeaderEvent, obj);
            }
        } catch (Throwable th) {
            TBSdkLog.e(TAG, this.mtopContext.seqNo, "onHeader failed.", th);
        }
    }

    public void onFinish(Response response, Object obj) {
        onFinish(response, obj, false);
    }

    public void onFinish(final Response response, final Object obj, final boolean z) {
        this.mtopContext.stats.netSendEndTime = this.mtopContext.stats.currentTimeMillis();
        this.mtopContext.property.reqContext = obj;
        FilterUtils.submitCallbackTask(this.mtopContext.property.handler, new Runnable() {
            public void run() {
                MtopResponse mtopResponse;
                try {
                    if (z) {
                        NetworkCallbackAdapter.this.onHeader(response, obj);
                    }
                    NetworkCallbackAdapter.this.mtopContext.stats.startCallbackTime = NetworkCallbackAdapter.this.mtopContext.stats.currentTimeMillis();
                    NetworkCallbackAdapter.this.mtopContext.stats.bizRspProcessStart = System.currentTimeMillis();
                    NetworkCallbackAdapter.this.mtopContext.stats.netStats = response.stat;
                    NetworkCallbackAdapter.this.mtopContext.networkResponse = response;
                    mtopResponse = new MtopResponse(NetworkCallbackAdapter.this.mtopContext.mtopRequest.getApiName(), NetworkCallbackAdapter.this.mtopContext.mtopRequest.getVersion(), (String) null, (String) null);
                    mtopResponse.setResponseCode(response.code);
                    mtopResponse.setHeaderFields(response.headers);
                    mtopResponse.setMtopStat(NetworkCallbackAdapter.this.mtopContext.stats);
                    if (response.body != null) {
                        mtopResponse.setBytedata(response.body.getBytes());
                    }
                } catch (IOException e) {
                    TBSdkLog.e(NetworkCallbackAdapter.TAG, NetworkCallbackAdapter.this.mtopContext.seqNo, "call getBytes of response.body() error.", e);
                } catch (Throwable th) {
                    TBSdkLog.e(NetworkCallbackAdapter.TAG, NetworkCallbackAdapter.this.mtopContext.seqNo, "onFinish failed.", th);
                    return;
                }
                NetworkCallbackAdapter.this.mtopContext.mtopResponse = mtopResponse;
                NetworkCallbackAdapter.this.filterManager.callback((String) null, NetworkCallbackAdapter.this.mtopContext);
            }
        }, this.mtopContext.seqNo.hashCode());
    }

    public void onFailure(Call call, Exception exc) {
        Response build = new Response.Builder().request(call.request()).code(-7).message(exc.getMessage()).build();
        onFinish(build, build.request.reqContext);
    }

    public void onResponse(Call call, Response response) {
        onFinish(response, response.request.reqContext, true);
    }

    public void onCancel(Call call) {
        Response build = new Response.Builder().request(call.request()).code(-8).build();
        onFinish(build, build.request.reqContext);
    }
}
