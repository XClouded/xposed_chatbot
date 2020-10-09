package com.alipay.sdk.auth;

import android.webkit.WebView;

class c implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ AuthActivity b;

    c(AuthActivity authActivity, String str) {
        this.b = authActivity;
        this.a = str;
    }

    public void run() {
        try {
            WebView f = this.b.c;
            f.loadUrl("javascript:" + this.a);
        } catch (Exception unused) {
        }
    }
}
