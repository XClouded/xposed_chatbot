package com.xiaomi.push;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class p {
    private static volatile p a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public Context f795a;

    /* renamed from: a  reason: collision with other field name */
    private Handler f796a = new Handler(Looper.getMainLooper());

    /* renamed from: a  reason: collision with other field name */
    private Map<String, Map<String, String>> f797a = new HashMap();

    private p(Context context) {
        this.f795a = context;
    }

    public static p a(Context context) {
        if (a == null) {
            synchronized (p.class) {
                if (a == null) {
                    a = new p(context);
                }
            }
        }
        return a;
    }

    private synchronized String a(String str, String str2) {
        if (this.f797a == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return "";
        }
        try {
            Map map = this.f797a.get(str);
            if (map == null) {
                return "";
            }
            return (String) map.get(str2);
        } catch (Throwable unused) {
            return "";
        }
    }

    private synchronized void b(String str, String str2, String str3) {
        if (this.f797a == null) {
            this.f797a = new HashMap();
        }
        Map map = this.f797a.get(str);
        if (map == null) {
            map = new HashMap();
        }
        map.put(str2, str3);
        this.f797a.put(str, map);
    }

    public synchronized String a(String str, String str2, String str3) {
        String a2 = a(str, str2);
        if (!TextUtils.isEmpty(a2)) {
            return a2;
        }
        return this.f795a.getSharedPreferences(str, 4).getString(str2, str3);
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m539a(String str, String str2, String str3) {
        b(str, str2, str3);
        this.f796a.post(new q(this, str, str2, str3));
    }
}
