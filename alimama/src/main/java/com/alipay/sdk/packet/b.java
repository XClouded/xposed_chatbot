package com.alipay.sdk.packet;

import android.text.TextUtils;
import com.alipay.sdk.util.c;
import org.json.JSONObject;

public final class b {
    private final String a;
    private final String b;

    public b(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public JSONObject c() {
        if (TextUtils.isEmpty(this.b)) {
            return null;
        }
        try {
            return new JSONObject(this.b);
        } catch (Exception e) {
            c.a(e);
            return null;
        }
    }

    public String toString() {
        return String.format("<Letter envelop=%s body=%s>", new Object[]{this.a, this.b});
    }
}
