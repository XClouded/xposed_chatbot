package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.hl;
import com.xiaomi.push.service.ag;

final class c implements Runnable {
    final /* synthetic */ Context a;

    c(Context context) {
        this.a = context;
    }

    public void run() {
        if (b.a(this.a)) {
            String a2 = b.b(ag.a(this.a).a(hl.AggregationSdkMonitorDepth.a(), 4));
            if (!TextUtils.isEmpty(a2)) {
                MiTinyDataClient.upload(this.a, "monitor_upload", "call_stack", 1, a2);
                b.b(this.a);
            }
        }
    }
}
