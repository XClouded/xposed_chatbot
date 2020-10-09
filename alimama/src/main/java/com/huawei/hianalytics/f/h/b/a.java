package com.huawei.hianalytics.f.h.b;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.g.b;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class a {
    public static Long a(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        try {
            return Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(str).getTime());
        } catch (ParseException unused) {
            b.c("V1Common", "timestampAdapter: convertBisdkTime failed to parse time");
            return -1L;
        }
    }

    public static boolean a(Context context) {
        return ((Boolean) g.b(g.c(context, "global_v2"), "v1cacheHandleFlag", false)).booleanValue();
    }

    public static void b(Context context) {
        g.a(g.c(context, "global_v2"), "v1cacheHandleFlag", (Object) true);
    }
}
