package com.taobao.xcode.szxing;

public final class FormatException extends Exception {
    private static final FormatException instance = new FormatException();
    private static final long serialVersionUID = -4850430760994424743L;

    public final Throwable fillInStackTrace() {
        return null;
    }

    private FormatException() {
    }

    public static FormatException getFormatInstance() {
        return instance;
    }
}
