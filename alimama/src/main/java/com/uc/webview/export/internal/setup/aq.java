package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class aq implements ValueCallback<l> {
    final /* synthetic */ ap a;

    aq(ap apVar) {
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Log.d(ap.b, "switch callback do nothing.");
    }
}
