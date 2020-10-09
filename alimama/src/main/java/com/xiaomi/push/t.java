package com.xiaomi.push;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.utils.tools.TimeCalculator;
import com.xiaomi.channel.commonutils.logger.b;

public class t {
    private static Context a;

    /* renamed from: a  reason: collision with other field name */
    private static String f940a;

    public static int a() {
        try {
            Class<?> cls = Class.forName("miui.os.Build");
            if (cls.getField("IS_STABLE_VERSION").getBoolean((Object) null)) {
                return 3;
            }
            return cls.getField("IS_DEVELOPMENT_VERSION").getBoolean((Object) null) ? 2 : 1;
        } catch (Exception unused) {
            return 0;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static Context m629a() {
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized String m630a() {
        String str;
        synchronized (t.class) {
            if (f940a != null) {
                String str2 = f940a;
                return str2;
            }
            String str3 = Build.VERSION.INCREMENTAL;
            if (a() <= 0) {
                str = b();
                if (TextUtils.isEmpty(str)) {
                    str = c();
                    if (TextUtils.isEmpty(str)) {
                        str = d();
                        if (TextUtils.isEmpty(str)) {
                            str3 = String.valueOf(s.a("ro.product.brand", TimeCalculator.PLATFORM_ANDROID) + "_" + str3);
                        }
                    }
                }
                f940a = str;
                return str;
            }
            str = str3;
            f940a = str;
            return str;
        }
    }

    public static String a(Context context) {
        if (l.b()) {
            return "";
        }
        String str = (String) at.a("com.xiaomi.xmsf.helper.MIIDAccountHelper", "getMIID", context);
        return TextUtils.isEmpty(str) ? "0" : str;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m631a(Context context) {
        a = context.getApplicationContext();
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m632a() {
        return TextUtils.equals((String) at.a("android.os.SystemProperties", "get", "sys.boot_completed"), "1");
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m633a(Context context) {
        try {
            return (context.getApplicationInfo().flags & 2) != 0;
        } catch (Exception e) {
            b.a((Throwable) e);
            return false;
        }
    }

    private static String b() {
        f940a = s.a("ro.build.version.emui", "");
        return f940a;
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m634b() {
        try {
            return Class.forName("miui.os.Build").getField("IS_GLOBAL_BUILD").getBoolean(false);
        } catch (ClassNotFoundException unused) {
            b.d("miui.os.Build ClassNotFound");
            return false;
        } catch (Exception e) {
            b.a((Throwable) e);
            return false;
        }
    }

    private static String c() {
        String a2 = s.a("ro.build.version.opporom", "");
        if (!TextUtils.isEmpty(a2) && !a2.startsWith("ColorOS_")) {
            f940a = "ColorOS_" + a2;
        }
        return f940a;
    }

    private static String d() {
        String a2 = s.a("ro.vivo.os.version", "");
        if (!TextUtils.isEmpty(a2) && !a2.startsWith("FuntouchOS_")) {
            f940a = "FuntouchOS_" + a2;
        }
        return f940a;
    }
}
