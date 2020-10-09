package com.vivo.push.c;

import com.vivo.push.cache.ClientConfigManagerImpl;
import com.vivo.push.util.p;
import com.vivo.push.v;
import com.vivo.push.y;

/* compiled from: InitTask */
final class c extends v {
    c(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        p.a(ClientConfigManagerImpl.getInstance(this.a).isDebug());
    }
}
