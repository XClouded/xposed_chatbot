package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.a.b;
import com.huawei.hianalytics.f.b.a;
import com.huawei.hianalytics.f.b.c;
import com.huawei.hianalytics.f.b.f;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.i;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class e implements g {
    private Context a;
    private String b;
    private String c;
    private int d = -1;
    private String e;

    public e(Context context, String str, String str2, String str3) {
        this.a = context;
        this.b = str;
        this.c = str2;
        this.e = str3;
    }

    private void a() {
        Set<String> a2 = g.a(g.c(this.a, "stat_v2_1"));
        Set<String> a3 = g.a(g.c(this.a, "cached_v2_1"));
        HashSet hashSet = new HashSet(a2);
        hashSet.addAll(a3);
        Set<String> a4 = i.a(b.a());
        a2.removeAll(a4);
        g.a(a4, (Set<String>) hashSet, this.a);
    }

    private void a(String str, c[] cVarArr) {
        String str2 = str;
        c[] cVarArr2 = cVarArr;
        new c(this.a, str2, cVarArr2, this.e, UUID.randomUUID().toString().replace("-", "")).a();
    }

    public void run() {
        com.huawei.hianalytics.g.b.b("HiAnalytics/event", "run report.TAG : %s,TYPE: %s", this.b, this.c);
        String str = this.b;
        boolean b2 = g.b(this.a);
        if (b2) {
            this.d = 1;
            a();
        } else if (TextUtils.isEmpty(this.c) && TextUtils.isEmpty(this.b)) {
            a();
            b2 = true;
        } else if (!"_default_config_tag".equals(this.b) && "allType".equals(this.c)) {
            com.huawei.hianalytics.g.b.c("EventReportTask", "eventType UnKnown,Stop Report!");
            return;
        } else if (!"_default_config_tag".equals(this.b) && !"allType".equals(this.c)) {
            str = str + "-" + this.c;
        }
        Map<String, c[]> a2 = f.a(g.c(this.a, "stat_v2_1"), this.a, str, b2);
        Map<String, c[]> a3 = a.a(g.c(this.a, "cached_v2_1"), this.a, str, b2);
        if (a2 == null || a3 == null) {
            com.huawei.hianalytics.g.b.c("EventReportTask", "Unknown anomaly,No data send!");
            return;
        }
        for (Map.Entry next : a2.entrySet()) {
            String str2 = (String) next.getKey();
            c[] cVarArr = (c[]) next.getValue();
            c[] cVarArr2 = a3.get(str2);
            if (cVarArr2 != null) {
                c[] cVarArr3 = new c[(cVarArr.length + cVarArr2.length)];
                System.arraycopy(cVarArr2, 0, cVarArr3, 0, cVarArr2.length);
                System.arraycopy(cVarArr, 0, cVarArr3, cVarArr2.length, cVarArr.length);
                a3.remove(str2);
                a(str2, cVarArr3);
            } else {
                a(str2, cVarArr);
            }
        }
        if (a3.size() > 0) {
            for (Map.Entry next2 : a3.entrySet()) {
                a((String) next2.getKey(), (c[]) next2.getValue());
            }
        }
        g.a(str, b2, this.a);
        com.huawei.hianalytics.f.g.a.a(this.a).a(com.huawei.hianalytics.f.g.e.a(), this.d);
    }
}
