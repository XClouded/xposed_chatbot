package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: U4Source */
final class cd implements ValueCallback<UpdateTask> {
    final /* synthetic */ bo a;
    final /* synthetic */ bw b;

    cd(bw bwVar, bo boVar) {
        this.b = bwVar;
        this.a = boVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.a.a(4, (Object) null);
    }
}
