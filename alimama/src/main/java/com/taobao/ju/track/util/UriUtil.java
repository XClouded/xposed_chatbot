package com.taobao.ju.track.util;

import android.net.Uri;

public class UriUtil {
    public static String getString(Uri uri, String str) {
        if (str == null || uri == null || uri.getQueryParameter(str) == null) {
            return null;
        }
        return uri.getQueryParameter(str);
    }

    public static String getString(Uri uri, String str, String str2) {
        return (str == null || uri == null || uri.getQueryParameter(str) == null) ? str2 : uri.getQueryParameter(str);
    }
}
