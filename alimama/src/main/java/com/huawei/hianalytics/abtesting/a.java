package com.huawei.hianalytics.abtesting;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.process.HiAnalyticsConfig;
import com.huawei.hianalytics.process.HiAnalyticsInstance;
import com.huawei.hianalytics.process.d;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public final class a {
    private static a c = new a();
    public Context a;
    private final Object b = new Object();
    private HiAnalyticsInstance d = null;
    private ExecutorService e = Executors.newSingleThreadExecutor();
    private long f = 86400000;

    public static a a() {
        return c;
    }

    private boolean a(ABTestConfig aBTestConfig) {
        String str;
        String str2;
        if (aBTestConfig == null) {
            str = "ABTestManager";
            str2 = "ABTestConfig is null,initialization failed";
        } else if (TextUtils.isEmpty(aBTestConfig.b().c())) {
            str = "ABTestManager";
            str2 = "URL error ,initialization failed";
        } else if (TextUtils.isEmpty(aBTestConfig.b().d())) {
            str = "ABTestManager";
            str2 = "userId error,initialization failed";
        } else if (TextUtils.isEmpty(aBTestConfig.b().a())) {
            str = "ABTestManager";
            str2 = "secretKey error,initialization failed";
        } else if (aBTestConfig.a() != null) {
            return true;
        } else {
            str = "ABTestManager";
            str2 = "HiAnalytics config is null,initialization failed";
        }
        b.d(str, str2);
        return false;
    }

    private void b(ABTestConfig aBTestConfig) {
        d dVar = new d("ABTesting");
        dVar.c(new HiAnalyticsConfig(aBTestConfig.a()));
        com.huawei.hianalytics.process.a.b().a(this.a);
        com.huawei.hianalytics.process.b.a().a(this.a);
        d a2 = com.huawei.hianalytics.process.a.b().a("ABTesting", dVar);
        if (a2 != null) {
            dVar = a2;
        }
        this.d = dVar;
        this.f = ((long) aBTestConfig.b().b()) * 60000;
    }

    private void d() {
        if (f()) {
            try {
                this.e.execute(new com.huawei.hianalytics.abtesting.b.b(this.a));
            } catch (Exception unused) {
                b.c("ABTestManager", "startSyncTask : This exception was not catch,Exception has happened in thread!");
            }
        } else {
            this.e.execute(new com.huawei.hianalytics.abtesting.b.a(this.a));
        }
    }

    private void e() {
        if (!com.huawei.hianalytics.abtesting.a.b.a().c()) {
            com.huawei.hianalytics.abtesting.a.b.a().b(true);
            if (f()) {
                b.b("ABTestManager", "syncDataTask(): requesting network...");
                this.e.execute(new com.huawei.hianalytics.abtesting.b.b(this.a));
                return;
            }
            com.huawei.hianalytics.abtesting.a.b.a().b(false);
            return;
        }
        b.b("ABTestManager", "Already requesting network, quit.");
    }

    private boolean f() {
        String str;
        String str2;
        long longValue = ((Long) com.huawei.hianalytics.util.d.b(com.huawei.hianalytics.util.d.a(this.a, "abtest"), "expdata_refresh_time", -1L)).longValue() + this.f;
        boolean z = longValue == 0 || longValue < Long.valueOf(System.currentTimeMillis()).longValue();
        if (z) {
            str = "ABTestManager";
            str2 = "Achieving Request Cycle";
        } else {
            str = "ABTestManager";
            str2 = "Not reaching the request cycle";
        }
        b.b(str, str2);
        return z;
    }

    public String a(String str) {
        if (!com.huawei.hianalytics.abtesting.a.b.a().b()) {
            b.c("ABTestManager", "ABTest sdk is not initialized");
            return "";
        }
        String b2 = com.huawei.hianalytics.abtesting.a.b.a().b(str);
        e();
        return b2;
    }

    public void a(int i) {
        if (!com.huawei.hianalytics.abtesting.a.b.a().b()) {
            b.c("ABTestManager", "setSyncInterval : ABTest sdk is not initialized");
        } else {
            this.f = ((long) i) * 60000;
        }
    }

    public void a(Context context, ABTestConfig aBTestConfig) {
        if (context == null) {
            b.d("ABTestManager", "context is null,initialization failed!");
        } else if (context.getApplicationContext() == null) {
            b.d("ABTestManager", "context.getApplicationContext() is null,initialization failed!");
        } else if (a(aBTestConfig)) {
            synchronized (this.b) {
                if (this.a != null) {
                    b.b("ABTestManager", "SDK has been initialized");
                    return;
                }
                this.a = context.getApplicationContext();
                b(aBTestConfig);
                com.huawei.hianalytics.abtesting.a.b.a().a(aBTestConfig.b());
                d();
            }
        }
    }

    public void a(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        if (!com.huawei.hianalytics.abtesting.a.b.a().b()) {
            b.c("ABTestManager", "ABTest sdk is not initialized");
        } else if (this.d == null) {
            b.c("ABTestManager", "onEvent : instance is null");
        } else {
            if (linkedHashMap == null) {
                b.b("ABTestManager", "onEvent: mapValue is empty!");
                linkedHashMap = new LinkedHashMap<>();
            }
            String c2 = com.huawei.hianalytics.abtesting.a.b.a().c(str);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("groupId", c2);
            } catch (JSONException unused) {
                b.c("ABTestManager", "json exception from getGroupId");
            }
            linkedHashMap.put("experiment", jSONObject.toString());
            String e2 = com.huawei.hianalytics.abtesting.a.b.a().e();
            if (!e2.equals(linkedHashMap.get("userId"))) {
                linkedHashMap.put("userId", e2);
            }
            this.d.onEvent(str2, linkedHashMap);
        }
    }

    public void b() {
        if (!com.huawei.hianalytics.abtesting.a.b.a().b()) {
            b.c("ABTestManager", "onReport : ABTest sdk is not initialized");
        } else if (this.d == null) {
            b.c("ABTestManager", "instance is null");
        } else {
            this.d.onReport(0);
        }
    }

    public void c() {
        if (!com.huawei.hianalytics.abtesting.a.b.a().b()) {
            b.c("ABTestManager", "syncExpParameters: ABTest sdk is not initialized");
        } else {
            this.e.execute(new com.huawei.hianalytics.abtesting.b.b(this.a));
        }
    }
}
