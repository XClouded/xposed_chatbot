package com.taobao.phenix.loader.network;

import com.taobao.weex.el.parse.Operators;

public class NetworkResponseException extends RuntimeException {
    private final int mExtraCode;
    private final int mHttpCode;

    public NetworkResponseException(int i, String str, int i2, Throwable th) {
        super(str, th);
        this.mHttpCode = i;
        this.mExtraCode = i2;
    }

    public NetworkResponseException(int i, String str) {
        this(i, str, 0, (Throwable) null);
    }

    public int getHttpCode() {
        return this.mHttpCode;
    }

    public int getExtraCode() {
        return this.mExtraCode;
    }

    public String toString() {
        return getClass().getName() + Operators.BRACKET_START_STR + "httpCode=" + this.mHttpCode + ", extraCode=" + this.mExtraCode + ", desc=" + getMessage() + Operators.BRACKET_END_STR;
    }
}
