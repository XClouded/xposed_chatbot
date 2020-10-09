package com.huawei.hms.support.api.push.b.a;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hms.support.api.push.b.a.a.c;
import com.huawei.hms.support.api.push.b.b.d;
import com.huawei.hms.support.log.a;

/* compiled from: PushDataEncrypterManager */
public abstract class b {
    public static String a(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            str2 = d.b(context, new c(context, str).b("token_info_v2"));
        } catch (Exception e) {
            a.d("PushDataEncrypterManager", "getSecureData" + e.getMessage());
            str2 = "";
        }
        if (TextUtils.isEmpty(str2)) {
            a.a("PushDataEncrypterManager", "getSecureData not exist");
        }
        return str2;
    }

    public static void b(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                new c(context, str).d("token_info_v2");
            } catch (Exception e) {
                a.d("PushDataEncrypterManager", "removeSecureData" + e.getMessage());
            }
        }
    }

    public static boolean a(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new c(context, str).a("token_info_v2", d.a(context, str2));
    }
}
