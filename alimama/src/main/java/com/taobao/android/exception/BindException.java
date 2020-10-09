package com.taobao.android.exception;

public class BindException extends RuntimeException {
    public static final int CODE_BIND_FAILD = -6;
    public static final int CODE_CLASS_NOT_FOUND = -2;
    public static final int CODE_CONNECTING = -3;
    public static final int CODE_CONNECTION_EXIST = -1;
    public static final int CODE_UNFIND_SERVICE = -4;
    public static final int CODE_UNKNOW = -5;
    public int code;

    public BindException(int i, String str) {
        this(i, str, (Throwable) null);
    }

    public BindException(int i, String str, Throwable th) {
        super(str, th);
        this.code = i;
    }

    public String getMessage() {
        return super.getMessage() + " code=" + this.code;
    }

    public String toString() {
        return super.toString() + " code=" + this.code;
    }
}
