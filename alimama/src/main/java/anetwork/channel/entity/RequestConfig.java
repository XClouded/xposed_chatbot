package anetwork.channel.entity;

import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.Utils;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.util.RequestConstant;
import anetwork.channel.util.SeqGen;
import java.util.HashMap;
import java.util.Map;

public class RequestConfig {
    private static final String TAG = "anet.RequestConfig";
    private Request awcnRequest = null;
    public final int connectTimeout;
    public int currentRetryTimes = 0;
    private final boolean isSync;
    private int mRedirectTimes = 0;
    private int maxRetryTime = 0;
    public final int readTimeout;
    private ParcelableRequest request;
    public final int requestType;
    public RequestStatistic rs;
    public final String seqNo;

    public RequestConfig(ParcelableRequest parcelableRequest, int i, boolean z) {
        if (parcelableRequest != null) {
            this.request = parcelableRequest;
            this.requestType = i;
            this.isSync = z;
            this.seqNo = SeqGen.createSeqNo(parcelableRequest.seqNo, this.requestType == 0 ? "HTTP" : "DGRD");
            this.connectTimeout = parcelableRequest.connectTimeout <= 0 ? (int) (Utils.getNetworkTimeFactor() * 12000.0f) : parcelableRequest.connectTimeout;
            this.readTimeout = parcelableRequest.readTimeout <= 0 ? (int) (Utils.getNetworkTimeFactor() * 12000.0f) : parcelableRequest.readTimeout;
            this.maxRetryTime = (parcelableRequest.retryTime < 0 || parcelableRequest.retryTime > 3) ? 2 : parcelableRequest.retryTime;
            HttpUrl initHttpUrl = initHttpUrl();
            this.rs = new RequestStatistic(initHttpUrl.host(), String.valueOf(parcelableRequest.bizId));
            this.rs.url = initHttpUrl.simpleUrlString();
            this.awcnRequest = buildRequest(initHttpUrl);
            return;
        }
        throw new IllegalArgumentException("request is null");
    }

    public Request getAwcnRequest() {
        return this.awcnRequest;
    }

    public void setAwcnRequest(Request request2) {
        this.awcnRequest = request2;
    }

    private HttpUrl initHttpUrl() {
        HttpUrl parse = HttpUrl.parse(this.request.url);
        if (parse != null) {
            if (!NetworkConfigCenter.isSSLEnabled()) {
                parse.downgradeSchemeAndLock();
            } else if ("false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_SCHEME_REPLACE))) {
                parse.lockScheme();
            }
            return parse;
        }
        throw new IllegalArgumentException("url is invalid. url=" + this.request.url);
    }

    private Request buildRequest(HttpUrl httpUrl) {
        Request.Builder requestStatistic = new Request.Builder().setUrl(httpUrl).setMethod(this.request.method).setBody(this.request.bodyEntry).setReadTimeout(this.readTimeout).setConnectTimeout(this.connectTimeout).setRedirectEnable(this.request.allowRedirect).setRedirectTimes(this.mRedirectTimes).setBizId(this.request.bizId).setSeq(this.seqNo).setRequestStatistic(this.rs);
        requestStatistic.setParams(this.request.params);
        if (this.request.charset != null) {
            requestStatistic.setCharset(this.request.charset);
        }
        requestStatistic.setHeaders(initHeaders(httpUrl));
        return requestStatistic.build();
    }

    public int getWaitTimeout() {
        return this.readTimeout * (this.maxRetryTime + 1);
    }

    public boolean isSyncRequest() {
        return this.isSync;
    }

    public String getRequestProperty(String str) {
        return this.request.getExtProperty(str);
    }

    public boolean isAllowRetry() {
        return this.currentRetryTimes < this.maxRetryTime;
    }

    public boolean isHttpSessionEnable() {
        return NetworkConfigCenter.isHttpSessionEnable() && !"false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_HTTP_DNS)) && (NetworkConfigCenter.isAllowHttpIpRetry() || this.currentRetryTimes == 0);
    }

    public HttpUrl getHttpUrl() {
        return this.awcnRequest.getHttpUrl();
    }

    public String getUrlString() {
        return this.awcnRequest.getUrlString();
    }

    public Map<String, String> getHeaders() {
        return this.awcnRequest.getHeaders();
    }

    private Map<String, String> initHeaders(HttpUrl httpUrl) {
        String host = httpUrl.host();
        boolean z = !anet.channel.strategy.utils.Utils.isIPV4Address(host);
        if (host.length() > 2 && host.charAt(0) == '[' && host.charAt(host.length() - 1) == ']' && anet.channel.strategy.utils.Utils.isIPV6Address(host.substring(1, host.length() - 1))) {
            z = false;
        }
        HashMap hashMap = new HashMap();
        if (this.request.headers != null) {
            for (Map.Entry next : this.request.headers.entrySet()) {
                String str = (String) next.getKey();
                if (!HttpConstant.HOST.equalsIgnoreCase(str) && !":host".equalsIgnoreCase(str)) {
                    boolean equalsIgnoreCase = "true".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.KEEP_CUSTOM_COOKIE));
                    if (!HttpConstant.COOKIE.equalsIgnoreCase(str) || equalsIgnoreCase) {
                        hashMap.put(str, next.getValue());
                    }
                } else if (!z) {
                    hashMap.put(HttpConstant.HOST, next.getValue());
                }
            }
        }
        return hashMap;
    }

    public boolean isRequestCookieEnabled() {
        return !"false".equalsIgnoreCase(this.request.getExtProperty(RequestConstant.ENABLE_COOKIE));
    }

    public boolean shouldCheckContentLength() {
        return "true".equals(this.request.getExtProperty(RequestConstant.CHECK_CONTENT_LENGTH));
    }

    public void retryRequest() {
        this.currentRetryTimes++;
        this.rs.retryTimes = this.currentRetryTimes;
    }

    public void redirectToUrl(HttpUrl httpUrl) {
        ALog.i(TAG, "redirect", this.seqNo, "to url", httpUrl.toString());
        this.mRedirectTimes++;
        this.rs.url = httpUrl.simpleUrlString();
        this.awcnRequest = buildRequest(httpUrl);
    }
}
