package com.uc.webview.export.internal.uc;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.c;

/* compiled from: U4Source */
public final class b extends com.uc.webview.export.internal.a {
    static Runnable f = new c();
    /* access modifiers changed from: private */
    public static c g;

    /* compiled from: U4Source */
    public class a implements c.a {
        private Context b;

        public a(Context context) {
            this.b = context.getApplicationContext();
        }

        public final void a() {
            Log.d("WebViewDetector", "onScreenOn: onScreenOn");
            c unused = b.g;
            if (!c.a(this.b) && SDKFactory.d != null) {
                SDKFactory.d.onScreenUnLock();
                SDKFactory.d.onResume();
                Log.d("WebViewDetector", "onScreenOn: onScreenUnLock");
            }
        }

        public final void b() {
            Log.d("WebViewDetector", "onScreenOff: onScreenOff");
            if (SDKFactory.d != null) {
                SDKFactory.d.onScreenLock();
                SDKFactory.d.onPause();
                Log.d("WebViewDetector", "onScreenOff: onScreenLock");
            }
        }

        public final void c() {
            Log.d("WebViewDetector", "onUserPresent: onUserPresent");
            if (SDKFactory.d != null) {
                SDKFactory.d.onScreenUnLock();
                SDKFactory.d.onResume();
                Log.d("WebViewDetector", "onUserPresent: onScreenUnLock");
            }
        }
    }

    public b(Context context) {
        if (!SDKFactory.f && g == null) {
            c cVar = new c(context);
            g = cVar;
            cVar.b = new a(context);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            cVar.a.registerReceiver(cVar, intentFilter);
            if (c.a((PowerManager) cVar.a.getSystemService("power"))) {
                if (cVar.b != null) {
                    cVar.b.a();
                }
            } else if (cVar.b != null) {
                cVar.b.b();
            }
        }
    }

    public final void a(int i, int i2) {
        if (b != i || c != i2) {
            if (!SDKFactory.f && SDKFactory.d != null) {
                SDKFactory.d.onWindowSizeChanged();
            }
            b = i;
            c = i2;
        }
    }

    public final void a(IWebView iWebView, int i) {
        Log.d("WebViewDetector", "onWindowVisibilityChanged: " + i);
        iWebView.notifyForegroundChanged(i == 0);
        if (i == 0) {
            if (d != 1) {
                if (!SDKFactory.f && SDKFactory.d != null) {
                    SDKFactory.d.onResume();
                }
                Log.d("WebViewDetector", "WebViewDetector:onResume");
                d = 1;
            }
        } else if (d == 1) {
            e.removeCallbacks(f);
            e.post(f);
        }
    }

    public final void b(IWebView iWebView) {
        a.remove(iWebView);
        if (a.isEmpty()) {
            if (IWaStat.WaStat.getPrintLogEnable()) {
                Log.d("SDKWaStat", "WebViewDetector:destroy");
            }
            IWaStat.WaStat.saveData(true);
        }
    }
}
