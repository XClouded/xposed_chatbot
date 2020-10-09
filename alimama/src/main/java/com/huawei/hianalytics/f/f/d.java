package com.huawei.hianalytics.f.f;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.hianalytics.a.a;
import com.huawei.hianalytics.a.c;
import com.huawei.hianalytics.f.b.f;
import com.huawei.hianalytics.f.e.b;
import com.huawei.hianalytics.f.g.g;

public class d implements g {
    private String a;
    private String b;
    private String c = "oper";
    private long d;
    private String e;
    private Context f;
    private String g;
    private Boolean h;

    public d(Context context, String str, int i, String str2, String str3, long j) {
        String str4;
        this.f = context;
        this.e = str;
        this.a = str2;
        this.b = str3;
        switch (i) {
            case 1:
                str4 = "maint";
                break;
            case 2:
                str4 = "preins";
                break;
            case 3:
                str4 = "diffprivacy";
                break;
            default:
                this.c = "oper";
                if (a.e(str, "oper")) {
                    b a2 = com.huawei.hianalytics.f.e.a.a().a(str, j);
                    this.g = a2.a();
                    this.h = Boolean.valueOf(a2.b());
                    break;
                }
                break;
        }
        this.c = str4;
        this.d = j;
    }

    public d(Context context, String str, String str2, String str3, long j) {
        this.f = context;
        this.e = str;
        this.a = str2;
        this.b = str3;
        this.c = "oper";
        this.d = j;
        if (a.e(str, "oper")) {
            b a2 = com.huawei.hianalytics.f.e.a.a().a(str, j);
            this.g = a2.a();
            this.h = Boolean.valueOf(a2.b());
        }
    }

    @SuppressLint({"ApplySharedPref"})
    private void a() {
        String str;
        String str2;
        int l = com.huawei.hianalytics.a.b.l();
        int h2 = c.h(this.e, this.c);
        if (g.a(this.f, "stat_v2_1", l * 1048576)) {
            com.huawei.hianalytics.g.b.b("HiAnalytics/event", "stat sp file reach max limited size, discard new event");
            h.a().a("", "");
            return;
        }
        SharedPreferences c2 = g.c(this.f, "stat_v2_1");
        if (c2 == null) {
            com.huawei.hianalytics.g.b.c("EventRecordTask", "event sp is null");
            return;
        }
        f fVar = new f();
        fVar.d(this.a);
        fVar.f(com.huawei.hianalytics.f.g.c.b(this.b, this.f));
        fVar.g(this.e);
        fVar.e(this.c);
        fVar.c(String.valueOf(this.d));
        fVar.a(this.g);
        if (this.h == null) {
            str = null;
        } else {
            str = this.h + "";
        }
        fVar.b(str);
        fVar.a(c2);
        if ("_default_config_tag".equals(this.e)) {
            str2 = this.e;
        } else {
            str2 = this.e + "-" + this.c;
        }
        if (g.a(c2, h2 * 1024, str2)) {
            h.a().a(this.e, this.c);
        }
    }

    public void run() {
        a();
    }
}
