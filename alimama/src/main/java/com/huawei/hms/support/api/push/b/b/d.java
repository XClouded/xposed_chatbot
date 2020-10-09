package com.huawei.hms.support.api.push.b.b;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: PushEncrypter */
public class d {
    public static String a(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return a.a(str, e.a());
    }

    public static String b(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return a.b(str, e.a());
    }
}
