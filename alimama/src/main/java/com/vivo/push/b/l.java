package com.vivo.push.b;

import com.vivo.push.a;

/* compiled from: OnChangePushStatusReceiveCommand */
public final class l extends u {
    private int a = -1;
    private int b = -1;

    public final String toString() {
        return "OnChangePushStatusCommand";
    }

    public l() {
        super(12);
    }

    public final int d() {
        return this.a;
    }

    public final int e() {
        return this.b;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("OnChangePushStatus.EXTRA_REQ_SERVICE_STATUS", this.a);
        aVar.a("OnChangePushStatus.EXTRA_REQ_RECEIVER_STATUS", this.b);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b("OnChangePushStatus.EXTRA_REQ_SERVICE_STATUS", this.a);
        this.b = aVar.b("OnChangePushStatus.EXTRA_REQ_RECEIVER_STATUS", this.b);
    }
}
