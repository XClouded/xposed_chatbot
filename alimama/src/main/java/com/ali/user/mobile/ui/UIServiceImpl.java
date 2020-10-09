package com.ali.user.mobile.ui;

import android.content.Context;
import com.ali.user.mobile.service.UIService;
import com.ali.user.mobile.webview.WebViewActivity;

public class UIServiceImpl implements UIService {
    public boolean finishWebViewActivity(Context context) {
        if (!(context instanceof WebViewActivity)) {
            return false;
        }
        ((WebViewActivity) context).finish();
        return true;
    }

    public boolean isWebViewActivity(Context context) {
        return context instanceof WebViewActivity;
    }

    public void switchWebViewTitleBarRightButton(Context context, boolean z, String str) {
        if (context instanceof WebViewActivity) {
            ((WebViewActivity) context).switchHelpMenu(z, str);
        }
    }

    public void setWebViewTitleBarVisibility(Context context, boolean z) {
        WebViewActivity webViewActivity;
        if ((context instanceof WebViewActivity) && (webViewActivity = (WebViewActivity) context) != null) {
            try {
                if (webViewActivity.getSupportActionBar() == null) {
                    return;
                }
                if (z) {
                    webViewActivity.getSupportActionBar().show();
                } else {
                    webViewActivity.getSupportActionBar().hide();
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
