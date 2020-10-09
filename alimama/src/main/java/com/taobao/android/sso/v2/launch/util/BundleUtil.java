package com.taobao.android.sso.v2.launch.util;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import mtopsdk.common.util.SymbolExpUtil;

public class BundleUtil {
    public static String deserialBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : bundle.keySet()) {
            String obj = bundle.get(str).toString();
            if (!(obj == null || Uri.parse(obj).getScheme() == null)) {
                try {
                    obj = URLEncoder.encode(obj, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("login.BundleUtil", e.toString());
                }
            }
            sb.append(str + SymbolExpUtil.SYMBOL_EQUAL + obj + "&");
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

    public static Bundle serialBundle(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        String[] split = str.split("&");
        Bundle bundle = new Bundle();
        for (String str2 : split) {
            int indexOf = str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL);
            if (indexOf > 0 && indexOf < str2.length() - 1) {
                bundle.putString(str2.substring(0, indexOf), str2.substring(indexOf + 1));
            }
        }
        return bundle;
    }
}
