package com.taobao.aipc.exception;

public class IPCException extends Exception {
    private int mErrorCode;

    public IPCException(int i, String str) {
        super(str);
        this.mErrorCode = i;
    }

    public IPCException(int i, String str, Throwable th) {
        super(str, th);
        this.mErrorCode = i;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
