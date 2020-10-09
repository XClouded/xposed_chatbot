package android.taobao.windvane.jsbridge;

import android.taobao.windvane.webview.IWVWebView;

public abstract class WVJSAPIPageAuth implements WVJSAPIAuthCheck {
    /* access modifiers changed from: protected */
    public boolean needAuth(IWVWebView iWVWebView) {
        return false;
    }
}
