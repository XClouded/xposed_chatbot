package com.ali.user.mobile.login.ui;

import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.util.Log;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;
import com.uc.webview.export.WebSettings;
import java.lang.reflect.Method;

public class WebUrlHelper {
    protected static final String CALLBACK = "https://www.alipay.com/webviewbridge";

    public boolean checkWebviewBridge(String str) {
        Uri parse = Uri.parse(str);
        StringBuilder sb = new StringBuilder();
        sb.append(parse.getAuthority());
        sb.append(parse.getPath());
        return CALLBACK.contains(sb.toString());
    }

    public static void setWebView(WVUCWebView wVUCWebView) {
        wVUCWebView.setVerticalScrollbarOverlay(true);
        WebSettings settings = wVUCWebView.getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(DataProviderFactory.getApplicationContext().getDir("cache", 0).getPath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);
        settings.setBuiltInZoomControls(false);
        Log.i("", "user agent=" + settings.getUserAgentString());
        if (Build.VERSION.SDK_INT >= 7) {
            try {
                Method method = wVUCWebView.getSettings().getClass().getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                if (method != null) {
                    method.invoke(wVUCWebView.getSettings(), new Object[]{true});
                }
            } catch (Exception e) {
                TLogAdapter.e("", "2.2 setDomStorageEnabled Failed!", e);
            }
        }
        try {
            wVUCWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            wVUCWebView.removeJavascriptInterface("accessibility");
            wVUCWebView.removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        wVUCWebView.setBackgroundColor(0);
        wVUCWebView.getBackground().setAlpha(0);
    }
}
