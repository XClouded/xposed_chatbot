package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class d implements ValueCallback<l> {
    final ValueCallback a = this.b.getCallback("switch");
    final /* synthetic */ b b;

    d(b bVar) {
        this.b = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d("DecompressSetupTask", "switch callback.");
        if (this.a != null) {
            this.a.onReceiveValue(lVar);
        }
    }
}
