package com.alibaba.ut.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.alibaba.ut.IWebView;
import com.alibaba.ut.TrackerWebView;
import com.alibaba.ut.webviewadapter.SystemWebView;
import com.alibaba.ut.webviewadapter.UCWebView;

public class ViewTools {
    public static IWebView findWebView(Activity activity) {
        TrackerWebView trackerWebView = (TrackerWebView) activity.getClass().getAnnotation(TrackerWebView.class);
        WebView webView = trackerWebView != null ? (WebView) activity.findViewById(trackerWebView.value()) : null;
        if (webView != null) {
            return new SystemWebView(webView);
        }
        return traceWebView(activity.getWindow().getDecorView());
    }

    private static IWebView traceWebView(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof WebView) {
            return new SystemWebView((WebView) view);
        }
        if (isUcWebView(view.getClass())) {
            return new UCWebView((com.uc.webview.export.WebView) view);
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            IWebView traceWebView = traceWebView(viewGroup.getChildAt(i));
            if (traceWebView != null) {
                return traceWebView;
            }
        }
        return null;
    }

    private static boolean isUcWebView(Class cls) {
        if (cls == null || "android.view.View".equalsIgnoreCase(cls.getName()) || cls.getSuperclass() == null) {
            return false;
        }
        if ("com.uc.webview.export.WebView".equalsIgnoreCase(cls.getName())) {
            return true;
        }
        return isUcWebView(cls.getSuperclass());
    }
}
