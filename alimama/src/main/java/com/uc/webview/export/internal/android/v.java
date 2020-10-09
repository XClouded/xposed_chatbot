package com.uc.webview.export.internal.android;

import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import java.util.Iterator;

/* compiled from: U4Source */
final class v implements Runnable {
    final /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    public final void run() {
        boolean z;
        Iterator it = u.a.iterator();
        while (true) {
            if (it.hasNext()) {
                if (((IWebView) it.next()).getView().getWindowVisibility() == 0) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        if (!z && u.d == 1) {
            if (IWaStat.WaStat.getPrintLogEnable()) {
                Log.d("SDKWaStat", "WebViewDetector:onPause");
            }
            IWaStat.WaStat.saveData();
            int unused = u.d = 0;
        }
    }
}
