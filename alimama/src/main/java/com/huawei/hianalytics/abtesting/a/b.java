package com.huawei.hianalytics.abtesting.a;

import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class b {
    private static b e = new b();
    private ConcurrentHashMap<String, c> a = null;
    private volatile boolean b = false;
    private volatile boolean c = false;
    private a d = null;

    public static b a() {
        return e;
    }

    public void a(a aVar) {
        this.d = aVar;
        this.c = false;
        this.b = false;
    }

    public void a(String str) {
        JSONArray jSONArray = new JSONObject(str).getJSONArray("parameters");
        c[] cVarArr = new c[jSONArray.length()];
        ConcurrentHashMap<String, c> concurrentHashMap = new ConcurrentHashMap<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String string = jSONObject.getString("groupId");
            String string2 = jSONObject.getString("key");
            cVarArr[i] = new c(jSONObject.getString("value"), string);
            concurrentHashMap.put(string2, cVarArr[i]);
        }
        this.a = concurrentHashMap;
    }

    public synchronized void a(boolean z) {
        this.c = z;
    }

    public String b(String str) {
        if (this.a == null) {
            com.huawei.hianalytics.g.b.c("ABDataCenter", "getParamValue(): Experiment data is empty.");
            return "";
        } else if (this.a.get(str) != null) {
            return this.a.get(str).a();
        } else {
            com.huawei.hianalytics.g.b.c("ABDataCenter", "getParamValue() : No corresponding value was found.");
            return "";
        }
    }

    public synchronized void b(boolean z) {
        this.b = z;
    }

    public synchronized boolean b() {
        return this.c;
    }

    public String c(String str) {
        if (this.a == null) {
            com.huawei.hianalytics.g.b.c("ABDataCenter", "getGroupID(): Experiment data is empty.");
            return "";
        } else if (this.a.get(str) == null) {
            com.huawei.hianalytics.g.b.b("ABDataCenter", "getGroupID: Not found getGroupId from expParamKey");
            return "";
        } else {
            String b2 = this.a.get(str).b();
            if (b2 != null) {
                return b2;
            }
            com.huawei.hianalytics.g.b.b("ABDataCenter", "getGroupID: groupId is null");
            return "";
        }
    }

    public synchronized boolean c() {
        return this.b;
    }

    public String d() {
        if (this.d != null) {
            return this.d.c();
        }
        com.huawei.hianalytics.g.b.b("ABDataCenter", "getABServerURL(): ABDataCenter needs init first");
        return "";
    }

    public String e() {
        if (this.d != null) {
            return this.d.d();
        }
        com.huawei.hianalytics.g.b.b("ABDataCenter", "getUserID(): ABDataCenter needs init first");
        return "";
    }

    public String f() {
        if (this.d != null) {
            return this.d.a();
        }
        com.huawei.hianalytics.g.b.b("ABDataCenter", "getSecretKey(): ABDataCenter needs init first");
        return "";
    }
}
