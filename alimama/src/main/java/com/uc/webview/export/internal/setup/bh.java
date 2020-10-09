package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bh implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bh(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        h.a(af.a, Integer.valueOf(this.a.a), (String) this.a.e.coreImplModule.first);
        return Integer.valueOf(ae.e.c);
    }
}
