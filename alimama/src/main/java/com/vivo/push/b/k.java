package com.vivo.push.b;

import com.vivo.push.a;

/* compiled from: OnAppReceiveCommand */
public final class k extends u {
    private String a;
    private String b;
    private String c;

    public final String toString() {
        return "OnBindCommand";
    }

    public k(int i) {
        super(i);
    }

    public final String d() {
        return this.a;
    }

    public final String e() {
        return this.c;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("app_id", this.a);
        aVar.a("client_id", this.b);
        aVar.a("client_token", this.c);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.a("app_id");
        this.b = aVar.a("client_id");
        this.c = aVar.a("client_token");
    }
}
