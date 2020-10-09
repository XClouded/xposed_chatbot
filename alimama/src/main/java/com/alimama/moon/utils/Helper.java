package com.alimama.moon.utils;

import android.net.Uri;
import java.util.Set;

public final class Helper {
    public static Uri appendQueryParameter(Uri uri, String str, String str2) {
        String str3;
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        Uri.Builder clearQuery = uri.buildUpon().clearQuery();
        boolean z = false;
        for (String next : queryParameterNames) {
            if (next.equals(str)) {
                z = true;
                str3 = str2;
            } else {
                str3 = uri.getQueryParameter(next);
            }
            clearQuery.appendQueryParameter(next, str3);
        }
        if (!z) {
            clearQuery.appendQueryParameter(str, str2);
        }
        return clearQuery.build();
    }
}
