package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.BaseSetupTask;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class j implements ValueCallback<BaseSetupTask> {
    final /* synthetic */ a a;

    j(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        BaseSetupTask baseSetupTask = (BaseSetupTask) obj;
        String a2 = a.a;
        Log.d(a2, "mDieDelegateCallback " + baseSetupTask.toString() + " init type: " + baseSetupTask.getInitType());
        a.h(this.a);
        a.c(this.a, baseSetupTask.getInitType());
        if (k.i()) {
            IWaStat.WaStat.saveData(true);
            IWaStat.WaStat.upload();
        }
        String a3 = a.a;
        Log.d(a3, "options: " + this.a.mOptions);
    }
}
