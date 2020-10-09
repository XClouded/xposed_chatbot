package com.taobao.monitor.impl.data;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

public class DefaultWebView extends AbsWebView {
    public static final DefaultWebView INSTANCE = new DefaultWebView();
    private String lastUrl;

    private DefaultWebView() {
    }

    public boolean isWebView(View view) {
        return view instanceof WebView;
    }

    public int getProgress(View view) {
        WebView webView = (WebView) view;
        String url = webView.getUrl();
        if (TextUtils.equals(this.lastUrl, url)) {
            return webView.getProgress();
        }
        this.lastUrl = url;
        return 0;
    }
}
