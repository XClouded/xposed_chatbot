package com.uc.webview.export;

import android.webkit.ValueCallback;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.ICookieManager;
import java.util.HashMap;

@Api
/* compiled from: U4Source */
public class CookieManager {
    private static HashMap<Integer, CookieManager> a;
    private ICookieManager b;

    private CookieManager(ICookieManager iCookieManager) {
        this.b = iCookieManager;
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("doesn't implement Cloneable");
    }

    public static CookieManager getInstance() {
        return a(SDKFactory.d());
    }

    public static CookieManager getInstance(WebView webView) {
        return a(webView.getCurrentViewCoreType());
    }

    private static synchronized CookieManager a(int i) throws RuntimeException {
        CookieManager cookieManager;
        synchronized (CookieManager.class) {
            if (a == null) {
                a = new HashMap<>();
            }
            cookieManager = a.get(Integer.valueOf(i));
            if (cookieManager == null) {
                cookieManager = new CookieManager(SDKFactory.b(i));
                a.put(Integer.valueOf(i), cookieManager);
            }
        }
        return cookieManager;
    }

    public void setAcceptCookie(boolean z) {
        this.b.setAcceptCookie(z);
    }

    public boolean acceptCookie() {
        return this.b.acceptCookie();
    }

    public void setCookie(String str, String str2) {
        this.b.setCookie(str, str2);
    }

    public String getCookie(String str) {
        return this.b.getCookie(str);
    }

    public boolean hasCookies() {
        return this.b.hasCookies();
    }

    public void setCookie(String str, String str2, ValueCallback<Boolean> valueCallback) {
        this.b.setCookie(str, str2, valueCallback);
    }

    public void setAcceptThirdPartyCookies(WebView webView, boolean z) {
        this.b.setAcceptThirdPartyCookies(webView, z);
    }

    public boolean acceptThirdPartyCookies(WebView webView) {
        return this.b.acceptThirdPartyCookies(webView);
    }

    public static boolean allowFileSchemeCookies() {
        return getInstance().b.allowFileSchemeCookiesImpl();
    }

    public static void setAcceptFileSchemeCookies(boolean z) {
        getInstance().b.setAcceptFileSchemeCookiesImpl(z);
    }

    public void flush() {
        this.b.flush();
    }

    public void removeAllCookies(ValueCallback<Boolean> valueCallback) {
        this.b.removeAllCookies(valueCallback);
    }

    public void removeSessionCookies(ValueCallback<Boolean> valueCallback) {
        this.b.removeSessionCookies(valueCallback);
    }

    public String toString() {
        return "CookieManager@" + hashCode() + Operators.ARRAY_START_STR + this.b + Operators.ARRAY_END_STR;
    }
}
