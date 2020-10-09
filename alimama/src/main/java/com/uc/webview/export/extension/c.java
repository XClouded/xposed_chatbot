package com.uc.webview.export.extension;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.l;

/* compiled from: U4Source */
final class c implements ValueCallback<l> {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UCCore.a(UCCore.EVENT_DOWNLOAD_EXCEPTION, (l) obj, this.a.b);
    }
}
