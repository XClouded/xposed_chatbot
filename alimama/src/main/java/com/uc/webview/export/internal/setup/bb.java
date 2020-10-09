package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.az;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
final class bb implements Callable<Object> {
    final /* synthetic */ Context a;
    final /* synthetic */ az.a b;

    bb(az.a aVar, Context context) {
        this.b = aVar;
        this.a = context;
    }

    public final Object call() throws Exception {
        g.a(this.b.e, this.a, br.class.getClassLoader(), (ConcurrentHashMap<String, Object>) az.this.mOptions);
        return Integer.valueOf(ae.e.c);
    }
}
