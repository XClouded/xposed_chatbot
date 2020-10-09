package com.alimama.unionwl.unwcache;

public class MemoryResult {
    public byte[] data;
    public String extra;

    public MemoryResult(byte[] bArr, String str) {
        this.data = bArr;
        this.extra = str;
    }
}
