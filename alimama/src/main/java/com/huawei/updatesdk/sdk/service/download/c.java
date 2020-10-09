package com.huawei.updatesdk.sdk.service.download;

public class c extends Exception {
    private String a;
    private int b;

    public c() {
    }

    public c(int i, String str) {
        this(str);
        this.b = i;
    }

    public c(String str) {
        super(str);
        this.a = str;
    }

    public String a() {
        return this.a;
    }

    public int b() {
        return this.b;
    }
}
