package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.az;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class ba implements ValueCallback<Object> {
    final /* synthetic */ az.a a;

    ba(az.a aVar) {
        this.a = aVar;
    }

    public final void onReceiveValue(Object obj) {
        Log.d("ThickSetupTask", "task observer : " + obj);
        if (obj instanceof UCSetupException) {
            if (az.a.d() != ae.d.b) {
                this.a.f.setException((UCSetupException) obj);
                return;
            }
            throw ((UCSetupException) obj);
        } else if (obj instanceof ae.c) {
            ae.c cVar = (ae.c) obj;
            if (cVar.c == ae.f.c && ae.b.CHECK_OLD_KERNEL.equals(cVar.a)) {
                this.a.b();
            }
        }
    }
}
