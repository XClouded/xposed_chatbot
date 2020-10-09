package com.taobao.zcache.network;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private byte[] data = null;
    private String errorMsg = null;
    private Map<String, String> headers = new HashMap();
    private int httpCode = 0;

    public boolean isSuccess() {
        return this.httpCode == 200;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public void setHttpCode(int i) {
        this.httpCode = i;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> map) {
        if (map != null) {
            this.headers = map;
        }
    }

    public void addHeader(String str, String str2) {
        this.headers.put(str, str2);
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String str) {
        this.errorMsg = str;
    }
}
