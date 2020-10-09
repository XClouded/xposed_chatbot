package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class ca implements ValueCallback<l> {
    final ValueCallback a = this.b.getCallback("switch");
    final /* synthetic */ bw b;

    ca(bw bwVar) {
        this.b = bwVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d(bw.e, "switch callback.");
        if (this.a != null) {
            this.a.onReceiveValue(lVar);
        }
    }
}
