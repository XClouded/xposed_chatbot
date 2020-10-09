package com.vivo.push.b;

import com.vivo.push.a;

/* compiled from: OnVerifyReceiveCommand */
public abstract class x extends u {
    private String a;
    private long b;

    public x(int i) {
        super(i);
    }

    public final long f() {
        return this.b;
    }

    public final String i() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public void c(a aVar) {
        super.c(aVar);
        aVar.a("OnVerifyCallBackCommand.EXTRA_SECURITY_CONTENT", this.a);
        aVar.a("notify_id", this.b);
    }

    /* access modifiers changed from: protected */
    public void d(a aVar) {
        super.d(aVar);
        this.a = aVar.a("OnVerifyCallBackCommand.EXTRA_SECURITY_CONTENT");
        this.b = aVar.b("notify_id", -1);
    }
}
