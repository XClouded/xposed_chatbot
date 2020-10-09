package com.alipay.sdk.widget;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

class s extends WebChromeClient {
    final /* synthetic */ WebViewWindow a;

    s(WebViewWindow webViewWindow) {
        this.a = webViewWindow;
    }

    public void onProgressChanged(WebView webView, int i) {
        if (i == 100) {
            this.a.d.setVisibility(4);
            return;
        }
        if (4 == this.a.d.getVisibility()) {
            this.a.d.setVisibility(0);
        }
        this.a.d.setProgress(i);
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        return this.a.g.a(this.a, str, str2, str3, jsPromptResult);
    }

    public void onReceivedTitle(WebView webView, String str) {
        this.a.g.a(this.a, str);
    }
}
