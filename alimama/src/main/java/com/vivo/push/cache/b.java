package com.vivo.push.cache;

import android.content.Context;
import com.vivo.push.util.p;
import com.vivo.push.util.z;
import java.lang.reflect.Method;

/* compiled from: ConfigManagerFactory */
public final class b {
    private static final Object a = new Object();
    private static volatile b b;
    private e c;

    private b() {
    }

    public static b a() {
        if (b == null) {
            synchronized (a) {
                if (b == null) {
                    b = new b();
                }
            }
        }
        return b;
    }

    public final e a(Context context) {
        if (this.c != null) {
            return this.c;
        }
        try {
            String str = z.a(context) ? "com.vivo.push.cache.ServerConfigManagerImpl" : "com.vivo.push.cache.ClientConfigManagerImpl";
            Method method = Class.forName(str).getMethod("getInstance", new Class[]{Context.class});
            p.d("ConfigManagerFactory", "createConfig success is " + str);
            this.c = (e) method.invoke((Object) null, new Object[]{context});
            return this.c;
        } catch (Exception e) {
            e.printStackTrace();
            p.b("ConfigManagerFactory", "createConfig error", e);
            return null;
        }
    }
}
