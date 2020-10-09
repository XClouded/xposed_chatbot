package com.xiaomi.push.service;

import android.text.TextUtils;
import com.xiaomi.push.ay;

public class aj {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static String f845a = "";

    public static String a() {
        if (TextUtils.isEmpty(f845a)) {
            f845a = ay.a(4);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(f845a);
        long j = a;
        a = 1 + j;
        sb.append(j);
        return sb.toString();
    }
}
