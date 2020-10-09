package com.uc.webview.export.internal;

import android.content.Context;

/* compiled from: U4Source */
final class b implements Runnable {
    final /* synthetic */ Context a;

    b(Context context) {
        this.a = context;
    }

    public final void run() {
        SDKFactory.g(this.a);
    }
}
