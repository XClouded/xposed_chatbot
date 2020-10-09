package com.uc.webview.export;

import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IMimeTypeMap;
import java.util.HashMap;

@Api
/* compiled from: U4Source */
public class MimeTypeMap {
    private static HashMap<Integer, MimeTypeMap> a;
    private IMimeTypeMap b;

    private MimeTypeMap(IMimeTypeMap iMimeTypeMap) {
        this.b = iMimeTypeMap;
    }

    public static String getFileExtensionFromUrl(String str) {
        return getSingleton().b.getFileExtensionFromUrlEx(str);
    }

    public boolean hasMimeType(String str) {
        return this.b.hasMimeType(str);
    }

    public String getMimeTypeFromExtension(String str) {
        return this.b.getMimeTypeFromExtension(str);
    }

    public boolean hasExtension(String str) {
        return this.b.hasExtension(str);
    }

    public String getExtensionFromMimeType(String str) {
        return this.b.getExtensionFromMimeType(str);
    }

    public static MimeTypeMap getSingleton() throws RuntimeException {
        return a(SDKFactory.d());
    }

    public static MimeTypeMap getSingleton(WebView webView) throws RuntimeException {
        return a(webView.getCurrentViewCoreType());
    }

    private static synchronized MimeTypeMap a(int i) throws RuntimeException {
        MimeTypeMap mimeTypeMap;
        synchronized (MimeTypeMap.class) {
            if (a == null) {
                a = new HashMap<>();
            }
            mimeTypeMap = a.get(Integer.valueOf(i));
            if (mimeTypeMap == null) {
                mimeTypeMap = new MimeTypeMap(SDKFactory.e(i));
                a.put(Integer.valueOf(i), mimeTypeMap);
            }
        }
        return mimeTypeMap;
    }

    public String toString() {
        return "MimeTypeMap@" + hashCode() + Operators.ARRAY_START_STR + this.b + Operators.ARRAY_END_STR;
    }
}
