package com.taobao.windvane.plugins;

import android.app.Activity;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.webview.IWVWebView;
import android.util.Log;
import android.view.View;
import com.taobao.gcanvas.GCanvasWebView;

public class GCanvasWVWebView extends GCanvasWebView {
    public static GCanvasWVWebView instance;
    private IWVWebView mWebview;

    public static void init(Activity activity, IWVWebView iWVWebView) {
        if (instance == null) {
            instance = new GCanvasWVWebView(iWVWebView);
        }
        instance.setWindVaneWebView(iWVWebView);
        Log.i("luanxuan", "before init ua");
        Log.i("luanxuan", "after init ua");
    }

    public static void unInit() {
        instance = null;
    }

    public GCanvasWVWebView(IWVWebView iWVWebView) {
        super((View) null);
        this.mWebview = iWVWebView;
    }

    public void setWindVaneWebView(IWVWebView iWVWebView) {
        initWebView(iWVWebView);
    }

    private void initWebView(IWVWebView iWVWebView) {
        if (iWVWebView instanceof WVUCWebView) {
            setWebView(((WVUCWebView) iWVWebView).getCoreView());
        }
        setWebView(iWVWebView.getView());
    }

    public String getUserAgentString() {
        return this.mWebview.getUserAgentString();
    }

    public void setUserAgentString(String str) {
        this.mWebview.setUserAgentString(str);
    }

    public String getOriginalUrl() {
        return this.mWebview.getUrl();
    }

    public String getUrl() {
        return this.mWebview.getUrl();
    }
}
