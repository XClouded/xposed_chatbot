package com.uc.webview.export.internal.android;

import android.annotation.TargetApi;
import android.os.Handler;
import com.uc.webview.export.WebMessage;
import com.uc.webview.export.WebMessagePort;

@TargetApi(23)
/* compiled from: U4Source */
public final class n extends WebMessagePort {
    android.webkit.WebMessagePort a;

    public final void postMessage(WebMessage webMessage) {
    }

    n(android.webkit.WebMessagePort webMessagePort) {
        this.a = webMessagePort;
    }

    public final void close() {
        this.a.close();
    }

    public final void setWebMessageCallback(WebMessagePort.WebMessageCallback webMessageCallback) {
        setWebMessageCallback(webMessageCallback, (Handler) null);
    }

    public final void setWebMessageCallback(WebMessagePort.WebMessageCallback webMessageCallback, Handler handler) {
        this.a.setWebMessageCallback(new o(this));
    }
}
