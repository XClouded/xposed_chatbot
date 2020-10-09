package com.alipay.sdk.util;

import com.alipay.sdk.app.k;
import com.alipay.sdk.app.statistic.a;
import com.alipay.sdk.app.statistic.c;
import java.util.HashMap;
import java.util.Map;

public class l {
    public static final String a = "resultStatus";
    public static final String b = "memo";
    public static final String c = "result";

    public static Map<String, String> a(String str) {
        Map<String, String> a2 = a();
        try {
            return b(str);
        } catch (Throwable th) {
            a.a(c.b, c.g, th);
            return a2;
        }
    }

    private static Map<String, String> a() {
        k b2 = k.b(k.CANCELED.a());
        HashMap hashMap = new HashMap();
        hashMap.put(a, Integer.toString(b2.a()));
        hashMap.put(b, b2.b());
        hashMap.put("result", "");
        return hashMap;
    }

    public static Map<String, String> b(String str) {
        String[] split = str.split(";");
        HashMap hashMap = new HashMap();
        for (String str2 : split) {
            String substring = str2.substring(0, str2.indexOf("={"));
            hashMap.put(substring, a(str2, substring));
        }
        return hashMap;
    }

    private static String a(String str, String str2) {
        String str3 = str2 + "={";
        return str.substring(str.indexOf(str3) + str3.length(), str.lastIndexOf("}"));
    }
}
