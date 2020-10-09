package com.huawei.hianalytics.log.a;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import mtopsdk.common.util.SymbolExpUtil;

public class b {
    private SortedMap<String, String> a = new TreeMap();

    public b(String str) {
        b(str);
    }

    private void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split("&")) {
                int indexOf = str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL);
                this.a.put(str2.substring(0, indexOf), str2.substring(indexOf + 1));
            }
        }
    }

    public String a() {
        StringBuilder sb = new StringBuilder(512);
        try {
            for (Map.Entry next : this.a.entrySet()) {
                String str = (String) next.getKey();
                if (!TextUtils.isEmpty(str)) {
                    String encode = URLEncoder.encode(str, "UTF-8");
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    String str2 = (String) next.getValue();
                    String str3 = "";
                    if (!TextUtils.isEmpty(str2)) {
                        str3 = URLDecoder.decode(str2, "UTF-8");
                    }
                    sb.append(encode);
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(str3);
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException unused) {
            com.huawei.hianalytics.g.b.c("CanonicalQueryString", "Exception when toString,Encode does not support!");
            return "";
        }
    }

    public String a(String str) {
        String str2 = (String) this.a.get(URLEncoder.encode(str, "UTF-8"));
        return !TextUtils.isEmpty(str2) ? URLDecoder.decode(str2, "UTF-8") : str2;
    }
}
