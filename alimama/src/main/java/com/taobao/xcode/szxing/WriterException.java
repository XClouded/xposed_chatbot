package com.taobao.xcode.szxing;

public final class WriterException extends Exception {
    private static final long serialVersionUID = -2091664531129267634L;

    public WriterException() {
    }

    public WriterException(String str) {
        super(str);
    }

    public WriterException(Throwable th) {
        super(th);
    }
}
