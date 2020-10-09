package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.ay;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class m {
    private static m a;

    /* renamed from: a  reason: collision with other field name */
    private Context f914a;

    /* renamed from: a  reason: collision with other field name */
    private List<String> f915a = new ArrayList();
    private final List<String> b = new ArrayList();
    private final List<String> c = new ArrayList();

    private m(Context context) {
        this.f914a = context.getApplicationContext();
        if (this.f914a == null) {
            this.f914a = context;
        }
        SharedPreferences sharedPreferences = this.f914a.getSharedPreferences("mipush_app_info", 0);
        for (String str : sharedPreferences.getString("unregistered_pkg_names", "").split(",")) {
            if (TextUtils.isEmpty(str)) {
                this.f915a.add(str);
            }
        }
        for (String str2 : sharedPreferences.getString("disable_push_pkg_names", "").split(",")) {
            if (!TextUtils.isEmpty(str2)) {
                this.b.add(str2);
            }
        }
        for (String str3 : sharedPreferences.getString("disable_push_pkg_names_cache", "").split(",")) {
            if (!TextUtils.isEmpty(str3)) {
                this.c.add(str3);
            }
        }
    }

    public static m a(Context context) {
        if (a == null) {
            a = new m(context);
        }
        return a;
    }

    public void a(String str) {
        synchronized (this.f915a) {
            if (!this.f915a.contains(str)) {
                this.f915a.add(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("unregistered_pkg_names", ay.a((Collection<?>) this.f915a, ",")).commit();
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m608a(String str) {
        boolean contains;
        synchronized (this.f915a) {
            contains = this.f915a.contains(str);
        }
        return contains;
    }

    public void b(String str) {
        synchronized (this.b) {
            if (!this.b.contains(str)) {
                this.b.add(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("disable_push_pkg_names", ay.a((Collection<?>) this.b, ",")).commit();
            }
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m609b(String str) {
        boolean contains;
        synchronized (this.b) {
            contains = this.b.contains(str);
        }
        return contains;
    }

    public void c(String str) {
        synchronized (this.c) {
            if (!this.c.contains(str)) {
                this.c.add(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("disable_push_pkg_names_cache", ay.a((Collection<?>) this.c, ",")).commit();
            }
        }
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m610c(String str) {
        boolean contains;
        synchronized (this.c) {
            contains = this.c.contains(str);
        }
        return contains;
    }

    public void d(String str) {
        synchronized (this.f915a) {
            if (this.f915a.contains(str)) {
                this.f915a.remove(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("unregistered_pkg_names", ay.a((Collection<?>) this.f915a, ",")).commit();
            }
        }
    }

    public void e(String str) {
        synchronized (this.b) {
            if (this.b.contains(str)) {
                this.b.remove(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("disable_push_pkg_names", ay.a((Collection<?>) this.b, ",")).commit();
            }
        }
    }

    public void f(String str) {
        synchronized (this.c) {
            if (this.c.contains(str)) {
                this.c.remove(str);
                this.f914a.getSharedPreferences("mipush_app_info", 0).edit().putString("disable_push_pkg_names_cache", ay.a((Collection<?>) this.c, ",")).commit();
            }
        }
    }
}
