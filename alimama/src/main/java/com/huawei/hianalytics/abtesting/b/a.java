package com.huawei.hianalytics.abtesting.b;

import android.content.Context;
import com.huawei.hianalytics.f.g.c;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.d;
import org.json.JSONException;

public class a implements Runnable {
    private Context a;

    public a(Context context) {
        this.a = context;
    }

    public void run() {
        b.b("ABTest/ReadCacheDataTask", "read cache task running");
        try {
            com.huawei.hianalytics.abtesting.a.b.a().a(c.a((String) d.b(d.a(this.a, "abtest"), "exp_data", ""), this.a));
        } catch (JSONException unused) {
            b.c("ABTest/ReadCacheDataTask", "experiment data error");
        }
        com.huawei.hianalytics.abtesting.a.b.a().a(true);
        com.huawei.hianalytics.abtesting.a.b.a().b(false);
    }
}
