package com.taobao.zcache.network;

import android.net.Uri;
import java.util.Map;

public class HttpRequest {
    public static final String DEFAULT_HTTPS_ERROR_EXPIRED = "EXPIRED";
    public static final String DEFAULT_HTTPS_ERROR_INVALID = "INVALID";
    public static final String DEFAULT_HTTPS_ERROR_NONE = "NONE";
    public static final int DEFAULT_MAX_LENGTH = 5242880;
    public static final int DEFAULT_MAX_REDIRECT_TIMES = 5;
    private int connectTimeout = 5000;
    private Map<String, String> headers = null;
    private String httpsVerifyError = "NONE";
    private boolean isRedirect = true;
    private String method = "GET";
    private byte[] postData;
    private int readTimeout = 5000;
    private int retryTime = 1;
    private Uri uri;

    public HttpRequest(String str) {
        if (str != null) {
            this.uri = Uri.parse(str);
            return;
        }
        throw new NullPointerException("HttpRequest init error, url is null.");
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        if (uri2 != null) {
            this.uri = uri2;
        }
    }

    public byte[] getPostData() {
        return this.postData;
    }

    public void setPostData(byte[] bArr) {
        this.postData = bArr;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> map) {
        this.headers = map;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int i) {
        this.connectTimeout = i;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int i) {
        this.readTimeout = i;
    }

    public boolean isRedirect() {
        return this.isRedirect;
    }

    public void setRedirect(boolean z) {
        this.isRedirect = z;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void setRetryTime(int i) {
        this.retryTime = i;
    }

    public String getHttpsVerifyError() {
        return this.httpsVerifyError;
    }

    public void setHttpsVerifyError(String str) {
        this.httpsVerifyError = str;
    }
}
