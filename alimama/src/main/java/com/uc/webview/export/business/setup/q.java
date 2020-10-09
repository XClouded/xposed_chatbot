package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.business.a;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class q implements ValueCallback<l> {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ o c;

    q(o oVar, String str, String str2) {
        this.c = oVar;
        this.a = str;
        this.b = str2;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d(o.a, "setup callback.");
        lVar.stop();
        this.c.b.a(a.c.g);
        if (k.a((Boolean) this.c.getOption("o_flag_odex_done"))) {
            o.a(this.a, this.b);
        }
        o.a(this.c, lVar);
    }
}
