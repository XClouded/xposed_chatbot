package com.taobao.monitor.exception;

import android.annotation.TargetApi;

public class ProcedureException extends RuntimeException {
    public ProcedureException() {
    }

    public ProcedureException(String str) {
        super(str);
    }

    public ProcedureException(String str, Throwable th) {
        super(str, th);
    }

    public ProcedureException(Throwable th) {
        super(th);
    }

    @TargetApi(24)
    public ProcedureException(String str, Throwable th, boolean z, boolean z2) {
        super(str, th, z, z2);
    }
}
