package com.alibaba.aliweex.interceptor.mtop;

import android.taobao.windvane.extra.config.TBConfigManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.interceptor.IWeexAnalyzerInspector;
import com.alibaba.aliweex.interceptor.InspectRequest;
import com.alibaba.aliweex.interceptor.InspectResponse;
import com.alibaba.aliweex.interceptor.NetworkEventReporterProxy;
import com.alibaba.aliweex.interceptor.TrackerManager;
import com.alibaba.aliweex.interceptor.WeexAnalyzerInspectorImpl;
import com.alibaba.aliweex.interceptor.utils.RequestBodyUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopTracker {
    private static final String TAG = "MtopTracker";
    private static boolean enabled = true;
    /* access modifiers changed from: private */
    public NetworkEventReporterProxy mEventReporter;
    private IWeexAnalyzerInspector mNetworkInspector;
    private final int mRequestId = TrackerManager.nextRequestId();
    @Nullable
    private String mRequestIdString;
    /* access modifiers changed from: private */
    public RequestBodyUtil requestBodyUtil;
    /* access modifiers changed from: private */
    public String url;

    private MtopTracker() {
        if (WXEnvironment.isApkDebugable()) {
            this.mEventReporter = NetworkEventReporterProxy.getInstance();
            this.mNetworkInspector = WeexAnalyzerInspectorImpl.createDefault();
            WXLogUtils.d(TAG, "Create new instance " + toString());
        }
    }

    public static MtopTracker newInstance() {
        return new MtopTracker();
    }

    public static void setEnabled(boolean z) {
        enabled = z;
    }

    public void preRequest(@NonNull final RemoteBusiness remoteBusiness) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    WXLogUtils.d(MtopTracker.TAG, "preRequest -> " + remoteBusiness.request.getApiName());
                    RequestBodyUtil unused = MtopTracker.this.requestBodyUtil = new RequestBodyUtil(MtopTracker.this.mEventReporter, MtopTracker.this.getRequestId());
                    InspectRequest inspectRequest = new InspectRequest();
                    MtopRequest mtopRequest = remoteBusiness.request;
                    inspectRequest.addHeader("api-name", mtopRequest.getApiName());
                    inspectRequest.addHeader("api-version", mtopRequest.getVersion());
                    inspectRequest.addHeader("api-key", mtopRequest.getKey());
                    inspectRequest.addHeader("need-ecode", mtopRequest.isNeedEcode() + "");
                    inspectRequest.addHeader("need-session", mtopRequest.isNeedSession() + "");
                    inspectRequest.addHeader("legal-request", mtopRequest.isLegalRequest() + "");
                    for (Map.Entry next : mtopRequest.dataParams.entrySet()) {
                        inspectRequest.addHeader((String) next.getKey(), (String) next.getValue());
                    }
                    for (Map.Entry next2 : remoteBusiness.mtopProp.getRequestHeaders().entrySet()) {
                        inspectRequest.addHeader((String) next2.getKey(), (String) next2.getValue());
                    }
                    if (inspectRequest.firstHeaderValue("Content-Type") == null) {
                        inspectRequest.addHeader("Content-Type", "application/json");
                    }
                    inspectRequest.setRequestId(MtopTracker.this.getRequestId());
                    inspectRequest.setFriendlyName("MTOP");
                    inspectRequest.setUrl(remoteBusiness.request.getApiName() + ":" + remoteBusiness.request.getVersion());
                    byte[] bytes = remoteBusiness.request.getData().getBytes();
                    if (bytes != null) {
                        try {
                            OutputStream createBodySink = MtopTracker.this.requestBodyUtil.createBodySink(inspectRequest.contentEncoding());
                            createBodySink.write(bytes);
                            createBodySink.close();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                        inspectRequest.setBody(MtopTracker.this.requestBodyUtil.getDisplayBody());
                    }
                    inspectRequest.setMethod(remoteBusiness.mtopProp.getMethod().getMethod());
                    MtopTracker.this.mEventReporter.requestWillBeSent(inspectRequest);
                    String unused2 = MtopTracker.this.url = (String) inspectRequest.getData().get("url");
                    MtopTracker.this.mEventReporter.dataSent(MtopTracker.this.getRequestId(), inspectRequest.contentLength(), 0);
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mNetworkInspector != null && this.mNetworkInspector.isEnabled()) {
            try {
                this.mNetworkInspector.onRequest("mtop", new IWeexAnalyzerInspector.InspectorRequest(remoteBusiness.request.getApiName(), remoteBusiness.mtopProp.getMethod().getMethod(), remoteBusiness.mtopProp.getRequestHeaders()));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void onResponse(final MtopResponse mtopResponse) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    WXLogUtils.d(MtopTracker.TAG, "onResponse -> " + mtopResponse.getApi());
                    if (MtopTracker.this.requestBodyUtil.hasBody()) {
                        MtopTracker.this.requestBodyUtil.reportDataSent();
                    }
                    InspectResponse inspectResponse = new InspectResponse();
                    inspectResponse.setRequestId(MtopTracker.this.getRequestId());
                    inspectResponse.setUrl(MtopTracker.this.url);
                    inspectResponse.setStatusCode(mtopResponse.getResponseCode());
                    inspectResponse.setReasonPhrase(mtopResponse.getRetCode());
                    inspectResponse.setFromDiskCache(mtopResponse.getSource() != MtopResponse.ResponseSource.NETWORK_REQUEST);
                    Map<String, List<String>> headerFields = mtopResponse.getHeaderFields();
                    if (headerFields != null) {
                        for (Map.Entry next : headerFields.entrySet()) {
                            if (next.getValue() != null) {
                                for (String addHeader : (List) next.getValue()) {
                                    inspectResponse.addHeader((String) next.getKey(), addHeader);
                                }
                            } else {
                                inspectResponse.addHeader((String) next.getKey(), (String) null);
                            }
                        }
                        if (inspectResponse.firstHeaderValue("Content-Type") == null) {
                            inspectResponse.addHeader("Content-Type", "application/json");
                        }
                        MtopTracker.this.mEventReporter.responseHeadersReceived(inspectResponse);
                        MtopResponse unused = MtopTracker.this.interceptResponse(mtopResponse, inspectResponse);
                    }
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mNetworkInspector != null && this.mNetworkInspector.isEnabled()) {
            try {
                this.mNetworkInspector.onResponse("mtop", new IWeexAnalyzerInspector.InspectorResponse(mtopResponse.getApi(), new String(mtopResponse.getBytedata()), mtopResponse.getResponseCode(), mtopResponse.getHeaderFields()));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void onFailed(String str, final String str2) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    WXLogUtils.d(MtopTracker.TAG, "onFailed -> " + str2);
                    MtopTracker.this.mEventReporter.httpExchangeFailed(MtopTracker.this.getRequestId(), str2);
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mNetworkInspector != null && this.mNetworkInspector.isEnabled()) {
            try {
                this.mNetworkInspector.onResponse("mtop", new IWeexAnalyzerInspector.InspectorResponse(str, str2, 200, (Map<String, List<String>>) null));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void preRequest(final JSONObject jSONObject) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    AnonymousClass1 r0 = new InspectRequest() {
                        public String contentType() {
                            String firstHeaderValue = firstHeaderValue("Content-Type");
                            return firstHeaderValue == null ? "application/json" : firstHeaderValue;
                        }
                    };
                    r0.setRequestId(MtopTracker.this.getRequestId());
                    for (String next : jSONObject.keySet()) {
                        Object obj = jSONObject.get(next);
                        if (!"param".equals(next)) {
                            r0.addHeader(next, String.valueOf(obj));
                        } else {
                            Object obj2 = jSONObject.get("param");
                            if (obj2 != null && (obj2 instanceof JSONObject)) {
                                JSONObject jSONObject = (JSONObject) obj2;
                                for (String next2 : jSONObject.keySet()) {
                                    r0.addHeader(next2, String.valueOf(jSONObject.get(next2)));
                                }
                            }
                        }
                    }
                    r0.addHeader("Content-Type", "application/json");
                    r0.setUrl(jSONObject.getString("api"));
                    r0.setFriendlyName(TBConfigManager.WINDVANE_COMMMON_CONFIG);
                    r0.setMethod(TextUtils.isEmpty(jSONObject.getString("type")) ? "GET" : jSONObject.getString("type").toUpperCase());
                    MtopTracker.this.mEventReporter.requestWillBeSent(r0);
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mNetworkInspector != null && this.mNetworkInspector.isEnabled()) {
            try {
                String upperCase = TextUtils.isEmpty(jSONObject.getString("type")) ? "GET" : jSONObject.getString("type").toUpperCase();
                JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                String string = jSONObject.getString("api");
                IWeexAnalyzerInspector iWeexAnalyzerInspector = this.mNetworkInspector;
                if (jSONObject2 == null || jSONObject2.isEmpty()) {
                    jSONObject2 = null;
                }
                iWeexAnalyzerInspector.onRequest("mtop", new IWeexAnalyzerInspector.InspectorRequest(string, upperCase, jSONObject2));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void onResponse(final String str) {
        if (canReport()) {
            this.mEventReporter.execAsync(new Runnable() {
                public void run() {
                    InspectResponse inspectResponse = new InspectResponse();
                    inspectResponse.setRequestId(MtopTracker.this.getRequestId());
                    JSONObject parseObject = JSON.parseObject(str);
                    inspectResponse.addHeader("Content-Type", "application/json");
                    for (String next : parseObject.keySet()) {
                        if (!"data".equals(next)) {
                            inspectResponse.addHeader(next, parseObject.getString(next));
                        }
                    }
                    inspectResponse.setUrl(parseObject.getString("api"));
                    inspectResponse.setStatusCode(parseObject.getIntValue("code"));
                    inspectResponse.setReasonPhrase(parseObject.getString("ret"));
                    inspectResponse.setFromDiskCache(!"0".equals(parseObject.getString("isFromCache")));
                    MtopTracker.this.mEventReporter.responseHeadersReceived(inspectResponse);
                    String unused = MtopTracker.this.interceptResponse(JSONObject.parseObject(str).getString("data"));
                }
            });
        }
        if (WXEnvironment.isApkDebugable() && enabled && this.mNetworkInspector != null && this.mNetworkInspector.isEnabled()) {
            try {
                JSONObject parseObject = JSON.parseObject(str);
                this.mNetworkInspector.onResponse("mtop", new IWeexAnalyzerInspector.InspectorResponse(parseObject.getString("api"), str, parseObject.getIntValue("code"), (Map<String, List<String>>) null));
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public String interceptResponse(String str) {
        if (canReport()) {
            this.mEventReporter.interpretResponseStream(getRequestId(), "application/json", (String) null, new ByteArrayInputStream(str.getBytes()), false);
            this.mEventReporter.responseReadFinished(getRequestId());
        }
        return str;
    }

    /* access modifiers changed from: private */
    public MtopResponse interceptResponse(MtopResponse mtopResponse, InspectResponse inspectResponse) {
        if (!(!canReport() || mtopResponse == null || mtopResponse.getBytedata() == null)) {
            this.mEventReporter.interpretResponseStream(getRequestId(), inspectResponse.contentType(), inspectResponse.contentEncoding(), new ByteArrayInputStream(mtopResponse.getBytedata()), false);
            this.mEventReporter.responseReadFinished(getRequestId());
        }
        return mtopResponse;
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
}
