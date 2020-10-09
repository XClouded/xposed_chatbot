package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;

/* compiled from: U4Source */
final class bt implements ValueCallback {
    bt() {
    }

    public final void onReceiveValue(Object obj) {
        SDKFactory.j = false;
        SDKFactory.f();
    }
}
