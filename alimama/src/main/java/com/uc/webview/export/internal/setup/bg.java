package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class bg implements ValueCallback<Object> {
    final /* synthetic */ bf.a a;

    bg(bf.a aVar) {
        this.a = aVar;
    }

    public final void onReceiveValue(Object obj) {
        Log.d("ThinSetupTask", "task observer : " + obj);
        if (obj instanceof UCSetupException) {
            if (bf.a.d() != ae.d.b) {
                this.a.f.setException((UCSetupException) obj);
                return;
            }
            throw ((UCSetupException) obj);
        } else if (obj instanceof ae.c) {
            ae.c cVar = (ae.c) obj;
            if (cVar.c != ae.f.c) {
                return;
            }
            if (ae.b.LOAD_SDK_SHELL.equals(cVar.a)) {
                bf.a.a(this.a);
                bf.a.b(this.a);
                bf.a.c(this.a);
            } else if (ae.b.VERIFY_CORE_JAR.equals(cVar.a)) {
                this.a.b();
            }
        }
    }
}
