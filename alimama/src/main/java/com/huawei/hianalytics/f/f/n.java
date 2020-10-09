package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.a.b;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.util.e;

public class n {
    private Context a;
    private j b = l.a();

    n(Context context) {
        this.a = context;
    }

    public void a() {
        String str;
        String str2;
        if (b.h()) {
            String a2 = e.a("ro.product.CustCVersion", "");
            com.huawei.hianalytics.g.b.b("HiAnalytics/event", "cust version: %s", a2);
            String a3 = this.b.a(a2);
            if (!TextUtils.isEmpty(a3)) {
                b.a(a3);
                SharedPreferences c = g.c(this.a, "global_v2");
                g.a(c, "upload_url", (Object) a3);
                g.a(c, "upload_url_time", (Object) Long.valueOf(System.currentTimeMillis()));
                b.a(false);
                return;
            }
            str2 = "HiAnalytics/event";
            str = "ServerAddrGetTask() No access to preloaded URL";
        } else {
            str2 = "HiAnalytics/event";
            str = "ServerAddrGetTask() Not need RetrieveUploadUrl,URL is empty, But the switch is closed !";
        }
        com.huawei.hianalytics.g.b.c(str2, str);
    }
}
