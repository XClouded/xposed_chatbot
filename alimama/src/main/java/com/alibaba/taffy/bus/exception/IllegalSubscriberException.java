package com.alibaba.taffy.bus.exception;

public class IllegalSubscriberException extends RuntimeException {
    public IllegalSubscriberException() {
    }

    public IllegalSubscriberException(String str) {
        super(str);
    }

    public IllegalSubscriberException(String str, Throwable th) {
        super(str, th);
    }

    public IllegalSubscriberException(Throwable th) {
        super(th);
    }
}
