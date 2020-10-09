package com.huawei.hms.api;

import android.app.Activity;
import android.content.Context;
import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.c.a;
import com.huawei.hms.c.g;
import com.huawei.hms.c.h;
import com.huawei.hms.update.c.c;
import com.huawei.hms.update.e.v;

/* compiled from: HuaweiApiAvailabilityImpl */
final class e extends HuaweiApiAvailability {
    private static final e a = new e();

    public boolean isUserResolvableError(int i) {
        if (i == 6) {
            return true;
        }
        switch (i) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    private e() {
    }

    public static e a() {
        return a;
    }

    private static void a(Activity activity, String str, int i) {
        activity.startActivityForResult(BridgeActivity.getIntentStartBridgeActivity(activity, str), i);
    }

    public int isHuaweiMobileServicesAvailable(Context context, int i) {
        a.a(context, "context must not be null.");
        return HuaweiMobileServicesUtil.isHuaweiMobileServicesAvailable(context, i);
    }

    public int isHuaweiMobileNoticeAvailable(Context context) {
        a.a(context, "context must not be null.");
        return new g(context).b(HuaweiApiAvailability.SERVICES_PACKAGE) < 20600000 ? 2 : 0;
    }

    public void resolveError(Activity activity, int i, int i2) {
        a.a(activity, "activity must not be null.");
        com.huawei.hms.support.log.a.b("HuaweiApiAvailabilityImpl", "Enter resolveError, errorCode: " + i);
        if (i != 6) {
            switch (i) {
                case 1:
                case 2:
                    v vVar = new v();
                    vVar.a(true);
                    vVar.a(HuaweiApiAvailability.SERVICES_PACKAGE);
                    vVar.a(HuaweiApiAvailability.getServicesVersionCode());
                    vVar.b(HuaweiApiAvailability.APPID_HMS);
                    if (h.a() == null) {
                        h.a(activity.getApplicationContext());
                    }
                    vVar.c(h.d("hms_update_title"));
                    c.a(activity, i2, vVar);
                    return;
                default:
                    return;
            }
        } else {
            a(activity, BindingFailedResolution.class.getName(), i2);
        }
    }
}
