package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bj implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bj(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        h.a(af.a, Integer.valueOf(this.a.a), (String) this.a.e.sdkShellModule.first);
        return Integer.valueOf(ae.e.c);
    }
}
