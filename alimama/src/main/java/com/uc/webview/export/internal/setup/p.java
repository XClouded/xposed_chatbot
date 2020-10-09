package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.utility.SetupTask;

/* compiled from: U4Source */
final class p implements ValueCallback<l> {
    final /* synthetic */ o a;

    p(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        if (lVar.getLoadedUCM() == null) {
            this.a.setException(new UCSetupException(4001, String.format("Task [%s] report success but no loaded UCM.", new Object[]{lVar.getClass().getName()})));
            return;
        }
        UCMRunningInfo loadedUCM = lVar.getLoadedUCM();
        o.a(this.a, loadedUCM);
        try {
            new UCAsyncTask((Runnable) new q(this, lVar, loadedUCM)).setParent(SetupTask.getRoot()).start();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        af.a(5, new Object[0]);
    }
}
