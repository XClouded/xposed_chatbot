package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.at;

public class o {
    private static int a = -1;

    public static ap a(Context context) {
        try {
            return (context.getPackageManager().getServiceInfo(new ComponentName(HuaweiApiAvailability.SERVICES_PACKAGE, "com.huawei.hms.core.service.HMSCoreService"), 128) == null || !a()) ? ap.f : ap.HUAWEI;
        } catch (Exception unused) {
            return ap.f;
        }
    }

    private static boolean a() {
        try {
            String str = (String) at.a("android.os.SystemProperties", "get", "ro.build.hw_emui_api_level", "");
            return !TextUtils.isEmpty(str) && Integer.parseInt(str) >= 9;
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m73a(Context context) {
        Object a2 = at.a(at.a("com.google.android.gms.common.GoogleApiAvailability", "getInstance", new Object[0]), "isGooglePlayServicesAvailable", context);
        Object a3 = at.a("com.google.android.gms.common.ConnectionResult", "SUCCESS");
        if (a3 == null || !(a3 instanceof Integer)) {
            b.c("google service is not avaliable");
            a = 0;
            return false;
        }
        int intValue = Integer.class.cast(a3).intValue();
        if (a2 != null) {
            if (a2 instanceof Integer) {
                a = Integer.class.cast(a2).intValue() == intValue ? 1 : 0;
            } else {
                a = 0;
                b.c("google service is not avaliable");
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("is google service can be used");
        sb.append(a > 0);
        b.c(sb.toString());
        return a > 0;
    }

    public static boolean b(Context context) {
        boolean z = false;
        Object a2 = at.a("com.xiaomi.assemble.control.COSPushManager", "isSupportPush", context);
        if (a2 != null && (a2 instanceof Boolean)) {
            z = Boolean.class.cast(a2).booleanValue();
        }
        b.c("color os push  is avaliable ? :" + z);
        return z;
    }

    public static boolean c(Context context) {
        boolean z = false;
        Object a2 = at.a("com.xiaomi.assemble.control.FTOSPushManager", "isSupportPush", context);
        if (a2 != null && (a2 instanceof Boolean)) {
            z = Boolean.class.cast(a2).booleanValue();
        }
        b.c("fun touch os push  is avaliable ? :" + z);
        return z;
    }
}
