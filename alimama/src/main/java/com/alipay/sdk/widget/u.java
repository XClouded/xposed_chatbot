package com.alipay.sdk.widget;

import java.util.Iterator;
import java.util.Stack;

public class u {
    private Stack<WebViewWindow> a = new Stack<>();

    public WebViewWindow a() {
        return this.a.pop();
    }

    public void a(WebViewWindow webViewWindow) {
        this.a.push(webViewWindow);
    }

    public boolean b() {
        return this.a.isEmpty();
    }

    public void c() {
        if (!b()) {
            Iterator it = this.a.iterator();
            while (it.hasNext()) {
                ((WebViewWindow) it.next()).a();
            }
            this.a.clear();
        }
    }
}
