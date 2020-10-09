package com.alipay.literpc.android.phone.mrpc.core;

import android.content.ContentResolver;
import android.content.Context;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import anet.channel.util.HttpConstant;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpWorker implements Callable<Response> {
    private static final String TAG = "HttpWorker";
    private static final HttpRequestRetryHandler sHttpRequestRetryHandler = new ZHttpRequestRetryHandler();
    private String etagCacheKey = null;
    private boolean hasEtagInResponse = false;
    private boolean hasIfNoneMatchInRequest = false;
    protected Context mContext;
    private CookieManager mCookieManager;
    private CookieStore mCookieStore = new BasicCookieStore();
    private HttpHost mHttpHost;
    protected HttpManager mHttpManager;
    private HttpUriRequest mHttpRequest;
    private HttpContext mLocalContext = new BasicHttpContext();
    private String mOperationType;
    private AbstractHttpEntity mPostDataEntity;
    protected HttpUrlRequest mRequest;
    private int mRetryTimes = 0;
    private URL mTargetUrl;
    String mUrl;

    /* access modifiers changed from: protected */
    public boolean willHandleOtherCode(int i, String str) {
        return i == 304;
    }

    public HttpWorker(HttpManager httpManager, HttpUrlRequest httpUrlRequest) {
        this.mHttpManager = httpManager;
        this.mContext = this.mHttpManager.mContext;
        this.mRequest = httpUrlRequest;
    }

    /* access modifiers changed from: protected */
    public URI getUri() throws URISyntaxException {
        String url = this.mRequest.getUrl();
        if (this.mUrl != null) {
            url = this.mUrl;
        }
        if (url != null) {
            return new URI(url);
        }
        throw new RuntimeException("url should not be null");
    }

    /* access modifiers changed from: protected */
    public AbstractHttpEntity getPostData() throws IOException {
        if (this.mPostDataEntity != null) {
            return this.mPostDataEntity;
        }
        byte[] reqData = this.mRequest.getReqData();
        String tag = this.mRequest.getTag("gzip");
        if (reqData != null) {
            if (TextUtils.equals(tag, "true")) {
                this.mPostDataEntity = AndroidHttpClient.getCompressedEntity(reqData, (ContentResolver) null);
            } else {
                this.mPostDataEntity = new ByteArrayEntity(reqData);
            }
            this.mPostDataEntity.setContentType(this.mRequest.getContentType());
        }
        return this.mPostDataEntity;
    }

    /* access modifiers changed from: protected */
    public ArrayList<Header> getHeaders() {
        return this.mRequest.getHeaders();
    }

    private HttpUriRequest getHttpUriRequest() throws Exception {
        if (this.mHttpRequest != null) {
            return this.mHttpRequest;
        }
        AbstractHttpEntity postData = getPostData();
        if (postData != null) {
            HttpPost httpPost = new HttpPost(getUri());
            httpPost.setEntity(postData);
            this.mHttpRequest = httpPost;
        } else {
            this.mHttpRequest = new HttpGet(getUri());
        }
        return this.mHttpRequest;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x03cc, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x03fc, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(3, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x03fd, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x03fe, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0405, code lost:
        if (getTransportCallback() != null) goto L_0x0407;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0407, code lost:
        getTransportCallback().onFailed(r11.mRequest, 6, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0421, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0451, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(6, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0452, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0453, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x045a, code lost:
        if (getTransportCallback() != null) goto L_0x045c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x045c, code lost:
        getTransportCallback().onFailed(r11.mRequest, 2, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0476, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x04a6, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(2, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x04a7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x04a8, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x04af, code lost:
        if (getTransportCallback() != null) goto L_0x04b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x04b1, code lost:
        getTransportCallback().onFailed(r11.mRequest, 2, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x04cb, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x04fb, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(2, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x04fc, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x0508, code lost:
        throw new java.lang.RuntimeException("Url parser error!", r0.getCause());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0509, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x050a, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0511, code lost:
        if (getTransportCallback() != null) goto L_0x0513;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x0513, code lost:
        getTransportCallback().onFailed(r11.mRequest, r0.getCode(), r0.getMsg());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x0524, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x053a, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0129, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x012a, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, "", r1);
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0138, code lost:
        if (getTransportCallback() != null) goto L_0x013a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x013a, code lost:
        getTransportCallback().onFailed(r11.mRequest, 0, r1 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x016e, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(0, r1 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x016f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0170, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0176, code lost:
        if (r11.mRetryTimes < 1) goto L_0x0178;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0178, code lost:
        r11.mRetryTimes++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0181, code lost:
        return call();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0182, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r1 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01b2, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(0, r1 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x01b3, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x01b4, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01bb, code lost:
        if (getTransportCallback() != null) goto L_0x01bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x01bd, code lost:
        getTransportCallback().onFailed(r11.mRequest, 6, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01d7, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0207, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(6, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0208, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0209, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0212, code lost:
        if (getTransportCallback() != null) goto L_0x0214;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0214, code lost:
        getTransportCallback().onFailed(r11.mRequest, 9, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x022e, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x025e, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(9, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x025f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0260, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0269, code lost:
        if (getTransportCallback() != null) goto L_0x026b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x026b, code lost:
        getTransportCallback().onFailed(r11.mRequest, 8, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0285, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, "", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x02a6, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(8, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x02a7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x02a8, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x02b0, code lost:
        if (getTransportCallback() != null) goto L_0x02b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x02b2, code lost:
        getTransportCallback().onFailed(r11.mRequest, 5, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x02cc, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x02fc, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(5, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x02fd, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x02fe, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0306, code lost:
        if (getTransportCallback() != null) goto L_0x0308;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0308, code lost:
        getTransportCallback().onFailed(r11.mRequest, 4, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0322, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0352, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(4, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0353, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0354, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x035b, code lost:
        if (getTransportCallback() != null) goto L_0x035d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x035d, code lost:
        getTransportCallback().onFailed(r11.mRequest, 3, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0377, code lost:
        android.util.Log.e(com.alipay.literpc.android.phone.mrpc.core.HttpManager.TAG, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x03a7, code lost:
        throw new com.alipay.literpc.android.phone.mrpc.core.HttpException(3, r0 + "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x03a8, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x03a9, code lost:
        abortRequest();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x03b0, code lost:
        if (getTransportCallback() != null) goto L_0x03b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x03b2, code lost:
        getTransportCallback().onFailed(r11.mRequest, 3, r0 + "");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x03fd A[ExcHandler: SSLException (r0v10 'e' javax.net.ssl.SSLException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0452 A[ExcHandler: SSLPeerUnverifiedException (r0v7 'e' javax.net.ssl.SSLPeerUnverifiedException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x04a7 A[ExcHandler: SSLHandshakeException (r0v4 'e' javax.net.ssl.SSLHandshakeException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x04fc A[ExcHandler: URISyntaxException (r0v2 'e' java.net.URISyntaxException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0509 A[ExcHandler: HttpException (r0v1 'e' com.alipay.literpc.android.phone.mrpc.core.HttpException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x016f A[ExcHandler: NullPointerException (r1v47 'e' java.lang.NullPointerException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01b3 A[ExcHandler: IOException (r0v31 'e' java.io.IOException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0208 A[ExcHandler: UnknownHostException (r0v28 'e' java.net.UnknownHostException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x025f A[ExcHandler: HttpHostConnectException (r0v25 'e' org.apache.http.conn.HttpHostConnectException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x02a7 A[ExcHandler: NoHttpResponseException (r0v22 'e' org.apache.http.NoHttpResponseException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x02fd A[ExcHandler: SocketTimeoutException (r0v19 'e' java.net.SocketTimeoutException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0353 A[ExcHandler: ConnectTimeoutException (r0v16 'e' org.apache.http.conn.ConnectTimeoutException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x03a8 A[ExcHandler: ConnectionPoolTimeoutException (r0v13 'e' org.apache.http.conn.ConnectionPoolTimeoutException A[CUSTOM_DECLARE]), Splitter:B:1:0x0004] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alipay.literpc.android.phone.mrpc.core.Response call() throws com.alipay.literpc.android.phone.mrpc.core.HttpException {
        /*
            r11 = this;
            r0 = 0
            r1 = 3
            r2 = 6
            r3 = 2
            android.content.Context r4 = r11.mContext     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            boolean r4 = com.alipay.literpc.android.phone.mrpc.core.NetworkUtils.isNetworkAvailable(r4)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r4 != 0) goto L_0x0013
            java.lang.String r4 = "HttpWorker"
            java.lang.String r5 = "The network is not available"
            android.util.Log.w(r4, r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x0013:
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r4 = r11.getTransportCallback()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r4 == 0) goto L_0x0022
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r4 = r11.getTransportCallback()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r5 = r11.mRequest     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r4.onPreExecute(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x0022:
            r11.addRequestHeaders()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            org.apache.http.protocol.HttpContext r4 = r11.mLocalContext     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r5 = "http.cookie-store"
            org.apache.http.client.CookieStore r6 = r11.mCookieStore     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r4.setAttribute(r5, r6)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.AndroidHttpClient r4 = r11.getHttpClient()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            org.apache.http.client.HttpRequestRetryHandler r5 = sHttpRequestRetryHandler     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r4.setHttpRequestRetryHandler(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            org.apache.http.HttpResponse r6 = r11.executeRequest()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.HttpManager r9 = r11.mHttpManager     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r10 = 0
            long r7 = r7 - r4
            r9.addConnectTime(r7)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            org.apache.http.client.CookieStore r4 = r11.mCookieStore     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.util.List r4 = r4.getCookies()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r5 = r11.mRequest     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            boolean r5 = r5.isResetCookie()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 == 0) goto L_0x005f
            android.webkit.CookieManager r5 = r11.getCookieManager()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r5.removeAllCookie()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x005f:
            boolean r5 = r4.isEmpty()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 != 0) goto L_0x00c7
            java.util.Iterator r4 = r4.iterator()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x0069:
            boolean r5 = r4.hasNext()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 == 0) goto L_0x00c7
            java.lang.Object r5 = r4.next()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            org.apache.http.cookie.Cookie r5 = (org.apache.http.cookie.Cookie) r5     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r7 = r5.getDomain()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r7 != 0) goto L_0x007c
            goto L_0x0069
        L_0x007c:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r7.<init>()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = r5.getName()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r7.append(r8)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = "="
            r7.append(r8)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = r5.getValue()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r7.append(r8)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = "; domain="
            r7.append(r8)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = r5.getDomain()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r7.append(r8)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            boolean r5 = r5.isSecure()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 == 0) goto L_0x00a9
            java.lang.String r5 = "; Secure"
            goto L_0x00ab
        L_0x00a9:
            java.lang.String r5 = ""
        L_0x00ab:
            r7.append(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r5 = r7.toString()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            android.webkit.CookieManager r7 = r11.getCookieManager()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r8 = r11.mRequest     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r8 = r8.getUrl()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r7.setCookie(r8, r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            android.webkit.CookieSyncManager r5 = android.webkit.CookieSyncManager.getInstance()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r5.sync()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            goto L_0x0069
        L_0x00c7:
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r4 = r11.mRequest     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.Response r4 = r11.processResponse(r6, r4)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r5 = -1
            if (r4 == 0) goto L_0x00de
            byte[] r7 = r4.getResData()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r7 == 0) goto L_0x00de
            byte[] r7 = r4.getResData()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            int r7 = r7.length     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            long r7 = (long) r7     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            goto L_0x00df
        L_0x00de:
            r7 = r5
        L_0x00df:
            int r9 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r9 != 0) goto L_0x00ff
            boolean r5 = r4 instanceof com.alipay.literpc.android.phone.mrpc.core.HttpUrlResponse     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 == 0) goto L_0x00ff
            r5 = r4
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlResponse r5 = (com.alipay.literpc.android.phone.mrpc.core.HttpUrlResponse) r5     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlHeader r5 = r5.getHeader()     // Catch:{ Exception -> 0x00f8, HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f }
            java.lang.String r6 = "Content-Length"
            java.lang.String r5 = r5.getHead(r6)     // Catch:{ Exception -> 0x00f8, HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f }
            java.lang.Long.parseLong(r5)     // Catch:{ Exception -> 0x00f8, HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f }
            goto L_0x00ff
        L_0x00f8:
            java.lang.String r5 = "HttpWorker"
            java.lang.String r6 = "parse Content-Length error"
            android.util.Log.e(r5, r6)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x00ff:
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r5 = r11.mRequest     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r5 = r5.getUrl()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r5 == 0) goto L_0x0128
            java.lang.String r6 = r11.getOperationType()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            if (r6 != 0) goto L_0x0128
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r6.<init>()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r6.append(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r5 = "#"
            r6.append(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            java.lang.String r5 = r11.getOperationType()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r6.append(r5)     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
            r6.toString()     // Catch:{ HttpException -> 0x0509, URISyntaxException -> 0x04fc, SSLHandshakeException -> 0x04a7, SSLPeerUnverifiedException -> 0x0452, SSLException -> 0x03fd, ConnectionPoolTimeoutException -> 0x03a8, ConnectTimeoutException -> 0x0353, SocketTimeoutException -> 0x02fd, NoHttpResponseException -> 0x02a7, HttpHostConnectException -> 0x025f, UnknownHostException -> 0x0208, IOException -> 0x01b3, NullPointerException -> 0x016f, Exception -> 0x0129 }
        L_0x0128:
            return r4
        L_0x0129:
            r1 = move-exception
            java.lang.String r2 = "HttpManager"
            java.lang.String r3 = ""
            android.util.Log.e(r2, r3, r1)
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            if (r2 == 0) goto L_0x0154
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r1)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.onFailed(r3, r0, r4)
        L_0x0154:
            com.alipay.literpc.android.phone.mrpc.core.HttpException r2 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r1)
            java.lang.String r1 = ""
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r2.<init>(r0, r1)
            throw r2
        L_0x016f:
            r1 = move-exception
            r11.abortRequest()
            int r2 = r11.mRetryTimes
            r3 = 1
            if (r2 >= r3) goto L_0x0182
            int r0 = r11.mRetryTimes
            int r0 = r0 + r3
            r11.mRetryTimes = r0
            com.alipay.literpc.android.phone.mrpc.core.Response r0 = r11.call()
            return r0
        L_0x0182:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            java.lang.String r3 = ""
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r2)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r2 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r1)
            java.lang.String r1 = ""
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r2.<init>(r0, r1)
            throw r2
        L_0x01b3:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            if (r1 == 0) goto L_0x01d7
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x01d7:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = ""
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x0208:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            r2 = 9
            if (r1 == 0) goto L_0x022e
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x022e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = ""
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x025f:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            r2 = 8
            if (r1 == 0) goto L_0x0285
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x0285:
            java.lang.String r1 = "HttpManager"
            java.lang.String r3 = ""
            android.util.Log.e(r1, r3, r0)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x02a7:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            r2 = 5
            if (r1 == 0) goto L_0x02cc
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x02cc:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = ""
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x02fd:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            r2 = 4
            if (r1 == 0) goto L_0x0322
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x0322:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = ""
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x0353:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            if (r2 == 0) goto L_0x0377
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.onFailed(r3, r1, r4)
        L_0x0377:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.String r3 = ""
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r2)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r2 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.<init>(r1, r0)
            throw r2
        L_0x03a8:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            if (r2 == 0) goto L_0x03cc
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r2 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.onFailed(r3, r1, r4)
        L_0x03cc:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.String r3 = ""
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r2)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r2 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.<init>(r1, r0)
            throw r2
        L_0x03fd:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            if (r1 == 0) goto L_0x0421
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r3 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r3, r2, r4)
        L_0x0421:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r3 = ""
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "HttpManager"
            android.util.Log.e(r3, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x0452:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            if (r1 == 0) goto L_0x0476
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r2 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r2, r3, r4)
        L_0x0476:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r2 = ""
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "HttpManager"
            android.util.Log.e(r2, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x04a7:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            if (r1 == 0) goto L_0x04cb
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r2 = r11.mRequest
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r1.onFailed(r2, r3, r4)
        L_0x04cb:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r2 = ""
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "HttpManager"
            android.util.Log.e(r2, r1)
            com.alipay.literpc.android.phone.mrpc.core.HttpException r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpException
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            java.lang.String r0 = ""
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        L_0x04fc:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            java.lang.Throwable r0 = r0.getCause()
            java.lang.String r2 = "Url parser error!"
            r1.<init>(r2, r0)
            throw r1
        L_0x0509:
            r0 = move-exception
            r11.abortRequest()
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            if (r1 == 0) goto L_0x0524
            com.alipay.literpc.android.phone.mrpc.core.TransportCallback r1 = r11.getTransportCallback()
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlRequest r2 = r11.mRequest
            int r3 = r0.getCode()
            java.lang.String r4 = r0.getMsg()
            r1.onFailed(r2, r3, r4)
        L_0x0524:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r2 = ""
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "HttpManager"
            android.util.Log.e(r2, r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.literpc.android.phone.mrpc.core.HttpWorker.call():com.alipay.literpc.android.phone.mrpc.core.Response");
    }

    private void abortRequest() {
        if (this.mHttpRequest != null) {
            this.mHttpRequest.abort();
        }
    }

    private TransportCallback getTransportCallback() {
        return this.mRequest.getCallback();
    }

    private HttpResponse executeRequest() throws Exception {
        return executeHttpClientRequest();
    }

    private HttpResponse executeHttpClientRequest() throws IOException {
        Log.d(TAG, "By Http/Https to request. operationType=" + getOperationType() + " url=" + this.mHttpRequest.getURI().toString());
        getHttpClient().getParams().setParameter("http.route.default-proxy", getProxy());
        HttpHost httpHost = getHttpHost();
        if (getTargetPort() == 80) {
            httpHost = new HttpHost(getTargetURL().getHost());
        }
        return getHttpClient().execute(httpHost, (HttpRequest) this.mHttpRequest, this.mLocalContext);
    }

    private String getOperationType() {
        if (!TextUtils.isEmpty(this.mOperationType)) {
            return this.mOperationType;
        }
        this.mOperationType = this.mRequest.getTag("operationType");
        return this.mOperationType;
    }

    private AndroidHttpClient getHttpClient() {
        return this.mHttpManager.getHttpClient();
    }

    private void addRequestHeaders() throws Exception {
        ArrayList<Header> headers = getHeaders();
        if (headers != null && !headers.isEmpty()) {
            Iterator<Header> it = headers.iterator();
            while (it.hasNext()) {
                getHttpUriRequest().addHeader(it.next());
            }
        }
        AndroidHttpClient.modifyRequestToAcceptGzipResponse(getHttpUriRequest());
        AndroidHttpClient.modifyRequestToKeepAlive(getHttpUriRequest());
        getHttpUriRequest().addHeader("cookie", getCookieManager().getCookie(this.mRequest.getUrl()));
    }

    private HttpHost getHttpHost() throws MalformedURLException {
        if (this.mHttpHost != null) {
            return this.mHttpHost;
        }
        URL targetURL = getTargetURL();
        this.mHttpHost = new HttpHost(targetURL.getHost(), getTargetPort(), targetURL.getProtocol());
        return this.mHttpHost;
    }

    private int getTargetPort() throws MalformedURLException {
        URL targetURL = getTargetURL();
        if (targetURL.getPort() == -1) {
            return targetURL.getDefaultPort();
        }
        return targetURL.getPort();
    }

    private URL getTargetURL() throws MalformedURLException {
        if (this.mTargetUrl != null) {
            return this.mTargetUrl;
        }
        this.mTargetUrl = new URL(this.mRequest.getUrl());
        return this.mTargetUrl;
    }

    private HttpHost getProxy() {
        HttpHost proxy = NetworkUtils.getProxy(this.mContext);
        if (proxy == null || !TextUtils.equals(proxy.getHostName(), "127.0.0.1") || proxy.getPort() != 8087) {
            return proxy;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public HashMap<String, String> getContentType(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str2 : str.split(";")) {
            String[] split = str2.indexOf(61) == -1 ? new String[]{"Content-Type", str2} : str2.split(SymbolExpUtil.SYMBOL_EQUAL);
            hashMap.put(split[0], split[1]);
        }
        return hashMap;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00b7 A[SYNTHETIC, Splitter:B:19:0x00b7] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alipay.literpc.android.phone.mrpc.core.Response handleResponse(org.apache.http.HttpResponse r9, int r10, java.lang.String r11) throws java.io.IOException {
        /*
            r8 = this;
            java.lang.String r0 = "HttpWorker"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "开始handle，handleResponse-1,"
            r1.append(r2)
            java.lang.Thread r2 = java.lang.Thread.currentThread()
            long r2 = r2.getId()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            org.apache.http.HttpEntity r0 = r9.getEntity()
            r1 = 0
            if (r0 == 0) goto L_0x00d0
            org.apache.http.StatusLine r2 = r9.getStatusLine()
            int r2 = r2.getStatusCode()
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 != r3) goto L_0x00d0
            java.lang.String r2 = "HttpWorker"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "200，开始处理，handleResponse-2,threadid = "
            r3.append(r4)
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            long r4 = r4.getId()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r2, r3)
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x00b3 }
            r2.<init>()     // Catch:{ all -> 0x00b3 }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00b1 }
            r5 = 0
            r8.writeData(r0, r5, r2)     // Catch:{ all -> 0x00b1 }
            byte[] r0 = r2.toByteArray()     // Catch:{ all -> 0x00b1 }
            r1 = 0
            r8.hasEtagInResponse = r1     // Catch:{ all -> 0x00b1 }
            com.alipay.literpc.android.phone.mrpc.core.HttpManager r1 = r8.mHttpManager     // Catch:{ all -> 0x00b1 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00b1 }
            r7 = 0
            long r5 = r5 - r3
            r1.addSocketTime(r5)     // Catch:{ all -> 0x00b1 }
            com.alipay.literpc.android.phone.mrpc.core.HttpManager r1 = r8.mHttpManager     // Catch:{ all -> 0x00b1 }
            int r3 = r0.length     // Catch:{ all -> 0x00b1 }
            long r3 = (long) r3     // Catch:{ all -> 0x00b1 }
            r1.addDataSize(r3)     // Catch:{ all -> 0x00b1 }
            java.lang.String r1 = "HttpWorker"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b1 }
            r3.<init>()     // Catch:{ all -> 0x00b1 }
            java.lang.String r4 = "res:"
            r3.append(r4)     // Catch:{ all -> 0x00b1 }
            int r4 = r0.length     // Catch:{ all -> 0x00b1 }
            r3.append(r4)     // Catch:{ all -> 0x00b1 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x00b1 }
            android.util.Log.i(r1, r3)     // Catch:{ all -> 0x00b1 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlResponse r1 = new com.alipay.literpc.android.phone.mrpc.core.HttpUrlResponse     // Catch:{ all -> 0x00b1 }
            com.alipay.literpc.android.phone.mrpc.core.HttpUrlHeader r3 = r8.handleResponseHeader(r9)     // Catch:{ all -> 0x00b1 }
            r1.<init>(r3, r10, r11, r0)     // Catch:{ all -> 0x00b1 }
            r8.fillResponse(r1, r9)     // Catch:{ all -> 0x00b1 }
            r2.close()     // Catch:{ IOException -> 0x00a4 }
            java.lang.String r9 = "HttpWorker"
            java.lang.String r10 = "finally,handleResponse"
            android.util.Log.d(r9, r10)
            goto L_0x00dc
        L_0x00a4:
            r9 = move-exception
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.Throwable r9 = r9.getCause()
            java.lang.String r11 = "ArrayOutputStream close error!"
            r10.<init>(r11, r9)
            throw r10
        L_0x00b1:
            r9 = move-exception
            goto L_0x00b5
        L_0x00b3:
            r9 = move-exception
            r2 = r1
        L_0x00b5:
            if (r2 == 0) goto L_0x00c8
            r2.close()     // Catch:{ IOException -> 0x00bb }
            goto L_0x00c8
        L_0x00bb:
            r9 = move-exception
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.Throwable r9 = r9.getCause()
            java.lang.String r11 = "ArrayOutputStream close error!"
            r10.<init>(r11, r9)
            throw r10
        L_0x00c8:
            java.lang.String r10 = "HttpWorker"
            java.lang.String r11 = "finally,handleResponse"
            android.util.Log.d(r10, r11)
            throw r9
        L_0x00d0:
            if (r0 != 0) goto L_0x00dc
            org.apache.http.StatusLine r9 = r9.getStatusLine()
            int r9 = r9.getStatusCode()
            r10 = 304(0x130, float:4.26E-43)
        L_0x00dc:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.literpc.android.phone.mrpc.core.HttpWorker.handleResponse(org.apache.http.HttpResponse, int, java.lang.String):com.alipay.literpc.android.phone.mrpc.core.Response");
    }

    /* access modifiers changed from: protected */
    public HttpUrlHeader handleResponseHeader(HttpResponse httpResponse) {
        HttpUrlHeader httpUrlHeader = new HttpUrlHeader();
        for (Header header : httpResponse.getAllHeaders()) {
            httpUrlHeader.setHead(header.getName(), header.getValue());
        }
        return httpUrlHeader;
    }

    /* access modifiers changed from: protected */
    public void fillResponse(HttpUrlResponse httpUrlResponse, HttpResponse httpResponse) {
        String str;
        long period = getPeriod(httpResponse);
        Header contentType = httpResponse.getEntity().getContentType();
        String str2 = null;
        if (contentType != null) {
            HashMap<String, String> contentType2 = getContentType(contentType.getValue());
            str2 = contentType2.get(WVConstants.CHARSET);
            str = contentType2.get("Content-Type");
        } else {
            str = null;
        }
        httpUrlResponse.setContentType(str);
        httpUrlResponse.setCharset(str2);
        httpUrlResponse.setCreateTime(System.currentTimeMillis());
        httpUrlResponse.setPeriod(period);
    }

    /* access modifiers changed from: protected */
    public long getPeriod(HttpResponse httpResponse) {
        Header firstHeader = httpResponse.getFirstHeader(HttpConstant.CACHE_CONTROL);
        if (firstHeader != null) {
            String[] split = firstHeader.getValue().split(SymbolExpUtil.SYMBOL_EQUAL);
            if (split.length >= 2) {
                try {
                    return parserMaxage(split);
                } catch (NumberFormatException e) {
                    Log.w(TAG, e);
                }
            }
        }
        Header firstHeader2 = httpResponse.getFirstHeader("Expires");
        if (firstHeader2 != null) {
            return AndroidHttpClient.parseDate(firstHeader2.getValue()) - System.currentTimeMillis();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public long parserMaxage(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if ("max-age".equalsIgnoreCase(strArr[i])) {
                int i2 = i + 1;
                if (strArr[i2] != null) {
                    try {
                        return Long.parseLong(strArr[i2]);
                    } catch (Exception unused) {
                    }
                } else {
                    continue;
                }
            }
        }
        return 0;
    }

    public HttpUrlRequest getRequest() {
        return this.mRequest;
    }

    /* access modifiers changed from: protected */
    public void writeData(HttpEntity httpEntity, long j, OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            InputStream ungzippedContent = AndroidHttpClient.getUngzippedContent(httpEntity);
            long contentLength = httpEntity.getContentLength();
            try {
                byte[] bArr = new byte[2048];
                while (true) {
                    int read = ungzippedContent.read(bArr);
                    if (read == -1 || this.mRequest.isCanceled()) {
                        outputStream.flush();
                    } else {
                        outputStream.write(bArr, 0, read);
                        j += (long) read;
                        if (getTransportCallback() != null && contentLength > 0) {
                            TransportCallback transportCallback = getTransportCallback();
                            HttpUrlRequest httpUrlRequest = this.mRequest;
                            double d = (double) j;
                            double d2 = (double) contentLength;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            transportCallback.onProgressUpdate(httpUrlRequest, d / d2);
                        }
                    }
                }
                outputStream.flush();
                IOUtil.closeStream(ungzippedContent);
            } catch (Exception e) {
                Log.w(TAG, e.getCause());
                throw new IOException("HttpWorker Request Error!" + e.getLocalizedMessage());
            } catch (Throwable th) {
                IOUtil.closeStream(ungzippedContent);
                throw th;
            }
        } else {
            httpEntity.consumeContent();
            throw new IllegalArgumentException("Output stream may not be null");
        }
    }

    private CookieManager getCookieManager() {
        if (this.mCookieManager != null) {
            return this.mCookieManager;
        }
        this.mCookieManager = CookieManager.getInstance();
        return this.mCookieManager;
    }

    public Response processResponse(HttpResponse httpResponse, HttpUrlRequest httpUrlRequest) throws HttpException, IOException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        if (statusCode == 200 || willHandleOtherCode(statusCode, reasonPhrase)) {
            return handleResponse(httpResponse, statusCode, reasonPhrase);
        }
        throw new HttpException(Integer.valueOf(httpResponse.getStatusLine().getStatusCode()), httpResponse.getStatusLine().getReasonPhrase());
    }
}
