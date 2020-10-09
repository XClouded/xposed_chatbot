package com.uc.webview.export.internal.setup;

import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: U4Source */
final class ag implements Runnable {
    final /* synthetic */ int a;

    ag(int i) {
        this.a = i;
    }

    public final void run() {
        IWaStat.WaStat.stat("core_sust", "st_" + this.a + ":1", 0);
    }
}
