package com.alibaba.motu.tbrest.data;

public class RestData {
    private final String appKey;
    private final int count;
    private final byte[] packRequest;
    private final String url;

    public RestData(String str, String str2, int i, byte[] bArr) {
        this.appKey = str;
        this.url = str2;
        this.count = i;
        this.packRequest = bArr;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getUrl() {
        return this.url;
    }

    public int getCount() {
        return this.count;
    }

    public byte[] getPackRequest() {
        return this.packRequest;
    }
}
