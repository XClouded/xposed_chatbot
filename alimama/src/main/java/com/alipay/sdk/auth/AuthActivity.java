package com.alipay.sdk.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alipay.sdk.app.i;
import com.alipay.sdk.authjs.d;
import com.alipay.sdk.util.n;
import java.lang.ref.WeakReference;
import org.json.JSONException;

@SuppressLint({"SetJavaScriptEnabled", "DefaultLocale"})
public class AuthActivity extends Activity {
    static final String a = "params";
    static final String b = "redirectUri";
    /* access modifiers changed from: private */
    public WebView c;
    /* access modifiers changed from: private */
    public String d;
    private com.alipay.sdk.widget.a e;
    /* access modifiers changed from: private */
    public Handler f;
    /* access modifiers changed from: private */
    public boolean g;
    /* access modifiers changed from: private */
    public boolean h;

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:13|(3:15|16|(1:18))|19|20|21|22|(1:24)|26|(2:28|35)(1:36)) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0105 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x011b */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x012b A[Catch:{ Throwable -> 0x014d }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r6) {
        /*
            r5 = this;
            super.onCreate(r6)
            android.content.Intent r6 = r5.getIntent()     // Catch:{ Exception -> 0x0162 }
            android.os.Bundle r6 = r6.getExtras()     // Catch:{ Exception -> 0x0162 }
            if (r6 != 0) goto L_0x0011
            r5.finish()
            return
        L_0x0011:
            java.lang.String r0 = "redirectUri"
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Exception -> 0x015e }
            r5.d = r0     // Catch:{ Exception -> 0x015e }
            java.lang.String r0 = "params"
            java.lang.String r6 = r6.getString(r0)     // Catch:{ Exception -> 0x015e }
            boolean r0 = com.alipay.sdk.util.n.f((java.lang.String) r6)
            if (r0 != 0) goto L_0x0029
            r5.finish()
            return
        L_0x0029:
            r0 = 1
            super.requestWindowFeature(r0)
            android.os.Handler r1 = new android.os.Handler
            android.os.Looper r2 = r5.getMainLooper()
            r1.<init>(r2)
            r5.f = r1
            android.widget.LinearLayout r1 = new android.widget.LinearLayout
            r1.<init>(r5)
            android.widget.LinearLayout$LayoutParams r2 = new android.widget.LinearLayout$LayoutParams
            r3 = -1
            r2.<init>(r3, r3)
            r1.setOrientation(r0)
            r5.setContentView(r1, r2)
            android.webkit.WebView r3 = new android.webkit.WebView
            r3.<init>(r5)
            r5.c = r3
            r3 = 1065353216(0x3f800000, float:1.0)
            r2.weight = r3
            android.webkit.WebView r3 = r5.c
            r4 = 0
            r3.setVisibility(r4)
            android.webkit.WebView r3 = r5.c
            r1.addView(r3, r2)
            android.webkit.WebView r1 = r5.c
            android.webkit.WebSettings r1 = r1.getSettings()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r1.getUserAgentString()
            r2.append(r3)
            android.content.Context r3 = r5.getApplicationContext()
            java.lang.String r3 = com.alipay.sdk.util.n.c((android.content.Context) r3)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.setUserAgentString(r2)
            android.webkit.WebSettings$RenderPriority r2 = android.webkit.WebSettings.RenderPriority.HIGH
            r1.setRenderPriority(r2)
            r1.setSupportMultipleWindows(r0)
            r1.setJavaScriptEnabled(r0)
            r1.setSavePassword(r4)
            r1.setJavaScriptCanOpenWindowsAutomatically(r0)
            int r2 = r1.getMinimumFontSize()
            int r2 = r2 + 8
            r1.setMinimumFontSize(r2)
            r1.setAllowFileAccess(r4)
            r1.setAllowFileAccessFromFileURLs(r4)
            r1.setAllowUniversalAccessFromFileURLs(r4)
            r1.setAllowContentAccess(r4)
            android.webkit.WebSettings$TextSize r2 = android.webkit.WebSettings.TextSize.NORMAL
            r1.setTextSize(r2)
            android.webkit.WebView r1 = r5.c
            r1.setVerticalScrollbarOverlay(r0)
            android.webkit.WebView r1 = r5.c
            com.alipay.sdk.auth.AuthActivity$c r2 = new com.alipay.sdk.auth.AuthActivity$c
            r3 = 0
            r2.<init>(r5, r3)
            r1.setWebViewClient(r2)
            android.webkit.WebView r1 = r5.c
            com.alipay.sdk.auth.AuthActivity$b r2 = new com.alipay.sdk.auth.AuthActivity$b
            r2.<init>(r5, r3)
            r1.setWebChromeClient(r2)
            android.webkit.WebView r1 = r5.c
            com.alipay.sdk.auth.a r2 = new com.alipay.sdk.auth.a
            r2.<init>(r5)
            r1.setDownloadListener(r2)
            android.webkit.WebView r1 = r5.c
            r1.loadUrl(r6)
            int r6 = android.os.Build.VERSION.SDK_INT
            r1 = 7
            if (r6 < r1) goto L_0x0105
            android.webkit.WebView r6 = r5.c     // Catch:{ Exception -> 0x0105 }
            android.webkit.WebSettings r6 = r6.getSettings()     // Catch:{ Exception -> 0x0105 }
            java.lang.Class r6 = r6.getClass()     // Catch:{ Exception -> 0x0105 }
            java.lang.String r1 = "setDomStorageEnabled"
            java.lang.Class[] r2 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x0105 }
            java.lang.Class r3 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0105 }
            r2[r4] = r3     // Catch:{ Exception -> 0x0105 }
            java.lang.reflect.Method r6 = r6.getMethod(r1, r2)     // Catch:{ Exception -> 0x0105 }
            if (r6 == 0) goto L_0x0105
            android.webkit.WebView r1 = r5.c     // Catch:{ Exception -> 0x0105 }
            android.webkit.WebSettings r1 = r1.getSettings()     // Catch:{ Exception -> 0x0105 }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0105 }
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0105 }
            r2[r4] = r3     // Catch:{ Exception -> 0x0105 }
            r6.invoke(r1, r2)     // Catch:{ Exception -> 0x0105 }
        L_0x0105:
            android.webkit.WebView r6 = r5.c     // Catch:{ Throwable -> 0x011b }
            java.lang.String r1 = "searchBoxJavaBridge_"
            r6.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x011b }
            android.webkit.WebView r6 = r5.c     // Catch:{ Throwable -> 0x011b }
            java.lang.String r1 = "accessibility"
            r6.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x011b }
            android.webkit.WebView r6 = r5.c     // Catch:{ Throwable -> 0x011b }
            java.lang.String r1 = "accessibilityTraversal"
            r6.removeJavascriptInterface(r1)     // Catch:{ Throwable -> 0x011b }
            goto L_0x014e
        L_0x011b:
            android.webkit.WebView r6 = r5.c     // Catch:{ Throwable -> 0x014d }
            java.lang.Class r6 = r6.getClass()     // Catch:{ Throwable -> 0x014d }
            java.lang.String r1 = "removeJavascriptInterface"
            java.lang.Class[] r2 = new java.lang.Class[r4]     // Catch:{ Throwable -> 0x014d }
            java.lang.reflect.Method r6 = r6.getMethod(r1, r2)     // Catch:{ Throwable -> 0x014d }
            if (r6 == 0) goto L_0x014e
            android.webkit.WebView r1 = r5.c     // Catch:{ Throwable -> 0x014d }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x014d }
            java.lang.String r3 = "searchBoxJavaBridge_"
            r2[r4] = r3     // Catch:{ Throwable -> 0x014d }
            r6.invoke(r1, r2)     // Catch:{ Throwable -> 0x014d }
            android.webkit.WebView r1 = r5.c     // Catch:{ Throwable -> 0x014d }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x014d }
            java.lang.String r3 = "accessibility"
            r2[r4] = r3     // Catch:{ Throwable -> 0x014d }
            r6.invoke(r1, r2)     // Catch:{ Throwable -> 0x014d }
            android.webkit.WebView r1 = r5.c     // Catch:{ Throwable -> 0x014d }
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x014d }
            java.lang.String r3 = "accessibilityTraversal"
            r2[r4] = r3     // Catch:{ Throwable -> 0x014d }
            r6.invoke(r1, r2)     // Catch:{ Throwable -> 0x014d }
            goto L_0x014e
        L_0x014d:
        L_0x014e:
            int r6 = android.os.Build.VERSION.SDK_INT
            r1 = 19
            if (r6 < r1) goto L_0x015d
            android.webkit.WebView r6 = r5.c
            android.webkit.WebSettings r6 = r6.getSettings()
            r6.setCacheMode(r0)
        L_0x015d:
            return
        L_0x015e:
            r5.finish()
            return
        L_0x0162:
            r5.finish()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.auth.AuthActivity.onCreate(android.os.Bundle):void");
    }

    public void onBackPressed() {
        if (!this.c.canGoBack()) {
            g.a((Activity) this, this.d + "?resultCode=150");
            finish();
        } else if (this.h) {
            g.a((Activity) this, this.d + "?resultCode=150");
            finish();
        }
    }

    private class c extends WebViewClient {
        private c() {
        }

        /* synthetic */ c(AuthActivity authActivity, a aVar) {
            this();
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            boolean unused = AuthActivity.this.h = true;
            super.onReceivedError(webView, i, str, str2);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            if (AuthActivity.this.g) {
                sslErrorHandler.proceed();
                boolean unused = AuthActivity.this.g = false;
                return;
            }
            AuthActivity.this.runOnUiThread(new d(this, sslErrorHandler));
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.toLowerCase().startsWith(com.alipay.sdk.cons.a.j.toLowerCase()) || str.toLowerCase().startsWith(com.alipay.sdk.cons.a.k.toLowerCase())) {
                try {
                    n.a a2 = n.a((Context) AuthActivity.this, i.a);
                    if (a2 != null && !a2.a()) {
                        if (!a2.b()) {
                            if (str.startsWith("intent://platformapi/startapp")) {
                                str = str.replaceFirst(com.alipay.sdk.cons.a.k, com.alipay.sdk.cons.a.j);
                            }
                            AuthActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                            return true;
                        }
                    }
                    return true;
                } catch (Throwable unused) {
                }
            } else if (!AuthActivity.this.a(str)) {
                return super.shouldOverrideUrlLoading(webView, str);
            } else {
                webView.stopLoading();
                return true;
            }
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            AuthActivity.this.a();
            AuthActivity.this.f.postDelayed(new a(AuthActivity.this, (a) null), 30000);
            super.onPageStarted(webView, str, bitmap);
        }

        public void onPageFinished(WebView webView, String str) {
            AuthActivity.this.b();
            AuthActivity.this.f.removeCallbacksAndMessages((Object) null);
        }
    }

    private class b extends WebChromeClient {
        private b() {
        }

        /* synthetic */ b(AuthActivity authActivity, a aVar) {
            this();
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String message = consoleMessage.message();
            if (TextUtils.isEmpty(message)) {
                return super.onConsoleMessage(consoleMessage);
            }
            String str = null;
            if (message.startsWith("h5container.message: ")) {
                str = message.replaceFirst("h5container.message: ", "");
            }
            if (TextUtils.isEmpty(str)) {
                return super.onConsoleMessage(consoleMessage);
            }
            AuthActivity.this.b(str);
            return super.onConsoleMessage(consoleMessage);
        }
    }

    /* access modifiers changed from: private */
    public boolean a(String str) {
        if (TextUtils.isEmpty(str) || str.startsWith("http://") || str.startsWith("https://")) {
            return false;
        }
        if (!"SDKLite://h5quit".equalsIgnoreCase(str)) {
            if (TextUtils.equals(str, this.d)) {
                str = str + "?resultCode=150";
            }
            g.a((Activity) this, str);
        }
        finish();
        return true;
    }

    /* access modifiers changed from: private */
    public void b(String str) {
        new d(getApplicationContext(), new b(this)).a(str);
    }

    /* access modifiers changed from: private */
    public void a(com.alipay.sdk.authjs.a aVar) {
        if (this.c != null && aVar != null) {
            try {
                runOnUiThread(new c(this, String.format("AlipayJSBridge._invokeJS(%s)", new Object[]{aVar.g()})));
            } catch (JSONException e2) {
                com.alipay.sdk.util.c.a("msp", (Throwable) e2);
            }
        }
    }

    private static final class a implements Runnable {
        private final WeakReference<AuthActivity> a;

        /* synthetic */ a(AuthActivity authActivity, a aVar) {
            this(authActivity);
        }

        private a(AuthActivity authActivity) {
            this.a = new WeakReference<>(authActivity);
        }

        public void run() {
            AuthActivity authActivity = (AuthActivity) this.a.get();
            if (authActivity != null) {
                authActivity.b();
            }
        }
    }

    /* access modifiers changed from: private */
    public void a() {
        try {
            if (this.e == null) {
                this.e = new com.alipay.sdk.widget.a(this, com.alipay.sdk.widget.a.a);
            }
            this.e.b();
        } catch (Exception unused) {
            this.e = null;
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        if (this.e != null) {
            this.e.c();
        }
        this.e = null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.c != null) {
            this.c.removeAllViews();
            try {
                this.c.destroy();
            } catch (Throwable unused) {
            }
            this.c = null;
        }
    }
}
