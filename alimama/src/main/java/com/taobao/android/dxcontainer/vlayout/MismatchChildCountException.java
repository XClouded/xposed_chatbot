package com.taobao.android.dxcontainer.vlayout;

public class MismatchChildCountException extends RuntimeException {
    public MismatchChildCountException(String str) {
        super(str);
    }

    public MismatchChildCountException(String str, Throwable th) {
        super(str, th);
    }
}
