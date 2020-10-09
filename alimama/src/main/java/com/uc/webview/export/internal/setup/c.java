package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class c implements ValueCallback<l> {
    final ValueCallback<l> a = this.b.getCallback(UCCore.LEGACY_EVENT_SETUP);
    final /* synthetic */ b b;

    c(b bVar) {
        this.b = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d("DecompressSetupTask", "setup callback.");
        if (this.a != null) {
            this.a.onReceiveValue(lVar);
        } else if (!k.a(UCSetupTask.getTotalLoadedUCM())) {
            lVar.stop();
        }
    }
}
