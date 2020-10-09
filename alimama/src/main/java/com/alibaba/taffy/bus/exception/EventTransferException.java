package com.alibaba.taffy.bus.exception;

public class EventTransferException extends RuntimeException {
    public EventTransferException() {
    }

    public EventTransferException(String str) {
        super(str);
    }

    public EventTransferException(String str, Throwable th) {
        super(str, th);
    }

    public EventTransferException(Throwable th) {
        super(th);
    }
}
