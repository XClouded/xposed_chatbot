package com.uc.webview.export.internal.uc;

import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import java.util.Iterator;

/* compiled from: U4Source */
final class c implements Runnable {
    c() {
    }

    public final void run() {
        boolean z = false;
        try {
            Iterator it = b.a.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((IWebView) it.next()).getView().getWindowVisibility() == 0) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z && b.d == 1) {
                if (IWaStat.WaStat.getPrintLogEnable()) {
                    Log.d("WebViewDetector", "WebViewDetector:onPause");
                }
                IWaStat.WaStat.saveData();
                if (!SDKFactory.f && SDKFactory.d != null) {
                    SDKFactory.d.onPause();
                }
                int unused = b.d = 0;
            }
        } catch (Throwable unused2) {
        }
    }
}
