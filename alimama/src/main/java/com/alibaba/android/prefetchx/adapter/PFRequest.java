package com.alibaba.android.prefetchx.adapter;

import com.taobao.weex.common.WXRequest;
import java.util.Map;

public class PFRequest {
    public static final int DEFAULT_TIMEOUT_MS = 3000;
    public String body;
    public String method;
    public Map<String, String> paramMap;
    public int timeoutMs = 3000;
    public String url;

    public PFRequest() {
    }

    public PFRequest(WXRequest wXRequest) {
        this.paramMap = wXRequest.paramMap;
        this.url = wXRequest.url;
        this.method = wXRequest.method;
        this.body = wXRequest.body;
        this.timeoutMs = wXRequest.timeoutMs;
    }

    public WXRequest toWXRequest() {
        WXRequest wXRequest = new WXRequest();
        wXRequest.paramMap = this.paramMap;
        wXRequest.url = this.url;
        wXRequest.method = this.method;
        wXRequest.body = this.body;
        wXRequest.timeoutMs = this.timeoutMs;
        return wXRequest;
    }
}
