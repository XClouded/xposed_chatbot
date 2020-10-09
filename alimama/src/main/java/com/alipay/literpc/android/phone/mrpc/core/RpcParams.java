package com.alipay.literpc.android.phone.mrpc.core;

import java.util.List;
import org.apache.http.Header;

public class RpcParams {
    private String gwUrl;
    private boolean gzip;
    private List<Header> headers;

    public String getGwUrl() {
        return this.gwUrl;
    }

    public void setGwUrl(String str) {
        this.gwUrl = str;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public void setHeaders(List<Header> list) {
        this.headers = list;
    }

    public boolean isGzip() {
        return this.gzip;
    }

    public void setGzip(boolean z) {
        this.gzip = z;
    }
}
