package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
final class bi implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bi(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        boolean a2 = g.a(af.a, this.a.e, this.a.f.mOptions);
        br brVar = this.a.e;
        ConcurrentHashMap<String, Object> concurrentHashMap = this.a.f.mOptions;
        Context context = af.a;
        g.a(brVar, concurrentHashMap, a2, this.a.b, false);
        return Integer.valueOf(ae.e.c);
    }
}
