package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;
import com.huawei.hianalytics.f.b.a;
import com.huawei.hianalytics.f.b.e;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.i;
import com.huawei.hianalytics.f.g.j;
import com.huawei.hianalytics.g.b;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class c {
    private Context a;
    private String b;
    private com.huawei.hianalytics.f.b.c[] c;
    private String d;
    private String e;

    c(Context context, String str, com.huawei.hianalytics.f.b.c[] cVarArr, String str2, String str3) {
        this.a = context;
        this.b = str;
        this.c = cVarArr;
        this.d = str2;
        this.e = str3;
    }

    private void a(List<e> list, String str, String str2) {
        int size = list.size();
        if (size > 0) {
            while (size > 0) {
                int i = 500;
                if (size <= 500) {
                    i = size;
                }
                int i2 = size - i;
                b(list.subList(i2, size), str, str2);
                size = i2;
            }
            return;
        }
        b.b("HiAnalytics/event", "no events to send,TAG : %s,TYPE: %s", str2, str);
    }

    private void a(com.huawei.hianalytics.f.b.c[] cVarArr, String str, String str2) {
        b.a("EventDataHandler", "choiceHandleEvents TAG : %s,TYPE: %s", str2, str);
        a(m.a(cVarArr), str, str2);
    }

    @SafeVarargs
    private final void a(com.huawei.hianalytics.f.b.c[] cVarArr, List<com.huawei.hianalytics.f.b.c>... listArr) {
        List<com.huawei.hianalytics.f.b.c> list;
        for (com.huawei.hianalytics.f.b.c cVar : cVarArr) {
            String g = cVar.g();
            if (TextUtils.isEmpty(g) || "oper".equals(g)) {
                list = listArr[0];
            } else if ("maint".equals(g)) {
                list = listArr[1];
            } else if ("preins".equals(g)) {
                list = listArr[2];
            } else if ("diffprivacy".equals(g)) {
                list = listArr[3];
            }
            list.add(cVar);
        }
    }

    private void b(List<e> list, String str, String str2) {
        if (list.size() > 0) {
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArray = new JSONArray();
            long currentTimeMillis = System.currentTimeMillis();
            long g = ((long) com.huawei.hianalytics.a.c.g(str2, str)) * 86400000;
            for (e next : list) {
                if (!next.a(currentTimeMillis, g)) {
                    a a2 = next.a();
                    arrayList.add(a2);
                    jSONArray.put(a2.a());
                }
            }
            if (arrayList.size() <= 0 || jSONArray.length() <= 0) {
                b.c("EventDataHandler", "Not have data need to send.TAG : %s,TYPE: %s", str2, str);
                return;
            }
            SharedPreferences c2 = g.c(this.a, "backup_event");
            if (!g.a(this.a, "backup_event", 5242880)) {
                String a3 = i.a(str2, str, this.e);
                b.b("EventDataHandler", "Update data cached into backup,spKey: " + a3);
                g.a(c2, a3, (Object) jSONArray.toString());
            } else {
                b.b("EventDataHandler", "backup file reach max limited size, discard new event ");
            }
            j.b(new a(this.a, arrayList, str, str2, this.d, this.e));
            return;
        }
        b.b("HiAnalytics/event", "empty event data, no need to send,TAG : %s,TYPE: %s", str2, str);
    }

    public void a() {
        b.b("EventDataHandler", "handler event report...");
        Pair<String, String> a2 = i.a(this.b);
        if (!"_default_config_tag".equals(a2.first)) {
            a(this.c, (String) a2.second, (String) a2.first);
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        a(this.c, arrayList, arrayList2, arrayList3, arrayList4);
        if (arrayList.size() > 0) {
            a((com.huawei.hianalytics.f.b.c[]) arrayList.toArray(new com.huawei.hianalytics.f.b.c[arrayList.size()]), "oper", "_default_config_tag");
        }
        if (arrayList2.size() > 0) {
            a((com.huawei.hianalytics.f.b.c[]) arrayList2.toArray(new com.huawei.hianalytics.f.b.c[arrayList2.size()]), "maint", "_default_config_tag");
        }
        if (arrayList3.size() > 0) {
            a((com.huawei.hianalytics.f.b.c[]) arrayList3.toArray(new com.huawei.hianalytics.f.b.c[arrayList3.size()]), "preins", "_default_config_tag");
        }
        if (arrayList4.size() > 0) {
            a((com.huawei.hianalytics.f.b.c[]) arrayList4.toArray(new com.huawei.hianalytics.f.b.c[arrayList4.size()]), "diffprivacy", "_default_config_tag");
        }
    }
}
