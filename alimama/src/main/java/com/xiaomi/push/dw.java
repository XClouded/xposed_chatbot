package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;

public class dw extends dx {
    public static String a = "";
    public static String b = "";

    public dw(Context context, int i) {
        super(context, i);
    }

    private String a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return "";
        }
        String[] split = str2.split(",");
        if (split.length <= 10) {
            return str2;
        }
        int length = split.length;
        while (true) {
            length--;
            if (length < split.length - 10) {
                return str;
            }
            str = str + split[length];
        }
    }

    public int a() {
        return 12;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hi m183a() {
        return hi.BroadcastAction;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m184a() {
        String str = "";
        if (!TextUtils.isEmpty(a)) {
            str = str + a(dq.f239a, a);
            a = "";
        }
        if (TextUtils.isEmpty(b)) {
            return str;
        }
        String str2 = str + a(dq.b, b);
        b = "";
        return str2;
    }
}
