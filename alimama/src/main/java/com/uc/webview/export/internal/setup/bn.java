package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bn implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bn(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        g.b(this.a.e, af.a, this.a.e.mSdkShellClassLoader, this.a.a);
        return Integer.valueOf(ae.e.c);
    }
}
