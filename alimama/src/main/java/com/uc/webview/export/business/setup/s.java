package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.business.a;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class s implements ValueCallback<l> {
    final /* synthetic */ o a;

    s(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        String a2 = o.a;
        Log.d(a2, "exception " + lVar);
        this.a.b.a(a.c.h);
        o.a(this.a, lVar);
    }
}
