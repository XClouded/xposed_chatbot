package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class bx implements ValueCallback<l> {
    final /* synthetic */ bw a;

    bx(bw bwVar) {
        this.a = bwVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d(bw.e, "setup callback.");
        if (!k.a(UCSetupTask.getTotalLoadedUCM())) {
            lVar.stop();
        }
    }
}
