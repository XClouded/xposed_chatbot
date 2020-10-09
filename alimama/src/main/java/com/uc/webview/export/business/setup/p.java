package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.utility.SetupTask;
import java.util.Map;

/* compiled from: U4Source */
final class p implements ValueCallback<l> {
    final ValueCallback<SetupTask> a = ((ValueCallback) this.b.getValue());
    final /* synthetic */ Map.Entry b;
    final /* synthetic */ o c;

    p(o oVar, Map.Entry entry) {
        this.c = oVar;
        this.b = entry;
    }

    public final /* bridge */ /* synthetic */ void onReceiveValue(Object obj) {
        this.a.onReceiveValue(this.c);
    }
}
