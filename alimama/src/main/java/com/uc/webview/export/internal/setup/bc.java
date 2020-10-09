package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.az;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bc implements Callable<Object> {
    final /* synthetic */ Context a;
    final /* synthetic */ boolean b;
    final /* synthetic */ az.a c;

    bc(az.a aVar, Context context, boolean z) {
        this.c = aVar;
        this.a = context;
        this.b = z;
    }

    public final Object call() throws Exception {
        g.a(this.c.e, this.c.f.mOptions, g.a(this.a, this.c.e, this.c.f.mOptions), this.b, true);
        return Integer.valueOf(ae.e.c);
    }
}
