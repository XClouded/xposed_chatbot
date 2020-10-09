package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;

/* compiled from: U4Source */
final class ab implements ValueCallback<l> {
    final /* synthetic */ String a;
    final /* synthetic */ o b;

    ab(o oVar, String str) {
        this.b = oVar;
        this.a = str;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        try {
            new au();
            au.a(this.b.getContext().getApplicationContext(), this.a, ((bw) lVar).b);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
