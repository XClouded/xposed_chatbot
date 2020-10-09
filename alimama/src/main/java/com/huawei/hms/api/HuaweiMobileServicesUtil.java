package com.huawei.hms.api;

import android.content.Context;
import android.os.Build;
import com.huawei.hms.c.a;
import com.huawei.hms.c.g;

public abstract class HuaweiMobileServicesUtil {
    public static int isHuaweiMobileServicesAvailable(Context context, int i) {
        a.a(context, "context must not be null.");
        if (Build.VERSION.SDK_INT < 16) {
            return 21;
        }
        g gVar = new g(context);
        g.a a = gVar.a(HuaweiApiAvailability.SERVICES_PACKAGE);
        if (g.a.NOT_INSTALLED.equals(a)) {
            return 1;
        }
        if (g.a.DISABLED.equals(a)) {
            return 3;
        }
        if (!HuaweiApiAvailability.SERVICES_SIGNATURE.equalsIgnoreCase(gVar.c(HuaweiApiAvailability.SERVICES_PACKAGE))) {
            return 9;
        }
        int b = gVar.b(HuaweiApiAvailability.SERVICES_PACKAGE);
        com.huawei.hms.support.log.a.b("HuaweiMobileServicesUtil", "connect versionCode:" + b);
        return b < i ? 2 : 0;
    }
}
