package com.vivo.push.b;

import com.vivo.push.a;

/* compiled from: OnLogReceiveCommand */
public final class p extends u {
    private String a;
    private int b = 0;
    private boolean c = false;

    public final String toString() {
        return "OnLogCommand";
    }

    public p() {
        super(7);
    }

    public final String d() {
        return this.a;
    }

    public final void b(String str) {
        this.a = str;
    }

    public final int e() {
        return this.b;
    }

    public final void a(int i) {
        this.b = i;
    }

    public final boolean f() {
        return this.c;
    }

    public final void a(boolean z) {
        this.c = z;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("content", this.a);
        aVar.a("log_level", this.b);
        aVar.a("is_server_log", this.c);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.a("content");
        this.b = aVar.b("log_level", 0);
        this.c = aVar.d("is_server_log");
    }
}
