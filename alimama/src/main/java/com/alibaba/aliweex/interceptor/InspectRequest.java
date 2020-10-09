package com.alibaba.aliweex.interceptor;

public class InspectRequest extends InspectCommon {
    public void setFriendlyNameExtra(Integer num) {
        this.payload.put("friendlyNameExtra", num);
    }

    public void setUrl(String str) {
        this.payload.put("url", str);
    }

    public void setMethod(String str) {
        this.payload.put("method", str);
    }

    public void setBody(String str) {
        this.payload.put("body", str);
    }

    public void setBody(byte[] bArr) {
        this.payload.put("body", bArr);
    }

    public void setFriendlyName(String str) {
        this.payload.put("friendlyName", str);
    }
}
