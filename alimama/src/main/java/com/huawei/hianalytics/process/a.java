package com.huawei.hianalytics.process;

import android.content.Context;
import com.coloros.mcssdk.mode.Message;
import com.huawei.hianalytics.d.c;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.f;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class a {
    private static final String[] b = {"ABTesting", "_default_config_tag"};
    private static a d = null;
    private static final Object e = new Object();
    private static final Object f = new Object();
    private ConcurrentHashMap<String, d> a = new ConcurrentHashMap<>();
    private c c = null;
    private Context g;

    private a() {
    }

    public static a b() {
        if (d == null) {
            g();
        }
        return d;
    }

    private static synchronized void g() {
        synchronized (a.class) {
            if (d == null) {
                d = new a();
            }
        }
    }

    public int a() {
        return this.a.size();
    }

    public d a(String str) {
        if (str == null) {
            b.c("HianalyticsSDK", "getInstanceByTag() tag Can't be null");
            return null;
        } else if (this.a.containsKey(str)) {
            b.a("HianalyticsSDK", "getInstanceByTag(): TAG: " + str + " found.");
            return this.a.get(str);
        } else {
            b.c("HianalyticsSDK", "getInstanceByTag(): TAG: " + str + " not found.");
            return null;
        }
    }

    public d a(String str, d dVar) {
        d putIfAbsent = this.a.putIfAbsent(str, dVar);
        com.huawei.hianalytics.e.a.a().a(str, this.a.get(str).a);
        return putIfAbsent;
    }

    public void a(int i) {
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.setSPCacheSize is execute.");
        if (this.g == null) {
            b.c("HianalyticsSDK", "sdk is not init");
        } else {
            c.a(f.a(i, 10, 5));
        }
    }

    public void a(Context context) {
        synchronized (e) {
            if (this.g != null) {
                b.c("HianalyticsSDK", "SDK has been initialized,But an instance can be registered!");
                return;
            }
            this.g = context;
            com.huawei.hianalytics.e.a.a().f().g(context.getPackageName());
            com.huawei.hianalytics.d.a.a().a(context);
        }
    }

    public void a(Context context, HiAnalyticsLogConfig hiAnalyticsLogConfig) {
        if (hiAnalyticsLogConfig == null || context == null) {
            b.c("HianalyticsSDK", "enableLogCollection(): config or context is null, Log disabled.");
            com.huawei.hianalytics.e.a.a().c();
            return;
        }
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.enableLogCollection() is execute.");
        if (com.huawei.hianalytics.e.a.a().d()) {
            b.c("HianalyticsSDK", "enableLogCollection(): LogConfig already exists.");
            return;
        }
        com.huawei.hianalytics.e.a.a().a(hiAnalyticsLogConfig.getLogData());
        com.huawei.hianalytics.log.d.a.a().a(context);
    }

    public void a(HiAnalyticsLogConfig hiAnalyticsLogConfig, boolean z) {
        if (hiAnalyticsLogConfig == null) {
            b.c("HianalyticsSDK", "refreshLogConfig(): config is null, Log disabled.");
            com.huawei.hianalytics.e.a.a().c();
            return;
        }
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.refreshLogConfig() is execute.");
        synchronized (f) {
            com.huawei.hianalytics.e.a.a().a(hiAnalyticsLogConfig.getLogData());
            com.huawei.hianalytics.log.d.a.a().a(z);
        }
    }

    public void a(c cVar) {
        this.c = cVar;
        com.huawei.hianalytics.e.a.a().a("_instance_ex_tag", cVar.a);
    }

    public void a(boolean z) {
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.setHandlerAbnormalData is execute.");
        c.a(z);
    }

    public boolean b(String str) {
        if (str == null) {
            b.c("HianalyticsSDK", "getInitFlag() tag Can't be null");
            return false;
        }
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.getInitFlag(String tag) is execute.");
        return "_instance_ex_tag".equals(str) ? this.c != null : this.a.containsKey(str);
    }

    public List<String> c() {
        return new ArrayList(this.a.keySet());
    }

    public boolean c(String str) {
        for (String equals : b) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public c d() {
        return this.c;
    }

    public void d(String str) {
        if (this.g == null) {
            b.c("HianalyticsSDK", "clearDataByTag() sdk is not init");
            return;
        }
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.clearDataByTag(String appid) is execute.");
        g.a(str, this.g);
    }

    public int e() {
        int i = 0;
        for (String containsKey : b) {
            if (this.a.containsKey(containsKey)) {
                i++;
            }
        }
        return i;
    }

    public void e(String str) {
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.setAppid(String appid) is execute.");
        if (this.g == null) {
            b.c("HianalyticsSDK", "sdk is not init");
        } else {
            c.a(f.a(Message.APP_ID, str, "[a-zA-Z0-9_][a-zA-Z0-9. _-]{0,255}", this.g.getPackageName()));
        }
    }

    public void f() {
        b.b("HianalyticsSDK", "clearCachedData() is execute.");
        if (this.g == null) {
            b.c("HianalyticsSDK", "clearCachedData() sdk is not init");
            return;
        }
        b.b("HianalyticsSDK", "HiAnalyticsDataManager.clearCachedData() is execute.");
        g.a("", true, this.g);
    }
}
