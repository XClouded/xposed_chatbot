package com.ali.user.mobile.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.util.AttributeSet;
import com.ali.user.mobile.log.TLogAdapter;
import com.uc.webview.export.WebSettings;
import java.lang.reflect.Method;

public class LoginWebView extends WVUCWebView {
    private static String TAG = "login.LoginWebView";

    public LoginWebView(Context context) {
        super(context);
    }

    public LoginWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LoginWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void init() {
        setVerticalScrollbarOverlay(true);
        WebSettings settings = getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(this.context.getDir("cache", 0).getPath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);
        settings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= 7) {
            try {
                Method method = settings.getClass().getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                if (method != null) {
                    method.invoke(settings, new Object[]{true});
                }
            } catch (Exception e) {
                TLogAdapter.e(TAG, "2.2 setDomStorageEnabled Failed!", e);
            }
        }
        try {
            this.mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            this.mWebView.removeJavascriptInterface("accessibility");
            this.mWebView.removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
