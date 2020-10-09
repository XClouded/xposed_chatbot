package com.alipay.sdk.widget;

import android.app.Activity;
import android.webkit.WebView;
import com.alipay.sdk.app.b;
import com.alipay.sdk.app.j;
import com.alipay.sdk.app.k;

public class h extends g {
    private b b;
    private WebView c;

    public h(Activity activity) {
        super(activity);
        this.c = new WebView(activity);
        a(this.c, activity);
        addView(this.c);
        this.b = new b(activity);
        this.c.setWebViewClient(this.b);
    }

    public boolean b() {
        if (!this.c.canGoBack()) {
            j.a(j.c());
            this.a.finish();
            return true;
        } else if (!this.b.b()) {
            return true;
        } else {
            k b2 = k.b(k.NETWORK_ERROR.a());
            j.a(j.a(b2.a(), b2.b(), ""));
            this.a.finish();
            return true;
        }
    }

    public void a() {
        this.b.a();
        removeAllViews();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(2:6|8)(1:10)) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0074 */
    /* JADX WARNING: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0084 A[Catch:{ Throwable -> 0x00a5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.webkit.WebView r5, android.content.Context r6) {
        /*
            r4 = this;
            android.webkit.WebView r5 = r4.c
            android.webkit.WebSettings r5 = r5.getSettings()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r5.getUserAgentString()
            r0.append(r1)
            java.lang.String r6 = com.alipay.sdk.util.n.c((android.content.Context) r6)
            r0.append(r6)
            java.lang.String r6 = r0.toString()
            r5.setUserAgentString(r6)
            android.webkit.WebSettings$RenderPriority r6 = android.webkit.WebSettings.RenderPriority.HIGH
            r5.setRenderPriority(r6)
            r6 = 1
            r5.setSupportMultipleWindows(r6)
            r5.setJavaScriptEnabled(r6)
            r0 = 0
            r5.setSavePassword(r0)
            r5.setJavaScriptCanOpenWindowsAutomatically(r6)
            int r1 = r5.getMinimumFontSize()
            int r1 = r1 + 8
            r5.setMinimumFontSize(r1)
            r5.setAllowFileAccess(r0)
            android.webkit.WebSettings$TextSize r1 = android.webkit.WebSettings.TextSize.NORMAL
            r5.setTextSize(r1)
            r5.setDomStorageEnabled(r6)
            r5.setCacheMode(r6)
            android.webkit.WebView r5 = r4.c
            r5.resumeTimers()
            android.webkit.WebView r5 = r4.c
            r5.setVerticalScrollbarOverlay(r6)
            android.webkit.WebView r5 = r4.c
            com.alipay.sdk.widget.i r1 = new com.alipay.sdk.widget.i
            r1.<init>(r4)
            r5.setDownloadListener(r1)
            android.webkit.WebView r5 = r4.c     // Catch:{ Throwable -> 0x0074 }
            java.lang.String r1 = "searchBoxJavaBridge_"
            r5.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x0074 }
            android.webkit.WebView r5 = r4.c     // Catch:{ Throwable -> 0x0074 }
            java.lang.String r1 = "accessibility"
            r5.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x0074 }
            android.webkit.WebView r5 = r4.c     // Catch:{ Throwable -> 0x0074 }
            java.lang.String r1 = "accessibilityTraversal"
            r5.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x0074 }
            goto L_0x00a5
        L_0x0074:
            android.webkit.WebView r5 = r4.c     // Catch:{ Throwable -> 0x00a5 }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r1 = "removeJavascriptInterface"
            java.lang.Class[] r2 = new java.lang.Class[r0]     // Catch:{ Throwable -> 0x00a5 }
            java.lang.reflect.Method r5 = r5.getMethod(r1, r2)     // Catch:{ Throwable -> 0x00a5 }
            if (r5 == 0) goto L_0x00a5
            android.webkit.WebView r1 = r4.c     // Catch:{ Throwable -> 0x00a5 }
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r3 = "searchBoxJavaBridge_"
            r2[r0] = r3     // Catch:{ Throwable -> 0x00a5 }
            r5.invoke(r1, r2)     // Catch:{ Throwable -> 0x00a5 }
            android.webkit.WebView r1 = r4.c     // Catch:{ Throwable -> 0x00a5 }
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r3 = "accessibility"
            r2[r0] = r3     // Catch:{ Throwable -> 0x00a5 }
            r5.invoke(r1, r2)     // Catch:{ Throwable -> 0x00a5 }
            android.webkit.WebView r1 = r4.c     // Catch:{ Throwable -> 0x00a5 }
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00a5 }
            java.lang.String r2 = "accessibilityTraversal"
            r6[r0] = r2     // Catch:{ Throwable -> 0x00a5 }
            r5.invoke(r1, r6)     // Catch:{ Throwable -> 0x00a5 }
        L_0x00a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.widget.h.a(android.webkit.WebView, android.content.Context):void");
    }

    public void a(String str) {
        this.c.loadUrl(str);
    }
}
