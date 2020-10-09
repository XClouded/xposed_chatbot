package com.alimama.unionwl.unwcache;

public class DiskLruResult {
    public byte[] data;
    public String extra;
    public boolean isSuccess;
    public String key;
    public String path;

    public DiskLruResult() {
    }

    public DiskLruResult(String str, String str2, byte[] bArr) {
        this.key = str;
        this.extra = str2;
        this.data = bArr;
    }
}
