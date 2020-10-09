package com.huawei.updatesdk.sdk.a.d;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.mipush.sdk.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public abstract class e {
    private static final Pattern a = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    public static boolean a(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20").replace("*", "%2A").replace(Constants.WAVE_SEPARATOR, "%7E");
        } catch (UnsupportedEncodingException e) {
            a.a("StringUtils", "encode2utf8 error", e);
            return null;
        }
    }

    public static boolean c(String str) {
        return str != null && str.trim().startsWith(Operators.BLOCK_START_STR) && str.trim().endsWith("}");
    }

    public static boolean d(String str) {
        return a.matcher(str).matches();
    }
}
