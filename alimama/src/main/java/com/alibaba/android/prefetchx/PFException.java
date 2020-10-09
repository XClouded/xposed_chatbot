package com.alibaba.android.prefetchx;

public class PFException extends RuntimeException {
    private static final long serialVersionUID = -2375363393616874049L;

    public PFException() {
    }

    public PFException(String str) {
        super(str);
    }

    public PFException(String str, Throwable th) {
        super(str, th);
    }

    public PFException(Throwable th) {
        super(th);
    }
}
