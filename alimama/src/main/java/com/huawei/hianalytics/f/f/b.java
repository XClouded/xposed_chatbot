package com.huawei.hianalytics.f.f;

import android.content.Context;
import com.huawei.hianalytics.b.a;
import com.huawei.hianalytics.f.g.j;
import org.json.JSONObject;

public class b implements com.huawei.hianalytics.b.b {
    private static b b;
    private Context a;

    public b(Context context) {
        this.a = context;
    }

    public static b a(Context context) {
        b bVar;
        synchronized (a.class) {
            if (b == null) {
                b = new b(context);
            }
            bVar = b;
        }
        return bVar;
    }

    public void a() {
        a.a().a(this.a, this);
    }

    public void a(JSONObject jSONObject) {
        j.a((com.huawei.hianalytics.i.a) new d(this.a, "_instance_ex_tag", "$AppOnCrash", jSONObject.toString(), System.currentTimeMillis()));
    }

    public void b() {
        a.a().c();
    }
}
