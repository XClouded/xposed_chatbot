package com.alibaba.android.update.exception;

public class StateNotFoundException extends Exception {
    public StateNotFoundException() {
    }

    public StateNotFoundException(String str) {
        super(str);
    }

    public StateNotFoundException(String str, Throwable th) {
        super(str, th);
    }

    public StateNotFoundException(Throwable th) {
        super(th);
    }
}
