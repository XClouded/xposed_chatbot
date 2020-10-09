package com.xiaomi.push;

import com.xiaomi.channel.commonutils.logger.b;

public class s {
    public static String a(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{str, str2});
        } catch (Exception e) {
            b.a("SystemProperties.get: " + e);
            return str2;
        }
    }
}
