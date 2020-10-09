package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;

/* compiled from: U4Source */
final class bu implements ValueCallback {
    bu() {
    }

    public final void onReceiveValue(Object obj) {
        SDKFactory.j = true;
        SDKFactory.k = true;
    }
}
