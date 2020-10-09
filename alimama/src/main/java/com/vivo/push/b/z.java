package com.vivo.push.b;

import com.vivo.push.a;
import com.vivo.push.y;

/* compiled from: PushModeCommand */
public final class z extends y {
    private int a = 0;

    public final boolean c() {
        return true;
    }

    public final String toString() {
        return "PushModeCommand";
    }

    public z() {
        super(2011);
    }

    public final int d() {
        return this.a;
    }

    public final void a(int i) {
        this.a = i;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        aVar.a("com.bbk.push.ikey.MODE_TYPE", this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        this.a = aVar.b("com.bbk.push.ikey.MODE_TYPE", 0);
    }
}
