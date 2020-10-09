package com.alibaba.ut.webviewadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.utils.Logger;

public class SystemWebView implements IWebView {
    private final WebView mWebview;

    public SystemWebView(WebView webView) {
        this.mWebview = webView;
    }

    public boolean post(Runnable runnable) {
        return this.mWebview.post(runnable);
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mWebview.evaluateJavascript(str, valueCallback);
            return;
        }
        WebView webView = this.mWebview;
        webView.loadUrl("javascript:" + str);
    }

    @SuppressLint({"JavascriptInterface"})
    public void addJavascriptInterface(Object obj, String str) {
        this.mWebview.addJavascriptInterface(obj, str);
        Logger.e((String) null, "mWebview" + this.mWebview);
    }

    public Context getContext() {
        return this.mWebview.getContext();
    }

    public int getDelegateHashCode() {
        return this.mWebview.hashCode();
    }

    public void loadUrl(String str) {
        this.mWebview.loadUrl(str);
    }
}
