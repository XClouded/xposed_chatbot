package com.xiaomi.push;

import java.util.HashMap;
import java.util.Map;

public class aq {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public String f122a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f123a = new HashMap();

    public String a() {
        return this.f122a;
    }

    public String toString() {
        return String.format("resCode = %1$d, headers = %2$s, response = %3$s", new Object[]{Integer.valueOf(this.a), this.f123a.toString(), this.f122a});
    }
}
