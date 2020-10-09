package com.huawei.hianalytics.f.h.a;

import android.content.Context;
import com.huawei.hianalytics.f.f.d;
import com.huawei.hianalytics.f.f.h;
import com.huawei.hianalytics.f.g.j;
import com.huawei.hianalytics.f.h.c.b;
import java.util.LinkedHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    private static final Object a = new Object();
    private static Context b;

    public static void a(Context context) {
        b = context;
    }

    private static void a(Context context, String str, long j, LinkedHashMap<String, String> linkedHashMap, String str2, String str3, long j2, String str4) {
        String str5 = str;
        long j3 = j;
        LinkedHashMap<String, String> linkedHashMap2 = linkedHashMap;
        String str6 = str3;
        j.a((com.huawei.hianalytics.i.a) new d(context, str4, str2, j.a(str, j, linkedHashMap, str3).toString(), j2));
    }

    public static void a(com.huawei.hianalytics.f.h.c.a aVar, String str) {
        if (aVar != null && aVar.d()) {
            a(b, aVar.a(), aVar.c(), (LinkedHashMap<String, String>) null, "$AppOnPause", "OnPause", aVar.b(), str);
        }
    }

    public static void a(b bVar, String str) {
        if (b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalytics/V1Server", "onEvent null context");
            return;
        }
        String a2 = bVar.a();
        String b2 = bVar.b();
        Long c = bVar.c();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("_constants", b2);
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("HiAnalytics/V1Server", "onEvent(): JSONException");
        }
        String str2 = str;
        j.a((com.huawei.hianalytics.i.a) new d(b, str2, a2, jSONObject.toString(), c.longValue()));
    }

    public static void a(String str) {
        h.a().a(str, 0);
    }

    public static void b(com.huawei.hianalytics.f.h.c.a aVar, String str) {
        if (aVar != null && aVar.d()) {
            a(b, aVar.a(), 0, (LinkedHashMap<String, String>) null, "$AppOnResume", "OnResume", aVar.b(), str);
        }
    }

    public static void b(String str) {
        if (b == null) {
            com.huawei.hianalytics.g.b.b("HiAnalytics/event", "You must execute Builder.create() before you execute this method.");
            return;
        }
        synchronized (a) {
            if (com.huawei.hianalytics.f.h.b.a.a(b)) {
                com.huawei.hianalytics.g.b.b("HiAnalytics/V1Server", "cached data by BISDK has already handled.");
                return;
            }
            com.huawei.hianalytics.f.h.b.a.b(b);
            j.a((com.huawei.hianalytics.i.a) new c(b, str));
        }
    }
}
