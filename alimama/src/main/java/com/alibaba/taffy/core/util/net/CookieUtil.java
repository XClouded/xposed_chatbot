package com.alibaba.taffy.core.util.net;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class CookieUtil {
    private static final String FIELD_NAME_COOKIE1 = "Set-cookie";
    private static final String FIELD_NAME_COOKIE2 = "Set-cookie2";
    private static final String TAG = "CookieUtil";

    public static void storeCookies(HttpURLConnection httpURLConnection) {
        storeCookies(httpURLConnection.getURL().toString(), httpURLConnection.getHeaderFields());
    }

    public static void storeCookies(String str, Map<String, List<String>> map) {
        if (str != null && map != null) {
            try {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry next : map.entrySet()) {
                    String str2 = (String) next.getKey();
                    if (FIELD_NAME_COOKIE1.equalsIgnoreCase(str2) || FIELD_NAME_COOKIE2.equalsIgnoreCase(str2)) {
                        for (String parse : (List) next.getValue()) {
                            try {
                                for (HttpCookie httpCookie : HttpCookie.parse(parse)) {
                                    sb.append(";");
                                    sb.append(httpCookie.toString());
                                }
                            } catch (IllegalArgumentException e) {
                                Log.d(TAG, e.getMessage(), e);
                            }
                        }
                    }
                }
                if (sb.length() > 0) {
                    CookieManager.getInstance().setCookie(str, sb.substring(1));
                    CookieSyncManager.getInstance().sync();
                }
            } catch (Exception e2) {
                Log.e(TAG, e2.getMessage(), e2);
            }
        }
    }

    public static String getCookieString(String str) {
        return CookieManager.getInstance().getCookie(str);
    }
}
