package com.alipay.sdk.widget;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class t extends WebViewClient {
    final /* synthetic */ WebViewWindow a;

    t(WebViewWindow webViewWindow) {
        this.a = webViewWindow;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!this.a.h.b(this.a, str)) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        return true;
    }

    public void onPageFinished(WebView webView, String str) {
        if (!this.a.h.c(this.a, str)) {
            super.onPageFinished(webView, str);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (!this.a.h.a(this.a, i, str, str2)) {
            super.onReceivedError(webView, i, str, str2);
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        if (!this.a.h.a(this.a, sslErrorHandler, sslError)) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
}
