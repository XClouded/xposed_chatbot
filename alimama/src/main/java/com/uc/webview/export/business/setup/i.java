package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.business.a;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.BaseSetupTask;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class i implements ValueCallback<BaseSetupTask> {
    final /* synthetic */ a a;

    i(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        BaseSetupTask baseSetupTask = (BaseSetupTask) obj;
        String a2 = a.a;
        Log.d(a2, "mExceptionCallback " + baseSetupTask.getException().errCode() + ":" + baseSetupTask.getException().getMessage());
        this.a.c.a(a.d.g);
        baseSetupTask.getException().errCode();
        a.b();
        a.a(this.a, UCCore.EVENT_EXCEPTION, baseSetupTask);
    }
}
