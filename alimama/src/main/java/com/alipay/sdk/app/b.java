package com.alipay.sdk.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alipay.sdk.app.statistic.c;
import com.alipay.sdk.util.n;
import java.lang.ref.WeakReference;

public class b extends WebViewClient {
    @Nullable
    private Activity a;
    /* access modifiers changed from: private */
    public boolean b;
    private Handler c = new Handler(this.a.getMainLooper());
    private com.alipay.sdk.widget.a d;
    private boolean e;

    public b(@NonNull Activity activity) {
        this.a = activity;
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        this.e = true;
        super.onReceivedError(webView, i, str, str2);
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        Activity activity = this.a;
        if (activity != null) {
            com.alipay.sdk.app.statistic.a.a(c.a, c.r, "证书错误");
            if (this.b) {
                sslErrorHandler.proceed();
                this.b = false;
                return;
            }
            activity.runOnUiThread(new c(this, activity, sslErrorHandler));
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return n.a(webView, str, this.a);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        Activity activity = this.a;
        if (!(this.c == null || activity == null || activity.isFinishing())) {
            c();
            this.c.postDelayed(new a(this), 30000);
        }
        super.onPageStarted(webView, str, bitmap);
    }

    public void onPageFinished(WebView webView, String str) {
        Activity activity = this.a;
        if (this.c != null && activity != null && !activity.isFinishing()) {
            d();
            this.c.removeCallbacksAndMessages((Object) null);
        }
    }

    private void c() {
        Activity activity = this.a;
        if (activity != null) {
            if (this.d == null) {
                this.d = new com.alipay.sdk.widget.a(activity, com.alipay.sdk.widget.a.a);
                this.d.a(true);
            }
            this.d.b();
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        if (this.d != null) {
            this.d.c();
        }
        this.d = null;
    }

    private static final class a implements Runnable {
        private final WeakReference<b> a;

        a(b bVar) {
            this.a = new WeakReference<>(bVar);
        }

        public void run() {
            b bVar = (b) this.a.get();
            if (bVar != null) {
                bVar.d();
            }
        }
    }

    public void a() {
        this.c = null;
        this.a = null;
    }

    public boolean b() {
        return this.e;
    }
}
