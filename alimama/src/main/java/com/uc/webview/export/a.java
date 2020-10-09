package com.uc.webview.export;

import java.util.HashMap;
import java.util.Locale;

/* compiled from: U4Source */
final class a extends HashMap<String, String> {
    a() {
        put("export.webview", String.format(Locale.CHINA, "total:%d, u4:%d, system:%d", new Object[]{Integer.valueOf(WebView.j.get()), Integer.valueOf(WebView.l.get()), Integer.valueOf(WebView.k.get())}));
    }
}
