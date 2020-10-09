package com.alipay.sdk.util;

import android.content.Context;
import android.text.TextUtils;
import com.alipay.sdk.app.statistic.a;
import com.alipay.sdk.app.statistic.c;

public class i {
    public static final String a = "pref_trade_token";
    public static final String b = ";";
    public static final String c = "result={";
    public static final String d = "}";
    public static final String e = "trade_token=\"";
    public static final String f = "\"";
    public static final String g = "trade_token=";

    public static void a(Context context, String str) {
        try {
            String a2 = a(str);
            c.b("", "PayResultUtil::saveTradeToken > tradeToken:" + a2);
            if (!TextUtils.isEmpty(a2)) {
                j.a(context, a, a2);
            }
        } catch (Throwable th) {
            a.a(c.b, c.z, th);
            c.a(th);
        }
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(";");
        String str2 = null;
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith(c) && split[i].endsWith("}")) {
                String[] split2 = split[i].substring(c.length(), split[i].length() - "}".length()).split("&");
                int i2 = 0;
                while (true) {
                    if (i2 < split2.length) {
                        if (split2[i2].startsWith(e) && split2[i2].endsWith("\"")) {
                            str2 = split2[i2].substring(e.length(), split2[i2].length() - "\"".length());
                            break;
                        } else if (split2[i2].startsWith(g)) {
                            str2 = split2[i2].substring(g.length());
                            break;
                        } else {
                            i2++;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return str2;
    }

    public static String a(Context context) {
        String b2 = j.b(context, a, "");
        c.b("", "PayResultUtil::fetchTradeToken > tradeToken:" + b2);
        return b2;
    }
}
