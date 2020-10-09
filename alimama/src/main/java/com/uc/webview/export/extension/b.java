package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.l;

/* compiled from: U4Source */
final class b implements ValueCallback<l> {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UCCore.a(UCCore.EVENT_UPDATE_PROGRESS, (l) obj, this.a.b);
    }
}
