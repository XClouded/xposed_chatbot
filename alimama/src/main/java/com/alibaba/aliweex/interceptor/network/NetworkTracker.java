package com.alibaba.aliweex.interceptor.network;

import android.os.SystemClock;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import anetwork.channel.Header;
import anetwork.channel.NetworkEvent;
import anetwork.channel.Param;
import anetwork.channel.Request;
import com.alibaba.aliweex.interceptor.IWeexAnalyzerInspector;
import com.alibaba.aliweex.interceptor.InspectRequest;
import com.alibaba.aliweex.interceptor.InspectResponse;
import com.alibaba.aliweex.interceptor.NetworkEventReporterProxy;
import com.alibaba.aliweex.interceptor.TrackerManager;
import com.alibaba.aliweex.interceptor.WeexAnalyzerInspectorImpl;
import com.alibaba.aliweex.interceptor.utils.RequestBodyUtil;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.http.Status;
import com.taobao.weex.utils.WXLogUtils;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class NetworkTracker {
    private static final String TAG = "NetworkTracker";
    private static boolean enabled = true;
    /* access modifiers changed from: private */
    public boolean hasHeaderReported = false;
    private IWeexAnalyzerInspector mAnalyzerInspector;
    /* access modifiers changed from: private */
    public NetworkEventReporterProxy mEventReporter;
    private final int mRequestId;
    @Nullable
    private String mRequestIdString;
    /* access modifiers changed from: private */
    public InspectResponse mResponse;
    /* access modifiers changed from: private */
    public String mUrl;
    /* access modifiers changed from: private */
    public double requestTime;

    private NetworkTracker() {
        double elapsedRealtime = (double) SystemClock.elapsedRealtime();
        Double.isNaN(elapsedRealtime);
        this.requestTime = elapsedRealtime / 1000.0d;
        this.mRequestId = TrackerManager.nextRequestId();
        if (WXEnvironment.isApkDebugable()) {
            this.mEventReporter = NetworkEventReporterProxy.getInstance();
            this.mAnalyzerInspector = WeexAnalyzerInspectorImpl.createDefault();
            WXLogUtils.d(TAG, "Create new instance " + toString());
        }
    }

    public static NetworkTracker newInstance() {
        return new NetworkTracker();
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public void preRequest(final Request request) {
        try {
            if (canReport()) {
                this.mEventReporter.execAsync(new Runnable() {
                    public void run() {
                        OutputStream createBodySink;
                        WXLogUtils.d(NetworkTracker.TAG, NetworkTracker.this.getRequestId() + " preRequest -> " + request.getURL());
                        InspectRequest inspectRequest = new InspectRequest();
                        for (Header next : request.getHeaders()) {
                            inspectRequest.addHeader(next.getName(), next.getValue());
                        }
                        if (!(request.getBodyEntry() == null || request.getBodyEntry().getContentType() == null)) {
                            inspectRequest.addHeader("Content-Type", request.getBodyEntry().getContentType());
                        }
                        if (request.getParams() != null) {
                            for (Param next2 : request.getParams()) {
                                inspectRequest.addHeader(next2.getKey(), next2.getValue());
                            }
                        }
                        inspectRequest.addHeader(WVConstants.CHARSET, request.getCharset());
                        inspectRequest.addHeader("connectTimeout", String.valueOf(request.getConnectTimeout()));
                        inspectRequest.addHeader("readTimeout", String.valueOf(request.getReadTimeout()));
                        inspectRequest.addHeader("retryTime", String.valueOf(request.getRetryTime()));
                        String unused = NetworkTracker.this.mUrl = request.getURL().toString();
                        inspectRequest.setUrl(NetworkTracker.this.mUrl);
                        inspectRequest.setRequestId(NetworkTracker.this.getRequestId());
                        inspectRequest.setFriendlyName("ANet");
                        inspectRequest.setMethod(TextUtils.isEmpty(request.getMethod()) ? "GET" : request.getMethod());
                        if (request.getBodyEntry() != null) {
                            try {
                                RequestBodyUtil requestBodyUtil = new RequestBodyUtil(NetworkTracker.this.mEventReporter, NetworkTracker.this.getRequestId());
                                createBodySink = requestBodyUtil.createBodySink(inspectRequest.contentType());
                                request.getBodyEntry().writeTo(createBodySink);
                                createBodySink.close();
                                inspectRequest.setBody(requestBodyUtil.getDisplayBody());
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                        NetworkTracker.this.mEventReporter.requestWillBeSent(inspectRequest);
                        NetworkTracker.this.mEventReporter.dataSent(NetworkTracker.this.getRequestId(), inspectRequest.contentLength(), 0);
                    }
                });
            }
            if (WXEnvironment.isApkDebugable() && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled()) {
                this.mUrl = request.getUrlString();
                HashMap hashMap = new HashMap();
                hashMap.put(WVConstants.CHARSET, request.getCharset());
                hashMap.put("connectTimeout", String.valueOf(request.getConnectTimeout()));
                hashMap.put("readTimeout", String.valueOf(request.getReadTimeout()));
                hashMap.put("retryTime", String.valueOf(request.getRetryTime()));
                if (request.getHeaders() != null) {
                    for (Header next : request.getHeaders()) {
                        hashMap.put(next.getName(), next.getValue());
                    }
                }
                this.mAnalyzerInspector.onRequest("http", new IWeexAnalyzerInspector.InspectorRequest(TextUtils.isEmpty(this.mUrl) ? "unknown" : this.mUrl, "GET", hashMap));
            }
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
        } catch (Throwable th) {
            reportException("Exception on preRequest()", th);
        }
    }

    public void onResponseCode(final int i, final Map<String, List<String>> map) {
        try {
            if (canReport() && !this.hasHeaderReported) {
                this.mEventReporter.execAsync(new Runnable() {
                    public void run() {
                        WXLogUtils.d(NetworkTracker.TAG, NetworkTracker.this.getRequestId() + " onResponseCode -> " + i + AVFSCacheConstants.COMMA_SEP + map.toString());
                        InspectResponse unused = NetworkTracker.this.mResponse = new InspectResponse();
                        NetworkTracker.this.mResponse.setStatusCode(i);
                        NetworkTracker.this.mResponse.setRequestId(NetworkTracker.this.getRequestId());
                        NetworkTracker.this.mResponse.setUrl(NetworkTracker.this.mUrl);
                        NetworkTracker.this.mResponse.setReasonPhrase(Status.getStatusText(String.valueOf(i)));
                        String str = "";
                        for (Map.Entry entry : map.entrySet()) {
                            String str2 = (String) entry.getKey();
                            String obj = ((List) entry.getValue()).toString();
                            if (str2 != null) {
                                NetworkTracker.this.mResponse.addHeader(str2, obj);
                            } else {
                                str = str + obj + ";";
                            }
                        }
                        NetworkTracker.this.mResponse.addHeader("NULL", str);
                        NetworkTracker.this.mEventReporter.responseHeadersReceived(NetworkTracker.this.mResponse);
                        boolean unused2 = NetworkTracker.this.hasHeaderReported = true;
                    }
                });
            }
            if (WXEnvironment.isApkDebugable() && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled()) {
                this.mResponse = new InspectResponse();
                this.mResponse.setStatusCode(i);
                this.mResponse.setUrl(this.mUrl);
                for (Map.Entry next : map.entrySet()) {
                    String str = (String) next.getKey();
                    String obj = ((List) next.getValue()).toString();
                    if (str != null) {
                        this.mResponse.addHeader(str, obj);
                    }
                }
            }
        } catch (Throwable th) {
            reportException("Exception on onResponseCode()", th);
        }
    }

    public void onDataReceived(final NetworkEvent.ProgressEvent progressEvent) {
        try {
            if (canReport()) {
                this.mEventReporter.execAsync(new Runnable() {
                    public void run() {
                        int length = progressEvent.getBytedata() == null ? 0 : progressEvent.getBytedata().length;
                        WXLogUtils.d(NetworkTracker.TAG, NetworkTracker.this.getRequestId() + " onDataReceived -> " + length + " bytes");
                        NetworkTracker.this.mEventReporter.dataReceived(NetworkTracker.this.getRequestId(), length, 0);
                    }
                });
            }
        } catch (Throwable th) {
            reportException("Exception on onDataReceived()", th);
        }
    }

    public void onFinished(final byte[] bArr) {
        try {
            if (canReport()) {
                this.mEventReporter.execAsync(new Runnable() {
                    public void run() {
                        WXLogUtils.d(NetworkTracker.TAG, NetworkTracker.this.getRequestId() + " onFinished -> " + bArr.length + " bytes");
                        NetworkTracker.this.interceptResponse(bArr, NetworkTracker.this.mResponse);
                    }
                });
            }
            if (WXEnvironment.isApkDebugable() && this.mAnalyzerInspector != null && this.mAnalyzerInspector.isEnabled() && this.mResponse != null && bArr != null) {
                IWeexAnalyzerInspector iWeexAnalyzerInspector = this.mAnalyzerInspector;
                String str = TextUtils.isEmpty((CharSequence) this.mResponse.getData().get("url")) ? "unknown" : (String) this.mResponse.getData().get("url");
                String str2 = new String(bArr);
                int intValue = ((Integer) this.mResponse.getData().get("statusCode")).intValue();
                iWeexAnalyzerInspector.onResponse("http", new IWeexAnalyzerInspector.InspectorResponse(str, str2, intValue, Collections.singletonMap("Content-Length", Collections.singletonList(bArr.length + ""))));
            }
        } catch (Throwable th) {
            reportException("Exception on onFinished()", th);
        }
    }

    public void onFailed(String str) {
        try {
            if (canReport()) {
                WXLogUtils.d(TAG, getRequestId() + " onFailed: " + str);
                this.mEventReporter.httpExchangeFailed(getRequestId(), str);
            }
        } catch (Throwable th) {
            reportException("Exception on onFailed()", th);
        }
    }

    /* access modifiers changed from: private */
    public void interceptResponse(final byte[] bArr, final InspectResponse inspectResponse) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    if (bArr != null) {
                        NetworkTracker.this.mEventReporter.interpretResponseStream(NetworkTracker.this.getRequestId(), inspectResponse.contentType(), inspectResponse.contentEncoding(), new ByteArrayInputStream(bArr), false);
                    }
                    NetworkTracker.this.mEventReporter.responseReadFinished(NetworkTracker.this.getRequestId());
                }
            });
        }
    }

    private boolean canReport() {
        return enabled && WXEnvironment.isApkDebugable() && this.mEventReporter != null && this.mEventReporter.isEnabled();
    }

    /* access modifiers changed from: private */
    public String getRequestId() {
        if (this.mRequestIdString == null) {
            this.mRequestIdString = String.valueOf(this.mRequestId);
        }
        return this.mRequestIdString;
    }

    public void onStatisticDataReceived(final Map<String, Object> map) {
        if (this.mResponse != null && !map.isEmpty()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    map.put("requestTime", Double.valueOf(NetworkTracker.this.requestTime));
                    NetworkTracker.this.mResponse.setTiming(map);
                }
            });
        }
    }

    private void reportException(String str, Throwable th) {
        try {
            enabled = false;
            WXLogUtils.w("Disable NetworkTracker");
            IWXUserTrackAdapter iWXUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
            if (iWXUserTrackAdapter != null && WXEnvironment.getApplication() != null) {
                WXPerformance wXPerformance = new WXPerformance("useless");
                wXPerformance.args = "message: " + str + ";" + "requestId: " + this.mRequestId + ";" + "isApkDebugable: " + WXEnvironment.isApkDebugable() + ";" + "canReport: " + canReport() + ";" + "exception: " + WXLogUtils.getStackTrace(th);
                wXPerformance.errCode = WXErrorCode.WX_ERR_INVOKE_NATIVE.getErrorCode();
                wXPerformance.appendErrMsg(WXErrorCode.WX_ERR_INVOKE_NATIVE.getErrorMsg());
                iWXUserTrackAdapter.commit(WXEnvironment.getApplication(), (String) null, IWXUserTrackAdapter.STREAM_MODULE, wXPerformance, (Map<String, Serializable>) null);
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }
}
