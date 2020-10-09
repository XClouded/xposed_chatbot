package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.hianalytics.f.b.a;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.j;
import com.huawei.hianalytics.g.b;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class i {
    private String a;
    private String b;
    private String c;
    private String d;
    private Context e;

    public i(String str, String str2, String str3, String str4, Context context) {
        this.e = context;
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
    }

    public void a() {
        b.b("IMEventReportTask", "Stream Event run!");
        a aVar = new a(this.e);
        long currentTimeMillis = System.currentTimeMillis();
        aVar.f(this.d);
        aVar.e(this.b);
        aVar.d(this.c);
        aVar.c(currentTimeMillis + "");
        aVar.g(this.a);
        if ("oper".equals(this.b) && com.huawei.hianalytics.a.a.e(this.a, "oper")) {
            com.huawei.hianalytics.f.e.b a2 = com.huawei.hianalytics.f.e.a.a().a(this.a, currentTimeMillis);
            String a3 = a2.a();
            Boolean valueOf = Boolean.valueOf(a2.b());
            aVar.a(a3);
            aVar.b(valueOf + "");
        }
        String replace = UUID.randomUUID().toString().replace("-", "");
        if (!g.a(this.e, "backup_event", 5242880)) {
            SharedPreferences c2 = g.c(this.e, "backup_event");
            JSONObject a4 = aVar.a();
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(a4);
            g.a(c2, com.huawei.hianalytics.f.g.i.a(this.a, this.b, replace), (Object) jSONArray.toString());
        } else {
            b.b("IMEventReportTask", "backup file reach max limited size, discard new event ");
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(aVar);
        j.b(new a(this.e, arrayList, this.b, this.a, com.huawei.hianalytics.a.b.g(), replace));
    }
}
