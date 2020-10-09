package com.uc.webview.export;

import android.net.Uri;
import com.uc.webview.export.annotations.Api;
import java.util.Map;

@Api
/* compiled from: U4Source */
public class WebResourceRequest {
    String a;
    Map<String, String> b;
    Uri c;
    boolean d;
    boolean e;
    boolean f;

    public WebResourceRequest(String str, Map<String, String> map, String str2, boolean z, boolean z2) {
        this.a = str;
        this.b = map;
        this.c = Uri.parse(str2);
        this.d = z;
        this.e = z2;
    }

    public WebResourceRequest(String str, Map<String, String> map, String str2, boolean z, boolean z2, boolean z3) {
        this.f = z3;
        this.a = str;
        this.b = map;
        this.c = Uri.parse(str2);
        this.d = z;
        this.e = z2;
    }

    public WebResourceRequest(String str, Map<String, String> map, Uri uri, boolean z, boolean z2) {
        this.a = str;
        this.b = map;
        this.c = uri;
        this.d = z;
        this.e = z2;
    }

    public WebResourceRequest(String str, String str2, Map<String, String> map) {
        this.a = str;
        this.b = map;
        this.c = Uri.parse(str2);
    }

    public WebResourceRequest(String str, Map<String, String> map) {
        this("Get", str, map);
    }

    public String getMethod() {
        return this.a;
    }

    public void setMethod(String str) {
        this.a = str;
    }

    public Map<String, String> getRequestHeaders() {
        return this.b;
    }

    public void setRequestHeaders(Map<String, String> map) {
        this.b = map;
    }

    public Uri getUrl() {
        return this.c;
    }

    public void setUrl(String str) {
        this.c = Uri.parse(str);
    }

    public void setUrl(Uri uri) {
        this.c = uri;
    }

    public boolean hasGesture() {
        return this.d;
    }

    public boolean isForMainFrame() {
        return this.e;
    }

    public boolean isRedirect() {
        return this.f;
    }

    public String toString() {
        return "method=" + this.a + ",header=" + this.b + ",uri=" + this.c + ",hasGesture=" + this.d + ",isForMainFrame=" + this.e;
    }
}
