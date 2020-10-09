package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class z implements ValueCallback<l> {
    final /* synthetic */ o a;

    z(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        if (((l) obj) instanceof ao) {
            Log.d("SdkSetupTask", "ShareCoreSdcardSetupTask.EVENT_DELAY_SEARCH_CORE_FILE callback");
            l unused = this.a.e = o.i(this.a);
        }
    }
}
