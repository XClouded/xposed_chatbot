package com.huawei.updatesdk.sdk.service.c.a;

public class d extends b {
    public static final int ERROR = 1;
    public static final int NETWORK_ERROR = 3;
    public static final int OK = 0;
    public static final int PARSE_ERROR = 4;
    public static final int PROGUARD_ERROR = 6;
    public static final int REQ_PARAM_ERROR = 5;
    public static final int RTN_CODE_OK = 0;
    public static final int TIMEOUT = 2;
    private a errCause = a.NORMAL;
    private String reason;
    private int responseCode = 1;
    private int rtnCode_ = 0;

    public enum a {
        NORMAL,
        NO_NETWORK,
        JSON_ERROR,
        PARAM_ERROR,
        IO_EXCEPTION,
        CONNECT_EXCEPTION,
        UNKNOWN_EXCEPTION,
        NO_PROGUARD
    }

    public void a(int i) {
        this.responseCode = i;
    }

    public void a(a aVar) {
        this.errCause = aVar;
    }

    public void a(String str) {
        this.reason = str;
    }

    public void b(int i) {
        this.rtnCode_ = i;
    }

    public int c() {
        return this.responseCode;
    }

    public int d() {
        return this.rtnCode_;
    }

    public a e() {
        return this.errCause;
    }

    public String f() {
        return this.reason;
    }

    public String toString() {
        return getClass().getName() + " { \n\tresponseCode: " + c() + "\n\trtnCode_: " + d() + "\n\terrCause: " + e() + "\n}";
    }
}
