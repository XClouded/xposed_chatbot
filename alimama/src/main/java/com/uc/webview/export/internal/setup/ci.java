package com.uc.webview.export.internal.setup;

import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class ci implements Runnable {
    final /* synthetic */ UpdateTask a;
    final /* synthetic */ ch b;

    ci(ch chVar, UpdateTask updateTask) {
        this.b = chVar;
        this.a = updateTask;
    }

    public final void run() {
        this.a.start();
    }
}
