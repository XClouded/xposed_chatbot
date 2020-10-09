package com.alimamaunion.support.debugimpl;

import java.lang.Thread;

public class ErrorReportCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String LOGTAG = "ErrorReportCrashHandler";
    private Thread.UncaughtExceptionHandler mCrashHandler;

    public ErrorReportCrashHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.mCrashHandler = uncaughtExceptionHandler;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (this.mCrashHandler != null) {
            this.mCrashHandler.uncaughtException(thread, th);
        }
        System.exit(1);
    }
}
