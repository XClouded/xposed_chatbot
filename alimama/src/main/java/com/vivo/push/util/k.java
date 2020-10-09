package com.vivo.push.util;

import android.os.Build;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.android.agoo.common.AgooConstants;

/* compiled from: Device */
public final class k {
    public static final boolean a = z.b("ro.vivo.product.overseas", "no").equals("yes");
    public static final String b;
    public static final boolean c = "RU".equals(b);
    public static final boolean d = "IN".equals(b);
    public static final boolean e = b("rom_1.0");
    public static final boolean f = b("rom_2.0");
    public static final boolean g = b("rom_2.5");
    public static final boolean h = b("rom_3.0");
    private static Method i;
    private static String j = null;
    private static String k = null;
    private static String l = "";
    private static String m = "";

    public static synchronized String a() {
        synchronized (k.class) {
            if (j == null && k == null) {
                try {
                    Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class});
                    i = declaredMethod;
                    declaredMethod.setAccessible(true);
                    j = (String) i.invoke((Object) null, new Object[]{"ro.vivo.rom", "@><@"});
                    k = (String) i.invoke((Object) null, new Object[]{"ro.vivo.rom.version", "@><@"});
                } catch (Exception unused) {
                    p.b("Device", "getRomCode error");
                }
            }
            p.d("Device", "sRomProperty1 : " + j + " ; sRomProperty2 : " + k);
            String a2 = a(j);
            if (!TextUtils.isEmpty(a2)) {
                return a2;
            }
            String a3 = a(k);
            if (!TextUtils.isEmpty(a3)) {
                return a3;
            }
            return null;
        }
    }

    private static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Matcher matcher = Pattern.compile("rom_([\\d]*).?([\\d]*)", 2).matcher(str);
        if (!matcher.find()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(matcher.group(1));
        sb.append(TextUtils.isEmpty(matcher.group(2)) ? "0" : matcher.group(2).substring(0, 1));
        return sb.toString();
    }

    private static boolean b(String str) {
        String b2 = z.b("ro.vivo.rom", "");
        String b3 = z.b("ro.vivo.rom.version", "");
        p.d("Device", "ro.vivo.rom = " + b2 + " ; ro.vivo.rom.version = " + b3);
        if (b2 == null || !b2.contains(str)) {
            return b3 != null && b3.contains(str);
        }
        return true;
    }

    public static boolean b() {
        if (TextUtils.isEmpty(Build.MANUFACTURER)) {
            p.d("Device", "Build.MANUFACTURER is null");
            return false;
        }
        p.d("Device", "Build.MANUFACTURER is " + Build.MANUFACTURER);
        if (Build.MANUFACTURER.toLowerCase().contains("bbk") || Build.MANUFACTURER.toLowerCase().startsWith(AgooConstants.MESSAGE_SYSTEM_SOURCE_VIVO)) {
            return true;
        }
        return false;
    }

    static {
        String str;
        if (Build.VERSION.SDK_INT >= 26) {
            str = z.b("ro.product.country.region", "N");
        } else {
            str = z.b("ro.product.customize.bbk", "N");
        }
        b = str;
    }
}
