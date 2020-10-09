package com.huawei.hianalytics.log.f.a;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class c {
    private String a;
    private String b;
    private String c;
    private Map<String, String> d = new HashMap();

    public c() {
    }

    public c(String str, String str2, String str3) {
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    public String a() {
        return this.a;
    }

    public void a(String str, String str2) {
        if (str != null && !TextUtils.isEmpty(str)) {
            this.d.put(str, str2);
        }
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public final Map<String, String> d() {
        return this.d;
    }
}
