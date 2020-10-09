package com.taobao.android.dinamic.exception;

public class DinamicException extends Exception {
    public DinamicException() {
    }

    public DinamicException(String str) {
        super(str);
    }

    public DinamicException(String str, Throwable th) {
        super(str, th);
    }

    public DinamicException(Throwable th) {
        super(th);
    }
}
