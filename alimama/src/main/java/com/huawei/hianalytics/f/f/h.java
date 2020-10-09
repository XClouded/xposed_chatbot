package com.huawei.hianalytics.f.f;

import android.content.Context;
import com.huawei.hianalytics.a.b;
import com.huawei.hianalytics.c.c;
import com.huawei.hianalytics.f.c.a;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.i;
import com.huawei.hianalytics.f.g.j;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class h {
    private static h a;
    private static Map<String, Long> d = new HashMap();
    private Context b;
    private a c = new a();
    private long e = 0;

    public static h a() {
        return d();
    }

    private static void a(String str, Context context, String str2, long j, LinkedHashMap<String, String> linkedHashMap, String str3, String str4) {
        j.a((com.huawei.hianalytics.i.a) new d(context, str, str3, j.a(str2, j, linkedHashMap, str4).toString(), System.currentTimeMillis()));
    }

    private static void a(String str, Context context, String str2, LinkedHashMap<String, String> linkedHashMap) {
        long currentTimeMillis = System.currentTimeMillis();
        long longValue = d.containsKey(str) ? d.get(str).longValue() : 0;
        a(str, context, str2, longValue == 0 ? 0 : currentTimeMillis - longValue, linkedHashMap, "$AppOnPause", "OnPause");
    }

    private void b(Context context) {
        String b2 = c.b(context);
        String a2 = g.a(context);
        g.a(context, b2);
        b.e(b2);
        b.f(a2);
    }

    private static synchronized h d() {
        h hVar;
        synchronized (h.class) {
            if (a == null) {
                a = new h();
            }
            hVar = a;
        }
        return hVar;
    }

    public void a(Context context) {
        this.b = context;
        b(context);
        j.a((com.huawei.hianalytics.i.a) new k(context));
    }

    public void a(String str, int i) {
        a(str, this.b, i.a(i), b.g());
    }

    public void a(String str, int i, String str2, LinkedHashMap<String, String> linkedHashMap) {
        JSONObject jSONObject = linkedHashMap == null ? new JSONObject() : j.a(linkedHashMap);
        long currentTimeMillis = System.currentTimeMillis();
        if (2 == i) {
            currentTimeMillis = i.a("yyyy-MM-dd", currentTimeMillis);
        }
        j.a((com.huawei.hianalytics.i.a) new d(this.b, str, i, str2, jSONObject.toString(), currentTimeMillis));
    }

    public void a(String str, Context context) {
        if (context == null || this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onPause's mContext is null or SDK was not init.");
            return;
        }
        a(str, this.b, context.getClass().getCanonicalName(), (LinkedHashMap<String, String>) null);
        d.put(str, 0L);
    }

    public void a(String str, Context context, String str2, String str3) {
        if (context == null || this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onReport() null context or SDK was not init.");
            return;
        }
        com.huawei.hianalytics.g.b.b("HiAnalytics/event", "onReport: Before calling runtaskhandler()");
        j.a((com.huawei.hianalytics.i.a) new e(context, str, str2, str3));
    }

    public void a(String str, Context context, LinkedHashMap<String, String> linkedHashMap) {
        if (context == null || this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onPause null context or SDK was not init.");
            return;
        }
        a(str, this.b, context.getClass().getCanonicalName(), linkedHashMap);
        d.put(str, 0L);
    }

    public void a(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.e > 30000) {
            com.huawei.hianalytics.g.b.a("HiAnalyticsEventServer", "begin to call onReport!");
            this.e = currentTimeMillis;
            a(str, this.b, str2, b.g());
            return;
        }
        com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "autoReport timeout. interval < 30s ");
    }

    public void a(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("_constants", str3);
            String str4 = str;
            String str5 = str2;
            j.a((com.huawei.hianalytics.i.a) new d(this.b, str4, str5, jSONObject.toString(), System.currentTimeMillis()));
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onEvent():JSON structure Exception!");
        }
    }

    public void a(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        if (this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onPause null context or SDK was not init.");
            return;
        }
        a(str, this.b, str2, linkedHashMap);
        d.put(str, 0L);
    }

    public void a(boolean z) {
        if (this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onEventCrashInit() SDK was not init.");
        } else if (z) {
            b.a(this.b).a();
        } else {
            b.a(this.b).b();
        }
    }

    public a b() {
        return this.c;
    }

    public void b(String str, int i, String str2, LinkedHashMap<String, String> linkedHashMap) {
        new i(str, i.a(i), str2, (linkedHashMap == null ? new JSONObject() : j.a(linkedHashMap)).toString(), this.b).a();
    }

    public void b(String str, Context context) {
        if (context == null || this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onResume's mContext is null or SDK was not init.");
            return;
        }
        d.put(str, Long.valueOf(System.currentTimeMillis()));
        a(str, this.b, context.getClass().getCanonicalName(), 0, (LinkedHashMap<String, String>) null, "$AppOnResume", "OnResume");
    }

    public void b(String str, Context context, LinkedHashMap<String, String> linkedHashMap) {
        d.put(str, Long.valueOf(System.currentTimeMillis()));
        if (context == null || this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onResume() null context or SDK was not init.");
            return;
        }
        a(str, this.b, context.getClass().getCanonicalName(), 0, linkedHashMap, "$AppOnResume", "OnResume");
    }

    public void b(String str, String str2) {
        if (this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onStartApp() SDK was not init.");
            return;
        }
        JSONObject a2 = com.huawei.hianalytics.f.g.b.a(this.b, str, str2);
        if (a2 != null) {
            j.a((com.huawei.hianalytics.i.a) new d(this.b, "_instance_ex_tag", "$AppOnStart", a2.toString(), System.currentTimeMillis()));
        } else {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onStartApp() getInfoJson is null,The end of the event ");
        }
    }

    public void b(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        d.put(str, Long.valueOf(System.currentTimeMillis()));
        a(str, this.b, str2, 0, linkedHashMap, "$AppOnResume", "OnResume");
    }

    public void c() {
        if (this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onFirstRun() SDK was not init.");
            return;
        }
        JSONObject a2 = com.huawei.hianalytics.f.g.b.a(this.b);
        if (a2 != null) {
            j.a((com.huawei.hianalytics.i.a) new d(this.b, "_instance_ex_tag", "$AppFirstStart", a2.toString(), System.currentTimeMillis()));
        } else {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onFirstRun() getInfoJson is null,The end of the event ");
        }
    }

    public void c(String str, String str2) {
        if (this.b == null) {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onAppUpgrade() SDK was not init.");
            return;
        }
        JSONObject a2 = com.huawei.hianalytics.f.g.b.a(str, str2);
        if (a2 != null) {
            j.a((com.huawei.hianalytics.i.a) new d(this.b, "_instance_ex_tag", "$AppOnUpdate", a2.toString(), System.currentTimeMillis()));
        } else {
            com.huawei.hianalytics.g.b.c("HiAnalyticsEventServer", "onAppUpgrade() getInfoJson is null,The end of the event ");
        }
    }
}
