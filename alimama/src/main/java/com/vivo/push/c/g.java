package com.vivo.push.c;

import com.vivo.push.util.p;
import com.vivo.push.y;

/* compiled from: OnClearCacheReceiveTask */
final class g extends ab {
    g(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        p.d("OnClearCacheTask", "delete push info " + this.a.getPackageName());
        com.vivo.push.util.y.b(this.a).a();
    }
}
