package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.az;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bd implements Callable<Object> {
    final /* synthetic */ Context a;
    final /* synthetic */ Integer b;
    final /* synthetic */ az.a c;

    bd(az.a aVar, Context context, Integer num) {
        this.c = aVar;
        this.a = context;
        this.b = num;
    }

    public final Object call() throws Exception {
        g.a(this.c.e, this.a, br.class.getClassLoader(), this.b.intValue());
        return Integer.valueOf(ae.e.c);
    }
}
