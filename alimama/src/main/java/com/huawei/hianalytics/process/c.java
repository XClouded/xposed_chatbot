package com.huawei.hianalytics.process;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.global.AutoCollectEventType;
import com.huawei.hianalytics.util.d;
import com.huawei.hianalytics.util.f;
import java.util.List;

public class c extends d implements HiAnalyticsInstanceEx {
    private Context b;

    c(Context context) {
        super("_instance_ex_tag");
        this.b = context;
    }

    private boolean a() {
        SharedPreferences a = d.a(this.b, "global_v2");
        boolean booleanValue = ((Boolean) d.b(a, "isFirstRun", false)).booleanValue();
        if (!booleanValue) {
            d.a(a, "isFirstRun", true);
        }
        return !booleanValue;
    }

    private boolean a(String str, String str2) {
        return !TextUtils.isEmpty(str2) && !str.equals(str2);
    }

    public void a(List<AutoCollectEventType> list) {
        b a;
        boolean z;
        b.b("HianalyticsSDK", "autoCollect() is executed.");
        if (list == null) {
            b.c("HianalyticsSDK", "autoCollect() eventTypes is null,End this method!");
            return;
        }
        b.b("HianalyticsSDK", "autoCollect() executed.");
        if (list.contains(AutoCollectEventType.APP_FIRST_RUN) && a()) {
            b.b("HianalyticsSDK", "autoCollect: APP_FIRST_RUN");
            b.a().b();
        }
        String g = com.huawei.hianalytics.a.b.g();
        String b2 = com.huawei.hianalytics.a.b.b();
        if (list.contains(AutoCollectEventType.APP_UPGRADE) && a(g, b2)) {
            b.b("HianalyticsSDK", "autoCollect: APP_UPGRADE");
            b.a().b(g, b2);
        }
        if (list.contains(AutoCollectEventType.APP_CRASH)) {
            b.b("HianalyticsSDK", "autoCollect: APP_CRUSH : true");
            a = b.a();
            z = true;
        } else {
            b.b("HianalyticsSDK", "autoCollect: APP_CRUSH : false");
            a = b.a();
            z = false;
        }
        a.a(z);
    }

    public void enableLogCollection(Context context, HiAnalyticsLogConfig hiAnalyticsLogConfig) {
        b.b("HianalyticsSDK", "enableLogCollection() is executed.");
        if (context != null) {
            a.b().a(context.getApplicationContext(), hiAnalyticsLogConfig);
        }
    }

    @Deprecated
    public void handleV1Cache() {
        b.b("HianalyticsSDK", "handleV1Cache() is executed.");
        b.a().a("_instance_ex_tag");
    }

    public void onStartApp(String str, String str2) {
        b.b("HianalyticsSDK", "onStartApp() is executed.");
        if (!f.a("startType", str, 4096) || !f.a("startCMD", str2, 4096)) {
            b.c("HianalyticsSDK", "onStartApp() Parameter error, please enter the correct parameter");
        } else {
            b.a().a(str, str2);
        }
    }

    public void refreshLogCollection(HiAnalyticsLogConfig hiAnalyticsLogConfig, boolean z) {
        b.b("HianalyticsSDK", "refreshLogCollection() is executed.");
        a.b().a(hiAnalyticsLogConfig, z);
    }
}
