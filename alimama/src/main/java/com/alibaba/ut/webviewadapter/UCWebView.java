package com.alibaba.ut.webviewadapter;

import android.content.Context;
import android.webkit.ValueCallback;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.utils.Logger;
import com.uc.webview.export.WebView;

public class UCWebView implements IWebView {
    WebView mWebView = null;

    public UCWebView(WebView webView) {
        this.mWebView = webView;
    }

    public boolean post(Runnable runnable) {
        return this.mWebView.post(runnable);
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        this.mWebView.evaluateJavascript(str, valueCallback);
    }

    public void addJavascriptInterface(Object obj, String str) {
        this.mWebView.addJavascriptInterface(obj, str);
        Logger.e((String) null, "mWebview" + this.mWebView);
    }

    public Context getContext() {
        return this.mWebView.getContext();
    }

    public int getDelegateHashCode() {
        return this.mWebView.hashCode();
    }

    public void loadUrl(String str) {
        this.mWebView.loadUrl(str);
    }
}
