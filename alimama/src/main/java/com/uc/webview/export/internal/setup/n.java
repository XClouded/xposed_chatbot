package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.SDKFactory;
import java.util.HashMap;

/* compiled from: U4Source */
public final class n extends UCSubSetupTask<n, n> implements ValueCallback<Pair<String, HashMap<String, String>>> {
    public final /* synthetic */ void onReceiveValue(Object obj) {
        callbackStat((Pair) obj);
    }

    public final void run() {
        SDKFactory.b = this;
    }
}
