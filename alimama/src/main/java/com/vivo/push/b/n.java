package com.vivo.push.b;

import com.uc.webview.export.cyclone.ErrorCode;
import com.vivo.push.a;

/* compiled from: OnDispatcherReceiveCommand */
public final class n extends u {
    private int a;

    public n() {
        super(ErrorCode.UCSERVICE_PARAM_NULL);
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("key_dispatch_environment", this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b("key_dispatch_environment", 0);
    }

    public final int d() {
        return this.a;
    }
}
