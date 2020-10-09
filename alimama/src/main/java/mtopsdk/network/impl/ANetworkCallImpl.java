package mtopsdk.network.impl;

import android.content.Context;
import android.os.Handler;
import anetwork.channel.Network;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.http.HttpNetwork;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MockResponse;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.AbstractCallImpl;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.NetworkStats;
import mtopsdk.network.domain.Request;
import mtopsdk.network.domain.Response;
import mtopsdk.network.util.ANetworkConverter;

public class ANetworkCallImpl extends AbstractCallImpl {
    private static final String TAG = "mtopsdk.ANetworkCallImpl";
    Network mDegradalbeNetwork;
    Network mHttpNetwork;
    Network mNetwork;

    public boolean isNoNetworkError(int i) {
        return i == -200;
    }

    public ANetworkCallImpl(Request request, Context context) {
        super(request, context);
        if (!SwitchConfig.getInstance().isGlobalSpdySwitchOpen()) {
            if (this.mHttpNetwork == null) {
                this.mHttpNetwork = new HttpNetwork(this.mContext);
            }
            this.mNetwork = this.mHttpNetwork;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, this.seqNo, "mNetwork=HttpNetwork in ANetworkCallImpl");
                return;
            }
            return;
        }
        this.mDegradalbeNetwork = new DegradableNetwork(this.mContext);
        this.mNetwork = this.mDegradalbeNetwork;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, "mNetwork=DegradableNetwork in ANetworkCallImpl");
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.util.Map<java.lang.String, java.util.List<java.lang.String>>} */
    /* JADX WARNING: type inference failed for: r4v1, types: [java.util.Map] */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x006a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public mtopsdk.network.domain.Response execute() throws java.lang.Exception {
        /*
            r10 = this;
            mtopsdk.network.domain.Request r1 = r10.request()
            boolean r0 = isDebugApk
            r2 = 0
            r3 = 0
            if (r0 == 0) goto L_0x003f
            boolean r0 = isOpenMock
            if (r0 == 0) goto L_0x003f
            java.lang.String r0 = r1.api
            mtopsdk.mtop.domain.MockResponse r0 = r10.getMockResponse(r0)
            if (r0 == 0) goto L_0x003d
            int r3 = r0.statusCode
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r4 = r0.headers
            byte[] r5 = r0.byteData
            mtopsdk.common.util.TBSdkLog$LogEnable r6 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r6 = mtopsdk.common.util.TBSdkLog.isLogEnable(r6)
            if (r6 == 0) goto L_0x0042
            java.lang.String r6 = "mtopsdk.ANetworkCallImpl"
            java.lang.String r7 = r10.seqNo
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "[execute]get MockResponse succeed.mockResponse="
            r8.append(r9)
            r8.append(r0)
            java.lang.String r8 = r8.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r6, (java.lang.String) r7, (java.lang.String) r8)
            goto L_0x0042
        L_0x003d:
            r4 = r2
            goto L_0x0041
        L_0x003f:
            r0 = r2
            r4 = r0
        L_0x0041:
            r5 = r4
        L_0x0042:
            if (r0 != 0) goto L_0x006a
            anetwork.channel.Network r0 = r10.mNetwork
            anetwork.channel.Request r2 = mtopsdk.network.util.ANetworkConverter.convertRequest(r1)
            java.lang.Object r3 = r1.reqContext
            anetwork.channel.Response r0 = r0.syncSend(r2, r3)
            int r2 = r0.getStatusCode()
            java.lang.String r3 = r0.getDesc()
            java.util.Map r4 = r0.getConnHeadFields()
            byte[] r5 = r0.getBytedata()
            anetwork.channel.statist.StatisticData r0 = r0.getStatisticData()
            mtopsdk.network.domain.NetworkStats r0 = mtopsdk.network.util.ANetworkConverter.convertNetworkStats(r0)
            r6 = r0
            goto L_0x006d
        L_0x006a:
            r6 = r2
            r2 = r3
            r3 = r6
        L_0x006d:
            r0 = r10
            mtopsdk.network.domain.Response r0 = r0.buildResponse(r1, r2, r3, r4, r5, r6)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.network.impl.ANetworkCallImpl.execute():mtopsdk.network.domain.Response");
    }

    public void enqueue(final NetworkCallback networkCallback) {
        MockResponse mockResponse;
        Request request = request();
        if (!isDebugApk || !isOpenMock) {
            mockResponse = null;
        } else {
            mockResponse = getMockResponse(request.api);
            if (mockResponse != null) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, this.seqNo, "[enqueue]get MockResponse succeed.mockResponse=" + mockResponse);
                }
                final Response buildResponse = buildResponse(request, mockResponse.statusCode, (String) null, mockResponse.headers, mockResponse.byteData, (NetworkStats) null);
                MtopSDKThreadPoolExecutorFactory.submitCallbackTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), new Runnable() {
                    public void run() {
                        try {
                            networkCallback.onResponse(ANetworkCallImpl.this, buildResponse);
                        } catch (Exception e) {
                            TBSdkLog.e(ANetworkCallImpl.TAG, ANetworkCallImpl.this.seqNo, "[enqueue]call NetworkCallback.onResponse error.", e);
                        }
                    }
                });
                return;
            }
        }
        if (mockResponse == null) {
            this.future = this.mNetwork.asyncSend(ANetworkConverter.convertRequest(request), request.reqContext, (Handler) null, new NetworkListenerAdapter(this, networkCallback, request.seqNo));
        }
    }
}
